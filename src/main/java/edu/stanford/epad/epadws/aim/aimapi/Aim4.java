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
package edu.stanford.epad.epadws.aim.aimapi;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import edu.stanford.epad.epadws.aim.Aimapi;
import edu.stanford.hakan.aim4api.base.ImageAnnotationCollection;
import edu.stanford.hakan.aim4api.compability.aimv3.AnatomicEntity;
import edu.stanford.hakan.aim4api.compability.aimv3.DICOMImageReference;
import edu.stanford.hakan.aim4api.compability.aimv3.Equipment;
import edu.stanford.hakan.aim4api.compability.aimv3.Image;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageReference;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageSeries;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageStudy;
import edu.stanford.hakan.aim4api.compability.aimv3.Person;
import edu.stanford.hakan.aim4api.compability.aimv3.Segmentation;
import edu.stanford.hakan.aim4api.compability.aimv3.User;

/**
 * 
 * @author debra willrett
 * 
 *         Aim wraps ImageAnnotation and calls methods: addGeometricShape,
 *         addImageReference, addPerson, addSegmentation, addTextAnnotation,
 *         setName, setDateTime, setCagridId, setComment, addUser,createUser, ,
 *         addEquipment, createEquipment, addImageReference,
 *         createImageReference, addAnatomicEntity, createAnatomicEntity,
 *         addCalculation, setCagridId, setCodingSchemeDesignator,
 *         setCodeMeaning,setCodeValue(result.getCodeValue());
 * 
 */
@SuppressWarnings("serial")
public class Aim4 extends ImageAnnotation implements Serializable, Aimapi {

	private static final Logger logger = Logger.getLogger("Aim");

	private int caGridId = 0;
	String DSO_SOP_CLASSUID = "1.2.840.10008.5.1.4.1.1.66.4";

	public Aim4() {
	}
	
	public Aim4(ImageAnnotationCollection iac)
	{
		super(iac);
	}

	// create the aim from the imageAnnotation
	public Aim4(ImageAnnotation ia) {

		super();

		setListUser(ia.getListUser());
		setListEquipment(ia.getListEquipment());
		setListAimStatus(ia.getListAimStatus());
		setCagridId(ia.getCagridId());
		setAimVersion(ia.getAimVersion(), "al536anhb55555");
		setComment(ia.getComment());
//		setDateTime(ia.getDateTime());
		//ml datetime change for clunie
		logger.warning("date is:" + ia.getDateTime());
		String date=ia.getDateTime();
		if (!ia.getDateTime().contains("-")) {//new format change to old
			if (date.length()==14)
				date=date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8)+"T"+date.substring(8,10)+":"+date.substring(10,12)+":"+date.substring(12,14);
			else
				date=date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
				
		}
		setDateTime(date);
		setName(ia.getName());
		setUniqueIdentifier(ia.getUniqueIdentifier(), "al536anhb55555");
		setCodeValue(ia.getCodeValue());
		setCodeMeaning(ia.getCodeMeaning());
		setCodingSchemeDesignator(ia.getCodingSchemeDesignator());
		setCodingSchemeVersion(ia.getCodingSchemeVersion());
		setPrecedentReferencedAnnotationUID(ia
				.getPrecedentReferencedAnnotationUID());
		setXsiType(ia.getXsiType());
		setOntologyPrefix(ia.getOntologyPrefix());

		setInferenceCollection(ia.getInferenceCollection());
		setAnatomicEntityCollection(ia.getAnatomicEntityCollection());
		setImagingObservationCollection(ia.getImagingObservationCollection());

