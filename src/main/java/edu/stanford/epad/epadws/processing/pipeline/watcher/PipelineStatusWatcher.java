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
