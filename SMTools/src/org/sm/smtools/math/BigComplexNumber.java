// -------------------------------------
// Filename      : BigComplexNumber.java
// Author        : Sven Maerivoet
// Last modified : 06/05/2014
// Target        : Java VM (1.8)
// -------------------------------------

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

import java.math.*;
import org.nevec.rjm.*;

/**
 * The <CODE>BigComplexNumber</CODE> class provides mathematical operations on big complex numbers
 * (it uses Richard J. Mathar's slightly modified <CODE>BigDecimalMath</CODE> framework for specific functions
 * and switches all internal calculations to the DECIMAL128 FP-format by default).
 * <P>
 * The class has a <U>partial ordering</U> imposed, which is measured via the modulus.
 * <P>
 * <B>Note that this class is immutable and cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 06/05/2014
 */
public final class BigComplexNumber implements Comparable<BigComplexNumber>
{
	/**
	 * Constant for specifying 32-bits precision.
	 */
	public static final MathContext kPrecision32bits = MathContext.DECIMAL32;

	/**
	 * Constant for specifying 64-bits precision.
	 */
	public static final MathContext kPrecision64bits = MathContext.DECIMAL64;

	/**
	 * Constant for specifying 128-bits precision.
	 */
	public static final MathContext kPrecision128bits = MathContext.DECIMAL128;

	// BigDecimal constants
	private static final BigDecimal kBD0 = BigDecimal.ZERO;
	private static final BigDecimal kBD1 = BigDecimal.ONE;
	private static final BigDecimal kBD2 = kBD1.add(kBD1);
	private static final BigDecimal kBD3 = kBD2.add(kBD1);
	private static final BigDecimal kBD10 = BigDecimal.TEN;

	// precision and rounding controls
	private final RoundingMode kDefaultRoundingMode = RoundingMode.HALF_EVEN;
	private static MathContext kDefaultMathContext;
	private static int kDefaultPrecision;

	// internal datastructures
	private final BigDecimal fRealPart;
	private final BigDecimal fImaginaryPart;
	private final BigDecimal fModulusSquared;
	private final BigDecimal fModulus;
	private final BigDecimal fArgument;
	
	/*************************
	 * STATIC INITIALISATION *
	 *************************/

	static {
		setPrecision(kPrecision128bits);
	}

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a complex number equal to 0 + 0i.
	 */
	public BigComplexNumber()
	{
		this(kBD0);
	}

	/**
	 * Constructs a complex number as a real number with a zero imaginary part.
	 *
	 * @param realPart  the real part
	 */
	public BigComplexNumber(BigDecimal realPart)
	{
		this(realPart,kBD0);
	}

