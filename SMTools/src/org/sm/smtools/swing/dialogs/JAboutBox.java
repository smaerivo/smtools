// ------------------------------
// Filename      : JAboutBox.java
// Author        : Sven Maerivoet
// Last modified : 03/03/2021
// Target        : Java VM (1.8)
// ------------------------------

/**
 * Copyright 2003-2018 Sven Maerivoet
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
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.math.*;
import org.sm.smtools.util.*;

/**
 * The <CODE>JAboutBox</CODE> class provides a modal dialog box containing general
 * application information.
 * <P>
 * Note that a valid {@link I18NL10N} database must be available!
 * <P>
 * The dialog box is <I>modal</I>, <I>non-resizable</I> and contains an <I>"Ok" button</I>
 * to close it. Here's an example of a complete about box (Microsoft Windows L&amp;F):
 * <P>
 * <IMG src="doc-files/about-box-about-windows.png" alt="">
 * <P>
 * As seen in the above image, there can be up to four different tabs: the first tab contains the
 * <B>application's logo and its accompanying about text</B>, the second tab contains a <B>copyright
 * notice</B> (see second image below), the third tab contains the <B>licence information</B> (see third
 * image below) and the fourth tab contains the <B>author's affiliations</B> (see third image below).
 * <P>
 * <IMG src="doc-files/about-box-copyright-windows.png" alt="">
 * <P>
 * <IMG src="doc-files/about-box-licence-windows.png" alt="">
 * <P>
 * <IMG src="doc-files/about-box-affiliations-windows.png" alt="">
 * <P>
 * Typically, <CODE>JAboutBox</CODE> is subclassed, with several methods overridden, allow
 * customisation of each of the previously shown four tabs. The overrideable methods that control
 * these aspects of the visual layout of the dialog box are:
 * <UL>
 *   <LI>{@link JAboutBox#setupLogo}</LI>
 *   <LI>{@link JAboutBox#setupLogoPosition}</LI>
 *   <LI>{@link JAboutBox#setupAboutText}</LI>
 *   <LI>{@link JAboutBox#setupCopyrightContent}</LI>
 *   <LI>{@link JAboutBox#setupLicenceContent}</LI>
 *   <LI>{@link JAboutBox#setupAffiliationsLabels}</LI>
 *   <LI>{@link JAboutBox#setupUsedLibrariesDescriptions}</LI>
 * </UL>
 * <P>
 * In the first tab, the amount of free memory available to the Java Virtual Machine is also shown.
 * <P>
 * Finally, if either {@link JAboutBox#setupCopyrightContent}, {@link JAboutBox#setupLicenceContent},
 * or {@link JAboutBox#setupAffiliationsLabels} returns <CODE>null</CODE>, then its corresponding
 * tab is <B>not</B> shown.
 * 
 * @author  Sven Maerivoet
 * @version 03/03/2021
 */
public class JAboutBox extends JDefaultDialog
{
	/**
	 * Useful constants to specify the logo's position relative to its accompanying about text.
	 */
	public static enum ELogoPosition {
		/**
		 * Place the logo in a top position.
		 */
		kTop,

		/**
		 * Place the logo in a left position.
		 */
		kLeft};

	/**
	 * Provides access to an application's resources.
	 */
	protected JARResources fResources;
	
	// some constraints on the visual layout
	private static final int kTextAreaInitialNrOfRows = 20;
	private static final int kTextAreaExcessScrollPaneSpace = 30;

	// the names of the files containing the animation images
	private static final String kJavaCupImageFilename = "smtools-resources/images/javacup-animated.gif";
	private static final String kSwingImageFilename = "smtools-resources/images/swing-animated.gif";

	// internal datastructures
	private static final int kSwingWidth = 64 + 8; // leave 8 pixel border
	private static final int kSwingHeight = 100 + 20 + 8; // leave 8 pixel border
	private static final int kJavaCupWidth = 64 + 8; // leave 8 pixel border
	private static final int kJavaCupHeight = 100 + 20 + 8; // leave 8 pixel border

