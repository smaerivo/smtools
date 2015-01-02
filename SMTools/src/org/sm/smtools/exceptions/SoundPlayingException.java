// ------------------------------------------
// Filename      : SoundPlayingException.java
// Author        : Sven Maerivoet
// Last modified : 08/04/2011
// Target        : Java VM (1.8)
// ------------------------------------------

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

package org.sm.smtools.exceptions;

/**
 * Indicates that playing of a sound has failed.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 08/04/2011
 * @see     org.sm.smtools.util.MP3Player
 */
public final class SoundPlayingException extends Exception
{
	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>SoundPlayingException</CODE> object, based on the specified filename.
	 *
	 * @see org.sm.smtools.util.MP3Player
	 */
	public SoundPlayingException()
	{
	}
}
