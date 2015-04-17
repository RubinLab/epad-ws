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

public class User extends AbstractDAO {

	long id;
	String username;
	String firstName;
	String lastName;
	String email;
	String password;
	boolean enabled = true;
	String permissions;
	Date lastLogin;
	boolean admin;
	boolean passwordExpired;
	Date passwordUpdate;
	String creator;
	Date createdTime;
	Date updateTime;
	transient String role;  // Only valid within context of a project
	transient List<EventLog> eventLogs = new ArrayList<EventLog>();
	transient Map<String, String> projectToRole; // List of projects and the user's role in them
	public static final String CreateProjectPermission = "CreateProject";
	public static final String CreateWorkListPermission = "CreateWorkList";
	public static final String CreateUserPermission = "CreateUser";
	public static final String CreatePACPermission = "CreatePAC";
	public static final String CreateAutoPACQueryPermission = "CreateAutoPACQuery";
	
	public static final class EventLog
	{
		public final Date date;
		public final Level level;
		public final String message;		

		public EventLog(Level level, String message)
		{
			this.level = level;
			this.message = message;
			this.date = new Date();
		}
	}
	
	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPermissions() {
		if (permissions == null) return CreateProjectPermission;
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public Date getPasswordUpdate() {
		return passwordUpdate;
	}

	public void setPasswordUpdate(Date passwordUpdate) {
		this.passwordUpdate = passwordUpdate;
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

	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Map<String, String> getProjectToRole() {
		return projectToRole;
	}

	public void setProjectToRole(Map<String, String> projectToRole) {
		this.projectToRole = projectToRole;
	}

	public List<EventLog> getEventLogs() {
		return eventLogs;
	}

	public void setEventLogs(List<EventLog> eventLogs) {
		this.eventLogs = eventLogs;
	}

	public void addEventLog(Level level, String message)
	{
		EventLog el = new EventLog(level, message);
		eventLogs.add(el);
	}
	
	public boolean hasPermission(String permission)
	{
		if (("," + getPermissions() + ",").indexOf("," + permission + ",") != -1)
			return true;
		else
			return false;
	}
	public final static String DBTABLE = "user";
	public final static String[][] DBCOLUMNS = {
        {"id","long","id","Id"},
        {"username","String","username","varchar"},
        {"firstName","String","firstname","varchar"},
        {"lastName","String","lastname","varchar"},
        {"email","String","email","varchar"},
        {"password","String","password","varchar"},
        {"permissions","String","permissions","varchar"},
        {"enabled","boolean","enabled","bit"},
        {"admin","boolean","admin","bit"},
        {"passwordExpired","boolean","passwordexpired","bit"},
        {"passwordUpdate","Date","passwordupdate","date"},
        {"lastLogin","Date","lastLogin","timestamp"},
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
