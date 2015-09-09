package edu.stanford.epad.epadws.models;

import java.util.Date;

import edu.stanford.epad.epadws.models.dao.AbstractDAO;

/**
 * Project to PlugIn relation
 * 
 * @author Emel Alkim
 *
 */

public class ProjectToPlugin extends AbstractDAO {

	long id;
	long projectId;
	long pluginId;
	boolean enabled;
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

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	
	public long getPluginId() {
		return pluginId;
	}

	public void setPluginId(long pluginId) {
		this.pluginId = pluginId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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

	public final static String DBTABLE = "project_plugin";
	public final static String[][] DBCOLUMNS = {
        {"id","long","id","Id"},
        {"projectId","long","project_id","integer"},  
        {"pluginId","long","plugin_id","integer"},
        {"enabled","Boolean","enabled","tinyint(1)"},
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
