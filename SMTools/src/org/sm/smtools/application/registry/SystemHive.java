// -------------------------------
// Filename      : SystemHive.java
// Author        : Sven Maerivoet
// Last modified : 28/10/2004
// Target        : Java VM (1.8)
// -------------------------------

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

package org.sm.smtools.application.registry;

import java.util.*;

/**
 * The <CODE>SystemHive</CODE> class provides a concrete implementation of the {@link Hive} interface.
 * <P>
 * Different hives can be created in the global registry. This class provides a concrete implementation
 * of a hive, in which objects can be stored to a <CODE>Hashtable</CODE>.  This <CODE>Hashtable</CODE> is
 * parameterised, so that an <CODE>Object</CODE> is referenced/looked up as a <CODE>String</CODE>. 
 * 
 * @author  Sven Maerivoet
 * @version 28/10/2004
 * @see     Hive
 * @see     Registry
 */
public class SystemHive implements Hive
{
	// version number of serialisation
	private static final long serialVersionUID = 1L;

	/**
	 * The parameterized contents of the hive <CODE>Object</CODE>s are referenced/looked up
	 * as <CODE>String</CODE>s).
	 */
	public Hashtable<String,Object> fContents;
}
