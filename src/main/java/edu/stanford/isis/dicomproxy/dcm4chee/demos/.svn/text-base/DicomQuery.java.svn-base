/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.dcm4chee.demos;

import edu.stanford.isis.dicomproxy.common.*;
import edu.stanford.isis.dicomproxy.server.*;
import org.apache.commons.cli.*;
import org.dcm4che2.data.*;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.net.*;
import org.dcm4che2.net.service.DicomService;
import org.dcm4che2.net.service.StorageService;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Make a DicomQuery.
 * <p>The following methods are used to build the arguments for the DcmQr method call.</p>
 * <ul>
 * <li>{@link #buildImagesSearchArgs(String)}</li>
 * <li>{@link #buildSeriesSearchArgs(String)}</li>
 * <li>{@link #buildStudySearchArgs(DicomSearchType, String)}</li>
 * </ul>
 * <p>These appear to be the methods called by other packages to carry out the
 *    Dicom search.</p>
 * <ul>
 * <li>
 * {@link #searchForStudies(DicomSearchType, String)} called by 
 * {@link edu.stanford.isis.dicomproxy.server.ProxyManager#getSearchResult(DicomSearchType,String)}
 * </li>
 * <li>
 * {@link #searchForSeries(String, String)} called by 
 * {@link edu.stanford.isis.dicomproxy.server.ProxyManager#getSeriesSearchResult(String,String)}
 * </li>
 * <li>
 * {@link #getImagesForSeries(DicomSeriesUID)} - can't find what calls this
 * </li>
 * </ul>
 * <p>{@link DicomSearchResult} represents the data returned by a search
 *    operation.</p>
 */
public class DicomQuery {

    private static ProxyLogger log = ProxyLogger.getInstance();

    public static void main(String[] args){
        DicomSearchResult retVal = DicomQuery.searchForStudies(DicomSearchType.PATIENT_ID,
                "83393488148532965622665512597663741359");

        System.out.println("#studies found: " + retVal.getStudies().size());
        int debug=1;

        for(DicomStudyData curr: retVal.getStudies()){
            System.out.println( "studyId="+curr.getStudyId() );
            System.out.println( "patientName="+curr.getPatientName() );
            System.out.println( "patientId="+curr.getPatientId() );
            System.out.println( "examType="+curr.getExamType() );
            System.out.println( "studyDate="+curr.getStudyDate() );
        }

        debug=2;
    }

    /**
     * Obtain a list of studies, based on a specified type of search and a search criteria.
     * 
     * @param searchType
     * @param searchParam
     * @return DicomSearchResult -
     */
    public static DicomSearchResult searchForStudies(DicomSearchType searchType, String searchParam){

        String[] dicomQueryArguments = buildStudySearchArgs(searchType, searchParam);

        mainOrig(dicomQueryArguments);
        return parseStudySearchResult(searchType, searchParam);
    }

    private static void debugSearchResultCache(SearchResultCache cache){
        ProxyLogger logger = ProxyLogger.getInstance();
        logger.info("DEBUG SearchResultCache: "+cache.toString());
    }

    /**
     * Obtain a list of series based on a study instance UID.
     * @param studyUID
     * @param remoteAddr
     * @return list of series
     */
    public static DicomSearchResult searchForSeries(String studyUID, String remoteAddr)
    {
        SearchResultCache searchResultCache = SearchResultCache.getInstance();

        SearchResult result = searchResultCache.getMostRecent(remoteAddr);
        if(result==null){
            debugSearchResultCache(searchResultCache);
            throw new IllegalStateException("Failed to find the root search for: studyUID="
                    +studyUID+" remoteAddr="+remoteAddr+" searchResultCache="+searchResultCache);
        }

        String[] dicomQueryArguments = buildSeriesSearchArgs(studyUID);

        mainOrig(dicomQueryArguments);
        return parseSeriesSearchResult(studyUID,result);
    }

    /**
     *	Return a list of instances, based on a series instance UID (Can't find any calls
     *  to this method).
     * @param seriesUID
     * @return
     */
    public static List<DicomImageData> getImagesForSeries(DicomSeriesUID seriesUID)
    {
        String[] dicomQueryArguments = buildImagesSearchArgs(seriesUID.toString());

        debugQueryArguments(dicomQueryArguments);

        mainOrig(dicomQueryArguments);
        return parseImageSearchResult();
    }

    /**
     * Prints the command line arguments for debugging.
     * @param args
     */
    private static void debugQueryArguments(String[] args){
        StringBuilder sb = new StringBuilder();
        int i=1;
        for(String curr : args){
            sb.append(i).append(") ").append(curr).append("\n");
            i++;
        }
        log.debug("Search Series command args: "+sb.toString());
    }

    /**
     *
     * @param studyUID
     * @param rootResult
     * @return
     */
    private static DicomSearchResult parseSeriesSearchResult(String studyUID, SearchResult rootResult){
        boolean isReadingTags=false;

        DicomStudyUID dicomStudyUID = new DicomStudyUID(studyUID);

        //ToDo: replace LOG which are static and thus single threaded, with a StringBuffer.
        String messages = LOG.toString();
        LOG.clear();

        Map<DicomTag,String> tags = null;

        String[] lines = messages.split("\n");
        for(String currLine : lines){

            //starting a new study?
            if( isReadingTags ){
                if(currLine.indexOf(")")<0){
                    isReadingTags=false;
                    rootResult.addSeriesToStudy(dicomStudyUID, tags);

                }else{
                    DicomTag tag = parseDcm4cheeTagFromLine(currLine);
                    String value = parseDcm4cheeResultFromLine(currLine);
                    System.out.println("Series Tag: "+tag+" = "+value); //ToDo: remove when debug finished.
                    tags.put(tag,value);
                }
            }else{
                if(currLine.indexOf("Query Response #")>0){
                    isReadingTags = true;
                    tags = new HashMap<DicomTag,String>();
                }
            }
        }

        return rootResult;
    }

    /**
     * Below is an example for parsing.
     *
     * This routine needs to look for "Query Response #" and then It needs to parse tags
     * until the next "Query Response #", or a line that has no tags.
     *
     * An example log is below.
     *
5 sec. ,Info: Connected to DCM4CHEE@171.65.102.125:11112 in 0.134 s
5 sec. ,Info: Send Query Request using 1.2.840.10008.5.1.4.1.2.2.1/Study Root Query/Retrieve Information Model - FIND:
(0008,0020) DA #0 [] Study Date
(0008,0030) TM #0 [] Study Time
(0008,0050) SH #0 [] Accession Number
(0008,0052) CS #6 [STUDY] Query/Retrieve Level
(0008,0061) CS #0 [] Modalities in Study
(0010,0010) PN #0 [] Patient's Name
(0010,0020) LO #38 [83393488148532965622665512597663741359] Patient ID
(0020,000D) UI #0 [] Study Instance UID
(0020,0010) SH #0 [] Study ID
(0020,1206) IS #0 [] Number of Study Related Series
(0020,1208) IS #0 [] Number of Study Related Instances

5 sec. ,Info: Query Response #1:
(0008,0020) DA #8 [20110110] Study Date
(0008,0030) TM #6 [095705] Study Time
(0008,0050) SH #8 [5989569] Accession Number
(0008,0052) CS #6 [STUDY] Query/Retrieve Level
(0008,0054) AE #8 [DCM4CHEE] Retrieve AE Title
(0008,0056) CS #6 [ONLINE] Instance Availability
(0008,0061) CS #2 [CR] Modalities in Study
(0010,0010) PN #16 [PTL-1-132-833934] Patient's Name
(0010,0020) LO #38 [83393488148532965622665512597663741359] Patient ID
(0020,000D) UI #64 [1.2.826.0.1.3680043.8.420.20881664157860029483433361314058674184] Study Instance
(0020,0010) SH #8 [5989569] Study ID
(0020,1206) IS #2 [2] Number of Study Related Series
(0020,1208) IS #2 [2] Number of Study Related Instances
(0088,0130) SH #0 [] Storage Media File-set ID
(0088,0140) UI #0 [] Storage Media File-set UID

5 sec. ,Info: Received 1 matching entries in 0.054 s
5 sec. ,Info: Released connection to DCM4CHEE@171.65.102.125:11112
Disconnected from the target VM, address: '127.0.0.1:50011', transport: 'socket'     *
     *
     */
    private static DicomSearchResult parseStudySearchResult(DicomSearchType searchType, String searchParam){

        boolean isReadingTags=false;

        SearchResult retVal = new SearchResult(searchType,searchParam);

        //NOTE:: We are using a static log here, which will not work for multiple
        //       simultaneous calls. This needs to be fixed.
        String messages = LOG.toString();
        LOG.clear();

        Map<DicomTag,String> tags = null;

        String[] lines = messages.split("\n");
        for(String currLine : lines){

            //starting a new study?
            if( isReadingTags ){
                if(currLine.indexOf(")")<0){
                    isReadingTags=false;
                    retVal.addStudyResults(tags);
                }else{
                    DicomTag tag = parseDcm4cheeTagFromLine(currLine);
                    String value = parseDcm4cheeResultFromLine(currLine);

                    tags.put(tag,value);
                }
            }else{
                if(currLine.indexOf("Query Response #")>0){
                    isReadingTags = true;
                    tags = new HashMap<DicomTag,String>();
                }
            }
        }

        SearchResultCache cache = SearchResultCache.getInstance();
        cache.cache(retVal,searchType,searchParam);

        return retVal;

    }

    /**
     *
     *
11:03:24,816 INFO   - Query Response #22 for Query Request #4/61:
(0008,0005) CS #10 [ISO_IR 100] Specific Character Set
(0008,0016) UI #26 [1.2.840.10008.5.1.4.1.1.4] SOP Class UID
(0008,0018) UI #64 [1.2.826.0.1.3680043.8.420.30985929668125050082535509534643661667] SOP Instance U
(0008,0022) DA #0 [] Acquisition Date
(0008,0023) DA #0 [] Content Date
(0008,002A) DT #0 [] Acquisition DateTime
(0008,0052) CS #6 [IMAGE] Query/Retrieve Level
(0008,0054) AE #8 [DCM4CHEE] Retrieve AE Title
(0008,0056) CS #6 [ONLINE] Instance Availability
(0020,000D) UI #64 [1.2.826.0.1.3680043.8.420.17864099421429702484295733867604129208] Study Instance
(0020,000E) UI #64 [1.2.826.0.1.3680043.8.420.16088526745603431705311736218566340373] Series Instanc
(0020,0013) IS #2 [23] Instance Number
(0088,0130) SH #0 [] Storage Media File-set ID
(0088,0140) UI #0 [] Storage Media File-set UID

11:03:24,816 INFO   - Query Response #23 for Query Request #4/61:
(0008,0005) CS #10 [ISO_IR 100] Specific Character Set
(0008,0016) UI #26 [1.2.840.10008.5.1.4.1.1.4] SOP Class UID
(0008,0018) UI #64 [1.2.826.0.1.3680043.8.420.14882538330581136693695353976522568667] SOP Instance U
(0008,0022) DA #0 [] Acquisition Date
(0008,0023) DA #0 [] Content Date
(0008,002A) DT #0 [] Acquisition DateTime
(0008,0052) CS #6 [IMAGE] Query/Retrieve Level
(0008,0054) AE #8 [DCM4CHEE] Retrieve AE Title
(0008,0056) CS #6 [ONLINE] Instance Availability
(0020,000D) UI #64 [1.2.826.0.1.3680043.8.420.17864099421429702484295733867604129208] Study Instance
(0020,000E) UI #64 [1.2.826.0.1.3680043.8.420.16088526745603431705311736218566340373] Series Instanc
(0020,0013) IS #2 [14] Instance Number
(0088,0130) SH #0 [] Storage Media File-set ID
(0088,0140) UI #0 [] Storage Media File-set UID
     *
     * @return
     */
    private static List<DicomImageData> parseImageSearchResult(){

        boolean isReadingTags=false;
        List<RImageData> retVal = new ArrayList<RImageData>();

        String messages = LOG.toString();
        LOG.clear();

        Map<DicomTag,String> tags = null;

        String[] lines = messages.split("\n");
        for(String currLine : lines){

            //starting a new study?
            if( isReadingTags ){
                if(currLine.indexOf(")")<0){
                    isReadingTags=false;

                    retVal.add( new RImageData(tags) );

                }else{
                    DicomTag tag = parseDcm4cheeTagFromLine(currLine);
                    String value = parseDcm4cheeResultFromLine(currLine);
                    System.out.println("parseSearchResults - Image Tag: "+tag+" = "+value); //ToDo: remove when debug finished.
                    tags.put(tag,value);
                }
            }else{
                if(currLine.indexOf("Query Response #")>0 && (currLine.indexOf("for Query Request #"))>0){
                    isReadingTags = true;
                    tags = new HashMap<DicomTag,String>();
                }
            }
        }

        //sort the list by InstanceNumber.
        log.debug("series has: #images="+retVal.size());

        Collections.sort(retVal);
        return new ArrayList<DicomImageData>(retVal);
    }


    /**
     *
     * @param line - like (0010,0010) PN #16 [PTL-1-132-833934] Patient's Name
     * @return Tag - DCM4CHEE tag for (0010,0010)
     */
    private static DicomTag parseDcm4cheeTagFromLine(String line){

        String backetFormat = line.substring(line.indexOf("("),line.indexOf(")"));
        return DicomTag.forBracketFormat(backetFormat);
    }

    /**
     * Get the text between the [] characters.
     * @param line - like (0010,0010) PN #16 [PTL-1-132-833934] Patient's Name
     * @return String - PTL-1-132-833934
     */
    private static String parseDcm4cheeResultFromLine(String line){

        return line.substring(line.indexOf("[")+1,line.indexOf("]"));

    }

    /**
     * Create a request string for a group of instances belonging to a series.
     * @param seriesUID
     * @return array of strings containing cli request broken into words by splitting
     *         on spaces
     */
    private static String[] buildImagesSearchArgs(String seriesUID){
        ProxyConfig proxyConfig = ProxyConfig.getInstance();

        StringBuilder sb = new StringBuilder();
        sb.append(proxyConfig.getParam("DicomServerAETitle"));
        sb.append("@");
        sb.append(proxyConfig.getParam("DicomServerIP")); //("171.65.102.125");
        sb.append(":");
        sb.append(proxyConfig.getParam("DicomServerPort")); //("11112");
        sb.append(" -L ");
        sb.append(proxyConfig.getParam("ListenIP")); //("171.65.102.233");
        sb.append(":");
        sb.append(proxyConfig.getParam("ListenPort")); //("4096");
        sb.append(" -q ");
        sb.append("SeriesInstanceUID=");
        sb.append(seriesUID);
        sb.append(" -I");
        sb.append(" -r AcquisitionDate");
        sb.append(" -r ContentDate");
        sb.append(" -r AcquisitionDateTime");
        sb.append(" -r SOPInstanceUID");
        sb.append(" -r InstanceNumber");

        return sb.toString().split(" ");
    }

    /**
     * Create a request string for a group of series belonging to a specific study.
     * <p>Example call:</p>
     *
     * <p>./dcmqr DCM4CHEE@171.65.102.125:11112 -L 171.65.102.233:4096 -r PatientName -q StudyInstanceUID=1.2.826.0.1.3680043.8.420.17864099421429702484295733867604129208 -S -r SeriesDate -r AcquisitionDate -r ContentDate -r AcquisitionDateTime -r Modality</p>
     *
     * @param studyUID - String
     * @return String[]
     */
    private static String[] buildSeriesSearchArgs(String studyUID){

        ProxyConfig proxyConfig = ProxyConfig.getInstance();

        StringBuilder sb = new StringBuilder();
        sb.append(proxyConfig.getParam("DicomServerAETitle"));
        sb.append("@");
        sb.append(proxyConfig.getParam("DicomServerIP")); //("171.65.102.125");
        sb.append(":");
        sb.append(proxyConfig.getParam("DicomServerPort")); //("11112");
        sb.append(" -L ");
        sb.append(proxyConfig.getParam("ListenIP")); //("171.65.102.233");
        sb.append(":");
        sb.append(proxyConfig.getParam("ListenPort")); //("4096");
        sb.append(" -q ");
        sb.append("StudyInstanceUID=");
        sb.append(studyUID);
        sb.append(" -S");
        sb.append(" -r SeriesDate");
        sb.append(" -r SeriesTime");
        sb.append(" -r AcquisitionDate");
        sb.append(" -r ContentDate");
        sb.append(" -r AcquisitionDateTime");
        sb.append(" -r Modality");
        sb.append(" -r PatientName");
        sb.append(" -r PatientID");
        sb.append(" -r SeriesDescription");
        //sb.append(" -r ActualSeriesDataTimeStamp");
        sb.append(" -r SeriesInstanceUID");
        sb.append(" -r SeriesNumber");
        sb.append(" -r AcquisitionsInSeries");
        sb.append(" -r ImagesInSeries");
        sb.append(" -r SeriesType");

        return sb.toString().split(" ");
    }

    /**
     * Create a request string for a group of studies based on a search parameter.
     * 
     * @param searchType - String
     * @param searchParam - String
     * @return String[]
     */
    private static String[] buildStudySearchArgs(DicomSearchType searchType, String searchParam){

        ProxyConfig proxyConfig = ProxyConfig.getInstance();

        Set<String> neededParams = createRequiredParams();

        StringBuilder sb = new StringBuilder();
        sb.append(proxyConfig.getParam("DicomServerAETitle"));//("DCM4CHEE"); //
        sb.append("@");
        sb.append(proxyConfig.getParam("DicomServerIP"));//("171.65.102.125"); //
        sb.append(":");
        sb.append(proxyConfig.getParam("DicomServerPort"));//("11112"); //
        sb.append(" -L ");
        sb.append(proxyConfig.getParam("ListenIP"));//("171.65.102.233"); //
        sb.append(":");
        sb.append(proxyConfig.getParam("ListenPort"));//("4096"); //
        sb.append(" -q ");
        switch (searchType){
            case PATIENT_NAME:
                sb.append("PatientName=");
                neededParams.remove("PatientName");
                break;
            case PATIENT_ID:
                sb.append("PatientID=");
                neededParams.remove("PatientID");
                break;
            case ASSESION_NUM:
                sb.append("AccessionNumber=");
                break;
            case STUDY_DATE:
                sb.append("StudyDate=");
                break;
            case EXAM_TYPE:
                sb.append("ModalitiesInStudy=");
                neededParams.remove("ModalitiesInStudy");
                break;

            default:
        }
        sb.append(searchParam);
        sb.append(appendExtraResultParams(neededParams));

        return sb.toString().split(" ");
    }

    /**
     * These are some extra parameter needed as -r in the DicomQuery. Create
     * a set to determine which ones are needed depending on the search type since
     * -r and -q parameters cannot be duplicate.
     * @return
     */
    private static Set<String> createRequiredParams(){
        Set<String> retVal = new HashSet<String>();
        retVal.add("PatientName");
        retVal.add("PatientID");
        retVal.add("ModalitiesInStudy");

        return retVal;
    }

    /**
     *
     * @return Sting in format " -r PatientName -r ModalitiesInStudy"
     */
    private static String appendExtraResultParams(Set<String> extraRParams){
        StringBuilder sb = new StringBuilder("");
        for(String curr: extraRParams){
            sb.append(" -r ");
            sb.append(curr);
        }
        return sb.toString();
    }


    private static final MessageBuilder LOG = new MessageBuilder();

    private static final int KB = 1024;

    private static final String USAGE = "dcmqr <aet>[@<host>[:<port>]] [Options]";

    private static final String DESCRIPTION =
        "Query specified remote Application Entity (=Query/Retrieve SCP) "
        + "and optional (s. option -cget/-cmove) retrieve instances of "
        + "matching entities. If <port> is not specified, DICOM default port "
        + "104 is assumed. If also no <host> is specified localhost is assumed. "
        + "Also Storage Services can be provided (s. option -cstore) to receive "
        + "retrieved instances. For receiving objects retrieved by C-MOVE in a "
        + "separate association, a local listening port must be specified "
        + "(s.option -L).\n"
        + "Options:";

    private static final String EXAMPLE =
        "\nExample: dcmqr -L QRSCU:11113 QRSCP@localhost:11112 -cmove QRSCU " +
        "-qStudyDate=20060204 -qModalitiesInStudy=CT -cstore CT -cstore PR:LE " +
        "-cstoredest /tmp\n"
        + "=> Query Application Entity QRSCP listening on local port 11112 for "
        + "CT studies from Feb 4, 2006 and retrieve matching studies by C-MOVE "
        + "to own Application Entity QRSCU listing on local port 11113, "
        + "storing received CT images and Grayscale Softcopy Presentation "
        + "states to /tmp.";

    private static String[] TLS1 = { "TLSv1" };

    private static String[] SSL3 = { "SSLv3" };

    private static String[] NO_TLS1 = { "SSLv3", "SSLv2Hello" };

    private static String[] NO_SSL2 = { "TLSv1", "SSLv3" };

    private static String[] NO_SSL3 = { "TLSv1", "SSLv2Hello" };

    private static char[] SECRET = { 's', 'e', 'c', 'r', 'e', 't' };

    public static enum QueryRetrieveLevel {
        PATIENT("PATIENT", PATIENT_RETURN_KEYS, PATIENT_LEVEL_FIND_CUID,
                PATIENT_LEVEL_GET_CUID, PATIENT_LEVEL_MOVE_CUID),
        STUDY("STUDY", STUDY_RETURN_KEYS, STUDY_LEVEL_FIND_CUID,
                STUDY_LEVEL_GET_CUID, STUDY_LEVEL_MOVE_CUID),
        SERIES("SERIES", SERIES_RETURN_KEYS, SERIES_LEVEL_FIND_CUID,
                SERIES_LEVEL_GET_CUID, SERIES_LEVEL_MOVE_CUID),
        IMAGE("IMAGE", INSTANCE_RETURN_KEYS, SERIES_LEVEL_FIND_CUID,
                SERIES_LEVEL_GET_CUID, SERIES_LEVEL_MOVE_CUID);

        private final String code;
        private final int[] returnKeys;
        private final String[] findClassUids;
        private final String[] getClassUids;
        private final String[] moveClassUids;

        private QueryRetrieveLevel(String code, int[] returnKeys,
                String[] findClassUids, String[] getClassUids,
                String[] moveClassUids) {
            this.code = code;
            this.returnKeys = returnKeys;
            this.findClassUids = findClassUids;
            this.getClassUids = getClassUids;
            this.moveClassUids = moveClassUids;
        }

        public String getCode() {
            return code;
        }

        public int[] getReturnKeys() {
            return returnKeys;
        }

        public String[] getFindClassUids() {
            return findClassUids;
        }

        public String[] getGetClassUids() {
            return getClassUids;
        }

        public String[] getMoveClassUids() {
            return moveClassUids;
        }
    }

    private static final String[] PATIENT_LEVEL_FIND_CUID = {
        UID.PatientRootQueryRetrieveInformationModelFIND,
        UID.PatientStudyOnlyQueryRetrieveInformationModelFINDRetired };

    private static final String[] STUDY_LEVEL_FIND_CUID = {
        UID.StudyRootQueryRetrieveInformationModelFIND,
        UID.PatientRootQueryRetrieveInformationModelFIND,
        UID.PatientStudyOnlyQueryRetrieveInformationModelFINDRetired };

    private static final String[] SERIES_LEVEL_FIND_CUID = {
        UID.StudyRootQueryRetrieveInformationModelFIND,
        UID.PatientRootQueryRetrieveInformationModelFIND, };

    private static final String[] PATIENT_LEVEL_GET_CUID = {
        UID.PatientRootQueryRetrieveInformationModelGET,
        UID.PatientStudyOnlyQueryRetrieveInformationModelGETRetired };

    private static final String[] STUDY_LEVEL_GET_CUID = {
        UID.StudyRootQueryRetrieveInformationModelGET,
        UID.PatientRootQueryRetrieveInformationModelGET,
        UID.PatientStudyOnlyQueryRetrieveInformationModelGETRetired };

    private static final String[] SERIES_LEVEL_GET_CUID = {
        UID.StudyRootQueryRetrieveInformationModelGET,
        UID.PatientRootQueryRetrieveInformationModelGET };

    private static final String[] PATIENT_LEVEL_MOVE_CUID = {
        UID.PatientRootQueryRetrieveInformationModelMOVE,
        UID.PatientStudyOnlyQueryRetrieveInformationModelMOVERetired };

    private static final String[] STUDY_LEVEL_MOVE_CUID = {
        UID.StudyRootQueryRetrieveInformationModelMOVE,
        UID.PatientRootQueryRetrieveInformationModelMOVE,
        UID.PatientStudyOnlyQueryRetrieveInformationModelMOVERetired };

    private static final String[] SERIES_LEVEL_MOVE_CUID = {
        UID.StudyRootQueryRetrieveInformationModelMOVE,
        UID.PatientRootQueryRetrieveInformationModelMOVE };

    private static final int[] PATIENT_RETURN_KEYS = {
        Tag.PatientName,
        Tag.PatientID,
        Tag.PatientBirthDate,
        Tag.PatientSex,
        Tag.NumberOfPatientRelatedStudies,
        Tag.NumberOfPatientRelatedSeries,
        Tag.NumberOfPatientRelatedInstances };

    private static final int[] PATIENT_MATCHING_KEYS = {
        Tag.PatientName,
        Tag.PatientID,
        Tag.IssuerOfPatientID,
        Tag.PatientBirthDate,
        Tag.PatientSex };

    private static final int[] STUDY_RETURN_KEYS = {
        Tag.StudyDate,
        Tag.StudyTime,
        Tag.AccessionNumber,
        Tag.StudyID,
        Tag.StudyInstanceUID,
        Tag.NumberOfStudyRelatedSeries,
        Tag.NumberOfStudyRelatedInstances };

    private static final int[] STUDY_MATCHING_KEYS = {
        Tag.StudyDate,
        Tag.StudyTime,
        Tag.AccessionNumber,
        Tag.ModalitiesInStudy,
        Tag.ReferringPhysicianName,
        Tag.StudyID,
        Tag.StudyInstanceUID };

    private static final int[] PATIENT_STUDY_MATCHING_KEYS = {
        Tag.StudyDate,
        Tag.StudyTime,
        Tag.AccessionNumber,
        Tag.ModalitiesInStudy,
        Tag.ReferringPhysicianName,
        Tag.PatientName,
        Tag.PatientID,
        Tag.IssuerOfPatientID,
        Tag.PatientBirthDate,
        Tag.PatientSex,
        Tag.StudyID,
        Tag.StudyInstanceUID };

    private static final int[] SERIES_RETURN_KEYS = {
        Tag.Modality,
        Tag.SeriesNumber,
        Tag.SeriesInstanceUID,
        Tag.NumberOfSeriesRelatedInstances };

    private static final int[] SERIES_MATCHING_KEYS = {
        Tag.Modality,
        Tag.SeriesNumber,
        Tag.SeriesInstanceUID,
        Tag.RequestAttributesSequence
    };

    private static final int[] INSTANCE_RETURN_KEYS = {
        Tag.InstanceNumber,
        Tag.SOPClassUID,
        Tag.SOPInstanceUID, };

    private static final int[] MOVE_KEYS = {
        Tag.QueryRetrieveLevel,
        Tag.PatientID,
        Tag.StudyInstanceUID,
        Tag.SeriesInstanceUID,
        Tag.SOPInstanceUID, };

    private static final String[] IVRLE_TS = {
        UID.ImplicitVRLittleEndian };

    private static final String[] NATIVE_LE_TS = {
        UID.ExplicitVRLittleEndian,
        UID.ImplicitVRLittleEndian};

    private static final String[] NATIVE_BE_TS = {
        UID.ExplicitVRBigEndian,
        UID.ImplicitVRLittleEndian};

    private static final String[] DEFLATED_TS = {
        UID.DeflatedExplicitVRLittleEndian,
        UID.ExplicitVRLittleEndian,
        UID.ImplicitVRLittleEndian};

    private static final String[] NOPX_TS = {
        UID.NoPixelData,
        UID.ExplicitVRLittleEndian,
        UID.ImplicitVRLittleEndian};

    private static final String[] NOPXDEFL_TS = {
        UID.NoPixelDataDeflate,
        UID.NoPixelData,
        UID.ExplicitVRLittleEndian,
        UID.ImplicitVRLittleEndian};

    private static final String[] JPLL_TS = {
        UID.JPEGLossless,
        UID.JPEGLosslessNonHierarchical14,
        UID.JPEGLSLossless,
        UID.JPEG2000LosslessOnly,
        UID.ExplicitVRLittleEndian,
        UID.ImplicitVRLittleEndian};

    private static final String[] JPLY_TS = {
        UID.JPEGBaseline1,
        UID.JPEGExtended24,
        UID.JPEGLSLossyNearLossless,
        UID.JPEG2000,
        UID.ExplicitVRLittleEndian,
        UID.ImplicitVRLittleEndian};

    private static final String[] MPEG2_TS = { UID.MPEG2 };

    private static final String[] DEF_TS = {
        UID.JPEGLossless,
        UID.JPEGLosslessNonHierarchical14,
        UID.JPEGLSLossless,
        UID.JPEGLSLossyNearLossless,
        UID.JPEG2000LosslessOnly,
        UID.JPEG2000,
        UID.JPEGBaseline1,
        UID.JPEGExtended24,
        UID.MPEG2,
        UID.DeflatedExplicitVRLittleEndian,
        UID.ExplicitVRBigEndian,
        UID.ExplicitVRLittleEndian,
        UID.ImplicitVRLittleEndian};

    private static enum TS {
        IVLE(IVRLE_TS),
        LE(NATIVE_LE_TS),
        BE(NATIVE_BE_TS),
        DEFL(DEFLATED_TS),
        JPLL(JPLL_TS),
        JPLY(JPLY_TS),
        MPEG2(MPEG2_TS),
        NOPX(NOPX_TS),
        NOPXD(NOPXDEFL_TS);

        final String[] uids;
        TS(String[] uids) { this.uids = uids; }
    }

    private static enum CUID {
        CR(UID.ComputedRadiographyImageStorage),
        CT(UID.CTImageStorage),
        MR(UID.MRImageStorage),
        US(UID.UltrasoundImageStorage),
        NM(UID.NuclearMedicineImageStorage),
        PET(UID.PositronEmissionTomographyImageStorage),
        SC(UID.SecondaryCaptureImageStorage),
        XA(UID.XRayAngiographicImageStorage),
        XRF(UID.XRayRadiofluoroscopicImageStorage),
        DX(UID.DigitalXRayImageStorageForPresentation),
        MG(UID.DigitalMammographyXRayImageStorageForPresentation),
        PR(UID.GrayscaleSoftcopyPresentationStateStorageSOPClass),
        KO(UID.KeyObjectSelectionDocumentStorage),
        SR(UID.BasicTextSRStorage);

        final String uid;
        CUID(String uid) { this.uid = uid; }

    }

    private static final String[] EMPTY_STRING = {};

    private final Executor executor;

    private final NetworkApplicationEntity remoteAE = new NetworkApplicationEntity();

    private final NetworkConnection remoteConn = new NetworkConnection();

    private final Device device;

    private final NetworkApplicationEntity ae = new NetworkApplicationEntity();

    private final NetworkConnection conn = new NetworkConnection();

    private Association assoc;

    private int priority = 0;

    private boolean cfind;

    private boolean cget;

    private String moveDest;

    private File storeDest;

    private boolean devnull;

    private int fileBufferSize = 256;

    private boolean evalRetrieveAET = false;

    private QueryRetrieveLevel qrlevel = QueryRetrieveLevel.STUDY;

    private List<String> privateFind = new ArrayList<String>();

    private final List<TransferCapability> storeTransferCapability =
            new ArrayList<TransferCapability>(8);

    private DicomObject keys = new BasicDicomObject();

    private int cancelAfter = Integer.MAX_VALUE;

    private int completed;

    private int warning;

    private int failed;

    private boolean relationQR;

    private boolean dateTimeMatching;

    private boolean fuzzySemanticPersonNameMatching;

    private boolean noExtNegotiation;

    private String keyStoreURL = "resource:tls/test_sys_1.p12";

    private char[] keyStorePassword = SECRET;

    private char[] keyPassword;

    private String trustStoreURL = "resource:tls/mesa_certs.jks";

    private char[] trustStorePassword = SECRET;

    /**
     *
     * @param name
     */
    public DicomQuery(String name) {
        device = new Device(name);
        executor = new NewThreadExecutor(name);
        remoteAE.setInstalled(true);
        remoteAE.setAssociationAcceptor(true);
        remoteAE.setNetworkConnection(new NetworkConnection[] { remoteConn });

        device.setNetworkApplicationEntity(ae);
        device.setNetworkConnection(conn);
        ae.setNetworkConnection(conn);
        ae.setAssociationInitiator(true);
        ae.setAssociationAcceptor(true);
        ae.setAETitle(name);
    }

    public final void setLocalHost(String hostname) {
        conn.setHostname(hostname);
    }

    public final void setLocalPort(int port) {
        conn.setPort(port);
    }

    public final void setRemoteHost(String hostname) {
        remoteConn.setHostname(hostname);
    }

    public final void setRemotePort(int port) {
        remoteConn.setPort(port);
    }

    public final void setTlsProtocol(String[] tlsProtocol) {
        conn.setTlsProtocol(tlsProtocol);
    }

    public final void setTlsWithoutEncyrption() {
        conn.setTlsWithoutEncyrption();
        remoteConn.setTlsWithoutEncyrption();
    }

    public final void setTls3DES_EDE_CBC() {
        conn.setTls3DES_EDE_CBC();
        remoteConn.setTls3DES_EDE_CBC();
    }

    public final void setTlsAES_128_CBC() {
        conn.setTlsAES_128_CBC();
        remoteConn.setTlsAES_128_CBC();
    }

    public final void setTlsNeedClientAuth(boolean needClientAuth) {
        conn.setTlsNeedClientAuth(needClientAuth);
    }

    public final void setKeyStoreURL(String url) {
        keyStoreURL = url;
    }

    public final void setKeyStorePassword(String pw) {
        keyStorePassword = pw.toCharArray();
    }

    public final void setKeyPassword(String pw) {
        keyPassword = pw.toCharArray();
    }

    public final void setTrustStorePassword(String pw) {
        trustStorePassword = pw.toCharArray();
    }

    public final void setTrustStoreURL(String url) {
        trustStoreURL = url;
    }

    public final void setCalledAET(String called, boolean reuse) {
        remoteAE.setAETitle(called);
        if (reuse)
            ae.setReuseAssocationToAETitle(new String[] { called });
    }

    public final void setCalling(String calling) {
        ae.setAETitle(calling);
    }

    public final void setUserIdentity(UserIdentity userIdentity) {
        ae.setUserIdentity(userIdentity);
    }

    public final void setPriority(int priority) {
        this.priority = priority;
    }

    public final void setConnectTimeout(int connectTimeout) {
        conn.setConnectTimeout(connectTimeout);
    }

    public final void setMaxPDULengthReceive(int maxPDULength) {
        ae.setMaxPDULengthReceive(maxPDULength);
    }

    public final void setMaxOpsInvoked(int maxOpsInvoked) {
        ae.setMaxOpsInvoked(maxOpsInvoked);
    }

    public final void setMaxOpsPerformed(int maxOps) {
        ae.setMaxOpsPerformed(maxOps);
    }

    public final void setPackPDV(boolean packPDV) {
        ae.setPackPDV(packPDV);
    }

    public final void setAssociationReaperPeriod(int period) {
        device.setAssociationReaperPeriod(period);
    }

    public final void setDimseRspTimeout(int timeout) {
        ae.setDimseRspTimeout(timeout);
    }

    public final void setRetrieveRspTimeout(int timeout) {
        ae.setRetrieveRspTimeout(timeout);
    }

    public final void setTcpNoDelay(boolean tcpNoDelay) {
        conn.setTcpNoDelay(tcpNoDelay);
    }

    public final void setAcceptTimeout(int timeout) {
        conn.setAcceptTimeout(timeout);
    }

    public final void setReleaseTimeout(int timeout) {
        conn.setReleaseTimeout(timeout);
    }

    public final void setSocketCloseDelay(int timeout) {
        conn.setSocketCloseDelay(timeout);
    }

    public final void setMaxPDULengthSend(int maxPDULength) {
        ae.setMaxPDULengthSend(maxPDULength);
    }

    public final void setReceiveBufferSize(int bufferSize) {
        conn.setReceiveBufferSize(bufferSize);
    }

    public final void setSendBufferSize(int bufferSize) {
        conn.setSendBufferSize(bufferSize);
    }

    public final void setFileBufferSize(int size) {
        fileBufferSize = size;
    }

    private static CommandLine parse(String[] args) throws IllegalArgumentException {
        Options opts = new Options();

        OptionBuilder.withArgName("name");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "set device name, use DCMQR by default");
        opts.addOption(OptionBuilder.create("device"));

        OptionBuilder.withArgName("aet[@host][:port]");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "set AET, local address and listening port of local"
                + "Application Entity, use device name and pick up any valid "
                + "local address to bind the socket by default");
        opts.addOption(OptionBuilder.create("L"));

        OptionBuilder.withArgName("username");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "enable User Identity Negotiation with specified username and "
                + " optional passcode");
        opts.addOption(OptionBuilder.create("username"));

        OptionBuilder.withArgName("passcode");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "optional passcode for User Identity Negotiation, "
                + "only effective with option -username");
        opts.addOption(OptionBuilder.create("passcode"));

        opts.addOption("uidnegrsp", false,
                "request positive User Identity Negotation response, "
                + "only effective with option -username");

        OptionBuilder.withArgName("NULL|3DES|AES");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "enable TLS connection without, 3DES or AES encryption");
        opts.addOption(OptionBuilder.create("tls"));

        OptionGroup tlsProtocol = new OptionGroup();
        tlsProtocol.addOption(new Option("tls1",
                "disable the use of SSLv3 and SSLv2 for TLS connections"));
        tlsProtocol.addOption(new Option("ssl3",
                "disable the use of TLSv1 and SSLv2 for TLS connections"));
        tlsProtocol.addOption(new Option("no_tls1",
                "disable the use of TLSv1 for TLS connections"));
        tlsProtocol.addOption(new Option("no_ssl3",
                "disable the use of SSLv3 for TLS connections"));
        tlsProtocol.addOption(new Option("no_ssl2",
                "disable the use of SSLv2 for TLS connections"));
        opts.addOptionGroup(tlsProtocol);

        opts.addOption("noclientauth", false,
                "disable client authentification for TLS");

        OptionBuilder.withArgName("file|url");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "file path or URL of P12 or JKS keystore, resource:tls/test_sys_1.p12 by default");
        opts.addOption(OptionBuilder.create("keystore"));

        OptionBuilder.withArgName("password");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "password for keystore file, 'secret' by default");
        opts.addOption(OptionBuilder.create("keystorepw"));

        OptionBuilder.withArgName("password");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "password for accessing the key in the keystore, keystore password by default");
        opts.addOption(OptionBuilder.create("keypw"));

        OptionBuilder.withArgName("file|url");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "file path or URL of JKS truststore, resource:tls/mesa_certs.jks by default");
        opts.addOption(OptionBuilder.create("truststore"));

        OptionBuilder.withArgName("password");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "password for truststore file, 'secret' by default");
        opts.addOption(OptionBuilder.create("truststorepw"));

        OptionBuilder.withArgName("aet");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "retrieve instances of matching entities by C-MOVE to specified destination.");
        opts.addOption(OptionBuilder.create("cmove"));

        opts.addOption("nocfind", false,
                "retrieve instances without previous query - unique keys must be specified by -q options");

        opts.addOption("cget", false, "retrieve instances of matching entities by C-GET.");

        OptionBuilder.withArgName("cuid[:ts]");
        OptionBuilder.hasArgs();
        OptionBuilder.withDescription(
                "negotiate support of specified Storage SOP Class and Transfer "
                + "Syntaxes. The Storage SOP Class may be specified by its UID "
                + "or by one of following key words:\n"
                + "CR  - Computed Radiography Image Storage\n"
                + "CT  - CT Image Storage\n"
                + "MR  - MRImageStorage\n"
                + "US  - Ultrasound Image Storage\n"
                + "NM  - Nuclear Medicine Image Storage\n"
                + "PET - PET Image Storage\n"
                + "SC  - Secondary Capture Image Storage\n"
                + "XA  - XRay Angiographic Image Storage\n"
                + "XRF - XRay Radiofluoroscopic Image Storage\n"
                + "DX  - Digital X-Ray Image Storage for Presentation\n"
                + "MG  - Digital Mammography X-Ray Image Storage for Presentation\n"
                + "PR  - Grayscale Softcopy Presentation State Storage\n"
                + "KO  - Key Object Selection Document Storage\n"
                + "SR  - Basic Text Structured Report Document Storage\n"
                + "The Transfer Syntaxes may be specified by a comma "
                + "separated list of UIDs or by one of following key "
                + "words:\n"
                + "IVRLE - offer only Implicit VR Little Endian "
                + "Transfer Syntax\n"
                + "LE - offer Explicit and Implicit VR Little Endian "
                + "Transfer Syntax\n"
                + "BE - offer Explicit VR Big Endian Transfer Syntax\n"
                + "DEFL - offer Deflated Explicit VR Little "
                + "Endian Transfer Syntax\n"
                + "JPLL - offer JEPG Loss Less Transfer Syntaxes\n"
                + "JPLY - offer JEPG Lossy Transfer Syntaxes\n"
                + "MPEG2 - offer MPEG2 Transfer Syntax\n"
                + "NOPX - offer No Pixel Data Transfer Syntax\n"
                + "NOPXD - offer No Pixel Data Deflate Transfer Syntax\n"
                + "If only the Storage SOP Class is specified, all "
                + "Transfer Syntaxes listed above except No Pixel Data "
                + "and No Pixel Data Delflate Transfer Syntax are "
                + "offered.");
        opts.addOption(OptionBuilder.create("cstore"));

        OptionBuilder.withArgName("dir");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "store received objects into files in specified directory <dir>."
                        + " Do not store received objects by default.");
        opts.addOption(OptionBuilder.create("cstoredest"));

        opts.addOption("ivrle", false, "offer only Implicit VR Little Endian Transfer Syntax.");

        OptionBuilder.withArgName("maxops");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("maximum number of outstanding C-MOVE-RQ " +
                "it may invoke asynchronously, 1 by default.");
        opts.addOption(OptionBuilder.create("async"));

        OptionBuilder.withArgName("maxops");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "maximum number of outstanding storage operations performed "
                        + "asynchronously, unlimited by default.");
        opts.addOption(OptionBuilder.create("storeasync"));

        opts.addOption("noextneg", false, "disable extended negotiation.");
        opts.addOption("rel", false,
                "negotiate support of relational queries and retrieval.");
        opts.addOption("datetime", false,
                "negotiate support of combined date and time attribute range matching.");
        opts.addOption("fuzzy", false,
                "negotiate support of fuzzy semantic person name attribute matching.");

        opts.addOption("retall", false, "negotiate private FIND SOP Classes " +
                "to fetch all available attributes of matching entities.");
        opts.addOption("blocked", false, "negotiate private FIND SOP Classes " +
                "to return attributes of several matching entities per FIND " +
                "response.");
        opts.addOption("vmf", false, "negotiate private FIND SOP Classes to " +
                "return attributes of legacy CT/MR images of one series as " +
                "virtual multiframe object.");
        opts.addOption("pdv1", false,
                "send only one PDV in one P-Data-TF PDU, pack command and data "
                + "PDV in one P-DATA-TF PDU by default.");
        opts.addOption("tcpdelay", false,
                "set TCP_NODELAY socket option to false, true by default");

        OptionBuilder.withArgName("ms");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "timeout in ms for TCP connect, no timeout by default");
        opts.addOption(OptionBuilder.create("connectTO"));

        OptionBuilder.withArgName("ms");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "delay in ms for Socket close after sending A-ABORT, 50ms by default");
        opts.addOption(OptionBuilder.create("soclosedelay"));

        OptionBuilder.withArgName("ms");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "period in ms to check for outstanding DIMSE-RSP, 10s by default");
        opts.addOption(OptionBuilder.create("reaper"));

        OptionBuilder.withArgName("ms");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "timeout in ms for receiving C-FIND-RSP, 60s by default");
        opts.addOption(OptionBuilder.create("cfindrspTO"));

        OptionBuilder.withArgName("ms");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "timeout in ms for receiving C-MOVE-RSP and C-GET RSP, 600s by default");
        opts.addOption(OptionBuilder.create("cmoverspTO"));

        OptionBuilder.withArgName("ms");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "timeout in ms for receiving C-GET-RSP and C-MOVE RSP, 600s by default");
        opts.addOption(OptionBuilder.create("cgetrspTO"));

        OptionBuilder.withArgName("ms");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "timeout in ms for receiving A-ASSOCIATE-AC, 5s by default");
        opts.addOption(OptionBuilder.create("acceptTO"));

        OptionBuilder.withArgName("ms");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "timeout in ms for receiving A-RELEASE-RP, 5s by default");
        opts.addOption(OptionBuilder.create("releaseTO"));

        OptionBuilder.withArgName("KB");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "maximal length in KB of received P-DATA-TF PDUs, 16KB by default");
        opts.addOption(OptionBuilder.create("rcvpdulen"));

        OptionBuilder.withArgName("KB");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "maximal length in KB of sent P-DATA-TF PDUs, 16KB by default");
        opts.addOption(OptionBuilder.create("sndpdulen"));

        OptionBuilder.withArgName("KB");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "set SO_RCVBUF socket option to specified value in KB");
        opts.addOption(OptionBuilder.create("sorcvbuf"));

        OptionBuilder.withArgName("KB");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "set SO_SNDBUF socket option to specified value in KB");
        opts.addOption(OptionBuilder.create("sosndbuf"));

        OptionBuilder.withArgName("KB");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "minimal buffer size to write received object to file, 1KB by default");
        opts.addOption(OptionBuilder.create("filebuf"));

        OptionGroup qrlevel = new OptionGroup();

        OptionBuilder.withDescription("perform patient level query, multiple "
                + "exclusive with -S and -I, perform study level query "
                + "by default.");
        OptionBuilder.withLongOpt("patient");
        opts.addOption(OptionBuilder.create("P"));

        OptionBuilder.withDescription("perform series level query, multiple "
                + "exclusive with -P and -I, perform study level query "
                + "by default.");
        OptionBuilder.withLongOpt("series");
        opts.addOption(OptionBuilder.create("S"));

        OptionBuilder.withDescription("perform instance level query, multiple "
                + "exclusive with -P and -S, perform study level query "
                + "by default.");
        OptionBuilder.withLongOpt("image");
        opts.addOption(OptionBuilder.create("I"));

        OptionBuilder.withArgName("cuid");
        OptionBuilder.hasArgs();
        OptionBuilder.withDescription("negotiate addition private C-FIND SOP "
                + "class with specified UID");
        opts.addOption(OptionBuilder.create("cfind"));

        opts.addOptionGroup(qrlevel);

        OptionBuilder.withArgName("[seq/]attr=value");
        OptionBuilder.hasArgs();
        OptionBuilder.withValueSeparator('=');
        OptionBuilder.withDescription("specify matching key. attr can be " +
                "specified by name or tag value (in hex), e.g. PatientName " +
                "or 00100010. Attributes in nested Datasets can " +
                "be specified by including the name/tag value of " +
                "the sequence attribute, e.g. 00400275/00400009 " +
                "for Scheduled Procedure Step ID in the Request " +
                "Attributes Sequence");
        opts.addOption(OptionBuilder.create("q"));

        OptionBuilder.withArgName("attr");
        OptionBuilder.hasArgs();
        OptionBuilder.withDescription("specify additional return key. attr can " +
                "be specified by name or tag value (in hex).");
        opts.addOption(OptionBuilder.create("r"));

        OptionBuilder.withArgName("num");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("cancel query after receive of specified " +
                "number of responses, no cancel by default");
        opts.addOption(OptionBuilder.create("C"));

        OptionBuilder.withArgName("aet");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("retrieve matching objects to specified " +
                "move destination.");
        opts.addOption(OptionBuilder.create("cmove"));

        opts.addOption("evalRetrieveAET", false,
                "Only Move studies not allready stored on destination AET");
        opts.addOption("lowprior", false,
                "LOW priority of the C-FIND/C-MOVE operation, MEDIUM by default");
        opts.addOption("highprior", false,
                "HIGH priority of the C-FIND/C-MOVE operation, MEDIUM by default");

        OptionBuilder.withArgName("num");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("repeat query (and retrieve) several times");
        opts.addOption(OptionBuilder.create("repeat"));

        OptionBuilder.withArgName("ms");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(
                "delay in ms between repeated query (and retrieve), no delay by default");
        opts.addOption(OptionBuilder.create("repeatdelay"));

        opts.addOption("reuseassoc", false,
                "Reuse association for repeated query (and retrieve)");
        opts.addOption("closeassoc", false,
                "Close association between repeated query (and retrieve)");

        opts.addOption("h", "help", false, "print this message");
        opts.addOption("V", "version", false,
                "print the version information and exit");
        CommandLine cl = null;
        try {
            cl = new GnuParser().parse(opts, args);
        } catch (ParseException e) {
            System.err.println("dcmqr: " + e.getMessage());
            throw new RuntimeException("unreachable");
        }
        if (cl.hasOption('V')) {

            //Just to get the DCM4CHEE version.

            Package p = DicomQuery.class.getPackage();
            System.out.println("dcmqr v" + p.getImplementationVersion());
            //Syst//em.exit(0);
            throw new IllegalArgumentException("dcmqr v" + p.getImplementationVersion());
        }
        if (cl.hasOption('h') || cl.getArgList().size() != 1) {

            //Just to print out help.

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(USAGE, DESCRIPTION, opts, EXAMPLE);
            //Syst//em.exit(0); //ToDo: delete
            throw new IllegalArgumentException("Help option not valid.");
        }

        return cl;
    }

    @SuppressWarnings("unchecked")
    public static void mainOrig(String[] args) {
        CommandLine cl = parse(args);
        DicomQuery dcmqr = new DicomQuery(cl.hasOption("device")
                ? cl.getOptionValue("device") : "DCMQR");
        final List<String> argList = cl.getArgList();
        String remoteAE = argList.get(0);
        String[] calledAETAddress = split(remoteAE, '@');
        dcmqr.setCalledAET(calledAETAddress[0], cl.hasOption("reuseassoc"));
        if (calledAETAddress[1] == null) {
            dcmqr.setRemoteHost("127.0.0.1");
            dcmqr.setRemotePort(104);
        } else {
            String[] hostPort = split(calledAETAddress[1], ':');
            dcmqr.setRemoteHost(hostPort[0]);
            dcmqr.setRemotePort(toPort(hostPort[1]));
        }
        if (cl.hasOption("L")) {
            String localAE = cl.getOptionValue("L");
            String[] localPort = split(localAE, ':');
            if (localPort[1] != null) {
                dcmqr.setLocalPort(toPort(localPort[1]));
            }
            String[] callingAETHost = split(localPort[0], '@');
            dcmqr.setCalling(callingAETHost[0]);
            if (callingAETHost[1] != null) {
                dcmqr.setLocalHost(callingAETHost[1]);
            }
        }
        if (cl.hasOption("username")) {
            String username = cl.getOptionValue("username");
            UserIdentity userId;
            if (cl.hasOption("passcode")) {
                String passcode = cl.getOptionValue("passcode");
                userId = new UserIdentity.UsernamePasscode(username,
                        passcode.toCharArray());
            } else {
                userId = new UserIdentity.Username(username);
            }
            userId.setPositiveResponseRequested(cl.hasOption("uidnegrsp"));
            dcmqr.setUserIdentity(userId);
        }
        if (cl.hasOption("connectTO"))
            dcmqr.setConnectTimeout(parseInt(cl.getOptionValue("connectTO"),
                    "illegal argument of option -connectTO", 1,
                    Integer.MAX_VALUE));
        if (cl.hasOption("reaper"))
            dcmqr.setAssociationReaperPeriod(parseInt(cl.getOptionValue("reaper"),
                            "illegal argument of option -reaper", 1,
                            Integer.MAX_VALUE));
        if (cl.hasOption("cfindrspTO"))
            dcmqr.setDimseRspTimeout(parseInt(cl.getOptionValue("cfindrspTO"),
                    "illegal argument of option -cfindrspTO", 1, Integer.MAX_VALUE));
        if (cl.hasOption("cmoverspTO"))
            dcmqr.setRetrieveRspTimeout(parseInt(cl.getOptionValue("cmoverspTO"),
                    "illegal argument of option -cmoverspTO", 1, Integer.MAX_VALUE));
        if (cl.hasOption("cgetrspTO"))
            dcmqr.setRetrieveRspTimeout(parseInt(cl.getOptionValue("cgetrspTO"),
                    "illegal argument of option -cgetrspTO", 1, Integer.MAX_VALUE));
        if (cl.hasOption("acceptTO"))
            dcmqr.setAcceptTimeout(parseInt(cl.getOptionValue("acceptTO"),
                    "illegal argument of option -acceptTO", 1,
                    Integer.MAX_VALUE));
        if (cl.hasOption("releaseTO"))
            dcmqr.setReleaseTimeout(parseInt(cl.getOptionValue("releaseTO"),
                    "illegal argument of option -releaseTO", 1,
                    Integer.MAX_VALUE));
        if (cl.hasOption("soclosedelay"))
            dcmqr.setSocketCloseDelay(parseInt(cl
                    .getOptionValue("soclosedelay"),
                    "illegal argument of option -soclosedelay", 1, 10000));
        if (cl.hasOption("rcvpdulen"))
            dcmqr.setMaxPDULengthReceive(parseInt(cl
                    .getOptionValue("rcvpdulen"),
                    "illegal argument of option -rcvpdulen", 1, 10000)
                    * KB);
        if (cl.hasOption("sndpdulen"))
            dcmqr.setMaxPDULengthSend(parseInt(cl.getOptionValue("sndpdulen"),
                    "illegal argument of option -sndpdulen", 1, 10000)
                    * KB);
        if (cl.hasOption("sosndbuf"))
            dcmqr.setSendBufferSize(parseInt(cl.getOptionValue("sosndbuf"),
                    "illegal argument of option -sosndbuf", 1, 10000)
                    * KB);
        if (cl.hasOption("sorcvbuf"))
            dcmqr.setReceiveBufferSize(parseInt(cl.getOptionValue("sorcvbuf"),
                    "illegal argument of option -sorcvbuf", 1, 10000)
                    * KB);
        if (cl.hasOption("filebuf"))
            dcmqr.setFileBufferSize(parseInt(cl.getOptionValue("filebuf"),
                    "illegal argument of option -filebuf", 1, 10000)
                    * KB);
        dcmqr.setPackPDV(!cl.hasOption("pdv1"));
        dcmqr.setTcpNoDelay(!cl.hasOption("tcpdelay"));
        dcmqr.setMaxOpsInvoked(cl.hasOption("async") ? parseInt(cl
                .getOptionValue("async"), "illegal argument of option -async",
                0, 0xffff) : 1);
        dcmqr.setMaxOpsPerformed(cl.hasOption("cstoreasync") ? parseInt(cl
                .getOptionValue("cstoreasync"), "illegal argument of option -cstoreasync",
                0, 0xffff) : 0);
        if (cl.hasOption("C"))
            dcmqr.setCancelAfter(parseInt(cl.getOptionValue("C"),
                    "illegal argument of option -C", 1, Integer.MAX_VALUE));
        if (cl.hasOption("lowprior"))
            dcmqr.setPriority(CommandUtils.LOW);
        if (cl.hasOption("highprior"))
            dcmqr.setPriority(CommandUtils.HIGH);
        if (cl.hasOption("cstore")) {
            String[] storeTCs = cl.getOptionValues("cstore");
            for (String storeTC : storeTCs) {
                String cuid;
                String[] tsuids;
                int colon = storeTC.indexOf(':');
                if (colon == -1) {
                    cuid = storeTC;
                    tsuids = DEF_TS;
                } else {
                    cuid = storeTC.substring(0, colon);
                    String ts = storeTC.substring(colon+1);
                    try {
                        tsuids = TS.valueOf(ts).uids;
                    } catch (IllegalArgumentException e) {
                        tsuids = ts.split(",");
                    }
                }
                try {
                    cuid = CUID.valueOf(cuid).uid;
                } catch (IllegalArgumentException e) {
                    // assume cuid already contains UID
                }
                dcmqr.addStoreTransferCapability(cuid, tsuids);
            }
            if (cl.hasOption("cstoredest"))
                dcmqr.setStoreDestination(cl.getOptionValue("cstoredest"));
        }
        dcmqr.setCFind(!cl.hasOption("nocfind"));
        dcmqr.setCGet(cl.hasOption("cget"));
        if (cl.hasOption("cmove"))
            dcmqr.setMoveDest(cl.getOptionValue("cmove"));
        if (cl.hasOption("evalRetrieveAET"))
            dcmqr.setEvalRetrieveAET(true);
        if (cl.hasOption("P"))
            dcmqr.setQueryLevel(QueryRetrieveLevel.PATIENT);
        else if (cl.hasOption("S"))
            dcmqr.setQueryLevel(QueryRetrieveLevel.SERIES);
        else if (cl.hasOption("I"))
            dcmqr.setQueryLevel(QueryRetrieveLevel.IMAGE);
        else
            dcmqr.setQueryLevel(QueryRetrieveLevel.STUDY);
        if (cl.hasOption("noextneg"))
            dcmqr.setNoExtNegotiation(true);
        if (cl.hasOption("rel"))
            dcmqr.setRelationQR(true);
        if (cl.hasOption("datetime"))
            dcmqr.setDateTimeMatching(true);
        if (cl.hasOption("fuzzy"))
            dcmqr.setFuzzySemanticPersonNameMatching(true);
        if (!cl.hasOption("P")) {
            if (cl.hasOption("retall"))
                dcmqr.addPrivate(
                        UID.PrivateStudyRootQueryRetrieveInformationModelFIND);
            if (cl.hasOption("blocked"))
                dcmqr.addPrivate(
                        UID.PrivateBlockedStudyRootQueryRetrieveInformationModelFIND);
            if (cl.hasOption("vmf"))
                dcmqr.addPrivate(
                        UID.PrivateVirtualMultiframeStudyRootQueryRetrieveInformationModelFIND);
        }
        if (cl.hasOption("cfind")) {
            String[] cuids = cl.getOptionValues("cfind");
            for (int i = 0; i < cuids.length; i++)
                dcmqr.addPrivate(cuids[i]);
        }
        if (cl.hasOption("q")) {
            String[] matchingKeys = cl.getOptionValues("q");
            for (int i = 1; i < matchingKeys.length; i++, i++)
                dcmqr.addMatchingKey(Tag.toTagPath(matchingKeys[i - 1]), matchingKeys[i]);
        }
        if (cl.hasOption("r")) {
            String[] returnKeys = cl.getOptionValues("r");
            for (int i = 0; i < returnKeys.length; i++)
                dcmqr.addReturnKey(Tag.toTagPath(returnKeys[i]));
        }

        dcmqr.configureTransferCapability(cl.hasOption("ivrle"));


        int repeat = cl.hasOption("repeat") ? parseInt(cl
                .getOptionValue("repeat"),
                "illegal argument of option -repeat", 1, Integer.MAX_VALUE) : 0;
        int interval = cl.hasOption("repeatdelay") ? parseInt(cl
                .getOptionValue("repeatdelay"),
                "illegal argument of option -repeatdelay", 1, Integer.MAX_VALUE)
                : 0;
        boolean closeAssoc = cl.hasOption("closeassoc");

        if (cl.hasOption("tls")) {
            String cipher = cl.getOptionValue("tls");
            if ("NULL".equalsIgnoreCase(cipher)) {
                dcmqr.setTlsWithoutEncyrption();
            } else if ("3DES".equalsIgnoreCase(cipher)) {
                dcmqr.setTls3DES_EDE_CBC();
            } else if ("AES".equalsIgnoreCase(cipher)) {
                dcmqr.setTlsAES_128_CBC();
            } else {
                System.err.println("Invalid parameter for option -tls: " + cipher);
                return;
            }
            if (cl.hasOption("tls1")) {
                dcmqr.setTlsProtocol(TLS1);
            } else if (cl.hasOption("ssl3")) {
                dcmqr.setTlsProtocol(SSL3);
            } else if (cl.hasOption("no_tls1")) {
                dcmqr.setTlsProtocol(NO_TLS1);
            } else if (cl.hasOption("no_ssl3")) {
                dcmqr.setTlsProtocol(NO_SSL3);
            } else if (cl.hasOption("no_ssl2")) {
                dcmqr.setTlsProtocol(NO_SSL2);
            }
            dcmqr.setTlsNeedClientAuth(!cl.hasOption("noclientauth"));
            if (cl.hasOption("keystore")) {
                dcmqr.setKeyStoreURL(cl.getOptionValue("keystore"));
            }
            if (cl.hasOption("keystorepw")) {
                dcmqr.setKeyStorePassword(
                        cl.getOptionValue("keystorepw"));
            }
            if (cl.hasOption("keypw")) {
                dcmqr.setKeyPassword(cl.getOptionValue("keypw"));
            }
            if (cl.hasOption("truststore")) {
                dcmqr.setTrustStoreURL(
                        cl.getOptionValue("truststore"));
            }
            if (cl.hasOption("truststorepw")) {
                dcmqr.setTrustStorePassword(
                        cl.getOptionValue("truststorepw"));
            }
            long t1 = System.currentTimeMillis();
            try {
                dcmqr.initTLS();
            } catch (Exception e) {
                System.err.println("ERROR: Failed to initialize TLS context:"
                        + e.getMessage());
                return;
            }
            long t2 = System.currentTimeMillis();
            LOG.info("Initialize TLS context in {} s", Float.valueOf((t2 - t1) / 1000f));
        }
        try {
            dcmqr.start();
        } catch (Exception e) {
            System.err.println("ERROR: Failed to start server for receiving " +
                    "requested objects:" + e.getMessage());
            return;
        }
        try {
            long t1 = System.currentTimeMillis();
            try {
                dcmqr.open();
            } catch (Exception e) {
                LOG.error("Failed to establish association:", e);
                return;
            }
            long t2 = System.currentTimeMillis();
            LOG.info("Connected to {} in {} s", remoteAE, Float.valueOf((t2 - t1) / 1000f));

            for (;;) {
                List<DicomObject> result;
                if (dcmqr.isCFind()) {
                    result = dcmqr.query();
                    long t3 = System.currentTimeMillis();
                    LOG.info("Received {} matching entries in {} s", Integer.valueOf(result.size()),
                            Float.valueOf((t3 - t2) / 1000f));
                    t2 = t3;
                } else {
                    result = Collections.singletonList(dcmqr.getKeys());
                }
                if (dcmqr.isCMove() || dcmqr.isCGet()) {
                    if (dcmqr.isCMove())
                        dcmqr.move(result);
                    else
                        dcmqr.get(result);
                    long t4 = System.currentTimeMillis();
                    LOG.info("Retrieved {} objects (warning: {}, failed: {}) in {}s",
                            new Object[] {
                            Integer.valueOf(dcmqr
                                    .getTotalRetrieved()),
                                    Integer.valueOf(dcmqr.getWarning()),
                                    Integer.valueOf(dcmqr.getFailed()),
                                    Float.valueOf((t4 - t2) / 1000f) });
                }
                if (repeat == 0 || closeAssoc) {
                    try {
                        dcmqr.close();
                    } catch (InterruptedException e) {
                        LOG.error(e.getMessage(), e);
                    }
                    LOG.info("Released connection to {}",remoteAE);
                }
                if (repeat-- == 0)
                    break;
                Thread.sleep(interval);
                long t4 = System.currentTimeMillis();
                dcmqr.open();
                t2 = System.currentTimeMillis();
                LOG.info("Reconnect or reuse connection to {} in {} s",
                        remoteAE, Float.valueOf((t2 - t4) / 1000f));
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        } catch (ConfigurationException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            dcmqr.stop();
        }
    }

    public void addStoreTransferCapability(String cuid, String[] tsuids) {
        storeTransferCapability.add(new TransferCapability(
                cuid, tsuids, TransferCapability.SCP));
    }

    public void setEvalRetrieveAET(boolean evalRetrieveAET) {
       this.evalRetrieveAET = evalRetrieveAET;
    }

    public boolean isEvalRetrieveAET() {
        return evalRetrieveAET;
    }

    public void setNoExtNegotiation(boolean b) {
        this.noExtNegotiation = b;
    }

    public void setFuzzySemanticPersonNameMatching(boolean b) {
        this.fuzzySemanticPersonNameMatching = b;
    }

    public void setDateTimeMatching(boolean b) {
        this.dateTimeMatching = b;
    }

    public void setRelationQR(boolean b) {
        this.relationQR = b;
    }

    public final int getFailed() {
        return failed;
    }

    public final int getWarning() {
        return warning;
    }

    public final int getTotalRetrieved() {
        return completed + warning;
    }

    public void setCancelAfter(int limit) {
        this.cancelAfter = limit;
    }

    public void addMatchingKey(int[] tagPath, String value) {
        keys.putString(tagPath, null, value);
    }

    public void addReturnKey(int[] tagPath) {
        keys.putNull(tagPath, null);
    }

    public void configureTransferCapability(boolean ivrle) {
        String[] findcuids = qrlevel.getFindClassUids();
        String[] movecuids = moveDest != null ? qrlevel.getMoveClassUids()
                : EMPTY_STRING;
        String[] getcuids = cget ? qrlevel.getGetClassUids()
                : EMPTY_STRING;
        TransferCapability[] tcs = new TransferCapability[findcuids.length
                + privateFind.size() + movecuids.length + getcuids.length
                + storeTransferCapability.size()];
        int i = 0;
        for (String cuid : findcuids)
            tcs[i++] = mkFindTC(cuid, ivrle ? IVRLE_TS : NATIVE_LE_TS);
        for (String cuid : privateFind)
            tcs[i++] = mkFindTC(cuid, ivrle ? IVRLE_TS : DEFLATED_TS);
        for (String cuid : movecuids)
            tcs[i++] = mkRetrieveTC(cuid, ivrle ? IVRLE_TS : NATIVE_LE_TS);
        for (String cuid : getcuids)
            tcs[i++] = mkRetrieveTC(cuid, ivrle ? IVRLE_TS : NATIVE_LE_TS);
        for (TransferCapability tc : storeTransferCapability) {
            tcs[i++] = tc;
        }
        ae.setTransferCapability(tcs);
        if (!storeTransferCapability.isEmpty()) {
            ae.register(createStorageService());
        }
    }

    private DicomService createStorageService() {
        String[] cuids = new String[storeTransferCapability.size()];
        int i = 0;
        for (TransferCapability tc : storeTransferCapability) {
            cuids[i++] = tc.getSopClass();
        }
        return new StorageService(cuids) {
            @Override
            protected void onCStoreRQ(Association as, int pcid, DicomObject rq,
                    PDVInputStream dataStream, String tsuid, DicomObject rsp)
                    throws IOException, DicomServiceException {
                if (storeDest == null) {
                    super.onCStoreRQ(as, pcid, rq, dataStream, tsuid, rsp);
                } else {
                    try {
                        String cuid = rq.getString(Tag.AffectedSOPClassUID);
                        String iuid = rq.getString(Tag.AffectedSOPInstanceUID);
                        BasicDicomObject fmi = new BasicDicomObject();
                        fmi.initFileMetaInformation(cuid, iuid, tsuid);
                        File file = devnull ? storeDest : new File(storeDest, iuid);
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(fos,
                                fileBufferSize);
                        DicomOutputStream dos = new DicomOutputStream(bos);
                        dos.writeFileMetaInformation(fmi);
                        dataStream.copyTo(dos);
                        dos.close();
                    } catch (IOException e) {
                        throw new DicomServiceException(rq, Status.ProcessingFailure, e
                                .getMessage());
                    }
                }
            }

        };
    }

    private TransferCapability mkRetrieveTC(String cuid, String[] ts) {
        ExtRetrieveTransferCapability tc = new ExtRetrieveTransferCapability(
                cuid, ts, TransferCapability.SCU);
        tc.setExtInfoBoolean(
                ExtRetrieveTransferCapability.RELATIONAL_RETRIEVAL, relationQR);
        if (noExtNegotiation)
            tc.setExtInfo(null);
        return tc;
    }

    private TransferCapability mkFindTC(String cuid, String[] ts) {
        ExtQueryTransferCapability tc = new ExtQueryTransferCapability(cuid,
                ts, TransferCapability.SCU);
        tc.setExtInfoBoolean(ExtQueryTransferCapability.RELATIONAL_QUERIES,
                relationQR);
        tc.setExtInfoBoolean(ExtQueryTransferCapability.DATE_TIME_MATCHING,
                dateTimeMatching);
        tc.setExtInfoBoolean(ExtQueryTransferCapability.FUZZY_SEMANTIC_PN_MATCHING,
                fuzzySemanticPersonNameMatching);
        if (noExtNegotiation)
            tc.setExtInfo(null);
        return tc;
    }

    public void setQueryLevel(QueryRetrieveLevel qrlevel) {
        this.qrlevel = qrlevel;
        keys.putString(Tag.QueryRetrieveLevel, VR.CS, qrlevel.getCode());
        for (int tag : qrlevel.getReturnKeys()) {
            keys.putNull(tag, null);
        }
    }

    public final void addPrivate(String cuid) {
        privateFind.add(cuid);
    }

    public void setCFind(boolean cfind) {
        this.cfind = cfind;
    }

    public boolean isCFind() {
        return cfind;
    }

    public void setCGet(boolean cget) {
        this.cget = cget;
    }

    public boolean isCGet() {
        return cget;
    }

    public void setMoveDest(String aet) {
        moveDest = aet;
    }

    public boolean isCMove() {
        return moveDest != null;
    }

    private static int toPort(String port) {
        return port != null ? parseInt(port, "illegal port number", 1, 0xffff)
                : 104;
    }

    private static int parseInt(String s, String errPrompt, int min, int max) {
        try {
            int i = Integer.parseInt(s);
            if (i >= min && i <= max)
                return i;
        } catch (NumberFormatException e) {
            // parameter is not a valid integer; fall through to exit
        }
        throw new RuntimeException();
    }

    private static String[] split(String s, char delim) {
        String[] s2 = { s, null };
        int pos = s.indexOf(delim);
        if (pos != -1) {
            s2[0] = s.substring(0, pos);
            s2[1] = s.substring(pos + 1);
        }
        return s2;
    }

    public void start() throws IOException {
        if (conn.isListening()) {
            conn.bind(executor );
            System.out.println("Start Server listening on port " + conn.getPort());
        }
    }

    public void stop() {
        if (conn.isListening()) {
            conn.unbind();
        }
    }

    public void open() throws IOException, ConfigurationException,
            InterruptedException {
        assoc = ae.connect(remoteAE, executor);
    }

    public DicomObject getKeys() {
        return keys;
    }

    /**
     * NOTE: This is where the call occurs. Collect the information here!!!
     *
     * ToDo: modify this section to get results in a format useable by GWT!!
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public List<DicomObject> query()
            throws IOException, InterruptedException
    {
        List<DicomObject> result = new ArrayList<DicomObject>();
        TransferCapability tc = selectFindTransferCapability();
        String cuid = tc.getSopClass();
        String tsuid = selectTransferSyntax(tc);
        if (tc.getExtInfoBoolean(ExtQueryTransferCapability.RELATIONAL_QUERIES)
                || containsUpperLevelUIDs(cuid)) {
            LOG.info("Send Query Request using {}:\n{}",
                    UIDDictionary.getDictionary().prompt(cuid), keys);
            DimseRSP rsp = assoc.cfind(cuid, priority, keys, tsuid, cancelAfter);
            while (rsp.next()) {
                DicomObject cmd = rsp.getCommand();
                if (CommandUtils.isPending(cmd)) {
                    DicomObject data = rsp.getDataset();
                    result.add(data);
                    LOG.info("Query Response #{}:\n{}", Integer.valueOf(result.size()), data);
                }
            }
        } else {
            List<DicomObject> upperLevelUIDs = queryUpperLevelUIDs(cuid, tsuid);
            List<DimseRSP> rspList = new ArrayList<DimseRSP>(upperLevelUIDs.size());
            for (int i = 0, n = upperLevelUIDs.size(); i < n; i++) {
                upperLevelUIDs.get(i).copyTo(keys);
                LOG.info("Send Query Request #{}/{} using {}:\n{}",
                        new Object[] {
                            Integer.valueOf(i+1),
                            Integer.valueOf(n),
                            UIDDictionary.getDictionary().prompt(cuid),
                            keys
                        });
                rspList.add(assoc.cfind(cuid, priority, keys, tsuid, cancelAfter));
            }
            for (int i = 0, n = rspList.size(); i < n; i++) {
                DimseRSP rsp = rspList.get(i);
                for (int j = 0; rsp.next(); ++j) {
                    DicomObject cmd = rsp.getCommand();
                    if (CommandUtils.isPending(cmd)) {
                        DicomObject data = rsp.getDataset();
                        result.add(data);
                        LOG.info("Query Response #{} for Query Request #{}/{}:\n{}",
                                new Object[]{ Integer.valueOf(j+1), Integer.valueOf(i+1), Integer.valueOf(n), data });
                    }
                }
            }
        }
        return result;
    }

    @SuppressWarnings("fallthrough")
    private boolean containsUpperLevelUIDs(String cuid) {
        switch (qrlevel) {
        case IMAGE:
            if (!keys.containsValue(Tag.SeriesInstanceUID)) {
                return false;
            }
            // fall through
        case SERIES:
            if (!keys.containsValue(Tag.StudyInstanceUID)) {
                return false;
            }
            // fall through
        case STUDY:
            if (Arrays.asList(PATIENT_LEVEL_FIND_CUID).contains(cuid)
                    && !keys.containsValue(Tag.PatientID)) {
                return false;
            }
            // fall through
        case PATIENT:
            // fall through
        }
        return true;
    }

    private List<DicomObject> queryUpperLevelUIDs(String cuid, String tsuid)
            throws IOException, InterruptedException {
        List<DicomObject> keylist = new ArrayList<DicomObject>();
        if (Arrays.asList(PATIENT_LEVEL_FIND_CUID).contains(cuid)) {
            queryPatientIDs(cuid, tsuid, keylist);
            if (qrlevel == QueryRetrieveLevel.STUDY) {
                return keylist;
            }
            keylist = queryStudyOrSeriesIUIDs(cuid, tsuid, keylist,
                    Tag.StudyInstanceUID, STUDY_MATCHING_KEYS, QueryRetrieveLevel.STUDY);
        } else {
            keylist.add(new BasicDicomObject());
            keylist = queryStudyOrSeriesIUIDs(cuid, tsuid, keylist,
                    Tag.StudyInstanceUID, PATIENT_STUDY_MATCHING_KEYS, QueryRetrieveLevel.STUDY);
        }
        if (qrlevel == QueryRetrieveLevel.IMAGE) {
            keylist = queryStudyOrSeriesIUIDs(cuid, tsuid, keylist,
                    Tag.SeriesInstanceUID, SERIES_MATCHING_KEYS, QueryRetrieveLevel.SERIES);
        }
        return keylist;
    }

    private void queryPatientIDs(String cuid, String tsuid,
            List<DicomObject> keylist) throws IOException, InterruptedException {
        String patID = keys.getString(Tag.PatientID);
        String issuer = keys.getString(Tag.IssuerOfPatientID);
        if (patID != null) {
            DicomObject patIdKeys = new BasicDicomObject();
            patIdKeys.putString(Tag.PatientID, VR.LO, patID);
            if (issuer != null) {
                patIdKeys.putString(Tag.IssuerOfPatientID, VR.LO, issuer);
            }
            keylist.add(patIdKeys);
        } else {
            DicomObject patLevelQuery = new BasicDicomObject();
            keys.subSet(PATIENT_MATCHING_KEYS).copyTo(patLevelQuery);
            patLevelQuery.putNull(Tag.PatientID, VR.LO);
            patLevelQuery.putNull(Tag.IssuerOfPatientID, VR.LO);
            patLevelQuery.putString(Tag.QueryRetrieveLevel, VR.CS, "PATIENT");
            LOG.info("Send Query Request using {}:\n{}",
                    UIDDictionary.getDictionary().prompt(cuid), patLevelQuery);
            DimseRSP rsp = assoc.cfind(cuid, priority, patLevelQuery, tsuid,
                    Integer.MAX_VALUE);
            for (int i = 0; rsp.next(); ++i) {
                DicomObject cmd = rsp.getCommand();
                if (CommandUtils.isPending(cmd)) {
                    DicomObject data = rsp.getDataset();
                    LOG.info("Query Response #{}:\n{}", Integer.valueOf(i+1), data);
                    DicomObject patIdKeys = new BasicDicomObject();
                    patIdKeys.putString(Tag.PatientID, VR.LO,
                            data.getString(Tag.PatientID));
                    issuer = keys.getString(Tag.IssuerOfPatientID);
                    if (issuer != null) {
                        patIdKeys.putString(Tag.IssuerOfPatientID, VR.LO,
                                issuer);
                    }
                    keylist.add(patIdKeys);
                }
            }
        }
    }

    private List<DicomObject> queryStudyOrSeriesIUIDs(String cuid, String tsuid,
            List<DicomObject> upperLevelIDs, int uidTag, int[] matchingKeys,
            QueryRetrieveLevel qrLevel) throws IOException,
            InterruptedException {
        List<DicomObject> keylist = new ArrayList<DicomObject>();
        String uid = keys.getString(uidTag);
        for (DicomObject upperLevelID : upperLevelIDs) {
            if (uid != null) {
                DicomObject suidKey = new BasicDicomObject();
                upperLevelID.copyTo(suidKey);
                suidKey.putString(uidTag, VR.UI, uid);
                keylist.add(suidKey);
            } else {
                DicomObject keys2 = new BasicDicomObject();
                keys.subSet(matchingKeys).copyTo(keys2);
                upperLevelID.copyTo(keys2);
                keys2.putNull(uidTag, VR.UI);
                keys2.putString(Tag.QueryRetrieveLevel, VR.CS, qrLevel.getCode());
                LOG.info("Send Query Request using {}:\n{}",
                        UIDDictionary.getDictionary().prompt(cuid), keys2);
                DimseRSP rsp = assoc.cfind(cuid, priority, keys2,
                        tsuid, Integer.MAX_VALUE);
                for (int i = 0; rsp.next(); ++i) {
                    DicomObject cmd = rsp.getCommand();
                    if (CommandUtils.isPending(cmd)) {
                        DicomObject data = rsp.getDataset();
                        LOG.info("Query Response #{}:\n{}", Integer.valueOf(i+1), data);
                        DicomObject suidKey = new BasicDicomObject();
                        upperLevelID.copyTo(suidKey);
                        suidKey.putString(uidTag, VR.UI, data.getString(uidTag));
                        keylist.add(suidKey);
                    }
                }
            }
        }
        return keylist;
    }

    public TransferCapability selectFindTransferCapability()
            throws NoPresentationContextException {
        TransferCapability tc;
        if ((tc = selectTransferCapability(privateFind)) != null)
            return tc;
        if ((tc = selectTransferCapability(qrlevel.getFindClassUids())) != null)
            return tc;
        throw new NoPresentationContextException(UIDDictionary.getDictionary()
                .prompt(qrlevel.getFindClassUids()[0])
                + " not supported by " + remoteAE.getAETitle());
    }

    public String selectTransferSyntax(TransferCapability tc) {
        String[] tcuids = tc.getTransferSyntax();
        if (Arrays.asList(tcuids).indexOf(UID.DeflatedExplicitVRLittleEndian) != -1)
            return UID.DeflatedExplicitVRLittleEndian;
        return tcuids[0];
    }

    public void move(List<DicomObject> findResults)
            throws IOException, InterruptedException {
        if (moveDest == null)
            throw new IllegalStateException("moveDest == null");
        TransferCapability tc = selectTransferCapability(qrlevel.getMoveClassUids());
        if (tc == null)
            throw new NoPresentationContextException(UIDDictionary
                    .getDictionary().prompt(qrlevel.getMoveClassUids()[0])
                    + " not supported by " + remoteAE.getAETitle());
        String cuid = tc.getSopClass();
        String tsuid = selectTransferSyntax(tc);
        for (int i = 0, n = Math.min(findResults.size(), cancelAfter); i < n; ++i) {
            DicomObject keys = findResults.get(i).subSet(MOVE_KEYS);
            if (isEvalRetrieveAET() && containsMoveDest(
                    findResults.get(i).getStrings(Tag.RetrieveAETitle))) {
                LOG.info("Skipping {}:\n{}",
                        UIDDictionary.getDictionary().prompt(cuid), keys);
            } else {
                LOG.info("Send Retrieve Request using {}:\n{}",
                        UIDDictionary.getDictionary().prompt(cuid), keys);
                assoc.cmove(cuid, priority, keys, tsuid, moveDest, rspHandler);
            }
        }
        assoc.waitForDimseRSP();
    }


    private boolean containsMoveDest(String[] retrieveAETs) {
        if (retrieveAETs != null) {
            for (String aet : retrieveAETs) {
                if (moveDest.equals(aet)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void get(List<DicomObject> findResults)
            throws IOException, InterruptedException {
        TransferCapability tc = selectTransferCapability(qrlevel.getGetClassUids());
        if (tc == null)
            throw new NoPresentationContextException(UIDDictionary
                    .getDictionary().prompt(qrlevel.getGetClassUids()[0])
                    + " not supported by " + remoteAE.getAETitle());
        String cuid = tc.getSopClass();
        String tsuid = selectTransferSyntax(tc);
        for (int i = 0, n = Math.min(findResults.size(), cancelAfter); i < n; ++i) {
            DicomObject keys = findResults.get(i).subSet(MOVE_KEYS);
            LOG.info("Send Retrieve Request using {}:\n{}",
                    UIDDictionary.getDictionary().prompt(cuid), keys);
            assoc.cget(cuid, priority, keys, tsuid, rspHandler);
        }
        assoc.waitForDimseRSP();
    }

    private final DimseRSPHandler rspHandler = new DimseRSPHandler() {
        @Override
        public void onDimseRSP(Association as, DicomObject cmd,
                DicomObject data) {
            DicomQuery.this.onMoveRSP(as, cmd, data);
        }
    };

    protected void onMoveRSP(Association as, DicomObject cmd, DicomObject data) {
        if (!CommandUtils.isPending(cmd)) {
            completed += cmd.getInt(Tag.NumberOfCompletedSuboperations);
            warning += cmd.getInt(Tag.NumberOfWarningSuboperations);
            failed += cmd.getInt(Tag.NumberOfFailedSuboperations);
        }

    }

    public TransferCapability selectTransferCapability(String[] cuid) {
        TransferCapability tc;
        for (int i = 0; i < cuid.length; i++) {
            tc = assoc.getTransferCapabilityAsSCU(cuid[i]);
            if (tc != null)
                return tc;
        }
        return null;
    }

    public TransferCapability selectTransferCapability(List<String> cuid) {
        TransferCapability tc;
        for (int i = 0, n = cuid.size(); i < n; i++) {
            tc = assoc.getTransferCapabilityAsSCU(cuid.get(i));
            if (tc != null)
                return tc;
        }
        return null;
    }

    public void close() throws InterruptedException {
        assoc.release(true);
    }

    public void setStoreDestination(String filePath) {
        this.storeDest = new File(filePath);
        this.devnull = "/dev/null".equals(filePath);
        if (!devnull)
            storeDest.mkdir();
    }

    public void initTLS() throws GeneralSecurityException, IOException {
        KeyStore keyStore = loadKeyStore(keyStoreURL, keyStorePassword);
        KeyStore trustStore = loadKeyStore(trustStoreURL, trustStorePassword);
        device.initTLS(keyStore,
                keyPassword != null ? keyPassword : keyStorePassword,
                trustStore);
    }

    private static KeyStore loadKeyStore(String url, char[] password)
            throws GeneralSecurityException, IOException {
        KeyStore key = KeyStore.getInstance(toKeyStoreType(url));
        InputStream in = openFileOrURL(url);
        try {
            key.load(in, password);
        } finally {
            in.close();
        }
        return key;
    }

    private static InputStream openFileOrURL(String url) throws IOException {
        if (url.startsWith("resource:")) {
            return DicomQuery.class.getClassLoader().getResourceAsStream(
                    url.substring(9));
        }
        try {
            return new URL(url).openStream();
        } catch (MalformedURLException e) {
            return new FileInputStream(url);
        }
    }

    private static String toKeyStoreType(String fname) {
        return fname.endsWith(".p12") || fname.endsWith(".P12")
                 ? "PKCS12" : "JKS";
    }


}
