// ---------------------------------------
// Filename      : JGradientColorRamp.java
// Author        : Sven Maerivoet
// Last modified : 21/12/2012
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
 * The <CODE>JGradientColorRamp</CODE> class provides a gradient colour ramp.
 * <P>
 * A gradient colour ramp provides a visual display of a bar with a certain specified spectrum:
 * <P>
 * <UL>
 *   <B>Gray scale:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-grayscale.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Jet:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-jet.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Copper:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-copper.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Bone:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-bone.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Green-red diverging:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-greenreddiverging.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Hot:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-hot.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Discontinuous blue-white-green:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-discontinuousbluewhitegreen.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Discontinuous dark-red-yellow:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-discontinuousdarkredyellow.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Black and white:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-blackandwhite.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Hue/saturation/brightness (HSB):</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-huesaturationbrightness.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Red:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-red.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Green:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-green.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Blue:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-blue.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Yellow:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-yellow.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Cyan:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-cyan.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Magenta:</B><BR />
 *   <IMG src="doc-files/gradient-color-ramp-magenta.png">
 * </UL>
 * <P>
 * A gradient colour ramp can have four orientations (see {@link JGradientColorRamp.EOrientation}):
 * <P>
 * <UL>
 *   <LI>horizontal left to right (tick marks are supported),</LI>
 *   <LI>horizontal right to left (tick marks are supported),</LI>
 *   <LI>vertical bottom to top</LI>
 *   <LI>and vertical top to bottom.</LI>
 * </UL>
 * <P>
 * This class has also one method that can be used to derive a <CODE>Color</CODE> that
 * is linearly interpolated across the specified spectrum:
 * <P>
 * <UL>
 *  <CODE>Color interpolatedColor = myGradientColorRamp.interpolate(0.6);</CODE>
 * </UL>
 * <P>
 * or via a static method:
 * <P>
 * <UL>
 *  <CODE>Color interpolatedColor = JGradientColorRamp.interpolate(0.6,EColorMap.kJet);</CODE>
 * </UL>
 * <P>
 * both, e.g. corresponds to the following interpolation scheme:
 * <P>
 * <UL>
 *   <IMG src="doc-files/gradient-color-ramp-interpolated.png">
 * </UL>
 * <P>
 * The value can also be indicated on the colour ramp itself.
 * <P>
 * <I>All documentation is written in British English, except for the API-code, which was kept in American English for compatability
 * with the Java API interface.</I>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 21/12/2012
 */
public final class JGradientColorRamp extends JPanel
{
	/**
	 * The supported horizontal and vertical orientations for the gradient colour ramp.
	 */
	public static enum EOrientation {kHorizontalLeftToRight, kHorizontalRightToLeft, kVerticalBottomToTop, kVerticalTopToBottom};

	/**
	 * The various supported colour maps.
	 */
	public static enum EColorMap
		{kGrayScale, kJet, kCopper, kBone, kGreenRedDiverging, kHot,
		 kDiscontinuousBlueWhiteGreen, kDiscontinuousDarkRedYellow,
		 kBlackAndWhite, kHueSaturationBrightness,
		 kRed, kGreen, kBlue, kYellow, kCyan, kMagenta};

	// colour ramp preferences
	private static final float kLowerTreshold = 0.33f;
	private static final float kUpperTreshold = 0.66f;
	private static final float kDifference = kUpperTreshold - kLowerTreshold;
	private static final int kDefaultWidth = 100;
	private static final int kDefaultHeight = 20;
	private static final int kTickMarkSize = 10;
	private static final int kHalfTickMarkSize = (int) Math.round(kTickMarkSize / 2.0);

	// internal datastructures
	private EOrientation fOrientation;
	private EColorMap fColorMap;
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
	 * The gradient colour ramp has by default a horizontal orientation (going from left
	 * to right) with a width of 100 pixels and a height of 20 pixels; the jet colour map is used by default.
	 */
	public JGradientColorRamp()
	{
		this(EOrientation.kHorizontalLeftToRight,kDefaultWidth,kDefaultHeight);
	}

	/**
	 * Constructs a <CODE>JGradientColorRamp</CODE> object with the specified orientation and size.
	 * <P>
	 * The jet colour map is used by default.
	 *
	 * @param orientation the orientation of the gradient colour ramp ({@link JGradientColorRamp.EOrientation})
	 * @param width       the width of the gradient colour ramp (expressed in pixels)
	 * @param height      the height of the gradient colour ramp (expressed in pixels)
	 */
	public JGradientColorRamp(EOrientation orientation, int width, int height)
	{
		this(orientation,EColorMap.kJet,width,height);
	}

