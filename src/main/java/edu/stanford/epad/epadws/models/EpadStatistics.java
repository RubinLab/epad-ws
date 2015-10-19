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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;

import edu.stanford.epad.epadws.models.dao.AbstractDAO;

public class EpadStatistics extends AbstractDAO {

	long id;
	String host;
	int numOfUsers;
	int numOfProjects;
	int numOfPatients;
	int numOfStudies;
	int numOfSeries;
	int numOfAims;
	int numOfDSOs;
	int numOfPacs;
	int numOfAutoQueries;
	int numOfWorkLists;
	int numOfFiles;
	int numOfTemplates;
	int numOfPlugins;
	
	String creator;
	Date createdTime;
	Date updateTime;
	
	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getNumOfUsers() {
		return numOfUsers;
	}

	public void setNumOfUsers(int numOfUsers) {
		this.numOfUsers = numOfUsers;
	}

	public int getNumOfProjects() {
		return numOfProjects;
	}

	public void setNumOfProjects(int numOfProjects) {
		this.numOfProjects = numOfProjects;
	}

	public int getNumOfPatients() {
		return numOfPatients;
	}

	public void setNumOfPatients(int numOfPatients) {
		this.numOfPatients = numOfPatients;
	}

	public int getNumOfStudies() {
		return numOfStudies;
	}

	public void setNumOfStudies(int numOfStudies) {
		this.numOfStudies = numOfStudies;
	}

	public int getNumOfSeries() {
		return numOfSeries;
	}

	public void setNumOfSeries(int numOfSeries) {
		this.numOfSeries = numOfSeries;
	}

	public int getNumOfAims() {
		return numOfAims;
	}

	public void setNumOfAims(int numOfAims) {
		this.numOfAims = numOfAims;
	}

	public int getNumOfDSOs() {
		return numOfDSOs;
	}

	public void setNumOfDSOs(int numOfDSOs) {
		this.numOfDSOs = numOfDSOs;
	}

	public int getNumOfWorkLists() {
		return numOfWorkLists;
	}

	public void setNumOfWorkLists(int numOfWorkLists) {
		this.numOfWorkLists = numOfWorkLists;
	}

	public int getNumOfPacs() {
		return numOfPacs;
	}

	public void setNumOfPacs(int numOfPacs) {
		this.numOfPacs = numOfPacs;
	}

	public int getNumOfAutoQueries() {
		return numOfAutoQueries;
	}

	public void setNumOfAutoQueries(int numOfAutoQueries) {
		this.numOfAutoQueries = numOfAutoQueries;
	}

	public int getNumOfFiles() {
		return numOfFiles;
	}

	public void setNumOfFiles(int numOfFiles) {
		this.numOfFiles = numOfFiles;
	}

	public int getNumOfTemplates() {
		return numOfTemplates;
	}

	public void setNumOfTemplates(int numOfTemplates) {
		this.numOfTemplates = numOfTemplates;
	}

	public int getNumOfPlugins() {
		return numOfPlugins;
	}

	public void setNumOfPlugins(int numOfPlugins) {
		this.numOfPlugins = numOfPlugins;
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

	public void addStatistics(EpadStatistics other) {
		numOfUsers = numOfUsers + other.getNumOfUsers();
		numOfProjects = numOfProjects + other.getNumOfProjects();
		numOfPatients = numOfPatients + other.getNumOfPatients();
		numOfStudies = numOfStudies + other.getNumOfStudies();
		numOfSeries = numOfSeries + other.getNumOfSeries();
		numOfAims = numOfAims + other.getNumOfAims();
		numOfDSOs = numOfDSOs + other.getNumOfDSOs();
		numOfPacs = numOfPacs + other.getNumOfPacs();
		numOfAutoQueries = numOfAutoQueries + other.getNumOfAutoQueries();
		numOfWorkLists = numOfWorkLists + other.getNumOfWorkLists();
		numOfFiles = numOfFiles + other.getNumOfFiles();
		numOfTemplates = numOfTemplates + other.getNumOfTemplates();
		numOfPlugins = numOfPlugins + other.getNumOfPlugins();
	}
	
	public final static String DBTABLE = "epadstatistics";
	public final static String[][] DBCOLUMNS = {
        {"id","long","id","Id"},
        {"host","String","host","varchar"},
        {"numOfUsers","int","numOfUsers","Integer"},
		{"numOfProjects","int","numOfProjects","Integer"},
		{"numOfPatients","int","numOfPatients","Integer"},
		{"numOfStudies","int","numOfStudies","Integer"},
		{"numOfSeries","int","numOfSeries","Integer"},
		{"numOfAims","int","numOfAims","Integer"},
		{"numOfDSOs","int","numOfDSOs","Integer"},
		{"numOfWorkLists","int","numOfWorkLists","Integer"},
		{"numOfPacs","int","numOfPacs","Integer"},
		{"numOfAutoQueries","int","numOfAutoQueries","Integer"},
		{"numOfFiles","int","numOfFiles","Integer"},
		{"numOfTemplates","int","numOfTemplates","Integer"},
		{"numOfPlugins","int","numOfPlugins","Integer"},
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
