/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline.process;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import edu.stanford.isis.epadws.processing.pipeline.task.UnzipTask;

public class UnzipProcess extends AbstractPipelineProcess<List<File>>
{
	public UnzipProcess(ExecutorService unzipExec, BlockingQueue<File> unzipQueue, BlockingQueue<File> tagQueue)
	{
		super(unzipExec, unzipQueue, tagQueue);
	}

	@Override
	public Callable<List<File>> getTask(File submitFile)
	{
		logger.info("UnzipProcess.getTask(" + submitFile.getName() + ")");
		return new UnzipTask(submitFile);
	}

	@Override
	public void process() throws InterruptedException, ExecutionException
	{
		submitTask();

		// since List<File> isn't the typical case using custom response here.
		for (Future<List<File>> currTask : tasksInProgress) {
			if (currTask.isDone()) {
				List<File> files = currTask.get();
				if (files == null) {
					logger.info("Null from UnzipTask. Don't forward file.");
					continue;
				}
				for (File currFile : files) {
					boolean taken = outQueue.offer(currFile, 500, TimeUnit.MILLISECONDS);

					if (taken) {
						tasksInProgress.remove(currTask);
					} else {
						logger.info("WARNING: Tag queue is blocking. It might be full. Failed to take file="
								+ currFile.getAbsolutePath());
					}
				}
			}
		}
	}

	@Override
	public String getProcessName()
	{
		return "Unzip";
	}
}
