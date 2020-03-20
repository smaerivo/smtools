// --------------------------------------------
// Filename      : JStandardGUIApplication.java
// Author        : Sven Maerivoet
// Last modified : 20/03/2020
// Target        : Java VM (1.8)
// --------------------------------------------

/**
 * Copyright 2003-2020 Sven Maerivoet
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
 *     <LI>{@link JStandardGUIApplication#getApplicationRequiredMajorJavaVersion()}</LI>
 *     <LI>{@link JStandardGUIApplication#getApplicationRequiredMinorJavaVersion()}</LI>
 *   </UL>
 *   <LI><B><U>Command-line parameter parsing</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#parseApplicationParameter(int,String)}</LI>
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
 *     <I>As a locale, you can also specify a BCP 47 short name (cf. the
 *     <a href="http://en.wikipedia.org/wiki/IETF_language_tag">IETF's Best Current Practice</a>).<BR>
 *     Note that the application is sized to fullscreen if either width or height or set to reflect this. Fullscreen is this case
 *     this implies window-mode (which is maximised if the OS allows it).</I>
 *   </UL>
 *   <LI><B><U>Custom initialisation and clean-up</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#getApplicationResourceArchiveFilename()}</LI>
 *     <LI>{@link JStandardGUIApplication#getApplicationLocalePrefix()}</LI>
 *     <LI>{@link JStandardGUIApplication#loadApplicationRegistry()}</LI>
 *     <LI>{@link JStandardGUIApplication#initialiseApplication(Object[])}</LI>
 *     <LI>{@link JStandardGUIApplication#shutdownApplication()}</LI>
 *   </UL>
 *   <LI><B><U>Splash screen during startup</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#getGUISplashScreen()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUISplashScreenContent()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUISplashScreenSound()} [<I>see also {@link JSplashScreen} and {@link MP3Player}</I>]</LI>
 *   </UL>
 *   <LI><B><U>Sound set selection</U></B></LI>
 *   <UL>
 *      <LI>{@link JStandardGUIApplication#getGUIInitialSoundSet()}</LI>
 *   </UL>
 *   <LI><B><U>Visual layout (window related)</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#getGUIInitialLookAndFeel()}</LI>
 *     <LI>{@link JStandardGUIApplication#guiLookAndFeelChanged()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUIInitialSize()}</LI>
 *     <LI>{@link JStandardGUIApplication#isGUIResizable()}</LI>
 *     <LI>{@link JStandardGUIApplication#isGUIRepaintedWhenResizing()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUIIcon()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUITitle()}</LI>
 *     <LI>{@link JStandardGUIApplication#isGUIMinimiseToSystemTrayAllowed()}</LI>
 *     <LI>{@link JStandardGUIApplication#setGUIDynamicLayout(boolean)}</LI>
 *   </UL>
 *   <LI><B><U>Visual layout (content related)</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#getGUIContentPane(JPanel)}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUIToolBarTitle()}</LI>
 *     <LI>{@link JStandardGUIApplication#isGUIToolBarFloatable()}</LI>
 *     <LI>{@link JStandardGUIApplication#createGUIToolBar()}</LI>
 *     <LI>{@link JStandardGUIApplication#addGUIToolBarButton(AbstractButton,String,String,ActionListener)}</LI>
 *     <LI>{@link JStandardGUIApplication#addGUIToolBarSeparator()}</LI>
 *     <LI>{@link JStandardGUIApplication#showGUIToolBar()}</LI>
 *     <LI>{@link JStandardGUIApplication#hideGUIToolBar()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUIToolBarInputMap()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUIToolBarActionMap()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUIMenus()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUIRightHandMenu()}</LI>
 *     <LI>{@link JStandardGUIApplication#constructGUIMenuItem(String,boolean)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructGUIMenuItem(String)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructGUIRadioButtonMenuItem(String,boolean)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructGUIRadioButtonMenuItem(String)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructGUICheckBoxMenuItem(String,boolean)}</LI>
 *     <LI>{@link JStandardGUIApplication#constructGUICheckBoxMenuItem(String)}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUIStatusBarCustomLabels()}</LI>
 *     <LI>{@link JStandardGUIApplication#isGUIStatusBarEnabled()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUIStatusBar()}</LI>
 *     <LI>{@link JStandardGUIApplication#getGUIGlassPane()}</LI>
 *     <LI>{@link JStandardGUIApplication#isGUIClockEnabled()}</LI>
 *     <LI>{@link JStandardGUIApplication#hasGUIAboutBox()} [<I>see also {@link JAboutBox}</I>]</LI>
 *     <LI>{@link JStandardGUIApplication#showGUIAboutBox()} [<I>see also {@link JAboutBox}</I>]</LI>
 *     <LI>{@link JStandardGUIApplication#showGUIDefaultMouseCursor()}</LI>
 *     <LI>{@link JStandardGUIApplication#showGUIWaitMouseCursor()}</LI>
 *     <LI>{@link JStandardGUIApplication#hideGUIMouseCursor()}</LI>
 *   </UL>
 *   <LI><B><U>Reacting to user input</U></B></LI>
 *   <UL>
 *     <LI>{@link JStandardGUIApplication#actionPerformed(ActionEvent)}</LI>
 *     <LI>{@link JStandardGUIApplication#guiWindowResized()}</LI>
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
 * As the application is ran, the global system {@link Registry} is read from the file <CODE>registry.ser</CODE>
 * (and stored back to file at the end).
 * <P>
 * When the user wants to quit the application, a confirmation dialog is shown:
 * <P>
 * <IMG src="doc-files/quit-dialog.png" alt="">
 * <P>
 * Note that this confirmation can be skipped if {@link DevelopMode#isActivated} is <CODE>true</CODE>.
 * 
 * @author  Sven Maerivoet
 * @version 20/03/2020
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
	private static final int kApplicationRequiredMajorJavaVersion = 1;
	private static final int kApplicationRequiredMinorJavaVersion = 8;

	// the location of the JAR archive containing all the resources
	private static final String kSystemResourceArchiveFilename = "smtools.jar";

	// the location of the system locale database
	private static final String kSystemLocalePrefix = "smtools-resources/locales/locale-";

	// system registry filename
	private static final String kApplicationRegistryFilename = "registry.ser";

	// default window title
	private static final String kGUIDefaultTitle = "smtools.application.JStandardGUIApplication";

	// default application icon filename
	private static final String kGUIDefaultIconFilename = "smtools-resources/images/icon.jpg";

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
	private static final String kActionCommandMenuItemSoundSetLCARS = "menuItem.SoundSetLCARS";
	private static final String kActionCommandMenuItemSoundSetApple = "menuItem.SoundSetApple";
	private static final String kActionCommandMenuItemSoundSetSpace = "menuItem.SoundSetSpace";
	private static final String kActionCommandMenuItemQuit = "menuItem.Quit";

	// set the clock's update period to half a second
	private static final int kGUIClockUpdatePeriod = 500;

	// switch for first-time initialisation of the system registry
	private static final boolean kCreateApplicationRegistry = false;

	// internal datastructures
	private Registry fApplicationRegistry;
	private int fGUIWidth;
	private int fGUIHeight;
	private String fGUICurrentLAF;
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
	private JRadioButtonMenuItem frbLCARSSoundSet;
	private JRadioButtonMenuItem frbAppleSoundSet;
	private JRadioButtonMenuItem frbSpaceSoundSet;
	private JToolBar fGUIToolBar;
	private JStatusBar fGUIStatusBar;
	private JLabel fGUIClockLabel;
	private String fApplicationLocale;
	private JSplashScreen fGUISplashScreen;
	private Image fGUIIcon;
	private boolean fGUIMinimiseToSystemTray;
	private TrayIcon fGUITrayIcon;
	private JPanel fGUIGlassPane;
	private int fGUIWindowWidth;
	private int fGUIWindowHeight;

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
	 *   <LI>The required version of the Java runtime engine is checked (see {@link JStandardGUIApplication#getApplicationRequiredMajorJavaVersion()} and
	 *       {@link JStandardGUIApplication#getApplicationRequiredMinorJavaVersion()}).</LI>
	 *   <LI>The system {@link JARResources} are loaded.</LI>
	 *   <LI>The GUI is set to auto-size by default (see {@link JStandardGUIApplication#getGUIInitialSize()} and
	 *       {@link JStandardGUIApplication#isGUIResizable()}).</LI>
	 *   <LI>British English is the default language used.</LI>
	 *   <LI>The command-line parameters are parsed (see {@link JStandardGUIApplication#parseApplicationParameter(int,String)}).</LI>
	 *   <LI>The system's locale {@link I18NL10N} database is loaded.</LI>
	 *   <LI>The application's {@link JARResources} and locale {@link I18NL10N} database are loaded (if they are present)
	 *       (see  {@link JStandardGUIApplication#getApplicationResourceArchiveFilename()} and
	 *       {@link JStandardGUIApplication#getApplicationLocalePrefix()}).</LI>
	 *   <LI>The global system {@link Registry} is read from file.</LI>
	 *   <LI>The look-and-feel of the operating system is used by default.</LI>
	 *   <LI>A GUI sound set is selected (see {@link JStandardGUIApplication#getGUIInitialSoundSet()}).</LI>
	 *   <LI>A optional splash screen is shown (see {@link JStandardGUIApplication#getGUISplashScreenContent()} and
	 *       {@link JStandardGUIApplication#getGUISplashScreenSound()}).</LI>
	 *   <LI>Custom initialisation is performed (see  {@link JStandardGUIApplication#loadApplicationRegistry()} and 
	 *       {@link JStandardGUIApplication#initialiseApplication(Object[])}).
	 *       Note that the objects are specified as <CODE>new Object[] {object1,object2}</CODE>.</LI>
	 *   <LI>The window's icon and title are set (see {@link JStandardGUIApplication#getGUIIcon()} and
	 *       {@link JStandardGUIApplication#getGUITitle()}).</LI>
	 *   <LI>The GUI's content pane is constructed (see {@link JStandardGUIApplication#getGUIContentPane(JPanel)}).</LI>
	 *   <LI>The GUI's tool bar is constructed (see {@link JStandardGUIApplication#getGUIToolBarTitle()}, 
	 *       {@link JStandardGUIApplication#isGUIToolBarFloatable()}, {@link JStandardGUIApplication#createGUIToolBar()},
	 *       {@link JStandardGUIApplication#addGUIToolBarButton(AbstractButton,String,String,ActionListener)},
	 *       {@link JStandardGUIApplication#addGUIToolBarSeparator()}, {@link JStandardGUIApplication#showGUIToolBar()},
	 *       {@link JStandardGUIApplication#hideGUIToolBar()}, {@link JStandardGUIApplication#getGUIToolBarInputMap()} and
	 *       {@link JStandardGUIApplication#getGUIToolBarActionMap()}).</LI>
	 *   <LI>The GUI's status bar is constructed (see {@link JStandardGUIApplication#getGUIStatusBarCustomLabels()},
	 *       {@link JStandardGUIApplication#isGUIStatusBarEnabled()}, and {@link JStandardGUIApplication#getGUIStatusBar()}).</LI>
	 *   <LI>The GUI's menu bar is constructed (see {@link JStandardGUIApplication#getGUIMenus()} and
	 *       {@link JStandardGUIApplication#getGUIRightHandMenu()}).</LI>
	 *   <LI>The about box is shown (see {@link JStandardGUIApplication#hasGUIAboutBox()} and {@link JStandardGUIApplication#showGUIAboutBox()}).</LI>
	 *   <LI>The repainting behaviour when resizing is set to dynamic layout (see {@link JStandardGUIApplication#setGUIDynamicLayout(boolean)}).</LI>
	 *   <LI>The application checks if minimisation to the system tray is allowed (see {@link JStandardGUIApplication#isGUIMinimiseToSystemTrayAllowed()}).</LI>
	 *   <LI>The glass pane is constructed (see {@link JStandardGUIApplication#getGUIGlassPane()}).</LI>
	 * </UL>
	 * If no parameters are to be passed to the GUI, specify <CODE>null</CODE>
	 * for <CODE>parameters</CODE>.
	 *
	 * @param argv        an array of strings containing the <B>command-line</B> parameters
	 * @param parameters  an array of objects containing the parameters to be passed to the GUI's
	 *                    {@link JStandardGUIApplication#initialiseApplication(Object[])} method
	 */
	public JStandardGUIApplication(String[] argv, Object[] parameters)
	{
		// check Java runtime version requirements
		kLogger.info("Checking Java runtime environment requirements...");
		String javaRuntimeVersion = System.getProperty("java.version");
		int dotPosition = javaRuntimeVersion.indexOf(".");
		int javaMajorVersion = (Integer.valueOf(javaRuntimeVersion.substring(dotPosition - 1,dotPosition))).intValue();
		int javaMinorVersion = (Integer.valueOf(javaRuntimeVersion.substring(dotPosition + 1,dotPosition + 2))).intValue();
		if ((javaMajorVersion < getApplicationRequiredMajorJavaVersion()) ||
				((javaMajorVersion == getApplicationRequiredMajorJavaVersion()) && (javaMinorVersion < getApplicationRequiredMinorJavaVersion()))) {
			abortApplication("Current version of Java runtime environment (" +
				javaRuntimeVersion +
				") is incompatible with requirements (" +
				String.valueOf(getApplicationRequiredMajorJavaVersion()) + "." + String.valueOf(getApplicationRequiredMinorJavaVersion()) +
				"); application aborted.");
		}

		// load JAR archive containing the system resources
		try {
			kLogger.info("Loading system resources...");
			JARResources.fSystemResources = new JARResources(kSystemResourceArchiveFilename);
		}
		catch (FileNotFoundException exc) {
			abortApplication("Archive (" + kSystemResourceArchiveFilename + ") containing system resources not found; application aborted.");
		}
		catch (FileReadException exc) {
			abortApplication("Error while processing archive (" + kSystemResourceArchiveFilename + ") containing system resources; application aborted.");
		}

		kLogger.info("Starting application...");

		fGUIWidth = kAutoSizeGUI;
		fGUIHeight = kAutoSizeGUI;

		Dimension initialGUISize = getGUIInitialSize();
		if (initialGUISize != null) {
			fGUIWidth = initialGUISize.width;
			fGUIHeight = initialGUISize.height;
		}

		fApplicationLocale = I18NL10N.kLocaleBritishEnglish;
		fSystemSoundsEnabled = getGUISystemSoundsEnabledOnStartup();
		parseApplicationCommandLine(argv);

		// load the system's locale database
		kLogger.info("Loading system locale database [" + fApplicationLocale + "]...");
		try {
			I18NL10N.kINSTANCE.load(JARResources.fSystemResources.getInputStream(I18NL10N.kINSTANCE.getFilename(kSystemLocalePrefix,fApplicationLocale)));
		}
		catch (FileNotFoundException exc) {
			abortApplication("System locale database (" + kSystemLocalePrefix + " + " + fApplicationLocale + ") not found; application aborted.");
		}
		catch (FileReadException exc) {
			abortApplication("Error while processing system locale database (" + kSystemLocalePrefix + " + " + fApplicationLocale + "); application aborted.");
		}

		// load application's resources
		String applicationResourceArchiveFilename = getApplicationResourceArchiveFilename(); 
		try {
			kLogger.info(I18NL10N.kINSTANCE.translate("text.LoadingApplicationResources"));
			fResources = new JARResources(applicationResourceArchiveFilename);
		}
		catch (FileNotFoundException exc) {
			abortApplication(I18NL10N.kINSTANCE.translate("error.ApplicationResourcesArchiveNotFound",applicationResourceArchiveFilename),true);
		}
		catch (FileReadException exc) {
			abortApplication(I18NL10N.kINSTANCE.translate("error.ApplicationResourcesArchiveProcessing",applicationResourceArchiveFilename),true);
		}

		// load application's locale database
		String applicationLocalePrefix = getApplicationLocalePrefix();
		if (applicationLocalePrefix != null) {
			try {
				kLogger.info(I18NL10N.kINSTANCE.translate("text.LoadingApplicationLocaleDatabase"));
				I18NL10N.kINSTANCE.load(fResources.getInputStream(I18NL10N.kINSTANCE.getFilename(applicationLocalePrefix,fApplicationLocale)));
			}
			catch (FileNotFoundException exc) {
				abortApplication(I18NL10N.kINSTANCE.translate("error.ApplicationLocaleDatabaseNotFound",applicationLocalePrefix + " + " + fApplicationLocale),true);
			}
			catch (FileReadException exc) {
				abortApplication(I18NL10N.kINSTANCE.translate("error.ApplicationLocaleDatabaseProcessing",applicationLocalePrefix + " + " + fApplicationLocale),true);
			}
		}

		// obtain a reference to the application registry
		fApplicationRegistry = Registry.kINSTANCE;

		// load (deserialise) the application registry
		kLogger.info(I18NL10N.kINSTANCE.translate("text.LoadingApplicationRegistry"));
		try {
			fApplicationRegistry.load(kApplicationRegistryFilename);
		}
		catch (RegistryException exc) {
			abortApplication(exc.getRegistryError(),true);
		}

		// make the contents of windows dynamic (e.g., resizing background images, ...)
		if (isGUIRepaintedWhenResizing() && ((boolean) Toolkit.getDefaultToolkit().getDesktopProperty("awt.dynamicLayoutSupported"))) {
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
		}

		// set the look-and-feel to that of the platform
		kLogger.info(I18NL10N.kINSTANCE.translate("text.SettingGUILookAndFeel"));
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			fGUICurrentLAF = translateLookAndFeelName(getGUIInitialLookAndFeel());
			UIManager.setLookAndFeel(fGUICurrentLAF);
		}
		catch (Exception exc) {
		}

		// set the GUI sound set
		JGUISounds.kINSTANCE.selectSoundSet(getGUIInitialSoundSet());

		// activate the splash screen
		fGUISplashScreen = new JSplashScreen(getGUISplashScreenContent(),getGUISplashScreenSound());

		// change the cursor to indicate that the user has to wait
		showGUIWaitMouseCursor();

		setTitle(kGUIDefaultTitle);

		// allow custom initialisation
		kLogger.info(I18NL10N.kINSTANCE.translate("text.PerformingCustomInitialisation"));
		loadApplicationRegistry();
		initialiseApplication(parameters);

		kLogger.info(I18NL10N.kINSTANCE.translate("text.CreatingGUIComponents"));
		getGUISplashScreen().setStatusMessage(I18NL10N.kINSTANCE.translate("text.ConstructingGUI"));

		// load the application's icon
		fGUIIcon = getGUIIcon();
		if (fGUIIcon != null) {
			setIconImage(fGUIIcon);
		}

		setTitle(getGUITitle());
		setResizable(isGUIResizable());

		// setup content pane
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());

		// construct the tool bar
		fGUIToolBar = new JToolBar();
			if (getGUIToolBarTitle() != null) {
				fGUIToolBar.setName(getGUIToolBarTitle());
			}
			fGUIToolBar.setFloatable(isGUIToolBarFloatable());
			if (!isGUIToolBarFloatable()) {
				// provide some whitespace to the left edge
				fGUIToolBar.addSeparator(new Dimension(5,0));
			}
			fGUIToolBar.setRollover(true);
			createGUIToolBar();
		contentPane.add(fGUIToolBar,BorderLayout.NORTH);

		// if necessary, add the status bar
		fGUIStatusBar = new JStatusBar(isGUIResizable(),getGUIStatusBarCustomLabels());
		if (isGUIStatusBarEnabled()) {
			contentPane.add(fGUIStatusBar,BorderLayout.SOUTH);

			// create a Swing timer to periodically update the status bar battery level and memory usage labels
			Action updateStatusBarAction = new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
					fGUIStatusBar.updateBatteryLevelAndMemoryUsage();
				}
			};
			new javax.swing.Timer(JStatusBar.kUpdatePeriod,updateStatusBarAction).start();

			// perform the first update when the GUI is displayed
			fGUIStatusBar.updateBatteryLevelAndMemoryUsage();
		}

			JPanel embeddedContentPane = new JPanel();
			getGUIContentPane(embeddedContentPane);
		contentPane.add(embeddedContentPane,BorderLayout.CENTER);

		setContentPane(contentPane);

		// if necessary, add the clock
		if (isGUIClockEnabled()) {
			fGUIClockLabel = new JLabel("",SwingConstants.RIGHT);

			// create a Swing timer to periodically update the clock label
			Action updateClockPanelAction = new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
					updateGUICurrentTimeLabel();
				}
			};
			new javax.swing.Timer(kGUIClockUpdatePeriod,updateClockPanelAction).start();

			// perform the first update when the GUI is displayed
			updateGUICurrentTimeLabel();
		}

		// setup menu bar
		JMenuBar menuBar = new JMenuBar();
		constructGUIMenuBar(menuBar);
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
				Dimension screenSize = getGUIScreenSize();
				Insets screenInsets = getGUIScreenInsets();
				// take into account all the space that a possible OS taskbar tasks
				fGUIWidth = (int) Math.round(screenSize.getWidth() - screenInsets.left - screenInsets.right);
				fGUIHeight = (int) Math.round(screenSize.getHeight() - screenInsets.top - screenInsets.bottom);

