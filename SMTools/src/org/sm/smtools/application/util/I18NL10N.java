// ------------------------------
// Filename      : I18NL10N.java
// Author        : Sven Maerivoet
// Last modified : 26/06/2018
// Target        : Java VM (1.8)
// ------------------------------

/**
 * Copyright 2003-2018 Sven Maerivoet
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

import java.awt.event.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.*;
import org.sm.smtools.exceptions.*;
import org.sm.smtools.util.*;

/**
 * The <CODE>I18NL10N</CODE> class provides a mechanism for implementing a
 * multi-lingual interface through internationalisation (I18N) and localisation (L10N).
 * <P>
 * The idea behind this class is that, whenever a <CODE>String</CODE> locale is
 * needed, it is replaced by a lookup in the database that gives a
 * translation to the currently selected locale. The database is based
 * on <I>keys</I> and <I>values</I>: the keys provide the lookup-mechanism,
 * whereas the values represent the translations.
 * <P>
 * When an application is starting, the database (a '<CODE>properties</CODE>' file) containing the selected locale
 * should be loaded using the {@link I18NL10N#load(String)} or
 * {@link I18NL10N#load(InputStream)} methods. Note that, as opposed to a <CODE>ResourceBundle</CODE>,
 * the <CODE>I18NL10N</CODE> class can easily handle different file locations. Use the
 * {@link I18NL10N#getFilename(String)} and {@link I18NL10N#getFilename(String,String)} methods to
 * specify a file location and an optional locale, for example:
 * <CODE>
 *   I18NL10N.kINSTANCE.load(I18NL10N.kINSTANCE.getFilename("locales-",I18NL10N.kINSTANCE.kLocaleBritishEnglish));
 * </CODE>
 * <P>
 * Translations are performed by the following lookup:
 * <CODE>
 *   String translation = I18NL10N.kINSTANCE.translate(key,parameters);
 * </CODE>
 * <P>
 * The optional parameters are specified in the keys using <CODE>(^i)</CODE>
 * with <CODE>i</CODE> the number of the parameter. In order to get the caret itself,
 * it should be escaped by writing it twice (^^).
 * <P>
 * <B><U>Some examples</U></B>
 * <P>
 * The file should have the following structure: each line should contain at
 * most one (key,value) pair. They are specified as:
 * <CODE>
 *   key=value
 * </CODE>
 * <P>
 * Blank lines are allowed; comments are preceeded by a # character.
 * <UL>
 *   <LI>Consider the following database file:
 *   <UL>
 *     <LI><CODE>my.First.Key=my first value</CODE></LI>
 *     <LI><CODE>my.Second.Key=my ^1 value</CODE></LI>
 *   </UL>
 *   </LI>
 *   <LI>The following translations demonstrate its usage:
 *   <UL>
 *     <LI><CODE>I18NL10N.kINSTANCE.translate("my.First.Key")</CODE> will result in "<CODE>my first value</CODE>".</LI>
 *     <LI><CODE>I18NL10N.kINSTANCE.translate("my.Second.Key","second")</CODE> will result in "<CODE>my second value</CODE>".</LI>
 *   </UL>
 *   </LI>
 *   <LI>A GUI is typically constructed with statements like the following one: <BR>
 *   <UL>
 *     <LI><CODE>JLabel label = new JLabel(I18NL10N.kINSTANCE.translate(&quot;labelKey&quot;,&quot;parameter 1&quot;,...,&quot;parameter N&quot;));</CODE></LI>
 *   </UL>
 *   </LI>
 * </UL>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author Sven Maerivoet
 * @version 26/06/2018
 */
public enum I18NL10N
{
	kINSTANCE;

	// locale-related definitions
	/**
	 * Useful constant for specifying a Dutch locale.
	 */
	public static final String kLocaleDutch = "nl_BE";

	/**
	 * Useful constant for specifying a British English locale.
	 */
	public static final String kLocaleBritishEnglish = Locale.UK.toString();

	/**
	 * Useful constant for specifying an American English locale.
	 */
	public static final String kLocaleAmericanEnglish = Locale.US.toString();

	// access point to the Log4j logging facility
	private static final Logger kLogger = Logger.getLogger(I18NL10N.class.getName());

