// -------------------------------
// Filename      : MathTools.java
// Author        : Sven Maerivoet
// Last modified : 19/11/2014
// Target        : Java VM (1.8)
// -------------------------------

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

package org.sm.smtools.math;

import java.awt.geom.*;
import java.math.*;

/**
 * The <CODE>MathTools</CODE> class offers some basic useful mathematical operations.
 * <P>
 * All methods in this class are static, so they should be invoked as:
 * <P>
 * <CODE>... = MathTools.method(...);</CODE>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 19/11/2014
 */
public final class MathTools
{
	/**
	 * The different kernel types.
	 */
	public static enum EKernelType {kRectangular, kTriangular, kEpanechnikov, kQuartic, kGaussian, kLanczos};

	/**
	 * The number of decimals in a double, equal to log10(2^(#bits_mantissa)) with #bits_mantissa = #bits_total - #bits_exponent - #bits_sign.
	 */
	public static final int kNrOfDoubleDecimals = (int) Math.round(Math.log10(Math.pow(2.0,64.0 - 11.0 - 1.0)));

	// the a parameter of the Lanczos kernel
	private static double kLanczosA = 2.0;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation.
	 */
	private MathTools()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Calculates the fractional part of a <CODE>double</CODE>.
	 *
	 * @param x  the <CODE>double</CODE> to calculate the fractional part of
	 * @return   the fractional part of the <CODE>double</CODE>
	 */
	public static double frac(double x)
	{
		return (x - Math.floor(x));
	}

	/**
	 * Calculates the square of a <CODE>double</CODE>.
	 *
	 * @param x  the <CODE>double</CODE> to be squared
	 * @return   the squared <CODE>double</CODE>
	 * @see      MathTools#cube(double)
	 * @see      MathTools#quadr(double)
	 */
	public static double sqr(double x)
	{
		return (x * x);
	}

	/**
	 * Calculates the cube of a <CODE>double</CODE>.
	 *
	 * @param x  the <CODE>double</CODE> to be cubed
	 * @return   the cubed <CODE>double</CODE>
	 * @see      MathTools#sqr(double)
	 * @see      MathTools#quadr(double)
	 */
	public static double cube(double x)
	{
		return (x * x * x);
	}

	/**
	 * Calculates the quadratic of a <CODE>double</CODE>.
	 *
	 * @param x  the <CODE>double</CODE> to be quadrated
	 * @return   the quadrated <CODE>double</CODE>
	 * @see      MathTools#sqr(double)
	 * @see      MathTools#cube(double)
	 */
	public static double quadr(double x)
	{
		return (x * x * x * x);
	}

	/**
	 * Calculates the factorial of a <CODE>double</CODE>.
	 *
	 * @param n  the <CODE>double</CODE> to calculate the factorial for
	 * @return   the factorial of the specified <CODE>double</CODE>
	 * @see      MathTools#facApprox(double)
	 */
	public static double fac(double n)
	{
		if (n == 0) {
			return 1;
		}
		else {
			return (n * fac(n - 1));
		}
	}
	
	/**
	 * Calculates the approximated factorial of a <CODE>double</CODE> (using Stirling's formula).
	 *
	 * @param n  the <CODE>double</CODE> to calculate the approximated factorial for
	 * @return   the approximated factorial of the specified <CODE>double</CODE>
	 * @see      MathTools#fac(double)
	 */
	public static double facApprox(double n)
	{
		// Stirling's approximation
		return (Math.sqrt(2.0 * Math.PI * n) * Math.pow((n / Math.E),n));
	}

