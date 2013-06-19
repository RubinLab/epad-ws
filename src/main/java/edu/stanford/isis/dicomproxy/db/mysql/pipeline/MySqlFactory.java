package edu.stanford.isis.dicomproxy.db.mysql.pipeline;

import edu.stanford.isis.dicomproxy.db.mysql.login.MySqlLoginDirWatcher;
import edu.stanford.isis.dicomproxy.db.mysql.model.SeriesOrder;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 *
 * @author amsnyder
 */
public class MySqlFactory {

    private static final ProxyLogger log = ProxyLogger.getInstance();

    private static final BlockingQueue<SeriesOrder> seriesWatchQueue = new ArrayBlockingQueue<SeriesOrder>(2000);
    //private static final BlockingQueue<DicomHeadersTask> dicomHeadersTaskQueue = new ArrayBlockingQueue<DicomHeadersTask>(2000);
    private static final BlockingQueue<GeneratorTask> pngTaskQueue = new ArrayBlockingQueue<GeneratorTask>(2000);

    final ExecutorService dbTableWatcherExec = Executors.newSingleThreadExecutor();
    final ExecutorService seriesWatcherExec = Executors.newSingleThreadExecutor();
    final ExecutorService pngProcessExec = Executors.newSingleThreadExecutor();

    //watches upload directory.
    final ExecutorService uploadDirWatcherExec = Executors.newSingleThreadExecutor();

    //login watcher.
    final ExecutorService loginDirWatcherExec = Executors.newSingleThreadExecutor();

    private static MySqlFactory ourInstance = new MySqlFactory();

    final DcmDbTableWatcher dbTableWatcher;
    final SeriesWatcher seriesWatcher;
    final PngGeneratorProcess pngGeneratorProcess;

    final MySqlUploadDirWatcher uploadDirWatcher;
    final MySqlLoginDirWatcher loginDirWatcher;

    public static MySqlFactory getInstance() {
        return ourInstance;
    }

    private MySqlFactory() {
        dbTableWatcher = new DcmDbTableWatcher(seriesWatchQueue);
        seriesWatcher = new SeriesWatcher(seriesWatchQueue,pngTaskQueue);
        pngGeneratorProcess = new PngGeneratorProcess(pngTaskQueue);
        uploadDirWatcher = new MySqlUploadDirWatcher();
        loginDirWatcher= new MySqlLoginDirWatcher();
    }

    public void buildAndStart(){
        log.info("Starting PNG generator pipeline.");
        dbTableWatcherExec.execute(dbTableWatcher);
        seriesWatcherExec.execute(seriesWatcher);
        pngProcessExec.execute(pngGeneratorProcess);
        uploadDirWatcherExec.execute(uploadDirWatcher);
        
        log.info("Starting login checker.");
        loginDirWatcherExec.execute(loginDirWatcher);
    }

    public void shutdown(){
        log.info("Stopping PNG generator pipeline.");
        dbTableWatcherExec.shutdown();
        seriesWatcherExec.shutdown();
        pngProcessExec.shutdown();
        uploadDirWatcherExec.shutdown();
    }

}
