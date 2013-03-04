// ---------------------------------------
// Filename      : JExtendedStatusBar.java
// Author        : Sven Maerivoet
// Last modified : 04/03/2013
// Target        : Java VM (1.6)
// ---------------------------------------

/**
 * Copyright 2003-2013 Sven Maerivoet
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

package smtools.application.util;

import com.sun.jna.*;
import com.sun.jna.win32.*;
import java.util.*;
import javax.swing.*;
import smtools.exceptions.*;
import smtools.math.*;
import smtools.miscellaneous.*;
import smtools.swing.util.*;

/**
 * The <CODE>JExtendedStatusBar</CODE> class constructs an extended status bar that also indicates the battery level and memory usage.
 *
 * @author  Sven Maerivoet
 * @version 04/03/2013
 */
public class JExtendedStatusBar extends JStatusBar
{
	/**
	 * The status bar's update period.
	 */
	public static final int kUpdatePeriod = 1 * 1000;

	// the names of the files containing the battery icons
	private static final String kBatteryLevelHighImageFilename = "smtools-resources/images/statusbar-battery-level-high.png";
	private static final String kBatteryLevelMediumImageFilename = "smtools-resources/images/statusbar-battery-level-medium.png";
	private static final String kBatteryLevelLowImageFilename = "smtools-resources/images/statusbar-battery-level-low.png";
	private static final String kBatteryLevelCriticalImageFilename = "smtools-resources/images/statusbar-battery-level-critical.png";
	private static final String kBatteryLevelChargingImageFilename = "smtools-resources/images/statusbar-battery-level-charging.png";
	private static final String kBatteryLevelUnknownImageFilename = "smtools-resources/images/statusbar-battery-level-unknown.png";
	private static final String kBatteryLevelOnACPowerImageFilename = "smtools-resources/images/statusbar-battery-level-onacpower.png";

	// internal datastructures
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
	 * Constructs a <JExtendedStatusBar</CODE> object.
	 * <P>
	 * Note that the GUI resizable-icon is shown.
	 */
	public JExtendedStatusBar()
	{
		this(true);
	}	

	/**
	 * Constructs a <CODE>JExtendedStatusBar</CODE> object.
	 *
	 * @param showGUIResizable indicates whether or not the resizable-icon should be shown
	 */
	public JExtendedStatusBar(boolean showGUIResizable)
	{
		super(showGUIResizable);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Updates the status bar's righthand side
	 */
	public void updateRighthandSide()
	{	
		// obtain the battery status
		Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
		Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);

		// update the battery level label
		fBatteryUsageLabel.setIcon(fBatteryLevelUnknownImage);

		if (batteryStatus.isOnACPower()) {
			fBatteryUsageLabel.setIcon(fBatteryLevelOnACPowerImage);
		}

		int batteryLifePercent = batteryStatus.getBatteryLifePercent();

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
		
		fBatteryUsageLabel.setText(" " + batteryLifePercent + "%");

		int batteryLifeTimeSeconds = batteryStatus.getBatteryLifeTime();
		if (batteryLifeTimeSeconds > 0) {
			int batteryLifeTimeHours = batteryLifeTimeSeconds / 3600;
			int batteryLifeTimeMinutes = (batteryLifeTimeSeconds - (batteryLifeTimeHours * 3600)) / 60;
			if (batteryLifeTimeHours > 0) {
				fBatteryUsageLabel.setToolTipText(I18NL10N.translate("tooltip.BatteryLifeRemaining",String.valueOf(batteryLifeTimeHours),String.valueOf(batteryLifeTimeMinutes)));
			}
			else {
				fBatteryUsageLabel.setToolTipText(I18NL10N.translate("tooltip.BatteryLifeRemainingShort",String.valueOf(batteryLifeTimeMinutes)));
			}
		}
		else {
			fBatteryUsageLabel.setToolTipText(null);
		}

		// update the memory usage label
		double percentageFree = ((double) JMemoryStatistics.getFreeMemory() / (double) JMemoryStatistics.getTotalMemory()) * 100;
		fMemoryUsageLabel.setText(
			I18NL10N.translate("text.MemoryFree") + ": " +
			StringTools.convertDoubleToString(MathTools.convertBToMiB(JMemoryStatistics.getFreeMemory()),0) + " " +
			I18NL10N.translate("text.MiBAbbreviation") + " (" + StringTools.convertDoubleToString(percentageFree,0) + "%)");
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * Adds battery level and memory usage labels to the righthand side of the status bar.
	 */
	protected void addRighthandSideLabels()
	{
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
		addRighthandSideLabel(fBatteryUsageLabel);

		// add the memory usage label
		fMemoryUsageLabel = new JLabel();
		addRighthandSideLabel(fMemoryUsageLabel);
	}

	/*****************
	 * INNER CLASSES *
	 *****************/

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
			protected List<String> getFieldOrder()
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
		 */
		public int GetSystemPowerStatus(SYSTEM_POWER_STATUS result);
	}
}
