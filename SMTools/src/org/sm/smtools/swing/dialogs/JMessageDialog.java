// -----------------------------------
// Filename      : JMessageDialog.java
// Author        : Sven Maerivoet
// Last modified : 06/08/2019
// Target        : Java VM (1.8)
// -----------------------------------

/**
 * Copyright 2003-2019 Sven Maerivoet
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
 * The <CODE>JMessageDialog</CODE> class pops up a standard dialog box containing a message.
 * <P>
 * Note that this class cannot be instantiated, nor can it be subclassed; use the static
 * {@link JMessageDialog#show} method instead. A valid {@link I18NL10N} database must
 * be available!
 * <P>
 * Depending on the application's current <I>look-and-feel</I>, the dialog box
 * looks as follows:
 * <P>
 * <B><U>Java Metal L&amp;F</U></B><BR>
 * <BR>
 * <IMG src="doc-files/message-dialog-metal.png" alt="">
 * <P>
 * <B><U>Microsoft Windows L&amp;F</U></B><BR>
 * <BR>
 * <IMG src="doc-files/message-dialog-windows.png" alt="">
 * <P>
 * <B><U>Motif L&amp;F</U></B><BR>
 * <BR>
 * <IMG src="doc-files/message-dialog-motif.png" alt="">
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 06/08/2019
 */
public final class JMessageDialog
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation.
	 */
	private JMessageDialog()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Pops up a up a standard dialog box containing a specified message.
	 * <P>
	 * This method should be called as follows:
	 * <P>
	 * <CODE>JMessageDialog.show(parent,message);</CODE>
	 * <P>
	 * Note that a valid {@link I18NL10N} database must be available!
	 *
	 * @param parentComponent  the frame in which this dialog is to be displayed
	 * @param message          a string containing a specified message
	 */
	public static void show(Component parentComponent, String message)
	{
		MP3Player.playSystemSound(JGUISounds.kINSTANCE.getMessageDialogSoundFilename());
		Object[] options = {I18NL10N.kINSTANCE.translate("button.Ok")};

		JOptionPane.showOptionDialog(parentComponent,message,I18NL10N.kINSTANCE.translate("text.Attention"),
				JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
	}
}
