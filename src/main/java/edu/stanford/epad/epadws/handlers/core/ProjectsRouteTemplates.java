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
package edu.stanford.epad.epadws.handlers.core;

public class ProjectsRouteTemplates
{
	public static final String PROJECT_LIST = "/projects/";
	public static final String PROJECT = PROJECT_LIST + "{project}";
	public static final String PROJECT_AIM_LIST = PROJECT + "/aims/";
	public static final String PROJECT_AIM = PROJECT_AIM_LIST + "{aid}";
	public static final String PROJECT_FILE_LIST = PROJECT + "/files/";
	public static final String PROJECT_FILE = PROJECT_FILE_LIST + "{filename}";
	public static final String SUBJECT_LIST = PROJECT + "/subjects/";
	public static final String SUBJECT = SUBJECT_LIST + "{subject}";
	public static final String SUBJECT_STATUS = SUBJECT + "/status/{status}";
	public static final String SUBJECT_AIM_LIST = SUBJECT + "/aims/";
	public static final String SUBJECT_AIM = SUBJECT_AIM_LIST + "{aid}";
	public static final String SUBJECT_FILE_LIST = SUBJECT + "/files/";
	public static final String SUBJECT_FILE = SUBJECT_FILE_LIST + "{filename}";
	public static final String STUDY_LIST = SUBJECT + "/studies/";
	public static final String STUDY = STUDY_LIST + "{study}";
	public static final String STUDY_AIM_LIST = STUDY + "/aims/";
	public static final String STUDY_AIM = STUDY_AIM_LIST + "{aid}";
	public static final String STUDY_FILE_LIST = STUDY + "/files/";
	public static final String STUDY_FILE = STUDY_FILE_LIST + "{filename}";
	public static final String SERIES_LIST = STUDY + "/series/";
	public static final String SERIESFILE_LIST = STUDY + "/seriesfiles/";
	public static final String SERIES = SERIES_LIST + "{series}";
	public static final String SERIES_AIM_LIST = SERIES + "/aims/";
	public static final String SERIES_AIM = SERIES_AIM_LIST + "{aid}";
	public static final String SERIES_FILE_LIST = SERIES + "/files/";
	public static final String SERIES_FILE = SERIES_FILE_LIST + "{filename}";
	public static final String SERIES_FILE_COMPARE = SERIES_FILE_LIST + "{filename}" + "/file2/" + "{filename2}";
	public static final String IMAGE_LIST = SERIES + "/images/";
	public static final String IMAGEFILE_LIST = SERIES + "/imagefiles/";
	public static final String IMAGE = IMAGE_LIST + "{image}";
	public static final String IMAGE_AIM_LIST = IMAGE + "/aims/";
	public static final String IMAGE_AIM = IMAGE_AIM_LIST + "{aid}";
	public static final String FRAME_LIST = IMAGE + "/frames/";
	public static final String FRAME = FRAME_LIST + "{frame}";
	public static final String FRAME_AIM_LIST = FRAME + "/aims/";
	public static final String FRAME_AIM = FRAME_AIM_LIST + "{aid}";
	public static final String USER_LIST = PROJECT + "/users/";
	public static final String USER = USER_LIST + "{username}";
	public static final String WORKLISTS = PROJECT + "/worklists/";
	public static final String WORKLIST = WORKLISTS + "{workListID}";
	public static final String USER_WORKLISTS = USER + "/worklists/";
	public static final String USER_WORKLIST = USER_WORKLISTS + "{workListID}";
	public static final String USER_WORKLIST_SUBJECTS = USER_WORKLISTS + "{workListID}" + "/subjects/";
	public static final String USER_WORKLIST_SUBJECT = USER_WORKLIST_SUBJECTS + "{subjectID}";
	public static final String USER_WORKLIST_SUBJECT_STUDIES = USER_WORKLIST_SUBJECT + "/studies/";
	public static final String USER_WORKLIST_STUDIES = USER_WORKLISTS + "{workListID}" + "/studies/";
	public static final String USER_WORKLIST_STUDY = USER_WORKLIST_STUDIES + "{studyUID}";
	public static final String USER_WORKLIST_SUBJECT_STUDY = USER_WORKLIST_SUBJECT_STUDIES + "{studyUID}";
	public static final String USER_SUBJECT = USER + "/subjects/{subjectID}";
	public static final String USER_STUDY = USER + "/studies/{studyUID}";
	public static final String TEMPLATE_LIST = PROJECT + "/templates/";
	public static final String TEMPLATE = PROJECT + "/templates/{templatename}";
}
