package edu.stanford.isis.epadws.db.mysql.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a cache to prevent repeated calls to the database for finding the parent Series/Studies.
 *
 * @author amsnyder
 */
public class DicomParentCache {
    private static DicomParentCache ourInstance = new DicomParentCache();

    Map<String,DicomParent> parentMap = new ConcurrentHashMap<String, DicomParent>();

    public static DicomParentCache getInstance() {
        return ourInstance;
    }

    private DicomParentCache() {
    }

    /**
     * Get the DicomParent for a certain UID.
     * @param uid String
     * @return DicomParent - is a UID and enum for SERIES, STUDY, etc ...
     */
    public DicomParent getParent(String uid){
        return parentMap.get(uid);
    }

    public void setParent(String childUID, String parentUID, DicomParentType type){
        parentMap.put(childUID, new DicomParent(parentUID,type));
    }

    public boolean hasParent(String childUID){
        return getParent(childUID)!=null;
    }

}
