// --------------------------------
// Filename      : StringTools.java
// Author        : Sven Maerivoet
// Last modified : 05/05/2014
// Target        : Java VM (1.8)
// --------------------------------

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

package org.sm.smtools.util;

import java.text.*;
import java.util.*;
import org.sm.smtools.math.*;

/**
 * The <CODE>StringTools</CODE> class is mainly intended for string alignment operations.
 * <P>
 * Using this class, strings can be aligned left, right or centered. Aggressive alignment is
 * also supported, in the sense that any string excess is truncated after alignment.
 * <P>
 * Furthermore, lines of characters can be created (e.g., dashed lines) and doubles
 * can be converted to strings (with respect to a certain number of decimals).
 * <P>
 * All methods in this class are static, so they should be invoked as:
 * <P>
 * <CODE>... = StringTools.method(...);</CODE>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 05/05/2014
 */
public final class StringTools
{
	// the default end-of-line (EOL) character sequence
	private static final String kDefaultEOLCharacterSequence = "\n";

	/**
	 * The platform-dependent end-of-line (EOL) character sequence.
	 */
	public static final String kEOLCharacterSequence = System.getProperty("line.separator",kDefaultEOLCharacterSequence);

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Prevent instantiation.
	 */
	private StringTools()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Aligns a string left, padding with extra characters if necessary.
	 *
	 * @param stringToAlign   the string that should be aligned left
	 * @param nrOfCharacters  the minimum length of the string after the alignment
	 * @param padCharacter    the character used to pad the string at the right end
	 * @return                the left aligned string
	 */
	public static String alignLeft(String stringToAlign, int nrOfCharacters, char padCharacter)
	{
		int nrOfCharactersToPad = nrOfCharacters - stringToAlign.length();

		if (nrOfCharactersToPad <= 0) {
			// no padding necessary
			return stringToAlign;
		}

		String result = stringToAlign;

		for (int i = 0; i < nrOfCharactersToPad; ++i) {
			result += padCharacter;
		}

		return result;
	}

	/**
	 * Aligns a string right, padding with extra characters if necessary.
	 *
	 * @param stringToAlign   the string that should be aligned right
	 * @param nrOfCharacters  the minimum length of the string after the alignment
	 * @param padCharacter    the character used to pad the string at the left end
	 * @return                the right aligned string
	 */
	public static String alignRight(String stringToAlign, int nrOfCharacters, char padCharacter)
	{
		int nrOfCharactersToPad = nrOfCharacters - stringToAlign.length();

		if (nrOfCharactersToPad <= 0) {
			// no padding necessary
			return stringToAlign;
		}

		String result = "";

		for (int i = 0; i < nrOfCharactersToPad; ++i) {
			result += padCharacter;
		}

		result += stringToAlign;

		return result;
	}

	/**
	 * Centers a string, padding with extra characters if necessary.
	 *
	 * @param stringToCenter  the string that should be centered
	 * @param nrOfCharacters  the minimum length of the string after the centering
	 * @param padCharacter    the character used to pad the string at the left and right ends
	 * @return                the centered string
	 */
	public static String alignCenter(String stringToCenter, int nrOfCharacters, char padCharacter)
	{
		int nrOfCharactersToPad = nrOfCharacters - stringToCenter.length();

		if (nrOfCharactersToPad <= 0) {
			// no padding necessary
			return stringToCenter;
		}

		int nrOfCharactersToPadLeft = (nrOfCharactersToPad / 2);
		int nrOfCharactersToPadRight = (nrOfCharactersToPad - nrOfCharactersToPadLeft);

		String result = "";

		// pad left side
		for (int i = 0; i < nrOfCharactersToPadLeft; ++i) {
			result += padCharacter;
		}

		// add string to center
		result += stringToCenter;

		// pad right side
		for (int i = 0; i < nrOfCharactersToPadRight; ++i) {
			result += padCharacter;
		}

		return result;
	}

