// ------------------------------
// Filename      : TimeStamp.java
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
import org.sm.smtools.exceptions.*;

/**
 * The <CODE>TimeStamp</CODE> class acts as a container for time stamps.
 * <P>
 * A time stamp is defined by its hour, minute, second and millisecond.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 03/04/2020
 */
public final class TimeStamp implements Comparable<TimeStamp>
{
	// container holding the current time
	private LocalTime fTimeStamp;
	
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to the current system time.
	 */
	public TimeStamp()
	{
		setToNow();
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to the specified values.
	 * <P>
	 * The millisecond is set to zero.
	 *
	 * @param hour    the hour component
	 * @param minute  the minute component
	 * @param second  the second component
	 */
	public TimeStamp(int hour, int minute, int second)
	{
		set(hour,minute,second,0);
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to the specified values.
	 *
	 * @param hour         the hour component
	 * @param minute       the minute component
	 * @param second       the second component
	 * @param millisecond  the millisecond component
	 */
	public TimeStamp(int hour, int minute, int second, int millisecond)
	{
		set(hour,minute,second,millisecond);
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to the specified string representation.
	 * <P>
	 * The string has to have the following specific format: <B>HH:mm:ss</B>, e.g., 12:45:16
	 *
	 * @param timeString  the string representation of the time stamp (in the format HH:mm:ss)
	 * @throws DateTimeFormatException  if an error occurred during conversion
	 * @see                             java.text.SimpleDateFormat
	 */
	public TimeStamp(String timeString) throws DateTimeFormatException
	{
		setToHMS(timeString);
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to the specified Unix time (number of milliseconds since the epoch)
	 * using the user's local time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param unixMilliseconds  the Unix time (number of milliseconds since the epoch)
	 */
	public TimeStamp(long unixMilliseconds)
	{
		convertFromUnixTime(unixMilliseconds);
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to the specified Unix time (number of milliseconds since the epoch)
	 * using a specified time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param unixMilliseconds  the Unix time (number of milliseconds since the epoch)
	 * @param timeZoneID        the ID of the time zone
	 */
	public TimeStamp(long unixMilliseconds, ZoneId timeZoneID)
	{
		convertFromUnixTime(unixMilliseconds,timeZoneID);
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object as a copy of another <CODE>TimeStamp</CODE> object.
	 * <P>
	 * This is the <B>copy constructor</B>.
	 *
	 * @param timeStamp  the <CODE>TimeStamp</CODE> object to <B>deep copy</B>
	 */
	public TimeStamp(TimeStamp timeStamp)
	{
		set(timeStamp);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets this <CODE>TimeStamp</CODE> to 00:00:00.000.
	 */
	public void clear()
	{
		set(0,0,0,0);
	}

	/**
	 * Sets this <CODE>TimeStamp</CODE> to the specified values.
	 *
	 * @param hour         the hour component
	 * @param minute       the minute component
	 * @param second       the second component
	 * @param millisecond  the millisecond component
	 */
	public void set(int hour, int minute, int second, int millisecond)
	{
		fTimeStamp = LocalTime.of(hour,minute,second,millisecond * 1000000);
	}

	/**
	 * Sets this <CODE>TimeStamp</CODE> equal to another one.
	 *
	 * @param timeStamp  the <CODE>TimeStamp</CODE> to copy
	 */
	public void set(TimeStamp timeStamp)
	{
		set(timeStamp.getHour(),timeStamp.getMinute(),timeStamp.getSecond(),timeStamp.getMillisecond());
	}

	/**
	 * Sets this <CODE>TimeStamp</CODE> based on a cumulative millisecond representation (since 00:00:00.000).
	 *
	 * @param millisecond  the number of milliseconds (since 00:00:00.000) to convert to a <CODE>TimeStamp</CODE> object
	 */
	public void setToMillisecondOfDay(long millisecond)
	{
		fTimeStamp = LocalTime.ofNanoOfDay(millisecond * 1000000);
	}

	/**
	 * Sets this <CODE>TimeStamp</CODE> object corresponding to the specified string representation.
	 * <P>
	 * The string has to have the following specific format: <B>HH:mm</B>, e.g., 10:15
	 *
	 * @param                           timeString  the string representation of the time stamp (in the format HH:mm)
	 * @throws DateTimeFormatException  if an error occurred during conversion
	 * @see                             java.text.SimpleDateFormat
	 */
	public void setToHM(String timeString) throws DateTimeFormatException
	{
		setTime(timeString,"H:m");
	}

	/**
	 * Sets this <CODE>TimeStamp</CODE> object corresponding to the specified string representation.
	 * <P>
	 * The string has to have the following specific format: <B>HH:mm:ss</B>, e.g., 12:45:16 or 8:32:48
	 *
	 * @param                           timeString  the string representation of the time stamp (in the format HH:mm:ss)
	 * @throws DateTimeFormatException  if an error occurred during conversion
	 * @see                             java.text.SimpleDateFormat
	 */
	public void setToHMS(String timeString) throws DateTimeFormatException
	{
		setTime(timeString,"H:m:s");
	}

	/**
	 * Sets this <CODE>TimeStamp</CODE> object corresponding to the specified string representation.
	 * <P>
	 * The string has to have the following specific format: <B>HH:mm:ss.mls</B>, e.g., 12:45:16.002
	 *
	 * @param                           timeString  the string representation of the time stamp (in the format HH:mm:ss.SSS)
	 * @throws DateTimeFormatException  if an error occurred during conversion
	 * @see                             java.text.SimpleDateFormat
	 */
	public void setToHMSMs(String timeString) throws DateTimeFormatException
	{
		String msDescr = timeString.substring(timeString.indexOf('.') + 1);
		if (msDescr.length() == 1) {
			msDescr += "00";
		}
		else if (msDescr.length() == 2) {
			msDescr += "0";
		}
		timeString = timeString.substring(0,timeString.indexOf('.') + 1) + msDescr;
		setTime(timeString,"H:m:s.SSS");
	}

	/**
	 * Sets this <CODE>TimeStamp</CODE> to the current system time.
	 */
	public void setToNow()
	{
		fTimeStamp = LocalTime.now();
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s hour of the day [0-23].
	 *
	 * @return the hour
	 */
	public int getHour()
	{
		return fTimeStamp.getHour();
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s minute [0-59].
	 * 
	 * @return the minute
	 */
	public int getMinute()
	{
		return fTimeStamp.getMinute();
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s second [0-59].
	 * 
	 * @return the second
	 */
	public int getSecond()
	{
		return fTimeStamp.getSecond();
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s millisecond [0-999].
	 * 
	 * @return the millisecond
	 */
	public int getMillisecond()
	{
		return (fTimeStamp.getNano() / 1000000);
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s minute-of-day (since 00:00:00.000).
	 *
	 * @return the number of minutes (since 00:00:00:000) corresponding to this <CODE>TimeStamp</CODE> object
	 */
	public int getMinuteOfDay()
	{
		return fTimeStamp.get(ChronoField.MINUTE_OF_DAY);
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s second-of-day (since 00:00:00.000).
	 *
	 * @return the number of seconds (since 00:00:00:000) corresponding to this <CODE>TimeStamp</CODE> object
	 */
	public int getSecondOfDay()
	{
		return fTimeStamp.get(ChronoField.SECOND_OF_DAY);
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s millisecond-of-day (since 00:00:00.000).
	 *
	 * @return the number of milliseconds (since 00:00:00:000) corresponding to this <CODE>TimeStamp</CODE> object
	 */
	public int getMillisecondOfDay()
	{
		return fTimeStamp.get(ChronoField.MILLI_OF_DAY);
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm:ss.SSS.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm:ss.SSS
	 * @see    java.text.SimpleDateFormat
	 */
	public String getHMSMsString()
	{
		return fTimeStamp.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm:ss.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm:ss
	 * @see    java.text.SimpleDateFormat
	 */
	public String getHMSString()
	{
		return fTimeStamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm
	 * @see    java.text.SimpleDateFormat
	 */
	public String getHMString()
	{
		return fTimeStamp.format(DateTimeFormatter.ofPattern("HH:mm"));
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format mm:ss.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format mm:ss
	 * @see    java.text.SimpleDateFormat
	 */
	public String getMSString()
	{
		return fTimeStamp.format(DateTimeFormatter.ofPattern("mm:ss"));
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format mm:ss.SSS.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format mm:ss.SSS
	 * @see    java.text.SimpleDateFormat
	 */
	public String getMSMsString()
	{
		return fTimeStamp.format(DateTimeFormatter.ofPattern("mm:ss.SSS"));
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format ss.SSS.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format ss.SSS
	 * @see    java.text.SimpleDateFormat
	 */
	public String getSMsString()
	{
		return fTimeStamp.format(DateTimeFormatter.ofPattern("ss.SSS"));
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format ss.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format ss
	 * @see    java.text.SimpleDateFormat
	 */
	public String getSString()
	{
		return fTimeStamp.format(DateTimeFormatter.ofPattern("ss"));
	}

	/**
	 * Converts this <CODE>TimeStamp</CODE> object from a Unix time (number of milliseconds since the epoch) using a specified time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param unixMilliseconds  the Unix time (number of milliseconds since the epoch)
	 * @param timeZoneID        the ID of the time zone
	 */
	public void convertFromUnixTime(long unixMilliseconds, ZoneId timeZoneID)
	{
		Instant instant = Instant.ofEpochSecond(unixMilliseconds / 1000L);
		fTimeStamp = LocalDateTime.ofInstant(instant,timeZoneID.getRules().getOffset(instant)).toLocalTime();
	}

	/**
	 * Converts this <CODE>TimeStamp</CODE> object from a Unix time (number of milliseconds since the epoch) using the user's local time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param unixMilliseconds  the Unix time (number of milliseconds since the epoch)
	 */
	public void convertFromUnixTime(long unixMilliseconds)
	{
		convertFromUnixTime(unixMilliseconds,ZoneId.systemDefault());
	}
	
	/**
	 * Converts this <CODE>TimeStamp</CODE> object to a Unix time (number of milliseconds since the epoch) using a specified date and time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param date        the date at which the timestamp occurs
	 * @param timeZoneID  the ID of the time zone
	 * @return            the Unix time (number of milliseconds since the epoch)
	 */
	public long convertToUnixTime(DateStamp date, ZoneId timeZoneID)
	{
		LocalDate localDate = LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth());
		LocalDateTime ldt = LocalDateTime.of(localDate,fTimeStamp);
		return ldt.atZone(timeZoneID).toEpochSecond() * 1000L;
	}

	/**
	 * Converts this <CODE>TimeStamp</CODE> object to a Unix time (number of milliseconds since the epoch) using the user's date and local time zone.
	 * The Unix time typically looks like, e.g., 1563319245000L.
	 * 
	 * @param date        the date at which the timestamp occurs
	 * @return the Unix time (number of milliseconds since the epoch)
	 */
	public long convertToUnixTime(DateStamp date)
	{
		return convertToUnixTime(date,ZoneId.systemDefault());
	}

	/**
	 * The overloaded <CODE>Comparable</CODE> interface.
	 */
	@Override
	public int compareTo(TimeStamp otherTimeStamp)
	{
		if (getHour() < otherTimeStamp.getHour()) {
			return -1;
		}
		else if (getHour() > otherTimeStamp.getHour()) {
			return +1;
		}
		else {
			if (getMinute() < otherTimeStamp.getMinute()) {
				return -1;
			}
			else if (getMinute() > otherTimeStamp.getMinute()) {
				return +1;
			}
			else {
				if (getSecond() < otherTimeStamp.getSecond()) {
					return -1;
				}
				else if (getSecond() > otherTimeStamp.getSecond()) {
					return +1;
				}
				else {
					if (getMillisecond() < otherTimeStamp.getMillisecond()) {
						return -1;
					}
					else if (getMillisecond() > otherTimeStamp.getMillisecond()) {
						return +1;
					}
					else {
						return 0;
					}
				}
			}
		}
	}

	/**
	 * Default overloaded <CODE>toString()</CODE> method.
	 * 
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm:ss.SSS
	 * @see    java.text.SimpleDateFormat
	 */
	public String toString()
	{
		return getHMSMsString();
	}

	/**
	 * Default <CODE>equals()</CODE> operator.
	 * 
	 * @return <CODE>true</CODE> if both objects denote the same time stamp, <CODE>false</CODE> otherwise
	 */
	public boolean equals(Object object)
	{
		if (object == this) {
			return true;
		}

		if (!(object instanceof TimeStamp)) {
			return false;
		}

		return (compareTo((TimeStamp) object) == 0);
	}

	/**
	 * @return the object's unique hashcode
	 */
	@Override
	public int hashCode()
	{
		return getMillisecondOfDay();
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 * @param timeString     -
	 * @param format         -
	 * @throws DateTimeFormatException  if an error occurred during conversion
	 * @see                             java.text.SimpleDateFormat
	 */
	private void setTime(String timeString, String format) throws DateTimeFormatException
	{
		try {
			fTimeStamp = LocalTime.parse(timeString,DateTimeFormatter.ofPattern(format));
		}
		catch (DateTimeParseException exc) {
			throw (new DateTimeFormatException(timeString));
		}
	}
}
