// ---------------------------------
// Filename      : TaskExecutor.java
// Author        : Sven Maerivoet
// Last modified : 07/05/2014
// Target        : Java VM (1.8)
// ---------------------------------

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

import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.swing.util.*;

/**
 * The <CODE>TaskExecutor</CODE> class provides a facility for concurrently executing a number of <CODE>ATask</CODE>s.
 * <P>
 * The class executes a certain number of tasks simultaneously, each one starting in its own thread.<BR>
 * It then waits until all tasks have completed.<BR>
 * Each task can consist of multiple subtasks that are all executed within the same task's thread.
 * <P>
 * Tasks are added via the {@link TaskExecutor#addTask(ATask)} or {@link TaskExecutor#addTasks(ArrayList)} methods;
 * results can be collected via the {@link TaskExecutor#finishTasks()} method.
 * <P>
 * Once all tasks are defined, a user calls the <CODE>TaskExecutor.execute()</CODE> method; the class can only be used once.
 * The current state of execution can be queried via the {@link TaskExecutor#isBusy()} method.
 * <P>
 * Progress of the tasks' executions is shown via an optional progress update glasspane.
 * 
 * @author  Sven Maerivoet
 * @version 07/05/2014
 * @see     ATask
 */
public class TaskExecutor extends SwingWorker<Void,Void>
{
	// internal datastructures
	private ArrayList<ATask> fTasks;
	private CountDownLatch fCountDownLatch;
	private JProgressUpdateGlassPane fProgressUpdateGlassPane;
	private boolean fBusy;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>TaskExecutor</CODE> object with a specified progress update glasspane.
	 *
	 * @param progressUpdateGlassPane  the progress update glasspane to use for task updates
	 * @param nrOfThreads              the number of threads to use
	 */
	public TaskExecutor(JProgressUpdateGlassPane progressUpdateGlassPane, int nrOfThreads)
	{
		super();
		fProgressUpdateGlassPane = progressUpdateGlassPane;
		fTasks = new ArrayList<ATask>();
		fBusy = false;

		// override Swing's internal fixed number of worker threads
		sun.awt.AppContext.getAppContext().put(SwingWorker.class,Executors.newFixedThreadPool(nrOfThreads));
	}

	/**
	 * Constructs a <CODE>TaskExecutor</CODE> object with a specified progress update glasspane
	 * and with as meany threads as there are processors..
	 *
	 * @param progressUpdateGlassPane  the progress update glasspane to use for task updates
	 */
	public TaskExecutor(JProgressUpdateGlassPane progressUpdateGlassPane)
	{
		super();
		fProgressUpdateGlassPane = progressUpdateGlassPane;
		fTasks = new ArrayList<ATask>();
		fBusy = false;

		// override Swing's internal fixed number of worker threads
		sun.awt.AppContext.getAppContext().put(SwingWorker.class,Executors.newFixedThreadPool(MemoryStatistics.getNrOfProcessors()));
	}

	/**
	 * Constructs a <CODE>TaskExecutor</CODE> object without a progress update glasspane
	 * and with as meany threads as there are processors.
	 */
	public TaskExecutor()
	{
		this(null);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Adds a task to be executed.
	 *
	 * @param task  the task to be executed
	 */
	public final void addTask(ATask task)
	{
		fTasks.add(task);
	}

	/**
	 * Adds a series of tasks to be executed.
	 *
	 * @param tasks  the tasks to be executed
	 */
	public final void addTasks(ArrayList<ATask> tasks)
	{
		for (ATask task : tasks) {
			addTask(task);
		}
	}

	/**
	 * Returns the list with tasks.
	 *
	 * @return the list with tasks
	 */
	public final ArrayList<ATask> getTasks()
	{
		return fTasks;
	}

	/**
	 * Returns whether or not the task executor is busy.
	 *
	 * @return a <CODE>boolean</CODE> indicating whether or not the task executor is busy
	 */
	public final boolean isBusy()
	{
		return fBusy;
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * This method is called when all tasks have finished.
	 */
	protected void finishTasks()
	{
	}

	/**
	 *  @return            -
	 *  @throws Exception  -
	 */
	@Override
	protected final Void doInBackground() throws Exception
	{
		fBusy = true;

		// setup the synchronisation and progress updating mechanisms
		int totalNrOfProgressUpdates = 0;
		fCountDownLatch = new CountDownLatch(fTasks.size());
		for (ATask task : fTasks) {
			task.installCountDownLatch(fCountDownLatch);
			task.installProgressUpdateGlassPane(fProgressUpdateGlassPane);
			totalNrOfProgressUpdates += task.getNrOfSubTasks();
		}
		if (fProgressUpdateGlassPane != null) {
			fProgressUpdateGlassPane.reset();
			fProgressUpdateGlassPane.setTotalNrOfProgressUpdates(totalNrOfProgressUpdates);
		}

		// execute all tasks
		for (ATask task : fTasks) {
			task.execute();
		}

		try {
			// we wait until all worker threads have finished
			fCountDownLatch.await();
		}
		catch (InterruptedException exc) {
			// ignore
		}

		return null;
	}

	/**
	 */
	@Override
	protected final void done()
	{
		if (!isCancelled()) {
			finishTasks();
			if (fProgressUpdateGlassPane != null) {
				fProgressUpdateGlassPane.done();
			}
			fBusy = false;
		}
	}
}
