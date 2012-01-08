// ----------------------------------------------
// Filename      : FileDoesNotExistException.java
// Author        : Sven Maerivoet
// Last modified : 22/01/2004
// Target        : Java VM (1.6)
// ----------------------------------------------

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

package smtools.exceptions;

/**
 * Indicates that an attempt to open the file denoted by the specified filename has failed.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 22/01/2004
 */
public final class FileDoesNotExistException extends Exception
{
	// internal datastructures
	private String fFilename;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>FileDoesNotExistException</CODE> object, based on the specified filename.
	 *
	 * @param filename the name of the file this exception corresponds to
	 */
	public FileDoesNotExistException(String filename)
	{
		fFilename = filename;
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
}
