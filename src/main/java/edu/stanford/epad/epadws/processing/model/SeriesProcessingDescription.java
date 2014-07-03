package edu.stanford.epad.epadws.processing.model;

import java.util.ArrayList;
import java.util.Set;

import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * Keeps all the information about an uploaded series that is being processed by ePAD. A {@link SeriesPipelineState}
 * tracks the processing state of this series when it is in the pipeline.
 */
public class SeriesProcessingDescription
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private final int numberOfInstances;
	private final ArrayList<DicomImageDescription> instances;
	private final String studyUID;
	private final String seriesUID;
	private final String subjectName;
	private final String subjectID;

	public SeriesProcessingDescription(int numberOfInstances, String seriesUID, String studyUID, String patientName,
			String subjectID)
	{
		if (numberOfInstances < 1)
			throw new IllegalArgumentException("numInstances must be a positive value.");

		this.numberOfInstances = numberOfInstances;
		int instanceSize = 2000;
		if (instanceSize < numberOfInstances + 1)
			instanceSize = 2 * numberOfInstances;

		instances = new ArrayList<DicomImageDescription>(instanceSize);
		for (int i = 0; i < instanceSize + 1; i++)
			instances.add(null); // Indicates that it is not processed yet

		this.seriesUID = seriesUID;
		this.studyUID = studyUID;
		this.subjectName = patientName;
		this.subjectID = subjectID;
	}

	public String getSeriesUID()
	{
		return seriesUID;
	}

	public String getStudyUID()
	{
		return studyUID;
	}

	public String getPatientName()
	{
		return subjectName;
	}

	public String getSubjectID()
	{
		return subjectID;
	}

	public boolean isComplete()
	{
		int size = instances.size();
		for (int i = 0; i < size; i++) {
			if (!hasInstance(i)) {
				return false;
			}
		}
		return true;
	}

	public float percentComplete()
	{
		int numberOfInstances = getNumberOfInstances();
		int numberOfCompletedInstances = getNumberOfCompletedInstances();

		return (numberOfCompletedInstances) / ((float)numberOfInstances) * 100.0f;
	}

	public int getNumberOfCompletedInstances()
	{
		int count = 0;
		int size = size();
		for (int i = 0; i < size; i++) {
			if (hasInstance(i)) {
				count++;
			}
		}
		return count;
	}

	public int getNumberOfInstances()
	{
		return numberOfInstances;
	}

	private int size()
	{
		return instances.size();
	}

	public boolean hasInstance(int index)
	{
		if (index >= instances.size()) {
			return false;
		}

		return instances.get(index) != null;
	}

	public void updateWithDICOMFileDescriptions(Set<DICOMFileDescription> dicomFileDescriptions)
	{
		for (DICOMFileDescription dicomFileDescription : dicomFileDescriptions) {
			int instanceNumber = dicomFileDescription.instanceNumber;
			String imageUID = dicomFileDescription.imageUID;
			addCompletedInstance(instanceNumber, imageUID);
		}
	}

	/**
	 * @param instanceNumber int
	 * @param sopInstanceUID String
	 */
	private void addCompletedInstance(int instanceNumber, String sopInstanceUID)
	{
		DicomImageDescription imageEntry = new DicomImageDescription(instanceNumber, sopInstanceUID);
		if (!hasInstance(instanceNumber)) {
			if (instances.size() < instanceNumber + 1) {
				int start = instances.size();
				logger.warning("resizing array from=" + instances.size() + " to=" + (instanceNumber + 1) + " series="
						+ seriesUID);
				instances.ensureCapacity(instanceNumber + 1);
				for (int i = start; i < instanceNumber + 1; i++) {
					instances.add(start, null);
				}
			}
			instances.set(instanceNumber, imageEntry);
		}
	}
}
