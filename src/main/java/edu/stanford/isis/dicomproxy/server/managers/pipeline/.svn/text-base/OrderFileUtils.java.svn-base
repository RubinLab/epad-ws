/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.server.managers.pipeline;

import edu.stanford.isis.dicomproxy.common.DicomFormatUtil;
import edu.stanford.isis.dicomproxy.common.DicomTagFileUtils;
import edu.stanford.isis.dicomproxy.common.ProxyFileUtils;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;

import java.io.*;
import java.util.*;

/**
 * Utility methods for order files.
 */
public class OrderFileUtils {

    private static final ProxyLogger logger = ProxyLogger.getInstance();

    private OrderFileUtils(){}

    /**
     * Creates the order file if is doesn't exist.
     * @param studyUID String
     * @param seriesUID String
     * @return File
     */
    public static File createOrderFile(String studyUID, String seriesUID){
        String orderFilePath = DicomFormatUtil.createOrderFilePath(studyUID,seriesUID);
        File orderFile = new File(orderFilePath);
        if( !orderFile.exists() ){
            try{
                logger.info("OrderFileUtils orderFilePath.createNewFile(...)="+orderFilePath+", seriesUID="+seriesUID);
                boolean success = orderFile.createNewFile();
                if(success){
                    return orderFile;
                }else{
                    throw new IllegalStateException("createNewFile method failed to make file: "+orderFile.getAbsolutePath());
                }
            }catch(IOException ioe){
                throw new IllegalStateException("Failed to create order file: "+orderFile.getAbsolutePath());
            }
        }
        return orderFile;
    }

    public static List<ImageOrder> readOrderFileFromTags(File orderFile){
        List<ImageOrder> retVal = new ArrayList<ImageOrder>();
        try{
            //Get the directory series from the order file name.
            File seriesDir = getSeriesDirFromOrderFile(orderFile.getCanonicalPath());
            //logger.info("readOrderFileFromTags: seriesDir="+seriesDir.getAbsolutePath());

            List<File> tagFiles = ProxyFileUtils.getAllFilesWithExtension(seriesDir, ".tag");

            for(File currTagFile : tagFiles){
                Map<String,String> currTagMap = DicomTagFileUtils.readTagFile(currTagFile);
                String order = currTagMap.get(DicomTagFileUtils.INSTANCE_NUMBER);
                String sopInstanceID = currTagMap.get(DicomTagFileUtils.SOP_INST_UID);

                retVal.add(new ImageOrder(Integer.parseInt(order),sopInstanceID,currTagMap));
            }
        }catch(Exception e){
            logger.warning("Could read tag files for: "+orderFile.getAbsolutePath(),e);
        }

        if(retVal.size()==0){
            logger.info("WARNING: readOrderFileFromTags size=0");
        }else{
            logger.info("readOrderFileFromTags size="+retVal.size());
        }

        retVal = removeDuplicateInstanceNumbers(retVal);
        Collections.sort(retVal);

        return retVal;
    }

    /**
     * Check the list for a duplicate ImageOrder and remove it.
     * @param imageOrderList List of ImageOrder
     * @return List of ImageOrder
     */
    public static List<ImageOrder> removeDuplicateInstanceNumbers(List<ImageOrder> imageOrderList){
        List<ImageOrder> retVal = new ArrayList<ImageOrder>();

        Set<Integer> orderSet = new HashSet<Integer>();
        for(ImageOrder imageOrder : imageOrderList){
            int order = imageOrder.getOrder();
            if( !orderSet.contains(new Integer(order))){
                retVal.add(imageOrder);
                orderSet.add(new Integer(order));
            }else{
                logger.info("Eliminating duplicate: "+imageOrder.toNewFormatString());
            }
        }
        return retVal;
    }


    /**
     * Just need to remove the *.order extension for the series directory.
     * @param orderFilePath String
     * @return File
     */
    private static File getSeriesDirFromOrderFile(String orderFilePath){
        String dir = orderFilePath.replaceAll(".order","");
        dir = dir.replaceAll("series_","");
        logger.info("getSeriesDirFromOrderFile: "+dir);
        return new File(dir);
    }//getSeriesDirFromOrderFile


