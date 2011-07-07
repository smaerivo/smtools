// -----------------------------------
// Filename      : TextFileParser.java
// Author        : Sven Maerivoet
// Last modified : 22/04/2011
// Target        : Java VM (1.6)
// -----------------------------------

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

package smtools.miscellaneous;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import smtools.exceptions.*;

/**
 * The <CODE>TextFileParser</CODE> class allows easy parsing of text files.
 * <P>
 * In the text file to parse, each line may contain at most one value.
 * <P>
 * The recognised datatypes are:
 * <UL>
 *   <LI><CODE>String</CODE> (unquoted)</LI>
 *   <LI><CODE>char</CODE></LI>
 *   <LI><CODE>byte</CODE></LI>
 *   <LI><CODE>int</CODE></LI>
 *   <LI><CODE>double</CODE></LI>
 *   <LI><CODE>boolean</CODE> (yes, true, 1, no, false and 0)</LI>
 *   <LI><CODE>DateStamp</CODE> (dd/MM/yyyy)</LI>
 *   <LI><CODE>TimeStamp</CODE> (HH:mm:ss or HH:mm:ss.mls)</LI>
 *   <LI>comma-separated values (CSV)</LI>
 * </UL>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 22/04/2011
 */
public final class TextFileParser
{
	// internal datastructures
	private Scanner fContentScanner;
	private int fLineNr;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Sets up a text file parser for the specified file.
	 *
	 * @param  filename the name of the file to parse
	 * @throws FileDoesNotExistException if the file is not found
	 */
	public TextFileParser(String filename) throws FileDoesNotExistException
	{
		try {
			// try to open the file
			FileInputStream fileInputStream = new FileInputStream(filename);
			initialise(fileInputStream);
		}
		catch (FileNotFoundException exc) {
			throw (new FileDoesNotExistException(filename));
		}
	}

