// ---------------------------------------
// Filename      : JGradientColorRamp.java
// Author        : Sven Maerivoet
// Last modified : 11/07/2011
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

import java.awt.*;
import javax.swing.*;
import smtools.math.*;
import smtools.miscellaneous.*;

/**
 * The <CODE>JGradientColorRamp</CODE> class provides a gradient color ramp.
 * <P>
 * A gradient color ramp provides a visual display of a bar with a spectrum ranging
 * from blue to green to yellow to red:
 * <P>
 * <UL>
 *   <IMG src="doc-files/gradient-color-ramp.png">
 * </UL>
 * <P>
 * A gradient color ramp can have four orientations (see {@link JGradientColorRamp.EOrientation}):
 * <P>
 * <UL>
 *   <LI>horizontal left to right (tick marks are supported),</LI>
 *   <LI>horizontal right to left (tick marks are supported),</LI>
 *   <LI>vertical bottom to top</LI>
 *   <LI>and vertical top to bottom.</LI>
 * </UL>
 * <P>
 * This class has also one static method, which can be used to derive a <CODE>Color</CODE> that
 * is linearly interpolated across the shown spectrum:
 * <P>
 * <UL>
 *  <CODE>Color interpolatedColor = JGradientColorRamp.interpolate(0.6);</CODE>
 * </UL>
 * <P>
 * which corresponds to the following interpolation scheme:
 * <P>
 * <UL>
 *   <IMG src="doc-files/gradient-color-ramp-interpolated.png">
 * </UL>
 * <P>
 * The value can also be indicated on the color ramp itself.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 11/06/2011
 */
public final class JGradientColorRamp extends JPanel
{
	/**
	 * Useful constants to specify a horizontally or vertically oriented gradient color ramp.
	 */
	public static enum EOrientation {kHorizontalLeftToRight, kHorizontalRightToLeft, kVerticalBottomToTop, kVerticalTopToBottom};

	// color-ramp preferences
	private static final float kLowerTreshold = 0.35f;
	private static final float kUpperTreshold = 0.65f;
	private static final float kDifference = kUpperTreshold - kLowerTreshold;
	private static final int kDefaultWidth = 100;
	private static final int kDefaultHeight = 20;
	private static final int kTickMarkSize = 10;
	private static final int kHalfTickMarkSize = (int) Math.round(kTickMarkSize / 2.0);

	// internal datastructures
	private EOrientation fOrientation;
	private int fWidth;
	private int fHeight;
	private boolean fAnnotated;
	private double fLowerTickValue;
	private String fLowerTickValuePrefix;
	private double fUpperTickValue;
	private String fUpperTickValuePrefix;
	private String fTickValuePrefix;
	private String fTickValueSuffix;
	private int fNrOfTickMarks;
	private int fTickValueNrOfDecimals;
	private boolean fValueIndicationEnabled;
	private double fValueToIndicate;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JGradientColorRamp</CODE> object.
	 * <P>
	 * The gradient color ramp has by default a horizontal orientation (going from left
	 * to right) with a width of 100 pixels and a height of 20 pixels.
	 */
	public JGradientColorRamp()
	{
		this(EOrientation.kHorizontalLeftToRight,kDefaultWidth,kDefaultHeight);
	}

