// ------------------------------
// Filename      : Registry.java
// Author        : Sven Maerivoet
// Last modified : 06/05/2011
// Target        : Java VM (1.6)
// ------------------------------

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

package smtools.application.registry;

import java.io.*;
import java.util.*;
import smtools.application.util.*;
import smtools.exceptions.*;

/**
 * The <CODE>Registry</CODE> class provides a mechanism for accessing (persistent) global variables.  
 * <P>
 * The <CODE>Registry</CODE> class is actually a <B>singleton</B> instance, and a local reference to it
 * should be obtained as follows:
 * <UL>
 * <CODE>
 *   Registry localReference = Registry.getInstance();
 * </CODE> 
 * </UL>
 * The registry itself contains different hives (which are classes implementing the {@link Hive}
 * interface), such as for example the {@link Hive} implementation. From anywhere within the
 * application, the registry can be accessed using the aforementioned locally constructed
 * reference. Furthermore, all hives in the registry can be serialised for persisent storage to
 * a file (as well as deserialising from a file).
 * <P>
 * Each hive in the registry has a unique name, and can individually be saved to and removed
 * from the registry. Continuing the previous train of thought, we can for example obtain a
 * local reference to a hive as follows:
 * <UL>
 * <CODE>
 *   Hive hive = (Hive) localReference.getHive("hive");
 * </CODE>
 * </UL>
 * <P>
 * When consecutively loading multiple registries, they are joined together in memory.
 * <P>
 * Note that a valid {@link Messages} database must be available!
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 06/05/2011
 */
