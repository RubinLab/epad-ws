package edu.stanford.isis.epadws.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.jetty.http.HttpException;
import org.restlet.Client;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;

public class WADOServerResource extends BaseServerResource
{
	private static final String NO_QUERY_ERROR_TEXT = "No query specified in request!";
	private static final String WADO_REQUEST_URI_BUILD_ERROR_TEXT = "Error building WADO URI for request";
	private static final String WADO_REQUEST_HTTP_ERROR_TEXT = "Error calling WADO. Status code = ";
	private static final String WADO_REQUEST_IO_ERROR_TEXT = "IO error calling WADO";

	public WADOServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the WADO resource.", throwable);
	}

	@Get("png")
	public Representation operation()
	{
		String queryString = getQuery().getQueryString(CharacterSet.UTF_8);
		log.info("WADO query received from ePAD : " + queryString);

		if (queryString != null) {
			try { // We use WADO to get the DICOM image.
				String wadoURL = buildWADOURL(queryString);
				log.info("WADO URL = " + wadoURL);

				Client client = new Client(getContext(), Protocol.HTTP);
				ClientResource clientResource = new ClientResource(wadoURL);
				clientResource.setNext(client);
				Representation representation = clientResource.get();
				Status clientResponse = clientResource.getStatus();

				if (clientResponse == Status.SUCCESS_OK) {
					InputStream stream = representation.getStream();
					setStatus(Status.SUCCESS_OK);
					return new DICOMStreamWriter(stream); // We stream the representation back to the client.
				} else {
					log.info(WADO_REQUEST_HTTP_ERROR_TEXT + clientResponse);
					setStatus(Status.SERVER_ERROR_INTERNAL);
					return new StringRepresentation(WADO_REQUEST_HTTP_ERROR_TEXT + clientResponse);
				}
			} catch (UnsupportedEncodingException e) {
				log.warning(WADO_REQUEST_URI_BUILD_ERROR_TEXT, e);
				setStatus(Status.SERVER_ERROR_INTERNAL);
				return new StringRepresentation(WADO_REQUEST_URI_BUILD_ERROR_TEXT);
			} catch (HttpException e) {
				log.warning(WADO_REQUEST_HTTP_ERROR_TEXT, e);
				setStatus(Status.SERVER_ERROR_INTERNAL);
				return new StringRepresentation(WADO_REQUEST_HTTP_ERROR_TEXT);
			} catch (IOException e) {
				log.warning(WADO_REQUEST_IO_ERROR_TEXT, e);
				setStatus(Status.SERVER_ERROR_INTERNAL);
				return new StringRepresentation(WADO_REQUEST_IO_ERROR_TEXT);
			}
		} else {
			log.info(NO_QUERY_ERROR_TEXT);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return new StringRepresentation(NO_QUERY_ERROR_TEXT);
		}
	}

	private String buildWADOURL(String queryString)
	{
		String host = config.getStringPropertyValue("NameServer"); // TODO DICOM name constants
		int port = config.getIntegerPropertyValue("DicomServerWadoPort");
		String base = config.getStringPropertyValue("WadoUrlExtension");

		StringBuilder sb = new StringBuilder();
		sb.append("http://").append(host);
		sb.append(":").append(port);
		sb.append(base);
		sb.append(queryString);
		String wadoURL = sb.toString();
		return wadoURL;
	}

	// A Restlet {@link OutputRepresentation} provides streaming infrastructure.
	private class DICOMStreamWriter extends OutputRepresentation
	{
		private final InputStream inputStream;

		public DICOMStreamWriter(InputStream inputStream)
		{
			super(MediaType.IMAGE_PNG);
			this.inputStream = inputStream;
		}

		@Override
		public void write(OutputStream outputStream) throws IOException
		{
			int read = 0;
			byte[] bytes = new byte[4096];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
				outputStream.flush();
			}
		}
	}
}
