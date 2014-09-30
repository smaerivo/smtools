// -----------------------------------
// Filename      : AComplexNumber.java
// Author        : Sven Maerivoet
// Last modified : 30/09/2014
// Target        : Java VM (1.8)
// -----------------------------------

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
 * The <CODE>AComplexNumber&lt;T&gt;</CODE> class provides the abstract base class for complex numbers.
 * 
 * @author  Sven Maerivoet
 * @version 30/09/2014
 */
public abstract class AComplexNumber<T> implements Comparable<AComplexNumber<T>>
{
	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns the real part of this complex number.
	 *
	 * @return the real part of this complex number
	 */
	public abstract T getRealPart();

	/**
	 * Returns the imaginary part of this complex number.
	 *
	 * @return the imaginary part of this complex number
	 */
	public abstract T getImaginaryPart();

	/**
	 * Returns whether or not this complex number is only real.
	 * 
	 * @return whether or not this complex number is only real
	 */
	public abstract boolean isReal();

	/**
	 * Returns whether or not this complex number is only imaginary.
	 * 
	 * @return whether or not this complex number is only imaginary
	 */
	public abstract boolean isImaginary();

	/**
	 * Returns the negative of this complex number.
	 *
	 * @return the negative of this complex number
	 */
	public abstract AComplexNumber<T> negate();

	/**
	 * Add the specified complex number to this one and returns a reference to the result.
	 *
	 * @param c  the complex number to add
	 * @return   a reference to the result
	 */
	public abstract AComplexNumber<T> add(AComplexNumber<T> c);

	/**
	 * Subtracts the specified complex number from this one and returns a reference to the result.
	 *
	 * @param c  the complex number to subtract
	 * @return   a reference to the subtraction
	 */
	public abstract AComplexNumber<T> subtract(AComplexNumber<T> c);

	/**
	 * Multiplies the specified complex number with this one and returns a reference to the result.
	 *
	 * @param c  the complex number to multiply with
	 * @return   a reference to the multiplication
	 */
	public abstract AComplexNumber<T> multiply(AComplexNumber<T> c);

	/**
	 * Returns the multiplicative inverse and returns a reference to the result.
	 *
	 * @return a reference to the multiplicative inverse
	 */
	public abstract AComplexNumber<T> inverse();

	/**
	 * Returns the modulus of this complex number.
	 *
	 * @return the modulus of this complex number
	 */
	public abstract T modulus();

	/**
	 * Returns the squared modulus of this complex number.
	 *
	 * @return the squared modulus of this complex number
	 */
	public abstract T modulusSquared();

	/**
	 * Returns the argument (phase) of this complex number (the result lies between 0 and 2 * Math.PI).
	 *
	 * @return the argument (phase) of this complex number
	 */
	public abstract T argument();

	/**
	 * Returns a reference to the conjugate of this complex number.
	 *
	 * @return a reference to the conjugate
	 */
	public abstract  AComplexNumber<T> conjugate();

	/**
	 * Returns a reference to the reciprocal of this complex number.
	 *
	 * @return a reference to the reciprocal
	 */
	public abstract AComplexNumber<T> reciprocal();

	/**
	 * Divides this complex number by the specified one and returns a reference to the result.
	 *
	 * @param c  the complex number to divide by
	 * @return   a reference to the division
	 */
	public abstract AComplexNumber<T> divide(AComplexNumber<T> c);

	/**
	 * Calculates the absolute value of the square root (i.e., the principal square root) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the absolute value of the square root of this complex number
	 */
	public abstract AComplexNumber<T> sqrt();

	/**
	 * Calculates the cubic root of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cubic root of this complex number
	 */
	public final AComplexNumber<T> cbrt()
	{
		return pow(1.0 / 3.0);
	}

	/**
	 * Returns a reference to the square of this complex number.
	 *
	 * @return a reference to the square of this complex number
	 */
	public abstract AComplexNumber<T> sqr();

	/**
	 * Returns a reference to the cube of this complex number.
	 *
	 * @return a reference to the cube of this complex number
	 */
	public abstract AComplexNumber<T> cube();