		setCalculationCollection(ia.getCalculationCollection());
		setSegmentationCollection(ia.getSegmentationCollection());
		setImageReferenceCollection(ia.getImageReferenceCollection());
		setGeometricShapeCollection(ia.getGeometricShapeCollection());
		setTextAnnotationCollection(ia.getTextAnnotationCollection());
		setListPerson(ia.getListPerson());

	}

	// create a default name for an annotation
	private String todaysDate() {
		Date today = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return fmt.format(today);
	}

	// create a comment field
	private String fillComment(String modality, String description, int image) {
		return modality + " / " + description + " / " + image;
	}

	// create a person object for this aim
	private Person createPerson(String name, String id, String sex,
			String birthdate) {

		Person person = new Person();
		person.setBirthDate(birthdate);
		person.setCagridId(caGridId);
		person.setName(name);
		person.setId(id);
		person.setSex(sex);
		return person;
	}

	// create the equipment object for this series
	private Equipment createEquipment(String name, String model, String version) {

		Equipment equipment = new Equipment();
		equipment.setCagridId(caGridId);
		equipment.setManufacturerName(name);
		equipment.setManufacturerModelName(model);
		equipment.setSoftwareVersion(version);
		return equipment;
	}

	// update the image reference for the study and series to point to this
	// imageID
	private void updateImageID(String studyID, String seriesID, String imageID) {

		for (ImageReference imageReference : getImageReferenceCollection()
				.getImageReferenceList()) {

			DICOMImageReference dicomImageReference = (DICOMImageReference) imageReference;

			String study = dicomImageReference.getImageStudy().getInstanceUID();
			String series = dicomImageReference.getImageStudy()
					.getImageSeries().getInstanceUID();

			if (study.equals(studyID) && series.equals(seriesID)) {

				Image image = new Image();
				image.setSopInstanceUID(imageID);

				List<Image> images = dicomImageReference.getImageStudy()
						.getImageSeries().getImageCollection().getImageList();
				images.clear();
				images.add(image);

			}
		}

	}

	private AnatomicEntity createAnatomicEntity() {

		AnatomicEntity entity = new AnatomicEntity();
		entity.setAnnotatorConfidence(0.0);
		entity.setCagridId(caGridId);
		entity.setCodeMeaning("background");
		entity.setCodeValue("0");
		entity.setCodingSchemeDesignator("ePAD");
		entity.setLabel("background");
		return entity;
	}

	public boolean hasSeries(String seriesID) {
		boolean result = false;
		try {

			for (ImageReference imageReference : getImageReferenceCollection()
					.getImageReferenceList()) {

				DICOMImageReference dicomImageReference = (DICOMImageReference) imageReference;

				if (dicomImageReference.getImageStudy().getImageSeries()
						.getInstanceUID().equals(seriesID)) {
					result = true;
					break;
				}
			}
		} finally {
		}
		return result;
	}

	
	public boolean hasImage(String imageID) {
		boolean result = false;
		try {

			for (ImageReference imageReference : getImageReferenceCollection()
					.getImageReferenceList()) {

				DICOMImageReference dicomImageReference = (DICOMImageReference) imageReference;

				for (Image image : dicomImageReference.getImageStudy()
						.getImageSeries().getImageCollection().getImageList()) {
					if (image.getSopInstanceUID().equals(imageID)) {
						result = true;
						break;
					}
				}
			}
		} finally {
		}
		return result;
	}

	// get the series id for this image from the image reference
	
	public String getFirstSeriesID() {
		String result = "";

		try {
			List<ImageReference> imageList = getImageReferenceCollection()
					.getImageReferenceList();
			if (imageList.size() > 0) {
				ImageReference imageReference = imageList.get(0);
				DICOMImageReference dicomImageReference = (DICOMImageReference) imageReference;
				ImageStudy imageStudy = dicomImageReference.getImageStudy();
				ImageSeries imageSeries = imageStudy.getImageSeries();
				result = imageSeries.getInstanceUID();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> getSeriesIDs() {
		List<String> results = new ArrayList<String>();

		try {
			List<ImageReference> imageList = getImageReferenceCollection()
					.getImageReferenceList();
			for (ImageReference imageReference: imageList) {
				DICOMImageReference dicomImageReference = (DICOMImageReference) imageReference;
				ImageStudy imageStudy = dicomImageReference.getImageStudy();
				ImageSeries imageSeries = imageStudy.getImageSeries();
				results.add(imageSeries.getInstanceUID());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	
	public String getSeriesID(String imageID) {

		String result = "";
		try {

			// look for the reference to this image
			for (ImageReference reference : getImageReferenceCollection()
					.getImageReferenceList()) {

				if (reference.getXsiType().equals("DICOMImageReference")) {

					DICOMImageReference dicomReference = (DICOMImageReference) reference;
					for (Image image : dicomReference.getImageStudy()
							.getImageSeries().getImageCollection()
							.getImageList()) {

						if (image.getSopInstanceUID().equals(imageID)) {

							result = dicomReference.getImageStudy()
									.getImageSeries().getInstanceUID();
							break;

						}
					}
				}
			}
		} finally {
		}
		return result;
	}

	
	public String getFirstStudyID() {

		String studyID = "";
		try {

			DICOMImageReference reference = (DICOMImageReference) getImageReferenceCollection()
					.getImageReferenceList().get(0);
			studyID = reference.getImageStudy().getInstanceUID();

		} finally {
		}

		return studyID;
	}

	
	public String getStudyID(String seriesID) {

		String result = "";
		try {

			for (ImageReference imageReference : getImageReferenceCollection()
					.getImageReferenceList()) {

				DICOMImageReference dicomImageReference = (DICOMImageReference) imageReference;

				if (dicomImageReference.getImageStudy().getImageSeries()
						.getInstanceUID().equals(seriesID)) {
					result = dicomImageReference.getImageStudy()
							.getInstanceUID();
					break;
				}
			}
		} finally {
		}
		return result;
	}
	
	public Date getFirstStudyDate() {

		try {
			List<ImageReference> imageList = getImageReferenceCollection()
					.getImageReferenceList();

			ImageReference imageReference = imageList.get(0);
			DICOMImageReference dicomImageReference = (DICOMImageReference) imageReference;
			ImageStudy study = dicomImageReference.getImageStudy();

//			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = new Date();
//			date = fmt.parse(study.getStartDate().substring(0, 10));
			//ml
			SimpleDateFormat fmt ;
			Date date = new Date();
			logger.warning("date is:" + study.getStartDate());
			if (study.getStartDate().contains("-")) {
				fmt = new SimpleDateFormat("yyyy-MM-dd");
				date = fmt.parse(study.getStartDate().substring(0, 10));
			}else
			{
				fmt = new SimpleDateFormat("yyyyMMdd");
				date = fmt.parse(study.getStartDate().substring(0, 8));	
			}

			return date;

		} catch (Exception e) {
			logger.info("Error: aimApi.getStudyDate " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	// 
	// public String formatStudyDate() {
	//
	// try {
	// Date studyDate = getStudyDate();
	// SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	// return fmt.format(studyDate);
	// } catch (Exception e) {
	// logger.info("Error: Aimapi.formatStudyDate " + e.getMessage());
	// e.printStackTrace();
	// }
	// return "";
	// }


	
	public String getPatientID() {
		String result = "";
		try {
			return getListPerson().get(0).getId();

		} catch (Exception e) {
			logger.info("Error: Aim getPatientId " + e.getMessage());
		}
		return result;

	}

	 public String getOriginalPatientID() {
	        String result = "";
	        try {
	            result= getListPerson().get(0).getOriginalId();
	            if (result==null)
	            	return getPatientID();

	        } catch (Exception e) {
	            logger.info("Error: Aim getOriginalPatientId " + e.getMessage());
	        }
	        return result;

	    }

	private String getObservationCodeValue() {
		String result = null;

		try {
			result = getImagingObservationCollection()
					.getImagingObservationList().get(0).getCodeValue();
		} catch (Exception e) {
			logger.info("Error: Aim getObservationCodeValue " + e.getMessage());
		}

		return result;

	}

	
	public boolean isBaseline() {

		return (getObservationCodeValue().equals("S81"));

	}

	
	public boolean isFollowUp() {

		return (getObservationCodeValue().equals("S82"));

	}

	
	public boolean isTarget() {

		String value = "";

		try {
			value = getImagingObservationCollection()
					.getImagingObservationList().get(0)
					.getImagingObservationCharacteristicCollection()
					.getImagingObservationCharacteristicList().get(0)
					.getCodeValue();
		} catch (Exception e) {
			logger.info("Error: AimUtils isTarget " + e.getMessage());
		}

		return (value.equalsIgnoreCase("S71"));

	}

	
	public boolean isNonTarget() {

		String value = "";

		try {
			value = getImagingObservationCollection()
					.getImagingObservationList().get(0)
					.getImagingObservationCharacteristicCollection()
					.getImagingObservationCharacteristicList().get(0)
					.getCodeValue();
		} catch (Exception e) {
			logger.info("Error: AimUtils isTarget " + e.getMessage());
		}

		return (value.equalsIgnoreCase("S72"));

	}

	
	public boolean isResolved() {

		String value = "";

		try {
			value = getImagingObservationCollection()
					.getImagingObservationList().get(0)
					.getImagingObservationCharacteristicCollection()
					.getImagingObservationCharacteristicList().get(0)
					.getCodeValue();
		} catch (Exception e) {
			logger.info("Error: AimUtils isResolved " + e.getMessage());
		}

		return (value.equalsIgnoreCase("S74"));
	}

	
	public boolean isNew() {

		String value = "";

		try {
			value = getImagingObservationCollection()
					.getImagingObservationList().get(0)
					.getImagingObservationCharacteristicCollection()
					.getImagingObservationCharacteristicList().get(0)
					.getCodeValue();
		} catch (Exception e) {
			logger.info("Error: AimUtils isNew " + e.getMessage());
		}

		return (value.equalsIgnoreCase("S73"));
	}

	
	// find the lesion status field in the annotation anatomic entity list
	public String getLesionStatus() {

		try {
			Iterator<AnatomicEntity> it = getAnatomicEntityCollection()
					.getAnatomicEntityList().iterator();
			while (it.hasNext()) {
				AnatomicEntity entity = it.next();
				if (entity.getLabel().equals("Status")) {
					return entity.getCodeMeaning();
				}
			}
		} catch (Exception e) {
			logger.info("Error: Aim getLesionStatus " + e.getMessage());
		}

		return "";
	}

	
	public String getLesionLocation() {
		String result = "";

		try {
			result = (((List<?>) getAnatomicEntityCollection()
					.getAnatomicEntityList()).size() > 0 ? getAnatomicEntityCollection()
					.getAnatomicEntityList().get(0).getCodeMeaning()
					: "");
		} catch (Exception e) {
			logger.info("Error: Aim getLesionLocation " + e.getMessage());
		}

		return result;

	}

	
	public String getPatientName() {
		if (getListPerson() != null) {
			if (!getListPerson().isEmpty()) {
				return getListPerson().get(0).getName().replace("^", " ");
			}
		}
		return "";
	}

	
	public String cleanPatientName() {
		return getPatientName().replace("^", " ");
	}

	
	public String getFirstImageID() {
		String result = "";
		try {

			DICOMImageReference dicomImageReference = (DICOMImageReference) getImageReferenceCollection()
					.getImageReferenceList().get(0);
			result = dicomImageReference.getImageStudy().getImageSeries()
					.getImageCollection().getImageList().get(0)
					.getSopInstanceUID();

		} finally {
		}
		return result;

	}

	
	// does this annotation contain an image reference?
	public boolean hasImage() {
		try {
			getImageReferenceCollection().getImageReferenceList().get(0);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// does this annotation contain a segmentation component?
	
	public boolean hasSegmentation() {
		try {
			getSegmentationCollection().getSegmentationList().get(0);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	
	public boolean hasSegmentationImage(String imageID) {
		boolean found = false;
		try {
			for (Segmentation segmentation : getSegmentationCollection()
					.getSegmentationList()) {

				if (segmentation.getSopInstanceUID().equals(imageID)) {
					found = true;
					break;
				}
			}
		} finally {
		}
		return found;

	}

	
	public String getFirstSegmentationImageID() {
		String id = "";

		try {
			id = getSegmentationCollection().getSegmentationList().get(0)
					.getSopInstanceUID();
		} catch (Exception e) {
			logger.info("Error: has no segmentation " + hasSegmentation());
		}
		return id;
	}

	
	public void clearValues() {
		getAnatomicEntityCollection().getAnatomicEntityList().clear();
		getImagingObservationCollection().getImagingObservationList().clear();
		getInferenceCollection().getInferenceList().clear();

	}

	private Person getFirstPerson() {
		return getListPerson().get(0);
	}

	
	public Patient getPatient() {
		return new Patient(getFirstPerson());
	}

	
	public void setPatient(Patient patient) {
		Person person = getFirstPerson();
		person.setName(patient.getName());
		person.setId(patient.getId());
		person.setOriginalId(patient.getOriginalId());
		person.setSex(patient.getSex());
		person.setBirthDate(patient.getBirthDate());

	}

	
	public LoggedInUser getLoggedInUser() {
		LoggedInUser result = null;

		List<User> users = getListUser();
		if (users.size() > 0) {
			User firstUser = users.get(0);
			result = new LoggedInUser(firstUser.getLoginName(),
					firstUser.getName());
		}

		return result;
	}

	
	public void setLoggedInUser(LoggedInUser loggedInUser) {

		// create a new user
		User user = new User();
		user.setLoginName(loggedInUser.getLoginName());
		user.setName(loggedInUser.getName());

		// and a list of users with this user
		ArrayList<User> users = new ArrayList<User>();
		users.add(user);

		// put this one user in the aim
		setListUser(users);
	}



}
