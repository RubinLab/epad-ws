package edu.stanford.epad.epadws.aim.dicomsr;

import com.google.gson.Gson;

public class MeasurementItem {
	
	String value="37.3289";
    ControlledTerm quantity= new ControlledTerm("112031", "DCM", "Attenuation Coefficient");
    ControlledTerm units= new ControlledTerm("[hnsf'U]", "UCUM", "Hounsfield unit");
    ControlledTerm derivationModifier= new ControlledTerm("R-00317", "SRT", "Mean");
    
    public String toJSON()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}
}
