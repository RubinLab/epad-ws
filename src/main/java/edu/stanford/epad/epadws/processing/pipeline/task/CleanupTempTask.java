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
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.util.EPADLogger;

/**
 * Clean up temp directory
 * 
 * @author dev
 * 
 */
public class CleanupTempTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	@Override
	public void run()
	{
		File tmp = new File(System.getProperty("java.io.tmpdir"));
		File[] files = tmp.listFiles();
		for (File file: files)
		{
			if (file.getName().contains(".png") ||file.getName().contains(".tif") ||file.getName().contains(".tmp") || file.getName().endsWith(".dcm") || file.getName().endsWith(".dso"))
			{
		        try
		        {
		        	BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			        long updateTime = attributes.lastAccessTime().toMillis();
			        if (updateTime < (System.currentTimeMillis() + 1000*60*60*4))
			        	deleteQuietly(file);			        		
		        }
		        catch (Exception exception)
		        {
		        }				
			}
		}
	}
	
	private static void deleteQuietly(File file)
	{
		try {
			//log.info("Deleting temp file:" + file.getAbsolutePath());
			file.delete();
		}
		catch (Exception x) {}
	}
}
