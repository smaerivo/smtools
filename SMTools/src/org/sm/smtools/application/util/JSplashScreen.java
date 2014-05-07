// ----------------------------------
// Filename      : JSplashScreen.java
// Author        : Sven Maerivoet
// Last modified : 04/12/2012
// Target        : Java VM (1.8)
// ----------------------------------

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

package org.sm.smtools.application.util;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import org.sm.smtools.exceptions.*;
import org.sm.smtools.util.*;

/**
 * The <CODE>JSplashScreen</CODE> class provides a splash screen for Swing-based GUIs.
 * <P>
 * Note that a valid {@link I18NL10N} database must be available!
 * <P>
 * When visible, the splash screen looks as follows:
 * <P>
 * <IMG src="doc-files/splash-screen.png" alt="">
 * <P>
 * As can be seen, there are three main areas:
 * <UL>
 *   <LI>An area where <B>custom content</B> can be shown.
 *   <UL>
 *     <LI>
 *     The custom content is passed as a <CODE>JLabel</CODE> to the <CODE>JSplashScreen</CODE>
 *     object via its constructor. Typically, an image is provided; for visual coherence, we
 *     suggest using a <CODE>JLabel</CODE>/image with a <B>maximum width of 500 pixels</B>.
 *     </LI>
 *   </UL>
 *   </LI>
 *   <LI>The <B>common <I>SMTools</I> area</B> (with the hammer and the spanner).</LI>
 *   <LI>An area containing custom <B>status messages</B> and a <B>progress bar</B>.
 *   <UL>
 *     <LI>
 *     The caller updates the status message by invoking the {@link JSplashScreen#setStatusMessage(String)}
 *     method.
 *     </LI>
 *   </UL>
 *   </LI>
 * </UL>
 * <P>
 * When the splash screen is shown, an optional MP3 soundfile can be played.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 04/12/2012
 * @see     org.sm.smtools.application.JStandardGUIApplication
 * @see     org.sm.smtools.util.MP3Player
 */
public final class JSplashScreen extends JWindow
{
	// the offset used when the dialog is at the edge of the screen
	private static final int kDialogOffset = 50;

	// the name of the default splash screen's banner
	private static final String kSplashScreenBannerFilename = "smtools-resources/images/smtools-splash-banner.png";

