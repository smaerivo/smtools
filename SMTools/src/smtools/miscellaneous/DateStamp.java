// ------------------------------
// Filename      : DateStamp.java
// Author        : Sven Maerivoet
// Last modified : 09/12/2011
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

package smtools.miscellaneous;

import java.text.*;
import java.util.*;
import smtools.application.util.*;
import smtools.exceptions.*;

/**
 * The <CODE>DateStamp</CODE> class acts as a container for date stamps.
 * <P>
 * A date stamp is defined by its year, month and day.
 * <P>
 * Note that a valid {@link Messages} database must be available!
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 09/12/2011
 */
public final class DateStamp implements Comparable<DateStamp>
{
	// container holding the current date
	private Calendar fDateStamp;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>DateStamp</CODE> object corresponding to the current system date.
	 *
	 * @see DateStamp#DateStamp(int,int,int)
	 * @see DateStamp#DateStamp(int,int)
	 * @see DateStamp#DateStamp(String)
	 * @see DateStamp#DateStamp(DateStamp)
	 */
	public DateStamp()
	{
		setToCurrentDate();
	}

	/**
	 * Constructs a <CODE>DateStamp</CODE> object corresponding to the specified values.
	 *
	 * @param day   the day component
	 * @param month the month component
	 * @param year  the year component
	 * @see   DateStamp#DateStamp()
	 * @see   DateStamp#DateStamp(int,int)
	 * @see   DateStamp#DateStamp(String)
	 * @see   DateStamp#DateStamp(DateStamp)
	 */
	public DateStamp(int day, int month, int year)
	{
		set(day,month,year);
	}

	/**
	 * Constructs a <CODE>DateStamp</CODE> object corresponding to the specified values.
	 *
	 * @param weekOfYear the week of the year component
	 * @param year       the year component
	 * @see   DateStamp#DateStamp()
	 * @see   DateStamp#DateStamp(int,int,int)
	 * @see   DateStamp#DateStamp(String)
	 * @see   DateStamp#DateStamp(DateStamp)
	 */
	public DateStamp(int weekOfYear, int year)
	{
		set(weekOfYear,year);
	}

	/**
	 * Constructs a <CODE>DateStamp</CODE> object corresponding to the specified string representation.
	 * <P>
	 * The string has to have the following specific format:
	 * <P>
	 * <UL>
	 *   <B>dd/MM/yyyy</B>, e.g., 11/04/1976
	 * </UL>
	 *
	 * @param  dateString the string representation of the date stamp (in the format dd/MM/yyyy)
	 * @throws DateTimeFormatException if an error occurred during conversion
	 * @see    DateStamp#DateStamp()
	 * @see    DateStamp#DateStamp(int,int,int)
	 * @see    DateStamp#DateStamp(int,int)
	 * @see    DateStamp#DateStamp(DateStamp)
	 * @see    java.text.SimpleDateFormat
	 */
	public DateStamp(String dateString) throws DateTimeFormatException
	{
		set(dateString);
	}

