// ------------------------------
// Filename      : Extrema.java
// Author        : Sven Maerivoet
// Last modified : 08/11/2012
// Target        : Java VM (1.6)
// ------------------------------

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

import java.util.*;

/**
 * The <CODE>Extrema</CODE> class provides a container for storing local minima and maxima indices and values.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 08/11/2012
 */
public final class Extrema
{
	// internal datastructures
	private ArrayList<Extremum> fLocalMinima;
	private ArrayList<Extremum> fLocalMaxima;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an empty <CODE>Extrema</CODE> object.
	 */
	public Extrema()
	{
		reset();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns the number of local minima available.
	 * 
	 * @return the number of local minima available
	 */
	public int getNrOfLocalMinima()
	{
		return fLocalMinima.size();
	}

	/**
	 * Returns the number of local maxima available.
	 * 
	 * @return the number of local maxima available
	 */
	public int getNrOfLocalMaxima()
	{
		return fLocalMaxima.size();
	}

	/**
	 * Adds a local minimum.
	 * 
	 * @param index  the index of the local minimum to set
	 * @param value  the value of the local minimum to set
	 */
	public void addLocalMinimum(int index, double value)
	{
		fLocalMinima.add(new Extremum(index,value));
	}

	/**
	 * Adds a local maximum.
	 * 
	 * @param index  the index of the local maximum to set
	 * @param value  the value of the local maximum to set
	 */
	public void addLocalMaximum(int index, double value)
	{
		fLocalMaxima.add(new Extremum(index,value));
	}

	/**
	 * Returns a local minimum.
	 *
	 * @param sequenceNumber  the sequence number of the requested local minimum
	 * @return                the requested local minimum
	 */
	public Extremum getLocalMinimum(int sequenceNumber)
	{
		return fLocalMinima.get(sequenceNumber);
	}

	/**
	 * Returns a local maximum.
	 *
	 * @param sequenceNumber  the sequence number of the requested local maximum
	 * @return                the requested local maximum
	 */
	public Extremum getLocalMaximum(int sequenceNumber)
	{
		return fLocalMaxima.get(sequenceNumber);
	}

	/**
	 * Resets all local extrema.
	 */
	public void reset()
	{
		fLocalMinima = new ArrayList<Extremum>(); 
		fLocalMaxima = new ArrayList<Extremum>(); 
	}
}
