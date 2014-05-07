// -------------------------------------
// Filename      : MemoryStatistics.java
// Author        : Sven Maerivoet
// Last modified : 03/11/2012
// Target        : Java VM (1.8)
// -------------------------------------

/**
 * Copyright 2003-2014 Sven Maerivoet
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

package org.sm.smtools.application.util;

/**
 * The <CODE>MemoryStatistics</CODE> class allows gathering of some Java VM memory statistics.
 * <P>
 * <B>Note that this class cannot be subclassed, nor instantiated!</B>
 * 
 * @author  Sven Maerivoet
 * @version 03/11/2012
 */
public final class MemoryStatistics
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation.
	 */
	private MemoryStatistics()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns the total available memory that the JVM can have access to.
	 *
	 * @return the total available memory (in bytes) that the JVM can have access to
	 */
	public static long getTotalMemory()
	{
		return Runtime.getRuntime().maxMemory();
	}

	/**
	 * Returns the amount of memory currently used by the JVM.
	 *
	 * @return the amount of memory (in bytes) currently used by the JVM
	 */
	public static long getUsedMemory()
	{
		System.gc();
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
	}

	/**
	 * Returns the amount of free memory for the application.
	 *
	 * @return the amount of free memory (in bytes) for the application
	 */
	public static long getFreeMemory()
	{
		System.gc();
		return (Runtime.getRuntime().freeMemory() + (getTotalMemory() - Runtime.getRuntime().totalMemory()));
	}

	/**
	 * Returns the number of processors for the JVM.
	 *
	 * @return the number of processors for the JVM
	 */
	public static int getNrOfProcessors()
	{
		return Runtime.getRuntime().availableProcessors();
	}
}
