// ---------------------------------
// Filename      : JTimeChooser.java
// Author        : Sven Maerivoet
// Last modified : 04/05/2014
// Target        : Java VM (1.8)
// ---------------------------------

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

package org.sm.smtools.swing.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.exceptions.*;
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
 * There's also the <B>exclamation button</B> <IMG src="doc-files/calendar-exclamation.png" alt="">
 * that jumps to the current time (and selects it).
 * <P>
 * Depending on the desired functionality, the time chooser can be allowed to select only:
 * <UL>
 *   <LI>the hour and minute,</LI>
 *   <LI>the hour, minute and second,</LI>
 *   <LI>or the hour, minute, second and millisecond.</LI>
 * </UL>
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
 * The clock's digits can be set to either always show the numbers 1 to 12, or to show the
 * numbers 1 to 12 and 13 to 24 in the AM and PM time periods respectively.
 * <P>
 * <B><U>Important remark</U></B><BR>
 * This GUI-component supports <B>caching</B> in the <I>SMTools</I> framework. Using the
 * {@link JTimeChooser#JTimeChooser(JFrame,String,JDefaultDialog.EType,TimeStamp,EType,EClockDigits,EUpdating,EDigitalClock)} constructor, dialog
 * activation can be postponed until an explicit call to {@link JDefaultDialog#activate}
 * is made.
 * <P>
 * Note that the system resources must be initialised (see {@link JARResources#fSystemResources}).
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 04/05/2014
 * @see     TimeStamp
 */
public final class JTimeChooser extends JDefaultDialog implements ChangeListener
{
	/**
	 * Useful constants to specify a time chooser for selecting only the hour and minute, and/or second and/or millisecond.
	 */
	public static enum EType {kHourMinute, kHourMinuteSecond, kHourMinuteSecondMillisecond};

	/**
	 * Useful constants to specify that the clock's digits should always be numbered from 1 to 12,
	 * or from 1 to 12 in the AM, and from 13 to 24 in the PM
	 */
	public static enum EClockDigits {kUse12Hour, kUse24Hour};

	/**
	 * Useful constants to specify a time chooser that shows a clock which is updated continuously or in second time steps.
	 */
	public static enum EUpdating {kContinuous, kDiscrete};

	/**
	 * Useful constants to specify whether or not a digital indication of the current time should be shown.
	 */
	public static enum EDigitalClock {kShown, kNotShown};

	// switch to control whether or not setting the seconds can be done via the clock's main panel
	private static final boolean kEnableMouseListeners = true;

	// the locations of the control icons
	private static final String kExclamationIconFilename = "smtools-resources/images/exclamation.gif";
	private static final String kExclamationRolloverIconFilename = "smtools-resources/images/exclamation-hl.gif";

	// an action-command
	private static final String kCurrentTime = "current-time";

	// internal datastructures
	private String fWindowTitle;
	private TimeStamp fTimeStamp;
	private EType fType;
	private EClockDigits fClockDigits;
	private EUpdating fUpdating;
	private EDigitalClock fDigitalClock;
	private ClockPanel fClockPanel;
	private JSpinner fHourChooser;
	private JSpinner fMinuteChooser;
	private JSpinner fSecondChooser;
	private JSpinner fMillisecondChooser;
	
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JTimeChooser</CODE> object and shows it on the screen.
	 * <P>
	 * Depending on the desired functionality, the time chooser can be allowed to select only:
	 * <UL>
	 *   <LI>the hour and minute,</LI>
	 *   <LI>the hour, minute and second,</LI>
	 *   <LI>or the hour, minute, second and millisecond.</LI>
	 * </UL>
	 *
	 * @param owner         the frame in which this dialog is to be displayed
	 * @param title         the dialog's window title
	 * @param dialogType    the type of dialog
	 * @param timeStamp     the initial <CODE>TimeStamp</CODE> used when the time chooser is shown
	 * @param type          the <CODE>EType</CODE> type of time chooser
	 * @param clockDigits   an <CODE>EClockDigits</CODE> flag indicating whether or not the clock's digits should
	 *                      always be numbered from 1 to 12, or from 1 to 12 and 13 to 24 in AM and PM respectively
	 * @param updating      an <CODE>EUpdating</CODE> flag indicating whether or not the clock's hands should
	 *                      be updated continuously
	 * @param digitalClock  an <CODE>EDigitalClock</CODE> flag indicating whether or not a digital indication of
	 *                      the current time should be shown
	 * @see                 JTimeChooser#JTimeChooser(JFrame,String,JDefaultDialog.EType,TimeStamp,EType,EClockDigits,EUpdating,EDigitalClock,JDefaultDialog.EActivation)
	 * @see                 JTimeChooser.EType
	 * @see                 JTimeChooser.EClockDigits
	 * @see                 JTimeChooser.EUpdating
	 * @see                 JTimeChooser.EDigitalClock
	 */
	public JTimeChooser(JFrame owner, String title, JDefaultDialog.EType dialogType, TimeStamp timeStamp, EType type, EClockDigits clockDigits, EUpdating updating, EDigitalClock digitalClock)
	{
		this(owner,title,dialogType,timeStamp,type,clockDigits,updating,digitalClock,JDefaultDialog.EActivation.kImmediately);
	}

	/**
	 * Constructs a <CODE>JTimeChooser</CODE> object and allows postponing of activation.
	 * <P>
	 * Depending on the desired functionality, the time chooser can be allowed to select only:
	 * <UL>
	 *   <LI>the hour and minute,</LI>
	 *   <LI>the hour, minute and second,</LI>
	 *   <LI>or the hour, minute, second and millisecond.</LI>
	 * </UL>
	 *
	 * @param owner         the frame in which this dialog is to be displayed
	 * @param title         the dialog's window title
	 * @param dialogType    the type of dialog
	 * @param timeStamp     the initial <CODE>TimeStamp</CODE> used when the time chooser is shown
	 * @param type          the <CODE>EType</CODE> type of time chooser
	 * @param clockDigits   an <CODE>EClockDigits</CODE> flag indicating whether or not the clock's digits should
	 *                      always be numbered from 1 to 12, or from 1 to 12 and 13 to 24 in AM and PM respectively
	 * @param updating      an <CODE>EUpdating</CODE> flag indicating whether or not the clock's hands should
	 *                      be updated continuously
	 * @param digitalClock  an <CODE>EDigitalClock</CODE> flag indicating whether or not a digital indication of
	 *                      the current time should be shown
	 * @param activation    an <CODE>EActivation</CODE> flag indicating whether or not the dialog box should be made
	 *                      visible at the end of the constructor (which can be useful for <B>caching</B>)
	 * @see                 JTimeChooser#JTimeChooser(JFrame,String,JDefaultDialog.EType,TimeStamp,EType,EClockDigits,EUpdating,EDigitalClock)
	 * @see                 JTimeChooser.EType
	 * @see                 JTimeChooser.EClockDigits
	 * @see                 JTimeChooser.EUpdating
	 * @see                 JTimeChooser.EDigitalClock
	 */
	public JTimeChooser(JFrame owner, String title, JDefaultDialog.EType dialogType, TimeStamp timeStamp, EType type, EClockDigits clockDigits, EUpdating updating, EDigitalClock digitalClock, JDefaultDialog.EActivation activation)
	{
		super(owner,
				JDefaultDialog.EModality.kModal,
				JDefaultDialog.ESize.kFixedSize,
				dialogType,
				new Object[] {title, timeStamp, type, clockDigits, updating, digitalClock},
				activation);
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
			fTimeStamp.setToCurrentTime();

			if ((fType == EType.kHourMinute) || (fType == EType.kHourMinute)) {
				fTimeStamp.set(fTimeStamp.getHour(),fTimeStamp.getMinute(),fTimeStamp.getSecond(),0);
			}
			if (fType == EType.kHourMinute) {
				fTimeStamp.set(fTimeStamp.getHour(),fTimeStamp.getMinute(),0,0);
			}
			fHourChooser.setValue(fTimeStamp.getHour());
			fMinuteChooser.setValue(fTimeStamp.getMinute());
			fSecondChooser.setValue(fTimeStamp.getSecond());
			fMillisecondChooser.setValue(fTimeStamp.getMillisecond());
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

	/**
	 * Sets the default time.
	 * <P>
	 * The default time is initially shown in the time chooser.
	 *
	 * @param defaultTimeStamp  the default time for the time chooser
	 */
	public void setDefaultTime(TimeStamp defaultTimeStamp)
	{
		fTimeStamp = defaultTimeStamp;
		fHourChooser.setValue(fTimeStamp.getHour());
		fMinuteChooser.setValue(fTimeStamp.getMinute());
		fSecondChooser.setValue(fTimeStamp.getSecond());
		fMillisecondChooser.setValue(fTimeStamp.getMillisecond());
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
		fTimeStamp = new TimeStamp((TimeStamp) parameters[1]);
		fType = (EType) parameters[2];
		fClockDigits = (EClockDigits) parameters[3];
		fUpdating = (EUpdating) parameters[4];
		fDigitalClock = (EDigitalClock) parameters[5];
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
		JUnfocusableButton unfocusableButton = null;

		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(5,5,5,5));

		// add the clock panel
		fClockPanel = new ClockPanel(fUpdating,fDigitalClock);
		fClockPanel.setToolTipText(I18NL10N.translate("tooltip.ClockPanel"));
		mainPanel.add(fClockPanel);

		mainPanel.add(new JEtchedLine(JEtchedLine.EOrientation.kHorizontal));

		// add the time controls
		try {
			Image exclamationIcon = JARResources.fSystemResources.getImage(kExclamationIconFilename);
			Image exclamationRolloverIcon = JARResources.fSystemResources.getImage(kExclamationRolloverIconFilename);

			panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER));
			unfocusableButton = new JUnfocusableButton(new ImageIcon(exclamationIcon));
			unfocusableButton.setBorder(new EmptyBorder(0,0,0,0));
			unfocusableButton.setFocusPainted(false);
			unfocusableButton.setRolloverIcon(new ImageIcon(exclamationRolloverIcon));
			unfocusableButton.setRolloverEnabled(true);
			unfocusableButton.setToolTipText(I18NL10N.translate("tooltip.GetCurrentTime"));
			unfocusableButton.setActionCommand(kCurrentTime);
			unfocusableButton.addActionListener(this);
			panel.add(unfocusableButton);
		}
		catch (FileDoesNotExistException exc) {
			JWarningDialog.warn(this,I18NL10N.translate("error.GUIComponentImageNotFound"));
		}

		SpinnerNumberModel hourSpinnerNumberModel = new SpinnerNumberModel(fTimeStamp.getHour(),0,23,1);
		fHourChooser = new JSpinner(hourSpinnerNumberModel);	
		// format number as an integer
		JSpinner.NumberEditor hourSpinnerNumberEditor = new JSpinner.NumberEditor(fHourChooser,"0");
		fHourChooser.setEditor(hourSpinnerNumberEditor);
		fHourChooser.addChangeListener(this);
		fHourChooser.setToolTipText(I18NL10N.translate("tooltip.SetHour"));
		panel.add(fHourChooser);
		panel.add(new JLabel("<HTML><B>:</B></HTML>"));

		SpinnerNumberModel minuteSpinnerNumberModel = new SpinnerNumberModel(fTimeStamp.getMinute(),0,59,1);
		fMinuteChooser = new JSpinner(minuteSpinnerNumberModel);	
		// format number as an integer
		JSpinner.NumberEditor minuteSpinnerNumberEditor = new JSpinner.NumberEditor(fMinuteChooser,"0");
		fMinuteChooser.setEditor(minuteSpinnerNumberEditor);
		fMinuteChooser.addChangeListener(this);
		fMinuteChooser.setToolTipText(I18NL10N.translate("tooltip.SetMinute"));
		panel.add(fMinuteChooser);
		panel.add(new JLabel("<HTML><B>:</B></HTML>"));

		SpinnerNumberModel secondSpinnerNumberModel = new SpinnerNumberModel(fTimeStamp.getSecond(),0,59,1);
		fSecondChooser = new JSpinner(secondSpinnerNumberModel);	
		// format number as an integer
		JSpinner.NumberEditor secondSpinnerNumberEditor = new JSpinner.NumberEditor(fSecondChooser,"0");
		fSecondChooser.setEditor(secondSpinnerNumberEditor);
		fSecondChooser.addChangeListener(this);
		fSecondChooser.setToolTipText(I18NL10N.translate("tooltip.SetSecond"));
		if (fType == EType.kHourMinute) {
			fSecondChooser.setEnabled(false);
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
		fMillisecondChooser.setToolTipText(I18NL10N.translate("tooltip.SetMillisecond"));
		if ((fType == EType.kHourMinute) || (fType == EType.kHourMinuteSecond)) {
			fMillisecondChooser.setEnabled(false);
			fMillisecondChooser.setVisible(false);
		}
		panel.add(fMillisecondChooser);

		mainPanel.add(panel);
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
		private static final int kContinuousUpdatePeriod = 50;
		private static final int kDiscreteUpdatePeriod = 500;
		private static final int kClockTickUpdatePeriod = 1000;

		// the dialog's size
		private static final int kClockWidth = 250;
		private static final int kClockHeight = kClockWidth;

		// internal datastructures
		private EUpdating fUpdating;
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
		public ClockPanel(EUpdating updating, EDigitalClock digitalClock)
		{
			fUpdating = updating;
			fDigitalClock = digitalClock;

			fCurrentTimeStamp = new TimeStamp();
			setPreferredSize(new Dimension(kClockWidth,kClockHeight));

			if (kEnableMouseListeners &&
					((fType == EType.kHourMinuteSecond) || (fType == EType.kHourMinuteSecondMillisecond))) {
				addMouseListener(this);
				addMouseMotionListener(this);
			}

			// create a Swing timer to periodically update the clock's display
			Action updateClockPanelAction = new AbstractAction()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fCurrentTimeStamp.setToCurrentTime();
					repaint();
				}
			};

			// set the update period
			if (fUpdating == EUpdating.kContinuous) {
				new javax.swing.Timer(kContinuousUpdatePeriod,updateClockPanelAction).start();
			}
			else if (fUpdating == EUpdating.kDiscrete) {
				new javax.swing.Timer(kDiscreteUpdatePeriod,updateClockPanelAction).start();
			}

			Action clockTickingAction = new AbstractAction()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (isShown()) {
						MP3Player.playSystemSound(MP3Player.kSoundFilenameClockTick);
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

				double angle = i * ((2.0 * Math.PI) / 60.0);
				int x = fCenterX + ((int) Math.round(tickRadius * Math.cos(angle)));
				int y = fCenterY + ((int) Math.round(tickRadius * Math.sin(angle)));

				if ((i % 5) == 0) {

					// plot an hourly tickmark (dark green)
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
					if (fClockDigits == EClockDigits.kUse12Hour) {
						if (hoursLabel > 12) {
							gr.drawString(String.valueOf(hoursLabel - 12),x,y);
						}
						else {
							gr.drawString(String.valueOf(hoursLabel),x,y);
						}
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
					// a minute/second tickmark
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
			int secondsHandRadius = minutesHandRadius;

			// show the clock's hour hand
			int hours = fCurrentTimeStamp.getHour();
			if (hours >= 12) {
				hours -= 12;
			}
			double hoursAngle = hours * ((2.0 * Math.PI) / 12.0) - (Math.PI / 2.0);
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
			double minutesAngle = fCurrentTimeStamp.getMinute() * ((2.0 * Math.PI) / 60.0) - (Math.PI / 2.0);
			if (fUpdating == EUpdating.kContinuous) {
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
			fSecondsRadiusZone = secondsHandRadius;
			double secondsAngle = fCurrentTimeStamp.getSecond() * ((2.0 * Math.PI) / 60.0) - (Math.PI / 2.0);
			if (fUpdating == EUpdating.kContinuous) {
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

				int seconds = 60 - ((int) Math.floor(60.0 * (MathTools.atan(x,y) / (2.0 * Math.PI)))) + 15;
				if (seconds > 60) {
					seconds = seconds - 61;
				}
				seconds = MathTools.clip(seconds,0,59);
				fTimeStamp.set(fTimeStamp.getHour(),fTimeStamp.getMinute(),seconds,fTimeStamp.getMillisecond());
				fSecondChooser.setValue(fTimeStamp.getSecond());
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
