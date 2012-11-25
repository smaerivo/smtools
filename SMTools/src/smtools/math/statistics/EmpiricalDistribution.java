// ------------------------------------------
// Filename      : EmpiricalDistribution.java
// Author        : Sven Maerivoet
// Last modified : 16/11/2012
// Target        : Java VM (1.6)
// ------------------------------------------

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

package smtools.math.statistics;

import java.util.*;
import smtools.application.util.*;
import smtools.math.*;

/**
 * The <CODE>EmpiricalDistribution</CODE> class offers a means to calculate the empirical cumulative distribution (CDF) and probability density (PDF) functions, including percentiles.
 * <P>
 * Other available statistics are the expected value, the variance, the standard deviation, the median, and the interquartile range (IQR).
 * <P>
 * The distribution can only contain <CODE>Integer.MAX_VALUE</CODE> samples.
 * <P>
 * Note that a valid {@link I18NL10N} database must be available!
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 16/11/2012
 */
public final class EmpiricalDistribution
{
	// internal datastructures
	private int fN;
	private double[] fX;
	private double fXMin;
	private double fXMax;
	private double fXRange;
	private double fKDEXMin;
	private double fKDEXMax;
	private double fKDEXRange;
	private double[] fCDF;
	private double[] fPercentiles;
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
	 * <UL>
	 *   bin width = 2 * IQR / n^1/3
	 * </UL>
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
	 * @param x the array of values to estimate the empirical distribution for
	 * @param nrOfHistogramBins the user-specified number of histogram bins
	 */
	public EmpiricalDistribution(double[] x, int nrOfHistogramBins)
	{
		this(x,false,nrOfHistogramBins);
	}

	/**
	 * Constructs an <CODE>EmpiricalDistribution</CODE> object for a given array of values and user-specified histogram bin right edges.
	 * 
	 * @param x the array of values to estimate the empirical distribution for
	 * @param histogramBinRightEdges the array of values containing the histogram bin right edges
	 */
	public EmpiricalDistribution(double[] x, double[] histogramBinRightEdges)
	{
		setData(x,false,0,histogramBinRightEdges);
	}

	/**
	 * Private constructor that is invoked in case of a number of histograms was specified or requested.
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
	 */
	public double[] getData()
	{
		return fX;
	}

	/**
	 * Estimates the empirical distribution for a given array of values.
	 * <P>
	 * The Freedman-Diaconis rule is applied for finding the optimal histogram bin width, and consequently the optimal number of histogram bins:
	 * <P>
	 * <UL>
	 *   bin width = 2 * IQR / n^1/3
	 * </UL>
	 * 
	 * @param x the array of values to estimate the empirical distribution for
	 */
	public void setData(double[] x)
	{
		setData(x,true,0,null);
	}

	/**
	 * Estimates the empirical distribution for a given array of values and a user-specified number of histogram bins.
	 * 
	 * @param x the array of values to estimate the empirical distribution for
	 * @param nrOfHistogramBins the user-specified number of histogram bins
	 */
	public void setData(double[] x, int nrOfHistogramBins)
	{
		setData(x,false,nrOfHistogramBins,null);
	}

