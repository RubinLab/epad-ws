/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
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

import dicomrt.DicomRTSegExtractor;

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
import edu.stanford.epad.dtos.internal.DICOMElement;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.UserProjectService;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation;

public class RTDICOMProcessingTask implements GeneratorTask
{
	private DicomRTSegExtractor segExtractor = null; // MATLAB-generated Java class containing function

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
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // Let interactive thread run sooner
		if (seriesBeingProcessed.contains(seriesUID))
		{
			log.info("RT series  " + seriesUID + " already being processed");
			return;
		}
		log.info("Processing DicomRT for series  " + seriesUID + "; file=" + dicomFile.getAbsolutePath());

		String username = null;
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
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
			log.info("The retrieved username is "+username);
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "RT Dicom Processing Started", new Date(), null);
			DICOMElementList dicomElementList = Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, seriesUID, imageUID);
			String inputDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis()) + "/";
			File inputDir = new File(inputDirPath);
			inputDir.mkdirs();
			String seriesDirPath= inputDirPath+"series/";
			File seriesDir =  new File(seriesDirPath);
			seriesDir.mkdirs();
			String filename = dicomFile.getName();
			if (filename.indexOf(".") == -1) filename = filename + ".dcm";
			File rtFile = new File(inputDir, filename);
			EPADFileUtils.copyFile(dicomFile, rtFile);
			if (segExtractor == null) {
				try {
					segExtractor = new DicomRTSegExtractor();
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
				            File dicomFile = new File(seriesDir, referencedImageUID + ".dcm");
							projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Downloading referenced image: " + j++, null, null);
				            DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, seriesUID, referencedImageUID, dicomFile);
				            dicomFilePaths.add(dicomFile.getAbsolutePath());
				        }
			       }
			    }
			}
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Download Completed", null, null);
			MWCharArray seriesFolderPath = new MWCharArray(seriesDirPath);
			MWCharArray rtPath = new MWCharArray(rtFile.getAbsolutePath());
			MWCharArray outFolderPath = new MWCharArray(outputDirPath);
			log.info("Invoking MATLAB-generated code..., inputPath:" + inputDirPath);
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_MATLAB, seriesUID, "MATLAB Processing Started", new Date(), null);
			long starttime = System.currentTimeMillis();
			segExtractor.extractDSOsFromRT(1,seriesFolderPath,rtPath,outFolderPath);
			long endtime = System.currentTimeMillis();
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_MATLAB, seriesUID, "MATLAB Processing Completed", null, new Date());
			log.info("Returned from MATLAB..., outFolderPath:" + outputDirPath + " took " + (endtime-starttime)/1000 + " secs");
			List<DICOMElement> delems = getDICOMElementsByCode(dicomElementList, PixelMedUtils.ROIDisplayColor);
			if (delems == null) delems = new ArrayList<DICOMElement>();
			if (delems.size() > 0)
				log.debug("Number of color tags:" + delems.size() + " first:" + delems.get(0).value);
			try {
				MatFileReader reader = new MatFileReader(outFolderPath + "/" + patientID + ".mat");
				int numOfRoi = reader.getMLArray("contours").getSize();
				log.info("Number of ROIs:" + numOfRoi);
				MLStructure contours = (MLStructure) reader.getMLArray("contours");
				log.info("Field:" + contours.getFieldNames() + " ROIs:" + contours.getField("ROIName"));
				for (int r = 0; r < numOfRoi; r++)
				{
					MLChar roiName = null;
					try {
					MLUInt8 seg = (MLUInt8) contours.getField("Segmentation", r); // [512x512x98  uint8 array]
					roiName = (MLChar) contours.getField("ROIName", r);
					String roi = roiName.getString(0);
					log.info("ROI:" + roi);
					if (seg == null) continue;
					//	throw new Exception("No Segmentation found in MATLAN output file");
					int[] dims = seg.getDimensions();
					byte[][] segdata = seg.getArray();
					try {
					MLDouble points = (MLDouble) contours.getField("Points"); // [19956x3  double array]
					MLDouble vps = (MLDouble) contours.getField("VoxPoints"); // [19956x3  double array]
						log.info("Types, Segmentation:" + seg + " segdata:" + segdata.length + "x" + segdata[0].length + " Points:" + points + " VoxPoints:" + vps);
					} catch (Exception x) {}
					File matfile = new File(outFolderPath + "/" + patientID + ".mat");
					EPADFileUtils.copyFile(matfile, new File(outFilePath));
					log.info("file copied to "+outFilePath);
//					matfile.renameTo(new File(outFilePath)); 
					for (int i = 0; i < dims.length; i++)
						log.info("seg dimensions " + i + ":" + dims[i]);
					if (seg != null) {
						byte[] pixels = null;
						TIFFMasksToDSOConverter converter = new TIFFMasksToDSOConverter();
						List<String> segDicomFilePaths = new ArrayList<String>();
						//copy to the same index
						for (int i=dicomFilePaths.size()-1;i>=0;i--)
							segDicomFilePaths.add(dicomFilePaths.size()-i-1,dicomFilePaths.get(i));
						List<Integer> emptyFileIndex = new ArrayList<Integer>();
						boolean removeEmpty = true;
						int minInstanceNo = converter.getAttributesFromDICOMFiles(segDicomFilePaths);
						if (minInstanceNo > 1) removeEmpty = false;
						log.info("min istance no is: "+minInstanceNo + " remove empty:"+removeEmpty);
						// Convert 1 byte/pixel to 1 bit/pixel
						int numbytes = dims[0]*dims[1]/8;
//						byte[] pixel_data = new byte[numbytes*dims[2]];
						int totframes = dims[2];
						//ml the bound was dims[2]-1 with note //Skip last frame because matlab is off by 1 slice
						//but it was throwing error when I added the optimization 
						//and it works fine with the exact dimension. I guess it was smt about the offset of the pixeldata to hold all 
						//slices at once
						//get it reverse
						for (int frame = dims[2]-1; frame >=0; frame--) { 
//						for (int frame = 0; frame < dims[2]; frame++) { 
							boolean nonzerodata = false;
							byte[] pixel_data = new byte[numbytes];

							//int offset = frame*numbytes;
	//						int offset = (frame+1)*numbytes; // one slice off
							int offset = (totframes-frame-1)*numbytes; // one slice off
							log.info("frame:" + frame + " offset:" + offset);
							for (int k = 0; k < numbytes; k++)
							{
								int index = k*8;
								int x = index/dims[1];
								int y = index%dims[1];
								pixel_data[k] = 0;
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
										int setBit =  pixel_data[ k] + (1 << l);
										pixel_data[ k] =(byte) setBit;
										nonzerodata = true;
									}
								}
	//								if (pixel_data[k] != 0)
	//									log.info("maskfile" + i + ": " + k + " pixel:" + pixel_data[k]);
							}
							if (!nonzerodata && removeEmpty) {
								log.info("Frame " + frame + " empty. mark for removal " );
								emptyFileIndex.add(frame);
								continue;
							}
							
							if (pixels == null) {
								//pixels = new_frame.clone();
								pixels = pixel_data;
								log.info("adding first frame "+frame);
							} else {
								log.info("adding frame "+frame);
								byte[] temp = new byte[pixels.length + pixel_data.length];
								System.arraycopy(pixels, 0, temp, 0, pixels.length);
								System.arraycopy(pixel_data, 0, temp, pixels.length, pixel_data.length);
								//pixels = temp.clone();
								pixels = temp;
							}
						}
						if (removeEmpty) {
							log.info("empty size "+emptyFileIndex.size());
							for (int i = 0; i<emptyFileIndex.size() ; i++)
							{
								int index = emptyFileIndex.get(i);
								
								//it was dicomattributes.length
								if (index<segDicomFilePaths.size()) {
									log.info("Removing dicom " + index);
									segDicomFilePaths.remove( index );
									//log.info("before "+converter.getDicomAttributes()[index]+ " index "+index);
									converter.getDicomAttributes()[converter.getDicomAttributes().length - index -1]=null;
									log.info("after "+converter.getDicomAttributes()[converter.getDicomAttributes().length - index -1]+ " index "+(converter.getDicomAttributes().length - index -1));
								}else {
									log.warning("size check fail in index "+index + " calc index to remove " + (converter.getDicomAttributes().length - index -1) +" dicom files size "+ segDicomFilePaths.size());
								}
								
							}
							
							
						}
						if (segDicomFilePaths.size() != converter.getDicomAttributes().length)
						{
							AttributeList[] dicomAttributesNew = new AttributeList[converter.getDicomAttributes().length];
							int i = 0;
							for (AttributeList attrs: converter.getDicomAttributes())
							{
								if (attrs == null) continue;
								dicomAttributesNew[i++] = attrs;
							}
							converter.setDicomAttributes(dicomAttributesNew);
							log.info("setting number of frames to "+segDicomFilePaths.size());
							converter.setNumberOfFrames(segDicomFilePaths.size());
						}
						File dsoFile = new File(outFolderPath + "/" + seriesUID + ".dso");
						if (roi == null) roi = "";
						String dsoDescr = roi.replace('\'',' ').trim();
						if (dsoDescr.length() < 4) dsoDescr = description + "_" + dsoDescr;
						projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Generating DSO", null, null);
						log.info("Generating new DSO for RTSTRUCT series " + seriesUID);
						
						
						String[] seriesImageUids = converter.generateDSO(pixels, segDicomFilePaths, dsoFile.getAbsolutePath(), dsoDescr, null, null);
						String dsoSeriesUID = seriesImageUids[0];
						String dsoImageUID = seriesImageUids[1];
						log.info("Sending generated DSO " + dsoFile.getAbsolutePath() + " imageUID:" + dsoImageUID + " to dcm4chee...");
						DCM4CHEEUtil.dcmsnd(dsoFile.getAbsolutePath(), false);
						List<Project> projects = projectOperations.getProjectsForSubject(patientID);
						log.info("Patient "+patientID+ " has "+ projects.size() + " projects");
						String color = "";
						if (r < delems.size())
						{
							color = delems.get(r).value;
							String[] colors = color.replace('\\',',').split(",");
							if (colors.length == 3)	
								color = formatColor(getInt(colors[0]), getInt(colors[1]), getInt(colors[2]));
						}
						for (Project project: projects) {
							log.info("RT Dicom projectID:" + project.getProjectId());
							if (project.getProjectId().equals(EPADConfig.xnatUploadProjectID)) continue;
							if (projectOperations.isStudyInProjectAndSubject(project.getProjectId(), patientID, studyUID))
							{
								String projectID = project.getProjectId();
								//get the project's creator if the username does not exist in upload or is admin
								if (username==null || "admin".equals(username)) {
									username = project.getCreator();
								}
								log.info("creating annotation for dicom rt using username: "+username +  " and project "+ projectID);
								ImageAnnotation ia = AIMUtil.generateAIMFileForDSO(dsoFile, username, projectID, dsoDescr);
								epadDatabaseOperations.updateAIMColor(ia.getUniqueIdentifier(), color);
							}
							else
								log.info("RT Dicom study not is project:" + studyUID);
								
						}
						projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Completed DSO Generation " + r, null, null);
					}
					} catch (Exception x) {
						if (roiName != null)
							log.warning("Error processing RT DICOM, ROI:"+ roiName.getString(0), x);
						else
							log.warning("Error processing RT DICOM", x);
					}
				}
			} catch (Exception x) {
				log.warning("Error reading results", x);
			}
			log.info("Creating entry in epad_files:" + outFilePath + " imageUID:" + imageUID);
			Map<String, String>  epadFilesRow = Dcm4CheeDatabaseUtils.createEPadFilesRowData(outFilePath, 0, imageUID);			
			epadDatabaseOperations.updateEpadFileRow(epadFilesRow.get("file_path"), PNGFileProcessingStatus.DONE, 0, "");
			
