package edu.stanford.isis.epad.plugin.server.impl;

import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import edu.stanford.isis.epad.plugin.server.PluginHandler;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Place to keep the class loader test code.
 *
 * @author amsnyder
 */
public class ClassFinderTestUtils
{
    private static final ProxyLogger logger = ProxyLogger.getInstance();

    /**
     *
     * @param clazz Class
     * @return String
     */
    public static String readJarManifestForClass(Class clazz){

        StringBuilder sb = new StringBuilder();
        try{

            String className = clazz.getSimpleName() + ".class";
            String classPath = clazz.getResource(className).toString();
            if (!classPath.startsWith("jar")) {
                // Class not from JAR
                return "Class "+className+" not from jar file.";
            }

            String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) +
            "/META-INF/MANIFEST.MF";
            Manifest manifest = new Manifest(new URL(manifestPath).openStream());
            Attributes attr = manifest.getMainAttributes();
            String value = attr.getValue("Manifest-Version");


            Set<Object> keys = attr.keySet();
            for(Object currKey : keys){
                String currValue = attr.getValue(currKey.toString());
                if(currValue!=null){
                    sb.append(currKey.toString()).append(" : ").append(currValue).append("\n");
                }else{
                    sb.append(currKey.toString()).append(": Didn't have a value");
                }
            }
        }catch(Exception e){
            logger.warning("Failed to read manifest for "+clazz.getSimpleName(),e);
        }

        return sb.toString();

    }



    /**
     * Methods to find classes and annotations
     */
    public static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException
    {
        List<Class> classes = new ArrayList<Class>();
        try{
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            //ClassLoader classLoader = ClassLoader.getSystemClassLoader();

            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            List<File> dirs = new ArrayList<File>();
            while (resources.hasMoreElements())
            {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }

            logger.info("getClasses has: "+dirs.size()+" directories.");

            for (File directory : dirs)
            {
                logger.info("getClasses - dir="+directory);
                classes.addAll(ClassFinderTestUtils.findClasses(directory, packageName));
            }

        }catch(Exception e){
            logger.warning("getClass had: "+e.getMessage(),e);
        }

        return classes;
    }



    public static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException
    {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists())
        {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            }
            else if (file.getName().endsWith(".class"))
            {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }


    public static boolean isPluginHandler(Class testClass){
        return testClass.isAnnotationPresent(PluginHandler.class);
    }

    public static boolean hasAnnotation(Class testClass, Class<? extends Annotation> annotationClass)
    {
        return testClass.isAnnotationPresent(annotationClass);
    }


    public static List<Method> findMethodsWithAnnotation(Class testClass, Class<? extends Annotation> annotationClass)
    {
        List<Method> retVal = new ArrayList<Method>();
        for (Method method : testClass.getMethods())
        {
            if (method.isAnnotationPresent(PluginHandler.class))
            {
                retVal.add(method);
                System.out.println("Found ePad plugin-class: "+testClass.getName());
            }
        }
        return retVal;
    }

    public static String printClasspath()
    {
        StringBuilder sb = new StringBuilder();
        try{
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

            URL[] urls = ((URLClassLoader)systemClassLoader).getURLs();
            for(URL url : urls){
                sb.append(url.getFile()).append("\n");
            }

        }catch (Exception e){
            logger.warning("printClasspath failed for: ",e);
        }
        return sb.toString();
    }

    public static void classForName(){
        try{
            Class clazz = Class.forName("edu.stanford.isis.plugins.first.FirstHandler");
            if(clazz!=null){
                logger.info("found FirstHandler by Class.forName");
                //ToDo: add this here.
            }else{
                logger.info("FirstHandler was not found");
            }
        }catch (Exception e){
            logger.sever("classForName had: ",e);
        }catch (IncompatibleClassChangeError err){
            logger.sever(err.getMessage(),err);
        }
    }

    public static void dynamicClassLoader()
    {
        try{
            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
             // Step 2: Define a class to be loaded.
            String classNameToBeLoaded = "edu.stanford.isis.plugins.first.FirstHandler";
             // Step 3: Load the class
            Class myClass = myClassLoader.loadClass(classNameToBeLoaded);

            if(myClass!=null){
                logger.info("dynamicClassLoader found FirstHandler.");
                // Step 4: create a new instance of that class
                Object whatInstance = myClass.newInstance();
            }else{
                logger.info("FirstHandler was not found");
            }
        }catch (Exception e){
            logger.warning(e.getMessage(),e);
        }catch (IncompatibleClassChangeError err){
            logger.warning(err.getMessage(),err);
        }
    }


    public static void classFinderMethod()
    {
        //ToDo: try this again after reading jar file manifest.

//        logger.info("trying classFinderMethod ... ");
//
//        ClassFinder cf = new ClassFinder();
//        Vector<Class<?>> pluginClasses = cf.findSubclasses("edu.stanford.isis.plugins");
//
//        if(pluginClasses!=null){
//
//            for(Class<?> currClass : pluginClasses){
//                logger.info("plugin class: "+currClass.getName());
//            }
//        }else{
//            logger.info("No classes found.");
//        }

    }



}
