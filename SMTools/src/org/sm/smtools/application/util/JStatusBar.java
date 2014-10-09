// -------------------------------
// Filename      : JStatusBar.java
// Author        : Sven Maerivoet
// Last modified : 03/10/2014
// Target        : Java VM (1.8)
// -------------------------------

/**
 * Copyright 2003-2014 Sven Maerivoet
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sm.smtools.application.util;

import com.sun.jna.*;
import com.sun.jna.win32.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.sm.smtools.exceptions.*;
import org.sm.smtools.math.*;
import org.sm.smtools.swing.util.*;
import org.sm.smtools.util.*;

/**
 * The <CODE>JStatusBar</CODE> class constructs a <CODE>JPanel</CODE> that provides an application with a status bar.
 * The status bar contains an indicator of the battery level as well as the current memory usage (they are automatically
 * updated every 30 seconds).
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 03/10/2014
 */
public final class JStatusBar extends JPanel
{
	/**
	 * The battery level and memory usage labels' update period in milliseconds.
	 */
	public static final int kUpdatePeriod = 30 * 1000;

	/**
	 * The height of the status bar (expressed in pixels).
	 */
	public static final int kHeight = 23;

	// the separation offset
	private static final int kSeparationOffset = 5;

	// the names of the files containing the battery icons
	private static final String kBatteryLevelHighImageFilename = "smtools-resources/images/statusbar-battery-level-high.png";
	private static final String kBatteryLevelMediumImageFilename = "smtools-resources/images/statusbar-battery-level-medium.png";
	private static final String kBatteryLevelLowImageFilename = "smtools-resources/images/statusbar-battery-level-low.png";
	private static final String kBatteryLevelCriticalImageFilename = "smtools-resources/images/statusbar-battery-level-critical.png";
	private static final String kBatteryLevelChargingImageFilename = "smtools-resources/images/statusbar-battery-level-charging.png";
	private static final String kBatteryLevelUnknownImageFilename = "smtools-resources/images/statusbar-battery-level-unknown.png";
	private static final String kBatteryLevelOnACPowerImageFilename = "smtools-resources/images/statusbar-battery-level-onacpower.png";

	// internal datastructures
	private JLabel fStatusTextLabel;
	private JPanel fCustomLabelPanel;
	private JLabel fBatteryUsageLabel;
	private ImageIcon fBatteryLevelHighImage;
	private ImageIcon fBatteryLevelMediumImage;
	private ImageIcon fBatteryLevelLowImage;
	private ImageIcon fBatteryLevelCriticalImage;
	private ImageIcon fBatteryLevelChargingImage;
	private ImageIcon fBatteryLevelUnknownImage;
	private ImageIcon fBatteryLevelOnACPowerImage;
	private JLabel fMemoryUsageLabel;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JStatusBar</CODE> object.
	 * <P>
	 * Note that the GUI resizable-icon is shown.
	 */
	public JStatusBar()
	{
		this(true,null);
	}	