	// the extension of the locale filenames
	private static final String kLocaleFilenameExtension = ".properties";

	// internal datastructures
	private Properties fTranslationTable;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation.
	 */
	private I18NL10N()
	{
		fTranslationTable = new Properties();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns a filename based on a prefix and locale.
	 * <P>
	 * The system's default locale is used if <CODE>null</CODE> is assigned to the <CODE>localeSpecifier</CODE>. 
	 * 
	 * @param localeFilenamePrefix        the path and prefix name of the file containing the database
	 * @param localeSpecifier             the locale to be used
	 * @return                            a filename based on a prefix and locale
	 * @throws FileDoesNotExistException  if the file containing the database could not be opened or the locale was not found
	 * @see                               I18NL10N#getFilename(String)
	 * @see                               I18NL10N#load(String)
	 * @see                               I18NL10N#load(InputStream)
	 */
	public String getFilename(String localeFilenamePrefix, String localeSpecifier) throws FileDoesNotExistException
	{
		if (localeSpecifier == null) {
			// use system default
			localeSpecifier = Locale.getDefault().toString();
		}

		String[] localeDetails = localeSpecifier.split("_");
		String language = localeDetails[0];
		String country = null;
		if (localeDetails.length > 1) {
			// extract country
			country = localeDetails[1];
		}

		Locale[] locales = Locale.getAvailableLocales();
		boolean validLocale = false;
		for (Locale locale : locales) {
			if (localeSpecifier.equals(locale.toString())) {
	      validLocale = true;
			}
	  }
		if (!validLocale) {
			kLogger.error("Invalid locale specified (" + localeSpecifier + ").");
			throw (new FileDoesNotExistException(localeSpecifier));
		}
		else {
			try {
				if (country == null) {
					Locale.setDefault(new Locale(language));
				}
				else {
					Locale.setDefault(new Locale(language,country));
				}
			}
			catch (SecurityException exc) {
				kLogger.error("Invalid locale specified (" + language + ").");
				throw (new FileDoesNotExistException(language));
			}
		}

		return (localeFilenamePrefix + localeSpecifier + kLocaleFilenameExtension);
	}

	/**
	 * Returns a filename based on a prefix only (the system's default locale is assumed).
	 * 
	 * @param localeFilenamePrefix        the path and prefix name of the file containing the database
	 * @return                            a filename based on a prefix and the system's default locale
	 * @throws FileDoesNotExistException  if the file containing the database could not be opened or the locale was not found
	 * @see                               I18NL10N#getFilename(String,String)
	 * @see                               I18NL10N#load(String)
	 * @see                               I18NL10N#load(InputStream)
	 */
	public String getFilename(String localeFilenamePrefix) throws FileDoesNotExistException
	{
		return getFilename(localeFilenamePrefix,null);
	}

	/**
	 * Loads a database from a specified file.
	 * <P>
	 * Note: all existing keys will be overwritten with the new values.
	 * 
	 * @param localeFilename              the name of the file containing the database
	 * @throws FileDoesNotExistException  if the file containing the database could not be opened
	 * @see                               I18NL10N#getFilename(String)
	 * @see                               I18NL10N#getFilename(String,String)
	 * @see                               I18NL10N#load(InputStream)
	 */
	public void load(String localeFilename) throws FileDoesNotExistException
	{
		TextFileParser fLanguageFile = new TextFileParser(localeFilename);

		while (!fLanguageFile.endOfFileReached()) {
			try {
				extractKeyValuePair(fLanguageFile.getNextString().trim());
			}
			catch (FileParseException exc) {
				// ignore
			}
		}
	}

	/**
	 * Loads a database from an <CODE>InputStream</CODE>.
	 * <P>
	 * Note: all existing keys will be overwritten with the new values.
	 * 
	 * @param languageInputStream  the <CODE>InputStream</CODE> containing the database
	 * @throws FileReadException   if an I/O error occurs during reading from the specified <CODE>InputStream</CODE>
	 * @see                        I18NL10N#getFilename(String)
	 * @see                        I18NL10N#getFilename(String,String)
	 * @see                        I18NL10N#load(String)
	 */
	public void load(InputStream languageInputStream) throws FileReadException
	{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(languageInputStream));
			String line = br.readLine();
			while (line != null) {
				extractKeyValuePair(line);
				line = br.readLine();
			}
		}
		catch (IOException exc) {
			kLogger.error("Error while processing language definition file.");
			throw (new FileReadException(exc.getMessage()));
		}
	}

	/**
	 * Clears the database.
	 */
	public void clear()
	{
		fTranslationTable.clear();
	}

	/**
	 * Returns the descriptive name of the currently used locale.
	 *
	 * @return the descriptive name of the currently used locale
	 */
	public String getCurrentLocaleDescription()
	{
		return Locale.getDefault().getDisplayName();
	}

	/**
	 * Returns the short BCP 47 name of the currently used locale.
	 * <P>
	 * This refers to the IETF's Best Current Practice, see also http://en.wikipedia.org/wiki/IETF_language_tag.
	 *
	 * @return the short BCP 47 name of the currently used locale
	 */
	public String getCurrentLocaleName()
	{
		return Locale.getDefault().toString();
	}

	/**
	 * Returns the currently used locale.
	 *
	 * @return the currently used locale
	 */
	public Locale getCurrentLocale()
	{
		return Locale.getDefault();
	}

	/**
	 * Looks up a key in the database and retrieves its translation.
	 * <P>
	 * Note that when the key is not found, an empty string ("") is returned instead of <CODE>null</CODE>.
	 * <P>
	 * Parameters are identified with a caret ^ (the caret itself is escaped as ^^).
	 * Any redundant specified parameters are ignored.
	 * 
	 * @param languageKey  the key to search for in the database
	 * @param parameters   the optional parameters to substitute in the translated value
	 * @return             the value corresponding to the <CODE>languageKey</CODE>
	 */
	public String translate(String languageKey, String ... parameters)
	{
		// retrieve value from database
		String value = fTranslationTable.getProperty(languageKey);

		if (value == null) {
			kLogger.warn("Language key (" + languageKey + ") not found in language definition file.");
			return "";
		}
		String fParsed = "";

		// parse the translation
		for (int strPos = 0; strPos < value.length(); ++strPos) {
			String fToAdd = "";

			// was a parameter specified ?
			if (value.charAt(strPos) == '^') {

				++strPos;

				// have we reached the end of the string ?
				if (strPos < value.length()) {

					// do we need to escape the caret ?
					if (value.charAt(strPos) == '^') {
						fToAdd = "^";
					}
					else {
						// extract the parameter's number
						try {
							int parameterNr = Integer.parseInt(value.substring(strPos,strPos + 1)) - 1;

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
				fToAdd = String.valueOf(value.charAt(strPos));
			}

			fParsed = fParsed.concat(fToAdd);
		}

		return fParsed;
	}

	/**
	 * Returns the <CODE>KeyEvent</CODE> code associated with the specified mnemonic.
	 * <P>
	 * A mnemonic consists of a single character; this method does however relaxes
	 * this assumption, as it allows for a complete <CODE>String</CODE> to be
	 * specified (only the first character is considered). So its convenient for
	 * the caller to pass a <I>key</I> as parameter to this method.
	 * 
	 * @param mnemonic  the mnemonic to retrieve the <CODE>KeyEvent</CODE> code from
	 * @return          the <CODE>KeyEvent</CODE> code associated with the specified mnemonic (<CODE>null</CODE> if the mnemonic was empty)
	 */
	public Integer translateMnemonic(final String mnemonic)
	{
		if ((mnemonic == null) || (mnemonic.length() == 0)) {
			return null;
		}

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

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 * @param line  -
	 */
	private void extractKeyValuePair(String line)
	{
		// ignore empty lines and comments
		if ((line.length() > 0) && (!line.startsWith("#"))) {
			// separate key from value
			int equalPos = line.indexOf("=");

			// ignore when there is no key
			if (equalPos != 0) {
				// re-use the key if no value was specified
				String key = line;
				String value = key;

				if (equalPos > 0) {
					key = line.substring(0,equalPos).trim();
					value = line.substring(equalPos + 1).trim();
				}

				// store (key,value) pair
				fTranslationTable.setProperty(key,value);
			}
		}
	}
}
