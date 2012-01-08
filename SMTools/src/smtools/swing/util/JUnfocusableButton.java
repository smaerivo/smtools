// ---------------------------------------
// Filename      : JUnfocusableButton.java
// Author        : Sven Maerivoet
// Last modified : 20/01/2004
// Target        : Java VM (1.6)
// ---------------------------------------

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

package smtools.swing.util;

import javax.swing.*;

/**
 * The <CODE>JUnfocusableButton</CODE> class is a special implementation of a <CODE>JButton</CODE>.
 * <P>
 * This button cannot receive the focus.
 * 
 * @author  Sven Maerivoet
 * @version 20/01/2004
 */
public class JUnfocusableButton extends JButton
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Creates a <CODE>JUnfocusableButton</CODE> with no text or icon set.
	 *
	 * @see JUnfocusableButton#JUnfocusableButton(String)
	 * @see JUnfocusableButton#JUnfocusableButton(Icon)
	 * @see JUnfocusableButton#JUnfocusableButton(String,Icon)
	 */
	public JUnfocusableButton()
	{
		super();
	}

	/**
	 * Creates a <CODE>JUnfocusableButton</CODE> with text.
	 *
	 * @param text the text of the button
	 * @see   JUnfocusableButton#JUnfocusableButton()
	 * @see   JUnfocusableButton#JUnfocusableButton(Icon)
	 * @see   JUnfocusableButton#JUnfocusableButton(String,Icon)
	 */
	public JUnfocusableButton(String text)
	{
		super(text);
	}

	/**
	 * Creates a <CODE>JUnfocusableButton</CODE> with an icon.
	 *
	 * @param icon the <CODE>Icon</CODE> image to display on the button
	 * @see   JUnfocusableButton#JUnfocusableButton()
	 * @see   JUnfocusableButton#JUnfocusableButton(String)
	 * @see   JUnfocusableButton#JUnfocusableButton(String,Icon)
	 */
	public JUnfocusableButton(Icon icon)
	{
		super(icon);
	}

	/**
	 * Creates a <CODE>JUnfocusableButton</CODE> with text and an icon.
	 *
	 * @param text the text of the button
	 * @param icon the <CODE>Icon</CODE> image to display on the button
	 * @see   JUnfocusableButton#JUnfocusableButton()
	 * @see   JUnfocusableButton#JUnfocusableButton(String)
	 * @see   JUnfocusableButton#JUnfocusableButton(Icon)
	 */
	public JUnfocusableButton(String text, Icon icon)
	{
		super(text,icon);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * This method always returns <CODE>false</CODE> because the button is not allowed to receive the focus.
	 *
	 * @return always <CODE>false</CODE>
	 */
	@Override
	public final boolean isFocusable()
	{
		return false;
	}
}
