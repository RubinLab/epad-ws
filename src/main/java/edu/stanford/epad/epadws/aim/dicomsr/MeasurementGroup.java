package edu.stanford.epad.epadws.aim.dicomsr;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class MeasurementGroup {
	
	String TrackingIdentifier="Measurements group 1";
    int ReferencedSegment= 1;
    String SourceSeriesForImageSegmentation="1.2.392.200103.20080913.113635.2.2009.6.22.21.43.10.23431.1";
    String segmentationSOPInstanceUID= "1.2.276.0.7230010.3.1.4.0.42154.1458337731.665796";
    ControlledTerm Finding =new ControlledTerm("T-D0060", "SRT", "Organ");
    ControlledTerm FindingSite =new ControlledTerm("T-62000", "SRT", "Liver");
    
    List<MeasurementItem> measurementItems=new  ArrayList<MeasurementItem>();
    
    public MeasurementGroup() {
    	measurementItems= new ArrayList<MeasurementItem>();
    	measurementItems.add(new MeasurementItem());
	}
	
    public String getTrackingIdentifier() {
		return TrackingIdentifier;
	}

	public void setTrackingIdentifier(String trackingIdentifier) {
		TrackingIdentifier = trackingIdentifier;
	}

	public int getReferencedSegment() {
		return ReferencedSegment;
	}

	public void setReferencedSegment(int referencedSegment) {
		ReferencedSegment = referencedSegment;
	}

	public String getSourceSeriesForImageSegmentation() {
		return SourceSeriesForImageSegmentation;
	}

	public void setSourceSeriesForImageSegmentation(String sourceSeriesForImageSegmentation) {
		SourceSeriesForImageSegmentation = sourceSeriesForImageSegmentation;
	}

	public String getSegmentationSOPInstanceUID() {
		return segmentationSOPInstanceUID;
	}

	public void setSegmentationSOPInstanceUID(String segmentationSOPInstanceUID) {
		this.segmentationSOPInstanceUID = segmentationSOPInstanceUID;
	}

	public ControlledTerm getFinding() {
		return Finding;
	}

	public void setFinding(ControlledTerm finding) {
		Finding = finding;
	}

	public ControlledTerm getFindingSite() {
		return FindingSite;
	}

	public void setFindingSite(ControlledTerm findingSite) {
		FindingSite = findingSite;
	}

	public List<MeasurementItem> getMeasurementItems() {
		return measurementItems;
	}

	public void setMeasurementItems(List<MeasurementItem> measurementItems) {
		this.measurementItems = measurementItems;
	}

	public String toJSON()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}
}
