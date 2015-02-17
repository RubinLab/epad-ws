package edu.stanford.epad.epadws.models.dao;

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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import edu.stanford.epad.epadws.epaddb.DatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;

public abstract class AbstractDAO implements Serializable, Cloneable {
	abstract public String returnDBTABLE();
	abstract public String[][] returnDBCOLUMNS();
	abstract public long getId();
	abstract public void setCreatedTime(Date time);
	abstract public void setUpdateTime(Date time);
	
	public final int MAX_RECORDS = 1000;
	protected final EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
	
	public void insert() throws Exception
	{
		Date time = new Date();
		this.setCreatedTime(time);
		this.setUpdateTime(time);
		epadDatabaseOperations.insertDBObject(this, this.returnDBTABLE(), this.returnDBCOLUMNS());
	}
	
	public void update() throws Exception
	{
		this.setUpdateTime(new Date());
		epadDatabaseOperations.updateDBObject(this, returnDBTABLE(), returnDBCOLUMNS());
	}
	
	public void delete() throws Exception
	{
		epadDatabaseOperations.deleteDBObject(returnDBTABLE(), getId());
	}
	
	public AbstractDAO retrieve() throws Exception
	{
		return getObject("id = " + getId());
	}
	
	public void save() throws Exception
	{
		if (getId() <= 0)
			insert();
		else
			update();
	}

    public List getObjects(String criteria) throws Exception
    {
        return getObjects(criteria, 0, MAX_RECORDS);
    }

    public List getObjects(String criteria, int offset, int max) throws Exception
    {
        return epadDatabaseOperations.getDBObjects(this.getClass(), returnDBTABLE(), returnDBCOLUMNS(), criteria, offset, max, false);
    }

    public List getIDs(String criteria) throws Exception
    {
        return getIDs(criteria, 0, MAX_RECORDS);
    }

    public List getIDs(String criteria, int offset, int max) throws Exception
    {
        return epadDatabaseOperations.getDBIds(returnDBTABLE(), criteria, offset, max);
    }
    
	public int deleteObjects(String criteria) throws Exception
	{
		return epadDatabaseOperations.deleteDBObjects(returnDBTABLE(), criteria);
	}
        
    public AbstractDAO getObject(String criteria) throws Exception
    {
        List objects = getObjects(criteria);
        if (objects.size() == 1)
            return (AbstractDAO) objects.get(0);
        else if (objects.size() > 1)
            throw new Exception("More than one object found");
        else
            return null;
    }

	public int getCount(String criteria) throws Exception
	{
		return epadDatabaseOperations.getDBCount(returnDBTABLE(), criteria);
	}
        
    public static String toSQL(Object value)
    {
        return DatabaseUtils.toSQL(value);
    }
        
	public Object clone () 
		throws CloneNotSupportedException 
	{ 
		return super.clone(); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractDAO)
		{
			if (this.getId() == ((AbstractDAO) obj).getId())
				return true;
		}
		return super.equals(obj);
	}		
}
