package edu.stanford.epad.epadws.handlers.core;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.epad.common.util.EPADLogger;

/**
 * 
 * 
 * 
 * @author martin
 * 
 * @see EPADSearchFilter
 */
public class EPADSearchFilterBuilder
{
	private static final String PROJECT_NAME_MATCH_PARAMETER_NAME = "projectName";
	private static final String PATIENT_NAME_MATCH_PARAMETER_NAME = "patientName";
	private static final String PATIENT_ID_MATCH_PARAMETER_NAME = "patientID";
	private static final String ACCESSION_NUMBER_MATCH_PARAMETER_NAME = "accessionNumber";
	private static final String MODALITY_MATCH_PARAMETER_NAME = "modality";
	private static final String INSERT_DATE_START_MATCH_PARAMETER_NAME = "insertDateStart";
	private static final String INSERT_DATE_FINISH_MATCH_PARAMETER_NAME = "insertDateFinish";
	private static final String STUDY_DATE_START_MATCH_PARAMETER_NAME = "studyDateStart";
	private static final String STUDY_DATE_FINISH_MATCH_PARAMETER_NAME = "studyDateFinish";
	private static final String ANNOTATION_MATCH_PARAMETER_NAME = "annotation";
	private static final String ANNOTATION_MATCH_HAS_ANNOTATIONS_PARAMETER_VALUE = "hasAnnotations";
	private static final String ANNOTATION_MATCH_HAS_NO_ANNOTATIONS_PARAMETER_VALUE = "hasNoAnnotations";

	private static final EPADLogger log = EPADLogger.getInstance();

	public static EPADSearchFilter build(HttpServletRequest httpRequest)
	{
		EPADSearchFilter searchFilter = new EPADSearchFilter();

		if (httpRequest.getParameter(PROJECT_NAME_MATCH_PARAMETER_NAME) != null) {
			searchFilter.setProjectNameMatch(httpRequest.getParameter(PROJECT_NAME_MATCH_PARAMETER_NAME));
		}

		if (httpRequest.getParameter(PATIENT_NAME_MATCH_PARAMETER_NAME) != null)
			searchFilter.setPatientNameMatch(httpRequest.getParameter(PATIENT_NAME_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(PATIENT_ID_MATCH_PARAMETER_NAME) != null)
			searchFilter.setPatientIDMatch(httpRequest.getParameter(PATIENT_ID_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(ACCESSION_NUMBER_MATCH_PARAMETER_NAME) != null)
			searchFilter.setAccessionNumberMatch(httpRequest.getParameter(ACCESSION_NUMBER_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(MODALITY_MATCH_PARAMETER_NAME) != null) {
			searchFilter.setModalityMatch(httpRequest.getParameter(MODALITY_MATCH_PARAMETER_NAME));
		}

		if (httpRequest.getParameter(INSERT_DATE_START_MATCH_PARAMETER_NAME) != null)
			searchFilter.setInsertDateStartMatch(httpRequest.getParameter(INSERT_DATE_START_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(INSERT_DATE_FINISH_MATCH_PARAMETER_NAME) != null)
			searchFilter.setInsertDateFinishMatch(httpRequest.getParameter(INSERT_DATE_FINISH_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(STUDY_DATE_START_MATCH_PARAMETER_NAME) != null)
			searchFilter.setStudyDateStartMatch(httpRequest.getParameter(STUDY_DATE_START_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(STUDY_DATE_FINISH_MATCH_PARAMETER_NAME) != null)
			searchFilter.setStudyDateFinishMatch(httpRequest.getParameter(STUDY_DATE_FINISH_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(ANNOTATION_MATCH_PARAMETER_NAME) != null) {
			String parameterValue = httpRequest.getParameter(ANNOTATION_MATCH_PARAMETER_NAME);

			if (parameterValue.equalsIgnoreCase(ANNOTATION_MATCH_HAS_ANNOTATIONS_PARAMETER_VALUE))
				searchFilter.setHasAnnotationsAnnotationMatch();
			else if (parameterValue.equalsIgnoreCase(ANNOTATION_MATCH_HAS_NO_ANNOTATIONS_PARAMETER_VALUE))
				searchFilter.setHasNoAnnotationsAnnotationMatch();
			else
				throw new IllegalArgumentException("Invalid " + ANNOTATION_MATCH_PARAMETER_NAME + " filter parameter value");

			log.info("" + searchFilter.hasAnnotationMatch());
			log.info("" + searchFilter.hasAnnotationsAnnotationMatch());
			log.info("" + searchFilter.hasNoAnnotationsAnnotationMatch());
		}

		return searchFilter;
	}
}
