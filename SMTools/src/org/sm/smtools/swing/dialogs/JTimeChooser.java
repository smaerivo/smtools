// ---------------------------------
// Filename      : JTimeChooser.java
// Author        : Sven Maerivoet
// Last modified : 06/08/2019
// Target        : Java VM (1.8)
// ---------------------------------

/**
 * Copyright 2003-2019 Sven Maerivoet
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

package org.sm.smtools.swing.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.math.*;
import org.sm.smtools.swing.util.*;
import org.sm.smtools.util.*;

/**
 * The <CODE>JTimeChooser</CODE> class provides a dialog box for choosing a time.
 * <P>
 * Note that a valid {@link I18NL10N} database must be available!
 * <P>
 * The dialog box is <I>modal</I>, <I>non-resizable</I> and contains <I>"Ok"</I> and <I>"Cancel" buttons</I>
 * to close it. Here's an example of a time chooser (Microsoft Windows L&amp;F):
 * <P>
 * <IMG src="doc-files/time-chooser-windows.png" alt="">
 * <P>
 * As can be seen, the time chooser's GUI consists of two main areas:
 * <UL>
 *   <LI>An <B>analogue clock</B> showing the currently selected hour, minute and second. An
 *       <B>optional digital clock</B> can also be shown in the clock's panel. Both clocks are
 *       updated in real-time.</LI>
 *   <LI>Four <B><CODE>JSpinner</CODE></B>s for selecting the hour, minute, second and millisecond.</LI>
 * </UL>
 * <P>
 * There's also the <B>exclamation checkbox</B> that allows tracking (and selecting) the current time.
 * <P>
 * Depending on the desired functionality, the time chooser can be allowed to select only:
 * <UL>
 *   <LI>the hour and minute,</LI>
 *   <LI>the hour, minute and second,</LI>
 *   <LI>or the hour, minute, second and millisecond.</LI>
 * </UL>
 * <P>
 * When the clock is showing HMS or HMS.ms, the user can also select the seconds by clicking on the clock's face.
 * <P>
 * When the user closes the time chooser's dialog box, its state should be queried as follows:
 * <P>
 * <CODE>
 *   if (!myTimeChooser.isCancelled()) {<BR>
 *     TimeStamp timeStamp = myTimeChooser.getSelectedTime();<BR>
 *     // rest of code<BR>
 *   }<BR>
 * </CODE>
 * <P>
 * Note that the system resources must be initialised (see {@link JARResources#fSystemResources}).
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 06/08/2019
 * @see     TimeStamp
 */
public final class JTimeChooser extends JDefaultDialog implements ChangeListener
{
	/**
	 * Useful constants to specify a time chooser for selecting only the hour and minute, and/or second and/or millisecond.
	 */
	public static enum EType {kHourMinute, kHourMinuteSecond, kHourMinuteSecondMillisecond};

	/**
	 * Useful constants to specify a time chooser that shows a clock which is updated continuously or in second time steps.
	 */
	public static enum EUpdatingMethod {kContinuous, kDiscrete};

	/**
	 * Useful constants to specify whether or not a digital indication of the current time should be shown.
	 */
	public static enum EDigitalClock {kShown, kNotShown};

	// switch to control whether or not setting the seconds can be done via the clock's main panel
	private static final boolean kEnableMouseListeners = true;

	// useful constant to specify the sound of a single clock tick.
	private static final String kSoundFilenameClockTick = "smtools-resources/sounds/clock-tick.mp3";

	// an action-command
	private static final String kCurrentTime = "current-time";

