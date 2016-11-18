package edu.stanford.epad.epadws.aim.dicomsr;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * this class is the metadata information defined in the format for dcmqi
 * @author emelalkim
 *
 */
public class DicomSRMetadata {

	String schema="https://raw.githubusercontent.com/qiicr/dcmqi/master/doc/sr-tid1500-schema.json#";
	String SeriesDescription = "Measurements";
	String SeriesNumber = "1001";
	String InstanceNumber= "1";

	ArrayList<String> compositeContext;
	

	ArrayList<String>  imageLibrary;


	ObserverContext observerContext= new ObserverContext();

	String VerificationFlag= "VERIFIED";
	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getSeriesDescription() {
		return SeriesDescription;
	}

	public void setSeriesDescription(String seriesDescription) {
		SeriesDescription = seriesDescription;
	}

	public String getSeriesNumber() {
		return SeriesNumber;
	}

	public void setSeriesNumber(String seriesNumber) {
		SeriesNumber = seriesNumber;
	}

	public String getInstanceNumber() {
		return InstanceNumber;
	}

	public void setInstanceNumber(String instanceNumber) {
		InstanceNumber = instanceNumber;
	}

	public ArrayList<String> getCompositeContext() {
		return compositeContext;
	}

	public void setCompositeContext(ArrayList<String> compositeContext) {
		this.compositeContext = compositeContext;
	}

	public ArrayList<String> getImageLibrary() {
		return imageLibrary;
	}

	public void setImageLibrary(ArrayList<String> imageLibrary) {
		this.imageLibrary = imageLibrary;
	}

	public ObserverContext getObserverContext() {
		return observerContext;
	}

	public void setObserverContext(ObserverContext observerContext) {
		this.observerContext = observerContext;
	}

	public String getVerificationFlag() {
		return VerificationFlag;
	}

	public void setVerificationFlag(String verificationFlag) {
		VerificationFlag = verificationFlag;
	}

	public String getCompletionFlag() {
		return CompletionFlag;
	}

	public void setCompletionFlag(String completionFlag) {
		CompletionFlag = completionFlag;
	}

	public String getActivitySession() {
		return activitySession;
	}

	public void setActivitySession(String activitySession) {
		this.activitySession = activitySession;
	}

	public String getTimePoint() {
		return timePoint;
	}

	public void setTimePoint(String timePoint) {
		this.timePoint = timePoint;
	}

	public List<MeasurementGroup> getMeasurements() {
		return Measurements;
	}

	public void setMeasurements(List<MeasurementGroup> measurements) {
		Measurements = measurements;
	}


	String CompletionFlag="COMPLETE";

	String activitySession= "1";
	String timePoint= "1";
	List<MeasurementGroup> Measurements = new ArrayList<MeasurementGroup>();


	public DicomSRMetadata() {
		Measurements= new ArrayList<MeasurementGroup>();
		Measurements.add(new MeasurementGroup());
		compositeContext=new ArrayList<>();
		compositeContext.add("liver.dcm");
		imageLibrary=new ArrayList<>();
		imageLibrary.add("01.dcm");
		imageLibrary.add("02.dcm");
		imageLibrary.add("03.dcm");
	
		
	}
	
	public DicomSRMetadata(ArrayList<String> compositeContext, ArrayList<String> imageLibrary ) {
		Measurements= new ArrayList<MeasurementGroup>();
		Measurements.add(new MeasurementGroup());
		this.compositeContext=compositeContext;
		this.imageLibrary=imageLibrary;
	}
	
	
	public static DicomSRMetadata fromJSON(String json)
	{
		Gson gson = new Gson();
		
		return gson.fromJson(json, DicomSRMetadata.class);
	}
	
	public String toJSON()
	{
		Gson gson = new Gson();
		
		return gson.toJson(this).replace("\"schema\"", "\"@schema\"");
	}



}
