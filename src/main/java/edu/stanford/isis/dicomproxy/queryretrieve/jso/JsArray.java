/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.queryretrieve.jso;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Base object for arrays.
 * @author amsnyder
 */
public class JsArray<E extends JavaScriptObject> extends JavaScriptObject {
    protected JsArray(){}
    public final native int length()/*-{ return this.length; }-*/;
    public final native E get(int i)/*-{ return this[i]; }-*/;
}