	/**
	 * Calculates the arc tangent of the two <CODE>doubles</CODE>.
	 * <P>
	 * Both <CODE>doubles x</CODE> and <CODE>y</CODE> are used as y / x
	 * (<CODE>x</CODE> is allowed to be zero). The arc tangent will be a <B>positive angle</B>,
	 * lying in [0,2*PI]. The following cases are considered:
	 * <UL>
	 *   <LI><CODE>x</CODE> &ge; 0, <CODE>y</CODE> &ge; 0: 0 &le; <CODE>atan(x,y)</CODE> &le; PI/2</LI>
	 *   <LI><CODE>x</CODE> &le; 0, <CODE>y</CODE> &ge; 0: PI/2 &le; <CODE>atan(x,y)</CODE> &le; PI</LI>
	 *   <LI><CODE>x</CODE> &le; 0, <CODE>y</CODE> &le; 0: PI &le; <CODE>atan(x,y)</CODE> &le; 3*PI/2</LI>
	 *   <LI><CODE>x</CODE> &ge; 0, <CODE>y</CODE> &le; 0: 3*PI/2 &le; <CODE>atan(x,y)</CODE> &le; 2*PI</LI>
	 * </UL>
	 * <P>
	 * With these two special cases:
	 * <UL>
	 *   <LI><CODE>x</CODE> = 0, <CODE>y</CODE> &gt; 0: <CODE>atan(x,y)</CODE> = PI/2</LI>
	 *   <LI><CODE>x</CODE> = 0, <CODE>y</CODE> &lt; 0: <CODE>atan(x,y)</CODE> = 3*PI/2</LI>
	 * </UL>
	 *
	 * @param x  the denominator of the <CODE>double</CODE> to calculate the arc tangent of
	 * @param y  the numerator of the <CODE>double</CODE> to calculate the arc tangent of
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
				angle = 3.0 * (Math.PI / 2.0); // instead of -Math.PI / 2.0 
			}
		}
		else {
			angle = Math.atan2(y,x);
			if (y < 0.0) {
				angle = (2.0 * Math.PI) + angle;
			}
		}

		return angle;
	}

	/**
	 * Calculates the unnormalised sinc (sinus cardinalis) function of a <CODE>double</CODE>.
	 *
	 * @param x  the <CODE>double</CODE> to calculate the unnormalised sinc of
	 * @return   the unnormalised sinc of the <CODE>double</CODE>
	 */
	public static double sinc(double x)
	{
		if (x == 0.0) {
			return 1.0;
		}
		else {
			return (Math.sin(x) / x);
		}
	}

	/**
	 * Calculates the normalised sinc (sinus cardinalis) function of a <CODE>double</CODE>.
	 *
	 * @param x  the <CODE>double</CODE> to calculate the normalised sinc of
	 * @return   the normalised sinc of the <CODE>double</CODE>
	 */
	public static double sincn(double x)
	{
		if (x == 0.0) {
			return 1.0;
		}
		else {
			return (Math.sin(Math.PI * x) / (Math.PI * x));
		}
	}

	/**
	 * Converts degrees to radians.
	 *
	 * @param  degrees  the number of degrees to convert
	 * @return          the number of degrees converted to radians
	 * @see             MathTools#rad2deg(double)
	 */
	public static double deg2rad(double degrees)
	{
		return (degrees * Math.PI / 180.0);
	}

	/**
	 * Converts radians to degrees.
	 *
	 * @param radians  the radians to convert
	 * @return         the radians converted to number of degrees
	 * @see            MathTools#deg2rad(double)
	 */
	public static double rad2deg(double radians)
	{
		return (radians / Math.PI * 180.0);
	}

	/**
	 * Clips an <CODE>int</CODE> between two extrema.
	 *
	 * @param  value    the <CODE>int</CODE> to clip between the two extrema
	 * @param  minimum  the lower boundary to clip the <CODE>int</CODE>
	 * @param  maximum  the upper boundary to clip the <CODE>int</CODE>
	 * @return          the <CODE>int</CODE> clipped between the two extrema
	 * @see             MathTools#clip(double,double,double)
	 */
	public static int clip(int value, int minimum, int maximum)
	{
		return ((int) clip((double) value,(double) minimum,(double) maximum));
	}

