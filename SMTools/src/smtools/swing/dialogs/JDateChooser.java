// ---------------------------------
// Filename      : JDateChooser.java
// Author        : Sven Maerivoet
// Last modified : 18/12/2011
// Target        : Java VM (1.6)
// ---------------------------------

/**
 * Copyright 2003-2012 Sven Maerivoet
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

package smtools.swing.dialogs;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import smtools.application.util.*;
import smtools.exceptions.*;
import smtools.miscellaneous.*;
import smtools.swing.util.*;

/**
 * The <CODE>JDateChooser</CODE> class provides a dialog box for choosing a date.
 * <P>
 * Note that a valid {@link Messages} database must be available!
 * <P>
 * The dialog box is <I>modal</I>, <I>non-resizable</I> and contains <I>"Ok"</I> and <I>"Cancel" buttons</I>
 * to close it. Here's an example of a date chooser (Microsoft Windows L&F):
 * <P>
 * <UL>
 *   <IMG src="doc-files/date-chooser-windows.png">
 * </UL>
 * <P>
 * As can be seen, the following features are available:
 * <P>
 * <UL>
 *   <LI>The <B>currently selected date</B> is shown in white.</LI>
 *   <P>
 *   <LI>Whenever the mouse pointer moves over the day numbers of the calendar, they become
 *       <B>clickable buttons</B>. The button currently underneath the mouse pointer
 *       is shown in green (a tooltip containing the full date is shown after
 *       a while).</LI>
 *   <P>
 *   <LI>At the top of the dialog box, a navigational area is present for choosing a month and a year.</LI>
 *   <P>
 *   <UL>
 *     <LI>The optional <B>undo button</B> <IMG src="doc-files/calendar-undo.png" align=center>
 *         resets the calendar to its initial date (and selects it).</LI>
 *     <P>
 *     <LI>The <B>exclamation button</B> <IMG src="doc-files/calendar-exclamation.png" align=center>
 *         moves the calendar to the current date (and selects it).</LI>
 *   </UL>
 * </UL>
 * <P>
 * When the user closes the date chooser's dialog box, its state should be queried as follows:
 * <P>
 * <CODE>
 * <PRE>
 *   if (!myDateChooser.cancelled()) {
 *     DateStamp dateStamp = myDateChooser.getSelectedDate();
 *     // rest of code
 *   }
 * </PRE>
 * </CODE>
 * <P>
 * <B><U>Important remark</U></B>
 * <P>
 * <UL>
 *   This GUI-component supports <B>caching</B> in the <I>SMTools</I> framework. Using the
 *   {@link JDateChooser#JDateChooser(JFrame,String,JDefaultDialog.EType,DateStamp,EUseDefaultDate,JDefaultDialog.EActivation)} constructor, dialog
 *   activation can be postponed until an explicit call to {@link JDefaultDialog#activate}
 *   is made.
 * </UL>
 * <P>
 * Note that the system resources must be initialised (see {@link JARResources#fSystemResources}).
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 18/12/2011
 */
public final class JDateChooser extends JDefaultDialog implements ChangeListener
{
	// the different default date modes
	/**
	 * Useful constants to allow the use of the <B>undo button</B>
	 * <IMG src="doc-files/calendar-undo.png" align=center>.
	 */
	public static enum EUseDefaultDate {kEnabled, kDisabled};

	// the locations of the navigation icons
	private static final String kUndoIconFilename = "smtools-resources/images/undo.gif";
	private static final String kUndoRolloverIconFilename = "smtools-resources/images/undo-hl.gif";
	private static final String kExclamationIconFilename = "smtools-resources/images/exclamation.gif";
	private static final String kExclamationRolloverIconFilename = "smtools-resources/images/exclamation-hl.gif";

