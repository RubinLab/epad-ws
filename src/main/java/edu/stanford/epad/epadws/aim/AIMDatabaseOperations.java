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
package edu.stanford.epad.epadws.aim;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.internal.XNATSubjectList;
import edu.stanford.epad.dtos.internal.XNATUserList;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.dao.AbstractDAO;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;
import edu.stanford.hakan.aim4api.base.AimException;
import edu.stanford.hakan.aim4api.base.Enumerations.AimVersion;
import edu.stanford.hakan.aim4api.database.exist.ExistManager;
import edu.stanford.hakan.aim4api.database.exist.ExistResponderThread;
import edu.stanford.hakan.aim4api.utility.XML;

/**
 *
 * @author Hakan
 */
public class AIMDatabaseOperations {
	private static final EPADLogger log = EPADLogger.getInstance();

    private Statement statement = null;
    private String existServerURL = "";
    private String existNameSpace = "";
    private String existCollectionName = "";
    private String existUserName = "";
    private String existUserPassword = "";
    private Connection mySqlConnection = null;

    public static final String ANNOTATIONS_TABLE = "annotations";
	private final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
    
    public AIMDatabaseOperations(Connection mySqlConnection, String existServerURL,
            String existNameSpace, String existCollectionName, String existUserName, String existUserPassword) throws SQLException {
        this.existServerURL = existServerURL;
        this.existNameSpace = existNameSpace;
        this.existCollectionName = existCollectionName;
        this.existUserName = existUserName;
        this.existUserPassword = existUserPassword;
        this.mySqlConnection = mySqlConnection;
    }
    
    public static final String aimcol_username = "UserLoginName";
    public static final String aimcol_subjectID = "PatientID";
    public static final String aimcol_projectID = "ProjectUID";
    public static final String aimcol_aimID = "AnnotationUID";

    public void createAnnotationsTable() throws SQLException {
       String sqlCreateTable = "CREATE TABLE IF NOT EXISTS `" + ANNOTATIONS_TABLE + "` (\n"
                + "  `UserLoginName` VARCHAR(255) NOT NULL,\n"
                + "  `PatientID` VARCHAR(255) NOT NULL,\n"
                + "  `SeriesUID` VARCHAR(255),\n"
                + "  `DSOSeriesUID` VARCHAR(255),\n"
                + "  `StudyUID` VARCHAR(255),\n"
                + "  `ImageUID` VARCHAR(255),\n"
                + "  `FrameID` INT,\n"
                + "  `AnnotationUID` VARCHAR(255) NOT NULL,\n"
                //+ "  `AnnotationName` VARCHAR(255) NOT NULL,\n"
                + "  `ProjectUID` VARCHAR(255),\n"
                + "  UPDATETIME TIMESTAMP,\n"
                + "  DSOFRAMENO INTEGER,\n"
                + "  NAME VARCHAR(128),\n"
                + "  SHAREDPROJECTS VARCHAR(2000),\n"
                + "  AIMCOLOR VARCHAR(64),\n"
                + "  PRIMARY KEY (`AnnotationUID`));";
       boolean closeStmt = false;
	   	try {
	   		if (this.statement == null)
	   		{
	   			this.statement = mySqlConnection.createStatement();
	   			closeStmt = true;
	   		}
	        this.statement.executeUpdate(sqlCreateTable);
		} catch (SQLException x) {
				log.warning("Error creating annotations table", x);
		} finally {
			if (statement != null && closeStmt) statement.close();
		}
    }

    public void alterAnnotationsTable() throws SQLException {
    	addColumn("XML MEDIUMTEXT");
    	addColumn("UPDATETIME TIMESTAMP");
    	addColumn("DSOFRAMENO INTEGER");
    	addColumn("TEMPLATECODE VARCHAR(64)");
    	addColumn("SHAREDPROJECTS VARCHAR(2000)");
    	addColumn("NAME VARCHAR(128)");
    	addColumn("AIMCOLOR VARCHAR(64)");
    	try {
	    	this.statement = mySqlConnection.createStatement();
	        this.statement.executeUpdate("CREATE INDEX annotations_series_ind ON annotations(seriesuid)");
	        this.statement.executeUpdate("CREATE INDEX annotations_project_ind ON annotations(projectuid)");
       	} catch (SQLException x) {
    		if (!x.getMessage().contains("Duplicate"))
    			log.warning("Error adding index", x);
    	} finally {
    		if (statement != null) statement.close();
    	}
    }
    
    private void addColumn(String nameAndType) throws SQLException {
    	try {
	    	this.statement = mySqlConnection.createStatement();
	        this.statement.executeUpdate("ALTER TABLE " + ANNOTATIONS_TABLE + " ADD COLUMN " + nameAndType);
    	} catch (SQLException x) {
    		if (!x.getMessage().contains("Duplicate"))
    			log.warning("Error adding column", x);
    	} finally {
    		if (statement != null) statement.close();
    	}   	
    }
    
