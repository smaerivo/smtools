// ------------------------------
// Filename      : DateStamp.java
// Author        : Sven Maerivoet
// Last modified : 03/04/2020
// Target        : Java VM (1.8)
// ------------------------------

/**
 * Copyright 2003-2020 Sven Maerivoet
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

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.exceptions.*;

/**
 * The <CODE>DateStamp</CODE> class acts as a container for date stamps.
 * <P>
 * A date stamp is defined by its year, month and day.
 * <P>
 * Note that a valid {@link I18NL10N} database must be available!
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 03/04/2020
 */
public final class DateStamp implements Comparable<DateStamp>
{
	// container holding the current date
	private LocalDate fDateStamp;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>DateStamp</CODE> object corresponding to the current system date.
	 */
	public DateStamp()
	{
		setToNow();
	}

	/**
	 * Constructs a <CODE>DateStamp</CODE> object corresponding to the specified values.
	 *
	 * @param day    the day component
	 * @param month  the month component
	 * @param year   the year component
	 */
	public DateStamp(int day, int month, int year)
	{
		setToDMY(day,month,year);
	}

	/**
	 * Constructs a <CODE>DateStamp</CODE> object corresponding to the specified values.
	 *
	 * @param weekOfYear  the week of the year component
	 * @param year        the year component
	 */
	public DateStamp(int weekOfYear, int year)
	{
		setToWoYY(weekOfYear,year);
	}

	/**
	 * Constructs a <CODE>DateStamp</CODE> object corresponding to the specified string representation.
	 * <P>
	 * The string has to have the following specific format: <B>dd/MM/yyyy</B>, e.g., 11/04/1976,
	 * or <B>yyyy-MM-dd</B> in case the optional <CODE>boolean</CODE> flag is set to true.
	 *
	 * @param dateString  the string representation of the date stamp (in the format dd/MM/yyyy)
	 * @param ymdFlag     an optional <CODE>boolean</CODE> indicating if the format is to be interpreted as yyyy-MM-dd
	 */
	public DateStamp(String dateString, boolean... ymdFlag)
	{
		try {
			if (ymdFlag.length > 0) {
				if (ymdFlag[0]) {
					setToYMD(dateString);
				}
				else {
					setToDMY(dateString);
				}
			}
			else {
				setToDMY(dateString);
			}
		}
		catch (Exception exc) {
			// ignore
		}
	}

	/**
	 * Constructs a <CODE>DateStamp</CODE> object corresponding to the specified Unix time (number of milliseconds since the epoch)
	 * using the user's local time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param unixMilliseconds  the Unix time (number of milliseconds since the epoch)
	 */
	public DateStamp(long unixMilliseconds)
	{
		convertFromUnixTime(unixMilliseconds);
	}

	/**
	 * Constructs a <CODE>DateStamp</CODE> object corresponding to the specified Unix time (number of milliseconds since the epoch)
	 * using a specified time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param unixMilliseconds  the Unix time (number of milliseconds since the epoch)
	 * @param timeZoneID        the ID of the time zone
	 */
	public DateStamp(long unixMilliseconds, ZoneId timeZoneID)
	{
		convertFromUnixTime(unixMilliseconds,timeZoneID);
	}