	/**
	 * Constructs a complex number with a specified value.
	 *
	 * @param realPart       the real part
	 * @param imaginaryPart  the imaginaryPart
	 */
	public BigComplexNumber(BigDecimal realPart, BigDecimal imaginaryPart)
	{
		// set default scales on input data
		fRealPart = realPart.setScale(kDefaultPrecision,kDefaultRoundingMode);
		fImaginaryPart = imaginaryPart.setScale(kDefaultPrecision,kDefaultRoundingMode);
		fModulusSquared = (fRealPart.multiply(fRealPart)).add(fImaginaryPart.multiply(fImaginaryPart)).setScale(kDefaultPrecision,kDefaultRoundingMode);
		fModulus = BigDecimalMath.sqrt(fModulusSquared,kDefaultMathContext);
		if (fRealPart.compareTo(kBD0) == 0) {
			if (fImaginaryPart.compareTo(kBD0) > 0) {
				fArgument = new BigDecimal(Math.PI / 2.0);
			}
			else {
				fArgument = new BigDecimal(3.0 * Math.PI / 2.0);
			}
		}
		else {
			BigDecimal argument = BigDecimalMath.atan(fImaginaryPart.divide(fRealPart,kDefaultMathContext).setScale(kDefaultPrecision,kDefaultRoundingMode));
			// adjust so the argument lies between [0,2 * pi]
			if (fRealPart.compareTo(kBD0) < 0) {
				fArgument = argument.add(BigDecimalMath.pi(kDefaultMathContext));
			}
			else if ((fRealPart.compareTo(kBD0) >= 0) && (fImaginaryPart.compareTo(kBD0) < 0)) {
				fArgument = argument.add(BigDecimalMath.pi(kDefaultMathContext).multiply(kBD2));
			}
			else {
				fArgument = argument;
			}
		}
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the precision to use for all subsequent calculations.
	 *
	 * @param precision  the precision to use
	 */
	public static void setPrecision(MathContext precision)
	{
		kDefaultMathContext = precision;
		kDefaultPrecision = kDefaultMathContext.getPrecision();
	}

	/**
	 * Returns the real part of this complex number.
	 *
	 * @return the real part of this complex number
	 */
	public BigDecimal getRealPart()
	{
		return fRealPart;
	}

	/**
	 * Returns the imaginary part of this complex number.
	 *
	 * @return the imaginary part of this complex number
	 */
	public BigDecimal getImaginaryPart()
	{
		return fImaginaryPart;
	}

	/**
	 * Returns the negative of this complex number.
	 *
	 * @return the negative of this complex number
	 */
	public BigComplexNumber negate()
	{
		return (new BigComplexNumber(
			fRealPart.negate(),
			fImaginaryPart.negate()));
	}

	/**
	 * Add the specified complex number to this one and returns a reference to the result.
	 *
	 * @param c  the complex number to add
	 * @return   a reference to the result
	 */
	public BigComplexNumber add(BigComplexNumber c)
	{
		return (new BigComplexNumber(
			fRealPart.add(c.getRealPart()),
			fImaginaryPart.add(c.getImaginaryPart())));
	}

	/**
	 * Subtracts the specified complex number from this one and returns a reference to the result.
	 *
	 * @param c  the complex number to subtract
	 * @return   a reference to the subtraction
	 */
	public BigComplexNumber subtract(BigComplexNumber c)
	{
		return (new BigComplexNumber(
			fRealPart.subtract(c.getRealPart()),
			fImaginaryPart.subtract(c.getImaginaryPart())));
	}

	/**
	 * Multiplies the specified complex number with this one and returns a reference to the result.
	 *
	 * @param c  the complex number to multiply with
	 * @return   a reference to the multiplication
	 */
	public BigComplexNumber multiply(BigComplexNumber c)
	{
		return (new BigComplexNumber(
			(fRealPart.multiply(c.getRealPart())).subtract(fImaginaryPart.multiply(c.getImaginaryPart())),
			(fImaginaryPart.multiply(c.getRealPart())).add(fRealPart.multiply(c.getImaginaryPart()))));
	}

	/**
	 * Returns the modulus of this complex number.
	 *
	 * @return the modulus of this complex number
	 */
	public BigDecimal modulus()
	{
		return fModulus;
	}

	/**
	 * Returns the squared modulus of this complex number.
	 *
	 * @return the squared modulus of this complex number
	 */
	public BigDecimal modulusSquared()
	{
		return fModulusSquared;
	}

	/**
	 * Returns the argument (phase) of this complex number (the result lies between 0 and 2 * Math.PI).
	 *
	 * @return the argument (phase) of this complex number
	 */
	public BigDecimal argument()
	{
		return fArgument;
	}

	/**
	 * Returns a reference to the conjugate of this complex number.
	 *
	 * @return a reference to the conjugate
	 */
	public BigComplexNumber conjugate()
	{
		return (new BigComplexNumber(
			fRealPart,
			fImaginaryPart.negate()));
	}

	/**
	 * Returns a reference to the reciprocal of this complex number.
	 *
	 * @return a reference to the reciprocal
	 */
	public BigComplexNumber reciprocal()
	{
		BigComplexNumber conjugate = conjugate();
		return (new BigComplexNumber(
			conjugate.getRealPart().divide(fModulusSquared,kDefaultMathContext),
			conjugate.getImaginaryPart().divide(fModulusSquared,kDefaultMathContext)));
	}

	/**
	 * Divides this complex number by the specified one and returns a reference to the result.
	 *
	 * @param c  the complex number to divide by
	 * @return   a reference to the division
	 */
	public BigComplexNumber divide(BigComplexNumber c)
	{
		BigDecimal cModulusSquared = c.modulusSquared();
		if (cModulusSquared.compareTo(kBD0) == 0) {
			return (new BigComplexNumber());
		}
		else {
			return (new BigComplexNumber(
				((fRealPart.multiply(c.getRealPart())).add(fImaginaryPart.multiply(c.getImaginaryPart()))).divide(cModulusSquared,kDefaultMathContext),
				((fImaginaryPart.multiply(c.getRealPart())).subtract(fRealPart.multiply(c.getImaginaryPart()))).divide(cModulusSquared,kDefaultMathContext)));
		}
	}

	/**
	 * Calculates the absolute value of the square root (i.e., the principal square root) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the absolute value of the square root of this complex number
	 */
	public BigComplexNumber sqrt()
	{
		BigDecimal signum = kBD1;
		if (fImaginaryPart.signum() < 0) {
			signum = kBD1.negate();
		}
		return (new BigComplexNumber(
			BigDecimalMath.sqrt(fRealPart.add(fModulus).divide(kBD2,kDefaultMathContext),kDefaultMathContext),
			signum.multiply(BigDecimalMath.sqrt(fRealPart.negate().add(fModulus).divide(kBD2,kDefaultMathContext),kDefaultMathContext))));
	}

	/**
	 * Calculates the cubic root of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cubic root of this complex number
	 */
	public BigComplexNumber cbrt()
	{
		return pow(1.0 / 3.0);
	}

	/**
	 * Returns a reference to the square of this complex number.
	 *
	 * @return a reference to the square of this complex number
	 */
	public BigComplexNumber sqr()
	{
		return (new BigComplexNumber(
			fRealPart.multiply(fRealPart).subtract(fImaginaryPart.multiply(fImaginaryPart)),
			kBD2.multiply(fRealPart).multiply(fImaginaryPart)));
	}

	/**
	 * Returns a reference to the cube of this complex number.
	 *
	 * @return a reference to the cube of this complex number
	 */
	public BigComplexNumber cube()
	{
		return (new BigComplexNumber(
			fRealPart.multiply(fRealPart).multiply(fRealPart).subtract(kBD3.multiply(fRealPart).multiply(fImaginaryPart).multiply(fImaginaryPart)),
			kBD3.multiply(fRealPart).multiply(fRealPart).multiply(fImaginaryPart).subtract(fImaginaryPart.multiply(fImaginaryPart).multiply(fImaginaryPart))));
	}

	/**
	 * Exponentiates this complex number to a specified power and returns a reference to the result.
	 *
	 * @param n  the power for the exponentiation
	 * @return   a reference to this exponentiated complex number 
	 */
	public BigComplexNumber pow(double n)
	{
		BigDecimal nPower = (new BigDecimal(n)).setScale(kDefaultPrecision,kDefaultRoundingMode);
		return convertPolarToComplex(
			BigDecimalMath.pow(fModulus,nPower),
			nPower.multiply(fArgument));
	}

	/**
	 * Takes the natural logarithm of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the natural logarithm of this complex number 
	 */
	public BigComplexNumber ln()
	{
		return new BigComplexNumber(
			BigDecimalMath.log(fModulus),
			fArgument);
	}

	/**
	 * Takes the base 10 logarithm of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the base 10 logarithm of this complex number 
	 */
	public BigComplexNumber log()
	{
		return logBase(new BigComplexNumber(kBD10));
	}

	/**
	 * Takes a custom logarithm of this complex number and returns a reference to the result.
	 *
	 * @param base  the base of the custom logarithm
	 * @return      a reference to the custom logarithm of this complex number 
	 */
	public BigComplexNumber logBase(BigComplexNumber base)
	{
		return (ln().divide(base.ln()));
	}

	/**
	 * Takes the exponential of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the exponential of this complex number 
	 */
	public BigComplexNumber exp()
	{
		return (new BigComplexNumber(
			BigDecimalMath.exp(fRealPart).multiply(BigDecimalMath.cos(fImaginaryPart)),
			BigDecimalMath.exp(fRealPart).multiply(BigDecimalMath.sin(fImaginaryPart))));
	}
	
	/**
	 * Takes the cosine of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cosine of this complex number 
	 */
	public BigComplexNumber cos()
	{
		return 
			(new BigComplexNumber(fImaginaryPart.negate(),fRealPart)).exp().add(
				(new BigComplexNumber(fImaginaryPart,fRealPart.negate())).exp()).divide(new BigComplexNumber(kBD2));
	}

	/**
	 * Takes the sine of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the sine of this complex number 
	 */
	public BigComplexNumber sin()
	{
		return 
			(new BigComplexNumber(fImaginaryPart.negate(),fRealPart)).exp().subtract(
				(new BigComplexNumber(fImaginaryPart,fRealPart.negate())).exp()).divide(new BigComplexNumber(kBD0,kBD2));
	}

	/**
	 * Takes the tangens (= sin/cos) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the tangens of this complex number 
	 */
	public BigComplexNumber tan()
	{
		return sin().divide(cos());
	}

	/**
	 * Takes the cotangens (= cos/sin) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cotangens of this complex number 
	 */
	public BigComplexNumber cot()
	{
		return cos().divide(sin());
	}

	/**
	 * Takes the secans (= 1/cos) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the secans of this complex number 
	 */
	public BigComplexNumber sec()
	{
		return cos().reciprocal();
	}

	/**
	 * Takes the cosecans (= 1/sin) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cosecans of this complex number 
	 */
	public BigComplexNumber cosec()
	{
		return sin().reciprocal();
	}

	/**
	 * Returns 0 + 0i.
	 *
	 * @return 0 + 0i
	 */
	public BigComplexNumber zero()
	{
		return (new BigComplexNumber());
	}

	/**
	 * Returns 1 + 0i.
	 *
	 * @return 1 + 0i
	 */
	public BigComplexNumber one()
	{
		return (new BigComplexNumber(kBD1.setScale(kDefaultPrecision,kDefaultRoundingMode)));
	}

	/**
	 * Returns e + 0i.
	 *
	 * @return e + 0i
	 */
	public BigComplexNumber e()
	{
		return (new BigComplexNumber(BigDecimalMath.exp(kDefaultMathContext)));
	}
	
	/**
	 * Returns 0 + i.
	 *
	 * @return 0 + i
	 */
	public BigComplexNumber i()
	{
		return (new BigComplexNumber(kBD0,kBD1));
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this complex number.
	 *
	 * @return a <CODE>String</CODE> representation of this complex number
	 */
	public String toString()
	{
		if (fImaginaryPart.compareTo(kBD0) >= 0) {
			return (fRealPart + " + " + fImaginaryPart + "i");
		}
		else {
			return (fRealPart + " - " + (fImaginaryPart.negate()) + "i");
		}
	}

	/**
	 * Helper method for converting from polar to complex form.
	 *
	 * @param  modulus   the modulus
	 * @param  argument  the argument
	 * @return           a complex number corresponding to the specified polar form
	 */
	public static BigComplexNumber convertPolarToComplex(BigDecimal modulus, BigDecimal argument)
	{
		return (new BigComplexNumber(
			modulus.multiply(BigDecimalMath.cos(argument)),
			modulus.multiply(BigDecimalMath.sin(argument))));
	}

	/**
	 * Compares this complex number to another one, via a partial ordering based on their moduli.
	 *
	 * @param c  the complex number to compare to
	 * @return   -1, 0, or 1 as this complex number's modulus is numerically less than, equal to, or greater than that of c
	 */
	public int compareTo(BigComplexNumber c)
	{
		BigDecimal cModulus = c.modulus();
		if (fModulus.compareTo(cModulus) < 0) {
			return -1;
		}
		if (fModulus.compareTo(cModulus) > 0) {
			return +1;
		}
		else {
			return 0;
		}
	}

	/**
	 * Compares this complex number to another one for total equality.
	 *
	 * @param c  the complex number to compare to
	 * @return   <CODE>true</CODE> if both complex numbers are equal, <CODE>false</CODE> otherwise
	 */
	public boolean equals(BigComplexNumber c)
	{
		if (c instanceof BigComplexNumber) {
			return ((fRealPart.equals(c.getRealPart())) && (fImaginaryPart.equals(c.getImaginaryPart())));
		}
		else {
			return false;
		}
	}
}

/*
  *************************************************************************************
  TESTING CODE FOR COMPARING operations on ComplexNumber with those on BigComplexNumber
  *************************************************************************************

	public ScrapBook()
	{
		kLogger.info("ScrapBook::ctor()");

		int nrOfSteps = 360;
		for (int i = 0; i < nrOfSteps; ++i) {
			double arg = ((2.0 * Math.PI) / nrOfSteps) * i;
			ComplexNumber loc = ComplexNumber.convertPolarToComplex(Math.random(),arg);
			double a = loc.getRealPart();
			double b = loc.getImaginaryPart();

			ComplexNumber c = new ComplexNumber(a,b);
			BigComplexNumber bc = new BigComplexNumber(new BigDecimal(a),new BigDecimal(b));

			System.out.println("(x,y) = (" + a + "," + b + ")");
			System.out.println("   m(c)      = " + c.modulus());
			System.out.println("   m(bc)     = " + bc.modulus());
			System.out.println("   m^2(c)    = " + c.modulusSquared());
			System.out.println("   m^2(bc)   = " + bc.modulusSquared());
			System.out.println("   arg(c)    = " + MathTools.rad2deg(c.argument()));
			System.out.println("   arg(bc)   = " + MathTools.rad2deg(bc.argument().doubleValue()));
			System.out.println("   recip(c)  = " + c.reciprocal());
			System.out.println("   recip(bc) = " + bc.reciprocal());
			System.out.println("   1/c       = " + (new ComplexNumber()).one().divide(c));
			System.out.println("   1/bc      = " + (new BigComplexNumber()).one().divide(bc));
			System.out.println("   sqrt(c)   = " + c.sqrt());
			System.out.println("   sqrt(bc)  = " + bc.sqrt());
			System.out.println("   sqr(c)    = " + c.sqr());
			System.out.println("   sqr(bc)   = " + bc.sqr());
			System.out.println("   cbrt(c)   = " + c.cbrt());
			System.out.println("   cbrt(bc)  = " + bc.cbrt());
			System.out.println("   cube(c)   = " + c.cube());
			System.out.println("   cube(bc)  = " + bc.cube());
			System.out.println("   ln(c)     = " + c.ln());
			System.out.println("   ln(bc)    = " + bc.ln());
			System.out.println("   log(c)    = " + c.log());
			System.out.println("   log(bc)   = " + bc.log());
			System.out.println("   exp(c)    = " + c.exp());
			System.out.println("   exp(bc)   = " + bc.exp());
			System.out.println("   cos(c)    = " + c.cos());
			System.out.println("   cos(bc)   = " + bc.cos());
			System.out.println("   sin(c)    = " + c.sin());
			System.out.println("   sin(bc)   = " + bc.sin());
			System.out.println("   tan(c)    = " + c.tan());
			System.out.println("   tan(bc)   = " + bc.tan());
			System.out.println("   cot(c)    = " + c.cot());
			System.out.println("   cot(bc)   = " + bc.cot());
			System.out.println("   sec(c)    = " + c.sec());
			System.out.println("   sec(bc)   = " + bc.sec());
			System.out.println("   cosec(c)  = " + c.cosec());
			System.out.println("   cosec(bc) = " + bc.cosec());
			System.out.println();
		}		
	}
*/
