package edu.stanford.isis.epad.plugin.server;
/**
 *   This interface defines a class that will obtain the parameters from the
 *   proxy-config.properties file.
 */
public interface EPadProxyConfig
{
	/**
	 * Return the value of the parameter.  
	 * <p>If the parameter is not set, return null.</p>
	 *
	 * @param key name of property to be returned
	 * @return value of property
	 */
    public String getProxyConfigParam(String key);
    /**
     * Return the value of the parameter.
     * <p>If the parameter is not set, return the default value.</p>
     * @param key name of property to be returned
     * @param defaultValue default value of property
     * @return value of property
     */
    public String getProxyConfigParam(String key, String defaultValue);
}
