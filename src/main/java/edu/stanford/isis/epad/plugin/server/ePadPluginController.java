package edu.stanford.isis.epad.plugin.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Request all server-side interfaces from here
 */
public class ePadPluginController {
    private static ePadPluginController ourInstance = new ePadPluginController();

    public static ePadPluginController getInstance() {
        return ourInstance;
    }

    //ToDo: Each plug-in should have its own logger and log file.
    Logger logger = PluginLogger.getLogger();

    EPadFiles ePadFiles=null;

    EPadProxyConfig ePadProxyConfig=null;

    /***
     * Get ePadFiles and ePadProxyConfig via reflection.
     */
    private ePadPluginController() {

    }


    public void setImpl(EPadFiles ePadFiles){
        this.ePadFiles = ePadFiles;
    }

    public Logger getLogger(){
        return logger;
    }

    public EPadFiles getEPadFiles(){
        return ePadFiles;
    }


    public EPadProxyConfig getEPadProxyConfig(){
        return ePadProxyConfig;
    }


    public String getInterfaceVersion(){
        return EPadPlugin.PLUGIN_INTERFACE_VERSION;
    }


    /*******************
     *
     *******************/

    @SuppressWarnings("unused")
	private void logExceptionWithDetails(String name, Exception e){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" version: ").append(EPadPlugin.PLUGIN_INTERFACE_VERSION);

//        logger.info("ePadProxyConfig version is: " + EPadPlugin.PLUGIN_INTERFACE_VERSION);
//        logger.warning("ePadPluginController failed for " + e.getMessage());
//        e.printStackTrace();

        logger.warning(sb.toString());
        e.printStackTrace();
    }

    @SuppressWarnings("unused")
	private void debugClassCastException(Class<?> clazz){

        StringBuilder sb = new StringBuilder();
        sb.append("ClassName: "+clazz.getName()).append("\n");
        //methods
        Method[] methods = clazz.getMethods();
        sb.append("has ").append(methods.length).append(" methods.").append("\n");
        for(Method method : methods){
            sb.append("method:").append(method.getName()).append("\n");
        }
        //constructors
        Constructor<?>[] ctors = clazz.getConstructors();
        sb.append("has ").append(ctors.length).append(" ctors.").append("\n");
        for(Constructor<?> ctor : ctors){
            sb.append("CTors name: ").append(ctor.getName()).append("\n");
        }
        //interfaces
        Class<?>[] interfaces = clazz.getInterfaces();
        sb.append("has ").append(interfaces.length).append("\n");
        for(Class<?> ifc : interfaces){
            sb.append("interface:").append(ifc.getName()).append("\n");
        }
        Class<?> superClass = clazz.getSuperclass();
        if(superClass!=null){
            sb.append("super class: ").append(superClass.getName()).append("\n");
        }

        logger.info(sb.toString());
    }

}
