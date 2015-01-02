// ------------------------------
// Filename      : TimeStamp.java
// Author        : Sven Maerivoet
// Last modified : 12/07/2013
// Target        : Java VM (1.8)
// ------------------------------

/**
 * Copyright 2003-2015 Sven Maerivoet
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
import org.sm.smtools.exceptions.*;

/**
 * The <CODE>TimeStamp</CODE> class acts as a container for time stamps.
 * <P>
 * A time stamp is defined by its hour, minute, second and millisecond.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 12/07/2013
 */
public final class TimeStamp implements Comparable<TimeStamp>
{
	// container holding the current time
	private Calendar fTimeStamp;
	
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to the current system time.
	 *
	 * @see TimeStamp#clear()
	 * @see TimeStamp#TimeStamp(int,int,int)
	 * @see TimeStamp#TimeStamp(int,int,int,int)
	 * @see TimeStamp#TimeStamp(long)
	 * @see TimeStamp#TimeStamp(String)
	 * @see TimeStamp#TimeStamp(TimeStamp)
	 */
	public TimeStamp()
	{
		setToCurrentTime();
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to the specified values.
	 * <P>
	 * The millisecond is set to zero.
	 *
	 * @param hour    the hour component
	 * @param minute  the minute component
	 * @param second  the second component
	 * @see           TimeStamp#TimeStamp()
	 * @see           TimeStamp#TimeStamp(int,int,int,int)
	 * @see           TimeStamp#TimeStamp(long)
	 * @see           TimeStamp#TimeStamp(String)
	 * @see           TimeStamp#TimeStamp(TimeStamp)
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
	 * @see                TimeStamp#TimeStamp()
	 * @see                TimeStamp#TimeStamp(int,int,int)
	 * @see                TimeStamp#TimeStamp(long)
	 * @see                TimeStamp#TimeStamp(String)
	 * @see                TimeStamp#TimeStamp(TimeStamp)
	 */
	public TimeStamp(int hour, int minute, int second, int millisecond)
	{
		set(hour,minute,second,millisecond);
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to a number of milliseconds (since 00:00:00.000).
	 *
	 * @param millisecondOfDay  the number of milliseconds (since 00:00:00.000) to convert to a <CODE>TimeStamp</CODE> object
	 * @see                     TimeStamp#TimeStamp()
	 * @see                     TimeStamp#TimeStamp(int,int,int)
	 * @see                     TimeStamp#TimeStamp(int,int,int, int)
	 * @see                     TimeStamp#TimeStamp(String)
	 * @see                     TimeStamp#TimeStamp(TimeStamp)
	 */
	public TimeStamp(long millisecondOfDay)
	{
		setToMillisecondOfDay(millisecondOfDay);
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to the specified string representation.
	 * <P>
	 * The string has to have the following specific format:
	 * <P>
	 * <B>HH:mm</B> or <B>HH:mm:ss</B>, or <B>HH:mm:ss.mls</B>, e.g., 10:15, 12:45:16, or 05:03:06.002
	 *
	 * @param timeString                the string representation of the time stamp (in the format HH:mm, HH:mm:ss, or HH:mm:ss.SSS)
	 * @throws DateTimeFormatException  if an error occurred during conversion
	 * @see                             TimeStamp#TimeStamp()
	 * @see                             TimeStamp#TimeStamp(int,int,int)
	 * @see                             TimeStamp#TimeStamp(long)
	 * @see                             TimeStamp#TimeStamp(TimeStamp)
	 * @see                             java.text.SimpleDateFormat
	 */
	public TimeStamp(String timeString) throws DateTimeFormatException
	{
		set(timeString);
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object as a copy of another <CODE>TimeStamp</CODE> object.
	 * <P>
	 * This is the <B>copy constructor</B>.
	 *
	 * @param timeStamp  the <CODE>TimeStamp</CODE> object to <B>deep copy</B>
	 * @see              TimeStamp#TimeStamp()
	 * @see              TimeStamp#TimeStamp(int,int,int)
	 * @see              TimeStamp#TimeStamp(int,int,int, int)
	 * @see              TimeStamp#TimeStamp(long)
	 * @see              TimeStamp#TimeStamp(String)
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
		fTimeStamp = Calendar.getInstance();
		fTimeStamp.set(Calendar.HOUR_OF_DAY,hour);
		fTimeStamp.set(Calendar.MINUTE,minute);
		fTimeStamp.set(Calendar.SECOND,second);
		fTimeStamp.set(Calendar.MILLISECOND,millisecond);
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
		int hour = (int) (millisecond / (1000 * 60 * 60));
		millisecond %= (1000 * 60 * 60);

		int minute = (int) (millisecond / (1000 * 60));
		millisecond %= (1000 * 60);

		int second = (int) (millisecond / 1000);

		millisecond = (int) (millisecond % 1000);

		set(hour,minute,second,(int) millisecond);
	}

	/**
	 * Sets this <CODE>TimeStamp</CODE> object corresponding to the specified string representation.
	 * <P>
	 * The string has to have the following specific format:
	 * <P>
	 * <B>HH:mm</B> or <B>HH:mm:ss</B>, or <B>HH:mm:ss.mls</B>, e.g., 10:15, 12:45:16, or 05:03:06.002
	 *
	 * @param                           timeString the string representation of the time stamp (in the format HH:mm, HH:mm:ss, or HH:mm:ss.SSS)
	 * @throws DateTimeFormatException  if an error occurred during conversion
	 * @see                             java.text.SimpleDateFormat
	 */
	public void set(String timeString) throws DateTimeFormatException
	{
		fTimeStamp = Calendar.getInstance();

		String[] timeStampParts = timeString.split("\\:");
		try {
			int hour = Integer.parseInt(timeStampParts[0]);
			int minute = Integer.parseInt(timeStampParts[1]);
			int second = 0;
			int millisecond = 0;
			if (timeStampParts.length > 2) {
				if (timeStampParts[2].indexOf(".") > -1) {
					String[] secondParts = timeStampParts[2].split("\\.");
					second = Integer.parseInt(secondParts[0]);
					millisecond = Integer.parseInt(secondParts[1]);
				}
				else {
					second = Integer.parseInt(timeStampParts[2]);
				}
			}
			set(hour,minute,second,millisecond);
		}
		catch (ArrayIndexOutOfBoundsException exc) {
			throw (new DateTimeFormatException(timeString));
		}
		catch (NumberFormatException exc) {
			throw (new DateTimeFormatException(timeString));
		}
	}

	/**
	 * Sets this <CODE>TimeStamp</CODE> to the current system time.
	 */
	public void setToCurrentTime()
	{
		fTimeStamp = Calendar.getInstance();
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s hour of the day [0-23].
	 *
	 * @return the hour
	 */
	public int getHour()
	{
		return fTimeStamp.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s minute [0-59].
	 * 
	 * @return the minute
	 */
	public int getMinute()
	{
		return fTimeStamp.get(Calendar.MINUTE);
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s second [0-59].
	 * 
	 * @return the second
	 */
	public int getSecond()
	{
		return fTimeStamp.get(Calendar.SECOND);
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s millisecond [0-999].
	 * 
	 * @return the millisecond
	 */
	public int getMillisecond()
	{
		return fTimeStamp.get(Calendar.MILLISECOND);
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s minute-of-day (since 00:00:00.000).
	 *
	 * @return the number of minutes (since 00:00:00:000) corresponding to this <CODE>TimeStamp</CODE> object
	 */
	public int getMinuteOfDay()
	{
		return (getMinute() + (getHour() * 60));
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s second-of-day (since 00:00:00.000).
	 *
	 * @return the number of seconds (since 00:00:00:000) corresponding to this <CODE>TimeStamp</CODE> object
	 */
	public int getSecondOfDay()
	{
		return (getSecond() + (60 * getMinute()) + (getHour() * 3600));
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s millisecond-of-day (since 00:00:00.000).
	 *
	 * @return the number of milliseconds (since 00:00:00:000) corresponding to this <CODE>TimeStamp</CODE> object
	 */
	public int getMillisecondOfDay()
	{
		return (getMillisecond() + (getSecond() * 1000) + (getMinute() * 1000 * 60) + (getHour() * 1000 * 60 * 60));
	}

	/**
	 * Returns a Java <CODE>Date</CODE> object representing this <CODE>TimeStamp</CODE>.
	 *
	 * @return a Java <CODE>Date</CODE> object representing this <CODE>TimeStamp</CODE>
	 */
	public Date getJavaDate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,getHour());
		calendar.set(Calendar.MINUTE,getMinute());
		calendar.set(Calendar.SECOND,getSecond());
		calendar.set(Calendar.MILLISECOND,getMillisecond());
		return calendar.getTime();
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm:ss.SSS.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm:ss.SSS
	 * @see    java.text.SimpleDateFormat
	 */
	public String getHMSMsString()
	{
		return (new SimpleDateFormat("HH:mm:ss.SSS")).format(fTimeStamp.getTime());
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm:ss.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm:ss
	 * @see    java.text.SimpleDateFormat
	 */
	public String getHMSString()
	{
		return (new SimpleDateFormat("HH:mm:ss")).format(fTimeStamp.getTime());
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format HH:mm
	 * @see    java.text.SimpleDateFormat
	 */
	public String getHMString()
	{
		return (new SimpleDateFormat("HH:mm")).format(fTimeStamp.getTime());
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format mm:ss.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format mm:ss
	 * @see    java.text.SimpleDateFormat
	 */
	public String getMSString()
	{
		return (new SimpleDateFormat("mm:ss")).format(fTimeStamp.getTime());
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format mm:ss.SSS.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format mm:ss.SSS
	 * @see    java.text.SimpleDateFormat
	 */
	public String getMSMsString()
	{
		return (new SimpleDateFormat("mm:ss.SSS")).format(fTimeStamp.getTime());
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format ss.SSS.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format ss.SSS
	 * @see    java.text.SimpleDateFormat
	 */
	public String getSMsString()
	{
		return (new SimpleDateFormat("ss.SSS")).format(fTimeStamp.getTime());
	}

	/**
	 * Returns a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format ss.
	 *
	 * @return a <CODE>String</CODE> representation of this <CODE>TimeStamp</CODE> object in the format ss
	 * @see    java.text.SimpleDateFormat
	 */
	public String getSString()
	{
		return (new SimpleDateFormat("ss")).format(fTimeStamp.getTime());
	}

	/**
	 * Converts this <CODE>TimeStamp</CODE> object from one time zone to another.
	 * Note that daylight savings are taken into account when applicable.
	 * <P> 
	 * Time zones can be obtained via, e.g., <CODE>TimeZone.getTimeZone("Europe/Brussels")</CODE>.
	 * 
	 * @param date          the <CODE>DateStamp</CODE> of this <CODE>TimeStamp</CODE> object
	 * @param fromTimeZone  the <CODE>TimeZone</CODE> to convert from
	 * @param toTimeZone    the <CODE>TimeZone</CODE> to convert to
	 * 
	 */
	public void convertBetweenTimeZones(DateStamp date, TimeZone fromTimeZone, TimeZone toTimeZone)
	{
		Calendar fromCalendar = Calendar.getInstance(fromTimeZone);
		Calendar toCalendar = Calendar.getInstance(toTimeZone);

		// perform conversion via the total milliseconds timestamp
		fromCalendar.set(Calendar.DAY_OF_MONTH,date.getDay());
		fromCalendar.set(Calendar.MONTH,date.getMonth() - fromCalendar.getMinimum(Calendar.MONTH) - 1);
		fromCalendar.set(Calendar.YEAR,date.getYear());
		fromCalendar.set(Calendar.HOUR_OF_DAY,getHour());
		fromCalendar.set(Calendar.MINUTE,getMinute());
		fromCalendar.set(Calendar.SECOND,getSecond());
		fromCalendar.set(Calendar.MILLISECOND,getMillisecond());
		long utcMilliseconds = fromCalendar.getTimeInMillis();

		toCalendar.setTimeInMillis(utcMilliseconds);
		set(toCalendar.get(Calendar.HOUR_OF_DAY),toCalendar.get(Calendar.MINUTE),toCalendar.get(Calendar.SECOND),toCalendar.get(Calendar.MILLISECOND));
	}

	/**
	 * Converts this <CODE>TimeStamp</CODE> object from a time zone to UTC.
	 * Note that daylight savings are taken into account when applicable.
	 * 
	 * @param date          the <CODE>DateStamp</CODE> of this <CODE>TimeStamp</CODE> object
	 * @param fromTimeZone  the <CODE>TimeZone</CODE> to convert from
	 * 
	 */
	public void convertFromTimeZoneToUTC(DateStamp date, TimeZone fromTimeZone)
	{
		convertBetweenTimeZones(date,fromTimeZone,TimeZone.getTimeZone("UTC"));
	}

	/**
	 * Converts this <CODE>TimeStamp</CODE> object from UTC to a time zone.
	 * Note that daylight savings are taken into account when applicable.
	 * 
	 * @param date        the <CODE>DateStamp</CODE> of this <CODE>TimeStamp</CODE> object
	 * @param toTimeZone  the <CODE>TimeZone</CODE> to convert to
	 * 
	 */
	public void convertFromUTCToTimeZone(DateStamp date, TimeZone toTimeZone)
	{
		convertBetweenTimeZones(date,TimeZone.getTimeZone("UTC"),toTimeZone);
	}

	/**
	 * Converts this <CODE>TimeStamp</CODE> object from the user's local time zone to UTC.
	 * Note that daylight savings are taken into account when applicable.
	 * 
	 * @param date  the <CODE>DateStamp</CODE> of this <CODE>TimeStamp</CODE> object
	 * 
	 */
	public void convertFromLocalTimeZoneToUTC(DateStamp date)
	{
		convertFromTimeZoneToUTC(date,TimeZone.getDefault());
	}

	/**
	 * Converts this <CODE>TimeStamp</CODE> object from UTC to the user's local time zone.
	 * Note that daylight savings are taken into account when applicable.
	 * 
	 * @param date  the <CODE>DateStamp</CODE> of this <CODE>TimeStamp</CODE> object
	 * 
	 */
	public void convertFromUTCToLocalTimeZone(DateStamp date)
	{
		convertFromUTCToTimeZone(date,TimeZone.getDefault());
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

		TimeStamp timeStamp = (TimeStamp) object;
		if ((getHour() != timeStamp.getHour()) ||
				(getMinute() != timeStamp.getMinute()) ||
				(getSecond() != timeStamp.getSecond()) ||
				(getMillisecond() != timeStamp.getMillisecond())) {
			return false;
		}

		return true;
	}
}
