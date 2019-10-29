// ------------------------------
// Filename      : JGraphics.java
// Author        : Sven Maerivoet
// Last modified : 22/08/2019
// Target        : Java VM (1.8)
// ------------------------------

/**
 * Copyright 2019 Sven Maerivoet
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
 * The <CODE>JGraphics</CODE> class provides static methodes for changing fonts, colors, ...
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 22/08/2019
 */
public final class JGraphics
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation of a <CODE>JGraphics</CODE> object.
	 */
	private JGraphics()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Helper method to return the current font size.
	 *
	 * @param g2D  a handle to the <CODE>Graphics2D</CODE> object
	 * @return     the current font size
	 */	
	public static int getFontSize(Graphics2D g2D)
	{
		return g2D.getFont().getSize();
	}

	/**
	 * Helper method to set the current font size.
	 *
	 * @param g2D       a handle to the <CODE>Graphics2D</CODE> object
	 * @param fontSize  the new font size
	 */	
	public static void setFontSize(Graphics2D g2D, int fontSize)
	{
		Font font = g2D.getFont();
		g2D.setFont(new Font(font.getFontName(),font.getStyle(),fontSize));
	}

	/**
	 * Helper method to return the current font's height.
	 *
	 * @param g2D  a handle to the <CODE>Graphics2D</CODE> object
	 * @return     the current font height
	 */	
	public static int getFontHeight(Graphics2D g2D)
	{
		FontMetrics fontMetrics = g2D.getFontMetrics();
		int textDescent = fontMetrics.getMaxDescent();
		return (fontMetrics.getHeight() + textDescent);
	}

	/**
	 * Helper method to set the font to monospaced.
	 *
	 * @param g2D  a handle to the <CODE>Graphics2D</CODE> object
	 */	
	public static void setFontMonospaced(Graphics2D g2D)
	{
		Font font = g2D.getFont();
		g2D.setFont(new Font(Font.MONOSPACED,font.getStyle(),font.getSize()));
	}

	/**
	 * Helper method to set the font style to bold.
	 *
	 * @param g2D  a handle to the <CODE>Graphics2D</CODE> object
	 */	
	public static void setFontBold(Graphics2D g2D)
	{
		Font font = g2D.getFont();
		g2D.setFont(new Font(font.getFontName(),Font.BOLD,font.getSize()));
	}

	/**
	 * Helper method to set the font style to italic.
	 *
	 * @param g2D  a handle to the <CODE>Graphics2D</CODE> object
	 */	
	public static void setFontItalic(Graphics2D g2D)
	{
		Font font = g2D.getFont();
		g2D.setFont(new Font(font.getFontName(),Font.ITALIC,font.getSize()));
	}

	/**
	 * Helper method to set the font style to bold and italic.
	 *
	 * @param g2D  a handle to the <CODE>Graphics2D</CODE> object
	 */	
	public static void setFontBoldItalic(Graphics2D g2D)
	{
		Font font = g2D.getFont();
		g2D.setFont(new Font(font.getFontName(),Font.BOLD + Font.ITALIC,font.getSize()));
	}

	/**
	 * Sets a color by also taking a specified transparency into account.
	 * 
	 * @param g2D           a handle to the <CODE>Graphics2D</CODE> object
	 * @param color         the color to use
	 * @param transparency  the transparency to use (between 0.0 and 1.0)
	 */
	public static void setColor(Graphics g2D, Color color, double transparency)
	{
		g2D.setColor(new Color((float) color.getRed() / 255.0f,(float) color.getGreen() / 255.0f,(float) color.getBlue() / 255.0f,1.0f - (float) transparency));
	}
}
