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
package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.epad.epadws.epaddb.PNGFilesOperations;

/**
 * Given a set of PNG files, generate a single PNG containing a grid of these files.
 * 
 * @author martin
 */
public class PNGGridGeneratorTask implements GeneratorTask
{
	private final String seriesUID;
	private final String imageUID;
	private final File pngInputFile;
	private final List<File> inputPNGGridFiles;
	private final File outputPNGFile;

	public PNGGridGeneratorTask(String seriesUID, String imageUID, File pngInputFile, List<File> inputPNGGridFiles,
			File outputPNGFile)
	{
		this.seriesUID = seriesUID;
		this.imageUID = imageUID;
		this.pngInputFile = pngInputFile;
		this.inputPNGGridFiles = new ArrayList<File>(inputPNGGridFiles);
		this.outputPNGFile = outputPNGFile;
	}

	@Override
	public void run()
	{
		PNGFilesOperations.writePNGGridFile(seriesUID, imageUID, pngInputFile, inputPNGGridFiles, outputPNGFile);
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
	public String getSeriesUID()
	{
		return this.seriesUID;
	}

	@Override
	public File getDICOMFile()
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
