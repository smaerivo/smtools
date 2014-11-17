// --------------------------------------
// Filename      : JGradientColorMap.java
// Author        : Sven Maerivoet
// Last modified : 17/11/2014
// Target        : Java VM (1.8)
// --------------------------------------

/**
 * Copyright 2003-2014 Sven Maerivoet
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
import java.util.*;
import javax.swing.*;
import org.sm.smtools.math.*;
import org.sm.smtools.util.*;

/**
 * The <CODE>JGradientColorMap</CODE> class provides a gradient colour map.
 * <P>
 * A gradient colour map provides a visual display of a bar with a certain specified spectrum:
 * <P>
 * <B>Gray scale:</B><BR>
 * <IMG src="doc-files/gradient-color-map-grayscale.png" alt="">
 * <P>
 * <B>Jet:</B><BR>
 * <IMG src="doc-files/gradient-color-map-jet.png" alt="">
 * <P>
 * <B>Copper:</B><BR>
 * <IMG src="doc-files/gradient-color-map-copper.png" alt="">
 * <P>
 * <B>Bone:</B><BR>
 * <IMG src="doc-files/gradient-color-map-bone.png" alt="">
 * <P>
 * <B>Green-red diverging:</B><BR>
 * <IMG src="doc-files/gradient-color-map-greenreddiverging.png" alt="">
 * <P>
 * <B>Hot:</B><BR>
 * <IMG src="doc-files/gradient-color-map-hot.png" alt="">
 * <P>
 * <B>Discontinuous blue-white-green:</B><BR>
 * <IMG src="doc-files/gradient-color-map-discontinuousbluewhitegreen.png" alt="">
 * <P>
 * <B>Discontinuous dark-red-yellow:</B><BR>
 * <IMG src="doc-files/gradient-color-map-discontinuousdarkredyellow.png" alt="">
 * <P>
 * <B>Black and white:</B><BR>
 * <IMG src="doc-files/gradient-color-map-blackandwhite.png" alt="">
 * <P>
 * <B>Hue/saturation/brightness (HSB):</B><BR>
 * <IMG src="doc-files/gradient-color-map-huesaturationbrightness.png" alt="">
 * <P>
 * <B>Separated red/green/blue:</B><BR>
 * <IMG src="doc-files/gradient-color-map-separatedredgreenblue.png" alt="">
 * <P>
 * <B>Red:</B><BR>
 * <IMG src="doc-files/gradient-color-map-red.png" alt="">
 * <P>
 * <B>Green:</B><BR>
 * <IMG src="doc-files/gradient-color-map-green.png" alt="">
 * <P>
 * <B>Blue:</B><BR>
 * <IMG src="doc-files/gradient-color-map-blue.png" alt="">
 * <P>
 * <B>Yellow:</B><BR>
 * <IMG src="doc-files/gradient-color-map-yellow.png" alt="">
 * <P>
 * <B>Cyan:</B><BR>
 * <IMG src="doc-files/gradient-color-map-cyan.png" alt="">
 * <P>
 * <B>Magenta:</B><BR>
 * <IMG src="doc-files/gradient-color-map-magenta.png" alt="">
 * <P>
 * <B>Ultralight pastel:</B><BR>
 * <IMG src="doc-files/gradient-color-map-ultralightpastel.png" alt="">
 * <P>
 * <B>Light pastel:</B><BR>
 * <IMG src="doc-files/gradient-color-map-lightpastel.png" alt="">
 * <P>
 * <B>Dark pastel:</B><BR>
 * <IMG src="doc-files/gradient-color-map-darkpastel.png" alt="">
 * <P>
 * <B>Greens:</B><BR>
 * <IMG src="doc-files/gradient-color-map-greens.png" alt="">
 * <P>
 * <B>Blues:</B><BR>
 * <IMG src="doc-files/gradient-color-map-blues.png" alt="">
 * <P>
 * <B>Yellow browns:</B><BR>
 * <IMG src="doc-files/gradient-color-map-yellowbrowns.png" alt="">
 * <P>
 * <B>Violet purples:</B><BR>
 * <IMG src="doc-files/gradient-color-map-violetpurples.png" alt="">
 * <P>
 * <B>Deep space:</B><BR>
 * <IMG src="doc-files/gradient-color-map-deepspace.png" alt="">
 * <P>
 * <B>Custom:</B><BR>
 * Dependent on the colours specified.
 * <P>
 * A gradient colour map can have four orientations (see {@link JGradientColorMap.EOrientation}):
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
 * <CODE>Color interpolatedColor = myGradientColorMap.interpolate(0.6);</CODE>
 * <P>
 * This corresponds to the following interpolation scheme (dependent on the colour map):
 * <P>
 * <IMG src="doc-files/gradient-color-map-interpolated.png" alt="">
 * <P>
 * The value can also be indicated on the colour map itself.
 * <P>
 * <I>All documentation is written in British English, except for the API-code, which was kept in American English for compatability
 * with the Java API interface.</I>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 17/11/2014
 */
