// ---------------------------------------
// Filename      : FileWriteException.java
// Author        : Sven Maerivoet
// Last modified : 22/01/2004
// Target        : Java VM (1.8)
// ---------------------------------------

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

package org.sm.smtools.exceptions;

/**
 * Indicates that an attempt to write a value to the file denoted by the
 * specified filename has failed.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 22/01/2004
 */
public final class FileWriteException extends Exception
{
	// internal datastructures
	private String fFilename;
	private String fValue;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>FileWriteException</CODE> object, based on the specified filename
	 * and the value that should have been written.
	 *
	 * @param filename  the name of the file this exception corresponds to
	 * @param value     the value that should have been written
	 */
	public FileWriteException(String filename, String value)
	{
		fFilename = filename;
		fValue = value;
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns the name of the file this exception corresponds to.
	 *
	 * @return the name of the file this exception corresponds to
	 */
	public String getFilename()
	{
		return fFilename;
	}

	/**
	 * Returns the value that should have been written.
	 *
	 * @return the value that should have been written
	 */
	public String getValue()
	{
		return fValue;
	}
}
