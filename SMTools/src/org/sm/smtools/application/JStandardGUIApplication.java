// --------------------------------------------
// Filename      : JStandardGUIApplication.java
// Author        : Sven Maerivoet
// Last modified : 19/11/2014
// Target        : Java VM (1.8)
// --------------------------------------------

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
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.apache.log4j.*;
import org.sm.smtools.application.registry.*;
import org.sm.smtools.util.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.exceptions.*;
import org.sm.smtools.swing.dialogs.*;

/**
 * The <CODE>JStandardGUIApplication</CODE> class provides a standard Swing based GUI framework.
 * <P>
 * By default, the <CODE>JStandardGUIApplication</CODE> class will result in the following GUI:
 * <P>
 * <IMG src="doc-files/standard-gui.png" alt="">
 * <P>
 * Typically, this <CODE>JStandardGUIApplication</CODE> is subclassed, with several methods overridden.
 * These methods control the application's settings, the visual layout of its GUI, and the actions that need to be
 * taken upon user input. 
 * <P>
 * The overridable methods that setup the GUI's form and behaviour are:
 * <UL>
 *   <LI><B><U>Java runtime environment version checking</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#setupRequiredMajorJavaVersion()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupRequiredMinorJavaVersion()}</LI>
 *   </UL>
 *   <LI><B><U>Command-line parameter parsing</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#parseParameter(int,String)}</LI>
 *     <BR>
 *     Already built-in command-line parameters are:
 *     <UL>
 *       <LI>-<B>locale</B>=<I>dutch</I>|<I>ukenglish</I>|<I>usenglish</I> (default is <I>ukenglish</I>)</LI>
 *       <LI>-<B>developmode</B></LI>
 *       <LI>-<B>width</B>=<I>autosize</I>|<I>fullscreen</I>|&lt;<I>number</I>&gt; (default is <I>autosize</I>)</LI>
 *       <LI>-<B>height</B>=<I>autosize</I>|<I>fullscreen</I>|&lt;<I>number</I>&gt; (default is <I>autosize</I>)</LI>
 *       <LI>-<B>silent</B></LI>
 *       <LI>-<B>help</B></LI>
 *     </UL>
 *     <I>Note that the application is sized to fullscreen if either width or height or set to reflect this. Fullscreen is this case
 *     this implies window-mode (which is maximised if the OS allows it).</I>
 *   </UL>
 *   <LI><B><U>Custom initialisation and clean-up</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#setupApplicationResourceArchiveFilename()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupApplicationLocalePrefix()}</LI>
 *     <LI>{@link JStandardGUIApplication#initialise(Object[])}</LI>
 *     <LI>{@link JStandardGUIApplication#shutdown()}</LI>
 *   </UL>
 *   <LI><B><U>Splash screen during startup</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#getSplashScreen()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupSplashScreenContent()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupSplashScreenSound()} [<I>see also {@link JSplashScreen} and {@link MP3Player}</I>]</LI>
 *   </UL>
 *   <LI><B><U>Visual layout (window related)</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#setupInitialLookAndFeel()}</LI>
 *     <LI>{@link JStandardGUIApplication#lookAndFeelChanged()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupInitialGUISize()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupIsGUIResizable()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupIsGUIRepaintedWhenResizing()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupIcon()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupWindowTitle()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupMinimiseToSystemTrayAllowed()}</LI>
 *     <LI>{@link JStandardGUIApplication#setDynamicLayout(boolean)}</LI>
 *   </UL>
 *   <LI><B><U>Visual layout (content related)</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#setupContentPane(JPanel)}</LI>
 *     <LI>{@link JStandardGUIApplication#setupMenus()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupRightHandMenu()}</LI>
 *     <LI>{@link JStandardGUIApplication#constructMenuItem(String,boolean)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructMenuItem(String)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructRadioButtonMenuItem(String,boolean)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructRadioButtonMenuItem(String)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructCheckBoxMenuItem(String,boolean)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructCheckBoxMenuItem(String)}</LI>
 *     <LI>{@link JStandardGUIApplication#setupStatusBarCustomLabels()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupIsStatusBarEnabled()}</LI>
 *     <LI>{@link JStandardGUIApplication#getStatusBar()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupGlassPane()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupIsClockEnabled()}</LI>
 *     <LI>{@link JStandardGUIApplication#setupAboutBox()} [<I>see also {@link JAboutBox}</I>]</LI>
 *   </UL>
 *   <LI><B><U>Reacting to user input</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#actionPerformed(ActionEvent)}</LI>
 *     <LI>{@link JStandardGUIApplication#windowResized()}</LI>
 *   </UL>
 * </UL>
 * <P>
 * Note that by default, a "<I>General</I>" menu is present, which looks as follows:
 * <P>
 * <IMG src="doc-files/standard-menu.png" alt="">
 * <P>
 * Note that if the underlying operating system allows for minimisation to the system tray,
 * then this options becomes available in the menu.
 * <P>
 * As the application is ran, the global system {@link Registry} is read from the file <CODE>system-registry.ser</CODE>
 * (and stored back to file at the end). The user registry is also loaded from the file <CODE>application-registry.ser</CODE> if it exists.
 * <P>
 * When the user wants to quit the application, a confirmation dialog is shown:
 * <P>
 * <IMG src="doc-files/quit-dialog.png" alt="">
 * <P>
 * Note that this confirmation can be skipped if {@link DevelopMode#isActivated} is <CODE>true</CODE>.
 * 
 * @author  Sven Maerivoet
 * @version 19/11/2014
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
	 * Useful constant for specifying the GTK look-and-feel.
	 */
	protected static final String klafGTK = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";

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
	 * Useful constant for specifying the Quaqua (Mac OS X emulation) look-and-feel.
	 */
	protected static final String klafQuaqua = "ch.randelshofer.quaqua.QuaquaLookAndFeel";

	/**
	 * Useful constant for specifying the Microsoft Windows look-and-feel.
	 */
	protected static final String klafWindows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	/**
	 * Useful constant for specifying the Microsoft Windows Classic look-and-feel.
	 */
	protected static final String klafWindowsClassic = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";

	/**
	 * Useful constant for specifying the current platform's look-and-feel.
	 */
	protected static final String klafSystem = "platform-dependent";

	/**
	 * Access point to the application's own resources.
	 */
	protected JARResources fResources;

	// access point to the Log4j logging facility
	private static final Logger kLogger = Logger.getLogger(JStandardGUIApplication.class.getName());

	// the required Java version for the SMTools package
	private static final int kRequiredMajorJavaVersion = 1;
	private static final int kRequiredMinorJavaVersion = 7;

	// the location of the JAR archive containing all the resources
	private static final String kResourceArchiveFilename = "smtools.jar";

	// the location of the system locale database
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
	private static final String kActionCommandMenuItemMetalLAF = "menuItem.MetalLAF";
	private static final String kActionCommandMenuItemNimbusLAF = "menuItem.NimbusLAF";
	private static final String kActionCommandMenuItemMotifLAF = "menuItem.MotifLAF";
	private static final String kActionCommandMenuItemGTKLAF = "menuItem.GTKLAF";
	private static final String kActionCommandMenuItemWindowsLAF = "menuItem.WindowsLAF";
	private static final String kActionCommandMenuItemWindowsClassicLAF = "menuItem.WindowsClassicLAF";
	private static final String kActionCommandMenuItemMacLAF = "menuItem.MacLAF";
	private static final String kActionCommandMenuItemQuaquaLAF = "menuItem.QuaquaLAF";
	private static final String kActionCommandMenuItemSystemLAF = "menuItem.SystemLAF";
	private static final String kActionCommandMenuItemMinimiseToSystemTray = "menuItem.MinimiseToSystemTray";
	private static final String kActionCommandMenuItemEnableWindowResizing = "menuItem.EnableWindowResizing";
	private static final String kActionCommandMenuItemAbout = "menuItem.About";
	private static final String kActionCommandMenuItemSystemSoundsEnabled = "menuItem.SystemSoundsEnabled";
	private static final String kActionCommandMenuItemQuit = "menuItem.Quit";

	// set the clock's update period to half a second
	private static final int kClockUpdatePeriod = 500;

	// switch for first-time initialisation of the system registry
	private static final boolean kSaveSystemRegistry = false;

	// internal datastructures
	private Registry fSystemRegistry;
	private int fGUIWidth;
	private int fGUIHeight;
	private String fCurrentLAF;
	private JRadioButtonMenuItem frbGTK;
	private JRadioButtonMenuItem frbMac;
	private JRadioButtonMenuItem frbMetal;
	private JRadioButtonMenuItem frbMotif;
	private JRadioButtonMenuItem frbNimbus;
	private JRadioButtonMenuItem frbQuaqua;
	private JRadioButtonMenuItem frbWindows;
	private JRadioButtonMenuItem frbWindowsClassic;
	private JCheckBoxMenuItem fcbEnableWindowResizing;
	private boolean fSystemSoundsEnabled;
	private JCheckBoxMenuItem fcbSystemSoundsEnabled;
	private JStatusBar fStatusBar;
	private JLabel fClockLabel;
	private String fLocale;
	private JSplashScreen fSplashScreen;
	private Image fIcon;
	private boolean fMinimiseToSystemTray;
	private TrayIcon fTrayIcon;
	private JPanel fGlassPane;
	private int fWindowWidth;
	private int fWindowHeight;

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
	 * <UL>
	 *   <LI>The required version of the Java runtime engine is checked (see {@link JStandardGUIApplication#setupRequiredMajorJavaVersion()} and
	 *       {@link JStandardGUIApplication#setupRequiredMinorJavaVersion()}).</LI>
	 *   <LI>The system {@link JARResources} are loaded.</LI>
	 *   <LI>The GUI is set to auto-size by default (see {@link JStandardGUIApplication#setupInitialGUISize()} and
	 *       {@link JStandardGUIApplication#setupIsGUIResizable()}).</LI>
	 *   <LI>British English is the default language used.</LI>
	 *   <LI>The command-line parameters are parsed (see {@link JStandardGUIApplication#parseParameter(int,String)}).</LI>
	 *   <LI>The system's locale {@link I18NL10N} database is loaded.</LI>
	 *   <LI>The application's {@link JARResources} and locale {@link I18NL10N} database are loaded (if they are present).</LI>
	 *   <LI>The global system {@link Registry} is read from file.</LI>
	 *   <LI>The look-and-feel of the operating system is used by default.</LI>
	 *   <LI>A optional splash screen is shown (see {@link JStandardGUIApplication#setupSplashScreenContent()} and
	 *       {@link JStandardGUIApplication#setupSplashScreenSound()}).</LI>
	 *   <LI>Custom initialisation is performed (see {@link JStandardGUIApplication#initialise(Object[])}).
	 *       Note that the objects are specified as <CODE>new Object[] {object1,object2}</CODE>.</LI>
	 *   <LI>The window's icon and title are set (see {@link JStandardGUIApplication#setupIcon()} and
	 *       {@link JStandardGUIApplication#setupWindowTitle()}).</LI>
	 *   <LI>The GUI's content pane is constructed (see {@link JStandardGUIApplication#setupContentPane(JPanel)}).</LI>
	 *   <LI>The GUI's status bar is constructed (see {@link JStandardGUIApplication#setupStatusBarCustomLabels()},
	 *       {@link JStandardGUIApplication#setupIsStatusBarEnabled()}, and {@link JStandardGUIApplication#getStatusBar()}).</LI>
	 *   <LI>The GUI's menu bar is constructed (see {@link JStandardGUIApplication#setupMenus()} and
	 *       {@link JStandardGUIApplication#setupRightHandMenu()}).</LI>
	 *   <LI>The about box is shown (see {@link JStandardGUIApplication#setupAboutBox()}).</LI>
	 *   <LI>The repainting behaviour when resizing is set to dynamic layout (see {@link JStandardGUIApplication#setDynamicLayout(boolean)}).</LI>
	 *   <LI>The application checks if minimisation to the system tray is allowed (see {@link JStandardGUIApplication#setupMinimiseToSystemTrayAllowed()}).</LI>
	 *   <LI>The glass pane is constructed (see {@link JStandardGUIApplication#setupGlassPane()}).</LI>
	 * </UL>
	 * If no parameters are to be passed to the GUI, specify <CODE>null</CODE>
	 * for <CODE>parameters</CODE>.
	 *
	 * @param argv        an array of strings containing the <B>command-line</B> parameters
	 * @param parameters  an array of objects containing the parameters to be passed to the GUI's
	 *                    {@link JStandardGUIApplication#initialise(Object[])} method
	 */
	public JStandardGUIApplication(String[] argv, Object[] parameters)
	{
		// check Java runtime version requirements
		kLogger.info("Checking Java runtime environment requirements...");
		String javaRuntimeVersion = System.getProperty("java.version");
		int dotPosition = javaRuntimeVersion.indexOf(".");
		int javaMajorVersion = (Integer.valueOf(javaRuntimeVersion.substring(dotPosition - 1,dotPosition))).intValue();
		int javaMinorVersion = (Integer.valueOf(javaRuntimeVersion.substring(dotPosition + 1,dotPosition + 2))).intValue();
		String requiredJavaRuntimeVersion = setupRequiredMajorJavaVersion() + "." + setupRequiredMinorJavaVersion();
		if ((javaMajorVersion < setupRequiredMajorJavaVersion()) ||
				((javaMajorVersion >= setupRequiredMajorJavaVersion()) && (javaMinorVersion < setupRequiredMinorJavaVersion()))) {
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

		Dimension initialGUISize = setupInitialGUISize();
		if (initialGUISize != null) {
			fGUIWidth = initialGUISize.width;
			fGUIHeight = initialGUISize.height;
		}

		fLocale = I18NL10N.kLocaleBritishEnglish;
		fSystemSoundsEnabled = true;
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
		String applicationResourceArchiveFilename = setupApplicationResourceArchiveFilename(); 
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
		String applicationLocalePrefix = setupApplicationLocalePrefix();
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

		// make the contents of windows dynamic (e.g., resizing background images, ...)
		if (setupIsGUIRepaintedWhenResizing() && ((boolean) Toolkit.getDefaultToolkit().getDesktopProperty("awt.dynamicLayoutSupported"))) {
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
		}

		// set the look-and-feel to that of the platform
		kLogger.info(I18NL10N.translate("text.SettingGUILookAndFeel"));
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		setInitialLookAndFeel(setupInitialLookAndFeel());

		// activate the splash screen
		fSplashScreen = new JSplashScreen(setupSplashScreenContent(),setupSplashScreenSound());

		// change the cursor to indicate that the user has to wait
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		setTitle(kDefaultWindowTitle);

		// allow custom initialisation
		kLogger.info(I18NL10N.translate("text.PerformingCustomInitialisation"));
		initialise(parameters);

		kLogger.info(I18NL10N.translate("text.CreatingGUIComponents"));
		getSplashScreen().setStatusMessage(I18NL10N.translate("text.ConstructingGUI"));

		// load the application's icon
		fIcon = setupIcon();
		if (fIcon != null) {
			setIconImage(fIcon);
		}

		setTitle(setupWindowTitle());
		setResizable(setupIsGUIResizable());

		// setup content pane
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());

		// if necessary, add the status bar
		fStatusBar = new JStatusBar(setupIsGUIResizable(),setupStatusBarCustomLabels());
		if (setupIsStatusBarEnabled()) {
			contentPane.add(fStatusBar,BorderLayout.SOUTH);

			// create a Swing timer to periodically update the status bar battery level and memory usage labels
			Action updateStatusBarAction = new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
					fStatusBar.updateBatteryLevelAndMemoryUsage();
				}
			};
			new javax.swing.Timer(JStatusBar.kUpdatePeriod,updateStatusBarAction).start();

			// perform the first update when the GUI is displayed
			fStatusBar.updateBatteryLevelAndMemoryUsage();
		}

			JPanel embeddedContentPane = new JPanel();
			setupContentPane(embeddedContentPane);
		contentPane.add(embeddedContentPane,BorderLayout.CENTER);

		setContentPane(contentPane);

		// if necessary, add the clock
		if (setupIsClockEnabled()) {
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

		// determine the requested GUI's screensize
		if ((fGUIWidth == kAutoSizeGUI) || (fGUIHeight == kAutoSizeGUI)) {
			// fit tightly around all the components' natural widths and heights
			pack();
			fGUIWidth = getSize().width;
			fGUIHeight = getSize().height;
		}
		else {
			boolean fullScreenGUISelected = ((fGUIWidth == kFullScreenGUI) || (fGUIHeight == kFullScreenGUI));
			if (fullScreenGUISelected) {
				// determine the GUI's maximum screensize
				Dimension screenSize = getScreenSize();
				Insets screenInsets = getScreenInsets();
				// take into account all the space that a possible OS taskbar tasks
				fGUIWidth = (int) Math.round(screenSize.getWidth() - screenInsets.left - screenInsets.right);
				fGUIHeight = (int) Math.round(screenSize.getHeight() - screenInsets.top - screenInsets.bottom);

				// check if the OS can set the window to a maximised state
				if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
					setExtendedState(Frame.MAXIMIZED_BOTH);
				}
				else {
					// we approximate the fullscreen GUI ourselves
					setSize(new Dimension(fGUIWidth,fGUIHeight));
				}
			}
			else {
				setSize(new Dimension(fGUIWidth,fGUIHeight));
			}
		}

		if (fSplashScreen.isAvailable()) {
			fSplashScreen.setVisible(false);
			fSplashScreen.dispose();
		}

		// install the callback method for when the window is resized
		fWindowWidth = 0;
		fWindowHeight = 0;
		addComponentListener(this);

		setVisible(true);

		// restore the cursor
		setCursor(Cursor.getDefaultCursor());

		// update the menu's radio buttons
		setLookAndFeelMenuItems();

		// check whether or not minimising to the system tray is supported
		fMinimiseToSystemTray = (SystemTray.isSupported() && setupMinimiseToSystemTrayAllowed());

		kLogger.info(I18NL10N.translate("text.ApplicationReady"));

		// show the aboutbox
		JDefaultDialog aboutBox = setupAboutBox();
		if ((!DevelopMode.isActivated()) && (aboutBox != null)) {
			aboutBox.activate();
		}

		// allows real-time repainting of the window's contents upon resizing
		setDynamicLayout(true);

		// setup glass pane
		fGlassPane = setupGlassPane();
		if (fGlassPane != null) {
			setGlassPane(fGlassPane);
			fGlassPane.setVisible(false);
			fGlassPane.setOpaque(false);
		}
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
	 *   super.actionPerformed(e);<BR>
	 *   // rest of method's code
	 * </CODE>
	 *
	 * @param e  the <CODE>ActionEvent</CODE> that is received
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		if (command.startsWith("menuItem")) {
			MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSMenuItem,MP3Player.EPlaying.kBlocked);
		}

		if (command.equalsIgnoreCase(kActionCommandMenuItemAbout)) {
			JDefaultDialog aboutBox = setupAboutBox();
			if (aboutBox != null) {
				aboutBox.activate();
			}
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemGTKLAF)) {
			setLookAndFeel(klafGTK,false);
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
		else if (command.equalsIgnoreCase(kActionCommandMenuItemQuaquaLAF)) {
			setLookAndFeel(klafQuaqua,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemWindowsLAF)) {
			setLookAndFeel(klafWindows,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemWindowsClassicLAF)) {
			setLookAndFeel(klafWindowsClassic,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemSystemLAF)) {
			setLookAndFeel(klafSystem,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemMinimiseToSystemTray)) {
			fMinimiseToSystemTray = !fMinimiseToSystemTray; 
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemEnableWindowResizing)) {
			setResizable(fcbEnableWindowResizing.isSelected());
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemSystemSoundsEnabled)) {
			if (fcbSystemSoundsEnabled.isSelected()) {
				MP3Player.enableSystemSounds();
			}
			else {
				MP3Player.disableSystemSounds();
			}
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemQuit)) {
			windowClosing(null);
		}
	}

	// the component-listener
	/**
	 * This method does nothing.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void componentMoved(ComponentEvent e)
	{
	}

	/**
	 * This method does nothing.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void componentShown(ComponentEvent e)
	{
	}

	/**
	 * This method does nothing.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void componentHidden(ComponentEvent e)
	{
	}

	/**
	 * This method calls the callback function {@link JStandardGUIApplication#windowResized()}.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void componentResized(ComponentEvent e)
	{
		// only execute when the window has physically changed in size
		if ((fWindowWidth != getWidth()) || (fWindowHeight != getHeight())) {
			fWindowWidth = getWidth();
			fWindowHeight = getHeight();

			// activate the callback
			windowResized();
		}
	}

	// the window-listener
	/**
	 * This method does nothing.
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public void windowActivated(WindowEvent e)
	{
	}

	/**
	 * This method does nothing.
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	/**
	 * Gracefully ends the application by running the shutdown sequence and saving the registry.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowClosing(WindowEvent e)
	{
		if (!DevelopMode.isActivated()) {

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
	 * This method does nothing.
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

	/**
	 * Plays a sound.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowDeiconified(WindowEvent e)
	{
		MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSWindowEvent);
	}

	/**
	 * Plays a sound and if necessary creates an icon in the system tray.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowIconified(WindowEvent e)
	{
		MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSWindowEvent);
		if (fMinimiseToSystemTray) {
			fTrayIcon = new TrayIcon(fIcon,setupWindowTitle(),null);
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
				fTrayIcon.displayMessage(setupWindowTitle(),I18NL10N.translate("text.RestoreFromSystemTray"),TrayIcon.MessageType.INFO);
				setVisible(false);
			}
			catch (AWTException exc) {
				kLogger.error(I18NL10N.translate("error.MinimisingToSystemTray"));
			}
		}
	}

	/**
	 * This method does nothing.
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public void windowOpened(WindowEvent e)
	{
	}

	/**
	 * The application's <CODE>main</CODE> method.
	 * <P>
	 * Note that this method should be overridden by a derived subclass:
	 * <P>
	 * <CODE>
	 *   public static void main(String[] argv)<BR>
	 *   {<BR>
	 *     DerivedGUIApplication derivedGUIApplication = new DerivedGUIApplication(argv);<BR>
	 *   }
	 * </CODE>
	 *
	 * @param argv  an array of strings containing the <B>command-line</B> parameters
	 */
	public static void main(String[] argv)
	{
		saveSystemRegistry();
		new JStandardGUIApplication(argv,null);
	}

	/**
	 * Emits an audible beep.
	 */
	public final void beep()
	{
		Toolkit.getDefaultToolkit().beep();
	}

	/**
	 * Emulates hiding the mouse cursor.
	 */
	public final void hideMouseCursor()
	{
		int[] emptyImageData = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16,16,emptyImageData,0,16));
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(image,new Point(0,0),"invisibleCursor"));
	}

	/**
	 * Sets the use of dynamic layout (i.e., real-time repainting of the <CODE>JFrame</CODE>'s components when resizing).
	 * 
	 * @param dynamicLayout  a <CODE>boolean</CODE> indicating whether or not dynamic layout should be activated
	 */
	public final void setDynamicLayout(boolean dynamicLayout)
	{
		// check if dynamic layout is currently active and supported by the underlying operating system and/or window manager
		if (dynamicLayout && Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
		}
		else {
			Toolkit.getDefaultToolkit().setDynamicLayout(false);
		}
	}

	/**
	 * Returns the screen size.
	 *
	 * @return the screen size
	 */
	public final Dimension getScreenSize()
	{
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	/**
	 * Returns the screen insets.
	 *
	 * @return the screen insets
	 */
	public final Insets getScreenInsets()
	{
		return Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * Sets up the required major version of the Java runtime engine that wants to run this application.
	 * <P>
	 * <B>The default is 1 (for JRE 1.7.0).</B>
	 *
	 * @return the required major version of the Java runtime engine
	 */
	protected int setupRequiredMajorJavaVersion()
	{
		return kRequiredMajorJavaVersion;
	}

	/**
	 * Sets up the required minor version of the Java runtime engine that wants to run this application.
	 * <P>
	 * <B>The default is 7 (for JRE 1.7.0).</B>
	 *
	 * @return the required minor version of the Java runtime engine
	 */
	protected int setupRequiredMinorJavaVersion()
	{
		return kRequiredMinorJavaVersion;
	}

	/**
	 * Sets up the filename of the JAR or ZIP file containing the application's resources.
	 * <P>
	 * If <CODE>null</CODE> is specified, then the system ignores loading the application's resources.
	 *
	 * @return the filename of the archive (JAR or ZIP) containing the application's resources
	 * @see    org.sm.smtools.application.util.JARResources
	 */
	protected String setupApplicationResourceArchiveFilename()
	{
		return null;
	}

	/**
	 * Sets up the path and prefix names to the application's locale databases.
	 * <P>
	 * If <CODE>null</CODE> is specified, then the system ignores loading the application's locale databases.
	 *
	 * @return the path to the location of the application's locale databases
	 * @see    org.sm.smtools.application.util.I18NL10N
	 */
	protected String setupApplicationLocalePrefix()
	{
		return null;
	}

	/**
	 * Allows custom initialisation of the subclass's member fields.
	 * <P>
	 * The parameters in the <CODE>Object[]</CODE> array are passed through
	 * the class's constructor (see {@link JStandardGUIApplication#JStandardGUIApplication(String[],Object[])}).
	 * They are typically specified as <CODE>new Object[] {object1,object2}</CODE>.
	 *
	 * @param parameters  an array of <CODE>Objects</CODE>
	 */
	protected void initialise(Object[] parameters)
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
	 *   final String kCustomParameter = "PARAMETER";<BR>
	 *   final String kCustomOption    = "OPTION";<BR>
	 *   <BR>
	 *   String upperCaseParameter = parameter.toUpperCase();<BR>
	 *   <BR>
	 *   // parse parameter<BR>
	 *   if (upperCaseParameter.startsWith(kCustomParameter &#43; "=")) {<BR>
	 *   <BR>
	 *     String upperCaseOption = upperCaseParameter.substring(kCustomParameter.length() &#43; 1);<BR>
	 *     // parse option<BR>
	 *     if (upperCaseOption.equalsIgnoreCase(kCustomOption)) {<BR>
	 *       // take action as parameter is parsed<BR>
	 *     }<BR>
	 *     else {<BR>
	 *       showParameterWarning(paramNr,parameter,"not a valid option");<BR>
	 *     }<BR>
	 *     <BR>
	 *     // indicate that parameter was valid<BR>
	 *     return true;<BR>
	 *   }<BR>
	 *   else {<BR>
	 *     <BR>
	 *     // indicate that parameter is unknown<BR>
	 *     return false;<BR>
	 *   }
	 * </CODE>
	 *
	 * @param  paramNr    the number of the parameter that is being parsed
	 * @param  parameter  the unmodified parameter as specified on the command-line
	 * @return            <CODE>true</CODE> if the parameter was parsed successfully, <CODE>false</CODE> otherwise
	 * @see               JStandardGUIApplication#showParameterWarning(int,String,String)
	 */
	protected boolean parseParameter(int paramNr, String parameter)
	{
		return false;
	}

	/**
	 * Logs a textual warning message.
	 *
	 * @param paramNr    the number of the parameter that failed to get parsed
	 * @param parameter  the unmodified parameter as specified on the command-line
	 * @param message    the warning message to log
	 * @see              JStandardGUIApplication#parseParameter(int,String)
	 */
	protected final void showParameterWarning(int paramNr, String parameter, String message)
	{
		kLogger.warn(
			"Warning: parameter #" + String.valueOf(paramNr) + " (" + parameter + ") ignored.\n" +
			"\t => " + message + ".");
	}

	/**
	 * Sets up a <CODE>JLabel</CODE> containing the splash screen's custom content.
	 * <P>
	 * See {@link JSplashScreen} for more information regarding the splash screen.
	 *
	 * @return a <CODE>JLabel</CODE> containing the splash screen's custom content
	 * @see    org.sm.smtools.application.util.JSplashScreen
	 */
	protected JLabel setupSplashScreenContent()
	{
		return null;
	}

	/**
	 * Sets up the MP3 sound to play during the splash screen.
	 * <P>
	 * See {@link JSplashScreen} for more information regarding the splash screen and {@link MP3Player} for playing MP3 sounds.
	 *
	 * @return the MP3 sound to play during the splash screen
	 * @see    org.sm.smtools.application.util.JSplashScreen
	 * @see    org.sm.smtools.util.MP3Player
	 */
	protected InputStream setupSplashScreenSound()
	{
		return null;
	}

	/**
	 * Returns a handle to the GUI's splash screen.
	 *
	 * @return a handle to the GUI's splash screen
	 * @see    org.sm.smtools.application.util.JSplashScreen
	 */
	protected final JSplashScreen getSplashScreen()
	{
		return fSplashScreen;
	}

	/**
	 * Sets up the application's initial look-and-feel.
	 * <P>
	 * The possible values that can be returned are:<BR>
	 * <BR>
	 * <UL>
	 *   <LI>the GTK look-and-feel ({@link JStandardGUIApplication#klafGTK})</LI>
	 *   <LI>the Mac OS X look-and-feel ({@link JStandardGUIApplication#klafMac})</LI>
	 *   <LI>Java's Metal look-and-feel ({@link JStandardGUIApplication#klafMetal})</LI>
	 *   <LI>the Motif look-and-feel ({@link JStandardGUIApplication#klafMotif})</LI>
	 *   <LI>Java's Nimbus look-and-feel ({@link JStandardGUIApplication#klafNimbus})</LI>
	 *   <LI>the Quaqua (Mac OS X emulation) look-and-feel ({@link JStandardGUIApplication#klafQuaqua})</LI>
	 *   <LI>the Microsoft Windows look-and-feel ({@link JStandardGUIApplication#klafWindows})</LI>
	 *   <LI>the Microsoft Windows (classic) look-and-feel ({@link JStandardGUIApplication#klafWindowsClassic})</LI>
	 *   <LI>the current platform's look-and-feel ({@link JStandardGUIApplication#klafSystem})</LI>
	 * </UL>
	 * <P>
	 * Note that this method returns the current platform's look-and-feel by default.
	 * 
	 * @return the application's initial look-and-feel
	 */
	protected String setupInitialLookAndFeel()
	{
		return klafInitialLaF;
	}

	/**
	 * A callback method for when the look-and-feel has changed.
	 */
	protected void lookAndFeelChanged()
	{
		windowResized();
	}

	/**
	 * Sets up the GUI's initial size on the screen.
	 * <P>
	 * A derived subclass should return a <CODE>Dimension</CODE> object containing the initial
	 * width and height of the GUI's window. Their values are expressed in pixels, but the following
	 * two special values are also accepted:
	 * <UL>
	 *   <LI>{@link JStandardGUIApplication#kAutoSizeGUI}</LI>
	 *   <LI>{@link JStandardGUIApplication#kFullScreenGUI}</LI>
	 * </UL>
	 * <P>
	 * An example:
	 * <P>
	 * <CODE>return (new Dimension(JStandardGUIApplication.kFullScreenGUI,250));</CODE>
	 * <P>
	 * which specifies a GUI with full screen width and 250 pixels height.
	 *
	 * @return the GUI's initial size on the screen as a <CODE>Dimension</CODE> object
	 */
	protected Dimension setupInitialGUISize()
	{
		return null;
	}

	/**
	 * Sets up whether or not the GUI's window should be resizable.
	 * <P>
	 * This method returns <CODE>true</CODE> by default.
	 *
	 * @return <CODE>true</CODE> if the GUI's window should be resizable, <CODE>false</CODE> if it should be fixed size
	 */
	protected boolean setupIsGUIResizable()
	{
		return true;
	}

	/**
	 * Sets up whether or not the GUI should always be repainted when the window is resized.
	 * This only works if the host platform supports it.
	 * <P>
	 * This method returns <CODE>true</CODE> by default.
	 *
	 * @return <CODE>true</CODE> if the GUI should be repainted when the window is resized
	 */
	protected boolean setupIsGUIRepaintedWhenResizing()
	{
		return true;
	}

	/**
	 * A callback function for when the GUI's window is resized.
	 */
	protected void windowResized()
	{
	}

	/**
	 * Sets up the <CODE>Image</CODE> containing the GUI's icon.
	 * <P>
	 * The <CODE>Image</CODE> should be a JPG-file, 32x32 pixels with 24-bit colourdepth (i.e., true colour).
	 *
	 * @return the <CODE>Image</CODE> containing the GUI's icon
	 */
	protected Image setupIcon()
	{
		try {
			return JARResources.fSystemResources.getImage(kDefaultApplicationIconFilename);
		}
		catch (FileDoesNotExistException exc) {
			return null;
		}
	}

	/**
	 * Sets up the GUI's window title.
	 *
	 * @return the GUI's window title
	 */
	protected String setupWindowTitle()
	{
		return kDefaultWindowTitle;
	}

	/**
	 * Sets up the GUI's content pane.
	 * <P>
	 * A derived subclass typically overrides this method. The subclass can
	 * operate on the given <CODE>contentPane</CODE> parameter (which is initialised,
	 * i.e., not <CODE>null</CODE>, by default).
	 * <P>
	 * A subclass thus sets the content pane's layout managers, adds components to it, ...
	 *
	 * @param contentPane  the GUI's main content pane to modify
	 */
	protected void setupContentPane(JPanel contentPane)
	{
	}

	/**
	 * Sets up custom menus.
	 * <P>
	 * By default, the application already contains a "<I>General</I>" menu containing
	 * access to the optional about box, setting the GUI's look and feel and getting confirmation
	 * when the user wants to quit the application.
	 * <P>
	 * Note that this method returns <CODE>null</CODE> by default.
	 *
	 * @return an <CODE>ArrayList</CODE> of menus
	 * @see    JStandardGUIApplication#setupRightHandMenu()
	 */
	protected ArrayList<JMenu> setupMenus()
	{
		return null;
	}

	/**
	 * Sets up a custom right hand menu (e.g., a "<I>Help</I>" menu).
	 * <P>
	 * Note that this method returns <CODE>null</CODE> by default.
	 *
	 * @return a menu used in the right part of the menubar
	 * @see    JStandardGUIApplication#setupMenus()
	 */
	protected JMenu setupRightHandMenu()
	{
		return null;
	}

	/**
	 * Helper method for constructing a menu item object; an optional mnemonic can be used if it is found.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param menuItemKey  the key of the menu item
	 * @param useMnemonic  a flag indicating whether to use a mnemonic or not
	 * @return             a menu item object
	 */
	protected final JMenuItem constructMenuItem(String menuItemKey, boolean useMnemonic)
	{
		String translation = I18NL10N.translate(menuItemKey.trim());
		String indentation = StringTools.getIndentation(menuItemKey);

		if (useMnemonic) {
			Integer mnemonic = I18NL10N.translateMnemonic(I18NL10N.translate(menuItemKey.trim() + ".Mnemonic"));
			if (mnemonic != null) {
				return (new JMenuItem(indentation + translation,mnemonic));
			}
			else {
				return (new JMenuItem(indentation + translation));
			}
		}
		else {
			return (new JMenuItem(indentation + translation));
		}
	}

	/**
	 * Helper method constructing a menu item object; a mnemonic is used if it is found.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param menuItemKey  the key of the menu item
	 * @return             a menu item object
	 */
	protected final JMenuItem constructMenuItem(String menuItemKey)
	{
		return constructMenuItem(menuItemKey,true);
	}

	/**
	 * Helper method for constructing a radio button menu item object; an optional mnemonic can be used if it is found.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param radioButtonMenuItemKey  the key of the menu item
	 * @param useMnemonic             a flag indicating whether to use a mnemonic or not
	 * @return                        a radio button menu item object
	 */
	protected final JRadioButtonMenuItem constructRadioButtonMenuItem(String radioButtonMenuItemKey, boolean useMnemonic)
	{
		String translation = I18NL10N.translate(radioButtonMenuItemKey.trim());
		String indentation = StringTools.getIndentation(radioButtonMenuItemKey);

		JRadioButtonMenuItem radioButtonMenuItem = new JRadioButtonMenuItem(indentation + translation);

		if (useMnemonic) {
			Integer mnemonic = I18NL10N.translateMnemonic(I18NL10N.translate(radioButtonMenuItemKey.trim() + ".Mnemonic"));

			if (mnemonic != null) {
				radioButtonMenuItem.setMnemonic(mnemonic);
			}
		}

		return radioButtonMenuItem;
	}

	/**
	 * Helper method for constructing a radio button menu item object; a mnemonic is used if it is found.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param radioButtonMenuItemKey  the key of the menu item
	 * @return                        a radio button menu item object
	 */
	protected final JRadioButtonMenuItem constructRadioButtonMenuItem(String radioButtonMenuItemKey)
	{
		return constructRadioButtonMenuItem(radioButtonMenuItemKey,true);
	}

	/**
	 * Helper method for constructing a check box menu item object; an optional mnemonic can be used if it is found.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param checkBoxMenuItemKey  the key of the menu item
	 * @param useMnemonic          a flag indicating whether to use a mnemonic or not
	 * @return                     a check box menu item object
	 */
	protected final JCheckBoxMenuItem constructCheckBoxMenuItem(String checkBoxMenuItemKey, boolean useMnemonic)
	{
		String translation = I18NL10N.translate(checkBoxMenuItemKey.trim());
		String indentation = StringTools.getIndentation(checkBoxMenuItemKey);

		JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(indentation + translation);

		if (useMnemonic) {
			Integer mnemonic = I18NL10N.translateMnemonic(I18NL10N.translate(checkBoxMenuItemKey.trim() + ".Mnemonic"));

			if (mnemonic != null) {
				checkBoxMenuItem.setMnemonic(mnemonic);
			}
		}

		return checkBoxMenuItem;
	}

	/**
	 * Helper method for constructing a check box menu item object; a mnemonic is used if it is found.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param checkBoxMenuItemKey  the key of the menu item
	 * @return                     a check box menu item object
	 */
	protected final JCheckBoxMenuItem constructCheckBoxMenuItem(String checkBoxMenuItemKey)
	{
		return constructCheckBoxMenuItem(checkBoxMenuItemKey,true);
	}

	/**
	 * Sets up custom labels that are shown to the right hand side of the status bar.
	 *
	 * @return an <CODE>ArrayList</CODE> of custom labels, or <CODE>null</CODE> if none are provided
	 */
	protected ArrayList<JLabel> setupStatusBarCustomLabels()
	{
		return null;
	}

	/**
	 * Sets up whether or not the status bar should be shown at the bottom of the application's window.
	 * <P>
	 * Note that this method returns <CODE>true</CODE> by default.
	 *
	 * @return whether or not the status bar should be shown at the bottom of the application's window
	 */
	protected boolean setupIsStatusBarEnabled()
	{
		return true;
	}

	/**
	 * Getter method for the status bar.
	 * 
	 * @return the status bar
	 */
	protected final JStatusBar getStatusBar()
	{
		return fStatusBar;
	}

	/**
	 * Sets up a constructed glass pane.
	 * <P>
	 * An application will typically override this method and create its own glass pane object inside it after returning it.
	 *
	 * @return a constructed glass pane
	 */
	protected JPanel setupGlassPane()
	{
		return null;
	}

	/**
	 * Sets up whether or not a clock (HH:MM:SS) should be shown at the right of the menubar.
	 * <P>
	 * Note that this method returns <CODE>true</CODE> by default.
	 *
	 * @return whether or not a clock (HH:MM:SS) should be shown at the right of the menubar
	 */
	protected boolean setupIsClockEnabled()
	{
		return true;
	}

	/**
	 * Sets up a custom about box.
	 * <P>
	 * Note that this method returns <CODE>null</CODE> by default, indicating that no
	 * about box is available.
	 *
	 * @return the about box dialog
	 * @see    org.sm.smtools.swing.dialogs.JAboutBox
	 */
	protected JAboutBox setupAboutBox()
	{
		return null;
	}

	/**
	 * Sets up whether or not the application is allowed to minimise to the system tray (if supported by the host platform).
	 * <P>
	 * Note that this method returns <CODE>true</CODE> by default.
	 *
	 * @return whether or not the application is allowed to minimise to the system tray
	 */
	protected boolean setupMinimiseToSystemTrayAllowed()
	{
		return true;
	}

	/**
	 * Immediately aborts the running application.
	 *
	 * @param abortMessage  the message to display when aborting
	 * @param appendLocale  optional argument that, when set to <CODE>true</CODE>, appends a localised message that the application is aborted
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
	 * @param lookAndFeel  -
	 * @param silent       -
	 */
	private void setLookAndFeel(String lookAndFeel, boolean silent)
	{
		String previousLookAndFeel = fCurrentLAF;
		fCurrentLAF = translateLookAndFeelName(lookAndFeel);

		try {
			UIManager.setLookAndFeel(fCurrentLAF);
			if (!silent) {
				MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSChangeLookAndFeel,MP3Player.EPlaying.kBlocked);
			}
			SwingUtilities.updateComponentTreeUI(this);
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

		// run the callback
		lookAndFeelChanged();
	}

	/**
	 */
	private void setLookAndFeelMenuItems()
	{
		frbGTK.setSelected(false);
		frbMac.setSelected(false);
		frbMetal.setSelected(false);
		frbMotif.setSelected(false);
		frbNimbus.setSelected(false);
		frbQuaqua.setSelected(false);
		frbWindows.setSelected(false);
		frbWindowsClassic.setSelected(false);

		if (fCurrentLAF.equalsIgnoreCase(klafGTK)) {
			frbGTK.setSelected(true);
		}
		else if (fCurrentLAF.equalsIgnoreCase(klafMac)) {
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
		else if (fCurrentLAF.equalsIgnoreCase(klafQuaqua)) {
			frbQuaqua.setSelected(true);
		}
		else if (fCurrentLAF.equalsIgnoreCase(klafWindows)) {
			frbWindows.setSelected(true);
		}
		else if (fCurrentLAF.equalsIgnoreCase(klafWindowsClassic)) {
			frbWindowsClassic.setSelected(true);
		}
	}

	/**
	 * @param lookAndFeel  -
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
	 * @param  lookAndFeel  -
	 * @return              -
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
	 * @param menuBar  -
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

		JAboutBox aboutBox = setupAboutBox();
		if (aboutBox != null) {
				menuItem = constructMenuItem(kActionCommandMenuItemAbout);
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

			frbGTK = new JRadioButtonMenuItem(I18NL10N.translate(kActionCommandMenuItemGTKLAF));
			frbGTK.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemGTKLAF + ".Mnemonic")));
			frbGTK.setSelected(false);
			frbGTK.setActionCommand(kActionCommandMenuItemGTKLAF);
			frbGTK.addActionListener(this);
			buttonGroup.add(frbGTK);
		subMenu.add(frbGTK);

			frbWindows = new JRadioButtonMenuItem(I18NL10N.translate(kActionCommandMenuItemWindowsLAF));
			frbWindows.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemWindowsLAF + ".Mnemonic")));
			frbWindows.setSelected(false);
			frbWindows.setActionCommand(kActionCommandMenuItemWindowsLAF);
			frbWindows.addActionListener(this);
			buttonGroup.add(frbWindows);
		subMenu.add(frbWindows);

			frbWindowsClassic = new JRadioButtonMenuItem(I18NL10N.translate(kActionCommandMenuItemWindowsClassicLAF));
			frbWindowsClassic.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemWindowsClassicLAF + ".Mnemonic")));
			frbWindowsClassic.setSelected(false);
			frbWindowsClassic.setActionCommand(kActionCommandMenuItemWindowsClassicLAF);
			frbWindowsClassic.addActionListener(this);
			buttonGroup.add(frbWindowsClassic);
		subMenu.add(frbWindowsClassic);

			frbMac = new JRadioButtonMenuItem(I18NL10N.translate(kActionCommandMenuItemMacLAF));
			frbMac.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemMacLAF + ".Mnemonic")));
			frbMac.setSelected(false);
			frbMac.setActionCommand(kActionCommandMenuItemMacLAF);
			frbMac.addActionListener(this);
			buttonGroup.add(frbMac);
		subMenu.add(frbMac);

			frbQuaqua = new JRadioButtonMenuItem(I18NL10N.translate(kActionCommandMenuItemQuaquaLAF));
			frbQuaqua.setMnemonic(I18NL10N.translateMnemonic(I18NL10N.translate(kActionCommandMenuItemQuaquaLAF + ".Mnemonic")));
			frbQuaqua.setSelected(false);
			frbQuaqua.setActionCommand(kActionCommandMenuItemQuaquaLAF);
			frbQuaqua.addActionListener(this);
			buttonGroup.add(frbQuaqua);
		subMenu.add(frbQuaqua);

		subMenu.addSeparator();

			menuItem = constructMenuItem(kActionCommandMenuItemSystemLAF);
			menuItem.setActionCommand(kActionCommandMenuItemSystemLAF);
			menuItem.addActionListener(this);
		subMenu.add(menuItem);

		if (SystemTray.isSupported()) {
			subMenu.addSeparator();

				JCheckBoxMenuItem checkBoxMenuItem = constructCheckBoxMenuItem(kActionCommandMenuItemMinimiseToSystemTray);
				checkBoxMenuItem.setState(true);
				checkBoxMenuItem.setActionCommand(kActionCommandMenuItemMinimiseToSystemTray);
				checkBoxMenuItem.addActionListener(this);
				if (!setupMinimiseToSystemTrayAllowed()) {
					checkBoxMenuItem.setState(false);
					checkBoxMenuItem.setEnabled(false);
				}
			subMenu.add(checkBoxMenuItem);
		}

		if (setupIsGUIResizable()) {
			if (!SystemTray.isSupported()) {
				subMenu.addSeparator();
			}
				fcbEnableWindowResizing = constructCheckBoxMenuItem(kActionCommandMenuItemEnableWindowResizing);
				fcbEnableWindowResizing.setState(true);
				fcbEnableWindowResizing.setActionCommand(kActionCommandMenuItemEnableWindowResizing);
				fcbEnableWindowResizing.addActionListener(this);
			subMenu.add(fcbEnableWindowResizing);
		}

		menu.add(subMenu);

		menu.addSeparator();

			fcbSystemSoundsEnabled = constructCheckBoxMenuItem(kActionCommandMenuItemSystemSoundsEnabled);
			fcbSystemSoundsEnabled.setState(fSystemSoundsEnabled);
			fcbSystemSoundsEnabled.setActionCommand(kActionCommandMenuItemSystemSoundsEnabled);
			fcbSystemSoundsEnabled.addActionListener(this);
		menu.add(fcbSystemSoundsEnabled);

		menu.addSeparator();

			menuItem = constructMenuItem(kActionCommandMenuItemQuit);
			menuItem.setActionCommand(kActionCommandMenuItemQuit);
			menuItem.addActionListener(this);
			menuItem.setAccelerator(KeyStroke.getKeyStroke((int) 'Q',java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menu.add(menuItem);

		// if necessary, add other menus
		ArrayList<JMenu> otherMenus = setupMenus();
		if (otherMenus != null) {
			for (JMenu otherMenu : otherMenus) {
				menuBar.add(otherMenu);
			}
		}

		// if necessary, add right hand menu
		boolean menuBarGlued = false;
		JMenu rightHandMenu = setupRightHandMenu();
		if (rightHandMenu != null) {
			menuBar.add(Box.createHorizontalGlue());
			menuBarGlued = true;
			menuBar.add(rightHandMenu);
		}

		// if necessary, add the clock
		if (setupIsClockEnabled()) {
			if (!menuBarGlued) {
				menuBar.add(Box.createHorizontalGlue());
			}
			menuBar.add(fClockLabel);
		}
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
	 * @param argv  -
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
						DevelopMode.activate();
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
						fSystemSoundsEnabled = false;
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
