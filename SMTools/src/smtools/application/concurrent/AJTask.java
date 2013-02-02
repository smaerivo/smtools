// ------------------------------
// Filename      : AJTask.java
// Author        : Sven Maerivoet
// Last modified : 02/02/2013
// Target        : Java VM (1.6)
// ------------------------------

/**
 * Copyright 2003-2013 Sven Maerivoet
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

package smtools.application.concurrent;

import java.util.concurrent.*;
import javax.swing.*;
import smtools.swing.util.*;

/**
 * The <CODE>AJTask</CODE> class provides the basic functionality for a task.
 * <P>
 * A user must implement the {@link AJTask#executeSubTask(int)} and {@link AJTask#finishTask} methods. The former is called for each subtask, whereas the latter is called
 * upon completion of all the subtasks.
 * <P>
 * <B>Note that this is an abstract class.</B>
 *
 * @author  Sven Maerivoet
 * @version 02/02/2013
 * @see     JTaskExecutor
 */
public abstract class AJTask extends SwingWorker<Void,Integer>
{
	/**
	 * The ID of this task.
	 */
	protected int fID;

	// internal datastructures
	private int fNrOfSubTasks;
	private CountDownLatch fCountDownLatch;
	private JProgressUpdateGlassPane fProgressUpdateGlassPane;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an <CODE>AJTask</CODE> object for 1 task.
	 */
	public AJTask()
	{
		super();
		setNrOfSubTasks(1);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the ID of this task.
	 *
	 * @param id the ID of this task
	 */
	public final void setID(int id)
	{
		fID = id;
	}

	/**
	 * Sets the number of subtasks.
	 *
	 * @param nrOfSubTasks the number of subtasks
	 */
	public final void setNrOfSubTasks(int nrOfSubTasks)
	{
		fNrOfSubTasks = nrOfSubTasks;
	}

	/**
	 * Returns the number of subtasks.
	 *
	 * @return the number of subtasks
	 */
	public final int getNrOfSubTasks()
	{
		return fNrOfSubTasks;
	}

	/**
	 * Installs the <CODE>CountDownLatch</CODE> that is used to synchronise this task.
	 *
	 * @param countDownLatch the <CODE>CountDownLatch</CODE> to use for synchronisation
	 */
	public final void installCountDownLatch(CountDownLatch countDownLatch)
	{
		fCountDownLatch = countDownLatch;
	}

	/**
	 * Installs the <CODE>AJProgressUpdateGlassPane</CODE> that is used for progress updates of this task.
	 *
	 * @param progressUpdateGlassPane the <CODE>JProgressUpdateGlassPane</CODE> to use for progress updates of this task
	 */
	public final void installProgressUpdateGlassPane(JProgressUpdateGlassPane progressUpdateGlassPane)
	{
		fProgressUpdateGlassPane = progressUpdateGlassPane;
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * This method is called when a subtask is executed.
	 *
	 * @param subTaskID the ID of the subtask (between 0 and the number of subtasks - 1)
	 */
	protected abstract void executeSubTask(int subTaskID);

	/**
	 * This method is called when all subtasks have finished.
	 */
	protected abstract void finishTask();

	/**
	 */
	@Override
	protected final Void doInBackground() throws Exception
	{
		for (int subTaskID = 0; subTaskID < fNrOfSubTasks; ++subTaskID) {
			executeSubTask(subTaskID);
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
