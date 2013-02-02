// ----------------------------------
// Filename      : JTaskExecutor.java
// Author        : Sven Maerivoet
// Last modified : 02/02/2013
// Target        : Java VM (1.6)
// ----------------------------------

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

import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import smtools.swing.util.*;

/**
 * The <CODE>JTaskExecutor</CODE> class provides a facility for concurrently executing a number of <CODE>AJTask</CODE>s.
 * <P>
 * The class executes a certain number of tasks simultaneously, each one starting in its own thread. It then waits until all tasks have completed.
 * Each task can consist of multiple subtasks that are all executed within the same task's thread. Results can be collected via the {@link JTaskExecutor#finishTasks()} method.
 * <P>
 * Progress of the tasks' executions is shown via an optional progress update glasspane.
 * 
 * @author  Sven Maerivoet
 * @version 02/02/2013
 * @see     AJTask
 */
public class JTaskExecutor extends SwingWorker<Void,Void>
{
	// the number of threads to use
	private final static int kNrOfThreads = 1000;

	// internal datastructures
	private ArrayList<AJTask> fTasks;
	private CountDownLatch fCountDownLatch;
	private JProgressUpdateGlassPane fProgressUpdateGlassPane;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JTaskExecutor</CODE> object with a specified progress update glasspane.
	 *
	 * @param progressUpdateGlassPane the progress update glasspane to use for task updates
	 */
	public JTaskExecutor(JProgressUpdateGlassPane progressUpdateGlassPane)
	{
		super();
		fTasks = new ArrayList<AJTask>();
		fProgressUpdateGlassPane = progressUpdateGlassPane;

		// override Swing's internal fixed number of worker threads
		sun.awt.AppContext.getAppContext().put(SwingWorker.class,Executors.newFixedThreadPool(kNrOfThreads));
	}

	/**
	 * Constructs a <CODE>JTaskExecutor</CODE> object without a progress update glasspane.
	 */
	public JTaskExecutor()
	{
		this(null);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Adds a task to be executed.
	 *
	 * @param task the task to be executed
	 */
	public final void addTask(AJTask task)
	{
		fTasks.add(task);
	}

	/**
	 * Adds a series of tasks to be executed.
	 *
	 * @param tasks the tasks to be executed
	 */
	public final void addTasks(ArrayList<AJTask> tasks)
	{
		for (AJTask task : tasks) {
			addTask(task);
		}
	}

	/**
	 * Returns the list with tasks.
	 *
	 * @return the list with tasks
	 */
	public final ArrayList<AJTask> getTasks()
	{
		return fTasks;
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
	 */
	@Override
	protected final Void doInBackground() throws Exception
	{
		// setup the synchronisation and progress updating mechanisms
		int totalNrOfProgressUpdates = 0;
		fCountDownLatch = new CountDownLatch(fTasks.size());
		for (AJTask task : fTasks) {
			task.installCountDownLatch(fCountDownLatch);
			task.installProgressUpdateGlassPane(fProgressUpdateGlassPane);
			totalNrOfProgressUpdates += task.getNrOfSubTasks();
		}
		if (fProgressUpdateGlassPane != null) {
			fProgressUpdateGlassPane.reset();
			fProgressUpdateGlassPane.setTotalNrOfProgressUpdates(totalNrOfProgressUpdates);
		}

		// execute all tasks
		for (AJTask task : fTasks) {
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
			if (fProgressUpdateGlassPane != null) {
				finishTasks();
				fProgressUpdateGlassPane.done();
			}
		}
	}
}
