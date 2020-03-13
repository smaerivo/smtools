// ---------------------------------
// Filename      : JImageLoader.java
// Author        : Sven Maerivoet
// Last modified : 13/03/2020
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

package org.sm.smtools.swing.util;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import org.apache.log4j.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.exceptions.*;

/**
 * The <CODE>JImageLoader</CODE> helper class provides functionality for loading images.
 * <P>
 * Note that a valid {@link I18NL10N} database must be available!
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 13/03/2020
 */
public final class JImageLoader
{
	// access point to the Log4j logging facility
	private static final Logger kLogger = Logger.getLogger(JImageLoader.class.getName());

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation.
	 */
	private JImageLoader()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Helper method to load an image from a specified file.
	 * 
	 * @param  filename           the name of the image file to load
	 * @param  caller             the object that called this method
	 * @return                    an <CODE>ImageIcon</CODE> object containing the loaded image
	 * @throws FileReadException  if an error occurred during the loading of the image
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
			kLogger.error(I18NL10N.kINSTANCE.translate("error.ImageNotFound",filename));
			throw (new FileReadException(filename));
		}

		// check if the image loaded correctly
		if ((mediaTracker.statusAll(false) & MediaTracker.ERRORED) != 0) {
			kLogger.error(I18NL10N.kINSTANCE.translate("error.ImageNotFound",filename));
			throw (new FileReadException(filename));
		}

		return (new ImageIcon(image));
	}

	/**
	 * Helper method to load an image from a specified file.
	 * 
	 * @param  filename           the name of the image file to load
	 * @return                    an <CODE>BufferedImage</CODE> object containing the loaded image
	 * @throws FileReadException  if an error occurred during the loading of the image
	 */
	public static BufferedImage loadImage(String filename) throws FileReadException
	{
		try {
			return ImageIO.read(new File(filename));
    }
		catch (IOException e) {
			kLogger.error(I18NL10N.kINSTANCE.translate("error.ImageNotFound",filename));
			throw (new FileReadException(filename));
		}
	}

	/**
	 * Helper method to efficiently rescale an image to an arbitrary width and height.
	 * 
	 * @param  image   the image to rescale
	 * @param  width   the new width of the image
	 * @param  height  the new height of the image
	 * @return         a rescaled version of the original image
	 */
	public static BufferedImage rescaleImageWH(BufferedImage image, int width, int height)
	{
		BufferedImage rescaledImage = new BufferedImage(width,height,image.getType());
		Graphics2D g2D = rescaledImage.createGraphics();
		g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2D.drawImage(image,0,0,width,height,0,0,image.getWidth(),image.getHeight(),null);
		g2D.dispose();
		return rescaledImage;
	}

	/**
	 * Helper method to efficiently rescale an image to an arbitrary width
	 * and keeping the aspect ratio for the height.
	 * 
	 * @param  image  the image to rescale
	 * @param  width  the new width of the image (its new height is automatically calculated)
	 * @return        a rescaled version of the original image
	 */
	public static BufferedImage rescaleImageW(BufferedImage image, int width)
	{
		double aspectRatio = (double) image.getHeight() / (double) image.getWidth();
		int height = (int) (width * aspectRatio);
		return rescaleImageWH(image,width,height);
	}

	/**
	 * Helper method to efficiently rescale an image to an arbitrary height
	 * and keeping the aspect ratio for the width.
	 * 
	 * @param  image   the image to rescale
	 * @param  height  the new height of the image (its new width is automatically calculated)
	 * @return         a rescaled version of the original image
	 */
	public static BufferedImage rescaleImageH(BufferedImage image, int height)
	{
		double aspectRatio = (double) image.getWidth() / (double) image.getHeight();
		int width = (int) (height * aspectRatio);
		return rescaleImageWH(image,width,height);
	}

	/**
	 * Helper method to efficiently rescale an image with arbitrary width and height factors.
	 * 
	 * @param  image         the image to rescale
	 * @param  widthFactor   the width factor of the image
	 * @param  heightFactor  the height factor of the image
	 * @return               a rescaled version of the original image
	 */
	public static BufferedImage rescaleImageWH(BufferedImage image, double widthFactor, double heightFactor)
	{
		int width = (int) (image.getWidth() * widthFactor);
		int height = (int) (image.getHeight() * heightFactor);
		return rescaleImageWH(image,width,height);
	}

	/**
	 * Helper method to efficiently rescale an image with an arbitrary width factor
	 * and keeping the aspect ratio for the height.
	 * 
	 * @param  image        the image to rescale
	 * @param  widthFactor  the width factor of the image (its new height is automatically calculated)
	 * @return              a rescaled version of the original image
	 */
	public static BufferedImage rescaleImageW(BufferedImage image, double widthFactor)
	{
		int width = (int) (image.getWidth() * widthFactor);
		return rescaleImageW(image,width);
	}

	/**
	 * Helper method to efficiently rescale an image with an arbitrary height factor
	 * and keeping the aspect ratio for the width.
	 * 
	 * @param  image         the image to rescale
	 * @param  heightFactor  the height factor of the image (its new width is automatically calculated)
	 * @return               a rescaled version of the original image
	 */
	public static BufferedImage rescaleImageH(BufferedImage image, double heightFactor)
	{
		int height = (int) (image.getHeight() * heightFactor);
		return rescaleImageH(image,height);
	}


	/**
	 * Converts a given <CODE>Image</CODE> into a <CODE>BufferedImage</CODE>
	 *
	 * @param image  the image to be converted
	 * @return the converted <CODE>BufferedImage</CODE>
	 */
	public static BufferedImage convertImageToBufferedImage(Image image)
	{
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// create a buffered image with transparency
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);

		// draw the image on to the buffered image
		Graphics2D g2D = bufferedImage.createGraphics();
		g2D.drawImage(image,0,0,null);
		g2D.dispose();

		return bufferedImage;
	}
}
