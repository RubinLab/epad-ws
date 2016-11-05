package edu.stanford.epad.epadws.aim.dicomsr;

import com.google.gson.Gson;

public class ObserverContext {
	String ObserverType="PERSON";
    String PersonObserverName= "Reader1";
    
    
    public String toJSON()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}

}
