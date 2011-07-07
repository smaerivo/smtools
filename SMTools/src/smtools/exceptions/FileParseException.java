// ---------------------------------------
// Filename      : FileParseException.java
// Author        : Sven Maerivoet
// Last modified : 18/04/2011
// Target        : Java VM (1.6)
// ---------------------------------------

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

package smtools.exceptions;

import smtools.miscellaneous.*;

/**
 * Indicates that an attempt to parse a line in the file denoted by the
 * specified filename has failed.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 18/04/2011
 */
public final class FileParseException extends Exception
{
	// internal datastructures
	private String fFilename;
	private String fValue;
	private int fLineNr;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>FileParseException</CODE> object, based on the specified filename,
	 * the incorrect value that was read and the number of the line at which the
	 * parse error occurred.
	 *
	 * @param filename the name of the file this exception corresponds to
	 * @param value the incorrect value that was read
	 * @param lineNr the number of the line at which the parse error occurred
	 * @see   TextFileParser
	 */
	public FileParseException(String filename, String value, int lineNr)
	{
		fFilename = filename;
		fValue = value;
		fLineNr = lineNr;
	}

	/**
	 * Constructs a <CODE>FileParseException</CODE>, based on the incorrect value that was read
	 * and the number of the line at which the parse error occurred.
	 *
	 * @param value the incorrect value that was read
	 * @param lineNr the number of the line at which the parse error occurred
	 * @see   TextFileParser
	 */
	public FileParseException(String value, int lineNr)
	{
		fValue = value;
		fLineNr = lineNr;
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
	 * Returns the incorrect value that was read.
	 *
	 * @return the incorrect value that was read
	 */
	public String getValue()
	{
		return fValue;
	}

	/**
	 * Returns the number of the line at which the parse error occurred.
	 *
	 * @return the number of the line at which the parse error occurred
	 */
	public int getLineNr()
	{
		return fLineNr;
	}
}
