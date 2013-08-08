package edu.stanford.isis.epadws.processing.pipeline;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.server.ShutdownSignal;

/**
 * Create one or multiple of these processes to listen for PngGeneratorTask on the queue and run them when ready.
 * 
 * NOTE: This has been extended to include Dicom-Segmentation-Object tasks too.
 * 
 * @author alansnyder
 */
public class PngGeneratorProcess implements Runnable
{
	private final BlockingQueue<GeneratorTask> pngTaskQueue;
	private final ExecutorService pngExecs;
	private final ExecutorService tagExec;
	private final ProxyLogger logger = ProxyLogger.getInstance();
	private final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();

	public PngGeneratorProcess(BlockingQueue<GeneratorTask> taskQueue)
	{
		this.pngTaskQueue = taskQueue;
		pngExecs = Executors.newFixedThreadPool(20);
		tagExec = Executors.newFixedThreadPool(20);
	}

	@Override
	public void run()
	{
		while (!shutdownSignal.hasShutdown()) {
			try {
				GeneratorTask task = pngTaskQueue.poll(500, TimeUnit.MILLISECONDS);
				if (task == null)
					continue;
				logger.info("Executing: " + task.toString());
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
			logger.info("Creating new DicomHeadersTask");
			DicomHeadersTask dicomHeadersTask = new DicomHeadersTask(task.getInputFile(), new File(tagPath));
			tagExec.execute(dicomHeadersTask);
		} catch (Exception e) {
			logger.warning("Dicom tags file not created. taskType=" + taskType, e);
		}
	}
}
