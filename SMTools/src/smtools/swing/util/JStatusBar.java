// -------------------------------
// Filename      : JStatusBar.java
// Author        : Sven Maerivoet
// Last modified : 02/12/2012
// Target        : Java VM (1.6)
// -------------------------------

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

package smtools.swing.util;

import java.awt.*;
import javax.swing.*;

/**
 * The <CODE>JStatusBar</CODE> class provides a <CODE>JPanel</CODE> that provides an application with a status bar.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 02/12/2012
 */
public final class JStatusBar extends JPanel
{
	// the vertical size of the status bar
	private static final int kHeight = 23;

	// the offset
	private static final int kOffset = 5;

	// internal datastructures
	private JLabel fStatusTextLabel;
	private JPanel fSeparatorPanel;
	private JLabel fMiscellaneousTextLabel;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JStatusBar</CODE> object.
	 * <P>
	 * Note that the resizable-icon is shown.
	 */
	public JStatusBar()
	{
		this(true);
	}

	/**
	 * Constructs a <CODE>JStatusBar</CODE> object.
	 *
	 * @param isGUIResizable indicates whether or not the resizable-icon should be shown
	 */
	public JStatusBar(boolean isGUIResizable)
	{
		setPreferredSize(new Dimension(getWidth(),kHeight));
		setLayout(new BorderLayout());
		add(Box.createRigidArea(new Dimension(kOffset,0)),BorderLayout.WEST);

		// add left panel
			fStatusTextLabel = new JLabel();
			fStatusTextLabel.setOpaque(false);
		add(fStatusTextLabel,BorderLayout.CENTER);

			// add right panel
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new BorderLayout());
			rightPanel.setOpaque(false);

				fSeparatorPanel = new JPanel();
				fSeparatorPanel.setLayout(new BoxLayout(fSeparatorPanel,BoxLayout.Y_AXIS));
				fSeparatorPanel.add(Box.createRigidArea(new Dimension(0,kOffset)));
				fSeparatorPanel.add(new JEtchedLine(JEtchedLine.EOrientation.kVertical));
				fSeparatorPanel.add(Box.createRigidArea(new Dimension(0,kOffset)));
				fSeparatorPanel.setOpaque(false);
			rightPanel.add(fSeparatorPanel,BorderLayout.WEST);
				fMiscellaneousTextLabel = new JLabel();
				fMiscellaneousTextLabel.setOpaque(false);
			rightPanel.add(fMiscellaneousTextLabel,BorderLayout.CENTER);

			// if necessary, add the resize icon
			if (isGUIResizable) {
				JPanel resizeIconPanel = new JPanel();
				resizeIconPanel.setLayout(new BoxLayout(resizeIconPanel,BoxLayout.Y_AXIS));
				resizeIconPanel.add(Box.createVerticalGlue());
					JLabel resizeIconLabel = new JLabel(new JResizeIcon()); 
					resizeIconLabel.setOpaque(false);
				resizeIconPanel.add(resizeIconLabel);
				resizeIconPanel.setOpaque(false);
				rightPanel.add(resizeIconPanel,BorderLayout.EAST);
			}
		add(rightPanel,BorderLayout.EAST);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the status text.
	 *
	 * @param statusText the new status text to show
	 */
	public void setStatusText(String statusText)
	{
		fStatusTextLabel.setText(statusText);
	}

	/**
	 * Clears the status text.
	 */
	public void clearStatusText()
	{
		setStatusText("");
	}

	/**
	 * Sets the status text's color.
	 *
	 * @param color the status text's color
	 */
	public void setStatusTextColor(Color color)
	{
		fStatusTextLabel.setForeground(color);
	}

	/**
	 * Returns the status text's color to black.
	 */
	public void removeStatusTextColor()
	{
		setStatusTextColor(Color.BLACK);
	}

