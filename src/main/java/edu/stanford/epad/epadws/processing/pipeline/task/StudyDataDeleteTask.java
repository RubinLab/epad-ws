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

import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.models.NonDicomSeries;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.UserProjectService;
import edu.stanford.epad.epadws.xnat.XNATDeletionOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * 
 * @author martin
 * 
 */
public class StudyDataDeleteTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final String username;
	private final String projectID;
	private final String patientID;
	private final String studyUID;
	private final boolean deleteAims;
	
	private static final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();	
	
	public StudyDataDeleteTask(String username, String projectID, String patientID, String studyUID, boolean deleteAims)
	{
		this.username = username;
		this.projectID = projectID;
		this.patientID = patientID;
		this.studyUID = studyUID;
		this.deleteAims = deleteAims;
	}

	@Override
	public void run()
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();

		try {
    		Set<String>projectIds = UserProjectService.getAllProjectIDs();
    		boolean deleteCompletely = true;
    		boolean deleteInUnassigned = false;
    		for (String projectId: projectIds)
    		{
    			Set<String> allStudyUIDs = UserProjectService.getAllStudyUIDsForProject(projectId);
    			if (allStudyUIDs.contains(studyUID.replace('.', '_')) || allStudyUIDs.contains(studyUID))
    			{
        			if (projectId.equals(EPADConfig.xnatUploadProjectID))
        			{
        				deleteInUnassigned =  true;
        				continue;
        			}
    				deleteCompletely = false;
    				break;
    			}
    		}
			if (deleteCompletely)
			{
				if (deleteInUnassigned)
				{
					List<NonDicomSeries> ndSerieses = projectOperations.getNonDicomSeriesForStudy(studyUID);
					for (NonDicomSeries series: ndSerieses)
					{
						projectOperations.deleteNonDicomSeries(series.getSeriesUID());
					}
					projectOperations.deleteStudy("admin", studyUID, patientID, projectID);
					projectOperations.deleteStudy("admin", studyUID);
				}
				epadOperations.deleteStudyFromEPadAndDcm4CheeDatabases(studyUID, deleteAims);
			}
			else
			{
				String msg = "Study " + studyUID + " in use by other projects or subjects so will not be deleted from DCM4CHEE";
				log.info(msg);
				EpadDatabase.getInstance().getEPADDatabaseOperations().insertEpadEvent(
						username, 
						"Study Not Deleted", 
						"", "", patientID, "", "", "", msg, projectID,"",studyUID,"", false);					

			}
		} catch (Exception e) {
			log.warning("Error deleting study " + studyUID + " for patient " + patientID + " in project " + projectID, e);
		}
	}
}
