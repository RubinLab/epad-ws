package edu.stanford.epad.epadws.listener;

import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.stanford.epad.epadws.Main;
import edu.stanford.epad.epadws.handlers.admin.ServerStatusHandler;

/**
 * <p>StartupListener class used to initialize stuff at Startup (including Spring Context).
 */
public class StartupListener extends ContextLoaderListener
    implements ServletContextListener {
    
    private static final Log log = LogFactory.getLog(StartupListener.class);
    
    private static ApplicationContext appContext;
    private static String webAppURL;
    private static String webAppPath;
    
    public void contextInitialized(ServletContextEvent event) {
    	// Skip, if we are using embedded Jetty
    	if (Main.embeddedJetty)
    		return;
		log.info("#####################################################");
		log.info("############# Starting ePAD Web Service #############");
		log.info("#####################################################");

        // call Spring's context ContextLoaderListener to initialize
        // all the context files specified in web.xml
        super.contextInitialized(event);

        ServletContext servletContext = event.getServletContext();
       	appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    	webAppPath = servletContext.getRealPath("/");
    	try
    	{
        	URL url = servletContext.getResource("/");
        	webAppURL = "http:/" + url.getPath(); // Does not look correct
    		System.out.println("Context initialized , webAppUrl=" + webAppURL + " webappPath=" + webAppPath);
    	}
    	catch (Exception x) {}
		Main.initializePlugins();
		Main.loadPluginClasses();
		Main.startSupportThreads();
		new ServerStatusHandler(); // Sets startup time
    }

	public static ApplicationContext getAppContext() {
		return appContext;
	}

	public static String getWebAppPath() {
		return webAppPath;
	}

	public static String getWebAppURL() {
		return webAppURL;
	}

}
