package edu.stanford.epad.epadws.models;

import java.util.Date;

import edu.stanford.epad.epadws.models.dao.AbstractDAO;

/**
 * Project to PlugIn Parameter relation
 * 
 * @author Emel Alkim
 *
 */

public class ProjectToPluginParameter extends AbstractDAO {

	long id;
	long projectId;
	long pluginId;
	String name;
	String defaultValue;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
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

	public final static String DBTABLE = "project_pluginparameter";
	public final static String[][] DBCOLUMNS = {
        {"id","long","id","Id"},
        {"projectId","long","project_id","integer"},  
        {"pluginId","long","plugin_id","integer"},
        {"name","String","name","varchar"},
        {"defaultValue","String","default_value","varchar"},
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
