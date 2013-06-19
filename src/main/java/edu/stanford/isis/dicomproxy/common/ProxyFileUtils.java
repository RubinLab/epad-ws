/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.common;

import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Utility for writing generic files with text data.
 */
public class ProxyFileUtils
{
    private static final ProxyLogger log = ProxyLogger.getInstance();

    ProxyFileUtils(){}

    /**
     * True if the write succeed otherwise it is false.
     * @param file - File including full directory path to write.
     * @param contents - String complete contents of file.
     * @return boolean
     */
    public static boolean write(File file, String contents){
        try{
            Writer out = new BufferedWriter(new FileWriter(file));
            out.write(contents);
            out.flush();
            out.close();
            return true;
        }catch(Exception e){
            log.warning("Failed to write file: "+file.getAbsolutePath(),e);
            return false;
        }
    }

    /**
     * User this instead of write when a file will be over-written often.
     * @param file File the file to over-write.
     * @param contents String
     * @return boolean
     */
    public static boolean overwrite(File file, String contents){
        String tempFilename = file.getAbsoluteFile()+"."+UUID.randomUUID().toString()+".tmp";
        File tempFile = new File(tempFilename);

        return write(tempFile, contents) && tempFile.renameTo(file);
    }//overwrite

    /**
     * Read a text based file.
     * @param file File the file to read
     * @return String content of the file if it is text.
     * @throws IOException if exception happens during read
     */
    public static String read(File file)
        throws IOException
    {

        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        StringBuilder sb = new StringBuilder();
        String line;
        while( (line=br.readLine())!=null ){
            sb.append(line).append("\n");
        }
        br.close();
        in.close();
        fstream.close();
        return sb.toString();
    }

    /**
     * Read a file that has a "comma-separated-value" format, as a List of Maps.
     * This file format looks like the following:
     *
     * line[1]:   Key1  , Key2  , Key3
     * line[2]:   Value1, Value2, Value3
     * line[3]:   ValueA, ValueB, ValueC
     *
     * This returns a List with two Maps. This first Map is (key1=Value1,Key2=Value2,...). The
     * second Map is (key1=ValueA, key2=ValueB, ...).
     *
     * @param file File
     * @return List of Maps of String to String
     * @throws IllegalArgumentException is the file format is not valid.
     * @throws IOException with read error
     */
    public static List<Map<String,String>> readCsvFormattedFile(File file)
        throws IOException
    {
        String content = read(file);
        List<Map<String,String>> retVal = new ArrayList<Map<String, String>>();

        String errorMsg = checkForValidCsvFormat(content);
        if(!"".equals(errorMsg)){
            throw new IllegalArgumentException(errorMsg);
        }//if

        //parse file contents.
        String currVal="";
        try{
            String[] lines = content.split("\n");
            String[] keys = lines[0].split(",");

            for(int i=1; i<lines.length; i++){
                currVal = lines[i];
                String[] values = lines[i].split(",");
                Map<String,String> valueMap = createMap(keys,values);
                retVal.add(valueMap);
            }//for

        }catch(Exception e){
            log.warning("readCsvFormattedFile had: "+e.getMessage()+" for _"+currVal+"_",e);
        }

        return retVal;
    }

    /**
     * Read a file with the following format:
     *
     * ## This is a comment
     * key1=value1
     * key2=value2
     *
     * It will return a Map with key the text before the first = sign.
     *
     * @param file File
     * @return Map of String key to String values
     * @throws java.io.IOException on read error
     */
    public static Map<String,String> readKeyValueFormattedFile(File file)
        throws IOException
    {
        String content = read(file);

        Map<String,String> retVal = new HashMap<String,String>();
        try{
            String[] lines = content.split("\n");
            for(String currLine : lines){
                currLine = currLine.trim();
                if(currLine.startsWith("#")){
                    continue;
                }
                String[] parts = currLine.split("=",2);
                if(parts.length==2){
                    retVal.put(parts[0].trim(),parts[1].trim());
                }
            }//for

        }catch (Exception e){
            log.warning("readKeyValueFormattedFile had: "+e.getMessage()+" for _"+content+"_",e);
        }

        return retVal;
    }