	/**
	 * Truncates the end of a string.
	 *
	 * @param stringToTruncate  the string that should be truncated
	 * @param nrOfCharacters    the maximum length of the string after the truncation
	 * @return                  the truncated string
	 */
	public static String truncate(String stringToTruncate, int nrOfCharacters)
	{
		if (stringToTruncate.length() <= nrOfCharacters) {
			return stringToTruncate;
		}
		else {
			return stringToTruncate.substring(0,nrOfCharacters);
		}
	}

	/**
	 * Aggressively aligns a string left, padding with extra characters if necessary.
	 * <P>
	 * If the length of the resulting string is too long, the end is truncated.
	 *
	 * @param stringToAlign   the string that should be aggressively aligned left
	 * @param nrOfCharacters  the maximum length of the string after the aggressive alignment
	 * @param padCharacter    the character used to pad the string at the right end
	 * @return                the left aggressively aligned string
	 * @see                   StringTools#alignLeft(String,int,char)
	 * @see                   StringTools#truncate(String,int)
	 */
	public static String aggressiveAlignLeft(String stringToAlign, int nrOfCharacters,
			char padCharacter)
	{
		return truncate(alignLeft(stringToAlign,nrOfCharacters,padCharacter),nrOfCharacters);
	}

	/**
	 * Aggressively aligns a string right, padding with extra characters if necessary.
	 * <P>
	 * If the length of the resulting string is too long, the end is truncated.
	 *
	 * @param stringToAlign   the string that should be aggressively aligned right
	 * @param nrOfCharacters  the maximum length of the string after the aggressive alignment
	 * @param padCharacter    the character used to pad the string at the left end
	 * @return                the right aggressively aligned string
	 * @see                   StringTools#alignRight(String,int,char)
	 * @see                   StringTools#truncate(String,int)
	 */
	public static String aggressiveAlignRight(String stringToAlign, int nrOfCharacters,
			char padCharacter)
	{
		return truncate(alignRight(stringToAlign,nrOfCharacters,padCharacter),nrOfCharacters);
	}

	/**
	 * Aggressively centers a string, padding with extra characters if necessary.
	 * <P>
	 * If the length of the resulting string is too long, the end is truncated.
	 *
	 * @param stringToCenter  the string that should be aggressively centered
	 * @param nrOfCharacters  the maximum length of the string after the aggressive centering
	 * @param padCharacter    the character used to pad the string at the left and right ends
	 * @return                the aggressively centered string
	 * @see                   StringTools#alignCenter(String,int,char)
	 * @see                   StringTools#truncate(String,int)
	 */
	public static String aggressiveAlignCenter(String stringToCenter, int nrOfCharacters, char padCharacter)
	{
		return truncate(alignCenter(stringToCenter,nrOfCharacters,padCharacter),nrOfCharacters);
	}

	/**
	 * Capitalises the first letter in a string.
	 * 
	 * @param text  the text to capitalise the first letter of
	 * @return      the text with the first letter capitalised
	 */
	public static String capitaliseFirstLetter(String text)
	{
		return (text.substring(0,1).toUpperCase() + text.substring(1));
	}

	/**
	 * Creates a string containing a repetition of a character.
	 * <P>
	 * As an example, a dashed line of length 5 can be created as:
	 * <P>
	 * <CODE>StringTools.createLineOfCharacters(5,'-')</CODE>
	 * <P>
	 * The result will be "-----".
	 *
	 * @param length         the total length of the line
	 * @param lineCharacter  the character that is used to create the line
	 * @return               a line containing the repetition of the specified character
	 */
	public static String createLineOfCharacters(int length, char lineCharacter)
	{
		return alignLeft("",length,lineCharacter);
	}

