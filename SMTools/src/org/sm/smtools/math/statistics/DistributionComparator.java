// -------------------------------------------
// Filename      : DistributionComparator.java
// Author        : Sven Maerivoet
// Last modified : 01/05/2014
// Target        : Java VM (1.8)
// -------------------------------------------

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

package org.sm.smtools.math.statistics;

import java.util.*;
import org.sm.smtools.math.*;

/**
 * The <CODE>DistributionComparator</CODE> class offers various statistics to compare two empirical distributions based on their sequences of values.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 01/05/2014
 */
public final class DistributionComparator
{
	// internal datastructures
	private EmpiricalDistribution fX;
	private EmpiricalDistribution fY;
	private int fN;
	private double fMAE;
	private double fMSE;
	private double fRMSE;
	private double fSSE;
	private double fMRE;
	private double fRRMSE;
	private double fRMSEP;
	private double fMAXE;
	private double fME;
	private double fMAPE;
	private double fEQC;
	private double fCovariance;
	private double fPearsonCorrelation;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an empty <CODE>DistributionComparator</CODE> object.
	 */
	public DistributionComparator()
	{
	}

	/**
	 * Constructs a <CODE>DistributionComparator</CODE> object with specified <I>X</I> and <I>Y</I> sequences and compares them.
	 * <P>
	 * Note that both sequences should have the same number of values.
	 *
	 * @param x  the <I>X</I> sequence
	 * @param y  the <I>Y</I> sequence
	 */
	public DistributionComparator(EmpiricalDistribution x, EmpiricalDistribution y)
	{
		setData(x,y);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Getter method for the <I>X</I> sequence.
	 *
	 * @return the <I>X</I> sequence
	 */
	public EmpiricalDistribution getXData()
	{
		return fX;
	}

	/**
	 * Getter method for the <I>Y</I> sequence.
	 *
	 * @return the <I>Y</I> sequence
	 */
	public EmpiricalDistribution getYData()
	{
		return fY;
	}

	/**
	 * Loads specific <I>X</I> and <I>Y</I> sequences and compares them.
	 * <P>
	 * Note that both sequences should have the same number of values.
	 *
	 * @param x  the <I>X</I> sequence
	 * @param y  the <I>Y</I> sequence
	 */
	public void setData(EmpiricalDistribution x, EmpiricalDistribution y)
	{
		load(x,y);
		analyse();
	}

	/**
	 * Loads a specific <I>X</I> sequence and compares them.
	 * <P>
	 * Note that it should have the same number of values as the <I>Y</I> sequence.
	 *
	 * @param x  the <I>X</I> sequence
	 */
	public void setXData(EmpiricalDistribution x)
	{
		loadX(x);
		analyse();
	}

	/**
	 * Loads a specific <I>Y</I> sequence and compares them.
	 * <P>
	 * Note that it should have the same number of values as the <I>X</I> sequence.
	 *
	 * @param y  the <I>Y</I> sequence
	 */
	public void setYData(EmpiricalDistribution y)
	{
		loadY(y);
		analyse();
	}

	/**
	 * Getter method for the number of values per sequence.
	 *
	 * @return the number of values per sequence
	 */
	public int getN()
	{
		return fN;
	}

	/**
	 * Getter method for the mean absolute error (MAE).
	 * <P>
	 * MAE = the mean of all absolute differences.
	 * <P>
	 * MAE = (1 / N) * SUM |Xi - Yi|
	 *
	 * @return the MAE
	 */
	public double getMAE()
	{
		return fMAE;
	}

	/**
	 * Getter method for the mean square error (MSE).
	 * <P>
	 * MSE = the mean of all squared differences (more sensitive to large outliers).
	 * <P>
	 * MSE = (1 / N) * SUM (Xi - Yi)^2
	 *
	 * @return the MSE
	 */
	public double getMSE()
	{
		return fMSE;
	}

	/**
	 * Getter method for the root mean square error (RMSE).
	 * <P>
	 * RMSE = SQRT(MSE)
	 *
	 * @return the RMSE
	 */
	public double getRMSE()
	{
		return fRMSE;
	}

	/**
	 * Getter method for the sum of square errors (SSE).
	 * <P>
	 * SSE = SUM (Xi - Yi)^2
	 *
	 * @return the SSE
	 */
	public double getSSE()
	{
		return fSSE;
	}

	/**
	 * Getter method for the mean relative error (MRE).
	 * <P>
	 * MRE = the mean absolute difference in relation to one of the values; becomes very large or infinite if values appear near or equal to 0.
	 * <P>
	 * MRE = (1 / N) * SUM |Xi - Yi|/Xi
	 *
	 * @return the MRE
	 */
	public double getMRE()
	{
		return fMRE;
	}

	/**
	 * Getter method for the relative root mean square error (RRMSE).
	 * <P>
	 * RRMSE becomes very large or infinite if values appear near or equal to 0.
	 * <P>
	 * RRMSE = SQRT[ (1 / N) * SUM [(Xi - Yi)/Xi]^2 ]
	 *
	 * @return the RRMSE
	 */
	public double getRRMSE()
	{
		return fRRMSE;
	}

	/**
	 * Getter method for the root mean square error proportional (RMSEP).
	 * <P>
	 * RMSEP = RMSE divided by the mean of the measures and avoids the problem of infinite values of the MRE.
	 * <P>
	 * RMSEP = SQRT[ N * SUM (Xi - Yi)^2 ] / SUM Xi
	 *
	 * @return the RMSEP
	 */
	public double getRMSEP()
	{
		return fRMSEP;
	}

	/**
	 * Getter method for the maximum error (MAXE).
	 * <P>
	 * MAXE = upper bound of all differences.
	 * <P>
	 * MAXE = MAX{ |Xi - Yi| }
	 *
	 * @return the MAXE
	 */
	public double getMAXE()
	{
		return fMAXE;
	}

	/**
	 * Getter method for the mean error (ME).
	 * <P>
	 * ME makes sense if positive and negative fluctuations equalise each other.
	 * <P>
	 * ME = (1 / N) * SUM (Xi - Yi)
	 *
	 * @return the ME
	 */
	public double getME()
	{
		return fME;
	}

	/**
	 * Getter method for the mean absolute percent error (MAPE).
	 * <P>
	 * MAPE is the MRE in percent.
	 * <P>
	 * MAPE = 100 * MRE
	 *
	 * @return the MAPE
	 */
	public double getMAPE()
	{
		return fMAPE;
	}

	/**
	 * Getter method for the equality coefficient (EQC).
	 * <P>
	 * EQC lies between 0 and 1; total equality results in EQC = 1.
	 * <P>
	 * EQC = 1 - [ SQRT(SUM (Xn - Yn)^2 ) / [SQRT( SUM Xn ) + SQRT( SUM Yn )] ]
	 *
	 * @return the EQC
	 */
	public double getEQC()
	{
		return fEQC;
	}

	/**
	 * Getter method for the covariance.
	 *
	 * @return the covariance
	 */
	public double getCovariance()
	{
		return fCovariance;
	}

	/**
	 * Getter method for Pearson's correlation coefficient.
	 *
	 * @return Pearson's correlation coefficient
	 */
	public double getPearsonCorrelation()
	{
		return fPearsonCorrelation;
	}

	/******************
	 * STATIC METHODS *
	 ******************/

	/**
	 * Peforms a Kolmogorov-Smirnov (KS) test on 2 sequences (they can have different lengths).
	 *
	 * @param x      the <I>X</I> sequence
	 * @param y      the <I>Y</I> sequence
	 * @param alpha  the alpha value for the KS-test
	 * @return       <CODE>true</CODE> when the H0 hypothesis is accepted (i.e. there is not enough evidence to conclude that <I>X</I> and <I>Y</I> are significantly different) 
	 */
	public static boolean performKolmogorovSmirnovTest(EmpiricalDistribution x, EmpiricalDistribution y, double alpha)
	{
		int n1 = x.getN();
		int n2 = y.getN();
		int n = n1 + n2;

		// construct new cumulative distribution functions
		double[] binEdges = new double[n];
		System.arraycopy(x.getData(),0,binEdges,0,n1);
		System.arraycopy(y.getData(),0,binEdges,n1,n2);
		Arrays.sort(binEdges);
		EmpiricalDistribution ed1 = new EmpiricalDistribution(x.getData(),binEdges);
		EmpiricalDistribution ed2 = new EmpiricalDistribution(y.getData(),binEdges);

		double[] binCounts1 = ed1.getHistogramBinCounts();
		double[] cdf1 = new double[binCounts1.length];
		double sum1 = 0.0;
		for (int binIndex = 0; binIndex < binCounts1.length; ++binIndex) {
			cdf1[binIndex] = binCounts1[binIndex] + sum1;
			sum1 += binCounts1[binIndex];
		}
		for (int binIndex = 0; binIndex < binCounts1.length; ++binIndex) {
			cdf1[binIndex] /= sum1;
		}

		double[] binCounts2 = ed2.getHistogramBinCounts();
		double[] cdf2 = new double[binCounts2.length];
		double sum2 = 0.0;
		for (int binIndex = 0; binIndex < n; ++binIndex) {
			cdf2[binIndex] = binCounts2[binIndex] + sum2;
			sum2 += binCounts2[binIndex];
		}
		for (int binIndex = 0; binIndex < n; ++binIndex) {
			cdf2[binIndex] /= sum2;
		}

		// calculate the K-S statistic
		double ksStatistic = Double.NEGATIVE_INFINITY;
		for (int binIndex = 0; binIndex < n; ++binIndex) {
			double delta = Math.abs(cdf1[binIndex] - cdf2[binIndex]);
			if (delta > ksStatistic) {
				ksStatistic = delta;
			}
		}

		// compute the asymptotic P-value approximation
		double nAdjust = ((double) n1 * (double) n2) / ((double) n1 + (double) n2);
		double lambda = (Math.sqrt((double) nAdjust) + 0.12 + (0.11 / Math.sqrt((double) nAdjust))) * ksStatistic;
		if (lambda < 0.0) {
			lambda = 0.0;
		}

		double pValue = 0.0;
		for (int j = 1; j < 101; ++j) {
			pValue += (Math.pow(-1.0,(double) j - 1.0) * Math.exp(-2.0 * MathTools.sqr(lambda) * MathTools.sqr((double) j)));
		}
		pValue *= 2.0;
		if (pValue < 0.0) {
			pValue = 0.0;
		}
		else if (pValue > 1.0) {
			pValue = 1.0;
		}

		// test the H0 hypothesis based on alpha and the p-value
		boolean H0 = false;
		if (alpha >= pValue) {
			H0 = true;
		}

		return H0;
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 * @param x  -
	 * @param y  -
	 */
	private void load(EmpiricalDistribution x, EmpiricalDistribution y)
	{
		if ((x.getN() == 0) || (y.getN() != x.getN())) {
			return;
		}

		fN = x.getN();
		fX = x;
		fY = y;
	}

	/**
	 * @param x  -
	 */
	private void loadX(EmpiricalDistribution x)
	{
		if ((x.getN() == 0) || (x.getN() != fN)) {
			return;
		}

		fX = x;
	}

	/**
	 * @param y  -
	 */
	private void loadY(EmpiricalDistribution y)
	{
		if ((y.getN() == 0) || (y.getN() != fN)) {
			return;
		}

		fY = y;
	}

	/**
	 */
	private void analyse()
	{
		fMAE = 0.0;
		fMSE = 0.0;
		fRMSE = 0.0;
		fSSE = 0.0;
		fMRE = 0.0;
		fRRMSE = 0.0;
		fRMSEP = 0.0;
		double fRMSEPdenominator = 0.0;
		fMAXE = fX.getData()[0] - fY.getData()[0];
		fME = 0.0;
		fMAPE = 0.0;
		fEQC = 0.0;
		double fEQCdenominatorTermX = 0.0;
		double fEQCdenominatorTermY = 0.0;

		for (int n = 0; n < fN; ++n) {
			double xn = fX.getData()[n];
			double yn = fY.getData()[n];
			double delta = xn - yn;
			fMAE += Math.abs(delta);
			fMSE += MathTools.sqr(delta);
			fSSE += MathTools.sqr(delta);
			if (xn != 0.0) {
				fMRE += (Math.abs(delta) / xn);
				fRRMSE += MathTools.sqr(delta / xn);
			}
			fRMSEP += MathTools.sqr(delta);
			fRMSEPdenominator += xn;
			if (Math.abs(delta) > fMAXE) {
				fMAXE = Math.abs(delta);
			}
			fME += delta;
			fEQC += MathTools.sqr(delta);
			fEQCdenominatorTermX += MathTools.sqr(xn);
			fEQCdenominatorTermY += MathTools.sqr(yn);
		}

		fMAE /= ((double) fN);
		fMSE /= ((double) fN);
		fRMSE = Math.sqrt(fMSE);
		fMRE /= ((double) fN);
		fRRMSE = Math.sqrt(fRRMSE / ((double) fN));
		if (fRMSEPdenominator != 0.0) {
			fRMSEP = Math.sqrt(fRMSEP * ((double) fN)) / fRMSEPdenominator;
		}
		else {
			fRMSEP = 0.0;
		}
		fME /= ((double) fN);
		fMAPE = fMRE * 100.0;
		double fEQCdenominatorTerm = Math.sqrt(fEQCdenominatorTermX) + Math.sqrt(fEQCdenominatorTermY);
		if (fEQCdenominatorTerm != 0.0) {
			fEQC = 1.0 - (Math.sqrt(fEQC) / fEQCdenominatorTerm);
		}
		else {
			fEQC = 0.0;
		}

		fCovariance = 0.0;
		for (int n = 0; n < fN; ++n) {
			fCovariance += ((fX.getData()[n] - fX.getMean()) * (fY.getData()[n] - fY.getMean()));
		}
		fCovariance /= (fN - 1.0);

		fPearsonCorrelation = fCovariance / (fX.getStandardDeviation() * fY.getStandardDeviation());
	}
}
