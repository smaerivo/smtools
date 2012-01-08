// -----------------------------------
// Filename      : JMessageDialog.java
// Author        : Sven Maerivoet
// Last modified : 22/01/2004
// Target        : Java VM (1.6)
// -----------------------------------

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

package smtools.swing.dialogs;

import java.awt.*;
import javax.swing.*;
import smtools.application.util.*;
import smtools.miscellaneous.*;

/**
 * The <CODE>JMessageDialog</CODE> class pops up a standard dialog box containing a message.
 * <P>
 * Note that this class cannot be instantiated, nor can it be subclassed; use the static
 * {@link JMessageDialog#show} method instead. A valid {@link Messages} database must
 * be available!
 * <P>
 * Depending on the application's current <I>look-and-feel</I>, the dialog box
 * looks as follows:
 * <P>
 * <UL>
 *   <B><U>Java Metal L&F</U></B><BR />
 *   <BR />
 *   <IMG src="doc-files/message-dialog-metal.png">
 * </UL>
 * <P>
 * <UL>
 *   <B><U>Microsoft Windows L&F</U></B><BR />
 *   <BR />
 *   <IMG src="doc-files/message-dialog-windows.png">
 * </UL>
 * <P>
 * <UL>
 *   <B><U>Motif L&F</U></B><BR />
 *   <BR />
 *   <IMG src="doc-files/message-dialog-motif.png">
 * </UL>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 22/01/2004
 */
public final class JMessageDialog
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	// prevent instantiation
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
	 * <UL>
	 *   <CODE>JMessageDialog.show(parent,message);</CODE>
	 * </UL>
	 * <P>
	 * Note that a valid {@link Messages} database must be available!
	 *
	 * @param parentComponent the frame in which this dialog is to be displayed
	 * @param message         a string containing a specified message
	 */
	public static void show(Component parentComponent, String message)
	{
		MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSMessageDialog);
		Object[] options = {Messages.lookup("buttonOk")};

		JOptionPane.showOptionDialog(parentComponent,message,Messages.lookup("textAttention"),
				JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
	}
}
