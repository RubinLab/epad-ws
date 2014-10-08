package edu.stanford.epad.epadws.aim.aimapi;

//Copyright (c) 2013 The Board of Trustees of the Leland Stanford Junior University
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
		setDateTime(ia.getDateTime());
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

			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			date = fmt.parse(study.getStartDate().substring(0, 10));

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
