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
package edu.stanford.epad.epadws.processing.pipeline.task;

import java.util.Date;
import java.util.Set;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.TCIAService;
import edu.stanford.epad.epadws.service.UserProjectService;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * Delete a patient and all studies for that patient in the ePAD and DCMCHEE databases.
 * 
 * @author martin
 * 
 */
public class TCIADownloadTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final String projectID;
	private final String collection;
	private final String patientID;
	private final String studyUID;
	private final String username;
	private static final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();	

	public TCIADownloadTask(String projectID, String collection, String patientID, String studyUID, String username)
	{
		this.projectID = projectID;
		this.collection = collection;
		this.patientID = patientID;
		this.studyUID = studyUID;
		this.username = username;
	}

	@Override
	public void run()
	{
		try
		{
			if (studyUID == null || studyUID.length() == 0)
			{
				projectOperations.createEventLog(username, projectID, patientID, null, null, null, null, "DOWNLOAD PATIENT FROM TCIA", "");
				TCIAService.getInstance().downloadPatientFromTCIA(username, collection, patientID, projectID);
			}
			else
			{
				projectOperations.createEventLog(username, projectID, patientID, studyUID, null, null, null, "DOWNLOAD STUDY FROM TCIA", "");
				TCIAService.getInstance().downloadStudyFromTCIA(username, collection, patientID, studyUID, projectID);
			}
		}
		catch (Exception x) {
			log.warning("Error downloading from TCIA", x);
		}
	}
}
