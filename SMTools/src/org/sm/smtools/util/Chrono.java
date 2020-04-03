// ------------------------------
// Filename      : Chrono.java
// Author        : Sven Maerivoet
// Last modified : 01/04/2020
// Target        : Java VM (1.8)
// ------------------------------

/**
 * Copyright 2003-2015, 2020 Sven Maerivoet
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

package org.sm.smtools.util;

/**
 * The <CODE>Chrono</CODE> class contains functionality for a simple chronometer.
 * <P>
 * With this class, a single chronometer with an accuracy of milliseconds can be used.
 * <P>
 * There is also a (static) method for suspending application execution for a certain amount of time.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 01/04/2020
 */
public final class Chrono
{
	// internal datastructures
	private long fStartTimeMs;
	private long fStopTimeMs;
	private long fAlreadyElapsedTimeMs;
	private boolean fRunning;

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
	 * Stops the chronometer and resets it's elapsed time to zero.
	 */
	public void reset()
	{
		fRunning = false;
		fStartTimeMs = 0;
		fStopTimeMs = 0;
		fAlreadyElapsedTimeMs = 0;
	}

	/**
	 * Starts/continues the chronometer.
	 */
	public void start()
	{
		fRunning = true;
		fAlreadyElapsedTimeMs += (fStopTimeMs - fStartTimeMs);
		fStartTimeMs = System.currentTimeMillis();
	}

	/**
	 * Stops the chronometer.
	 */
	public void stop()
	{
		fRunning = false;
		fStopTimeMs = System.currentTimeMillis();
	}

	/**
	 * Returns whether or not the chronometer is currently running.
	 * 
	 * @return a <CODE>boolean</CODE> indicating whether or not the chronometer is currently running
	 */
	public boolean isRunning()
	{
		return fRunning;
	}

	/**
	 * Queries this chronometer's elapsed time in milliseconds (excluding all stopped times).
	 *
	 * @return the chronometer's elapsed time in milliseconds
	 */
	public long getElapsedTime()
	{
		if (fRunning) {
			return (fAlreadyElapsedTimeMs + (System.currentTimeMillis() - fStartTimeMs));
		}
		else {
			return (fAlreadyElapsedTimeMs + (fStopTimeMs - fStartTimeMs));
		}
	}

	/**
	 * Suspends application execution for a certain amount of time.
	 * <P>
	 * Note that this is a static method and it should be called as:
	 * <P>
	 * <CODE>Chrono.wait(...);</CODE>
	 *
	 * @param ms  the amount of time to wait, expressed in milliseconds
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
