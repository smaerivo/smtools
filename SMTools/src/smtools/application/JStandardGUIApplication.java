// --------------------------------------------
// Filename      : JStandardGUIApplication.java
// Author        : Sven Maerivoet
// Last modified : 04/12/2012
// Target        : Java VM (1.6)
// --------------------------------------------

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

package smtools.application;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.apache.log4j.*;
import smtools.application.registry.*;
import smtools.application.util.*;
import smtools.exceptions.*;
import smtools.math.*;
import smtools.miscellaneous.*;
import smtools.swing.dialogs.*;
import smtools.swing.util.*;

/**
 * The <CODE>JStandardGUIApplication</CODE> class provides a standard Swing based GUI framework.
 * <P>
 * By default, the <CODE>JStandardGUIApplication</CODE> class will result in the following GUI:
 * <P>
 * <UL>
 *   <IMG src="doc-files/standard-gui.png">
 * </UL>
 * <P>
 * Typically, this <CODE>JStandardGUIApplication</CODE> is subclassed, with several methods overridden.
 * These methods control the application's settings, the visual layout of its GUI, and the actions that need to be
 * taken upon user input. 
 * <P>
 * The overridable methods that define the GUI's form and behavior are:
 * <P>
 * <UL>
 *   <LI><B><U>Java runtime environment version checking</U></B></LI>
 *   <P>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#getRequiredMajorJavaVersion()}</LI>
 *     <LI>{@link JStandardGUIApplication#getRequiredMinorJavaVersion()}</LI>
 *   </UL>
 *   <P>
 *   <LI><B><U>Command-line parameter parsing</U></B></LI>
 *   <P>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#parseParameter(int,String)}</LI>
 *     <BR />
 *     Already built-in command-line parameters are:
 *     <UL>
 *       <LI>-<B>locale</B>=<I>dutch</I>|<I>ukenglish</I>|<I>usenglish</I> (default is <I>ukenglish</I>)</LI>
 *       <LI>-<B>developmode</B></LI>
 *       <LI>-<B>width</B>=<I>autosize</I>|<I>fullscreen</I>|&lt;<I>number</I>&gt; (default is <I>autosize</I>)</LI>
 *       <LI>-<B>height</B>=<I>autosize</I>|<I>fullscreen</I>|&lt;<I>number</I>&gt; (default is <I>autosize</I>)</LI>
 *       <LI>-<B>silent</B></LI>
 *       <LI>-<B>help</B></LI>
 *     </UL>
 *     <I>Note that the application is sized to fullscreen if either width or height or set to reflect this.</I>
 *   </UL>
 *   <P>
 *   <LI><B><U>Custom initialisation and clean-up</U></B></LI>
 *   <P>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#getApplicationResourceArchiveFilename()}</LI>
 *     <LI>{@link JStandardGUIApplication#getApplicationLocalePrefix()}</LI>
 *     <LI>{@link JStandardGUIApplication#initialiseClass(Object[])}</LI>
 *     <LI>{@link JStandardGUIApplication#postInitialise()}</LI>
 *     <LI>{@link JStandardGUIApplication#shutdown()}</LI>
 *   </UL>
 *   <P>
 *   <LI><B><U>Splash screen during startup</U></B></LI>
 *   <P>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#getSplashScreen()}</LI>
 *     <LI>{@link JStandardGUIApplication#getSplashScreenContent()}</LI>
 *     <LI>{@link JStandardGUIApplication#getSplashScreenSound()} [<I>see also {@link JSplashScreen} and {@link MP3Player}</I>]</LI>
 *   </UL>
 *   <P>
 *   <LI><B><U>Visual layout (window related)</U></B></LI>
 *   <P>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#getInitialLookAndFeel()}</LI>
 *     <LI>{@link JStandardGUIApplication#getInitialGUISize()}</LI>
 *     <LI>{@link JStandardGUIApplication#isGUIResizable()}</LI>
 *     <LI>{@link JStandardGUIApplication#isGUIRepaintedWhenResizing()}</LI>
 *     <LI>{@link JStandardGUIApplication#getIcon()}</LI>
 *     <LI>{@link JStandardGUIApplication#getWindowTitle()}</LI>
 *   </UL>
 *   <P>
 *   <LI><B><U>Visual layout (content related)</U></B></LI>
 *   <P>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#constructContentPane(JPanel)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructMenus()}</LI>
 *     <LI>{@link JStandardGUIApplication#constructRightHandMenu()}</LI>
 *     <LI>{@link JStandardGUIApplication#getStatusBar()}</LI>
 *     <LI>{@link JStandardGUIApplication#isStatusBarEnabled()}</LI>
 *     <LI>{@link JStandardGUIApplication#isClockEnabled()}</LI>
 *     <LI>{@link JStandardGUIApplication#getAboutBox()} [<I>see also {@link JAboutBox}</I>]</LI>
 *   </UL>
 *   <P>
 *   <LI><B><U>Reacting to user input</U></B></LI>
 *   <P>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#actionPerformed(ActionEvent)}</LI>
 *     <LI>{@link JStandardGUIApplication#windowResized()}</LI>
 *   </UL>
 * </UL>
 * <P>
 * Note that by default, a "<I>General</I>" menu is present, which looks as follows:
 * <P>
 * <UL>
 *   <IMG src="doc-files/standard-menu.png">
 * </UL>
 * <P>
 * Note that if the underlying operating system allows for minimisation to the system tray,
 * then this options becomes available in the menu.
 * <P>
 * As the application is ran, the global system {@link Registry} is read from the file <CODE>system-registry.ser</CODE>
 * (and stored back to file at the end). The user registry is also loaded from the file <CODE>application-registry.ser</CODE> if it exists.
 * <P>
 * When the user wants to quit the application, a confirmation dialog is shown:
 * <P>
 * <UL>
 *   <IMG src="doc-files/quit-dialog.png">
 * </UL>
 * <P>
 * Note that this confirmation can be skipped if {@link JDevelopMode#isActivated} is <CODE>true</CODE>.
 * 
 * @author  Sven Maerivoet
 * @version 04/12/2012
 */
public class JStandardGUIApplication extends JFrame implements ActionListener, ComponentListener, WindowListener
{
	// the different window-sizes
	/**
	 * Useful constant to specify that the GUI should fit exactly around its components.
	 */
	protected static final int kAutoSizeGUI = -1;

	/**
	 * Useful constant to specify that the GUI should have full screen size.
	 * <P>
	 * Note that the <B>maximised state</B> of a window is currently not yet supported due to Java lack thereof.
	 */
	protected static final int kFullScreenGUI = 0;

	// the different look-and-feels
	/**
	 * Useful constant for specifying the Mac OS X look-and-feel.
	 */
	protected static final String klafMac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";

	/**
	 * Useful constant for specifying Java's Metal look-and-feel.
	 */
	protected static final String klafMetal = "javax.swing.plaf.metal.MetalLookAndFeel";

	/**
	 * Useful constant for specifying the Motif look-and-feel.
	 */
	protected static final String klafMotif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

