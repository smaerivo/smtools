// ---------------------------------
// Filename      : JImageLoader.java
// Author        : Sven Maerivoet
// Last modified : 07/04/2011
// Target        : Java VM (1.6)
// ---------------------------------

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

package smtools.swing.util;

import java.awt.*;
import javax.swing.*;
import org.apache.log4j.*;
import smtools.application.util.*;
import smtools.exceptions.*;

/**
 * The <CODE>JImageLoader</CODE> helper class provides functionality for loading images.
 * <P>
 * Note that a valid {@link Messages} database must be available!
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 07/04/2011
 */
public final class JImageLoader
{
	// access point to the Log4j logging facility
	private static final Logger kLogger = Logger.getLogger(JImageLoader.class.getName());

	/****************
	 * CONSTRUCTORS *
	 ****************/

	// prevent instantiation
	private JImageLoader()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Helper method to load an image from a specified file.
	 * 
	 * @param  filename          the name of the image file to load
	 * @param  caller            the object that called this method
	 * @return                   an <CODE>ImageIcon</CODE> object containing the loaded image
	 * @throws FileReadException if an error occurred during the loading of the image
	 */
	public static ImageIcon loadImageIcon(String filename, Component caller) throws FileReadException
	{
		Image image = Toolkit.getDefaultToolkit().getImage(filename);

		// wait until entire image is loaded
		MediaTracker mediaTracker = new MediaTracker(caller);
		mediaTracker.addImage(image,0);
		try {
			mediaTracker.waitForID(0);
		}
		catch (InterruptedException exception) {
			kLogger.error(Messages.lookup("errorImageNotFound",filename));
			throw (new FileReadException(filename));
		}

		// check if the image loaded correctly
		if ((mediaTracker.statusAll(false) & MediaTracker.ERRORED) != 0) {
			kLogger.error(Messages.lookup("errorImageNotFound",filename));
			throw (new FileReadException(filename));
		}

		return (new ImageIcon(image));
	}
}
