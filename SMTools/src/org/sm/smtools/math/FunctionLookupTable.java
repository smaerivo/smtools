// ----------------------------------------
// Filename      : FunctionLookupTable.java
// Author        : Sven Maerivoet
// Last modified : 08/11/2012
// Target        : Java VM (1.6)
// ----------------------------------------

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

package org.sm.smtools.math;

/**
 * The <CODE>FunctionLookupTable</CODE> class provides a raw container for storing a 1D function's (X,Y) values.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 08/11/2012
 */
public final class FunctionLookupTable
{
	// internal datastructures
	public double[] fX;
	public double[] fY;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an empty <CODE>FunctionLookupTable</CODE> object.
	 */
	public FunctionLookupTable()
	{
		reset();
	}

	/**
	 * Constructs a <CODE>FunctionLookupTable</CODE> object with specified inputs.
	 *
	 * @param x the X-values
	 * @param y the Y-values
	 */
	public FunctionLookupTable(double[] x, double[] y)
	{
		fX = x;
		fY = y;
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Resets the function lookup table.
	 */
	public void reset()
	{
		fX = null;
		fY = null;
	}
}
