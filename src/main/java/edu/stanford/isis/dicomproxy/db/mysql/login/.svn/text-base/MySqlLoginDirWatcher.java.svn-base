package edu.stanford.isis.dicomproxy.db.mysql.login;

import edu.stanford.isis.dicomproxy.common.FileKey;
import edu.stanford.isis.dicomproxy.common.ProxyFileUtils;
import edu.stanford.isis.dicomproxy.db.mysql.MySqlInstance;
import edu.stanford.isis.dicomproxy.db.mysql.MySqlQueries;
import edu.stanford.isis.dicomproxy.db.mysql.pipeline.DicomSendTask;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import edu.stanford.isis.dicomproxy.server.ShutdownSignal;
import edu.stanford.isis.dicomproxy.server.managers.pipeline.UploadFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

/**
 * Watches for the login file. When a new login file is
 * found it creates a user entry into the database
 *
 * @author ckurtz
 */
public class MySqlLoginDirWatcher implements Runnable
{

	public static final String LOGIN_ROOT_DIR = "../etc/login/";
	public static final int CHECK_INTERVAL = 120000; //check every 2 minutes.

	public static final String FOUND_DIR_FILE = "dir.found";
	private static final long MAX_WAIT_TIME = 120000; //in milliseconds

	ProxyLogger log = ProxyLogger.getInstance();

	@Override
	public void run() {
		try{
			ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
			while(true){
				if(shutdownSignal.hasShutdown()){
					return;
				}

				File rootDir = new File(LOGIN_ROOT_DIR);
				try{
					List<File> newUserList = findNewUsers(rootDir);
					if(newUserList!=null){
						for(File currUserFile : newUserList){
							processUser(currUserFile);
						}
					}
				}catch(ConcurrentModificationException cme){
					log.warning("Login Thread had: ",cme);
				}

				if(shutdownSignal.hasShutdown()){
					return;
				}
				TimeUnit.MILLISECONDS.sleep(CHECK_INTERVAL);
			}//while


		}catch(Exception e){
			log.sever("UploadDirWatcher error.", e);
		}finally{
			log.info("Done. UploadDirWatcher thread.");
		}
	}

	//looks for new users files.
	private List<File> findNewUsers(File dir)
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();

		//For the return
		List<File> retVal = new ArrayList<File>();

		//Extract all the names of the existing user from the database
		List<String> existingUsers = queries.getAllUsers();

		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.getName().endsWith(".properties");
			}
		};

		File[] allFiles = dir.listFiles(fileFilter);
		if (allFiles == null) {
			System.out.println("Directory is a empty Directory");
		} else {
			//for each file, we check the validity of the user file
			for(File currFile : allFiles){
				//Check the validity of the file
				if(isValidUserFile(currFile) 
						//Check if user already exists in the db
						&& !existingUsers.contains(extractUserName(currFile))
				){
					retVal.add(currFile);
				}
			}//for
		}

		return retVal;
	}//findNewDir


	private boolean isValidUserFile(File currFile) {
		boolean isValid=false;

		if(currFile!=null && currFile.isFile()){
			//Check the name of the file
			String rawName = stripExtension(currFile.getName());
			String [] parts=rawName.split("_");
			if(parts!=null && parts.length==3){
				String name = parts[0];
				String software = parts[1];
				String email= parts[2];

				if(software.equals("epad") && name.length()>=1 && email.contains("@")){	
					isValid=true;
				}
			}
		}

		return isValid;
	}

	private String extractUserName(File currFile) {
		String userName=null;

		if(currFile!=null && currFile.isFile()){
			//Check the name of the file
			String rawName = stripExtension(currFile.getName());
			String [] parts=rawName.split("_");
			if(parts!=null && parts.length==3){
				String name = parts[0];

				if(name.length()>=1){	
					userName=name;
				}
			}
		}

		return userName;
	}

	public static String stripExtension(String str) {
		// Handle null case specially.
		if (str == null) 
			return null;

		// Get position of last '.'.
		int pos = str.lastIndexOf(".");

		// If there wasn't any '.' just return the string as is.
		if (pos == -1) return str;

		// Otherwise return the string, up to the dot.
		return str.substring(0, pos);
	}

	private void processUser(File f)
	throws InterruptedException
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();

		if(f!=null){
			String userName=extractUserName(f);
			//Extract the content of the file
			try {
				TreeMap<String, String> content = extractContentFromUserFile(f);
				//Check validity of file content
				if(content.containsKey("email")
						&& content.containsKey("password")
						&& content.containsKey("expirationdate")
						&& content.containsKey("userrole")
						&& content.containsKey("username")){
					
					//Insert the user into the database
					String email=content.get("email");
					String password=  content.get("password");
					String expirationdate= content.get("expirationdate");
					String userrole = content.get("userrole");
					String username =content.get("username");

					queries.insertUserInDb(username,email,password,expirationdate,userrole);

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(Exception e){
				log.warning("Failed to extract file content for "+userName+": "+e.getMessage(),e);
			}


		}

	}

	private static boolean isCommentLine(String line){
		line = line.trim();
		if(line.startsWith("//"))return true;
		if(line.startsWith("#"))return true;
		if(line.startsWith("*"))return true;

		//count a blank line as a comment.
		if("".equals(line))return true;

		return false;
	}

	private TreeMap<String, String> extractContentFromUserFile(File f) throws IOException {

		TreeMap<String, String> content = new TreeMap<String, String>();

		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while ((line = br.readLine()) != null) {
			// process the line.
			line = line.trim();
			if( !isCommentLine(line) ){
				//add this to the list of content.
				String[] linePart =line.split("=");
				if(linePart.length!=2){
					log.info("Wrong line from user login file: "+line);
					content.put(linePart[0],"");
				}else{
					content.put(linePart[0],linePart[1]);
				}
			}
		}
		br.close();

		return content;
	}


}
