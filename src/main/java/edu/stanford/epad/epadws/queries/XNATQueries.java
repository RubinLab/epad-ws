/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.queries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.internal.XNATExperiment;
import edu.stanford.epad.dtos.internal.XNATExperimentList;
import edu.stanford.epad.dtos.internal.XNATProject;
import edu.stanford.epad.dtos.internal.XNATProjectList;
import edu.stanford.epad.dtos.internal.XNATSubject;
import edu.stanford.epad.dtos.internal.XNATSubjectList;
import edu.stanford.epad.dtos.internal.XNATUserList;
import edu.stanford.epad.epadws.xnat.XNATQueryUtil;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * Methods for querying XNAT
 * 
 * 
 * @author martin
 */
public class XNATQueries
{
	private static final EPADLogger log = EPADLogger.getInstance();

	public static XNATProjectList allProjects(String sessionID)
	{
		String allProjectsQueryURL = XNATQueryUtil.buildAllProjectsQueryURL();
		return invokeXNATProjectsQuery(sessionID, allProjectsQueryURL);
	}

	public static String getProjectType(String projectID, String sessionID)
	{
		String allProjectsQueryURL = XNATQueryUtil.buildProjectTypeQueryURL(projectID);
		return invokeXNATQuery(sessionID, allProjectsQueryURL);
	}

	public static Set<String> allProjectIDs(String sessionID)
	{
		XNATProjectList xnatProjectList = allProjects(sessionID);
		Set<String> projectIDs = new HashSet<String>();

		for (XNATProject project : xnatProjectList.ResultSet.Result)
			projectIDs.add(project.ID);
		return projectIDs;
	}

	public static Set<String> getSubjectIDsForProject(String sessionID, String projectID)
	{
		XNATSubjectList xnatSubjectList = XNATQueries.getSubjectsForProject(sessionID, projectID);
		Set<String> subjectIDs = new HashSet<String>();

		for (XNATSubject subject : xnatSubjectList.ResultSet.Result)
			subjectIDs.add(subject.label);
		return subjectIDs;
	}

	public static Set<String> getAllStudyUIDsForProject(String projectID, String sessionID)
	{
		XNATExperimentList dicomExperiments = getDICOMExperiments(sessionID, projectID);
		Set<String> result = new HashSet<String>();

		for (XNATExperiment dicomExperiment : dicomExperiments.ResultSet.Result)
			result.add(dicomExperiment.label);

		return result;
	}

	public static String getFirstProjectForStudy(String sessionID, String studyUID)
	{
		XNATProjectList xnatProjectList = allProjects(sessionID);

		for (XNATProject project : xnatProjectList.ResultSet.Result)
		{
			if (project.name.equalsIgnoreCase(EPADConfig.xnatUploadProjectID)) continue;
			XNATExperimentList dicomExperiments = getDICOMExperiments(sessionID, project.name);	
			for (XNATExperiment dicomExperiment : dicomExperiments.ResultSet.Result)
				if(dicomExperiment.label.equals(studyUID))
					return project.name;
		}
		return EPADConfig.xnatUploadProjectID;
	}

	// Returns map: subjectID -> set of studyUIDs
	public static Map<String, Set<String>> getSubjectsAndStudies(String sessionID, String projectID)
	{
		Map<String, Set<String>> result = new HashMap<>();

		for (String subjectID : getSubjectIDsForProject(sessionID, projectID)) {
			Set<String> studyUIDs = getStudyUIDsForSubject(sessionID, projectID, subjectID);
			result.put(subjectID, studyUIDs);
		}
		return result;
	}

	public static Set<String> getStudyUIDsForSubject(String sessionID, String projectID, String subjectID)
	{
		Set<String> studyIDs = new HashSet<String>();
		XNATExperimentList xnatExperiments = XNATQueries.getDICOMExperiments(sessionID, projectID, subjectID);

		for (XNATExperiment xnatExperiment : xnatExperiments.ResultSet.Result) {
			String studyID = xnatExperiment.label.replace("_", ".");
			studyIDs.add(studyID);
		}
		return studyIDs;
	}
	
