package edu.stanford.isis.epad.plugin.server;

/**
 * Plugins use the Exception to include human readable text messages that
 * need to appear in a dialog box when reporting an error.
 *
 * @author alansnyder
 */
public class PluginServletException extends Exception {

    private static final long serialVersionUID = -6586646313136630786L;
	
    String humanReadableMessage;

    public PluginServletException(){
        super();
        humanReadableMessage="unknown";
    }

    /**
     *
     * @param humanReadableMessage String That will appear on ePad in dialog box. This is intended for the user.
     * @param message String That will appear in log file, intended for the developer.
     */
    public PluginServletException(String humanReadableMessage, String message){
        super(message);
        this.humanReadableMessage=humanReadableMessage;
    }

    /**
     * Get the error message text that should appear in the ePad warning dialog box.
     * @return String error message for users.
     */
    public String getHumanReadableMessage(){
        return humanReadableMessage;
    }
}
