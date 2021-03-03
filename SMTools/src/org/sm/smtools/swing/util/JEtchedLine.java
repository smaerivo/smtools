// --------------------------------
// Filename      : JEtchedLine.java
// Author        : Sven Maerivoet
// Last modified : 27/04/2011
// Target        : Java VM (1.8)
// --------------------------------

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
import javax.swing.*;

/**
 * The <CODE>JEtchedLine</CODE> class provides an etched line.
 * <P>
 * An etched line typically consists of a dark line, with a white
 * line slightly offset below it. Here's a close-up view:
 * <P>
 * <IMG src="doc-files/etched-line.png" alt="">
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 27/04/2011
 */
public final class JEtchedLine extends JPanel
{
	/**
	 * Useful constant to specify a horizontally or vertically oriented etched line.
	 */
	public static enum EOrientation {
		/**
		 * A horizontal etched line.
		 */
		kHorizontal,

		/**
		 * A vertical etched line.
		 */
		kVertical};

	// the default color
	private static final Color kDefaultColor = Color.gray;

	// internal datastructure
	private Color fColor;
	private EOrientation fOrientation;
	private boolean fUseInsets;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JEtchedLine</CODE> object.
	 * <P>
	 * The etched line has a horizontal orientation and gray as its default dark color.
	 *
	 * @see JEtchedLine#JEtchedLine(Color)
	 * @see JEtchedLine#JEtchedLine(EOrientation)
	 * @see JEtchedLine#JEtchedLine(Color,EOrientation)
	 */
	public JEtchedLine()
	{
		this(kDefaultColor,EOrientation.kHorizontal);
	}

	/**
	 * Constructs a <CODE>JEtchedLine</CODE> object.
	 * <P>
	 * The etched line has a horizontal orientation and a specified dark color.
	 *
	 * @param color  the dark color to use (typically <CODE>Color.gray</CODE> or <CODE>Color.black</CODE>)
	 * @see          JEtchedLine#JEtchedLine()
	 * @see          JEtchedLine#JEtchedLine(EOrientation)
	 * @see          JEtchedLine#JEtchedLine(Color,EOrientation)
	 */
	public JEtchedLine(Color color)
	{
		this(color,EOrientation.kHorizontal);
	}

	/**
	 * Constructs a <CODE>JEtchedLine</CODE> object.
	 * <P>
	 * The etched line has a specified orientation and gray as its default dark color.
	 *
	 * @param orientation  the orientation of the etched line (see {@link JEtchedLine.EOrientation})
	 * @see                JEtchedLine#JEtchedLine()
	 * @see                JEtchedLine#JEtchedLine(Color)
	 * @see                JEtchedLine#JEtchedLine(Color,EOrientation)
	 */
	public JEtchedLine(EOrientation orientation)
	{
		this(kDefaultColor,orientation);
	}

	/**
	 * Constructs a <CODE>JEtchedLine</CODE> object.
	 * <P>
	 * The etched line has a specified orientation and a specified dark color.
	 *
	 * @param color        the dark color to use (typically <CODE>Color.gray</CODE> or <CODE>Color.black</CODE>)
	 * @param orientation  the orientation of the etched line (see {@link JEtchedLine.EOrientation})
	 * @see                JEtchedLine#JEtchedLine()
	 * @see                JEtchedLine#JEtchedLine(Color)
	 * @see                JEtchedLine#JEtchedLine(EOrientation)
	 */
	public JEtchedLine(Color color, EOrientation orientation)
	{
		fColor = color;
		fOrientation = orientation;
		fUseInsets = true;
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Disable the use of margins (so the etched line will expand slightly inside the <CODE>JPanel</CODE>).
	 */
	public void disableInsets()
	{
		fUseInsets = false;
	}

	/**
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (fOrientation == EOrientation.kHorizontal) {

			int x1, x2;
			if (fUseInsets) {
				x1 = getInsets().left;
				x2 = getWidth() - getInsets().right;
			}
			else {
				x1 = 0;
				x2 = getWidth();
			}
			int y = getHeight() / 2;

			g.setColor(fColor);
			g.drawLine(x1,y,x2 - 1,y);
			g.setColor(Color.white);
			g.drawLine(x1 + 1,y + 1,x2,y + 1);
		}
		else if (fOrientation == EOrientation.kVertical) {

			int x = getWidth() / 2;

			int y1, y2;
			if (fUseInsets) {
				y1 = getInsets().top;
				y2 = getHeight() - getInsets().bottom;
			}
			else {
				y1 = 0;
				y2 = getHeight();
			}

			g.setColor(fColor);
			g.drawLine(x,y1,x,y2 - 1);
			g.setColor(Color.white);
			g.drawLine(x + 1,y1 + 1,x + 1,y2);
		}
	}
}
