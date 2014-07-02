/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.processing.pipeline.watcher;

import java.io.File;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;

/**
 * Count the number of DICOM files in the ./resources/upload/temp* directory. Write the results to the
 * ./resources/dicom/pipeline.status file.
 */
public class PipelineStatusWatcher implements Runnable
{
	private static final EPADLogger log = EPADLogger.getInstance();

	public static void countDicomFileInUploadDir()
	{
		int nDcmFiles = EPADFileUtils.countFilesWithEnding(EPADConfig.getEPADWebServerUploadDir(), ".dcm");
		StringBuilder sb = new StringBuilder();
		sb.append("files remaining: ").append(nDcmFiles).append("\n");
		sb.append("last update:").append(System.currentTimeMillis());

		File pipelineStatusFile = new File(EPADConfig.getEPADWebServerPNGDir() + "pipeline.status");
		EPADFileUtils.overwrite(pipelineStatusFile, sb.toString());
	}

	@Override
	public void run()
	{
		try {
			ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
			while (true) {
				if (shutdownSignal.hasShutdown()) {
					return;
				}
				countDicomFileInUploadDir();
				if (shutdownSignal.hasShutdown()) {
					return;
				}
				long checkInterval = getCheckInterval();
				TimeUnit.MILLISECONDS.sleep(checkInterval);
			}
		} catch (Exception e) {
			log.severe("Exiting PipelineStatusWatcher thread!!", e);
		} finally {
			log.info("Done PipelineStatusWatcher runnable.");
		}
	}

	/**
	 * Calculate the check interval. When a temp file is in the upload section it should be shorter.
	 * 
	 * @return long time to sleep in milliseconds. Values between one and 5 seconds should be considered.
	 */
	private static long getCheckInterval()
	{
		return 1000;
	}
}
