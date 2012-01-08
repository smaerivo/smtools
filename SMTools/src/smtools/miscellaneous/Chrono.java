// ------------------------------
// Filename      : Chrono.java
// Author        : Sven Maerivoet
// Last modified : 26/01/2004
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

package smtools.miscellaneous;

/**
 * The <CODE>Chrono</CODE> class contains functionality for a simple chronometer.
 * <P>
 * With this class, a single chronometer with an accuracy of milliseconds can be used.
 * As opposed to the behavior of most chronometers, this one runs constantly.
 * Stopping the chronometer is not possible, instead its elapsed time can be queried;
 * to stop-and-start the chronometer a reset can be performed.
 * <P>
 * Note that it also contains a (static) method for suspending application execution for
 * a certain amount of time.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 26/01/2004
 */
public final class Chrono
{
	// internal datastructures
	private long fStartTimeMs;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a chronometer and resets it.
	 */
	public Chrono()
	{
		reset();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Resets this chronometer's elapsed time to zero.
	 */
	public void reset()
	{
		fStartTimeMs = System.currentTimeMillis();
	}

	/**
	 * Queries this chronometer's elapsed time.
	 *
	 * @return the chronometer's elapsed time in milliseconds
	 */
	public int getElapsedTimeInMilliseconds()
	{
		return ((int) (System.currentTimeMillis() - fStartTimeMs));
	}

	/**
	 * Suspends application execution for a certain amount of time.
	 * <P>
	 * Note that this is a static method and it should be called as:
	 * <P>
	 * <UL>
	 *   <CODE>Chrono.wait(...);</CODE>
	 * </UL>
	 *
	 * @param ms the amount of time to wait, expressed in milliseconds
	 */
	public static void wait(int ms)
	{
		try {
			Thread.sleep(ms,0);
		}
		catch (InterruptedException exc) {
		}
	}
}