public final class Registry
{
	private static Registry fInstance;
	private Hashtable<String,Hive> fHives;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	// prevent arbitrary instantiation (we only allow a singleton instance) 
	private Registry()
	{
		fHives = new Hashtable<String,Hive>();
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
	 * Clears the registry of all hives.
	 */
	public synchronized void clear()
	{
		fHives.clear();
	}

	/**
	 * Retrieves a hive from the registry.
	 * <P>
	 * If the hive is not found in the registry, <CODE>null</CODE> is returned.
	 * 
	 * @param hiveName the name of the hive to retrieve
	 * @see   Hive
	 */
	public synchronized Hive getHive(String hiveName)
	{
		return fHives.get(hiveName);
	}

	/**
	 * Adds a hive to the registry.
	 * 
	 * @param hiveName the name of the hive to add
	 * @param hive     the hive to add
	 * @see   Hive
	 */
	public synchronized void addHive(String hiveName, Hive hive)
	{
		fHives.put(hiveName,hive);
	}

	/**
	 * Removes a hive from the registry.
	 * 
	 * @param  hiveName the name of the hive to remove
	 * @throws RegistryException if the hive could not be found in the registry
	 * @see    Hive
	 */
	public synchronized void removeHive(String hiveName) throws RegistryException
	{
		Hive removedHive = fHives.remove(hiveName);

		if (removedHive == null) {
			throw new RegistryException(Messages.lookup("errorHiveNotFoundInRegistry",hiveName));
		}
	}

	/**
	 * Loads a hive from a file using deserialisation.
	 *
	 * @param  filename          the name of the file to load the hive from
	 * @return                   the hive loaded from the file
	 * @throws RegistryException if an error occurred during the deserialisation process
	 * @see    Hive
	 */
	public synchronized Hive loadHive(String filename) throws RegistryException
	{
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;

		try {
			fileInputStream = new FileInputStream(filename);
			objectInputStream = new ObjectInputStream(fileInputStream);

			// deserialise hives
			Hive hive = (Hive) objectInputStream.readObject();

			objectInputStream.close();
			fileInputStream.close();

			return hive;
		}
		catch (InvalidClassException exc) {
			throw new RegistryException(Messages.lookup("errorDeserialisingHive",filename));
		}
		catch (IOException exc) {
			throw new RegistryException(Messages.lookup("errorLoadingHive",filename));
		}
		catch (ClassNotFoundException exc) {
			throw new RegistryException(Messages.lookup("errorCastingHiveWhenLoading",filename));
		}
	}

	/**
	 * Saves a hive to a file using serialisation.
	 * <P>
	 * Note that care should be taken that all the fields in the specified hive are serialisable.
	 *
	 * @param  hive              the hive to save
	 * @param  filename          the name of the file to save the hive to
	 * @throws RegistryException if an error occurred during the serialisation process
	 * @see    Hive
	 */
	public synchronized void saveHive(Hive hive, String filename) throws RegistryException
	{
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(filename);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);

			// serialise hives
			objectOutputStream.writeObject(hive);

			objectOutputStream.close();
			fileOutputStream.close();
		}
		catch (IOException exc) {
			throw new RegistryException(Messages.lookup("errorSavingHive",filename));
		}
	}

	/**
	 * Loads all hives in the registry from a file using deserialisation.
	 *
	 * @param  filename          the name of the file to load the registry from
	 * @throws RegistryException if an error occurred during the deserialisation process
	 */
	public synchronized void load(String filename) throws RegistryException
	{
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;

		try {
			fileInputStream = new FileInputStream(filename);
			objectInputStream = new ObjectInputStream(fileInputStream);

			// deserialise hives
			Hashtable<String,Hive> hives = ((HivesWrapper) objectInputStream.readObject()).getHives();

			// join both registries
			fHives.putAll(hives);

			objectInputStream.close();
			fileInputStream.close();
		}
		catch (InvalidClassException exc) {
			throw new RegistryException(Messages.lookup("errorDeserialisingRegistry",filename));
		}
		catch (IOException exc) {
			throw new RegistryException(Messages.lookup("errorLoadingRegistry",filename));
		}
		catch (ClassNotFoundException exc) {
			throw new RegistryException(Messages.lookup("errorCastingRegistryHivesWhenLoading",filename));
		}
	}

	/**
	 * Saves all hives in the registry to a file using serialisation.
	 * <P>
	 * Note that care should be taken that all the fields in all the hives are serialisable.
	 *
	 * @param  filename          the name of the file to save the registry to
	 * @throws RegistryException if an error occurred during the serialisation process
	 */
	public synchronized void save(String filename) throws RegistryException
	{
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(filename);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);

			// serialise hives
			objectOutputStream.writeObject(new HivesWrapper(fHives));

			objectOutputStream.close();
			fileOutputStream.close();
		}
		catch (IOException exc) {
			throw new RegistryException(Messages.lookup("errorSavingRegistry",filename));
		}
	}

	/*****************
	 * INNER CLASSES *
	 *****************/

	private class HivesWrapper implements Serializable
	{
		private Hashtable<String,Hive> fHives;

		/****************
		 * CONSTRUCTORS *
		 ****************/

		public HivesWrapper(Hashtable<String,Hive> hives)
		{
			fHives = hives;
		}

		/******************
		 * PUBLIC METHODS *
		 ******************/

		public Hashtable<String,Hive> getHives()
		{
			return fHives;
		}

		/*******************
		 * PRIVATE METHODS *
		 *******************/

		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
		{
			// load the number of hives
			int nrOfHives = in.readInt();

			fHives = new Hashtable<String,Hive>();

			// deserialise the hives' names
			Vector<String> hiveNames = new Vector<String>();
			for (int hiveNr = 0; hiveNr < nrOfHives; ++hiveNr) {
				hiveNames.add(in.readUTF());
			}

			// deserialise the hives' contents
			for (int hiveNr = 0; hiveNr < nrOfHives; ++hiveNr) {
				Hive hive = (Hive) in.readObject();
				fHives.put(hiveNames.get(hiveNr),hive);
			}
		}

		private void writeObject(ObjectOutputStream out) throws IOException
		{
			// save the number of hives
			out.writeInt(fHives.size());

			// serialise the hives' names
			Enumeration<String> keysEnumeration = fHives.keys();
			while (keysEnumeration.hasMoreElements()) {
				out.writeUTF(keysEnumeration.nextElement());
			}

			// serialise the hives' contents
			Enumeration<Hive> hivesEnumeration = fHives.elements();
			while (hivesEnumeration.hasMoreElements()) {
				out.writeObject(hivesEnumeration.nextElement());
			}
		}
	}
}