public final class JGradientColorMap extends JPanel
{
	/**
	 * The supported horizontal and vertical orientations for the gradient colour map.
	 */
	public static enum EOrientation {kHorizontalLeftToRight, kHorizontalRightToLeft, kVerticalBottomToTop, kVerticalTopToBottom};

	/**
	 * The various supported colour maps.
	 */
	public static enum EColorMap
		{kGrayScale,
		 kJet,
		 kCopper,
		 kBone,
		 kGreenRedDiverging,
		 kHot,
		 kDiscontinuousBlueWhiteGreen,
		 kDiscontinuousDarkRedYellow,
		 kBlackAndWhite,
		 kHueSaturationBrightness,
		 kSeparatedRGB,
		 kRed,
		 kGreen,
		 kBlue,
		 kYellow,
		 kCyan,
		 kMagenta,
		 kUltraLightPastel,
		 kLightPastel,
		 kDarkPastel,
		 kGreens,
		 kBlues,
		 kYellowBrowns,
		 kVioletPurples,
		 kDeepSpace,
		 kCustom};

	// colour map preferences
	private static final float kLowerTreshold = 0.33f;
	private static final float kHigherTreshold = 0.66f;
	private static final float kDifference = kHigherTreshold - kLowerTreshold;
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
	private double fHigherTickValue;
	private String fHigherTickValuePrefix;
	private String fTickValuePrefix;
	private String fTickValueSuffix;
	private int fNrOfTickMarks;
	private int fTickValueNrOfDecimals;
	private boolean fValueIndicationEnabled;
	private double fValueToIndicate;
	private TreeMap<Integer,CustomColorMapComponent> fCustomColorMapComponents;
	private TreeMap<Double,Color> fCustomColorMap;
	private TreeMap<Double,Color> fUltraLightPastelColorMap;
	private TreeMap<Double,Color> fLightPastelColorMap;
	private TreeMap<Double,Color> fDarkPastelColorMap;
	private TreeMap<Double,Color> fGreensColorMap;
	private TreeMap<Double,Color> fBluesColorMap;
	private TreeMap<Double,Color> fYellowBrownsColorMap;
	private TreeMap<Double,Color> fVioletPurplesColorMap;
	private TreeMap<Double,Color> fDeepSpaceColorMap;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JGradientColorMap</CODE> object.
	 * <P>
	 * The gradient colour map has by default a horizontal orientation (going from left
	 * to right) with a width of 100 pixels and a height of 20 pixels; the jet colour map is used by default.
	 */
	public JGradientColorMap()
	{
		this(EOrientation.kHorizontalLeftToRight,kDefaultWidth,kDefaultHeight);
	}

	/**
	 * Constructs a <CODE>JGradientColorMap</CODE> object with the specified orientation and size.
	 * <P>
	 * The jet colour map is used by default.
	 *
	 * @param orientation  the orientation of the gradient colour map ({@link JGradientColorMap.EOrientation})
	 * @param width        the width of the gradient colour map (expressed in pixels)
	 * @param height       the height of the gradient colour map (expressed in pixels)
	 */
	public JGradientColorMap(EOrientation orientation, int width, int height)
	{
		this(orientation,EColorMap.kJet,width,height);
	}

	/**
	 * Constructs a <CODE>JGradientColorMap</CODE> object with a specified colour map.
	 * <P>
	 * The gradient colour map has by default a horizontal orientation (going from left
	 * to right) with a width of 100 pixels and a height of 20 pixels.
	 *
	 * @param colorMap  the colour map to use
	 */
	public JGradientColorMap(EColorMap colorMap)
	{
		this(EOrientation.kHorizontalLeftToRight,colorMap,kDefaultWidth,kDefaultHeight);
	}