	public static boolean isCollaborator(String sessionID, String username, String projectID) throws Exception
	{
		if (projectID == null || projectID.trim().length() == 0)
		{
			if (username.equals("admin")) 
				return false;
			else
				return true;
		}
		String role = getUserProjectRole(sessionID, username, projectID);
		if (role == null && username.equals("admin")) return false;
		if (role == null && projectID.equals(EPADConfig.xnatUploadProjectID)) return true;
		if (role == null)
			throw new Exception("User " + username  + " does not exist in project:" + projectID);
		if (role.toLowerCase().startsWith("collaborator"))
			return true;
		else
			return false;
	}
	
	public static boolean isMember(String sessionID, String username, String projectID) throws Exception
	{
		if (projectID == null || projectID.trim().length() == 0)
			return false;
		String role = getUserProjectRole(sessionID, username, projectID);
		if (role == null && username.equals("admin")) return true;
		if (role == null)
			throw new Exception("Does not exist in project:" + projectID);
		if (role.toLowerCase().startsWith("member"))
			return true;
		else
			return false;
	}
	
	public static boolean isOwner(String sessionID, String username, String projectID) throws Exception
	{
		if (projectID == null || projectID.trim().length() == 0)
			return false;
		String role = getUserProjectRole(sessionID, username, projectID);
		if (role == null && username.equals("admin")) return true;
		if (role == null)
			throw new Exception("Does not exist in project:" + projectID);
		if (role.toLowerCase().startsWith("owner"))
			return true;
		else
			return false;
	}
	
	public static String getUserProjectRole(String sessionID, String username, String projectID)
	{
		XNATUserList userList = getUsersForProject(projectID);
		Map<String, String> roles = userList.getRoles();
		for (String loginName: roles.keySet())
		{
			if (loginName.equals(username))
			{
				return roles.get(loginName);
			}
		}
		return null;
	}

	public static Set<String> getAllStudyUIDs()
	{
		XNATExperimentList dicomExperiments = getAllDICOMExperiments();
		Set<String> result = new HashSet<String>();

		for (XNATExperiment dicomExperiment : dicomExperiments.ResultSet.Result)
			result.add(dicomExperiment.label);

		return result;
	}

	public static XNATUserList getUsersForProject(String projectID)
	{
		String allUsersForProjectQueryURL = XNATQueryUtil.buildAllUsersForProjectQueryURL(projectID);
		String adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
		log.info("Users query:" + allUsersForProjectQueryURL);
		return invokeXNATUsersQuery(adminSessionID, allUsersForProjectQueryURL);
	}

	public static XNATUserList getAllUsers()
	{
		String allUsersQueryURL = XNATQueryUtil.buildAllUsersQueryURL();
		String adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
		log.info("Users query:" + allUsersQueryURL);
		return invokeXNATUsersQuery(adminSessionID, allUsersQueryURL);
	}

	public static Set<String> getPatientNamesForProject(String sessionID, String projectID)
	{
		XNATSubjectList xnatSubjectList = XNATQueries.getSubjectsForProject(sessionID, projectID);
		Set<String> subjectIDs = new HashSet<String>();

		for (XNATSubject subject : xnatSubjectList.ResultSet.Result) {
			subjectIDs.add(subject.src);
		}
		return subjectIDs;
	}

	public static XNATSubjectList getSubjectsForProject(String sessionID, String projectID)
	{
		String allSubjectsForProjectQueryURL = XNATQueryUtil.buildAllSubjectsForProjectQueryURL(projectID);

		return invokeXNATSubjectsQuery(sessionID, allSubjectsForProjectQueryURL);
	}

	public static int getNumberOfDICOMExperiments(String sessionID, String projectID)
	{
		String xnatExperimentsQueryURL = XNATQueryUtil.buildDICOMExperimentsForProjectQueryURL(projectID);

		// TODO Need a count without getting all records.
		return invokeXNATDICOMExperimentsQuery(sessionID, xnatExperimentsQueryURL).ResultSet.totalRecords;
	}

