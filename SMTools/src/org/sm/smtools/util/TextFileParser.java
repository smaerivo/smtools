// -----------------------------------
// Filename      : TextFileParser.java
// Author        : Sven Maerivoet
// Last modified : 21/09/2016
// Target        : Java VM (1.8)
// -----------------------------------

/**
 * Copyright 2003-2016 Sven Maerivoet
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

package org.sm.smtools.util;

import java.io.*;
import java.util.*;
import org.sm.smtools.exceptions.*;

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
 * @version 21/09/2016
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
	 * @param filename                    the name of the file to parse
	 * @throws FileDoesNotExistException  if the file is not found
	 */
	public TextFileParser(String filename) throws FileDoesNotExistException
	{
		try {
			// try to open the file
			FileInputStream fileInputStream = new FileInputStream(filename);
			initialise(fileInputStream,null);
		}
		catch (FileNotFoundException exc) {
			throw (new FileDoesNotExistException(filename));
		}
	}

	/**
	 * Sets up a text file parser for the specified file.
	 *
	 * @param filename                    the name of the file to parse
	 * @param encoding                    the encoding used (e.g., UTF-8)
	 * @throws FileDoesNotExistException  if the file is not found
	 */
	public TextFileParser(String filename, String encoding) throws FileDoesNotExistException
	{
		try {
			// try to open the file
			FileInputStream fileInputStream = new FileInputStream(filename);
			initialise(fileInputStream,encoding);
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
	 * @param inputStream  the <CODE>InputStream</CODE> containing the contents to parse
	 */
	public TextFileParser(InputStream inputStream)
	{
		initialise(inputStream,null);
	}

	/**
	 * Sets up a text file parser for the specified <CODE>InputStream</CODE>.
	 * <P>
	 * Note that a buffer is automatically wrapped around the specified <CODE>InputStream</CODE>.
	 *
	 * @param encoding     the encoding used (e.g., UTF-8)
	 * @param inputStream  the <CODE>InputStream</CODE> containing the contents to parse
	 */
	public TextFileParser(InputStream inputStream, String encoding)
	{
		initialise(inputStream,encoding);
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
	 * @return                     the next line converted to a <CODE>String</CODE>
	 * @throws FileParseException  if the end-of-file is reached (the exception only contains the line number)
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
	 * Returns the next non-empty line converted to a <CODE>String</CODE>.
	 * <P>
	 * Note that any whitespace surrounding the <CODE>String</CODE> is automatically trimmed.
	 *
	 * @return                     the next non-empty line converted to a <CODE>String</CODE>
	 * @throws FileParseException  if the end-of-file is reached (the exception only contains the line number)
	 */
	public String getNextNonEmptyString() throws FileParseException
	{
		String nextNonEmptyString = "";
		while (nextNonEmptyString.length() == 0) {
			if (!endOfFileReached()) {
				++fLineNr;
				nextNonEmptyString = fContentScanner.nextLine().trim();
			}
			else {
				throw (new FileParseException("",fLineNr));
			}
		}
		return nextNonEmptyString;
	}

	/**
	 * Returns the next line converted to a <CODE>char</CODE> (empty lines are ignored).
	 *
	 * @return                     the next line converted to a <CODE>char</CODE>
	 * @throws FileParseException  if the end-of-file is reached (the exception only contains the line number)
	 * @see                        TextFileParser#getNextString
	 */
	public char getNextChar() throws FileParseException
	{
		return getNextNonEmptyString().charAt(0);
	}

	/**
	 * Returns the next line converted to a <CODE>byte</CODE> (empty lines are ignored).
	 *
	 * @return                     the next line converted to a <CODE>byte</CODE>
	 * @throws FileParseException  if the end-of-file is reached or the line contains a malformed byte (the exception contains the value and line number)
	 * @see                        TextFileParser#getNextString
	 */
	public byte getNextByte() throws FileParseException
	{
		String stringRead = getNextNonEmptyString();
		try {
			return Byte.parseByte(stringRead);
		}
		catch (NumberFormatException exc) {
			throw (new FileParseException(stringRead,fLineNr));
		}
	}

	/**
	 * Returns the next line converted to an <CODE>int</CODE> (empty lines are ignored).
	 *
	 * @return                     the next line converted to an <CODE>int</CODE>
	 * @throws FileParseException  if the end-of-file is reached or the line contains a malformed integer (the exception contains the value and line number)
	 * @see                        TextFileParser#getNextString
	 */
	public int getNextInteger() throws FileParseException
	{
		String stringRead = getNextNonEmptyString();
		try {
			return Integer.parseInt(stringRead);
		}
		catch (NumberFormatException exc) {
			throw (new FileParseException(stringRead,fLineNr));
		}
	}

	/**
	 * Returns the next line converted to a <CODE>double</CODE> (empty lines are ignored).
	 *
	 * @return                     the next line converted to a <CODE>double</CODE>
	 * @throws FileParseException  if the end-of-file is reached or the line contains a malformed double (the exception contains the value and line number)
	 * @see                        TextFileParser#getNextString
	 */
	public double getNextDouble() throws FileParseException
	{
		String stringRead = getNextNonEmptyString();
		try {
			return Double.parseDouble(stringRead);
		}
		catch (NumberFormatException exc) {
			throw (new FileParseException(stringRead,fLineNr));
		}
	}

	/**
	 * Returns the next line converted to a <CODE>boolean</CODE> (empty lines are ignored).
	 * <P>
	 * The following truth values are recognised:
	 * <UL>
	 *   <LI><B>yes / true / 1</B></LI>
	 *   <LI><B>no / false / 0</B></LI>
	 * </UL>
	 *
	 * @return                     the next line converted to a <CODE>boolean</CODE>
	 * @throws FileParseException  if the end-of-file is reached or the line contains a malformed boolean (the exception contains the value and line number)
	 * @see                        TextFileParser#getNextString
	 */
	public boolean getNextBoolean() throws FileParseException
	{
		String stringRead = getNextNonEmptyString();
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
	 * Returns the next line converted to a <CODE>DateStamp</CODE> (empty lines are ignored).
	 * <P>
	 * The string has to have the following specific format:
	 * <P>
	 * <B>dd/MM/yyyy</B>, e.g., 11/04/1976
	 *
	 * @return                     the next line converted to a <CODE>DateStamp</CODE>
	 * @throws FileParseException  if the end-of-file is reached or the line contains a malformed date stamp (the exception contains the value and line number)
	 * @see                        DateStamp
	 */
	public DateStamp getNextDateStamp() throws FileParseException
	{
		String stringRead = getNextNonEmptyString();
		try {
			return (new DateStamp(stringRead));
		}
		catch (DateTimeFormatException exc) {
			throw (new FileParseException(stringRead,fLineNr));
		}
	}

	/**
	 * Returns the next line converted to a <CODE>TimeStamp</CODE> (empty lines are ignored).
	 * <P>
	 * The string has to have the following specific format:
	 * <P>
	 * <B>HH:mm:ss</B> or <B>HH:mm:ss.mls</B>, e.g., 12:45:16 or 05:03:06.002
	 *
	 * @return                     the next line converted to a <CODE>TimeStamp</CODE>
	 * @throws FileParseException  if the end-of-file is reached or the line contains a malformed time stamp (the exception contains the value and line number)
	 * @see                        TimeStamp
	 */
	public TimeStamp getNextTimeStamp() throws FileParseException
	{
		String stringRead = getNextNonEmptyString();
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
	 * @return                     the next line converted to a <CODE>String[]</CODE> array of comma-separated values
	 * @throws FileParseException  if the end-of-file is reached (the exception only contains the line number)
	 */
	public String[] getNextCSV() throws FileParseException
	{
		return getNextCSV(',');
	}

	/**
	 * Returns the next line converted to a <CODE>String[]</CODE> array of comma-separated values with a specified split character.
	 * <P>
	 * The CSV parser can operate on quoted, unquoted and empty <CODE>String</CODE>s.
	 *
	 * @param splitChar            the character used to split the CSV record
	 * @return                     the next line converted to a <CODE>String[]</CODE> array of comma-separated values
	 * @throws FileParseException  if the end-of-file is reached (the exception only contains the line number)
	 */
	public String[] getNextCSV(char splitChar) throws FileParseException
	{
		return StringTools.convertToCSV(getNextString(),splitChar);
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

	/******************
	 * STATIC METHODS *
	 ******************/

	/**
	 * Loads a file containing comma-separated values (empty lines are ignored).
	 * <P>
	 * Commented lines are preceeded by a hash-tag (#).
	 *
	 * @param filename        the name of the CSV file to load
	 * @param ignoreComments  a <CODE>boolean</CODE> indicating whether or not comments should be ignored 
	 * @return                a sequence of CSV values, <CODE>null</CODE> is a problem occurred
	 */
	public static ArrayList<String[]> loadGeneralCSVFile(String filename, boolean ignoreComments)
	{
		try {
			TextFileParser tfp = new TextFileParser(filename);

			// sequentially load file
			ArrayList<String[]> result = new ArrayList<String[]>();
			while (!tfp.endOfFileReached()) {
				String[] csvRead = tfp.getNextCSV();
				if (ignoreComments && (csvRead.length > 0) && (!StringTools.isComment(csvRead[0]))) {
					result.add(csvRead);
				}
			}

			return result;
		}
		catch (Exception exc) {
			return null;
		}
	}

	/**
	 * Loads a file containing a single column of <CODE>double</CODE>s (empty lines are ignored).
	 *
	 * @param filename  the name of the file to load
	 * @return          a sequence of <CODE>double</CODE>s, <CODE>null</CODE> is a problem occurred
	 */
	public static double[] loadDoubleFile(String filename)
	{
		try {
			TextFileParser tfp = new TextFileParser(filename);

			// sequentially load file
			ArrayList<Double> data = new ArrayList<Double>();
			while (!tfp.endOfFileReached()) {
				data.add(tfp.getNextDouble());
			}

			int nrOfValues = data.size();
			double[] result = new double[nrOfValues];
			for (int i = 0; i < nrOfValues; ++i) {
				result[i] = data.get(i);
			}

			return result;
		}
		catch (Exception exc) {
			return null;
		}
	}

	/**
	 * Loads a file containing a table with comma-separated <CODE>double</CODE>s (empty lines are ignored).
	 * <P>
	 * Commented lines preceeded by a hash-tag (#) are automatically ignored.
	 *
	 * @param                      filename the name of the file to load
	 * @return                     a sequence of <CODE>double</CODE>s
	 * @throws FileParseException  if a malformed number was encountered
	 */
	public static ArrayList<Double[]> loadDoubleCSVFile(String filename) throws FileParseException
	{
		ArrayList<Double[]> convertedData = new ArrayList<Double[]>();

		for (String[] csv : loadGeneralCSVFile(filename,true)) {
			Double[] dataArray = new Double[csv.length];
			int i = 0;
			for (String s : csv) {
				try {
					dataArray[i] = Double.parseDouble(s);
				}
				catch (NumberFormatException exc) {
					throw (new FileParseException(filename,s));
				}
				++i;
			}
			convertedData.add(dataArray);
		}

		return convertedData;
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 * @param inputStream  -
	 * @param encoding     -
	 */
	private void initialise(InputStream inputStream, String encoding)
	{
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		if (encoding == null) {
			fContentScanner = new Scanner(bufferedInputStream);
		}
		else {
			fContentScanner = new Scanner(bufferedInputStream,encoding);
		}
		fLineNr = 0;
	}
}
