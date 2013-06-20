/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server;

/**
 * Interface for columns.
 *
 * @author amsnyder
 */
public interface RDataColumn {

    /**
     * Headers for this data-type.
     * @return
     */
    //String getHeaderColumn();

    /**
     * Data for this data-type.
     * @return
     */
    String getDataColumn();

}
