// --------------------------------
// Filename      : DevelopMode.java
// Author        : Sven Maerivoet
// Last modified : 14/03/2011
// Target        : Java VM (1.8)
// --------------------------------

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

package org.sm.smtools.application.util;

/**
 * The <CODE>DevelopMode</CODE> class allows a global specification of
 * an application's status.
 * <P>
 * This class can be used to check whether or not an application should use
 * 'hacks' during its development. These hacks are created by the
 * programmer. They provide convenient backdoors during the development
 * phase (e.g., for skipping intros, about boxes, confirmation dialogs
 * when quitting, ...). Their use is checked against a global flag, in
 * the following manner:
 * <P>
 * <CODE>
 *   if (DevelopMode.isActivated()) {<BR>
 *     ... // perform 'hacks'<BR>
 *   }
 * </CODE>
 * <P>
 * Changing the status is done through direct assignment:
 * <P>
 * <CODE>
 *   DevelopMode.activate();
 * </CODE>
 * <P>
 * If activation is required before any class initialisation, then use a static initialiser as follows:
 * <P>
 * <CODE>
 *   static {<BR>
 *     DevelopMode.activate();<BR>
 *   }
 * </CODE>
 * <P>
 * Note that the default value is <CODE>false</CODE>.
 * <P>
 * <B>Note that this class cannot be subclassed, nor instantiated!</B>
 * 
 * @author  Sven Maerivoet
 * @version 14/03/2011
 */
public final class DevelopMode
{
	// internal datastructures
	private static boolean fModeActivated = false;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation.
	 */
	private DevelopMode()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Activates develop mode.
	 */
	public static void activate()
	{
		fModeActivated = true;
	}

	/**
	 * Deactivates develop mode.
	 */
	public static void deactivate()
	{
		fModeActivated = false;
	}

	/**
	 * Returns whether or not the develop mode is activated.
	 *
	 * @return <CODE>true</CODE> if the develop mode is activated, <CODE>false</CODE> otherwise
	 */
	public static boolean isActivated()
	{
		return fModeActivated;
	}
}
