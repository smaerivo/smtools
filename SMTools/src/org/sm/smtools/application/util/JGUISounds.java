// -------------------------------
// Filename      : JGUISounds.java
// Author        : Sven Maerivoet
// Last modified : 07/08/2019
// Target        : Java VM (1.8)
// -------------------------------

/**
 * Copyright 2019 Sven Maerivoet
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

/**
 * This class contains the filenames of the selected set of GUI sounds.
 * <P>
 * The sounds' filenames are queries using the <CODE>JGUISounds.kINSTANCE.getXXX()</CODE> methods.
 *
 * @author  Sven Maerivoet
 * @version 07/08/2019
 */
public enum JGUISounds
{
	kINSTANCE;

	public enum EGUISoundSet {kLCARS, kApple, kSpace};

	// internal datastructures
	private String fButtonSoundFilename;
	private String kChangeLookAndFeelSoundFilename;
	private String kMenuItemSoundFilename;
	private String kMessageDialogSoundFilename;
	private String kWarningDialogSoundFilename;
	private String kWindowEventSoundFilename;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation.
	 */
	private JGUISounds()
	{
		selectDefault();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Selects the default sound set.
	 */
	public void selectDefault()
	{
		selectSoundSet(EGUISoundSet.kLCARS);
	}

	/**
	 * Selects a specified sound set.
	 *
	 * @param guiSoundSet  the sound set to use for the GUI
	 */
	public void selectSoundSet(EGUISoundSet guiSoundSet)
	{
		if (guiSoundSet == EGUISoundSet.kLCARS) {
			selectLCARSSoundSet();
		}
		else if (guiSoundSet == EGUISoundSet.kApple) {
			selectAppleSoundSet();
		}
		else if (guiSoundSet == EGUISoundSet.kSpace) {
			selectSpaceSoundSet();
		}
	}

	/**
	 * Returns the sound filename associated with a button click.
	 *
	 * @return the sound filename associated with a button click
	 */
	public String getButtonSoundFilename()
	{
		return fButtonSoundFilename;
	}

	/**
	 * Returns the sound filename associated when the application's look and feel has changed.
	 *
	 * @return the sound filename associated when the application's look and feel has change
	 */
	public String getChangeLookAndFeelSoundFilename()
	{
		return kChangeLookAndFeelSoundFilename;
	}

	/**
	 * Returns the sound filename associated with a selection of a menu item.
	 *
	 * @return the sound filename associated with a selection of a menu item
	 */
	public String getMenuItemSoundFilename()
	{
		return kMenuItemSoundFilename;
	}

	/**
	 * Returns the sound filename associated with a message dialog popup.
	 *
	 * @return the sound filename associated with a message dialog popup
	 */
	public String getMessageDialogSoundFilename()
	{
		return kMessageDialogSoundFilename;
	}

	/**
	 * Returns the sound filename associated with a warning dialog popup.
	 *
	 * @return the sound filename associated with a warning dialog popup
	 */
	public String getWarningDialogSoundFilename()
	{
		return kWarningDialogSoundFilename;
	}

	/**
	 * Returns the sound filename associated when the application's GUI window is iconified or deiconified.
	 *
	 * @return the sound filename associated when the application's GUI window is iconified or deiconified
	 */
	public String getWindowEventSoundFilename()
	{
		return kWindowEventSoundFilename;
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 */
	private void selectLCARSSoundSet()
	{
		fButtonSoundFilename = "smtools-resources/sounds/lcars-button.mp3";
		kChangeLookAndFeelSoundFilename = "smtools-resources/sounds/lcars-change-look-and-feel.mp3";
		kMenuItemSoundFilename = "smtools-resources/sounds/lcars-menu-item.mp3";
		kMessageDialogSoundFilename = "smtools-resources/sounds/lcars-message-dialog.mp3";
		kWarningDialogSoundFilename = "smtools-resources/sounds/lcars-warning-dialog.mp3";
		kWindowEventSoundFilename = "smtools-resources/sounds/lcars-window-event.mp3";
	}

	/**
	 */
	private void selectAppleSoundSet()
	{
		fButtonSoundFilename = "smtools-resources/sounds/apple-button.mp3";
		kChangeLookAndFeelSoundFilename = "smtools-resources/sounds/apple-change-look-and-feel.mp3";
		kMenuItemSoundFilename = "smtools-resources/sounds/apple-menu-item.mp3";
		kMessageDialogSoundFilename = "smtools-resources/sounds/apple-message-dialog.mp3";
		kWarningDialogSoundFilename = "smtools-resources/sounds/apple-warning-dialog.mp3";
		kWindowEventSoundFilename = "smtools-resources/sounds/apple-window-event.mp3";
	}

	/**
	 */
	private void selectSpaceSoundSet()
	{
		fButtonSoundFilename = "smtools-resources/sounds/space-button.mp3";
		kChangeLookAndFeelSoundFilename = "smtools-resources/sounds/space-change-look-and-feel.mp3";
		kMenuItemSoundFilename = "smtools-resources/sounds/space-menu-item.mp3";
		kMessageDialogSoundFilename = "smtools-resources/sounds/space-message-dialog.mp3";
		kWarningDialogSoundFilename = "smtools-resources/sounds/space-warning-dialog.mp3";
		kWindowEventSoundFilename = "smtools-resources/sounds/space-window-event.mp3";
	}
}