	/**
	 * Constructs a <CODE>JGradientColorRamp</CODE> object with the specified orientation and size.
	 *
	 * @param orientation the orientation of the gradient color ramp ({@link JGradientColorRamp.EOrientation})
	 * @param width       the width of the gradient color ramp (expressed in pixels)
	 * @param height      the height of the gradient color ramp (expressed in pixels)
	 */
	public JGradientColorRamp(EOrientation orientation, int width, int height)
	{
		fOrientation = orientation;
		fWidth = width;
		fHeight = height;
		fAnnotated = false;
		setMinimumSize(getMinimumSize());
		setMaximumSize(getMaximumSize());
		setPreferredSize(getPreferredSize());
		setSize(getPreferredSize());
		disableValueIndication();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the optional tick marks for the gradient color ramp.
	 * <P>
	 * Note that these tick marks only appear on <B>horizontally</B>-oriented gradient color ramps.
	 *
	 * @param lowerTickValue the value associated with the left colour
	 * @param lowerTickValuePrefix the prefix label for the value associated with the left colour
	 * @param upperTickValue the value associated with the right colour
	 * @param upperTickValuePrefix the prefix label for the value associated with the right colour
	 * @param tickValuePrefix the prefix label for each value
	 * @param tickValueSuffix the suffix label for each value
	 * @param nrOfTickMarks the number of tick marks to produce
	 * @param nrOfDecimals the number of decimals to retain in the values beneath the tick marks
	 */
	public void setTickMarks(double lowerTickValue, String lowerTickValuePrefix, double upperTickValue, String upperTickValuePrefix, String tickValuePrefix, String tickValueSuffix, int nrOfTickMarks, int nrOfDecimals)
	{
		fAnnotated = true;
		fLowerTickValue = lowerTickValue;
		fLowerTickValuePrefix = lowerTickValuePrefix;
		fUpperTickValue = upperTickValue;
		fUpperTickValuePrefix = upperTickValuePrefix;
		fTickValuePrefix = tickValuePrefix;
		fTickValueSuffix = tickValueSuffix;
		fNrOfTickMarks = nrOfTickMarks;
		fTickValueNrOfDecimals = nrOfDecimals;
	}

	/**
	 */
	public void indicateValue(double value)
	{
		fValueToIndicate = MathTools.clip(value,fLowerTickValue,fUpperTickValue);
		enableValueIndication();

		if (fAnnotated && ((fOrientation == EOrientation.kHorizontalLeftToRight) || (fOrientation == EOrientation.kHorizontalRightToLeft))) {
			setToolTipText(fTickValuePrefix + StringTools.convertDoubleToString(fValueToIndicate,fTickValueNrOfDecimals) + fTickValueSuffix);
		}

		repaint();
	}

	/**
	 */
	public void enableValueIndication()
	{
		fValueIndicationEnabled = true;
	}

	/**
	 */
	public void disableValueIndication()
	{
		fValueIndicationEnabled = false;

		// disable tooltips
		setToolTipText(null);
	}

	/**
	 */
	@Override
	public Dimension getMinimumSize()
	{
		return (new Dimension(fWidth,fHeight));
	}

	/**
	 */
	@Override
	public void setMinimumSize(Dimension dimension)
	{
		super.setMinimumSize(dimension);
		fWidth = dimension.width;
		fHeight = dimension.height;
	}

	/**
	 */
	@Override
	public Dimension getMaximumSize()
	{
		return (new Dimension(fWidth,fHeight));
	}

	/**
	 */
	@Override
	public void setMaximumSize(Dimension dimension)
	{
		super.setMaximumSize(dimension);
		fWidth = dimension.width;
		fHeight = dimension.height;
	}

	/**
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return (new Dimension(fWidth,fHeight));
	}

	/**
	 */
	@Override
	public void setPreferredSize(Dimension dimension)
	{
		super.setPreferredSize(dimension);
		fWidth = dimension.width;
		fHeight = dimension.height;
	}

	/**
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int trueHeight = fHeight;
		FontMetrics fontMetrics = getFontMetrics(getFont());
		if (fAnnotated) {
			fHeight -= (fontMetrics.getAscent() + fontMetrics.getHeight() + fontMetrics.getDescent() + kTickMarkSize);
		}

		// draw outline
		g.setColor(Color.black);
		g.drawRect(0,0,fWidth - 1,fHeight - 1);

		// create gradient color ramp
		if (fOrientation == EOrientation.kHorizontalLeftToRight) {

			for (int x = 1; x < (fWidth - 1); ++x) {
				double interpolationValue = ((double) x - 1.0) / ((double) fWidth - 3.0);
				g.setColor(JGradientColorRamp.interpolate(interpolationValue));
				g.drawLine(x,1,x,fHeight - 2);
			}
		}
		else if (fOrientation == EOrientation.kHorizontalRightToLeft) {

			for (int x = 1; x < (fWidth - 1); ++x) {
				double interpolationValue = 1.0 - (((double) x - 1.0) / ((double) fWidth - 3.0));
				g.setColor(JGradientColorRamp.interpolate(interpolationValue));
				g.drawLine(x,1,x,fHeight - 2);
			}
		}
		else if (fOrientation == EOrientation.kVerticalBottomToTop) {

			for (int y = 1; y < (fHeight - 1); ++y) {
				double interpolationValue = 1.0 - (((double) y - 1.0) / ((double) fHeight - 3.0));
				g.setColor(JGradientColorRamp.interpolate(interpolationValue));
				g.drawLine(1,y,fWidth - 2,y);
			}
		}
		else if (fOrientation == EOrientation.kVerticalTopToBottom) {

			for (int y = 1; y < (fHeight - 1); ++y) {
				double interpolationValue = ((double) y - 1.0) / ((double) fHeight - 3.0);
				g.setColor(JGradientColorRamp.interpolate(interpolationValue));
				g.drawLine(1,y,fWidth - 2,y);
			}
		}

		if (fAnnotated && ((fOrientation == EOrientation.kHorizontalLeftToRight) || (fOrientation == EOrientation.kHorizontalRightToLeft))) {
			for (int tickMarkIndex = 0; tickMarkIndex < fNrOfTickMarks; ++tickMarkIndex) {
				g.setColor(Color.BLACK);
				int tickMarkX1 = (fWidth / (fNrOfTickMarks - 1)) * tickMarkIndex;
				if (tickMarkIndex == (fNrOfTickMarks - 1)) {
					tickMarkX1 = fWidth - 1;
				}
				int tickMarkX2 = tickMarkX1;
				int tickMarkY1 = trueHeight - (trueHeight - fHeight) - kHalfTickMarkSize;
				int tickMarkY2 = tickMarkY1 + kHalfTickMarkSize + kTickMarkSize;
				g.drawLine(tickMarkX1,tickMarkY1,tickMarkX2,tickMarkY2);
			
				int textYPos = (trueHeight - (trueHeight - fHeight)) + (fontMetrics.getAscent() + fontMetrics.getHeight());
				double tickValue = fLowerTickValue + ((fUpperTickValue - fLowerTickValue) / (fNrOfTickMarks - 1)) * tickMarkIndex;

				String tickValueStr = fTickValuePrefix + StringTools.convertDoubleToString(tickValue,fTickValueNrOfDecimals) + fTickValueSuffix;
				if (tickMarkIndex == 0) {
					tickValueStr = fLowerTickValuePrefix + tickValueStr;
				}
				else if (tickMarkIndex == (fNrOfTickMarks - 1)) {
					tickValueStr = fUpperTickValuePrefix + tickValueStr;
				}

				// center tick values, except for the first (left-centered) and last (right-centered) tick values
				int tickValueXPos = 0;
				if (tickMarkIndex == (fNrOfTickMarks - 1)) {
					tickValueXPos = fWidth - fontMetrics.stringWidth(tickValueStr);
				}
				else if (tickMarkIndex > 0) { 
					tickValueXPos = tickMarkX1 - (int) Math.round((fontMetrics.stringWidth(tickValueStr) / 2.0));
				}
				g.drawString(tickValueStr,tickValueXPos,textYPos);			
			}

			if (fValueIndicationEnabled) {
				int indicationX1 = (int) Math.round(fWidth * ((fValueToIndicate - fLowerTickValue) / (fUpperTickValue - fLowerTickValue)));
				int indicationX2 = indicationX1;
				int indicationY1 = 0;
				int indicationY2 = fHeight - 1;
				g.setColor(Color.BLACK);
				if (indicationX1 > 0) {
					g.drawLine(indicationX1 - 1,indicationY1,indicationX2 - 1,indicationY2);
				}
				g.drawLine(indicationX1,indicationY1,indicationX2,indicationY2);
				if (indicationX1 < (fWidth - 1)) {
					g.drawLine(indicationX1 + 1,indicationY1,indicationX2 + 1,indicationY2);
				}
			}

			// restore height
			fHeight = trueHeight;
		}
	}

	/**
	 * Derives a <CODE>Color</CODE> that is linearly interpolated across a spectrum
	 * going from blue to green to yellow to red.
	 * <P>
	 * Note that the <CODE>normalisedValue</CODE> is clipped in the interval [0,1].
	 *
	 * @param normalisedValue the value to use when interpolating the spectrum
	 */
	public static Color interpolate(double normalisedValue)
	{
		normalisedValue = MathTools.clip(normalisedValue,0.0,1.0);

		float red = 0.0f;
		float green = 0.0f;
		float blue = 0.0f;

		if (normalisedValue <= kLowerTreshold) {
			// interpolate from blue to green
			float t = (float) normalisedValue / kLowerTreshold; // t lies in [0,1]
			red = 0.0f;
			green = t;
			blue = 1.0f - t;
		}
		else if (normalisedValue <= kUpperTreshold) {
			// interpolate from green to yellow
			float t = ((float) normalisedValue - kLowerTreshold) / kDifference; // t lies in [0,1]
			red = t;
			green = 1.0f;
			blue = 0.0f;
		}
		else if (normalisedValue <= 100) {
			// interpolate from yellow to red
			float t = ((float) normalisedValue - kUpperTreshold) / (1.0f - kUpperTreshold); // t lies in [0,1]
			red = 1.0f;
			green = 1.0f - t;
			blue = 0.0f;
		}

		Color color = new Color(red,green,blue);

		return color;
	}
}
