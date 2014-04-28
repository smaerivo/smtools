// --------------------------------------------
// Filename      : DateTimeFormatException.java
// Author        : Sven Maerivoet
// Last modified : 22/01/2004
// Target        : Java VM (1.6)
// --------------------------------------------

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

package org.sm.smtools.exceptions;

import org.sm.smtools.miscellaneous.*;

/**
 * Indicates a malformed {@link java.util.Date} or {@link TimeStamp} object.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 22/01/2004
 */
public final class DateTimeFormatException extends Exception
{
	// internal datastructures
	private String fDateTimeString;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>DateTimeFormatException</CODE> object, based on the string
	 * representation of a malformed <CODE>DateStamp</CODE> or <CODE>TimeStamp</CODE> object.
	 *
	 * @param dateTimeString the string representation of a malformed <CODE>DateStamp</CODE> or <CODE>TimeStamp</CODE> object
	 * @see   DateStamp
	 * @see   TimeStamp
	 */
	public DateTimeFormatException(String dateTimeString)
	{
		fDateTimeString = dateTimeString;
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns the string representation of this exception's malformed <CODE>DateStamp</CODE> or <CODE>TimeStamp</CODE> object.
	 *
	 * @return the string representation of the malformed <CODE>DateStamp</CODE> or <CODE>TimeStamp</CODE> object
	 */
	public String getDateTimeString()
	{
		return fDateTimeString;
	}
}
