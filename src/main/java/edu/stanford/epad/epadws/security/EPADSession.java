package edu.stanford.epad.epadws.security;

import java.util.Date;

public class EPADSession {

	String sessionId;
	String username;
	int lifespan; // milliseconds
	Date createdTime;
	Date lastActivity;
	
	EPADSession(String sessionId, String username, int lifespan)
	{
		this.sessionId = sessionId;
		this.username = username;
		this.lifespan = lifespan;
		this.createdTime = new Date();
		this.lastActivity = new Date();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(Date lastActivity) {
		this.lastActivity = lastActivity;
	}
	
}