	// the names of the months and the days
	private static final String[] kMonths =
		{Messages.lookup("textMonthJanuary"),
		Messages.lookup("textMonthFebruary"),
		Messages.lookup("textMonthMarch"),
		Messages.lookup("textMonthApril"),
		Messages.lookup("textMonthMay"),
		Messages.lookup("textMonthJune"),
		Messages.lookup("textMonthJuly"),
		Messages.lookup("textMonthAugust"),
		Messages.lookup("textMonthSeptember"),
		Messages.lookup("textMonthOctober"),
		Messages.lookup("textMonthNovember"),
		Messages.lookup("textMonthDecember")};

	private static final String[] kWeekDaysAbbreviated =
		{Messages.lookup("textDayMondayAbbreviated"),
		Messages.lookup("textDayTuesdayAbbreviated"),
		Messages.lookup("textDayWednesdayAbbreviated"),
		Messages.lookup("textDayThursdayAbbreviated"),
		Messages.lookup("textDayFridayAbbreviated"),
		Messages.lookup("textDaySaturdayAbbreviated"),
		Messages.lookup("textDaySundayAbbreviated")};

	// the number of days in each month
	private static final int[] kDaysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	// some sizes
	private static final int kDayButtonSize = 30;
	private static final int kSelectedLabelWidth = 220;

	// some action-commands
	private static final String kDaySelected = "day-selected";
	private static final String kDefaultDate = "default-date";
	private static final String kCurrentDate = "current-date";

	// internal datastructures
	private String fWindowTitle;
	private JUnfocusableButton fDefaultDateButton;
	private JComboBox<String> fMonthChooser;
	private JSpinner fYearChooser;
	private JUnfocusableTriggeredButton[] fDayButtons;
	private JLabel fSelectedDateLabel;
	private DateStamp fDefaultDate;
	private DateStamp fSelectedDate;
	private EUseDefaultDate fUseDefaultDate;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JDateChooser</CODE> object and shows it on the screen.
	 *
	 * @param owner            the frame in which this dialog is to be displayed
	 * @param title            the dialog's window title
	 * @param dialogType       the type of dialog
	 * @param defaultDateStamp the default <CODE>DateStamp</CODE> used when the calendar is shown
	 * @param useDefaultDate   an <CODE>EUseDefaultDate</CODE> switch for enabling/disabling the use of the default date
	 * @see   JDateChooser#JDateChooser(JFrame,String,JDefaultDialog.EType,DateStamp,EUseDefaultDate,JDefaultDialog.EActivation)
	 */
	public JDateChooser(JFrame owner, String title, JDefaultDialog.EType dialogType, DateStamp defaultDateStamp, EUseDefaultDate useDefaultDate)
	{
		this(owner,title,dialogType,defaultDateStamp,useDefaultDate,JDefaultDialog.EActivation.kImmediately);
	}

