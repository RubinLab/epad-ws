/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server.managers;

/**
 * This process keeps track of the least recently used studies and when
 * space is needed will delete the oldest studies first.
 *
 * Keeps information in z_delete_manager.dat in each study directory, and at
 * the dicom directory level.
 *
 * @author amsnyder
 */
public class DeleteManager {
    private static DeleteManager ourInstance = new DeleteManager();

    public static DeleteManager getInstance() {
        return ourInstance;
    }

    private DeleteManager() {
    }
}
