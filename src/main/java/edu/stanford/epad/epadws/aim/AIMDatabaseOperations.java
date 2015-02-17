//Copyright (c) 2013 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that 
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following 
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the 
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its 
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without 
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    	try {
	    	this.statement = mySqlConnection.createStatement();
	        this.statement.executeUpdate("ALTER TABLE " + ANNOTATIONS_TABLE + " ADD COLUMN XML MEDIUMTEXT");
    	} catch (SQLException x) {
    		if (!x.getMessage().contains("Duplicate"))
    			log.warning("Error adding column", x);
    	} finally {
    		if (statement != null) statement.close();
    	}
    	try {
	    	this.statement = mySqlConnection.createStatement();
	        this.statement.executeUpdate("ALTER TABLE " + ANNOTATIONS_TABLE + " ADD COLUMN UPDATETIME TIMESTAMP");
	        this.statement.executeUpdate("CREATE INDEX annotations_series_ind ON annotations(seriesuid)");
	        this.statement.executeUpdate("CREATE INDEX annotations_project_ind ON annotations(projectuid)");
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
	        
	        if (statement != null)
	        	this.statement.executeUpdate("DELETE FROM annotations");
	
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
	            log.info("AIMs XML Updated for:" + annotationID);
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
   
    public EPADAIM insert(String annotationUID, String userName, String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID) throws SQLException {
        return insert(annotationUID, userName, projectID, patientID, studyUID, seriesUID, imageUID, frameID, null);
    }
    
    public EPADAIM insert(String annotationUID, String userName, String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID, String dsoSeriesUID) throws SQLException {
        return insert(annotationUID, userName, projectID, patientID, studyUID, seriesUID, imageUID, frameID, dsoSeriesUID, null);
    }
    
    public EPADAIM insert(String annotationUID, String userName, String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID, String dsoSeriesUID, String aimXML) throws SQLException {
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
    
    public List<EPADAIM> getAIMs(String projectID, String patientID, String studyUID, String seriesUID, String imageUID, int frameID, String dsoSeriesUID, int start, int count) throws SQLException {
        String sqlSelect = "SELECT UserLoginName, ProjectUID, PatientID, StudyUID, SeriesUID, ImageUID, frameID, AnnotationUID, DSOSeriesUID, XML FROM annotations WHERE 1 = 1";
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
        log.info("AIMs select:" + sqlSelect);
       
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
				String xml = rs.getString(10);
				EPADAIM aim = new EPADAIM(AnnotationID, UserName, ProjectID, PatientID, StudyUID, SeriesUID, ImageUID, FrameID, DSOSeriesUID);
				aim.xml = xml;
				aims.add(aim);
				if (row > start+count) break;
			}
    	    log.info("AIM Records " + aims.size());
        }
        finally
        {
        	if (rs != null) rs.close();
        	statement.close();
        }
        log.info("Number of AIMs found in database:" + aims.size());
		return aims;
    }
    
    public List<EPADAIM> getAIMs(String criteria, int start, int count) throws SQLException {
    	if (!criteria.trim().toLowerCase().startsWith("where"))
    		criteria = "WHERE " + criteria;
        String sqlSelect = "SELECT UserLoginName, ProjectUID, PatientID, StudyUID, SeriesUID, ImageUID, frameID, AnnotationUID, DSOSeriesUID, XML FROM annotations " + criteria;
        log.info("AIMs select:" + sqlSelect);
       
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
				String xml = rs.getString(10);
				EPADAIM aim = new EPADAIM(AnnotationID, UserName, ProjectID, PatientID, StudyUID, SeriesUID, ImageUID, FrameID, DSOSeriesUID);
				aim.xml = xml;
				aims.add(aim);
				if (count > 0 && row > start+count) break;
			}
    	    log.info("AIM Records " + aims.size());
        }
        finally
        {
        	if (rs != null) rs.close();
        	statement.close();
        }
        log.info("Number of AIMs found in database:" + aims.size());
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
    
    public EPADAIM getAIM(String annotationID) throws SQLException {
        String sqlSelect = "SELECT UserLoginName, ProjectUID, PatientID, StudyUID, SeriesUID, ImageUID, frameID, DSOSeriesUID,XML FROM annotations WHERE AnnotationUID = '"
        		+ annotationID + "'";
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
				EPADAIM aim = new EPADAIM(annotationID, UserName, ProjectID, PatientID, StudyUID, SeriesUID, ImageUID, FrameID, DSOSeriesUID);
				aim.xml = xml;
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
        String sqlSelect = "SELECT UserLoginName, ProjectUID, PatientID, StudyUID, SeriesUID, ImageUID, frameID, AnnotationUID, DSOSeriesUID, XML FROM annotations WHERE AnnotationUID in ";
        String[] ids = annotationIDs.split(",");
        String delim = "(";
        for (String id: ids)
        {
        	sqlSelect = sqlSelect + delim + "'" + id + "'";
        	delim = ",";
        }
    	sqlSelect = sqlSelect + ")";
    	log.info(sqlSelect);
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
				EPADAIM aim = new EPADAIM(AnnotationID, UserName, ProjectID, PatientID, StudyUID, SeriesUID, ImageUID, FrameID, DSOSeriesUID);
				aim.xml = xml;
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

    /*
     * Following functions are unsafe because they do not close the statement or resultset, please do not use them
     */
    public ResultSet getAnnotationCountByPatientID() throws SQLException {
	    this.statement = mySqlConnection.createStatement();
        String sqlSelect = "SELECT PatientID, COUNT(AnnotationUID) FROM annotations GROUP BY PatientID";
        return this.statement.executeQuery(sqlSelect);
    }

    public ResultSet getAnnotationCountByPatientIDStudyUID() throws SQLException {
	    this.statement = mySqlConnection.createStatement();
        String sqlSelect = "SELECT PatientID, StudyUID, COUNT(AnnotationUID) FROM annotations GROUP BY PatientID, StudyUID";
        return this.statement.executeQuery(sqlSelect);
    }

    public ResultSet getAnnotationCountByPatientIDStudyUIDSeriesUID() throws SQLException {
	    this.statement = mySqlConnection.createStatement();
        String sqlSelect = "SELECT PatientID, StudyUID, SeriesUID, COUNT(AnnotationUID) FROM annotations GROUP BY PatientID, StudyUID, SeriesUID";
        return this.statement.executeQuery(sqlSelect);
    }

    public ResultSet getAnnotationCountByPatientIDStudyUIDSeriesUIDImageUID() throws SQLException {
	    this.statement = mySqlConnection.createStatement();
        String sqlSelect = "SELECT PatientID, StudyUID, SeriesUID, ImageUID, COUNT(AnnotationUID) FROM annotations GROUP BY PatientID, StudyUID, SeriesUID, ImageUID";
        return this.statement.executeQuery(sqlSelect);
    }

    public ResultSet getAnnotationCountByPatientIDStudyUIDSeriesUIDImageUIDFrameID() throws SQLException {
	    this.statement = mySqlConnection.createStatement();
        String sqlSelect = "SELECT PatientID, StudyUID, SeriesUID, ImageUID, FrameID, COUNT(AnnotationUID) FROM annotations GROUP BY PatientID, StudyUID, SeriesUID, ImageUID, FrameID";
        return this.statement.executeQuery(sqlSelect);
    }

    public ResultSet getAnnotationCountByPatientID(String patientID) throws SQLException {
	    this.statement = mySqlConnection.createStatement();
        String sqlSelect = "SELECT PatientID, COUNT(AnnotationUID) FROM annotations WHERE(PatientID = '" + patientID + "') GROUP BY PatientID";
        return this.statement.executeQuery(sqlSelect);
    }

    public ResultSet getAnnotationCountByPatientIDStudyUID(String patientID, String StudyUID) throws SQLException {
	    this.statement = mySqlConnection.createStatement();
        String sqlSelect = "SELECT PatientID, StudyUID, COUNT(AnnotationUID) FROM annotations WHERE(PatientID = '" + patientID + "' AND StudyUID = '" + StudyUID + "') GROUP BY PatientID, StudyUID";
        return this.statement.executeQuery(sqlSelect);
    }

    public ResultSet getAnnotationCountByPatientIDStudyUID(String patientID, String studyUID, String seriesUID) throws SQLException {
	    this.statement = mySqlConnection.createStatement();
        String sqlSelect = "SELECT PatientID, StudyUID, SeriesUID, COUNT(AnnotationUID) FROM annotations WHERE(PatientID = '" + patientID + "' AND StudyUID = '" + studyUID + "' AND SeriesUID = '" + seriesUID + "') GROUP BY PatientID, StudyUID, SeriesUID";
        return this.statement.executeQuery(sqlSelect);
    }

    public ResultSet getAnnotationCountByPatientIDStudyUIDSeriesUIDImageUID(String patientID, String studyUID, String seriesUID, String imageUID) throws SQLException {
	    this.statement = mySqlConnection.createStatement();
        String sqlSelect = "SELECT PatientID, StudyUID, SeriesUID, ImageUID, COUNT(AnnotationUID) FROM annotations WHERE(PatientID = '" + patientID + "' AND StudyUID = '" + studyUID + "' AND SeriesUID = '" + seriesUID + "' AND ImageUID = '" + imageUID + "') GROUP BY PatientID, StudyUID, SeriesUID, ImageUID";
        return this.statement.executeQuery(sqlSelect);
    }

    public ResultSet getAnnotationCountByPatientIDStudyUIDSeriesUIDImageUIDFrameID(String patientID, String studyUID, String seriesUID, String imageUID, String frameID) throws SQLException {
	    this.statement = mySqlConnection.createStatement();
        String sqlSelect = "SELECT PatientID, StudyUID, SeriesUID, ImageUID, frameID, COUNT(AnnotationUID) FROM annotations WHERE(PatientID = '" + patientID + "' AND StudyUID = '" + studyUID + "' AND SeriesUID = '" + seriesUID + "' AND ImageUID = '" + imageUID + "' AND FrameID = '" + frameID + "') GROUP BY PatientID, StudyUID, SeriesUID, ImageUID, FrameID";
        return this.statement.executeQuery(sqlSelect);
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