	/**
	 * Constructs a <CODE>JDateChooser</CODE> object and allows postponing of activation.
	 *
	 * @param owner            the frame in which this dialog is to be displayed
	 * @param title            the dialog's window title
	 * @param dialogType       the type of dialog
	 * @param defaultDateStamp the default <CODE>DateStamp</CODE> used when the calendar is shown
	 * @param useDefaultDate   an <CODE>EUseDefaultDate</CODE> switch for enabling/disabling the use of the default date
	 * @param activation       an <CODE>EActivation</CODE> flag indicating whether or not the dialog box should be made
	 *                         visible at the end of the constructor (which can be useful for <B>caching</B>)
	 * @see   JDateChooser#JDateChooser(JFrame,String,JDefaultDialog.EType,DateStamp,EUseDefaultDate)
	 * @see   JDefaultDialog.EActivation
	 */
	public JDateChooser(JFrame owner, String title, JDefaultDialog.EType dialogType, DateStamp defaultDateStamp, EUseDefaultDate useDefaultDate, JDefaultDialog.EActivation activation)
	{
		super(owner,
				JDefaultDialog.EModality.kModal,
				JDefaultDialog.ESize.kFixedSize,
				dialogType,
				new Object[] {title,defaultDateStamp,useDefaultDate},
				activation);
		JARResources.checkSystemInitialisation();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	// the action-listener
	/**
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);

		String command = e.getActionCommand();

		if (command.equals(kDaySelected)) {
			JButton dayButton = (JButton) e.getSource();
			String dayString = dayButton.getText();

			if (dayString.length() > 0) {
				fSelectedDate.set(Integer.parseInt(dayString),fSelectedDate.getMonth(),fSelectedDate.getYear());
				setCaptions();
			}
		}
		else {
			if (command.equals("comboBoxChanged")) {
				int selectedMonth = getMonthIndex((String) fMonthChooser.getSelectedItem()) + 1;
				fSelectedDate.set(fSelectedDate.getDay(),selectedMonth,fSelectedDate.getYear());
			}
			else if (command.equals(kDefaultDate)) {
				fSelectedDate.set(fDefaultDate);
				fMonthChooser.setSelectedIndex(fSelectedDate.getMonth() - 1);
				fYearChooser.setValue(fSelectedDate.getYear());
			}
			else if (command.equals(kCurrentDate)) {
				fSelectedDate.setToCurrentDate();
				fMonthChooser.setSelectedIndex(fSelectedDate.getMonth() - 1);
				fYearChooser.setValue(fSelectedDate.getYear());
			}

			setCaptions();
		}
	}

	// the change-listener
	/**
	 */
	@Override
	public void stateChanged(ChangeEvent e)
	{
		int selectedDay = preventDateOverlow(fSelectedDate.getDay());
		fSelectedDate.set(selectedDay,fSelectedDate.getMonth(),((Integer) fYearChooser.getValue()).intValue());
		setCaptions();
	}

	/**
	 * Returns the currently selected date.
	 *
	 * @return the currently selected date
	 */
	public DateStamp getSelectedDate()
	{
		if (cancelled()) {
			return fDefaultDate;
		}
		else {
			return fSelectedDate;
		}
	}

	/**
	 * Sets the default date.
	 * <P>
	 * The default date is initially shown in the calendar, it is furthermore
	 * accessible via the optional <B>undo button</B>
	 * <IMG src="doc-files/calendar-undo.png" align=center>.
	 *
	 * @param defaultDateStamp the default date for the date chooser
	 */
	public void setDefaultDate(DateStamp defaultDateStamp)
	{
		fDefaultDate.set(defaultDateStamp);
		fSelectedDate.set(fDefaultDate);
		if (fDefaultDateButton != null) {
			fDefaultDateButton.setToolTipText(Messages.lookup("tooltipCalendarCurrentDate",fDefaultDate.getDMYString()));
		}
		setCaptions();
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
		fDefaultDate = (DateStamp) parameters[1];
		fSelectedDate = new DateStamp(fDefaultDate);
		fUseDefaultDate = (EUseDefaultDate) parameters[2];
	}

	/**
	 */
	@Override
	protected final String getWindowTitle()
	{
		return fWindowTitle;
	}

	/**
	 */
	@Override
	protected void constructMainPanel(JPanel mainPanel)
	{
		JPanel panel = null;
		JPanel subPanel = null;
		JLabel label = null;
		JUnfocusableTriggeredButton dayButton = null;
		JUnfocusableButton navButton = null;

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(10,0,10,0));

			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

				subPanel = new JPanel();
				subPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));

				String[] monthNames = new String[12];
				for (int monthNr = 0; monthNr < 12; ++monthNr) {
					monthNames[monthNr] = kMonths[monthNr].substring(0,1).toUpperCase() + kMonths[monthNr].substring(1);
				}
				fMonthChooser = new JComboBox<String>(monthNames);
				fMonthChooser.setEditable(false);
				fMonthChooser.addActionListener(this);
				fMonthChooser.setToolTipText(Messages.lookup("tooltipCalendarMonth"));
				subPanel.add(fMonthChooser);

				// create a small gap
				subPanel.add(Box.createRigidArea(new Dimension(20,0)));

				SpinnerNumberModel yearSpinnerNumberModel = new SpinnerNumberModel(1900,1900,2100,1);
				fYearChooser = new JSpinner(yearSpinnerNumberModel);	
				// format number as an integer
				JSpinner.NumberEditor yearSpinnerNumberEditor = new JSpinner.NumberEditor(fYearChooser,"0");
				fYearChooser.setEditor(yearSpinnerNumberEditor);
				fYearChooser.addChangeListener(this);
				fYearChooser.setToolTipText(Messages.lookup("tooltipCalendarYear"));
				subPanel.add(fYearChooser);

				// create a small gap
				subPanel.add(Box.createRigidArea(new Dimension(20,0)));

				if (fUseDefaultDate == EUseDefaultDate.kEnabled) {
					try {
						Image undoIcon = JARResources.fSystemResources.getImage(kUndoIconFilename);
						Image undoRolloverIcon = JARResources.fSystemResources.getImage(kUndoRolloverIconFilename);

						fDefaultDateButton = new JUnfocusableButton(new ImageIcon(undoIcon));
						fDefaultDateButton.setBorder(new EmptyBorder(0,0,0,0));
						fDefaultDateButton.setFocusPainted(false);
						fDefaultDateButton.setRolloverIcon(new ImageIcon(undoRolloverIcon));
						fDefaultDateButton.setRolloverEnabled(true);
						fDefaultDateButton.setToolTipText(Messages.lookup("tooltipCalendarCurrentDate",fDefaultDate.getDMYString()));
						fDefaultDateButton.setActionCommand(kDefaultDate);
						fDefaultDateButton.addActionListener(this);
						subPanel.add(fDefaultDateButton);

						// create a small gap
						subPanel.add(Box.createRigidArea(new Dimension(15,0)));
					}
					catch (FileDoesNotExistException exc) {
						JWarningDialog.warn(this,Messages.lookup("errorGUIComponentImageNotFound"));
					}
				}

				try {
					Image exclamationIcon = JARResources.fSystemResources.getImage(kExclamationIconFilename);
					Image exclamationRolloverIcon = JARResources.fSystemResources.getImage(kExclamationRolloverIconFilename);
		
					navButton = new JUnfocusableButton(new ImageIcon(exclamationIcon));
					navButton.setBorder(new EmptyBorder(0,0,0,0));
					navButton.setFocusPainted(false);
					navButton.setRolloverIcon(new ImageIcon(exclamationRolloverIcon));
					navButton.setRolloverEnabled(true);
					navButton.setToolTipText(Messages.lookup("tooltipCalendarExclamation",(new DateStamp()).getDMYString()));
					navButton.setActionCommand(kCurrentDate);
					navButton.addActionListener(this);
					subPanel.add(navButton);
				}
				catch (FileDoesNotExistException exc) {
					JWarningDialog.warn(this,Messages.lookup("errorGUIComponentImageNotFound"));
				}
			panel.add(subPanel);
			panel.add(new JEtchedLine());

		mainPanel.add(panel,BorderLayout.NORTH);

			// show the first letters of the weekdays
			panel = new JPanel();
			panel.setLayout(new GridLayout(0,7));
			panel.setBorder(new EmptyBorder(10,10,10,9));
			for (int i = 0; i < 7; ++i) {
				label = new JLabel(kWeekDaysAbbreviated[i],JLabel.CENTER);
				label.setFont(label.getFont().deriveFont(Font.ITALIC).deriveFont(12.0f));
				label.setForeground(Color.BLUE);
				panel.add(label);
			}

			// show the days' buttons
			int nrOfDayButtons = 7 * 6;
			fDayButtons = new JUnfocusableTriggeredButton[nrOfDayButtons];
			for (int i = 0; i < nrOfDayButtons; i++) {
				dayButton = new JUnfocusableTriggeredButton(String.valueOf(i));
				dayButton.setPreferredSize(new Dimension(kDayButtonSize,kDayButtonSize));
				dayButton.setActionCommand(kDaySelected);
				dayButton.addActionListener(this);
				fDayButtons[i] = dayButton;
				fDayButtons[i].setHighlightColor(Color.ORANGE.darker());
				fDayButtons[i].setSelectedColor(Color.GREEN);
				panel.add(fDayButtons[i]);
			}
		mainPanel.add(panel,BorderLayout.CENTER);

			// show the label with the selected date
			panel = new JPanel();
			panel.setLayout(new FlowLayout());
			fSelectedDateLabel = new JLabel(fDefaultDate.getFullDateString(),JLabel.CENTER);
			Border emptyBorder = new EmptyBorder(10,10,10,9);
			Border etchedBorder = new EtchedBorder();
			fSelectedDateLabel.setBorder(new CompoundBorder(etchedBorder,emptyBorder));
			panel.add(fSelectedDateLabel);
		mainPanel.add(panel,BorderLayout.SOUTH);

		fMonthChooser.setSelectedIndex(fSelectedDate.getMonth() - 1);

		setCaptions();
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	private void setCaptions()
	{
		int selectedDay = fSelectedDate.getDay();
		int selectedMonth = fSelectedDate.getMonth();
		int selectedYear = fSelectedDate.getYear();
		
		fMonthChooser.setSelectedIndex(selectedMonth - 1);
		fYearChooser.setValue(selectedYear);

		// deselect all day-buttons
		for (int i = 0; i < fDayButtons.length; i++) {
			fDayButtons[i].setSelected(false);
		}

		// determine the number of days to leave blank at the start of this month
		DateStamp firstDayOfMonth = new DateStamp(1,selectedMonth,selectedYear);
		int dayOfWeek = firstDayOfMonth.getDayOfWeek();
		for (int i = 0; i < (dayOfWeek - 1); ++i) {
			fDayButtons[i].setVisible(false);
		}
		int dayButtonIndex = dayOfWeek - 1;

		int nrOfDaysInMonth = kDaysInMonth[selectedMonth - 1];
		if ((selectedMonth == 2) && firstDayOfMonth.isLeapYear()) {
			++nrOfDaysInMonth;
		}
		for (int day = 1; day <= nrOfDaysInMonth; ++day) {

			fDayButtons[dayButtonIndex].setVisible(true);
			fDayButtons[dayButtonIndex].setText(String.valueOf(day));
			fDayButtons[dayButtonIndex].setToolTipText((new DateStamp(day,selectedMonth,selectedYear)).getFullDateString());

			if (day == selectedDay) {
				fDayButtons[dayButtonIndex].setSelected(true);
			}

			++dayButtonIndex;
			++dayOfWeek;
			if (dayOfWeek > 7) {
				dayOfWeek = 1;
			}
		}

		// leave the remaining day-buttons blank
		for (int i = dayButtonIndex; i < fDayButtons.length; ++i) {
			fDayButtons[i].setVisible(false);
		}

		// adjust the label showing the currently selected date
		fSelectedDateLabel.setText(fSelectedDate.getFullDateString());
		fSelectedDateLabel.setFont(fSelectedDateLabel.getFont().deriveFont(Font.BOLD).deriveFont(12.0f));
		fSelectedDateLabel.setPreferredSize(new Dimension(kSelectedLabelWidth,30));

		// update the window title by appending it with the current selected week
		setTitle(getWindowTitle() + " (" + Messages.lookup("textWeek") + " " + String.valueOf(fSelectedDate.getWeekOfYear()) + ")");
	}

	private int preventDateOverlow(int selectedDay)
	{
		// find the last day of the current month
		int lastDayOfMonth = kDaysInMonth[fSelectedDate.getMonth() - 1];
		if ((fSelectedDate.getMonth() == 1) &&
				(fSelectedDate.isLeapYear())) {
			lastDayOfMonth = 29;
		}

		// check for overflow
		if (selectedDay > lastDayOfMonth) {
			return lastDayOfMonth;
		}
		else {
			return selectedDay;
		}
	}

	private int getMonthIndex(String month)
	{
		boolean found = false;
		int currentMonthIndex = 0;
		while ((!found) && (currentMonthIndex < 12)) {
			if (month.equalsIgnoreCase(kMonths[currentMonthIndex])) {
				found = true;
			}
			else {
				++currentMonthIndex;
			}
		}

		return currentMonthIndex;
	}
}
