/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.server.managers.localstore;

import edu.stanford.isis.dicomproxy.common.FileKey;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Watch the dicom directory for new directories that are not related to the pipeline.
 * Look for new files that don't have *.tag files.
 *
 * <p>When a new dicom file is seen without at tag file do the following:</p>
 * <ul>
 * <li>(a) Watch that dicom file until it stops increasing in size.</li>
 * <li>(b) Create the *.tag file.</li>
 * <li>(c) Create the series file if it doesn't exist.</li>
 * <li>(d) Create the *.jpg file.</li>
 * </li>(e) Update the order file.</li>
 * </ul>
 */
public class DicomDirWatcher implements Runnable{

    private static final ProxyLogger logger = ProxyLogger.getInstance();

    private final Set<FileKey> knownDicomFilesWithTagFile;
    private final Map<FileKey,WatchData> newDicomFilesWithoutTagFile;

    private final Set<FileKey> knownPipelineDirs;
    private final Set<FileKey> knownLocalStoreDirs;

    private final Map<FileKey,WatchData> newLocalStoreDirs;

    private static final String BASE_DICOM_DIR = "../resources/dicom";

    boolean isFirst=true;

    public DicomDirWatcher(){
        knownDicomFilesWithTagFile = Collections.synchronizedSet(new HashSet<FileKey>());
        newDicomFilesWithoutTagFile = new ConcurrentHashMap<FileKey,WatchData>();

        knownPipelineDirs = Collections.synchronizedSet(new HashSet<FileKey>());
        knownLocalStoreDirs = Collections.synchronizedSet(new HashSet<FileKey>());

        newLocalStoreDirs = new ConcurrentHashMap<FileKey,WatchData>();
    }//

    @Override
    public void run() {

        init();

        while(true){





        }//while

    }//run

    /**
     * Initialize all the Maps.
     */
    private void init(){

        File dicomDir = new File(BASE_DICOM_DIR);



    }

    class WatchData{
        long firstSeen;
        long lastUpdate;



    }
}
