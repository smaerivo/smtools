// ------------------------------
// Filename      : ATask.java
// Author        : Sven Maerivoet
// Last modified : 23/05/2014
// Target        : Java VM (1.8)
// ------------------------------

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

package org.sm.smtools.application.concurrent;

import java.util.concurrent.*;
import javax.swing.*;
import org.sm.smtools.swing.util.*;

/**
 * The <CODE>ATask</CODE> class provides the basic functionality for a task.
 * <P>
 * Derived classes must implement the {@link ATask#executeTask()} and {@link ATask#finishTask} methods.
 * <P>
 * <B>Note that this is an abstract class.</B>
 *
 * @author  Sven Maerivoet
 * @version 23/05/2014
 * @see     TaskExecutor
 */
public abstract class ATask extends SwingWorker<Void,Integer>
{
	// internal datastructures
	private CountDownLatch fCountDownLatch;
	private JProgressUpdateGlassPane fProgressUpdateGlassPane;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an <CODE>ATask</CODE> object.
	 */
	public ATask()
	{
		super();
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * This method is called when the task is executed.
	 */
	protected abstract void executeTask();

	/**
	 * This method is called when the task has finished.
	 */
	protected abstract void finishTask();

	/**
	 * Installs the <CODE>CountDownLatch</CODE> that is used to synchronise this task.
	 *
	 * @param countDownLatch  the <CODE>CountDownLatch</CODE> to use for synchronisation
	 */
	protected final void installCountDownLatch(CountDownLatch countDownLatch)
	{
		fCountDownLatch = countDownLatch;
	}

	/**
	 * Installs the <CODE>AJProgressUpdateGlassPane</CODE> that is used for progress updates of this task.
	 *
	 * @param progressUpdateGlassPane  the <CODE>JProgressUpdateGlassPane</CODE> to use for progress updates of this task
	 */
	protected final void installProgressUpdateGlassPane(JProgressUpdateGlassPane progressUpdateGlassPane)
	{
		fProgressUpdateGlassPane = progressUpdateGlassPane;
	}

	/**
	 * @return            -
	 * @throws Exception  -
	 */
	@Override
	protected final Void doInBackground() throws Exception
	{
		if (!isCancelled()) {
			executeTask();
			publish(0);
		}
		return null;
	}

	/**
	 */
	@Override
	@SuppressWarnings("unused")
	protected final void process(java.util.List<Integer> chunks)
	{
		if (fProgressUpdateGlassPane != null) {
			for (int dummy : chunks) {
				fProgressUpdateGlassPane.signalProgressUpdate();
			}
		}
	}

	/**
	 */
	@Override
	protected final void done()
	{
		if (!isCancelled()) {
			finishTask();
			fCountDownLatch.countDown();
		}
	}
}