	/**
	 * Constructs a <CODE>JStatusBar</CODE> object and sets up optional custom labels that are shown to the right hand side.
	 *
	 * @param showGUIResizable  indicates whether or not the resizable-icon should be shown
	 * @param customLabels      an <CODE>ArrayList</CODE> of custom labels, or <CODE>null</CODE> if none are provided
	 */
	public JStatusBar(boolean showGUIResizable, ArrayList<JLabel> customLabels)
	{
		setPreferredSize(new Dimension(getWidth(),kHeight));
		setLayout(new BorderLayout());
		add(Box.createRigidArea(new Dimension(kSeparationOffset,0)),BorderLayout.WEST);

			// add left panel
			fStatusTextLabel = new JLabel();
			fStatusTextLabel.setOpaque(false);
		add(fStatusTextLabel,BorderLayout.CENTER);

			// add right panel
			fCustomLabelPanel = new JPanel();
			fCustomLabelPanel.setLayout(new BoxLayout(fCustomLabelPanel,BoxLayout.X_AXIS));
			fCustomLabelPanel.setOpaque(false);

			// add other labels
			if (customLabels != null) {
				for (JLabel customLabel : customLabels) {
					addCustomLabel(customLabel);
				}
			}

			// preload the battery level images
			try {
				fBatteryLevelHighImage = new ImageIcon(JARResources.fSystemResources.getImage(kBatteryLevelHighImageFilename));
				fBatteryLevelMediumImage = new ImageIcon(JARResources.fSystemResources.getImage(kBatteryLevelMediumImageFilename));
				fBatteryLevelLowImage = new ImageIcon(JARResources.fSystemResources.getImage(kBatteryLevelLowImageFilename));
				fBatteryLevelCriticalImage = new ImageIcon(JARResources.fSystemResources.getImage(kBatteryLevelCriticalImageFilename));
				fBatteryLevelChargingImage = new ImageIcon(JARResources.fSystemResources.getImage(kBatteryLevelChargingImageFilename));
				fBatteryLevelUnknownImage = new ImageIcon(JARResources.fSystemResources.getImage(kBatteryLevelUnknownImageFilename));
				fBatteryLevelOnACPowerImage = new ImageIcon(JARResources.fSystemResources.getImage(kBatteryLevelOnACPowerImageFilename));
			}
			catch (FileDoesNotExistException exc) {
				// ignore
			}
			// add the battery level label
			fBatteryUsageLabel = new JLabel(" ",fBatteryLevelUnknownImage,SwingConstants.LEFT);
			addCustomLabel(fBatteryUsageLabel);

			// add the memory usage label
			fMemoryUsageLabel = new JLabel();
			addCustomLabel(fMemoryUsageLabel);

			// if necessary, add the resize icon
			if (showGUIResizable) {
				fCustomLabelPanel.add(Box.createRigidArea(new Dimension(kSeparationOffset,0)));
				JPanel resizeIconPanel = new JPanel();
				resizeIconPanel.setLayout(new BoxLayout(resizeIconPanel,BoxLayout.Y_AXIS));
				resizeIconPanel.add(Box.createVerticalGlue());
					JLabel resizeIconLabel = new JLabel(new JResizeIcon()); 
					resizeIconLabel.setOpaque(false);
				resizeIconPanel.add(resizeIconLabel);
				resizeIconPanel.setOpaque(false);
				fCustomLabelPanel.add(resizeIconPanel,BorderLayout.EAST);
			}
			
		add(fCustomLabelPanel,BorderLayout.EAST);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the status text.
	 *
	 * @param statusText  the new status text to show
	 */
	public void setStatusText(String statusText)
	{
		fStatusTextLabel.setText(statusText);
	}

	/**
	 * Clears the status text.
	 */
	public void clearStatusText()
	{
		setStatusText("");
	}

	/**
	 * Sets the status text's color.
	 *
	 * @param color  the status text's color
	 */
	public void setStatusTextColor(Color color)
	{
		fStatusTextLabel.setForeground(color);
	}

	/**
	 * Reverts the status text's color to black.
	 */
	public void clearStatusTextColor()
	{
		setStatusTextColor(Color.BLACK);
	}

	/**
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int y = 0;
		g.setColor(new Color(156,154,140));
		g.drawLine(0,y,getWidth(),y);
		++y;
		g.setColor(new Color(196,194,183));
		g.drawLine(0,y,getWidth(),y);
		++y;
		g.setColor(new Color(218,215,201));
		g.drawLine(0,y,getWidth(),y);
		++y;
		g.setColor(new Color(233,231,217));
		g.drawLine(0,y,getWidth(),y);

		y = getHeight() - 3;
		g.setColor(new Color(233,232,218));
		g.drawLine(0,y,getWidth(),y);
		++y;
		g.setColor(new Color(233,231,216));
		g.drawLine(0,y,getWidth(),y);
		y = getHeight() - 1;
		g.setColor(new Color(221,221,220));
		g.drawLine(0,y,getWidth(),y);
  }

	/**
	 * Updates the battery level and memory usage labels.
	 */
	public void updateBatteryLevelAndMemoryUsage()
	{	
		try {
			// obtain the battery status
			Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
			Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);

			// update the battery level label
			fBatteryUsageLabel.setIcon(fBatteryLevelUnknownImage);

			int batteryLifePercent = batteryStatus.getBatteryLifePercent();

			if (batteryStatus.isOnACPower() && !batteryStatus.isCharging()) {
				fBatteryUsageLabel.setIcon(fBatteryLevelOnACPowerImage);
			}
			else {
				if (batteryStatus.isCharging()) {
					fBatteryUsageLabel.setIcon(fBatteryLevelChargingImage);
				}
				else {
					if (batteryLifePercent >= 66) {
						fBatteryUsageLabel.setIcon(fBatteryLevelHighImage);
					}
					else if (batteryLifePercent >= 33) {
						fBatteryUsageLabel.setIcon(fBatteryLevelMediumImage);
					}
					else if (batteryLifePercent >= 10) {
						fBatteryUsageLabel.setIcon(fBatteryLevelLowImage);
					}
					else {
						fBatteryUsageLabel.setIcon(fBatteryLevelCriticalImage);
					}
				}
			}

			fBatteryUsageLabel.setText(" " + batteryLifePercent + "%");

			int batteryLifeTimeSeconds = batteryStatus.getBatteryLifeTime();
			if (batteryLifeTimeSeconds > 0) {
				int batteryLifeTimeHours = batteryLifeTimeSeconds / 3600;
				int batteryLifeTimeMinutes = (batteryLifeTimeSeconds - (batteryLifeTimeHours * 3600)) / 60;

				String batteryUsageTooltipText = "";
				if (batteryLifeTimeHours > 0) {
					if (batteryLifeTimeHours == 1) {
						batteryUsageTooltipText += I18NL10N.translate("tooltip.BatteryLifeRemainingHour");
					}
					else {
						batteryUsageTooltipText += I18NL10N.translate("tooltip.BatteryLifeRemainingHours",String.valueOf(batteryLifeTimeHours));
					}
					batteryUsageTooltipText += ", ";
				}
				if (batteryLifeTimeMinutes == 1) {
					batteryUsageTooltipText += I18NL10N.translate("tooltip.BatteryLifeRemainingMinute");
				}
				else {
					batteryUsageTooltipText += I18NL10N.translate("tooltip.BatteryLifeRemainingMinutes",String.valueOf(batteryLifeTimeMinutes));
				}
				
				fBatteryUsageLabel.setToolTipText(batteryUsageTooltipText);
			}
			else {
				fBatteryUsageLabel.setToolTipText(null);
			}
		}
		catch (UnsatisfiedLinkError exc) {
			// ignore
		}
		catch (NoClassDefFoundError exc) {
			// ignore
		}

		// update the memory usage label
		double percentageFree = ((double) MemoryStatistics.getFreeMemory() / (double) MemoryStatistics.getTotalMemory()) * 100;
		fMemoryUsageLabel.setText(
			I18NL10N.translate("text.MemoryFree") + ": " +
			StringTools.convertDoubleToString(MathTools.convertBToMiB(MemoryStatistics.getFreeMemory()),0) + " " +
			I18NL10N.translate("text.MiBAbbreviation") + " (" + StringTools.convertDoubleToString(percentageFree,0) + "%)");
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 * @param label  -
	 */
	private void addCustomLabel(JLabel label)
	{
		fCustomLabelPanel.add(createSeparator());
		label.setOpaque(false);
		fCustomLabelPanel.add(label);
	}

	/**
	 * @return -
	 */
	private JPanel createSeparator()
	{
		JPanel separatorPanel = new JPanel();
		separatorPanel.setLayout(new BoxLayout(separatorPanel,BoxLayout.X_AXIS));
		separatorPanel.add(Box.createRigidArea(new Dimension(kSeparationOffset,0)));

			JPanel separatorSubPanel = new JPanel();
			separatorSubPanel.setLayout(new BoxLayout(separatorSubPanel,BoxLayout.Y_AXIS));
			separatorSubPanel.add(Box.createRigidArea(new Dimension(0,kSeparationOffset)));
			separatorSubPanel.add(new JEtchedLine(JEtchedLine.EOrientation.kVertical));
			separatorSubPanel.add(Box.createRigidArea(new Dimension(0,kSeparationOffset)));
			separatorSubPanel.setOpaque(false);
		separatorPanel.add(separatorSubPanel);

		separatorPanel.add(Box.createRigidArea(new Dimension(kSeparationOffset,0)));
		separatorPanel.setOpaque(false);
		return separatorPanel;
	}

	/*****************
	 * INNER CLASSES *
	 *****************/

	/**
	 * @author  Sven Maerivoet
	 * @version 04/03/2013
	 */
	private final class JResizeIcon implements Icon
	{
		// the size of the icon
		private static final int kWidth = 12;
		private static final int kHeight = kWidth;

		// the colors of the icon
		private final Color kSquareColorLeft = new Color(184,180,163); 
		private final Color kSquareColorTopRight = new Color(184,180,161); 
    private final Color kSquareColorBottomRight = new Color(184,181,161);
    private final Color k3DEffectColor = Color.WHITE;

    /******************
     * PUBLIC METHODS *
     ******************/

    /**
		 */
		@Override
		public int getIconWidth()
		{
			return kWidth;
		}

		/**
		 */
		@Override
		public int getIconHeight()
		{
			return kHeight;
		}

		/**
		 */
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			int firstRow = 0;
			int firstColumn = 0;
			int rowDiff = 4;
			int columnDiff = 4;

			int secondRow = firstRow + rowDiff;
			int secondColumn = firstColumn + columnDiff;
			int thirdRow = secondRow + rowDiff;
			int thirdColumn = secondColumn + columnDiff;

			// first row
			draw3DSquare(g,firstColumn + 1,thirdRow + 1);

			// second row
			draw3DSquare(g,secondColumn + 1,secondRow + 1);
			draw3DSquare(g,secondColumn + 1,thirdRow + 1);

			// third row
			draw3DSquare(g,thirdColumn + 1,firstRow + 1);
			draw3DSquare(g,thirdColumn + 1,secondRow + 1);
			draw3DSquare(g,thirdColumn + 1,thirdRow + 1);

			// first row
			drawSquare(g,firstColumn,thirdRow);

			// second row
			drawSquare(g,secondColumn,secondRow);
			drawSquare(g,secondColumn,thirdRow);

			// third row
			drawSquare(g,thirdColumn,firstRow);
			drawSquare(g,thirdColumn,secondRow);
			drawSquare(g,thirdColumn,thirdRow);
		}

    /*******************
     * PRIVATE METHODS *
     *******************/

		/**
		 */
		private void drawSquare(Graphics g, int x, int y)
		{
			Color oldColor = g.getColor();
			g.setColor(kSquareColorLeft);
			g.drawLine(x,y,x,y + 1);
			g.setColor(kSquareColorTopRight);
			g.drawLine(x + 1,y,x + 1,y);
			g.setColor(kSquareColorBottomRight);
			g.drawLine(x + 1,y + 1,x + 1,y + 1);
			g.setColor(oldColor);
		}

		/**
		 */
		private void draw3DSquare(Graphics g, int x, int y)
		{
			Color oldColor = g.getColor();
			g.setColor(k3DEffectColor);
			g.fillRect(x,y,2,2);
			g.setColor(oldColor);
		}
	}

