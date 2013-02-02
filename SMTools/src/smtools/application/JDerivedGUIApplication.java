// -------------------------------------------
// Filename      : JDerivedGUIApplication.java
// Author        : Sven Maerivoet
// Last modified : 02/02/2013
// Target        : Java VM (1.6)
// -------------------------------------------

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

package smtools.application;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import smtools.application.concurrent.*;
import smtools.application.util.*;
import smtools.exceptions.*;
import smtools.miscellaneous.*;
import smtools.swing.dialogs.*;
import smtools.swing.util.*;

/**
 * The <CODE>JDerivedGUIApplication</CODE> class is a demonstration of the <I>SMTools</I> GUI framework.
 * <P>
 * When this class's {@link JDerivedGUIApplication#main(String[])} method is invoked, the resulting
 * GUI looks as follows:
 * <P>
 * <UL>
 *   <IMG src="doc-files/derived-gui.png">
 * </UL>
 * <P>
 * Refer to the {@link JStandardGUIApplication} class for further information.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 02/02/2013
 * @see     JStandardGUIApplication
 */
public final class JDerivedGUIApplication extends JStandardGUIApplication implements ActionListener
{
	// access point to the Log4j logging facility
	//private static final Logger kLogger = Logger.getLogger(JDerivedGUIApplication.class.getName());

	// the location of the JAR archive containing all the resources
	private static final String kResourceArchiveFilename = "application-resources.zip";

	// the location of the message-databasse
	private static final String kApplicationLocalePrefix = "application-resources/locales/locale-";

	// the location of the splash screen's MP3 sound
	private static final String kSplashScreenSoundFilename = "application-resources/sounds/thx-intro.mp3";

	// the location of the application's icon
	private static final String kApplicationIconFilename = "application-resources/images/icon.jpg";

	// the amount of time to explicitly wait during the splash screen show
	private static final int kSplashScreenStatusMessageWaitTime = 500;

	// the action commands for the menus
	private static final String kActionCommandMenuItemDateChooser = "menuItem.DateChooser";
	private static final String kActionCommandMenuItemTimeChooser = "menuItem.TimeChooser";
	private static final String kActionCommandMenuItemTaskRunner = "menuItem.TaskRunner";
	private static final String kActionCommandMenuItemIndex = "menuItem.Index";

	// internal datastructures
	private int fDateChooserID;
	private int fTimeChooserID;
	private JProgressUpdateGlassPane fProgressUpdateGlassPane;
	private int fVisualisationType;

	/*************************
	 * STATIC INITIALISATION *
	 *************************/

	static {
		JDevelopMode.deactivate();
	}

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JDerivedGUIApplication</CODE> object.
	 *
	 * @param argv an array of strings containing the <B>command-line</B> parameters
	 */
	public JDerivedGUIApplication(String argv[])
	{
		super(argv,null);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * The application's entry point.
	 */
	public static void main(String[] argv)
	{
		new JDerivedGUIApplication(argv);
	}

	// the action-listener
	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);

		String command = e.getActionCommand();

