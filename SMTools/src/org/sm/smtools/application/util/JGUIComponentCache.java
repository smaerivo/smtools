// ---------------------------------------
// Filename      : JGUIComponentCache.java
// Author        : Sven Maerivoet
// Last modified : 20/01/2012
// Target        : Java VM (1.8)
// ---------------------------------------

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

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * The <CODE>JGUIComponentCache</CODE> class provides a mechanism for caching GUI components.
 * <P>
 * When an application based on Java's Swing framework is running, a lot of classes need to be
 * loaded into the Java Virtual Machine's memory. This can be perceived as an annoyance to
 * the user because of the (sometimes long) waiting times.
 * <P>
 * In order to circumvent this, an implementer can choose to preload some GUI components to
 * a cache (using the {@link JGUIComponentCache#addComponent(Component)} method). This way, the
 * waiting time is transferred to the application's startup (instead of during its execution).
 * Later on, when a GUI component is needed, the application can just retrieve it from the
 * cache (using the {@link JGUIComponentCache#retrieveComponent(int)} method).
 * <P>
 * <B><U>Important remark</U></B>
 * <P>
 * It is the caller's responsability to take care of the bookkeeping: a unique identifier is
 * associated with each cached component. And although this identifier is automatically assigned,
 * it has to be maintained by the caller itself in order to be able to retrieve the component at
 * a later instance.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 20/01/2012
 */
public final class JGUIComponentCache
{
	// internal datastructures
	private HashMap<Integer,Component> fGUIComponentCache;
	private int fLastComponentID;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JGUIComponentCache</CODE> object.
	 */
	public JGUIComponentCache()
	{
		clear();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Adds an AWT <CODE>Component</CODE> to the cache.
	 *
	 * @param  component  the AWT <CODE>Component</CODE> to add to the cache
	 * @return            a unique identifier associated with the added component
	 * @see               JGUIComponentCache#retrieveComponent(int)
	 */
	public int addComponent(Component component)
	{
		++fLastComponentID;

		fGUIComponentCache.put(new Integer(fLastComponentID),component);

		return fLastComponentID;
	}

	/**
	 * Retrieves a previously added AWT <CODE>Component</CODE> from the cache.
	 *
	 * @param  componentID  the unique identifier of the AWT <CODE>Component</CODE> to retrieve
	 * @return              the AWT <CODE>Component</CODE> associated with the requested identifier
	 * @see                 JGUIComponentCache#addComponent(Component)
	 */
	public Component retrieveComponent(int componentID)
	{
		return fGUIComponentCache.get(new Integer(componentID));
	}

	/**
	 * Changes the look-and-feel of all components currently in the cache.
	 * <P>
	 * Note that the current look-and-feel is used when changing the components.
	 */
	public void updateComponentTreeUI()
	{
		Iterator<Component> iterator = fGUIComponentCache.values().iterator();

		while (iterator.hasNext()) {
			Component component = iterator.next();

			if (component != null) {
				SwingUtilities.updateComponentTreeUI(component);
			}
		}
	}

	/**
	 * Clears the component cache.
	 */
	public void clear()
	{
		// initialise GUI component cache (with an initial capacity of 10 components)
		// a HashMap is used because we need the capability to store null values
		fGUIComponentCache = new HashMap<>(10);
		fLastComponentID = -1;
	}
}
