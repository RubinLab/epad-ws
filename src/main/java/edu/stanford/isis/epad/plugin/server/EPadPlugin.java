package edu.stanford.isis.epad.plugin.server;

/**
 * This is the interface to the ePAD plugin
 * 
 * TODO Experimental
 */
public interface EPadPlugin {

    /**
     * Returns the version string for the ePadPlugin Implementation.
     * @return ePadPlugin
     */
    String getPluginImplVersion();

    String PLUGIN_INTERFACE_VERSION = "2012-09-10 23:00"; //ToDo: set at compile time

    /**
     * Write the UI code into this layout panel. Call this if you are using GWT widgets.
     * @return LayoutPanel
     */
    //LayoutPanel getGwtUILayoutPanel();              //?client or server or both?

    /**
     * Write the UI code into this DivElement. Call this if you are using HTML.
     * @return DivElement
     */
    //DivElement getGwtUIDivElement();               //?client or server or both?
}
