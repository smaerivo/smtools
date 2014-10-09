// ----------------------------------
// Filename      : ComplexNumber.java
// Author        : Sven Maerivoet
// Last modified : 10/10/2014
// Target        : Java VM (1.8)
// ----------------------------------

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

package org.sm.smtools.math.complex;

/**
 * The <CODE>ComplexNumber</CODE> class provides mathematical operations on complex numbers.
 * <P>
 * The class has a <U>partial ordering</U> imposed, which is measured via the modulus.
 * <P>
 * <B>Note that this class is immutable and cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 10/10/2014
 */
public final class ComplexNumber
{
	/**
	 * The constant zero.
	 */
	public final static ComplexNumber kZero = new ComplexNumber();

	/**
	 * The constant one.
	 */
	public final static ComplexNumber kOne = new ComplexNumber(1.0);

	/**
	 * The constant e.
	 */
	public final static ComplexNumber kE = new ComplexNumber(Math.E);

	/**
	 * The constant i.
	 */
	public final static ComplexNumber kI = new ComplexNumber(0.0,1.0);

	// internal datastructures
	private double fRealPart;
	private double fImaginaryPart;
	private double fModulusSquared;
	private double fModulus;
	private double fArgument;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a complex number equal to 0 + 0i.
	 */
	public ComplexNumber()
	{
		this(0.0,0.0);
	}

	/**
	 * Constructs a complex number as a real number with a zero imaginary part.
	 *
	 * @param realPart  the real part
	 */
	public ComplexNumber(double realPart)
	{
		this(realPart,0.0);
	}

	/**
	 * Constructs a complex number with a specified value.
	 *
	 * @param realPart       the real part
	 * @param imaginaryPart  the imaginaryPart
	 */
	public ComplexNumber(double realPart, double imaginaryPart)
	{
		set(realPart,imaginaryPart);
	}

