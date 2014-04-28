// ---------------------------------
// Filename      : JARResources.java
// Author        : Sven Maerivoet
// Last modified : 28/04/2013
// Target        : Java VM (1.6)
// ---------------------------------

/**
 * Copyright 2003-2013 Sven Maerivoet
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

import org.apache.log4j.*;
import org.sm.smtools.exceptions.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.swing.*;

/**
 * The <CODE>JARResources</CODE> class provides access to JAR and ZIP files.
 * <P>
 * All the system resources are to be loaded in the static field {@link JARResources#fSystemResources}.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 28/04/2013
 */
public final class JARResources
{
	/**
	 * Access point to the system resources.
	 */
	public static JARResources fSystemResources;

	// access point to the Log4j logging facility
	private static final Logger kLogger = Logger.getLogger(JARResources.class.getName());

	// internal data structures
	private Hashtable<String,byte[]> fhtJARContents;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JARResources</CODE> object and loads all resources from it into memory.
	 *
	 * @param  jarFilename               the filename of the JAR or ZIP file containing the resources
	 * @throws FileDoesNotExistException if the archive file is not found
	 * @throws FileReadException         if an error occurred during loading resources from the archive file
	 */
	public JARResources(String jarFilename) throws FileDoesNotExistException, FileReadException
	{
		fhtJARContents = new Hashtable<>();

		try {
			FileInputStream fis = new FileInputStream(jarFilename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ZipInputStream zis = new ZipInputStream(bis);

			ZipEntry zipEntry = null;
			while ((zipEntry = zis.getNextEntry()) != null) {
				if (!zipEntry.isDirectory()) {
					int size = (int) zipEntry.getSize();
					byte[] buffer = new byte[size];
					int nrOfBytesRead = 0;
					int chunkSize = 0;
					while (((int) size - nrOfBytesRead) > 0) {
						chunkSize = zis.read(buffer,nrOfBytesRead,size - nrOfBytesRead);
						if (chunkSize == -1) {
							break;
						}
						else {
							nrOfBytesRead += chunkSize;
						}
					}
					// add to internal resource hashtable
					fhtJARContents.put(zipEntry.getName(),buffer);
				}
			}

			zis.close();
		}
		catch (FileNotFoundException exc) {
			kLogger.error(exc.getMessage());
			throw (new FileDoesNotExistException(jarFilename));
		}
		catch (IOException exc) {
			kLogger.error(exc.getMessage());
			throw (new FileReadException(jarFilename));
		}
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Checks if the system resources are initialised.
	 * The running application is aborted if this is not the case.
	 */
	public static void checkSystemInitialisation()
	{
		if (JARResources.fSystemResources == null) {
			kLogger.fatal("System resources not available! Application aborted.");
			System.exit(0);
		}
	}

	/**
	 * Retrieves a raw resource from the archive.
	 * 
	 * @param  name                      the name of the resource to retrieve from the archive
	 * @return                           a byte array representing the resource
	 * @throws FileDoesNotExistException if the resource was not found
	 */
	public byte[] getRawResource(String name) throws FileDoesNotExistException
	{
		byte[] rawResource = fhtJARContents.get(name);
		if (rawResource == null) {
			kLogger.error("Resource (" + name + ") not found in archive.");
			throw (new FileDoesNotExistException(name));
		}
		return rawResource;
	}

	/**
	 * Retrieves a resource as an <CODE>InputStream</CODE> from the archive.
	 * 
	 * @param  name                      the name of the resource to retrieve from the archive
	 * @return                           an <CODE>InputStream</CODE> representing the resource
	 * @throws FileDoesNotExistException if the resource was not found
	 * @see                              JARResources#getRawResource(String name)
	 */
	public InputStream getInputStream(String name) throws FileDoesNotExistException
	{
		return (new ByteArrayInputStream(getRawResource(name)));
	}

	/**
	 * Retrieves a resource as a <CODE>StringBuilder</CODE> from the archive.
	 * 
	 * @param  name                      the name of the resource to retrieve from the archive
	 * @return                           a <CODE>StringBuilder</CODE> representing the resource
	 * @throws FileDoesNotExistException if the resource was not found
	 * @see                              JARResources#getRawResource(String name)
	 */
	public StringBuilder getText(String name) throws FileDoesNotExistException
	{
		return (new StringBuilder(new String(getRawResource(name))));
	}

	/**
	 * Retrieves a resource as an <CODE>Image</CODE> from the archive.
	 * 
	 * @param  name                      the name of the resource to retrieve from the archive
	 * @return                           an <CODE>Image</CODE> representing the resource
	 * @throws FileDoesNotExistException if the resource was not found
	 * @see                              JARResources#getRawResource(String name)
	 */
	public Image getImage(String name) throws FileDoesNotExistException
	{
		// use ImageIcon as a MediaTracker to completely load the image
		ImageIcon imageIcon = new ImageIcon(getRawResource(name));

		return imageIcon.getImage();
	}
}
