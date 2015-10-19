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
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.epadws.processing.pipeline.task.UnzipTask;
/* 
 * Note: This class does not appear to be in use - delete?
 */
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