	/**
	 * Clips a <CODE>double</CODE> between two extrema.
	 *
	 * @param  value    the <CODE>double</CODE> to clip between the two extrema
	 * @param  minimum  the lower boundary to clip the <CODE>double</CODE>
	 * @param  maximum  the upper boundary to clip the <CODE>double</CODE>
	 * @return          the <CODE>double</CODE> clipped between the two extrema
	 * @see             MathTools#clip(int,int,int)
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
	 * Forces a partial order on the components of two points p1 and p2 such that (x1,y1) &le; (x2,y2).
	 *
	 * @param p1  the first point
	 * @param p2  the second point
	 */	
	public static void forcePartialOrder(Point2D.Double p1, Point2D.Double p2)
	{
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();

		if (x1 > x2) {
			double temp = x1;
			x1 = x2;
			x2 = temp;
		}

		if (y1 > y2) {
			double temp = y1;
			y1 = y2;
			y2 = temp;
		}

		p1.setLocation(x1,y1);
		p2.setLocation(x2,y2);
	}

	/**
	 * Searches incrementally for the minimum value in an array.
	 * 
	 * @param x  the array to search in
	 * @return   the minimum value in the array
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
	 * @param x  the array to search in
	 * @return   the maximum value in the array
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
	 * @param  value  the <CODE>double</CODE> to interpolate linearly with respect to two boundary values
	 * @param  from   the lower boundary to use for the linear interpolation (corresponding to <CODE>value</CODE> = 0.0)
	 * @param  to     the upper boundary to use for the linear interpolation (corresponding to <CODE>value</CODE> = 1.0)
	 * @return        the linear interpolation of the specified <CODE>double</CODE> between the two boundary values
	 */
	public static double normalisedLinearInterpolation(double value, double from, double to)
	{
		return ((from * (1.0 - value)) + (to * value));
	}

	/**
	 * Determines the indices of the 2 values surrounding the searched value in an array of <CODE>x.length</CODE> elements, such that:
	 * <CODE>x[fLowerBound] &le; xSearch &lt; x[fUpperBound]</CODE>
	 * <P>
	 * There are 2 special cases:
	 * <UL>
	 *   <LI><CODE>xSearch &lt; min(x)</CODE> leads to <CODE>fLowerBound = fUpperBound = 0</CODE></LI>
	 *   <LI><CODE>xSearch &ge; max(x)</CODE> leads to <CODE>fLowerBound = fUpperBound = x.length - 1</CODE></LI>
	 * </UL>
	 * <P>
	 * <B>Note that this method assumes that the elements in <CODE>x</CODE> are sorted!</B>
	 *
	 * @param x        an array of <CODE>double</CODE>s containing the values
	 * @param xSearch  the value to search for
	 * @return         an <CODE>ArraySearchBounds</CODE> object containing the indices of the 2 values surrounding the searched value
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

	/**
	 * Checks whether or not a number is even.
	 *
	 * @param n  the number to check
	 * @return   <CODE>true</CODE> when <I>n</I> is even, <CODE>false</CODE> if it's odd
	 */
	public static boolean isEven(int n)
	{
		return ((n & 1) == 0);
	}

	/**
	 * Checks whether or not a number is odd.
	 *
	 * @param n  the number to check
	 * @return   <CODE>true</CODE> when <I>n</I> is odd, <CODE>false</CODE> if it's even
	 */
	public static boolean isOdd(int n)
	{
		return !isEven(n);
	}

	/**
	 * Converts a number of bytes to kilobytes (1 kB = 1000 B).
	 *
	 * @param nrOfBytes  the number of bytes to convert
	 * @return           the number of kilobytes corresponding to the number of bytes
	 */
	public static double convertBTokB(long nrOfBytes)
	{
		return ((double) nrOfBytes / 1000.0);
	}

	/**
	 * Converts a number of bytes to kibibytes (1 KiB = 1024 B).
	 *
	 * @param nrOfBytes  the number of bytes to convert
	 * @return           the number of kibibytes corresponding to the number of bytes
	 */
	public static double convertBToKiB(long nrOfBytes)
	{
		return ((double) nrOfBytes / 1024.0);
	}

	/**
	 * Converts a number of bytes to megabytes (1 MB = 1000^2 B).
	 *
	 * @param nrOfBytes  the number of bytes to convert
	 * @return           the number of megabytes corresponding to the number of bytes
	 */
	public static double convertBToMB(long nrOfBytes)
	{
		return ((double) nrOfBytes / (1000.0 * 1000.0));
	}

