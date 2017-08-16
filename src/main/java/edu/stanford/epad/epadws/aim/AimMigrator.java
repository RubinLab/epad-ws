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
package edu.stanford.epad.epadws.aim;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.stanford.epad.common.dicom.DCM4CHEEImageDescription;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.plugins.PluginAIMUtil;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.dtos.internal.DICOMElement;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.models.Template;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.hakan.aim4api.base.AimException;
import edu.stanford.hakan.aim4api.base.Algorithm;
import edu.stanford.hakan.aim4api.base.CD;
import edu.stanford.hakan.aim4api.base.CalculationEntity;
import edu.stanford.hakan.aim4api.base.CharacteristicQuantification;
import edu.stanford.hakan.aim4api.base.DicomImageReferenceEntity;
import edu.stanford.hakan.aim4api.base.Enumerations;
import edu.stanford.hakan.aim4api.base.ExtendedCalculationResult;
import edu.stanford.hakan.aim4api.base.II;
import edu.stanford.hakan.aim4api.base.Image;
import edu.stanford.hakan.aim4api.base.ImageAnnotationCollection;
import edu.stanford.hakan.aim4api.base.ImageCollection;
import edu.stanford.hakan.aim4api.base.ImageSeries;
import edu.stanford.hakan.aim4api.base.ImageStudy;
import edu.stanford.hakan.aim4api.base.ImagingObservationCharacteristic;
import edu.stanford.hakan.aim4api.base.ImagingObservationEntity;
import edu.stanford.hakan.aim4api.base.ImagingPhysicalEntity;
import edu.stanford.hakan.aim4api.base.ST;
import edu.stanford.hakan.aim4api.base.Scale;
import edu.stanford.hakan.aim4api.base.TwoDimensionSpatialCoordinate;
import edu.stanford.hakan.aim4api.base.TwoDimensionSpatialCoordinateCollection;
import edu.stanford.hakan.aim4api.base.Enumerations.ScaleType;
import edu.stanford.hakan.aim4api.compability.aimv3.Modality;
import edu.stanford.hakan.aim4api.project.epad.Enumerations.ShapeType;
import edu.stanford.hakan.aim4api.questions.Question;
import edu.stanford.hakan.aim4api.questions.QuestionCollection;
import edu.stanford.hakan.aim4api.usage.AnnotationValidator;

public class AimMigrator {
	private static final EPADLogger log = EPADLogger.getInstance();

	/****************************AIM Migration Methods***********************************/

	/**
	 * Save the aim xml to the project
	 * @param xml
	 * @param projectID
	 * @param username
	 * @throws AimException
	 */
	public static void saveAim(String xml, String projectID, String username) throws AimException {
		if (xml==null) {
			log.info("Input xml is empty. Skipping save");
		}else {
			String tmpAimName="/tmp/tmpAim"+System.currentTimeMillis()+".xml";
			File tmpAim=new File(tmpAimName);
			EPADFileUtils.write(tmpAim, xml);
			log.info("tmp aim path:"+ tmpAim.getAbsolutePath());

			if (AnnotationValidator.ValidateXML(tmpAim.getAbsolutePath(), EPADConfig.xsdFilePath)) {
				log.info("xml produced  is valid");
				//sending null. plugin wouldn't be triggered
				if (AIMUtil.saveAIMAnnotation(tmpAim, projectID, 0, null, username, false, false))
					log.warning("Error saving aim file:" + tmpAimName);
			}
			else 
				log.warning("xml produced from dicom sr is NOT valid");
		}
	}

	/**
	 * migrate a mint formatted json to an actual aim file and save to the project
	 * @param mintJson
	 * @param projectID
	 * @param username
	 * @param templateCode
	 */
	public static void migrateAimFromMintJson(JSONObject mintJson, String projectID, String username, String templateCode) {

		try{
			String aimXML= createAimFromMintJson(mintJson, username, templateCode);
			saveAim(aimXML, projectID, username);

		} catch(Exception e) {
			log.warning("Mint to Aim migration is unsuccessful", e);
		}

	}


	/**
	 * migrate a mint formatted json to an actual aim file  and download it
	 * @param mintJson
	 * @param username
	 * @param templateCode
	 */
	public static String migrateAimFromMintJson(JSONObject mintJson, String username, String templateCode) {

		try{
			String aimXML= createAimFromMintJson(mintJson, username, templateCode);
			String tmpAimName=EPADConfig.getEPADWebServerDownloadDir()+ mintJson.getString("name").replaceAll(" ", "")+System.currentTimeMillis()+".xml";
			File tmpAim=new File(tmpAimName);
			EPADFileUtils.write(tmpAim, aimXML);
			log.info("tmp aim path:"+ tmpAim.getAbsolutePath());
			if (AnnotationValidator.ValidateXML(tmpAim.getAbsolutePath(), EPADConfig.xsdFilePath)) {
				log.info("xml produced  is valid");
				return tmpAim.getName();
			}

		} catch(Exception e) {
			log.warning("Mint to Aim migration is unsuccessful", e);
		}
		return null;

	}

