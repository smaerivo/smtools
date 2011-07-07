// ------------------------------
// Filename      : Messages.java
// Author        : Sven Maerivoet
// Last modified : 06/04/2011
// Target        : Java VM (1.6)
// ------------------------------

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

package smtools.application.util;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.*;
import smtools.exceptions.*;

/**
 * The <CODE>Messages</CODE> class provides a mechanism for implementing a
 * multi-lingual message-database.
 * <P>
 * The idea behind this class is that, whenever a <CODE>String</CODE> locale is
 * needed, it is replaced by a lookup in the message-database that gives a
 * translation to the currently selected language. The message-database is based
 * on <I>keys</I> and <I>values</I>: the keys provide the lookup-mechanism,
 * whereas the values represent the translations.
 * <P>
 * When an application is starting, the database containing the selected messages
 * should be loaded using the static {@link Messages#load(String)} or
 * {@link Messages#load(InputStream)} methods. Lookups are performed as follows:
 * <P>
 * <UL>
 * <CODE>String translation = Messages.lookup(key,parameters);</CODE>
 * </UL>
 * <P>
 * The optional parameters are specified in the keys using <CODE>(^i)</CODE>
 * with <CODE>i</CODE> the number of the parameter. In order to get the caret itself,
 * it should be escaped by writing it twice (^^).
 * <P>
 * <B><U>Some examples</U></B>
 * <P>
 * <UL>
 * <LI>Consider the following message-database file:</LI>
 * <P>
 * <UL>
 * <CODE>myFirstKey=my first value</CODE><BR />
 * <CODE>mySecondKey=my ^1 value</CODE>
 * </UL>
 * <P>
 * The following lookups demonstrate its usage:
 * <P>
 * <UL>
 * <LI><CODE>Messages.lookup("myFirstKey")</CODE> will result in "
 * <CODE>my first value</CODE>".</LI>
 * <P>
 * <LI><CODE>Messages.lookup("mySecondKey","second")</CODE>
 * will result in "<CODE>my second value</CODE>".</LI>
 * </UL>
 * <P>
 * <LI>A GUI is typically constructed with statements like the following one:</LI>
 * <P>
 * 
 * <PRE>
 * JLabel label = new JLabel(Messages.lookup(&quot;labelKey&quot;,&quot;parameter 1&quot;,...,&quot;parameter N&quot;));
 * </PRE>
 * 
 * </UL>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author Sven Maerivoet
 * @version 06/04/2011
 */
public final class Messages {
	// language-related definitions
	/**
	 * Useful constant for specifying a Dutch message-database file.
	 */
	public static final String kFilenameLanguageDutch = "language-dutch.properties";

	/**
	 * Useful constant for specifying a British English message-database file.
	 */
	public static final String kFilenameLanguageEnglish = "language-english.properties";

	/**
	 * Useful constant for specifying an American English message-database file.
	 */
	public static final String kFilenameLanguageAmerican = "language-american.properties";

	// access point to the Log4j logging facility
	private static final Logger kLogger = Logger.getLogger(Messages.class.getName());

	// internal datastructures
	private static FileInputStream fMessageFile;
	private static Properties fMessageTable;

	/*************************
	 * STATIC INITIALISATION *
	 *************************/

	static {
		fMessageTable = new Properties();
	}

	/****************
	 * CONSTRUCTORS *
	 ****************/

	// prevent instantiation
	private Messages()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Loads a message-database from a specified file.
	 * <P>
	 * The file should have the following structure: each line should contain at
	 * most one (key,value) pair. They are specified as:
	 * <P>
	 * <UL>
	 * <CODE>key=value</CODE>
	 * </UL>
	 * <P>
	 * Note that although comments are not supported, blank lines are allowed.
	 * <P>
	 * Furthermore, when loading a message-database, all <I>existing</I> keys
	 * will be overwritten with the new values.
	 * 
	 * @param messageFilename            the name of the file containing the message-database
	 * @throws FileDoesNotExistException if the file containing the message-database could not be opened
	 */
	public static void load(String messageFilename) throws FileDoesNotExistException
	{
		try {
			fMessageFile = new FileInputStream(messageFilename);
			fMessageTable.load(fMessageFile);
		}
		catch (IOException exc) {
			kLogger.error("Language definition file (" + messageFilename + ") not found.", false);
			throw (new FileDoesNotExistException(messageFilename));
		}
	}