/*
				DISABLED DUE TO A BUG IN SWING SHOWING CHILD'S CONTENTS IN THE MAIN CONTENTPANE
				-------------------------------------------------------------------------------

				// check if the OS can set the window to a maximised state
				if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
					setExtendedState(Frame.MAXIMIZED_BOTH);
				}
				else {
					// we approximate the fullscreen GUI ourselves
					setSize(new Dimension(fGUIWidth,fGUIHeight));
				}
*/
				// we approximate the fullscreen GUI ourselves
				setSize(new Dimension(fGUIWidth,fGUIHeight));
			}
			else {
				setSize(new Dimension(fGUIWidth,fGUIHeight));
			}
		}

		if (fGUISplashScreen.isAvailable()) {
			fGUISplashScreen.setVisible(false);
			fGUISplashScreen.dispose();
		}

		// install the callback method for when the window is resized
		fGUIWindowWidth = 0;
		fGUIWindowHeight = 0;
		addComponentListener(this);

		setVisible(true);

		// restore the cursor
		showGUIDefaultMouseCursor();

		// update the menu's radio buttons
		setGUILookAndFeelMenuItems();
		setGUISoundSetMenuItems();

		// check whether or not minimising to the system tray is supported
		fGUIMinimiseToSystemTray = (SystemTray.isSupported() && isGUIMinimiseToSystemTrayAllowed());

		kLogger.info(I18NL10N.kINSTANCE.translate("text.ApplicationReady"));

		// allows real-time repainting of the window's contents upon resizing
		setGUIDynamicLayout(true);

		// setup glass pane
		fGUIGlassPane = getGUIGlassPane();
		if (fGUIGlassPane != null) {
			setGlassPane(fGUIGlassPane);
			fGUIGlassPane.setVisible(false);
			fGUIGlassPane.setOpaque(false);
		}

		// show the aboutbox
		if (!DevelopMode.kINSTANCE.isActivated()) {
			showGUIAboutBox();
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
	 * <PRE>
	 *   super.actionPerformed(e);
	 *   // rest of method's code
	 * </PRE>
	 *
	 * @param e  the <CODE>ActionEvent</CODE> that is received
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		if (command.startsWith("menuItem")) {
			MP3Player.playSystemSound(JGUISounds.kINSTANCE.getMenuItemSoundFilename(),MP3Player.EPlaying.kBlocked);
		}

		if (command.equalsIgnoreCase(kActionCommandMenuItemAbout)) {
			showGUIAboutBox();
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemGTKLAF)) {
			setGUILookAndFeel(klafGTK,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemMacLAF)) {
			setGUILookAndFeel(klafMac,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemMetalLAF)) {
			setGUILookAndFeel(klafMetal,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemMotifLAF)) {
			setGUILookAndFeel(klafMotif,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemNimbusLAF)) {
			setGUILookAndFeel(klafNimbus,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemQuaquaLAF)) {
			setGUILookAndFeel(klafQuaqua,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemWindowsLAF)) {
			setGUILookAndFeel(klafWindows,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemWindowsClassicLAF)) {
			setGUILookAndFeel(klafWindowsClassic,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemSystemLAF)) {
			setGUILookAndFeel(klafSystem,false);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemMinimiseToSystemTray)) {
			fGUIMinimiseToSystemTray = !fGUIMinimiseToSystemTray; 
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
		else if (command.equalsIgnoreCase(kActionCommandMenuItemSoundSetLCARS)) {
			JGUISounds.kINSTANCE.selectSoundSet(JGUISounds.EGUISoundSet.kLCARS);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemSoundSetApple)) {
			JGUISounds.kINSTANCE.selectSoundSet(JGUISounds.EGUISoundSet.kApple);
		}
		else if (command.equalsIgnoreCase(kActionCommandMenuItemSoundSetSpace)) {
			JGUISounds.kINSTANCE.selectSoundSet(JGUISounds.EGUISoundSet.kSpace);
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
	 * This method calls the callback function {@link JStandardGUIApplication#guiWindowResized()}.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e  the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void componentResized(ComponentEvent e)
	{
		// only execute when the window has physically changed in size
		if ((fGUIWindowWidth != getWidth()) || (fGUIWindowHeight != getHeight())) {
			fGUIWindowWidth = getWidth();
			fGUIWindowHeight = getHeight();

			// activate the callback
			guiWindowResized();
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
		if (!DevelopMode.kINSTANCE.isActivated()) {

			if (JConfirmationDialog.confirm(this,I18NL10N.kINSTANCE.translate("text.ConfirmExitApplication"))) {

				kLogger.info(I18NL10N.kINSTANCE.translate("text.RunningApplicationShutdownSequence"));
				shutdownApplication();

				kLogger.info(I18NL10N.kINSTANCE.translate("text.SavingApplicationRegistry"));
				saveApplicationRegistry();

				// quit the running application
				kLogger.info(I18NL10N.kINSTANCE.translate("text.ApplicationTerminated"));
				System.exit(0);
			}
		}
		else {
			kLogger.info(I18NL10N.kINSTANCE.translate("text.RunningApplicationShutdownSequence"));
			shutdownApplication();

			kLogger.info(I18NL10N.kINSTANCE.translate("text.SavingApplicationRegistry"));
			saveApplicationRegistry();

			// quit the running application
			kLogger.info(I18NL10N.kINSTANCE.translate("text.ApplicationTerminated"));
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
		MP3Player.playSystemSound(JGUISounds.kINSTANCE.getWindowEventSoundFilename());
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
		MP3Player.playSystemSound(JGUISounds.kINSTANCE.getWindowEventSoundFilename());
		if (fGUIMinimiseToSystemTray) {
			fGUITrayIcon = new TrayIcon(fGUIIcon,getGUITitle(),null);
			fGUITrayIcon.setImageAutoSize(true);

			MouseListener iconListener = new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setVisible(true);
					SystemTray.getSystemTray().remove(fGUITrayIcon);
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
			fGUITrayIcon.addMouseListener(iconListener);

			try {
				SystemTray.getSystemTray().add(fGUITrayIcon);
				fGUITrayIcon.displayMessage(getGUITitle(),I18NL10N.kINSTANCE.translate("text.RestoreFromSystemTray"),TrayIcon.MessageType.INFO);
				setVisible(false);
			}
			catch (AWTException exc) {
				kLogger.error(I18NL10N.kINSTANCE.translate("error.MinimisingToSystemTray"));
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
	 * <PRE>
	 *   public static void main(String[] argv)
	 *   {
	 *     DerivedGUIApplication derivedGUIApplication = new DerivedGUIApplication(argv);
	 *   }
	 * </PRE>
	 *
	 * @param argv  an array of strings containing the <B>command-line</B> parameters
	 */
	public static void main(String[] argv)
	{
		createApplicationRegistry();
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
	 * Shows the default mouse cursor.
	 */
	protected final void showGUIDefaultMouseCursor()
	{
		setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * Shows the wait mouse cursor.
	 */
	protected final void showGUIWaitMouseCursor()
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	/**
	 * Emulates hiding the mouse cursor.
	 */
	public final void hideGUIMouseCursor()
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
	public final void setGUIDynamicLayout(boolean dynamicLayout)
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
	public final Dimension getGUIScreenSize()
	{
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	/**
	 * Returns the screen insets.
	 *
	 * @return the screen insets
	 */
	public final Insets getGUIScreenInsets()
	{
		return Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * Sets up the required major version of the Java runtime engine that wants to run this application.
	 * <P>
	 * <B>The default is 1 (for JRE 1.8.0).</B>
	 *
	 * @return the required major version of the Java runtime engine
	 */
	protected int getApplicationRequiredMajorJavaVersion()
	{
		return kApplicationRequiredMajorJavaVersion;
	}

	/**
	 * Sets up the required minor version of the Java runtime engine that wants to run this application.
	 * <P>
	 * <B>The default is 8 (for JRE 1.8.0).</B>
	 *
	 * @return the required minor version of the Java runtime engine
	 */
	protected int getApplicationRequiredMinorJavaVersion()
	{
		return kApplicationRequiredMinorJavaVersion;
	}

	/**
	 * Sets up the filename of the JAR or ZIP file containing the application's resources.
	 * <P>
	 * If <CODE>null</CODE> is specified, then the system ignores loading the application's resources.
	 *
	 * @return the filename of the archive (JAR or ZIP) containing the application's resources
	 * @see    org.sm.smtools.application.util.JARResources
	 */
	protected String getApplicationResourceArchiveFilename()
	{
		return null;
	}

	/**
	 * Sets up the path and prefix names to the application's locale databases, for example:<BR>
	 * <PRE>"application-resources/locales/locale-"</PRE>
	 * <P>
	 * If <CODE>null</CODE> is specified, then the system ignores loading the application's locale databases.
	 *
	 * @return the path to the location of the application's locale databases
	 * @see    org.sm.smtools.application.util.I18NL10N
	 */
	protected String getApplicationLocalePrefix()
	{
		return null;
	}

	/**
	 * Allows loading of custom registry values. A typical code example looks like:
	 * <PRE>
	 *   (at)SuppressWarnings("unchecked")
	 *   
	 *   // obtain a local reference to the system registry
	 *   Registry systemRegistry = Registry.kINSTANCE;
	 *   
	 *   // load last opened folder
	 *   Object lastOpenedFolderEntry = systemRegistry.getObject("fLastOpenedFolder");
	 *   if (lastOpenedFolderEntry != null) {
	 *     fLastOpenedFolder = (String) lastOpenedFolderEntry;
	 *   }
	 *   else {
	 *     // setup and store default as the current folder
	 *     fLastOpenedFolder = ".";
	 *     systemRegistry.addObject("fLastOpenedFolder",fLastOpenedFolder);
	 *   }
	 * </PRE>
	 */
	protected void loadApplicationRegistry()
	{
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
	protected void initialiseApplication(Object[] parameters)
	{
	}

	/**
	 * Allows custom clean-up when the application is shutting down. A typical code example could include:
	 * <PRE>
	 *   // obtain a local reference to the system registry
	 *   Registry systemRegistry = Registry.kINSTANCE;
	 *   
	 *   // update registry
	 *   systemRegistry.addObject("fLastOpenedFolder",fLastOpenedFolder);
	 * </PRE>
	 * <I>Note that the <CODE>shutdown()</CODE> sequence is not ran in case of a fatal error resulting in early abort.</I>
	 *
	 * @see JStandardGUIApplication#abortApplication(String,boolean...)
	 */
	protected void shutdownApplication()
	{
	}

	/**
	 * Allows parsing of custom command-line parameters.
	 * <P>
	 * A derived subclass typically overrides this method to parse all its custom command-line parameters.
	 * <P>
	 * Note that if an error occurs during parsing, the {@link JStandardGUIApplication#showApplicationParameterWarning(int,String,String)}
	 * method should be called with the appropriate parameters.
	 * <P>
	 * The following example shows the parsing of one custom parameter (with a custom option):
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
	 *     // indicate that parameter is unknown
	 *     return false;
	 *   }
	 * </PRE>
	 *
	 * @param  paramNr    the number of the parameter that is being parsed
	 * @param  parameter  the unmodified parameter as specified on the command-line
	 * @return            <CODE>true</CODE> if the parameter was parsed successfully, <CODE>false</CODE> otherwise
	 * @see               JStandardGUIApplication#showApplicationParameterWarning(int,String,String)
	 */
	protected boolean parseApplicationParameter(int paramNr, String parameter)
	{
		return false;
	}

	/**
	 * Logs a textual warning message.
	 *
	 * @param paramNr    the number of the parameter that failed to get parsed
	 * @param parameter  the unmodified parameter as specified on the command-line
	 * @param message    the warning message to log
	 * @see              JStandardGUIApplication#parseApplicationParameter(int,String)
	 */
	protected final void showApplicationParameterWarning(int paramNr, String parameter, String message)
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
	protected JLabel getGUISplashScreenContent()
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
	protected InputStream getGUISplashScreenSound()
	{
		return null;
	}

	/**
	 * Returns a handle to the GUI's splash screen.
	 *
	 * @return a handle to the GUI's splash screen
	 * @see    org.sm.smtools.application.util.JSplashScreen
	 */
	protected final JSplashScreen getGUISplashScreen()
	{
		return fGUISplashScreen;
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
	protected String getGUIInitialLookAndFeel()
	{
		return klafInitialLaF;
	}

	/**
	 * Sets up the application's initial GUI sound set.
	 * <P>
	 * The possible values that can be returned are (see also {@link JGUISounds}):<BR>
	 * <BR>
	 * <UL>
	 *   <LI>the LCARS sound set</LI>
	 *   <LI>the Apple sound set</LI>
	 *   <LI>the space sound set</LI>
	 * </UL>
	 * <P>
	 * Note that this method returns the LCARS GUI sound set by default.
	 * 
	 * @return the application's initial GUI sound set
	 */
	protected JGUISounds.EGUISoundSet getGUIInitialSoundSet()
	{
		return JGUISounds.EGUISoundSet.kLCARS;
	}

	/**
	 * Specifies whether or not the GUI's system sounds should be enabled at startup.
	 * <P>
	 * This method returns <CODE>true</CODE> by default.
	 * 
	 * @return a <CODE>boolean</CODE> indicating whether or not the GUI's system sounds should be enabled at startup
	 */
	protected boolean getGUISystemSoundsEnabledOnStartup()
	{
		return true;
	}

	/**
	 * A callback method for when the look-and-feel has changed.
	 */
	protected void guiLookAndFeelChanged()
	{
		guiWindowResized();
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
	 * <PRE>
	 *   return (new Dimension(JStandardGUIApplication.kFullScreenGUI,250));
	 * </PRE>
	 * which specifies a GUI with full screen width and 250 pixels height.
	 *
	 * @return the GUI's initial size on the screen as a <CODE>Dimension</CODE> object
	 */
	protected Dimension getGUIInitialSize()
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
	protected boolean isGUIResizable()
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
	protected boolean isGUIRepaintedWhenResizing()
	{
		return true;
	}

	/**
	 * A callback function for when the GUI's window is resized.
	 */
	protected void guiWindowResized()
	{
	}

	/**
	 * Sets up the <CODE>Image</CODE> containing the GUI's icon.
	 * <P>
	 * The <CODE>Image</CODE> should be a JPG-file, 32x32 pixels with 24-bit colourdepth (i.e., true colour).
	 *
	 * @return the <CODE>Image</CODE> containing the GUI's icon
	 */
	protected Image getGUIIcon()
	{
		try {
			return JARResources.fSystemResources.getImage(kGUIDefaultIconFilename);
		}
		catch (FileNotFoundException exc) {
			return null;
		}
	}

	/**
	 * Sets up the GUI's window title.
	 *
	 * @return the GUI's window title
	 */
	protected String getGUITitle()
	{
		return kGUIDefaultTitle;
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
	protected void getGUIContentPane(JPanel contentPane)
	{
	}

	/**
	 * Returns the GUI's tool bar's title.
	 *
	 * @return the GUI's tool bar's title
	 */
	protected String getGUIToolBarTitle()
	{
		return null;
	}

	/**
	 * Returns if the GUI's tool bar's can float freely.
	 * <P>
	 * By default, this method returns <CODE>true</CODE>.
	 *
	 * @return a <CODE>boolean</CODE> indicating whether or not the GUI's tool bar can float freely
	 */
	protected boolean isGUIToolBarFloatable()
	{
		return true;
	}

	/**
	 * Sets up the GUI's tool bar.
	 * <P>
	 * A derived subclass typically overrides this method. The subclass can add
	 * buttons to the tool bar via the {@link JStandardGUIApplication#addGUIToolBarButton(AbstractButton,String,String,ActionListener)} and
	 * {@link JStandardGUIApplication#addGUIToolBarSeparator()} methods.
	 * <P>
	 * This would typically look like the following:
	 * <PRE>
	 *   String kActionCommandMenuItemXXX = "xxx";
	 *   String kActionCommandMenuItemYYY = "yyy";
	 *   JButton button1 = new JButton(new ImageIcon(fResources.getImage("application-resources/icons/load-parameters-icon.png")));
	 *   JButton button2 = new JButton("Button #2");
	 *   
	 *   addToolBarButton(
	 *     button1,
	 *     I18NL10N.kINSTANCE.translate(kActionCommandMenuItemXXX),
	 *     kActionCommandMenuItemXXX,this);
	 *     
	 *   addToolBarSeparator();
	 *     
	 *   addToolBarButton(
	 *     button2,
	 *     I18NL10N.kINSTANCE.translate(kActionCommandMenuItemYYY),
	 *     kActionCommandMenuItemYYY,this);
	 * </PRE>
	 */
	protected void createGUIToolBar()
	{
	}

	/**
	 * Helper method to add a button to the GUI's tool bar.
	 *
	 * @param button          the button to add
	 * @param toolTipText     the tool tip text associated with the button
	 * @param actionCommand   the action command of the button
	 * @param actionListener  the action listener for the button (usually <CODE>this</CODE>)
	 */
	protected final void addGUIToolBarButton(AbstractButton button, String toolTipText, String actionCommand, ActionListener actionListener)
	{
			button.setToolTipText(toolTipText);
			button.setActionCommand(actionCommand);
			button.addActionListener(actionListener);
		fGUIToolBar.add(button);
	}

	/**
	 * Helper method to add an arbitrary component to the GUI's tool bar.
	 *
	 * @param component    the component to add
	 * @param toolTipText  the tool tip text associated with the component
	 */
	protected final void addGUIToolBarComponent(JComponent component, String toolTipText)
	{
			component.setToolTipText(toolTipText);
		fGUIToolBar.add(component);
	}

	/**
	 * Helper method to add a separator to the GUI's tool bar.
	 */
	protected final void addGUIToolBarSeparator()
	{
		fGUIToolBar.addSeparator();
	}

	/**
	 * Helper method to show the GUI's tool bar.
	 * <P>
	 * Note that the GUI's tool bar is only shown if it contains any content.
	 */
	protected final void showGUIToolBar()
	{
		fGUIToolBar.setVisible(true);
	}

	/**
	 * Helper method to hide the GUI's tool bar.
	 */
	protected final void hideGUIToolBar()
	{
		fGUIToolBar.setVisible(false);
	}

	/**
	 * Helper method to retrieve the GUI's tool bar's input map.
	 * 
	 * @return the GUI's tool bar's input map
	 */
	protected final InputMap getGUIToolBarInputMap()
	{
		return fGUIToolBar.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	/**
	 * Helper method to retrieve the GUI's tool bar's action map.
	 * 
	 * @return the GUI's tool bar's action map
	 */
	protected final ActionMap getGUIToolBarActionMap()
	{
		return fGUIToolBar.getActionMap();
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
	 * @see    JStandardGUIApplication#getGUIRightHandMenu()
	 */
	protected ArrayList<JMenu> getGUIMenus()
	{
		return null;
	}

	/**
	 * Sets up a custom right hand menu (e.g., a "<I>Help</I>" menu).
	 * <P>
	 * Note that this method returns <CODE>null</CODE> by default.
	 *
	 * @return a menu used in the right part of the menubar
	 * @see    JStandardGUIApplication#getGUIMenus()
	 */
	protected JMenu getGUIRightHandMenu()
	{
		return null;
	}

	/**
	 * Helper method for constructing a menu item object; an optional mnemonic can be used if it is found.<BR>
	 * Note that the I18NL10N database is automatically used.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param menuItemKey  the key of the menu item
	 * @param useMnemonic  a flag indicating whether to use a mnemonic or not
	 * @return             a menu item object
	 */
	protected final JMenuItem constructGUIMenuItem(String menuItemKey, boolean useMnemonic)
	{
		String translation = I18NL10N.kINSTANCE.translate(menuItemKey.trim());
		String indentation = StringTools.getIndentation(menuItemKey);

		if (useMnemonic) {
			Integer mnemonic = I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(menuItemKey.trim() + ".Mnemonic"));
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
	 * Helper method constructing a menu item object; a mnemonic is used if it is found.<BR>
	 * Note that the I18NL10N database is automatically used.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param menuItemKey  the key of the menu item
	 * @return             a menu item object
	 */
	protected final JMenuItem constructGUIMenuItem(String menuItemKey)
	{
		return constructGUIMenuItem(menuItemKey,true);
	}

	/**
	 * Helper method for constructing a radio button menu item object; an optional mnemonic can be used if it is found.<BR>
	 * Note that the I18NL10N database is automatically used.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param radioButtonMenuItemKey  the key of the menu item
	 * @param useMnemonic             a flag indicating whether to use a mnemonic or not
	 * @return                        a radio button menu item object
	 */
	protected final JRadioButtonMenuItem constructGUIRadioButtonMenuItem(String radioButtonMenuItemKey, boolean useMnemonic)
	{
		String translation = I18NL10N.kINSTANCE.translate(radioButtonMenuItemKey.trim());
		String indentation = StringTools.getIndentation(radioButtonMenuItemKey);

		JRadioButtonMenuItem radioButtonMenuItem = new JRadioButtonMenuItem(indentation + translation);

		if (useMnemonic) {
			Integer mnemonic = I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(radioButtonMenuItemKey.trim() + ".Mnemonic"));

			if (mnemonic != null) {
				radioButtonMenuItem.setMnemonic(mnemonic);
			}
		}

		return radioButtonMenuItem;
	}

	/**
	 * Helper method for constructing a radio button menu item object; a mnemonic is used if it is found.<BR>
	 * Note that the I18NL10N database is automatically used.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param radioButtonMenuItemKey  the key of the menu item
	 * @return                        a radio button menu item object
	 */
	protected final JRadioButtonMenuItem constructGUIRadioButtonMenuItem(String radioButtonMenuItemKey)
	{
		return constructGUIRadioButtonMenuItem(radioButtonMenuItemKey,true);
	}

	/**
	 * Helper method for constructing a check box menu item object; an optional mnemonic can be used if it is found.<BR>
	 * Note that the I18NL10N database is automatically used.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param checkBoxMenuItemKey  the key of the menu item
	 * @param useMnemonic          a flag indicating whether to use a mnemonic or not
	 * @return                     a check box menu item object
	 */
	protected final JCheckBoxMenuItem constructGUICheckBoxMenuItem(String checkBoxMenuItemKey, boolean useMnemonic)
	{
		String translation = I18NL10N.kINSTANCE.translate(checkBoxMenuItemKey.trim());
		String indentation = StringTools.getIndentation(checkBoxMenuItemKey);

		JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(indentation + translation);

		if (useMnemonic) {
			Integer mnemonic = I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(checkBoxMenuItemKey.trim() + ".Mnemonic"));

			if (mnemonic != null) {
				checkBoxMenuItem.setMnemonic(mnemonic);
			}
		}

		return checkBoxMenuItem;
	}

	/**
	 * Helper method for constructing a check box menu item object; a mnemonic is used if it is found.<BR>
	 * Note that the I18NL10N database is automatically used.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param checkBoxMenuItemKey  the key of the menu item
	 * @return                     a check box menu item object
	 */
	protected final JCheckBoxMenuItem constructGUICheckBoxMenuItem(String checkBoxMenuItemKey)
	{
		return constructGUICheckBoxMenuItem(checkBoxMenuItemKey,true);
	}

	/**
	 * Sets up custom labels that are shown to the right hand side of the status bar.
	 *
	 * @return an <CODE>ArrayList</CODE> of custom labels, or <CODE>null</CODE> if none are provided
	 */
	protected ArrayList<JLabel> getGUIStatusBarCustomLabels()
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
	protected boolean isGUIStatusBarEnabled()
	{
		return true;
	}

	/**
	 * Getter method for the status bar.
	 * 
	 * @return the status bar
	 */
	protected final JStatusBar getGUIStatusBar()
	{
		return fGUIStatusBar;
	}

	/**
	 * Sets up a constructed glass pane.
	 * <P>
	 * An application will typically override this method and create its own glass pane object inside it after returning it.
	 *
	 * @return a constructed glass pane
	 */
	protected JPanel getGUIGlassPane()
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
	protected boolean isGUIClockEnabled()
	{
		return true;
	}

	/**
	 * Returns a <CODE>boolean</CODE> indicating whether or not a custom about box is available.
	 * <P>
	 * Note that this method returns <CODE>false</CODE> by default, indicating that no custom
	 * about box is available.
	 *
	 * @return a <CODE>boolean</CODE> indicating whether or not a custom about box is available
	 * @see    org.sm.smtools.swing.dialogs.JAboutBox
	 */
	protected boolean hasGUIAboutBox()
	{
		return false;
	}

	/**
	 * Shows a custom about box.
	 *
	 * @see org.sm.smtools.swing.dialogs.JAboutBox
	 */
	protected void showGUIAboutBox()
	{
	}

	/**
	 * Sets up whether or not the application is allowed to minimise to the system tray (if supported by the host platform).
	 * <P>
	 * Note that this method returns <CODE>true</CODE> by default.
	 *
	 * @return whether or not the application is allowed to minimise to the system tray
	 */
	protected boolean isGUIMinimiseToSystemTrayAllowed()
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
				kLogger.fatal(abortMessage + "\n" + I18NL10N.kINSTANCE.translate("text.ApplicationAborted"));
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
	private void setGUILookAndFeel(String lookAndFeel, boolean silent)
	{
		String previousLookAndFeel = fGUICurrentLAF;
		fGUICurrentLAF = translateLookAndFeelName(lookAndFeel);

		try {
			UIManager.setLookAndFeel(fGUICurrentLAF);
			if (!silent) {
				MP3Player.playSystemSound(JGUISounds.kINSTANCE.getChangeLookAndFeelSoundFilename(),MP3Player.EPlaying.kBlocked);
			}
			SwingUtilities.updateComponentTreeUI(this);
			repaint();
			String message = I18NL10N.kINSTANCE.translate("text.GUILookAndFeelAdjusted",fGUICurrentLAF);
			kLogger.info(message);
			if (!silent) {
				JMessageDialog.show(this,message);
			}
		}
		catch (Exception exc) {
			String message = I18NL10N.kINSTANCE.translate("error.LAFNotFound",fGUICurrentLAF);
			kLogger.error(message);
			if (!silent) {
				JWarningDialog.warn(this,message);
			}
			fGUICurrentLAF = previousLookAndFeel;
		}

		setGUILookAndFeelMenuItems();

		// run the callback
		guiLookAndFeelChanged();
	}

	/**
	 */
	private void setGUILookAndFeelMenuItems()
	{
		frbGTK.setSelected(false);
		frbMac.setSelected(false);
		frbMetal.setSelected(false);
		frbMotif.setSelected(false);
		frbNimbus.setSelected(false);
		frbQuaqua.setSelected(false);
		frbWindows.setSelected(false);
		frbWindowsClassic.setSelected(false);

		if (fGUICurrentLAF.equalsIgnoreCase(klafGTK)) {
			frbGTK.setSelected(true);
		}
		else if (fGUICurrentLAF.equalsIgnoreCase(klafMac)) {
			frbMac.setSelected(true);
		}
		else if (fGUICurrentLAF.equalsIgnoreCase(klafMetal)) {
			frbMetal.setSelected(true);
		}
		else if (fGUICurrentLAF.equalsIgnoreCase(klafMotif)) {
			frbMotif.setSelected(true);
		}
		else if (fGUICurrentLAF.equalsIgnoreCase(klafNimbus)) {
			frbNimbus.setSelected(true);
		}
		else if (fGUICurrentLAF.equalsIgnoreCase(klafQuaqua)) {
			frbQuaqua.setSelected(true);
		}
		else if (fGUICurrentLAF.equalsIgnoreCase(klafWindows)) {
			frbWindows.setSelected(true);
		}
		else if (fGUICurrentLAF.equalsIgnoreCase(klafWindowsClassic)) {
			frbWindowsClassic.setSelected(true);
		}
	}

	/**
	 */
	private void setGUISoundSetMenuItems()
	{
		frbLCARSSoundSet.setSelected(false);
		frbAppleSoundSet.setSelected(false);
		frbSpaceSoundSet.setSelected(false);

		if (JGUISounds.kINSTANCE.getCurrentSoundSet() == JGUISounds.EGUISoundSet.kLCARS) {
			frbLCARSSoundSet.setSelected(true);
		}
		else if (JGUISounds.kINSTANCE.getCurrentSoundSet() == JGUISounds.EGUISoundSet.kApple) {
			frbAppleSoundSet.setSelected(true);
		}
		else if (JGUISounds.kINSTANCE.getCurrentSoundSet() == JGUISounds.EGUISoundSet.kSpace) {
			frbSpaceSoundSet.setSelected(true);
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
	private void constructGUIMenuBar(JMenuBar menuBar)
	{
		JMenu menu = null;
		JMenu subMenu = null;
		JMenuItem menuItem = null;
		ButtonGroup buttonGroup = null;

		// the general menu
		menu = new JMenu(I18NL10N.kINSTANCE.translate("menu.General"));
		menu.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate("menu.General.Mnemonic")));
		menuBar.add(menu);

		if (hasGUIAboutBox()) {
				menuItem = constructGUIMenuItem(kActionCommandMenuItemAbout);
				menuItem.setActionCommand(kActionCommandMenuItemAbout);
				menuItem.addActionListener(this);
				menuItem.setAccelerator(KeyStroke.getKeyStroke((int) 'A',java.awt.event.InputEvent.CTRL_DOWN_MASK));
			menu.add(menuItem);

			menu.addSeparator();
		}

		// the look-and-feel submenu
		subMenu = new JMenu(I18NL10N.kINSTANCE.translate("menu.LookAndFeel"));
		subMenu.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate("menu.LookAndFeel.Mnemonic")));

			buttonGroup = new ButtonGroup();
			frbMetal = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemMetalLAF));
			frbMetal.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemMetalLAF + ".Mnemonic")));
			frbMetal.setSelected(false);
			frbMetal.setActionCommand(kActionCommandMenuItemMetalLAF);
			frbMetal.addActionListener(this);
			buttonGroup.add(frbMetal);
		subMenu.add(frbMetal);

			frbNimbus = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemNimbusLAF));
			frbNimbus.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemNimbusLAF + ".Mnemonic")));
			frbNimbus.setSelected(false);
			frbNimbus.setActionCommand(kActionCommandMenuItemNimbusLAF);
			frbNimbus.addActionListener(this);
			buttonGroup.add(frbNimbus);
		subMenu.add(frbNimbus);

			frbMotif = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemMotifLAF));
			frbMotif.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemMotifLAF + ".Mnemonic")));
			frbMotif.setSelected(false);
			frbMotif.setActionCommand(kActionCommandMenuItemMotifLAF);
			frbMotif.addActionListener(this);
			buttonGroup.add(frbMotif);
		subMenu.add(frbMotif);

			frbGTK = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemGTKLAF));
			frbGTK.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemGTKLAF + ".Mnemonic")));
			frbGTK.setSelected(false);
			frbGTK.setActionCommand(kActionCommandMenuItemGTKLAF);
			frbGTK.addActionListener(this);
			buttonGroup.add(frbGTK);
		subMenu.add(frbGTK);

			frbWindows = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemWindowsLAF));
			frbWindows.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemWindowsLAF + ".Mnemonic")));
			frbWindows.setSelected(false);
			frbWindows.setActionCommand(kActionCommandMenuItemWindowsLAF);
			frbWindows.addActionListener(this);
			buttonGroup.add(frbWindows);
		subMenu.add(frbWindows);

			frbWindowsClassic = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemWindowsClassicLAF));
			frbWindowsClassic.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemWindowsClassicLAF + ".Mnemonic")));
			frbWindowsClassic.setSelected(false);
			frbWindowsClassic.setActionCommand(kActionCommandMenuItemWindowsClassicLAF);
			frbWindowsClassic.addActionListener(this);
			buttonGroup.add(frbWindowsClassic);
		subMenu.add(frbWindowsClassic);

			frbMac = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemMacLAF));
			frbMac.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemMacLAF + ".Mnemonic")));
			frbMac.setSelected(false);
			frbMac.setActionCommand(kActionCommandMenuItemMacLAF);
			frbMac.addActionListener(this);
			buttonGroup.add(frbMac);
		subMenu.add(frbMac);

			frbQuaqua = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemQuaquaLAF));
			frbQuaqua.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemQuaquaLAF + ".Mnemonic")));
			frbQuaqua.setSelected(false);
			frbQuaqua.setActionCommand(kActionCommandMenuItemQuaquaLAF);
			frbQuaqua.addActionListener(this);
			buttonGroup.add(frbQuaqua);
		subMenu.add(frbQuaqua);

		subMenu.addSeparator();

			menuItem = constructGUIMenuItem(kActionCommandMenuItemSystemLAF);
			menuItem.setActionCommand(kActionCommandMenuItemSystemLAF);
			menuItem.addActionListener(this);
		subMenu.add(menuItem);

		if (SystemTray.isSupported()) {
			subMenu.addSeparator();

				JCheckBoxMenuItem checkBoxMenuItem = constructGUICheckBoxMenuItem(kActionCommandMenuItemMinimiseToSystemTray);
				checkBoxMenuItem.setState(true);
				checkBoxMenuItem.setActionCommand(kActionCommandMenuItemMinimiseToSystemTray);
				checkBoxMenuItem.addActionListener(this);
				if (!isGUIMinimiseToSystemTrayAllowed()) {
					checkBoxMenuItem.setState(false);
					checkBoxMenuItem.setEnabled(false);
				}
			subMenu.add(checkBoxMenuItem);
		}

		if (isGUIResizable()) {
			if (!SystemTray.isSupported()) {
				subMenu.addSeparator();
			}
				fcbEnableWindowResizing = constructGUICheckBoxMenuItem(kActionCommandMenuItemEnableWindowResizing);
				fcbEnableWindowResizing.setState(true);
				fcbEnableWindowResizing.setActionCommand(kActionCommandMenuItemEnableWindowResizing);
				fcbEnableWindowResizing.addActionListener(this);
			subMenu.add(fcbEnableWindowResizing);
		}

		menu.add(subMenu);

		// the sound set submenu
		subMenu = new JMenu(I18NL10N.kINSTANCE.translate("menu.SoundSet"));
		subMenu.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate("menu.SoundSet.Mnemonic")));

			fcbSystemSoundsEnabled = constructGUICheckBoxMenuItem(kActionCommandMenuItemSystemSoundsEnabled);
			fcbSystemSoundsEnabled.setState(fSystemSoundsEnabled);
			fcbSystemSoundsEnabled.setActionCommand(kActionCommandMenuItemSystemSoundsEnabled);
			fcbSystemSoundsEnabled.addActionListener(this);
		subMenu.add(fcbSystemSoundsEnabled);

		subMenu.addSeparator();

		buttonGroup = new ButtonGroup();
			frbLCARSSoundSet = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemSoundSetLCARS));
			frbLCARSSoundSet.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemSoundSetLCARS + ".Mnemonic")));
			frbLCARSSoundSet.setSelected(true);
			frbLCARSSoundSet.setActionCommand(kActionCommandMenuItemSoundSetLCARS);
			frbLCARSSoundSet.addActionListener(this);
			buttonGroup.add(frbLCARSSoundSet);
		subMenu.add(frbLCARSSoundSet);

			frbAppleSoundSet = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemSoundSetApple));
			frbAppleSoundSet.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemSoundSetApple + ".Mnemonic")));
			frbAppleSoundSet.setSelected(false);
			frbAppleSoundSet.setActionCommand(kActionCommandMenuItemSoundSetApple);
			frbAppleSoundSet.addActionListener(this);
			buttonGroup.add(frbAppleSoundSet);
		subMenu.add(frbAppleSoundSet);

			frbSpaceSoundSet = new JRadioButtonMenuItem(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemSoundSetSpace));
			frbSpaceSoundSet.setMnemonic(I18NL10N.kINSTANCE.translateMnemonic(I18NL10N.kINSTANCE.translate(kActionCommandMenuItemSoundSetSpace + ".Mnemonic")));
			frbSpaceSoundSet.setSelected(false);
			frbSpaceSoundSet.setActionCommand(kActionCommandMenuItemSoundSetSpace);
			frbSpaceSoundSet.addActionListener(this);
			buttonGroup.add(frbSpaceSoundSet);
		subMenu.add(frbSpaceSoundSet);

		menu.add(subMenu);

		menu.addSeparator();

			menuItem = constructGUIMenuItem(kActionCommandMenuItemQuit);
			menuItem.setActionCommand(kActionCommandMenuItemQuit);
			menuItem.addActionListener(this);
			menuItem.setAccelerator(KeyStroke.getKeyStroke((int) 'Q',java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menu.add(menuItem);

		// if necessary, add other menus
		ArrayList<JMenu> otherMenus = getGUIMenus();
		if (otherMenus != null) {
			for (JMenu otherMenu : otherMenus) {
				menuBar.add(otherMenu);
			}
		}

		// if necessary, add right hand menu
		boolean menuBarGlued = false;
		JMenu rightHandMenu = getGUIRightHandMenu();
		if (rightHandMenu != null) {
			menuBar.add(Box.createHorizontalGlue());
			menuBarGlued = true;
			menuBar.add(rightHandMenu);
		}

		// if necessary, add the clock
		if (isGUIClockEnabled()) {
			if (!menuBarGlued) {
				menuBar.add(Box.createHorizontalGlue());
			}
			menuBar.add(fGUIClockLabel);
		}
	}

	/**
	 */
	private void updateGUICurrentTimeLabel()
	{
 		DateStamp currentDate = new DateStamp();
 		TimeStamp currentTime = new TimeStamp();
		fGUIClockLabel.setText("[ " + currentDate.getDMYString() + " " + currentTime.getHMSString() + " ] "); // provide right-side padding
	}

	/**
	 * @param argv  -
	 */
	private void parseApplicationCommandLine(String[] argv)
	{
		if (argv.length > 0) {

			// parse parameters
			for (int paramNr = 1; paramNr <= argv.length; ++paramNr) {

				String parameter = argv[paramNr - 1];
				if (!parameter.startsWith("-")) {
					showApplicationParameterWarning(paramNr,parameter,"it doesn't start with a hyphen");
				}
				else {

					// remove the leading hypen and convert to upper case
					parameter = parameter.substring(1);
					String upperCaseParameter = parameter.toUpperCase();

					// parse parameters
					if (upperCaseParameter.startsWith(kParamLocale + "=")) {

						String option = upperCaseParameter.substring(kParamLocale.length() + 1);
						if (option.equalsIgnoreCase(kParamLocaleDutch)) {
							fApplicationLocale = I18NL10N.kLocaleDutch;
						}
						else if (option.equalsIgnoreCase(kParamLocaleBritishEnglish)) {
							fApplicationLocale = I18NL10N.kLocaleBritishEnglish;
						}
						else if (option.equalsIgnoreCase(kParamLocaleAmericanEnglish)) {
							fApplicationLocale = I18NL10N.kLocaleAmericanEnglish;
						}
						else {
							// user-specified locale
							fApplicationLocale = parameter.substring(kParamLocale.length() + 1);
						}
					}
					else if (upperCaseParameter.equalsIgnoreCase(kParamDevelopMode)) {
						DevelopMode.kINSTANCE.activate();
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
								showApplicationParameterWarning(paramNr,parameter,"incorrect width specified");
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
								showApplicationParameterWarning(paramNr,parameter,"incorrect height specified");
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
					else if (!parseApplicationParameter(paramNr,parameter)) {
						showApplicationParameterWarning(paramNr,parameter,"unknown parameter specified");
					}
				}
			}
		}
	}

	/**
	 */
	private void saveApplicationRegistry()
	{
		try {
			fApplicationRegistry.save(kApplicationRegistryFilename);
		}
		catch (RegistryException exc) {
			JWarningDialog.warn(this,exc.getRegistryError());
		}
	}

	/**
	 */
	private static void createApplicationRegistry()
	{
		if (kCreateApplicationRegistry) {
			kLogger.info("Creating application registry...");

			try {
				Registry registry = Registry.kINSTANCE;
				registry.clear();
				registry.save(kApplicationRegistryFilename);
				kLogger.info("Done, application stopped.");
			}
			catch (Exception exc) {
				kLogger.fatal(exc);
			}
			System.exit(0);
		}
	}
}
