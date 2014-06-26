package edu.stanford.epad.epadws.handlers.core;

import java.util.Map;

import edu.stanford.epad.epadws.handlers.HandlerUtil;

public class AIMReference
{
	public final String aimID;

	public AIMReference(String aimID)
	{
		this.aimID = aimID;
	}

	public static AIMReference extract(String template, String pathInfo)
	{
		Map<String, String> templateMap = HandlerUtil.getTemplateMap(template, pathInfo);
		String aimID = HandlerUtil.getTemplateParameter(templateMap, "aid");

		validateAIMID(aimID);

		return new AIMReference(aimID);
	}

	public static void validateAIMID(String aimID)
	{
		if (aimID == null)
			throw new RuntimeException("Invalid AIM ID found in request");
	}
}
