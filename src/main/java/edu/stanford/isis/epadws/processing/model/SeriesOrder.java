package edu.stanford.isis.epadws.processing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.util.EPADLogger;

/**
 * Keeps all the information about each instance in a series and the instance order.
 * 
 * @author amsnyder
 */
public class SeriesOrder
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private final ArrayList<DicomImageEntry> instances;
	private final String seriesUID;

	public SeriesOrder(int numInstance, String seriesUID)
	{
		if (numInstance < 1) {
			throw new IllegalArgumentException("numInstances must be a positive value.");
		}

		int instanceSize = 2000;
		if (instanceSize < numInstance + 1) {
			instanceSize = 2 * numInstance;
		}

		instances = new ArrayList<DicomImageEntry>(instanceSize);
		for (int i = 0; i < instanceSize + 1; i++) {
			instances.add(null);
		}
		this.seriesUID = seriesUID;
	}

	public String getSeriesUID()
	{
		return seriesUID;
	}

	public boolean hasInstance(int index)
	{
		if (index >= instances.size()) {
			return false;
		}

		return instances.get(index) != null;
	}

	public int set(DicomImageEntry entry)
	{
		int index = entry.getInstanceNum();
		instances.set(index, entry);
		return index;
	}

	public DicomImageEntry getByInstanceNo(int index) throws Exception
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

	/**
	 * 
	 * @return boolean
	 */
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

	public int getFinishedCount()
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

	public int size()
	{
		return instances.size();
	}

	public void updateImageDescriptions(List<Map<String, String>> imageDescriptions)
	{
		for (Map<String, String> imageDescription : imageDescriptions) {
			String instanceNum = imageDescription.get("inst_no");

			// //ToDo: delete below once debugged.
			// if(instanceNum==null){
			// Set<String> s = currImage.keySet();
			// logger.info("WARNING: Didn't find 'inst_no' for key-set: "+s.toString());
			// throw new IllegalArgumentException("didn't find 'inst_no' for key-set: "+s.toString());
			// }else{
			// logger.info("[TEMP] instanceNum="+instanceNum);
			// }
			// //ToDo: delete above once debugged.

			int instNum = Integer.parseInt(instanceNum);
			String sopInstanceUID = imageDescription.get("sop_iuid");

			addNewImage(instNum, sopInstanceUID);
		}
	}

	/**
	 * New a new image. Add it to the list.
	 * 
	 * @param instNum int
	 * @param sopInstanceUID String
	 */
	private void addNewImage(int instNum, String sopInstanceUID)
	{
		DicomImageEntry imageEntry = new DicomImageEntry(instNum, sopInstanceUID);
		if (!hasInstance(instNum)) {
			logger.info("[TEMP LOG-DEBUGGING] adding: " + instNum + " sopInstanceUID: " + sopInstanceUID);

			if (instances.size() < instNum + 1) {
				int start = instances.size();
				logger.info("WARNING: resizing array from=" + instances.size() + " to=" + (instNum + 1) + " series="
						+ seriesUID);
				instances.ensureCapacity(instNum + 1);
				for (int i = start; i < instNum + 1; i++) {
					instances.add(start, null);
				}
			}
			instances.set(instNum, imageEntry);
		}
	}
}
