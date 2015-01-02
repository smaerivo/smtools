// -----------------------------------
// Filename      : JWarningDialog.java
// Author        : Sven Maerivoet
// Last modified : 04/12/2012
// Target        : Java VM (1.8)
// -----------------------------------

/**
 * Copyright 2003-2015 Sven Maerivoet
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
import javax.swing.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.util.*;

/**
 * The <CODE>JWarningDialog</CODE> class pops up a standard dialog box containing a warning.
 * <P>
 * Note that this class cannot be instantiated, nor can it be subclassed; use the static
 * {@link JWarningDialog#warn} method instead. A valid {@link I18NL10N} database must
 * be available!
 * <P>
 * Depending on the application's current <I>look-and-feel</I>, the dialog box
 * looks as follows:
 * <P>
 * <B><U>Java Metal L&amp;F</U></B><BR>
 * <BR>
 * <IMG src="doc-files/warning-dialog-metal.png" alt="">
 * <P>
 * <B><U>Microsoft Windows L&amp;F</U></B><BR>
 * <BR>
 * <IMG src="doc-files/warning-dialog-windows.png" alt="">
 * <P>
 * <B><U>Motif L&amp;F</U></B><BR>
 * <BR>
 * <IMG src="doc-files/warning-dialog-motif.png" alt="">
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 04/12/2012
 */
public final class JWarningDialog
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation.
	 */
	private JWarningDialog()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Pops up a up a standard dialog box containing a specified warning message.
	 * <P>
	 * This method should be called as follows:
	 * <P>
	 * <CODE>JWarningDialog.warn(parent,warning);</CODE>
	 * <P>
	 * Note that a valid {@link I18NL10N} database must be available!
	 *
	 * @param parentComponent  the frame in which this dialog is to be displayed
	 * @param warning          a string containing a specified warning message
	 */
	public static void warn(Component parentComponent, String warning)
	{
		MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSWarningDialog);
		Object[] options = {I18NL10N.translate("button.Ok")};

		JOptionPane.showOptionDialog(parentComponent,warning,I18NL10N.translate("text.Attention"),
				JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE,null,options,options[0]);
	}
}
