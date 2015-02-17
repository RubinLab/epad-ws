package edu.stanford.epad.epadws.models;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.epad.epadws.aim.AIMSearchType;

public enum UserRole {
	OWNER("Owner"), COLLABORATOR("Collaborator"), MEMBER("Member");

	private String name;

	private UserRole(String role)
	{
		this.name = role;
	}

	public String getName()
	{
		return name;
	}

	public static Set<String> names()
	{
		Set<String> names = new HashSet<String>();

		for (UserRole userRole : UserRole.values())
			names.add(userRole.name());

		return names;
	}
	
	public static UserRole getRole(String role)
	{
		for (UserRole urole : UserRole.values()) {
			if (urole.getName().equals(role))
				return urole;
		}
		return null;
	}		
}