	// TODO Fix this so that it makes a direct query with the subjectUID to XNAT
	public static XNATExperiment getDICOMExperiment(String sessionID, String projectID, String subjectID, String studyUID)
	{
		XNATExperimentList xnatExperimentList = getDICOMExperiments(sessionID, projectID, subjectID);

		for (XNATExperiment xnatExperiment : xnatExperimentList.ResultSet.Result)
			if (xnatExperiment.label.equals(studyUID))
				return xnatExperiment;

		return null;
	}

	private static XNATExperimentList getDICOMExperiments(String sessionID, String projectID)
	{
		String xnatExperimentsQueryURL = XNATQueryUtil.buildDICOMExperimentsForProjectQueryURL(projectID);

		return invokeXNATDICOMExperimentsQuery(sessionID, xnatExperimentsQueryURL);
	}

	private static XNATExperimentList getAllDICOMExperiments()
	{
		String xnatExperimentsQueryURL = XNATQueryUtil.buildDICOMExperimentsQueryURL();
		String adminSessionID = XNATSessionOperations.getXNATAdminSessionID();

		return invokeXNATDICOMExperimentsQuery(adminSessionID, xnatExperimentsQueryURL);
	}

	public static XNATExperimentList getDICOMExperiments(String sessionID, String projectID, String subjectID)
	{
		String xnatExperimentsQueryURL = XNATQueryUtil.buildDICOMExperimentsForProjectAndSubjectQueryURL(projectID,
				subjectID);

		return invokeXNATDICOMExperimentsQuery(sessionID, xnatExperimentsQueryURL);
	}

	private static XNATProjectList invokeXNATProjectsQuery(String sessionID, String xnatProjectsQueryURL)
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(xnatProjectsQueryURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + sessionID);

		try {
			xnatStatusCode = client.executeMethod(method);
		} catch (IOException e) {
			log.warning("Error performing XNAT projects query " + xnatProjectsQueryURL, e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return processXNATProjectsQueryResponse(method, xnatStatusCode); // Will release connection
	}

	private static XNATUserList invokeXNATUsersQuery(String sessionID, String xnatUsersQueryURL)
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(xnatUsersQueryURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + sessionID);

		try {
			xnatStatusCode = client.executeMethod(method);
		} catch (IOException e) {
			log.warning("Error performing XNAT projects query " + xnatUsersQueryURL, e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return processXNATUsersQueryResponse(method, xnatStatusCode); // Will release connection
	}

	private static XNATUserList processXNATUsersQueryResponse(GetMethod method, int xnatStatusCode)
	{
		try {
			if (xnatStatusCode == HttpServletResponse.SC_OK) {
				return extractXNATUsersFromResponse(method);
			} else if (xnatStatusCode == HttpServletResponse.SC_UNAUTHORIZED) {
				log.warning("Invalid session token for XNAT users query");
				return XNATUserList.emptyUsers();
			} else {
				log.warning("Error performing XNAT projects query; XNAT status code = " + xnatStatusCode);
				return XNATUserList.emptyUsers();
			}
		} finally {
			method.releaseConnection();
		}
	}

	private static XNATProjectList processXNATProjectsQueryResponse(GetMethod method, int xnatStatusCode)
	{
		try {
			if (xnatStatusCode == HttpServletResponse.SC_OK) {
				return extractXNATProjectsFromResponse(method);
			} else if (xnatStatusCode == HttpServletResponse.SC_UNAUTHORIZED) {
				log.warning("Invalid session token for XNAT projects query");
				return XNATProjectList.emptyProjects();
			} else {
				log.warning("Error performing XNAT projects query; XNAT status code = " + xnatStatusCode);
				return XNATProjectList.emptyProjects();
			}
		} finally {
			method.releaseConnection();
		}
	}

	private static XNATUserList extractXNATUsersFromResponse(GetMethod method)
	{
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			Gson gson = new Gson();
			isr = new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8");
			br = new BufferedReader(isr);
			return gson.fromJson(br, XNATUserList.class);
		} catch (IOException e) {
			log.warning("Error processing XNAT users query result", e);
			return XNATUserList.emptyUsers();
		} catch (JsonSyntaxException e) {
			log.warning("Error processing XNAT users query result", e);
			return XNATUserList.emptyUsers();
		} finally {
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(br);
			method.releaseConnection();
		}
	}

	private static XNATProjectList extractXNATProjectsFromResponse(GetMethod method)
	{
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			Gson gson = new Gson();
			isr = new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8");
			br = new BufferedReader(isr);
			return gson.fromJson(br, XNATProjectList.class);
		} catch (IOException e) {
			log.warning("Error processing XNAT projects query result", e);
			return XNATProjectList.emptyProjects();
		} catch (JsonSyntaxException e) {
			log.warning("Error processing XNAT projects query result", e);
			return XNATProjectList.emptyProjects();
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
		}
	}

