package edu.stanford.isis.epadws.handlers.dicom;

public class StudySearchResult
{
	public final String studyUID, patientName, patientID, examType, dateAcquired, pngStatus, seriesCount, firstSeriesUID,
			firstSeriesDateAcquired, studyAccessionNumber, imagesCount, stuidID, studyDescription, physicianName, birthdate,
			sex;

	public StudySearchResult(String studyUID, String patientName, String patientID, String examType, String dateAcquired,
			String pngStatus, String seriesCount, String firstSeriesUID, String firstSeriesDateAcquired,
			String studyAccessionNumber, String imagesCount, String stuidID, String studyDescription, String physicianName,
			String birthdate, String sex)
	{
		this.studyUID = studyUID;
		this.patientName = patientName;
		this.patientID = patientID;
		this.examType = examType;
		this.dateAcquired = dateAcquired;
		this.pngStatus = pngStatus;
		this.seriesCount = seriesCount;
		this.firstSeriesUID = firstSeriesUID;
		this.firstSeriesDateAcquired = firstSeriesDateAcquired;
		this.studyAccessionNumber = studyAccessionNumber;
		this.imagesCount = imagesCount;
		this.stuidID = stuidID;
		this.studyDescription = studyDescription;
		this.physicianName = physicianName;
		this.birthdate = birthdate;
		this.sex = sex;
	}
}