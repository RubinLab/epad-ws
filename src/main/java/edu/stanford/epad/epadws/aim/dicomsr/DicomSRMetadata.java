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

	String[] compositeContext= new String[] {
			"liver.dcm"
	};

	String[] imageLibrary= new String[] {"01.dcm",
			"02.dcm",
			"03.dcm"
	};


	ObserverContext observerContext= new ObserverContext();

	String VerificationFlag= "VERIFIED";
	String CompletionFlag="COMPLETE";

	String activitySession= "1";
	String timePoint= "1";
	List<MeasurementGroup> Measurements = new ArrayList<MeasurementGroup>();


	public DicomSRMetadata() {
		Measurements= new ArrayList<MeasurementGroup>();
		Measurements.add(new MeasurementGroup());
	}
	public String toJSON()
	{
		Gson gson = new Gson();

		return gson.toJson(this).replace("\"schema\"", "\"@schema\"");
	}



}