	/**
	 * The <I>copy-constructor</I>.
	 *
	 * @param c  the complex number to copy
	 */
	public ComplexNumber(ComplexNumber c)
	{
		this(c.realPart(),c.imaginaryPart());
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Explicitly sets the real and imaginary parts of the complex number.
	 * 
	 * @param realPart       the real part
	 * @param imaginaryPart  the imaginaryPart
	 */
	public void set(double realPart, double imaginaryPart)
	{
		fRealPart = realPart;
		fImaginaryPart = imaginaryPart;
		fModulusSquared = (realPart * realPart) + (imaginaryPart * imaginaryPart);
		fModulus = Math.sqrt(fModulusSquared);
		fArgument = Math.atan2(fImaginaryPart,fRealPart);
	}

	/**
	 * Returns the real part of this complex number.
	 *
	 * @return the real part of this complex number
	 */
	public Double realPart()
	{
		return fRealPart;
	}

	/**
	 * Returns the imaginary part of this complex number.
	 *
	 * @return the imaginary part of this complex number
	 */
	public Double imaginaryPart()
	{
		return fImaginaryPart;
	}

	/**
	 * Returns whether or not this complex number is only real.
	 * 
	 * @return whether or not this complex number is only real
	 */
	public boolean isReal()
	{
		return (fImaginaryPart == 0.0);
	}

	/**
	 * Returns whether or not this complex number is only imaginary.
	 * 
	 * @return whether or not this complex number is only imaginary
	 */
	public boolean isImaginary()
	{
		return (fRealPart == 0.0);
	}

	/**
	 * Returns the negative of this complex number.
	 *
	 * @return the negative of this complex number
	 */
	public ComplexNumber negate()
	{
		return (new ComplexNumber(
			-fRealPart,
			-fImaginaryPart));
	}

	/**
	 * Add the specified complex number to this one and returns a reference to the result.
	 *
	 * @param c  the complex number to add
	 * @return   a reference to the result
	 */
	public ComplexNumber add(ComplexNumber c)
	{
		return (new ComplexNumber(
			fRealPart + c.realPart(),
			fImaginaryPart + c.imaginaryPart()));
	}

	/**
	 * Subtracts the specified complex number from this one and returns a reference to the result.
	 *
	 * @param c  the complex number to subtract
	 * @return   a reference to the subtraction
	 */
	public ComplexNumber subtract(ComplexNumber c)
	{
		return (new ComplexNumber(
			fRealPart - c.realPart(),
			fImaginaryPart - c.imaginaryPart()));
	}

	/**
	 * Multiplies the specified complex number with this one and returns a reference to the result.
	 *
	 * @param c  the complex number to multiply with
	 * @return   a reference to the multiplication
	 */
	public ComplexNumber multiply(ComplexNumber c)
	{
		return (new ComplexNumber(
			(fRealPart * c.realPart()) - (fImaginaryPart * c.imaginaryPart()),
			(fImaginaryPart * c.realPart()) + (fRealPart * c.imaginaryPart())));
	}

	/**
	 * Returns the multiplicative inverse and returns a reference to the result.
	 *
	 * @return a reference to the multiplicative inverse
	 */
	public ComplexNumber inverse()
	{
		if (fModulusSquared == 0.0) {
			return (new ComplexNumber());
		}
		else {
			return (new ComplexNumber(
				fRealPart / fModulusSquared,
				-fImaginaryPart / fModulusSquared));
		}
	}

	/**
	 * Returns the modulus of this complex number.
	 *
	 * @return the modulus of this complex number
	 */
	public Double modulus()
	{
		return fModulus;
	}

	/**
	 * Returns the squared modulus of this complex number.
	 *
	 * @return the squared modulus of this complex number
	 */
	public Double modulusSquared()
	{
		return fModulusSquared;
	}

	/**
	 * Returns the argument (phase) of this complex number (the result lies between 0 and 2 * Math.PI).
	 *
	 * @return the argument (phase) of this complex number
	 */
	public Double argument()
	{
		return fArgument;
	}

	/**
	 * Returns a reference to the conjugate of this complex number.
	 *
	 * @return a reference to the conjugate
	 */
	public ComplexNumber conjugate()
	{
		return (new ComplexNumber(
			fRealPart,
			-fImaginaryPart));
	}

	/**
	 * Returns a reference to the reciprocal of this complex number.
	 *
	 * @return a reference to the reciprocal
	 */
	public ComplexNumber reciprocal()
	{
		ComplexNumber conjugate = conjugate();
		return (new ComplexNumber(
			conjugate.realPart() / fModulusSquared,
			conjugate.imaginaryPart() / fModulusSquared));
	}

	/**
	 * Divides this complex number by the specified one and returns a reference to the result.
	 *
	 * @param c  the complex number to divide by
	 * @return   a reference to the division
	 */
	public ComplexNumber divide(ComplexNumber c)
	{
		double cModulusSquared = c.modulusSquared();
		if (cModulusSquared == 0.0) {
			return (new ComplexNumber());
		}
		else {
			return (new ComplexNumber(
				((fRealPart * c.realPart()) + (fImaginaryPart * c.imaginaryPart())) / cModulusSquared,
				((fImaginaryPart * c.realPart()) - (fRealPart * c.imaginaryPart())) / cModulusSquared));
		}
	}

	/**
	 * Calculates the absolute value of the square root (i.e., the principal square root) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the absolute value of the square root of this complex number
	 */
	public ComplexNumber sqrt()
	{
		return (new ComplexNumber(
			Math.sqrt((fRealPart + fModulus) / 2.0),
			Math.signum(fImaginaryPart) * Math.sqrt((-fRealPart + fModulus) / 2.0)));
	}

	/**
	 * Calculates the cubic root of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cubic root of this complex number
	 */
	public final ComplexNumber cbrt()
	{
		return pow(1.0 / 3.0);
	}

	/**
	 * Returns a reference to the square of this complex number.
	 *
	 * @return a reference to the square of this complex number
	 */
	public ComplexNumber sqr()
	{
		return (new ComplexNumber(
			(fRealPart * fRealPart) - (fImaginaryPart * fImaginaryPart),
			2.0 * fRealPart * fImaginaryPart));
	}

	/**
	 * Returns a reference to the cube of this complex number.
	 *
	 * @return a reference to the cube of this complex number
	 */
	public ComplexNumber cube()
	{
		return (new ComplexNumber(
			(fRealPart * fRealPart * fRealPart) - (3.0 * fRealPart * fImaginaryPart * fImaginaryPart),
			(3.0 * fRealPart * fRealPart * fImaginaryPart) - (fImaginaryPart * fImaginaryPart * fImaginaryPart)));
	}

	/**
	 * Exponentiates this complex number to a specified real power and returns a reference to the result.
	 * <P>
	 * If the modulus is 0, then the result is 0 + 0i.
	 *
	 * @param n  the real power for the exponentiation
	 * @return   a reference to this exponentiated complex number 
	 */
	public ComplexNumber pow(double n)
	{
		if (fModulus == 0.0) {
			return (new ComplexNumber());
		}
		else {
			return convertPolarToComplex(
				Math.pow(fModulus,n),
				n * fArgument);
		}
	}

	/**
	 * Exponentiates this complex number to a specified complex power and returns a reference to the result.
	 * <P>
	 * If the modulus is 0, then the result is 0 + 0i.
	 *
	 * @param n  the complex power for the exponentiation
	 * @return   a reference to this exponentiated complex number 
	 */
	public ComplexNumber pow(ComplexNumber n)
	{
		if (fModulus == 0.0) {
			return (new ComplexNumber());
		}
		else {
			return ln().multiply(n).exp();
		}
	}

	/**
	 * Takes the natural logarithm of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the natural logarithm of this complex number 
	 */
	public ComplexNumber ln()
	{
		return new ComplexNumber(
			Math.log(fModulus),
			fArgument);
	}

	/**
	 * Takes the base 10 logarithm of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the base 10 logarithm of this complex number 
	 */
	public ComplexNumber log()
	{
		return logBase(new ComplexNumber(10.0));
	}

	/**
	 * Takes a custom logarithm of this complex number and returns a reference to the result.
	 *
	 * @param base  the base of the custom logarithm
	 * @return      a reference to the custom logarithm of this complex number 
	 */
	public ComplexNumber logBase(ComplexNumber base)
	{
		return (ln().divide(base.ln()));
	}

	/**
	 * Takes the exponential of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the exponential of this complex number 
	 */
	public ComplexNumber exp()
	{
		return (new ComplexNumber(
			Math.exp(fRealPart) * Math.cos(fImaginaryPart),
			Math.exp(fRealPart) * Math.sin(fImaginaryPart)));
	}
	
	/**
	 * Takes the cosine of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cosine of this complex number 
	 */
	public ComplexNumber cos()
	{
		return 
			(new ComplexNumber(-fImaginaryPart,fRealPart)).exp().add(
				(new ComplexNumber(fImaginaryPart,-fRealPart)).exp()).divide(new ComplexNumber(2.0,0.0));
	}

	/**
	 * Takes the sine of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the sine of this complex number 
	 */
	public ComplexNumber sin()
	{
		return 
			(new ComplexNumber(-fImaginaryPart,fRealPart)).exp().subtract(
				(new ComplexNumber(fImaginaryPart,-fRealPart)).exp()).divide(new ComplexNumber(0.0,2.0));
	}

	/**
	 * Takes the tangens (= sin/cos) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the tangens of this complex number 
	 */
	public final ComplexNumber tan()
	{
		return sin().divide(cos());
	}

	/**
	 * Takes the cotangens (= cos/sin) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cotangens of this complex number 
	 */
	public final ComplexNumber cot()
	{
		return cos().divide(sin());
	}

	/**
	 * Takes the secans (= 1/cos) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the secans of this complex number 
	 */
	public final ComplexNumber sec()
	{
		return cos().reciprocal();
	}

	/**
	 * Takes the cosecans (= 1/sin) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cosecans of this complex number 
	 */
	public final ComplexNumber cosec()
	{
		return sin().reciprocal();
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this complex number.
	 *
	 * @return a <CODE>String</CODE> representation of this complex number
	 */
	@Override
	public String toString()
	{
		if (fImaginaryPart >= 0.0) {
			return (fRealPart + " + " + fImaginaryPart + "i");
		}
		else {
			return (fRealPart + " - " + (-fImaginaryPart) + "i");
		}
	}

	/**
	 * Converts from polar to complex form.
	 *
	 * @param modulus   the modulus of the specified polar form
	 * @param argument  the argument of the specified polar form
	 * @return          a complex number corresponding to the specified polar form
	 */
	public ComplexNumber convertPolarToComplex(Double modulus, Double argument)
	{
		return (new ComplexNumber(
			modulus * Math.cos(argument),
			modulus * Math.sin(argument)));
	}

	/**
	 * Compares this complex number to another one, via a partial ordering based on their moduli.
	 *
	 * @param c  the complex number to compare to
	 * @return   -1, 0, or 1 as this complex number's modulus is numerically less than, equal to, or greater than that of c
	 */
	public int compareTo(ComplexNumber c)
	{
		double cModulus = c.modulus();
		if (fModulus < cModulus) {
			return -1;
		}
		if (fModulus > cModulus) {
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
	public boolean equals(ComplexNumber c)
	{
		return ((fRealPart == c.realPart()) && (fImaginaryPart == c.imaginaryPart()));
	}

	/**
	 */
	@Override
	public Object clone()
	{
		return (new ComplexNumber(fRealPart,fImaginaryPart));
	}

	/**
	 * Forces a partial order on two complex numbers c1 and c2 such that (Re1',Im1') &le; (Re2',Im2').
	 *
	 * @param c1  the first complex number
	 * @param c2  the second complex number
	 */	
	public static void forcePartialOrder(ComplexNumber c1, ComplexNumber c2)
	{
		double c1Re = c1.realPart();
		double c1Im = c1.imaginaryPart();
		double c2Re = c2.realPart();
		double c2Im = c2.imaginaryPart();

		if (c1Re > c2Re) {
			double temp = c1Re;
			c1Re = c2Re;
			c2Re = temp;
		}

		if (c1Im > c2Im) {
			double temp = c1Im;
			c1Im = c2Im;
			c2Im = temp;
		}

		c1.set(c1Re,c1Im);
		c2.set(c2Re,c2Im);
	}
}