	/**
	 * Sets up a text file parser for the specified <CODE>InputStream</CODE>.
	 * <P>
	 * Note that a buffer is automatically wrapped around the specified <CODE>InputStream</CODE>.
	 *
	 * @param  inputStream the <CODE>InputStream</CODE> containing the contents to parse
	 */
	public TextFileParser(InputStream inputStream)
	{
		initialise(inputStream);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Returns whether or not the parsing has reached the end of the file.
	 *
	 * @return <CODE>true</CODE> if the end of the file is reached, <CODE>false</CODE> otherwise
	 */
	public boolean endOfFileReached()
	{
		return !fContentScanner.hasNext();
	}

	/**
	 * Returns the number of the line that the parser last tried to read.
	 *
	 * @return the number of the line that the parser last tried to read
	 */
	public int getLastReadLineNr()
	{
		return fLineNr;
	}

	/**
	 * Returns the next line converted to a <CODE>String</CODE>.
	 * <P>
	 * Note that any whitespace surrounding the <CODE>String</CODE> is automatically trimmed.
	 *
	 * @return the next line converted to a <CODE>String</CODE>
	 * @throws FileParseException if the end-of-file is reached (the exception only contains the line number)
	 */
	public String getNextString() throws FileParseException
	{
		if (!endOfFileReached()) {
			++fLineNr;
			return fContentScanner.nextLine().trim();
		}
		else {
			throw (new FileParseException("",fLineNr));
		}
	}

	/**
	 * Returns the next line converted to a <CODE>char</CODE>.
	 *
	 * @return the next line converted to a <CODE>char</CODE>
	 * @throws FileParseException if the end-of-file is reached (the exception only contains the line number)
	 * @see    TextFileParser#getNextString
	 */
	public char getNextChar() throws FileParseException
	{
		return getNextString().charAt(0);
	}

	/**
	 * Returns the next line converted to a <CODE>byte</CODE>.
	 *
	 * @return the next line converted to a <CODE>byte</CODE>
	 * @throws FileParseException if the end-of-file is reached or the line contains a malformed byte (the exception contains the value and line number)
	 * @see    TextFileParser#getNextString
	 */
	public byte getNextByte() throws FileParseException
	{
		String stringRead = getNextString();
		try {
			return Byte.parseByte(stringRead);
		}
		catch (NumberFormatException exc) {
			throw (new FileParseException(stringRead,fLineNr));
		}
	}

	/**
	 * Returns the next line converted to an <CODE>int</CODE>.
	 *
	 * @return the next line converted to an <CODE>int</CODE>
	 * @throws FileParseException if the end-of-file is reached or the line contains a malformed integer (the exception contains the value and line number)
	 * @see    TextFileParser#getNextString
	 */
	public int getNextInteger() throws FileParseException
	{
		String stringRead = getNextString();
		try {
			return Integer.parseInt(stringRead);
		}
		catch (NumberFormatException exc) {
			throw (new FileParseException(stringRead,fLineNr));
		}
	}

	/**
	 * Returns the next line converted to a <CODE>double</CODE>.
	 *
	 * @return the next line converted to a <CODE>double</CODE>
	 * @throws FileParseException if the end-of-file is reached or the line contains a malformed double (the exception contains the value and line number)
	 * @see    TextFileParser#getNextString
	 */
	public double getNextDouble() throws FileParseException
	{
		String stringRead = getNextString();
		try {
			return Double.parseDouble(stringRead);
		}
		catch (NumberFormatException exc) {
			throw (new FileParseException(stringRead,fLineNr));
		}
	}

	/**
	 * Returns the next line converted to a <CODE>boolean</CODE>.
	 * <P>
	 * The following truth values are recognised:
	 * <P>
	 * <UL>
	 *   <LI><B>yes / true / 1</B></LI>
	 *   <LI><B>no / false / 0</B></LI>
	 * </UL>
	 *
	 * @return the next line converted to a <CODE>boolean</CODE>
	 * @throws FileParseException if the end-of-file is reached or the line contains a malformed boolean (the exception contains the value and line number)
	 * @see    TextFileParser#getNextString
	 */
	public boolean getNextBoolean() throws FileParseException
	{
		String stringRead = getNextString();
		if (stringRead.equalsIgnoreCase("YES") || stringRead.equalsIgnoreCase("TRUE") || stringRead.equalsIgnoreCase("1")) {
			return true;
		}
		if (stringRead.equalsIgnoreCase("NO") || stringRead.equalsIgnoreCase("FALSE") || stringRead.equalsIgnoreCase("0")) {
			return false;
		}
		else {
			throw (new FileParseException(stringRead,fLineNr));
		}
	}

	/**
	 * Returns the next line converted to a <CODE>DateStamp</CODE>.
	 * <P>
	 * The string has to have the following specific format:
	 * <P>
	 * <UL>
	 *   <B>dd/MM/yyyy</B>, e.g., 11/04/1976
	 * </UL>
	 *
	 * @return the next line converted to a <CODE>DateStamp</CODE>
	 * @throws FileParseException if the end-of-file is reached or the line contains a malformed date stamp (the exception contains the value and line number)
	 * @see    DateStamp
	 */
	public DateStamp getNextDateStamp() throws FileParseException
	{
		String stringRead = getNextString();
		try {
			return (new DateStamp(stringRead));
		}
		catch (DateTimeFormatException exc) {
			throw (new FileParseException(stringRead,fLineNr));
		}
	}

	/**
	 * Returns the next line converted to a <CODE>TimeStamp</CODE>.
	 * <P>
	 * The string has to have the following specific format:
	 * <P>
	 * <UL>
	 *   <B>HH:mm:ss</B> or <B>HH:mm:ss.mls</B>, e.g., 12:45:16 or 05:03:06.002
	 * </UL>
	 *
	 * @return the next line converted to a <CODE>TimeStamp</CODE>
	 * @throws FileParseException if the end-of-file is reached or the line contains a malformed time stamp (the exception contains the value and line number)
	 * @see    TimeStamp
	 */
	public TimeStamp getNextTimeStamp() throws FileParseException
	{
		String stringRead = getNextString();
		try {
			return (new TimeStamp(stringRead));
		}
		catch (DateTimeFormatException exc) {
			throw (new FileParseException(stringRead,fLineNr));
		}
	}

	/**
	 * Returns the next line converted to a <CODE>String[]</CODE> array of comma-separated values.
	 * <P>
	 * The CSV parser can operate on quoted, unquoted and empty <CODE>String</CODE>s.
	 *
	 * @return the next line converted to a <CODE>String[]</CODE> array of comma-separated values
	 * @throws FileParseException if the end-of-file is reached (the exception only contains the line number)
	 */
	public String[] getNextCSV() throws FileParseException
	{
		// match quoted, unquoted and null fields
		String kCSVPattern = "\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\",?|([^,]+),?|,";
		Pattern csvRegEx = Pattern.compile(kCSVPattern);
		Matcher csvMatcher = csvRegEx.matcher(getNextString());

		// extract all fields
		ArrayList<String> list = new ArrayList<String>();
		while (csvMatcher.find()) {
			String match = csvMatcher.group();
			if (match == null) {
				break;
			}
			if (match.endsWith(",")) {
				// trim trailing separator
				match = match.substring(0,match.length() - 1);
			}
			if (match.endsWith("\"")) {
				// trim leading double quote
				match = match.substring(1,match.length() - 1);
			}
			list.add(match);
		}
		
		// convert to an array of strings
		String[] csvValues = new String[list.size()];
		list.toArray(csvValues);
		return csvValues;
	}

	/**
	 * Returns all the lines as a <CODE>StringBuilder</CODE> object.
	 * <P>
	 * Note that any whitespace surrounding the lines is automatically trimmed.
	 *
	 * @return all the lines converted to a <CODE>StringBuilder</CODE> object
	 * @see    TextFileParser#getNextString
	 * @see    TextFileParser#getAllLinesAsStringArray
	 */
	public StringBuilder getAllLines()
	{
		StringBuilder contents = new StringBuilder();
		try {
			while (!endOfFileReached()) {
				contents.append(getNextString() + StringTools.kEOLCharacterSequence);
			}
		}
		catch (FileParseException exc) {
			// ignore
		}
		return contents;
	}

	/**
	 * Returns all the lines as a <CODE>String[]</CODE> array.
	 * <P>
	 * Note that any whitespace surrounding the lines is automatically trimmed.
	 *
	 * @return all the lines converted to a <CODE>String[]</CODE> array
	 * @see    TextFileParser#getNextString
	 * @see    TextFileParser#getAllLines
	 */
	public String[] getAllLinesAsStringArray()
	{
		return getAllLines().toString().split(StringTools.kEOLCharacterSequence);
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	private void initialise(InputStream inputStream)
	{
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		fContentScanner = new Scanner(bufferedInputStream);
		fLineNr = 0;
	}
}
