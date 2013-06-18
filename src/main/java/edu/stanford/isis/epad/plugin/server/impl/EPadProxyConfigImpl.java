/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epad.plugin.server.impl;

import edu.stanford.isis.dicomproxy.server.ProxyConfig;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import edu.stanford.isis.epad.plugin.server.EPadProxyConfig;

/**
 *
 */
public class EPadProxyConfigImpl implements EPadProxyConfig {

    private ProxyLogger logger = ProxyLogger.getInstance();

    ProxyConfig proxyConfig = ProxyConfig.getInstance();

    public EPadProxyConfigImpl(){
    }

    @Override
    public String getProxyConfigParam(String name) {
        return proxyConfig.getParam(name);
    }

    @Override
    public String getProxyConfigParam(String name, String defaultValue) {

        String retVal = getProxyConfigParam(name);
        if(retVal==null){
            logger.info("WARNING: ProxyConfig using default value: "+name+"="+defaultValue);
            retVal=defaultValue;
        }
        return retVal;
    }

}
