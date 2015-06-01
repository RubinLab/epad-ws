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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.dicom.DicomReader;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * Creates a packed PNG file based. DicomReader code is from Bradley Ross.
 * 
 * @see edu.stanford.epad.common.dicom.DicomReader#main
 */
public class PackedPngTask implements Callable<File>
{
	static private final EPADLogger logger = EPADLogger.getInstance();

	private final File file;

	protected PackedPngTask(File f)
	{
		file = f;
	}

	@Override
	public File call() throws Exception
	{
		File inputFile = file;
		File outputFile = null;
		OutputStream outputStream = null;

		try {
			DicomReader instance = new DicomReader(inputFile);
			String pngFilePath = file.getAbsolutePath().replaceAll("\\.dcm", ".png");
			logger.info("Creating PNG file: " + pngFilePath);
			outputFile = new File(pngFilePath);
			outputStream = new FileOutputStream(outputFile);
			ImageIO.write(instance.getPackedImage(), "png", outputStream);
		} catch (FileNotFoundException e) {
			logger.warning("failed to create packed PNG for: " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			logger.warning("failed to create packed PNG for: " + file.getAbsolutePath(), e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
		return outputFile;
	}
}
