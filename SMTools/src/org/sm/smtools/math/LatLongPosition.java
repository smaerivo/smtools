// ------------------------------------
// Filename      : LatLongPosition.java
// Author        : Sven Maerivoet
// Last modified : 04/09/2019
// Target        : Java VM (1.8)
// ------------------------------------

/**
 * Copyright 2003-2019 Sven Maerivoet
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
 * The <CODE>LatLongPosition</CODE> class provides a data container for a (latitude,longitude) position.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 04/09/2019
 */
public final class LatLongPosition
{
	// internal datastructures
	private double fLatitude;
	private double fLongitude;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>LatLongPosition</CODE> object with the specified (latitude,longitude) coordinates.
	 * 
	 * @param latitude   -
	 * @param longitude  -
	 */
	public LatLongPosition(double latitude, double longitude)
	{
		fLatitude = latitude;
		fLongitude = longitude;
	}

	/**
	 * Constructs a <CODE>LatLongPosition</CODE> object with zero-coordinates.
	 */
	public LatLongPosition()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Gets the position's latitude.
	 * 
	 * @return the position's latitude
	 */
	public double getLatitude()
	{
		return fLatitude;
	}

	/**
	 * Sets the position's latitude.
	 * 
	 * @param latitude  -
	 */
	public void setLatitude(double latitude)
	{
		fLatitude = latitude;
	}

	/**
	 * Gets the position's longitude.
	 * 
	 * @return the position's longitude
	 */
	public double getLongitude()
	{
		return fLongitude;
	}

	/**
	 * Sets the position's longitude.
	 * 
	 * @param longitude  -
	 */
	public void setLongitude(double longitude)
	{
		fLongitude = longitude;
	}
}
