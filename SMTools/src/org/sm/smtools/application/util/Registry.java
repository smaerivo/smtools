// ------------------------------
// Filename      : Registry.java
// Author        : Sven Maerivoet
// Last modified : 05/01/2015
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

package org.sm.smtools.application.util;

import java.io.*;
import java.util.*;
import org.sm.smtools.exceptions.*;

/**
 * The <CODE>Registry</CODE> class provides a mechanism for accessing (persistent) global variables.  
 * <P>
 * The <CODE>Registry</CODE> class is actually a <B>singleton</B> instance, and a local reference to it
 * should be obtained as follows:
 * <P>
 * <CODE>Registry registry = Registry.getInstance();</CODE>
 * <P>
 * The registry contains serialisable objects that can be stored and retrieved via a <CODE>String</CODE> key using the
 * {@link Registry#addObject(String,Object)} and {@link Registry#getObject(String)} methods.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 05/01/2015
 */
public final class Registry
{
	// internal datastructures
	private static Registry fInstance;
	public Hashtable<String,Object> fContents;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent arbitrary instantiation (we only allow a singleton instance)
	 */ 
	private Registry()
	{
		clear();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns the singleton instance of the <CODE>Registry</CODE> class.
	 * 
	 * @return the singleton instance of the <CODE>Registry</CODE> class
	 */
	public synchronized static Registry getInstance()
	{
		// return a singleton instance
		if (fInstance == null) {
			fInstance = new Registry();
		}

		return fInstance;
	}

	/**
	 * Clears the registry.
	 */
	public synchronized void clear()
	{
		fContents = new Hashtable<String,Object>();
	}

	/**
	 * Adds an object to the registry.
	 *
	 * @param name    the name of the object
	 * @param object  a reference to the object
	 */
	public synchronized void addObject(String name, Object object)
	{
		fContents.put(name,object);
	}

	/**
	 * Returns an object from the registry.
	 *
	 * @param name  the name of the object
	 * @return      a reference to the object
	 */
	public synchronized Object getObject(String name)
	{
		return fContents.get(name);
	}

	/**
	 * Loads all objects in the registry from a file using deserialisation.
	 *
	 * @param  filename           the name of the file to load the registry from
	 * @throws RegistryException  if an error occurred during the deserialisation process
	 */
	@SuppressWarnings("unchecked")
	public synchronized void load(String filename) throws RegistryException
	{
		// prevent resource leaks
		try (FileInputStream fileInputStream = new FileInputStream(filename); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

			// deserialise registry
			fContents = (Hashtable<String,Object>) objectInputStream.readObject();

			objectInputStream.close();
			fileInputStream.close();
		}
		catch (InvalidClassException exc) {
			throw new RegistryException(I18NL10N.translate("error.DeserialisingRegistry",filename));
		}
		catch (IOException exc) {
			throw new RegistryException(I18NL10N.translate("error.LoadingRegistry",filename));
		}
		catch (ClassNotFoundException exc) {
			throw new RegistryException(I18NL10N.translate("error.CastingRegistryObjectsWhenLoading",filename));
		}
	}

	/**
	 * Saves all objects in the registry to a file using serialisation.
	 * <P>
	 * Note that care should be taken that all the objects are serialisable.
	 *
	 * @param  filename           the name of the file to save the registry to
	 * @throws RegistryException  if an error occurred during the serialisation process
	 */
	public synchronized void save(String filename) throws RegistryException
	{
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(filename);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);

			// serialise registry
			objectOutputStream.writeObject(fContents);

			objectOutputStream.close();
			fileOutputStream.close();
		}
		catch (IOException exc) {
			throw new RegistryException(I18NL10N.translate("error.SavingRegistry",filename));
		}
	}
}
