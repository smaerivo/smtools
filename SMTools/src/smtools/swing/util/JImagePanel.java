// --------------------------------
// Filename      : JImagePanel.java
// Author        : Sven Maerivoet
// Last modified : 23/04/2011
// Target        : Java VM (1.6)
// --------------------------------

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
import smtools.exceptions.*;

/**
 * The <CODE>JImagePanel</CODE> class provides a <CODE>JPanel</CODE> with a background image.
 * <P>
 * Currently, only GIF, JPG or PNG images are allowed.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 23/04/2011
 */
public final class JImagePanel extends JPanel
{
	// internal datastructures
	private Image fBackgroundImage;
	private boolean fRetainAspectRatio;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JImagePanel</CODE> object with the image in the specified file as its background.
	 * <P>
	 * Only GIF, JPG or PNG images are allowed.
	 * <P>
	 * Note that a {@link FileReadException} is thrown when the image file cannot be read.
	 * 
	 * @param  imageFilename     the name of the file containing the background image
	 * @throws FileReadException if the specified image file cannot be read
	 */
	public JImagePanel(String imageFilename) throws FileReadException
	{
		fBackgroundImage = JImageLoader.loadImageIcon(imageFilename,this).getImage();
		fRetainAspectRatio = false;

		// set the panel's size equal to the image's size
		setPreferredSize(new Dimension(fBackgroundImage.getWidth(this),fBackgroundImage.getHeight(this)));
	}

	/**
	 * Constructs a <CODE>JImagePanel</CODE> object with a specified <CODE>Image</CODE>.
	 * <P>
	 * Only GIF, JPG or PNG images are allowed.
	 * 
	 * @param  backgroundImage the background <CODE>Image</CODE>
	 */
	public JImagePanel(Image backgroundImage)
	{
		fBackgroundImage = backgroundImage;
		fRetainAspectRatio = false;

		// set the panel's size equal to the image's size
		setPreferredSize(new Dimension(fBackgroundImage.getWidth(this),fBackgroundImage.getHeight(this)));
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 */
	public void retainAspectRatio()
	{
		fRetainAspectRatio = true;
	}

	/**
	 * Blocks (re)painting of the image until it's completely loaded.
	 */
	@Override
	public boolean imageUpdate(Image img, int flags, int x, int y, int w, int h)
	{
		// only allow repainting of this canvas when the image is completely loaded
		// this prevents flickering
		if ((flags & ALLBITS) != 0) {
			repaint();
		}

		return ((flags & (ALLBITS | ERROR)) == 0);
	}

	/**
	 * Paints the image on the canvas.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Dimension panelDimension = getSize();

		if (fRetainAspectRatio) {
	
			int imageWidth = fBackgroundImage.getWidth(this);
			int imageHeight = fBackgroundImage.getHeight(this);

			double scaleX = (double) panelDimension.width / (double) imageWidth;
			double scaleY = (double) panelDimension.height / (double) imageHeight;
			double scalingFactor = Math.min(scaleX,scaleY);

			imageWidth = (int) Math.floor(scalingFactor * imageWidth);
			imageHeight = (int) Math.floor(scalingFactor * imageHeight);

			g.drawImage(fBackgroundImage,0,0,imageWidth,imageHeight,this);
		}
		else {
			// scale image to fit the panel's current size
			g.drawImage(fBackgroundImage,0,0,panelDimension.width,panelDimension.height,this);
		}
	}
}
