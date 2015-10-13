package edu.stanford.epad.epadws.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.PluginOperations;

/**
 * Reads the file plugin-config.txt one line at a time. Each line is the class name for the plugin handler.
 * 
 * @author alansnyder
 */
public class PluginConfig
{
	private final PluginOperations pluginOperations = PluginOperations.getInstance();
	private static final EPADLogger log = EPADLogger.getInstance();
	private static PluginConfig ourInstance = new PluginConfig();
	private final List<String> pluginHandlerList = new ArrayList<String>();
	private final List<String> pluginTemplateList = new ArrayList<String>();
	private final List<String> pluginNameList = new ArrayList<String>();

	public static PluginConfig getInstance()
	{
		return ourInstance;
	}

	private PluginConfig()
	{
		try {
			if (!pluginOperations.getPlugins(pluginHandlerList, pluginTemplateList, pluginNameList)) {
				migrate2DB();
			}
		} catch (Exception e) {
			log.info("Failed to read plugin list from database "+e.getMessage());
		}
	}
	
	
	/**
	 * Read plugin list from config file, fill handlerList, templateList and nameList and 
	 * insert the plugin list to database
	 */
	private void migrate2DB() {
		try {
			File configFile = getConfigFile();
			if (!configFile.exists())
				throw new IllegalStateException("No plugin config file: " + configFile.getAbsolutePath());

			BufferedReader br = new BufferedReader(new FileReader(configFile));
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim(); // process the line.
				if (!isCommentLine(line)) {
					log.info("looking for plugin class: " + line); // Add this to the list of plugins.
					String[] linePart = line.split("=");
					if (linePart.length != 3) {
						log.info("Wrong plugin ");
					} else {
						pluginHandlerList.add(linePart[0]);
						pluginTemplateList.add(linePart[1]);
						pluginNameList.add(linePart[2]);
						pluginOperations.createPlugin("admin", linePart[1], linePart[2], "", linePart[0], "true", "", "","","","");
						pluginOperations.setProjectPluginEnable("admin", EPADConfig.xnatUploadProjectID, linePart[1], "true", "");
					}
				}
			}
			br.close();
		} catch (Exception e) {
			log.warning("Failed to read plugin config file for: " + e.getMessage(), e);
		}
		
	}

	private static boolean isCommentLine(String line)
	{
		line = line.trim();
		if (line.startsWith("//"))
			return true;
		if (line.startsWith("#"))
			return true;
		if (line.startsWith("*"))
			return true;

		if ("".equals(line)) // count a blank line as a comment.
			return true;

		return false;
	}

	private File getConfigFile()
	{
		File configFile = new File(EPADConfig.getEPADWebServerPluginConfigFilePath());

		log.info("EPadPlugin config file: " + configFile.getAbsolutePath());
		return configFile;
	}

	/**
	 * Get the list of plugins.
	 * 
	 * @return List of String
	 */
	public List<String> getPluginHandlerList()
	{
		return pluginHandlerList;
	}

	public List<String> getPluginTemplateList()
	{
		return pluginTemplateList;
	}

	public List<String> getPluginNameList()
	{
		return pluginNameList;
	}
}
