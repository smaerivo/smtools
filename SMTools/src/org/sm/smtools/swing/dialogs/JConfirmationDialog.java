// ----------------------------------------
// Filename      : JConfirmationDialog.java
// Author        : Sven Maerivoet
// Last modified : 26/06/2018
// Target        : Java VM (1.8)
// ----------------------------------------

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
import javax.swing.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.util.*;

/**
 * The <CODE>JMessageDialog</CODE> class pops up a standard "Ok/Cancel" dialog box containing a question.
 * <P>
 * Note that this class cannot be instantiated, nor can it be subclassed; use the static
 * {@link JConfirmationDialog#confirm} method instead. A valid {@link I18NL10N} database must
 * be available!
 * <P>
 * Depending on the application's current <I>look-and-feel</I>, the dialog box
 * looks as follows:
 * <P>
 * <B><U>Java Metal L&amp;F</U></B><BR>
 * <BR>
 * <IMG src="doc-files/confirmation-dialog-metal.png" alt="">
 * <P>
 * <B><U>Microsoft Windows L&amp;F</U></B><BR>
 * <BR>
 * <IMG src="doc-files/confirmation-dialog-windows.png" alt="">
 * <P>
 * <B><U>Motif L&amp;F</U></B><BR>
 * <BR>
 * <IMG src="doc-files/confirmation-dialog-motif.png" alt="">
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 26/06/2018
 */
public final class JConfirmationDialog
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation.
	 */
	private JConfirmationDialog()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Pops up a up a standard "Ok/Cancel" dialog box containing a specified question.
	 * <P>
	 * This method should be called as follows:
	 * <P>
	 * <CODE>boolean ok = JConfirmationDialog.confirm(parent,message);</CODE>
	 * <P>
	 * Note that a valid {@link I18NL10N} database must be available!
	 *
	 * @param parentComponent  the frame in which this dialog is to be displayed
	 * @param question         a string containing a specified question
	 * @return                 <CODE>true</CODE> when the user has agreed (i.e., "ok"), <CODE>false</CODE> otherwise
	 */
	public static boolean confirm(Component parentComponent, String question)
	{
		MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSMessageDialog);
		Object[] options = {I18NL10N.kINSTANCE.translate("button.Yes"), I18NL10N.kINSTANCE.translate("button.No")};

		int n = JOptionPane.showOptionDialog(parentComponent,question,
			I18NL10N.kINSTANCE.translate("text.PleaseConfirm"),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,
			options,options[1]);

		return (n == JOptionPane.YES_OPTION);
	}
}
