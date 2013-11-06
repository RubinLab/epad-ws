package edu.stanford.isis.epadws.processing.model;

public enum DicomStudyProcessingStatus {
	NEW(0), PNG_IN_PROCESS(1), PNG_COMPLETE(2);

	private final int value;

	private DicomStudyProcessingStatus(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}