		if (command.equalsIgnoreCase(kActionCommandMenuItemDateChooser)) {
			getStatusBar().setStatusText(I18NL10N.translate("text.ChooseDateDialogTitle"));
			JDateChooser dateChooser = (JDateChooser) getGUIComponentCache().retrieveComponent(fDateChooserID);
			dateChooser.setDefaultDate(new DateStamp(11,4,1976));
			dateChooser.activate();
			getStatusBar().clearStatusText();

			if (dateChooser.cancelled()) {
				JWarningDialog.warn(this,I18NL10N.translate("text.ChoiceCancelled"));
			}
			else {
				JMessageDialog.show(this,I18NL10N.translate("text.SelectedDate",dateChooser.getSelectedDate().getFullDateString()));
			}
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemTimeChooser)) {
			getStatusBar().setStatusText(I18NL10N.translate("text.ChooseTimeDialogTitle"));
			JTimeChooser timeChooser = (JTimeChooser) getGUIComponentCache().retrieveComponent(fTimeChooserID);
			timeChooser.setDefaultTime(new TimeStamp(12,25,20,10));
			timeChooser.activate();

			getStatusBar().clearStatusText();
			if (timeChooser.cancelled()) {
				JWarningDialog.warn(this,I18NL10N.translate("text.ChoiceCancelled"));
			}
			else {
				JMessageDialog.show(this,I18NL10N.translate("text.SelectedTime",timeChooser.getSelectedTime().getHMSString()));
			}
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemIndex)) {
			JIncompleteWarningDialog.warn(this,"smtools.application.DerivedGUIApplication.actionPerformed()");
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemTaskRunner)) {
			// cycle to the next visualisation type
			switch (fVisualisationType) {
				case 0:
					fProgressUpdateGlassPane.setVisualisationType(JProgressUpdateGlassPane.EVisualisationType.kBar);
					break;
				case 1:
					fProgressUpdateGlassPane.setVisualisationType(JProgressUpdateGlassPane.EVisualisationType.kCircles);
					break;
				case 2:
					fProgressUpdateGlassPane.setVisualisationType(JProgressUpdateGlassPane.EVisualisationType.kFixedSector);
					break;
				case 3:
					fProgressUpdateGlassPane.setVisualisationType(JProgressUpdateGlassPane.EVisualisationType.kRotatingSector);
					break;
			}
			fVisualisationType = (fVisualisationType + 1) % 4;

			MyTaskExecutor taskExecutor = new MyTaskExecutor(fProgressUpdateGlassPane,this);
			for (int taskID = 0; taskID < 100; ++taskID) {
				// setup a task with custom input
				MyTask task = new MyTask(taskID);
				task.setID(taskID);
				task.setNrOfSubTasks(1000);
				taskExecutor.addTask(task);
			}
			// schedule asynchronous execution
			taskExecutor.execute();
		}
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected String getApplicationResourceArchiveFilename()
	{
		return kResourceArchiveFilename;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected String getApplicationLocalePrefix()
	{
		return kApplicationLocalePrefix;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected void initialiseClass(Object[] parameters)
	{
		getSplashScreen().setStatusMessageWaitTime(kSplashScreenStatusMessageWaitTime);

		getSplashScreen().setStatusMessage(I18NL10N.translate("text.CachingCustomGUIComponents"));

		// cache custom GUI components
		JDateChooser dateChooser = new JDateChooser(
			this,
			I18NL10N.translate("text.ChooseDateDialogTitle"),
			JDefaultDialog.EType.kOkCancel,
			new DateStamp(),
			JDateChooser.EUseDefaultDate.kEnabled,
			JDefaultDialog.EActivation.kPostponed);
		fDateChooserID = getGUIComponentCache().addComponent(dateChooser);

		JTimeChooser timeChooser = new JTimeChooser(
			this,
			I18NL10N.translate("text.ChooseTimeDialogTitle"),
			JDefaultDialog.EType.kOkCancel,
			new TimeStamp(),
			JTimeChooser.EType.kHourMinuteSecondMillisecond,
			JTimeChooser.EClockDigits.kUse12Hour,
			JTimeChooser.EUpdating.kDiscrete,
			JTimeChooser.EDigitalClock.kShown,
			JDefaultDialog.EActivation.kPostponed);
		fTimeChooserID = getGUIComponentCache().addComponent(timeChooser);
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected boolean parseParameter(int paramNr, String parameter)
	{
		final String kCustomParameter = "PARAMETER";
		final String kCustomOption = "OPTION";

		String upperCaseParameter = parameter.toUpperCase();

		// parse parameter
		if (upperCaseParameter.startsWith(kCustomParameter + "=")) {
			String upperCaseOption = upperCaseParameter.substring(kCustomParameter.length() + 1);

			// parse option
			if (upperCaseOption.equalsIgnoreCase(kCustomOption)) {
				System.out.println("Parameter parsed.");
			}
			else {
				showParameterWarning(paramNr,parameter,"not a valid option");
			}

			// indicate that parameter was valid
			return true;
		}
		else {
			// indicate that parameter is unknown
			return false;
		}
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected JLabel getSplashScreenContent()
	{
		JLabel label = new JLabel("JDerivedGUIApplication",JLabel.LEFT);
		label.setFont(label.getFont().deriveFont(Font.BOLD).deriveFont(20.0f));
		return label;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected InputStream getSplashScreenSound()
	{
		try {
			return fResources.getInputStream(kSplashScreenSoundFilename);
		}
		catch (FileDoesNotExistException exc) {
			return null;
		}
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected final Dimension getInitialGUISize()
	{
		return (new Dimension(JStandardGUIApplication.kFullScreenGUI,JStandardGUIApplication.kFullScreenGUI));
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected Image getIcon()
	{
		try {
			return fResources.getImage(kApplicationIconFilename);
		}
		catch (FileDoesNotExistException exc) {
			return null;
		}
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected String getWindowTitle()
	{
		return "smtools.application.JDerivedGUIApplication";
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected void constructContentPane(JPanel contentPane)
	{
		contentPane.setLayout(new BorderLayout());

		String backgroundImageFilename = "application-resources/images/clouds.jpg";

		JImagePanel jip = null;
		try {
			jip = new JImagePanel(fResources.getImage(backgroundImageFilename));
			jip.setOpaque(false);
			contentPane.add(jip,BorderLayout.CENTER);
		}
		catch (FileDoesNotExistException exc) {
			// ignore
		}
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected JPanel constructGlassPane()
	{
		fProgressUpdateGlassPane = new JProgressUpdateGlassPane();
		fVisualisationType = 0;
		return fProgressUpdateGlassPane;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected JMenu[] constructMenus()
	{
		JMenu[] menus = new JMenu[1];
		JMenuItem menuItem = null;

		menus[0] = new JMenu(I18NL10N.translate("menu.Demonstration"));
		menus[0].setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate("menu.Demonstration.Mnemonic")));

			menuItem = constructMenuItem(kActionCommandMenuItemDateChooser);
			menuItem.setActionCommand(kActionCommandMenuItemDateChooser);
			menuItem.addActionListener(this);
		menus[0].add(menuItem);

			menuItem = constructMenuItem(kActionCommandMenuItemTimeChooser);
			menuItem.setActionCommand(kActionCommandMenuItemTimeChooser);
			menuItem.addActionListener(this);
		menus[0].add(menuItem);

		menus[0].addSeparator();

			menuItem = constructMenuItem(kActionCommandMenuItemTaskRunner);
			menuItem.setActionCommand(kActionCommandMenuItemTaskRunner);
			menuItem.addActionListener(this);
		menus[0].add(menuItem);

		return menus;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected JMenu constructRightHandMenu()
	{
		JMenu rightHandMenu = null;
		JMenuItem menuItem = null;

		rightHandMenu = new JMenu(I18NL10N.translate("menu.Help"));
		rightHandMenu.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate("menu.Help.Mnemonic")));

		menuItem = constructMenuItem(kActionCommandMenuItemIndex);
		menuItem.setActionCommand(kActionCommandMenuItemIndex);
		menuItem.addActionListener(this);
		rightHandMenu.add(menuItem);

		return rightHandMenu;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected boolean isStatusBarEnabled()
	{
		return true;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected boolean isClockEnabled()
	{
		return true;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected JAboutBox getAboutBox()
	{
		return (new JDerivedAboutBox(this,fResources));
	}

	/*****************
	 * INNER CLASSES *
	 *****************/

	/**
	 * @author  Sven Maerivoet
	 * @version 02/02/2013
	 */
	public class MyTaskExecutor extends JTaskExecutor
	{
		// internal datastructures
		private Component fMainWindow;

		/****************
		 * CONSTRUCTORS *
		 ****************/

		/**
		 */
		public MyTaskExecutor(JProgressUpdateGlassPane progressUpdateGlassPane, Component mainWindow)
		{
			super(progressUpdateGlassPane);
			fMainWindow = mainWindow;
		}

		/*********************
		 * PROTECTED METHODS *
		 *********************/

		/**
		 */
		@Override
		protected void finishTasks()
		{
			int result = 0;
			for (AJTask task : getTasks()) {
				result += ((MyTask) task).getResult();
			}

			JMessageDialog.show(fMainWindow,I18NL10N.translate("text.TaskCompleted",String.valueOf(result)));
		}
	}

	/**
	 * @author  Sven Maerivoet
	 * @version 02/02/2013
	 */
	public class MyTask extends AJTask
	{
		// internal datastructures
		private int fTaskID;
		private ArrayList<Integer> fSubTaskResults;
		private int fTaskResult;

		/****************
		 * CONSTRUCTORS *
		 ****************/

		/**
		 */
		public MyTask(int taskID)
		{
			fTaskID = taskID;
			fSubTaskResults = new ArrayList<Integer>();
		}

		/******************
		 * PUBLIC METHODS *
		 ******************/

		/**
		 */
		public int getResult()
		{
			return fTaskResult;
		}

		/*********************
		 * PROTECTED METHODS *
		 *********************/

		/**
		 */
		@Override
		protected void executeSubTask(int subTaskID)
		{
			fSubTaskResults.add(fTaskID);
			Chrono.wait(5);
		}

		/**
		 */
		@Override
		protected void finishTask()
		{
			fTaskResult = 0;
			for (int subTaskResult : fSubTaskResults) {
				fTaskResult += subTaskResult;
			}
		}
	}
}
