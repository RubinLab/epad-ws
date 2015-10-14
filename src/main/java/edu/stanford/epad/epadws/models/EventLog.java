package edu.stanford.epad.epadws.models;

//Copyright (c) 2014 The Board of Trustees of the Leland Stanford Junior University
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

import java.util.Date;

import edu.stanford.epad.epadws.models.dao.AbstractDAO;

public class EventLog extends AbstractDAO {

	long id;
	String projectID;
	String subjectUID;
	String studyUID;
	String seriesUID;
	String imageUID;
	String aimID;
	String username;
	String filename;
	String function;
	String params;
	String creator;
	boolean error;
	Date createdTime;
	Date updateTime;

	public EventLog(long id)
	{
		this.id = id;
	}
	
	public EventLog() {
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public String getSubjectUID() {
		return subjectUID;
	}

	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
	}

	public String getStudyUID() {
		return studyUID;
	}

	public void setStudyUID(String studyUID) {
		this.studyUID = studyUID;
	}

	public String getSeriesUID() {
		return seriesUID;
	}

	public void setSeriesUID(String seriesUID) {
		this.seriesUID = seriesUID;
	}

	public String getImageUID() {
		return imageUID;
	}

	public void setImageUID(String imageUID) {
		this.imageUID = imageUID;
	}

	public String getAimID() {
		return aimID;
	}

	public void setAimID(String aimID) {
		this.aimID = aimID;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getTarget() {
		String t = "";
		if (subjectUID != null)
			t = t + subjectUID;
		if (studyUID != null)
			t = t + ":" + studyUID;
		if (seriesUID != null)
			t = t + ":" + seriesUID;
		if (aimID != null)
			t = t + ":" + aimID;
		if (filename != null)
			t = t + ":" + filename;
		if (t.startsWith(":"))
			t = t.substring(1);
		return t;
	}
	
	public final static String DBTABLE = "eventlog";
	public final static String[][] DBCOLUMNS = {
        {"id","long","id","Id"},
        {"projectID","String","projectID","varchar"},
        {"subjectUID","String","subjectUID","varchar"},
        {"studyUID","String","studyUID","varchar"},
        {"seriesUID","String","seriesUID","varchar"},
        {"imageUID","String","imageUID","varchar"},
        {"aimID","String","aimID","varchar"},
        {"filename","String","filename","varchar"},
        {"username","String","username","varchar"},
        {"function","String","function","varchar"},
        {"params","String","params","varchar"},
        {"error","boolean","error","bit"},
        {"creator","String","creator","varchar"},
        {"createdTime","Date","createdtime","timestamp"},
        {"updateTime","Date","updatetime","timestamp"},	
	};

	@Override
	public String returnDBTABLE() {
		return DBTABLE;
	}

	@Override
	public String[][] returnDBCOLUMNS() {
		return DBCOLUMNS;
	}

}
