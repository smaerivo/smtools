// --------------------------------
// Filename      : StringTools.java
// Author        : Sven Maerivoet
// Last modified : 29/05/2011
// Target        : Java VM (1.6)
// --------------------------------

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

import java.text.*;
import java.util.*;

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
 * <UL>
 *   <CODE>... = StringTools.method(...);</CODE>
 * </UL>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 29/05/2011
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

	// prevent instantiation
	private StringTools()
	{
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Aligns a string left, padding with extra characters if necessary.
	 *
	 * @param  stringToAlign  the string that should be aligned left
	 * @param  nrOfCharacters the minimum length of the string after the alignment
	 * @param  padCharacter   the character used to pad the string at the right end
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
	 * @param  stringToAlign  the string that should be aligned right
	 * @param  nrOfCharacters the minimum length of the string after the alignment
	 * @param  padCharacter   the character used to pad the string at the left end
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
	 * @param  stringToCenter the string that should be centered
	 * @param  nrOfCharacters the minimum length of the string after the centering
	 * @param  padCharacter   the character used to pad the string at the left and right ends
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
	 * @param  stringToTruncate the string that should be truncated
	 * @param  nrOfCharacters   the maximum length of the string after the truncation
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
	 * @param  stringToAlign  the string that should be aggressively aligned left
	 * @param  nrOfCharacters the maximum length of the string after the aggressive alignment
	 * @param  padCharacter   the character used to pad the string at the right end
	 * @return                the left aggressively aligned string
	 * @see    StringTools#alignLeft(String,int,char)
	 * @see    StringTools#truncate(String,int)
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
	 * @param  stringToAlign  the string that should be aggressively aligned right
	 * @param  nrOfCharacters the maximum length of the string after the aggressive alignment
	 * @param  padCharacter   the character used to pad the string at the left end
	 * @return                the right aggressively aligned string
	 * @see    StringTools#alignRight(String,int,char)
	 * @see    StringTools#truncate(String,int)
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
	 * @param  stringToCenter the string that should be aggressively centered
	 * @param  nrOfCharacters the maximum length of the string after the aggressive centering
	 * @param  padCharacter   the character used to pad the string at the left and right ends
	 * @return                the aggressively centered string
	 * @see    StringTools#alignCenter(String,int,char)
	 * @see    StringTools#truncate(String,int)
	 */
	public static String aggressiveAlignCenter(String stringToCenter, int nrOfCharacters, char padCharacter)
	{
		return truncate(alignCenter(stringToCenter,nrOfCharacters,padCharacter),nrOfCharacters);
	}

	/**
	 * Creates a string containing a repetition of a character.
	 * <P>
	 * As an example, a dashed line of length 5 can be created as:
	 * <P>
	 * <UL>
	 *   <CODE>StringTools.createLineOfCharacters(5,'-')</CODE>
	 * </UL>
	 * <P>
	 * The result will be "-----".
	 *
	 * @param  length        the total length of the line
	 * @param  lineCharacter the character that is used to create the line
	 * @return               a line containing the repetition of the specified character
	 */
	public static String createLineOfCharacters(int length, char lineCharacter)
	{
		return alignLeft("",length,lineCharacter);
	}

	/**
	 * Creates a string representation of the specified double.
	 * <P>
	 * Some examples:
	 * <P>
	 * <UL>
	 *   <LI><CODE>StringTools.convertDoubleToString(5.2,3)</CODE> results in "5.200".</LI>
	 *   <LI><CODE>StringTools.convertDoubleToString(5.6,0)</CODE> results in "6" (rounding occurs!).</LI>
	 * </UL>
	 * <P>
	 * The default locale to be is <CODE>Locale.UK</CODE>; this can be changed by specifying an optional <CODE>locale</CODE> parameter.
	 * For example, to use the Belgian comma as a separator instead of the dot, specify <CODE>new Locale("be")</CODE> as a parameter.
	 *
	 * @param  number       the double to convert to a string
	 * @param  nrOfDecimals the number of decimals in the resulting string
	 * @param  locale       an optional parameter specifying the locale to be used (default is <CODE>Local.UK</CODE>)
	 * @return              a string representation of the specified double
	 */
	public static String convertDoubleToString(double number, int nrOfDecimals, Locale ... locale)
	{
		String formatSpecifier = "0";

		if (nrOfDecimals > 0) {

			formatSpecifier += ".";
			for (int decimal = 0; decimal < nrOfDecimals; ++decimal) {
				formatSpecifier += "0";
			}
		}

		DecimalFormat decimalFormat = new DecimalFormat(formatSpecifier);
		if (locale.length > 0) {
			decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols());
		}
		else {
				decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.UK));
		}

		return String.valueOf(decimalFormat.format(number));
	}

	/**
	 * Returns a <CODE>String</CODE> containing an <CODE>int</CODE> padded with leading zeros.
	 *
	 * @param  number the number to padd
	 * @param  minFieldWidth the minimal number of characters in the field
	 * @return a <CODE>String</CODE> containing the specified number padded with leading zeros
	 */
	public static String paddLeadingZeros(int number, int minFieldWidth)
	{
		return String.format("%0" + String.valueOf(minFieldWidth) + "d",number);
	}

	/**
	 * Converts all system-dependent end-of-line (EOL) character sequences into readable string representations of \r\n.
	 * 
	 * @param  input the <CODE>String</CODE> to convert all system-dependent EOL character sequences of
	 * @return a <CODE>String</CODE> with all EOL character sequences converted into readable string representations of \r\n
	 */
	public static String convertEOLsToStrings(String input)
	{
		// convert system dependent EOL to CRLF
		return input.replace(kEOLCharacterSequence,"\\r\\n");
	}

	/**
	 * Converts all readable string representations of \r\n into system-dependent end-of-line (EOL) character sequences.
	 * 
	 * @param  input the <CODE>String</CODE> with all readable string representations of \r\n to convert
	 * @return a <CODE>String</CODE> with all string representations converted into character sequences of system-dependent end-of-line (EOL) character sequences
	 */
	public static String convertStringsToEOLs(String input)
	{
		// convert CRLF to system dependent EOL
		return input.replace("\\r\\n",kEOLCharacterSequence);
	}
}
