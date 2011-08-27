// -------------------------------
// Filename      : MathTools.java
// Author        : Sven Maerivoet
// Last modified : 26/08/2011
// Target        : Java VM (1.6)
// -------------------------------

/**
 * Copyright 2003-2011 Sven Maerivoet
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

package smtools.math;

/**
 * The <CODE>MathTools</CODE> class offers some basic useful mathematical operations.
 * <P>
 * All methods in this class are static, so they should be invoked as:
 * <P>
 * <UL>
 *   <CODE>... = MathTools.method(...);</CODE>
 * </UL>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 26/08/2011
 */
public final class MathTools
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	// prevent instantiation
	private MathTools()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Calculates the square of a <CODE>double</CODE>.
	 *
	 * @param  x the <CODE>double</CODE> to be squared
	 * @return   the squared <CODE>double</CODE>
	 * @see    MathTools#cube(double)
	 * @see    MathTools#quadr(double)
	 */
	public static double sqr(double x)
	{
		return (x * x);
	}

	/**
	 * Calculates the cube of a <CODE>double</CODE>.
	 *
	 * @param  x the <CODE>double</CODE> to be cubed
	 * @return the cubed <CODE>double</CODE>
	 * @see    MathTools#sqr(double)
	 * @see    MathTools#quadr(double)
	 */
	public static double cube(double x)
	{
		return (x * x * x);
	}

	/**
	 * Calculates the quadratic of a <CODE>double</CODE>.
	 *
	 * @param  x the <CODE>double</CODE> to be quadrated
	 * @return   the quadrated <CODE>double</CODE>
	 * @see    MathTools#sqr(double)
	 * @see    MathTools#cube(double)
	 */
	public static double quadr(double x)
	{
		return (x * x * x * x);
	}

	/**
	 * Calculates the cubic root of a <CODE>double</CODE>.
	 *
	 * @param  x the <CODE>double</CODE> to calculate the cubic root for
	 * @return the <CODE>double</CODE>'s cubic root
	 */
	public static double cubicRoot(double x)
	{
		return Math.exp((Math.log(x) / 3.0));
	}

	/**
	 * Calculates the arc tangent of the two <CODE>doubles</CODE>.
	 * <P>
	 * Both <CODE>doubles x</CODE> and <CODE>y</CODE> are used as y / x
	 * (<CODE>x</CODE> is allowed to be zero). The arc tangent will be a <B>positive angle</B>,
	 * lying in [0,2*PI]. The following cases are considered:
	 * <P>
	 * <UL>
	 *   <LI><CODE>x</CODE> >= 0, <CODE>y</CODE> >= 0: 0 <= <CODE>atan(x,y)</CODE> <= PI/2</LI>
	 *   <LI><CODE>x</CODE> <= 0, <CODE>y</CODE> >= 0: PI/2 <= <CODE>atan(x,y)</CODE> <= PI</LI>
	 *   <LI><CODE>x</CODE> <= 0, <CODE>y</CODE> <= 0: PI <= <CODE>atan(x,y)</CODE> <= 3*PI/2</LI>
	 *   <LI><CODE>x</CODE> >= 0, <CODE>y</CODE> <= 0: 3*PI/2 <= <CODE>atan(x,y)</CODE> <= 2*PI</LI>
	 * </UL>
	 * <P>
	 * With these two special cases:
	 * <P>
	 * <UL>
	 *   <LI><CODE>x</CODE> = 0, <CODE>y</CODE> > 0: <CODE>atan(x,y)</CODE> = PI/2</LI>
	 *   <LI><CODE>x</CODE> = 0, <CODE>y</CODE> < 0: <CODE>atan(x,y)</CODE> = 3*PI/2</LI>
	 * </UL>
	 *
	 * @param  x the denominator of the <CODE>double</CODE> to calculate the arc tangent of
	 * @param  y the numerator of the <CODE>double</CODE> to calculate the arc tangent of
	 * @return   the arc tangent of the <CODE>doubles</CODE>
	 */
	public static double atan(double x, double y)
	{
		double angle = 0.0;

		if (x == 0.0) {
			if (y > 0.0) {
				angle = Math.PI / 2.0;
			}
			else if (y < 0.0) {
				angle = 3.0 * (Math.PI / 2.0);
			}
		}
		else {
			double absAngle = Math.atan(Math.abs(y) / Math.abs(x));

			if ((x >= 0.0) && (y >= 0.0)) {
				angle = absAngle;
			}
			else if ((x <= 0.0) && (y >= 0.0)) {
				angle = Math.PI - absAngle;
			}
			else if ((x <= 0.0) && (y <= 0.0)) {
				angle = Math.PI + absAngle;
			}
			else {
				angle = (2.0 * Math.PI) - absAngle;
			}
		}

		return ((2.0 * Math.PI) - angle);
	}

	/**
	 * Converts degrees to radians.
	 *
	 * @param  degrees the number of degrees to convert
	 * @return the number of degrees converted to radians
	 * @see    MathTools#rad2deg(double)
	 */
	public static double deg2rad(double degrees)
	{
		return (degrees * Math.PI / 180.0);
	}

	/**
	 * Converts radians to degrees.
	 *
	 * @param  radians the radians to convert
	 * @return the radians converted to number of degrees
	 * @see    MathTools#deg2rad(double)
	 */
	public static double rad2deg(double radians)
	{
		return (radians / Math.PI * 180.0);
	}

	/**
	 * Clips an <CODE>int</CODE> between two extrema.
	 *
	 * @param  value   the <CODE>int</CODE> to clip between the two extrema
	 * @param  minimum the lower boundary to clip the <CODE>int</CODE>
	 * @param  maximum the upper boundary to clip the <CODE>int</CODE>
	 * @return         the <CODE>int</CODE> clipped between the two extrema
	 * @see MathTools#clip(double,double,double)
	 */
	public static int clip(int value, int minimum, int maximum)
	{
		return ((int) clip((double) value,(double) minimum,(double) maximum));
	}

	/**
	 * Clips a <CODE>double</CODE> between two extrema.
	 *
	 * @param  value   the <CODE>double</CODE> to clip between the two extrema
	 * @param  minimum the lower boundary to clip the <CODE>double</CODE>
	 * @param  maximum the upper boundary to clip the <CODE>double</CODE>
	 * @return         the <CODE>double</CODE> clipped between the two extrema
	 * @see MathTools#clip(int,int,int)
	 */
	public static double clip(double value, double minimum, double maximum)
	{
		if (value < minimum) {
			return minimum;
		}
		else if (value > maximum) {
			return maximum;
		}
		else {
			return value;
		}
	}

	/**
	 * Searches incrementally for the minimum value in an array.
	 * 
	 * @param x the array to search in
	 */
	public static double findMinimum(double[] x)
	{
		double minimum = x[0];
		for (int i = 1; i < x.length; ++i) {
			if (x[i] < minimum) {
				minimum = x[i];
			}
		}
		return minimum;
	}

	/**
	 * Searches incrementally for the maximum value in an array.
	 * 
	 * @param x the array to search in
	 */
	public static double findMaximum(double[] x)
	{
		double maximum = x[0];
		for (int i = 1; i < x.length; ++i) {
			if (x[i] > maximum) {
				maximum = x[i];
			}
		}
		return maximum;
	}

	/**
	 * Performs linear interpolation of a <CODE>double</CODE> with respect to two boundary values.
	 * <P>
	 * This method assumes that the specified <CODE>value</CODE> lies in the interval [0,1], with 0.0 corresponding to the
	 * lower boundary (<CODE>from</CODE>) and 1.0 to the upper boundary (<CODE>to</CODE>).
	 *
	 * @param  value the <CODE>double</CODE> to interpolate linearly with respect to two boundary values
	 * @param  from  the lower boundary to use for the linear interpolation (corresponding to <CODE>value</CODE> = 0.0)
	 * @param  to    the upper boundary to use for the linear interpolation (corresponding to <CODE>value</CODE> = 1.0)
	 * @return       the linear interpolation of the specified <CODE>double</CODE> between the two boundary values
	 */
	public static double normalisedLinearInterpolation(double value, double from, double to)
	{
		return ((from * (1.0 - value)) + (to * value));
	}

	/**
	 * Determines the indices of the 2 values surrounding the searched value in an array of <CODE>x.length</CODE> elements, such that:
	 * <P>
	 * <UL>
	 *   <CODE>x[fLowerBound] <= xSearch < x[fUpperBound]</CODE>
	 * </UL>
	 * <P>
	 * There are 2 special cases:
	 * <UL>
	 *   <LI><CODE>xSearch < min(x)</CODE> leads to <CODE>fLowerBound = fUpperBound = 0</CODE></LI>
	 *   <LI><CODE>xSearch >= max(x)</CODE> leads to <CODE>fLowerBound = fUpperBound = x.length - 1</CODE></LI>
	 * </UL>
	 * <P>
	 * <B>Note that this method assumes that the elements in <CODE>x</CODE> are sorted!</B>
	 *
	 * @param  x an array of <CODE>double</CODE>s containing the values
	 * @param  xSearch the value to search for
	 * @return an <CODE>ArraySearchBounds</CODE> object containing the indices of the 2 values surrounding the searched value
	 */
	public static ArraySearchBounds searchArrayBounds(double[] x, double xSearch)
	{
		final int n = x.length;

		int lowerBound = 0;
		int upperBound = 0;

		if (xSearch >= x[n - 1]) {
			lowerBound = n - 1;
			upperBound = n - 1;
		}			
		else if (xSearch >= x[0]) {
			// find lower bound
			if (xSearch < x[n - 1]) {
				boolean found = false;
				while (!found) {
					if (x[lowerBound] > xSearch) {
						--lowerBound;
						found = true;
					}
					else {
						++lowerBound;
					}
				}
			}
			else {
				lowerBound = n - 1;
			}

			// set upper bound
			upperBound = lowerBound + 1;
		}

		return (new ArraySearchBounds(lowerBound,upperBound));
	}
}
