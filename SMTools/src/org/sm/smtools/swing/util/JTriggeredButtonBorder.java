// -------------------------------------------
// Filename      : JTriggeredButtonBorder.java
// Author        : Sven Maerivoet
// Last modified : 22/04/2002
// Target        : Java VM (1.8)
// -------------------------------------------

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

package org.sm.smtools.swing.util;

import java.awt.*;
import javax.swing.border.*;

/**
 * The <CODE>JTriggeredButtonBorder</CODE> class provides a special border for the <CODE>JTriggeredButton</CODE> class.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 22/04/2002
 * @see     JTriggeredButton
 */
public final class JTriggeredButtonBorder extends AbstractBorder
{
	// the different border shadow colors
	private Color kHighlightColor = Color.white;
	private Color kNormalColor = Color.darkGray;

	// internal datastructures
	private Insets fInsets;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Creates a <CODE>JTriggeredButtonBorder</CODE> with default dark gray and white border colors.
	 *
	 * @see JTriggeredButtonBorder#JTriggeredButtonBorder(boolean,boolean)
	 * @see JTriggeredButton
	 */
	public JTriggeredButtonBorder()
	{
	}

	/**
	 * Creates a <CODE>JTriggeredButtonBorder</CODE> with specfied border colors.
	 *
	 * @param isBackgroundDark  if the background is dark, this flag specifies the use of a light border color
	 * @param isSunk            specifies whether or not the border is sunk or raised
	 * @see                     JTriggeredButtonBorder#JTriggeredButtonBorder()
	 * @see                     JTriggeredButton
	 */
	public JTriggeredButtonBorder(boolean isBackgroundDark, boolean isSunk)
	{
		if (isBackgroundDark) {
			kHighlightColor = Color.lightGray;
		}

		if (isSunk) {
			// swap normal and highlight colors
			Color temp = kHighlightColor;
			kHighlightColor = kNormalColor;
			kNormalColor = temp;
		}
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the margins used by the border.
	 *
	 * @param insets  the margins used by the border
	 * @see           JTriggeredButtonBorder#getBorderInsets(Component)
	 */
	public void setInsets(Insets insets)
	{
		fInsets = insets;
	}

	/**
	 * Returns the margins used by the border
	 * <P>
	 * <B>Note that this method does not return the insets of the specified component!</B>
	 *
	 * @return the margins used by this border
	 * @see    JTriggeredButtonBorder#setInsets(Insets)
	 */
	@Override
	public Insets getBorderInsets(Component c)
	{
		return fInsets;
	}

	/**
	 */
	@Override
	public void paintBorder(Component component, Graphics gr, int x, int y, int width, int height)
	{
		Color oldColor = gr.getColor();
		gr.translate(x,y);

		gr.setColor(kHighlightColor);
		gr.drawLine(0,0,width - 1,0);
		gr.drawLine(0,0,0,height - 1);

		gr.setColor(kNormalColor);
		gr.drawLine(0,height - 1,width - 1,height - 1);
		gr.drawLine(width - 1,0,width - 1,height - 1);

		gr.translate(-x,-y);
		gr.setColor(oldColor);
	}

	/**
	 * This method always returns <CODE>false</CODE> because the border is made transparent.
	 *
	 * @return always <CODE>false</CODE>
	 */
	@Override
	public boolean isBorderOpaque()
	{
		return false;
	}
}
