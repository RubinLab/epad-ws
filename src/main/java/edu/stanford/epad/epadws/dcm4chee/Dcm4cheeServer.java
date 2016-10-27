package edu.stanford.epad.epadws.dcm4chee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.VersionInfo;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;

public class Dcm4cheeServer {
	protected static final EPADLogger log = EPADLogger.getInstance();
	private String dcmUser;
	private String dcmPassword;
	private String dcmHostname;
	private Short dcmPort;
	
	HttpClient httpclient;
	HttpPost httppost;
	List<NameValuePair> params=new ArrayList<NameValuePair>(2);

	HttpResponse response;
	HttpEntity entity;
	public void connect(String dcmUser, String dcmPassword, String dcmHostname, Short dcmPort) throws ClientProtocolException, IOException {
		
		this.dcmUser = dcmUser;
		this.dcmPassword = dcmPassword;
		this.dcmHostname = dcmHostname;
		this.dcmPort = dcmPort;
		this.httpclient = HttpClients.createDefault();
		this.httppost = new HttpPost("http://"+this.dcmUser+":"+this.dcmPassword+"@"+this.dcmHostname+":"+this.dcmPort+"/jmx-console/HtmlAdaptor");

	}
	
	public String addAetitle(String aetTitleName, String hostName , String hostPortNo) throws ClientProtocolException, IOException{
		this.params.removeAll(this.params);
		System.out.println(this.params.size());
		this.params = new ArrayList<NameValuePair>(2);
		this.params.add(new BasicNameValuePair("action", "invokeOp"));
		this.params.add(new BasicNameValuePair("name", "dcm4chee.archive:service=AE"));
		this.params.add(new BasicNameValuePair("methodIndex", "5"));
		this.params.add(new BasicNameValuePair("arg0", aetTitleName));
		this.params.add(new BasicNameValuePair("arg1", hostName));
		this.params.add(new BasicNameValuePair("arg2", hostPortNo));
		this.params.add(new BasicNameValuePair("arg3", ""));
		this.params.add(new BasicNameValuePair("arg4", ""));
		this.params.add(new BasicNameValuePair("arg5", ""));
		this.params.add(new BasicNameValuePair("arg6", ""));
		this.params.add(new BasicNameValuePair("arg7", ""));
		this.params.add(new BasicNameValuePair("arg8", ""));
		this.params.add(new BasicNameValuePair("arg9", ""));
		this.params.add(new BasicNameValuePair("arg10", ""));
		this.params.add(new BasicNameValuePair("arg11", ""));
		this.params.add(new BasicNameValuePair("arg12", ""));
		this.params.add(new BasicNameValuePair("arg13", ""));
		this.params.add(new BasicNameValuePair("arg14", ""));
		this.params.add(new BasicNameValuePair("arg15", "True"));
		this.params.add(new BasicNameValuePair("arg16", "False"));
		this.params.add(new BasicNameValuePair("submit", "Invoke"));
		System.out.println(this.params.size());
		executeHttprequest();
		return getError();
		
	}
	