	/**
	 * Constructs a <CODE>JGradientColorRamp</CODE> object with a specified colour map.
	 * <P>
	 * The gradient colour ramp has by default a horizontal orientation (going from left
	 * to right) with a width of 100 pixels and a height of 20 pixels.
	 */
	public JGradientColorRamp(EColorMap colorMap)
	{
		this(EOrientation.kHorizontalLeftToRight,colorMap,kDefaultWidth,kDefaultHeight);
	}

	/**
	 * Constructs a <CODE>JGradientColorRamp</CODE> object with the specified orientation and size.
	 *
	 * @param orientation the orientation of the gradient colour ramp ({@link JGradientColorRamp.EOrientation})
	 * @param colorMap    the colour map to use
	 * @param width       the width of the gradient colour ramp (expressed in pixels)
	 * @param height      the height of the gradient colour ramp (expressed in pixels)
	 */
	public JGradientColorRamp(EOrientation orientation, EColorMap colorMap, int width, int height)
	{
		fOrientation = orientation;
		fColorMap = colorMap;
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
	 * Changes the colour map that is used.
	 * 
	 * @param colorMap the colour map to use
	 */
	public void setColorMap(EColorMap colorMap)
	{
		fColorMap = colorMap;
		repaint();
	}

	/**
	 * Returns the colour map that is used.
	 * 
	 * @return the colour map that is used
	 */
	public EColorMap getColorMap()
	{
		return fColorMap;
	}

	/**
	 * Sets the optional tick marks for the gradient colour ramp.
	 * <P>
	 * Note that these tick marks only appear on <B>horizontally</B>-oriented gradient colour ramps.
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

		// create gradient colour ramp
		if (fOrientation == EOrientation.kHorizontalLeftToRight) {

			for (int x = 1; x < (fWidth - 1); ++x) {
				double interpolationValue = ((double) x - 1.0) / ((double) fWidth - 3.0);
				g.setColor(interpolate(interpolationValue));
				g.drawLine(x,1,x,fHeight - 2);
			}
		}
		else if (fOrientation == EOrientation.kHorizontalRightToLeft) {

			for (int x = 1; x < (fWidth - 1); ++x) {
				double interpolationValue = 1.0 - (((double) x - 1.0) / ((double) fWidth - 3.0));
				g.setColor(interpolate(interpolationValue));
				g.drawLine(x,1,x,fHeight - 2);
			}
		}
		else if (fOrientation == EOrientation.kVerticalBottomToTop) {

			for (int y = 1; y < (fHeight - 1); ++y) {
				double interpolationValue = 1.0 - (((double) y - 1.0) / ((double) fHeight - 3.0));
				g.setColor(interpolate(interpolationValue));
				g.drawLine(1,y,fWidth - 2,y);
			}
		}
		else if (fOrientation == EOrientation.kVerticalTopToBottom) {

			for (int y = 1; y < (fHeight - 1); ++y) {
				double interpolationValue = ((double) y - 1.0) / ((double) fHeight - 3.0);
				g.setColor(interpolate(interpolationValue));
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
	 * Derives a <CODE>Color</CODE> that is linearly interpolated across a spectrum.
	 * <P>
	 * Note that the value of <CODE>u</CODE> is clipped in the interval [0,1].
	 *
	 * @param u the value to use when interpolating the spectrum
	 */
	public Color interpolate(double u)
	{
		return interpolate(u,fColorMap);
	}

	/******************
	 * STATIC METHODS *
	 ******************/

	/**
	 * Derives a <CODE>Color</CODE> that is linearly interpolated across a spectrum from a specified colour map.
	 * <P>
	 * Note that the value of <CODE>u</CODE> is clipped in the interval [0,1].
	 *
	 * @param u the value to use when interpolating the spectrum
	 * @param colorMap the colour map to use
	 */
	public static Color interpolate(double u, EColorMap colorMap)
	{
		float t = (float) MathTools.clip(u,0.0,1.0);

		float red = 0.0f;
		float green = 0.0f;
		float blue = 0.0f;

		if (colorMap == EColorMap.kGrayScale) {			
			red = t;
			green = t;
			blue = t;
		}
		else if (colorMap == EColorMap.kJet) {
			if (t <= kLowerTreshold) {
				// interpolate from blue to green
				t = t / kLowerTreshold;
				red = 0.0f;
				green = t;
				blue = 1.0f - t;
			}
			else if (t <= kUpperTreshold) {
				// interpolate from green to yellow
				t = (t - kLowerTreshold) / kDifference;
				red = t;
				green = 1.0f;
				blue = 0.0f;
			}
			else if (t <= 100) {
				// interpolate from yellow to red
				t = (t - kUpperTreshold) / (1.0f - kUpperTreshold);
				red = 1.0f;
				green = 1.0f - t;
				blue = 0.0f;
			}
		}
		else if (colorMap == EColorMap.kCopper) {
			red = (float) MathTools.clip(t * 1.5f,0.0,1.0);
			green = t;
			blue = t / 10.0f;
		}
		else if (colorMap == EColorMap.kBone) {
			float n = 3.0f / 8.0f;
			if (t < n) {
				red = 0.0f;
				green = 0.0f;
				blue = t * (1.0f / n);
			}
			else if (t < (2.0f * n)) {
				red = 0.0f;
				green = (t - n) * (1.0f / n);
				blue = 1.0f;
			}
			else {
				red = (t - (2.0f * n)) * (1.0f / n);
				green = 1.0f;
				blue = 1.0f;
			}
			red = ((7.0f * t) + red) / 8.0f;
			green = ((7.0f * t) + green) / 8.0f;
			blue = ((7.0f * t) + blue) / 8.0f;
		}
		else if (colorMap == EColorMap.kGreenRedDiverging) {
			if (t < 0.5f) {
				float divergingFactor = t / 0.5f;
				red = divergingFactor;
				green = 1.0f;
				blue = divergingFactor;
			}
			else {
				float divergingFactor = (1.0f - t) / 0.5f;
				red = 1.0f;
				green = divergingFactor;
				blue = divergingFactor;
			}
		}
		else if (colorMap == EColorMap.kHot) {
			float n = 3.0f / 8.0f;
			if (t < n) {
				red = t * (1.0f / n);
				green = 0.0f;
				blue = 0.0f;
			}
			else if (t < (2.0f * n)) {
				red = 1.0f;
				green = (t - n) * (1.0f / n);
				blue = 0.0f;
			}
			else {
				red = 1.0f;
				green = 1.0f;
				blue = (t - (2.0f * n)) * (1.0f / n);
			}
		}
		else if (colorMap == EColorMap.kDiscontinuousBlueWhiteGreen) {
			if (t < 0.0625f) {
				red = t * 8.0f;
				green = t * 8.0f;
				blue = 0.5f + (t * 4.0f);
			}
			else if (t < 0.25f) {
				red = 0.5f + t - 0.0625f;
				green = 0.5f + t - 0.0625f;
				blue = 0.75f + t - 0.0625f;
			}
			else {
				float x = (319.0f / 256.0f) - t;
				red = x;
				green = 0.5f + (x / 2.0f);
				blue = x;
			}
		}
		else if (colorMap == EColorMap.kDiscontinuousDarkRedYellow) {
			if (t < 0.33f) {
				red = t;
				green = 0.0f;
				blue = 0.0f;
			}
			else if (t < 0.66f) {
				red = 1.0f;
				green = t;
				blue = 0.0f;
			}
			else {
				red = 1.0f;
				green = 1.0f;
				blue = t;
			}
		}
		else if (colorMap == EColorMap.kBlackAndWhite) {
			if (t < 0.5f) {
				red = 0.0f;
				green = 0.0f;
				blue = 0.0f;
			}
			else {
				red = 1.0f;
				green = 1.0f;
				blue = 1.0f;
			}
		}
		else if (colorMap == EColorMap.kHueSaturationBrightness) {
			return Color.getHSBColor(t,1.0f,1.0f);
		}
		else if (colorMap == EColorMap.kRed) {
			red = t;
		}
		else if (colorMap == EColorMap.kGreen) {
			green = t;
		}
		else if (colorMap == EColorMap.kBlue) {
			blue = t;
		}
		else if (colorMap == EColorMap.kYellow) {
			red = t;
			green = t;
		}
		else if (colorMap == EColorMap.kCyan) {
			green = t;
			blue = t;
		}
		else if (colorMap == EColorMap.kMagenta) {
			red = t;
			blue = t;
		}

		Color color = new Color(red,green,blue);

		return color;
	}
}
