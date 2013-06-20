/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server.managers.support;

/**
 * Data-structure for passing window level and
 */
public class DicomWindowSetting {

    private int level;
    private int width;

    /**
     *
     * @param windowWidth -
     * @param windowLevel -
     */
    public DicomWindowSetting(int windowWidth, int windowLevel){
        this.level = windowLevel;
        this.width = windowWidth;
    }

    /**
     *
     * @return int
     */
    public int getLevel(){
        return level;
    }

    /**
     *
     * @return int
     */
    public int getWidth(){
        return width;
    }

}