	private static XNATSubjectList invokeXNATSubjectsQuery(String sessionID, String xnatSubjectsQueryURL)
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(xnatSubjectsQueryURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + sessionID);

		try {
			xnatStatusCode = client.executeMethod(method);
		} catch (IOException e) {
			log.warning("Warning: error performing XNAT subject query " + xnatSubjectsQueryURL, e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return processXNATSubjectQueryResponse(method, xnatStatusCode); // Will release connection
	}

	private static XNATSubjectList processXNATSubjectQueryResponse(GetMethod method, int xnatStatusCode)
	{
		try {
			if (xnatStatusCode == HttpServletResponse.SC_OK) {
				return extractXNATSubjectsFromResponse(method);
			} else if (xnatStatusCode == HttpServletResponse.SC_UNAUTHORIZED) {
				log.warning("Invalid session token for XNAT subjects query");
				return XNATSubjectList.emptySubjects();
			} else {
				log.warning("Error performing XNAT subjects query; XNAT status code = " + xnatStatusCode);
				return XNATSubjectList.emptySubjects();
			}
		} finally {
			method.releaseConnection();
		}
	}

	private static XNATSubjectList extractXNATSubjectsFromResponse(GetMethod method)
	{
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			Gson gson = new Gson();
			isr = new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8");
			br = new BufferedReader(isr);
			return gson.fromJson(br, XNATSubjectList.class);
		} catch (IOException e) {
			log.warning("Error processing XNAT subjects query result", e);
			return XNATSubjectList.emptySubjects();
		} catch (JsonSyntaxException e) {
			log.warning("Error processing XNAT subjects query result", e);
			return XNATSubjectList.emptySubjects();
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
		}
	}

	public static int setXNATSubjectField(String sessionID, String subjectID, String fieldName, String fieldValue)
	{
		HttpClient client = new HttpClient();
		//HttpPut httpput = new HttpPut(XNATQueryUtil.buildSubjectURL(subjectID));
		PutMethod method = new PutMethod(XNATQueryUtil.buildSubjectURL(subjectID));
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + sessionID);

		try {
			//httpput.setHeader("Cookie", "JSESSIONID=" + sessionID);
			//httpput.setEntity(new StringEntity(, "text/xml", "utf8"));
			method.setRequestBody(XNATQueryUtil.buildSubjectFieldXML(fieldName, fieldValue));
			//HttpResponse response = client.execute(httpput);
			xnatStatusCode = client.executeMethod(method);
		} catch (IOException e) {
			log.warning("Warning: error performing XNAT subject query " + XNATQueryUtil.buildSubjectURL(subjectID), e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			method.releaseConnection();
		}
		return xnatStatusCode;
	}