	/**
	 * Exponentiates this complex number to a specified real power and returns a reference to the result.
	 *
	 * @param n  the real power for the exponentiation
	 * @return   a reference to this exponentiated complex number 
	 */
	public abstract AComplexNumber<T> pow(double n);

	/**
	 * Exponentiates this complex number to a specified complex power and returns a reference to the result.
	 *
	 * @param n  the complex power for the exponentiation
	 * @return   a reference to this exponentiated complex number 
	 */
	public abstract AComplexNumber<T> pow(AComplexNumber<T> n);

	/**
	 * Takes the natural logarithm of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the natural logarithm of this complex number 
	 */
	public abstract AComplexNumber<T> ln();

	/**
	 * Takes the base 10 logarithm of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the base 10 logarithm of this complex number 
	 */
	public abstract AComplexNumber<T> log();

	/**
	 * Takes a custom logarithm of this complex number and returns a reference to the result.
	 *
	 * @param base  the base of the custom logarithm
	 * @return      a reference to the custom logarithm of this complex number 
	 */
	public abstract AComplexNumber<T> logBase(AComplexNumber<T> base);

	/**
	 * Takes the exponential of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the exponential of this complex number 
	 */
	public abstract AComplexNumber<T> exp();
	
	/**
	 * Takes the cosine of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cosine of this complex number 
	 */
	public abstract AComplexNumber<T> cos();

	/**
	 * Takes the sine of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the sine of this complex number 
	 */
	public abstract AComplexNumber<T> sin();

	/**
	 * Takes the tangens (= sin/cos) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the tangens of this complex number 
	 */
	public final AComplexNumber<T> tan()
	{
		return sin().divide(cos());
	}

	/**
	 * Takes the cotangens (= cos/sin) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cotangens of this complex number 
	 */
	public final AComplexNumber<T> cot()
	{
		return cos().divide(sin());
	}

	/**
	 * Takes the secans (= 1/cos) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the secans of this complex number 
	 */
	public final AComplexNumber<T> sec()
	{
		return cos().reciprocal();
	}

	/**
	 * Takes the cosecans (= 1/sin) of this complex number and returns a reference to the result.
	 *
	 * @return a reference to the cosecans of this complex number 
	 */
	public final AComplexNumber<T> cosec()
	{
		return sin().reciprocal();
	}

	/**
	 * Returns 0 + 0i.
	 *
	 * @return 0 + 0i
	 */
	public abstract AComplexNumber<T> zero();

	/**
	 * Returns 1 + 0i.
	 *
	 * @return 1 + 0i
	 */
	public abstract AComplexNumber<T> one();

	/**
	 * Returns e + 0i.
	 *
	 * @return e + 0i
	 */
	public abstract AComplexNumber<T> e();
	
	/**
	 * Returns 1 + 0i.
	 *
	 * @return 1 + 0i
	 */
	public abstract AComplexNumber<T> i();

	/**
	 * Returns a <CODE>String</CODE> representation of this complex number.
	 *
	 * @return a <CODE>String</CODE> representation of this complex number
	 */
	public abstract String toString();

	/**
	 * Converts from polar to complex form.
	 *
	 * @param modulus   the modulus of the specified polar form
	 * @param argument  the argument of the specified polar form
	 * @return          a complex number corresponding to the specified polar form
	 */
	public abstract AComplexNumber<T> convertPolarToComplex(T modulus, T argument);

	/**
	 * Compares this complex number to another one, via a partial ordering based on their moduli.
	 *
	 * @param c  the complex number to compare to
	 * @return   -1, 0, or 1 as this complex number's modulus is numerically less than, equal to, or greater than that of c
	 */
	public abstract int compareTo(AComplexNumber<T> c);

	/**
	 * Compares this complex number to another one for total equality.
	 *
	 * @param c  the complex number to compare to
	 * @return   <CODE>true</CODE> if both complex numbers are equal, <CODE>false</CODE> otherwise
	 */
	public abstract boolean equals(AComplexNumber<T> c);
}