	/**
	 * Returns the optional indentation (i.e., any whitespace at the beginning) of a given <CODE>String</CODE>.
	 *
	 * @param stringToProcess  the <CODE>String</CODE> to process
	 * @return                 the optional indentation
	 */
	public static String getIndentation(String stringToProcess)
	{
		String indentation = "";

		if ((stringToProcess != null) && (stringToProcess.length() > 0)) {
			char c = stringToProcess.charAt(0);
			if (Character.isWhitespace(c)) {
				int pos = 1;
				while ((pos < stringToProcess.length()) && Character.isWhitespace(stringToProcess.charAt(pos))) {
					++pos;
				}
				indentation = stringToProcess.substring(0,pos);
			}
		}

		return indentation;
	}

	/**
	 * Creates a string representation of the specified double.
	 * <P>
	 * Some examples:
	 * <UL>
	 *   <LI><CODE>StringTools.convertDoubleToString(5.2,3)</CODE> results in "5.200".</LI>
	 *   <LI><CODE>StringTools.convertDoubleToString(5.6,0)</CODE> results in "6" (rounding occurs!).</LI>
	 * </UL>
	 * <P>
	 * The default locale to be is <CODE>Locale.UK</CODE>; this can be changed by specifying an optional <CODE>locale</CODE> parameter.
	 * For example, to use the Belgian comma as a separator instead of the dot, specify <CODE>new Locale("be")</CODE> as a parameter.
	 *
	 * @param number        the double to convert to a string
	 * @param nrOfDecimals  the number of decimals in the resulting string
	 * @param locale        an optional parameter specifying the locale to be used (default is <CODE>Local.UK</CODE>)
	 * @return              a string representation of the specified double
	 */
	public static String convertDoubleToString(double number, int nrOfDecimals, Locale ... locale)
	{
		String formatSpecifier = "0";

		if (nrOfDecimals > 0) {

			formatSpecifier += "."; // use the locale-specific decimale separator
			for (int decimal = 0; decimal < nrOfDecimals; ++decimal) {
				formatSpecifier += "0";
			}
		}
		
		if (locale.length > 0) {
			return (new DecimalFormat(formatSpecifier,new DecimalFormatSymbols(locale[0]))).format(number);
		}
		else {
			return (new DecimalFormat(formatSpecifier,new DecimalFormatSymbols(Locale.UK))).format(number);
		}
	}

	/**
	 * Creates a string representation of the specified complex number.
	 * <P>
	 * The default locale to be is <CODE>Locale.UK</CODE>; this can be changed by specifying an optional <CODE>locale</CODE> parameter.
	 * For example, to use the Belgian comma as a separator instead of the dot, specify <CODE>new Locale("be")</CODE> as a parameter.
	 *
	 * @param a             the real part of the complex number to convert to a string
	 * @param b             the imaginary part of the complex number to convert to a string
	 * @param nrOfDecimals  the number of decimals in the resulting string
	 * @param locale        an optional parameter specifying the locale to be used (default is <CODE>Local.UK</CODE>)
	 * @return              a string representation of the specified complex number
	 */
	public static String convertComplexNumberToString(double a, double b, int nrOfDecimals, Locale ... locale)
	{
		String operator = "+";
		if (b < 0.0) {
			operator = "-";
			b = -b;
		}
		return (convertDoubleToString(a,nrOfDecimals,locale) +
			" " + operator + " " +
			convertDoubleToString(b,nrOfDecimals,locale) + " i");
	}

	/**
	 * Creates a string representation of the specified complex number.
	 * <P>
	 * The default locale to be is <CODE>Locale.UK</CODE>; this can be changed by specifying an optional <CODE>locale</CODE> parameter.
	 * For example, to use the Belgian comma as a separator instead of the dot, specify <CODE>new Locale("be")</CODE> as a parameter.
	 *
	 * @param c             the complex number to convert to a string
	 * @param nrOfDecimals  the number of decimals in the resulting string
	 * @param locale        an optional parameter specifying the locale to be used (default is <CODE>Local.UK</CODE>)
	 * @return              a string representation of the specified complex number
	 */
	public static String convertComplexNumberToString(ComplexNumber c, int nrOfDecimals, Locale ... locale)
	{
		return convertComplexNumberToString(c.getRealPart(),c.getImaginaryPart(),nrOfDecimals,locale);
	}