	/**
	 * Converts a number of bytes to mebibytes (1 MiB = 1024^2 B).
	 *
	 * @param nrOfBytes  the number of bytes to convert
	 * @return           the number of mebibytes corresponding to the number of bytes
	 */
	public static double convertBToMiB(long nrOfBytes)
	{
		return ((double) nrOfBytes / (1024.0 * 1024.0));
	}

	/**
	 * Converts a number of bytes to gigabytes (1 GB = 1000^3 B).
	 *
	 * @param nrOfBytes  the number of bytes to convert
	 * @return           the number of gigabytes corresponding to the number of bytes
	 */
	public static double convertBToGB(long nrOfBytes)
	{
		return ((double) nrOfBytes / (1000.0 * 1000.0 * 1000.0));
	}

	/**
	 * Converts a number of bytes to gibibytes (1 GiB = 1024^3 B).
	 *
	 * @param nrOfBytes  the number of bytes to convert
	 * @return           the number of gibibytes corresponding to the number of bytes
	 */
	public static double convertBToGiB(long nrOfBytes)
	{
		return ((double) nrOfBytes / (1024.0 * 1024.0 * 1024.0));
	}

	/**
	 * Converts a number of bytes to terabytes (1 GB = 1000^4 B).
	 *
	 * @param nrOfBytes  the number of bytes to convert
	 * @return           the number of terabytes corresponding to the number of bytes
	 */
	public static double convertBToTB(long nrOfBytes)
	{
		return ((double) nrOfBytes / (1000.0 * 1000.0 * 1000.0 * 1000.0));
	}

	/**
	 * Converts a number of bytes to tebibytes (1 TiB = 1024^4 B).
	 *
	 * @param nrOfBytes  the number of bytes to convert
	 * @return           the number of tebibytes corresponding to the number of bytes
	 */
	public static double convertBToTiB(long nrOfBytes)
	{
		return ((double) nrOfBytes / (1024.0 * 1024.0 * 1024.0 * 1024.0));
	}

	/**
	 * Rounds a <CODE>double</CODE> to a <CODE>long</CODE>.
	 *
	 * @param x  the <CODE>double</CODE> to round
	 * @return   a <CODE>long</CODE> representing the rounded <CODE>double</CODE>
	 */
	public static long round(double x)
	{
		return (long) Math.round(x);
	}

	/**
	 * Takes a logarithm of a <CODE>double</CODE> in a custom base.
	 *
	 * @param x     the <CODE>double</CODE> to take the logarithm of
	 * @param base  the base of the logarithm
	 * @return      a <CODE>double</CODE> representing the calculated logarithm in the specified base
	 */
	public static double logBase(double x, double base)
	{
		return (Math.log(x) / Math.log(base));
	}

	/**
	 * Returns whether or not a given <CODE>long</CODE> number is prime.
	 *
	 * @param x  the <CODE>long</CODE> to test for primality
	 * @return   <CODE>true</CODE> if the given <CODE>long</CODE> is prime, <CODE>false</CODE> otherwise 
	 */
	public static boolean isPrime(long x)
	{
		// see also http://stackoverflow.com/questions/2385909/what-would-be-the-fastest-method-to-test-for-primality-in-java
		return ((new BigInteger(String.valueOf(x))).isProbablePrime(15));
	}

