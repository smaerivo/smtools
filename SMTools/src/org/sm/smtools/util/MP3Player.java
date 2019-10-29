// ------------------------------
// Filename      : MP3Player.java
// Author        : Sven Maerivoet
// Last modified : 06/08/2019
// Target        : Java VM (1.8)
// ------------------------------

/**
 * Copyright 2003-2019 Sven Maerivoet
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
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;
import org.apache.log4j.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.exceptions.*;

/**
 * The <CODE>MP3Player</CODE> class is a helper class for the JLayer framework.
 * <P>
 * When playing a sound file or stream, the application's execution can be blocked, or it the playing can be delegated to a non-blocking thread.
 * <P>
 * Note that a valid {@link I18NL10N} database must be available!
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 06/08/2019
 */
public final class MP3Player extends Thread
{
	/**
	 * Useful constants to specify whether or not the application's execution is blocked while playing a sound.
	 */
	public static enum EPlaying {kBlocked, kUnblocked};

	// access point to the Log4j logging facility
	private static final Logger kLogger = Logger.getLogger(MP3Player.class.getName());

	// toggle switch for playing system sounds
	private static boolean fSystemSoundsEnabled;
	
	// internal datastructures
	private Player fPlayer;

	/*************************
	 * STATIC INITIALISATION *
	 *************************/

	static {
		enableSystemSounds();
	}

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an <CODE>MP3Player</CODE> object based on a specified filename.
	 *
	 * @param soundFilename           the name of the file containing the MP3 sound
	 * @throws FileNotFoundException  if the specified file could not be found or loaded
	 * @throws SoundPlayingException  if something went wrong during playing
	 */
	public MP3Player(String soundFilename) throws FileNotFoundException, SoundPlayingException
	{
		loadSoundFile(soundFilename);
	}

	/**
	 * Constructs an <CODE>MP3Player</CODE> object based on a specified <CODE>InputStream</CODE>.
	 *
	 * @param soundInputStream        the <CODE>InputStream</CODE> containing the MP3 sound
	 * @throws SoundPlayingException  if something went wrong during playing
	 */
	public MP3Player(InputStream soundInputStream) throws SoundPlayingException
	{
		createPlayer(soundInputStream);
	}

	/**************
	 * DESTRUCTOR *
	 **************/

	/**
	 * Class destructor.
	 */
	public void finalize()
	{
		fPlayer.close();
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * The <CODE>Thread</CODE>'s run() method.
	 */
	@Override
	public void run()
	{
		try {
			fPlayer.play();
		}
		catch (JavaLayerException exc) {
			// ignore
		}
	}

	/**
	 * Starts playing a loaded sound file or stream.
	 *
	 * @param playing                 specifies whether or not the application's execution should be blocked when playing the sound
	 * @throws SoundPlayingException  when an exception occurs during the sound playing
	 */
	public void play(EPlaying playing) throws SoundPlayingException
	{
		try {
			if (playing == EPlaying.kBlocked) {
				fPlayer.play();
			}
			else {
				// execute thread
				start();
			}
		}
		catch (JavaLayerException exc) {
			throw (new SoundPlayingException());
		}
	}

	/**
	 * Enables the playing of system sounds.
	 */
	public static void enableSystemSounds()
	{
		fSystemSoundsEnabled = true;
	}

	/**
	 * Disables the playing of system sounds.
	 */
	public static void disableSystemSounds()
	{
		fSystemSoundsEnabled = false;
	}

	/**
	 * Returns whether or not system sounds are enabled.
	 * 
	 * @return <CODE>true</CODE> if system sounds are enabled, <CODE>false</CODE> otherwise
	 */
	public static boolean systemSoundsEnabled()
	{
		return fSystemSoundsEnabled;
	}

	/**
	 * Plays a specified system sound.
	 * <P>
	 * Note that the application is not blocked.
	 * 
	 * @param soundFilename  the filename of the system sound to play
	 */
	public static void playSystemSound(String soundFilename)
	{
		playSystemSound(soundFilename,EPlaying.kUnblocked);
	}

	/**
	 * Plays a specified system sound.
	 * 
	 * @param soundFilename  the filename of the system sound to play
	 * @param playing        specifies whether or not the application's execution should be blocked when playing the sound
	 */
	public static void playSystemSound(String soundFilename, EPlaying playing)
	{
		if (soundFilename == null) {
			return;
		}

		if (!DevelopMode.kINSTANCE.isActivated() && (fSystemSoundsEnabled)) {
			try {
				(new MP3Player(JARResources.fSystemResources.getInputStream(soundFilename))).play(playing);
			}
			catch (FileNotFoundException exc) {
				// ignore
			}
			catch (SoundPlayingException exc) {
				kLogger.error(I18NL10N.kINSTANCE.translate("error.PlayingSound"));
			}
		}
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 * @param soundFilename           -
	 * @throws FileNotFoundException  -
	 * @throws SoundPlayingException  -
	 */
	private void loadSoundFile(String soundFilename) throws FileNotFoundException, SoundPlayingException
	{
		try {
			FileInputStream fileInputStream = new FileInputStream(soundFilename);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
			createPlayer(bufferedInputStream);
		}
		catch (FileNotFoundException exc) {
			kLogger.error(I18NL10N.kINSTANCE.translate("error.SoundFileNotFound",soundFilename));
			throw (new FileNotFoundException(soundFilename));
		}		
	}

	/**
	 * @param soundInputStream        -
	 * @throws SoundPlayingException  -
	 */
	private void createPlayer(InputStream soundInputStream) throws SoundPlayingException
	{
		try {
			AudioDevice audioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
			fPlayer = new Player(soundInputStream,audioDevice);
		}
		catch (JavaLayerException exc) {
			kLogger.error(I18NL10N.kINSTANCE.translate("error.PlayingSound"));
			throw (new SoundPlayingException());
		}
	}
}
