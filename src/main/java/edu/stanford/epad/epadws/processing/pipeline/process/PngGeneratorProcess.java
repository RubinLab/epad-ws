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
			DicomHeadersTask dicomHeadersTask = new DicomHeadersTask(task.getInputFile(), new File(tagPath));
			tagExec.execute(dicomHeadersTask);
		} catch (Exception e) {
			logger.warning("Dicom tags file not created. taskType=" + taskType, e);
		}
	}
}