    public void refreshTheAnnotationTable(AimVersion aimVersion) throws AimException, IOException, SQLException {

		log.info("Creating annotations table, AIMVersion:" + aimVersion);
    	try {
    	    if (mySqlConnection != null)
    	    {
    	    	this.statement = mySqlConnection.createStatement();
    	    	createAnnotationsTable();
    	    }
    	    
	        String query = " declare default element namespace '" + this.existNameSpace + "'; ";
	        if (aimVersion == AimVersion.AIMv4_0) {
	            query += " for $iac in collection('" + this.existCollectionName + "')/ImageAnnotationCollection ";
	            query += " return ($iac/uniqueIdentifier, $iac/person/id, $iac/user/loginName, $iac/imageAnnotations/ImageAnnotation/markupEntityCollection, $iac/imageAnnotations/ImageAnnotation/imageReferenceEntityCollection) ";
	        //} else if (aimVersion == AimVersion.AIMv3_0 || aimVersion == AimVersion.AIMv3_0_1 || aimVersion == AimVersion.AIMv3_0_2) {
	        } else if (aimVersion == AimVersion.AIMv3_0_1 || aimVersion == AimVersion.AIMv3_0_2) {
	            query += " for $ia in collection('" + this.existCollectionName + "')/ImageAnnotation ";
	            query += " return (string($ia/@uniqueIdentifier), string($ia/person/Person/@id), string($ia/user/User/@loginName), $ia/imageReferenceCollection/ImageReference/imageStudy/ImageStudy) ";
	        } else {
	            throw new AimException("Error: refreshTheAnnotationTable, the aimVersion is not correct.");
	        }
	
	        int totalCount = 0;
	        int totalRetrieved = 0;
	        int pageCount = 10000;
	        String serverResponse = "";
	        int startIndex = 1;
	        StringBuilder sb = new StringBuilder();
	
	        log.info("XQuery:" + query);
	        serverResponse = ExistManager.getXMLStringFromExistWithStartIndexCount(this.existServerURL, query, this.existUserName, this.existUserPassword, 1, 1);
	        Document doc = XML.getDocumentFromString(serverResponse);
	        totalCount = ExistManager.getHitsCountFromDocument(doc);
	        log.info("Total Count from Exact DB:" + totalCount);
	        if (totalCount > pageCount) {
	            pageCount = totalCount / 3;
	        }
	
	        List<ExistResponderThread> listThreads = new ArrayList<>();
	        while (true) {
	            listThreads.add(new ExistResponderThread(this.existServerURL + "~" + query + "~" + this.existUserName + "~" + this.existUserPassword + "~" + startIndex + "~" + pageCount));
	            totalRetrieved = startIndex + pageCount - 1;
	            if (totalRetrieved >= totalCount) {
	                break;
	            }
	            startIndex = startIndex + pageCount;
	        }
	
	        log.info("Number of Threads:" + listThreads.size());
	        for (ExistResponderThread thread : listThreads) {
	            thread.start();
	        }
	
	        boolean allOK = false;
	        while (!allOK) {
	            allOK = true;
	            for (ExistResponderThread thread : listThreads) {
	                if (!thread.isFinished()) {
	                    allOK = false;
	                }
	            }
	        }
	        for (ExistResponderThread thread : listThreads) {
	            serverResponse = thread.getRespond();
	            serverResponse = serverResponse.replace(serverResponse.substring(0, serverResponse.indexOf(">") + 1), "");
	            serverResponse = serverResponse.replace("</exist:result>", "");
	            serverResponse = serverResponse.replace("exist:value", "exist");
	            serverResponse = serverResponse.replace("exist >", "exist>");
	            serverResponse = serverResponse.replace("<exist >", "<exist>");
	            serverResponse = serverResponse.replace("exist:type=\"xs:string\"", "");
	            sb.append(serverResponse);
	        }
	        //if (mySqlConnection == null)
	        //	log.info(sb.toString());
	        doc = XML.getDocumentFromString("<results>" + sb.toString() + "</results>");
	
	        Node node = doc.getFirstChild();
	        boolean uidOK = false;
	        boolean markupOK = false;
	        boolean irefOK = false;
	        boolean patOK = false;
	        boolean userOK = false;
	        boolean nameOK = false;
	
	        String userLoginName = "";
	        String patientID = "";
	        String annotationID = "";
	        String annotationName = "";
	        String imageID = "";
	        String frameID = "0";
	        String studyID = "";
	        String seriesID = "";
	        
	        //if (statement != null)
	        //	this.statement.executeUpdate("DELETE FROM annotations");
	
	        NodeList listChilds = node.getChildNodes();
	        for (int i = 0; i < listChilds.getLength(); i++) {
	            Node currentNode = listChilds.item(i);
	            if (aimVersion == AimVersion.AIMv4_0) {
	
		            if ("uniqueIdentifier".equals(currentNode.getNodeName())) {
		                annotationID = currentNode.getAttributes().getNamedItem("root").getNodeValue();
		                uidOK = true;
		            } else if ("id".equals(currentNode.getNodeName())) {
		                patientID = currentNode.getAttributes().getNamedItem("value").getNodeValue();
		                patOK = true;
	                } else if ("loginName".equals(currentNode.getNodeName())) {
	                    userLoginName = currentNode.getAttributes().getNamedItem("value").getNodeValue();
	                    userOK = true;
		            } else if ("markupEntityCollection".equals(currentNode.getNodeName())) {
		                NodeList listMarkupEntityCollection = currentNode.getChildNodes();
		                for (int j = 0; j < listMarkupEntityCollection.getLength(); j++) {
		                    Node nodeMarkupEntity = listMarkupEntityCollection.item(j);
		                    if ("MarkupEntity".equals(nodeMarkupEntity.getNodeName())) {
		                        NodeList listMarkupChilds = nodeMarkupEntity.getChildNodes();
		                        for (int k = 0; k < listMarkupChilds.getLength(); k++) {
		                            Node nodeMarkupEntityChild = listMarkupChilds.item(k);
		                            if ("imageReferenceUid".equals(nodeMarkupEntityChild.getNodeName())) {
		                                imageID = nodeMarkupEntityChild.getAttributes().getNamedItem("root").getNodeValue();
		                            } else if ("referencedFrameNumber".equals(nodeMarkupEntityChild.getNodeName())) {
		                                frameID = nodeMarkupEntityChild.getAttributes().getNamedItem("value").getNodeValue();
		                            }
		                        }
		                    }
		                }
		                markupOK = true;
		            } else if ("imageReferenceEntityCollection".equals(currentNode.getNodeName())) {
		                NodeList listImageReferenceEntityCollection = currentNode.getChildNodes();
		                for (int j = 0; j < listImageReferenceEntityCollection.getLength(); j++) {
		                    Node nodeImageReferenceEntity = listImageReferenceEntityCollection.item(j);
		                    NodeList listChildImageReferenceEntity = nodeImageReferenceEntity.getChildNodes();
		                    for (int k = 0; k < listChildImageReferenceEntity.getLength(); k++) {
		                        Node nodeImageStudy = listChildImageReferenceEntity.item(k);
		                        if ("imageStudy".equals(nodeImageStudy.getNodeName())) {
		                            NodeList listChildImageStudy = nodeImageStudy.getChildNodes();
		                            for (int l = 0; l < listChildImageStudy.getLength(); l++) {
		                                Node nodeImageSeries = listChildImageStudy.item(l);
		                                if ("instanceUid".equals(nodeImageSeries.getNodeName())) {
		                                    studyID = nodeImageSeries.getAttributes().getNamedItem("root").getNodeValue();
		                                } else if ("imageSeries".equals(nodeImageSeries.getNodeName())) {
		                                    NodeList listChildImageSeries = nodeImageSeries.getChildNodes();
		                                    for (int m = 0; m < listChildImageSeries.getLength(); m++) {
		                                        Node nodeChildImageSeries = listChildImageSeries.item(m);
		                                        if ("instanceUid".equals(nodeChildImageSeries.getNodeName())) {
		                                            seriesID = nodeChildImageSeries.getAttributes().getNamedItem("root").getNodeValue();
		                                        }
		                                    }
		                                }
		                            }
		                        }
		                    }
		                }
		                irefOK = true;
		            }
	            } else {
	                if (!uidOK && "exist".equals(currentNode.getNodeName())) {
	                    annotationID = currentNode.getTextContent();
	                    uidOK = true;
	                } else if (!patOK && uidOK && "exist".equals(currentNode.getNodeName())) {
	                    patientID = currentNode.getTextContent();
	                    patOK = true;
	
	                } else if (!userOK && patOK && uidOK && "exist".equals(currentNode.getNodeName())) {
	                    userLoginName = currentNode.getTextContent();
	                    userOK = true;
	
	                } else if (uidOK && patOK && userOK && "ImageStudy".equals(currentNode.getNodeName())) {
	                    studyID = currentNode.getAttributes().getNamedItem("instanceUID").getNodeValue();
	                    NodeList childsImageStudy = currentNode.getChildNodes();
	                    for (int j = 0; j < childsImageStudy.getLength(); j++) {
	                        Node childImageStudy = childsImageStudy.item(j);
	                        if ("imageSeries".equals(childImageStudy.getNodeName())) {
	                            NodeList childsimageSeries = childImageStudy.getChildNodes();
	                            for (int k = 0; k < childsimageSeries.getLength(); k++) {
	                                Node childimageSeries = childsimageSeries.item(k);
	                                if ("ImageSeries".equals(childimageSeries.getNodeName())) {
	                                    seriesID = childimageSeries.getAttributes().getNamedItem("instanceUID").getNodeValue();
	                                    NodeList childsImageSeries = childimageSeries.getChildNodes();
	                                    for (int l = 0; l < childsImageSeries.getLength(); l++) {
	                                        Node childImageSeries = childsImageSeries.item(l);
	                                        if ("imageCollection".equals(childImageSeries.getNodeName())) {
	                                            NodeList childsimageCollection = childImageSeries.getChildNodes();
	                                            for (int m = 0; m < childsimageCollection.getLength(); m++) {
	                                                Node childimageCollection = childsimageCollection.item(m);
	                                                if ("Image".equals(childimageCollection.getNodeName())) {
	                                                    imageID = childimageCollection.getAttributes().getNamedItem("sopInstanceUID").getNodeValue();
	                                                    markupOK = true;
	                                                }
	                                            }
	                                        }
	                                    }
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	
	                if ((aimVersion == AimVersion.AIMv4_0 && uidOK && patOK && markupOK && irefOK && userOK) || (aimVersion != AimVersion.AIMv4_0 && uidOK && patOK && userOK && markupOK)) {
	                uidOK = false;
	                patOK = false;
	                markupOK = false;
	                irefOK = false;
	                userOK = false;
	                nameOK = false;
	                String projectID = "";
	                if (mySqlConnection != null)
	                	projectID =	getProjectIdForAnnotation(userLoginName, patientID, studyID, seriesID);
	                if (projectID.trim().length() == 0) projectID = EPADConfig.xnatUploadProjectID;
	                if (frameID.trim().length() == 0) frameID = "0";
	                log.info("uid:" + annotationID + " patientID:" + patientID + "\t studyUID:" + studyID + " seriesUID:" + seriesID + " imageUID:" +  imageID + " projectID:" + projectID + " user:" + userLoginName);
	                String sqlInsert = "INSERT INTO annotations (UserLoginName,PatientID,SeriesUID,StudyUID,ImageUID,FrameID,AnnotationUID,ProjectUID) VALUES (" 
	                				+ "'" + userLoginName + "', '" + patientID + "', '" + seriesID + "', '" 
	                				+ studyID + "', '" + imageID + "', " + frameID + ", '" + annotationID + "', '" + projectID + "')";
	                if (statement != null)
	                	this.statement.executeUpdate(sqlInsert);
	
	                annotationID = "";
	                imageID = "";
	                frameID = "";
	                studyID = "";
	                seriesID = "";
	                patientID = "";
	                annotationName = "";
	            }
	        }
    	} finally {
    		if (statement != null) statement.close();
    	}
    }

    public int delete(String annotationUID) throws SQLException {
    	try {
    	    this.statement = mySqlConnection.createStatement();
    	    String delSql = "DELETE FROM annotations WHERE(AnnotationUID = '" + annotationUID + "')";
    		int count = this.statement.executeUpdate(delSql);
    		log.info(delSql + " result:" + count);
    		return count;
    	} finally {
    		statement.close();
    	}
    }

    public EPADAIM updateAIM(String annotationID, String newProjectID, String username) throws SQLException
    {
    	EPADAIM aim = this.getAIM(annotationID);
    	if (aim != null)
    	{
    		try {
	    	    this.statement = mySqlConnection.createStatement();
	    	    String sql = "UPDATE " + ANNOTATIONS_TABLE + " set ProjectUID = '" + newProjectID + "', UserLoginName = '" + username + "'  where AnnotationUID = '" + annotationID + "'";
	            log.info("AIMs update:" + sql);
	            this.statement.executeUpdate(sql);   				
				return new EPADAIM(aim.aimID, username, newProjectID, aim.subjectID, aim.subjectID, aim.seriesUID, aim.imageUID, aim.instanceOrFrameNumber, aim.dsoSeriesUID);
        	} finally {
        		if (statement != null)
        			statement.close();
        		statement = null;
        	}
    	}
    	return aim;
    }

    public EPADAIM updateAIMXml(String annotationID, String xml) throws SQLException
    {
    	EPADAIM aim = this.getAIM(annotationID);
    	if (aim != null)
    	{
    		try {
	    	    this.statement = mySqlConnection.createStatement();
	    	    String sql = "UPDATE " + ANNOTATIONS_TABLE + " set XML = " + AbstractDAO.toSQL(xml) + " where AnnotationUID = '" + annotationID + "'";
	            log.info("Updating AIMs XML for:" + annotationID);
	            this.statement.executeUpdate(sql);
	            aim.xml = xml;
	            return aim;
       	} finally {
        		if (statement != null)
        			statement.close();
        		statement = null;
        	}
    	}
    	return null;
    }

    public EPADAIM updateAIMDSOFrameNo(String annotationID, int frameNo) throws SQLException
    {
    	EPADAIM aim = this.getAIM(annotationID);
    	if (aim != null)
    	{
    		try {
	    	    this.statement = mySqlConnection.createStatement();
	    	    String sql = "UPDATE " + ANNOTATIONS_TABLE + " set DSOFRAMENO = " + frameNo + " where AnnotationUID = '" + annotationID + "'";
	            log.info("Updating AIMs DSOFRAMENO for:" + annotationID + " to " + frameNo);
	            this.statement.executeUpdate(sql);
	            aim.dsoFrameNo = frameNo;
	            return aim;
       	} finally {
        		if (statement != null)
        			statement.close();
        		statement = null;
        	}
    	}
    	return null;
    }

    public EPADAIM updateAIMDSOSeriesUID(String annotationID, String dsoSeriesUID) throws SQLException
    {
    	EPADAIM aim = this.getAIM(annotationID);
    	if (aim != null)
    	{
    		try {
	    	    this.statement = mySqlConnection.createStatement();
	    	    String sql = "UPDATE " + ANNOTATIONS_TABLE + " set DSOSeriesUID = '" + dsoSeriesUID + "' where AnnotationUID = '" + annotationID + "'";
	            log.info("Updating AIMs DSOSeriesUID for:" + annotationID + " to " + dsoSeriesUID);
	            this.statement.executeUpdate(sql);
	            aim.dsoSeriesUID = dsoSeriesUID;
	            return aim;
       	} finally {
        		if (statement != null)
        			statement.close();
        		statement = null;
        	}
    	}
    	return null;
    }

    public EPADAIM updateAIMName(String annotationID, String name) throws SQLException
    {
    	EPADAIM aim = this.getAIM(annotationID);
    	if (aim != null)
    	{
    		try {
	    	    this.statement = mySqlConnection.createStatement();
	    	    String sql = "UPDATE " + ANNOTATIONS_TABLE + " set name = " + AbstractDAO.toSQL(name) + " where AnnotationUID = '" + annotationID + "'";
	            log.info("Updating AIMs name for:" + annotationID);
	            this.statement.executeUpdate(sql);
	            aim.name = name;
	            return aim;
       	} finally {
        		if (statement != null)
        			statement.close();
        		statement = null;
        	}
    	}
    	return null;
    }
    
    public EPADAIM updateAIMColor(String annotationID, String color) throws SQLException
    {
    	EPADAIM aim = this.getAIM(annotationID);
    	if (aim != null)
    	{
    		try {
	    	    this.statement = mySqlConnection.createStatement();
	    	    String sql = "UPDATE " + ANNOTATIONS_TABLE + " set aimcolor = " + AbstractDAO.toSQL(color) + " where AnnotationUID = '" + annotationID + "'";
	            log.info("Updating AIMs name for:" + annotationID);
	            this.statement.executeUpdate(sql);
	            aim.color = color;
	            return aim;
       	} finally {
        		if (statement != null)
        			statement.close();
        		statement = null;
        	}
    	}
    	return null;
    }
    
	public void addProjectToAIM(String projectID, String annotationID) throws SQLException {
  		ResultSet rs = null;
		try {
    	    this.statement = mySqlConnection.createStatement();
      	    String sql = "SELECT SHAREDPROJECTS FROM " + ANNOTATIONS_TABLE + " where AnnotationUID = '" + annotationID + "'";
        	rs = this.statement.executeQuery(sql);
			String sharedProjects = null;
			if (rs.next()) {
				sharedProjects = rs.getString(1);
				if (sharedProjects == null || sharedProjects.length() == 0) sharedProjects = ",";
				if (sharedProjects.indexOf("," + projectID + ",") != -1)
					return;	
			}
			else {
				throw new SQLException("Annotation not found, aimID:" + annotationID);
			}
			rs.close();
			sharedProjects = sharedProjects + projectID + ",";
      	    sql = "UPDATE " + ANNOTATIONS_TABLE + " set SHAREDPROJECTS = '" + sharedProjects + "' where AnnotationUID = '" + annotationID + "'";
            this.statement.executeUpdate(sql);
		} finally {
    		if (statement != null)
    			statement.close();
    		statement = null;
    	}

	}
	   
		public void removeProjectFromAIM(String projectID, String annotationID) throws SQLException {
	  		ResultSet rs = null;
			try {
	    	    this.statement = mySqlConnection.createStatement();
	      	    String sql = "SELECT SHAREDPROJECTS FROM " + ANNOTATIONS_TABLE + " where AnnotationUID = '" + annotationID + "'";
	        	rs = this.statement.executeQuery(sql);
				String sharedProjects = null;
				if (rs.next()) {
					sharedProjects = rs.getString(1);
					if (sharedProjects == null || sharedProjects.length() == 0) sharedProjects = ",";
					if (sharedProjects.indexOf("," + projectID + ",") == -1)
						return;	
				}
				else {
					throw new SQLException("Annotation not found, aimID:" + annotationID);
				}
				rs.close();
				sharedProjects = sharedProjects.replace("," + projectID + ",", ",");
	      	    sql = "UPDATE " + ANNOTATIONS_TABLE + " set SHAREDPROJECTS = '" + sharedProjects + "' where AnnotationUID = '" + annotationID + "'";
	            this.statement.executeUpdate(sql);
			} finally {
	    		if (statement != null)
	    			statement.close();
	    		statement = null;
	    	}

		}

	public List<EPADAIM> getSharedAIMs(String projectID, String patientID, String seriesUID) throws SQLException 
	{
		String sql = "SHAREDPROJECTS like '%," + projectID + ",%'";
		if (patientID != null && patientID.length() > 0)
			sql = sql + " and patientID = '" + patientID + "'";
		if (seriesUID != null && seriesUID.length() > 0)
			sql = sql + " and seriesUID = '" + seriesUID + "'";
		return this.getAIMs(sql, 0, 5000);
	}
	
    public EPADAIM updateAIMTemplateCode(String annotationID, String templateCode) throws SQLException
    {
    	EPADAIM aim = this.getAIM(annotationID);
    	if (aim != null)
    	{
    		try {
	    	    this.statement = mySqlConnection.createStatement();
	    	    String sql = "UPDATE " + ANNOTATIONS_TABLE + " set TEMPLATECODE = '" + templateCode + "' where AnnotationUID = '" + annotationID + "'";
	            log.info("Updating AIMs TEMPLATECODE for:" + annotationID + " to " + templateCode);
	            this.statement.executeUpdate(sql);
	            return aim;
       	} finally {
        		if (statement != null)
        			statement.close();
        		statement = null;
        	}
    	}
    	return null;
    }
  
    public EPADAIM insert(String annotationUID, String userName, String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID) throws SQLException {
        return insert(annotationUID, userName, projectID, patientID, studyUID, seriesUID, imageUID, frameID, null);
    }
    
    public EPADAIM insert(String annotationUID, String userName, String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID, String dsoSeriesUID) throws SQLException {
        return insert(annotationUID, userName, projectID, patientID, studyUID, seriesUID, imageUID, frameID, dsoSeriesUID, null, null);
    }
    
    public EPADAIM insert(String annotationUID, String userName, String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID, String dsoSeriesUID, String aimXML, String aimName) throws SQLException {
    	try {
    		EPADAIM aim = getAIM(annotationUID);
    		if (aim != null)
    		{
    			if (dsoSeriesUID != null || aim.dsoSeriesUID != null)
    				log.info("AIM already exists in table, aimID:" + annotationUID + " dsoSeriesUID:" + aim.dsoSeriesUID + " newDSOSeriesUID:" + dsoSeriesUID);
    			if (dsoSeriesUID != null && dsoSeriesUID.length() > 0 && (aim.dsoSeriesUID == null || aim.dsoSeriesUID.length() == 0))
    			{
    	    	    this.statement = mySqlConnection.createStatement();
    	    	    String sql = "UPDATE " + ANNOTATIONS_TABLE + " set DSOSeriesUID = '" + dsoSeriesUID + "' where AnnotationUID = '" + annotationUID + "'";
    	            log.info("AIMs update:" + sql);
    	            this.statement.executeUpdate(sql);   				
    			}
    			if (aimXML != null)
    			{
    	    	    this.statement = mySqlConnection.createStatement();
    	    	    String sql = "UPDATE " + ANNOTATIONS_TABLE + " set XML = " + AbstractDAO.toSQL(aimXML) + " where AnnotationUID = '" + annotationUID + "'";
    	            //log.info("AIMs update:" + sql);
    	            this.statement.executeUpdate(sql);   				
     			}
    			if (aimName != null)
    			{
    	    	    this.statement = mySqlConnection.createStatement();
    	    	    String sql = "UPDATE " + ANNOTATIONS_TABLE + " set NAME = " + AbstractDAO.toSQL(aimName) + " where AnnotationUID = '" + annotationUID + "'";
    	            //log.info("AIMs update:" + sql);
    	            this.statement.executeUpdate(sql);   				
     			}
				return new EPADAIM(aim.aimID, aim.userName, aim.projectID, aim.subjectID, aim.subjectID, aim.seriesUID, aim.imageUID, aim.instanceOrFrameNumber, dsoSeriesUID);
    		}
            String sql = "INSERT into " + ANNOTATIONS_TABLE + " (AnnotationUID";
            String values = "'" + annotationUID + "'";
            if (projectID != null && projectID.length() > 0)
    		{
                sql = sql + ",ProjectUID";
                values = values + ",'" + projectID + "'";
    		}
    		if (userName != null && userName.length() > 0)
    		{
                sql = sql + ",UserLoginName";
                values = values + ",'" + userName + "'";
    		}
    		if (patientID != null && patientID.length() > 0)
    		{
                sql = sql + ",PatientID";
       			values = values + ",'" + patientID + "'";
    		}
    		if (studyUID != null && studyUID.length() > 0)
    		{
                sql = sql + ",StudyUID";
       			values = values + ",'" + studyUID + "'";
    		}
    		if (seriesUID != null && seriesUID.length() > 0)
    		{
                sql = sql + ",SeriesUID";
       			values = values + ",'" + seriesUID + "'";
    		}
    		if (imageUID != null && imageUID.length() > 0)
    		{
                sql = sql + ",ImageUID";
       			values = values + ",'" + imageUID + "'";
    		}
    		if (frameID != 0)
    		{
                sql = sql + ",FrameID";
       			values = values + "," + frameID;
    		}
       		if (dsoSeriesUID != null)
    		{
                sql = sql + ",DSOSeriesUID";
       			values = values + ",'" + dsoSeriesUID + "'";
    		}
       		if (aimXML != null)
    		{
                sql = sql + ",XML";
       			values = values + "," + AbstractDAO.toSQL(aimXML);
    		}
       		if (aimName != null)
    		{
                sql = sql + ",NAME";
       			values = values + "," + AbstractDAO.toSQL(aimName);
    		}
            sql = sql + ") VALUES (" + values + ")";
            log.info("AIMs insert:" + sql);
    	    this.statement = mySqlConnection.createStatement();
 	        this.statement.executeUpdate(sql);
			return new EPADAIM(annotationUID, userName, projectID, patientID, studyUID, seriesUID, imageUID, frameID, dsoSeriesUID);
    	} finally {
    		if (statement != null)
    			statement.close();
    		statement = null;
    	}
    }

    public int getTotalAnnotationCount() throws SQLException {
        String sqlSelect = "SELECT COUNT(*) FROM " + ANNOTATIONS_TABLE;
        ResultSet rs = null;
        try {
    	    this.statement = mySqlConnection.createStatement();
        	rs =  this.statement.executeQuery(sqlSelect);
			if (rs.next()) {
				return rs.getInt(1);
			}
		} finally {
			if (rs != null) rs.close();
			if (statement != null) statement.close();
		}
		return 0;
    }
    
    public List<EPADAIM> getAIMs(String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID) throws SQLException {
    	return getAIMs(projectID, patientID, studyUID, seriesUID, imageUID, frameID, 1, 5000);
    }
    
    public List<EPADAIM> getAIMs(String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID, int start, int count) throws SQLException {
        return getAIMs(projectID, patientID, studyUID, seriesUID, imageUID, frameID, null, start, count);
    }

	public static final String[] defaultColors = {
		"#00ffff",
		"#000fff",
		"#0000ff",
		"#80ffff",
		"#c4ffff",
		"#ff0000",
		"#ff8080",
		"#ff00ff",
		"#00ff78",
		"#00ff78",
		"#ff00ff",
		"#bbffbb",
		"#ff8080",
		"#ffff00",
		"#ff00ff",
		"#ff00ff",
		"#ff0000",
		"#ff8000",
		"#00ff00",
		"#ffc060",
		"#4040ff",
		"#00ff00",
		"#a4a4ff",
		"#dca078",
		"#4040ff",
		"#ff00ff",		
	};
	
	//select a.projectuid,a.patientid,a.studyuid,count(*) from annotations a,project_user pu,project p, user u where a.projectuid=p.projectid and p.id=pu.project_id and pu.role not like 'Collaborator' and pu.user_id=u.id and u.username='admin' and studyuid='1.2.826.0.1.3680043.8.420.17214402469099817596602593812838717198' and pu.project_id=7; 
	 public int getAIMCount(String projectID, String studyUID, String username) throws SQLException {
		 	int count=0;
	        String sqlSelect = "SELECT count(*) FROM annotations a,project_user pu,project p, user u WHERE a.projectuid=p.projectid and p.id=pu.project_id  and pu.user_id=u.id and pu.role not like 'Collaborator'  ";
			if (projectID != null && projectID.length() > 0)
				sqlSelect = sqlSelect + " and (p.projectid = '" + projectID + "')";
			if (studyUID != null && studyUID.length() > 0)
				sqlSelect = sqlSelect + " AND StudyUID = '" + studyUID + "'";
			if (username != null && username.length() > 0)
				sqlSelect = sqlSelect + " AND u.username = '" + username + "'";
			sqlSelect = sqlSelect + " AND (a.userloginname = '" + username + "' OR a.userloginname = 'shared')";
			
			log.warning("AIMs count select:" + sqlSelect);
	       
			ResultSet rs = null;
	        try
	        {
	        	int row = 1;
	    	    this.statement = mySqlConnection.createStatement();
	        	rs = this.statement.executeQuery(sqlSelect);
				if (rs.next()) {
					count = Integer.parseInt(rs.getString(1));
				}
	        }
	        finally
	        {
	        	if (rs != null) rs.close();
	        	statement.close();
	        }
	        log.warning("Number of AIMs found in database:" +count);
			return count;
	    }
	
    public List<EPADAIM> getAIMs(String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID, String dsoSeriesUID, int start, int count) throws SQLException {
        String sqlSelect = "SELECT UserLoginName, ProjectUID, PatientID, StudyUID, SeriesUID, ImageUID, frameID, AnnotationUID, DSOSeriesUID, DSOFRAMENO, XML, NAME, AIMCOLOR, TEMPLATECODE FROM annotations WHERE 1 = 1";
		if (projectID != null && projectID.length() > 0)
			sqlSelect = sqlSelect + " and (ProjectUID = '" + projectID + "')";
		//sqlSelect = sqlSelect + " and (ProjectUID = '" + projectID + "' or ProjectUID = '" + EPADConfig.xnatUploadProjectID + "')";
		if (patientID != null && patientID.length() > 0)
			sqlSelect = sqlSelect + " and PatientID = '" + patientID + "'";
		if (studyUID != null && studyUID.length() > 0)
			sqlSelect = sqlSelect + " AND StudyUID = '" + studyUID + "'";
		if (seriesUID != null && seriesUID.length() > 0)
			sqlSelect = sqlSelect + " AND SeriesUID = '" + seriesUID + "'";
		if (imageUID != null && imageUID.length() > 0)
			sqlSelect = sqlSelect + " AND ImageUID = '" + imageUID + "'";
		if (frameID != 0)
			sqlSelect = sqlSelect + " AND FrameID = " + frameID;
		if (dsoSeriesUID != null && dsoSeriesUID.length() > 0)
		{
			sqlSelect = sqlSelect + " AND DSOSeriesUID = '" + dsoSeriesUID + "'";
			if (seriesUID == null || seriesUID.length() == 0)
				sqlSelect = sqlSelect + " AND SeriesUID is not null";
		}
		
		sqlSelect = sqlSelect + " ORDER BY ProjectUID, PatientID, StudyUID, SeriesUID, ImageUID, FrameID";
        log.warning("AIMs select:" + sqlSelect);
       
		ResultSet rs = null;
        List<EPADAIM> aims = new ArrayList<EPADAIM>();
        try
        {
        	int row = 1;
    	    this.statement = mySqlConnection.createStatement();
        	rs = this.statement.executeQuery(sqlSelect);
			while (rs.next()) {
				if (row++ < start) continue;
				String UserName = rs.getString(1);
				String ProjectID = rs.getString(2);
				String PatientID = rs.getString(3);
				String StudyUID = rs.getString(4);
				String SeriesUID = rs.getString(5);
				String ImageUID = rs.getString(6);
				int FrameID = rs.getInt(7);
				String AnnotationID = rs.getString(8);
				String DSOSeriesUID = rs.getString(9);
				Integer dsoFrameNo = rs.getInt(10);
				String xml = rs.getString(11);
				String name = rs.getString(12);
				String color = rs.getString(13);
				String template = rs.getString(14);
				EPADAIM aim = new EPADAIM(AnnotationID, UserName, ProjectID, PatientID, StudyUID, SeriesUID, ImageUID, FrameID, DSOSeriesUID);
				aim.xml = xml;
				aim.name = name;
				aim.color = color;
				aim.templateType = template;
				if (dsoFrameNo != null)
					aim.dsoFrameNo = dsoFrameNo;
				aims.add(aim);
				if (aim.color == null || aim.color.trim().length() == 0)
				{
					aim.color = defaultColors[(row-1)%defaultColors.length];
				}
				else if (aim.color.equals(","))
				{
					aim.color = defaultColors[(row-1)%defaultColors.length] + "," + defaultColors[(row+5)%defaultColors.length];
				}
				if (row > start+count) break;
			}
    	    log.debug("AIM Records " + aims.size());
        }
        finally
        {
        	if (rs != null) rs.close();
        	statement.close();
        }
        log.warning("Number of AIMs found in database:" + aims.size());
		return aims;
    }
    
    public List<EPADAIM> getAIMs(String criteria, int start, int count) throws SQLException {
    	if (!criteria.trim().toLowerCase().startsWith("where"))
    		criteria = "WHERE " + criteria;
        String sqlSelect = "SELECT UserLoginName, ProjectUID, PatientID, StudyUID, SeriesUID, ImageUID, frameID, AnnotationUID, DSOSeriesUID, DSOFRAMENO, XML, NAME, AIMCOLOR FROM annotations " + criteria;
        log.debug("AIMs select:" + sqlSelect);
       
		ResultSet rs = null;
        List<EPADAIM> aims = new ArrayList<EPADAIM>();
        try
        {
        	int row = 1;
    	    this.statement = mySqlConnection.createStatement();
        	rs = this.statement.executeQuery(sqlSelect);
			while (rs.next()) {
				if (row++ < start) continue;
				String UserName = rs.getString(1);
				String ProjectID = rs.getString(2);
				String PatientID = rs.getString(3);
				String StudyUID = rs.getString(4);
				String SeriesUID = rs.getString(5);
				String ImageUID = rs.getString(6);
				int FrameID = rs.getInt(7);
				String AnnotationID = rs.getString(8);
				String DSOSeriesUID = rs.getString(9);
				Integer dsoFrameNo = rs.getInt(10);
				String xml = rs.getString(11);
				String name = rs.getString(12);
				String color = rs.getString(12);
				EPADAIM aim = new EPADAIM(AnnotationID, UserName, ProjectID, PatientID, StudyUID, SeriesUID, ImageUID, FrameID, DSOSeriesUID);
				aim.xml = xml;
				aim.name = name;
				aim.color = color;
				if (aim.color == null || aim.color.trim().length() == 0)
				{
					aim.color = defaultColors[(row-1)%defaultColors.length];
				}
				else if (aim.color.equals(","))
				{
					aim.color = defaultColors[(row-1)%defaultColors.length] + "," + defaultColors[(row+5)%defaultColors.length];
				}
				if (dsoFrameNo != null)
					aim.dsoFrameNo = dsoFrameNo;
				aims.add(aim);
				if (count > 0 && row > start+count) break;
			}
    	    log.debug("AIM Records " + aims.size());
        }
        finally
        {
        	if (rs != null) rs.close();
        	statement.close();
        }
        log.debug("Number of AIMs found in database:" + aims.size());
		return aims;
    }
   
    public int getAIMCount(String userName, String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID) throws SQLException {
        String sqlSelect = "SELECT COUNT(*) FROM annotations WHERE 1 = 1";
		if (userName != null && userName.length() > 0)
			sqlSelect = sqlSelect + " and UserLoginName = '" + userName + "'";
		if (projectID != null && projectID.length() > 0)
			sqlSelect = sqlSelect + " and ProjectUID = '" + projectID + "'";
//		sqlSelect = sqlSelect + " and (ProjectUID = '" + projectID + "' or ProjectUID = '" + EPADConfig.xnatUploadProjectID + "' or ProjectUID = '')";
		if (patientID != null && patientID.length() > 0)
			sqlSelect = sqlSelect + " and PatientID = '" + patientID + "'";
		if (studyUID != null && studyUID.length() > 0)
			sqlSelect = sqlSelect + " AND StudyUID = '" + studyUID + "'";
		if (seriesUID != null && seriesUID.length() > 0)
			sqlSelect = sqlSelect + " AND SeriesUID = '" + seriesUID + "'";
		if (imageUID != null && imageUID.length() > 0)
			sqlSelect = sqlSelect + " AND ImageUID = '" + imageUID + "'";
		if (frameID != 0)
			sqlSelect = sqlSelect + " AND FrameID = " + frameID;
        log.info("AIMs count select:" + sqlSelect);
       
		ResultSet rs = null;
        Set<EPADAIM> aims = new HashSet<EPADAIM>();
        try
        {
    	    this.statement = mySqlConnection.createStatement();
        	rs = this.statement.executeQuery(sqlSelect);
			if (rs.next()) {
				int count = rs.getInt(1);
	    	    log.info(sqlSelect + " result:" + count);
				return count;
			}
        }
        finally
        {
        	if (rs != null) rs.close();
        	statement.close();
        }
		return 0;
    }
    
    public int getAIMCount(String criteria) throws SQLException {
    	if (!criteria.trim().toLowerCase().startsWith("where"))
    		criteria = "WHERE " + criteria;
        String sqlSelect = "SELECT count(*) FROM " + ANNOTATIONS_TABLE + " "+ criteria;
        log.info("AIMs count select:" + sqlSelect);
       
		ResultSet rs = null;
        try
        {
    	    this.statement = mySqlConnection.createStatement();
        	rs = this.statement.executeQuery(sqlSelect);
			if (rs.next()) {
				int count = rs.getInt(1);
	    	    log.info(sqlSelect + " result:" + count);
				return count;
			}
        }
        finally
        {
        	if (rs != null) rs.close();
        	statement.close();
        }
		return 0;
    }
    
    public EPADAIM getAIM(String annotationID) throws SQLException { //ml shared projects added
        String sqlSelect = "SELECT UserLoginName, ProjectUID, PatientID, StudyUID, SeriesUID, ImageUID, frameID, DSOSeriesUID,XML,NAME,SHAREDPROJECTS FROM annotations WHERE AnnotationUID = '"
        		+ annotationID + "'";
    	log.debug(sqlSelect);
		ResultSet rs = null;
        try
        {
    	    this.statement = mySqlConnection.createStatement();
        	rs = this.statement.executeQuery(sqlSelect);
			if (rs.next()) {
				String UserName = rs.getString(1);
				String ProjectID = rs.getString(2);
				String PatientID = rs.getString(3);
				String StudyUID = rs.getString(4);
				String SeriesUID = rs.getString(5);
				String ImageUID = rs.getString(6);
				int FrameID = rs.getInt(7);
				String DSOSeriesUID = rs.getString(8);
				String xml = rs.getString(9);
				String name = rs.getString(10);
				String sharedProjects = rs.getString(11);
				EPADAIM aim = new EPADAIM(annotationID, UserName, ProjectID, PatientID, StudyUID, SeriesUID, ImageUID, FrameID, DSOSeriesUID, sharedProjects);
				aim.xml = xml;
				aim.name = name;
				return aim;
			}
        }
        finally
        {
        	if (rs != null) rs.close();
        	statement.close();
    		statement = null;
        }
		return null;
    }
    
    public List<EPADAIM> getAIMs(String annotationIDs) throws SQLException {
        String sqlSelect = "SELECT UserLoginName, ProjectUID, PatientID, StudyUID, SeriesUID, ImageUID, frameID, AnnotationUID, DSOSeriesUID, XML, NAME FROM annotations WHERE AnnotationUID in ";
        String[] ids = annotationIDs.split(",");
        String delim = "(";
        for (String id: ids)
        {
        	sqlSelect = sqlSelect + delim + "'" + id + "'";
        	delim = ",";
        }
    	sqlSelect = sqlSelect + ")";
    	log.debug(sqlSelect);
		ResultSet rs = null;
		List<EPADAIM> aims = new ArrayList<EPADAIM>();
        try
        {
    	    this.statement = mySqlConnection.createStatement();
        	rs = this.statement.executeQuery(sqlSelect);
			while (rs.next()) {
				String UserName = rs.getString(1);
				String ProjectID = rs.getString(2);
				String PatientID = rs.getString(3);
				String StudyUID = rs.getString(4);
				String SeriesUID = rs.getString(5);
				String ImageUID = rs.getString(6);
				int FrameID = rs.getInt(7);
				String AnnotationID = rs.getString(8);
				String DSOSeriesUID = rs.getString(9);
				String xml = rs.getString(10);
				String name = rs.getString(11);
				EPADAIM aim = new EPADAIM(AnnotationID, UserName, ProjectID, PatientID, StudyUID, SeriesUID, ImageUID, FrameID, DSOSeriesUID);
				aim.xml = xml;
				aim.name = name;
				aims.add(aim);
			}
        }
        finally
        {
        	if (rs != null) rs.close();
        	statement.close();
    		statement = null;
        }
		return aims;
    }
	
	public List<EPADAIM> getAIMs(String projectID, AIMSearchType aimSearchType, String value) throws SQLException
	{
		return getAIMs(projectID, aimSearchType, value, 1, 10000);
	}
	public List<EPADAIM> getAIMs(String projectID, AIMSearchType aimSearchType, String value, int start, int count) throws SQLException
	{
		List<EPADAIM> aims = new ArrayList<EPADAIM>();
		long time1 = System.currentTimeMillis();
		if (aimSearchType == AIMSearchType.PERSON_NAME) {
			String patientID = null;
			if (EPADConfig.UseEPADUsersProjects) {
				Subject subject;
				try {
					subject = projectOperations.getSubjectFromNameForProject(value, projectID);
					patientID = subject.getSubjectUID();
				} catch (Exception e) {}
			} else {
	    		String adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
	    		XNATSubjectList subjects = XNATQueries.getSubjectsForProject(adminSessionID, projectID);
	    		Map<String, String> patientIdToName = subjects.getNameMap();
				for (String id: patientIdToName.keySet())
	    		{
	    			if (patientIdToName.get(id).equals(value))
	    			{
	    				patientID = id;
	    			}
	    		}
			}
			if (patientID == null) return aims;
		    return getAIMs(projectID, patientID, null, null, null, 0, start, count);
		} else if (aimSearchType == AIMSearchType.PATIENT_ID) {
			String patientID = value;
		    return getAIMs(projectID, patientID, null, null, null, 0, start, count);
		} else if (aimSearchType == AIMSearchType.SERIES_UID) {
			String seriesID = value;
		    return getAIMs(projectID, null, null, seriesID, null, 0, start, count);
		} else if (aimSearchType == AIMSearchType.TEMPLATE_CODE) {
			String code = value;
		    return this.getAIMs("TEMPLATECODE = '" + value + "'", start, count);
		} else if (aimSearchType == AIMSearchType.ANNOTATION_UID) {
			if (value.equalsIgnoreCase("all")) {
				return this.getAIMs(projectID, null, null, null, null, 0, start, count);
			} else {
				String annotationUID = value;
				if (annotationUID.contains(","))
				{
					return getAIMs(annotationUID);
				}
				else
				{
					EPADAIM aim =  getAIM(annotationUID);
					aims.add(aim);
					return aims;
				}
			}
		} else if (aimSearchType.equals(AIMSearchType.AIM_QUERY)) {
			return this.getAIMs(projectID, null, null, null, null, 0, start, count);
		} else if (aimSearchType.equals(AIMSearchType.JSON_QUERY)) {
			return this.getAIMs(projectID, null, null, null, null, 0, start, count);
		} else {
			log.warning("Unknown AIM search type " + aimSearchType.getName());
		}
		long time2 = System.currentTimeMillis();
		log.info("AIM table query took " + (time2-time1) + " msecs");
		return aims;
	}
    
    // This returns the most likely projectId
    static Set<String> projectIds = null;
    static Map<String,Set<String>> usersForProjects = new HashMap<String, Set<String>>();
    static Map<String,Set<String>> patientsForProjects = new HashMap<String, Set<String>>();
    static Map<String,Map<String, Set<String>>> subjectStudiesForProjects = new HashMap<String, Map<String, Set<String>>>();
    private static String getProjectIdForAnnotation(String userName, String patientID, String studyID, String seriesID)
    {
    	// First time, get all info from XNAT
    	if (projectIds == null)
    	{
    		String adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
    		projectIds = XNATQueries.allProjectIDs(adminSessionID);
    		log.info("Projects:" + projectIds);
    		for (String projectId: projectIds)
    		{
    			if (projectId.equalsIgnoreCase(EPADConfig.xnatUploadProjectID)) continue;
    			log.info("Getting users for project:" + projectId);
    			if (!XNATSessionOperations.hasValidXNATSessionID(adminSessionID))
    				adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
    			XNATUserList users = XNATQueries.getUsersForProject(projectId);
				Set<String> usernames = users.getLoginNames();
    			log.info("Users for project:" + projectId + " Users:"+ usernames);
    			usersForProjects.put(projectId, usernames);
    			log.info("Getting Subjects for project:" + projectId);
    			if (!XNATSessionOperations.hasValidXNATSessionID(adminSessionID))
    				adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
     			Map<String, Set<String>> subjectAndStudies = XNATQueries.getSubjectsAndStudies(adminSessionID, projectId);
    			log.info("Subjects for project:" + projectId + " Subjects:"+ subjectAndStudies.keySet());
    			subjectStudiesForProjects.put(projectId, subjectAndStudies);
    		}
    	}
    	if (userName == null || userName.length() == 0 
    			|| patientID == null || patientID.length() == 0 
    			|| studyID == null || studyID.length() == 0 
    			|| seriesID == null || seriesID.length() == 0)
    		return "";
    	
    	Set<String> userProjects = new HashSet<String>();
		for (String projectId: projectIds)
		{
			if (projectId.equalsIgnoreCase(EPADConfig.xnatUploadProjectID)) continue;
			Set<String> users = usersForProjects.get(projectId);
			if (users == null) continue;
			if (users.contains(userName))
				userProjects.add(projectId);
		}
		for (String projectId: userProjects)
		{
			if (projectId.equalsIgnoreCase(EPADConfig.xnatUploadProjectID)) continue;
			Map<String, Set<String>> subjectStudies = subjectStudiesForProjects.get(projectId);
			if (subjectStudies == null) continue;
			Set<String> studies = subjectStudies.get(patientID);
			if (studies == null)
				studies = subjectStudies.get(patientID.replace('-', '_'));
			if (studies != null && studies.contains(studyID))
			{
				return projectId;
			}
		}
		if (userName.equals("admin"))
		{
			// admin can add annotations to any project, so search them all
			userProjects = projectIds;
			for (String projectId: userProjects)
			{
				if (projectId.equalsIgnoreCase(EPADConfig.xnatUploadProjectID)) continue;
				Map<String, Set<String>> subjectStudies = subjectStudiesForProjects.get(projectId);
				if (subjectStudies == null) continue;
				Set<String> studies = subjectStudies.get(patientID);
				if (studies != null && studies.contains(studyID))
				{
					return projectId;
				}
			}
		}
		return "";
    }

    public static void main(String [ ] args)
    {
    	AIMDatabaseOperations adb;
		try {
			System.out.println("Running AIMDatabaseOperations");
			adb = new AIMDatabaseOperations(null, "http://epad-dev2.stanford.edu:8899/exist/",
			        "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM", "aim.dbxml", "epaduser", "3p4dus3r");
	    	adb.refreshTheAnnotationTable(AimVersion.AIMv3_0_1);
			System.out.println("Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
