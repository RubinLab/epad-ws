package edu.stanford.epad.epadws.handlers.search;

public class EPADSearchFilter
{
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

	public String getProjectNameMatch()
	{
		return this.projectNameMatch;
	}

	public boolean hasProjectNameMatch()
	{
		return this.projectNameMatch != null;
	}

	public void setProjectNameMatch(String projectNameMatch)
	{
		this.projectNameMatch = projectNameMatch;
	}

	public String getPatientNameMatch()
	{
		return this.patientNameMatch;
	}

	public boolean hasPatientNameMatch()
	{
		return this.patientNameMatch != null;
	}

	public void setPatientNameMatch(String patientNameMatch)
	{
		this.patientNameMatch = patientNameMatch;
	}

	public String getPatientIDMatch()
	{
		return this.patientIDMatch;
	}

	public boolean hasPatientIDMatch()
	{
		return this.patientIDMatch != null;
	}

	public void setPatientIDMatch(String patientIDMatch)
	{
		this.patientIDMatch = patientIDMatch;
	}

	public String getAccessionNumberMatch()
	{
		return this.accessionNumberMatch;
	}

	public boolean hasAccessionNumberMatch()
	{
		return this.accessionNumberMatch != null;
	}

	public void setAccessionNumberMatch(String accessionNumberMatch)
	{
		this.accessionNumberMatch = accessionNumberMatch;
	}

	public String getModalityMatch()
	{
		return this.modalityMatch;
	}

	public boolean hasModalityMatch()
	{
		return this.modalityMatch != null;
	}

	public void setModalityMatch(String modailityMatch)
	{
		this.modalityMatch = modailityMatch;
	}

	public boolean hasAnnotationMatch()
	{
		return this.annotationMatch == AnnotationMatch.NONE;
	}

	public boolean hasAnnotationsAnnotationMatch()
	{
		return this.annotationMatch == AnnotationMatch.HAS_ANNOTATIONS;
	}

	public void setHasAnnotationsAnnotationMatch()
	{
		this.annotationMatch = AnnotationMatch.HAS_ANNOTATIONS;
	}

	public boolean hasNoAnnotationsAnnotationMatch()
	{
		return this.annotationMatch == AnnotationMatch.HAS_NO_ANNOTATIONS;
	}

	public void setHasNoAnnotationsAnnotationMatch()
	{
		this.annotationMatch = AnnotationMatch.HAS_NO_ANNOTATIONS;
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

	private enum AnnotationMatch {
		NONE, HAS_ANNOTATIONS, HAS_NO_ANNOTATIONS
	};
}
