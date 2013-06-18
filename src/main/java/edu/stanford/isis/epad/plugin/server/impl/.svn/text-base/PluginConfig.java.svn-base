package edu.stanford.isis.epad.plugin.server.impl;

import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import edu.stanford.isis.dicomproxy.server.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the file plugin-config.txt one line at a time. Each line is the class name
 * for the plugin handler.
 *
 * @author alansnyder
 */
public class PluginConfig {

    private static final ProxyLogger log = ProxyLogger.getInstance();

    private static PluginConfig ourInstance = new PluginConfig();

    private List<String> pluginHandlerList = new ArrayList<String>();
    private List<String> pluginTemplateList = new ArrayList<String>();
    private List<String> pluginNameList = new ArrayList<String>();
    

	public static PluginConfig getInstance() {
        return ourInstance;
    }

    private PluginConfig() {
        try{
            File configFile = getConfigFile();
            if( !configFile.exists() ){
                throw new IllegalStateException("No plugin config file: "+configFile.getAbsolutePath());
            }

            BufferedReader br = new BufferedReader(new FileReader(configFile));
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                line = line.trim();
                if(!isCommentLine(line) ){
                    //add this to the list of plugins.
                    log.info("looking for plugin class: "+line);
                    String[] linePart =line.split("=");
                    if(linePart.length!=3){
                    	log.info("Wrong plugin ");
                    }else{
                    	pluginHandlerList.add(linePart[0]);
                    	pluginTemplateList.add(linePart[1]);
                    	pluginNameList.add(linePart[2]);
                    }
                }
            }
            br.close();
        }catch(Exception e){
            log.warning("Failed to read plugin config file for: "+e.getMessage(),e);
        }
    }

    private static boolean isCommentLine(String line){
        line = line.trim();
        if(line.startsWith("//"))return true;
        if(line.startsWith("#"))return true;
        if(line.startsWith("*"))return true;

        //count a blank line as a comment.
        if("".equals(line))return true;

        return false;
    }

    private File getConfigFile(){
        File base = ResourceUtils.getDicomProxyBaseDir();

        String configPath = base.getAbsolutePath();
        if( !configPath.endsWith("/") ){
            configPath=configPath+"/";
        }
        File configFile = new File(configPath+"etc/plugin-config.txt");

        log.info("EPadPlugin config file: " + configFile.getAbsolutePath());
        return configFile;
    }

    /**
     * Get the list of plugins.
     * @return List of String
     */
    public List<String> getPluginHandlerList(){
        return pluginHandlerList;
    }

    
    public List<String> getPluginTemplateList() {
		return pluginTemplateList;
	}

	public List<String> getPluginNameList() {
		return pluginNameList;
	}

}
