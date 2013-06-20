/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epad.plugin.server.impl;

import edu.stanford.isis.epad.plugin.server.PluginServletHandler;
import edu.stanford.isis.epadws.server.ProxyLogger;

import java.util.*;

/**
 * Keep all the registered handlers around.
 */
public class PluginHandlerMap {

    private static ProxyLogger logger = ProxyLogger.getInstance();
    private static PluginHandlerMap ourInstance = new PluginHandlerMap();

    private final Map<String,PluginServletHandler> pluginServletMap = new HashMap<String,PluginServletHandler>();

    public static PluginHandlerMap getInstance() {
        return ourInstance;
    }

    private PluginHandlerMap() {

    }

    /**
     * Normalize the name, so small variations map to something similar.
     * @param pluginName String
     * @return String
     */
    public static String normalizeName(String pluginName)
    {
        String retVal = pluginName.replace('/',' ').replace('?',' ').toLowerCase();
        return retVal.trim();
    }


    public Set<String> getAllPluginNames(){
        return pluginServletMap.keySet();
    }

    /**
     *
     * @param pluginName String
     * @param pluginServletHandler PluginServletHandler
     * @return boolean
     */
    public boolean setPluginServletHandler(String pluginName, PluginServletHandler pluginServletHandler){

        //normalize the name
        String cleanedPluginName = normalizeName(pluginName);

        if(pluginServletMap.containsKey(cleanedPluginName)){
            logger.info("WARNING: The ePad Plugin: "+cleanedPluginName+" already exists.");
            return false;
        }else{
            logger.info("Adding ePadPlugin: "+cleanedPluginName);
            pluginServletMap.put(cleanedPluginName,pluginServletHandler);
            return true;
        }
    }

    public PluginServletHandler getPluginServletHandler(String pluginName){
        String cleanedPluginName = normalizeName(pluginName);
        return pluginServletMap.get(cleanedPluginName);
    }


    /**
     * Takes a plugin class name and returns a PluginServletHandler if it exists.
     * @param className String ex. edu.stanford.isis.plugins.first.FirstHandler
     * @return PluginServletHandler class if it exists and is right type, null if not.
     */
    public PluginServletHandler loadFromClassName(String className){
        try{
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            Class<?> clazz = systemClassLoader.loadClass(className);
            if(clazz!=null){
                if(ClassFinderTestUtils.isPluginHandler(clazz)){
                     if((clazz.newInstance() instanceof PluginServletHandler)){
                        logger.info(className+" is instanceof PluginServletHandler");
                        return (PluginServletHandler)clazz.newInstance();
                     }else{
                         return null;
                     }
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }catch (Exception e){
            logger.warning(e.getMessage(),e);
        }catch (Error err){
            logger.sever(err.getMessage(),err);
        }
        return null;
    }

    public void classLoaderForName(){

        try{
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            @SuppressWarnings("unused")
			Class<?> clazzA = systemClassLoader.loadClass("edu.stanford.isis.plugins.first.FirstHandler");

            Class<?> clazz = systemClassLoader.loadClass("edu.stanford.isis.plugins.first.FirstHandler");

            if(clazz!=null){
                logger.info("found FirstHandler by loadClass");

                if( ClassFinderTestUtils.isPluginHandler(clazz)){
                    logger.info("FirstHandler has @PluginHandler annotation.");

                    //verify that this is a PluginHandlerMap class.
                    if((clazz.newInstance() instanceof PluginServletHandler)){
                        logger.info("FirstHandler is instanceof PluginServletHandler");
                        PluginServletHandler plugHandler = (PluginServletHandler)clazz.newInstance();

                        String name = plugHandler.getName();
                        setPluginServletHandler(name,plugHandler);

                    }else{
                        Object o = clazz.newInstance();
                        logger.info("NOT INSTANCE-OF: "+o.toString()+" FirstHandler");
                    }
                }

            }else{
                logger.info("FirstHandler was not found..");
            }

        }catch(Exception e){
            logger.sever("classLoaderForName had: ",e);
        }catch (IncompatibleClassChangeError err){
            logger.sever(err.getMessage(),err);
        }
    }

}
