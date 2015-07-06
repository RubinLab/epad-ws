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
	private static final String FILE_TYPE_MATCH_PARAMETER_NAME = "fileType";

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

			log.debug("HasAnnotationMatch:" + searchFilter.hasAnnotationMatch());
			log.debug("HasAnnotations" + searchFilter.hasAnnotationsAnnotationMatch());
			log.debug("HasNoAnnotations" + searchFilter.hasNoAnnotationsAnnotationMatch());
		}

		if (httpRequest.getParameter(FILE_TYPE_MATCH_PARAMETER_NAME) != null)
			searchFilter.setFileTypeMatch(httpRequest.getParameter(FILE_TYPE_MATCH_PARAMETER_NAME));

		log.debug("patientName:" + searchFilter.getPatientNameMatch() + " patientID:" + searchFilter.getPatientIDMatch() + " modality:" + searchFilter.getModalityMatch() + " accession:" + searchFilter.getAccessionNumberMatch());
		return searchFilter;
	}
}