	/**
	 * create an aim file using the information in the mintjson and the template code that is sent
	 * @param mintJson
	 * @param username
	 * @param templateCode
	 * @return
	 * @throws Exception
	 */
	public static String createAimFromMintJson(JSONObject mintJson, String username, String templateCode) throws Exception {


		String lesionName=mintJson.getString("name");
		String comment=mintJson.getString("comment");
		String sopClassUID="na",studyDate="na",studyTime="na", pName="na",pId="na",pBirthDate="na",pSex="na", studyUID="na", sourceSeriesUID="na";
		String accessionNumber="";
		String imageUID=mintJson.optString("imageInstanceUid");
		if (imageUID!=null && !imageUID.equals("")) {
			log.info("Retrieved image uid is "+imageUID);

			//fill the missing information from dicom tags
			DICOMElementList tags= Dcm4CheeQueries.getDICOMElementsFromWADO("*", "*", imageUID);
			if (tags==null) {
				log.warning("Dicom image couldn't be retrieved. Cannot get the necessary information!");
			}
			else {
				log.info("study code:"+PixelMedUtils.StudyInstanceUIDCode + " series code:"+PixelMedUtils.SeriesInstanceUIDCode);
				for (int i=0; i< tags.ResultSet.totalRecords; i++) {
					DICOMElement tag=tags.ResultSet.Result.get(i);

					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.SOPClassUIDCode)) 
						sopClassUID=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.StudyDateCode)) 
						studyDate=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.StudyTimeCode)) 
						studyTime=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.AccessionNumberCode)) 
						accessionNumber=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.PatientNameCode)) 
						pName=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.PatientIDCode)) 
						pId=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.PatientBirthDateCode)) 
						pBirthDate=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.PatientSexCode)) 
						pSex=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.StudyInstanceUIDCode)) 
						studyUID=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.SeriesInstanceUIDCode)) 
						sourceSeriesUID=tag.value;
				}
			}

		}else { 
			String seriesUID=mintJson.optString("imageSeriesUid");
			sourceSeriesUID=seriesUID;
			if (seriesUID!=null && !seriesUID.equals("")) {
				DCM4CHEESeries series=Dcm4CheeQueries.getSeries(seriesUID);
				if (series!=null) {
					//				sopClassUID=;
					studyDate=series.seriesDate;
					studyTime=series.createdTime;
					accessionNumber=series.accessionNumber;
					pName=series.patientName;
					pId=series.patientID;
					//				pBirthDate=tag.value;
					//				pSex=tag.value;
					studyUID=series.studyUID;

				}


			}else {
				//imageInstanceUID not present read the available data from mintjson.
				imageUID=((JSONObject)((JSONObject)((JSONArray)((JSONObject)((JSONObject)mintJson.get("imageStudy")).get("imageSeries")).get("imageCollection")).get(0)).get("image")).optString("sopInstanceUid", "na");
				sopClassUID=((JSONObject)((JSONObject)((JSONArray)((JSONObject)((JSONObject)mintJson.get("imageStudy")).get("imageSeries")).get("imageCollection")).get(0)).get("image")).optString("sopClassUid", "na");

				studyDate=((JSONObject)mintJson.get("imageStudy")).getString("startDate");
				studyTime=((JSONObject)mintJson.get("imageStudy")).getString("startTime");
				accessionNumber=((JSONObject)mintJson.get("imageStudy")).optString("accessionNumber");

				pName=((JSONObject)mintJson.get("person")).getString("name");
				pId=((JSONObject)mintJson.get("person")).getString("id");
				pBirthDate=((JSONObject)mintJson.get("person")).getString("birthDate");
				pSex=((JSONObject)mintJson.get("person")).getString("sex");

				studyUID=((JSONObject)mintJson.get("imageStudy")).getString("instanceUid");
				sourceSeriesUID=((JSONObject)((JSONObject)mintJson.get("imageStudy")).get("imageSeries")).optString("instanceUid", "na");
			}
		}
		JSONObject pf=mintJson.optJSONObject("PlanarFigure");
		if (pf!=null){
			Double sliceLoc=((JSONObject) ((JSONObject)pf.get("Geometry")).get("Origin")).getDouble("z");
			Double sliceThickness=((JSONObject) ((JSONObject)pf.get("Geometry")).get("Spacing")).getDouble("z");
			log.info("slice location is "+ sliceLoc);
			comment =comment+" / "+sliceLoc + " / " +sliceThickness;
			if ((imageUID==null || imageUID.equals("") || imageUID.equals("na"))&& !(sourceSeriesUID==null||sourceSeriesUID.equals("na")||sourceSeriesUID.equals(""))) {
				//get the image uid using the slice location
				String dcm4cheStudyUID=studyUID;
				if (dcm4cheStudyUID==null || dcm4cheStudyUID.equals("") || dcm4cheStudyUID.equals("na"))
					dcm4cheStudyUID="*";
				Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations= Dcm4CheeDatabase.getInstance()
						.getDcm4CheeDatabaseOperations();
				EpadOperations epadOperations = DefaultEpadOperations.getInstance();
				List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(
						dcm4cheStudyUID, sourceSeriesUID);
				for(DCM4CHEEImageDescription image:imageDescriptions){
					DICOMElementList imageDICOMElements = epadOperations.getDICOMElements(image.studyUID,
							image.seriesUID, image.imageUID);
					String imagePosition=epadOperations.getDICOMElement(imageDICOMElements,PixelMedUtils.ImagePositionPatientCode);
					Double imageLoc=-1.0;
					try{
						if (imagePosition!=null) {
							imageLoc=Double.parseDouble(imagePosition.split("\\\\")[2]);
						}
					}catch(Exception e){
	
						log.warning("Couldn't get image position "+e.getMessage()) ;
					}
					if ( imageLoc==-1.0) {
						imageLoc=((Double)Double.parseDouble(image.sliceLocation));
						log.info("Couldn't get image position using slice loc" +imageLoc) ;
					}
					if (((Double)(imageLoc-(0.5*sliceThickness))).intValue()==sliceLoc.intValue()||imageLoc.intValue()==sliceLoc.intValue()){
						log.info("found image "+ image.instanceNumber +  " uid "+image.imageUID);
						imageUID=image.imageUID;
						break;
					}
				}
	
			}
		}else {
			comment=comment+" / no geometric roi";
		}
		if (imageUID.equals(""))
			imageUID="na"; //to keep all the same
		log.info("the values retrieved are "+ sopClassUID+" "+studyDate+" "+studyTime+" "+pName+" "+pId+" "+pBirthDate+" "+pSex+" "+studyUID+" "+sourceSeriesUID+" ");
		ImageAnnotationCollection iac = createImageAnnotationColectionFromProperties(username, pName, pId, pBirthDate, pSex);
		edu.stanford.hakan.aim4api.base.ImageAnnotation ia=createImageAnnotationFromProperties(username, templateCode, lesionName, comment, imageUID, sopClassUID, studyDate, studyTime, studyUID, sourceSeriesUID, accessionNumber);
		
		//see if you can find trial info and store as freetext
		String trial=mintJson.optString("trial");
		String trialArm=mintJson.optString("trialArm");
		String trialCaseID=mintJson.optString("trialCaseID");
		QuestionCollection qc= new QuestionCollection();
		if (trial!=null && !trial.equals("")){
			qc.addQuestion(new Question("Trial", trial));
		}
		if (trialArm!=null && !trialArm.equals("")){
			qc.addQuestion(new Question("Trial Arm", trialArm));
		}
		if (trialCaseID!=null && !trialCaseID.equals("")){
			qc.addQuestion(new Question("Trial CaseID", trialCaseID));
		}
		ia.setQuestionCollection(qc);
		
		//create the entities using information from pf
		if (pf!=null)
			ia=addMarkupAndCalculationFromPF(ia,pf);

		String location = ((JSONObject)mintJson.get("lesion")).optString("location");
		if (location!=null && !location.equals(""))
			ia.addImagingPhysicalEntity(getImagingPhysicalEntityFromPF("Location",location));
		//ia.addImagingPhysicalEntity(getImagingPhysicalEntityFromPF("Status",((JSONObject)mintJson.get("lesion")).getString("status")));
		CD qualityCode=null;
		//default yes
		if (((JSONObject)mintJson.get("lesion")).optString("evaluable","yes").equalsIgnoreCase("no"))
			qualityCode=new CD("RID39225","Nonevaluable","Radlex",""); // is not evaluable
		else 
			qualityCode=new CD("S86","Evaluable","RECIST-AMS",""); //is evaluable

		String status=((JSONObject)mintJson.get("lesion")).optString("status");
		String enhancement=((JSONObject)mintJson.get("lesion")).optString("enhancement");
		ia.addImagingObservationEntity(getImagingObservationEntityFromPF("Lesion Quality",qualityCode,"Timepoint",((JSONObject)mintJson.get("lesion")).getInt("timepoint"), "Type",((JSONObject)mintJson.get("lesion")).getString("type"),"Lesion Status", status, "Lesion Enhancement",enhancement));
		
		edu.stanford.epad.common.util.Lexicon lexicon=edu.stanford.epad.common.util.Lexicon.getInstance();
		CD parent = lexicon.getLex("mint");
		log.info("parent code is "+ parent.getCode());
		if (parent.getCode().trim().equalsIgnoreCase("99EPADD0")){
			parent = lexicon.createLex("mint","Mint Calculations",null,"99EPADM1");
			log.info("new parent code is "+ parent.getCode());

		}

		iac.addImageAnnotation(ia);

		//add the rest of the calculations
		ArrayList<String[]> features=getMeasurementsFromPF(mintJson);
		if (features!=null)
			iac=PluginAIMUtil.addFeatures(iac, features , 1,parent, true) ; 

		//this should be called after addfeatures as addfeatures tries to init v3.CalculationData and fails as it is not double
		addSummaryCalcsFromPF(mintJson, parent, iac.getImageAnnotation());
		log.info("annotation is: "+iac.toStringXML());
		return iac.toStringXML();

	}

	public static void addSummaryCalcsFromPF(JSONObject mintJson, CD parent, edu.stanford.hakan.aim4api.base.ImageAnnotation ia){
		JSONArray measurements = (JSONArray) mintJson.optJSONArray("measurements");
		if (measurements!=null){
			for (int i = 0; i < measurements.length(); i++) {
				//if this is a summary. add it as a separate calculation
				try{ 
					double val=Double.parseDouble(((JSONObject)measurements.get(i)).getString("CurrentValue"));
				}catch(NumberFormatException ne){
					CD featureCD = edu.stanford.epad.common.util.Lexicon.getInstance().getLex(((JSONObject)measurements.get(i)).getString("Type"));
					if (featureCD.getCode().equals("99EPADD0")){
						featureCD = edu.stanford.epad.common.util.Lexicon.getInstance().createLex(((JSONObject)measurements.get(i)).getString("Type"),((JSONObject)measurements.get(i)).getString("Type"),parent,null);
					}
					ia.addCalculationEntity(addCalculation(((JSONObject)measurements.get(i)).getString("CurrentValue"),1,"",featureCD.getDisplayName().getValue(), featureCD.getCode()));
	
				}
			}
		}

	}

	public static ArrayList<String[]> getMeasurementsFromPF(JSONObject mintJson){
		JSONArray measurements = (JSONArray) mintJson.optJSONArray("measurements");
		ArrayList<String[]> features=null;
		if (measurements!=null){
			features=new ArrayList<>();
		
			for (int i = 0; i < measurements.length(); i++) {
				try {
					double val=Double.parseDouble(((JSONObject)measurements.get(i)).getString("CurrentValue"));
					features.add(new String[] {((JSONObject)measurements.get(i)).getString("Type"),((JSONObject)measurements.get(i)).getString("CurrentValue"),((JSONObject)measurements.get(i)).getString("Unit")});
				}catch(NumberFormatException ne){
					log.info("this is a summary. dont add it as a feature add a separate calculation. "+((JSONObject)measurements.get(i)).getString("Type"));
				}
			}
		}
		return features;
	}

	/**
	 * create an ImageAnnotationCollection object using the properties
	 * @param username
	 * @param pName
	 * @param pId
	 * @param pBirthDate
	 * @param pSex
	 * @return
	 */
	public static edu.stanford.hakan.aim4api.base.ImageAnnotationCollection createImageAnnotationColectionFromProperties(String username, String pName, String pId, String pBirthDate, String pSex){
		ImageAnnotationCollection iac = new ImageAnnotationCollection();
		edu.stanford.hakan.aim4api.base.User user=new edu.stanford.hakan.aim4api.base.User();
		user.setName(new ST(username));
		user.setLoginName(new ST(username));
		iac.setUser(user);
		//set the date to current date
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now=new Date();

		iac.setDateTime(dateFormat.format(now));

		//put the patient information in aim
		edu.stanford.hakan.aim4api.base.Person p=new edu.stanford.hakan.aim4api.base.Person();
		p.setBirthDate(formatPatientBirthDate(pBirthDate));
		p.setId(new ST(pId));
		p.setName(new ST(pName));
		p.setSex(new ST(pSex));
		iac.setPerson(p);

		return iac;
	}


	/**
	 * create an ImageAnnotation object using the properties
	 * @param username
	 * @param templateCode
	 * @param lesionName
	 * @param comment
	 * @param imageUID
	 * @param sopClassUID
	 * @param studyDate
	 * @param studyTime
	 * @param studyUID
	 * @param sourceSeriesUID
	 * @return
	 * @throws Exception
	 */
	
	public static edu.stanford.hakan.aim4api.base.ImageAnnotation createImageAnnotationFromProperties(String username, String templateCode, String lesionName, String comment, String imageUID,String sopClassUID,String studyDate, String studyTime,String studyUID, String sourceSeriesUID) throws Exception {
		return createImageAnnotationFromProperties(username, templateCode, lesionName, comment, imageUID, sopClassUID, studyDate, studyTime, studyUID, sourceSeriesUID, "");
	}
	public static edu.stanford.hakan.aim4api.base.ImageAnnotation createImageAnnotationFromProperties(String username, String templateCode, String lesionName, String comment, String imageUID,String sopClassUID,String studyDate, String studyTime,String studyUID, String sourceSeriesUID, String accessionNumber) throws Exception {
		log.info("creating image annotation for template:"+ templateCode +" lesion:" +lesionName+ " comment:" +comment+" imageuid:"+ imageUID) ;
		EpadProjectOperations projOp = DefaultEpadProjectOperations.getInstance();

		//set the date to current date
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now=new Date();


		edu.stanford.hakan.aim4api.base.ImageAnnotation ia=new edu.stanford.hakan.aim4api.base.ImageAnnotation();
		ia.refreshUniqueIdentifier();
		ia.setDateTime(dateFormat.format(now));

		Template t=null;
		if (templateCode==null)
			t=projOp.getTemplate("RECIST");
		else
			t=projOp.getTemplate(templateCode);
		if (t!=null){
			ia.setName(new ST(lesionName));

			ArrayList<CD> types=new ArrayList<>();
			//TODO it puts different values in the aim file than standard use. What does the gui put????
			types.add(new CD(t.getTemplateCode(),t.getTemplateName(),t.getCodingSchemeDesignator(),t.getCodingSchemeVersion()));
			ia.setTypeCode(types);

		}

		ia.setComment(new ST(comment));



		//add the image reference entity
		DicomImageReferenceEntity dicomImageReferenceEntity  = new DicomImageReferenceEntity();
		ImageStudy study = new ImageStudy();
		study.setInstanceUid(new II(studyUID));
		ImageSeries series=new ImageSeries();
		series.setInstanceUid(new II(sourceSeriesUID));
		Image image=new Image();
		image.setSopInstanceUid(new II(imageUID));
		image.setSopClassUid(new II(sopClassUID));
		ImageCollection imageCol=new ImageCollection();
		imageCol.addImage(image);
		series.setImageCollection(imageCol);
		Modality mod=Modality.getInstance();
		//if the modality cannot be retrieved put default
		if ((mod.get(sopClassUID))!=null)
			series.setModality(mod.get(sopClassUID));
		else
			series.setModality(mod.getDefaultModality());
		study.setImageSeries(series);
		study.setStartDate(studyDate);
		study.setStartTime(studyTime);
		if (accessionNumber!=null && !accessionNumber.equals("")) {
			study.setAccessionNumber(new ST(accessionNumber));
		}
		dicomImageReferenceEntity.setImageStudy(study);
		ia.addImageReferenceEntity(dicomImageReferenceEntity);

		return ia;

	}

	/**
	 * create a ImagingObservationEntity using the timepoint and type (in characteristic label and value) values
	 * @param label
	 * @param value
	 * @param tpCharacteristicLabel timepoint characteristic
	 * @param tpCharacteristicValue
	 * @param typeCharacteristicLabel type characteristic
	 * @param typeCharacteristicValue
	 * @return
	 */
	private static ImagingObservationEntity getImagingObservationEntityFromPF(String label, CD value,
			String tpCharacteristicLabel, Integer tpCharacteristicValue, String typeCharacteristicLabel, String typeCharacteristicValue) {
		ImagingObservationEntity oe= new ImagingObservationEntity();
		oe.setUniqueIdentifier();
		oe.setLabel(new ST(label));
		oe.setAnnotatorConfidence(0.0);

		oe.addTypeCode(value);

		ImagingObservationCharacteristic oc=new ImagingObservationCharacteristic();
		oc.setLabel(new ST(tpCharacteristicLabel));
		oc.setAnnotatorConfidence(0.0);
		oc.addTypeCode(new CD("S90","FU Number (0=Baseline)","RECIST-AMS",""));
		Scale cq=new Scale();
		cq.setAnnotatorConfidence(0.0);
		cq.setValue(new ST(String.valueOf(tpCharacteristicValue)));
		cq.setType(ScaleType.Nominal);
		String cqLabel="Baseline";
		if (tpCharacteristicValue>0)
			cqLabel="FU"+tpCharacteristicValue;
		cq.setLabel(new ST(tpCharacteristicLabel));
		cq.setValueLabel(new ST(cqLabel));
		oc.addCharacteristicQuantification(cq);

		oe.addImagingObservationCharacteristic(oc);

		oc=new ImagingObservationCharacteristic();
		oc.setLabel(new ST(typeCharacteristicLabel));
		oc.setAnnotatorConfidence(0.0);
		//do not clean lesion type
		oc.addTypeCode(edu.stanford.epad.common.util.Lexicon.getInstance().getLex(typeCharacteristicValue));

		oe.addImagingObservationCharacteristic(oc);
		
		
		return oe;
	}

	/**
	 * new getImagingObservationEntity with status and enhancing. Uses the old version 
	 * @param label
	 * @param value
	 * @param tpCharacteristicLabel
	 * @param tpCharacteristicValue
	 * @param typeCharacteristicLabel
	 * @param typeCharacteristicValue
	 * @param statusCharacteristicLabel
	 * @param statusCharacteristicValue
	 * @param enhancingCharacteristicLabel
	 * @param enhancingCharacteristicValue
	 * @return
	 */
	private static ImagingObservationEntity getImagingObservationEntityFromPF(String label, CD value,
			String tpCharacteristicLabel, Integer tpCharacteristicValue, String typeCharacteristicLabel, String typeCharacteristicValue, String statusCharacteristicLabel, String statusCharacteristicValue,String enhancingCharacteristicLabel, String enhancingCharacteristicValue ) {
		ImagingObservationEntity oe= getImagingObservationEntityFromPF(label,value,tpCharacteristicLabel,  tpCharacteristicValue,  typeCharacteristicLabel,  typeCharacteristicValue);
		
		ImagingObservationCharacteristic oc=new ImagingObservationCharacteristic();
		oc.setLabel(new ST(statusCharacteristicLabel));
		oc.setAnnotatorConfidence(0.0);
		//do not clean 
		oc.addTypeCode(edu.stanford.epad.common.util.Lexicon.getInstance().getLex(statusCharacteristicValue));

		oe.addImagingObservationCharacteristic(oc);
		
		if (enhancingCharacteristicValue!=null && !enhancingCharacteristicValue.equals("")) {
			oc=new ImagingObservationCharacteristic();
			oc.setLabel(new ST(enhancingCharacteristicLabel));
			oc.setAnnotatorConfidence(0.0);
			//do not clean 
			oc.addTypeCode(edu.stanford.epad.common.util.Lexicon.getInstance().getLex(enhancingCharacteristicValue));
	
			oe.addImagingObservationCharacteristic(oc);
		}
		return oe;
	}

	
	private static String cleanString(String value) {
		return value.replaceAll("-", " ");
	}

	/**
	 * create ImagingPhysicalEntity for location and status
	 * @param label
	 * @param value
	 * @return
	 */
	private static ImagingPhysicalEntity getImagingPhysicalEntityFromPF(String label, String value) {
		ImagingPhysicalEntity pe= new ImagingPhysicalEntity();
		pe.setUniqueIdentifier();
		pe.setLabel(new ST(label));
		pe.setAnnotatorConfidence(0.0);
		pe.addTypeCode(edu.stanford.epad.common.util.Lexicon.getInstance().getLex(cleanString(value)));
		return pe;
	}

	/**
	 * create the Markup entity and line length Calculation entities using information from pf
	 * adds it the the input image annotation and returns the image annotation
	 * @param ia
	 * @param pf
	 * @return ia
	 */
	private static edu.stanford.hakan.aim4api.base.ImageAnnotation addMarkupAndCalculationFromPF(edu.stanford.hakan.aim4api.base.ImageAnnotation ia,JSONObject pf) {
		//extract the geometry
		JSONObject transformParam = (JSONObject) ((JSONObject)pf.get("Geometry")).get("transformParam");
		double[][] transformMatrix=new double[3][3];
		transformMatrix[0][0]=transformParam.getDouble("param0");
		transformMatrix[0][1]=transformParam.getDouble("param1");
		transformMatrix[0][2]=transformParam.getDouble("param2");
		transformMatrix[1][0]=transformParam.getDouble("param3");
		transformMatrix[1][1]=transformParam.getDouble("param4");
		transformMatrix[1][2]=transformParam.getDouble("param5");
		transformMatrix[2][0]=transformParam.getDouble("param6");
		transformMatrix[2][1]=transformParam.getDouble("param7");
		transformMatrix[2][2]=transformParam.getDouble("param8");
		log.info("Transform matrix");
		log.info(transformMatrix[0][0]+" "+transformMatrix[0][1]+" "+transformMatrix[0][2]);
		log.info(transformMatrix[1][0]+" "+transformMatrix[1][1]+" "+transformMatrix[1][2]);
		log.info(transformMatrix[2][0]+" "+transformMatrix[2][1]+" "+transformMatrix[2][2]);


		double[] originVectorTrasform=new double[3];
		originVectorTrasform[0]=transformParam.getDouble("param9");
		originVectorTrasform[1]=transformParam.getDouble("param10");
		originVectorTrasform[2]=transformParam.getDouble("param11");
		log.info("Transform origin");
		log.info(originVectorTrasform[0]+" "+originVectorTrasform[1]+" "+originVectorTrasform[2]);

		JSONObject boundsParam = (JSONObject) ((JSONObject)pf.get("Geometry")).get("boundsParam");
		double[][] boundsMatrix=new double[3][2];
		boundsMatrix[0][0]=boundsParam.getDouble("bound0");//x
		boundsMatrix[0][1]=boundsParam.getDouble("bound1");
		boundsMatrix[1][0]=boundsParam.getDouble("bound2");//y
		boundsMatrix[1][1]=boundsParam.getDouble("bound3");
		boundsMatrix[2][0]=boundsParam.getDouble("bound4");//z
		boundsMatrix[2][1]=boundsParam.getDouble("bound5");
		log.info("Bounds matrix");
		log.info(boundsMatrix[0][0]+" "+boundsMatrix[0][1]);
		log.info(boundsMatrix[1][0]+" "+boundsMatrix[1][1]);
		log.info(boundsMatrix[2][0]+" "+boundsMatrix[2][1]);


		JSONObject spacingParam = (JSONObject) ((JSONObject)pf.get("Geometry")).get("Spacing");
		double[] spacingVector=new double[3];
		spacingVector[0]=spacingParam.getDouble("x");
		spacingVector[1]=spacingParam.getDouble("y");
		spacingVector[2]=spacingParam.getDouble("z");
		log.info("Spacing");
		log.info(spacingVector[0]+" "+spacingVector[1]+" "+spacingVector[2]);

		JSONObject originParam = (JSONObject) ((JSONObject)pf.get("Geometry")).get("Origin");
		double[] originVector=new double[3];
		originVector[0]=originParam.getDouble("x");
		originVector[1]=originParam.getDouble("y");
		originVector[2]=originParam.getDouble("z");
		log.info("Origin ");
		log.info(originVector[0]+" "+originVector[1]+" "+originVector[2]);

		JSONArray points = (JSONArray) ((JSONObject)pf.get("ControlPoints")).get("Vertex");
		double [][] pointsMM = new double[points.length()][3];
		for (int i = 0; i < points.length(); i++) {
			log.info(i+". point index="+((JSONObject)points.get(i)).getString("id")+ " x="+Double.parseDouble(((JSONObject)points.get(i)).getString("x"))+ " y="+Double.parseDouble(((JSONObject)points.get(i)).getString("y")));

			//TODO should put them in the correct id order in case it is unordered
			pointsMM[i][0]=(Double.parseDouble(((JSONObject)points.get(i)).getString("id")));
			pointsMM[i][1]=(Double.parseDouble(((JSONObject)points.get(i)).getString("x")));
			pointsMM[i][2]=(Double.parseDouble(((JSONObject)points.get(i)).getString("y")));
		}

		double [][] pointsPX=transformPoints(pointsMM,transformMatrix,originVectorTrasform,boundsMatrix,spacingVector,originVector);

		ia=addMarkupFromPointsPX(ia, pointsPX,ShapeType.SPLINE);
		//let the json send the one from measurements
//		ia=addLengthCalculationFromPointsPX(ia, pointsPX, spacingVector);
		return ia;
	}

	/**
	 * create the Markup entity using information from pf
	 * adds it the the input image annotation and returns the image annotation
	 * @param ia
	 * @param pf
	 * @return ia
	 */
	private static edu.stanford.hakan.aim4api.base.ImageAnnotation addMarkupFromPointsPX(edu.stanford.hakan.aim4api.base.ImageAnnotation ia,double [][] pointsPX, ShapeType shapeType) {

		//add the geometric shape entity
		edu.stanford.hakan.aim4api.base.TwoDimensionGeometricShapeEntity res = null;
		if (shapeType != null) {
			switch (shapeType) {
			case POINT:
				res = new edu.stanford.hakan.aim4api.base.TwoDimensionPoint();
				break;
			case LINE:
			case OPENPOLY:
				res = new edu.stanford.hakan.aim4api.base.TwoDimensionMultiPoint();
				break;
			case POLY:
			case RECTANGLE:
				res = new edu.stanford.hakan.aim4api.base.TwoDimensionPolyline();
				break;
			case SPLINE:
				res = new edu.stanford.hakan.aim4api.base.TwoDimensionSpline();
				break;
			case CIRCLE:
				res = new edu.stanford.hakan.aim4api.base.TwoDimensionCircle();
				break;
			case NORMAL:
				res = new edu.stanford.hakan.aim4api.base.TwoDimensionEllipse();
				break;
			default:
				res=new edu.stanford.hakan.aim4api.base.TwoDimensionMultiPoint();
				break;
			}
		}else{
			res=new edu.stanford.hakan.aim4api.base.TwoDimensionMultiPoint();
		}
		res.setUniqueIdentifier();
		res.setShapeIdentifier(1);
		res.setIncludeFlag(true);
		TwoDimensionSpatialCoordinateCollection cc=new TwoDimensionSpatialCoordinateCollection();
		for (int i = 0; i < pointsPX.length; i++) {
			TwoDimensionSpatialCoordinate c=new TwoDimensionSpatialCoordinate();
			log.info(i+". point index="+pointsPX[i][0]+ " x="+pointsPX[i][1]+ " y="+pointsPX[i][2]);
			c.setCoordinateIndex((int)pointsPX[i][0]);
			c.setX(pointsPX[i][1]);
			c.setY(pointsPX[i][2]);
			cc.addTwoDimensionSpatialCoordinate(c);
		}
		res.setTwoDimensionSpatialCoordinateCollection(cc);
		ia.addMarkupEntity(res);
		return ia;
	}

	/**
	 * create the line length Calculation entity using information from pf. calculates the longest line in the roi and adds that as length calculation
	 * @param ia
	 * @param pointsPX
	 * @param spacingVector
	 * @return
	 */
	private static edu.stanford.hakan.aim4api.base.ImageAnnotation addLengthCalculationFromPointsPX(edu.stanford.hakan.aim4api.base.ImageAnnotation ia,double [][] pointsPX, double[] spacingVector) {

		//calculate the longest line in the closed shape 
		double[] majorAxis=getMajorAxis(pointsPX, spacingVector);
		//add length calculation for major axis
		ia.addCalculationEntity(addLengthCalculation(majorAxis[2],1,"cm"));

		return ia;
	}
	//values from edu.stanford.hakan.aim4api.project.epad.Aim to keep the same for default
	private static final String LINE_LENGTH = "LineLength";
	private static final String VERSION = "1.0";
	private static final String PRIVATE_DESIGNATOR = "private";
	private static final String MEAN = "Mean"; 
	private static final String AREA = "Area";
	private static final String STD_DEV = "Standard Deviation";
	private static final String MIN = "Minimum";
	private static final String MAX = "Maximum";
	private static final String VOLUME = "Volume";

	/**
	 * the code retrieved from edu.stanford.hakan.aim4api.project.epad.Aim and converted to aim4 classes
	 * @param length
	 * @return
	 */

	public static CalculationEntity addMeanCalculation(double value, Integer shapeId, String units) {
		return addCalculation(String.valueOf(value),shapeId,units,MEAN, "R-00317");
	}
	public static CalculationEntity addAreaCalculation(double value, Integer shapeId, String units) {
		return addCalculation(String.valueOf(value),shapeId,units,AREA, "99EPADA4");
	}
	public static CalculationEntity addStdDevCalculation(double value, Integer shapeId, String units) {
		return addCalculation(String.valueOf(value),shapeId,units,STD_DEV, "R-10047");
	}
	public static CalculationEntity addMinCalculation(double value, Integer shapeId, String units) {
		return addCalculation(String.valueOf(value),shapeId,units,MIN, "R-404FB");
	}
	public static CalculationEntity addMaxCalculation(double value, Integer shapeId, String units) {
		return addCalculation(String.valueOf(value),shapeId,units,MAX, "G-A437");
	}
	public static CalculationEntity addLengthCalculation(double value, Integer shapeId, String units) {
		return addCalculation(String.valueOf(value),shapeId,units,LINE_LENGTH, "G-D7FE");
	}
	public static CalculationEntity addVolumeCalculation(double value, Integer shapeId, String units) {
		return addCalculation(String.valueOf(value),shapeId,units,VOLUME, "RID28668");
	}

	public static CalculationEntity addCalculation(String value, Integer shapeId, String units, String name, String code) {

		CalculationEntity cal =new CalculationEntity();
		cal.setUniqueIdentifier();
		CD calcCD= edu.stanford.hakan.aim4api.compability.aimv3.Lexicon.getInstance().get(code);
		String desc="";
		if (calcCD!=null || ((calcCD=edu.stanford.epad.common.util.Lexicon.getInstance().getLex(name))!=null)) {
			cal.addTypeCode(new CD(calcCD.getCode(),calcCD.getDisplayName().getValue(),calcCD.getCodeSystemName()));
			cal.setDescription(new ST(calcCD.getDisplayName().getValue()));
			desc=calcCD.getDisplayName().getValue();
		}else {

			cal.addTypeCode(new CD(name,name,PRIVATE_DESIGNATOR));
			cal.setDescription(new ST(name));
			desc=name;

		}
		ExtendedCalculationResult calculationResult=new ExtendedCalculationResult();

		calculationResult.setType(Enumerations.CalculationResultIdentifier.Scalar);
		calculationResult.setUnitOfMeasure(new ST(units));
		if (units.equals(""))
			calculationResult.setDataType(new CD("99EPADD2","String","99EPAD"));
		else
			calculationResult.setDataType(new CD("99EPADD1","Double","99EPAD"));

		// Create a CalculationData instance
		edu.stanford.hakan.aim4api.base.CalculationData calculationData = new edu.stanford.hakan.aim4api.base.CalculationData();
		calculationData.setValue(new ST(value));
		calculationData.addCoordinate(0, 0);

		// Create a Dimension instance
		edu.stanford.hakan.aim4api.base.Dimension dimension = new edu.stanford.hakan.aim4api.base.Dimension(0, 1, desc);

		// Add calculationData to calculationResult
		calculationResult.addCalculationData(calculationData);

		// Add dimension to calculationResult
		calculationResult.addDimension(dimension);

		//this should be rdf removing for now. do not have shape id. and do not see it in the recist aim.
		//                    // add the shape reference to the calculation
		//                    ReferencedGeometricShape reference = new ReferencedGeometricShape();
		//                    reference.setCagridId(0);
		//                    reference.setReferencedShapeIdentifier(shapeId);
		//                    calculation.addReferencedGeometricShape(reference);

		// Add calculationResult to calculation
		cal.addCalculationResult(calculationResult);

		Algorithm alg=new Algorithm();
		alg.setName(new ST(desc));
		alg.setVersion(new ST(VERSION));
		ArrayList<CD> types=new ArrayList<>();
		types.add(new CD("RID12780","Calculation","RadLex","3.2"));
		alg.setType(types);
		cal.setAlgorithm(alg);

		return cal;

	}

	/**************** math functions ********************/

	/**
	 * calculate the longest line in the close shape
	 * for each point pi calculate distance to all other pj
	 * 	get max dist from pi
	 * get max dist
	 * @param points
	 * @param spacing
	 * @return a vector identifying the major axis point indexes and the line length in cm. 
	 * [3,5,2.23] meaning from point 3 to point 5, length is 2.23cm
	 */
	private static double[] getMajorAxis(double[][] points, double[] spacing) {
		double[][] maxDists=new double[points.length][2]; //matrix holding the index of the farthest point and the distance to that point for the each point
		double[] majorAxis=null; //a vector identifying the major axis point indexes and the line length in cm. [3,5,2.23]
		for (int i=0;i<points.length; i++) {
			double maxDist=-1;
			int point=-1;
			for (int j=i+1;j<points.length; j++) {
				double dist=calculateLineLength(points[i][1],points[i][2],points[j][1],points[j][2],spacing[0],spacing[1]);
				log.info("distance from "+i+" to "+j+ " is "+ dist);
				if (dist>maxDist) {
					maxDist=dist;
					point=j;
				}
			}	
			maxDists[i][0]=point;
			maxDists[i][1]=maxDist;

		}
		//get the maximum of maximums
		double maxDist=-1;
		int point=-1;
		for (int i=0;i<maxDists.length; i++) {
			log.info("max distance from "+i +" is "+ maxDists[i][1] + " and to "+ maxDists[i][0]);
			if (maxDists[i][1]>maxDist) {
				maxDist=maxDists[i][1];
				point=i;
			}
		}
		log.info("max distance is between point "+point+ "("+points[point][1]+":"+points[point][2]+") and " + maxDists[point][0] +"("+points[(int)maxDists[point][0]][1]+":"+points[(int)maxDists[point][0]][2]+") and is "+ maxDists[point][1] +" cm");
		majorAxis=new double[]{point,maxDists[point][0],maxDists[point][1]};
		return majorAxis;

	}

	/**
	 * calculate line length code from edu.stanford.hakan.aim4api.project.epad.Aim. modified to get the actual coordinates
	 * possible problem. just uses pixelSpacingX
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param pixelSpacingX
	 * @param pixelSpacingY
	 * @return
	 */
	private static double calculateLineLength(double x1, double y1, double x2, double y2,
			double pixelSpacingX, double pixelSpacingY) {
		double length = Math.abs(x1
				- x2);
		double width = Math
				.abs(y1 - y2);
		return Math.sqrt(length * length + width * width) * pixelSpacingX
				/ 10.0;
	}

	/**
	 * transpose a matrix
	 * @param m
	 * @return
	 */
	public static double[][] transposeMatrix(double [][] m){
		double[][] temp = new double[m[0].length][m.length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				temp[j][i] = m[i][j];
		return temp;
	}

	/**
	 * invert matrix code from http://www.sanfoundry.com/java-program-find-inverse-matrix/
	 * @param a
	 * @return
	 */
	public static double[][] invert(double a[][]) 
	{
		int n = a.length;
		double x[][] = new double[n][n];
		double b[][] = new double[n][n];
		int index[] = new int[n];
		for (int i=0; i<n; ++i) 
			b[i][i] = 1;

		// Transform the matrix into an upper triangle
		gaussian(a, index);

		// Update the matrix b[i][j] with the ratios stored
		for (int i=0; i<n-1; ++i)
			for (int j=i+1; j<n; ++j)
				for (int k=0; k<n; ++k)
					b[index[j]][k]
							-= a[index[j]][i]*b[index[i]][k];

		// Perform backward substitutions
		for (int i=0; i<n; ++i) 
		{
			x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
			for (int j=n-2; j>=0; --j) 
			{
				x[j][i] = b[index[j]][i];
				for (int k=j+1; k<n; ++k) 
				{
					x[j][i] -= a[index[j]][k]*x[k][i];
				}
				x[j][i] /= a[index[j]][j];
			}
		}
		return x;
	}

	// Method to carry out the partial-pivoting Gaussian
	// elimination.  Here index[] stores pivoting order.

	public static void gaussian(double a[][], int index[]) 
	{
		int n = index.length;
		double c[] = new double[n];

		// Initialize the index
		for (int i=0; i<n; ++i) 
			index[i] = i;

		// Find the rescaling factors, one from each row
		for (int i=0; i<n; ++i) 
		{
			double c1 = 0;
			for (int j=0; j<n; ++j) 
			{
				double c0 = Math.abs(a[i][j]);
				if (c0 > c1) c1 = c0;
			}
			c[i] = c1;
		}

		// Search the pivoting element from each column
		int k = 0;
		for (int j=0; j<n-1; ++j) 
		{
			double pi1 = 0;
			for (int i=j; i<n; ++i) 
			{
				double pi0 = Math.abs(a[index[i]][j]);
				pi0 /= c[index[i]];
				if (pi0 > pi1) 
				{
					pi1 = pi0;
					k = i;
				}
			}

			// Interchange rows according to the pivoting order
			int itmp = index[j];
			index[j] = index[k];
			index[k] = itmp;
			for (int i=j+1; i<n; ++i) 	
			{
				double pj = a[index[i]][j]/a[index[j]][j];

				// Record pivoting ratios below the diagonal
				a[index[i]][j] = pj;

				// Modify other elements accordingly
				for (int l=j+1; l<n; ++l)
					a[index[i]][l] -= pj*a[index[j]][l];
			}
		}
	}


	/**
	 * subtracts second vector from the first
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double[] substractVector(double [] v1, double[] v2){
		if (v1.length!=v2.length){
			log.warning("vector sizes should be the same");
			return null;
		}
		double[] temp = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			temp[i] = v1[i]-v2[i];
		return temp;
	}

	/**
	 * adds two input vectors
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double[] addVector(double [] v1, double[] v2){
		if (v1.length!=v2.length){
			log.warning("vector sizes should be the same");
			return null;
		}
		double[] temp = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			temp[i] = v1[i]+v2[i];
		return temp;
	}

	/**
	 * multiplies a matrix and a vector
	 * @param m
	 * @param v
	 * @return
	 */
	public static double[] multiply(double [][] m, double[] v){
		if (m[0].length!=v.length){
			log.warning("sizes not compatible for multiplying");
			return null;
		}
		double[] temp = new double[m.length];
		for (int i = 0; i < m.length; i++){
			temp[i]=0;
			for (int j = 0; j < m[0].length; j++)
				temp[i] += m[i][j]*v[j];
		}
		return temp;
	}
	/**************** end of math functions ********************/

	private static double[] index2world(double[] point,double[][] transformMatrix, double[] originVectorTrasform){
		double[] newPoint = addVector(multiply(transformMatrix, point),originVectorTrasform);
		log.info("index2world transformed point from "+point[0]+" "+point[1]+ " "+point[2]+ " to "+newPoint[0]+" "+newPoint[1]+ " "+newPoint[2]);
		return newPoint;
	}

	private static double[] world2index(double[] point,double[][] transformMatrix, double[] originVectorTrasform){
		double[][] transformMatrixTranspose=invert(transformMatrix);
		double[] newPoint = multiply(transformMatrixTranspose, substractVector(point, originVectorTrasform));
		log.info("world2index transformed point from "+point[0]+" "+point[1]+ " "+point[2]+ " to "+newPoint[0]+" "+newPoint[1]+ " "+newPoint[2]);
		return newPoint;

	}

	/**
	 * transform points from MM space to PX space using geometry from pf file
	 * @param pointsMM
	 * @param transformMatrix
	 * @param originVectorTrasform
	 * @param boundsMatrix
	 * @param spacingVector
	 * @param originVector
	 * @return
	 */
	private static double[][] transformPoints(double[][] pointsMM, double[][] transformMatrix, double[] originVectorTrasform, double[][] boundsMatrix,
			double[] spacingVector, double[] originVector) {
		//asked imon. she said just multiply with the transformation matrix and add transformation origin vector
		double[][] pointsPX=new double[pointsMM.length][3];
		//		double[][] transformMatrixTranspose=transposeMatrix(transformMatrix);
		for (int i=0;i<pointsMM.length;i++) {
			pointsPX[i][0]=pointsMM[i][0];
			double[] point=new double[]{pointsMM[i][1],pointsMM[i][2],1.0};
			//			double[] w2i=world2index(point, transformMatrix, originVectorTrasform);
			//			//go from index to world. then world to index
			//			double[] transformedPoint=world2index(index2world(point, transformMatrix, originVectorTrasform), transformMatrix, originVectorTrasform);
			//			log.info("tests");
			//			double[] pointW=new double[]{56.66,64.69,-162.74};
			//			double[] pointP=world2index(pointW, transformMatrix, originVectorTrasform);
			//			double[] pointP2=new double[]{387,360,146};
			//			double[] pointW2=index2world(pointP2, transformMatrix, originVectorTrasform);

			pointsPX[i][1]=pointsMM[i][1]*(1.0/(getExtentInMM(0, boundsMatrix,transformMatrix)/getExtent(0, boundsMatrix)));
			pointsPX[i][2]=pointsMM[i][2]*(1.0/(getExtentInMM(1, boundsMatrix,transformMatrix)/getExtent(1, boundsMatrix)));
			//			if (pointsPX[i][1]<0) 
			//				pointsPX[i][1] = boundsMatrix[0][1] +pointsPX[i][1];
			//if (pointsPX[i][2]<0) 
			//they start from the bottom left of image, we start from top
			pointsPX[i][2] = boundsMatrix[1][1] -pointsPX[i][2];

			log.info("point is"+pointsPX[i][1]+" "+pointsPX[i][2] );

			//			pointsPX[i][1]=transformedPoint[0];
			//			pointsPX[i][2]=transformedPoint[1];

		}

		return pointsPX;
	}

	private static double getExtent(int direction, double[][] bounds){
		return bounds[direction][1]-bounds[direction][0];
	}
	private static double getExtentInMM(int direction, double[][] bounds, double[][] transformMatrix){
		double magnitude=Math.sqrt(Math.pow(transformMatrix[0][direction], 2)+Math.pow(transformMatrix[1][direction], 2)+Math.pow(transformMatrix[2][direction], 2));
		return getExtent(direction,bounds)*magnitude;
	}

	/**
	 * puts 19000101000000 if null or empty
	 * @param d
	 * @return
	 */
	public static String formatPatientBirthDate(String d) {
		if (d==null) d="";
		String date = ((d.length() >= 4) ? d.substring(0, 4) : "1900") 
				+ ((d.length() >= 6) ? d.substring(4, 6) : "01") 
				+ ((d.length() >= 8) ? d.substring(6, 8) : "01") + "000000";
		return date;

	}


	/**************************Osirix******************************/
	/**
	 * migrate an osirix file to actual aim files and save to the project. produces multiple aims. handles the parsing
	 * @param osirixJson
	 * @param projectID
	 * @param username
	 * @param templateCode
	 */
	public static void migrateAimFromOsirixJson(File osirixFile, String projectID, String username, String templateCode) {

		try{

			String fileContent=EPADFileUtils.readFileAsString(osirixFile);
			JSONObject osirixJson = HandlerUtil.parsePListFile(fileContent);
			ArrayList<String> aimXMLs= createAimsFromOsirixJson(osirixJson, username, templateCode);
			for(String aimXML:aimXMLs)
				saveAim(aimXML, projectID, username);

		} catch(Exception e) {
			log.warning("Mint to Aim migration is unsuccessful", e);
		}

	}

	/**
	 * migrate an osirix formatted json to actual aim files and save to the project. produces multiple aims
	 * @param osirixJson
	 * @param projectID
	 * @param username
	 * @param templateCode
	 */
	public static void migrateAimFromOsirixJson(JSONObject osirixJson, String projectID, String username, String templateCode) {

		try{
			ArrayList<String> aimXMLs= createAimsFromOsirixJson(osirixJson, username, templateCode);
			for(String aimXML:aimXMLs)
				saveAim(aimXML, projectID, username);

		} catch(Exception e) {
			log.warning("Mint to Aim migration is unsuccessful", e);
		}

	}

	/**
	 * creates multiple aims from the osirix json.
	 * @param osirixJson
	 * @param username
	 * @param templateCode
	 * @return an arraylist of aim xmls
	 * @throws Exception
	 */
	public static ArrayList<String> createAimsFromOsirixJson(JSONObject osirixJson, String username, String templateCode) throws Exception {
		ArrayList<String> aims=new ArrayList<String>();
		JSONArray images=(JSONArray)osirixJson.get("Images");
		for (int i=0;i<images.length(); i++) {

			JSONArray rois=images.getJSONObject(i).getJSONArray("ROIs");
			for (int j=0;j<rois.length(); j++) {
				log.info("processing roi "+ j);
				aims.add(createAimFromOsirixLesionJson(rois.getJSONObject(j), username, templateCode));
			}
		}

		return aims;
	}

	/**
	 * creates an annotation for each roi in the osirix json
	 * @param osirixLesionJson
	 * @param username
	 * @param templateCode
	 * @return aim xml
	 * @throws Exception
	 */
	public static String createAimFromOsirixLesionJson(JSONObject osirixLesionJson, String username, String templateCode) throws Exception {

		String lesionName=osirixLesionJson.getString("Name");
		String comment="";//mintJson.getString("comment");
		String sopClassUID="na",studyDate="na",studyTime="na", pName="na",pId="na",pBirthDate="na",pSex="na", studyUID="na", sourceSeriesUID="na";
		String accessionNumber="";
		String imageUID=osirixLesionJson.optString("SOPInstanceUID");
		if (imageUID!=null && !imageUID.equals("")) {
			log.info("Retrieved image uid is "+imageUID);

			//fill the missing information from dicom tags
			DICOMElementList tags= Dcm4CheeQueries.getDICOMElementsFromWADO("*", "*", imageUID);
			if (tags==null) {
				log.warning("Dicom image couldn't be retrieved. Cannot get the necessary information!");
			}
			else {
				log.info("study code:"+PixelMedUtils.StudyInstanceUIDCode + " series code:"+PixelMedUtils.SeriesInstanceUIDCode);
				for (int i=0; i< tags.ResultSet.totalRecords; i++) {
					DICOMElement tag=tags.ResultSet.Result.get(i);

					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.SOPClassUIDCode)) 
						sopClassUID=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.StudyDateCode)) 
						studyDate=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.StudyTimeCode)) 
						studyTime=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.AccessionNumberCode)) 
						accessionNumber=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.PatientNameCode)) 
						pName=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.PatientIDCode)) 
						pId=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.PatientBirthDateCode)) 
						pBirthDate=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.PatientSexCode)) 
						pSex=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.StudyInstanceUIDCode)) 
						studyUID=tag.value;
					if (tag.tagCode.equalsIgnoreCase(PixelMedUtils.SeriesInstanceUIDCode)) 
						sourceSeriesUID=tag.value;
				}
			}

		}else { //imageInstanceUID not present read the available data from osirixjson.
			log.warning("cannot handle having no image in epad! will put na in most of the data");
			imageUID=osirixLesionJson.optString("SOPInstanceUID");
			studyUID=osirixLesionJson.optString("StudyInstanceUID");
			sourceSeriesUID=osirixLesionJson.optString("SeriesInstanceUID");			
		}

		log.info("the values retrieved are "+ sopClassUID+" "+studyDate+" "+studyTime+" "+pName+" "+pId+" "+pBirthDate+" "+pSex+" "+studyUID+" "+sourceSeriesUID+" ");
		ImageAnnotationCollection iac = createImageAnnotationColectionFromProperties(username, pName, pId, pBirthDate, pSex);
		edu.stanford.hakan.aim4api.base.ImageAnnotation ia=createImageAnnotationFromProperties(username, templateCode, lesionName, comment, imageUID, sopClassUID, studyDate, studyTime, studyUID, sourceSeriesUID, accessionNumber);

		//create markup entity
		double [][] pointsPX=extractPointsFromJsonArray(osirixLesionJson.getJSONArray("Point_px"));
		ShapeType shapeType=ShapeType.POLY;
		switch(osirixLesionJson.getInt("Type")){
		//		tLength,//5            LINE
		//		tROIBox,//6            
		//		t3DRotate,//7
		//		tCross,//8            NORMAL
		//		tOval,//9            SPLINE
		//		tOPolygon,//10        OPENPOLY
		//		tCPolygon,//11        POLY
		//		tAngle ,//12
		//		tText,//13
		//		tArrow,//14         LINE
		//		tPencil,//15
		//		t3Dpoint,//16
		//		t3DCut,//17
		//		tCamera3D,//18
		//		t2DPoint,//19         POINT
		//		tPlain,//20
		//		tBonesRemoval,//21
		//		tWLBlended,//  22
		//		tRepulsor,//  23
		//		tLayerROI,//24
		//		tROISelector,//25
		//		tAxis,//26
		//		tDynAngle,//27
		//		tCurvedROI,//28            SPLINE
		//		tTAGT,                      //  29
		//		tBall,                      //  30
		//		tOvalAngle,                 //  31
		case 6://		tROIBox
			shapeType=ShapeType.RECTANGLE;
			break;
		case 9: //		tOval
		case 28://		tCurvedROI
		case 20://  	tPlain
		case 11://		tCPolygon
			shapeType=ShapeType.SPLINE;
			break;
		case 14://		tArrow
		case 5: //		tLength
			shapeType=ShapeType.LINE;
			break;
		case 10://mouth 	tOPolygon
			shapeType=ShapeType.OPENPOLY;
			break;
		case 8: //		tCross
			shapeType=ShapeType.NORMAL;
			break;
		case 19://		t2DPoint
			shapeType=ShapeType.POINT;
			break;


		}
		ia=addMarkupFromPointsPX(ia, pointsPX,shapeType);

		//add the calculations. the calculations in osirix xml are:
		//AreaCm2, AreaPix2, Center, Dev (std dev), LengthCm, LengthPix, Max, Mean, Min, Total
		//we have definitions for these: AreaCm2,  Dev (std dev), LengthCm, Max, Mean, Min
		//we can put osirixLesionJson.getInt("IndexInImage") as the shape identifier but we save each shape to separate aim. I am  putting 1 in all
		//put the calculation onlt if it is different than 0. osirix puts those fields even if they are empty. (like area for a line)
		if (osirixLesionJson.getDouble("AreaCm2")!=0) ia.addCalculationEntity(addAreaCalculation(osirixLesionJson.getDouble("AreaCm2") ,1,"cm2"));
		if (osirixLesionJson.getDouble("Dev")!=0) ia.addCalculationEntity(addStdDevCalculation(osirixLesionJson.getDouble("Dev") ,1,"linear"));
		if (osirixLesionJson.getDouble("LengthCm")!=0) ia.addCalculationEntity(addLengthCalculation(osirixLesionJson.getDouble("LengthCm") ,1,"cm"));
		if (osirixLesionJson.getDouble("Max")!=0) ia.addCalculationEntity(addMaxCalculation(osirixLesionJson.getDouble("Max") ,1,"linear"));
		if (osirixLesionJson.getDouble("Mean")!=0) ia.addCalculationEntity(addMeanCalculation(osirixLesionJson.getDouble("Mean") ,1,"linear"));
		if (osirixLesionJson.getDouble("Min")!=0) ia.addCalculationEntity(addMinCalculation(osirixLesionJson.getDouble("Min") ,1,"linear"));


		iac.addImageAnnotation(ia);

		log.info("annotation is: "+iac.toStringXML());
		return iac.toStringXML();
	}




	/**
	 * extract points in {index,x,y} format
	 * @param jsonPoints
	 * @return
	 */
	private static double[][] extractPointsFromJsonArray(JSONArray jsonPoints) {
		double[][] points=new double[jsonPoints.length()][3];
		for (int i = 0; i < jsonPoints.length(); i++) {
			JSONArray point=jsonPoints.getJSONArray(i);
			points[i][0]=i;
			points[i][1]=point.getDouble(0);
			points[i][2]=point.getDouble(1);

		}
		return points;
	}
}
