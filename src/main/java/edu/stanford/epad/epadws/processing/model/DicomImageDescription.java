package edu.stanford.epad.epadws.processing.model;

/**
 * instanceNum SOPInstanceUID PNG file path header file path
 * 
 * @author amsnyder
 */
public class DicomImageDescription
{
	private final int instanceNum;
	private final String sopInstanceUID;
	private final String errorMessage = null;
	private String pngFilePath = null;
	private String dicomHeaderFilePath = null;
	private DicomImageProcessingState state;

	public DicomImageDescription(int instanceNum, String sopInstanceUID)
	{
		this.instanceNum = instanceNum;
		this.sopInstanceUID = sopInstanceUID;
		state = DicomImageProcessingState.NEW;
	}

	public int getInstanceNum()
	{
		return instanceNum;
	}

	public String getSopInstanceUID()
	{
		return sopInstanceUID;
	}

	public DicomImageProcessingState getState()
	{
		return state;
	}

	public void setState(DicomImageProcessingState newState)
	{
		state = newState;
	}

	public boolean hasPngFile()
	{
		return pngFilePath != null;
	}

	public void setPngFilePath(String pngFilePath)
	{
		this.pngFilePath = pngFilePath;
	}

	public String getPngFilePath()
	{
		return pngFilePath;
	}

	public boolean hasHeaderFile()
	{
		return dicomHeaderFilePath != null;
	}

	public void setDicomHeaderFilePath(String dicomHeaderFilePath)
	{
		this.dicomHeaderFilePath = dicomHeaderFilePath;
	}

	public boolean hasError()
	{
		return errorMessage != null;
	}

	/**
	 * Processing is complete when the PNG file and the dicom-header file is complete.
	 * 
	 * @return boolean
	 */
	public boolean isComplete()
	{
		if (hasHeaderFile() && hasPngFile()) {
			return true;
		}
		return false;
	}
}
