// --------------------------------
// Filename      : JFileFilter.java
// Author        : Sven Maerivoet
// Last modified : 26/10/2004
// Target        : Java VM (1.6)
// --------------------------------

/**
 * Copyright 2003-2012 Sven Maerivoet
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

package org.sm.smtools.miscellaneous;

import java.io.*;
import java.util.*;

/**
 * The <CODE>JFileFilter</CODE> class provides selection filter for the <CODE>JFileChooser</CODE> class.
 * <P>
 * This file filter can handle multiple file types (i.e., extensions) belonging to the same collective description.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 26/10/2004
 */
public final class JFileFilter extends javax.swing.filechooser.FileFilter
{
	// internal datastructures
	private Hashtable<String,JFileFilter> fExtensions = null;
	private String fDescription = null;
	private String fFullDescription = null;
	private boolean fUseExtensionsInDescription = true;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JFileFilter</CODE> object.
	 */
	public JFileFilter()
	{
		fExtensions = new Hashtable<>();
	}

	/**
	 * Constructs a <CODE>JFileFilter</CODE> object for the specified single file extension.
	 *
	 * @param extension the filetype (i.e., extension) to allow
	 */
	public JFileFilter(String extension)
	{
		this(extension,null);
	}

	/**
	 * Constructs a <CODE>JFileFilter</CODE> object for the specified single file extension.
	 * <P>
	 * The specified <CODE>description</CODE> is substituted in the <CODE>JFileChooser</CODE>'s dialog box.
	 *
	 * @param extension   the filetype (i.e., extension) to allow
	 * @param description the description of the filetype
	 */
	public JFileFilter(String extension, String description)
	{
		this();

		if (extension != null) {
			addExtension(extension);
		}

		if (description != null) {
			setDescription(description);
		}
	}

	/**
	 * Constructs a <CODE>JFileFilter</CODE> object for the specified multiple file extensions.
	 *
	 * @param extensions the different filetypes (i.e., extensions) to allow
	 */
	public JFileFilter(String[] extensions)
	{
		this(extensions,null);
	}

	/**
	 * Constructs a <CODE>JFileFilter</CODE> object for the specified multiple file extensions.
	 * <P>
	 * The specified <CODE>description</CODE> is substituted in the <CODE>JFileChooser</CODE>'s dialog box.
	 *
	 * @param extensions  the different filetypes (i.e., extensions) to allow
	 * @param description the <I>collective</I> description of the different filetypes
	 */
	public JFileFilter(String[] extensions, String description)
	{
		this();

		for (int i = 0; i < extensions.length; ++i) {
			addExtension(extensions[i]);
		}

		if (description != null) {
			setDescription(description);
		}
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns whether or not a specified file is accepted by the filter.
	 * <P>
	 * Note that this method accepts directories by default.
	 *
	 * @return <CODE>true</CODE> if the specified file is accepted by the filter, <CODE>false</CODE> otherwise
	 */
	@Override
	public boolean accept(File f)
	{
		if (f != null) {

			if (f.isDirectory()) {
				return true;
			}

			String extension = getExtension(f);

			if ((extension != null) && (fExtensions.get(getExtension(f)) != null)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the extension of a specified file.
	 * <P>
	 * The extension of a file is everything that follows after the last dot of the filename.
	 *
	 * @return the extension of a specified file
	 */
	public String getExtension(File f)
	{
		if (f != null) {

			String filename = f.getName();

			// search for the file's extension (after the last dot)
			int i = filename.lastIndexOf('.');

			if ((i > 0) && (i < (filename.length() - 1))) {
				// retain everything after the last dot
				return filename.substring(i + 1).toLowerCase();
			}
		}

		return null;
	}

	/**
	 * Adds the specified extension to the file filter.
	 *
	 * @param extension the extension to add to this <CODE>JFileFilter</CODE> object
	 */
	public void addExtension(String extension)
	{
		if (fExtensions == null) {
			// create extension table
			fExtensions = new Hashtable<>(5);
		}

		fExtensions.put(extension.toLowerCase(),this);

		fFullDescription = null;
	}

	/**
	 * Returns the description of the file filter.
	 * <P>
	 * Note that the file filter's extensions can be listed in the
	 * description (using the {@link JFileFilter#setExtensionListInDescription(boolean)}
	 * method).
	 *
	 * @return the description of the file filter
	 */
	@Override
	public String getDescription()
	{
		// is a full description available ?
		if (fFullDescription == null) {

			if ((fDescription == null) || (isExtensionListInDescription())) {

				// if a simple description is available, use it and append the list of extensions to it
				if (fDescription != null) {
					fFullDescription = fDescription + " (";
				}
				else {
					fFullDescription = "(";
				}

				Enumeration<String> extensions = fExtensions.keys();

				// concatenate all extensions
				if (extensions != null) {

					fFullDescription += "." + extensions.nextElement();

					while (extensions.hasMoreElements()) {

						fFullDescription += ", " + extensions.nextElement();
					}
				}

				fFullDescription += ")";
			}
			else {
				fFullDescription = fDescription;
			}
		}

		return fFullDescription;
	}

	/**
	 * Sets the description of the file filter.
	 *
	 * @param description the description of the file filter
	 */
	public void setDescription(String description)
	{
		fDescription = description;
		fFullDescription = null;
	}

	/**
	 * Sets whether or not the accepted extensions of the file filter should be appended to the description.
	 *
	 * @param b a <CODE>boolean</CODE> flag indicating whether or not extensions should be appended to the description
	 */
	public void setExtensionListInDescription(boolean b)
	{
		fUseExtensionsInDescription = b;
		fFullDescription = null;
	}

	/**
	 * Returns whether or not the accepted extensions of the file filter are appended to the description.
	 *
	 * @return <CODE>true</CODE> when extensions are appended to the description, <CODE>false</CODE> otherwise
	 */
	public boolean isExtensionListInDescription()
	{
		return fUseExtensionsInDescription;
	}
}
