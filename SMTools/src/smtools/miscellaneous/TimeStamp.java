// ------------------------------
// Filename      : TimeStamp.java
// Author        : Sven Maerivoet
// Last modified : 16/05/2011
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
import smtools.exceptions.*;

/**
 * The <CODE>TimeStamp</CODE> class acts as a container for time stamps.
 * <P>
 * A time stamp is defined by its hour, minute, second and millisecond.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 16/05/2011
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
	 * @param hour   the hour component
	 * @param minute the minute component
	 * @param second the second component
	 * @see   TimeStamp#TimeStamp()
	 * @see   TimeStamp#TimeStamp(int,int,int,int)
	 * @see   TimeStamp#TimeStamp(long)
	 * @see   TimeStamp#TimeStamp(String)
	 * @see   TimeStamp#TimeStamp(TimeStamp)
	 */
	public TimeStamp(int hour, int minute, int second)
	{
		set(hour,minute,second,0);
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to the specified values.
	 *
	 * @param hour        the hour component
	 * @param minute      the minute component
	 * @param second      the second component
	 * @param millisecond the millisecond component
	 * @see   TimeStamp#TimeStamp()
	 * @see   TimeStamp#TimeStamp(int,int,int)
	 * @see   TimeStamp#TimeStamp(long)
	 * @see   TimeStamp#TimeStamp(String)
	 * @see   TimeStamp#TimeStamp(TimeStamp)
	 */
	public TimeStamp(int hour, int minute, int second, int millisecond)
	{
		set(hour,minute,second,millisecond);
	}

	/**
	 * Constructs a <CODE>TimeStamp</CODE> object corresponding to a number of milliseconds (since 00:00:00.000).
	 *
	 * @param millisecondOfDay the number of milliseconds (since 00:00:00.000) to convert to a <CODE>TimeStamp</CODE> object
	 * @see   TimeStamp#TimeStamp()
	 * @see   TimeStamp#TimeStamp(int,int,int)
	 * @see   TimeStamp#TimeStamp(int,int,int, int)
	 * @see   TimeStamp#TimeStamp(String)
	 * @see   TimeStamp#TimeStamp(TimeStamp)
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
	 * <UL>
	 *   <B>HH:mm:ss</B> or <B>HH:mm:ss.mls</B>, e.g., 12:45:16 or 05:03:06.002
	 * </UL>
	 *
	 * @param  timeString the string representation of the time stamp (in the format HH:mm:ss.SSS)
	 * @see    TimeStamp#TimeStamp()
	 * @see    TimeStamp#TimeStamp(int,int,int)
	 * @see    TimeStamp#TimeStamp(long)
	 * @see    TimeStamp#TimeStamp(TimeStamp)
	 * @throws DateTimeFormatException if an error occurred during conversion
	 * @see    java.text.SimpleDateFormat
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
	 * @param timeStamp the <CODE>TimeStamp</CODE> object to <B>deep copy</B>
	 * @see   TimeStamp#TimeStamp()
	 * @see   TimeStamp#TimeStamp(int,int,int)
	 * @see   TimeStamp#TimeStamp(int,int,int, int)
	 * @see   TimeStamp#TimeStamp(long)
	 * @see   TimeStamp#TimeStamp(String)
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
	 * @param hour        the hour component
	 * @param minute      the minute component
	 * @param second      the second component
	 * @param millisecond the millisecond component
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
	 * @param timeStamp the <CODE>TimeStamp</CODE> to copy
	 */
	public void set(TimeStamp timeStamp)
	{
		set(timeStamp.getHour(),timeStamp.getMinute(),timeStamp.getSecond(),timeStamp.getMillisecond());
	}

	/**
	 * Sets this <CODE>TimeStamp</CODE> based on a cumulative millisecond representation (since 00:00:00.000).
	 *
	 * @param millisecond the number of milliseconds (since 00:00:00.000) to convert to a <CODE>TimeStamp</CODE> object
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
	 * <UL>
	 *   <B>HH:mm:ss</B> or <B>HH:mm:ss.mls</B>, e.g., 12:45:16 or 05:03:06.002
	 * </UL>
	 *
	 * @param  timeString the string representation of the time stamp (in the format HH:mm:ss or HH:mm:ss.SSS)
	 * @throws DateTimeFormatException if an error occurred during conversion
	 * @see    java.text.SimpleDateFormat
	 */
	public void set(String timeString) throws DateTimeFormatException
	{
		fTimeStamp = Calendar.getInstance();
		try {
			fTimeStamp.setTime((new SimpleDateFormat("HH:mm:ss")).parse(timeString));
		}
		catch (ParseException excType1) {
			try {
				fTimeStamp.setTime((new SimpleDateFormat("HH:mm:ss.SSS")).parse(timeString));
			}
			catch (ParseException excType2) {
				throw (new DateTimeFormatException(timeString));
			}
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
	 * Returns this <CODE>TimeStamp</CODE>'s hour of day [0-23].
	 */
	public int getHour()
	{
		return fTimeStamp.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s minute [0-59].
	 */
	public int getMinute()
	{
		return fTimeStamp.get(Calendar.MINUTE);
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s second [0-59].
	 */
	public int getSecond()
	{
		return fTimeStamp.get(Calendar.SECOND);
	}

	/**
	 * Returns this <CODE>TimeStamp</CODE>'s millisecond [0-999].
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
	 * The overloaded <CODE>Comparable<CODE> interface.
	 */
	@Override
	public int compareTo(TimeStamp otherTimeStamp)
	{
		Calendar otherCalendar = Calendar.getInstance();
		otherCalendar.set(Calendar.HOUR_OF_DAY,otherTimeStamp.getHour());
		otherCalendar.set(Calendar.MINUTE,otherTimeStamp.getMinute());
		otherCalendar.set(Calendar.SECOND,otherTimeStamp.getSecond());
		otherCalendar.set(Calendar.MILLISECOND,otherTimeStamp.getMillisecond());

		return fTimeStamp.compareTo(otherCalendar);
	}

	/**
	 * Default overloaded <CODE>toString()<CODE> method.
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
