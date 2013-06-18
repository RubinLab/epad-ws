/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Creates an MD5Hash
 *
 * @author amsnyder
 */
public class DicomHashUtil {

    MessageDigest md;

    public DicomHashUtil(){
        try{
            md = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException nsae){

        }

    }

    /**
     * Return an MD5 Hash of an arbitrary string.
     * @param input
     * @return
     */
    public static String hash(String input) //throws Exception
    {
        return input.replace('.','_');
    }


    /**
     * Make a string into an MD5 hash.
     * @param input
     * @return String MD5 hash
     * @throws Exception
     */
    public String md5Hash(String input)
        throws Exception
    {
        byte[] in = input.getBytes("UTF-8");
        md.digest(in);
        return new String(md.digest());
    }

}
