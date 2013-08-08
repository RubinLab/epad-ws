package edu.stanford.isis.epadws.processing.pipeline;

import java.io.File;

/**
 * Task generator for both Dicom images and Dicom Segmentation Objects and PNG grid generation..
 * 
 * @author alansnyder
 */
public interface GeneratorTask extends Runnable
{
	File getInputFile();

	String getTagFilePath();

	String getTaskType();
}