	/**
	 * Finds all local extreme values (and their indices) in an array.
	 *
	 * @param x  the sequence to find all local extreme values for
	 * @return   all encountered local extreme values
	 */
	public static Extrema findExtrema(double[] x)
	{
		Extrema extrema = new Extrema();

		if (x.length >= 3) {
			// determine initial direction
			double left = x[0];
			double middle = x[1];
			double right = 0;
			int direction = 0;
			if (middle > left) {
				direction = +1;
			}
			else if (middle < left) {
				direction = -1;
			}

			// process all elements
			for (int i = 1; i < (x.length - 1); ++i) {
				left = x[i - 1];
				middle = x[i];
				right = x[i + 1];
	
				if (right == middle) {
					// direction remains unchanged
				}
				else if (right > middle) {
					if (middle < left) {
						extrema.addLocalMinimum(i,middle);
						direction = +1;
					}
					else if (middle > left) {
						direction = +1;
					}
					else if ((middle == left) && (direction == -1)) {
						extrema.addLocalMinimum(i,middle);
						direction = +1;
					}
					else if ((middle == left) && ((direction == +1) || (direction == 0))) {
						direction = +1;
					}
				}
				else if (right < middle) {
					if (middle > left) {
						extrema.addLocalMaximum(i,middle);
						direction = -1;
					}
					else if (middle < left) {
						direction = -1;
					}
					else if ((middle == left) && (direction == +1)) {
						extrema.addLocalMaximum(i,middle);
						direction = -1;
					}
					else if ((middle == left) && ((direction == -1) || (direction == 0))) {
						direction = -1;
					}
				}
			} // for i
		} // if N >= 3

		return extrema;
	}

	/**
	 * Provides a kernel.
	 *
	 * @param u           the point where the kernel is to be evaluated
	 * @param kernelType  the type of kernel to use in the evaluation
	 * @return            the kernel evaluated in <I>u</I>
	 */
	public static double getKernel(double u, EKernelType kernelType)
	{
		double indicatorFunction = 1.0;
		if (Math.abs(u) > 1.0) {
			indicatorFunction = 0.0;
		}

		double result = 0.0;
		switch (kernelType) {
			case kRectangular:
				result = (1.0 / 2.0) * indicatorFunction;
				break;
			case kTriangular:
				result = (1.0 - Math.abs(u)) * indicatorFunction;
				break;
			case kEpanechnikov:
				result = ((3.0 / 4.0) * (1.0 - MathTools.sqr(u))) * indicatorFunction;
				break;
			case kQuartic:
				result = ((15.0 / 16.0) * MathTools.sqr(1.0 - MathTools.sqr(u))) * indicatorFunction;
				break;
			case kGaussian:
				result = (1.0 / Math.sqrt(2.0 * Math.PI)) * Math.exp((-1.0 / 2.0) * MathTools.sqr(u));
				break;
			case kLanczos:
				result = (sinc(u) * sinc(u / kLanczosA)) * indicatorFunction;
				break;
		}
		return result;
	}

	/**
	 * Performs kernel smoothing on a 1D function specified by lookup tables for in the (X,Y) plane.
	 * 
	 * @param  functionLookupTable  the lookup table for the specified 1D function
	 * @param  kernelType           the type of kernel to use
	 * @param  bandwidth            the bandwidth of the kernel
	 * @param  nrOfSupportPoints    the number of (X,Y) values to use for the smoothened 1D function
	 * @return                      the smoothed data in a new lookup table
	 */
	public static FunctionLookupTable getKernelSmoother(FunctionLookupTable functionLookupTable, EKernelType kernelType, double bandwidth, int nrOfSupportPoints)
	{
		double[] x = functionLookupTable.fX;
		double[] y = functionLookupTable.fY;
		if (x.length != y.length) {
			return null;
		}

		// prepare new support
		double minX = MathTools.findMinimum(x);
		double maxX = MathTools.findMaximum(x);
		double xRange = maxX - minX;
		double delta = xRange / (nrOfSupportPoints - 1);
		double[] xSupport = new double[nrOfSupportPoints];
		for (int i = 0; i < nrOfSupportPoints; ++i) {
			xSupport[i] = minX + (i * delta);
		}

		double[] xk = new double[nrOfSupportPoints];
		double[] yk = new double[nrOfSupportPoints];

		// apply kernel smoother to all points in the new support
		for (int k = 0; k < nrOfSupportPoints; ++k) {
			xk[k] = xSupport[k];

			// apply kernel to all points
			yk[k] = 0.0;
			double denominator = 0;
			for (int i = 0; i < x.length; ++i) {
				double u = (xk[k] - x[i]) / bandwidth;
				double uk = getKernel(u,kernelType);
				yk[k] += (uk * y[i]);
				denominator += uk;
			}

			if (denominator != 0.0) {
				yk[k] /= denominator;
			}
		}

		return (new FunctionLookupTable(xk,yk));
	}
}
