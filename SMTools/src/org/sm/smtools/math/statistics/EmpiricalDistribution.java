// ------------------------------------------
// Filename      : EmpiricalDistribution.java
// Author        : Sven Maerivoet
// Last modified : 29/10/2016
// Target        : Java VM (1.8)
// ------------------------------------------

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

package org.sm.smtools.math.statistics;

import java.util.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.math.*;

/**
 * The <CODE>EmpiricalDistribution</CODE> class offers a means to calculate the empirical cumulative distribution (CDF) and probability density (PDF) functions, including percentiles.
 * After the distribution is created, a user will typically call the {@link EmpiricalDistribution#analyse()} method to estimate the various statistical quantities.
 * <P>
 * The distribution can only contain <CODE>Integer.MAX_VALUE</CODE> samples.
 * <P>
 * Note that a valid {@link I18NL10N} database must be available!
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 29/10/2016
 */
public final class EmpiricalDistribution
{
	// the minimum number of histogram bins
	private static final int kMinNrOfHistogramBins = 10;

	// internal datastructures
	private int fN;
	private double[] fX;
	private double[] fXSorted;
	private double fXMin;
	private double fXMax;
	private double fXRange;
	private double fKDEXMin;
	private double fKDEXMax;
	private double fKDEXRange;
	private double[] fCDF;
	private double[] fPercentiles;
	private double fTrimmedMean;
	private double fMedian;
	private double fInterquartileRange;
	private boolean fUseOptimalNrOfHistogramBins;
	private int fNrOfHistogramBins;
	private double[] fHistogramBinCounts;
	private double[] fHistogramBinFrequencies;
	private double[] fHistogramBinRightEdges;
	private double[] fHistogramBinCentres;
	private double fHistogramBinWidth;
	private FunctionLookupTable fKDEPDF;
	private double fExpectedValue;
	private FunctionLookupTable fKDEPDFExtrema;
	private double fVariance;
	private double fStandardDeviation;
	private double fSkewness;
	private double fSkewnessConfidenceBounds;
	private double fSkewnessZStatistic;
	private double fKurtosis;
	private double fKurtosisZStatistic;
	private double[] fZScores;
	private boolean[] fOutliers;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an empty <CODE>EmpiricalDistribution</CODE> object.
	 */
	public EmpiricalDistribution()
	{
		this(null,true,0);
	}

	/**
	 * Constructs an <CODE>EmpiricalDistribution</CODE> object for a given array of values.
	 * <P>
	 * The Freedman-Diaconis rule is applied for finding the optimal histogram bin width, and consequently the optimal number of histogram bins:
	 * <P>
	 * bin width = 2 * IQR / n^1/3
	 * 
	 * @param x the array of values to estimate the empirical distribution for
	 */
	public EmpiricalDistribution(double[] x)
	{
		this(x,true,0);
	}

	/**
	 * Constructs an <CODE>EmpiricalDistribution</CODE> object for a given array of values and a user-specified number of histogram bins.
	 * 
	 * @param x                  the array of values to estimate the empirical distribution for
	 * @param nrOfHistogramBins  the user-specified number of histogram bins
	 */
	public EmpiricalDistribution(double[] x, int nrOfHistogramBins)
	{
		this(x,false,nrOfHistogramBins);
	}

	/**
	 * Constructs an <CODE>EmpiricalDistribution</CODE> object for a given array of values and user-specified histogram bin right edges.
	 * 
	 * @param x                       the array of values to estimate the empirical distribution for
	 * @param histogramBinRightEdges  the array of values containing the histogram bin right edges
	 */
	public EmpiricalDistribution(double[] x, double[] histogramBinRightEdges)
	{
		setData(x,false,0,histogramBinRightEdges);
	}

