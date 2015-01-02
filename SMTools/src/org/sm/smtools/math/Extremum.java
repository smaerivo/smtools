// ------------------------------
// Filename      : Extremum.java
// Author        : Sven Maerivoet
// Last modified : 08/11/2012
// Target        : Java VM (1.8)
// ------------------------------

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

package org.sm.smtools.math;

/**
 * The <CODE>Extremum</CODE> class provides a container for storing an extremum's index and value.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 08/11/2012
 */
public final class Extremum
{
	// internal datastructures
	private int fIndex;
	private double fValue;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an empty <CODE>Extremum</CODE> object.
	 */
	public Extremum()
	{
		reset();
	}

	/**
	 * Constructs an empty <CODE>Extremum</CODE> object.
	 *
	 * @param index  the extremum's index
	 * @param value  the extremum's value
	 */
	public Extremum(int index, double value)
	{
		set(index,value);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Getter method for the extremum's index.
	 * 
	 * @return the extremum's index
	 */
	public int getIndex()
	{
		return fIndex;
	}

	/**
	 * Getter method for the extremum's value.
	 * 
	 * @return the extremum's value
	 */
	public double getValue()
	{
		return fValue;
	}

	/**
	 * General setter method for the extremum.
	 *
	 * @param index  the extremum's index
	 * @param value  the extremum's value
	 */
	public void set(int index, double value)
	{
		setIndex(index);
		setValue(value);
	}

	/**
	 * Setter method for the extremum's index.
	 *
	 * @param index  the extremum's index
	 */
	public void setIndex(int index)
	{
		fIndex = index;
	}

	/**
	 * Setter method for the extremum's value.
	 *
	 * @param value  the extremum's value
	 */
	public void setValue(double value)
	{
		fValue = value;
	}

	/**
	 * Resets the extremum.
	 */
	public void reset()
	{
		fIndex = 0;
		fValue = 0.0;
	}
}
