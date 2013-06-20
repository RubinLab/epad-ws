/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server;

import edu.stanford.isis.epadws.common.*;
import edu.stanford.isis.epadws.tree.Node;
import edu.stanford.isis.epadws.tree.Tree;

import org.dcm4che2.data.Tag;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This tree has one Study at the root node, which contains 1 to n Series, which optionally contains
 * 1 to n Images or SOPInstances.
 *
 * The SearchResult can be built up over time, using multiple queries.
 *
 * @author amsnyder
 */
public class SearchResult implements DicomSearchResult {

    ProxyLogger logger = ProxyLogger.getInstance();
    /**
     * List of tree structures with one tree structure for each
     * study.
     */
    final Map<DicomStudyUID,Tree<DicomData>> searchResultMap;
    /**
     * List of study instance UID values for studies contained in
     * search results.
     */
    final List<DicomStudyUID> studyList;
    /**
     * String indicating type of search that was used to locate the
     * studies.
     */
    final DicomSearchType searchType;
    /**
     * Value used in search for studies.
     */
    final String searchParam;

    final DicomSearchMap searchMap;

    public SearchResult(DicomSearchType type, String param){
        searchResultMap = new ConcurrentHashMap<DicomStudyUID,Tree<DicomData>>();
        studyList = new ArrayList<DicomStudyUID>();

        searchType = type;
        this.searchParam = param;

        searchMap = DicomSearchMap.getInstance();
    }

    @Override
    public List<DicomStudyData> getStudies() {
        List<DicomStudyData> retVal = new ArrayList<DicomStudyData>();

        for(DicomStudyUID currID : studyList){
            Tree<DicomData> studyTree = searchResultMap.get(currID);
            Node<DicomData> node = studyTree.getRootElement();
            retVal.add((DicomStudyData)node.getData());
        }
        return retVal;
    }

    @Override
    public List<DicomSeriesData> getSeriesForStudyId(String studyID) {
        List<DicomSeriesData> retVal = new ArrayList<DicomSeriesData>();

        DicomStudyUID studyUID = new DicomStudyUID(studyID);
        Tree<DicomData> tree = searchResultMap.get(studyUID);

        if(tree==null){
            logger.info("WARNING: No Series found for StudyID="+studyID+"! Returning empty list.");
            return retVal;
        }

        Node<DicomData> node = tree.getRootElement();

        Set<DicomSeriesUID> seriesUIDSet = new HashSet<DicomSeriesUID>();
        for(Node<DicomData> currNode : node.getChildren()){

            //ToDo: Need to determine why Tree give nodes twice.
            if( isNew(seriesUIDSet,currNode)){
                retVal.add((DicomSeriesData) currNode.getData());
            }
        }
        return retVal;
    }

    /**
     * Check if this is new.
     * @param set
     * @param currNode
     * @return true if this ID hasn't been seen before. False if it has already been seen.
     */
    private static boolean isNew(Set<DicomSeriesUID> set, Node<DicomData> currNode){
        DicomSeriesData data = (DicomSeriesData) currNode.getData();
        DicomSeriesUID seriesId = data.getSeriesId();

        return set.add(seriesId);
    }

    @Override
    public List<DicomImageData> getSOPInstancesForSeriesId(String seriesID) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Add a series to the
     * @param data - Map<Tag,String> data
     */
    public void addStudyResults(Map<DicomTag,String> data){

        String studyInstanceId = data.get(DicomTag.forInt(Tag.StudyInstanceUID));
        DicomStudyUID studyUID = new DicomStudyUID(studyInstanceId);

        RStudyData studyData = RDataBuilder.createStudyData(data);

        Tree<DicomData> tree = new Tree<DicomData>();
        tree.setRootElement(new Node<DicomData>(studyData));
        searchResultMap.put(studyUID,tree);
        studyList.add(studyUID);

        System.out.println("Add Study Results: studyUID="+studyUID);
    }

    /**
     * Add Series results to a study.
     * @param data - Map<Tag,String> data
     */
    public void addSeriesToStudy(DicomStudyUID dicomStudyUID, Map<DicomTag,String> data){

        logger.info("Add Series to Study: studyUID="+dicomStudyUID);

        String seriesInstanceId = data.get(DicomTag.forInt(Tag.SeriesInstanceUID));
        DicomSeriesUID seriesUID = new DicomSeriesUID(seriesInstanceId);

        RSeriesData seriesData = RDataBuilder.createSeriesData(data,dicomStudyUID,seriesUID);


        Tree<DicomData> tree = searchResultMap.get(dicomStudyUID);
        if(tree==null){
            throw new IllegalStateException("Could not find. studyUID="
                    +dicomStudyUID+", "+printStateInfo());
        }

        Node<DicomData> studyNode = tree.getRootElement();
        studyNode.addChild(new Node<DicomData>(seriesData));

        searchMap.put(seriesUID,dicomStudyUID);
    }

    /**
     * Add Image results to a series.
     * @param data - Map<Tag,String> data
     */
    void addImagesToSeries(DicomSeriesUID seriesId, Map<DicomTag,String> data){

    }


    /**
     * Print the state of of this search result for debugging.
     * @return
     */
    private String printStateInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("searchType: ").append(searchType);
        sb.append(", searchParam: ").append(searchParam);
        sb.append(", searchResultMap: ").append(searchResultMap);
        sb.append(", studyList: ").append(studyList);

        return sb.toString();
    }

}
