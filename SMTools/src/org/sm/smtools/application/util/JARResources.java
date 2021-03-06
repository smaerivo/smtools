// ---------------------------------
// Filename      : JARResources.java
// Author        : Sven Maerivoet
// Last modified : 15/03/2020
// Target        : Java VM (1.8)
// ---------------------------------

/**
 * Copyright 2003-2020 Sven Maerivoet
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
import java.awt.image.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;
import javax.swing.*;
import org.apache.log4j.*;
import org.sm.smtools.exceptions.*;
import org.sm.smtools.swing.util.*;

/**
 * The <CODE>JARResources</CODE> class provides access to JAR and ZIP files.
 * <P>
 * All the system resources are to be loaded in the static field {@link JARResources#fSystemResources}.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 15/03/2020
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
	private Hashtable<String,byte[]> fJARContents;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JARResources</CODE> object and loads all resources from it into memory.
	 *
	 * @param  jarFilename            the filename of the JAR or ZIP file containing the resources
	 * @throws FileNotFoundException  if the archive file is not found
	 * @throws FileReadException      if an error occurred during loading resources from the archive file
	 */
	public JARResources(String jarFilename) throws FileNotFoundException, FileReadException
	{
		fJARContents = new Hashtable<>();

		if (jarFilename == null) {
			return;
		}

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
					fJARContents.put(zipEntry.getName(),buffer);
				}
			}

			zis.close();
		}
		catch (FileNotFoundException exc) {
			kLogger.error(exc.getMessage());
			throw (new FileNotFoundException(jarFilename));
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
			kLogger.fatal("System resources not available; application aborted.");
			System.exit(0);
		}
	}

	/**
	 * Retrieves a raw resource from the archive.
	 * 
	 * @param  name                   the (file)name of the resource to retrieve from the archive
	 * @return                        a byte array representing the resource
	 * @throws FileNotFoundException  if the resource was not found
	 */
	public byte[] getRawResource(String name) throws FileNotFoundException
	{
		if (name == null) {
			return null;
		}

		byte[] rawResource = fJARContents.get(name);
		if ((rawResource == null) || (DevelopMode.kINSTANCE.isActivated() && (this != JARResources.fSystemResources))) {
			// try to load resource from local file system
			try {
				if (rawResource == null) {
					kLogger.warn("Resource (" + name + ") not found in archive, trying to load from local file system...");
				}
				else {
					kLogger.warn("Development override for resource (" + name + "), trying to load from local file system...");
				}
				rawResource = Files.readAllBytes(Paths.get(name).toRealPath());
			}
			catch (Exception exc) {
				kLogger.error("Failed to load resource (" + name + "): " + exc);
				throw (new FileNotFoundException(name));
			}
		}
		return rawResource;
	}

	/**
	 * Retrieves a resource as an <CODE>InputStream</CODE> from the archive.
	 * 
	 * @param  name                   the (file)name of the resource to retrieve from the archive
	 * @return                        an <CODE>InputStream</CODE> representing the resource
	 * @throws FileNotFoundException  if the resource was not found
	 * @see                           JARResources#getRawResource(String name)
	 */
	public InputStream getInputStream(String name) throws FileNotFoundException
	{
		return (new ByteArrayInputStream(getRawResource(name)));
	}

	/**
	 * Retrieves a resource as a <CODE>StringBuilder</CODE> from the archive.
	 * 
	 * @param  name                   the (file)name of the resource to retrieve from the archive
	 * @return                        a <CODE>StringBuilder</CODE> representing the resource
	 * @throws FileNotFoundException  if the resource was not found
	 * @see                           JARResources#getRawResource(String name)
	 */
	public StringBuilder getText(String name) throws FileNotFoundException
	{
		return (new StringBuilder(new String(getRawResource(name))));
	}

	/**
	 * Retrieves a resource as an <CODE>Image</CODE> from the archive.
	 * 
	 * @param  name                   the (file)name of the resource to retrieve from the archive
	 * @return                        an <CODE>Image</CODE> representing the resource
	 * @throws FileNotFoundException  if the resource was not found
	 * @see                           JARResources#getRawResource(String name)
	 */
	public Image getImage(String name) throws FileNotFoundException
	{
		// use ImageIcon as a MediaTracker to completely load the image
		ImageIcon imageIcon = new ImageIcon(getRawResource(name));

		return imageIcon.getImage();
	}

	/**
	 * Retrieves a resource as a <CODE>BufferedImage</CODE> from the archive.
	 * 
	 * @param  name                   the (file)name of the resource to retrieve from the archive
	 * @return                        an <CODE>Image</CODE> representing the resource
	 * @throws FileNotFoundException  if the resource was not found
	 * @see                           JARResources#getRawResource(String name)
	 */
	public BufferedImage getBufferedImage(String name) throws FileNotFoundException
	{
		return JImageLoader.convertImageToBufferedImage(getImage(name));
	}
}
