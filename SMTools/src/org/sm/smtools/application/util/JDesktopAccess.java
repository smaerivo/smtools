// -----------------------------------
// Filename      : JDesktopAccess.java
// Author        : Sven Maerivoet
// Last modified : 21/09/2007
// Target        : Java VM (1.6)
// -----------------------------------

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

package org.sm.smtools.application.util;

import java.awt.*;
import java.io.*;
import java.net.*;

/**
 * The <CODE>JDesktopAccess</CODE> class allows easy access for launching the platform's
 * default browser, mail client, and applications for opening, editing, and printing files.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 21/09/2007
 */
public final class JDesktopAccess
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	// prevent instantiation
	private JDesktopAccess()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Executes the platform's default browser.
	 * 
	 * @param url  the target URL to browse to
	 */
	public static void executeBrowseApplication(String url)
	{
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					desktop.browse(new URI(url));
				}
				catch (Exception exc) {
				}
			}
		}
	}

	/**
	 * Executes the platform's default mail client.
	 * <P>
	 * Note that when specifying the <CODE>subject</CODE> parameter, 
	 * all spaces are translated into "%20" character sequences
	 * (specify <CODE>null</CODE> if no subject should be given).
	 * 
	 * @param recipient  the recipient's e-mail address
	 * @param subject    the subject line for the e-mail 
	 */
	public static void executeMailApplication(String recipient, String subject)
	{
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			if (desktop.isSupported(Desktop.Action.MAIL)) {

				try {
					String uriString = "mailto:" + recipient;
					if (subject != null) {
						// replace all spaces with "%20" character sequences
						subject = subject.replaceAll(" ","%20");
						uriString += ("?subject=" + subject);
					}
					desktop.mail(new URI(uriString));
				}
				catch (Exception exc) {
					System.out.println(exc);
				}
			}
		}
	}

	/**
	 * Executes the platform's default application for opening a certain file.
	 * 
	 * @param file  the file to open 
	 */
	public static void executeOpenApplication(File file)
	{
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			if (desktop.isSupported(Desktop.Action.OPEN)) {

				try {
					desktop.edit(file);
				}
				catch (Exception exc) {
					System.out.println(exc);
				}
			}
		}
	}

	/**
	 * Executes the platform's default application for editing a certain file.
	 * <P>
	 * If the specified file is a directory, the file manager of the current platform is launched to open it.
	 *
	 * @param file  the file to edit 
	 */
	public static void executeEditApplication(File file)
	{
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			if (desktop.isSupported(Desktop.Action.EDIT)) {

				try {
					desktop.edit(file);
				}
				catch (Exception exc) {
					System.out.println(exc);
				}
			}
		}
	}

	/**
	 * Executes the platform's default application for printing a certain file.
	 *
	 * @param file  the file to print
	 */
	public static void executePrintApplication(File file)
	{
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			if (desktop.isSupported(Desktop.Action.PRINT)) {

				try {
					desktop.edit(file);
				}
				catch (Exception exc) {
					System.out.println(exc);
				}
			}
		}
	}
}