	// internal datastructures
	private String fWindowTitle;
	private TimeStamp fTimeStamp;
	private EType fType;
	private EUpdatingMethod fUpdatingMethod;
	private EDigitalClock fDigitalClock;
	private ClockPanel fClockPanel;
	private JSpinner fHourChooser;
	private JSpinner fMinuteChooser;
	private JSpinner fSecondChooser;
	private JSpinner fMillisecondChooser;
	private boolean fUpdateInputFields;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JTimeChooser</CODE> object.
	 * <P>
	 * Depending on the desired functionality, the time chooser can be allowed to select only:
	 * <UL>
	 *   <LI>the hour and minute,</LI>
	 *   <LI>the hour, minute and second,</LI>
	 *   <LI>or the hour, minute, second and millisecond.</LI>
	 * </UL>
	 *
	 * @param owner           the frame in which this dialog is to be displayed
	 * @param title           the dialog's window title
	 * @param dialogType      the type of dialog
	 * @param type            the <CODE>EType</CODE> type of time chooser
	 * @param updatingMethod  an <CODE>EUpdatingMethod</CODE> flag indicating whether or not the clock's hands should
	 *                        be updated continuously or discretely
	 * @param digitalClock    an <CODE>EDigitalClock</CODE> flag indicating whether or not a digital indication of
	 *                        the current time should be shown
	 * @see                   JTimeChooser.EType
	 * @see                   JTimeChooser.EUpdatingMethod
	 * @see                   JTimeChooser.EDigitalClock
	 */
	public JTimeChooser(JFrame owner, String title, JDefaultDialog.EType dialogType, EType type, EUpdatingMethod updatingMethod, EDigitalClock digitalClock)
	{
		super(owner,
			JDefaultDialog.EModality.kModal,
			JDefaultDialog.ESize.kFixedSize,
			dialogType,
			new Object[] {title, type, updatingMethod, digitalClock});
		fUpdateInputFields = false;
		JARResources.checkSystemInitialisation();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	// the change-listener
	/**
	 */
	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == fHourChooser) {
			fTimeStamp.set(((SpinnerNumberModel) fHourChooser.getModel()).getNumber().intValue(),fTimeStamp.getMinute(),fTimeStamp.getSecond(),fTimeStamp.getMillisecond());
		}
		else if (e.getSource() == fMinuteChooser) {
			fTimeStamp.set(fTimeStamp.getHour(),((SpinnerNumberModel) fMinuteChooser.getModel()).getNumber().intValue(),fTimeStamp.getSecond(),fTimeStamp.getMillisecond());
		}
		else if (e.getSource() == fSecondChooser) {
			fTimeStamp.set(fTimeStamp.getHour(),fTimeStamp.getMinute(),((SpinnerNumberModel) fSecondChooser.getModel()).getNumber().intValue(),fTimeStamp.getMillisecond());
		}
		else if (e.getSource() == fMillisecondChooser) {
			fTimeStamp.set(fTimeStamp.getHour(),fTimeStamp.getMinute(),fTimeStamp.getSecond(),((SpinnerNumberModel) fMillisecondChooser.getModel()).getNumber().intValue());
		}