	/**
	 * Useful constant for specifying Java's Nimbus look-and-feel.
	 */
	protected static final String klafNimbus = "javax.swing.plaf.nimbus.NimbusLookAndFeel";

	/**
	 * Useful constant for specifying the Microsoft Windows look-and-feel.
	 */
	protected static final String klafWindows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	/**
	 * Useful constant for specifying the current platform's look-and-feel.
	 */
	protected static final String klafSystem = "platform-dependent";

	/*
	 * Access point to the application's own resources.
	 */
	protected JARResources fResources;

	// access point to the Log4j logging facility
	private static final Logger kLogger = Logger.getLogger(JStandardGUIApplication.class.getName());

	// the required Java version for the SMTools package
	private static final int kRequiredMajorJavaVersion = 1;
	private static final int kRequiredMinorJavaVersion = 6;

	// the location of the JAR archive containing all the resources
	private static final String kResourceArchiveFilename = "smtools.jar";

	// the location of the system locale databasse
	private static final String kSystemLocalePrefix = "smtools-resources/locales/locale-";

	// default window title
	private static final String kDefaultWindowTitle = "smtools.application.JStandardGUIApplication";

	// system registry filename
	private static final String kSystemRegistryFilename = "system-registry.ser";

	// system registry filename
	private static final String kApplicationRegistryFilename = "application-registry.ser";

	// default application icon filename
	private static final String kDefaultApplicationIconFilename = "smtools-resources/images/icon.jpg";

	// the different parameters
	private static final String kParamLocale = "LOCALE";
	private static final String kParamLocaleDutch = "DUTCH";
	private static final String kParamLocaleBritishEnglish = "UKENGLISH";
	private static final String kParamLocaleAmericanEnglish = "USENGLISH";
	private static final String kParamDevelopMode = "DEVELOPMODE";
	private static final String kParamWidth = "WIDTH";
	private static final String kParamHeight = "HEIGHT";
	private static final String kParamGUIFullScreen = "FULLSCREEN";
	private static final String kParamGUIAutoSize = "AUTOSIZE";
	private static final String kParamSilent = "SILENT";
	private static final String kParamHelp = "HELP";

	// the default initial look-and-feel
	private static final String klafInitialLaF = klafSystem;

	// the action commands for the default menu
	private static final String kActionCommandMenuItemAbout = "menuItem.About";
	private static final String kActionCommandMenuItemMacLAF = "menuItem.MacLAF";
	private static final String kActionCommandMenuItemMetalLAF = "menuItem.MetalLAF";
	private static final String kActionCommandMenuItemMinimiseToSystemTray = "menuItem.MinimiseToSystemTray";
	private static final String kActionCommandMenuItemMotifLAF = "menuItem.MotifLAF";
	private static final String kActionCommandMenuItemNimbusLAF = "menuItem.NimbusLAF";
	private static final String kActionCommandMenuItemQuit = "menuItem.Quit";
	private static final String kActionCommandMenuItemSystemLAF = "menuItem.SystemLAF";
	private static final String kActionCommandMenuItemWindowsLAF = "menuItem.WindowsLAF";

	// set the status bar miscellaneous text's update period to ten seconds
	private static final int kStatusBarUpdatePeriod = 10000;

	// set the clock's update period to half a second
	private static final int kClockUpdatePeriod = 500;

	// switch for first-time initialisation of the system registry
	private static final boolean kSaveSystemRegistry = false;

	// internal datastructures
	private Registry fSystemRegistry;
	private int fGUIWidth;
	private int fGUIHeight;
	private String fCurrentLAF;
	private JRadioButtonMenuItem frbMac;
	private JRadioButtonMenuItem frbMetal;
	private JRadioButtonMenuItem frbMotif;
	private JRadioButtonMenuItem frbNimbus;
	private JRadioButtonMenuItem frbWindows;
	private JStatusBar fStatusBar;
	private JLabel fClockLabel;
	private String fLocale;
	private JSplashScreen fSplashScreen;
	private Image fIcon;
	private JGUIComponentCache fGUIComponentCache;
	private int fAboutBoxID;
	private boolean fMinimiseToSystemTray;
	private TrayIcon fTrayIcon;

	/*************************
	 * STATIC INITIALISATION *
	 *************************/

