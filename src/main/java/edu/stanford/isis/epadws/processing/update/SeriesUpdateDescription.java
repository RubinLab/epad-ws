package edu.stanford.isis.epadws.processing.update;

public class SeriesUpdateDescription
{
	public final String seriesID;
	public float percentComplete;

	public SeriesUpdateDescription(String seriesID)
	{
		this.seriesID = seriesID;
		this.percentComplete = 0.0f;
	}
}