	/**
	 * Constructs a <CODE>JGradientColorMap</CODE> object with the specified orientation and size.
	 *
	 * @param orientation  the orientation of the gradient colour map ({@link JGradientColorMap.EOrientation})
	 * @param colorMap     the colour map to use
	 * @param width        the width of the gradient colour map (expressed in pixels)
	 * @param height       the height of the gradient colour map (expressed in pixels)
	 */
	public JGradientColorMap(EOrientation orientation, EColorMap colorMap, int width, int height)
	{
		clearAllCustomColorMapComponents();
		fOrientation = orientation;
		fWidth = width;
		fHeight = height;
		fAnnotated = false;
		setMinimumSize(getMinimumSize());
		setMaximumSize(getMaximumSize());
		setPreferredSize(getPreferredSize());
		setSize(getPreferredSize());
		disableValueIndication();
		setupCustomColorMaps();
		setColorMap(colorMap);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Changes the colour map that is used.
	 * 
	 * @param colorMap  the colour map to use
	 */
	public void setColorMap(EColorMap colorMap)
	{
		fColorMap = colorMap;

		if (fColorMap == EColorMap.kUltraLightPastel) {
			fCustomColorMap = fUltraLightPastelColorMap;
		}
		if (fColorMap == EColorMap.kLightPastel) {
			fCustomColorMap = fLightPastelColorMap;
		}
		if (fColorMap == EColorMap.kDarkPastel) {
			fCustomColorMap = fDarkPastelColorMap;
		}
		else if (fColorMap == EColorMap.kGreens) {
			fCustomColorMap = fGreensColorMap;
		}
		else if (fColorMap == EColorMap.kBlues) {
			fCustomColorMap = fBluesColorMap;
		}
		else if (fColorMap == EColorMap.kYellowBrowns) {
			fCustomColorMap = fYellowBrownsColorMap;
		}
		else if (fColorMap == EColorMap.kVioletPurples) {
			fCustomColorMap = fVioletPurplesColorMap;
		}
		else if (fColorMap == EColorMap.kDeepSpace) {
			fCustomColorMap = fDeepSpaceColorMap;
		}
	
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
	 * Sets the optional tick marks for the gradient colour map.
	 * <P>
	 * Note that these tick marks only appear on <B>horizontally</B>-oriented gradient colour maps.
	 *
	 * @param lowerTickValue         the value associated with the left colour
	 * @param lowerTickValuePrefix   the prefix label for the value associated with the left colour
	 * @param higherTickValue        the value associated with the right colour
	 * @param higherTickValuePrefix  the prefix label for the value associated with the right colour
	 * @param tickValuePrefix        the prefix label for each value
	 * @param tickValueSuffix        the suffix label for each value
	 * @param nrOfTickMarks          the number of tick marks to produce
	 * @param nrOfDecimals           the number of decimals to retain in the values beneath the tick marks
	 */
	public void setTickMarks(double lowerTickValue, String lowerTickValuePrefix, double higherTickValue, String higherTickValuePrefix, String tickValuePrefix, String tickValueSuffix, int nrOfTickMarks, int nrOfDecimals)
	{
		fAnnotated = true;
		fLowerTickValue = lowerTickValue;
		fLowerTickValuePrefix = lowerTickValuePrefix;
		fHigherTickValue = higherTickValue;
		fHigherTickValuePrefix = higherTickValuePrefix;
		fTickValuePrefix = tickValuePrefix;
		fTickValueSuffix = tickValueSuffix;
		fNrOfTickMarks = nrOfTickMarks;
		fTickValueNrOfDecimals = nrOfDecimals;
	}

	/**
	 * Instructs to indicate the specified value
	 * 
	 * @param value  the value to indicate
	 */
	public void indicateValue(double value)
	{
		fValueToIndicate = MathTools.clip(value,fLowerTickValue,fHigherTickValue);
		enableValueIndication();

		if (fAnnotated && ((fOrientation == EOrientation.kHorizontalLeftToRight) || (fOrientation == EOrientation.kHorizontalRightToLeft))) {
			setToolTipText(fTickValuePrefix + StringTools.convertDoubleToString(fValueToIndicate,fTickValueNrOfDecimals) + fTickValueSuffix);
		}

		repaint();
	}

	/**
	 * Enables the indication of values.
	 */
	public void enableValueIndication()
	{
		fValueIndicationEnabled = true;
	}

	/**
	 * Disables the indication of values.
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

		// create gradient colour map
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
				double tickValue = fLowerTickValue + ((fHigherTickValue - fLowerTickValue) / (fNrOfTickMarks - 1)) * tickMarkIndex;

				String tickValueStr = fTickValuePrefix + StringTools.convertDoubleToString(tickValue,fTickValueNrOfDecimals) + fTickValueSuffix;
				if (tickMarkIndex == 0) {
					tickValueStr = fLowerTickValuePrefix + tickValueStr;
				}
				else if (tickMarkIndex == (fNrOfTickMarks - 1)) {
					tickValueStr = fHigherTickValuePrefix + tickValueStr;
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
				int indicationX1 = (int) Math.round(fWidth * ((fValueToIndicate - fLowerTickValue) / (fHigherTickValue - fLowerTickValue)));
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
	 * Adds or updates a component in the custom colour map.
	 *
	 * @param id     the ID of the component
	 * @param level  the level of the component (between 0.0 and 1.0)
	 * @param color  the <CODE>Color</CODE> to associate with the component
	*/
	public void setCustomColorMapComponent(int id, double level, Color color)
	{
		removeCustomColorMapComponent(id);

		// constrain the level
		level = MathTools.clip(level,0.0,1.0);

		// store the mapping from ID to color map component
		CustomColorMapComponent customColorMapComponent = new CustomColorMapComponent(level,color);
		fCustomColorMapComponents.put(id,customColorMapComponent);

		// update the color map
		fCustomColorMap.put(level,color);
	}

	/**
	 * Removes a component from the custom colour map.
	 *
	 * @param id  the ID of the component
	 */
	public void removeCustomColorMapComponent(int id)
	{
		if (fCustomColorMapComponents.containsKey(id)) {
			fCustomColorMapComponents.remove(id);

			// rebuild custom color map
			fCustomColorMap = new TreeMap<Double,Color>();
			for (CustomColorMapComponent customColorMapComponent : fCustomColorMapComponents.values()) {
				fCustomColorMap.put(customColorMapComponent.fLevel,customColorMapComponent.fColor);
			}
		}
	}

	/**
	 * Clears all components from the custom colour map.
	 */
	public void clearAllCustomColorMapComponents()
	{
		fCustomColorMapComponents = new TreeMap<Integer,CustomColorMapComponent>();
		fCustomColorMap = new TreeMap<Double,Color>();
	}

	/**
	 * Sets all components of the custom colour map.
	 *
	 * @param colorMapComponents  a <CODE>TreeMap</CODE> containing all the custom colour map components
	 */
	public void setAllCustomColorMapComponents(TreeMap<Integer,CustomColorMapComponent> colorMapComponents)
	{
		clearAllCustomColorMapComponents();
		for (int id : colorMapComponents.keySet()) {
			CustomColorMapComponent colorMapComponent = colorMapComponents.get(id);
			setCustomColorMapComponent(id,colorMapComponent.fLevel,colorMapComponent.fColor);
		}
	}

	/**
	 * Returns all components of the custom colour map.
	 *
	 * @return a <CODE>TreeMap</CODE> containing all the custom colour map components
	 */
	public TreeMap<Integer,CustomColorMapComponent> getAllCustomColorMapComponents()
	{
		return fCustomColorMapComponents;
	}

	/**
	 * Converts the current colour map to a specified number of components.
	 *
	 * @param nrOfComponents  the specified number of components
	 * @return                the converted colour map
	 */
	public TreeMap<Integer,JGradientColorMap.CustomColorMapComponent> convertToComponents(int nrOfComponents)
	{
		TreeMap<Integer,JGradientColorMap.CustomColorMapComponent> colorMapComponents = new TreeMap<Integer,JGradientColorMap.CustomColorMapComponent>();

		for (int componentNr = 0; componentNr < nrOfComponents; ++componentNr) {
			double u = (double) componentNr / ((double) nrOfComponents - 1.0);
			
			CustomColorMapComponent colorMapComponent = new CustomColorMapComponent(u,interpolate(u));
			colorMapComponents.put(componentNr,colorMapComponent);
		}

		return colorMapComponents;
	}

	/**
	 * Derives a <CODE>Color</CODE> that is linearly interpolated across a spectrum.
	 * <P>
	 * Note that the value of <CODE>u</CODE> is clipped in the interval [0,1].
	 *
	 * @param u  the value to use when interpolating the spectrum
	 * @return   a linearly interpolated value across a spectrum
	 */
	public Color interpolate(double u)
	{
		float t = (float) MathTools.clip(u,0.0,1.0);

		float red = 0.0f;
		float green = 0.0f;
		float blue = 0.0f;

		if (fColorMap == EColorMap.kGrayScale) {			
			red = t;
			green = t;
			blue = t;
		}
		else if (fColorMap == EColorMap.kJet) {
			if (t <= kLowerTreshold) {
				// interpolate from blue to green
				t = t / kLowerTreshold;
				red = 0.0f;
				green = t;
				blue = 1.0f - t;
			}
			else if (t <= kHigherTreshold) {
				// interpolate from green to yellow
				t = (t - kLowerTreshold) / kDifference;
				red = t;
				green = 1.0f;
				blue = 0.0f;
			}
			else if (t <= 100) {
				// interpolate from yellow to red
				t = (t - kHigherTreshold) / (1.0f - kHigherTreshold);
				red = 1.0f;
				green = 1.0f - t;
				blue = 0.0f;
			}
		}
		else if (fColorMap == EColorMap.kCopper) {
			red = (float) MathTools.clip(t * 1.5f,0.0,1.0);
			green = t;
			blue = t / 10.0f;
		}
		else if (fColorMap == EColorMap.kBone) {
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
		else if (fColorMap == EColorMap.kGreenRedDiverging) {
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
		else if (fColorMap == EColorMap.kHot) {
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
		else if (fColorMap == EColorMap.kDiscontinuousBlueWhiteGreen) {
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
		else if (fColorMap == EColorMap.kDiscontinuousDarkRedYellow) {
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
		else if (fColorMap == EColorMap.kBlackAndWhite) {
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
		else if (fColorMap == EColorMap.kHueSaturationBrightness) {
			return Color.getHSBColor(t,1.0f,1.0f);
		}
		else if (fColorMap == EColorMap.kSeparatedRGB) {
			if (t < 0.125f) {
				// white to black
				red = 1.0f - (8.0f * t);
				green = 1.0f - (8.0f * t);
				blue = 1.0f - (8.0f * t);
			}
			else if (t < 0.250f) {
				// black to red
				red = 8.0f * (t - 0.125f);
				green = 0.0f;
				blue = 0.0f;
			}
			else if (t < 0.375f) {
				// red to black
				red = 1.0f - (8.0f * (t - 0.250f));
				green = 0.0f;
				blue = 0.0f;
			}
			else if (t < 0.500f) {
				// black to green
				red = 0.0f;
				green = 8.0f * (t - 0.375f);
				blue = 0.0f;
			}
			else if (t < 0.625f) {
				// green to black
				red = 0.0f;
				green = 1.0f - (8.0f * (t - 0.500f));
				blue = 0.0f;
			}
			else if (t < 0.750f) {
				// black to blue
				red = 0.0f;
				green = 0.0f;
				blue = 8.0f * (t - 0.625f);
			}
			else if (t < 0.875f) {
				// blue to black
				red = 0.0f;
				green = 0.0f;
				blue = 1.0f - (8.0f * (t - 0.750f));
			}
			else if (t <= 1.0f) {
				// black to white
				red = 8.0f * (t - 0.875f);
				green = 8.0f * (t - 0.875f);
				blue = 8.0f * (t - 0.875f);
			}
		}
		else if (fColorMap == EColorMap.kRed) {
			red = t;
		}
		else if (fColorMap == EColorMap.kGreen) {
			green = t;
		}
		else if (fColorMap == EColorMap.kBlue) {
			blue = t;
		}
		else if (fColorMap == EColorMap.kYellow) {
			red = t;
			green = t;
		}
		else if (fColorMap == EColorMap.kCyan) {
			green = t;
			blue = t;
		}
		else if (fColorMap == EColorMap.kMagenta) {
			red = t;
			blue = t;
		}
		else if ((fColorMap == EColorMap.kCustom) ||
						(fColorMap == EColorMap.kUltraLightPastel) ||
						(fColorMap == EColorMap.kLightPastel) ||
						(fColorMap == EColorMap.kDarkPastel) ||
						(fColorMap == EColorMap.kGreens) ||
						(fColorMap == EColorMap.kBlues) ||
						(fColorMap == EColorMap.kYellowBrowns) ||
						(fColorMap == EColorMap.kVioletPurples) ||
						(fColorMap == EColorMap.kDeepSpace)) {
			// find the values surrounding the requested value
			Double lowerValue = fCustomColorMap.floorKey((double) t);
			if (lowerValue == null) {
				return Color.BLACK;
			}
			Double higherValue = fCustomColorMap.ceilingKey((double) t);
			if (higherValue == null) {
				return Color.BLACK;
			}

			// find the interpolation value between both values
			double range = higherValue - lowerValue;
			double offset = t - lowerValue;
			double fraction = 0.0f;
			if (range > 0.0) {
				fraction = (float) (offset / range);
			}

			// find the corresponding colors
			Color lowerColor = fCustomColorMap.get(lowerValue);
			Color higherColor = fCustomColorMap.get(higherValue);

			// interpolate color components
			red = (float) MathTools.clip((lowerColor.getRed() / 255.0) + fraction * ((higherColor.getRed() / 255.0) - (lowerColor.getRed() / 255.0)),0.0,1.0);
			green = (float) MathTools.clip((lowerColor.getGreen() / 255.0) + fraction * ((higherColor.getGreen() / 255.0) - (lowerColor.getGreen() / 255.0)),0.0,1.0);
			blue = (float) MathTools.clip((lowerColor.getBlue() / 255.0) + fraction * ((higherColor.getBlue() / 255.0) - (lowerColor.getBlue() / 255.0)),0.0,1.0);
		}

		Color color = new Color(red,green,blue);

		return color;
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 */
	private void setupCustomColorMaps()
	{
		fUltraLightPastelColorMap = new TreeMap<Double,Color>();
		fUltraLightPastelColorMap.put(0.0,new Color(230,230,230));
		fUltraLightPastelColorMap.put(0.05,new Color(248,248,255));
		fUltraLightPastelColorMap.put(0.1,new Color(240,248,255));
		fUltraLightPastelColorMap.put(0.15,new Color(245,255,250));
		fUltraLightPastelColorMap.put(0.2,new Color(240,255,240));
		fUltraLightPastelColorMap.put(0.25,new Color(250,250,210));
		fUltraLightPastelColorMap.put(0.3,new Color(255,250,205));
		fUltraLightPastelColorMap.put(0.35,new Color(255,248,220));
		fUltraLightPastelColorMap.put(0.4,new Color(255,255,224));
		fUltraLightPastelColorMap.put(0.45,new Color(255,255,240));
		fUltraLightPastelColorMap.put(0.5,new Color(255,250,240));
		fUltraLightPastelColorMap.put(0.55,new Color(250,240,230));
		fUltraLightPastelColorMap.put(0.6,new Color(253,245,230));
		fUltraLightPastelColorMap.put(0.65,new Color(250,235,215));
		fUltraLightPastelColorMap.put(0.7,new Color(255,228,196));
		fUltraLightPastelColorMap.put(0.75,new Color(255,218,185));
		fUltraLightPastelColorMap.put(0.8,new Color(255,239,213));
		fUltraLightPastelColorMap.put(0.85,new Color(255,245,238));
		fUltraLightPastelColorMap.put(0.9,new Color(255,240,245));
		fUltraLightPastelColorMap.put(0.95,new Color(255,228,225));
		fUltraLightPastelColorMap.put(1.0,new Color(255,250,250));

		fLightPastelColorMap = new TreeMap<Double,Color>();
		fLightPastelColorMap.put(0.0,new Color(255,190,190));
		fLightPastelColorMap.put(0.2,new Color(255,255,190));
		fLightPastelColorMap.put(0.4,new Color(190,255,190));
		fLightPastelColorMap.put(0.6,new Color(190,255,255));
		fLightPastelColorMap.put(0.8,new Color(190,190,255));
		fLightPastelColorMap.put(1.0,new Color(255,190,255));

		fDarkPastelColorMap = new TreeMap<Double,Color>();
		fDarkPastelColorMap.put(0.0,new Color(130,182,222));
		fDarkPastelColorMap.put(0.11111111111,new Color(147,148,204));
		fDarkPastelColorMap.put(0.22222222222,new Color(160,205,179));
		fDarkPastelColorMap.put(0.33333333333,new Color(173,177,178));
		fDarkPastelColorMap.put(0.44444444444,new Color(209,179,145));
		fDarkPastelColorMap.put(0.55555555556,new Color(254,213,2));
		fDarkPastelColorMap.put(0.66666666667,new Color(230,105,103));
		fDarkPastelColorMap.put(0.77777777778,new Color(242,126,68));
		fDarkPastelColorMap.put(0.88888888889,new Color(175,113,176));
		fDarkPastelColorMap.put(1.0,new Color(13,107,182));

		fGreensColorMap = new TreeMap<Double,Color>();
		fGreensColorMap.put(0.0,new Color(127,155,212));
		fGreensColorMap.put(0.0526315789473684,new Color(102,205,170));
		fGreensColorMap.put(0.105263157894737,new Color(143,188,143));
		fGreensColorMap.put(0.157894736842105,new Color(60,179,113));
		fGreensColorMap.put(0.210526315789474,new Color(46,139,87));
		fGreensColorMap.put(0.263157894736842,new Color(0,100,0));
		fGreensColorMap.put(0.315789473684211,new Color(0,128,0));
		fGreensColorMap.put(0.368421052631579,new Color(34,139,34));
		fGreensColorMap.put(0.421052631578947,new Color(50,205,50));
		fGreensColorMap.put(0.473684210526316,new Color(0,255,0));
		fGreensColorMap.put(0.526315789473684,new Color(127,255,0));
		fGreensColorMap.put(0.578947368421053,new Color(102,255,0));
		fGreensColorMap.put(0.631578947368421,new Color(173,255,47));
		fGreensColorMap.put(0.684210526315789,new Color(152,251,152));
		fGreensColorMap.put(0.736842105263158,new Color(144,238,144));
		fGreensColorMap.put(0.789473684210526,new Color(0,255,127));
		fGreensColorMap.put(0.842105263157895,new Color(0,250,154));
		fGreensColorMap.put(0.894736842105263,new Color(85,107,47));
		fGreensColorMap.put(0.947368421052632,new Color(107,142,35));
		fGreensColorMap.put(1.0,new Color(128,128,0));

		fBluesColorMap = new TreeMap<Double,Color>();
		fBluesColorMap.put(0.0,new Color(70,130,180));
		fBluesColorMap.put(0.0625,new Color(65,105,225));
		fBluesColorMap.put(0.125,new Color(100,149,237));
		fBluesColorMap.put(0.1875,new Color(176,196,222));
		fBluesColorMap.put(0.25,new Color(123,104,238));
		fBluesColorMap.put(0.3125,new Color(106,90,205));
		fBluesColorMap.put(0.375,new Color(72,61,139));
		fBluesColorMap.put(0.4375,new Color(25,25,112));
		fBluesColorMap.put(0.5,new Color(0,0,128));
		fBluesColorMap.put(0.5625,new Color(0,0,139));
		fBluesColorMap.put(0.625,new Color(0,0,205));
		fBluesColorMap.put(0.6875,new Color(30,144,255));
		fBluesColorMap.put(0.75,new Color(0,191,255));
		fBluesColorMap.put(0.8125,new Color(135,206,250));
		fBluesColorMap.put(0.875,new Color(135,206,235));
		fBluesColorMap.put(0.9375,new Color(173,216,230));
		fBluesColorMap.put(1.0,new Color(153,204,255));

		fYellowBrownsColorMap = new TreeMap<Double,Color>();
		fYellowBrownsColorMap.put(0.0,new Color(189,183,107));
		fYellowBrownsColorMap.put(0.0476190476190476,new Color(184,134,11));
		fYellowBrownsColorMap.put(0.0952380952380952,new Color(218,165,32));
		fYellowBrownsColorMap.put(0.142857142857143,new Color(255,215,0));
		fYellowBrownsColorMap.put(0.19047619047619,new Color(240,230,140));
		fYellowBrownsColorMap.put(0.238095238095238,new Color(238,232,170));
		fYellowBrownsColorMap.put(0.285714285714286,new Color(255,235,205));
		fYellowBrownsColorMap.put(0.333333333333333,new Color(255,228,181));
		fYellowBrownsColorMap.put(0.380952380952381,new Color(245,222,179));
		fYellowBrownsColorMap.put(0.428571428571429,new Color(255,222,173));
		fYellowBrownsColorMap.put(0.476190476190476,new Color(222,184,135));
		fYellowBrownsColorMap.put(0.523809523809524,new Color(210,180,140));
		fYellowBrownsColorMap.put(0.571428571428571,new Color(188,143,143));
		fYellowBrownsColorMap.put(0.619047619047619,new Color(160,82,45));
		fYellowBrownsColorMap.put(0.666666666666667,new Color(139,69,19));
		fYellowBrownsColorMap.put(0.714285714285714,new Color(210,105,30));
		fYellowBrownsColorMap.put(0.761904761904762,new Color(205,133,63));
		fYellowBrownsColorMap.put(0.80952380952381,new Color(244,164,96));
		fYellowBrownsColorMap.put(0.857142857142857,new Color(139,0,0));
		fYellowBrownsColorMap.put(0.904761904761905,new Color(128,0,0));
		fYellowBrownsColorMap.put(0.952380952380952,new Color(165,42,42));
		fYellowBrownsColorMap.put(1.0,new Color(178,34,34));

		fVioletPurplesColorMap = new TreeMap<Double,Color>();
		fVioletPurplesColorMap.put(0.0,new Color(219,112,147));
		fVioletPurplesColorMap.put(0.0769230769230769,new Color(199,21,133));
		fVioletPurplesColorMap.put(0.153846153846154,new Color(128,0,128));
		fVioletPurplesColorMap.put(0.230769230769231,new Color(153,0,153));
		fVioletPurplesColorMap.put(0.307692307692308,new Color(147,112,219));
		fVioletPurplesColorMap.put(0.384615384615385,new Color(138,43,226));
		fVioletPurplesColorMap.put(0.461538461538462,new Color(75,0,130));
		fVioletPurplesColorMap.put(0.538461538461539,new Color(148,0,211));
		fVioletPurplesColorMap.put(0.615384615384615,new Color(153,50,204));
		fVioletPurplesColorMap.put(0.692307692307692,new Color(186,85,211));
		fVioletPurplesColorMap.put(0.769230769230769,new Color(218,112,214));
		fVioletPurplesColorMap.put(0.846153846153846,new Color(238,130,238));
		fVioletPurplesColorMap.put(0.923076923076923,new Color(221,160,221));
		fVioletPurplesColorMap.put(1.0,new Color(216,191,216));

		fDeepSpaceColorMap =  new TreeMap<Double,Color>();
		fDeepSpaceColorMap.put(0.0,new Color(0,0,100));
		fDeepSpaceColorMap.put(0.1,new Color(10,35,194));
		fDeepSpaceColorMap.put(0.2,new Color(52,103,233));
		fDeepSpaceColorMap.put(0.3,new Color(150,195,250));
		fDeepSpaceColorMap.put(0.4,new Color(232,254,255));
		fDeepSpaceColorMap.put(0.5,new Color(250,244,177));
		fDeepSpaceColorMap.put(0.6,new Color(255,201,27));
		fDeepSpaceColorMap.put(0.7,new Color(211,118,0));
		fDeepSpaceColorMap.put(0.8,new Color(48,25,0));
		fDeepSpaceColorMap.put(0.9,new Color(0,2,9));
		fDeepSpaceColorMap.put(1.0,new Color(0,0,100));
	}

	/*****************
	 * INNER CLASSES *
	 *****************/

	/**
	 * A container class for a custom colour map component.
	 *
	 * @author  Sven Maerivoet
	 * @version 02/01/2013
	 */
	public final class CustomColorMapComponent
	{
		/**
		 * The level associated with this color map component.
		 */
		public double fLevel;

		/**
		 * The colour associated with this color map component.
		 */
		public Color fColor;

		/**
		 * @param level  -
		 * @param color  -
		 */
		public CustomColorMapComponent(double level, Color color)
		{
			fLevel = level;
			fColor = color;
		}
	}
}

/*
	CODE IN JStandardGUIApplication::setupContentPane()
	===================================================

	import org.sm.smtools.swing.util.*;

		JGradientColorMap gcr = new JGradientColorMap(JGradientColorMap.EColorMap.kSeparatedRGB);
		gcr.setPreferredSize(new Dimension(1200,600));
		contentPane.add(gcr);
*/