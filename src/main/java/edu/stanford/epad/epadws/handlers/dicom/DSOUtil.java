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
package edu.stanford.epad.epadws.handlers.dicom;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.Opener;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.DicomInputStream;
import com.pixelmed.dicom.SequenceAttribute;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.dicom.UnsignedShortAttribute;
import com.pixelmed.display.ConsumerFormatImageMaker;
import com.pixelmed.display.SourceImage;

import edu.stanford.epad.common.dicom.DCM4CHEEImageDescription;
import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.dicom.DicomFileUtil;
import edu.stanford.epad.common.dicom.DicomSegmentationObject;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.pixelmed.TIFFMasksToDSOConverter;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.RunSystemCommand;
import edu.stanford.epad.dtos.DSOEditRequest;
import edu.stanford.epad.dtos.DSOEditResult;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADDSOFrame;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.dtos.internal.DICOMElement;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.epadws.aim.AIMQueries;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation;

/**
 * Code for handling DICOM Segmentation Objects
 * 
 * 
 * @author martin
 */
public class DSOUtil
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String baseDicomDirectory = EPADConfig.getEPADWebServerPNGDir();
	
	private final static Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
			.getDcm4CheeDatabaseOperations();

	/**
	 * Take an existing DSO and generate a new one (with new UIDs) with substituted masked frames.
	 */
	private static DSOEditResult createEditedDSO(DSOEditRequest dsoEditRequest, List<File> editFramesPNGMaskFiles, String referencedSeriesUID)
	{
		try {
			List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(
					dsoEditRequest.studyUID, referencedSeriesUID);
			int width = 0;
			int height = 0;
			List<String> dicomFilePaths = new ArrayList<String>();
			for (DCM4CHEEImageDescription imageDescription : imageDescriptions) {
				try {
					File temporaryDICOMFile = File.createTempFile(imageDescription.imageUID, ".dcm");
					//log.info("Downloading source DICOM file for image " + imageDescription.imageUID);
					DCM4CHEEUtil.downloadDICOMFileFromWADO(dsoEditRequest.studyUID, dsoEditRequest.seriesUID, imageDescription.imageUID, temporaryDICOMFile);
					if (width == 0) {
						DicomInputStream dicomInputStream = null;
						try {
							dicomInputStream = new DicomInputStream(new FileInputStream(temporaryDICOMFile));
							AttributeList localDICOMAttributes = new AttributeList();
							localDICOMAttributes.read(dicomInputStream);
							width = (short)Attribute.getSingleIntegerValueOrDefault(localDICOMAttributes, TagFromName.Columns, 1);
							height = (short)Attribute.getSingleIntegerValueOrDefault(localDICOMAttributes, TagFromName.Rows, 1);
						} finally {
							IOUtils.closeQuietly(dicomInputStream);
						}
					}
					dicomFilePaths.add(temporaryDICOMFile.getAbsolutePath());
				} catch (IOException e) {
					log.warning("Error downloading DICOM file for referenced image " + imageDescription.imageUID + " for series "
							+ dsoEditRequest.seriesUID, e);
					throw new Exception("Error downloading DICOM file for referenced image " + imageDescription.imageUID + " for series "
							+ dsoEditRequest.seriesUID);
				}
			}
			ImageReference imageReference = new ImageReference(dsoEditRequest);
			log.info("DSO to be edited, UID:" + imageReference.seriesUID);
			DICOMElementList dicomElements = Dcm4CheeQueries.getDICOMElementsFromWADO(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID);
			int numberOfSegments = getNumberOfSegments(dicomElements);
			if (numberOfSegments > 1)
				throw new Exception("Editing of Multi-Segment DSOs not supported, number of segments:" + numberOfSegments);
			String seriesDescription = null;
			String seriesUID = null;
			String instanceUID = null;
			for (DICOMElement dicomElement : dicomElements.ResultSet.Result) {
				if (dicomElement.tagCode.equalsIgnoreCase(PixelMedUtils.SeriesDescriptionCode)) {
					log.info("DSO to be edited, tag:" + dicomElement.tagName + " value:" + dicomElement.value);
					seriesDescription = dicomElement.value;
				}
			}
			// Always 'clobber' the orginal DSO
//			if (seriesDescription != null && seriesDescription.toLowerCase().contains("epad"))
			{
				seriesUID = imageReference.seriesUID;
				instanceUID = imageReference.imageUID;
			}
//			else
//				seriesDescription = null;
			List<File> editFramesTIFFMaskFiles = generateTIFFsFromPNGs(editFramesPNGMaskFiles);
			
			List<File> dsoTIFFMaskFiles = new ArrayList<>();
			for (int i = 0; i < imageDescriptions.size(); i++)
			{
					String fileName = "EmptyMask_" + i + "_";
					if (i == 0)
					{
						dsoTIFFMaskFiles.add(copyEmptyTiffFile(editFramesTIFFMaskFiles.get(0), fileName, width, height));
					}
					else
					{
						File tifFile = File.createTempFile(fileName,".tif");
						EPADFileUtils.copyFile(dsoTIFFMaskFiles.get(0), tifFile);
						dsoTIFFMaskFiles.add(tifFile);
					}
			}
			List<File> existingDSOTIFFMaskFiles = DSOUtil.getDSOTIFFMaskFiles(imageReference, dsoTIFFMaskFiles);
			int frameMaskFilesIndex = 0;
			for (Integer frameNumber : dsoEditRequest.editedFrameNumbers) {
				if (frameNumber >= 0 && frameNumber < imageDescriptions.size()) {
					log.info("Editing frame: " + frameNumber + " in new DSO");
					// For some reason the original DSO Masks are in reverse order
					int editMaskFileIndex = existingDSOTIFFMaskFiles.size() - frameNumber -1;
					File prev = dsoTIFFMaskFiles.get(editMaskFileIndex);
					deleteQuietly(prev);
					dsoTIFFMaskFiles.set(editMaskFileIndex, editFramesTIFFMaskFiles.get(frameMaskFilesIndex++));
				} else {
					log.warning("Frame number " + frameNumber + " is out of range for DSO image " + dsoEditRequest.imageUID
							+ " in series " + dsoEditRequest.seriesUID + " which has only " + existingDSOTIFFMaskFiles.size() + " frames");
					return null;
				}
			}

			if (DSOUtil.createDSO(imageReference, dsoTIFFMaskFiles, dicomFilePaths, seriesDescription, seriesUID, instanceUID))
			{
				log.info("Finished generating DSO");
				for (File file: dsoTIFFMaskFiles)
				{
					deleteQuietly(file);
				}
				for (String dicom: dicomFilePaths)
				{
					deleteQuietly(new File(dicom));
				}
				return new DSOEditResult(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,			
						imageReference.seriesUID, imageReference.imageUID, dsoEditRequest.aimID);
			}
			else
				return null;
		} catch (Exception e) {
			log.warning("Error generating DSO image " + dsoEditRequest.imageUID + " in series " + dsoEditRequest.seriesUID, e);
			return null;
		}
	}

	private static int getNumberOfSegments(DICOMElementList dicomElements)
	{
		int segments = 0;
		for (DICOMElement dicomElement : dicomElements.ResultSet.Result) {
			if (dicomElement.tagName.equalsIgnoreCase("Segment Number")) {
				segments++;
			}
		}
		return segments;
	}

	/**
	 * Generate a new DSO from scratch given series and masked frames.
	 */
	private static DSOEditResult createNewDSO(String dsoName, DSOEditRequest dsoEditRequest, List<File> editFramesPNGMaskFiles, String projectID, String username)
	{
		try {
			List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(
					dsoEditRequest.studyUID, dsoEditRequest.seriesUID);
			List<String> dicomFilePaths = new ArrayList<String>();
			int width = 0;
			int height = 0;
			for (DCM4CHEEImageDescription imageDescription : imageDescriptions) {
				try {
					File temporaryDICOMFile = File.createTempFile(imageDescription.imageUID, ".dcm");
					//log.info("Downloading source DICOM file for image " + imageDescription.imageUID);
					DCM4CHEEUtil.downloadDICOMFileFromWADO(dsoEditRequest.studyUID, dsoEditRequest.seriesUID, imageDescription.imageUID, temporaryDICOMFile);
					if (width == 0) {
						DicomInputStream dicomInputStream = null;
						try {
							dicomInputStream = new DicomInputStream(new FileInputStream(temporaryDICOMFile));
							AttributeList localDICOMAttributes = new AttributeList();
							localDICOMAttributes.read(dicomInputStream);
							width = (short)Attribute.getSingleIntegerValueOrDefault(localDICOMAttributes, TagFromName.Columns, 1);
							height = (short)Attribute.getSingleIntegerValueOrDefault(localDICOMAttributes, TagFromName.Rows, 1);
						} finally {
							IOUtils.closeQuietly(dicomInputStream);
						}
					}
					dicomFilePaths.add(temporaryDICOMFile.getAbsolutePath());
				} catch (IOException e) {
					log.warning("Error downloading DICOM file for referenced image " + imageDescription.imageUID + " for series "
							+ dsoEditRequest.seriesUID, e);
					throw new Exception("Error downloading DICOM file for referenced image " + imageDescription.imageUID + " for series "
							+ dsoEditRequest.seriesUID);
				}
			}
			List<File> tiffMaskFiles = generateTIFFsFromPNGs(editFramesPNGMaskFiles);
			log.info("Generating DSO for series " + dsoEditRequest.seriesUID + " with " + tiffMaskFiles.size() + " TIFF mask file(s)...");
			if (dicomFilePaths.size() != tiffMaskFiles.size()) {
				log.warning("Source dicom frames: " + dicomFilePaths.size() + " mask files: " + tiffMaskFiles.size());
			}
				
			File temporaryDSOFile = File.createTempFile(dsoEditRequest.seriesUID, ".dso");
			log.info("Found " + dicomFilePaths.size() + " source DICOM file(s) for series " + dsoEditRequest.seriesUID);
			List<File> dsoTIFFMaskFiles = new ArrayList<>();
			for (int i = 0; i < dicomFilePaths.size(); i++)
			{
					String fileName = "EmptyMask_" + i + "_";
					if (i == 0)
					{
						dsoTIFFMaskFiles.add(copyEmptyTiffFile(tiffMaskFiles.get(0), fileName, width, height));
					}
					else
					{
						File tifFile = File.createTempFile(fileName,".tif");
						EPADFileUtils.copyFile(dsoTIFFMaskFiles.get(0), tifFile);
						dsoTIFFMaskFiles.add(tifFile);
					}
			}
			int frameMaskFilesIndex = 0;
			for (Integer frameNumber : dsoEditRequest.editedFrameNumbers) {
				if (frameNumber >= 0 && frameNumber < dicomFilePaths.size()) {
					log.info("Creating frame: " + frameNumber + " in new DSO");
					// For some reason the original DSO Masks are in reverse order
					int editMaskFileIndex = dicomFilePaths.size() - frameNumber -1;
					dsoTIFFMaskFiles.set(editMaskFileIndex, tiffMaskFiles.get(frameMaskFilesIndex++));
				} else {
					log.warning("Frame number " + frameNumber + " is out of range for DSO image " + dsoEditRequest.imageUID
							+ " in series " + dsoEditRequest.seriesUID + " which has only " + dicomFilePaths.size() + " frames");
					return null;
				}
			}
			// For some reason the frames need to be in reverse order
//			List<File> reverseMaskFiles = new ArrayList<File>();
//			for (int i = dsoTIFFMaskFiles.size(); i > 0 ; i--)
//			{
//				reverseMaskFiles.add(dsoTIFFMaskFiles.get(i-1));
//			}
			
			log.info("Generating new DSO for series " + dsoEditRequest.seriesUID);
			TIFFMasksToDSOConverter converter = new TIFFMasksToDSOConverter();
			boolean removeEmptyMasks = false;
			if ("true".equals(EPADConfig.getParamValue("OptimizedDSOs", "true")))
				removeEmptyMasks = true;
			String[] seriesImageUids = converter.generateDSO(files2FilePaths(dsoTIFFMaskFiles), dicomFilePaths, temporaryDSOFile.getAbsolutePath(), dsoName, null, null, removeEmptyMasks);
			String dsoSeriesUID = seriesImageUids[0];
			String dsoImageUID = seriesImageUids[1];
			log.info("Sending generated DSO " + temporaryDSOFile.getAbsolutePath() + " dsoImageUID:" + dsoImageUID + " dsoSeriesUID:" + dsoSeriesUID + " to dcm4chee...");
			DCM4CHEEUtil.dcmsnd(temporaryDSOFile.getAbsolutePath(), false);
			EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			EPADAIM ea = null;
			if (dsoEditRequest.aimID != null && dsoEditRequest.aimID.trim().length() > 0)
			{
				ea = epadDatabaseOperations.getAIM(dsoEditRequest.aimID);
				if (ea != null)
					epadDatabaseOperations.updateAIMDSOSeries(ea.aimID, dsoSeriesUID, username);
			}
			if (ea == null)
			{
				ImageAnnotation aim = AIMUtil.generateAIMFileForDSO(temporaryDSOFile, username, projectID);
				log.info("DSO AimID:" + aim.getUniqueIdentifier());
				ea = epadDatabaseOperations.getAIM(aim.getUniqueIdentifier());
				ea.dsoFrameNo = dsoEditRequest.editedFrameNumbers.get(0);
				epadDatabaseOperations.updateAIMDSOFrameNo(ea.aimID, ea.dsoFrameNo);
			}
			for (File mask: dsoTIFFMaskFiles)
			{
				mask.delete();
			}
			for (String dicom: dicomFilePaths)
			{
				deleteQuietly(new File(dicom));
			}
			for (int i = 0; i < dsoEditRequest.editedFrameNumbers.size(); i++)
			{
				Integer frameNumber = dsoEditRequest.editedFrameNumbers.get(i);
				String pngMaskDirectoryPath = baseDicomDirectory + "/studies/" + ea.studyUID + "/series/" + dsoSeriesUID + "/images/"
						+ dsoImageUID + "/masks/";
				File pngFilesDirectory = new File(pngMaskDirectoryPath);
				pngFilesDirectory.mkdirs();				
				String pngMaskFilePath = pngMaskDirectoryPath + frameNumber + ".png";
				EPADFileUtils.copyFile(editFramesPNGMaskFiles.get(i), new File(pngMaskFilePath));
				editFramesPNGMaskFiles.get(i).delete();
				log.info("File copied:" + pngMaskFilePath);
			}
			return new DSOEditResult(dsoEditRequest.projectID, dsoEditRequest.patientID, dsoEditRequest.studyUID, dsoSeriesUID, dsoImageUID, ea.aimID);

		} catch (Exception e) {
			log.warning("Error generating DSO image for series " + dsoEditRequest.seriesUID, e);
			if (e.getMessage() != null)
				throw new RuntimeException(e.getMessage());
			else
			return null;
		}
	}

	private static File copyEmptyTiffFile(File original, String newFileName, int width, int height)
	{
		File newFile = null;
		try {
			long len = original.length();
			long rgbLen = width*height*4;
			long greyLen = width*height;
			long bwLen = width*height/8;
			int imagetype = BufferedImage.TYPE_BYTE_BINARY;
			if (len > greyLen)
				imagetype = BufferedImage.TYPE_BYTE_GRAY;
			if (len > rgbLen)
				imagetype = BufferedImage.TYPE_4BYTE_ABGR;
			newFile = File.createTempFile(newFileName,".tif");
			//log.info("Creating empty tiff:" + newFile.getAbsolutePath() + " width:" + width + " height:" + height);
			BufferedImage bufferedImage = new BufferedImage(width, height, imagetype);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
						bufferedImage.setRGB(x, y, Color.black.getRGB());
				}
			}
			ImageIO.write(bufferedImage, "tif", newFile);
		} catch (IOException e) {
			log.warning("Error creating empty TIFF  file" + newFile.getAbsolutePath());
		}
		return newFile;
	}
	
	private static void deleteQuietly(File file)
	{
		try {
			//log.info("Deleting temp file:" + file.getAbsolutePath());
			file.delete();
		}
		catch (Exception x) {}
	}

	public static boolean createDSO(ImageReference imageReference, List<File> tiffMaskFiles, List<String> dicomFilePaths, String dsoSeriesDescription, String dsoSeriesUID, String dsoInstanceUID)
	{
		log.info("Generating DSO " + imageReference.imageUID + " with " + tiffMaskFiles.size() + " TIFF mask file(s)...");
		try {
			File temporaryDSOFile = File.createTempFile(imageReference.imageUID, ".dso");
//			List<String> dicomFilePaths = downloadDICOMFilesForDSO(imageReference);
//			log.info("Found " + dicomFilePaths.size() + " source DICOM file(s) for DSO " + imageReference.imageUID);

			log.info("Generating new edited DSO from original DSO " + imageReference.imageUID);
			TIFFMasksToDSOConverter converter = new TIFFMasksToDSOConverter();
			boolean removeEmptyMasks = false;
			if ("true".equals(EPADConfig.getParamValue("OptimizedDSOs", "true")))
				removeEmptyMasks = true;
			String[] seriesImageUids = converter.generateDSO(files2FilePaths(tiffMaskFiles), dicomFilePaths, temporaryDSOFile.getAbsolutePath(), dsoSeriesDescription, dsoSeriesUID, dsoInstanceUID, removeEmptyMasks);
			imageReference.seriesUID = seriesImageUids[0];
			imageReference.imageUID = seriesImageUids[1];
			log.info("Sending generated DSO " + temporaryDSOFile.getAbsolutePath() + " imageUID:" + imageReference.imageUID + " to dcm4chee...");
			DCM4CHEEUtil.dcmsnd(temporaryDSOFile.getAbsolutePath(), false);
			if (dsoSeriesUID != null && EPADConfig.xnatServer.contains("dev6"))
			{
				// No longer needed since we are updating masks already
				EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
				epadDatabaseOperations.deleteSeries(dsoSeriesUID);
			}
			return true;
		} catch (Exception e) {
			log.warning("Error generating DSO " + imageReference.imageUID + " in series " + imageReference.seriesUID, e);
			return false;
		}
	}

	public static void writeMultiFramePNGs(String studyUID, String seriesUID, String imageUID, File dicomFile) throws Exception
	{
		String pngFilePath = "";
		EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		try {
			int numberOfFrames = 1;
			
			String pngDirectoryPath = baseDicomDirectory + "/studies/" + studyUID + "/series/" + seriesUID + "/images/"
					+ imageUID + "/frames/";
			File pngFilesDirectory = new File(pngDirectoryPath);
			pngFilePath = pngDirectoryPath + "0.png";

			log.info("Writing PNGs for MultiFrame DICOM " + imageUID + " in series " + seriesUID);

			pngFilesDirectory.mkdirs();
			Opener opener = new Opener();
			ImagePlus image = opener.openImage(dicomFile.getAbsolutePath());
			if (image != null) {
				numberOfFrames  = image.getNFrames();
				int numberOfSlices  = image.getNSlices();
				log.info("Multiframe dicom, frames:" + numberOfFrames + " slices:" + numberOfSlices + " stack size:" + image.getImageStackSize());
				ImageStack stack = image.getImageStack();
	
				for (int frameNumber = 0; frameNumber < numberOfSlices; frameNumber++) {
					BufferedImage bufferedImage = stack.getProcessor(frameNumber+1).getBufferedImage();
					pngFilePath = pngDirectoryPath + frameNumber + ".png";
					File pngFile = new File(pngFilePath);
					try {
						insertEpadFile(databaseOperations, pngFilePath, 0, imageUID);
						log.info("Writing PNG frame " + frameNumber + " in multi-frame image " + imageUID + " in series " + seriesUID);
						ImageIO.write(bufferedImage, "png", pngFile);
						databaseOperations.updateEpadFileRow(pngFilePath, PNGFileProcessingStatus.DONE, pngFile.length(), "");
					} catch (IOException e) {
						log.warning("Failure writing PNG file " + pngFilePath + " for frame " + frameNumber
								+ " in multi-frame image " + imageUID + " in series " + seriesUID, e);
					}
				}
			} else {
				log.info("Using pixelmed:" + pngFilePath + " Dir:" + pngFilesDirectory.getAbsolutePath());
				ConsumerFormatImageMaker.convertFileToEightBitImage(dicomFile.getAbsolutePath(), pngFilePath, "png", 0);
				File[] pngs = pngFilesDirectory.listFiles();
				for (File png: pngs)
				{
					if (!png.getName().endsWith(".png"))
					{
						deleteQuietly(png);
					}
					else
					{
						String name = png.getName().replace("0_","");
						File newFile = new File(pngDirectoryPath, name);
						png.renameTo(newFile);
						insertEpadFile(databaseOperations, newFile.getAbsolutePath(), 0, imageUID);
						databaseOperations.updateEpadFileRow(newFile.getAbsolutePath(), PNGFileProcessingStatus.DONE,png.length(), "");
					}
				}
			}
			log.info("Finished writing PNGs for multi-frame DICOM " + imageUID + " in series " + seriesUID);
		} catch (Exception e) {
			log.warning("Exception writing multi-frame PNGs", e);
			insertEpadFile(databaseOperations, pngFilePath, 0, imageUID);
			databaseOperations.updateEpadFileRow(pngFilePath, PNGFileProcessingStatus.ERROR, 0,
					e.getMessage());
			databaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
			throw e;
		} 
	}
	
	public static boolean checkDSOMaskPNGs(File dsoFile)
	{
		String seriesUID = "";
		try {
			EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			DicomSegmentationObject dso = new DicomSegmentationObject();
			SourceImage sourceDSOImage = dso.convert(dsoFile.getAbsolutePath());
			int numberOfFrames = sourceDSOImage.getNumberOfBufferedImages();
			AttributeList dicomAttributes = PixelMedUtils.readAttributeListFromDicomFile(dsoFile.getAbsolutePath());
			String studyUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.StudyInstanceUID);
			seriesUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.SeriesInstanceUID);
			String imageUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.SOPInstanceUID);
	
			String pngMaskDirectoryPath = baseDicomDirectory + "/studies/" + studyUID + "/series/" + seriesUID + "/images/"
					+ imageUID + "/masks/";
			File pngMaskFilesDirectory = new File(pngMaskDirectoryPath);
			if (!pngMaskFilesDirectory.exists()) return false;
			int numMaskFiles = pngMaskFilesDirectory.list().length;
			if (numMaskFiles >= numberOfFrames)
			{
				return true;
			}
			else
			{
				// One more check - find number of referenced images
				DICOMElementList dicomElementList = Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, seriesUID, imageUID);
				List<DICOMElement> referencedSOPInstanceUIDDICOMElements = getDICOMElementsByCode(dicomElementList,
						PixelMedUtils.ReferencedSOPInstanceUIDCode);
				for (int i = 0; i < referencedSOPInstanceUIDDICOMElements.size(); i++)
				{
					String referencedUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedSOPInstanceUIDDICOMElements.get(i).value);
					if (referencedUID == null || referencedUID.length() == 0)
					{
						referencedSOPInstanceUIDDICOMElements.remove(i);
						i--;
					}
				}
				log.info("DSO Series:" + seriesUID +  " numberOfReferencedImages:" +  referencedSOPInstanceUIDDICOMElements.size());
				if (pngMaskFilesDirectory.list().length >= referencedSOPInstanceUIDDICOMElements.size())
				{
					// Some referenced series are missing, but pngs are ok
					databaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
					return true;
				}
				log.info("DSO Series:" + seriesUID + " numberOfFrames:" + numberOfFrames + " mask files:" + pngMaskFilesDirectory.list().length + " dir:" + pngMaskDirectoryPath);
				return false;
			}
		} catch (Exception e) {
			log.warning("Exception checking DSO PNGs, series:" + seriesUID + " file:" + dsoFile.getAbsolutePath(), e);
			return false;
		}
	}

	public static void writeDSOMaskPNGs(File dsoFile) throws Exception
	{
		writeDSOMaskPNGs(dsoFile, null);
	}
	
	public static void writeDSOMaskPNGs(File dsoFile, String username) throws Exception
	{
		log.info("Start generating DSO PNGs: " + dsoFile.getName());
		String seriesUID = "";
		File tmpDSO = File.createTempFile("DSO_" + dsoFile.getName(), ".dcm");
		try {
			EPADFileUtils.copyFile(dsoFile, tmpDSO);
			EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
			DicomSegmentationObject dso = new DicomSegmentationObject();
			SourceImage sourceDSOImage = dso.convert(tmpDSO.getAbsolutePath());
			int numberOfFrames = sourceDSOImage.getNumberOfBufferedImages();
			AttributeList dicomAttributes = PixelMedUtils.readAttributeListFromDicomFile(tmpDSO.getAbsolutePath());
			String studyUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.StudyInstanceUID);
			seriesUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.SeriesInstanceUID);
			String imageUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.SOPInstanceUID);
			String pngMaskDirectoryPath = baseDicomDirectory + "/studies/" + studyUID + "/series/" + seriesUID + "/images/"
					+ imageUID + "/masks/";
			String pngContourDirectoryPath = baseDicomDirectory + "/studies/" + studyUID + "/series/" + seriesUID + "/images/"
					+ imageUID + "/contours/";
			File pngMaskFilesDirectory = new File(pngMaskDirectoryPath);
			pngMaskFilesDirectory.mkdirs();
			if ("true".equalsIgnoreCase(EPADConfig.getParamValue("GenerateDSOContours")))
			{
				File pngContourFilesDirectory = new File(pngContourDirectoryPath);
				pngContourFilesDirectory.mkdirs();
			}

			DICOMElementList dicomElementList = Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, seriesUID, imageUID);
			AttributeList dsoDICOMAttributes = PixelMedUtils.readDICOMAttributeList(tmpDSO);
			//SequenceAttribute segmentSequence = (SequenceAttribute) dsoDICOMAttributes.get(TagFromName.SegmentSequence);
			//AttributeTag t = TagFromName.SegmentNumber;
			//Attribute a = new UnsignedShortAttribute(t);

			int nonblankFrame = 0;
			String nonBlankImageUID="";
					
			List<DICOMElement> referencedSOPInstanceUIDDICOMElements = getDICOMElementsByCode(dicomElementList,
					PixelMedUtils.ReferencedSOPInstanceUIDCode);
			String[] segNums = SequenceAttribute.getArrayOfSingleStringValueOrEmptyStringOfNamedAttributeWithinSequenceItems(dsoDICOMAttributes, TagFromName.SegmentSequence, TagFromName.SegmentNumber);
			if (segNums == null || segNums.length <= 1)
			{
				String referencedSeriesUID = "";
				log.info("Writing PNG masks for DSO " + imageUID + " number of referenced instances:" + referencedSOPInstanceUIDDICOMElements.size());
				
				//dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedSOPInstanceUIDDICOMElements.get(0).value);
				for (int i = 0; i < referencedSOPInstanceUIDDICOMElements.size(); i++)
				{
					String referencedUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedSOPInstanceUIDDICOMElements.get(i).value);
					if (referencedUID == null || referencedUID.length() == 0)
					{
						referencedSOPInstanceUIDDICOMElements.remove(i);
						i--;
					}
					else
						referencedSeriesUID = referencedUID;
				}
				log.info("Writing PNG masks for DSO " + imageUID + " number of valid referenced instances:" + referencedSOPInstanceUIDDICOMElements.size());
				if (referencedSeriesUID == null || referencedSeriesUID.length() == 0)
				{
					try {
						// Best to delete this if the source series is missing ???
						//SeriesReference seriesReference = new SeriesReference(EPADConfig.xnatUploadProjectID, null, studyUID, seriesUID);
						//DefaultEpadOperations.getInstance().deleteSeries(seriesReference, true);
					} catch (Exception x) {}
					throw new Exception("Referenced series for DSO " + seriesUID + " not found");
				}
				int frameNumber = 0;
	
				File[] oldFiles = pngMaskFilesDirectory.listFiles();
	//			for (File oldFile: oldFiles)
	//			{
	//				try
	//				{
	//					if (oldFile.getName().contains("png"))
	//						oldFile.delete();
	//				} catch (Exception x) {};
	//			}
	
				log.info("Writing PNG masks for DSO " + imageUID + " in series " + seriesUID + " DSOFile:" + dsoFile.getAbsolutePath() + " number of frames:" + numberOfFrames + " ...");
				List<DCM4CHEEImageDescription> referencedImages = new ArrayList<DCM4CHEEImageDescription>();
				List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(
						studyUID, referencedSeriesUID);
				//starting offset of instances (for series that doesn't start from 1)
				int instanceOffset = imageDescriptions.size();
				Map<String, DCM4CHEEImageDescription> descMap = new HashMap<String, DCM4CHEEImageDescription>();
				for (DCM4CHEEImageDescription imageDescription : imageDescriptions) {
					descMap.put(imageDescription.imageUID, imageDescription);
					if (imageDescription.instanceNumber < instanceOffset)
						instanceOffset = imageDescription.instanceNumber;
				}
				if (referencedSOPInstanceUIDDICOMElements.size() < imageDescriptions.size())
					instanceOffset = 1;
				if (instanceOffset == 0) instanceOffset = 1;
				
				for (DICOMElement dicomElement : referencedSOPInstanceUIDDICOMElements) {
					String referencedImageUID = dicomElement.value;
					DCM4CHEEImageDescription dcm4cheeReferencedImageDescription = descMap.get(referencedImageUID);
					referencedImages.add(dcm4cheeReferencedImageDescription);
				}
				int index = 0;
				boolean onefound = false;
				int instanceCount = 0;
				log.info("Number of valid referenced Instances:" + referencedSOPInstanceUIDDICOMElements.size() + " instance offset:" + instanceOffset);
				for (DICOMElement dicomElement : referencedSOPInstanceUIDDICOMElements) {
					String referencedImageUID = dicomElement.value;
					DCM4CHEEImageDescription dcm4cheeReferencedImageDescription = referencedImages.get(index);
					index++;
					if (dcm4cheeReferencedImageDescription == null)
					{
						log.info("Did not find referenced image, seriesuid:" + referencedSeriesUID + " imageuid:" + referencedImageUID 
							+ " for DSO seriesUID:" + seriesUID + " DSO imageUID:" + imageUID);
						continue;
					}
	
					//log.info("Image dimensions - width " + bufferedImage.getWidth() + ", height " + bufferedImage.getHeight());
					int instanceNumber = dcm4cheeReferencedImageDescription.instanceNumber;
					if (instanceNumber == 1 && onefound) // These are dicoms where all instance numbers are one !
					{
						instanceCount++;
						instanceNumber = instanceCount;
					}
					if (instanceNumber == 1 && !onefound)
					{
						onefound = true;
						instanceCount = 1;
					}
					int refFrameNumber = instanceNumber - instanceOffset; // Frames 0-based, instances 1 or more
					if (refFrameNumber < 0) continue;
					log.info("FrameNumber:" + frameNumber + " refFrameNumber:" + refFrameNumber + " instance number:" + dcm4cheeReferencedImageDescription.instanceNumber);
					projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_DSO_PNG_GEN, seriesUID, "Generating PNGs, frame:" + frameNumber, null, null);
					String pngMaskFilePath = pngMaskDirectoryPath + refFrameNumber + ".png";
					try {
						log.info("buffered image ");
						BufferedImage bufferedImage = sourceDSOImage.getBufferedImage(frameNumber);
						log.info("buffered image "+ bufferedImage.toString());
						
						BufferedImage bufferedImageWithTransparency = generateTransparentImage(bufferedImage);
						log.info(" bufferedImageWithTransparency "+ bufferedImageWithTransparency.toString());
						
						if (nonBlank.get()) {
							nonblankFrame = refFrameNumber;
							nonBlankImageUID = dcm4cheeReferencedImageDescription.imageUID;
						}
						log.info(" nonblankFrame "+ nonblankFrame);
						
						File pngMaskFile = new File(pngMaskFilePath);
						log.info(" pngMaskFile "+ pngMaskFile.getAbsolutePath());
						
						insertEpadFile(databaseOperations, pngMaskFilePath, pngMaskFile.length(), imageUID);
						log.info("Writing PNG mask file frame " + frameNumber + " of " + numberOfFrames + " for DSO " + imageUID + " in series " + seriesUID + " file:" + pngMaskFilePath + " nonBlank:" + nonBlank.get());
						ImageIO.write(bufferedImageWithTransparency, "png", pngMaskFile);
						databaseOperations.updateEpadFileRow(pngMaskFilePath, PNGFileProcessingStatus.DONE, 0, "");
					} catch (Exception e) {
						log.warning("Failure writing PNG mask file " + pngMaskFilePath + " for frame " + frameNumber + " of DSO "
								+ imageUID + " in series " + seriesUID, e);
					}
					
					// Contours are currently never set to true, so never used
					if ("true".equalsIgnoreCase(EPADConfig.getParamValue("GenerateDSOContours")))
					{
						String pngContourFilePath = pngContourDirectoryPath + refFrameNumber + ".png";
						try {
							RunSystemCommand rsc = new RunSystemCommand("convert " + pngMaskFilePath + " -negate -edge 1 -negate " + pngContourFilePath);
							rsc.run();
						} catch (Exception e) {
							log.warning("Failure writing PNG contour file " + pngContourFilePath + " for frame " + frameNumber + " of DSO "
									+ imageUID + " in series " + seriesUID, e);
						}
					}
					frameNumber++;
				}
			}
			else {
				log.info("Oh my God, this is a stupid multi-segment DSO:" + seriesUID + " number of segments:" +segNums.length);
				SequenceAttribute perFrameSeq = (SequenceAttribute) dsoDICOMAttributes.get(TagFromName.PerFrameFunctionalGroupsSequence);
				int numFrames = perFrameSeq.getNumberOfItems();
				String[] referencedSOPInstanceUIDs = new String[numFrames];
				String[] segmentNumbers = new String[numFrames];
				for (int i = 0; i < numFrames; i++)
				{
					AttributeList segAttrs = SequenceAttribute.getAttributeListFromSelectedItemWithinSequence(perFrameSeq, i);
					SequenceAttribute derImageSeq = (SequenceAttribute) segAttrs.get(TagFromName.DerivationImageSequence);
					AttributeList derImageAttrs = SequenceAttribute.getAttributeListFromSelectedItemWithinSequence(derImageSeq, 0);
					String[] refUDIDs = SequenceAttribute.getArrayOfSingleStringValueOrEmptyStringOfNamedAttributeWithinSequenceItems(derImageAttrs, TagFromName.SourceImageSequence, TagFromName.ReferencedSOPInstanceUID);
					String[] segNos = SequenceAttribute.getArrayOfSingleStringValueOrEmptyStringOfNamedAttributeWithinSequenceItems(segAttrs, TagFromName.SegmentIdentificationSequence, TagFromName.ReferencedSegmentNumber);
					referencedSOPInstanceUIDs[i] = refUDIDs[0];
					segmentNumbers[i] = segNos[0];
				}
				String referencedSeriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedSOPInstanceUIDs[0]);
				if (referencedSeriesUID == null || referencedSeriesUID.length() == 0)
					throw new Exception("Referenced series for DSO " + seriesUID + " not found");
				List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(
						studyUID, referencedSeriesUID);
				Map<String, DCM4CHEEImageDescription> descMap = new HashMap<String, DCM4CHEEImageDescription>();
				for (DCM4CHEEImageDescription imageDescription : imageDescriptions) {
					descMap.put(imageDescription.imageUID, imageDescription);
				}
				numberOfFrames = referencedSOPInstanceUIDs.length;
				for (int i = 0; i < referencedSOPInstanceUIDs.length; i++)
				{
					DCM4CHEEImageDescription dcm4cheeReferencedImageDescription = descMap.get(referencedSOPInstanceUIDs[i]);
					int instanceNumber = dcm4cheeReferencedImageDescription.instanceNumber;
					int frameNumber = instanceNumber - 1;
					BufferedImage bufferedImage = sourceDSOImage.getBufferedImage(i);
					BufferedImage bufferedImageWithTransparency = generateTransparentImage(bufferedImage);
					if (nonBlank.get()) {
						nonblankFrame = frameNumber;
						nonBlankImageUID = dcm4cheeReferencedImageDescription.imageUID;
					}
					String pngMaskFilePath = pngMaskDirectoryPath + frameNumber  + "_"  + segmentNumbers[i] + ".png";
					
					File pngMaskFile = new File(pngMaskFilePath);
					try {
						insertEpadFile(databaseOperations, pngMaskFilePath, pngMaskFile.length(), imageUID);
						log.info("Writing PNG mask file frame " + frameNumber + " of " + numberOfFrames + " for DSO " + imageUID + " in series " + seriesUID + " file:" + pngMaskFilePath + " nonBlank:" + nonBlank.get());
						ImageIO.write(bufferedImageWithTransparency, "png", pngMaskFile);
						databaseOperations.updateEpadFileRow(pngMaskFilePath, PNGFileProcessingStatus.DONE, 0, "");
					} catch (IOException e) {
						log.warning("Failure writing PNG mask file " + pngMaskFilePath + " for frame " + frameNumber + " of DSO "
								+ imageUID + " in series " + seriesUID, e);
					}
				}
			
			}
	
			EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			List<EPADAIM> aims = epadDatabaseOperations.getAIMsByDSOSeries(seriesUID);
			for (EPADAIM aim: aims)
			{
				epadDatabaseOperations.updateAIMDSOFrameNo(aim.aimID, nonblankFrame);
				AIMUtil.updateDSOStartIndex(aim, nonblankFrame);
			}
			log.info("... finished writing PNG " + numberOfFrames + " masks for DSO image " + imageUID + " in series " + seriesUID + " nonBlankFrame:" + nonblankFrame);
		} catch (DicomException e) {
			log.warning("DICOM exception writing DSO PNG masks, series:" + seriesUID, e);
			throw new Exception("DICOM exception writing DSO PNG masks, series:" + seriesUID, e);
		} catch (IOException e) {
			log.warning("IO exception writing DSO PNG masks, series:" + seriesUID, e);
			throw new Exception("IO exception writing DSO PNG masks, series:" + seriesUID, e);
		} catch (Exception e) {
			log.warning("Exception writing DSO PNG masks, series:" + seriesUID, e);
			throw new Exception("Exception writing DSO PNG masks, series:" + seriesUID, e);
		} finally {
			try {
				tmpDSO.delete();
			} catch (Exception e) {};
		}
	}
	
	private static List<DICOMElement> getDICOMElementsByCode(DICOMElementList dicomElementList, String tagCode)
	{
		Set<DICOMElement> matchingDICOMElements = new LinkedHashSet<>(); // Maintain insertion order

		for (DICOMElement dicomElement : dicomElementList.ResultSet.Result) {
			// Do not allow duplicates.
			if (dicomElement.tagCode.equals(tagCode) && !matchingDICOMElements.contains(dicomElement))
				matchingDICOMElements.add(dicomElement);
		}

		return new ArrayList<>(matchingDICOMElements);
	}
	
	public static void writePNGMasksForNiftiDSO(String subjectID, String studyUID, String seriesUID, String imageUID, File niftiFile) throws Exception
	{
		String pngMaskDirectoryPath = baseDicomDirectory + "/studies/" + studyUID + "/series/" + seriesUID + "/images/"
				+ imageUID + "/masks/";
		File path = new File(pngMaskDirectoryPath);
		path.mkdirs();
		RunSystemCommand rsc = new RunSystemCommand("miconv " + niftiFile.getAbsolutePath() + " " + pngMaskDirectoryPath + imageUID + ".png");
		rsc.run();
	}

	public static boolean handleDSOFramesEdit(String projectID, String subjectID, String studyUID, String seriesUID,
			String imageUID, HttpServletRequest httpRequest, PrintWriter responseStream)
	{ // See http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm
		boolean uploadError = false;

		log.info("Received DSO edit request for series " + seriesUID);
		String confirm = dcm4CheeDatabaseOperations.getSeriesUIDForImage(imageUID);
		//ml if ui do not know series uid (new dso)
		if (seriesUID.equals("*")) {
			seriesUID=confirm;
		}
		//ml if ui do not know study uid (new dso)
		if (studyUID.equals("*")) {
			studyUID=dcm4CheeDatabaseOperations.getStudyUIDForSeries(seriesUID);
		}
		if (!confirm.equals(seriesUID))
		{
			log.warning("Invalid ImageUID for series:" + seriesUID);
			return true;
		}
		try {
			ServletFileUpload servletFileUpload = new ServletFileUpload();
			FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(httpRequest);

			DSOEditRequest dsoEditRequest = null;
			String editedFrameNumbers = httpRequest.getParameter("editedFrameNumbers");
			if (editedFrameNumbers == null || editedFrameNumbers.length() == 0)
			{
				dsoEditRequest = extractDSOEditRequest(fileItemIterator);
			}
			else
			{
				log.info("Uploaded mask frame numbers:" + editedFrameNumbers);
				String[] frameNumbers = editedFrameNumbers.split(",");
				List<Integer> numbers = new ArrayList<Integer>();
				for (String frameNumber: frameNumbers)
				{
					if (frameNumber.trim().length() == 0) continue;
					numbers.add(new Integer(frameNumber.trim()));
				}
				dsoEditRequest = new DSOEditRequest(projectID, subjectID, studyUID, seriesUID, imageUID, httpRequest.getParameter("aimID"),numbers);
			}

			if (dsoEditRequest != null) {
				log.info("DSOEditRequest, imageUID:" + dsoEditRequest.imageUID + " aimID:" + dsoEditRequest.aimID + " number Frames:" + dsoEditRequest.editedFrameNumbers.size());
				EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
				String username = httpRequest.getParameter("username");
				EPADAIM aim = epadDatabaseOperations.getAIM(dsoEditRequest.aimID);
				if (aim != null && username != null) {
					EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
					if (!projectOperations.isAdmin(username) && !username.equals(aim.userName) 
							&& !projectOperations.isOwner(username, projectID)) {
						log.warning("No permissions to update AIM:" + aim.aimID + " for user " + username);
						throw new Exception("No permissions to update AIM:" + aim.aimID + " for user " + username);
					}
				}
				List<File> editedFramesPNGMaskFiles = HandlerUtil.extractFiles(fileItemIterator, "DSOEditedFrame", ".PNG");
				if (editedFramesPNGMaskFiles.isEmpty()) {
					log.warning("No PNG masks supplied in DSO edit request for image " + imageUID + " in series " + seriesUID);
					uploadError = true;
				} else {
					log.info("Extracted " + editedFramesPNGMaskFiles.size() + " file mask(s) for DSO edit for image " + imageUID
							+ " in  series " + seriesUID);
					if (editedFramesPNGMaskFiles.size() != dsoEditRequest.editedFrameNumbers.size())
						throw new IOException("Number of files and frames number do not match");
					if (aim != null && (aim.dsoFrameNo == 0 || aim.dsoFrameNo < dsoEditRequest.editedFrameNumbers.get(0))) {
						aim.dsoFrameNo = dsoEditRequest.editedFrameNumbers.get(0);
						epadDatabaseOperations.updateAIMDSOFrameNo(aim.aimID, aim.dsoFrameNo);
					}
					DSOEditResult dsoEditResult = DSOUtil.createEditedDSO(dsoEditRequest, editedFramesPNGMaskFiles, aim.seriesUID);
					if (dsoEditResult != null)
					{
						log.info("Copying edited frame pngs: " + dsoEditRequest.editedFrameNumbers.size());
						for (int i = 0; i < dsoEditRequest.editedFrameNumbers.size(); i++)
						{
							Integer frameNumber = dsoEditRequest.editedFrameNumbers.get(i);
							String pngMaskDirectoryPath = baseDicomDirectory + "/studies/" + studyUID + "/series/" + seriesUID + "/images/"
									+ imageUID + "/masks/";
							String pngMaskFilePath = pngMaskDirectoryPath + frameNumber + ".png";
							EPADFileUtils.copyFile(editedFramesPNGMaskFiles.get(i), new File(pngMaskFilePath));
							editedFramesPNGMaskFiles.get(i).delete();
						}
						if (dsoEditResult.aimID != null && dsoEditResult.aimID.length() > 0)
						{
							List<ImageAnnotation> aims = AIMQueries.getAIMImageAnnotations(AIMSearchType.ANNOTATION_UID, dsoEditResult.aimID, "admin");
							if (aims.size() > 0)
							{
								log.info("DSO Annotation: " + dsoEditResult.aimID);
//								String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
//								ImageAnnotation imageAnnotation =  aims.get(0);
//								PluginAIMUtil.addSegmentToImageAnnotation(imageAnnotation.getSegmentationCollection().getSegmentationList().get(0).getSopClassUID(), dsoEditResult.imageUID, imageAnnotation.getSegmentationCollection().getSegmentationList().get(0).getReferencedSopInstanceUID(),
//										imageAnnotation);
//								DICOMImageReference dsoDICOMImageReference = PluginAIMUtil.createDICOMImageReference(dsoEditResult.studyUID, dsoEditResult.seriesUID,
//										dsoEditResult.imageUID);
//								imageAnnotation.addImageReference(dsoDICOMImageReference);
//								try {
//									AIMUtil.saveImageAnnotationToServer(imageAnnotation, sessionID);
//								} catch (AimException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								} catch (edu.stanford.hakan.aim4api.base.AimException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
							}
						}
						
						responseStream.append(dsoEditResult.toJSON());
					}
					else
					{
						log.info("Null return from createEditDSO");
						uploadError = true;
					}
				}
			} else {
				log.warning("Invalid JSON header in DSO edit request for image " + imageUID + " in  series " + seriesUID);
				uploadError = true;
			}
		} catch (IOException e) {
			log.warning("IO exception handling DSO edits for series " + seriesUID, e);
			uploadError = true;
		} catch (FileUploadException e) {
			log.warning("File upload exception handling DSO edits for series " + seriesUID, e);
			uploadError = true;
		} catch (Exception e) {
			log.warning("Exception handling DSO edits for series " + seriesUID, e);
			uploadError = true;
		}
		if (!uploadError)
			log.info("DSO successfully edited");
		return uploadError;
	}
	
	public static boolean handleCreateDSO(String projectID, String subjectID, String studyUID, String seriesUID,
			HttpServletRequest httpRequest, PrintWriter responseStream, String username)
	{ // See http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm
		boolean uploadError = false;

		log.info("Received DSO create request for series " + seriesUID);
		try {
			ServletFileUpload servletFileUpload = new ServletFileUpload();
			FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(httpRequest);
			DSOEditRequest dsoEditRequest = null;
			String editedFrameNumbers = httpRequest.getParameter("editedFrameNumbers");
			if (editedFrameNumbers == null || editedFrameNumbers.length() == 0)
			{
				dsoEditRequest = extractDSOEditRequest(fileItemIterator);
			}
			else
			{
				log.info("Uploaded mask frame numbers:" + editedFrameNumbers);
				String[] frameNumbers = editedFrameNumbers.split(",");
				List<Integer> numbers = new ArrayList<Integer>();
				for (String frameNumber: frameNumbers)
				{
					if (frameNumber.trim().length() == 0) continue;
					numbers.add(new Integer(frameNumber.trim()));
				}
				dsoEditRequest = new DSOEditRequest(projectID, subjectID, studyUID, seriesUID, "", "",numbers);
			}
			log.info("DSOCreateRequest, seriesUID:" + dsoEditRequest.seriesUID + " imageUID:" + dsoEditRequest.imageUID + " aimID:" + dsoEditRequest.aimID + " number Frames:" + dsoEditRequest.editedFrameNumbers.size());

			if (dsoEditRequest != null) {
				List<File> framesPNGMaskFiles = HandlerUtil.extractFiles(fileItemIterator, "DSOFrame", ".PNG");
				if (framesPNGMaskFiles.isEmpty()) {
					log.warning("No PNG masks supplied in DSO create request for series " + seriesUID);
					uploadError = true;
				} else {
					framesPNGMaskFiles = framesPNGMaskFiles.subList(0, dsoEditRequest.editedFrameNumbers.size());
					log.info("Extracted " + framesPNGMaskFiles.size() + " file mask(s) for DSO create for series " + seriesUID);
					String name = httpRequest.getParameter("name");
					DSOEditResult dsoEditResult = DSOUtil.createNewDSO(name, dsoEditRequest, framesPNGMaskFiles, projectID, username);
					if (dsoEditResult != null)
					{					
						responseStream.append(dsoEditResult.toJSON());
					}
					else
					{
						log.info("Null return from createNewDSO");
						uploadError = true;
					}
				}
			} else {
				log.warning("Invalid JSON header in DSO edit request for series " + seriesUID);
				uploadError = true;
			}

		} catch (IOException e) {
			log.warning("IO exception handling DSO edits for series " + seriesUID, e);
			uploadError = true;
		} catch (FileUploadException e) {
			log.warning("File upload exception handling DSO edits for series " + seriesUID, e);
			uploadError = true;
		}
		if (!uploadError)
			log.info("DSO successfully created ...");
		return uploadError;
	}

	public static String getNiftiDSOComparison(File standardDSO, File testDSO) throws Exception
	{
		String command = EPADConfig.getEPADWebServerBaseDir() + "bin/EvaluateSegmentation " + standardDSO.getAbsolutePath() + " " + testDSO.getAbsolutePath() 
				+ " -use DICE,JACRD,AUC,KAPPA,RNDIND,ADJRIND,ICCORR,VOLSMTY,MUTINF,MAHLNBS,VARINFO,GCOERR,PROBDST,SNSVTY,SPCFTY,PRCISON,ACURCY,FALLOUT,HDRFDST@0.96@,FMEASR@0.5@ -xml "
				+ EPADConfig.getEPADWebServerBaseDir() + "bin/result.xml";
		log.info(command);
		String[] args = command.split(" ");
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(args);
			processBuilder.directory(new File(EPADConfig.getEPADWebServerBaseDir() + "bin/"));
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
	
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.debug("./eval_seg output: " + line);
			}

			int exitValue = process.waitFor();
			log.info("Evaluate Segmentation exit value is: " + exitValue);
			return sb.toString();
		} catch (Exception e) {
			log.warning("Error evaluating dsos", e);
			throw e;
		}
	}
	
	public static String getDSOImagesComparison(String studyUID, String seriesUID1, String seriesUID2) throws Exception
	{
		File inputDir = null;
		try {			
			EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
			//NonDicomSeries series1 = projectOperations.getNonDicomSeries(seriesUID1);
			//NonDicomSeries series2 = projectOperations.getNonDicomSeries(seriesUID2);
			List<EpadFile> files1 = projectOperations.getEpadFiles(null, null, studyUID, seriesUID1, FileType.IMAGE, true);
			List<EpadFile> files2 = projectOperations.getEpadFiles(null, null, studyUID, seriesUID2, FileType.IMAGE, true);
			String inputDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis()) + "/";
			inputDir = new File(inputDirPath);
			inputDir.mkdirs();
			File[] niftis = null;
			if (files1.size() != 1 && files2.size() != 1)
			{	
				List<DCM4CHEEImageDescription> imageDescriptions1 = dcm4CheeDatabaseOperations.getImageDescriptions(
						studyUID, seriesUID1);
				if (imageDescriptions1.size() > 1)
					throw new Exception("Invalid DSO " + seriesUID1 + " has multiple images");
				if (imageDescriptions1.size() == 0)
					throw new Exception("DSO " + seriesUID1 + " not found");
				List<DCM4CHEEImageDescription> imageDescriptions2 = dcm4CheeDatabaseOperations.getImageDescriptions(
						studyUID, seriesUID2);
				if (imageDescriptions2.size() > 1)
					throw new Exception("Invalid DSO " + seriesUID2 + " has multiple images");
				File dicom1 = new File(inputDir, imageDescriptions1.get(0).imageUID + ".dcm");
				DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, seriesUID1, imageDescriptions1.get(0).imageUID, dicom1);
				File dicom2 = new File(inputDir, imageDescriptions2.get(0).imageUID + ".dcm");
				DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, seriesUID2, imageDescriptions2.get(0).imageUID, dicom2);
				niftis = DicomFileUtil.convertDicomsToNifti(inputDir);
				if (niftis.length != 2)
					throw new Exception("Error converting dicoms to nifi");
			}
			else
			{
				niftis = new File[2];
				niftis[0] = new File(files1.get(0).getFilePath());
				niftis[1] = new File(files2.get(0).getFilePath());
			}
			String result = getNiftiDSOComparison(niftis[0], niftis[1]); // TODO: need to check which is which
			return result;
		} finally {
			if (inputDir != null)
				EPADFileUtils.deleteDirectoryAndContents(inputDir);
		}
	}
	
	private static DSOEditRequest extractDSOEditRequest(FileItemIterator fileItemIterator) throws FileUploadException,
			IOException, UnsupportedEncodingException
	{
		DSOEditRequest dsoEditRequest = null;
		Gson gson = new Gson();
		FileItemStream headerJSONItemStream = fileItemIterator.next();
		InputStream headerJSONStream = headerJSONItemStream.openStream();
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			isr = new InputStreamReader(headerJSONStream, "UTF-8");
			br = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			String json = sb.toString();
			log.info("DSOEditRequest:" + json);
			dsoEditRequest = gson.fromJson(json, DSOEditRequest.class);
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
		}
		return dsoEditRequest;
	}

	private static List<String> downloadDICOMFilesForDSO(ImageReference dsoImageReference)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		List<String> dicomFilePaths = new ArrayList<>();

		EPADFrameList frameList = epadOperations.getFrameDescriptions(dsoImageReference);
		for (EPADFrame frame : frameList.ResultSet.Result) {
			if (frame instanceof EPADDSOFrame) {
				EPADDSOFrame dsoFrame = (EPADDSOFrame)frame;
				String studyUID = dsoFrame.studyUID;
				String sourceSeriesUID = dsoFrame.sourceSeriesUID;
				String sourceImageUID = dsoFrame.sourceImageUID;

				try {
					File temporaryDICOMFile = File.createTempFile(sourceImageUID, ".dcm");
//					log.info("Downloading source DICOM file for image " + sourceImageUID + " referenced by DSO image "
//							+ dsoImageReference.imageUID);
					DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, sourceSeriesUID, sourceImageUID, temporaryDICOMFile);
					dicomFilePaths.add(temporaryDICOMFile.getAbsolutePath());
				} catch (IOException e) {
					log.warning("Error downloading DICOM file for referenced image " + sourceImageUID + " for DSO "
							+ dsoImageReference.imageUID, e);
				}
			} else {
				log.warning("Image " + dsoImageReference.imageUID + " in series " + dsoImageReference.seriesUID
						+ " does not appear to be a DSO");
			}
		}
		return dicomFilePaths;
	}

	private static List<File> getDSOTIFFMaskFiles(ImageReference imageReference, List<File> dsoMaskFiles) throws IOException
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();

		EPADFrameList frameList = epadOperations.getFrameDescriptions(imageReference);

		for (EPADFrame frame : frameList.ResultSet.Result) {
			String maskFilePath = baseDicomDirectory + frame.losslessImage;
			File maskFile = new File(maskFilePath);
			if (!maskFile.exists())
				continue;
			//log.info("Creating TIFF mask file " + maskFilePath + " for frame " + frame.frameNumber + " for DSO "
			//		+ imageReference.imageUID);
			log.debug("Existing DSO masks, frameNo:" + frame.frameNumber + " maskFile:" + maskFile.getName());
			try {
				BufferedImage bufferedImage = ImageIO.read(maskFile);
				File tiffFile = File.createTempFile(imageReference.imageUID + "_frame_" + frame.frameNumber + "_", ".tif");
				ImageIO.write(bufferedImage, "tif", tiffFile);
				dsoMaskFiles.set(dsoMaskFiles.size() - frame.frameNumber-1, tiffFile);
			} catch (IOException e) {
				log.warning("Error creating TIFF mask file " + maskFilePath + " for frame " + frame.frameNumber + " for DSO "
						+ imageReference.imageUID, e);
				throw e;
			}
		}
		log.debug("Total masks" + dsoMaskFiles.size() + " Existing masks:" +  frameList.ResultSet.Result.size());
		return dsoMaskFiles;
	}

	private static List<File> generateTIFFsFromPNGs(List<File> pngFiles)
	{
		List<File> tiffFiles = new ArrayList<>();

		for (File pngFile : pngFiles) {
			log.debug("PNG FIle:" + pngFile.getAbsolutePath());
			try {
				BufferedImage bufferedImage = ImageIO.read(pngFile);
				File tiffFile = File.createTempFile(pngFile.getName(), ".tif");
				ImageIO.write(bufferedImage, "tif", tiffFile);
				tiffFiles.add(tiffFile);
			} catch (Exception e) {
				log.warning("Error creating TIFF  file from PNG " + pngFile.getAbsolutePath(), e);
			}
		}
		return tiffFiles;
	}

	private static BufferedImage generateTransparentImage(BufferedImage source)
	{
		Image image = makeColorOpaque(source, Color.BLACK); // Because somebody said to convert DSOs to white
//		Image image = makeAnyColorWhite(source); // To retain DSO color comment this out and uncomment previous line
		BufferedImage transparent = imageToBufferedImage(image);
		Image image2 = makeColorTransparent(transparent, Color.BLACK);
		BufferedImage transparent2 = imageToBufferedImage(image2);
		return transparent2;
	}

	private static BufferedImage imageToBufferedImage(Image image)
	{
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB); 
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return bufferedImage;
	}

	private static ThreadLocal<Boolean> nonBlank = new ThreadLocal<Boolean>();
	private static Image makeAnyColorWhite(BufferedImage im)
	{
		nonBlank.set(false);
		ImageFilter filter = new RGBImageFilter() {

			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				if ((rgb & 0x00FFFFFF) != 0) {
					nonBlank.set(true);
					return 0xFFFFFFFF;
				} else {
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private static Image makeColorOpaque(BufferedImage im, final Color color)
	{
		nonBlank.set(false);
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				if ((rgb & 0x00FFFFFF) != 0) {
					nonBlank.set(true);
				}
				if ((rgb | 0xFF000000) == markerRGB) {
//					nonBlank.set(true);
					return 0xFF000000 | rgb;
				} else {
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private static Image makeColorTransparent(BufferedImage im, final Color color)
	{
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				if ((rgb | 0xFF000000) == markerRGB) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private static List<String> files2FilePaths(List<File> files)
	{
		List<String> filePaths = new ArrayList<>();

		for (File file : files)
			filePaths.add(file.getAbsolutePath());

		return filePaths;
	}

	private static void insertEpadFile(EpadDatabaseOperations epadDatabaseOperations, String outputFilePath,
			long fileSize, String imageUID)
	{
		Map<String, String> epadFilesRow = Dcm4CheeDatabaseUtils.createEPadFilesRowData(outputFilePath, fileSize, imageUID);
		epadFilesRow.put("file_status", "" + PNGFileProcessingStatus.IN_PIPELINE.getCode());
		epadDatabaseOperations.insertEpadFileRow(epadFilesRow);
	}
}
