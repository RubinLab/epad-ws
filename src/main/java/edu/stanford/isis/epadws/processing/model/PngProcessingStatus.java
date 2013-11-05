package edu.stanford.isis.epadws.processing.model;

/**
 * @author alansnyder
 */
public enum PngProcessingStatus {
	NO_DICOM(1), IN_PIPELINE(2), DONE(3), ERROR(4);

	private int statusCode;

	PngProcessingStatus(int status)
	{
		this.statusCode = status;
	}

	public int getCode()
	{
		return statusCode;
	}
}