	public Vector<String[]> listAetitle() throws IOException{
		this.params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("action", "invokeOp"));
		params.add(new BasicNameValuePair("name", "dcm4chee.archive:service=AE"));
		params.add(new BasicNameValuePair("methodIndex", "0"));
		this.params.add(new BasicNameValuePair("submit", "Invoke"));
		executeHttprequest();
		return getAelist();
		
		
	}
	
	public String updateAetitle(String fromAetName , String toAetName) throws ClientProtocolException, IOException{
		this.params.removeAll(this.params);
		System.out.println(this.params.size());
		this.params = new ArrayList<NameValuePair>(2);
		this.params.add(new BasicNameValuePair("action", "invokeOp"));
		this.params.add(new BasicNameValuePair("name", "dcm4chee.archive:service=AE"));
		this.params.add(new BasicNameValuePair("methodIndex", "7"));
		this.params.add(new BasicNameValuePair("arg0", fromAetName));
		this.params.add(new BasicNameValuePair("arg1", toAetName));	
		this.params.add(new BasicNameValuePair("submit", "Invoke"));
		System.out.println(this.params.size());
		executeHttprequest();
		return getError();
		
		
	}
	
	public String editAetConfig(String oldhostName , String newTitle , String newHostName , String newPort) throws ClientProtocolException, IOException{
		//sqlConnection(hostName);
		this.params.removeAll(this.params);
		System.out.println(this.params.size());
		this.params = new ArrayList<NameValuePair>(2);
		this.params.add(new BasicNameValuePair("action", "invokeOp"));
		this.params.add(new BasicNameValuePair("name", "dcm4chee.archive:service=AE"));
		this.params.add(new BasicNameValuePair("methodIndex", "4"));
		this.params.add(new BasicNameValuePair("arg0", sqlConnection(oldhostName)));
		this.params.add(new BasicNameValuePair("arg1", newTitle));
		this.params.add(new BasicNameValuePair("arg2", newHostName));
		this.params.add(new BasicNameValuePair("arg3", newPort));
		this.params.add(new BasicNameValuePair("arg4", ""));
		this.params.add(new BasicNameValuePair("arg5", ""));
		this.params.add(new BasicNameValuePair("arg6", ""));
		this.params.add(new BasicNameValuePair("arg7", ""));
		this.params.add(new BasicNameValuePair("arg8", ""));
		this.params.add(new BasicNameValuePair("arg9", ""));
		this.params.add(new BasicNameValuePair("arg10", ""));
		this.params.add(new BasicNameValuePair("arg11", ""));
		this.params.add(new BasicNameValuePair("arg12", ""));
		this.params.add(new BasicNameValuePair("arg13", ""));
		this.params.add(new BasicNameValuePair("arg14", ""));
		this.params.add(new BasicNameValuePair("arg15", ""));
		this.params.add(new BasicNameValuePair("arg16", "True"));
		this.params.add(new BasicNameValuePair("arg17", "False"));
		this.params.add(new BasicNameValuePair("submit", "Invoke"));
		System.out.println(this.params.size());
		executeHttprequest();
		return getError();
		
	}
	
	public String deleteAetitle(String title) throws ClientProtocolException, IOException{
		this.params.removeAll(this.params);
		System.out.println(this.params.size());
		this.params = new ArrayList<NameValuePair>(2);
		this.params.add(new BasicNameValuePair("action", "invokeOp"));
		this.params.add(new BasicNameValuePair("name", "dcm4chee.archive:service=AE"));
		this.params.add(new BasicNameValuePair("methodIndex", "6"));
		this.params.add(new BasicNameValuePair("arg0", title));
		
		this.params.add(new BasicNameValuePair("submit", "Invoke"));
		System.out.println(this.params.size());
		executeHttprequest();
		return getError();

		
	}
	
	public void executeHttprequest() throws ClientProtocolException, IOException{
		
		this.httppost.setEntity(new UrlEncodedFormEntity(this.params, "UTF-8"));
		this.response = httpclient.execute(this.httppost);
		this.entity = response.getEntity();
	}
	
	public Vector<String[]>  getAelist() throws IOException {
		//BufferedWriter bw = new BufferedWriter(new FileWriter(new File("Filepath")));
		if (this.entity != null) {
		
			Vector<String[]> connectionVector = new Vector<String[]>();
		    InputStream in = this.entity.getContent();
		    String str = "";
		    
		    try {
		    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		    	StringBuilder result = new StringBuilder();
		    	String line;
		    	Boolean Control= false;
		    	while((line = reader.readLine()) != null) {
		    		System.out.println(line);
		    		if (contains(line,"<span class='OpResult'>")){
		    			Control = true;
		    		}
		    		if (Control == true){
		    			str= str + line ;
		    		}
		    	    result.append(line);
		    	    //bw.write(line);
		    	}
		    	
		    } finally {
		     in.close();
		     //bw.close();
		    }
		    System.out.println("str =================="+str);
		    String delims= "[\\[\\]]+";
		    String[] tokens = str.split(delims);
	    	
		    String allhosts = tokens[1];
		    delims = "[,]";
		    String delimsa = "[:\\//]+";
		    String[] tokensLine = allhosts.split(delims);
		    for (int i=0;i<tokensLine.length;i++){
		    	System.out.println(tokensLine[i]);
		    	String[] tokensLinea = tokensLine[i].split(delimsa);
		    	String[] connectionArray = new String[4];
		    	 for (int k=0;k<tokensLinea.length;k++){
		    		 
		    		
		    		 if (k==1){
		    			 // dcm name and hostname
		    			 String[] tokensLineab = tokensLinea[k].split("@",2);
		    			 for (int t=0;t<tokensLineab.length;t++){
		    				 if (t==0){
		    					 System.out.println("connection name : "+tokensLineab[t]);
		    					 connectionArray[1]=tokensLineab[t];
		    				 }else{
		    					 System.out.println("host name : "+tokensLineab[t]);
		    					 connectionArray[2]=tokensLineab[t];
		    				 }
		    				 
		    				 
		    			 }
		    		 }else if (k==2){
		    			 //port number
		    			 System.out.println("port "+tokensLinea[k]);
		    			 connectionArray[3]=tokensLinea[k];
		    		 }else{
		    			 //host type k==0
		    			 System.out.println("host type"+tokensLinea[k]);
		    			 connectionArray[0]=tokensLinea[k];
		    		 }
		    		 
		    	 }
		    	 connectionVector.add(connectionArray);
		    }
		   
		    return connectionVector;
		}else
		return null;
		
	}
	//if returns null means there is no error else error string
	public String getError() throws IOException {
		//BufferedWriter bw = new BufferedWriter(new FileWriter(new File("Filepath")));
		//Vector<String[]> connectionVector = new Vector<String[]>();
	    InputStream in = this.entity.getContent();
	    String str = "";
	    Boolean Control= false;
	    try {
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    	StringBuilder result = new StringBuilder();
	    	String line;
	    	
	    	while((line = reader.readLine()) != null) {
	    	//	System.out.println(line);
	    		if (contains(line,"Error")){
	    			Control = true;
	    		}
	    		//if (Control == true){
	    			str= str + line ;
	    		//}
	    	    result.append(line);
	    	    //bw.write(line);
	    	}
	    	
	    } finally {
	     in.close();
	     //bw.close();
	    }
		if (Control == true){
		    String[] tokens = str.split("Exception report");
		    String[] tokensa = tokens[1].split("logs.");
		    log.info(tokensa[0]+"logs.");
		    return tokensa[0]+"logs."	;
		}
		else{
			return "null";
		}
	}
	
	public boolean contains( String mainword, String lokingfor ) {
		if(mainword.toLowerCase().indexOf(lokingfor.toLowerCase()) != -1){
	           return true;
	       }else{
	           return false;
	       }

	
		}
	
	public String sqlConnection (String oldHostName){
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String id="0";
        String url = EPADConfig.dcm4CheeDatabaseURL; // do not use localhost "jdbc:mysql://localhost:3306/pacsdb";
        String user = "pacs";
        String password = "pacs";

        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT * from pacsdb.ae where hostname =\""+oldHostName+"\"");

            while(rs.next()) {
                System.out.println(rs.getString(1));
                id=(rs.getString(1));
            }
            
            
            
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(VersionInfo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                //Logger lgr = Logger.getLogger(Version.class.getName());
                //lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        System.out.println("host id ="+id);
		return id;
	}

	public String getDcmHostname() {
		return dcmHostname;
	}

	public void setDcmHostname(String dcmHostname) {
		this.dcmHostname = dcmHostname;
	}

	/**
	 * @return the dcmUser
	 */
	public String getDcmUser() {
		return dcmUser;
	}

	/**
	 * @param dcmUser the dcmUser to set
	 */
	public void setDcmUser(String dcmUser) {
		this.dcmUser = dcmUser;
	}

	/**
	 * @return the dcmPassword
	 */
	public String getDcmPassword() {
		return dcmPassword;
	}

	/**
	 * @param dcmPassword the dcmPassword to set
	 */
	public void setDcmPassword(String dcmPassword) {
		this.dcmPassword = dcmPassword;
	}
	

}
