package edu.stanford.isis.epadws.processing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.util.EPADLogger;

/**
 * Keeps all the information about each instance in a series and the instance order. A
 * {@link DicomSeriesProcessingStatus} tracks this series when it is in the pipeline.
 */
public class DicomSeriesDescription
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private final int numberOfInstances;
	private final ArrayList<DicomImageDescription> instances;
	private final String studyIUID;
	private final String seriesUID;
	private final String patientName;
	private final String patientID;

	public DicomSeriesDescription(int numberOfInstances, String seriesUID, String studyIUID, String patientName,
			String patientID)
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
		this.studyIUID = studyIUID;
		this.patientName = patientName;
		this.patientID = patientID;
	}

	public String getSeriesUID()
	{
		return seriesUID;
	}

	public String getStudyIUID()
	{
		return studyIUID;
	}

	public String getPatientName()
	{
		return patientName;
	}

	public String getPatientID()
	{
		return patientID;
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

	public int set(DicomImageDescription dicomImageDescription)
	{
		int index = dicomImageDescription.getInstanceNum();
		instances.set(index, dicomImageDescription);
		return index;
	}

	public DicomImageDescription getByInstanceNo(int index) throws Exception
	{
		try {
			if (!hasInstance(index)) {
				return null;
			}
			return instances.get(index);
		} catch (Exception e) {
			logger.info("SeriesOrder: " + e.getMessage() + " size=" + size() + " seriesUID=" + seriesUID);
			throw e;
		}
	}

	public void updateWithDicomImageFileDescriptions(List<Map<String, String>> dicomImageFileDescriptions)
	{
		for (Map<String, String> dicomImageFileDescription : dicomImageFileDescriptions) {
			String instanceNumberString = dicomImageFileDescription.get("inst_no");

			// //ToDo: delete below once debugged.
			// if(instanceNum==null){
			// Set<String> s = currImage.keySet();
			// logger.info("WARNING: Didn't find 'inst_no' for key-set: "+s.toString());
			// throw new IllegalArgumentException("didn't find 'inst_no' for key-set: "+s.toString());
			// }else{
			// logger.info("[TEMP] instanceNum="+instanceNum);
			// }
			// //ToDo: delete above once debugged.

			int instanceNumber = instanceNumberString == null ? 1 : Integer.parseInt(instanceNumberString);
			String sopInstanceUID = dicomImageFileDescription.get("sop_iuid");
			addCompletedInstance(instanceNumber, sopInstanceUID);
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
			// logger.info("[TEMP LOG-DEBUGGING] adding: " + instNum + " sopInstanceUID: " + sopInstanceUID);
			if (instances.size() < instanceNumber + 1) {
				int start = instances.size();
				logger.info("WARNING: resizing array from=" + instances.size() + " to=" + (instanceNumber + 1) + " series="
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
