package edu.stanford.isis.epad.plugin.server;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This is the interface for getting files from the DICOM proxy.
 */
public interface EPadFiles {

    /**
     * Does the local DicomProxy have this study.
     * @param studyUID String
     * @return boolean
     */
    public boolean hasStudy(String studyUID);

    /**
     * Get all the studies in the DicomProxy.
     * @return List<String>
     */
    public List<String> getStudies();

    /**
     * Get the study info as a map of key value pairs.
     * @param studyUID String studyUID in the format 1.23.456
     * @return Map of String (keys) to String (values)
     */
    public Map<String,String> getStudyInfo(String studyUID);

    /**
     * Does the DicomProxy have this series?
     * @param parentStudyUID study
     * @param series String
     * @return boolean
     */
    public boolean hasSeries(String parentStudyUID, String series);

    /**
     * Get a list of series for a study.
     * @param parentStudyUID String
     * @return List of Strings
     */
    public List<String> getSeries(String parentStudyUID);

    /**
     * Get the series info as a map of key value pairs.
     * @param seriesUID String seriesID in the format 1.23.456
     * @return Map of String (keys) to String (values)
     */
    public Map<String,String> getSeriesInfo(String seriesUID);


    /**
     * Get the number of files
     * @param seriesUID String
     * @param extension String
     * @return int number of files of type.
     */
    public int hasFiles(String seriesUID, String extension);

    /**
     * Get a list of file associated with an extension for a series.
     * @param seriesUID String
     * @param extension String file extension
     * @return List of file of extension type. If the files have an order, they are sorted.
     */
    public List<File> getFiles(String seriesUID, String extension);

    /**
     * Get the order of files.
     * @param seriesUID String seriesUID
     * @return List of DICOM InstanceUID associated with a series.
     */
    public List<String> getOrderOfFiles(String seriesUID);

    /**
     * Return the number of annotation files in this series.
     * @param seriesUID String
     * @return int
     */
    public int hasAnnotations(String seriesUID);

    /**
     * Get a list of all annotations for this series.
     * @param seriesUID String
     * @return List of AIM annotation files for this series.
     */
    public List<File> getAnnotations(String seriesUID);

    /**
     *
     * @param aimXmlFile File
     * @return True if the file is saved.
     */
    public boolean saveAnnotationFile(File aimXmlFile);


    /**
     * Get an Aim file by it's name. If the file doesn't exist then return null.
     * @param aimFileName String name of the AIM xml file like.  AIM_1234567.xml
     * @return File AIM xml file or null it name doesn't exist.
     */
    public File getAimFile(String aimFileName);

    /**
     * Return the UIDs for the currently selected slice on the client.
     * @return Map of UIdTypes to String
     */
    public Map<UIdType,String> getCurrentSliceUIDs();


    /**
     * Get the base directory as a File. The user should be able
     * to get the full path by calling File.getCanonicalPath();
     * @return File
     */
    public File getBaseDir();

    /**
     * Get the base directory as String in canonical path form, instead of as a file.
     * @return String fully qualified canonical path.
     */
    public String getBaseDirPath();

}
