package edu.stanford.isis.epadws.handlers.coordination;

/**
 * A term from some vocabulary.
 * 
 * @author martin
 */
public class Term
{
	private final String termID, schemaName, schemaVersion, description;

	public Term(String termID, String schemaName, String schemaVersion, String description)
	{
		this.termID = termID;
		this.schemaName = schemaName;
		this.schemaVersion = schemaVersion;
		this.description = description;
	}

	public String getTermID()
	{
		return termID;
	}

	public String getSchemaName()
	{
		return schemaName;
	}

	public String getSchemaVersion()
	{
		return schemaVersion;
	}

	public String getDescription()
	{
		return description;
	}

	public boolean isValid()
	{
		return termID != null && schemaName != null && schemaVersion != null && description != null;
	}

	@Override
	public String toString()
	{
		return "(" + termID + ", " + schemaName + ", " + schemaVersion + ", " + description + ")";
	}
}