	/**
	 * Returns the value of the cumulative distribution function (CDF) evaluated at <CODE>x</CODE>.
	 *
	 * @param x the value to evaluate the cumulative distribution function at
	 * @return the value of the cumulative distribution function evaluated at <CODE>x</CODE>
	 */
	public double getCDF(double x)
	{
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
	 * @param percentile the requested percentile (in the interval [0,100])
	 * @return the requested percentile value
	 */
	public double getPercentile(int percentile)
	{
		return fPercentiles[MathTools.clip(percentile,0,100)];
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
	 * Adjusts the probability density function (PDF).
	 * <P>
	 * The Freedman-Diaconis rule is applied for finding the optimal histogram bin width, and consequently the optimal number of histogram bins:
	 * <P>
	 * <UL>
	 *   bin width = 2 * IQR / n^1/3
	 * </UL>
	 */
	public void adjustPDF()
	{
		fUseOptimalNrOfHistogramBins = true;
		estimatePDF();
	}

	/**
	 * Adjusts the probability density function (PDF) using a user-specified number of histogram bins.
	 *
	 * @param nrOfHistogramBins the user-specified number of histogram bins
	 */
	public void adjustPDF(int nrOfHistogramBins)
	{
		fUseOptimalNrOfHistogramBins = false;
		fNrOfHistogramBins = nrOfHistogramBins;
		estimatePDF();
	}

	/**
	 * Calculates the bandwidth for kernel density estimation (KDE) based on Silverman's Rule-of-Thumb.
	 *
	 * @param  kernelType the type of kernel function to use in the calculation
	 * @return an estimation of the bandwidth
	 */
	public double calculateKDEPDFBandwidth(MathTools.EKernelType kernelType)
	{
		// apply Silverman Rule-of-Thumb to calculate the bandwidth
		double hConst = 0.0;
		switch (kernelType) {
			case kRectangular:
				hConst = 1;
				break;
			case kTriangular:
				hConst = 1;
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
		}

		return (getStandardDeviation() * hConst * Math.pow(getN(),-1.0 / 5.0));
	}

	/**
	 * Estimates the probability distribution function (PDF) using a specified kernel function.
	 *
	 * @param  kernelType the type of kernel function to use
	 * @param  bandwidth the bandwidth of the kernel function
	 * @param  nrOfSupportPoints the number of (X,Y) values to use for the smoothened 1D function
	 * @param  minSupport the minimum value for the support
	 * @param  maxSupport the maximum value for the support
	 */
	public void estimateKDEPDF(MathTools.EKernelType kernelType, double bandwidth, int nrOfSupportPoints, double minSupport, double maxSupport)
	{
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
	 * @param histogramBin the histogram bin to lookup the count for
	 * @return the count associated with the specified histogram bin
	 */
	public double getHistogramBinCount(int histogramBin)
	{
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
	 * @param histogramBin the histogram bin to lookup the frequency for
	 * @return the frequency associated with the specified histogram bin
	 */
	public double getHistogramBinFrequency(int histogramBin)
	{
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
	 * @param histogramBin the histogram bin to lookup the centre for
	 * @return the centre of the specified histogram bin
	 */
	public double getHistogramBinCentre(int histogramBin)
	{
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
	 * @param x the value to evaluate the probability density function at
	 * @return the value of the probability density function evaluated at <CODE>x</CODE>
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
	 * @param x the value to evaluate the probability density function at
	 * @return the value of the probability density function evaluated at <CODE>x</CODE>
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
	 * Returns the expected value.
	 * 
	 * @return the expected value
	 */
	public double getExpectedValue()
	{
		return fExpectedValue;
	}

	/**
	 * Alias for the expected value.
	 * 
	 * @return the expected value
	 */
	public double getMean()
	{
		return getExpectedValue();
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
	 * <P>
	 * <UL>
	 *   <LI><B>Positive skew</B>: longer right tail, density mass constrained to the left.</LI>
	 *   <LI><B>Negative skew</B>: longer left tail, density mass constrained to the right.</LI>
	 * </UL>
	 * <P>
	 * Note that the amount of skewness is determined as follows:
	 * <P>
	 * <UL>
	 *   <LI>-0.5 <= skewness <= +0.5: approximately symmetric distribution.</LI> 
	 *   <LI>-1 <= skewness < -0.5, or +0.5 < skewness <= +1: moderately skewed distribution.</LI> 
	 *   <LI>skewness < -1, or skewness > +1: highly skewed distribution.</LI> 
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
	 * <P>
	 * <UL>
	 *   <LI><B><I>Z</I> > +2</B>: population is very likely positively skewed.</LI>
	 *   <LI><B><I>Z</I> < -2</B>: population is very likely negatively skewed.</LI>
	 *   <LI><B>-2 <= <I>Z</I> <= +2</B>: inconclusive (might be symmetric, might be skewed).</LI>
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
	 * <P>
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
	 * <P>
	 * <UL>
	 *   <LI><B><I>Z</I> > +2</B>: population has very likely positive kurtosis (leptokurtic).</LI>
	 *   <LI><B><I>Z</I> < -2</B>: population has very likely negative kurtosis (platykurtic).</LI>
	 *   <LI><B>-2 <= <I>Z</I> <= +2</B>: inconclusive (might be negative, zero, or positive kurtosis).</LI>
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
	 * Returns a descriptive label of the mean (expected value).
	 *
	 * @return a descriptive label of the mean
	 */
	public static String getMeanDescription()
	{
		return I18NL10N.translate("textStatisticsMean");
	}

	/**
	 * Returns a descriptive label of the standard deviation.
	 *
	 * @return a descriptive label of the standard deviation
	 */
	public static String getStandardDeviationDescription()
	{
		return I18NL10N.translate("textStatisticsStandardDeviation");
	}

	/**
	 * Returns a descriptive label of the variance.
	 *
	 * @return a descriptive label of the variance
	 */
	public static String getVarianceDescription()
	{
		return I18NL10N.translate("textStatisticsVariance");
	}

	/**
	 * Returns a descriptive label of the median.
	 *
	 * @return a descriptive label of the median
	 */
	public static String getMedianDescription()
	{
		return I18NL10N.translate("textStatisticsMedian");
	}

	/**
	 * Returns a descriptive label of the interquartile range (IQR).
	 *
	 * @return a descriptive label of the interquartile range (IQR)
	 */
	public static String getInterquartileRangeDescription()
	{
		return I18NL10N.translate("textStatisticsInterquartileRange");
	}

	/**
	 * Returns a descriptive label of a percentile.
	 *
	 * @return a descriptive label of a percentile
	 */
	public static String getPercentileDescription()
	{
		return I18NL10N.translate("textStatisticsPercentile");
	}

	/**
	 * Returns a descriptive label of the skewness.
	 *
	 * @return a descriptive label of the skewness
	 */
	public static String getSkewnessDescription()
	{
		return I18NL10N.translate("textStatisticsSkewness");
	}

	/**
	 * Returns a descriptive label of the kurtosis.
	 *
	 * @return a descriptive label of the kurtosis
	 */
	public static String getKurtosisDescription()
	{
		return I18NL10N.translate("textStatisticsKurtosis");
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
			skewnessInterpretation = I18NL10N.translate("textStatisticsSkewnessInconclusive");
		}
		else {
			if (Math.abs(fSkewness) <= +0.5) {
				skewnessInterpretation = I18NL10N.translate("textStatisticsSkewnessSymmetric");
			}
			else if (fSkewness > +1.0) {
				skewnessInterpretation = I18NL10N.translate("textStatisticsSkewnessHighlyRightTailed");
			}
			else if (fSkewness > +0.5) {
				skewnessInterpretation = I18NL10N.translate("textStatisticsSkewnessModeratelyRightTailed");
			}
			else if (fSkewness < -1.0) {
				skewnessInterpretation = I18NL10N.translate("textStatisticsSkewnessHighlyLefttTailed");
			}
			else if (fSkewness < -0.5) {
				skewnessInterpretation = I18NL10N.translate("textStatisticsSkewnessModeratelyLeftTailed");
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
			kurtosisInterpretation = I18NL10N.translate("textStatisticsKurtosisInconclusive");
		}
		else {
			if (Math.abs(fKurtosis) < 1.0) {
				kurtosisInterpretation = I18NL10N.translate("textStatisticsKurtosisMesokurtic");
			}
			else if (fKurtosisZStatistic > +2.0) {
				kurtosisInterpretation = I18NL10N.translate("textStatisticsKurtosisLeptokurtic");
			}
			else if (fKurtosisZStatistic < -2.0) {
				kurtosisInterpretation = I18NL10N.translate("textStatisticsKurtosisPlatykurtic");
			}
		}

		return kurtosisInterpretation;
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 */
	private void setData(double[] x, boolean useOptimalNrOfHistogramBins, int nrOfHistogramBins, double histogramBinRightEdges[])
	{
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

    estimateDistributions();
	}

	/**
	 */
	private void estimateDistributions()
	{
		// check if data was loaded
		if (fX == null) {
			return;
		}

		// *********************************************************
		// estimate empirical cumulative distribution function (CDF)
		// *********************************************************

		// sort all samples in ascending order
		ArrayList<Double> sortedX = new ArrayList<>();
		for (int i = 0; i < fN; ++i) {
			sortedX.add(fX[i]);
		}
		Collections.sort(sortedX);
		fXMin = fX[0];
		fXMax = fX[0];
		for (int i = 0; i < fN; ++i) {
			// convert back to array
			fX[i] = sortedX.get(i);
			if (fX[i] < fXMin) {
				fXMin = fX[i];
			}
			if (fX[i] > fXMax) {
				fXMax = fX[i];
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
		fPercentiles = new double[101];
		for (int percentile = 0; percentile <= 100; ++percentile) {
			double rank = (((double) percentile / 100.0) * ((double) fN - 1.0)) + 1.0;
			if (rank <= 1.0) {
				fPercentiles[percentile] = fX[0];
			}
			else if (rank >= fN) {
				fPercentiles[percentile] = fX[fN - 1];
			}
			else {
				int k = (int) Math.floor(rank);
				double d = rank - (double) k;
				fPercentiles[percentile] = fX[k - 1] + (d * (fX[k] - fX[k - 1]));
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
				double optimalBinWidth = (2.0 * fInterquartileRange) / MathTools.cubicRoot(fN);
				fNrOfHistogramBins = ((int) Math.round((fXMax - fXMin) / optimalBinWidth));
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
			if (fX[i] < fHistogramBinRightEdges[currentBinIndex]) {
				++fHistogramBinCounts[currentBinIndex];
			}
			else {
				while ((currentBinIndex < (fNrOfHistogramBins - 1)) && (fX[i] >= fHistogramBinRightEdges[currentBinIndex])) {
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
