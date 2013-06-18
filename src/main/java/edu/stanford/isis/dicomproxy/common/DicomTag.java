/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.common;

import org.dcm4che2.data.Tag;

/**
 * Make the DCM4CHEE Tag class strongly typed for Maps.
 *
 * @author amsnyder
 */
public class DicomTag {

    private final int tag;

    private DicomTag(int tag){
        this.tag=tag;
    }

    /**
     *
     * @param tagName - String as in class org.dcm4che2.data.Tag
     * @return
     */
    public static DicomTag forName(String tagName){
        return new DicomTag(Tag.forName(tagName));
    }

    /**
     *
     * @param tagValue - This value is not verified.
     * @return
     */
    public static DicomTag forInt(int tagValue){
        return new DicomTag(tagValue);
    }

    /**
     *
     * @param value in formatUidToDir: (0018,9603)
     * @return
     */
    public static DicomTag forBracketFormat(String value){

        value = value.replace("(","");
        value = value.replace(")","");
        value = value.replace(",","");

        return new DicomTag(Tag.toTag(value));
    }

    @Override
    public boolean equals(Object o){
        if(o==this){
            return true;
        }
        if(!(o instanceof DicomTag)){
            return false;
        }
        DicomTag other = (DicomTag)o;
        return (tag==other.tag);
    }

    @Override
    public int hashCode(){
        return 7*tag;
    }

    @Override
    public String toString(){
        return "DicomTag["+Integer.toHexString(tag)+"]";
    }

}
