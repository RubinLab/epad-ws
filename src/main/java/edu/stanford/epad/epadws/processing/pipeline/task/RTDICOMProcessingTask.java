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
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import DicomRT.ConvertDicoms;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;
import com.jmatio.types.MLUInt8;
import com.mathworks.toolbox.javabuilder.MWCharArray;
import com.mathworks.toolbox.javabuilder.MWException;
import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.SequenceAttribute;
import com.pixelmed.dicom.SequenceItem;
import com.pixelmed.dicom.TagFromName;

import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.pixelmed.TIFFMasksToDSOConverter;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.UserProjectService;

public class RTDICOMProcessingTask implements GeneratorTask
{
	private ConvertDicoms convertDicoms = null; // MATLAB-generated Java class containing function
	private static final EPADLogger log = EPADLogger.getInstance();

	private final String studyUID;
	private final String seriesUID;
	private final String imageUID;
	private final File dicomFile;
	private final String outFilePath;

	static public Set seriesBeingProcessed = Collections.synchronizedSet(new HashSet());
	
	public RTDICOMProcessingTask(String studyUID, String seriesUID, String imageUID, File dicomFile, String outFilePath)
	{
		this.studyUID = studyUID;
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

		String username = null;
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		try {
			seriesBeingProcessed.add(seriesUID);
			if (UserProjectService.pendingUploads.containsKey(studyUID))
			{
				username = UserProjectService.pendingUploads.get(studyUID);
				if (username != null && username.indexOf(":") != -1)
					username = username.substring(0, username.indexOf(":"));
			}
			if (username == null  && UserProjectService.pendingPNGs.containsKey(seriesUID))
			{
				username = UserProjectService.pendingPNGs.get(seriesUID);
				if (username != null && username.indexOf(":") != -1)
					username = username.substring(0, username.indexOf(":"));
			}
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "RT Dicom Processing Started", new Date(), null);
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
			String patientID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.PatientID);
			String description = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.SeriesDescription);
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
			int j = 1;
			List<String> dicomFilePaths = new ArrayList<String>();
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
				            //log.info("Downloading ReferencedSOPInstanceUID:" + referencedImageUID);
				            File dicomFile = new File(inputDir, referencedImageUID + ".dcm");
							projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Downloading referenced image: " + j++, null, null);
				            DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, seriesUID, referencedImageUID, dicomFile);
				            dicomFilePaths.add(dicomFile.getAbsolutePath());
				        }
			       }
			    }
			}
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Download Completed", null, null);
			MWCharArray inFolderPath = new MWCharArray(inputDirPath);
			MWCharArray outFolderPath = new MWCharArray(outputDirPath);
			log.info("Invoking MATLAB-generated code..., inputPath:" + inputDirPath);
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_MATLAB, seriesUID, "MATLAB Processing Started", new Date(), null);
			long starttime = System.currentTimeMillis();
			convertDicoms.scanDir(inFolderPath, outFolderPath);
			long endtime = System.currentTimeMillis();
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_MATLAB, seriesUID, "MATLAB Processing Completed", null, new Date());
			log.info("Returned from MATLAB..., outFolderPath:" + outputDirPath + " took " + (endtime-starttime)/1000 + " secs");
			try {
				MatFileReader reader = new MatFileReader(outFolderPath + "/" + patientID + ".mat");
				int numOfRoi = reader.getMLArray("contours").getSize();
				log.info("Number of ROIs:" + numOfRoi);
				MLStructure contours = (MLStructure) reader.getMLArray("contours");
				log.info("Field:" + contours.getFieldNames() + " ROIs:" + contours.getField("ROIName"));
				for (int r = 0; r < numOfRoi; r++)
				{
					MLUInt8 seg = (MLUInt8) contours.getField("Segmentation", r); // [512x512x98  uint8 array]
					MLChar roiName = (MLChar) contours.getField("ROIName", r);
					String roi = roiName.getString(0);
					log.info("ROI:" + roi);
					if (seg == null) continue;
					//	throw new Exception("No Segmentation found in MATLAN output file");
					int[] dims = seg.getDimensions();
					byte[][] segdata = seg.getArray();
					MLDouble points = (MLDouble) contours.getField("Points"); // [19956x3  double array]
					MLDouble vps = (MLDouble) contours.getField("VoxPoints"); // [19956x3  double array]
					File matfile = new File(outFolderPath + "/" + patientID + ".mat");
					matfile.renameTo(new File(outFilePath));
					log.info("Types, Segmentation:" + seg + " segdata:" + segdata.length + "x" + segdata[0].length + " Points:" + points + " VoxPoints:" + vps);
					for (int i = 0; i < dims.length; i++)
						log.info("seg dimensions " + i + ":" + dims[i]);
					if (seg != null) {
						// Convert 1 byte/pixel to 1 bit/pixel
						int numbytes = dims[0]*dims[1]/8;
						byte[] pixel_data = new byte[numbytes*dims[2]];
						int totframes = dims[2];
						for (int frame = 0; frame < dims[2]-1; frame++) { // Skip last frame because matlab is off by 1 slice
							//int offset = frame*numbytes;
	//						int offset = (frame+1)*numbytes; // one slice off
							int offset = (totframes-frame-1)*numbytes; // one slice off
							log.info("frame:" + frame + " offset:" + offset);
							for (int k = 0; k < numbytes; k++)
							{
								int index = k*8;
								int x = index/dims[1];
								int y = index%dims[1];
								pixel_data[offset + k] = 0;
								for (int l = 0; l < 8; l++)
								{
									int y1 = y + l;
									int x1 = x;
									if (y1 >= dims[0])
									{
										x1 = x1+1;
										y1 = y1 - dims[0];
									}
									//log.info("x1:" + x1 + " y1:" + y1);
									if (segdata[x1][y1 + frame*dims[1]] != 0)
									{
										int setBit =  pixel_data[offset + k] + (1 << l);
										pixel_data[offset + k] =(byte) setBit;
									}
								}
	//								if (pixel_data[k] != 0)
	//									log.info("maskfile" + i + ": " + k + " pixel:" + pixel_data[k]);
							}
						}
						File dsoFile = new File(outFolderPath + "/" + seriesUID + ".dso");
						String dsoDescr = description + roi.replace('\'',' ').trim();
						projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Generating DSO", null, null);
						log.info("Generating new DSO for RTSTRUCT series " + seriesUID);
						TIFFMasksToDSOConverter converter = new TIFFMasksToDSOConverter();
						String[] seriesImageUids = converter.generateDSO(pixel_data, dicomFilePaths, dsoFile.getAbsolutePath(), dsoDescr, null, null, false);
						String dsoSeriesUID = seriesImageUids[0];
						String dsoImageUID = seriesImageUids[1];
						log.info("Sending generated DSO " + dsoFile.getAbsolutePath() + " imageUID:" + dsoImageUID + " to dcm4chee...");
						DCM4CHEEUtil.dcmsnd(dsoFile.getAbsolutePath(), false);
						List<Project> projects = projectOperations.getProjectsForSubject(patientID);
						for (Project project: projects) {
							if (project.getProjectId().equals(EPADConfig.xnatUploadProjectID)) continue;
							if (projectOperations.isStudyInProjectAndSubject(project.getProjectId(), patientID, studyUID))
							{
								String projectID = project.getProjectId();
								Study study = projectOperations.getStudy(studyUID);
								String owner = study.getCreator();
								if (!projectOperations.hasAccessToProject(owner, project.getId()))
									owner = project.getCreator();
								AIMUtil.generateAIMFileForDSO(dsoFile, owner, projectID);
							}
						}
						projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Completed DSO Generation " + r, null, null);
					}
				}
			} catch (Exception x) {
				log.warning("Error reading results", x);
			}
			log.info("Creating entry in epad_files:" + outFilePath + " imageUID:" + imageUID);
			Map<String, String>  epadFilesRow = Dcm4CheeDatabaseUtils.createEPadFilesRowData(outFilePath, 0, imageUID);			
			EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			epadDatabaseOperations.updateEpadFileRow(epadFilesRow.get("file_path"), PNGFileProcessingStatus.DONE, 0, "");
			
			EPADFileUtils.deleteDirectoryAndContents(inputDir);
			EPADFileUtils.deleteDirectoryAndContents(outputDir);
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Completed Processing", null, new Date());
		} catch (Exception e) {
			log.warning("Error processing DICOM RT file for series " + seriesUID, e);
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Failed Processing: " + e.getMessage(), null, new Date());
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
