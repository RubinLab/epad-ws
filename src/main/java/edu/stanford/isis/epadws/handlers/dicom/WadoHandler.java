package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epadws.server.ProxyConfig;
import edu.stanford.isis.epadws.server.ProxyLogger;

/**
 * WandoHandler
 *
 * @author ckurtz
 */
public class WadoHandler extends AbstractHandler {


	private static final ProxyLogger log = ProxyLogger.getInstance();
	ProxyConfig config = ProxyConfig.getInstance();

	public WadoHandler(){}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

		httpServletResponse.setContentType("image/jpeg");
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		request.setHandled(true);
		ServletOutputStream out = httpServletResponse.getOutputStream();

		String queryString = httpServletRequest.getQueryString();
		queryString = URLDecoder.decode(queryString, "UTF-8");
		log.info("Wado query from ePad : "+ queryString);

		if(queryString!=null){

			ProxyConfig config = ProxyConfig.getInstance();

			try {
				//we use wado to get the dicom image		
				String host = config.getParam("NameServer");
				int port = config.getIntParam("DicomServerWadoPort");
				String base = config.getParam("WadoUrlExtension");

				StringBuilder sb = new StringBuilder();
				sb.append("http://").append(host);
				sb.append(":").append(port);
				sb.append(base);
				sb.append(queryString);

				String wadoUrl= sb.toString();
				log.info("Build wadoUrl = "+wadoUrl);

				//--Get the Dicom file from the server
				HttpClient client = new HttpClient();

				GetMethod method = new GetMethod(wadoUrl);
				// Execute the GET method
				int statusCode = client.executeMethod(method);		
				
				if(statusCode != -1 ) {
					//Get the result as stream
					InputStream res = method.getResponseBodyAsStream();
					
					int read = 0;
					byte[] bytes = new byte[4096];

					while ((read = res.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}
					res.close();
					out.flush();
					out.close();
				}


			} catch (UnsupportedEncodingException e) {
				log.warning("Not able to build wado url for  ",e);
			} catch (HttpException e) {
				log.warning("Not able to get the wado image ",e);
			}

		}else{
			log.info("NO header Query from request.");
		}
	}


}