//			EPADFileUtils.deleteDirectoryAndContents(inputDir);
//			EPADFileUtils.deleteDirectoryAndContents(outputDir);
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Completed Processing", null, new Date());
		} catch (Exception e) {
			log.warning("Error processing DICOM RT file for series " + seriesUID, e);
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_RT_PROCESS, seriesUID, "Failed Processing: " + e.getMessage(), null, new Date());
		} finally {
			log.info("DICOM RT for series " + seriesUID + " completed");
			seriesBeingProcessed.remove(seriesUID);
			if (segExtractor != null) {
				segExtractor.dispose();
				segExtractor = null;
			}
		}
	}

	public static List<DICOMElement> getDICOMElementsByCode(DICOMElementList dicomElementList, String tagCode)
	{
		List<DICOMElement> matchingDICOMElements = new ArrayList<DICOMElement>();

		for (DICOMElement dicomElement : dicomElementList.ResultSet.Result) {
			if (dicomElement.tagCode.equalsIgnoreCase(tagCode))
			matchingDICOMElements.add(dicomElement);
		}

		return matchingDICOMElements;
	}

	public static String formatColor(int rint, int gint, int bint) {
        String r = (rint < 16) ? "0" + Integer.toHexString(rint) : Integer.toHexString(rint);
        String g = (gint < 16) ? "0" + Integer.toHexString(gint) : Integer.toHexString(gint);
        String b = (bint < 16) ? "0" + Integer.toHexString(bint) : Integer.toHexString(bint);
        return "#" + r + g + b;
    }
	
	public static int getInt(String value)
	{
		try {
			return new Integer(value.trim()).intValue();
		} catch (Exception x) {
			return 0;
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
		return outFilePath.replaceAll("\\.mat", ".tag");
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
