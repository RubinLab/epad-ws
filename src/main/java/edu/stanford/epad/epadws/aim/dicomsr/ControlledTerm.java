package edu.stanford.epad.epadws.aim.dicomsr;

import com.google.gson.Gson;

public class ControlledTerm {
	String CodeValue;
    String CodingSchemeDesignator;
    String CodeMeaning;
    
    public ControlledTerm(String codeValue,String codingSchemeDesignator,String codeMeaning){
    	this.CodeValue=codeValue;
    	this.CodingSchemeDesignator=codingSchemeDesignator;
    	this.CodeMeaning=codeMeaning;
    }
    
    public String toJSON()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}
}
