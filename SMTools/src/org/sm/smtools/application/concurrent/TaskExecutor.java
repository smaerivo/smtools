// ---------------------------------
// Filename      : TaskExecutor.java
// Author        : Sven Maerivoet
// Last modified : 19/09/2014
// Target        : Java VM (1.8)
// ---------------------------------

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

package org.sm.smtools.application.concurrent;

import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import org.sm.smtools.application.util.*;

/**
 * The <CODE>TaskExecutor</CODE> class provides a facility for concurrently executing a number of <CODE>ATask</CODE>s.
 * <P>
 * The class executes a certain number of tasks simultaneously, each one starting in its own thread.<BR>
 * It then waits until all tasks have completed.
 * <P>
 * Tasks are added via the {@link TaskExecutor#addTask(ATask)} or {@link TaskExecutor#addTasks(ArrayList)} methods;
 * results can be collected via the {@link TaskExecutor#finishTasks()} method.
 * <P>
 * Once all tasks are defined, a user calls the <CODE>TaskExecutor.execute()</CODE> method; <B>the class can only be used once</B>.
 * The current state of execution can be queried via the {@link TaskExecutor#isBusy()} method. Threading is executed with
 * a fixed thread pool based on the number of available cores.
 * <P>
 * Progress of the tasks' executions is shown via an optional progress update glasspane.
 * 
 * @author  Sven Maerivoet
 * @version 19/09/2014
 * @see     ATask
 */
public class TaskExecutor extends SwingWorker<Void,Void>
{
	// internal datastructures
	private JProgressUpdateGlassPane fProgressUpdateGlassPane;
	private ArrayList<ATask> fTasks;
	private CountDownLatch fCountDownLatch;
	private ExecutorService fExecutor;
	private int fNrOfThreadsToUse;
	private boolean fBusy;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>TaskExecutor</CODE> object with a specified progress update glasspane and a fixed thread pool
	 * based on the number of available cores.
	 *
	 * @param progressUpdateGlassPane  the progress update glasspane to use for task updates
	 */
	public TaskExecutor(JProgressUpdateGlassPane progressUpdateGlassPane)
	{
		super();
		fProgressUpdateGlassPane = progressUpdateGlassPane;
		fTasks = new ArrayList<ATask>();
		fBusy = false;

		setNrOfThreadsToUse(MemoryStatistics.getNrOfProcessors());
	}

	/**
	 * Constructs a <CODE>TaskExecutor</CODE> object without a progress update glasspane.
	 */
	public TaskExecutor()
	{
		this(null);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the number of threads to use by creating a fixed thread pool.
	 * <P>
	 * Note that this number is bound by the number of available processor cores in the system.
	 * 
	 * @param nrOfThreadsToUse  the number of threads to use for the fixed thread pool
	 */
	public final void setNrOfThreadsToUse(int nrOfThreadsToUse)
	{
		// setup the thread pool
		int nrOfProcessors = MemoryStatistics.getNrOfProcessors();
		if (nrOfThreadsToUse < 1) {
			nrOfThreadsToUse = 1;
		}
		else if (nrOfThreadsToUse > nrOfProcessors) {
			nrOfThreadsToUse = nrOfProcessors;
		}
		fNrOfThreadsToUse = nrOfThreadsToUse;
		fExecutor = Executors.newFixedThreadPool(fNrOfThreadsToUse);
	}

	/**
	 * Returns the number of threads that is used.
	 * 
	 * @return the number of threads that is used
	 */
	public final int getNrOfThreadsToUse()
	{
		return fNrOfThreadsToUse;
	}

	/**
	 * Adds a task to be executed.
	 *
	 * @param task  the task to be executed
	 */
	public final void addTask(ATask task)
	{
		prepareTask(task);
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
	 * Allows preparation of a task before adding it to the internal task list.
	 *
	 * @param task  the task to prepare
	 */
	protected void prepareTask(ATask task)
	{
	}

	/**
	 * Performs custom initialisation before tasks are executed.
	 */
	protected void initialise()
	{
	}

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

		initialise();

		// setup the synchronisation and progress updating mechanisms
		fCountDownLatch = new CountDownLatch(fTasks.size());
		for (ATask task : fTasks) {
			task.installCountDownLatch(fCountDownLatch);
			task.installProgressUpdateGlassPane(fProgressUpdateGlassPane);
		}
		if (fProgressUpdateGlassPane != null) {
			fProgressUpdateGlassPane.reset();
			fProgressUpdateGlassPane.setTotalNrOfProgressUpdates(fTasks.size());
		}

		// execute all tasks
		for (ATask task : fTasks) {
			fExecutor.execute(task);
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
		finishTasks();

		if (fProgressUpdateGlassPane != null) {
			fProgressUpdateGlassPane.done();
		}

		fBusy = false;
	}
}
