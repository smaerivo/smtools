// ----------------------------------
// Filename      : ComplexNumber.java
// Author        : Sven Maerivoet
// Last modified : 12/10/2014
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
 * <B>Note that this class is final cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 12/10/2014
 */
public final class ComplexNumber
{
	/**
	 * The constant 0 + 0i.
	 */
	public final static ComplexNumber kZero = new ComplexNumber();

	/**
	 * The constant 1 + 0i.
	 */
	public final static ComplexNumber kOne = new ComplexNumber(1.0);

	/**
	 * The constant 2 + 0i.
	 */
	public final static ComplexNumber kTwo = new ComplexNumber(2.0);

	/**
	 * The constant 3 + 0i.
	 */
	public final static ComplexNumber kThree = new ComplexNumber(3.0);

	/**
	 * The constant e.
	 */
	public final static ComplexNumber kE = new ComplexNumber(Math.E);

	/**
	 * The constant i.
	 */
	public final static ComplexNumber kI = new ComplexNumber(0.0,1.0);

	// internal datastructures
	private double fRealComponent;
	private double fImaginaryComponent;
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
	 * Constructs a complex number as a real number with a zero imaginary component.
	 *
	 * @param realComponent  the real component
	 */
	public ComplexNumber(double realComponent)
	{
		this(realComponent,0.0);
	}

	/**
	 * Constructs a complex number with a specified value.
	 *
	 * @param realComponent       the real component
	 * @param imaginaryComponent  the imaginary component
	 */
	public ComplexNumber(double realComponent, double imaginaryComponent)
	{
		set(realComponent,imaginaryComponent);
	}

	/**
	 * The <I>copy-constructor</I>.
	 *
	 * @param c  the complex number to copy
	 */
	public ComplexNumber(ComplexNumber c)
	{
		this(c.realComponent(),c.imaginaryComponent());
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Explicitly sets the real and imaginary components of the complex number.
	 * 
	 * @param realComponent       the real component
	 * @param imaginaryComponent  the imaginary component
	 */
	public void set(double realComponent, double imaginaryComponent)
	{
		fRealComponent = realComponent;
		fImaginaryComponent = imaginaryComponent;
		fModulusSquared = (realComponent * realComponent) + (imaginaryComponent * imaginaryComponent);
		fModulus = Math.sqrt(fModulusSquared);
		// convert the argument to an angle between 0 and 2PI
		fArgument = Math.atan2(-fImaginaryComponent,-fRealComponent) + Math.PI;
	}

	/**
	 * Returns the real component of this complex number.
	 *
	 * @return the real component of this complex number
	 */
	public Double realComponent()
	{
		return fRealComponent;
	}

	/**
	 * Returns the imaginary component of this complex number.
	 *
	 * @return the imaginary component of this complex number
	 */
	public Double imaginaryComponent()
	{
		return fImaginaryComponent;
	}

	/**
	 * Returns whether or not this complex number is only real.
	 * 
	 * @return whether or not this complex number is only real
	 */
	public boolean isReal()
	{
		return (fImaginaryComponent == 0.0);
	}

	/**
	 * Returns whether or not this complex number is only imaginary.
	 * 
	 * @return whether or not this complex number is only imaginary
	 */
	public boolean isImaginary()
	{
		return (fRealComponent == 0.0);
	}

	/**
	 * Returns the negative of this complex number.
	 *
	 * @return the negative of this complex number
	 */
	public ComplexNumber negate()
	{
		return (new ComplexNumber(
			-fRealComponent,
			-fImaginaryComponent));
	}

	/**
	 * Returns the complex absolute of this complex number.
	 * <P>
	 * It is defined as abs(real) + i abs(imaginary).
	 *
	 * @return the complex absolute of this complex number
	 */
	public ComplexNumber cabs()
	{
		return (new ComplexNumber(
			Math.abs(fRealComponent),
			Math.abs(fImaginaryComponent)));
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
			fRealComponent + c.realComponent(),
			fImaginaryComponent + c.imaginaryComponent()));
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
			fRealComponent - c.realComponent(),
			fImaginaryComponent - c.imaginaryComponent()));
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
			(fRealComponent * c.realComponent()) - (fImaginaryComponent * c.imaginaryComponent()),
			(fImaginaryComponent * c.realComponent()) + (fRealComponent * c.imaginaryComponent())));
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
				fRealComponent / fModulusSquared,
				-fImaginaryComponent / fModulusSquared));
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
			fRealComponent,
			-fImaginaryComponent));
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
			conjugate.realComponent() / fModulusSquared,
			conjugate.imaginaryComponent() / fModulusSquared));
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
				((fRealComponent * c.realComponent()) + (fImaginaryComponent * c.imaginaryComponent())) / cModulusSquared,
				((fImaginaryComponent * c.realComponent()) - (fRealComponent * c.imaginaryComponent())) / cModulusSquared));
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
			Math.sqrt((fRealComponent + fModulus) / 2.0),
			Math.signum(fImaginaryComponent) * Math.sqrt((-fRealComponent + fModulus) / 2.0)));
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
			(fRealComponent * fRealComponent) - (fImaginaryComponent * fImaginaryComponent),
			2.0 * fRealComponent * fImaginaryComponent));
	}

	/**
	 * Returns a reference to the cube of this complex number.
	 *
	 * @return a reference to the cube of this complex number
	 */
	public ComplexNumber cube()
	{
		return (new ComplexNumber(
			(fRealComponent * fRealComponent * fRealComponent) - (3.0 * fRealComponent * fImaginaryComponent * fImaginaryComponent),
			(3.0 * fRealComponent * fRealComponent * fImaginaryComponent) - (fImaginaryComponent * fImaginaryComponent * fImaginaryComponent)));
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
			Math.exp(fRealComponent) * Math.cos(fImaginaryComponent),
			Math.exp(fRealComponent) * Math.sin(fImaginaryComponent)));
	}
	
	/**
	 * Takes the cosine of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cosine of this complex number 
	 */
	public ComplexNumber cos()
	{
		return 
			(new ComplexNumber(-fImaginaryComponent,fRealComponent)).exp().add(
				(new ComplexNumber(fImaginaryComponent,-fRealComponent)).exp()).divide(new ComplexNumber(2.0,0.0));
	}

	/**
	 * Takes the sine of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the sine of this complex number 
	 */
	public ComplexNumber sin()
	{
		return 
			(new ComplexNumber(-fImaginaryComponent,fRealComponent)).exp().subtract(
				(new ComplexNumber(fImaginaryComponent,-fRealComponent)).exp()).divide(new ComplexNumber(0.0,2.0));
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
		if (fImaginaryComponent >= 0.0) {
			return (fRealComponent + " + " + fImaginaryComponent + "i");
		}
		else {
			return (fRealComponent + " - " + (-fImaginaryComponent) + "i");
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
		return ((fRealComponent == c.realComponent()) && (fImaginaryComponent == c.imaginaryComponent()));
	}

	/**
	 */
	@Override
	public Object clone()
	{
		return (new ComplexNumber(fRealComponent,fImaginaryComponent));
	}

	/**
	 * Forces a partial order on two complex numbers c1 and c2 such that (Re1',Im1') &le; (Re2',Im2').
	 *
	 * @param c1  the first complex number
	 * @param c2  the second complex number
	 */	
	public static void forcePartialOrder(ComplexNumber c1, ComplexNumber c2)
	{
		double c1Re = c1.realComponent();
		double c1Im = c1.imaginaryComponent();
		double c2Re = c2.realComponent();
		double c2Im = c2.imaginaryComponent();

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
