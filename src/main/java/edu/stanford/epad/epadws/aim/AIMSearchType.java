package edu.stanford.epad.epadws.aim;

import java.util.HashSet;
import java.util.Set;

public enum AIMSearchType {
	PATIENT_ID("patientId"), SERIES_UID("seriesUID"), PERSON_NAME("personName"), ANNOTATION_UID("annotationUID"), AIM_QUERY("aimQL"), JSON_QUERY("jsonQuery");

	private String name;

	private AIMSearchType(String type)
	{
		this.name = type;
	}

	public String getName()
	{
		return name;
	}

	public static Set<String> names()
	{
		Set<String> names = new HashSet<String>();

		for (AIMSearchType aimSearchType : AIMSearchType.values())
			names.add(aimSearchType.name());

		return names;
	}
}
