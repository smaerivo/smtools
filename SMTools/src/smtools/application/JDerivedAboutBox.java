// -------------------------------------
// Filename      : JDerivedAboutBox.java
// Author        : Sven Maerivoet
// Last modified : 27/04/2011
// Target        : Java VM (1.6)
// -------------------------------------

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

package smtools.application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import smtools.application.util.*;
import smtools.exceptions.*;
import smtools.swing.dialogs.*;

/**
 * This class contains an example about box.
 * 
 * @author  Sven Maerivoet
 * @version 27/04/2011
 * @see     smtools.swing.dialogs.JAboutBox
 */
public final class JDerivedAboutBox extends JAboutBox
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	public JDerivedAboutBox(JFrame owner, JARResources resources)
	{
		super(owner,resources);
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	@Override
	protected JLabel getLogo()
	{
		try {
			return (new JLabel(new ImageIcon(fResources.getImage("application-resources/images/about.png"))));
		}
		catch (FileDoesNotExistException exc) {
			return null;
		}
	}

	@Override
	protected ELogoPosition getLogoPosition()
	{
		return JAboutBox.ELogoPosition.kTop;
	}

	@Override
	protected String getAboutText()
	{
		return
		("<B>JDerivedGUIApplication v1.1</B><BR /><BR />" +
			"Copyright 2003-2011 Sven Maerivoet<BR />");
	}

	@Override
	protected StringBuilder getCopyrightContent()
	{
		try {
			return fResources.getText("application-resources/licence/copyright.txt");
		}
		catch (FileDoesNotExistException exc) {
			return null;
		}
	}

	@Override
	protected StringBuilder getLicenceContent()
	{
		try {
			return fResources.getText("application-resources/licence/apache-licence.txt");
		}
		catch (FileDoesNotExistException exc) {
			return null;
		}
	}

	@Override
	protected JLabel[] getAffiliationsLabels()
	{
		JLabel[] affiliationsLabels = new JLabel[4];

		affiliationsLabels[0] = new JLabel("",SwingConstants.CENTER);
		try {
			affiliationsLabels[0].setIcon(new ImageIcon(fResources.getImage("application-resources/images/tmleuven-logo-slogan.png")));
		}
		catch (FileDoesNotExistException exc) {
		}
		affiliationsLabels[0].setToolTipText(Messages.lookup("tooltipAboutBoxClickForBrowser"));

		affiliationsLabels[1] = new JLabel(
			"<html>" +
				"<b>Transport &amp; Mobility Leuven</b>" +
				"<p>Diestsesteenweg 57" +
				"<p>3010 Kessel-Lo (Leuven)" +
				"<p>Belgium" +
				"<p>" +
				"<p>Telephone: +32 (16) 31 77 30" +
				"<p>Fax: +32 (16) 31 77 39" +
			"</html>");

		affiliationsLabels[2] = new JLabel(
			"<html>" +
				"E-mail: info@tmleuven.be" +
			"</html>");
		affiliationsLabels[2].setToolTipText(Messages.lookup("tooltipAboutBoxClickForEmailClient"));

		affiliationsLabels[3] = new JLabel(
			"<html>" +
				"Website: http://www.tmleuven.be/" +
			"</html>");
		affiliationsLabels[3].setToolTipText(Messages.lookup("tooltipAboutBoxClickForBrowser"));

		for (JLabel affiliationLabel : affiliationsLabels) { 
			affiliationLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			affiliationLabel.setHorizontalAlignment(SwingConstants.LEFT);
			affiliationLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
			affiliationLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		}

		// install mouse listeners
		MouseListener browserLauncher = new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				JDesktopAccess.executeBrowseApplication("http://www.tmleuven.be/");
			} 
			@Override
			public void mouseEntered(MouseEvent e)
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			} 
			@Override
			public void mouseExited(MouseEvent e)
			{
				setCursor(Cursor.getDefaultCursor());							
			}
			@Override
			public void mousePressed(MouseEvent e) { }
			@Override
			public void mouseReleased(MouseEvent e) { } 
		};

		MouseListener emailLauncher = new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				JDesktopAccess.executeMailApplication("sven.maerivoet@tmleuven.be","Request for information on JDerivedGUIApplication");
			}
			@Override
			public void mouseEntered(MouseEvent e)
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e)
			{
				setCursor(Cursor.getDefaultCursor());
			}
			@Override
			public void mousePressed(MouseEvent e) { }
			@Override
			public void mouseReleased(MouseEvent e) { }
		};

		affiliationsLabels[0].addMouseListener(browserLauncher);
		affiliationsLabels[2].addMouseListener(emailLauncher);
		affiliationsLabels[3].addMouseListener(browserLauncher);

		return affiliationsLabels;
	}
}
