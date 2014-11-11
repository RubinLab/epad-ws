package edu.stanford.epad.epadws.processing.pipeline.task;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.admin.ImageCheckHandler;

/**
 * Check and fix all pngs.
 * 
 * @author dev
 * 
 */
public class ImageCheckTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	@Override
	public void run()
	{
		try {
			String results = ImageCheckHandler.verifyImageGeneration(true);
			log.info("\n" + results);
		} catch (Exception e) {
			log.warning("Error is image check and fix task", e);
		}
	}
}
