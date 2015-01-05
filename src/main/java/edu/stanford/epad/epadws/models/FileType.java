package edu.stanford.epad.epadws.models;

import java.util.HashSet;
import java.util.Set;

public enum FileType {
	TEMPLATE("Template"), IMAGE("Image");

	private String name;

	private FileType(String type)
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

		for (FileType fileType : FileType.values())
			names.add(fileType.name());

		return names;
	}
}