	static {
		PropertyConfigurator.configure("log4j.properties");
	}

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JStandardGUIApplication</CODE> object.
	 * <P>
	 * During construction of the GUI, the following events take place:
	 * <P>
	 * <UL>
	 *   <LI><I>The required version of the Java runtime engine is checked </I> (see {@link JStandardGUIApplication#getRequiredMajorJavaVersion()} and
	 *       {@link JStandardGUIApplication#getRequiredMinorJavaVersion()}).</LI>
	 *   <P>
	 *   <LI>The system {@link JARResources} are loaded.</LI>
	 *   <P>
	 *   <LI><I>The GUI is set to auto-size by default</I> (see {@link JStandardGUIApplication#getInitialGUISize()} and
	 *       {@link JStandardGUIApplication#isGUIResizable()}).</LI>
	 *   <P>
	 *   <LI>British English is the default language used.</LI>
	 *   <P>
	 *   <LI><I>The command-line parameters are parsed </I> (see {@link JStandardGUIApplication#parseParameter(int,String)}).</LI>
	 *   <P>
	 *   <LI>The system's locale {@link I18NL10N} database is loaded.</LI>
	 *   <P>
	 *   <LI>The application's {@link JARResources} and locale {@link I18NL10N} database are loaded (if they are present).</LI>
	 *   <P>
	 *   <LI>The global system {@link Registry} is read from file.</LI>
	 *   <P>
	 *   <LI>The GUI's component cache is initialised (see {@link JGUIComponentCache}).</LI>
	 *   <P>
	 *   <LI>The look-and-feel of the operating system is used by default.</LI>
	 *   <P>
	 *   <LI><I>A optional splash screen is shown</I> (see {@link JStandardGUIApplication#getSplashScreenContent()} and
	 *       {@link JStandardGUIApplication#getSplashScreenSound()}).</LI>
	 *   <P>
	 *   <LI><I>Custom initialisation is performed</I> (see {@link JStandardGUIApplication#initialiseClass(Object[])}).</LI>
	 *   <P>
	 *   <LI><I>The window's icon and title are set</I> (see {@link JStandardGUIApplication#getIcon()} and
	 *       {@link JStandardGUIApplication#getWindowTitle()}).</LI>
	 *   <P>
	 *   <LI><I>The GUI's content pane is constructed</I> (see {@link JStandardGUIApplication#constructContentPane(JPanel)}).</LI>
	 *   <P>
	 *   <LI><I>The GUI's menu bar is constructed</I> (see {@link JStandardGUIApplication#constructMenus()} and
	 *       {@link JStandardGUIApplication#constructRightHandMenu()}).</LI>
	 *   <P>
	 *   <LI><I>The about box is shown</I> (see {@link JStandardGUIApplication#getAboutBox()}).</LI>
	 *   <P>
	 *   <LI>Post initialisation is performed as the GUI is fully constructed</I> (sse {@link JStandardGUIApplication#postInitialise()}).</LI>
	 * </UL>
	 * <P>
	 * <B>The items in <I>italic</I> can be influenced in a derived subclass.</B>
	 * <P>
	 * If no parameters are to be passed to the GUI, specify <CODE>null</CODE>
	 * for <CODE>parameters</CODE>.
	 *
	 * @param argv       an array of strings containing the <B>command-line</B> parameters
	 * @param parameters an array of objects containing the parameters to be passed to the GUI's
	 *                   {@link JStandardGUIApplication#initialiseClass(Object[])} method
	 */
	public JStandardGUIApplication(String[] argv, Object[] parameters)
	{
		// check Java runtime version requirements
		kLogger.info("Checking Java runtime environment requirements...");
		String javaRuntimeVersion = System.getProperty("java.version");
		int dotPosition = javaRuntimeVersion.indexOf(".");
		int javaMajorVersion = (Integer.valueOf(javaRuntimeVersion.substring(dotPosition - 1,dotPosition))).intValue();
		int javaMinorVersion = (Integer.valueOf(javaRuntimeVersion.substring(dotPosition + 1,dotPosition + 2))).intValue();
		String requiredJavaRuntimeVersion = getRequiredMajorJavaVersion() + "." + getRequiredMinorJavaVersion();
		if ((javaMajorVersion < getRequiredMajorJavaVersion()) ||
				((javaMajorVersion >= getRequiredMajorJavaVersion()) && (javaMinorVersion < getRequiredMinorJavaVersion()))) {
			abortApplication("Current version of Java runtime environment (" +
				javaRuntimeVersion +
				") is incompatible with requirements (" +
				requiredJavaRuntimeVersion +
				"); application aborted.");
		}

		// load JAR archive containing the system resources
		try {
			kLogger.info("Loading system resources...");
			JARResources.fSystemResources = new JARResources(kResourceArchiveFilename);
		}
		catch (FileDoesNotExistException exc) {
			abortApplication("Archive (" + kResourceArchiveFilename + ") containing system resources not found; application aborted.");
		}
		catch (FileReadException exc) {
			abortApplication("Error while processing archive (" + kResourceArchiveFilename + ") containing system resources; application aborted.");
		}

		kLogger.info("Starting application...");

		fGUIWidth = kAutoSizeGUI;
		fGUIHeight = kAutoSizeGUI;

		Dimension initialGUISize = getInitialGUISize();
		if (initialGUISize != null) {
			fGUIWidth = initialGUISize.width;
			fGUIHeight = initialGUISize.height;
		}

		fLocale = I18NL10N.kLocaleBritishEnglish;
		parseCommandLine(argv);

		// load the system's locale database
		kLogger.info("Loading system locale database...");
		try {
			I18NL10N.load(JARResources.fSystemResources.getInputStream(I18NL10N.getFilename(kSystemLocalePrefix,fLocale)));
		}
		catch (FileDoesNotExistException exc) {
			abortApplication("System locale database (" + kSystemLocalePrefix + " + " + fLocale + ") not found; application aborted.");
		}
		catch (FileReadException exc) {
			abortApplication("Error while processing system locale database (" + kSystemLocalePrefix + " + " + fLocale + "); application aborted.");
		}

		// load application's resources
		String applicationResourceArchiveFilename = getApplicationResourceArchiveFilename(); 
		if (applicationResourceArchiveFilename != null) {
			try {
				kLogger.info(I18NL10N.translate("text.LoadingApplicationResources"));
				fResources = new JARResources(applicationResourceArchiveFilename);
			}
			catch (FileDoesNotExistException exc) {
				abortApplication(I18NL10N.translate("error.ApplicationResourcesArchiveNotFound",applicationResourceArchiveFilename),true);
			}
			catch (FileReadException exc) {
				abortApplication(I18NL10N.translate("error.ApplicationResourcesArchiveProcessing",applicationResourceArchiveFilename),true);
			}
		}

		// load application's locale database
		String applicationLocalePrefix = getApplicationLocalePrefix();
		if (applicationLocalePrefix != null) {
			try {
				kLogger.info(I18NL10N.translate("text.LoadingApplicationLocaleDatabase"));
				I18NL10N.load(fResources.getInputStream(I18NL10N.getFilename(applicationLocalePrefix,fLocale)));
			}
			catch (FileDoesNotExistException exc) {
				abortApplication(I18NL10N.translate("error.ApplicationLocaleDatabaseNotFound",applicationLocalePrefix + " + " + fLocale),true);
			}
			catch (FileReadException exc) {
				abortApplication(I18NL10N.translate("error.ApplicationLocaleDatabaseProcessing",applicationLocalePrefix + " + " + fLocale),true);
			}
		}

		// obtain a reference to the system registry
		fSystemRegistry = Registry.getInstance();

		// load (deserialise) the system registry
		kLogger.info(I18NL10N.translate("text.LoadingSystemRegistryHives"));
		try {
			fSystemRegistry.load(kSystemRegistryFilename);
		}
		catch (RegistryException exc) {
			abortApplication(exc.getRegistryError(),true);
		}

		// load (deserialise) the application's registry
		File file = new File(kApplicationRegistryFilename);
		if (file.exists()) {
			try {
				kLogger.info(I18NL10N.translate("text.LoadingApplicationRegistryHives"));

				// automatically join both system and application registries
				fSystemRegistry.load(kApplicationRegistryFilename);
			}
			catch (RegistryException exc) {
				abortApplication(exc.getRegistryError(),true);
			}
		}

		// initialise GUI component cache
		kLogger.info(I18NL10N.translate("text.InitialiseGUIComponentCache"));
		fGUIComponentCache = new JGUIComponentCache();

		// make the contents of windows dynamic (e.g., resizing background images, ...)
		if (isGUIRepaintedWhenResizing() && ((boolean) Toolkit.getDefaultToolkit().getDesktopProperty("awt.dynamicLayoutSupported"))) {
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
		}

		// set the look-and-feel to that of the platform
		kLogger.info(I18NL10N.translate("text.SettingGUILookAndFeel"));
		setInitialLookAndFeel(getInitialLookAndFeel());
		setDefaultLookAndFeelDecorated(true);

		// activate the splash screen
		fSplashScreen = new JSplashScreen(getSplashScreenContent(),getSplashScreenSound());

		// change the cursor to indicate that the user has to wait
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		setTitle(kDefaultWindowTitle);

		// allow custom initialisation
		kLogger.info(I18NL10N.translate("text.PerformingCustomInitialisation"));
		initialiseClass(parameters);

		kLogger.info(I18NL10N.translate("text.CreatingGUIComponents"));

		// add about box to the GUI component cache
		getSplashScreen().setStatusMessage(I18NL10N.translate("text.CachingAboutBox"));
		JDefaultDialog aboutBox = getAboutBox();
		fAboutBoxID = fGUIComponentCache.addComponent(aboutBox);

		getSplashScreen().setStatusMessage(I18NL10N.translate("text.ConstructingGUI"));

		// load the application's icon
		fIcon = getIcon();
		if (fIcon != null) {
			setIconImage(fIcon);
		}

		setTitle(getWindowTitle());
		setResizable(isGUIResizable());

		// setup content pane
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());

		// if necessary, add the status bar
		if (isStatusBarEnabled()) {
			fStatusBar = new JStatusBar(isGUIResizable());
			contentPane.add(fStatusBar,BorderLayout.SOUTH);

			// create a Swing timer to periodically update the status bar miscellaneous text
			Action updateStatusBarAction = new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
					updateStatusBarMiscellaneousText();
				}
			};
			new javax.swing.Timer(kStatusBarUpdatePeriod,updateStatusBarAction).start();

			// perform the first update when the GUI is displayed
			updateStatusBarMiscellaneousText();
		}

			JPanel embeddedContentPane = new JPanel();
			constructContentPane(embeddedContentPane);
		contentPane.add(embeddedContentPane,BorderLayout.CENTER);

		setContentPane(contentPane);

		// if necessary, add the clock
		if (isClockEnabled()) {

			fClockLabel = new JLabel("",SwingConstants.RIGHT);

			// create a Swing timer to periodically update the clock label
			Action updateClockPanelAction = new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
					updateCurrentTimeLabel();
				}
			};
			new javax.swing.Timer(kClockUpdatePeriod,updateClockPanelAction).start();

			// perform the first update when the GUI is displayed
			updateCurrentTimeLabel();
		}

		// setup menu bar
		JMenuBar menuBar = new JMenuBar();
		constructMenuBar(menuBar);
		setJMenuBar(menuBar);

		// we will handle the window-actions ourselves
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);

		pack();

		// determine the requested GUI's screensize
		if (fGUIWidth == kAutoSizeGUI) {
			// fit tightly around all the components' natural widths
			fGUIWidth = getSize().width;
		}
		if (fGUIHeight == kAutoSizeGUI) {
			// fit tightly around all the components' natural heights
			fGUIHeight = getSize().height;
		}

		// determine the GUI's maximum screensize
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
		boolean fullScreenGUISelected = ((fGUIWidth == kFullScreenGUI) || (fGUIHeight == kFullScreenGUI));
		if (fullScreenGUISelected) {
			// take into account all the space that a possible OS taskbar tasks
			fGUIWidth = (int) Math.round(screenSize.getWidth() - screenInsets.left - screenInsets.right);
			fGUIHeight = (int) Math.round(screenSize.getHeight() - screenInsets.top - screenInsets.bottom);
		}

		setSize(new Dimension(fGUIWidth,fGUIHeight));

		if (fullScreenGUISelected) {
			if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
				setExtendedState(Frame.MAXIMIZED_BOTH);
			}
		}

		if (fSplashScreen.isAvailable()) {
			fSplashScreen.setVisible(false);
			fSplashScreen.dispose();
		}

		// install the callback method for when the window is resized
		addComponentListener(this);

		setVisible(true);

		// restore the cursor
		setCursor(Cursor.getDefaultCursor());

		// update the menu's radio buttons
		setLookAndFeelMenuItems();

		// check whether or not minimising to the system tray is supported
		fMinimiseToSystemTray = SystemTray.isSupported();

		// show the aboutbox
		aboutBox = (JDefaultDialog) fGUIComponentCache.retrieveComponent(fAboutBoxID);

		kLogger.info(I18NL10N.translate("text.ApplicationReady"));

		if ((!JDevelopMode.isActivated()) && (aboutBox != null)) {
			aboutBox.activate();
		}

		// allow for custom post initialisation
		postInitialise();
	}
	
	/******************
	 * PUBLIC METHODS *
	 ******************/

	// the action-listener
	/**
	 * The GUI's <B>action listener</B>.
	 * <P>
	 * Note that when overriding this method in a subclass, its parent should
	 * explicitly be called in order to guarantee the correct default processing of
	 * the user's input:
	 * <P>
	 * <CODE>
	 * <PRE>
	 *   super.actionPerformed(e);
	 *   // rest of method's code
	 * </PRE>
	 * </CODE>
	 *
	 * @param e the <CODE>ActionEvent</CODE> that is received
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		if (command.startsWith("menuItem")) {
			MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSMenuItem,MP3Player.EPlaying.kBlocked);
		}

		if (command.equalsIgnoreCase(kActionCommandMenuItemAbout)) {

			JDefaultDialog aboutBox = (JDefaultDialog) fGUIComponentCache.retrieveComponent(fAboutBoxID);

			if (aboutBox != null) {
				aboutBox.activate();
			}
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemMacLAF)) {
			setLookAndFeel(klafMac,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemMetalLAF)) {
			setLookAndFeel(klafMetal,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemMotifLAF)) {
			setLookAndFeel(klafMotif,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemNimbusLAF)) {
			setLookAndFeel(klafNimbus,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemWindowsLAF)) {
			setLookAndFeel(klafWindows,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemSystemLAF)) {
			setLookAndFeel(klafSystem,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemMinimiseToSystemTray)) {
			fMinimiseToSystemTray = !fMinimiseToSystemTray; 
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemQuit)) {
			windowClosing(null);
		}
	}

	// the component-listener
	/**
	 * A method from the dialog box's <B>component listener</B>.
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public void componentMoved(ComponentEvent e)
	{
	}

	/**
	 * A method from the dialog box's <B>component listener</B>.
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public void componentShown(ComponentEvent e)
	{
	}

	/**
	 * A method from the dialog box's <B>component listener</B>.
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public void componentHidden(ComponentEvent e)
	{
	}

	/**
	 * A method from the dialog box's <B>component listener</B>.
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public void componentResized(ComponentEvent e)
	{
		windowResized();
	}

	// the window-listener
	/**
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowActivated(WindowEvent e)
	{
	}

	/**
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowClosed(WindowEvent e)
	{
	}

	/**
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowClosing(WindowEvent e)
	{
		if (!JDevelopMode.isActivated()) {

			if (JConfirmationDialog.confirm(this,I18NL10N.translate("text.ConfirmExitApplication"))) {

				kLogger.info(I18NL10N.translate("text.RunningApplicationShutdownSequence"));
				shutdown();

				kLogger.info(I18NL10N.translate("text.SavingRegistryHives"));
				saveRegistry();

				// quit the running application
				kLogger.info(I18NL10N.translate("text.ApplicationTerminated"));
				System.exit(0);
			}
		}
		else {
			kLogger.info(I18NL10N.translate("text.RunningApplicationShutdownSequence"));
			shutdown();

			kLogger.info(I18NL10N.translate("text.SavingRegistryHives"));
			saveRegistry();

			// quit the running application
			kLogger.info(I18NL10N.translate("text.ApplicationTerminated"));
			System.exit(0);
		}
	}

	/**
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowDeactivated(WindowEvent e)
	{
	}

	/**
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowDeiconified(WindowEvent e)
	{
		MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSWindowEvent);
	}

	/**
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowIconified(WindowEvent e)
	{
		MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSWindowEvent);
		if (fMinimiseToSystemTray) {
			fTrayIcon = new TrayIcon(fIcon,getWindowTitle(),null);
			fTrayIcon.setImageAutoSize(true);

			MouseListener iconListener = new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setVisible(true);
					SystemTray.getSystemTray().remove(fTrayIcon);
				}
				@Override
				public void mouseEntered(MouseEvent e) { }
				@Override
				public void mouseExited(MouseEvent e) { }
				@Override
				public void mousePressed(MouseEvent e) { }
				@Override
				public void mouseReleased(MouseEvent e) { }
			};
			fTrayIcon.addMouseListener(iconListener);

			try {
				SystemTray.getSystemTray().add(fTrayIcon);
				fTrayIcon.displayMessage(getWindowTitle(),I18NL10N.translate("text.RestoreFromSystemTray"),TrayIcon.MessageType.INFO);
				setVisible(false);
			}
			catch (AWTException exc) {
				kLogger.error(I18NL10N.translate("error.MinimisingToSystemTray"));
			}
		}
	}

	/**
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowOpened(WindowEvent e)
	{
	}

	/**
	 * A callback method for when the GUI's window is resized.
	 */
	protected void windowResized()
	{
	}

	/**
	 * The application's <CODE>main</CODE> method.
	 * <P>
	 * Note that this method should be overridden by a derived subclass:
	 * <P>
	 * <CODE>
	 * <PRE>
	 *   public static void main(String[] argv)
	 *   {
	 *     DerivedGUIApplication derivedGUIApplication = new DerivedGUIApplication(argv);
	 *   }
	 * </PRE>
	 * </CODE>
	 *
	 * @param argv an array of strings containing the <B>command-line</B> parameters
	 */
	public static void main(String[] argv)
	{
		saveSystemRegistry();
		new JStandardGUIApplication(argv,null);
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * Returns the required major version of the Java runtime engine that wants to run this application.
	 * <P>
	 * <B>The default is 1 (for JRE 1.6.0).</B>
	 *
	 * @return the required major version of the Java runtime engine
	 */
	protected int getRequiredMajorJavaVersion()
	{
		return kRequiredMajorJavaVersion;
	}

	/**
	 * Returns the required minor version of the Java runtime engine that wants to run this application.
	 * <P>
	 * <B>The default is 6 (for JRE 1.6.0).</B>
	 *
	 * @return the required minor version of the Java runtime engine
	 */
	protected int getRequiredMinorJavaVersion()
	{
		return kRequiredMinorJavaVersion;
	}

	/**
	 * Returns the filename of the JAR or ZIP file containing the application's resources.
	 * <P>
	 * If <CODE>null</CODE> is specified, then the system ignores loading the application's resources.
	 *
	 * @return the filename of the archive (JAR or ZIP) containing the application's resources
	 * @see    smtools.application.util.JARResources
	 */
	protected String getApplicationResourceArchiveFilename()
	{
		return null;
	}

	/**
	 * Returns the path and prefix names to the application's locale databases.
	 * <P>
	 * If <CODE>null</CODE> is specified, then the system ignores loading the application's locale databases.
	 *
	 * @return the path to the location of the application's locale databases
	 * @see    smtools.application.util.I18NL10N
	 */
	protected String getApplicationLocalePrefix()
	{
		return null;
	}

	/**
	 * Allows custom initialisation of the subclass's member fields.
	 * <P>
	 * The parameters in the <CODE>Object[]</CODE> array are passed through
	 * the class's constructor (see {@link JStandardGUIApplication#JStandardGUIApplication(String[],Object[])}).
	 *
	 * @param parameters an array of <CODE>Objects</CODE>
	 */
	protected void initialiseClass(Object[] parameters)
	{
	}

	/**
	 * Allows post initialisation (i.e., when the GUI is fully constructed).
	 */
	protected void postInitialise()
	{
	}

	/**
	 * Allows custom clean-up when the application is shutting down.
	 * <P>
	 * <I>Note that the <CODE>shutdown()</CODE> sequence is not ran in case of a fatal error resulting in early abort.</I>
	 *
	 * @see JStandardGUIApplication#abortApplication(String,boolean...)
	 */
	protected void shutdown()
	{
	}

	/**
	 * Allows parsing of custom command-line parameters.
	 * <P>
	 * A derived subclass typically overrides this method to parse all its custom command-line parameters.
	 * <P>
	 * Note that if an error occurs during parsing, the {@link JStandardGUIApplication#showParameterWarning(int,String,String)}
	 * method should be called with the appropriate parameters.
	 * <P>
	 * The following example shows the parsing of one custom parameter (with a custom option):
	 * <CODE>
	 * <PRE>
	 *   final String kCustomParameter = "PARAMETER";
	 *   final String kCustomOption    = "OPTION";
	 *
	 *   String upperCaseParameter = parameter.toUpperCase();
	 *
	 *   // parse parameter
	 *   if (upperCaseParameter.startsWith(kCustomParameter &#43; "=")) {
	 *
	 *     String upperCaseOption = upperCaseParameter.substring(kCustomParameter.length() &#43; 1);
	 *     // parse option
	 *     if (upperCaseOption.equalsIgnoreCase(kCustomOption)) {
	 *       // take action as parameter is parsed
	 *     }
	 *     else {
	 *       showParameterWarning(paramNr,parameter,"not a valid option");
	 *     }
	 *
	 *     // indicate that parameter was valid
	 *     return true;
	 *   }
	 *   else {
	 *
	 *     // indicate that parameter is unknown
	 *     return false;
	 *   }
	 * </PRE>
	 * </CODE>
	 *
	 * @param  paramNr   the number of the parameter that is being parsed
	 * @param  parameter the unmodified parameter as specified on the command-line
	 * @return           <CODE>true</CODE> if the parameter was parsed successfully, <CODE>false</CODE> otherwise
	 * @see    JStandardGUIApplication#showParameterWarning(int,String,String)
	 */
	protected boolean parseParameter(int paramNr, String parameter)
	{
		return false;
	}

	/**
	 * Logs a textual warning message.
	 *
	 * @param paramNr   the number of the parameter that failed to get parsed
	 * @param parameter the unmodified parameter as specified on the command-line
	 * @param message   the warning message to log
	 * @see   JStandardGUIApplication#parseParameter(int,String)
	 */
	protected final void showParameterWarning(int paramNr, String parameter, String message)
	{
		kLogger.warn(
			"Warning: parameter #" + String.valueOf(paramNr) + " (" + parameter + ") ignored.\n" +
			"\t => " + message + ".");
	}

	/**
	 * Returns a <CODE>JLabel</CODE> containing the splash screen's custom content.
	 * <P>
	 * See {@link JSplashScreen} for more information regarding the splash screen.
	 *
	 * @return a <CODE>JLabel</CODE> containing the splash screen's custom content
	 * @see    smtools.swing.util.JSplashScreen
	 */
	protected JLabel getSplashScreenContent()
	{
		return null;
	}

	/**
	 * Returns the MP3 sound to play during the splash screen.
	 * <P>
	 * See {@link JSplashScreen} for more information regarding the splash screen and {@link MP3Player} for playing MP3 sounds.
	 *
	 * @return the MP3 sound to play during the splash screen
	 * @see    smtools.swing.util.JSplashScreen
	 * @see    smtools.miscellaneous.MP3Player
	 */
	protected InputStream getSplashScreenSound()
	{
		return null;
	}

	/**
	 * Returns a handle to the GUI's splash screen.
	 *
	 * @return a handle to the GUI's splash screen
	 * @see    smtools.swing.util.JSplashScreen
	 */
	protected final JSplashScreen getSplashScreen()
	{
		return fSplashScreen;
	}

	/**
	 * Returns the application's initial look-and-feel.
	 * <P>
	 * The possible values that can be returned are:<BR />
	 * <BR />
	 * <UL>
	 *   <LI>the Mac OS X look-and-feel ({@link JStandardGUIApplication#klafMac})</LI>
	 *   <LI>Java's Metal look-and-feel ({@link JStandardGUIApplication#klafMetal})</LI>
	 *   <LI>the Motif look-and-feel ({@link JStandardGUIApplication#klafMotif})</LI>
	 *   <LI>Java's Nimbus look-and-feel ({@link JStandardGUIApplication#klafNimbus})</LI>
	 *   <LI>the Microsoft Windows look-and-feel ({@link JStandardGUIApplication#klafWindows})</LI>
	 *   <LI>the current platform's look-and-feel ({@link JStandardGUIApplication#klafSystem})</LI>
	 * </UL>
	 * <P>
	 * Note that this method returns the current platform's look-and-feel by default.
	 * 
	 * @return the application's initial look-and-feel
	 */
	protected String getInitialLookAndFeel()
	{
		return klafInitialLaF;
	}

	/**
	 * Returns the GUI's initial size on the screen.
	 * <P>
	 * A derived subclass should return a <CODE>Dimension</CODE> object containing the initial
	 * width and height of the GUI's window. Their values are expressed in pixels, but the following
	 * two special values are also accepted:
	 * <P>
	 * <UL>
	 *   <LI>{@link JStandardGUIApplication#kAutoSizeGUI}</LI>
	 *   <LI>{@link JStandardGUIApplication#kFullScreenGUI}</LI>
	 * </UL>
	 * <P>
	 * An example:
	 * <P>
	 * <UL>
	 *   <CODE>return (new Dimension(JStandardGUIApplication.kFullScreenGUI,250));</CODE>
	 * </UL>
	 * <P>
	 * which specifies a GUI with full screen width and 250 pixels height.
	 *
	 * @return the GUI's initial size on the screen as a <CODE>Dimension</CODE> object
	 */
	protected Dimension getInitialGUISize()
	{
		return null;
	}

	/**
	 * Returns whether or not the GUI's window should be resizable.
	 * <P>
	 * This method returns <CODE>true</CODE> by default.
	 *
	 * @return <CODE>true</CODE> if the GUI's window should be resizable, <CODE>false</CODE> if it should be fixed size
	 */
	protected boolean isGUIResizable()
	{
		return true;
	}

	/**
	 * Returns whether or not the GUI should always be repainted when the window is resized.
	 * This only works if the host platform supports it.
	 * <P>
	 * This method returns <CODE>true</CODE> by default.
	 *
	 * @return <CODE>true</CODE> if the GUI should be repainted when the window is resized
	 */
	protected boolean isGUIRepaintedWhenResizing()
	{
		return true;
	}

	/**
	 * Returns the <CODE>Image</CODE> containing the GUI's icon.
	 * <P>
	 * The <CODE>Image</CODE> should be a JPG-file, 32x32 pixels with 24-bit colourdepth (i.e., true colour).
	 *
	 * @return the <CODE>Image</CODE> containing the GUI's icon
	 */
	protected Image getIcon()
	{
		try {
			return JARResources.fSystemResources.getImage(kDefaultApplicationIconFilename);
		}
		catch (FileDoesNotExistException exc) {
			return null;
		}
	}

	/**
	 * Returns the GUI's window title.
	 *
	 * @return the GUI's window title
	 */
	protected String getWindowTitle()
	{
		return kDefaultWindowTitle;
	}

	/**
	 * Returns a handle to the GUI's internal component cache.
	 *
	 * @return a handle to the GUI's internal component cache
	 * @see    smtools.swing.util.JGUIComponentCache
	 */
	protected final JGUIComponentCache getGUIComponentCache()
	{
		return fGUIComponentCache;
	}

	/**
	 * Allows construction of the GUI's content pane.
	 * <P>
	 * A derived subclass typically overrides this method. The subclass can
	 * operate on the given <CODE>contentPane</CODE> parameter (which is initialised,
	 * i.e., not <CODE>null</CODE>, by default).
	 * <P>
	 * A subclass thus sets the content pane's layout managers, adds components to it, ...
	 *
	 * @param contentPane the GUI's main content pane to modify
	 */
	protected void constructContentPane(JPanel contentPane)
	{
	}

	/**
	 * Allows the construction of custom menus.
	 * <P>
	 * By default, the application already contains a "<I>General</I>" menu containing
	 * access to the optional about box, setting the GUI's look and feel and getting confirmation
	 * when the user wants to quit the application.
	 * <P>
	 * Note that this method returns <CODE>null</CODE> by default.
	 *
	 * @return an array of menus
	 * @see    JStandardGUIApplication#constructRightHandMenu()
	 */
	protected JMenu[] constructMenus()
	{
		return null;
	}

	/**
	 * Allows the construction of a custom right hand menu (e.g., a "<I>Help</I>" menu).
	 * <P>
	 * Note that this method returns <CODE>null</CODE> by default.
	 *
	 * @return a menu used in the right part of the menubar
	 * @see    JStandardGUIApplication#constructMenus()
	 */
	protected JMenu constructRightHandMenu()
	{
		return null;
	}

	/**
	 * Getter method for the status bar.
	 */
	protected JStatusBar getStatusBar()
	{
		return fStatusBar;
	}

	/**
	 * Returns whether or not the status bar should be shown at the bottom of the application's window.
	 * <P>
	 * Note that this method returns <CODE>true</CODE> by default.
	 *
	 * @return whether or not the status bar should be shown at the bottom of the application's window
	 */
	protected boolean isStatusBarEnabled()
	{
		return true;
	}

	/**
	 * Returns whether or not a clock (HH:MM:SS) should be shown at the right of the menubar.
	 * <P>
	 * Note that this method returns <CODE>true</CODE> by default.
	 *
	 * @return whether or not a clock (HH:MM:SS) should be shown at the right of the menubar
	 */
	protected boolean isClockEnabled()
	{
		return true;
	}

	/**
	 * Returns an object containing a custom about box.
	 * <P>
	 * Note that this method returns <CODE>null</CODE> by default, indicating that no
	 * about box is available.
	 *
	 * @return the about box dialog
	 * @see    smtools.swing.dialogs.JAboutBox
	 */
	protected JAboutBox getAboutBox()
	{
		return null;
	}

	/**
	 * Immediately aborts the running application.
	 *
	 * @param abortMessage the message to display when aborting
	 * @param appendLocale optional argument that, when set to <CODE>true</CODE>, appends a localised message that the application is aborted
	 */
	protected final void abortApplication(String abortMessage, boolean ... appendLocale)
	{
		if (abortMessage != null) {
			if ((appendLocale.length > 0) && (appendLocale[0])) {
				kLogger.fatal(abortMessage + "\n" + I18NL10N.translate("text.ApplicationAborted"));
			}
			else {
				kLogger.fatal(abortMessage);
			}
		}

		System.exit(0);
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 */
	private void setLookAndFeel(String lookAndFeel, boolean silent)
	{
		String previousLookAndFeel = fCurrentLAF;
		fCurrentLAF = translateLookAndFeelName(lookAndFeel);

		// change the look-and-feel
		try {
			UIManager.setLookAndFeel(fCurrentLAF);
			if (!silent) {
				MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSChangeLookAndFeel,MP3Player.EPlaying.kBlocked);
			}
			SwingUtilities.updateComponentTreeUI(this);
			fGUIComponentCache.updateComponentTreeUI();
			repaint();
			String message = I18NL10N.translate("text.GUILookAndFeelAdjusted",fCurrentLAF);
			kLogger.info(message);
			if (!silent) {
				JMessageDialog.show(this,message);
			}
		}
		catch (Exception exc) {
			String message = I18NL10N.translate("error.LAFNotFound",fCurrentLAF);
			kLogger.error(message);
			if (!silent) {
				JWarningDialog.warn(this,message);
			}
			fCurrentLAF = previousLookAndFeel;
		}

		setLookAndFeelMenuItems();
	}

	/**
	 */
	private void setLookAndFeelMenuItems()
	{
		frbMac.setSelected(false);
		frbMetal.setSelected(false);
		frbMotif.setSelected(false);
		frbNimbus.setSelected(false);
		frbWindows.setSelected(false);

		if (fCurrentLAF.equalsIgnoreCase(klafMac)) {
			frbMac.setSelected(true);
		}
		else if (fCurrentLAF.equalsIgnoreCase(klafMetal)) {
			frbMetal.setSelected(true);
		}
		else if (fCurrentLAF.equalsIgnoreCase(klafMotif)) {
			frbMotif.setSelected(true);
		}
		else if (fCurrentLAF.equalsIgnoreCase(klafNimbus)) {
			frbNimbus.setSelected(true);
		}
		else if (fCurrentLAF.equalsIgnoreCase(klafWindows)) {
			frbWindows.setSelected(true);
		}
	}

	/**
	 */
	private void setInitialLookAndFeel(String lookAndFeel)
	{
		try {
			fCurrentLAF = translateLookAndFeelName(lookAndFeel);
			UIManager.setLookAndFeel(fCurrentLAF);
		}
		catch (Exception exc) {
		}
	}

	/**
	 */
	private String translateLookAndFeelName(String lookAndFeel)
	{
		// if no look-and-feel was specified then the platform's look-and-feel is used
		if (lookAndFeel.equalsIgnoreCase(klafSystem)) {
			return UIManager.getSystemLookAndFeelClassName();
		}
		else {
			return lookAndFeel;
		}
	}

	/**
	 */
	private void constructMenuBar(JMenuBar menuBar)
	{
		JMenu menu = null;
		JMenu subMenu = null;
		JMenuItem menuItem = null;
		ButtonGroup buttonGroup = null;

		// the general menu
		menu = new JMenu(I18NL10N.translate("menu.General"));
		menu.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate("menu.General.Mnemonic")));
		menuBar.add(menu);

		JDefaultDialog aboutBox = (JDefaultDialog) fGUIComponentCache.retrieveComponent(fAboutBoxID);
		if (aboutBox != null) {
			menuItem = new JMenuItem(I18NL10N.translate(kActionCommandMenuItemAbout),
				I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemAbout + ".Mnemonic")));
			menuItem.setActionCommand(kActionCommandMenuItemAbout);
			menuItem.addActionListener(this);
			menuItem.setAccelerator(KeyStroke.getKeyStroke((int) 'A',java.awt.event.InputEvent.CTRL_DOWN_MASK));
			menu.add(menuItem);
		}

		// the look-and-feel submenu
		subMenu = new JMenu(I18NL10N.translate("menu.LookAndFeel"));
		subMenu.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate("menu.LookAndFeel.Mnemonic")));

		buttonGroup = new ButtonGroup();
		frbMetal = new JRadioButtonMenuItem(I18NL10N.translate(kActionCommandMenuItemMetalLAF));
		frbMetal.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemMetalLAF + ".Mnemonic")));
		frbMetal.setSelected(false);
		frbMetal.setActionCommand(kActionCommandMenuItemMetalLAF);
		frbMetal.addActionListener(this);
		buttonGroup.add(frbMetal);
		subMenu.add(frbMetal);

		frbNimbus = new JRadioButtonMenuItem(I18NL10N.translate(kActionCommandMenuItemNimbusLAF));
		frbNimbus.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemNimbusLAF + ".Mnemonic")));
		frbNimbus.setSelected(false);
		frbNimbus.setActionCommand(kActionCommandMenuItemNimbusLAF);
		frbNimbus.addActionListener(this);
		buttonGroup.add(frbNimbus);
		subMenu.add(frbNimbus);

		frbMotif = new JRadioButtonMenuItem(I18NL10N.translate(kActionCommandMenuItemMotifLAF));
		frbMotif.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemMotifLAF + ".Mnemonic")));
		frbMotif.setSelected(false);
		frbMotif.setActionCommand(kActionCommandMenuItemMotifLAF);
		frbMotif.addActionListener(this);
		buttonGroup.add(frbMotif);
		subMenu.add(frbMotif);

