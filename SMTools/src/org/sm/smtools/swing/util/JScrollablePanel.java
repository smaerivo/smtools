// -------------------------------------
// Filename      : JScrollablePanel.java
// Author        : Sven Maerivoet
// Last modified : 06/01/2013
// Target        : Java VM (1.8)
// -------------------------------------

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

package org.sm.smtools.swing.util;

import java.awt.*;
import javax.swing.*;

/**
 * The <CODE>JScrollablePanel</CODE> class provides a scrollable <CODE>JPanel</CODE>.
 * 
 * @author  Sven Maerivoet
 * @version 06/01/2013
 */
public class JScrollablePanel extends JPanel implements Scrollable
{
	// internal datastructures
	private JPanel fPanel;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Creates a <CODE>JScrollablePanel</CODE> object.
	 *
	 * @param panel  the contained <CODE>JPanel</CODE>
	 */
	public JScrollablePanel(JPanel panel)
	{
		super(new BorderLayout());
		fPanel = panel;
		add(panel,BorderLayout.CENTER);
	}

	/******************
	 * PUBLIC METHODS *
	 *****************/

	/**
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		Dimension preferredSize = fPanel.getPreferredSize();
		if (getParent() instanceof JViewport) {
			preferredSize.width += ((JScrollPane) getParent().getParent()).getVerticalScrollBar().getPreferredSize().width + 10;
			preferredSize.height += ((JScrollPane) getParent().getParent()).getHorizontalScrollBar().getPreferredSize().height + 10;
		}
		return preferredSize;
	}

	/**
	*/
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		return orientation == SwingConstants.HORIZONTAL ? Math.max(visibleRect.width / 10, 1) : Math.max(visibleRect.height / 10, 1);
	}

	/**
	*/
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		return orientation == SwingConstants.HORIZONTAL ? Math.max(visibleRect.width * 9 / 10, 1) : Math.max(visibleRect.height * 9 / 10, 1);
	}

	/**
	*/
	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		if (getParent() instanceof JViewport) {
			JViewport viewport = (JViewport) getParent();
			return (getPreferredSize().width < viewport.getWidth());
		}
		return false;
	}

	/**
	*/
	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		if (getParent() instanceof JViewport) {
			JViewport viewport = (JViewport) getParent();
			return (getPreferredSize().height < viewport.getHeight());
		}
		return false;
	}
}