    /**
     *
     * @param keys String[] keys
     * @param values String[] values
     * @return Map
     */
    private static Map<String,String> createMap(String[] keys, String[] values)
    {
        Map<String,String> retVal = new HashMap<String,String>();

        for(int i=0; i<keys.length; i++){
            retVal.put(keys[i],values[i]);
        }
        return retVal;
    }

    /**
     * Test a String to see if it is a valid CSV format. Return an empty string if it is valid.
     * If it isn't valid then return an error message for the reason.
     *
     * Here are the rules:
     *  (a) The String must have at least two line.
     *  (b) On each line the number of commas must be the same.
     *
     * @param contents String
     * @return String
     */
    public static String checkForValidCsvFormat(String contents)
    {
        String[] lines = contents.split("\n");
        if( lines.length<2 ){
            return "Too few lines";
        }

        final int INIT = -1;
        int numCommas = INIT;
        for(String line : lines){
            int n = countOccurrences(line, ',');
            if(numCommas==INIT){
                numCommas=n;
            }else{
                if(numCommas!=n){
                    return "Number of , characters need to be "+numCommas+". It was "+n+" for "+line;
                }
            }
        }//for
        //Maybe a better check here.
        int nFirstLine=INIT;
        for(String currLine : lines){
            String[] parts = currLine.split(",");
            if(nFirstLine==INIT){
                nFirstLine=parts.length;
            }else{
                if(parts.length!=nFirstLine){
                    return "Expected "+nFirstLine+" but found "+parts.length+" parts. For=_"+currLine+"_";
                }
            }
        }

        return "";
    }//checkForValidCsvFormat

    /**
     * Count of number of occurrences of a certain character within a String.
     * @param line String
     * @param check char
     * @return int
     */
    public static int countOccurrences(String line, char check){
        int count = 0;
        for(int i=0; i<line.length(); i++){
            if(line.charAt(i)==check){
                count++;
            }
        }
        return count;
    }

