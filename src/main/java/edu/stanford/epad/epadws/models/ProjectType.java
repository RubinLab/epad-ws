package edu.stanford.epad.epadws.models;

import java.util.HashSet;
import java.util.Set;

public enum ProjectType {
	PRIVATE("Private"), PUBLIC("Public");

	private String name;

	private ProjectType(String type)
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

		for (ProjectType projectType : ProjectType.values())
			names.add(projectType.name());

		return names;
	}
}
