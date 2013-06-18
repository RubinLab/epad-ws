/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.common;

import java.io.File;
import java.io.IOException;

/**
 * The key for a file is based on the "Absolute Path" as a String.
 *
 * @author amsnyder
 */
public class FileKey {

    final String canonicalPath;
    final File file;

    public FileKey(File f){
        this.file = f;
        canonicalPath = getCanonicalPath(f);
    }

    /**
     * The canonical path is preferred, but use the absolute as a backup.
     * @param f
     * @return
     */
    public static String getCanonicalPath(File f){
        try{
            return f.getCanonicalPath();
        }catch(IOException ioe){
            return f.getAbsolutePath();
        }
    }

    public File getFile(){
        return file;
    }

    @Override
    public int hashCode(){
        return 7* canonicalPath.hashCode()+5;
    }

    @Override
    public boolean equals(Object o){
        if(o==null){
            return false;
        }

        if( !(o instanceof FileKey)){
            return false;
        }
        FileKey that = (FileKey) o;

        return (canonicalPath.equalsIgnoreCase(that.canonicalPath));
    }

    @Override
    public String toString(){
        return canonicalPath;
    }
}
