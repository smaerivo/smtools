// ------------------------------
// Filename      : ScrapBook.java
// Author        : Sven Maerivoet
// Last modified : N/A
// Target        : Java VM (1.8)
// ------------------------------

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

package org.sm.smtools.scrapbook;

import org.apache.log4j.*;

/**
 * @author  Sven Maerivoet
 * @version N/A
 */
public class ScrapBook
{
	// access point to the Log4j logging facility
	private static final Logger kLogger = Logger.getLogger(ScrapBook.class.getName());

	/*************************
	 * STATIC INITIALISATION *
	 *************************/

	static {
		PropertyConfigurator.configure("log4j.properties");
	}

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 */
	public ScrapBook()
	{
		kLogger.info("ScrapBook::ctor()");
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * The application's entry point.
	 * 
	 * @param argv  the command-line parameters
	 */
	@SuppressWarnings("unused")
	public static void main(String[] argv)
	{
		ScrapBook scrapBook = new ScrapBook();
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/
}