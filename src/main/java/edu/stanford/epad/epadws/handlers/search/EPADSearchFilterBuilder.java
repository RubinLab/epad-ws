package edu.stanford.epad.epadws.handlers.search;

import javax.servlet.http.HttpServletRequest;

public class EPADSearchFilterBuilder
{
	private static final String PROJECT_NAME_MATCH_PARAMETER_NAME = "projectNameMatch";
	private static final String PATIENT_NAME_MATCH_PARAMETER_NAME = "patientNameMatch";
	private static final String PATIENT_ID_MATCH_PARAMETER_NAME = "patientIDMatch";
	private static final String ACCESSION_NUMBER_MATCH_PARAMETER_NAME = "accessionNumberMatch";
	private static final String MODALITY_MATCH_PARAMETER_NAME = "modalityMatch";
	private static final String INSERT_DATE_START_MATCH_PARAMETER_NAME = "insertDateStartMatch";
	private static final String INSERT_DATE_FINISH_MATCH_PARAMETER_NAME = "insertDateFinishMatch";
	private static final String STUDY_DATE_START_MATCH_PARAMETER_NAME = "studyDateStartMatch";
	private static final String STUDY_DATE_FINISH_MATCH_PARAMETER_NAME = "studyDateFinishMatch";
	private static final String ANNOTATION_MATCH_PARAMETER_NAME = "annotationMatch";
	private static final String ANNOTATION_MATCH_HAS_ANNOTATIONS_PARAMETER_VALUE = "hasAnnotations";
	private static final String ANNOTATION_MATCH_HAS_NO_ANNOTATIONS_PARAMETER_VALUE = "hasNoAnnotations";

	public static EPADSearchFilter build(HttpServletRequest httpRequest)
	{
		EPADSearchFilter searchFilter = new EPADSearchFilter();

		if (httpRequest.getParameter(PROJECT_NAME_MATCH_PARAMETER_NAME) != null)
			searchFilter.setProjectNameMatch(httpRequest.getParameter(PROJECT_NAME_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(PATIENT_NAME_MATCH_PARAMETER_NAME) != null)
			searchFilter.setPatientNameMatch(httpRequest.getParameter(PATIENT_NAME_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(PATIENT_ID_MATCH_PARAMETER_NAME) != null)
			searchFilter.setPatientIDMatch(httpRequest.getParameter(PATIENT_ID_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(ACCESSION_NUMBER_MATCH_PARAMETER_NAME) != null)
			searchFilter.setAccessionNumberMatch(httpRequest.getParameter(ACCESSION_NUMBER_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(MODALITY_MATCH_PARAMETER_NAME) != null)
			searchFilter.setModalityMatch(httpRequest.getParameter(MODALITY_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(INSERT_DATE_START_MATCH_PARAMETER_NAME) != null)
			searchFilter.setInsertDateStartMatch(httpRequest.getParameter(INSERT_DATE_START_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(INSERT_DATE_FINISH_MATCH_PARAMETER_NAME) != null)
			searchFilter.setInsertDateFinishMatch(httpRequest.getParameter(INSERT_DATE_FINISH_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(STUDY_DATE_START_MATCH_PARAMETER_NAME) != null)
			searchFilter.setStudyDateStartMatch(httpRequest.getParameter(STUDY_DATE_START_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(STUDY_DATE_FINISH_MATCH_PARAMETER_NAME) != null)
			searchFilter.setStudyDateFinishMatch(httpRequest.getParameter(STUDY_DATE_FINISH_MATCH_PARAMETER_NAME));

		if (httpRequest.getParameter(ANNOTATION_MATCH_PARAMETER_NAME) != null) {
			String paramaterValue = httpRequest.getParameter(ANNOTATION_MATCH_PARAMETER_NAME);

			if (paramaterValue.equalsIgnoreCase(ANNOTATION_MATCH_HAS_ANNOTATIONS_PARAMETER_VALUE))
				searchFilter.setHasAnnotationsAnnotationMatch();
			else if (paramaterValue.equalsIgnoreCase(ANNOTATION_MATCH_HAS_NO_ANNOTATIONS_PARAMETER_VALUE))
				searchFilter.setHasNoAnnotationsAnnotationMatch();
			else
				throw new IllegalArgumentException("Invalid " + ANNOTATION_MATCH_HAS_ANNOTATIONS_PARAMETER_VALUE + " parameter");

		}

		return searchFilter;
	}
}
