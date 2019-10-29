// --------------------------------
// Filename      : JLabelBox.java
// Author        : Sven Maerivoet
// Last modified : 24/08/2019
// Target        : Java VM (1.8)
// --------------------------------

/**
 * Copyright 2003-2016, 2019 Sven Maerivoet
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
 * The <CODE>JLabelBox</CODE> class provides static methodes for drawing labels in boxes and changing font sizes and styles.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 24/08/2019
 */
public final class JLabelBox
{
	/**
	 * A position offset from the viewport boundary.
	 */
	public static final int kViewportOffset = 10;

	// application specifics
	private static final int kTextInterlineSpace = 6;

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
	 * Draws multiple lines of text inside a coloured box.
	 *
	 * @param g2D              a handle to the <CODE>Graphics2D</CODE> object
	 * @param textColor        the text colour
	 * @param backgroundColor  the background colour
	 * @param borderColor      the border colour
	 * @param transparency     the background and border colours' transparency (0.0 is fully opaque, 1.0 is fully transparent)
	 * @param posX             the left position of the box
	 * @param posY             the top position of the box
	 * @param textInset        the inside single-margin between the text and any of the surrounding box's sides
	 * @param viewportBounds   the bounds of the viewport in which the label box should fit (it is ignored if the parameter is <CODE>null</CODE>)
	 * @param text             the label text (specified as new String[] {"String 1","String 2"})
	 * @return                 a <CODE>Point</CODE> containing optionally modified (x,y) coordinates to make the label box fit inside the viewport
	 */
	public static Point drawLabel(Graphics2D g2D, Color textColor, Color backgroundColor, Color borderColor, double transparency, int posX, int posY, int textInset, Rectangle viewportBounds, String[] text)
	{
		PositionInformation positionInformation = new PositionInformation();
		positionInformation = calculatePositionInformation(g2D,posX,posY,textInset,text,positionInformation,viewportBounds);
		drawLabel(g2D,textColor,backgroundColor,borderColor,transparency,text,positionInformation);
		return (new Point(positionInformation.fX1,positionInformation.fY1));
	}

	/**
	 * Draws a single line of text inside a coloured box.
	 *
	 * @param g2D              a handle to the <CODE>Graphics2D</CODE> object
	 * @param textColor        the text colour
	 * @param backgroundColor  the background colour
	 * @param borderColor      the border colour
	 * @param transparency     the background and border colours' transparency (0.0 is fully opaque, 1.0 is fully transparent)
	 * @param posX             the left position of the box
	 * @param posY             the top position of the box
	 * @param textInset        the inside single-margin between the text and any of the surrounding box's sides
	 * @param viewportBounds   the bounds of the viewport in which the label box should fit (it is ignored if the parameter is <CODE>null</CODE>)
	 * @param text             the label text
	 * @return                 a <CODE>Point</CODE> containing optionally modified (x,y) coordinates to make the label box fit inside the viewport
	 */
	public static Point drawLabel(Graphics2D g2D, Color textColor, Color backgroundColor, Color borderColor, double transparency, int posX, int posY, int textInset, Rectangle viewportBounds, String text)
	{
		return drawLabel(g2D,textColor,backgroundColor,borderColor,transparency,posX,posY,textInset,viewportBounds,new String[] {text});
	}

	/**
	 * Draws multiple lines of text inside a coloured box with its position centred.
	 *
	 * @param g2D              a handle to the <CODE>Graphics2D</CODE> object
	 * @param textColor        the text colour
	 * @param backgroundColor  the background colour
	 * @param borderColor      the border colour
	 * @param transparency     the background and border colours' transparency (0.0 is fully opaque, 1.0 is fully transparent)
	 * @param posX             the left position of the box
	 * @param posY             the top position of the box
	 * @param textInset        the inside single-margin between the text and any of the surrounding box's sides
	 * @param viewportBounds   the bounds of the viewport in which the label box should fit (it is ignored if the parameter is <CODE>null</CODE>)
	 * @param text             the label text
	 * @return                 a <CODE>Point</CODE> containing optionally modified (x,y) coordinates to make the label box fit inside the viewport
	 */
	public static Point drawLabelCentered(Graphics2D g2D, Color textColor, Color backgroundColor, Color borderColor, double transparency, int posX, int posY, int textInset, Rectangle viewportBounds, String[] text)
	{
		PositionInformation positionInformation = new PositionInformation();
		positionInformation = calculatePositionInformation(g2D,posX,posY,textInset,text,positionInformation,null);

		// move to center
		int halfWidth = positionInformation.fWidth / 2;
		int halfHeight = positionInformation.fHeight / 2;
		positionInformation.fX1 -= halfWidth;
		positionInformation.fY1 -= halfHeight;
		positionInformation.fX2 -= halfWidth;
		positionInformation.fY2 -= halfHeight;

		drawLabel(g2D,textColor,backgroundColor,borderColor,transparency,text,positionInformation);
		return (new Point(positionInformation.fX1,positionInformation.fY1));
	}