	/**
	 * @author  Sven Maerivoet
	 * @version 04/03/2013
	 */
	public interface Kernel32 extends StdCallLibrary
	{
		public Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("Kernel32",Kernel32.class);

		/**
		 * Refer to http://msdn2.microsoft.com/en-us/library/aa373232.aspx for more details.
		 */
		public class SYSTEM_POWER_STATUS extends Structure
		{
			// internal datastructures
			public byte ACLineStatus;
			public byte BatteryFlag;
			public byte BatteryLifePercent;
			public byte Reserved1;
			public int BatteryLifeTime;
			public int BatteryFullLifeTime;

			@Override
			protected java.util.List<String> getFieldOrder()
			{
				ArrayList<String> fields = new ArrayList<String>();
				fields.add("ACLineStatus");
				fields.add("BatteryFlag");
				fields.add("BatteryLifePercent");
				fields.add("Reserved1");
				fields.add("BatteryLifeTime");
				fields.add("BatteryFullLifeTime");
				return fields;
			}

			/**
			 * Returns the AC power status.
			 *
			 * @return the AC power status
			 */
			public boolean isOnACPower()
			{
				switch (ACLineStatus) {
					case 0: return false;
					case 1: return true;
					default: return false;
				}
			}

			/**
			 * Returns the charging status.
			 *
			 * @return the charging status
			 */
			public boolean isCharging()
			{
				return ((BatteryFlag & 8) == 8);
			}

			/**
			 * Returns the percentage of full battery charge remaining.
			 *
			 * @return the percentage of full battery charge remaining.
			 */
			public int getBatteryLifePercent()
			{
				return (BatteryLifePercent == (byte) 255) ? 0 : BatteryLifePercent;
			}

			/**
			 * Returns the number of seconds of battery life remaining.
			 *
			 * @return the number of seconds of battery life remaining
			 */
			public int getBatteryLifeTime()
			{
				return (BatteryLifeTime == -1) ? 0 : BatteryLifeTime;
			}
		}

		/**
		 * Fills the structure.
		 * 
		 * @param result  -
		 * @return        -
		 */
		public int GetSystemPowerStatus(SYSTEM_POWER_STATUS result);
	}
}
