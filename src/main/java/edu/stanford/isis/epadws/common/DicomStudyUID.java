/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.common;

/**
 *
 * @author amsnyder
 */
public class DicomStudyUID extends DicomUID {
    public DicomStudyUID(String idString) {
        super(idString);
    }

    @Override
    public boolean equals(Object o){
        if(o==this) return true;
        if(!(o instanceof DicomStudyUID)) return false;

        DicomStudyUID dsu = (DicomStudyUID) o;
        return id.equals(dsu.id);
    }

    @Override
    public int hashCode(){
        return 31+id.hashCode();
    }
}
