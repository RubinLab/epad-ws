package edu.stanford.isis.epadws.resources;

import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * Resource representing a window level
 * 
 * @author martin
 */
public interface WindowLevelResource
{
	public final String COMMAND_NAME = "cmd";

	@Get
	public String query();

	@Post
	public String create();

	enum WindowLevelCommand {
		CREATE("create"), QUERY("query");

		private final String commandName;

		WindowLevelCommand(String commandName)
		{
			this.commandName = commandName;
		}

		public String getCommandName()
		{
			return commandName;
		}

		public boolean hasCommandName(String otherName)
		{
			return (otherName == null) ? false : commandName.equalsIgnoreCase(otherName);
		}
	}
}