	/**
	 * Sets the miscellaneous text.
	 *
	 * @param miscellaneousText the new miscellaneous text to show
	 */
	public void setMiscellaneousText(String miscellaneousText)
	{
		// add extra spaces for justification purposes
		fMiscellaneousTextLabel.setText(" " + miscellaneousText + "  ");

		// hide the separator line if no miscellaneous text is displayed
		fSeparatorPanel.setVisible(miscellaneousText.length() > 0);
	}

	/**
	 * Clears the miscellaneous text.
	 */
	public void clearMiscellaneousText()
	{
		setMiscellaneousText("");
	}

	/**
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int y = 0;
		g.setColor(new Color(156,154,140));
		g.drawLine(0,y,getWidth(),y);
		++y;
		g.setColor(new Color(196,194,183));
		g.drawLine(0,y,getWidth(),y);
		++y;
		g.setColor(new Color(218,215,201));
		g.drawLine(0,y,getWidth(),y);
		++y;
		g.setColor(new Color(233,231,217));
		g.drawLine(0,y,getWidth(),y);

		y = getHeight() - 3;
		g.setColor(new Color(233,232,218));
		g.drawLine(0,y,getWidth(),y);
		++y;
		g.setColor(new Color(233,231,216));
		g.drawLine(0,y,getWidth(),y);
		y = getHeight() - 1;
		g.setColor(new Color(221,221,220));
		g.drawLine(0,y,getWidth(),y);
  }

	/*****************
	 * INNER CLASSES *
	 *****************/
	private class JResizeIcon implements Icon
	{
		// the size of the icon
		private static final int kWidth = 12;
		private static final int kHeight = kWidth;

		// the colors of the icon
		private final Color kSquareColorLeft = new Color(184,180,163); 
		private final Color kSquareColorTopRight = new Color(184,180,161); 
    private final Color kSquareColorBottomRight = new Color(184,181,161);
    private final Color k3DEffectColor = Color.WHITE;

    /******************
     * PUBLIC METHODS *
     ******************/

    /**
		 */
		@Override
		public int getIconWidth()
		{
			return kWidth;
		}

		/**
		 */
		@Override
		public int getIconHeight()
		{
			return kHeight;
		}

		/**
		 */
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			int firstRow = 0;
			int firstColumn = 0;
			int rowDiff = 4;
			int columnDiff = 4;

			int secondRow = firstRow + rowDiff;
			int secondColumn = firstColumn + columnDiff;
			int thirdRow = secondRow + rowDiff;
			int thirdColumn = secondColumn + columnDiff;

			//first row
			draw3DSquare(g,firstColumn + 1,thirdRow + 1);

			//second row
			draw3DSquare(g,secondColumn + 1,secondRow + 1);
			draw3DSquare(g,secondColumn + 1,thirdRow + 1);

			//third row
			draw3DSquare(g,thirdColumn + 1,firstRow + 1);
			draw3DSquare(g,thirdColumn + 1,secondRow + 1);
			draw3DSquare(g,thirdColumn + 1,thirdRow + 1);

			//first row
			drawSquare(g,firstColumn,thirdRow);

			//second row
			drawSquare(g,secondColumn,secondRow);
			drawSquare(g,secondColumn,thirdRow);

			//third row
			drawSquare(g,thirdColumn,firstRow);
			drawSquare(g,thirdColumn,secondRow);
			drawSquare(g,thirdColumn,thirdRow);
		}

    /*******************
     * PRIVATE METHODS *
     *******************/

		/**
		 */
		private void drawSquare(Graphics g, int x, int y)
		{
			Color oldColor = g.getColor();
			g.setColor(kSquareColorLeft);
			g.drawLine(x,y,x,y + 1);
			g.setColor(kSquareColorTopRight);
			g.drawLine(x + 1,y,x + 1,y);
			g.setColor(kSquareColorBottomRight);
			g.drawLine(x + 1,y + 1,x + 1,y + 1);
			g.setColor(oldColor);
		}

		/**
		 */
		private void draw3DSquare(Graphics g, int x, int y)
		{
			Color oldColor = g.getColor();
			g.setColor(k3DEffectColor);
			g.fillRect(x,y,2,2);
			g.setColor(oldColor);
		}
	}
}