    /**
     * Find all the files names with a certain extension in the specified directory.
     * @param dir File directory to check
     * @param extension String file ending to look for
     * @return List of Files where file ending matches extension parameter.
     */
    public static List<File> getAllFilesWithExtension(File dir, final String extension){
        File[] files = dir.listFiles( new FileFilter(){
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(extension);
            }
        });
        if(files!=null){
            return Arrays.asList(files);
        }else{
            //return an empty list of no files.
            return new ArrayList<File>();
        }
    }

    /**
     * Get all the directories under the file specified. If it isn't a directory
     * then
     * @param dir File
     * @return List of File
     */
    public static List<File> getDirectoriesIn(File dir){
        List<File> retVal = new ArrayList<File>();
        try{
            if( !dir.isDirectory() ){
                throw new IllegalArgumentException("Not a directory!!");
            }

            File[] candidates = dir.listFiles();
            for(File curr : candidates){
                if(curr.isDirectory()){
                    retVal.add(curr);
                }
            }
        }catch (Exception e){
            log.warning("Had:"+e.getMessage()+" for "+dir.getAbsolutePath(),e );
        }
        return retVal;
    }

    public static int countFilesWithEnding(String dirPath, String ending){
        return countFilesWithEnding(new File(dirPath),ending);
    }

    /**
     * Count the total number of files in a directory with a give ending. Recursively
     * descend down the directory structure.
     * @param dir File
     * @param ending String
     * @return int total files with this ending.
     */
    public static int countFilesWithEnding(File dir, final String ending){
        if(!dir.isDirectory() ){
            throw new IllegalStateException("Not a directory: "+dir.getAbsolutePath());
        }

        File[] files = dir.listFiles( new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(ending);
            }
        } );
        if(files==null){
            return 0;
        }
        int count = files.length;
        for(File currFile : files){
            if(currFile.isDirectory()){
                count += countFilesWithEnding(currFile,ending);
            }
        }

        return count;
    }

    /**
     * Concatenates a file name to a directory path.
     * @param dir String
     * @param name String
     * @return String
     */
    public static String appendNameToDir(String dir, String name){
        StringBuilder sb = new StringBuilder(dir);
        if(!dir.endsWith("/")){
            sb.append("/");
        }
        sb.append(name);
        return sb.toString();
    }

    /**
     * Replace the extension of a file with the a new extension.
     * @param file File the file to use.
     * @param newExt String the extension that will replace the other.
     * @return String the new file with the extension replaced.
     */
    public static String replaceExtensionWith(File file, String newExt){
        StringBuilder sb = new StringBuilder(fileAbsolutePathWithoutExtension(file));
        if( !newExt.startsWith(".") ){
            sb.append(".");
        }
        sb.append(newExt);
        return sb.toString();
    }

    /**
     * Replace the extension of a file with the a new extension.
     * @param filePath String the file path to use.
     * @param newExt String the extension that will replace the other.
     * @return String the new file with the extension replaced.
     */
    public static String replaceExtensionWith(String filePath, String newExt){
        return replaceExtensionWith(new File(filePath), newExt);
    }

    /**
     * Check a specific file for the extension. Return true if the file has the extension.
     * The file-ending are NOT case sensitive.
     * @param checkFile File the file to check.
     * @param extension String the extension to look for.
     * @return boolean if this matches extension.
     */
    public static boolean isFileType(File checkFile, String extension){
        String name = checkFile.getName().toLowerCase();
        return name.endsWith(extension.toLowerCase());
    }

    /**
     *
     * @param name String
     * @return String
     */
    private static String removeExtension(String name){
        int lastDotIndex = name.lastIndexOf('.');
        if(lastDotIndex<1){
            return name;
        }

        String ext = name.substring(lastDotIndex);
        ext = ext.replace('.',' ').trim();
        if( isNumber(ext) ){
            return name;
        }

        return name.substring(0,lastDotIndex);
    }

    /**
     *
     * @param f File
     * @return String
     */
    public static String getExtension(File f){
        if(!f.exists()){
            throw new IllegalStateException("File does not exists. file: "+f.getAbsolutePath());
        }

        String name = f.getName().toLowerCase();
        int dot = name.lastIndexOf('.');
        return name.substring(dot+1);
    }


    /**
     * Return true if the file doesn't have an extension.
     * @param f File
     * @return boolean
     */
    public static boolean hasExtension(File f){
        if(!f.exists()){
            throw new IllegalStateException("File does not exists. file: "+f.getAbsolutePath());
        }

        String name = f.getName().toLowerCase();
        int dot = name.lastIndexOf('.');

        return dot>0;
    }


    /**
     * Looks for an extension, which is the last dot '.' in a file name, but only
     * if the characters are alpha to distinguish it from DicomUIDs.
     * @param f File ex.  ./dir1/dir2/SomeFileName.ext
     * @return String - ./dir1/dir2/SomeFileName
     */
    public static String fileAbsolutePathWithoutExtension(File f)
    {
        String fullPath;
        try{
            fullPath = f.getCanonicalPath();
        }catch (IOException ioe){
            fullPath = f.getAbsolutePath();
        }
        return removeExtension(fullPath);
    }

    /**
     * Looks for an extension, which is the last dot '.' in a file name, but only
     * if the characters are alpha to distinguish it from DicomUIDs.
     * @param f File ex.  ./dir1/dir2/SomeFileName.ext
     * @return String - SomeFileName
     */
    public static String fileNameWithoutExtension(File f)
    {
        return removeExtension( f.getName() );
    }

    private static boolean isNumber(String checkForNumber){

        for (int i = 0; i < checkForNumber.length(); i++) {
            //If we find a non-digit character we return false.
            if (!Character.isDigit(checkForNumber.charAt(i)))
                return false;
        }

        return true;
    }

    /**
     * Unzip the specified file.
     * @param zipFile String path to zip file.
     * @throws IOException during zip or read process.
     */
    public static void extractFolder(String zipFile) throws IOException
    {
        try{

            log.info("Unzipping: "+zipFile);
            int BUFFER = 2048;
            File file = new File(zipFile);

            ZipFile zip = new ZipFile(file);
            String newPath = zipFile.substring(0, zipFile.length() - 4);

            makeDirs(new File(newPath));
            Enumeration <?> zipFileEntries = zip.entries();

            // Process each entry
            while (zipFileEntries.hasMoreElements())
            {
                // grab a zip file entry
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                File destFile = new File(newPath, currentEntry);
                File destinationParent = destFile.getParentFile();

                // create the parent directory structure if needed
                makeDirs(destinationParent);

                if (!entry.isDirectory())
                {
                    BufferedInputStream is = new BufferedInputStream(zip
                    .getInputStream(entry));
                    int currentByte;
                    // establish buffer for writing file
                    byte data[] = new byte[BUFFER];

                    // write the current file to disk
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos,
                    BUFFER);

                    // read and write until last byte is encountered
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }

                if (currentEntry.endsWith(".zip")){
                    // found a zip file, try to open
                    extractFolder(destFile.getAbsolutePath());
                }
    //            if(currentEntry.endsWith(".gz")){
    //               // found a gz file, try to decompress it.
    //               FileUtils.
    //            }
            }//while


        }catch(Exception e){
            //Catch and throw this exception to log some information about it.
            log.warning("Failed to unzip: "+zipFile,e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * Check to see if the file exists. If it doesn't then move it to the new location.
     * @param from File - The file in the old location.
     * @param to File - The file in the new location.
     * @throws IOException during move.
     */
    public static void checkAndMoveFile(File from, File to)
        throws IOException
    {
        //If the directory doesn't exist create it.
        File parentDir = to.getParentFile();
        if( !parentDir.exists() ){
            ProxyFileUtils.makeDirs(parentDir);
        }

        if( !to.exists() ){
//            log.info("Moving file to: "+to.getCanonicalPath());
            FileUtils.moveFile(from, to);
        }else{
            //log.info("Not moving file since it already exists. file="+to.getCanonicalPath());
            FileUtils.deleteQuietly(from);
        }
    }//checkAndMove

    /**
     * Make a directory in a thread-safe manner in case multiple threads
     * try to make it at the same time.
     * @param dir File the directory(ies) to make.
     * @return boolean true if the process was successful, or the directory already exists.
     */
    public static boolean makeDirs(File dir){
        //make a key based on the name.
        FileKey lock = new FileKey(dir);

        synchronized (lock.toString()){
            if( !dir.exists() ){
                return dir.mkdirs();
            }
        }
        return true;
    }

    public static boolean createDirsAndFile(File file){

        boolean success = makeDirs(file.getParentFile());
        if( !success ){
            return false;
        }

        FileKey lock = new FileKey(file);
        synchronized (lock.toString()){
            if( !file.exists() ){
                try{
                    return file.createNewFile();
                }catch (IOException ioe){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Delete a directory and all of its contents.
     * @param dirToDelete File
     * @return boolean true if everything deleted.
     */
    public static boolean deleteDirAndContents(File dirToDelete)
    {
        try{
            List<File> dirs = getDirectoriesIn(dirToDelete);
            //delete all the sub-directories.
            for(File currDir : dirs){
                if( !deleteDirAndContents(currDir) ){
                    throw new IllegalStateException("Filed to delete dir="+dirToDelete.getAbsolutePath());
                }
            }
            //delete all content files.
            File[] files = dirToDelete.listFiles();
            for(File currFile : files){
                if ( !currFile.delete() ){
                    throw new IllegalStateException("Could not delete file="+currFile.getAbsolutePath());
                }
            }//for

            //delete the root directory.
            if( !dirToDelete.delete() ){
                throw new IllegalStateException("Could not delete: "+dirToDelete.getAbsolutePath());
            }
            return true;

        }catch (IllegalStateException ise){
            //log.info(ise.getMessage());
            return false;
        }catch (Exception e) {
            //log.warning("Had: "+e.getMessage()+" for "+dirToDelete.getAbsolutePath(),e);
            return false;
        }
    }//deleteDirAndContents

}
