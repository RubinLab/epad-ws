/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server.managers.localstore;


/**
 * Keeps watch over the local store.
 *
 *
 */
public class LocalStoreFactory {
    //private static final ProxyLogger log = ProxyLogger.getInstance();

    private static LocalStoreFactory ourInstance = new LocalStoreFactory();

    public static LocalStoreFactory getInstance() {
        return ourInstance;
    }

    private LocalStoreFactory() {
    }



}