	/**
	 * Draws a single line of text inside a coloured box with its position centred.
	 *
	 * @param g2D              a handle to the <CODE>Graphics2D</CODE> object
	 * @param textColor        the text colour
	 * @param backgroundColor  the background colour
	 * @param borderColor      the border colour
	 * @param transparency     the background and border colours' transparency (0.0 is fully opaque, 1.0 is fully transparent)
	 * @param posX             the left position of the box
	 * @param posY             the top position of the box
	 * @param textInset        the inside single-margin between the text and any of the surrounding box's sides
	 * @param viewportBounds   the bounds of the viewport in which the label box should fit (it is ignored if the parameter is <CODE>null</CODE>)
	 * @param text             the label text
	 * @return                 a <CODE>Point</CODE> containing optionally modified (x,y) coordinates to make the label box fit inside the viewport
	 */
	public static Point drawLabelCentered(Graphics2D g2D, Color textColor, Color backgroundColor, Color borderColor, double transparency, int posX, int posY, int textInset, Rectangle viewportBounds, String text)
	{
		return drawLabelCentered(g2D,textColor,backgroundColor,borderColor,transparency,posX,posY,textInset,viewportBounds,new String[] {text});
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 * @param g2D                  -
	 * @param posX                 -
	 * @param posY                 -
	 * @param textInset            -
	 * @param text                 -
	 * @param positionInformation  -
	 * @param viewportBounds       -
	 * @return                     -
	 */
	private static PositionInformation calculatePositionInformation(Graphics2D g2D, int posX, int posY, int textInset, String[] text, PositionInformation positionInformation, Rectangle viewportBounds)
	{
		// calculate the maximum width of the text
		FontMetrics fontMetrics = g2D.getFontMetrics();
		int maxTextWidth = fontMetrics.stringWidth(text[0]);
		for (int i = 1; i < text.length; ++i) {
			int textWidth = fontMetrics.stringWidth(text[i]);
			if (textWidth > maxTextWidth) {
				maxTextWidth = textWidth;
			}
		}

		positionInformation.fTextInset = textInset;
		positionInformation.fTextDescent = fontMetrics.getMaxDescent();
		positionInformation.fLineHeight = fontMetrics.getHeight() - positionInformation.fTextDescent - 1 + kTextInterlineSpace;

		positionInformation.fX1 = posX;
		positionInformation.fY1 = posY;
		positionInformation.fX2 = positionInformation.fX1 + textInset + maxTextWidth + textInset;
		positionInformation.fY2 = positionInformation.fY1 + textInset + (text.length * positionInformation.fLineHeight) + textInset;
		positionInformation.fWidth = positionInformation.fX2 - positionInformation.fX1;
		positionInformation.fHeight = positionInformation.fY2 - positionInformation.fY1;

		if (viewportBounds != null) {
			if (positionInformation.fX1 < (viewportBounds.x + kViewportOffset)) {
				positionInformation.fX1 = (viewportBounds.x + + kViewportOffset);
				positionInformation.fX2 = positionInformation.fX1 + positionInformation.fWidth;
			}
			if (positionInformation.fY1 < (viewportBounds.y + kViewportOffset)) {
				positionInformation.fY1 = (viewportBounds.y + kViewportOffset);
				positionInformation.fY2 = positionInformation.fY1 + positionInformation.fHeight;
			}
			if (positionInformation.fX2 > (viewportBounds.x + viewportBounds.width - kViewportOffset)) {
				positionInformation.fX1 = (viewportBounds.x + viewportBounds.width - kViewportOffset) - positionInformation.fWidth;
				positionInformation.fX2 = positionInformation.fX1 + positionInformation.fWidth;
			}
			if (positionInformation.fY2 > (viewportBounds.y + viewportBounds.height - kViewportOffset)) {
				positionInformation.fY1 = (viewportBounds.y + viewportBounds.height - kViewportOffset) - positionInformation.fHeight;
				positionInformation.fY2 = positionInformation.fY1 + positionInformation.fHeight;
			}
		}

		return positionInformation;
	}

	/**
	 * @param g2D                  -
	 * @param textColor            -
	 * @param backgroundColor      -
	 * @param borderColor          -
	 * @param transparency         -
	 * @param text                 -
	 * @param positionInformation  -
	 */
	private static void drawLabel(Graphics2D g2D, Color textColor, Color backgroundColor, Color borderColor, double transparency, String[] text, PositionInformation positionInformation)
	{

		if (backgroundColor != null) {
			g2D.setColor(new Color((float) backgroundColor.getRed() / 255.0f,(float) backgroundColor.getGreen() / 255.0f,(float) backgroundColor.getBlue() / 255.0f,1.0f - (float) transparency));
			g2D.fillRect(positionInformation.fX1,positionInformation.fY1,positionInformation.fWidth,positionInformation.fHeight);
		}

		if (borderColor != null) {
			g2D.setColor(new Color((float) borderColor.getRed() / 255.0f,(float) borderColor.getGreen() / 255.0f,(float) borderColor.getBlue() / 255.0f,1.0f - (float) transparency));
			g2D.drawRect(positionInformation.fX1,positionInformation.fY1,positionInformation.fWidth,positionInformation.fHeight);
		}

		g2D.setColor(textColor);
		for (int i = 0; i < text.length; ++i) {
			g2D.drawString(text[i],positionInformation.fX1 + positionInformation.fTextInset,positionInformation.fY2 - positionInformation.fTextInset - positionInformation.fTextDescent - ((text.length - i - 1) * positionInformation.fLineHeight));
		}
	}

	/*******************
	 * PRIVATE CLASSES *
	 *******************/

	/**
 * @author  Sven Maerivoet
 * @version 15/08/2019
	 */
	private static final class PositionInformation
	{
		public int fTextInset;
		public int fTextDescent;
		public int fLineHeight;
		public int fX1;
		public int fY1;
		public int fX2;
		public int fY2;
		public int fWidth;
		public int fHeight;
	}
}
