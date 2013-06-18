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
 * Java Script Overlay Type for /Restful/search/... calls.
 * @author amsnyder
 */
public class StudySearch extends JavaScriptObject {

    protected StudySearch(){}

    public final native String getResult() /*-{ return this.result; }-*/;
    public final native String getMessage() /*-{ return this.message; }-*/;
    public final native JsArray<StudySearchEntry> getList() /*-{ return this.list; }-*/;
}
