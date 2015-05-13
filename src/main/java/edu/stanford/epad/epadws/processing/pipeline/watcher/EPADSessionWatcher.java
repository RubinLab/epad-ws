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

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.processing.pipeline.task.EpadStatisticsTask;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;

/**
 * Times out EPAD sessions
 * 
 * @author dev
 *
 */
public class EPADSessionWatcher implements Runnable
{
	private static final EPADLogger log = EPADLogger.getInstance();
	
	public static final int CHECK_INTERVAL = 1000*60; // Check every minute.

	private final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
	

	private String jsessionID = null;

	public EPADSessionWatcher()
	{
		log.info("Starting the EPAD Session Timer");
	}

	@Override
	public void run()
	{
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Calendar prevTime = null;
		int count = 0;
		while (!shutdownSignal.hasShutdown()) {
			try {
				long freeHeap = Runtime.getRuntime().freeMemory();
				long totalHeap = Runtime.getRuntime().totalMemory();
				if (freeHeap < totalHeap/4)
					log.info("Total Heap = " + totalHeap + ", Free Heap = " + freeHeap);

				count++;
				// Timeout expired sessions
				EPADSessionOperations.checkSessionTimeout();
				
				// Clear cache once a day
				Calendar now = Calendar.getInstance();
				if (now.get(Calendar.HOUR_OF_DAY) == 0 && prevTime != null && prevTime.get(Calendar.HOUR_OF_DAY) != 0)
				{
					projectOperations.clearCache();
					epadDatabaseOperations.deleteOldEvents();
					try {
						if (!"true".equalsIgnoreCase(EPADConfig.getParamValue("DISABLE_STATISTICS")))
						{	
							EpadStatisticsTask est = new EpadStatisticsTask();
							new Thread(est).start();
						}
					} catch (Exception x) {
						log.warning("Exception running statistics", x);
					}
				}
				prevTime = now;
				TimeUnit.MILLISECONDS.sleep(CHECK_INTERVAL);
			} catch (Exception e) {
				log.severe("Exception in EPAD Session Timer thread", e);
			}
		}
	}
}