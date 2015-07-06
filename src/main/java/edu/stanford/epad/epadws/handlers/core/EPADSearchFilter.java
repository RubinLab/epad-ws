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

import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;

/**
 * 
 * 
 * 
 * @author martin
 * @see EPADSearchFilterBuilder
 */
public class EPADSearchFilter
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private String projectNameMatch = null;
	private String patientNameMatch = null;
	private String patientIDMatch = null;
	private String accessionNumberMatch = null;
	private String modalityMatch = null;
	private AnnotationMatch annotationMatch = AnnotationMatch.NONE;
	private String insertDateStartMatch = null;
	private String insertDateFinishMatch = null;
	private String studyDateStartMatch = null;
	private String studyDateFinishMatch = null;
	private String fileTypeMatch = null;

	public boolean hasProjectNameMatch()
	{
		return this.projectNameMatch != null;
	}

	public boolean hasPatientNameMatch()
	{
		return this.patientNameMatch != null;
	}

	public boolean hasPatientIDMatch()
	{
		return this.patientIDMatch != null;
	}

	public boolean hasAccessionNumberMatch()
	{
		return this.accessionNumberMatch != null;
	}

	public boolean hasModalityMatch()
	{
		return this.modalityMatch != null;
	}

	public boolean hasAnnotationMatch()
	{
		return this.annotationMatch != null && this.annotationMatch != AnnotationMatch.NONE;
	}

	public String getProjectNameMatch()
	{
		return this.projectNameMatch;
	}

	public void setProjectNameMatch(String projectNameMatch)
	{
		this.projectNameMatch = projectNameMatch;
	}

	public boolean projectNameMatches(String projectName)
	{
		if (this.projectNameMatch == null)
			return true;
		else {
			projectNameMatch = removeAsterisk(projectNameMatch);
			String projectNameRegex = "(?i).*" + projectNameMatch + ".*";
			if (projectName.matches(projectNameRegex))
				return true;
			else
				return false;
		}
	}

	public String getPatientNameMatch()
	{
		return this.patientNameMatch;
	}

	public void setPatientNameMatch(String patientNameMatch)
	{
		this.patientNameMatch = patientNameMatch;
	}

	public boolean patientNameMatches(String patientName)
	{
		if (this.patientNameMatch == null)
			return true;
		else {
			patientNameMatch = removeAsterisk(patientNameMatch);
			String patientNameRegex = "(?i).*" + patientNameMatch.replace('^', ' ') + ".*";
			if (patientName.replace('^', ' ').matches(patientNameRegex))
				return true;
			else
				return false;
		}
	}

	public String getPatientIDMatch()
	{
		return this.patientIDMatch;
	}

	public void setPatientIDMatch(String patientIDMatch)
	{
		this.patientIDMatch = patientIDMatch;
	}

	public boolean patientIDMatches(String patientID)
	{
		if (this.patientIDMatch == null)
			return true;
		else {
			patientIDMatch = removeAsterisk(patientIDMatch);
			String patientIDRegex = "(?i).*" + patientIDMatch + ".*";
			if (patientID.matches(patientIDRegex))
				return true;
			else
				return false;
		}
	}

	public String getAccessionNumberMatch()
	{
		return this.accessionNumberMatch;
	}

	public void setAccessionNumberMatch(String accessionNumberMatch)
	{
		this.accessionNumberMatch = accessionNumberMatch;
	}

	public boolean accessionNumberMatches(String accessionNumber)
	{
		if (this.accessionNumberMatch == null)
			return true;
		else {
			accessionNumberMatch = removeAsterisk(accessionNumberMatch);
			String accessionNumberRegex = "(?i).*" + accessionNumberMatch + ".*";
			if (accessionNumber.matches(accessionNumberRegex))
				return true;
			else
				return false;
		}
	}

	public String getModalityMatch()
	{
		return this.modalityMatch;
	}

	public void setModalityMatch(String modalityMatch)
	{
		this.modalityMatch = modalityMatch;
	}

	public boolean modalitiesMatch(Set<String> modalities)
	{
		for (String modality : modalities)
			if (modalityMatches(modality))
				return true;
		return false;
	}

	public boolean modalityMatches(String modality)
	{
		if (this.modalityMatch == null)
			return true;
		else {
			modalityMatch = removeAsterisk(modalityMatch);
			String modalityRegex = "(?i).*" + modalityMatch + ".*";
			if (modality.matches(modalityRegex))
				return true;
			else
				return false;
		}
	}

	public boolean hasAnnotationsAnnotationMatch()
	{
		return this.annotationMatch == AnnotationMatch.HAS_ANNOTATIONS;
	}

	public boolean hasNoAnnotationsAnnotationMatch()
	{
		return this.annotationMatch == AnnotationMatch.HAS_NO_ANNOTATIONS;
	}

	public void setHasNoAnnotationsAnnotationMatch()
	{
		this.annotationMatch = AnnotationMatch.HAS_NO_ANNOTATIONS;
	}

	public void setHasAnnotationsAnnotationMatch()
	{
		this.annotationMatch = AnnotationMatch.HAS_ANNOTATIONS;
	}

	public boolean annotationNumberMatches(int numberOfAnnotations)
	{
		if (this.annotationMatch == null)
			return true;
		else {
			if (this.annotationMatch == AnnotationMatch.NONE)
				return true;
			else if (this.annotationMatch == AnnotationMatch.HAS_ANNOTATIONS)
				return numberOfAnnotations > 0;
			else
				// AnnotationMatch.HAS_NO_ANNOTATIONS
				return numberOfAnnotations == 0;
		}
	}

	public String getInsertDateStartMatch()
	{
		return this.insertDateStartMatch;
	}

	public boolean hasInsertDateStartMatch()
	{
		return this.insertDateStartMatch != null;
	}

	public void setInsertDateStartMatch(String insertDateStartMatch)
	{
		this.insertDateStartMatch = insertDateStartMatch;
	}

	public String getInsertDateFinishMatch()
	{
		return this.insertDateFinishMatch;
	}

	public boolean hasInsertDateFinishMatch()
	{
		return this.insertDateFinishMatch != null;
	}

	public void setInsertDateFinishMatch(String insertDateFinishMatch)
	{
		this.insertDateFinishMatch = insertDateFinishMatch;
	}

	public String getStudyDateStartMatch()
	{
		return this.studyDateStartMatch;
	}

	public boolean hasStudyDateStartMatch()
	{
		return this.studyDateStartMatch != null;
	}

	public void setStudyDateStartMatch(String studyDateStartMatch)
	{
		this.studyDateStartMatch = studyDateStartMatch;
	}

	public String getStudyDateFinishMatch()
	{
		return this.studyDateFinishMatch;
	}

	public boolean hasStudyDateFinishMatch()
	{
		return this.studyDateFinishMatch != null;
	}

	public void setStudyDateFinishMatch(String studyDateFinishMatch)
	{
		this.studyDateFinishMatch = studyDateFinishMatch;
	}

	public boolean shouldFilterProject(String projectName)
	{
		return (hasProjectNameMatch() && !projectNameMatches(projectName));
	}

	public boolean shouldFilterProject(String projectName, int numberOfAnnotations)
	{
		return (hasProjectNameMatch() && !projectNameMatches(projectName))
				|| (hasAnnotationMatch() && !annotationNumberMatches(numberOfAnnotations));
	}

	public boolean shouldFilterSubject(String patientID, String patientName)
	{
		return (hasPatientIDMatch() && !patientIDMatches(patientID))
				|| (hasPatientNameMatch() && !patientNameMatches(patientName));
	}

	public boolean shouldFilterSubject(String patientID, String patientName, Set<String> examTypes,
			int numberOfAnnotations)
	{
		return (hasPatientIDMatch() && !patientIDMatches(patientID))
				|| (hasPatientNameMatch() && !patientNameMatches(patientName))
				|| (hasModalityMatch() && !modalitiesMatch(examTypes))
				|| (hasAnnotationMatch() && !annotationNumberMatches(numberOfAnnotations));
	}

	public boolean shouldFilterSubject(String patientID, String patientName, int numberOfAnnotations)
	{
		return (hasPatientIDMatch() && !patientIDMatches(patientID))
				|| (hasPatientNameMatch() && !patientNameMatches(patientName))
				|| (hasAnnotationMatch() && !annotationNumberMatches(numberOfAnnotations));
	}

	public boolean shouldFilterSubject(String patientID, String patientName, String examType, String accessionNumber, int numberOfAnnotations)
	{
		return (hasPatientIDMatch() && !patientIDMatches(patientID))
				|| (hasPatientNameMatch() && !patientNameMatches(patientName))
				|| (hasAnnotationMatch() && !annotationNumberMatches(numberOfAnnotations));
	}

	public boolean shouldFilterStudy(String patientID, String accessionNumber, Set<String> examTypes,
			int numberOfAnnotations)
	{
		return (hasPatientIDMatch() && !patientIDMatches(patientID))
				|| (hasAccessionNumberMatch() && !accessionNumberMatches(accessionNumber))
				|| (hasModalityMatch() && !modalitiesMatch(examTypes))
				|| (hasAnnotationMatch() && !annotationNumberMatches(numberOfAnnotations));
	}

	public boolean shouldFilterSeries(String patientID, String patientName, String examType,
			int numberOfAnnotations)
	{
		return (hasPatientIDMatch() && !patientIDMatches(patientID))
				|| (hasPatientNameMatch() && !patientNameMatches(patientName))
				|| (hasModalityMatch() && !modalityMatches(examType))
				|| (hasAnnotationMatch() && !annotationNumberMatches(numberOfAnnotations));
	}

	private enum AnnotationMatch {
		NONE, HAS_ANNOTATIONS, HAS_NO_ANNOTATIONS
	}

	public String getFileTypeMatch() {
		return fileTypeMatch;
	}

	public void setFileTypeMatch(String fileTypeMatch) {
		this.fileTypeMatch = fileTypeMatch;
	};

	public boolean hasFileTypeMatch()
	{
		return this.fileTypeMatch != null;
	}

	public boolean fileTypeMatches(String fileType)
	{
		if (this.fileTypeMatch == null)
			return true;
		else {
			String fileTypeRegex = "(?i).*" + fileTypeMatch + ".*";
			if (fileType.matches(fileTypeRegex))
				return true;
			else
				return false;
		}
	}

	public boolean shouldFilterFileType(String fileType)
	{
		return (hasFileTypeMatch() && !fileTypeMatches(fileType));
	}

	public boolean shouldFilterFileType(String patientID, String patientName, String fileType)
	{
		return (hasPatientIDMatch() && !patientIDMatches(patientID))
				|| (hasPatientNameMatch() && !patientNameMatches(patientName))
				|| (hasFileTypeMatch() && !fileTypeMatches(fileType));
	}
	
	private String removeAsterisk(String match)
	{
		match = match.replace('%', '*');
		while (match.startsWith("*"))
		{
			match = match.substring(1);
		}
		while (match.endsWith("*"))
		{
			match = match.substring(0, match.length()-1);
		}
		return match;
	}

}
