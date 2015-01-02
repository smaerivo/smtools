// ---------------------------------
// Filename      : NumberFilter.java
// Author        : Sven Maerivoet
// Last modified : 05/09/2014
// Target        : Java VM (1.8)
// ---------------------------------

/**
 * Copyright 2003-2015 Sven Maerivoet
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

package org.sm.smtools.swing.util;

/**
 * The <CODE>NumberFilter</CODE> class provides methods to validate the input in a {@link JNumberInputField}.
 * <P>
 * <B>Note that this is an abstract class.</B>
 *
 * @author  Sven Maerivoet
 * @version 05/09/2014
 */
public abstract class ANumberFilter
{
	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Checks whether or not an <CODE>int</CODE> value is valid.
	 * <P>
	 * A subclass must override this method, thereby specifying its own acceptable
	 * range for the <CODE>int</CODE> value. For example, to accept only strictly
	 * positive numbers, the following code can be used:
	 * <P>
	 * <CODE>
	 *   return (i &gt; 0);
	 * </CODE>
	 *
	 * @param i  the <CODE>int</CODE> value to check
	 * @return   <CODE>true</CODE> when the specified <CODE>int</CODE> value is accepted, <CODE>false</CODE> otherwise
	 */
	public abstract boolean validateInteger(int i);

	/**
	 * Checks whether or not a <CODE>double</CODE> value is valid.
	 * <P>
	 * A subclass must override this method, thereby specifying its own acceptable
	 * range for the <CODE>double</CODE> value. For example, to accept only strictly
	 * positive numbers, the following code can be used:
	 * <P>
	 * <CODE>
	 *   return (d &gt; 0.0);
	 * </CODE>
	 *
	 * @param d  the <CODE>double</CODE> value to check
	 * @return   <CODE>true</CODE> when the specified <CODE>double</CODE> value is accepted, <CODE>false</CODE> otherwise
	 */
	public abstract boolean validateDouble(double d);
}