		fClockPanel.repaint();
	}
	
	// the action-listener
	/**
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);

		String command = e.getActionCommand();

		if (command.equalsIgnoreCase(kCurrentTime)) {
			fUpdateInputFields = !fUpdateInputFields;
			if (fUpdateInputFields) {
				updateInputFields(new TimeStamp());
			}

			fClockPanel.repaint();
		}
	}

	/**
	 * Returns the currently selected time.
	 *
	 * @return the currently selected time
	 */
	public TimeStamp getSelectedTime()
	{
		return fTimeStamp;
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 */
	@Override
	protected void initialiseClass(Object[] parameters)
	{
		fWindowTitle = (String) parameters[0];
		fType = (EType) parameters[1];
		fUpdatingMethod = (EUpdatingMethod) parameters[2];
		fDigitalClock = (EDigitalClock) parameters[3];
		fTimeStamp = new TimeStamp();
	}

	/**
	 */
	@Override
	protected final String setupWindowTitle()
	{
		return fWindowTitle;
	}

	/**
	 */
	@Override
	protected void setupMainPanel(JPanel mainPanel)
	{
		JPanel panel = null;
		JCheckBox checkBox = null;

		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(5,5,5,5));

		// add the clock panel
		fClockPanel = new ClockPanel(fUpdatingMethod,fDigitalClock);
		fClockPanel.setToolTipText(I18NL10N.kINSTANCE.translate("tooltip.ClockPanel"));
		mainPanel.add(fClockPanel);

		mainPanel.add(new JEtchedLine(JEtchedLine.EOrientation.kHorizontal));

		// add the time controls
		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		checkBox = new JCheckBox("!",false);
		checkBox.setBorder(new EmptyBorder(0,0,0,0));
		checkBox.setFocusPainted(false);
		checkBox.setToolTipText(I18NL10N.kINSTANCE.translate("tooltip.GetCurrentTime"));
		checkBox.setActionCommand(kCurrentTime);
		checkBox.addActionListener(this);
		panel.add(checkBox);

		SpinnerNumberModel hourSpinnerNumberModel = new SpinnerNumberModel(fTimeStamp.getHour(),0,23,1);
		fHourChooser = new JSpinner(hourSpinnerNumberModel);	
		// format number as an integer
		JSpinner.NumberEditor hourSpinnerNumberEditor = new JSpinner.NumberEditor(fHourChooser,"0");
		fHourChooser.setEditor(hourSpinnerNumberEditor);
		fHourChooser.addChangeListener(this);
		fHourChooser.setToolTipText(I18NL10N.kINSTANCE.translate("tooltip.SetHour"));
		panel.add(fHourChooser);
		panel.add(new JLabel("<HTML><B>:</B></HTML>"));

		SpinnerNumberModel minuteSpinnerNumberModel = new SpinnerNumberModel(fTimeStamp.getMinute(),0,59,1);
		fMinuteChooser = new JSpinner(minuteSpinnerNumberModel);	
		// format number as an integer
		JSpinner.NumberEditor minuteSpinnerNumberEditor = new JSpinner.NumberEditor(fMinuteChooser,"0");
		fMinuteChooser.setEditor(minuteSpinnerNumberEditor);
		fMinuteChooser.addChangeListener(this);
		fMinuteChooser.setToolTipText(I18NL10N.kINSTANCE.translate("tooltip.SetMinute"));
		panel.add(fMinuteChooser);
		if (fType != EType.kHourMinute) {
			panel.add(new JLabel("<HTML><B>:</B></HTML>"));
		}

		SpinnerNumberModel secondSpinnerNumberModel = new SpinnerNumberModel(fTimeStamp.getSecond(),0,59,1);
		fSecondChooser = new JSpinner(secondSpinnerNumberModel);	
		// format number as an integer
		JSpinner.NumberEditor secondSpinnerNumberEditor = new JSpinner.NumberEditor(fSecondChooser,"0");
		fSecondChooser.setEditor(secondSpinnerNumberEditor);
		fSecondChooser.addChangeListener(this);
		fSecondChooser.setToolTipText(I18NL10N.kINSTANCE.translate("tooltip.SetSecond"));
		if (fType == EType.kHourMinute) {
			fSecondChooser.setEnabled(false);
			fSecondChooser.setVisible(false);
		}
		panel.add(fSecondChooser);
		if (fType == EType.kHourMinuteSecondMillisecond) {
			panel.add(new JLabel("<HTML><B>.</B></HTML>"));
		}

		SpinnerNumberModel millisecondSpinnerNumberModel = new SpinnerNumberModel(fTimeStamp.getMillisecond(),0,999,1);
		fMillisecondChooser = new JSpinner(millisecondSpinnerNumberModel);	
		// format number as an integer
		JSpinner.NumberEditor millisecondSpinnerNumberEditor = new JSpinner.NumberEditor(fMillisecondChooser,"0");
		fMillisecondChooser.setEditor(millisecondSpinnerNumberEditor);
		fMillisecondChooser.addChangeListener(this);
		fMillisecondChooser.setToolTipText(I18NL10N.kINSTANCE.translate("tooltip.SetMillisecond"));
		if ((fType == EType.kHourMinute) || (fType == EType.kHourMinuteSecond)) {
			fMillisecondChooser.setEnabled(false);
			fMillisecondChooser.setVisible(false);
		}
		panel.add(fMillisecondChooser);

		mainPanel.add(panel);
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 * @param timeStamp  -
	 */
	private void updateInputFields(TimeStamp timeStamp)
	{
		fHourChooser.setValue(timeStamp.getHour());
		fMinuteChooser.setValue(timeStamp.getMinute());
		fSecondChooser.setValue(timeStamp.getSecond());
		fMillisecondChooser.setValue(timeStamp.getMillisecond());
	}

	/*****************
	 * INNER CLASSES *
	 *****************/

	/**
	 * @author Sven Maerivoet
	 */
	private final class ClockPanel extends JPanel implements MouseListener, MouseMotionListener
	{
		// the clock's update period
		private static final int kContinuousUpdatePeriod = 50; // 50 ms
		private static final int kDiscreteUpdatePeriod = 500; // 0.5 sec
		private static final int kInputFieldUpdatePeriodForHM = 30 * 1000; // 30 sec
		private static final int kInputFieldUpdatePeriodForHMS = 250; // 0.25 sec
		private static final int kInputFieldUpdatePeriodForHMSMs = 50; // 50 ms
		private static final int kClockTickUpdatePeriod = 1000;

		// the dialog's size
		private static final int kClockWidth = 250;
		private static final int kClockHeight = kClockWidth;

		// internal datastructures
		private EUpdatingMethod fUpdatingMethod;
		private EDigitalClock fDigitalClock;
		private TimeStamp fCurrentTimeStamp;
		private int fCenterX;
		private int fCenterY;
		private int fSecondsRadiusZone;

		/****************
		 * CONSTRUCTORS *
		 ****************/

		/**
		 */
		public ClockPanel(EUpdatingMethod updatingMethod, EDigitalClock digitalClock)
		{
			fUpdatingMethod = updatingMethod;
			fDigitalClock = digitalClock;

			fCurrentTimeStamp = new TimeStamp();
			setPreferredSize(new Dimension(kClockWidth,kClockHeight));

			if (kEnableMouseListeners &&
					((fType == EType.kHourMinuteSecond) || (fType == EType.kHourMinuteSecondMillisecond))) {
				addMouseListener(this);
				addMouseMotionListener(this);
			}

			// create a Swing timer to continuously update the clock's internal time
			Action updateInternalTimeAction = new AbstractAction()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fCurrentTimeStamp.setToNow();
				}
			};

			// set the update period
			new javax.swing.Timer(kContinuousUpdatePeriod,updateInternalTimeAction).start();

			// create a Swing timer to periodically update the clock's display
			Action updateClockPanelAction = new AbstractAction()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					repaint();
				}
			};

			// set the update period
			if (fUpdatingMethod == EUpdatingMethod.kContinuous) {
				new javax.swing.Timer(kContinuousUpdatePeriod,updateClockPanelAction).start();
			}
			else if (fUpdatingMethod == EUpdatingMethod.kDiscrete) {
				new javax.swing.Timer(kDiscreteUpdatePeriod,updateClockPanelAction).start();
			}


			Action updateInputFieldsAction = new AbstractAction()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (isShown() && fUpdateInputFields) {
						updateInputFields(new TimeStamp());
					}
				}
			};

			// set the update period
			if (fType == EType.kHourMinute) {
				new javax.swing.Timer(kInputFieldUpdatePeriodForHM,updateInputFieldsAction).start();
			}
			else if (fType == EType.kHourMinuteSecond) {
				new javax.swing.Timer(kInputFieldUpdatePeriodForHMS,updateInputFieldsAction).start();
			}
			else if (fType == EType.kHourMinuteSecondMillisecond) {
				new javax.swing.Timer(kInputFieldUpdatePeriodForHMSMs,updateInputFieldsAction).start();
			}

			Action clockTickingAction = new AbstractAction()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (isShown()) {
						MP3Player.playSystemSound(kSoundFilenameClockTick);
					}
				}
			};

			// set the update period
			new javax.swing.Timer(kClockTickUpdatePeriod,clockTickingAction).start();
		}

		/******************
		 * PUBLIC METHODS *
		 ******************/

		/**
		 */
		@Override
		public void paint(Graphics gr)
		{
			int width = getWidth();
			int height = getHeight();

			gr.setColor(getBackground());
			gr.fillRect(0,0,width,height);

			// show the tickmarks
			fCenterX = width / 2;
			fCenterY = height / 2;

			if (fDigitalClock == EDigitalClock.kShown) {
				String digitalTime = fCurrentTimeStamp.getHMSString();
				gr.setColor(Color.black);
				gr.drawString(digitalTime,fCenterX - (gr.getFontMetrics().stringWidth(digitalTime) / 2),fCenterY + (height / 5));
			}

			int tickRadius;
			if (width < height) {
				tickRadius = (4 * (width / 2)) / 5;
			}
			else {
				tickRadius = (4 * (height / 2)) / 5;
			}

			int hoursLabel = 3;
			if (fCurrentTimeStamp.getHour() > 11) {
				hoursLabel = 15;
			}
			int hoursLabelOffset = 16;
			for (int i = 0; i < 60; ++i) {

				double angle = (double) i * ((2.0 * Math.PI) / 60.0);
				int x = fCenterX + ((int) Math.round(tickRadius * Math.cos(angle)));
				int y = fCenterY + ((int) Math.round(tickRadius * Math.sin(angle)));

				if ((i % 5) == 0) {

					// plot an hourly tickmark
					Color green = Color.green.darker();

					putPixel(gr,x - 1,y - 1,Color.cyan);
					putPixel(gr,x,y - 1,Color.cyan);
					putPixel(gr,x + 1,y - 1,Color.black);

					putPixel(gr,x - 1,y,Color.cyan);
					putPixel(gr,x,y,green);
					putPixel(gr,x + 1,y,Color.black);

					putPixel(gr,x - 1,y + 1,Color.cyan);
					putPixel(gr,x,y + 1,Color.black);
					putPixel(gr,x + 1,y + 1,Color.black);

					// show the hour's label
					x = fCenterX + ((int) Math.round((tickRadius + hoursLabelOffset) * Math.cos(angle)));
					y = fCenterY + ((int) Math.round((tickRadius + hoursLabelOffset) * Math.sin(angle)));
					y += 5;
					if (hoursLabel >= 10) {
						x -= 5;
					}

					gr.setColor(Color.black);
					if (hoursLabel > 12) {
						gr.drawString(String.valueOf(hoursLabel - 12),x,y);
					}
					else {
						gr.drawString(String.valueOf(hoursLabel),x,y);
					}

					++hoursLabel;
					if ((hoursLabel > 24) && (fCurrentTimeStamp.getHour() > 11)) {
						hoursLabel = 13;
					}
					else if ((hoursLabel > 12) && (fCurrentTimeStamp.getHour() < 12)) {
						hoursLabel = 1;
					}
				}
				else {
					// plot a minute/second tickmark
					putPixel(gr,x - 1,y - 1,Color.gray);
					putPixel(gr,x,y - 1,Color.gray);

					putPixel(gr,x - 1,y,Color.gray);
					putPixel(gr,x,y,Color.lightGray);
					putPixel(gr,x + 1,y,Color.white);

					putPixel(gr,x,y + 1,Color.white);
					putPixel(gr,x + 1,y + 1,Color.white);
				}
			}

			int hoursHandRadius = (3 * tickRadius) / 5;
			int minutesHandRadius = (4 * tickRadius) / 5;
			int secondsHandRadius = (7 * tickRadius) / 8;
			fSecondsRadiusZone = (tickRadius * 5) / 4;

			// show the clock's hour hand
			int hours = fCurrentTimeStamp.getHour();
			if (hours >= 12) {
				hours -= 12;
			}
			double hoursAngle = (double) hours * ((2.0 * Math.PI) / 12.0) - (Math.PI / 2.0);
			hoursAngle += (((double) fCurrentTimeStamp.getMinute() / 60.0) * ((2.0 * Math.PI) / 12.0));
			int hoursX = fCenterX + ((int) Math.round(hoursHandRadius * Math.cos(hoursAngle)));
			int hoursY = fCenterY + ((int) Math.round(hoursHandRadius * Math.sin(hoursAngle)));
			gr.setColor(Color.black);
			gr.drawLine(fCenterX,fCenterY,hoursX,hoursY);
			gr.drawLine(fCenterX - 1,fCenterY,hoursX,hoursY);
			gr.drawLine(fCenterX + 1,fCenterY,hoursX,hoursY);
			gr.drawLine(fCenterX,fCenterY - 1,hoursX,hoursY);
			gr.drawLine(fCenterX,fCenterY + 1,hoursX,hoursY);

			// show the clock's minute hand
			double minutesAngle = (double) fCurrentTimeStamp.getMinute() * ((2.0 * Math.PI) / 60.0) - (Math.PI / 2.0);
			if (fUpdatingMethod == EUpdatingMethod.kContinuous) {
				minutesAngle += (((double) fCurrentTimeStamp.getSecond() / 60.0) * ((2.0 * Math.PI) / 60.0));
			}
			int minutesX = fCenterX + ((int) Math.round(minutesHandRadius * Math.cos(minutesAngle)));
			int minutesY = fCenterY + ((int) Math.round(minutesHandRadius * Math.sin(minutesAngle)));
			gr.setColor(Color.black);
			gr.drawLine(fCenterX,fCenterY,minutesX,minutesY);
			gr.drawLine(fCenterX - 1,fCenterY,minutesX,minutesY);
			gr.drawLine(fCenterX + 1,fCenterY,minutesX,minutesY);
			gr.drawLine(fCenterX,fCenterY - 1,minutesX,minutesY);
			gr.drawLine(fCenterX,fCenterY + 1,minutesX,minutesY);

			/*
			 if (ftimeChooserType != kHourMinuteType) {
			 ...
			 }
			 */

			// show the clock's second hand
			double secondsAngle = (double) fCurrentTimeStamp.getSecond() * ((2.0 * Math.PI) / 60.0) - (Math.PI / 2.0);
			if (fUpdatingMethod == EUpdatingMethod.kContinuous) {
				secondsAngle += (((double) fCurrentTimeStamp.getMillisecond() / 1000.0) * ((2.0 * Math.PI) / 60.0));
			}
			int secondsX = fCenterX + ((int) Math.round(secondsHandRadius * Math.cos(secondsAngle)));
			int secondsY = fCenterY + ((int) Math.round(secondsHandRadius * Math.sin(secondsAngle)));
			gr.setColor(Color.red);
			gr.drawLine(fCenterX,fCenterY,secondsX,secondsY);

			// show a small circle in the middle
			gr.setColor(Color.blue);
			gr.fillOval(fCenterX - 3,fCenterY - 3,7,7);
		}

		/**
		 */
		public void putPixel(Graphics gr, int x, int y, Color color)
		{
			gr.setColor(color);
			gr.drawLine(x,y,x,y);
		}

		// the mouse-listener
		/**
		 */
		@Override
		public final void mouseClicked(MouseEvent e)
		{
			int x = e.getX() - fCenterX;
			int y = e.getY() - fCenterY;
			int radius = (int) Math.round(Math.sqrt((x * x) + (y * y)));

			if ((radius > 20) && (radius <= fSecondsRadiusZone)) {
				int seconds = ((int) Math.floor(60.0 * (MathTools.atan(x,y) / (2.0 * Math.PI)))) + 15;
				if (seconds >= 60) {
					seconds = seconds - 60;
				}
				seconds = MathTools.clip(seconds,0,59);
				fTimeStamp.set(fTimeStamp.getHour(),fTimeStamp.getMinute(),seconds,fTimeStamp.getMillisecond());
				updateInputFields(fTimeStamp);
				repaint();
			}
		}

		/**
		 */
		@Override
		public final void mouseEntered(MouseEvent e)
		{
		}

		/**
		 */
		@Override
		public final void mouseExited(MouseEvent e)
		{
		}

		/**
		 */
		@Override
		public final void mousePressed(MouseEvent e)
		{
		}

		/**
		 */
		@Override
		public final void mouseReleased(MouseEvent e)
		{
		}

		// the mouse-motion-listener
		/**
		 */
		@Override
		public final void mouseDragged(MouseEvent e)
		{
			mouseClicked(e);
		}

		/**
		 */
		@Override
		public final void mouseMoved(MouseEvent e)
		{
		}
	}
}