	/**
	 * Creates a string representation of the specified big complex number (note that the real and imaginary parts are intermediately converted to <CODE>double</CODE>s).
	 * <P>
	 * The default locale to be is <CODE>Locale.UK</CODE>; this can be changed by specifying an optional <CODE>locale</CODE> parameter.
	 * For example, to use the Belgian comma as a separator instead of the dot, specify <CODE>new Locale("be")</CODE> as a parameter.
	 *
	 * @param c             the big complex number to convert to a string
	 * @param nrOfDecimals  the number of decimals in the resulting string
	 * @param locale        an optional parameter specifying the locale to be used (default is <CODE>Local.UK</CODE>)
	 * @return              a string representation of the specified complex number
	 */
	public static String convertBigComplexNumberToString(BigComplexNumber c, int nrOfDecimals, Locale ... locale)
	{
		return convertComplexNumberToString(c.getRealPart().doubleValue(),c.getImaginaryPart().doubleValue(),nrOfDecimals,locale);
	}

	/**
	 * Returns the precision of the specified number, based on a leading zero and the number of zero decimals directly following the decimal point.
	 *
	 * @param x  the number to calculate the precision of
	 * @return   the precision of the specified number
	 */
	public static int getDoublePrecision(double x)
	{
		final int kMaxConversionLength = 128;
		String s = StringTools.convertDoubleToString(x,kMaxConversionLength);

		int nrOfDecimals = 0;
		int zeroPosition = s.indexOf("0");
		// check if the precision has any decimals
		if (zeroPosition == 0) {
			int dotPosition = s.indexOf(".");
			s = s.substring(dotPosition + 1);
			nrOfDecimals = 0;
			while ((nrOfDecimals < s.length()) && (s.charAt(nrOfDecimals) == '0')) {
				++nrOfDecimals;
			}
			if (nrOfDecimals >= kMaxConversionLength) {
				// fail-safe
				nrOfDecimals = 1;
			}
			else {
				// the precision starts at the first non-zero digit
				++nrOfDecimals;			
			}
		}

		return nrOfDecimals;
	}

	/**
	 * Returns a <CODE>String</CODE> containing an <CODE>int</CODE> padded with leading zeros.
	 *
	 * @param number         the number to padd
	 * @param minFieldWidth  the minimal number of characters in the field
	 * @return               a <CODE>String</CODE> containing the specified number padded with leading zeros
	 */
	public static String paddLeadingZeros(int number, int minFieldWidth)
	{
		return String.format("%0" + String.valueOf(minFieldWidth) + "d",number);
	}

	/**
	 * Converts all system-dependent end-of-line (EOL) character sequences into readable string representations of \r\n.
	 * 
	 * @param input  the <CODE>String</CODE> to convert all system-dependent EOL character sequences of
	 * @return       a <CODE>String</CODE> with all EOL character sequences converted into readable string representations of \r\n
	 */
	public static String convertEOLsToStrings(String input)
	{
		// convert system dependent EOL to CR+LF
		return input.replace(kEOLCharacterSequence,"\\r\\n");
	}

	/**
	 * Converts all readable string representations of \r\n into system-dependent end-of-line (EOL) character sequences.
	 * 
	 * @param input  the <CODE>String</CODE> with all readable string representations of \r\n to convert
	 * @return       a <CODE>String</CODE> with all string representations converted into character sequences of system-dependent end-of-line (EOL) character sequences
	 */
	public static String convertStringsToEOLs(String input)
	{
		// convert CR+LF to system dependent EOL
		return input.replace("\\r\\n",kEOLCharacterSequence);
	}

	/**
	 * Checks whether or not a line contains a comment.
	 * <P>
	 * Comments are preceeded by a hash-sign (#).
	 * 
	 * @param input  the <CODE>String</CODE> to check
	 * @return       a <CODE>boolean</CODE> indicating whether or not the line contains a comment
	 */
	public static boolean isComment(String input)
	{
		return input.trim().startsWith("#");
	}
}
