/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Read the "etc/proxy-config.properties" file on start-up and provides method to look up values.
 * <ul>
 * <li>
 * ePadClientPort - port number on which Jetty server is listening.
 * </li>
 * <li>
 * ListenIP - IP address or domain name where Dicom server will be listening
 * as a C-STORE SCP.
 * </li>
 * <li>
 * ListenPort - Port number where Dicom server will be listening as a
 * C-STORE SCP.
 * </li>
 * <li>
 * ListenAETitle - This value is not present in the file, but would represent the 
 * application entity title for the Dicom server that will be listening as a
 * C-STORE SCP.  It will be necessary to set up an application entity title to allow
 * the process to accept Dicom instances.
 * </li>
 * <li>
 * DicomServerAETitle - Application entity title for Dicom server from which
 * DicomProxy will retrieve information and instances.
 * </li>
 * <li>
 * DicomServerIP - IP address or domain name for Dicom server from which
 * DicomProxy will retrieve information and instances.
 * </li>
 * <li>
 * DicomServerPort - Port number for Dicom server from which
 * DicomProxy will retrieve information and instances.
 * </li>
 * <li>
 * DicomServerWadoPort
 * </li>
 * <li>
 * WadoUrlExtension
 * </li>
 * <li>
 * MaxImagesInSeries
 * </li>
 * <li>
 * LoggerDebugOn
 * </li>
 * </ul>
 * @author amsnyder
 */
public class ProxyConfig {
    private static ProxyLogger log = ProxyLogger.getInstance();
    private static ProxyConfig ourInstance = new ProxyConfig();

    //private static String PROXY_CONFIG_FILE_PATH = "../etc/proxy-config.properties";

    Properties properties;

    public static ProxyConfig getInstance() {
        return ourInstance;
    }

    private ProxyConfig() {

        try{
            properties = new Properties();
            File configFile = getConfigFile();
            if( !configFile.exists() ){
                throw new IllegalStateException("Could not find file: "+configFile.getAbsolutePath());
            }

            FileInputStream fis = new FileInputStream(configFile);
            properties.load(fis);
            fis.close();

            readProxyLoggerDebugState();

        }catch (Exception e) {
            System.out.println("Error reading config file: "+e.getMessage());
            log.sever("Error reading config file",e);
        }
    }

    private File getConfigFile(){
        File base = ResourceUtils.getDicomProxyBaseDir();

        String configPath = base.getAbsolutePath();
        if( !configPath.endsWith("/") ){
            configPath=configPath+"/";
        }
        File configFile = new File(configPath+"etc/proxy-config.properties");

        System.out.println("Proxy config File: "+configFile.getAbsolutePath());
        //log.info("Config File: "+configFile.getAbsolutePath());

        return configFile;
    }

    /**
     * Returns the values of a key in the proxy-config.properties file.
     * @param name key in config file.
     * @return     the value of that parameter.
     */
    public String getParam(String name){

        return properties.getProperty(name);
    }

    /**
     *  Returns the value of a parameter in proxy-config.properties as an integer.
     * @param name   key
     * @return       the parameter value as an integer
     * @throws IllegalArgumentException if the value is not an integer in the config file.
     */
    public int getIntParam(String name){
        String s = properties.getProperty(name);
        try{
            return Integer.parseInt(s);
        }catch (NumberFormatException nfe){
            throw new IllegalArgumentException("The parameter in : "+name+" needs to be an integer. It was: "+s);
        }
    }

    /**
     * Get all the key,values as a Map of Strings.
     * @return Map of String keys to String values
     */
    public Map<String,String> getAllParams(){
        Set<String> keys = properties.stringPropertyNames();
        Map<String,String> retVal = new HashMap<String,String>();

        for(String key : keys){
            retVal.put(key, properties.getProperty(key));
        }
        return retVal;
    }

    /**
     * Read <code>LoggerDebugOn</code> to set the ProxyLogger debug state.
     */
    private void readProxyLoggerDebugState(){
        String debugOn = getParam("LoggerDebugOn");
        if( "true".equalsIgnoreCase(debugOn) ){
            log.setDebug(true);
        }
    }

}
