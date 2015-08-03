//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
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
package edu.stanford.epad.epadws.handlers.dicom;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpException;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.security.EPADSessionOperations;

/**
 * @author Dev Gude
 *
 * Utility class with functions to create a zip file for subject/study/series/image dicoms and 
 * 	either stream them to client 
 *  or just return a json with a pointer to the zip file (client needs to delete after use) 
 */
public class DownloadUtil {
	
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error from WADO";

	/**
	 * Method to download Subject dicoms
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param studyUIDs - download only these selected studies
	 * @throws Exception
	 */
	public static void downloadSubject(boolean stream, HttpServletResponse httpResponse, SubjectReference subjectReference, String username, String sessionID, 
									EPADSearchFilter searchFilter, String studyUIDs, boolean includeAIMs) throws Exception
	{
		log.info("Downloading subject:" + subjectReference.subjectID + " stream:" + stream);
		Set<String> studies = new HashSet<String>();
		if (studyUIDs != null) {
			String[] ids = studyUIDs.split(",");
			for (String id: ids)
				studies.add(id.trim());
		}
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		List<String> fileNames = new ArrayList<String>();
		EPADStudyList studyList = epadOperations.getStudyDescriptions(subjectReference, username, sessionID, searchFilter);
		for (EPADStudy study: studyList.ResultSet.Result)
		{
			if (!studies.isEmpty() && !studies.contains(study.studyUID)) continue;
			File studyDir = new File(downloadDir, "Study-" + study.studyUID);
			studyDir.mkdirs();
			StudyReference studyReference = new StudyReference(subjectReference.projectID, subjectReference.subjectID, study.studyUID);
			EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID, searchFilter, false);
			for (EPADSeries series: seriesList.ResultSet.Result)
			{
				File seriesDir = new File(studyDir, "Series-" + series.seriesUID);
				seriesDir.mkdirs();
				SeriesReference seriesReference = new SeriesReference(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, series.seriesUID);
				EPADImageList imageList = new EPADImageList();
				try {
					imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
				} catch (Exception x) {}
				for (EPADImage image: imageList.ResultSet.Result)
				{
					String name = image.imageUID + ".dcm";
					File imageFile = new File(seriesDir, name);
					fileNames.add("Study-" + studyReference.studyUID + "/Series-" + series.seriesUID + "/" + name);
					FileOutputStream fos = null;
					try 
					{
						fos = new FileOutputStream(imageFile);
						String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
								+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
						performWADOQuery(queryString, fos, username, sessionID);
					}
					catch (Exception x)
					{
						log.warning("Error downloading image using wado");
					}
					finally 
					{
						if (fos != null) fos.close();
					}
				}
			}
		}
		String zipName = "Patient-" + subjectReference.subjectID + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Patient", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, subjectReference.subjectID);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
		
	}

	/**
	 * Method to download list of Study dicoms
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param studyUIDs - download only these selected studies
	 * @throws Exception
	 */
	public static void downloadStudies(boolean stream, HttpServletResponse httpResponse, String studyUIDs, String username, String sessionID, boolean includeAIMs) throws Exception
	{
		log.info("Downloading studies:" + studyUIDs + " stream:" + stream);
		Set<String> studies = new HashSet<String>();
		if (studyUIDs != null) {
			String[] ids = studyUIDs.split(",");
			for (String id: ids)
				studies.add(id.trim());
		}
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		List<String> fileNames = new ArrayList<String>();
		for (String studyUID: studies)
		{
			if (studyUID.length() == 0) continue;
			File studyDir = new File(downloadDir, "Study-" + studyUID);
			studyDir.mkdirs();
			
			StudyReference studyReference = new StudyReference(null, null, studyUID);
			EPADStudy study = epadOperations.getStudyDescription(studyReference, username, sessionID);
			if (study == null)
				throw new Exception("Study not found:" + studyReference.studyUID);
			studyReference = new StudyReference(null, study.patientID, studyUID);
			EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID, new EPADSearchFilter(), false);
			for (EPADSeries series: seriesList.ResultSet.Result)
			{
				File seriesDir = new File(studyDir, "Series-" + series.seriesUID);
				seriesDir.mkdirs();
				SeriesReference seriesReference = new SeriesReference(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, series.seriesUID);
				EPADImageList imageList = new EPADImageList();
				try {
					imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
				} catch (Exception x) {}
				for (EPADImage image: imageList.ResultSet.Result)
				{
					String name = image.imageUID + ".dcm";
					File imageFile = new File(seriesDir, name);
					fileNames.add("Study-" + studyReference.studyUID + "/Series-" + series.seriesUID + "/" + name);
					FileOutputStream fos = null;
					try 
					{
						fos = new FileOutputStream(imageFile);
						String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
								+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
						performWADOQuery(queryString, fos, username, sessionID);
					}
					catch (Exception x)
					{
						log.warning("Error downloading image using wado");
					}
					finally 
					{
						if (fos != null) fos.close();
					}
				}
				if (includeAIMs)
				{
					EPADAIMList aimList = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
					for (EPADAIM aim: aimList.ResultSet.Result)
					{
						String name = "Aim_" + aim.aimID + ".xml";
						File aimFile = new File(seriesDir, name);
						fileNames.add("Study-" + studyReference.studyUID + "/Series-" + series.seriesUID + "/" + name);
						FileWriter fw = null;
						try 
						{
							fw = new FileWriter(aimFile);
							fw.write(aim.xml);
						}
						catch (Exception x)
						{
							log.warning("Error writing aim file");
						}
						finally 
						{
							if (fw != null) fw.close();
						}
					}
				}
			}
		}
		String ids = studyUIDs.replace(",","-");
		if (ids.length() > 128) ids = ids.substring(0, 128);
		String zipName = "Studies-" + ids + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Studies", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, studyUIDs);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
		
	}

	/**
	 * Method to download Study dicoms
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param seriesUIDs - download only these selected series
	 * @throws Exception
	 */
	public static void downloadStudy(boolean stream, HttpServletResponse httpResponse, StudyReference studyReference, String username, String sessionID, EPADSearchFilter searchFilter, String seriesUIDs, boolean includeAIMs) throws Exception
	{
		log.info("Downloading study:" + studyReference.studyUID + " stream:" + stream);
		Set<String> seriesSet = new HashSet<String>();
		if (seriesUIDs != null) {
			String[] ids = seriesUIDs.split(",");
			for (String id: ids)
				seriesSet.add(id.trim());
		}
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		List<String> fileNames = new ArrayList<String>();
		EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID, searchFilter, false);
		for (EPADSeries series: seriesList.ResultSet.Result)
		{
			if (!seriesSet.isEmpty() && !seriesSet.contains(series.seriesUID)) continue;
			File seriesDir = new File(downloadDir, "Series-"+ series.seriesUID);
			seriesDir.mkdirs();
			SeriesReference seriesReference = new SeriesReference(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, series.seriesUID);
			EPADImageList imageList = new EPADImageList();
			try {
				imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
			} catch (Exception x) {}
			int i = 0;
			for (EPADImage image: imageList.ResultSet.Result)
			{
				String name = image.imageUID + ".dcm";
				File imageFile = new File(seriesDir, name);
				fileNames.add("Series-" + series.seriesUID + "/" + name);
				FileOutputStream fos = null;
				try 
				{
					fos = new FileOutputStream(imageFile);
					String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
							+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
					performWADOQuery(queryString, fos, username, sessionID);
				}
				catch (Exception x)
				{
					log.warning("Error downloading image using wado");
				}
				finally 
				{
					if (fos != null) fos.close();
				}
			}
			if (includeAIMs)
			{
				EPADAIMList aimList = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
				for (EPADAIM aim: aimList.ResultSet.Result)
				{
					String name = "Aim_" + aim.aimID + ".xml";
					File aimFile = new File(seriesDir, name);
					fileNames.add("Series-" + series.seriesUID + "/" + name);
					FileWriter fw = null;
					try 
					{
						fw = new FileWriter(aimFile);
						fw.write(aim.xml);
					}
					catch (Exception x)
					{
						log.warning("Error writing aim file");
					}
					finally 
					{
						if (fw != null) fw.close();
					}
				}
			}
		}
		String zipName = "Patient-" + studyReference.subjectID + "-Study-" + studyReference.studyUID + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Study", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, studyReference.studyUID);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
		
	}

	/**
	 * Method to download dicoms from a list of series
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param seriesUIDs
	 * @throws Exception
	 */
	public static void downloadSeries(boolean stream, HttpServletResponse httpResponse, String seriesUIDs, String username, String sessionID, boolean includeAIMs) throws Exception
	{
		log.info("Downloading seriesUIDs:" + seriesUIDs + " stream:" + stream);
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		List<String> fileNames = new ArrayList<String>();
		String[] seriesIDs = seriesUIDs.split(",");
		for (String seriesUID: seriesIDs)
		{
			if (seriesUID.trim().length() == 0) continue;
			File seriesDir = new File(downloadDir, "Series-"+ seriesUID);
			seriesDir.mkdirs();
			SeriesReference seriesReference = new SeriesReference(null, null, null, seriesUID);
			EPADSeries series = epadOperations.getSeriesDescription(seriesReference, username, sessionID);
			seriesReference = new SeriesReference(null, series.patientID, series.studyUID, seriesUID);
			EPADImageList imageList = new EPADImageList();
			try {
				imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
			} catch (Exception x) {}
			int i = 0;
			for (EPADImage image: imageList.ResultSet.Result)
			{
				String name = image.imageUID + ".dcm";
				File imageFile = new File(seriesDir, name);
				fileNames.add("Series-" + series.seriesUID + "/" + name);
				FileOutputStream fos = null;
				try 
				{
					fos = new FileOutputStream(imageFile);
					String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
							+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
					performWADOQuery(queryString, fos, username, sessionID);
				}
				catch (Exception x)
				{
					log.warning("Error downloading image using wado");
				}
				finally 
				{
					if (fos != null) fos.close();
				}
			}
			if (includeAIMs)
			{
				EPADAIMList aimList = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
				for (EPADAIM aim: aimList.ResultSet.Result)
				{
					String name = "Aim_" + aim.aimID + ".xml";
					File aimFile = new File(seriesDir, name);
					fileNames.add("Series-" + series.seriesUID + "/" + name);
					FileWriter fw = null;
					try 
					{
						fw = new FileWriter(aimFile);
						fw.write(aim.xml);
					}
					catch (Exception x)
					{
						log.warning("Error writing aim file");
					}
					finally 
					{
						if (fw != null) fw.close();
					}
				}
			}
		}
		String ids = seriesUIDs.replace(",","-");
		if (ids.length() > 128) ids = ids.substring(0, 128);
		String zipName = "Series-" + ids + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Series", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, seriesUIDs);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
		
	}

	/**
	 * Method to download Series dicoms
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @throws Exception
	 */
	public static void downloadSeries(boolean stream, HttpServletResponse httpResponse, SeriesReference seriesReference, String username, String sessionID, boolean includeAIMs) throws Exception
	{
		log.info("Downloading series:" + seriesReference.seriesUID + " stream:" + stream);
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADImageList imageList = new EPADImageList();
		try {
			imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
		} catch (Exception x) {}
		List<String> fileNames = new ArrayList<String>();
		for (EPADImage image: imageList.ResultSet.Result)
		{
			String name = image.imageUID + ".dcm";
			File imageFile = new File(downloadDir, name);
			fileNames.add(name);
			FileOutputStream fos = null;
			try 
			{
				fos = new FileOutputStream(imageFile);
				String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
						+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
				performWADOQuery(queryString, fos, username, sessionID);
			}
			catch (Exception x)
			{
				log.warning("Error downloading image using wado");
			}
			finally 
			{
				if (fos != null) fos.close();
			}
		}
		if (includeAIMs)
		{
			EPADAIMList aimList = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
			for (EPADAIM aim: aimList.ResultSet.Result)
			{
				String name = "Aim_" + aim.aimID + ".xml";
				File aimFile = new File(downloadDir, name);
				fileNames.add(name);
				FileWriter fw = null;
				try 
				{
					fw = new FileWriter(aimFile);
					fw.write(aim.xml);
				}
				catch (Exception x)
				{
					log.warning("Error writing aim file");
				}
				finally 
				{
					if (fw != null) fw.close();
				}
			}
		}
		String zipName = "Patient-" + seriesReference.subjectID + "-Study-" + seriesReference.studyUID + "-Serie-" + seriesReference.seriesUID + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Series", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, seriesReference.seriesUID);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
	}
	
	/**
	 * Method to download a dicom
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param imageReference
	 * @param username
	 * @param sessionID
	 * @throws Exception
	 */
	public static void downloadImage(boolean stream, HttpServletResponse httpResponse, ImageReference imageReference, String username, String sessionID, boolean dicom) throws Exception
	{
		String queryString = "requestType=WADO&studyUID=" + imageReference.studyUID 
				+ "&seriesUID=" + imageReference.seriesUID + "&objectUID=" + imageReference.imageUID;
		if (dicom)
			queryString = queryString + "&contentType=application/dicom";
		if (stream)
		{
			httpResponse.setContentType("application/octet-stream");
			ServletOutputStream responseStream = httpResponse.getOutputStream();
			performWADOQuery(queryString, responseStream, username, sessionID);
		}
		else
		{
			String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/";
			File downloadDir = new File(downloadDirPath);
			downloadDir.mkdirs();
			String imageName = imageReference.imageUID + ".dcm";
			File imageFile = new File(downloadDir, imageName);
			FileOutputStream fos = null;
			try 
			{
				fos = new FileOutputStream(imageFile);
				performWADOQuery(queryString, fos, username, sessionID);
			}
			catch (Exception x)
			{
				log.warning("Error downloading image using wado");
			}
			finally 
			{
				if (fos != null) fos.close();
			}
			EPADFile epadFile = new EPADFile("", "", "", "", "", imageName, imageFile.length(), "Image", 
					formatDate(new Date()), "download/" + imageFile.getName(), true, imageReference.imageUID);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
	}
	
	/**
	 * Method to download a png
	 * 
	 * @param httpResponse
	 * @param imageReference
	 * @param username
	 * @param sessionID
	 * @throws Exception
	 */
	public static void downloadPNG(HttpServletResponse httpResponse, ImageReference imageReference, String username, String sessionID) throws Exception
	{
		String pngPath = EpadDatabase.getInstance().getEPADDatabaseOperations().getPNGLocation(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID);
		File file = new File(EPADConfig.getEPADWebServerResourcesDir() +  pngPath);
		if (!file.exists()) {
			throw new Exception("Image " + file.getAbsolutePath() + " does not exist");
		}
	    EPADFileUtils.downloadFile(null, httpResponse, file, imageReference.imageUID + ".png");
	}

	public static int performWADOQuery(String queryString, OutputStream outputStream, String username, String sessionID)
	{
		String wadoHost = EPADConfig.dcm4CheeServer;
		int wadoPort = EPADConfig.dcm4cheeServerWadoPort;
		String wadoBase = EPADConfig.wadoURLExtension;
		if (queryString.toLowerCase().indexOf("dicom") != -1)
		{
			log.info("User:" + username  + " host:" + EPADSessionOperations.getSessionHost(sessionID) 
					+ " Wado Request to download dicom:" + queryString);
		}
		String wadoURL = buildWADOURL(wadoHost, wadoPort, wadoBase, queryString);
		int statusCode;
		try {
			statusCode = HandlerUtil.streamGetResponse(wadoURL, outputStream, log);
			if (statusCode != HttpServletResponse.SC_OK)
				log.warning("Unexpected response " + statusCode + " to WADO request " + wadoURL);
		} catch (HttpException e) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE, log);
		} catch (IOException e) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE, log);
		}
		return statusCode;
	}

	private static String buildWADOURL(String host, int port, String base, String queryString)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("http://").append(host);
		sb.append(":").append(port);
		sb.append(base);
		sb.append(queryString);
		return sb.toString();
	}

	public static boolean ZipAndStreamFiles(OutputStream out, List<String> fileNames, String dirPath)
	{

		File dir_file = new File(dirPath);
		int dir_l = dir_file.getAbsolutePath().length();
		ZipOutputStream zipout = new ZipOutputStream(out);
		zipout.setLevel(1);
		for (int i = 0; i < fileNames.size(); i++)
		{
			File f = (File) new File(dirPath + fileNames.get(i));
			if (f.canRead())
			{
				log.debug("Adding file: " + f.getAbsolutePath());
				try
				{
					zipout.putNextEntry(new ZipEntry(f.getAbsolutePath().substring(dir_l + 1)));
				}
				catch (Exception e)
				{
					log.warning("Error adding to zip file", e);
					return false;
				}
				BufferedInputStream fr;
				try
				{
					fr = new BufferedInputStream(new FileInputStream(f));

					byte buffer[] = new byte[0xffff];
					int b;
					while ((b = fr.read(buffer)) != -1)
						zipout.write(buffer, 0, b);

					fr.close();
					zipout.closeEntry();

				}
				catch (Exception e)
				{
					log.warning("Error closing zip file", e);
					return false;
				}
			}
		}

		try
		{
			zipout.finish();
			out.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;

	}

	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private static String formatDate(Date date)
	{
		if (date == null)
			return "";
		else
			return dateFormat.format(date);
	}

}
