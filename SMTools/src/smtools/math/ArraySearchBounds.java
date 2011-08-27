// --------------------------------------
// Filename      : ArraySearchBounds.java
// Author        : Sven Maerivoet
// Last modified : 25/08/2011
// Target        : Java VM (1.6)
// --------------------------------------

/**
 * Copyright 2003-2011 Sven Maerivoet
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

package smtools.math;

/**
 * The <CODE>ArraySearchBounds</CODE> class provides a container for storing a lower and upper array index.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 25/08/2011
 */
public final class ArraySearchBounds
{
	// internal datastructures
	private int fLowerBound;
	private int fUpperBound;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an <CODE>ArraySearchBounds</CODE> object with both bounds set to 0.
	 */
	public ArraySearchBounds()
	{
		reset();
	}

	/**
	 * Constructs an <CODE>ArraySearchBounds</CODE> object with both bounds specified.
	 *
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 */
	public ArraySearchBounds(int lowerBound, int upperBound)
	{
		setLowerBound(lowerBound);
		setUpperBound(upperBound);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Getter method.
	 */
	public int getLowerBound()
	{
		return fLowerBound;
	}

	/**
	 * Setter method.
	 */
	public void setLowerBound(int lowerBound)
	{
		fLowerBound = lowerBound;
	}

	/**
	 * Getter method.
	 */
	public int getUpperBound()
	{
		return fUpperBound;
	}

	/**
	 * Setter method.
	 */
	public void setUpperBound(int upperBound)
	{
		fUpperBound = upperBound;
	}

	/**
	 * Resets both boundary indices to 0.
	 */
	public void reset()
	{
		fLowerBound = 0;
		fUpperBound = 0;
	}
}