	/**
	 * Private constructor that is invoked in case a number of histograms was specified or requested.
	 *
	 * @param x                            -
	 * @param useOptimalNrOfHistogramBins  -
	 * @param nrOfHistogramBins            -
	 */
	private EmpiricalDistribution(double[] x, boolean useOptimalNrOfHistogramBins, int nrOfHistogramBins)
	{
		setData(x,useOptimalNrOfHistogramBins,nrOfHistogramBins,null);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Retrieves the raw data for this empirical distribution.
	 * 
	 * @return the raw data for this empirical distribution
	 * @see    EmpiricalDistribution#getSortedData()
	 */
	public double[] getData()
	{
		return fX;
	}

	/**
	 * Retrieves the raw data for this empirical distribution.
	 * 
	 * @return the raw data for this empirical distribution
	 * @see    EmpiricalDistribution#getData()
	 */
	public double[] getSortedData()
	{
		return fXSorted;
	}

	/**
	 * Sets the source data for the empirical distribution.
	 * <P>
	 * The Freedman-Diaconis rule is applied for finding the optimal histogram bin width, and consequently the optimal number of histogram bins:
	 * <P>
	 * bin width = 2 * IQR / n^1/3
	 * 
	 * @param x  the array of values to estimate the empirical distribution for
	 */
	public void setData(double[] x)
	{
		setData(x,true,0,null);
	}

	/**
	 * Sets the source data for the empirical distribution, as well as a user-specified number of histogram bins.
	 * 
	 * @param x                  the array of values to estimate the empirical distribution for
	 * @param nrOfHistogramBins  the user-specified number of histogram bins
	 */
	public void setData(double[] x, int nrOfHistogramBins)
	{
		setData(x,false,nrOfHistogramBins,null);
	}

	/**
	 * Clears the empirical distribution.
	 */
	public void clear()
	{
		fN = 0;
		fX = null;
		fXSorted = null;
		fXMin = 0.0;
		fXMax = 0.0;
		fXRange = 0.0;
		fKDEXMin = 0.0;
		fKDEXMax = 0.0;
		fKDEXRange = 0.0;
		fCDF = null;
		fPercentiles = null;
		fTrimmedMean = 0.0;
		fMedian = 0.0;
		fInterquartileRange = 0.0;
		fUseOptimalNrOfHistogramBins = false;
		fNrOfHistogramBins = 0;
		fHistogramBinCounts = null;
		fHistogramBinFrequencies = null;
		fHistogramBinRightEdges = null;
		fHistogramBinCentres = null;
		fHistogramBinWidth = 0.0;
		fKDEPDF = null;
		fExpectedValue = 0.0;
		fKDEPDFExtrema = null;
		fVariance = 0.0;
		fStandardDeviation = 0.0;
		fSkewness = 0.0;
		fSkewnessConfidenceBounds = 0.0;
		fSkewnessZStatistic = 0.0;
		fKurtosis = 0.0;
		fKurtosisZStatistic = 0.0;
		fZScores = null;
		fOutliers = null;
	}

	/**
	 * Estimates the empirical distribution and analyses it various statistical quantities.
	 */
	public void analyse()
	{
		// check if data was loaded
		if (fX == null) {
			return;
		}
		else if (fX.length == 0) {
			return;
		}

		// *********************************************************
		// estimate empirical cumulative distribution function (CDF)
		// *********************************************************

		// sort all samples in ascending order
		ArrayList<Double> sortedX = new ArrayList<>();
		fXSorted = new double[fN];
		for (int i = 0; i < fN; ++i) {
			sortedX.add(fX[i]);
			fXSorted[i] = fX[i];
		}
		Collections.sort(sortedX);
		fXMin = fX[0];
		fXMax = fX[0];
		for (int i = 0; i < fN; ++i) {
			// convert back to array
			fXSorted[i] = sortedX.get(i);
			if (fXSorted[i] < fXMin) {
				fXMin = fXSorted[i];
			}
			if (fXSorted[i] > fXMax) {
				fXMax = fXSorted[i];
			}
		}
		fXRange = fXMax - fXMin;

		// use the product-limit (Kaplan-Meier) estimate of the survivor function and transform to the CDF
		double[] D = new double[fN];
		for (int i = 0; i < fN; ++i) {
			D[i] = 1;
		}
		double[] N = new double[fN];
		for (int i = 0; i < fN; ++i) {
			N[i] = fN - i;
		}

		double[] cumulativeProductTerms = new double[fN];
		for (int i = 0; i < fN; ++i) {
			cumulativeProductTerms[i] = 1.0 - (D[i] / N[i]);
		}
		double[] S = new double[fN];
		S[0] = cumulativeProductTerms[0];
		for (int i = 1; i < fN; ++i) {
			S[i] = S[i - 1] * cumulativeProductTerms[i];
		}
		double[] Func = new double[fN];
		double F0 = 0.0;
		for (int i = 0; i < fN; ++i) {
			Func[i] = 1 - S[i];
		}

		fCDF = new double[fN];
		fCDF[0] = F0;
		fCDF[fN - 1] = 1.0;
		for (int i = 1; i < (fN - 1); ++i) {
			fCDF[i] = Func[i - 1];
		}

		// ********************
		// estimate percentiles
		// ********************

		// estimate the percentiles
		fPercentiles = new double[1001];
		for (int percentile = 0; percentile <= 1000; ++percentile) {
			double rank = (((double) percentile / 1000.0) * ((double) fN - 1.0)) + 1.0;
			if (rank <= 1.0) {
				fPercentiles[percentile] = fXSorted[0];
			}
			else if (rank >= fN) {
				fPercentiles[percentile] = fXSorted[fN - 1];
			}
			else {
				int k = (int) Math.floor(rank);
				double d = rank - (double) k;
				fPercentiles[percentile] = fXSorted[k - 1] + (d * (fXSorted[k] - fXSorted[k - 1]));
			}
		}

		// *******************************
		// estimate statistical quantities
		// *******************************

		estimateStatistics();

		// *****************************************************
		// estimate empirical probability density function (PDF)
		// *****************************************************

		estimatePDF();
	}

	/**
	 * Returns the value of the cumulative distribution function (CDF) evaluated at <CODE>x</CODE>.
	 *
	 * @param x  the value to evaluate the cumulative distribution function at
	 * @return   the value of the cumulative distribution function evaluated at <CODE>x</CODE>
	 */
	public double getCDF(double x)
	{
		if (fX == null) {
			return 0.0;
		}

		// do linear interpolation
		ArraySearchBounds bounds = MathTools.searchArrayBounds(fX,x);

		double xRange = fX[bounds.getUpperBound()] - fX[bounds.getLowerBound()];
		double xDelta = x - fX[bounds.getLowerBound()];
		double fraction = 0.0;
		if ((new Double(xRange)).compareTo(0.0) != 0) {
			fraction = xDelta / xRange;
		}

		double cdfRange = fCDF[bounds.getUpperBound()] - fCDF[bounds.getLowerBound()];
		double cdfDelta = fraction * cdfRange;

		return (fCDF[bounds.getLowerBound()] + cdfDelta);
	}

	/**
	 * Returns the given percentile.
	 * 
	 * @param percentile  the requested percentile (in the interval [0,100])
	 * @return            the requested percentile value
	 */
	public double getPercentile(int percentile)
	{
		if (fPercentiles == null) {
			return 0.0;
		}

		return fPercentiles[MathTools.clip(percentile * 10,0,1000)];
	}

	/**
	 * Returns the given percentile.
	 * 
	 * @param percentile  the requested percentile (in the interval [0.0,100.0])
	 * @return            the requested percentile value
	 */
	public double getPercentile(double percentile)
	{
		if (fPercentiles == null) {
			return 0.0;
		}

		return fPercentiles[MathTools.clip((int) Math.round(percentile * 10.0),0,1000)];
	}

	/**
	 * Returns all the percentiles for the range [0,100].
	 *
	 * @return an array containing all the percentiles in the range [0,100]
	 */
	public double[] getPercentiles()
	{
		return fPercentiles;
	}

	/**
	 * Returns the minimum of the input values.
	 *
	 * @return the minimum of the input values
	 */
	public double getXMinimum()
	{
		return fXMin;
	}

	/**
	 * Returns the maximum of the input values.
	 *
	 * @return the maximum of the input values
	 */
	public double getXMaximum()
	{
		return fXMax;
	}

	/**
	 * Returns the range of the input values.
	 *
	 * @return the range of the input values
	 */
	public double getXRange()
	{
		return fXRange;
	}

	/**
	 * Returns the minimum of the values for a kernel density estimation (KDE) of the probability distribution function (PDF).
	 *
	 * @return the minimum of the values for a kernel density estimation (KDE) of the probability distribution function (PDF)
	 */
	public double getKDEXMinimum()
	{
		return fKDEXMin;
	}

	/**
	 * Returns the maximum of the values for a kernel density estimation (KDE) of the probability distribution function (PDF).
	 *
	 * @return the maximum of the values for a kernel density estimation (KDE) of the probability distribution function (PDF)
	 */
	public double getKDEXMaximum()
	{
		return fKDEXMax;
	}

	/**
	 * Returns the range of the values for a kernel density estimation (KDE) of the probability distribution function (PDF).
	 *
	 * @return the range of the values for a kernel density estimation (KDE) of the probability distribution function (PDF)
	 */
	public double getKDEXRange()
	{
		return fKDEXRange;
	}

	/**
	 * Returns the median (i.e., the 50th percentile).
	 * 
	 * @return the median
	 */
	public double getMedian()
	{
		return fMedian;
	}

	/**
	 * Returns the interquartile range (IQR) (i.e., the difference between the 75th and the 25th percentiles).
	 * 
	 * @return the interquartile range (IQR)
	 */
	public double getInterquartileRange()
	{
		return fInterquartileRange;
	}

	/**
	 * Recalculates the probability density function (PDF).
	 * <P>
	 * The Freedman-Diaconis rule is applied for finding the optimal histogram bin width, and consequently the optimal number of histogram bins:
	 * <P>
	 * bin width = 2 * IQR / n^1/3
	 */
	public void recalculatePDF()
	{
		fUseOptimalNrOfHistogramBins = true;
		estimatePDF();
	}

	/**
	 * Recalculates the probability density function (PDF) using a user-specified number of histogram bins.
	 *
	 * @param nrOfHistogramBins  the user-specified number of histogram bins
	 */
	public void recalculatePDF(int nrOfHistogramBins)
	{
		fUseOptimalNrOfHistogramBins = false;
		fNrOfHistogramBins = nrOfHistogramBins;
		estimatePDF();
	}

	/**
	 * Calculates the bandwidth for kernel density estimation (KDE) based on Silverman's Rule-of-Thumb.
	 *
	 * @param  kernelType  the type of kernel function to use in the calculation
	 * @return             an estimation of the bandwidth
	 */
	public double calculateKDEPDFBandwidth(MathTools.EKernelType kernelType)
	{
		if (fX == null) {
			return 0.0;
		}

		// apply Silverman Rule-of-Thumb to calculate the bandwidth
		double hConst = 0.0;
		switch (kernelType) {
			case kRectangular:
				hConst = 1.0;
				break;
			case kTriangular:
				hConst = 1.0;
				break;
			case kEpanechnikov:
				hConst = 2.34;
				break;
			case kQuartic:
				hConst = 2.78;
				break;
			case kGaussian:
				hConst = 1.06;
				break;
			case kLanczos:
				hConst = 1.0;
				break;
		}

		return (getStandardDeviation() * hConst * Math.pow(getN(),-1.0 / 5.0));
	}

	/**
	 * Estimates the probability distribution function (PDF) using a specified kernel function.
	 *
	 * @param  kernelType         the type of kernel function to use
	 * @param  bandwidth          the bandwidth of the kernel function
	 * @param  nrOfSupportPoints  the number of (X,Y) values to use for the smoothened 1D function
	 * @param  minSupport         the minimum value for the support
	 * @param  maxSupport         the maximum value for the support
	 */
	public void estimateKDEPDF(MathTools.EKernelType kernelType, double bandwidth, int nrOfSupportPoints, double minSupport, double maxSupport)
	{
		if (fX == null) {
			return;
		}

		// prepare new support
		double xRange = maxSupport - minSupport;
		double delta = xRange / (nrOfSupportPoints - 1);
		double[] xSupport = new double[nrOfSupportPoints];
		for (int i = 0; i < nrOfSupportPoints; ++i) {
			xSupport[i] = minSupport + (i * delta);
		}

		double[] xk = new double[nrOfSupportPoints];
		double[] yk = new double[nrOfSupportPoints];

		// apply kernel density estimator to all points in the new support
		for (int k = 0; k < nrOfSupportPoints; ++k) {
			xk[k] = xSupport[k];

			// apply kernel function to all points
			yk[k] = 0.0;
			for (int i = 0; i < getN(); ++i) {
				double u = (xk[k] - fX[i]) / bandwidth;
				double uk = MathTools.getKernel(u,kernelType);
				yk[k] += uk;
			}
			yk[k] /= (getN() * bandwidth);
		}

		fKDEXMin = minSupport;
		fKDEXMax = maxSupport;
		fKDEXRange = xRange;
		fKDEPDF = new FunctionLookupTable(xk,yk);

		// find all modes
		Extrema extrema = MathTools.findExtrema(fKDEPDF.fY);

		double[] x = new double[extrema.getNrOfLocalMaxima()];
		double[] y = new double[extrema.getNrOfLocalMaxima()];
		for (int i = 0; i < extrema.getNrOfLocalMaxima(); ++i) {
			Extremum maximum = extrema.getLocalMaximum(i);
			x[i] = fKDEPDF.fX[maximum.getIndex()];
			y[i] = maximum.getValue();
		}

		fKDEPDFExtrema = new FunctionLookupTable(x,y);
	}

	/**
	 * Returns the number of histogram bins used for estimating the probability density function (PDF).
	 *
	 * @return the number of histogram bins used for estimating the probability density function (PDF)
	 */
	public int getNrOfHistogramBins()
	{
		return fNrOfHistogramBins;
	}

	/**
	 * Returns the count associated with a specified histogram bin.
	 *
	 * @param histogramBin  the histogram bin to lookup the count for
	 * @return              the count associated with the specified histogram bin
	 */
	public double getHistogramBinCount(int histogramBin)
	{
		if (fHistogramBinCounts == null) {
			return 0.0;
		}

		return fHistogramBinCounts[histogramBin];
	}

	/**
	 * Returns the counts for all the histogram bins.
	 *
	 * @return an array containing the counts for all the histogram bins
	 */
	public double[] getHistogramBinCounts()
	{
		return fHistogramBinCounts;
	}

	/**
	 * Returns the frequency associated with a specified histogram bin.
	 *
	 * @param histogramBin  the histogram bin to lookup the frequency for
	 * @return              the frequency associated with the specified histogram bin
	 */
	public double getHistogramBinFrequency(int histogramBin)
	{
		if (fHistogramBinFrequencies == null) {
			return 0.0;
		}

		return fHistogramBinFrequencies[histogramBin];
	}

	/**
	 * Returns the frequencies for all the histogram bins.
	 *
	 * @return an array containing the frequencies for all the histogram bins
	 */
	public double[] getHistogramBinFrequencies()
	{
		return fHistogramBinFrequencies;
	}

	/**
	 * Returns the centre of a specified histogram bin.
	 *
	 * @param histogramBin  the histogram bin to lookup the centre for
	 * @return              the centre of the specified histogram bin
	 */
	public double getHistogramBinCentre(int histogramBin)
	{
		if (fHistogramBinCentres == null) {
			return 0.0;
		}

		return fHistogramBinCentres[histogramBin];
	}

	/**
	 * Returns the centres of all the histogram bins.
	 *
	 * @return an array containing the centres of all the histogram bins
	 */
	public double[] getHistogramBinCentres()
	{
		return fHistogramBinCentres;
	}

	/**
	 * Returns the width of a histogram bin.
	 *
	 * @return the width of a histogram bin
	 */
	public double getHistogramBinWidth()
	{
		return fHistogramBinWidth;
	}

	/**
	 * Returns the value of the probability density function (PDF) evaluated at <CODE>x</CODE> (based on a histogram).
	 *
	 * @param x  the value to evaluate the probability density function at
	 * @return   the value of the probability density function evaluated at <CODE>x</CODE>
	 */
	public double getPDF(double x)
	{
		if (fX == null) {
			return 0.0;
		}

		double pdf = 0.0;

		if ((x >= fXMin) && (x <= fXMax)) {
			double x1 = 0.0;
			double y1 = 0.0;
			double x2 = 0.0;
			double y2 = 0.0;
			if (x <= fHistogramBinCentres[0]) {
				// interpolate from (0,0) to the bin centre
				x2 = fHistogramBinCentres[0];
				y2 = fHistogramBinFrequencies[0];
			}
			else if (x >= fHistogramBinCentres[fNrOfHistogramBins - 1]) {
				// interpolate from the bin centre to (0,0)
				x1 = fHistogramBinCentres[fNrOfHistogramBins - 1];
				y1 = fHistogramBinFrequencies[fNrOfHistogramBins - 1];
			}
			else {
				ArraySearchBounds bounds = MathTools.searchArrayBounds(fHistogramBinCentres,x);
				x1 = fHistogramBinCentres[bounds.getLowerBound()];
				y1 = fHistogramBinFrequencies[bounds.getLowerBound()];
				x2 = fHistogramBinCentres[bounds.getUpperBound()];
				y2 = fHistogramBinFrequencies[bounds.getUpperBound()];
			}

			// do a linear interpolation
			if (x1 != x2) {
				pdf = y1 + ((x - x1) * ((y2 - y1) / (x2 - x1)));
			}
		}

		// fail-safe for negative probabilities
		if (pdf < 0.0) {
			pdf = 0.0;
		}

		return pdf;
	}

	/**
	 * Returns the value of the probability density function (PDF) evaluated at <CODE>x</CODE> (based on kernel density estimation, KDE).
	 *
	 * @param x  the value to evaluate the probability density function at
	 * @return   the value of the probability density function evaluated at <CODE>x</CODE>
	 */
	public double getKDEPDF(double x)
	{
		if ((fX == null) || (fKDEPDF == null)) {
			return 0.0;
		}

		double pdf = 0.0;
		if ((x >= fKDEXMin) && (x <= fKDEXMax)) {

			ArraySearchBounds bounds = MathTools.searchArrayBounds(fKDEPDF.fX,x);
			double x1 = fKDEPDF.fX[bounds.getLowerBound()];
			double y1 = fKDEPDF.fY[bounds.getLowerBound()];
			double x2 = fKDEPDF.fX[bounds.getUpperBound()];
			double y2 = fKDEPDF.fY[bounds.getUpperBound()];

			// do a linear interpolation
			if (x1 != x2) {
				pdf = y1 + ((x - x1) * ((y2 - y1) / (x2 - x1)));
			}
		}

		// fail-safe for negative probabilities
		if (pdf < 0.0) {
			pdf = 0.0;
		}

		return pdf;
	}

	/**
	 * Returns the previously complete calculated kernel density estimation (KDE) of the probability distribution function (PDF).
	 *
	 * @return the lookup table with <I>X</I> and <I>Y</I> values for the KDE PDF
	 */
	public FunctionLookupTable getFullKDEPDF()
	{
		return fKDEPDF;
	}

	/**
	 * Returns the sample size.
	 * 
	 * @return the sample size
	 */
	public int getN()
	{
		return fN;
	}

	/**
	 * Returns the expected value for the first moment (population mean), which in this case is approximated by the sample mean.
	 * 
	 * @return the expected value for the first moment
	 * @see    EmpiricalDistribution#getMean()
	 */
	public double getExpectedValue()
	{
		return fExpectedValue;
	}

	/**
	 * This is the sample mean, which in this case is an alias for the expected value.
	 * 
	 * @return the sample mean
	 * @see    EmpiricalDistribution#getExpectedValue()
	 * @see    EmpiricalDistribution#getTrimmedMean(double)
	 */
	public double getMean()
	{
		return getExpectedValue();
	}

	/**
	 * This is the trimmed (or truncated) mean, which corresponds to the mean calculated
	 * after symmetrically discarding a certain percentage of data points at the high and low end (without interpolation).
	 * 
	 * @param percentageToTrim  the percentage to trim (left and right combined)
	 * @return                  the trimmed mean
	 * @see                     EmpiricalDistribution#getMean()
	 */
	public double getTrimmedMean(double percentageToTrim)
	{
		int nrOfDataPointsToDiscardAtEachEnd = (int) ((double) fN * MathTools.clip(percentageToTrim,0.0,1.0) / 2.0);
		int lowEnd = (int) MathTools.clip(nrOfDataPointsToDiscardAtEachEnd,1.0,fN) - 1;
		int highEnd = (int) MathTools.clip(fN - nrOfDataPointsToDiscardAtEachEnd,lowEnd,fN) - 1;
		int range = highEnd - lowEnd + 1;

		// E[X] = sum(Xi * fi)
		if (range > 0) {
			double frequency = 1.0 / (double) range;
			fTrimmedMean = 0.0;
			for (int i = lowEnd; i <= highEnd; ++i) {
				fTrimmedMean += fX[i];
			}
			return (fTrimmedMean * frequency);
		}
		else {
			return 0.0;
		}
	}

	/**
	 * Returns all modes (i.e., local maxima) for the calculated kernel density estimation (KDE) of the probability density function (PDF).
	 *
	 * @return all modes of the specified PDF
	 */
	public FunctionLookupTable getKDEPDFModes()
	{
		return fKDEPDFExtrema;
	}

	/**
	 * Returns the sample variance (using an unbiased estimator of the population variance).
	 * 
	 * @return the sample variance (using an unbiased estimator of the population variance)
	 */
	public double getVariance()
	{
		return fVariance;
	}

	/**
	 * Returns the standard deviation (i.e., the positive square root of the variance).
	 * 
	 * @return the standard deviation
	 */
	public double getStandardDeviation()
	{
		return fStandardDeviation;
	}

	/**
	 * Returns the sample skewness (using an unbiased estimator).
	 * <P>
	 * Skewness implies:
	 * <UL>
	 *   <LI><B>Positive skew</B>: longer right tail, density mass constrained to the left.</LI>
	 *   <LI><B>Negative skew</B>: longer left tail, density mass constrained to the right.</LI>
	 * </UL>
	 * <P>
	 * Note that the amount of skewness is determined as follows:
	 * <UL>
	 *   <LI>-0.5 &le; skewness &le; +0.5: approximately symmetric distribution.</LI> 
	 *   <LI>-1 &le; skewness &lt; -0.5, or +0.5 &lt; skewness &le; +1: moderately skewed distribution.</LI> 
	 *   <LI>skewness &lt; -1, or skewness &gt; +1: highly skewed distribution.</LI> 
	 * </UL>
	 * 
	 * @return the sample skewness (using an unbiased estimator)
	 */
	public double getSkewness()
	{
		return fSkewness;
	}

	/**
	 * Returns the symmetrical skewness' confidence bounds for a 95% confidence interval, defined as twice the standard error of skewness (SES).
	 * 
	 * @return the symmetrical skewness' confidence bounds for a 95% confidence interval
	 */
	public double getSkewnessConfidenceBounds()
	{
		return fSkewnessConfidenceBounds;
	}

	/**
	 * Returns a two-tailed test statistic <I>Z</I> of skewness (different from zero) with a 5% significance level.
	 * <UL>
	 *   <LI><B><I>Z</I> &gt; +2</B>: population is very likely positively skewed.</LI>
	 *   <LI><B><I>Z</I> &lt; -2</B>: population is very likely negatively skewed.</LI>
	 *   <LI><B>-2 &le; <I>Z</I> &le; +2</B>: inconclusive (might be symmetric, might be skewed).</LI>
	 * </UL>
	 * <P>
	 * The larger <I>Z</I>, the higher the probability.
	 * 
	 * @return the skewness <I>Z</I>-statistic
	 */
	public double getSkewnessZStatistic()
	{
		return fSkewnessZStatistic;
	}

	/**
	 * Returns the sample kurtosis (using an unbiased estimator).
	 * <P>
	 * The value returned is the <I>excess kurtosis</I>, such that it is zero for a normal distribution:
	 * <UL>
	 *   <LI><B>Mesokurtic</B>: has zero excess (e.g., normal distribution).</LI>
	 *   <LI><B>Leptokurtic</B>: has positive excess, higher and sharper central peak, with longer and fatter tails (i.e., more extreme values).</LI>
	 *   <LI><B>Platykurtic</B>: has negative excess, lower and broader central peak, with shorter and thinner tails (i.e., less extreme values).</LI>
	 * </UL>
	 * <P>
	 * As the kurtosis increases, more probability mass is transferred from the distribution's shoulders to the centre and tails.
	 * 
	 * @return the sample kurtosis (using an unbiased estimator)
	 */
	public double getKurtosis()
	{
		return fKurtosis;
	}

	/**
	 * Returns a two-tailed test statistic <I>Z</I> of kurtosis (different from zero) with a 5% significance level.
	 * <UL>
	 *   <LI><B><I>Z</I> &gt; +2</B>: population has very likely positive kurtosis (leptokurtic).</LI>
	 *   <LI><B><I>Z</I> &lt; -2</B>: population has very likely negative kurtosis (platykurtic).</LI>
	 *   <LI><B>-2 &le; <I>Z</I> &le; +2</B>: inconclusive (might be negative, zero, or positive kurtosis).</LI>
	 * </UL>
	 * <P>
	 * The larger <I>Z</I>, the higher the probability.
	 * 
	 * @return the kurtosis <I>Z</I>-statistic
	 */
	public double getKurtosisZStatistic()
	{
		return fKurtosisZStatistic;
	}

	/**
	 * Calculates the Jarque-Bera test statistic.
	 * <P>
	 * This tests the goodness-of-fit of whether the distribution's skewness and kurtosis match that of the normal distribution.
	 * <P>
	 * <I>The test result should be compared to the values of the chi-square distribution with 2 degrees of freedom.</I> 
	 *
	 * @return the Jarque-Bera test statistic
	 * @see    EmpiricalDistribution#isJarqueBeraTestAccepted(double)
	 * @see    EmpiricalDistribution#getChiSquare(double,int)
	 */
	public double getJarqueBeraTestStatistic()
	{
		if (fX == null) {
			return 0.0;
		}

		return (fN * ((MathTools.sqr(fSkewness) / 6.0) + (MathTools.sqr(fKurtosis) / 24.0)));
	}

	/**
	 * Compares the Jarque-Bera test statistic with the chi-square distribution with 2 degrees of freedom for a given alpha level.
	 * <P>
	 * Alpha levels can be 0.995, 0.99, 0.975, 0.95, 0.90, 0.10, 0.05, 0.025, 0.01, or 0.005.
	 * 
	 * @param  alpha  the alpha level 
	 * @return        <CODE>true</CODE> if the test is accepted, <CODE>false</CODE> if it is rejected
	 * @see           EmpiricalDistribution#getJarqueBeraTestStatistic()
	 * @see           EmpiricalDistribution#getChiSquare(double,int)
	 */
	public boolean isJarqueBeraTestAccepted(double alpha)
	{
		return (getJarqueBeraTestStatistic() <= getChiSquare(alpha,2));
	}

	/**
	 * Returns the chi-square value corresponding to a specified alpha level and number of degrees of freedom.
	 * <P>
	 * Alpha levels can be 0.995, 0.99, 0.975, 0.95, 0.90, 0.10, 0.05, 0.025, 0.01, or 0.005.
	 * <P>
	 * The number of degrees of freedom is clipped between 1 and 100.
	 *
	 * @param alpha             the alpha level
	 * @param degreesOfFreedom  the number of degrees of freedom
	 * @return                  the chi-square value corresponding to the specified alpha level and number of degrees of freedom
	 * @see                     EmpiricalDistribution#getJarqueBeraTestStatistic()
	 * @see                     EmpiricalDistribution#isJarqueBeraTestAccepted(double)
	 */
	public static double getChiSquare(double alpha, int degreesOfFreedom)
	{
		double[][] kChiSquareValues = {
			{0.0,0.01,0.072,0.207,0.412,0.676,0.989,1.344,1.735,2.156,2.603,3.074,3.565,4.075,4.601,5.142,5.697,6.265,6.844,7.434,8.034,8.643,9.26,9.886,10.52,11.16,11.808,12.461,13.121,13.787,20.707,27.991,35.534,43.275,51.172,59.196,67.328},
			{0.0,0.02,0.115,0.297,0.554,0.872,1.239,1.646,2.088,2.558,3.053,3.571,4.107,4.66,5.229,5.812,6.408,7.015,7.633,8.26,8.897,9.542,10.196,10.856,11.524,12.198,12.879,13.565,14.256,14.953,22.164,29.707,37.485,45.442,53.54,61.754,70.065},
			{0.001,0.051,0.216,0.484,0.831,1.237,1.69,2.18,2.7,3.247,3.816,4.404,5.009,5.629,6.262,6.908,7.564,8.231,8.907,9.591,10.283,10.982,11.689,12.401,13.12,13.844,14.573,15.308,16.047,16.791,24.433,32.357,40.482,48.758,57.153,65.647,74.222},
			{0.004,0.103,0.352,0.711,1.145,1.635,2.167,2.733,3.325,3.94,4.575,5.226,5.892,6.571,7.261,7.962,8.672,9.39,10.117,10.851,11.591,12.338,13.091,13.848,14.611,15.379,16.151,16.928,17.708,18.493,26.509,34.764,43.188,51.739,60.391,69.126,77.929},
			{0.016,0.211,0.584,1.064,1.61,2.204,2.833,3.49,4.168,4.865,5.578,6.304,7.042,7.79,8.547,9.312,10.085,10.865,11.651,12.443,13.24,14.041,14.848,15.659,16.473,17.292,18.114,18.939,19.768,20.599,29.051,37.689,46.459,55.329,64.278,73.291,82.358},
			{2.706,4.605,6.251,7.779,9.236,10.645,12.017,13.362,14.684,15.987,17.275,18.549,19.812,21.064,22.307,23.542,24.769,25.989,27.204,28.412,29.615,30.813,32.007,33.196,34.382,35.563,36.741,37.916,39.087,40.256,51.805,63.167,74.397,85.527,96.578,107.565,118.498},
			{3.841,5.991,7.815,9.488,11.07,12.592,14.067,15.507,16.919,18.307,19.675,21.026,22.362,23.685,24.996,26.296,27.587,28.869,30.144,31.41,32.671,33.924,35.172,36.415,37.652,38.885,40.113,41.337,42.557,43.773,55.758,67.505,79.082,90.531,101.879,113.145,124.342},
			{5.024,7.378,9.348,11.143,12.833,14.449,16.013,17.535,19.023,20.483,21.92,23.337,24.736,26.119,27.488,28.845,30.191,31.526,32.852,34.17,35.479,36.781,38.076,39.364,40.646,41.923,43.195,44.461,45.722,46.979,59.342,71.42,83.298,95.023,106.629,118.136,129.561},
			{6.635,9.21,11.345,13.277,15.086,16.812,18.475,20.09,21.666,23.209,24.725,26.217,27.688,29.141,30.578,32,33.409,34.805,36.191,37.566,38.932,40.289,41.638,42.98,44.314,45.642,46.963,48.278,49.588,50.892,63.691,76.154,88.379,100.425,112.329,124.116,135.807},
			{7.879,10.597,12.838,14.86,16.75,18.548,20.278,21.955,23.589,25.188,26.757,28.3,29.819,31.319,32.801,34.267,35.718,37.156,38.582,39.997,41.401,42.796,44.181,45.559,46.928,48.29,49.645,50.993,52.336,53.672,66.766,79.49,91.952,104.215,116.321,128.299,140.169}
		};

		int alphaIndex = 0;
		if (alpha == 0.995) { alphaIndex = 0; }
		else if (alpha == 0.990) { alphaIndex = 1; }
		else if (alpha == 0.975) { alphaIndex = 2; }
		else if (alpha == 0.950) { alphaIndex = 3; }
		else if (alpha == 0.900) { alphaIndex = 4; }
		else if (alpha == 0.100) { alphaIndex = 5; }
		else if (alpha == 0.050) { alphaIndex = 6; }
		else if (alpha == 0.025) { alphaIndex = 7; }
		else if (alpha == 0.010) { alphaIndex = 8; }
		else if (alpha == 0.005) { alphaIndex = 9; }

		degreesOfFreedom = (int) MathTools.clip(degreesOfFreedom,1,100);
		if (degreesOfFreedom <= 30) {
			// values up to and including 30 are specified exactly
			return kChiSquareValues[alphaIndex][degreesOfFreedom - 1];
		}
		else {
			// interpolate for values above 30 (which are given in increments of 10)
			int lowerDoF = (degreesOfFreedom / 10) * 10;
			int upperDoF = (int) MathTools.clip(((degreesOfFreedom / 10) + 1) * 10,0,100);
			double interp = (double) (degreesOfFreedom - lowerDoF) / 10.0;
			lowerDoF = 30 + (lowerDoF / 10) - 3; // adjust for index into array
			upperDoF = 30 + (upperDoF / 10) - 3; // adjust for index into array
			double lowerChiSquare = kChiSquareValues[alphaIndex][lowerDoF - 1];
			double upperChiSquare = kChiSquareValues[alphaIndex][upperDoF - 1];
			return MathTools.normalisedLinearInterpolation(interp,lowerChiSquare,upperChiSquare);
		}
	}

	/**
	 * Returns the calculated z-scores, defined as:
	 * <P>
	 * (value - mean) / standard deviation
	 *
	 * @return the z-scores
	 * @see    EmpiricalDistribution#getOutliers()
	 */
	public double[] getZScores()
	{
		return fZScores;
	}

	/**
	 * Returns the outliers which are defined as having z-scores greater than 3.
	 *
	 * @return the outliers
	 * @see    EmpiricalDistribution#getZScores()
	 */
	public boolean[] getOutliers()
	{
		return fOutliers;
	}

	/**
	 * Returns a descriptive label of the mean (expected value).
	 *
	 * @return a descriptive label of the mean
	 */
	public static String getMeanDescription()
	{
		return I18NL10N.translate("text.Statistics.Mean");
	}

	/**
	 * Returns a descriptive label of the standard deviation.
	 *
	 * @return a descriptive label of the standard deviation
	 */
	public static String getStandardDeviationDescription()
	{
		return I18NL10N.translate("text.Statistics.StandardDeviation");
	}

	/**
	 * Returns a descriptive label of the variance.
	 *
	 * @return a descriptive label of the variance
	 */
	public static String getVarianceDescription()
	{
		return I18NL10N.translate("text.Statistics.Variance");
	}

	/**
	 * Returns a descriptive label of the median.
	 *
	 * @return a descriptive label of the median
	 */
	public static String getMedianDescription()
	{
		return I18NL10N.translate("text.Statistics.Median");
	}

	/**
	 * Returns a descriptive label of the interquartile range (IQR).
	 *
	 * @return a descriptive label of the interquartile range (IQR)
	 */
	public static String getInterquartileRangeDescription()
	{
		return I18NL10N.translate("text.Statistics.InterquartileRange");
	}

	/**
	 * Returns a descriptive label of a percentile.
	 *
	 * @return a descriptive label of a percentile
	 */
	public static String getPercentileDescription()
	{
		return I18NL10N.translate("text.Statistics.Percentile");
	}

	/**
	 * Returns a descriptive label of the skewness.
	 *
	 * @return a descriptive label of the skewness
	 */
	public static String getSkewnessDescription()
	{
		return I18NL10N.translate("text.Statistics.Skewness");
	}

	/**
	 * Returns a descriptive label of the kurtosis.
	 *
	 * @return a descriptive label of the kurtosis
	 */
	public static String getKurtosisDescription()
	{
		return I18NL10N.translate("text.Statistics.Kurtosis");
	}

	/**
	 * Returns a qualitative description of the skewness based on its test statistic.
	 *
	 * @return a qualitative description of the skewness
	 */
	public String getSkewnessInterpretation()
	{
		String skewnessInterpretation = "";
		if ((fSkewnessZStatistic >= -2.0) && (fSkewnessZStatistic <= +2.0)) {
			skewnessInterpretation = I18NL10N.translate("text.Statistics.SkewnessInconclusive");
		}
		else {
			if (Math.abs(fSkewness) <= +0.5) {
				skewnessInterpretation = I18NL10N.translate("text.Statistics.SkewnessSymmetric");
			}
			else if (fSkewness > +1.0) {
				skewnessInterpretation = I18NL10N.translate("text.Statistics.SkewnessHighlyRightTailed");
			}
			else if (fSkewness > +0.5) {
				skewnessInterpretation = I18NL10N.translate("text.Statistics.SkewnessModeratelyRightTailed");
			}
			else if (fSkewness < -1.0) {
				skewnessInterpretation = I18NL10N.translate("text.Statistics.SkewnessHighlyLefttTailed");
			}
			else if (fSkewness < -0.5) {
				skewnessInterpretation = I18NL10N.translate("text.Statistics.SkewnessModeratelyLeftTailed");
			}
		}

		return skewnessInterpretation;
	}

	/**
	 * Returns a qualitative description of the kurtosis based on its test statistic.
	 *
	 * @return a qualitative description of the kurtosis
	 */
	public String getKurtosisInterpretation()
	{
		String kurtosisInterpretation = "";
		if ((fKurtosisZStatistic >= -2.0) && (fKurtosisZStatistic <= +2.0)) {
			kurtosisInterpretation = I18NL10N.translate("text.Statistics.KurtosisInconclusive");
		}
		else {
			if (Math.abs(fKurtosis) < 1.0) {
				kurtosisInterpretation = I18NL10N.translate("text.Statistics.KurtosisMesokurtic");
			}
			else if (fKurtosisZStatistic > +2.0) {
				kurtosisInterpretation = I18NL10N.translate("text.Statistics.KurtosisLeptokurtic");
			}
			else if (fKurtosisZStatistic < -2.0) {
				kurtosisInterpretation = I18NL10N.translate("text.Statistics.KurtosisPlatykurtic");
			}
		}

		return kurtosisInterpretation;
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 * @param x                            -
	 * @param useOptimalNrOfHistogramBins  -
	 * @param nrOfHistogramBins            -
	 * @param histogramBinRightEdges       -
	 */
	private void setData(double[] x, boolean useOptimalNrOfHistogramBins, int nrOfHistogramBins, double histogramBinRightEdges[])
	{
		clear();

		if (x != null) {
			fN = x.length;

			// deep copy x
			fX = new double[fN];
	    System.arraycopy(x,0,fX,0,fN);
		}
		else {
			fN = 0;
			fX = null;
		}

    fUseOptimalNrOfHistogramBins = useOptimalNrOfHistogramBins;
		fNrOfHistogramBins = nrOfHistogramBins;
		fHistogramBinRightEdges = histogramBinRightEdges;

		analyse();
	}

	/**
	 */
	private void estimateStatistics()
	{
		fMedian = getPercentile(50);
		fInterquartileRange = getPercentile(75) - getPercentile(25);

		// E[X] = sum(Xi * fi)
		double frequency = 1.0 / (double) fN;
		fExpectedValue = 0.0;
		for (int i = 0; i < fN; ++i) {
			fExpectedValue += fX[i];
		}
		fExpectedValue *= frequency;

		// Var[X] = E[X^2] - E[X]^2
		fVariance = 0.0;
		for (int i = 0; i < fN; ++i) {
			fVariance += MathTools.sqr(fX[i]);
		}
		fVariance *= frequency;
		fVariance -= MathTools.sqr(fExpectedValue);
		if (fN > 1) {
			// make the variance an unbiased estimator
			fVariance *= ((double) fN / ((double) fN - 1.0));
		}

		// std[X] = sqrt(Var[X])
		fStandardDeviation = Math.sqrt(fVariance);

		// Skewness[X] = sum((Xi - E[X])^3) / std^3
		// Kurtosis[X] = sum((Xi - E[X])^4) / std^4
		double n = fN;
		double s2 = 0.0; // 2nd moment
		double m3 = 0.0; // 3rd moment
		double m4 = 0.0; // 4th moment
		for (int i = 0; i < fN; ++i) {
			s2 += MathTools.sqr(fX[i] - fExpectedValue); // biased variance estimator
			m3 += MathTools.cube(fX[i] - fExpectedValue);
			m4 += MathTools.quadr(fX[i] - fExpectedValue);
		}
		fSkewness = (m3 / n) / Math.pow(s2 / n,1.5); // population skewness
		fSkewness *= (Math.sqrt(n * (n - 1.0)) / (n - 2.0)); // sample skewness
		double standardErrorOfSkewness = Math.sqrt((6.0 * n * (n - 1.0)) / ((n - 2.0) * (n + 1.0) * (n + 3.0)));
		fSkewnessConfidenceBounds = 2.0 * standardErrorOfSkewness; // symmetric bounds for a 95% confidence interval
		fSkewnessZStatistic = fSkewness / standardErrorOfSkewness; // two-tailed test of skewness != 0 with 5% significance level

		fKurtosis = (m4 / n) / MathTools.sqr(s2 / n); // population kurtosis
		fKurtosis = ((fKurtosis * (n + 1.0)) - (3.0 * (n - 1.0))) * ((n - 1.0) / ((n - 2.0) * (n - 3.0))) + 3.0; // sample kurtosis
		fKurtosis -= 3.0; // leading to zero kurtosis for a normal distribution
		double standardErrorOfKurtosis = 2.0 * standardErrorOfSkewness * Math.sqrt((MathTools.sqr(n) - 1.0) / ((n - 3.0) * (n + 5.0)));
		fKurtosisZStatistic = fKurtosis / standardErrorOfKurtosis; // two-tailed test of kurtosis != 0 with 5% significance level

		if (fStandardDeviation != 0.0) {
			fZScores = new double[fN];
			fOutliers = new boolean[fN];
			for (int i = 0; i < fN; ++i) {
				fZScores[i] = (fX[i] - fExpectedValue) / fStandardDeviation;
				fOutliers[i] = (fZScores[i] > 3.0);
			}
		}
	}

	/**
	 */
	private void estimatePDF()
	{
		// construct histogram

		// define bin edges and centres
		if (fHistogramBinRightEdges == null) {
			if (fUseOptimalNrOfHistogramBins) {
				// apply the Freedman-Diaconis rule for finding the optimal histogram bin width
				double optimalBinWidth = (2.0 * fInterquartileRange) / Math.cbrt(fN);
				fNrOfHistogramBins = ((int) Math.round((fXMax - fXMin) / optimalBinWidth));
			}

			// fail-safe
			if (fNrOfHistogramBins < kMinNrOfHistogramBins) {
				fNrOfHistogramBins = kMinNrOfHistogramBins;
			}

			fHistogramBinWidth = (fXMax - fXMin) / fNrOfHistogramBins;
			fHistogramBinRightEdges = new double[fNrOfHistogramBins];
			for (int i = 0; i < fNrOfHistogramBins; ++i) {
				fHistogramBinRightEdges[i] = fXMin + ((i + 1) * fHistogramBinWidth);
			}
			fHistogramBinCentres = new double[fNrOfHistogramBins];
			for (int i = 0; i < fNrOfHistogramBins; ++i) {
				fHistogramBinCentres[i] = fHistogramBinRightEdges[i] - (fHistogramBinWidth / 2.0);
			}
		}
		else {
			fNrOfHistogramBins = fHistogramBinRightEdges.length;
			fHistogramBinCentres = new double[fNrOfHistogramBins];
			fHistogramBinCentres[0] = Double.NEGATIVE_INFINITY;
			for (int i = 1; i < fNrOfHistogramBins; ++i) {
				fHistogramBinCentres[i] = (fHistogramBinRightEdges[i - 1] + fHistogramBinRightEdges[i]) / 2.0;
			}
		}

		// calculate bin counts
		fHistogramBinCounts = new double[fNrOfHistogramBins];
		int currentBinIndex = 0;
		for (int i = 0; i < fN; ++i) {
			if (fXSorted[i] < fHistogramBinRightEdges[currentBinIndex]) {
				++fHistogramBinCounts[currentBinIndex];
			}
			else {
				while ((currentBinIndex < (fNrOfHistogramBins - 1)) && (fXSorted[i] >= fHistogramBinRightEdges[currentBinIndex])) {
					++currentBinIndex;
				}
				++fHistogramBinCounts[currentBinIndex];
			}
		}

		// convert bin counts to frequencies
		fHistogramBinFrequencies = new double[fNrOfHistogramBins];
		for (int i = 0; i < fNrOfHistogramBins; ++i) {
			fHistogramBinFrequencies[i] = fHistogramBinCounts[i] / (double) fN;
		}
	}
}