		frbWindows = new JRadioButtonMenuItem(I18NL10N.translate(kActionCommandMenuItemWindowsLAF));
		frbWindows.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemWindowsLAF + ".Mnemonic")));
		frbWindows.setSelected(false);
		frbWindows.setActionCommand(kActionCommandMenuItemWindowsLAF);
		frbWindows.addActionListener(this);
		buttonGroup.add(frbWindows);
		subMenu.add(frbWindows);

		frbMac = new JRadioButtonMenuItem(I18NL10N.translate(kActionCommandMenuItemMacLAF));
		frbMac.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemMacLAF + ".Mnemonic")));
		frbMac.setSelected(false);
		frbMac.setActionCommand(kActionCommandMenuItemMacLAF);
		frbMac.addActionListener(this);
		buttonGroup.add(frbMac);
		subMenu.add(frbMac);

		subMenu.addSeparator();

		menuItem = new JMenuItem(I18NL10N.translate(kActionCommandMenuItemSystemLAF),
				I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemSystemLAF + ".Mnemonic")));
		menuItem.setActionCommand(kActionCommandMenuItemSystemLAF);
		menuItem.addActionListener(this);
		subMenu.add(menuItem);

		if (SystemTray.isSupported()) {
			subMenu.addSeparator();

			JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(I18NL10N.translate(kActionCommandMenuItemMinimiseToSystemTray));
			checkBoxMenuItem.setState(true);
			checkBoxMenuItem.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemMinimiseToSystemTray + ".Mnemonic")));
			checkBoxMenuItem.setActionCommand(kActionCommandMenuItemMinimiseToSystemTray);
			checkBoxMenuItem.addActionListener(this);
			subMenu.add(checkBoxMenuItem);
		}

		menu.add(subMenu);

		menu.addSeparator();

		menuItem = new JMenuItem(I18NL10N.translate(kActionCommandMenuItemQuit),
				I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemQuit + ".Mnemonic")));
		menuItem.setActionCommand(kActionCommandMenuItemQuit);
		menuItem.addActionListener(this);
		menuItem.setAccelerator(KeyStroke.getKeyStroke((int) 'Q',java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menu.add(menuItem);

		// if necessary, add other menus
		JMenu[] otherMenus = constructMenus();
		if (otherMenus != null) {
			for (int menuNr = 0; menuNr < otherMenus.length; ++menuNr) {
				menuBar.add(otherMenus[menuNr]);
			}
		}

		// if necessary, add right hand menu
		boolean menuBarGlued = false;
		JMenu rightHandMenu = constructRightHandMenu();
		if (rightHandMenu != null) {
			menuBar.add(Box.createHorizontalGlue());
			menuBarGlued = true;
			menuBar.add(rightHandMenu);
		}

		// if necessary, add the clock
		if (isClockEnabled()) {
			if (!menuBarGlued) {
				menuBar.add(Box.createHorizontalGlue());
			}
			menuBar.add(fClockLabel);
		}
	}

	/**
	 */
	private void updateStatusBarMiscellaneousText()
	{
		double percentageFree = ((double) JMemoryStatistics.getFreeMemory() / (double) JMemoryStatistics.getTotalMemory()) * 100;

		fStatusBar.setMiscellaneousText(
			I18NL10N.translate("text.MemoryFree") + ": " +
			StringTools.convertDoubleToString(MathTools.convertBToMiB(JMemoryStatistics.getFreeMemory()),0) + " " +
			I18NL10N.translate("text.MiBAbbreviation") + " (" + StringTools.convertDoubleToString(percentageFree,0) + "%)");
	}

	/**
	 */
	private void updateCurrentTimeLabel()
	{
 		DateStamp currentDate = new DateStamp();
 		TimeStamp currentTime = new TimeStamp();
		fClockLabel.setText("[ " + currentDate.getDMYString() + " " + currentTime.getHMSString() + " ] "); // provide right-side padding
	}

	/**
	 */
	private void parseCommandLine(String[] argv)
	{
		if (argv.length > 0) {

			// parse parameters
			for (int paramNr = 1; paramNr <= argv.length; ++paramNr) {

				String parameter = argv[paramNr - 1];
				if (!parameter.startsWith("-")) {
					showParameterWarning(paramNr,parameter,"it doesn't start with a hyphen");
				}
				else {

					// remove the leading hypen and convert to upper case
					parameter = parameter.substring(1);
					String upperCaseParameter = parameter.toUpperCase();

					// parse parameters
					if (upperCaseParameter.startsWith(kParamLocale + "=")) {

						String option = upperCaseParameter.substring(kParamLocale.length() + 1);
						if (option.equalsIgnoreCase(kParamLocaleDutch)) {
							fLocale = I18NL10N.kLocaleDutch;
						}
						else if (option.equalsIgnoreCase(kParamLocaleBritishEnglish)) {
							fLocale = I18NL10N.kLocaleBritishEnglish;
						}
						else if (option.equalsIgnoreCase(kParamLocaleAmericanEnglish)) {
							fLocale = I18NL10N.kLocaleAmericanEnglish;
						}
						else {
							// user-specified locale
							fLocale = parameter.substring(kParamLocale.length() + 1);
						}
					}
					else if (upperCaseParameter.equalsIgnoreCase(kParamDevelopMode)) {
						JDevelopMode.activate();
					}
					else if (upperCaseParameter.startsWith(kParamWidth + "=")) {

						// note that this parameter overrides the settings in the (derived) class
						String option = upperCaseParameter.substring(kParamWidth.length() + 1);
						if (option.equalsIgnoreCase(kParamGUIFullScreen)) {
							fGUIWidth = kFullScreenGUI;
						}
						else if (option.equalsIgnoreCase(kParamGUIAutoSize)) {
							fGUIWidth = kAutoSizeGUI;
						}
						else {
							try {
								fGUIWidth = (new Integer(option)).intValue();
							}
							catch (NumberFormatException exc) {
								showParameterWarning(paramNr,parameter,"incorrect width specified");
							}
						}
					}
					else if (upperCaseParameter.startsWith(kParamHeight + "=")) {

						// note that this parameter overrides the settings in the (derived) class
						String option = upperCaseParameter.substring(kParamHeight.length() + 1);
						if (option.equalsIgnoreCase(kParamGUIFullScreen)) {
							fGUIHeight = kFullScreenGUI;
						}
						else if (option.equalsIgnoreCase(kParamGUIAutoSize)) {
							fGUIHeight = kAutoSizeGUI;
						}
						else {
							try {
								fGUIHeight = (new Integer(option)).intValue();
							}
							catch (NumberFormatException exc) {
								showParameterWarning(paramNr,parameter,"incorrect height specified");
							}
						}
					}
					else if (upperCaseParameter.startsWith(kParamSilent)) {
						MP3Player.disableSystemSounds();
					}
					else if (upperCaseParameter.startsWith(kParamHelp)) {
						kLogger.info(
							"Available command-line parameters:\n" +
							"\n" +
							"  -locale=lll\n" +
							"  -developmode\n" +
							"  -width=xxx\n" +
							"  -height=yyy\n" +
							"  -silent\n" +
							"  -help\n" +
							"\n" +
							"Available options:\n" +
							"\n" +
							"  lll can be either dutch, ukenglish, usenglish or a user-specified locale (default is usenglish).\n" +
							"  xxx and yyy can be either autosize, fullscreen or a number (default is autosize).");
						abortApplication(null,false);
					}
					else if (!parseParameter(paramNr,parameter)) {
						showParameterWarning(paramNr,parameter,"unknown parameter specified");
					}
				}
			}
		}
	}

	/**
	 */
	private void saveRegistry()
	{
		try {
			fSystemRegistry.save(kSystemRegistryFilename);
		}
		catch (RegistryException exc) {
			JWarningDialog.warn(this,exc.getRegistryError());
		}
	}

	/**
	 */
	private static void saveSystemRegistry()
	{
		if (kSaveSystemRegistry) {
			kLogger.info("Saving system registry...");

			try {
				Registry registry = Registry.getInstance();

				SystemHive systemHive = new SystemHive();
				systemHive.fContents = new Hashtable<>();

				// update contents of the system hive
				// ...

				registry.addHive("SystemHive",systemHive);
				registry.save(kSystemRegistryFilename);
				kLogger.info("Done saving registry; application stopped.");
			}
			catch (Exception exc) {
				kLogger.fatal(exc);
			}
			System.exit(0);
		}
	}
}
