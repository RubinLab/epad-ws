package edu.stanford.isis.dicomproxy;

import java.io.*;
import java.nio.channels.UnsupportedAddressTypeException;

/**
 * Script for starting/stopping/restarting the proxy. It
 *
 * a) builds the startup script by looking for new plugin jar files.
 * b) makes sure only one process is running.
 * c) Allows you to shut-down process.
 *
 * d) other command-line tools that are very quick. (maybe deleting a study for example)
 *
 * Looks for plugin jar in the lib directory and builds a startup script to start the proxy.
 * This should launch like a service.
 *
 * @author alansnyder
 */
public class Launcher {

    /**
     *
     *
     * @param args String  args[0] command   args[1..n] parameters
     */
    public static void main(String[] args){

        int len = args.length;

        if(len==0){
            System.out.println("Missing command.");
            printHelp();
            return;
        }

        String cmd = args[0];
        try{

            if("start".equalsIgnoreCase(cmd)){
                doStart();
                System.out.println("Start done.");
            }else if("stop".equalsIgnoreCase(cmd)){
                doStop();
                System.out.println("Stop done.");
            }else if("restart".equalsIgnoreCase(cmd)){
                main(new String[]{"stop"});
                main(new String[]{"start"});
                System.out.println("Restart done.");
            }else if("status".equalsIgnoreCase(cmd)){
                //implement later, if time.
            }else{
                System.out.println("Unrecognized command: "+cmd);
                printHelp();
            }

        }catch (Exception e){
            System.out.println("Failed: "+cmd+" due to: "+e.getMessage());
            e.printStackTrace();
        }catch (Error error){
            System.out.println("ERROR: "+cmd+" due to: "+error.getMessage());
            error.printStackTrace();
        }
    }

    private static void printHelp(){
        System.out.println("Command are start, stop, restart, status");
    }


    /**
     * Start the application if it isn't already running.
     */
    private static void doStart(){
        System.out.println("Starting ... ");
        buildStartupScript();
        if( !checkRunStatus() ){
            //start it here.
        }else{
            System.out.println("DicomProxy already running.");
            return;
        }

    }


    /**
     * Stop the application.
     */
    private static void doStop(){
        System.out.println("Stopping ... ");
        //stop by sending a "string with the command".
    }

    /**
     *
     */
    private static void buildStartupScript(){

        StringBuilder sb = new StringBuilder();

        sb.append("java -Djava.library.path=/home/epad/dbxml-2.5.16/install/lib");

        //memory
        sb.append(" -Xmx2G -Xms256m");

        //add all jar files to classpath
        sb.append(" -classpath ");
        listAllJarFiles("../lib/", sb, true);

        sb.append(" edu.stanford.isis.dicomproxy.Main");

        writeToFile("start-plugins.sh",sb.toString());

    }

    private static void writeToFile(String name, String content){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(name));
            out.write(content);
            out.close();
        }catch(Exception e){
           System.out.println("Couldn't write: "+name+" for: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listAllJarFiles(String path, StringBuilder sb, boolean first){
        File dir = new File(path);
        if(dir.isDirectory()){

            String[] jarFiles = dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String fileName) {
                    return fileName.endsWith(".jar");
                }
            });

            for(String currJarFile : jarFiles){
                if(!first){
                    sb.append(":");
                    first=false;
                }
                sb.append(path);
                if(!path.endsWith("/")){
                    sb.append("/");
                }
                sb.append(currJarFile);
            }

            //now file all the directories not starting with a . and go through them.
            File[] dirFiles = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });

            for(File currDirFile : dirFiles){

                String name = currDirFile.getName();
                String childDir = path;
                if(!path.endsWith("/")){
                    childDir +="/";
                }
                childDir = childDir+name;

                listAllJarFiles(childDir, sb, first);
            }//for

        }//if
    }

    /**
     * Option a) ps command
     * Option b) send "status" command.
     *
     *
     * @return boolean
     */
    private static boolean checkRunStatus(){
        throw new UnsupportedOperationException("not implemented");
    }

}
