package edu.stanford.epad.epadws.aim.dicomsr;

import com.google.gson.Gson;

import edu.stanford.hakan.aim4api.base.CD;

public class ControlledTerm {
	String CodeValue;
    String CodingSchemeDesignator;
    String CodeMeaning;
    
    public ControlledTerm(String codeValue,String codingSchemeDesignator,String codeMeaning){
    	this.CodeValue=codeValue;
    	this.CodingSchemeDesignator=codingSchemeDesignator;
    	this.CodeMeaning=codeMeaning;
    }
    public ControlledTerm(){
    	
    }
    public ControlledTerm(CD cd){
    	setCodeValue(cd.getCode());
    	setCodeMeaning(cd.getDisplayName().getValue());
    	setCodingSchemeDesignator(cd.getCodeSystemName());
    }
    
    public String getCodeValue() {
		return CodeValue;
	}
	public void setCodeValue(String codeValue) {
		CodeValue = codeValue;
	}
	public String getCodingSchemeDesignator() {
		return CodingSchemeDesignator;
	}
	public void setCodingSchemeDesignator(String codingSchemeDesignator) {
		CodingSchemeDesignator = codingSchemeDesignator;
	}
	public String getCodeMeaning() {
		return CodeMeaning;
	}
	public void setCodeMeaning(String codeMeaning) {
		CodeMeaning = codeMeaning;
	}
	public String toJSON()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}
}
