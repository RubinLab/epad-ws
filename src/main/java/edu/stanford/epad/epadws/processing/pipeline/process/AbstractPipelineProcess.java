//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.processing.pipeline.process;

import java.io.File;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.util.ConcurrentHashSet;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;

/**
 * Abstract class for a stage in the pipeline has the basic components of a pipeline process.
 */
public abstract class AbstractPipelineProcess<V> implements Runnable
{
	protected static final EPADLogger logger = EPADLogger.getInstance();
	protected final ShutdownSignal signal = ShutdownSignal.getInstance();

	protected final ExecutorService exec;
	protected final BlockingQueue<File> inQueue;
	protected final BlockingQueue<File> outQueue;

	final Set<Future<V>> tasksInProgress = new ConcurrentHashSet<Future<V>>();

	public AbstractPipelineProcess(ExecutorService exec, BlockingQueue<File> inQueue, BlockingQueue<File> outQueue)
	{
		this.exec = exec;
		this.inQueue = inQueue;
		this.outQueue = outQueue;
	}

	@Override
	public void run()
	{
		while (!signal.hasShutdown()) {
			try {
				process();
			} catch (InterruptedException e) {
				logger.warning("Interrupted Exception", e);
			} catch (ExecutionException e) {
				logger.severe(getProcessName() + " process ExecutionException", e);
				e.printStackTrace();
			} catch (Exception e) {
				logger.severe(getProcessName() + " process had exception", e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * A typical Task submit process for Callable<V> tasks.
	 * 
	 * @throws InterruptedException on interrupt
	 */
	protected void submitTask() throws InterruptedException
	{
		File file = inQueue.poll(100, TimeUnit.MILLISECONDS);
		if (file != null) {
			logger.info("submitTask(" + getProcessName() + "): file=" + file.getAbsolutePath());
			Future<V> task = exec.submit(getTask(file));
			tasksInProgress.add(task);
		}
	}

	/**
	 * Create a new task for this process.
	 * 
	 * @param submitFile File
	 * @return Callable<V>
	 */
	public abstract Callable<V> getTask(File submitFile);

	/**
	 * A typical method for checking and forwarding completed tasks. This method is tied to V as <File>
	 * 
	 * @throws InterruptedException on interrupt
	 * @throws java.util.concurrent.ExecutionException on task execute exception
	 */
	protected void forwardCompletedTasks() throws InterruptedException, ExecutionException
	{
		for (Future<V> currTask : tasksInProgress) {
			if (currTask.isDone()) {
				File currFile = (File)currTask.get();
				if (currFile == null) {
					tasksInProgress.remove(currTask);
					continue;
				}

				if (outQueue == null) {
					tasksInProgress.remove(currTask);
					return;
				}

				boolean taken = outQueue.offer(currFile, 500, TimeUnit.MILLISECONDS);
				if (taken) {
					tasksInProgress.remove(currTask);
				} else {
					logger.info("Could not submit task for " + currFile.getAbsolutePath() + " from the " + getProcessName()
							+ " queue.");
				}
			}
		}
	}

	/**
	 * Code specific to this process.
	 * 
	 * @throws InterruptedException on interrupt
	 * @throws java.util.concurrent.ExecutionException on task exception
	 */
	public abstract void process() throws InterruptedException, ExecutionException;

	/**
	 * Return a short name for this stage in the process.
	 * 
	 * @return String
	 */
	public abstract String getProcessName();

	/**
	 * Return the number of tasks in progress for measuring the throughput of the pipeline.
	 * 
	 * @return int number of items in the tasksInProgress map.
	 */
	public int getTasksInProgressCount()
	{
		return tasksInProgress.size();
	}
}
