package edu.stanford.isis.epadws.processing.pipeline.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.isis.epadws.persistence.FileOperations;

/**
 * Given a set of PNG files, generate a single PNG containing a grid of these files.
 * 
 * @author martin
 */
public class PNGGridGeneratorTask implements GeneratorTask
{
	private final File pngInputFile;
	private final List<File> inputPNGGridFiles;
	private final File outputPNGFile;

	public PNGGridGeneratorTask(File pngInputFile, List<File> inputPNGGridFiles, File outputPNGFile)
	{
		this.pngInputFile = pngInputFile;
		this.inputPNGGridFiles = new ArrayList<File>(inputPNGGridFiles);
		this.outputPNGFile = outputPNGFile;
	}

	@Override
	public void run()
	{
		FileOperations.writePNGGridFile(pngInputFile, inputPNGGridFiles, outputPNGFile);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("PNGGridGeneratorTask[").append(" in=").append(pngInputFile).append(" grid files=");
		for (File file : inputPNGGridFiles)
			sb.append(" " + file.getName());
		sb.append(" out=").append(outputPNGFile).append("]");

		return sb.toString();
	}

	@Override
	public File getInputFile()
	{
		return pngInputFile;
	}

	@Override
	public String getTagFilePath()
	{
		return outputPNGFile.getAbsolutePath().replaceAll("\\.png", ".tag");
	}

	@Override
	public String getTaskType()
	{
		return "PNGGrid";
	}
}
