package edu.stanford.isis.epadws.db.mysql.pipeline;

import java.io.File;

/**
 * Task generator for both Dicom images and Dicom Segmentation Objects.
 * @author alansnyder
 */
public interface GeneratorTask extends Runnable {

    File getDicomInputFile();

    String getTagFilePath();

    String getTaskType();
}
