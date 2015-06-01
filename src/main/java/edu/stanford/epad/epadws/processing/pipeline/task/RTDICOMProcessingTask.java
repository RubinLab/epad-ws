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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import DicomRT.ConvertDicoms;

import com.mathworks.toolbox.javabuilder.MWCharArray;
import com.mathworks.toolbox.javabuilder.MWException;
import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.SequenceAttribute;
import com.pixelmed.dicom.SequenceItem;
import com.pixelmed.dicom.TagFromName;

import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;

public class RTDICOMProcessingTask implements GeneratorTask
{
	private ConvertDicoms convertDicoms = null; // MATLAB-generated Java class containing function
	private static final EPADLogger log = EPADLogger.getInstance();

	private final String seriesUID;
	private final String imageUID;
	private final File dicomFile;
	private final String outFilePath;

	static public Set seriesBeingProcessed = Collections.synchronizedSet(new HashSet());
	
	public RTDICOMProcessingTask(String seriesUID, String imageUID, File dicomFile, String outFilePath)
	{
		this.seriesUID = seriesUID;
		this.imageUID = imageUID;
		this.dicomFile = dicomFile;
		this.outFilePath = outFilePath;
	}

	@Override
	public void run()
	{
		if (seriesBeingProcessed.contains(seriesUID))
		{
			log.info("RT series  " + seriesUID + " already being processed");
			return;
		}
		log.info("Processing DicomRT for series  " + seriesUID + "; file=" + dicomFile.getAbsolutePath());

		try {
			seriesBeingProcessed.add(seriesUID);
			String inputDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis()) + "/";
			File inputDir = new File(inputDirPath);
			inputDir.mkdirs();
			String filename = dicomFile.getName();
			if (filename.indexOf(".") == -1) filename = filename + ".dcm";
			File inputFile = new File(inputDir, filename);
			EPADFileUtils.copyFile(dicomFile, inputFile);
			if (convertDicoms == null) {
				try {
					convertDicoms = new ConvertDicoms();
				} catch (MWException t) {
					log.warning("Failed to initialize MATLAB RT Processor", t);
					return;
				}
			}
			String outputDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis()) + "/";
			File outputDir = new File(outputDirPath);
			outputDir.mkdirs();
			AttributeList dicomAttributes = PixelMedUtils.readDICOMAttributeList(dicomFile);
			String studyUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.StudyInstanceUID);
			// TODO: This call to get Referenced Image does not work ???
			String[] referencedImageUIDs = Attribute.getStringValues(dicomAttributes, TagFromName.ReferencedSOPInstanceUID);
			SequenceAttribute referencedFrameSequence =(SequenceAttribute)dicomAttributes.get(TagFromName.ReferencedFrameOfReferenceSequence);
			SequenceAttribute referencedSeriesSequence = null;
			if (referencedFrameSequence != null) {
			    Iterator sitems = referencedFrameSequence.iterator();
			    if (sitems.hasNext()) {
			        SequenceItem sitem = (SequenceItem)sitems.next();
			        if (sitem != null) {
			            AttributeList list = sitem.getAttributeList();
			            SequenceAttribute referencedStudySequence = (SequenceAttribute) list.get(TagFromName.RTReferencedStudySequence);
			            if (referencedStudySequence != null) {
						    sitems = referencedStudySequence.iterator();
					        sitem = (SequenceItem)sitems.next();
					        if (sitem != null) {
					            list = sitem.getAttributeList();
					            String instanceUID = (String) list.get(TagFromName.ReferencedSOPInstanceUID).getSingleStringValueOrEmptyString(); // What is this?
					            referencedSeriesSequence = (SequenceAttribute) list.get(TagFromName.RTReferencedSeriesSequence);
					        }
			            }
			        }
			    }
			}
			Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance().getDcm4CheeDatabaseOperations();
			if (referencedSeriesSequence != null) {
			    Iterator sitems = referencedSeriesSequence.iterator();
			    if (sitems.hasNext()) {
			        SequenceItem sitem = (SequenceItem)sitems.next();
			        if (sitem != null) {
			            AttributeList list = sitem.getAttributeList();
			            SequenceAttribute contourImageSequence = (SequenceAttribute) list.get(TagFromName.ContourImageSequence);
					    sitems = contourImageSequence.iterator();
					    while (sitems.hasNext()) {
					    	sitem = (SequenceItem)sitems.next();
				            list = sitem.getAttributeList();
			    			String referencedImageUID = list.get(TagFromName.ReferencedSOPInstanceUID).getSingleStringValueOrEmptyString();
			    			//String referencedSeriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedImageUIDs[i]);
				            log.info("Downloading ReferencedSOPInstanceUID:" + referencedImageUID);
				            DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, seriesUID, referencedImageUID, new File(inputDir, referencedImageUID + ".dcm"));
				        }
			       }
			    }
			}
			MWCharArray inFolderPath = new MWCharArray(inputDirPath);
			MWCharArray outFolderPath = new MWCharArray(outputDirPath);
			log.info("Invoking MATLAB-generated code..., inputPath:" + inputDirPath);
			convertDicoms.scanDir(inFolderPath, outFolderPath);
			log.info("Returned from MATLAB..., outFolderPath:" + outputDirPath);
			log.info("Creating entry in epad_files:" + outFilePath + " imageUID:" + imageUID);
			Map<String, String>  epadFilesRow = Dcm4CheeDatabaseUtils.createEPadFilesRowData(outFilePath, 0, imageUID);
			//log.info("PNG of size " + getFileSize(epadFilesRow) + " generated for instance " + instanceNumber + " in series "
			//		+ seriesUID + ", study " + studyUID + " for patient " + patientName);
			log.info("Created entry in epad_files:" + epadFilesRow);

			EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			epadDatabaseOperations.updateEpadFileRow(epadFilesRow.get("file_path"), PNGFileProcessingStatus.DONE, 0, "");
		} catch (Exception e) {
			log.warning("Error processing DICOM RT file for series " + seriesUID, e);
		} finally {
			log.info("DICOM RT for series " + seriesUID + " completed");
			seriesBeingProcessed.remove(seriesUID);
			if (convertDicoms != null) {
				convertDicoms.dispose();
				convertDicoms = null;
			}
		}
	}

	@Override
	public File getDICOMFile()
	{
		return dicomFile;
	}

	@Override
	public String getTagFilePath()
	{
		return outFilePath;
	}

	@Override
	public String getTaskType()
	{
		return "RTDICOMProcessing";
	}

	@Override
	public String getSeriesUID()
	{
		return this.seriesUID;
	}
}