	public static String getXNATSubjectFieldValue(String sessionID, String xnatSubjectID, String fieldName)
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(XNATQueryUtil.buildSubjectURL(xnatSubjectID) + "?format=xml");
		int xnatStatusCode;
		//log.info("Calling XNAT Subject info:" + XNATQueryUtil.buildSubjectURL(xnatSubjectID) + "?format=xml");
		method.setRequestHeader("Cookie", "JSESSIONID=" + sessionID);

		try {
			xnatStatusCode = client.executeMethod(method);
			String xmlResp = method.getResponseBodyAsString(10000);
			log.debug(xmlResp);
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    InputStream is = new StringBufferInputStream(xmlResp);
		    Document doc = db .parse(is);
		    doc.getDocumentElement().normalize();
		    NodeList nodes = doc.getElementsByTagName("xnat:field");
		    String value = "";
		    String subjfieldname = "";
		    for (int i = 0; i < nodes.getLength(); i++)
		    {
		    	Node node = nodes.item(i);
		    	value = node.getTextContent();
		    	if (value != null) value = value.replace('\n',' ').trim();
		        NamedNodeMap attrs = node.getAttributes() ;
	        	String attrName = null;
		        for (int j = 0 ; attrs != null && j < attrs.getLength() ; j++)
		        {  
		        	attrName = attrs.item(j).getNodeName();
		        	subjfieldname = attrs.item(j).getNodeValue();
		        	if (fieldName.equalsIgnoreCase(subjfieldname)) return value;
		        }
		    }
			return value;
		} catch (Exception e) {
			log.warning("Warning: error performing XNAT subject query " + XNATQueryUtil.buildSubjectURL(xnatSubjectID), e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			method.releaseConnection();
		}
		return null;
	}

	private static String invokeXNATQuery(String sessionID, String xnatQueryURL)
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(xnatQueryURL);
		int xnatStatusCode;
		log.info("XNATQuery:" + xnatQueryURL);
		method.setRequestHeader("Cookie", "JSESSIONID=" + sessionID);
		try {
			xnatStatusCode = client.executeMethod(method);
			String xmlResp = method.getResponseBodyAsString(10000);
			log.debug(xmlResp);
			return xmlResp;
		} catch (IOException e) {
			log.warning("Error performing XNAT experiment query " + xnatQueryURL, e);
		} finally {
			method.releaseConnection();
		}
		return null;
	}

	private static XNATExperimentList invokeXNATDICOMExperimentsQuery(String sessionID,
			String xnatDICOMExperimentsQueryURL)
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(xnatDICOMExperimentsQueryURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + sessionID);

		try {
			xnatStatusCode = client.executeMethod(method);
		} catch (IOException e) {
			log.warning("Error performing XNAT experiment query " + xnatDICOMExperimentsQueryURL, e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return processXNATExperimentsQueryResponse(method, xnatStatusCode); // Will release connection
	}

	private static XNATExperimentList processXNATExperimentsQueryResponse(GetMethod method, int xnatStatusCode)
	{
		try {
			if (xnatStatusCode == HttpServletResponse.SC_OK) {
				return extractXNATExperimentsFromResponse(method);
			} else if (xnatStatusCode == HttpServletResponse.SC_UNAUTHORIZED) {
				log.warning("Invalid session token for XNAT experiments query");
				return XNATExperimentList.emptyExperiments();
			} else {
				log.warning("Error performing XNAT experiments query; XNAT status code = " + xnatStatusCode);
				return XNATExperimentList.emptyExperiments();
			}
		} finally {
			method.releaseConnection();
		}
	}

	private static XNATExperimentList extractXNATExperimentsFromResponse(GetMethod method)
	{
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			Gson gson = new Gson();
			isr = new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8");
			br = new BufferedReader(isr);
			return gson.fromJson(br, XNATExperimentList.class);
		} catch (IOException e) {
			log.warning("Error processing XNAT experiments query result", e);
			return XNATExperimentList.emptyExperiments();
		} catch (JsonSyntaxException e) {
			log.warning("Error processing XNAT experiments query result", e);
			return XNATExperimentList.emptyExperiments();
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
		}
	}
}