	// internal datastructures
	private boolean fAvailable;
	private int fStatusWaitTime;
	private JLabel fStatusLabel;
	private JProgressBar fProgressBar;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JSplashScreen</CODE> object with a specified content.
	 * <P>
	 * When a <CODE>JSplashScreen</CODE> object should be created, but not be available,
	 * the caller should specify <CODE>null</CODE> as the value for the <CODE>customSplashScreenContent</CODE>.
	 * <P>
	 * The caller should use <CODE>null</CODE> as the value for the MP3 <CODE>soundFilename</CODE>
	 * parameter if no MP3 soundfile is to be played.
	 * <P>
	 * <B><U>Important remark:</U></B>
	 * <UL>
	 *   <LI>If the specified MP3 soundfile could not be played, it is ignored.</LI>
	 * </UL>
	 *
	 * @param customSplashScreenContent  the custom content (typically an image with a maximum dimension of 460x130 pixels)
	 * @param mp3SoundInputStream        an <CODE>InputStream</CODE> containing the MP3 sound to be played (use <CODE>null</CODE> for no sound)
	 * @see                              JSplashScreen#isAvailable()
	 */
	public JSplashScreen(JLabel customSplashScreenContent, InputStream mp3SoundInputStream)
	{
		fStatusWaitTime = 0;
		fAvailable = (customSplashScreenContent != null) && (!DevelopMode.isActivated());

		if (fAvailable) {

			// change the cursor to indicate that the user has to wait
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			JPanel contentPanel = new JPanel();
			setContentPane(contentPanel);

			// create a border around the window's contents
			contentPanel.setLayout(new BorderLayout());

			JPanel panel = null;
			JLabel label = null;

			// use a deep dark blue background color
			contentPanel.setBackground(new Color(0.12f,0.08f,0.67f));

			contentPanel.setLayout(new BorderLayout());
			contentPanel.setBorder(new LineBorder(Color.blue,2));

			panel = new JPanel();
			panel.setBorder(new EmptyBorder(5,5,5,5));
			panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
			panel.setOpaque(false);
			panel.setAlignmentX(Component.CENTER_ALIGNMENT);

			panel.add(Box.createRigidArea(new Dimension(0,20)));

			customSplashScreenContent.setForeground(Color.white);
			customSplashScreenContent.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(customSplashScreenContent);

			panel.add(Box.createRigidArea(new Dimension(0,30)));

			Image bannerImage = null;
			try {
				bannerImage = JARResources.fSystemResources.getImage(kSplashScreenBannerFilename);
				label = new JLabel(new ImageIcon(bannerImage),JLabel.CENTER);
				label.setAlignmentX(Component.CENTER_ALIGNMENT);
				panel.add(label);
			}
			catch (FileDoesNotExistException exc) {
				// ignore
			}

			panel.add(Box.createRigidArea(new Dimension(0,20)));

			fStatusLabel = new JLabel(I18NL10N.translate("text.SplashScreenMessage"),JLabel.LEFT);
			fStatusLabel.setForeground(Color.white);
			fStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(fStatusLabel);

			panel.add(Box.createRigidArea(new Dimension(0,5)));

			fProgressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,1);
			fProgressBar.setBorderPainted(false);
			fProgressBar.setStringPainted(false);
			fProgressBar.setOpaque(true);
			fProgressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
			fProgressBar.setIndeterminate(true);

			panel.add(fProgressBar);

			contentPanel.add(panel,BorderLayout.CENTER);

			pack();

			// position the splash screen in the middle of the screen
			Dimension splashScreenWindowSize = contentPanel.getPreferredSize();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			int xPos = ((int) Math.round(screenSize.getWidth()) / 2) - (splashScreenWindowSize.width / 2);
			int yPos = ((int) Math.round(screenSize.getHeight()) / 2) - (splashScreenWindowSize.height / 2);
			if (xPos < kDialogOffset) {
				xPos = kDialogOffset;
			}
			if (yPos < kDialogOffset) {
				yPos = kDialogOffset;
			}
			setLocation(xPos,yPos);

			setVisible(true);

			// optionally play an MP3 sound
			if ((mp3SoundInputStream != null) && MP3Player.systemSoundsEnabled()) {
				try {
					(new MP3Player(mp3SoundInputStream)).play(MP3Player.EPlaying.kUnblocked);
				}
				catch (SoundPlayingException exc) {
					// ignore
				}
			}
		}
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns whether or not the <CODE>JSplashScreen</CODE> object is available.
	 * <P>
	 * This method returns <CODE>false</CODE> when no custom content was specified to the constructor.
	 *
	 * @return <CODE>true</CODE> when the <CODE>JSplashScreen</CODE> object is available, <CODE>false</CODE> otherwise
	 */
	public boolean isAvailable()
	{
		return fAvailable;
	}

	/**
	 * Changes the status message.
	 *
	 * @param statusMessage  the status message
	 */
	public void setStatusMessage(String statusMessage)
	{
		if (fAvailable) {
			fStatusLabel.setText(statusMessage);

			if (!DevelopMode.isActivated()) {
				Chrono.wait(fStatusWaitTime);
			}
		}
	}

	/**
	 * Sets the delay that is forced each time the status message is changed.
	 *
	 * @param statusWaitTime  the time to wait (in milliseconds)
	 * @see                   JSplashScreen#setStatusMessage(String)
	 */
	public void setStatusMessageWaitTime(int statusWaitTime)
	{
		fStatusWaitTime = statusWaitTime;
	}

	/**
	 * Prevents flickering when painting.
	 */
	@Override
	public boolean imageUpdate(Image img, int flags, int x, int y, int w, int h)
	{
		// only allow repainting of this canvas when the image is completely loaded
		// this prevents flickering
		if ((flags & ALLBITS) != 0) {
			repaint();
		}

		return ((flags & (ALLBITS | ERROR)) == 0);
	}
}
