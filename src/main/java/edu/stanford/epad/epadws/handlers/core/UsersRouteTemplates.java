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

public class UsersRouteTemplates
{
	public static final String USER_LIST = "/users/";
	public static final String USER = USER_LIST + "{username}";
	public static final String USER_SESSIONS = USER + "/sessions/";
	public static final String USER_WORKLISTS = USER + "/worklists/";
	public static final String USER_WORKLIST = USER_WORKLISTS + "{worklistID}";
	public static final String USER_WORKLIST_SUBJECTS = USER_WORKLIST + "/subjects/";
	public static final String USER_WORKLIST_STUDIES = USER_WORKLIST + "/studies/";
	public static final String USER_PROJECT_SUBJECT = USER_WORKLIST + "/projects/" + "{projectID}" + "/subjects/" + "{subjectID}";
	public static final String USER_PROJECT_STUDY = USER_WORKLIST + "/projects/" + "{projectID}" + "/studies/" + "{studyUID}";
	public static final String USER_SUBJECT = USER_WORKLIST + "/subjects/" + "{subjectID}";
	public static final String USER_STUDY = USER_WORKLIST + "/studies/" + "{studyUID}";
	public static final String USER_REVIEWERS = USER + "/reviewers/";
	public static final String USER_REVIEWER = USER + "/reviewers/{username}";
	public static final String USER_REVIEWEES = USER + "/reviewees/";
	public static final String USER_REVIEWEE = USER + "/reviewees/{username}";
}
