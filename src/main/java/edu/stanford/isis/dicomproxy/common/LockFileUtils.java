/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.common;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Reads and write lock files.
 */
public class LockFileUtils {

    private LockFileUtils(){}

    public enum LockType{
        PIPELINE,
        LOCALSTORE
    }


    /**
     * Add a *.lock file to a directory if it doesn't already exist.
     * @param dir File directory to check.
     * @param lockType LockType
     * @return boolean true if the file was added, or it already exists.
     */
    public static boolean lockDir(File dir, LockType lockType){
        isDirectory(dir);
        if( !containsLock(dir,lockType)){
            String contents = ProxyDateFormatter.currentTimeInFormat(ProxyDateFormatter.YEAR_MONTH_DATE_HMS);
            File lockFile = new File(dir.getAbsoluteFile()+"/"+lockType.name()+".lock");
            return ProxyFileUtils.overwrite(lockFile,contents);
        }
        return true;
    }

    /**
     *
     * @param dir File the Directory to check
     * @param lockType LockType
     * @return boolean if this directory has lock of type.
     */
    public static boolean containsLock(File dir, final LockType lockType){
        isDirectory(dir);

        String[] lockFiles = dir.list( new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(lockType.name()+".lock");
            }
        });

        return lockFiles.length>0;
    }//containsLock

    /**
     * Checks that this is a directory and throws an IllegalArgumentException if it isn't.
     * @param dir File
     * @throws IllegalArgumentException if not a directory.
     */
    private static void isDirectory(File dir)throws IllegalArgumentException
    {
        if( !dir.isDirectory() ){
            throw new IllegalArgumentException("Not a directory. file="+dir.getAbsolutePath());
        }
    }

}//class
