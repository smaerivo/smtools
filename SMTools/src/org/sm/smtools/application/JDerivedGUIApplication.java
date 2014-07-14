// -------------------------------------------
// Filename      : JDerivedGUIApplication.java
// Author        : Sven Maerivoet
// Last modified : 13/07/2014
// Target        : Java VM (1.8)
// -------------------------------------------

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

package org.sm.smtools.application;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.sm.smtools.application.concurrent.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.exceptions.*;
import org.sm.smtools.swing.dialogs.*;
import org.sm.smtools.swing.util.*;
import org.sm.smtools.util.*;

/**
 * The <CODE>JDerivedGUIApplication</CODE> class is a demonstration of the <I>SMTools</I> GUI framework.
 * <P>
 * When this class's {@link JDerivedGUIApplication#main(String[])} method is invoked, the resulting
 * GUI looks as follows:
 * <P>
 * <IMG src="doc-files/derived-gui.png" alt="">
 * <P>
 * Refer to the {@link JStandardGUIApplication} class for further information.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 13/07/2014
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
	private JLabel fStatusBarCustomLabel;
	private int fDateChooserID;
	private int fTimeChooserID;
	private JProgressUpdateGlassPane fProgressUpdateGlassPane;
	private MyTaskExecutor fTaskExecutor;
	private int fVisualisationType;

	/*************************
	 * STATIC INITIALISATION *
	 *************************/

	static {
		DevelopMode.deactivate();
	}

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JDerivedGUIApplication</CODE> object.
	 *
	 * @param argv  an array of strings containing the <B>command-line</B> parameters
	 */
	public JDerivedGUIApplication(String[] argv)
	{
		super(argv,null);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * The application's entry point.
	 *
	 * @param argv  an array of strings containing the <B>command-line</B> parameters
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
			fStatusBarCustomLabel.setText("64-bit FP");
			getStatusBar().setStatusText(I18NL10N.translate("text.ChooseDateDialogTitle"));
			JDateChooser dateChooser = (JDateChooser) getGUIComponentCache().retrieveComponent(fDateChooserID);
			dateChooser.setDefaultDate(new DateStamp(11,4,1976));
			dateChooser.activate();
			getStatusBar().clearStatusText();

			if (dateChooser.isCancelled()) {
				JWarningDialog.warn(this,I18NL10N.translate("text.ChoiceCancelled"));
			}
			else {
				JMessageDialog.show(this,I18NL10N.translate("text.SelectedDate",dateChooser.getSelectedDate().getFullDateString()));
			}
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemTimeChooser)) {
			fStatusBarCustomLabel.setText("128-bit FP");
			getStatusBar().setStatusText(I18NL10N.translate("text.ChooseTimeDialogTitle"));
			JTimeChooser timeChooser = (JTimeChooser) getGUIComponentCache().retrieveComponent(fTimeChooserID);
			timeChooser.setDefaultTime(new TimeStamp(12,25,20,10));
			timeChooser.activate();

			getStatusBar().clearStatusText();
			if (timeChooser.isCancelled()) {
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
			// create or refresh a task executor if necessary
			if ((fTaskExecutor == null) || (!fTaskExecutor.isBusy())) {
				fTaskExecutor = new MyTaskExecutor(fProgressUpdateGlassPane,this);
			}

			if (!fTaskExecutor.isBusy()) {
				// cycle to the next visualisation type
				fVisualisationType = (fVisualisationType + 1) % 4;
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

				for (int taskID = 0; taskID < 500; ++taskID) {
					// setup a task with custom input
					MyTask task = new MyTask();
					fTaskExecutor.addTask(task);
				}

				// schedule asynchronous execution
				fTaskExecutor.execute();
			} // if (!fTaskExecutor.isBusy())
		}
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

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
	protected String setupApplicationResourceArchiveFilename()
	{
		return kResourceArchiveFilename;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected String setupApplicationLocalePrefix()
	{
		return kApplicationLocalePrefix;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected void initialise(Object[] parameters)
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
	protected JLabel setupSplashScreenContent()
	{
		JLabel label = new JLabel("JDerivedGUIApplication",JLabel.LEFT);
		label.setFont(label.getFont().deriveFont(Font.BOLD).deriveFont(20.0f));
		return label;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected InputStream setupSplashScreenSound()
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
	protected final Dimension setupInitialGUISize()
	{
		return (new Dimension(JStandardGUIApplication.kFullScreenGUI,JStandardGUIApplication.kFullScreenGUI));
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected Image setupIcon()
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
	protected String setupWindowTitle()
	{
		return "smtools.application.JDerivedGUIApplication";
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected void setupContentPane(JPanel contentPane)
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
	protected ArrayList<JMenu> setupMenus()
	{
		ArrayList<JMenu> menus = new ArrayList<JMenu>();
		JMenu menu = null;
		JMenuItem menuItem = null;

			menu = new JMenu(I18NL10N.translate("menu.Demonstration"));
			menu.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate("menu.Demonstration.Mnemonic")));

				menuItem = constructMenuItem(kActionCommandMenuItemDateChooser);
				menuItem.setActionCommand(kActionCommandMenuItemDateChooser);
				menuItem.addActionListener(this);
			menu.add(menuItem);

				menuItem = constructMenuItem(kActionCommandMenuItemTimeChooser);
				menuItem.setActionCommand(kActionCommandMenuItemTimeChooser);
				menuItem.addActionListener(this);
			menu.add(menuItem);

			menu.addSeparator();

				menuItem = constructMenuItem(kActionCommandMenuItemTaskRunner);
				menuItem.setActionCommand(kActionCommandMenuItemTaskRunner);
				menuItem.addActionListener(this);
			menu.add(menuItem);
		menus.add(menu);

		return menus;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected JMenu setupRightHandMenu()
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
	protected ArrayList<JLabel> setupStatusBarCustomLabels()
	{
		ArrayList<JLabel> customLabels = new ArrayList<JLabel>();
			fStatusBarCustomLabel = new JLabel("64-bit FP");
			customLabels.add(fStatusBarCustomLabel);
		return customLabels;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected boolean setupIsStatusBarEnabled()
	{
		return true;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected JPanel setupGlassPane()
	{
		fProgressUpdateGlassPane = new JProgressUpdateGlassPane();
		fProgressUpdateGlassPane.setBlocking(true);
		fProgressUpdateGlassPane.setShowTimeEstimation(true);
		fVisualisationType = 0;
		return fProgressUpdateGlassPane;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected boolean setupIsClockEnabled()
	{
		return true;
	}

	/**
	 * See {@link JStandardGUIApplication}.
	 */
	@Override
	protected JAboutBox setupAboutBox()
	{
		return (new JDerivedAboutBox(this,fResources));
	}

	/*****************
	 * INNER CLASSES *
	 *****************/

	/**
	 * This class contains an example about box.
	 * 
	 * @author  Sven Maerivoet
	 * @version 26/12/2013
	 * @see     org.sm.smtools.swing.dialogs.JAboutBox
	 */
	private final class JDerivedAboutBox extends JAboutBox
	{
		/****************
		 * CONSTRUCTORS *
		 ****************/

		/**
		 */
		public JDerivedAboutBox(JFrame owner, JARResources resources)
		{
			super(owner,resources);
		}

		/*********************
		 * PROTECTED METHODS *
		 *********************/

		/**
		 */
		@Override
		protected JLabel setupLogo()
		{
			try {
				return (new JLabel(new ImageIcon(fResources.getImage("application-resources/images/smtools-splash-banner.png"))));
			}
			catch (FileDoesNotExistException exc) {
				return null;
			}
		}

		/**
		 */
		@Override
		protected ELogoPosition setupLogoPosition()
		{
			return JAboutBox.ELogoPosition.kTop;
		}

		/**
		 * @return
		 */
		@Override
		protected String setupAboutText()
		{
			return
			("<B>JDerivedGUIApplication v1.1</B><BR />" +
				"Copyright 2003-2014 Sven Maerivoet");
		}

		/**
		 */
		@Override
		protected StringBuilder setupCopyrightContent()
		{
			try {
				return fResources.getText("application-resources/licence/copyright.txt");
			}
			catch (FileDoesNotExistException exc) {
				return null;
			}
		}

		/**
		 */
		@Override
		protected StringBuilder setupLicenceContent()
		{
			try {
				return fResources.getText("application-resources/licence/apache-licence.txt");
			}
			catch (FileDoesNotExistException exc) {
				return null;
			}
		}

		/**
		 */
		@Override
		protected ArrayList<JLabel> setupAffiliationsLabels()
		{
			ArrayList<JLabel> affiliationsLabels = new ArrayList<JLabel>();
			JLabel affiliationLabel = null;

				affiliationLabel = new JLabel("",SwingConstants.CENTER);
				try {
					affiliationLabel.setIcon(new ImageIcon(fResources.getImage("application-resources/images/smtools-splash-banner.png")));
				}
				catch (FileDoesNotExistException exc) {
				}
				affiliationLabel.setToolTipText(I18NL10N.translate("tooltip.AboutBox.ClickForBrowser"));
			affiliationsLabels.add(affiliationLabel);

				affiliationLabel = new JLabel(
					"<html>" +
						"<b>Sven Maerivoet</b>" +
					"</html>");
			affiliationsLabels.add(affiliationLabel);

				affiliationLabel = new JLabel(
					"<html>" +
						"E-mail: sven.maerivoet@gmail.com" +
					"</html>");
				affiliationLabel.setToolTipText(I18NL10N.translate("tooltip.AboutBox.ClickForEmailClient"));
			affiliationsLabels.add(affiliationLabel);

				affiliationLabel = new JLabel(
					"<html>" +
						"Website: http://www.maerivoet.org/" +
					"</html>");
				affiliationLabel.setToolTipText(I18NL10N.translate("tooltip.AboutBox.ClickForBrowser"));
			affiliationsLabels.add(affiliationLabel);

			for (JLabel label : affiliationsLabels) { 
				label.setHorizontalTextPosition(SwingConstants.CENTER);
				label.setHorizontalAlignment(SwingConstants.LEFT);
				label.setVerticalTextPosition(SwingConstants.BOTTOM);
				label.setVerticalAlignment(SwingConstants.BOTTOM);
			}

			// install mouse listeners
			MouseListener browserLauncher = new MouseListener()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					JDesktopAccess.executeBrowseApplication("http://www.maerivoet.org/");
				} 
				@Override
				public void mouseEntered(MouseEvent e)
				{
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} 
				@Override
				public void mouseExited(MouseEvent e)
				{
					setCursor(Cursor.getDefaultCursor());							
				}
				@Override
				public void mousePressed(MouseEvent e) { }
				@Override
				public void mouseReleased(MouseEvent e) { } 
			};

			MouseListener emailLauncher = new MouseListener()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					JDesktopAccess.executeMailApplication("sven.maerivoet@gmail.com","Request for information on JDerivedGUIApplication");
				}
				@Override
				public void mouseEntered(MouseEvent e)
				{
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				@Override
				public void mouseExited(MouseEvent e)
				{
					setCursor(Cursor.getDefaultCursor());
				}
				@Override
				public void mousePressed(MouseEvent e) { }
				@Override
				public void mouseReleased(MouseEvent e) { }
			};

			affiliationsLabels.get(0).addMouseListener(browserLauncher);
			affiliationsLabels.get(2).addMouseListener(emailLauncher);
			affiliationsLabels.get(3).addMouseListener(browserLauncher);

			return affiliationsLabels;
		}
	}

	/*****************
	 * INNER CLASSES *
	 *****************/

	/**
	 * @author  Sven Maerivoet
	 * @version 02/02/2013
	 */
	private class MyTaskExecutor extends TaskExecutor
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
			super.finishTasks();
			int result = 0;
			for (ATask task : getTasks()) {
				result += ((MyTask) task).getResult();
			}
			JMessageDialog.show(fMainWindow,I18NL10N.translate("text.TaskCompleted",String.valueOf(result)));
		}
	}

	/**
	 * @author  Sven Maerivoet
	 * @version 02/02/2013
	 */
	private class MyTask extends ATask
	{
		// internal datastructures
		private int fResult;

		/****************
		 * CONSTRUCTORS *
		 ****************/

		/**
		 */
		public MyTask()
		{
		}

		/******************
		 * PUBLIC METHODS *
		 ******************/

		/**
		 */
		public int getResult()
		{
			return fResult;
		}

		/*********************
		 * PROTECTED METHODS *
		 *********************/

		/**
		 */
		@Override
		protected void executeTask()
		{
			fResult = (int) (Math.random() * 100.0);
			Chrono.wait(100);
		}

		/**
		 */
		@Override
		protected void finishTask()
		{
		}
	}
}
