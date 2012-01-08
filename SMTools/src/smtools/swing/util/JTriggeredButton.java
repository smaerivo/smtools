// -------------------------------------
// Filename      : JTriggeredButton.java
// Author        : Sven Maerivoet
// Last modified : 22/01/2004
// Target        : Java VM (1.6)
// -------------------------------------

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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The <CODE>JTriggeredButton</CODE> class provides a button that automatically hides/shows its border.
 * <P>
 * Initially, a <CODE>JTriggeredButton</CODE> has no border drawn, but when
 * the mouse pointer hovers above its location, its border becomes activated.
 * 
 * @author  Sven Maerivoet
 * @version 22/01/2004
 */
public class JTriggeredButton extends JButton
{
	// internal datastructures
	private static JTriggeredButtonBorder fBorder = new JTriggeredButtonBorder(false,false);
	private static JTriggeredButtonBorder fSinkBorder = new JTriggeredButtonBorder(false,true);
	private static Insets fInsets = new Insets(0,0,0,0);

	// background colors
	private Color fBackgroundColor;
	private Color fHighlightColor;
	private Color fSelectedColor;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Creates a <CODE>JTriggeredButton</CODE> with no text or icon set.
	 *
	 * @see JTriggeredButton#JTriggeredButton(String)
	 * @see JTriggeredButton#JTriggeredButton(Icon)
	 * @see JTriggeredButton#JTriggeredButton(String,Icon)
	 */
	public JTriggeredButton()
	{
		customize();
	}

	/**
	 * Creates a <CODE>JTriggeredButton</CODE> with text.
	 *
	 * @param text the text of the button
	 * @see   JTriggeredButton#JTriggeredButton()
	 * @see   JTriggeredButton#JTriggeredButton(Icon)
	 * @see   JTriggeredButton#JTriggeredButton(String,Icon)
	 */
	public JTriggeredButton(String text)
	{
		super(text);
		customize();
	}

	/**
	 * Creates a <CODE>JTriggeredButton</CODE> with an icon.
	 *
	 * @param icon the <CODE>Icon</CODE> image to display on the button
	 * @see   JTriggeredButton#JTriggeredButton()
	 * @see   JTriggeredButton#JTriggeredButton(String)
	 * @see   JTriggeredButton#JTriggeredButton(String,Icon)
	 */
	public JTriggeredButton(Icon icon)
	{
		super(icon);
		customize();
	}

	/**
	 * Creates a <CODE>JTriggeredButton</CODE> with text and an icon.
	 *
	 * @param text the text of the button
	 * @param icon the <CODE>Icon</CODE> image to display on the button
	 * @see   JTriggeredButton#JTriggeredButton()
	 * @see   JTriggeredButton#JTriggeredButton(String)
	 * @see   JTriggeredButton#JTriggeredButton(Icon)
	 */
	public JTriggeredButton(String text, Icon icon)
	{
		super(text,icon);
		customize();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the color used when the mouse pointer is over the button.
	 *
	 * @param color the color used when the mouse pointer is over the button
	 */
	public final void setHighlightColor(Color color)
	{
		fHighlightColor = color;
	}

	/**
	 * Sets the color used when the button is selected.
	 *
	 * @param color the color used when the button is selected
	 */
	public final void setSelectedColor(Color color)
	{
		fSelectedColor = color;
	}

	/**
	 * Returns the current background color of the button.
	 * <P>
	 * Depending on whether or not the button is selected,
	 * a call to this method gives different results.
	 *
	 * @return the current background color of the button
	 */
	@Override
	public final Color getBackground()
	{
		if (isSelected()) {
			return fSelectedColor;
		}
		else {
			return super.getBackground();
		}
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	private void customize()
	{
		fBorder.setInsets(new Insets(4,4,4,4));
		fSinkBorder.setInsets(new Insets(5,6,4,4));

		fBackgroundColor = getBackground();
		fHighlightColor = fBackgroundColor;
		fSelectedColor = fBackgroundColor;
		setBorderPainted(false);
		setFocusPainted(false);
		setMargin(fInsets);
		setBorder(fBorder);

		addMouseListener(new MouseAdapter()
		{
			@Override
			public final void mouseEntered(MouseEvent e)
			{
				setBorder(fBorder);
				setBorderPainted(true);
				setBackground(fHighlightColor);
			}

			@Override
			public final void mouseExited(MouseEvent e)
			{
				setBorder(fBorder);
				setBorderPainted(false);
				setBackground(fBackgroundColor);
			}

			@Override
			public final void mousePressed(MouseEvent e)
			{
				setBorder(fSinkBorder);
				setBorderPainted(true);
			}

			@Override
			public final void mouseReleased(MouseEvent e)
			{
				setBorder(fBorder);
				setBorderPainted(false);
			}

			@Override
			public final void mouseClicked(MouseEvent e)
			{
				setBorder(fBorder);
				setBorderPainted(false);
			}
		});
	}
}
