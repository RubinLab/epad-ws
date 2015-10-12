package edu.stanford.epad.epadws.models;

import java.util.Date;

import edu.stanford.epad.epadws.models.dao.AbstractDAO;

/**
 * Plugin 
 * 
 * @author Emel Alkim
 *
 */

public class Plugin extends AbstractDAO {

	long id;
	String pluginId;
	String name;
	String description;
	String javaclass;
	Boolean enabled;
	String status;
	String modality;
	String developer;
	String documentation;
	int rateTotal;
	int rateCount;
	double rate;
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


	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJavaclass() {
		return javaclass;
	}

	public void setJavaclass(String javaclass) {
		this.javaclass = javaclass;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public int getRateTotal() {
		return rateTotal;
	}

	public void setRateTotal(int rateTotal) {
		this.rateTotal = rateTotal;
	}

	public int getRateCount() {
		return rateCount;
	}

	public void setRateCount(int rateCount) {
		this.rateCount = rateCount;
	}

	public void rate(int rate) {
		if (rate >= 0 && rate <= 5) {
			rateTotal+=rate;
			rateCount++;
		}
	}
	public double getRate() {
		if (rateTotal==0)
			return 0;
		return (double)rateTotal/(double)rateCount;
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

	public final static String DBTABLE = "plugin";
	public final static String[][] DBCOLUMNS = {
        {"id","long","id","Id"},
        {"pluginId","String","plugin_id","varchar"},
        {"name","String","name","varchar"},
        {"description","String","description","varchar"},
        {"javaclass","String","javaclass","varchar"},
        {"enabled","Boolean","enabled","tinyint(1)"},
        {"status","String","status","varchar"},
        {"modality","String","modality","varchar"},
        {"developer","String","developer","varchar"},
        {"documentation","String","documentation","varchar"},
        {"rateTotal","int","rateTotal","int"},
        {"rateCount","int","rateCount","int"},
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
