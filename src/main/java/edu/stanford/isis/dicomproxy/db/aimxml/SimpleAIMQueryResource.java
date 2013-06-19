/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.db.aimxml;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlManagerConfig;
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlResults;


/**
 *
 */
public class SimpleAIMQueryResource extends XMLDatabaseResource{

    /**
     * Returns annotation UIDs based on queries by StudyInstanceUID,
     * SeriesInstanceUID, or SOPInstanceUID.
     *
     * Mandatory parameters: collection, namespace
     * Optional parameters: studyInstanceUID, seriesInstanceUID, sopInstanceUID
     * @param queryForm Form
     * @return String
     */
    @AIMQueryAPI
    public String getAnnotationUIDsByDicomUIDs(Form queryForm) {
    	log.info("getAnnotationUIDsByDicomUIDs");
       String collection = queryForm.getFirstValue("collection");
       if (collection == null || collection.isEmpty())
          collection = dbVars.get("defaultcontainer");
       String namespace = queryForm.getFirstValue("namespace");

       // Read in the UIDs and build a query for them
       String studyInstanceUID = queryForm.getFirstValue("studyInstanceUID");
       String seriesInstanceUID = queryForm.getFirstValue("seriesInstanceUID");
       String sopInstanceUID = queryForm.getFirstValue("sopInstanceUID");

       String studyConstraint = "true()";
       if(studyInstanceUID != null && !studyInstanceUID.isEmpty())
          studyConstraint = "$x/study/Study[@instanceUID=\"" + studyInstanceUID + "\"]";
       String seriesConstraint = "true()";
       if(seriesInstanceUID != null && !seriesInstanceUID.isEmpty())
          seriesConstraint = "$x/study/Study/series/Series[@instanceUID=\"" + seriesInstanceUID + "\"]";
       String sopConstraint = "true()";
       if(sopInstanceUID != null && !sopInstanceUID.isEmpty())
          sopConstraint = "$x/study/Study/series/Series/imageCollection/Image[@sopInstanceUID=\"" + sopInstanceUID + "\"]";

       String queryString = "for $x in collection(\"" + collection + "\")/ImageAnnotation/imageReferenceCollection/ImageReference\n"
                          + "where " + studyConstraint + "and " + seriesConstraint + "and " + sopConstraint + "\n"
                          + "return data($x/../../@uniqueIdentifier)";

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null)
          return genErrorXML(getStatus().getDescription());
       else
          return toXml(results);
    }

    /**
     * Returns annotation UIDs for annotations matching the patient name
     *
     * Mandatory parameters: collection, namespace
     * Optional parameters: patientName
     * @param queryForm
     * @return
     */
    @AIMQueryAPI
    public String getAnnotationUIDsByPatientName(Form queryForm) {
    	log.info("getAnnotationUIDsByPatientName");
       String collection = queryForm.getFirstValue("collection");
       if (collection == null || collection.isEmpty())
          collection = dbVars.get("defaultcontainer");
       String namespace = queryForm.getFirstValue("namespace");

       // Read in the patient name and build a query for it
       String patientName = queryForm.getFirstValue("patientName");

       String patientConstraint = "true()";
       if(patientName != null && !patientName.isEmpty())
          patientConstraint = "$x/patient/Patient[@name=\"" + patientName + "\"]";

       String queryString = "for $x in collection(\"" + collection + "\")/ImageAnnotation\n"
                          + "where " + patientConstraint + "\n"
                          + "return data($x/@uniqueIdentifier)";

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null)
          return genErrorXML(getStatus().getDescription());
       else
          return toXml(results);
    }

    /**
     * Returns the study dates for all annotations matching the patient name
     *
     * Mandatory parameters: collection, namespace
     * Optional parameters: patientName
     * @param queryForm
     * @return
     */
    @AIMQueryAPI
    public String getStudyDatesForPatient(Form queryForm) {
    	log.info("getStudyDatesForPatient");
       String collection = queryForm.getFirstValue("collection");
       if (collection == null || collection.isEmpty())
          collection = dbVars.get("defaultcontainer");
       String namespace = queryForm.getFirstValue("namespace");

       // Read in the patient name and build a query for it
       String patientName = queryForm.getFirstValue("patientName");

       String patientConstraint = "true()";
       if(patientName != null && !patientName.isEmpty())
          patientConstraint = "$x/patient/Patient[@name=\"" + patientName + "\"]";

       String queryString = "for $x in collection(\"" + collection + "\")/ImageAnnotation\n"
                          + "where " + patientConstraint + "\n"
                          + "return data($x/imageReferenceCollection/ImageReference/study/Study/@studyDate)";

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null)
          return genErrorXML(getStatus().getDescription());
       else
          return toXml(new HashSet<String>(results));
    }

    /**
     * Returns the anatomic entities for the annotation with annotationUID
     *
     * Mandatory parameters: collection, namespace
     * Optional parameters: annotationUID
     * @param queryForm
     * @return
     */
    @AIMQueryAPI
    public String getAnatomicEntitiesForAnnotation(Form queryForm) {
    	log.info("getAnatomicEntitiesForAnnotation");
       String collection = queryForm.getFirstValue("collection");
       if (collection == null || collection.isEmpty())
          collection = dbVars.get("defaultcontainer");
       String namespace = queryForm.getFirstValue("namespace");

       // Read in the UID and build a query for it
       String uid = queryForm.getFirstValue("annotationUID");

       String uidConstraint = "true()";
       if(uid != null && !uid.isEmpty())
          uidConstraint = "$x[@uniqueIdentifier=\"" + uid + "\"]";

       String queryString = "for $x in collection(\"" + collection + "\")/ImageAnnotation\n"
                          + "where " + uidConstraint + "\n"
                          + "return data($x/anatomicEntityCollection/AnatomicEntity/@codeMeaning)";

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null)
          return genErrorXML(getStatus().getDescription());
       else
          return toXml(results);
    }

    /**
     * Returns the imaging observations for the annotation with annotationUID
     *
     * Mandatory parameters: collection, namespace
     * Optional parameters: annotationUID
     * @param queryForm
     * @return
     */
    @AIMQueryAPI
    public String getImagingObservationsForAnnotation(Form queryForm) {
    	log.info("getImagingObservationsForAnnotation");
       String collection = queryForm.getFirstValue("collection");
       if (collection == null || collection.isEmpty())
          collection = dbVars.get("defaultcontainer");
       String namespace = queryForm.getFirstValue("namespace");

       // Read in the UID and build a query for it
       String uid = queryForm.getFirstValue("annotationUID");

       String uidConstraint = "true()";
       if(uid != null && !uid.isEmpty())
          uidConstraint = "$x[@uniqueIdentifier=\"" + uid + "\"]";

       String queryString = "for $x in collection(\"" + collection + "\")/ImageAnnotation\n"
                          + "where " + uidConstraint + "\n"
                          + "return data($x/imagingObservationCollection/ImagingObservation/@codeMeaning)";

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null)
          return genErrorXML(getStatus().getDescription());
       else
          return toXml(results);
    }

    /**
     * Returns the annotation author for the annotation with annotationUID
     *
     * Mandatory parameters: collection, namespace
     * Optional parameters: annotationUID
     * @param queryForm
     * @return
     */
    @AIMQueryAPI
    public String getAIMCreatorForAnnotation(Form queryForm) {
    	log.info("getAIMCreatorForAnnotation");
       String collection = queryForm.getFirstValue("collection");
       if (collection == null || collection.isEmpty())
          collection = dbVars.get("defaultcontainer");
       String namespace = queryForm.getFirstValue("namespace");

       // Read in the UID and build a query for it
       String uid = queryForm.getFirstValue("annotationUID");

       String uidConstraint = "true()";
       if(uid != null && !uid.isEmpty())
          uidConstraint = "$x[@uniqueIdentifier=\"" + uid + "\"]";

       String queryString = "for $x in collection(\"" + collection + "\")/ImageAnnotation\n"
                          + "where " + uidConstraint + "\n"
                          + "return data($x/user/User/@name)";

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null)
          return genErrorXML(getStatus().getDescription());
       else
          return toXml(results);
    }

    /**
     * Returns the IDs of ROIs for the annotation with annotationUID
     *
     * Mandatory parameters: collection, namespace
     * Optional parameters: annotationUID
     * @param queryForm
     * @return
     */
    @AIMQueryAPI
    public String getROIShapeIDsForAnnotation(Form queryForm) {
    	log.info("getROIShapeIDsForAnnotation");
       String collection = queryForm.getFirstValue("collection");
       if (collection == null || collection.isEmpty())
          collection = dbVars.get("defaultcontainer");
       String namespace = queryForm.getFirstValue("namespace");

       // Read in the UID and build a query for it
       String uid = queryForm.getFirstValue("annotationUID");

       String uidConstraint = "true()";
       if(uid != null && !uid.isEmpty())
          uidConstraint = "$x[@uniqueIdentifier=\"" + uid + "\"]";

       String queryString = "for $x in collection(\"" + collection + "\")/ImageAnnotation\n"
                          + "where " + uidConstraint + "\n"
                          + "return data($x/geometricShapeCollection/GeometricShape/@id)";

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null)
          return genErrorXML(getStatus().getDescription());
       else
          return toXml(results);
    }

    /**
     * Returns two-dimensional coordinates in the ROI with roiID
     * for the annotation with annotationUID
     *
     * Mandatory parameters: collection, namespace, roiID, annotationUID
     * Optional parameters: none
     * @param queryForm
     * @return
     */
    @AIMQueryAPI
    public String get2DCoordinatesForROI(Form queryForm) {
    	log.info("get2DCoordinatesForROI");
       String collection = queryForm.getFirstValue("collection");
       if (collection == null || collection.isEmpty())
          collection = dbVars.get("defaultcontainer");
       String namespace = queryForm.getFirstValue("namespace");

       // Read in the UID and build a query for it
       String uid = queryForm.getFirstValue("annotationUID");
       String roiID = queryForm.getFirstValue("roiID");

       String uidConstraint = "true()";
       String roiConstraint = "true()";
       if(uid != null && !uid.isEmpty())
          uidConstraint = "$x[@uniqueIdentifier=\"" + uid + "\"]";
       if(roiID != null && !roiID.isEmpty())
          roiConstraint = "$x/geometricShapeCollection/GeometricShape[@id=\"" + roiID + "\"]";

       String queryString = "for $x in collection(\"" + collection + "\")/ImageAnnotation\n"
                          + "where " + uidConstraint + " and " + roiConstraint + "\n"
                          + "return (\n"
                          +    "for $y in $x/geometricShapeCollection/GeometricShape/spatialCoordinateCollection/SpatialCoordinate\n"
                          +    "where $y[@xsi:type=\"TwoDimensionSpatialCoordinate\"]\n"
                          +    "return concat(data($y/@x),', ',data($y/@y))\n"
                          + ")";

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null)
          return genErrorXML(getStatus().getDescription());
       else
          return toXml(results);
    }

    /**
     * Returns three-dimensional coordinates in the ROI with roiID
     * for the annotation with annotationUID
     *
     * Mandatory parameters: collection, namespace, roiID, annotationUID
     * Optional parameters: none
     * @param queryForm
     * @return
     */
    @AIMQueryAPI
    public String get3DCoordinatesForROI(Form queryForm) {
    	log.info("get3DCoordinatesForROI");
       String collection = queryForm.getFirstValue("collection");
       if (collection == null || collection.isEmpty())
          collection = dbVars.get("defaultcontainer");
       String namespace = queryForm.getFirstValue("namespace");

       // Read in the UID and build a query for it
       String uid = queryForm.getFirstValue("annotationUID");
       String roiID = queryForm.getFirstValue("roiID");

       String uidConstraint = "true()";
       String roiConstraint = "true()";
       if(uid != null && !uid.isEmpty())
          uidConstraint = "$x[@uniqueIdentifier=\"" + uid + "\"]";
       if(roiID != null && !roiID.isEmpty())
          roiConstraint = "$x/geometricShapeCollection/GeometricShape[@id=\"" + roiID + "\"]";

       String queryString = "for $x in collection(\"" + collection + "\")/ImageAnnotation\n"
                          + "where " + uidConstraint + " and " + roiConstraint + "\n"
                          + "return (\n"
                          +    "for $y in $x/geometricShapeCollection/GeometricShape/spatialCoordinateCollection/SpatialCoordinate\n"
                          +    "where $y[@xsi:type=\"ThreeDimensionSpatialCoordinate\"]\n"
                          +    "return concat(data($y/@x),', ',data($y/@y),', ',data($y/@z))\n"
                          + ")";

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null)
          return genErrorXML(getStatus().getDescription());
       else
          return toXml(results);
    }

    /**
     * Returns image reference UIDs for coordinates in the ROI with roiID
     * for the annotation with annotationUID
     *
     * Mandatory parameters: collection, namespace, roiID, annotationUID
     * Optional parameters: none
     * @param queryForm
     * @return
     */
    @AIMQueryAPI
    public String getImageReferenceUIDsForROI(Form queryForm) {
    	log.info("getImageReferenceUIDsForROI");
       String collection = queryForm.getFirstValue("collection");
       if (collection == null || collection.isEmpty())
          collection = dbVars.get("defaultcontainer");
       String namespace = queryForm.getFirstValue("namespace");

       // Read in the UID and build a query for it
       String uid = queryForm.getFirstValue("annotationUID");
       String roiID = queryForm.getFirstValue("roiID");

       String uidConstraint = "true()";
       String roiConstraint = "true()";
       if(uid != null && !uid.isEmpty())
          uidConstraint = "$x[@uniqueIdentifier=\"" + uid + "\"]";
       if(roiID != null && !roiID.isEmpty())
          roiConstraint = "$x/geometricShapeCollection/GeometricShape[@id=\"" + roiID + "\"]";

       String queryString = "for $x in collection(\"" + collection + "\")/ImageAnnotation\n"
                          + "where " + uidConstraint + " and " + roiConstraint + "\n"
                          + "return (\n"
                          +    "for $y in $x/geometricShapeCollection/GeometricShape/spatialCoordinateCollection/SpatialCoordinate\n"
                          +    "return data($y/@imageReferenceUID)\n"
                          + ")";

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null) {
          return genErrorXML(getStatus().getDescription());
       }
       else {
          return toXml(results);
       }
    }

    @AIMQueryAPI
    public String getXQuery(Form queryForm) {

    	String collection = queryForm.getFirstValue("collection");
        if (collection == null || collection.isEmpty())
           collection = dbVars.get("defaultcontainer");
        String namespace = queryForm.getFirstValue("namespace");
        String queryString = queryForm.getFirstValue("xquery");

       List<String> results = performQuery(collection, namespace, queryString);
       if(results == null) {
          return genErrorXML(getStatus().getDescription());
       }
       else {
          return toXml(results);
       }
    }
    
    @AIMQueryAPI
    public String deleteAnnotationByUID(Form queryForm) {

    	String collection = queryForm.getFirstValue("collection");
        if (collection == null || collection.isEmpty())
           collection = dbVars.get("defaultcontainer");
        //String namespace = queryForm.getFirstValue("namespace");
        String uid = queryForm.getFirstValue("uid");
        
        deleteFile(uid,collection);

        return uid;
    }

    
    /**
     * Performs an XQuery with the specified collection, namespace, and query
     * string.
     * @param collection
     * @param namespace
     * @param queryString
     * @return List containing the resulting values, or null if query failed
     * error.
     */
    private List<String> performQuery(String collection, String namespace, String queryString) {
       XmlManager dbManager = null;
       XmlContainer dbContainer = null;
       XmlResults queryResults = null;
       List<String> resultList = new ArrayList<String>();

       try {
          dbManager = new XmlManager(env, XmlManagerConfig.DEFAULT);
          dbContainer = dbManager.openContainer("aim.dbxml");

          // Set the context and collection for this query
          XmlQueryContext dbQueryContext = dbManager.createQueryContext(XmlQueryContext.LiveValues, XmlQueryContext.Eager);
          if (namespace != null && !namespace.isEmpty())
             dbQueryContext.setNamespace("", namespace);
          
          queryResults = dbManager.query(queryString, dbQueryContext);
          while (queryResults.hasNext()) {
             resultList.add(queryResults.next().asString());
          }
       } catch (DatabaseException de) {
          System.err.println(de.getMessage());
          setStatus(Status.SERVER_ERROR_INTERNAL, "XQuery evaluation error: " + de.getMessage());
          resultList = null;
//       } catch (Exception e) {
//    	   log.debug("Internal error e: " + e.getMessage() + " " +  resultList);
//          System.err.println(e.getMessage());
//          setStatus(Status.SERVER_ERROR_INTERNAL, "Internal error e: " + e.getMessage());
//          resultList = null;
       } finally {
          try {
             if(queryResults != null) {
                queryResults.delete();
             }
             if (dbContainer != null) {
                dbContainer.close();
             }
             if (dbManager != null) {
                dbManager.close();
             }
             cleanup();
          } catch (DatabaseException de) {
             System.err.println(de.getMessage());
          }
       }
       return resultList;
       
    }

    /**
     * Processes POST requests for annotations based on a predefined set of
     * functions.
     * @param entity
     * @return
     */
    @Post
    public Representation receiveRepresentation(Representation entity) {

       MediaType returnType = MediaType.TEXT_XML;
       Form queryForm = new Form(entity);
       String function = queryForm.getFirstValue("function");
       if(function == null) {

    	   Method m = getAIMQueryAPIMethod("getXQuery");
    	   
    	   try {
               String result = (String)m.invoke(this, queryForm);
               return new StringRepresentation(result, returnType);
            } catch (Exception e) {
               String err = "Error invoking function: " + e.getMessage();
               setStatus(Status.SERVER_ERROR_INTERNAL, err);
               return new StringRepresentation(genErrorXML(err), returnType);
            }
       }
       else {
          Method m = getAIMQueryAPIMethod(function);
          if(m != null) {
             try {
                String result = (String)m.invoke(this, queryForm);
                return new StringRepresentation(result, returnType);
             } catch (Exception e) {
                String err = "Error invoking function: " + e.getMessage();
                setStatus(Status.SERVER_ERROR_INTERNAL, err);
                return new StringRepresentation(genErrorXML(err), returnType);
             }
          }
          else {
             String err = "Unknown function requested.";
             setStatus(Status.CLIENT_ERROR_BAD_REQUEST, err);
             return new StringRepresentation(genErrorXML(err), returnType);
          }
       }

    }

    /**
     * Find and return the Method of this instance that matches the AIM
     * query API function
     * @param function
     * @return Matching AIM query API function, or null if it does not exist
     */
    private Method getAIMQueryAPIMethod(String function) {
       Method[] classMethods = this.getClass().getDeclaredMethods();
       for(Method m : classMethods) {
          if(m.getName().equals(function) && m.isAnnotationPresent(AIMQueryAPI.class))
             return m;
       }
       return null;
    }
    
    private Representation deleteFile(String documentName, String containerName) {
        Representation returnRep;

        log.info("AIM-XMLDB deleteFile containerName="+containerName+" remove document " + documentName);
        
        XmlManager manager = null;
        XmlContainer container = null;
        String returnString = "Documents removed:\n";
        try {

           manager = new XmlManager(env, XmlManagerConfig.DEFAULT);
           if (manager.existsContainer(containerName) == 0) {
              returnString = "Collection created.\n" + returnString;
              container = manager.createContainer(containerName);
           } else {
              container = manager.openContainer(containerName);
              returnString = "Removed from collection " + containerName + ".\n" + returnString;
           }
  
           XmlDocument currentDocument = container.getDocument(documentName);
     
           if (currentDocument != null) {
             container.deleteDocument(currentDocument);
             returnString += documentName + "\n";
            }
             
             returnRep = new StringRepresentation(returnString);
        } catch (DatabaseException e) {
           log.warning("XML Database exception during document delete.",e);
           returnRep = new StringRepresentation("Internal database error: " + e.getMessage());
        } catch (Exception e) {
            log.warning("Exception during XML document delete.",e);
            returnRep = new StringRepresentation("Exception during delete: " + e.getMessage());
        } finally {
             XmlDbUtil.closeXmlContainer(container);
             XmlDbUtil.closeXmlManager(manager);
             cleanup();
        }

        logReturnRep(returnRep); 
        return returnRep;
     }

    /**
     * For debugging.
     * @param returnRep Representation
     */
    private void logReturnRep(Representation returnRep){
        try{
            log.info(returnRep.getText());
        }catch (Exception e){
            log.warning("logReturnRep had",e);
        }
    }

}
