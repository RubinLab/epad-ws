/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.common;

/**
 * To strongly type the IDs which are Strings.
 *
 * ID look like:
 *
 * 1.2.826.0.1.3680043.8.420.14244124819651886510040305253484131351
 *
 *  - or -
 *
 * 281871669181413707956703696581366588089
 *
 * @author amsnyder
 */
public abstract class DicomUID {

    String id;

    public DicomUID(String idString){
        id = idString;
    }

    @Override
    public String toString(){
        return id;
    }
}
