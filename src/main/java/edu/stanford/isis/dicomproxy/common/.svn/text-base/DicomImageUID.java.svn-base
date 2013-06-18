/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.common;

/**
 * @author amsnyder
 */
public class DicomImageUID extends DicomUID {
    public DicomImageUID(String idString) {
        super(idString);
    }

    @Override
    public boolean equals(Object o){
        if(o==this) return true;
        if(!(o instanceof DicomImageUID)) return false;

        DicomImageUID diu = (DicomImageUID) o;
        return id.equals(diu.id);
    }

    @Override
    public int hashCode(){
        return 43+id.hashCode();
    }
}
