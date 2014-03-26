package edu.stanford.epad.epadws.processing.events;

public class SeriesEventDescription
{
	public final String seriesID;
	public float percentPNGGenerationComplete;

	public SeriesEventDescription(String seriesID)
	{
		this.seriesID = seriesID;
		this.percentPNGGenerationComplete = 0.0f;
	}
}
