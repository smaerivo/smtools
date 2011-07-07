// --------------------------------------
// Filename      : RegistryException.java
// Author        : Sven Maerivoet
// Last modified : 28/10/2004
// Target        : Java VM (1.6)
//--------------------------------------

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

import smtools.application.registry.*;

/**
 * Indicates an error involving the {@link Registry}.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 28/10/2004
 * @see     smtools.application.registry.Hive 
 * @see     smtools.application.registry.Registry 
 */
public final class RegistryException extends Exception
{
	// internal datastructures
	private String fRegistryError;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>RegistryException</CODE> object, based on the specified error.
	 *
	 * @param registryError the specified error
	 * @see   smtools.application.registry.Hive
	 * @see   smtools.application.registry.Registry
	 */
	public RegistryException(String registryError)
	{
		fRegistryError = registryError;
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns the registry's error.
	 *
	 * @return the registry's error
	 */
	public String getRegistryError()
	{
		return fRegistryError;
	}
}
