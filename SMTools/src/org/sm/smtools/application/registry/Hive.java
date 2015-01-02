// ------------------------------
// Filename      : Hive.java
// Author        : Sven Maerivoet
// Last modified : 27/06/2004
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

package org.sm.smtools.application.registry;

import java.io.*;

/**
 * The <CODE>Hive</CODE> marker interface is used for containers in the global registry.
 * <P>
 * In order to associate a group of objects with the global registry, a class implementing
 * the <CODE>Hive</CODE> interface should be created. This class can then contain the objects
 * themselves. Special care should be taken that <I>all</I> its fields are serialisable, as the
 * global registry (as well as the hives) can be loaded from and stored to file.
 * 
 * @author  Sven Maerivoet
 * @version 27/06/2004
 * @see     Registry
 */
public interface Hive extends Serializable
{
	// this is just a marker interface
}