	/**
	 * Constructs a <CODE>DateStamp</CODE> object as a copy of another <CODE>DateStamp</CODE> object.
	 * <P>
	 * This is the <B>copy constructor</B>.
	 *
	 * @param dateStamp  the <CODE>DateStamp</CODE> object to <B>deep copy</B>
	 */
	public DateStamp(DateStamp dateStamp)
	{
		set(dateStamp);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets this <CODE>DateStamp</CODE> to the specified values.
	 *
	 * @param day    the day component
	 * @param month  the month component
	 * @param year   the year component
	 */
	public void setToDMY(int day, int month, int year)
	{
		fDateStamp = LocalDate.of(year,month,day);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the specified values.
	 *
	 * @param weekOfYear  the week of the year component
	 * @param year        the year component
	 */
	public void setToWoYY(int weekOfYear, int year)
	{
		setToDMY(1,1,year);
		fDateStamp = fDateStamp.with(ChronoField.ALIGNED_WEEK_OF_YEAR,weekOfYear);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> equal to another one.
	 *
	 * @param dateStamp  the <CODE>DateStamp</CODE> to copy
	 */
	public void set(DateStamp dateStamp)
	{
		setToDMY(dateStamp.getDayOfMonth(),dateStamp.getMonth(),dateStamp.getYear());
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> object corresponding to the specified string representation.
	 * <P>
	 * The string has to have the following specific format:
	 * <P>
	 * <B>dd/MM/yyyy</B>, e.g., 11/04/1976
	 *
	 * @param dateString                the string representation of the date stamp (in the format dd/MM/yyyy)
	 * @throws DateTimeFormatException  if an error occurred during conversion
	 * @see                             java.text.SimpleDateFormat
	 */
	public void setToDMY(String dateString) throws DateTimeFormatException
	{
		try {
			fDateStamp = LocalDate.parse(dateString,DateTimeFormatter.ofPattern("d/M/u"));
		}
		catch (DateTimeParseException exc) {
			throw (new DateTimeFormatException(dateString));
		}
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> object corresponding to another string representation.
	 * <P>
	 * The string has to have the following specific format:
	 * <P>
	 * <B>yyyy-MM-dd</B>, e.g., 1976-04-11
	 *
	 * @param dateString                the string representation of the date stamp (in the format yyyy-MM-dd)
	 * @throws DateTimeFormatException  if an error occurred during conversion
	 * @see                             java.text.SimpleDateFormat
	 */
	public void setToYMD(String dateString) throws DateTimeFormatException
	{
		try {
			fDateStamp = LocalDate.parse(dateString,DateTimeFormatter.ofPattern("u-M-d"));
		}
		catch (DateTimeParseException exc) {
			throw (new DateTimeFormatException(dateString));
		}
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the next day.
	 * 
	 * @param nrOfNextDays  the number of days in the future
	 */
	public void setToNextDay(int nrOfNextDays)
	{
		fDateStamp = fDateStamp.plusDays(nrOfNextDays);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the previous day.
	 * 
	 * @param nrOfPreviousDays  the number of days in the past
	 */
	public void setToPreviousDay(int nrOfPreviousDays)
	{
		fDateStamp = fDateStamp.minusDays(nrOfPreviousDays);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the next month.
	 * 
	 * @param nrOfNextMonths  the number of months in the future
	 */
	public void setToNextMonth(int nrOfNextMonths)
	{
		fDateStamp = fDateStamp.plusMonths(nrOfNextMonths);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the previous month.
	 * 
	 * @param nrOfPreviousMonths  the number of months in the past
	 */
	public void setToPreviousMonth(int nrOfPreviousMonths)
	{
		fDateStamp = fDateStamp.minusMonths(nrOfPreviousMonths);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the next year.
	 * 
	 * @param nrOfNextYears  the number of years in the future
	 */
	public void setToNextYear(int nrOfNextYears)
	{
		fDateStamp = fDateStamp.plusYears(nrOfNextYears);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the previous year.
	 * 
	 * @param nrOfPreviousYears  the number of years in the past
	 */
	public void setToPreviousYear(int nrOfPreviousYears)
	{
		fDateStamp = fDateStamp.minusYears(nrOfPreviousYears);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the current system date.
	 */
	public void setToNow()
	{
		fDateStamp = LocalDate.now();
	}

	/**
	 * Converts this <CODE>DateStamp</CODE> object from a Unix time (number of milliseconds since the epoch) using a specified time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param unixMilliseconds  the Unix time (number of milliseconds since the epoch)
	 * @param timeZoneID        the ID of the time zone
	 */
	public void convertFromUnixTime(long unixMilliseconds, ZoneId timeZoneID)
	{
		Instant instant = Instant.ofEpochSecond(unixMilliseconds / 1000L);
		fDateStamp = LocalDateTime.ofInstant(instant,timeZoneID.getRules().getOffset(instant)).toLocalDate();
	}

	/**
	 * Converts this <CODE>DateStamp</CODE> object from a Unix time (number of milliseconds since the epoch) using the user's local time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param unixMilliseconds  the Unix time (number of milliseconds since the epoch)
	 */
	public void convertFromUnixTime(long unixMilliseconds)
	{
		convertFromUnixTime(unixMilliseconds,ZoneId.systemDefault());
	}
	
	/**
	 * Converts this <CODE>DateStamp</CODE> object to a Unix time (number of milliseconds since the epoch) using a specified time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param timeZoneID  the ID of the time zone
	 * @return            the Unix time (number of milliseconds since the epoch)
	 */
	public long convertToUnixTime(ZoneId timeZoneID)
	{
		return fDateStamp.atStartOfDay(timeZoneID).toEpochSecond() * 1000L;
	}

	/**
	 * Converts this <CODE>DateStamp</CODE> object to a Unix time (number of milliseconds since the epoch) using the user's local time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @return the Unix time (number of milliseconds since the epoch)
	 */
	public long convertToUnixTime()
	{
		return convertToUnixTime(ZoneId.systemDefault());
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s day-of-week [1-7].
	 *
	 * @return the day of the week corresponding to this <CODE>DateStamp</CODE> object
	 */
	public int getDayOfWeek()
	{
		return fDateStamp.getDayOfWeek().getValue();
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s day-of-month [1-31].
	 *
	 * @return the day of the month corresponding to this <CODE>DateStamp</CODE> object
	 */
	public int getDayOfMonth()
	{
		return fDateStamp.getDayOfMonth();
	}

	/**
	 * Returns the number of days in this <CODE>DateStamp</CODE>'s month.
	 *
	 * @return the number of days in this <CODE>DateStamp</CODE>'s month
	 */
	public int getNumberOfDaysInMonth()
	{
		return DateStamp.getNumberOfDaysInMonth(getMonth(),getYear());
	}

	/**
	 * Returns the number of days in a specified month [1-12] and a given year.
	 *
	 * @param month  the specified month [1-12]
	 * @param year   the year for the month
	 * @return       the number of days in the specified month
	 */
	public static int getNumberOfDaysInMonth(int month, int year)
	{
		YearMonth yearMonth = YearMonth.of(year,month);
		return yearMonth.lengthOfMonth();
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s day-of-year.
	 *
	 * @return the day of year corresponding to this <CODE>DateStamp</CODE> object
	 */
	public int getDayOfYear()
	{
		return fDateStamp.getDayOfYear();
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s week-of-year [1-52].
	 *
	 * @return the week of the year corresponding to this <CODE>DateStamp</CODE> object
	 */
	public int getWeekOfYear()
	{
		return fDateStamp.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s month [1-12].
	 * 
	 * @return the month
	 */
	public int getMonth()
	{
		return fDateStamp.getMonthValue();
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s year.
	 * 
	 * @return the year
	 */
	public int getYear()
	{
		return fDateStamp.getYear();
	}

	/**
	 * Checks whether or not this <CODE>DateStamp</CODE>'s year is a leap year (assuming a Gregorian calendar).
	 *
	 * @return <CODE>true</CODE> if this <CODE>DateStamp</CODE>'s year is leap year, <CODE>false</CODE> otherwise
	 */
	public boolean isLeapYear()
	{
		return fDateStamp.isLeapYear();
	}

	/**
	 * Returns whether or not the current <CODE>DateStamp</CODE> is in a weekend.
	 * 
	 * @return <CODE>true</CODE> if the current <CODE>DateStamp</CODE> is in a weekend, <CODE>false</CODE> otherwise
	 */
	public boolean isWeekend()
	{
		int dayOfWeek = getDayOfWeek();
		return ((dayOfWeek == 6) || (dayOfWeek == 7));
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object in the format dd/MM/yyyy.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object in the format dd/MM/yyyy
	 * @see    java.text.SimpleDateFormat
	 */
	public String getDMYString()
	{
		return fDateStamp.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object in the format yyyy-MM-dd.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object in the format yyyy-MM-dd
	 * @see    java.text.SimpleDateFormat
	 */
	public String getYMDString()
	{
		return fDateStamp.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object as a fully formatted string.
	 * <P>
	 * The string will have the following specific format:
	 * <P>
	 * <B>day-of-week dd month yyyy</B>, e.g., Sunday 11 April 1976
	 * <P>
	 * Note that a valid {@link I18NL10N} database must be available!
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object as a fully formatted string
	 * @see    I18NL10N
	 */
	public String getFullDateString()
	{
		String dayOfWeekString = DateStamp.getDayOfWeekString(getDayOfWeek());
		String dayString = String.valueOf(getDayOfMonth());
		String monthString = getMonthString(getMonth());
		String yearString = String.valueOf(getYear());

		return
			dayOfWeekString.substring(0,1).toUpperCase() + dayOfWeekString.substring(1) + " " +
			dayString + " " +
			monthString.substring(0,1).toUpperCase() + monthString.substring(1) + " " +
			yearString;
	}

	/**
	 * The overloaded <CODE>Comparable</CODE> interface.
	 */
	@Override
	public int compareTo(DateStamp otherDateStamp)
	{
		if (getYear() < otherDateStamp.getYear()) {
			return -1;
		}
		else if (getYear() > otherDateStamp.getYear()) {
			return +1;
		}
		else {
			if (getMonth() < otherDateStamp.getMonth()) {
				return -1;
			}
			else if (getMonth() > otherDateStamp.getMonth()) {
				return +1;
			}
			else {
				if (getDayOfMonth() < otherDateStamp.getDayOfMonth()) {
					return -1;
				}
				else if (getDayOfMonth() > otherDateStamp.getDayOfMonth()) {
					return +1;
				}
				else {
					return 0;
				}
			}
		}
	}
	 
	/**
	 * Default overloaded <CODE>toString()</CODE> method.
	 * 
	 * @return a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object in the format dd/MM/yyyy
	 * @see    java.text.SimpleDateFormat
	 */
	public String toString()
	{
		return getDMYString();
	}

	/**
	 * Default <CODE>equals()</CODE> operator.
	 * 
	 * @return <CODE>true</CODE> if both objects denote the same date stamp, <CODE>false</CODE> otherwise
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof DateStamp)) {
			return false;
		}

		if (object == this) {
			return true;
		}

		return (compareTo((DateStamp) object) == 0);
	}

	/**
	 * @return the object's unique hashcode
	 */
	@Override
	public int hashCode()
	{
		return ((getYear() * 1000) + getDayOfYear());
	}

  /******************
	 * STATIC METHODS *
	 ******************/

	/**
	 * Helper method to convert a time zone's short description into a <CODE>ZoneId</CODE> object.
	 * 
	 * @param timeZoneShortDescription  the short description of the time zone
	 * @return                          the <CODE>ZoneID</CODE> object corresponding to the specified time zone's description
	 */
	public static ZoneId getZoneID(String timeZoneShortDescription)
	{
		return ZoneId.of(timeZoneShortDescription);
	}

	/**
	 * Helper method to convert a day-of-week index [1 (Monday) - 7 (Sunday)] to a weekday name.
	 * <P>
	 * Note that a valid {@link I18NL10N} database must be available!
	 *
	 * @param dayOfWeek  the day-of-week index to convert [1-7]
	 * @return           a string corresponding to the weekday name
	 * @see              I18NL10N
	 */
	public static String getDayOfWeekString(int dayOfWeek)
	{
		switch (dayOfWeek) {
			case 1:
				return I18NL10N.kINSTANCE.translate("text.Day.Monday");
			case 2:
				return I18NL10N.kINSTANCE.translate("text.Day.Tuesday");
			case 3:
				return I18NL10N.kINSTANCE.translate("text.Day.Wednesday");
			case 4:
				return I18NL10N.kINSTANCE.translate("text.Day.Thursday");
			case 5:
				return I18NL10N.kINSTANCE.translate("text.Day.Friday");
			case 6:
				return I18NL10N.kINSTANCE.translate("text.Day.Saturday");
			case 7:
				return I18NL10N.kINSTANCE.translate("text.Day.Sunday");
		}

		return "";
	}

	/**
	 * Helper method to convert a month index [1-12] to a month name.
	 * <P>
	 * Note that a valid {@link I18NL10N} database must be available!
	 *
	 * @param month  the month index to convert
	 * @return       a string corresponding to the month name
	 * @see          I18NL10N
	 */
	public static String getMonthString(int month)
	{
		switch (month) {
			case 1:
				return I18NL10N.kINSTANCE.translate("text.Month.January");
			case 2:
				return I18NL10N.kINSTANCE.translate("text.Month.February");
			case 3:
				return I18NL10N.kINSTANCE.translate("text.Month.March");
			case 4:
				return I18NL10N.kINSTANCE.translate("text.Month.April");
			case 5:
				return I18NL10N.kINSTANCE.translate("text.Month.May");
			case 6:
				return I18NL10N.kINSTANCE.translate("text.Month.June");
			case 7:
				return I18NL10N.kINSTANCE.translate("text.Month.July");
			case 8:
				return I18NL10N.kINSTANCE.translate("text.Month.August");
			case 9:
				return I18NL10N.kINSTANCE.translate("text.Month.September");
			case 10:
				return I18NL10N.kINSTANCE.translate("text.Month.October");
			case 11:
				return I18NL10N.kINSTANCE.translate("text.Month.November");
			case 12:
				return I18NL10N.kINSTANCE.translate("text.Month.December");
		}

		return "";
	}

	/**
	 * Helper method to convert a month name to a month index [1-12].
	 * <P>
	 * Note that a valid {@link I18NL10N} database must be available!
	 *
	 * @param month  the month name to convert
	 * @return       a number corresponding to the month index
	 * @see          I18NL10N
	 */
	public static int getMonth(String month)
	{
		if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.January"))) {
			return 1;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.February"))) {
			return 2;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.March"))) {
			return 3;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.April"))) {
			return 4;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.May"))) {
			return 5;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.June"))) {
			return 6;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.July"))) {
			return 7;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.August"))) {
			return 8;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.September"))) {
			return 9;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.October"))) {
			return 10;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.November"))) {
			return 11;
		}
		else if (month.equalsIgnoreCase(I18NL10N.kINSTANCE.translate("text.Month.December"))) {
			return 12;
		}

		return 0;
	}
}
