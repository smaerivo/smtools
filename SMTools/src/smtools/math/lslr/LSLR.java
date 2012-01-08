// ------------------------------
// Filename      : LSLR.java
// Author        : Sven Maerivoet
// Last modified : 07/08/2007
// Target        : Java VM (1.6)
// ------------------------------

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

package smtools.math.lslr;

import java.awt.geom.*;
import java.util.*;
import smtools.math.*;

/**
 * The <CODE>LSLR</CODE> class offers <I>least squares linear regression</I> for 2D data.
 * <P>
 * The 2D data consists of a {@link Vector} of <CODE>Point2D.Double</CODE> objects (which contain
 * x and y values). The least squares linear regression (LSLR) will be calculated as follows:
 * <P>
 * <UL>
 *   y = (<B>slope</B> * x) + <B>intercept</B>
 * </UL>
 * <P>
 * <UL>
 *   <IMG src="doc-files/lslr.png">
 * </UL>
 * <P>
 * Along with the LSLR, this class also calculates the <B>mean</B> and the <B>standard
 * deviation</B> of the y values.
 * <P>
 * <I>Note that an application should check whether or not the LSLR operation was successful,
 * using the {@link LSLR#isSuccessful} method.</I>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 09/12/2004
 */
public final class LSLR
{
	// internal datastructures
	private Vector<Point2D.Double> fDataPoints;
	private boolean fSuccess;
	private double fSlope;
	private double fIntercept;
	private double fMean;
	private double fStandardDeviation;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an <CODE>LSLR</CODE> object with all fields cleared.
	 *
	 * @see LSLR#LSLR(Vector)
	 * @see LSLR#clear()
	 */
	public LSLR()
	{
		clear();
	}

	/**
	 * Constructs an <CODE>LSLR</CODE> object with the given 2D-data points.
	 * <P>
	 * The <CODE>LSLR's</CODE> slope, intercept, mean and standard deviation
	 * are available after the object's construction.
	 *
	 * @see LSLR#LSLR()
	 * @see LSLR#getSlope()
	 * @see LSLR#getIntercept()
	 * @see LSLR#getMean()
	 * @see LSLR#getStandardDeviation()
	 */
	public LSLR(Vector<Point2D.Double> dataPoints)
	{
		setDataPoints(dataPoints);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Uses the given 2D-data points to calculate the slope, intercept,
	 * mean and standard deviation.
	 *
	 * @param dataPoints a <CODE>Vector</CODE> of 2D-data points
	 * @see   Point2D
	 * @see   LSLR#getSlope()
	 * @see   LSLR#getIntercept()
	 * @see   LSLR#getMean()
	 * @see   LSLR#getStandardDeviation()
	 */
	public void setDataPoints(Vector<Point2D.Double> dataPoints)
	{
		clear();
		fDataPoints = dataPoints;
		calcCoefficients();
	}

	/**
	 * Sets all the object's fields equal to zero.
	 */
	public void clear()
	{
		fDataPoints = null;
		fSuccess = false;
		fSlope = 0.0;
		fIntercept = 0.0;
		fMean = 0.0;
		fStandardDeviation = 0.0;
	}

	/**
	 * Returns whether or not the LSLR operation was successful.
	 *
	 * The operation fails whenever:
	 * <P>
	 * <UL>
	 *   <LI>the number of 2D-data points is zero</LI>
	 *   <LI>the result is a vertical line (i.e., the slope is infinite)</LI>
	 * </UL>
	 *
	 * @return <CODE>true</CODE> if the LSLR operation was successful, <CODE>false</CODE> if it failed
	 */
	public boolean isSuccessful()
	{
		return fSuccess;
	}

	/**
	 * Returns the slope of the LSLR operation on the 2D-data points expressed in radians.
	 *
	 * @return the slope of the LSLR operation on the 2D-data points expressed in radians
	 * @see    LSLR#getIntercept()
	 */
	public double getSlope()
	{
		return fSlope;
	}

	/**
	 * Returns the intercept of the LSLR operation on the 2D-data points.
	 *
	 * @return the intercept of the LSLR operation on the 2D-data points
	 * @see    LSLR#getSlope()
	 */
	public double getIntercept()
	{
		return fIntercept;
	}

	/**
	 * Returns the mean of the y-values of the 2D-data points
	 *
	 * @return the mean of the y-values of the 2D-data points
	 * @see    LSLR#getStandardDeviation()
	 */
	public double getMean()
	{
		return fMean;
	}

	/**
	 * Returns the standard deviation of the y-values of the 2D-data points.
	 *
	 * @return the standard deviation of the y-values of the 2D-data points
	 * @see    LSLR#getMean()
	 */
	public double getStandardDeviation()
	{
		return fStandardDeviation;
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	private boolean calcCoefficients()
	{
		if (fDataPoints == null) {
			return false;
		}

		int n = fDataPoints.size();

		if (n == 0) {
			return false;
		}

		double sumOfX = 0.0;
		for (int i = 0; i < n; ++i) {
			sumOfX += fDataPoints.elementAt(i).x;
		}

		double sumOfXSquared = 0.0;
		for (int i = 0; i < n; ++i) {
			sumOfXSquared += (fDataPoints.elementAt(i).x * fDataPoints.elementAt(i).x);
		}

		double sumOfY = 0.0;
		for (int i = 0; i < n; ++i) {
			sumOfY += fDataPoints.elementAt(i).y;
		}

		double sumOfXY = 0.0;
		for (int i = 0; i < n; ++i) {
			sumOfXY += (fDataPoints.elementAt(i).x * fDataPoints.elementAt(i).y);
		}

		double nominator = (n * sumOfXY) - (sumOfX * sumOfY);
		double denominator = (n * sumOfXSquared) - (sumOfX * sumOfX);

		if (denominator == 0.0) {
			return false;
		}

		fSlope = nominator / denominator;

		fIntercept = (sumOfY - (fSlope * sumOfX)) / n;

		fMean = sumOfY / n;

		if (n >= 2) {

			double sum = 0.0;
			for (int i = 0; i < n; ++i) {
				sum += MathTools.sqr(fDataPoints.elementAt(i).y - fMean);
			}

			fStandardDeviation = Math.sqrt(sum / (n - 1));
		}

		fSuccess = true;

		return true;
	}
}