	// internal datastructures
	private JTabbedPane fTabbedPane;
	private JLabel fAboutTextLabel;
	private JTextArea fLicenceTextArea;
	private JScrollPane fLicenceScrollPane;
	private JScrollPane fAffiliationsScrollPane;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JAboutBox</CODE> object.
	 * <P>
	 * The about box is inactive at the end of the constructor, so it should explicitly
	 * shown using the {@link JDefaultDialog#activate} method.<BR> 
	 * It's also modal, has a fixed size and a standard "Ok" button.
	 *
	 * @param owner  the owner of the frame in which this about box is to be displayed
	 */
	public JAboutBox(JFrame owner)
	{
		this(owner,null);
	}

	/**
	 * Constructs a <CODE>JAboutBox</CODE> object.
	 * <P>
	 * The about box is inactive at the end of the constructor, so it should explicitly
	 * shown using the {@link JDefaultDialog#activate} method.<BR> 
	 * It's also modal, has a fixed size and a standard "Ok" button.
	 *
	 * @param owner      the owner of the frame in which this about box is to be displayed
	 * @param resources  the parent application's resources that can be accessed by this <CODE>JAboutBox</CODE>
	 */
	public JAboutBox(JFrame owner, JARResources resources)
	{
		super(owner,
			JDefaultDialog.EModality.kModal,
			JDefaultDialog.ESize.kFixedSize,
			JDefaultDialog.EType.kOk,
			new Object[] {resources});
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * Sets up a <CODE>JLabel</CODE> containing the application's logo.
	 * <P>
	 * A typical logo can span up to 500x200 pixels (when positioned at the top) or
	 * 200x300 pixels (when positioned at the left).
	 * <P>
	 * This method returns <CODE>null</CODE> by default, so in order to
	 * obtain a custom logo, the caller should override this method.
	 *
	 * @return a <CODE>JLabel</CODE> containing the application's logo
	 */
	protected JLabel setupLogo()
	{
		return null;
	}

	/**
	 * Sets up an <CODE>ELogoPosition</CODE> indicating where the application's logo should
	 * be relative to its accompanying about text.
	 * <P>
	 * See also {@link JAboutBox.ELogoPosition}.
	 * <P>
	 * The default is {@link JAboutBox.ELogoPosition#kTop}.
	 *
	 * @return an <CODE>ELogoPosition</CODE> indicating the application's logo's position
	 */
	protected ELogoPosition setupLogoPosition()
	{
		return ELogoPosition.kTop;
	}

	/**
	 * Sets up a <CODE>String</CODE> containing the about text displayed together
	 * with the application's logo.
	 * <P>
	 * In order to have some control over the copyright notice's layout, HTML tags
	 * are allowed (except the starting &lt;HTML&gt; and ending &lt;/HTML&gt; tags which	 
	 * are implicitly given by the about box).
	 * <P>
	 * This method returns <CODE>null</CODE> by default, so in order to
	 * obtain a custom text, the caller should override this method.
	 *
	 * @return a <CODE>String</CODE> containing the about text
	 */
	protected String setupAboutText()
	{
		return null;
	}

	/**
	 * Sets up a <CODE>StringBuilder</CODE> containing the <I>short</I> application's copyright notice.
	 * <P>
	 * In order to have some control over the copyright notice's layout, HTML tags
	 * are allowed (except the starting &lt;HTML&gt; and ending &lt;/HTML&gt; tags which	 
	 * are implicitly given by the about box).
	 * <P>
	 * This method returns <CODE>null</CODE> by default, so in order to
	 * obtain a custom copyright notice, the caller should override this method.
	 * <P>
	 * If no explicit content is given, the tab containing the copyright notice will
	 * not be displayed in the about box. 
	 *
	 * @return a <CODE>StringBuffer</CODE> containing the application's copyright notice
	 */
	protected StringBuilder setupCopyrightContent()
	{
		return null;
	}

	/**
	 * Sets up a <CODE>StringBuilder</CODE> containing the (long) application's licence text (e.g., the Apache Licence).
	 * <P>
	 * This method returns <CODE>null</CODE> by default, so in order to
	 * obtain a custom licence text, the caller should override this method.
	 * <P>
	 * <B>Note that the content should only be plain text without HTML tages</B>.
	 * <P>
	 * If no explicit content is given, the tab containing the licence text will
	 * not be displayed in the about box. 
	 *
	 * @return a <CODE>StringBuffer</CODE> containing the application's licence text
	 */
	protected StringBuilder setupLicenceContent()
	{
		return null;
	}

	/**
	 * Sets up a list of <CODE>JLabel</CODE>s containing the author's affiliations.
	 * <P>
	 * This method returns <CODE>null</CODE> by default, so in order to
	 * obtain custom affiliations, the caller should override this method.
	 * <P>
	 * The given affiliations are <CODE>JLabel</CODE>s that can contain text and/or images.
	 * <P>
	 * If no explicit affiliations are given, the tab containing them will
	 * not be displayed in the about box. 
	 *
	 * @return a list of <CODE>JLabel</CODE>s containing the author's affiliations
	 */
	protected ArrayList<JLabel> setupAffiliationsLabels()
	{
		return null;
	}

	/**
	 * Sets up a <CODE>String</CODE> describing the application's used libraries.
	 * <P>
	 * This method returns <CODE>null</CODE> by default, so in order to
	 * obtain custom affiliations, the caller should override this method.
	 *
	 * @return a <CODE>String</CODE> describing the application's used libraries
	 */
	protected String setupUsedLibrariesDescriptions()
	{
		return null;
	}

	/**
	 * Performs custom initialisation of the about box's member fields.
	 * <P>
	 * <B>Note that this method cannot be overridden!</B>
	 *
	 * @param parameters  internally fixed to the parent application's resources
	 */
	@Override
	protected final void initialiseClass(Object[] parameters)
	{
		// give derived subclasses access to their parent application's resources
		if (parameters.length > 0) {
			fResources = (JARResources) parameters[0];
		}
	}

	/**
	 * Sets up the window title of the about box.
	 * <P>
	 * <B>Note that this method cannot be overridden!</B>
	 */
	@Override
	protected final String setupWindowTitle()
	{
		return I18NL10N.kINSTANCE.translate("text.AboutBox.DialogTitle");
	}

	/**
	 * Sets up the about box content area.
	 * <P>
	 * <B>Note that this method cannot be overridden!</B>
	 */
	@Override
	protected final void setupMainPanel(JPanel mainPanel)
	{
		fTabbedPane = new JTabbedPane();

		// create the about pane
		JPanel aboutPane = new JPanel();
		Dimension customSpacing = null;
		if (setupLogoPosition() == ELogoPosition.kTop) {
			aboutPane.setLayout(new BoxLayout(aboutPane,BoxLayout.Y_AXIS));
			customSpacing = new Dimension(0,10);
		}
		else if (setupLogoPosition() == ELogoPosition.kLeft) {
			aboutPane.setLayout(new BoxLayout(aboutPane,BoxLayout.X_AXIS));
			customSpacing = new Dimension(10,0);
		}
		aboutPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		aboutPane.setAlignmentY(Component.TOP_ALIGNMENT);
		aboutPane.setBorder(new EmptyBorder(0,10,10,10));
		aboutPane.add(Box.createRigidArea(customSpacing));
		JLabel logo = setupLogo();
		if (logo != null) {
			aboutPane.add(logo);
			logo.setAlignmentX(Component.LEFT_ALIGNMENT);
			logo.setAlignmentY(Component.TOP_ALIGNMENT);
			aboutPane.add(Box.createRigidArea(customSpacing));
		}
		fAboutTextLabel = new JLabel("");
		fAboutTextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		fAboutTextLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		updateAboutTextLabel();		
		aboutPane.add(fAboutTextLabel);
		fTabbedPane.addTab(I18NL10N.kINSTANCE.translate("text.AboutBox.AboutPaneTitle"),aboutPane);

		// create the copyright pane
		if (setupCopyrightContent() != null) {
			JPanel copyrightPane = new JPanel();
			copyrightPane.setLayout(new BorderLayout());
			copyrightPane.setBorder(new EmptyBorder(5,5,10,10));
			JPanel northPanel = new JPanel();
			northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

			// preload the animation images
			ImageIcon javaCupImage = null;
			ImageIcon swingImage = null;
			try {
				javaCupImage = new ImageIcon(JARResources.fSystemResources.getImage(kJavaCupImageFilename));
				swingImage = new ImageIcon(JARResources.fSystemResources.getImage(kSwingImageFilename));
			}
			catch (FileNotFoundException exc) {
				// ignore
			}

			// show the Java-cup rotating
			JPanel subPanel = new JPanel();
			subPanel.setLayout(new BoxLayout(subPanel,BoxLayout.Y_AXIS));
			JLabel label = new JLabel(javaCupImage,JLabel.CENTER);
			label.setToolTipText(I18NL10N.kINSTANCE.translate("tooltip.JavaCupImage"));
			if (javaCupImage != null) {
				label.setBorder(BorderFactory.createEtchedBorder());
			}
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			subPanel.add(label);
			subPanel.add(Box.createRigidArea(new Dimension(0,10)));
			label = new JLabel("<HTML><B>Java 2 !</B></HTML>",JLabel.CENTER);
			label.setToolTipText(I18NL10N.kINSTANCE.translate("tooltip.JavaCupImage"));
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			subPanel.add(label);
			subPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
			subPanel.setAlignmentY(Component.TOP_ALIGNMENT);
			subPanel.setPreferredSize(new Dimension(kJavaCupWidth,kJavaCupHeight));
			northPanel.add(subPanel);

			// show the Duke swinging
			subPanel = new JPanel();
			subPanel.setLayout(new BoxLayout(subPanel,BoxLayout.Y_AXIS));
			label = new JLabel(swingImage,JLabel.CENTER);
			label.setToolTipText(I18NL10N.kINSTANCE.translate("tooltip.SwingImage"));
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			subPanel.add(label);
			subPanel.add(Box.createRigidArea(new Dimension(0,10)));
			label = new JLabel("<HTML><B>Swing !</B></HTML>",JLabel.CENTER);
			label.setToolTipText(I18NL10N.kINSTANCE.translate("tooltip.SwingImage"));
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			subPanel.add(label);
			subPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
			subPanel.setAlignmentY(Component.TOP_ALIGNMENT);
			subPanel.setPreferredSize(new Dimension(kSwingWidth,kSwingHeight));
			northPanel.add(subPanel);
			copyrightPane.add(northPanel,BorderLayout.NORTH);

			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			StringBuilder copyrightLabelText = new StringBuilder("<HTML>");
			String[] copyrightContent = setupCopyrightContent().toString().split(StringTools.kEOLCharacterSequence);
			// convert plain text to HTML code
			for (int lineNr = 0; lineNr < copyrightContent.length; ++lineNr) {
				copyrightLabelText.append(copyrightContent[lineNr] + "<BR>");
			}
			copyrightLabelText.append("</HTML>");
			centerPanel.add(new JLabel(copyrightLabelText.toString()));
			copyrightPane.add(centerPanel,BorderLayout.CENTER);

			fTabbedPane.addTab(I18NL10N.kINSTANCE.translate("text.AboutBox.CopyrightPaneTitle"),copyrightPane);
		}

		Dimension maximumDimensions = new Dimension(0,0);

		if (setupLicenceContent() != null) {
			// create the licence pane
			JPanel licencePane = new JPanel();
			licencePane.setLayout(new BorderLayout());
			fLicenceTextArea = new JTextArea(setupLicenceContent().toString());
			fLicenceTextArea.setRows(kTextAreaInitialNrOfRows);
			fLicenceTextArea.setFont(fLicenceTextArea.getFont().deriveFont(12.0f));
			fLicenceTextArea.setCaretPosition(0);
			fLicenceTextArea.setLineWrap(false);
			fLicenceTextArea.setEditable(false);

			// 'disable' the selection colors
			fLicenceTextArea.setSelectedTextColor(Color.black);
			fLicenceTextArea.setSelectionColor(Color.white);

			// make the JTextArea scrollable by embedding it in a JScrollPane
			fLicenceScrollPane = new JScrollPane(fLicenceTextArea);
			fLicenceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			fLicenceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

			// make some room for the vertical scrollbar
			maximumDimensions = fLicenceTextArea.getPreferredScrollableViewportSize();
			maximumDimensions.width += kTextAreaExcessScrollPaneSpace;
			fLicenceScrollPane.setPreferredSize(maximumDimensions);
			licencePane.add(fLicenceScrollPane,BorderLayout.CENTER);
			fTabbedPane.addTab(I18NL10N.kINSTANCE.translate("text.AboutBox.LicencePaneTitle"),licencePane);
		}

		if (setupAffiliationsLabels() != null) {
		
			JPanel affiliationsPane = new JPanel();
			affiliationsPane.setLayout(new BoxLayout(affiliationsPane,BoxLayout.Y_AXIS));
			affiliationsPane.setBorder(new EmptyBorder(10,10,10,10));
			affiliationsPane.setAlignmentX(Component.LEFT_ALIGNMENT);
			affiliationsPane.setAlignmentY(Component.TOP_ALIGNMENT);

			ArrayList<JLabel> affiliationsLabels = setupAffiliationsLabels();
			for (JLabel affiliationLabel : affiliationsLabels) {
				affiliationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
				affiliationLabel.setAlignmentY(Component.TOP_ALIGNMENT);
				affiliationsPane.add(affiliationLabel);
				if (affiliationLabel != affiliationsLabels.get(affiliationsLabels.size() - 1)) {
					affiliationsPane.add(Box.createRigidArea(new Dimension(0,15)));
				}
			}	

			// make the affiliations pane scrollable by embedding it in a JScrollPane
			fAffiliationsScrollPane = new JScrollPane(affiliationsPane);
			fAffiliationsScrollPane.setBorder(new EmptyBorder(0,0,0,0));
			fAffiliationsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			fAffiliationsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			fAffiliationsScrollPane.setPreferredSize(maximumDimensions);

			fTabbedPane.addTab(I18NL10N.kINSTANCE.translate("text.AboutBox.AffiliationsPaneTitle"),fAffiliationsScrollPane);
		}

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(fTabbedPane,BorderLayout.CENTER);
	}

	/**
	 * Performs custom initialisation during the about box's activation.
	 * <P>
	 * <B>Note that this method cannot be overridden!</B>
	 */
	@Override
	protected final void initialiseDuringActivation()
	{
		updateAboutTextLabel();

		// always jump to the first tab (logo and about text)
		fTabbedPane.setSelectedIndex(0);

		if (setupLicenceContent() != null) {
			// always scroll the beginning of the text in the licence tab
			fLicenceTextArea.setCaretPosition(0);
			fLicenceScrollPane.getVerticalScrollBar().setValue(0);
		}

		if (setupAffiliationsLabels() != null) {
			// always scroll the first affiliation in the affiliations tab
			fAffiliationsScrollPane.getVerticalScrollBar().setValue(0);
		}
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 */
	private void updateAboutTextLabel()
	{
		// incorporate a custom about text
		String aboutText = "<HTML>";
		if (setupAboutText() != null) {
			aboutText += setupAboutText() + "<BR>";
			aboutText += "<BR>";
		}

		// update the JVM, OS, current locale and machine specifics
		aboutText += "Java VM " + System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")<BR>";
		aboutText += I18NL10N.kINSTANCE.translate("text.OperatingSystem") + ": " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")<BR>";
		aboutText += I18NL10N.kINSTANCE.translate("text.CurrentLocale") + ": " + I18NL10N.kINSTANCE.getCurrentLocaleDescription() + "<BR/>";
		int nrOfProcessors = SystemInformation.getNrOfProcessors();
		aboutText += I18NL10N.kINSTANCE.translate("text.NrOfProcessors") + ": " + String.valueOf(nrOfProcessors) + "<BR>";		

		// update the available memory
		long totalMemory = MathTools.round(MathTools.convertBToMiB(SystemInformation.getTotalMemory()));
		long usedMemory = MathTools.round(MathTools.convertBToMiB(SystemInformation.getUsedMemory()));
		long freeMemory = MathTools.round(MathTools.convertBToMiB(SystemInformation.getFreeMemory()));
		aboutText += I18NL10N.kINSTANCE.translate("text.MemoryTotal") + ": " + String.valueOf(totalMemory) + " " + I18NL10N.kINSTANCE.translate("text.MiBAbbreviation") + "<BR>";		
		aboutText += I18NL10N.kINSTANCE.translate("text.MemoryUsed") + ": " + String.valueOf(usedMemory) + " " + I18NL10N.kINSTANCE.translate("text.MiBAbbreviation") + "<BR>";		
		aboutText += I18NL10N.kINSTANCE.translate("text.MemoryFree") + ": " + String.valueOf(freeMemory) + " " + I18NL10N.kINSTANCE.translate("text.MiBAbbreviation") + "<BR>";		

		// update the used libraries
		aboutText += "<BR>";
		aboutText += I18NL10N.kINSTANCE.translate("text.UsedLibraries") + ": JLayer 1.0.1, Log4j 1.3alpha-8, Java Native Access, Quaqua 8, BigDecimalMath 2012-03";
		if (setupUsedLibrariesDescriptions() != null) {
			aboutText += ", " + setupUsedLibrariesDescriptions();
		}

		aboutText += "</HTML>";

		fAboutTextLabel.setText(aboutText);
	}
}