    /**
     * Write out an "order" file which are in the series directory.
     * @param imageOrderList List ImageOrder
     * @param orderFile File
     * @throws IOException on write error.
     */
    public static void writeOrderFile(List<ImageOrder> imageOrderList, File orderFile)
        throws IOException
    {
        FileWriter fstream = new FileWriter(orderFile.getAbsolutePath());
        BufferedWriter oldFormat = new BufferedWriter(fstream);

        FileWriter newFstream = new FileWriter( orderFile.getAbsolutePath().replaceAll("order","ord") );
        BufferedWriter newFormat = new BufferedWriter( newFstream );

        logger.info("Start WriteOrderFile: "+orderFile.getAbsolutePath());
        newFormat.write(ImageOrder.toNewFormatHeader());
        newFormat.newLine();
        for(ImageOrder imageOrder : imageOrderList){
            String line = imageOrder.toString();
            if( !"".equals(line)){
                //logger.info("writeOrderFile: "+orderFile.getName()+" line="+line);
                oldFormat.write(line);
                oldFormat.newLine();

                newFormat.write(imageOrder.toNewFormatString());
                newFormat.newLine();
            }
        }
        logger.info("End order file: "+orderFile.getName());

        oldFormat.flush();
        oldFormat.close();

        newFormat.flush();
        newFormat.close();
    }//writeOrderFile


    /**
     * A line is assumed to be in the format:   order and seriesUID with '_' under-scores.
     *  [1_234_45_678.dcm] 3
     * @param imageOrderLine String
     * @return String
     */
    public static String getImageUidFromLine(String imageOrderLine){
        int start = imageOrderLine.indexOf('[');
        int end = imageOrderLine.indexOf(']');

        return imageOrderLine.substring(start+1,end);
    }

    /**
     *
     * @param imageOrderLine String [ImageUID] #order
     * @return int
     */
    public static int getImageOrderFromLine(String imageOrderLine){
        try{
            int index = imageOrderLine.indexOf(']');
            String number = imageOrderLine.substring(index+1);
            number =  number.trim();
            return Integer.parseInt(number);
        }catch(Exception e){
            throw new IllegalStateException("Exception in OrderFileUtils.getImageOrderFromLine for line=_"+imageOrderLine+"_",e);
        }
    }

    /**
     * If an order file exists, read it and parse it into a list of OrderFileEntry classes.
     * @param orderFilePath String file path to order file. ../resources/dicom/[studyUID]/[seriesUID].order
     * @param protoTagsMap Map of String to String. Contains a "prototype" tags from a different file.
     *              Here we will replace the InstanceNumber tag.
     * @param baseDir String
     * @return List of OrderFileEntry
     */
    public static List<OrderFileRunnable.OrderFileEntry> readOrderFile(String orderFilePath,
                                                                      Map<String,String> protoTagsMap,
                                                                      String baseDir)
    {
        List<OrderFileRunnable.OrderFileEntry> retVal = new ArrayList<OrderFileRunnable.OrderFileEntry>();
        try{
            String content = ProxyFileUtils.read(new File(orderFilePath));
            String[] lines = content.split("\n");

            for(String line : lines){

                if( "".equals(line) ) continue;

                int imageOrder = OrderFileUtils.getImageOrderFromLine(line);
                String name = OrderFileUtils.getImageUidFromLine(line);

                protoTagsMap.put(DicomTagFileUtils.INSTANCE_NUMBER,""+imageOrder);
                String path = ProxyFileUtils.appendNameToDir(baseDir, name);
                retVal.add(new OrderFileRunnable.OrderFileEntry(new File(path),protoTagsMap));
            }//for

        }catch(Exception e){
            logger.warning("OrderFileUtils.readOrderFile had: ",e);
        }
        return retVal;
    }//readOrderFile

}//class