	/**
	 * Constructs a <CODE>DateStamp</CODE> object as a copy of another <CODE>DateStamp</CODE> object.
	 * <P>
	 * This is the <B>copy constructor</B>.
	 *
	 * @param dateStamp the <CODE>DateStamp</CODE> object to <B>deep copy</B>
	 * @see   DateStamp#DateStamp()
	 * @see   DateStamp#DateStamp(int,int,int)
	 * @see   DateStamp#DateStamp(String)
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
	 * @param day   the day component
	 * @param month the month component
	 * @param year  the year component
	 */
	public void set(int day, int month, int year)
	{
		fDateStamp = Calendar.getInstance();
		fDateStamp.set(Calendar.DAY_OF_MONTH,day);
		fDateStamp.set(Calendar.MONTH,month - fDateStamp.getMinimum(Calendar.MONTH) - 1);
		fDateStamp.set(Calendar.YEAR,year);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the specified values.
	 *
	 * @param weekOfYear the week of the year component
	 * @param year       the year component
	 */
	public void set(int weekOfYear, int year)
	{
		fDateStamp = Calendar.getInstance();
		fDateStamp.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		fDateStamp.set(Calendar.WEEK_OF_YEAR,weekOfYear);
		fDateStamp.set(Calendar.YEAR,year);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> equal to another one.
	 *
	 * @param dateStamp the <CODE>DateStamp</CODE> to copy
	 */
	public void set(DateStamp dateStamp)
	{
		set(dateStamp.getDay(),dateStamp.getMonth(),dateStamp.getYear());
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> object corresponding to the specified string representation.
	 * <P>
	 * The string has to have the following specific format:
	 * <P>
	 * <UL>
	 *   <B>dd/MM/yyyy</B>, e.g., 11/04/1976
	 * </UL>
	 *
	 * @param  dateString the string representation of the date stamp (in the format dd/MM/yyyy)
	 * @throws DateTimeFormatException if an error occurred during conversion
	 * @see    java.text.SimpleDateFormat
	 */
	public void set(String dateString) throws DateTimeFormatException
	{
		fDateStamp = Calendar.getInstance();

		String[] dateStampParts = dateString.split("/");
		try {
			int day = Integer.parseInt(dateStampParts[0]);
			int month = Integer.parseInt(dateStampParts[1]);
			int year = Integer.parseInt(dateStampParts[2]);
			set(day,month,year);
		}
		catch (ArrayIndexOutOfBoundsException exc) {
			throw (new DateTimeFormatException(dateString));
		}
		catch (NumberFormatException exc) {
			throw (new DateTimeFormatException(dateString));
		}
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the next day.
	 * 
	 * @param nrOfNextDays the number of days in the future
	 */
	public void setToNextDay(int nrOfNextDays)
	{
		fDateStamp.add(Calendar.DAY_OF_MONTH,nrOfNextDays);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the previous day.
	 * 
	 * @param nrOfPreviousDays the number of days in the past
	 */
	public void setToPreviousDay(int nrOfPreviousDays)
	{
		fDateStamp.add(Calendar.DAY_OF_MONTH,-nrOfPreviousDays);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the next month.
	 * 
	 * @param nrOfNextMonths the number of months in the future
	 */
	public void setToNextMonth(int nrOfNextMonths)
	{
		fDateStamp.add(Calendar.MONTH,nrOfNextMonths);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the previous month.
	 * 
	 * @param nrOfPreviousMonths the number of months in the past
	 */
	public void setToPreviousMonth(int nrOfPreviousMonths)
	{
		fDateStamp.add(Calendar.MONTH,-nrOfPreviousMonths);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the next year.
	 * 
	 * @param nrOfNextYears the number of years in the future
	 */
	public void setToNextYear(int nrOfNextYears)
	{
		fDateStamp.add(Calendar.YEAR,nrOfNextYears);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the previous year.
	 * 
	 * @param nrOfPreviousYears the number of years in the past
	 */
	public void setToPreviousYear(int nrOfPreviousYears)
	{
		fDateStamp.add(Calendar.YEAR,-nrOfPreviousYears);
	}

	/**
	 * Sets this <CODE>DateStamp</CODE> to the current system date.
	 */
	public void setToCurrentDate()
	{
		fDateStamp = Calendar.getInstance();
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s day of the month [1-31].
	 */
	public int getDay()
	{
		return fDateStamp.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s day-of-week [1-7].
	 *
	 * @return the day of the week corresponding to this <CODE>DateStamp</CODE> object
	 */
	public int getDayOfWeek()
	{
		switch (fDateStamp.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY: return 1;
			case Calendar.TUESDAY: return 2;
			case Calendar.WEDNESDAY: return 3;
			case Calendar.THURSDAY: return 4;
			case Calendar.FRIDAY: return 5;
			case Calendar.SATURDAY: return 6;
			case Calendar.SUNDAY: return 7;
		}

		return 0;
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s day-of-month [1-31].
	 *
	 * @return the day of the month corresponding to this <CODE>DateStamp</CODE> object
	 */
	public int getDayOfMonth()
	{
		return fDateStamp.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns the number of days in this <CODE>DateStamp</CODE>'s month.
	 *
	 * @return the number of days in this <CODE>DateStamp</CODE>'s month
	 */
	public int getNumberOfDaysInMonth()
	{
		return getNumberOfDaysInMonth(getMonth(),getYear());
	}

	/**
	 * Returns the number of days in a specified month [1-12] and a given year.
	 *
	 * @param month the specified month [1-12]
	 * @param year the year for the month
	 * @return the number of days in the specified month
	 */
	public static int getNumberOfDaysInMonth(int month, int year)
	{
		final int[] kDaysInMonth = {31,28,31,30,31,30,31,31,30,31,30,31};

		if (month < 1) {
			month = 1;
		}
		else if (month > 12) {
			month = 12;
		}

		int daysInMonth = kDaysInMonth[month + 1];
		if ((month == 2) && isLeapYear(year)) {
			++daysInMonth;
		}

		return daysInMonth;
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s day-of-year.
	 *
	 * @return the day of year corresponding to this <CODE>DateStamp</CODE> object
	 */
	public int getDayOfYear()
	{
		return fDateStamp.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s week-of-year [1-52].
	 *
	 * @return the week of the year corresponding to this <CODE>DateStamp</CODE> object
	 */
	public int getWeekOfYear()
	{
		return fDateStamp.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s month [1-12].
	 */
	public int getMonth()
	{
		return (fDateStamp.get(Calendar.MONTH) - fDateStamp.getMinimum(Calendar.MONTH) + 1);
	}

	/**
	 * Returns this <CODE>DateStamp</CODE>'s year.
	 */
	public int getYear()
	{
		return fDateStamp.get(Calendar.YEAR);
	}

	/**
	 * Checks whether or not this <CODE>DateStamp</CODE>'s year is a leap year (assuming a Gregorian calendar).
	 *
	 * @return <CODE>true</CODE> if this <CODE>DateStamp</CODE>'s year is leap year, <CODE>false</CODE> otherwise
	 */
	public boolean isLeapYear()
	{
		return isLeapYear(getYear());
	}
	
	/**
	 * Checks whether or not a specified year is a leap year (assuming a Gregorian calendar).
	 *
	 * @return <CODE>true</CODE> if the specified year is leap year, <CODE>false</CODE> otherwise
	 */
	public static boolean isLeapYear(int year)
	{
		// check if the year is divisible by 4
		if ((year % 4) == 0) {

			// check if the year is divisible by 4 but not by 100
			if ((year % 100) != 0) {
				return true;
			}
			else if ((year % 400) == 0) {
				// check if the year is divisible by 4 and 100 and 400
				return true;
			}
			else {
				// check if the year is divisible by 4 and 100 but not by 400
				return false;
			}
		}
		else {
			return false;
		}
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
	 * Returns a Java <CODE>Date</CODE> object representing this <CODE>DateStamp</CODE>.
	 *
	 * @return a Java <CODE>Date</CODE> object representing this <CODE>DateStamp</CODE>
	 */
	public Date getJavaDate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(getYear(),getMonth() - calendar.getMinimum(Calendar.MONTH) - 1,getDay());
		return calendar.getTime();
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object in the format dd/MM/yyyy.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object in the format dd/MM/yyyy
	 * @see    java.text.SimpleDateFormat
	 */
	public String getDMYString()
	{
		return (new SimpleDateFormat("dd/MM/yyyy")).format(fDateStamp.getTime());
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object as a fully formatted string.
	 * <P>
	 * The string will have the following specific format:
	 * <P>
	 * <UL>
	 *   <B>day-of-week dd month yyyy</B>, e.g., Sunday 11 April 1976
	 * </UL>
	 * <P>
	 * Note that a valid {@link Messages} database must be available!
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>DateStamp</CODE> object as a fully formatted string
	 * @see    Messages
	 */
	public String getFullDateString()
	{
		String dayOfWeekString = DateStamp.getDayOfWeekString(getDayOfWeek());
		String dayString = String.valueOf(getDay());
		String monthString = getMonthString(getMonth());
		String yearString = String.valueOf(getYear());

		return
			dayOfWeekString.substring(0,1).toUpperCase() + dayOfWeekString.substring(1) + " " +
			dayString + " " +
			monthString.substring(0,1).toUpperCase() + monthString.substring(1) + " " +
			yearString;
	}

	/**
	 * Helper method to convert a day-of-week index [1 (Monday) - 7 (Sunday)] to a weekday name.
	 * <P>
	 * Note that a valid {@link Messages} database must be available!
	 *
	 * @param  dayOfWeek the day-of-week index to convert [1-7]
	 * @return a string corresponding to the weekday name
	 * @see    Messages
	 */
	public static String getDayOfWeekString(int dayOfWeek)
	{
		switch (dayOfWeek) {
			case 1:
				return Messages.lookup("textDayMonday");
			case 2:
				return Messages.lookup("textDayTuesday");
			case 3:
				return Messages.lookup("textDayWednesday");
			case 4:
				return Messages.lookup("textDayThursday");
			case 5:
				return Messages.lookup("textDayFriday");
			case 6:
				return Messages.lookup("textDaySaturday");
			case 7:
				return Messages.lookup("textDaySunday");
		}

		return "";
	}

	/**
	 * Helper method to convert a month index [1-12] to a month name.
	 * <P>
	 * Note that a valid {@link Messages} database must be available!
	 *
	 * @param  month the month index to convert
	 * @return a string corresponding to the month name
	 * @see    Messages
	 */
	public static String getMonthString(int month)
	{
		switch (month) {
			case 1:
				return Messages.lookup("textMonthJanuary");
			case 2:
				return Messages.lookup("textMonthFebruary");
			case 3:
				return Messages.lookup("textMonthMarch");
			case 4:
				return Messages.lookup("textMonthApril");
			case 5:
				return Messages.lookup("textMonthMay");
			case 6:
				return Messages.lookup("textMonthJune");
			case 7:
				return Messages.lookup("textMonthJuly");
			case 8:
				return Messages.lookup("textMonthAugust");
			case 9:
				return Messages.lookup("textMonthSeptember");
			case 10:
				return Messages.lookup("textMonthOctober");
			case 11:
				return Messages.lookup("textMonthNovember");
			case 12:
				return Messages.lookup("textMonthDecember");
		}

		return "";
	}

	/**
	 * Helper method to convert a month name to a month index [1-12].
	 * <P>
	 * Note that a valid {@link Messages} database must be available!
	 *
	 * @param  month the month name to convert
	 * @return a number corresponding to the month index
	 * @see    Messages
	 */
	public static int getMonth(String month)
	{
		if (month.equalsIgnoreCase(Messages.lookup("textMonthJanuary"))) {
			return 1;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthFebruary"))) {
			return 2;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthMarch"))) {
			return 3;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthApril"))) {
			return 4;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthMay"))) {
			return 5;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthJune"))) {
			return 6;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthJuly"))) {
			return 7;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthAugust"))) {
			return 8;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthSeptember"))) {
			return 9;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthOctober"))) {
			return 10;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthNovember"))) {
			return 11;
		}
		else if (month.equalsIgnoreCase(Messages.lookup("textMonthDecember"))) {
			return 12;
		}

		return 0;
	}

	/**
	 * The overloaded <CODE>Comparable<CODE> interface.
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
				if (getDay() < otherDateStamp.getDay()) {
					return -1;
				}
				else if (getDay() > otherDateStamp.getDay()) {
					return +1;
				}
				else {
					return 0;
				}
			}
		}
	}
	 
	/**
	 * Default overloaded <CODE>toString()<CODE> method.
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
		if (object == this) {
			return true;
		}

		if (!(object instanceof DateStamp)) {
			return false;
		}

		DateStamp dateStamp = (DateStamp) object;
		if ((getDay() != dateStamp.getDay()) ||
				(getMonth() != dateStamp.getMonth()) ||
				(getYear() != dateStamp.getYear())) {
			return false;
		}

		return true;
	}
}
