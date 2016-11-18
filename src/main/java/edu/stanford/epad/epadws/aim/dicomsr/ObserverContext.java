package edu.stanford.epad.epadws.aim.dicomsr;

import com.google.gson.Gson;

public class ObserverContext {
	String ObserverType="PERSON";
    String PersonObserverName= "Reader1";
    
    public ObserverContext(){
    	
    }
    
    
    public String getObserverType() {
		return ObserverType;
	}


	public void setObserverType(String observerType) {
		ObserverType = observerType;
	}


	public String getPersonObserverName() {
		return PersonObserverName;
	}


	public void setPersonObserverName(String personObserverName) {
		PersonObserverName = personObserverName;
	}

	public static ObserverContext fromJSON(String json)
	{
		Gson gson = new Gson();
		
		return gson.fromJson(json, ObserverContext.class);
	}
	public String toJSON()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}

}
