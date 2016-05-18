// --------------------------------
// Filename      : JLabelBox.java
// Author        : Sven Maerivoet
// Last modified : 18/05/2016
// Target        : Java VM (1.8)
// --------------------------------

/**
 * Copyright 2003-2016 Sven Maerivoet
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

/**
 * The <CODE>JLabelBox</CODE> class provides static methodes for drawing labels in boxes.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 18/05/2016
 */
public final class JLabelBox
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation of a <CODE>JLabelBox</CODE> object.
	 */
	private JLabelBox()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Draws a text inside a coloured box.
	 *
	 * @param g                a handle to the <CODE>Graphics2D</CODE> object
	 * @param textColor        the text colour
	 * @param backgroundColor  the background colour
	 * @param borderColor      the border colour
	 * @param transparency     the background and border colours' transparency (0.0 is fully opaque, 1.0 is fully transparent)
	 * @param posX             the left position of the box
	 * @param posY             the top position of the box
	 * @param textOffset       the inside single-margin between the text and any of the surrounding box's sides
	 * @param text             the label text
	 */
	public static void drawLabel(Graphics2D g, Color textColor, Color backgroundColor, Color borderColor, double transparency, int posX, int posY, int textOffset, String text)
	{
		FontMetrics fontMetrics = g.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(text);
		int textHeight = fontMetrics.getHeight();
		int textDescent = fontMetrics.getDescent();

		int x1 = posX;
		int y1 = posY;
		int x2 = x1 + textOffset + textWidth + textOffset;
		int y2 = y1 + textOffset + textHeight + textOffset;
		int width = x2 - x1;
		int height = y2 - y1;

		if (backgroundColor != null) {
			g.setColor(new Color((float) backgroundColor.getRed() / 255.0f,(float) backgroundColor.getGreen() / 255.0f,(float) backgroundColor.getBlue() / 255.0f,1.0f - (float) transparency));
			g.fillRect(x1,y1,width,height);
		}

		if (borderColor != null) {
			g.setColor(new Color((float) borderColor.getRed() / 255.0f,(float) borderColor.getGreen() / 255.0f,(float) borderColor.getBlue() / 255.0f,1.0f - (float) transparency));
			g.drawRect(x1,y1,width,height);
		}

		g.setColor(textColor);
		g.drawString(text,x1 + textOffset,y2 - textOffset - textDescent);
	}

	/**
	 * Draws a text inside a coloured box with its position centred.
	 *
	 * @param g                a handle to the <CODE>Graphics2D</CODE> object
	 * @param textColor        the text colour
	 * @param backgroundColor  the background colour
	 * @param borderColor      the border colour
	 * @param transparency     the background and border colours' transparency (0.0 is fully opaque, 1.0 is fully transparent)
	 * @param posX             the left position of the box
	 * @param posY             the top position of the box
	 * @param textOffset       the inside single-margin between the text and any of the surrounding box's sides
	 * @param text             the label text
	 */
	public static void drawLabelCentered(Graphics2D g, Color textColor, Color backgroundColor, Color borderColor, double transparency, int posX, int posY, int textOffset, String text)
	{
		FontMetrics fontMetrics = g.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(text);
		int textHeight = fontMetrics.getHeight();
		int textDescent = fontMetrics.getDescent();

		int x1 = posX;
		int y1 = posY;
		int x2 = x1 + textOffset + textWidth + textOffset;
		int y2 = y1 + textOffset + textHeight + textOffset;
		int width = x2 - x1;
		int height = y2 - y1;

		// move to center
		int halfWidth = width / 2;
		int halfHeight = height / 2;
		x1 -= halfWidth;
		y1 -= halfHeight;
		x2 -= halfWidth;
		y2 -= halfHeight;

		if (backgroundColor != null) {
			g.setColor(new Color((float) backgroundColor.getRed() / 255.0f,(float) backgroundColor.getGreen() / 255.0f,(float) backgroundColor.getBlue() / 255.0f,1.0f - (float) transparency));
			g.fillRect(x1,y1,width,height);
		}

		if (borderColor != null) {
			g.setColor(new Color((float) borderColor.getRed() / 255.0f,(float) borderColor.getGreen() / 255.0f,(float) borderColor.getBlue() / 255.0f,1.0f - (float) transparency));
			g.drawRect(x1,y1,width,height);
		}

		g.setColor(textColor);
		g.drawString(text,x1 + textOffset,y2 - textOffset - textDescent);
	}
}