	/**
	 * Loads a message-database from an <CODE>InputStream</CODE>.
	 * <P>
	 * The stream should have the following structure: each line should contain at
	 * most one (key,value) pair. They are specified as:
	 * <P>
	 * <UL>
	 * <CODE>key=value</CODE>
	 * </UL>
	 * <P>
	 * Note that although comments are not supported, blank lines are allowed.
	 * <P>
	 * Furthermore, when loading a message-database, all <I>existing</I> keys
	 * will be overwritten with the new values.
	 * 
	 * @param messageInputStream the <CODE>InputStream</CODE> containing the message-database
	 * @throws FileReadException if an I/O error occurs during reading from the specified <CODE>InputStream</CODE>
	 * @see Messages#load(String messageFile)
	 */
	public static void load(InputStream messageInputStream) throws FileReadException
	{
		try {
			fMessageTable.load(messageInputStream);
		}
		catch (IOException exc) {
			kLogger.error("Error while processing language definition file.", false);
			throw (new FileReadException(exc.getMessage()));
		}
	}

	/**
	 * Clears the message-database.
	 */
	public static void clear()
	{
		fMessageTable.clear();
	}

	/**
	 * Looks up a key in the message-database and retrieves its translation.
	 * <P>
	 * Note that when the key is not found, an empty string ("") is returned
	 * (<B>not <CODE>null</CODE></B>). Also note that any redundant specified
	 * parameters are ignored.
	 * 
	 * @param messageKey the key to search for in the message-database
	 * @param parameters the optional parameters to substitute in the translated value
	 * @return the value corresponding to the <CODE>messageKey</CODE>
	 */
	public static String lookup(String messageKey, String ... parameters)
	{
		// retrieve message from message table
		String message = fMessageTable.getProperty(messageKey);

		if (message == null) {
			kLogger.error("Message key (" + messageKey + ") not found in language definition file.");
			return "";
		}
		String fParsed = "";

		// parse the message
		for (int strPos = 0; strPos < message.length(); ++strPos) {
			String fToAdd = "";

			// was a parameter specified ?
			if (message.charAt(strPos) == '^') {

				++strPos;

				// have we reached the end of the string ?
				if (strPos < message.length()) {

					// do we need to escape the caret ?
					if (message.charAt(strPos) == '^') {
						fToAdd = "^";
					}
					else {
						// extract the parameter's number
						try {
							int parameterNr = Integer.parseInt(message.substring(strPos,strPos + 1)) - 1;

							// lookup the parameter in the list
							if ((parameters != null) && (parameterNr < parameters.length)) {
								fToAdd = parameters[parameterNr];
							}
						}
						catch (NumberFormatException exc) {
						}
					}
				}
			} else {
				// no parameter was specified
				fToAdd = String.valueOf(message.charAt(strPos));
			}

			fParsed = fParsed.concat(fToAdd);
		}

		return fParsed;
	}

	/**
	 * Returns the <CODE>KeyEvent</CODE> code associated with the specified
	 * mnemonic.
	 * <P>
	 * A mnemonic consists of a single character; this method does however relaxes
	 * this assumption, as it allows for a complete <CODE>String</CODE> to be
	 * specified (only the first character is considered). So its convenient for
	 * the caller to pass a <I>key</I> as parameter to this method.
	 * 
	 * @param mnemonic
	 *          the mnemonic to retrieve the <CODE>KeyEvent</CODE> code from
	 * @return the <CODE>KeyEvent</CODE> code associated with the specified
	 *         mnemonic
	 */
	public static int translateMnemonic(final String mnemonic)
	{
		char mnemonicChar = (mnemonic.toUpperCase()).charAt(0);

		switch (mnemonicChar) {
		case 'A':
			return KeyEvent.VK_A;
		case 'B':
			return KeyEvent.VK_B;
		case 'C':
			return KeyEvent.VK_C;
		case 'D':
			return KeyEvent.VK_D;
		case 'E':
			return KeyEvent.VK_E;
		case 'F':
			return KeyEvent.VK_F;
		case 'G':
			return KeyEvent.VK_G;
		case 'H':
			return KeyEvent.VK_H;
		case 'I':
			return KeyEvent.VK_I;
		case 'J':
			return KeyEvent.VK_J;
		case 'K':
			return KeyEvent.VK_K;
		case 'L':
			return KeyEvent.VK_L;
		case 'M':
			return KeyEvent.VK_M;
		case 'N':
			return KeyEvent.VK_N;
		case 'O':
			return KeyEvent.VK_O;
		case 'P':
			return KeyEvent.VK_P;
		case 'Q':
			return KeyEvent.VK_Q;
		case 'R':
			return KeyEvent.VK_R;
		case 'S':
			return KeyEvent.VK_S;
		case 'T':
			return KeyEvent.VK_T;
		case 'U':
			return KeyEvent.VK_U;
		case 'V':
			return KeyEvent.VK_V;
		case 'W':
			return KeyEvent.VK_W;
		case 'X':
			return KeyEvent.VK_X;
		case 'Y':
			return KeyEvent.VK_Y;
		case 'Z':
			return KeyEvent.VK_Z;
		}

		return 0;
	}
}
