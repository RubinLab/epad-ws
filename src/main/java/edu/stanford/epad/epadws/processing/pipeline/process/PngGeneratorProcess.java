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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.pipeline.task.DicomHeadersTask;
import edu.stanford.epad.epadws.processing.pipeline.task.GeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;

/**
 * Create one or multiple of these processes to listen for PngGeneratorTask on the queue and run them when ready.
 * 
 * NOTE: This has been extended to include DICOM Segmentation Object tasks too.
 * 
 * @author alansnyder
 */
public class PngGeneratorProcess implements Runnable
{
	private final BlockingQueue<GeneratorTask> pngTaskQueue;
	private final ExecutorService pngExecs;
	private final ExecutorService tagExec;
	private final EPADLogger logger = EPADLogger.getInstance();
	private final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();

	public PngGeneratorProcess(BlockingQueue<GeneratorTask> taskQueue)
	{
		this.pngTaskQueue = taskQueue;
		pngExecs = Executors.newFixedThreadPool(20);
		tagExec = Executors.newFixedThreadPool(20);
		logger.info("Starting the PNG generator process");
	}

	@Override
	public void run()
	{
		while (!shutdownSignal.hasShutdown()) {
			try {
				GeneratorTask task = pngTaskQueue.poll(500, TimeUnit.MILLISECONDS);
				if (task == null)
					continue;
				pngExecs.execute(task);
				readDicomHeadersTask(task);
			} catch (Exception e) {
				logger.warning("PngGeneratorProcess error", e);
			}
		}
	}

	/**
	 * @param task PngGeneratorTask
	 */
	private void readDicomHeadersTask(GeneratorTask task)
	{
		String taskType = "";
		try {
			taskType = task.getTaskType();
			String tagPath = task.getTagFilePath();
			String seriesUID = task.getSeriesUID();
			logger.info("readDicomHeadersTask, taskType:" + taskType + " seriesUID:" + seriesUID + " tagPath:" + tagPath);
			DicomHeadersTask dicomHeadersTask = new DicomHeadersTask(seriesUID, task.getDICOMFile(), new File(tagPath));
			tagExec.execute(dicomHeadersTask);
		} catch (Exception e) {
			logger.warning("Dicom tags file not created. taskType=" + taskType, e);
		}
	}
}
