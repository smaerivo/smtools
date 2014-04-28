// -----------------------------------
// Filename      : TextFileWriter.java
// Author        : Sven Maerivoet
// Last modified : 22/04/2011
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

package org.sm.smtools.miscellaneous;

import java.io.*;
import org.sm.smtools.exceptions.*;

/**
 * The <CODE>TextFileWriter</CODE> class allows easy writing to text files.
 * <P>
 * In the resulting text file, each line will contain at most one value.
 * <P>
 * The available datatypes to write are:
 * <UL>
 *   <LI><CODE>int</CODE></LI>
 *   <LI><CODE>double</CODE></LI>
 *   <LI><CODE>String</CODE> (unquoted)</LI>
 *   <LI><CODE>boolean</CODE> (true and false)</LI>
 *   <LI><CODE>Date</CODE> (dd/mm/yyyy)</LI>
 *   <LI><CODE>Time</CODE> (hh:mm:ss.ms)</LI>
 *   <LI>comma-separated values (CSV)</LI>
 * </UL>
 *
 * @author  Sven Maerivoet
 * @version 22/04/2011
 */
public class TextFileWriter
{
	// internal datastructures
	private PrintWriter fFileWriter;
	private String fFilename;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Sets up a text file writer for the specified file.
	 *
	 * @param  filename                   the name of the file to create
	 * @throws FileCantBeCreatedException if the file cannot be created
	 */
	public TextFileWriter(String filename) throws FileCantBeCreatedException
	{
		this(filename,false);
	}

	/**
	 * Sets up a text file writer for the specified file.
	 *
	 * @param  filename                   the name of the file to create
	 * @param  append                     whether or not the (existing) file should be appended
	 * @throws FileCantBeCreatedException if the file cannot be created
	 */
	public TextFileWriter(String filename, boolean append) throws FileCantBeCreatedException
	{
		fFilename = filename;

		try {
			// try to create the file
			FileOutputStream fileOutputStream = new FileOutputStream(fFilename,append);
			fFileWriter = new PrintWriter(fileOutputStream);
		}
		catch (IOException exc) {
			throw (new FileCantBeCreatedException(fFilename));
		}
	}

	/**************
	 * DESTRUCTOR *
	 **************/

	/**
	 * Class destructor.
	 */
	public final void finalize()
	{
		fFileWriter.close();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Writes an empty line to the file.
	 *
	 * @throws FileWriteException if an error during the writing occurred
	 */
	public final void writeLn() throws FileWriteException
	{
		fFileWriter.println();

		if (fFileWriter.checkError()) {
			throw (new FileWriteException(fFilename,""));
		}
	}

	/**
	 * Writes a string representation of an <CODE>int</CODE> to the file.
	 *
	 * @param  i                  the <CODE>int</CODE> to write to the file
	 * @throws FileWriteException if the <CODE>int</CODE> could not be written to the file
	 */
	public final void writeInt(int i) throws FileWriteException
	{
		fFileWriter.print(i);

		if (fFileWriter.checkError()) {
			throw (new FileWriteException(fFilename,String.valueOf(i)));
		}
	}

	/**
	 * Writes a string representation of a <CODE>double</CODE> to the file.
	 *
	 * @param  d                  the <CODE>double</CODE> to write to the file
	 * @throws FileWriteException if the <CODE>double</CODE> could not be written to the file
	 */
	public final void writeDouble(double d) throws FileWriteException
	{
		fFileWriter.print(d);

		if (fFileWriter.checkError()) {
			throw (new FileWriteException(fFilename,String.valueOf(d)));
		}
	}

	/**
	 * Writes a string to the file.
	 *
	 * @param  s                  the <CODE>String</CODE> to write to the file
	 * @throws FileWriteException if the <CODE>String</CODE> could not be written to the file
	 */
	public final void writeString(String s) throws FileWriteException
	{
		fFileWriter.print(s);

		if (fFileWriter.checkError()) {
			throw (new FileWriteException(fFilename,s));
		}
	}

	/**
	 * Writes a string representation of a <CODE>boolean</CODE> to the file.
	 * <P>
	 * The string representation is either <B>TRUE</B> or <B>FALSE</B>.
	 *
	 * @param  b                  the <CODE>boolean</CODE> to write to the file
	 * @throws FileWriteException if the <CODE>boolean</CODE> could not be written to the file
	 */
	public final void writeBoolean(boolean b) throws FileWriteException
	{
		String boolString = "FALSE";
		if (b) {
			boolString = "TRUE";
		}
		fFileWriter.print(boolString);

		if (fFileWriter.checkError()) {
			throw (new FileWriteException(fFilename,boolString));
		}
	}

	/**
	 * Writes a string representation of a <CODE>DateStamp</CODE> object to the file.
	 * <P>
	 * The string representation of the <CODE>DateStamp</CODE> object is
	 * <B>dd/mm/yyyy</B>, e.g., "11/04/1976".
	 *
	 * @param  dateStamp the <CODE>DateStamp</CODE> object to write to the file
	 * @throws FileWriteException if the <CODE>DateStamp</CODE> object could not be written to the file
	 */
	public final void writeDate(DateStamp dateStamp) throws FileWriteException
	{
		String dateString = dateStamp.getDMYString();
		fFileWriter.print(dateString);

		if (fFileWriter.checkError()) {
			throw (new FileWriteException(fFilename,dateString));
		}
	}

	/**
	 * Writes a string representation of a <CODE>TimeStamp</CODE> object to the file.
	 * <P>
	 * The string representation of the <CODE>TimeStamp</CODE> object is
	 * <B>hh:mm:ss.ms</B>, e.g., "12:45:16.154".
	 *
	 * @param  timeStamp          the <CODE>TimeStamp</CODE> object to write to the file
	 * @throws FileWriteException if the <CODE>TimeStamp</CODE> object could not be written to the file
	 */
	public final void writeTimeStamp(TimeStamp timeStamp) throws FileWriteException
	{
		String timeString = timeStamp.getHMSMsString();
		fFileWriter.print(timeString);

		if (fFileWriter.checkError()) {
			throw (new FileWriteException(fFilename,timeString));
		}
	}

	/**
	 * Write comma-separated values (CSV) to the file.
	 * <P>
	 * Note that all strings are automatically quoted.
	 *
	 * @param  csvValues          the comma-separated values to write to the file
	 * @throws FileWriteException if the <CODE>TimeStamp</CODE> object could not be written to the file
	 */
	public final void writeCSV(String[] csvValues) throws FileWriteException
	{
		for (int i = 0; i < csvValues.length; ++i) {
			String csvValue = csvValues[i];

			// quote strings
			try {
				Double.parseDouble(csvValue);
				// it's a number
				fFileWriter.print(csvValue);
			}
			catch (NumberFormatException exc) {
				// it's a string; quote it
				fFileWriter.print("\"" + csvValue + "\"");
			}

			if (i < (csvValues.length - 1)) {
				fFileWriter.print(",");
			}

			if (fFileWriter.checkError()) {
				throw (new FileWriteException(fFilename,csvValue));
			}
		}
	}
}
