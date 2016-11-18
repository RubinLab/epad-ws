package edu.stanford.epad.epadws.aim.dicomsr;

import com.google.gson.Gson;

public class MeasurementItem {
	
	String value="37.3289";
    ControlledTerm quantity= new ControlledTerm("112031", "DCM", "Attenuation Coefficient");
    ControlledTerm units= new ControlledTerm("[hnsf'U]", "UCUM", "Hounsfield unit");
    ControlledTerm derivationModifier= new ControlledTerm("R-00317", "SRT", "Mean");
    
    public MeasurementItem() {
    	
    }
    
    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ControlledTerm getQuantity() {
		return quantity;
	}

	public void setQuantity(ControlledTerm quantity) {
		this.quantity = quantity;
	}

	public ControlledTerm getUnits() {
		return units;
	}

	public void setUnits(ControlledTerm units) {
		this.units = units;
	}

	public ControlledTerm getDerivationModifier() {
		return derivationModifier;
	}

	public void setDerivationModifier(ControlledTerm derivationModifier) {
		this.derivationModifier = derivationModifier;
	}
	
	public static MeasurementItem fromJSON(String json)
	{
		Gson gson = new Gson();
		
		return gson.fromJson(json, MeasurementItem.class);
	}

	public String toJSON()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}
}
