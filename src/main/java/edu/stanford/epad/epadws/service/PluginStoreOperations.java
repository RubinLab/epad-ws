package edu.stanford.epad.epadws.service;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.net.ftp.FTPFile;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.FTPUtil;
import edu.stanford.epad.dtos.EPADPluginList;
import edu.stanford.epad.dtos.EPADPluginStore;

public class PluginStoreOperations {//cav
	private static final EPADLogger log = EPADLogger.getInstance();
	private EPADPluginList instancePluginList = new EPADPluginList() ;
	private FTPFile[] arrayftpFiles;
	private String tomcatPath = EPADConfig.tomcatLocation+"/webapps/";
	private String baseFilePath = System.getProperty("user.home")+"/DicomProxy/resources/pluginStore/";
	private String folderPluginStore = "/pluginStore/";
	private String Server="http://"+EPADConfig.dicomServerIP+":8080/epad/resources"+folderPluginStore;
	private String serverIp = "171.65.102.212";
	private String serverUser = "";
	private String serverUserPassword = "";
	
	
	public PluginStoreOperations () throws Exception{
		
		File file = new File(this.baseFilePath);
		if (!file.exists()) {
            System.out.print("No Folder");
            file.mkdir();
            System.out.print("Folder created");
        }
	}
	
	public boolean checkFolderExistance (String folderPath){
		File dir = new File(folderPath);
		
		if ((!dir.exists()) ) {
		   return false;
		}else{
			return true;
		}
	}
	
	public void installToTomcat (String javaClassJarWarName) throws Exception{
		if (checkFolderExistance(tomcatPath)==false)
			throw new Exception("Could not locate tomcat at " + tomcatPath );
		FTPUtil instanceFtp = new FTPUtil(serverIp, serverUser, serverUserPassword);
		this.arrayftpFiles = instanceFtp.getFiles("pluginStore");
	
        File output;
        output = new File( tomcatPath+javaClassJarWarName);
        instanceFtp.getFile(folderPluginStore+javaClassJarWarName, output);
		
	}
	public void deleteFromTomcat (String javaClassJarWarName) throws Exception{
		if (checkFolderExistance(tomcatPath)==false)
			throw new Exception("Could not locate tomcat at :" + tomcatPath );
		FTPUtil instanceFtp = new FTPUtil(serverIp, serverUser, serverUserPassword);
		this.arrayftpFiles = instanceFtp.getFiles("pluginStore");
        File output;
        output = new File( tomcatPath+javaClassJarWarName);
        output.delete();
	
	}
	public void getPluginStoreFileList () throws Exception{
		
		FTPUtil instanceFtp = new FTPUtil(serverIp, serverUser, serverUserPassword);
		this.arrayftpFiles = instanceFtp.getFiles("pluginStore");
		if (arrayftpFiles != null && arrayftpFiles.length > 0) {
   
            for (FTPFile file : arrayftpFiles) {
            	if ( (file.getName()).toLowerCase().endsWith("config.xml") || (file.getName()).toLowerCase().endsWith(".png") || (file.getName()).toLowerCase().endsWith(".jpg") ){
            		File output;
            		output = new File( baseFilePath + file.getName());
            		instanceFtp.getFile(folderPluginStore+file.getName(), output);
            	}
            }
        }
	}
	
	public boolean getPluginStoreFile (String varFileName) throws Exception{
		
		FTPUtil instanceFtp = new FTPUtil(serverIp, serverUser, serverUserPassword);
		this.arrayftpFiles = instanceFtp.getFiles("pluginStore");
		if (arrayftpFiles != null && arrayftpFiles.length > 0 && varFileName != "") {

              		 try { 
              			 
              			 	File output;
              			 	log.info("gettin jar file from remote server andsave to lacal " + varFileName);
              			 	output = new File( baseFilePath + varFileName);
              			 	instanceFtp.getFile(folderPluginStore+varFileName, output);
              			 	log.info("succesfuly downloaded " + varFileName);
              			 	return true;
              			 	
            		 } catch (Exception e) { 
            			 	
            			 	log.warning("Plugin file " + varFileName + " does not exist on the server");
            		    	return false;	    	
            		 }    
        }else{
        	return false;
        }		
	}
	public JSONObject readXml(File varFileName) throws ParserConfigurationException, SAXException, IOException{
		JSONObject obj = new JSONObject();	 
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File(this.baseFilePath+varFileName));
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("file");
	
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				if (temp==0){
					obj.put("pluginJar", eElement.getElementsByTagName("name").item(0).getTextContent());
					obj.put("pluginJarType", eElement.getElementsByTagName("type").item(0).getTextContent());
				}else if (temp==1){
					obj.put("pluginMathlabJar", eElement.getElementsByTagName("name").item(0).getTextContent());
					obj.put("pluginMathlabJarType", eElement.getElementsByTagName("type").item(0).getTextContent());
				}else{
					obj.put("pluginTemplateXml", eElement.getElementsByTagName("name").item(0).getTextContent());
					obj.put("pluginTemplateType", eElement.getElementsByTagName("type").item(0).getTextContent());
				}
		
			}
		}
		log.info("dpc res :"+ doc.getElementsByTagName("executable") );
		nList = doc.getElementsByTagName("executable");
		Node nNode = nList.item(0);
		Element eElement = (Element) nNode;
	
		obj.put("pluginId", eElement.getElementsByTagName("pluginid").item(0).getTextContent());
		obj.put("pluginName", eElement.getElementsByTagName("title").item(0).getTextContent());
		obj.put("pluginType", eElement.getElementsByTagName("plugintype").item(0).getTextContent());
		obj.put("className", eElement.getElementsByTagName("classname").item(0).getTextContent());
		obj.put("pluginCategory", eElement.getElementsByTagName("category").item(0).getTextContent());
		obj.put("pluginDescription", eElement.getElementsByTagName("description").item(0).getTextContent());
		if (eElement.getElementsByTagName("imagepath").item(0).getTextContent()=="")
			obj.put("pluginImagePath", Server+"blank.png");//	pluginImagePath = Server+"blank.png";
		else
			obj.put("pluginImagePath", Server+ eElement.getElementsByTagName("imagepath").item(0).getTextContent());//pluginImagePath = Server+ eElement.getElementsByTagName("imagepath").item(0).getTextContent();
		return obj;
		
	}
	public void readPropertiesRegisterPluginFromXml () throws Exception{
		
		File dir = new File(this.baseFilePath);
		File[] directoryListing = dir.listFiles();
		for (File eachFile : directoryListing) {
			
			if ((eachFile.getName()).endsWith("CONFIG.xml")){
				
				JSONObject xmlTags = readXml(new File(eachFile.getName()));
				EPADPluginStore instancePlugin = new EPADPluginStore(
						xmlTags.getString("pluginId"), 
						xmlTags.getString("pluginName"), 
						xmlTags.getString("pluginDescription"), 
						xmlTags.getString("pluginJar"),
						xmlTags.getString("pluginMathlabJar"), 
						checkIfPluginInstalled(xmlTags.getString("pluginId"),xmlTags.getString("pluginJarType"),xmlTags.getString("pluginJar")) ,
						xmlTags.getString("pluginImagePath"), 
						eachFile.getName());
				this.instancePluginList.addEPADPlugin(instancePlugin); 
			}
		}
	}
	
	public boolean checkIfPluginInstalled (String pluginId, String classType, String javaClassJarWarName) throws Exception{
		byte Check=0;
		if (classType.equals("tomcat")){
			File dir = new File(tomcatPath);			
			if ((!dir.exists()) && (!dir.isDirectory())) {
			   return false;
			}
			File[] directoryListing = dir.listFiles();
			for (File eachFile : directoryListing) {
				if (eachFile.getName().equals(javaClassJarWarName)){
					Check=1;
					break;
				}
			}
			if (Check==1){
				return true;
			}else{
				return false;
			}
		}else{
				if (PluginOperations.getInstance().getPlugin(pluginId) == null){
					return false;
				}else{ 
						if (PluginOperations.getInstance().getPlugin(pluginId).getEnabled()==true){
							return true;	
						}else {
							return false;
						}
				}
		}
	}
	
	public EPADPluginList getRegisteredPluginList (){
	
		return this.instancePluginList;
	}
	
	public void deletePluginFromStore(String pluginId,String username, String sessionID,String xmlPluginConfigFile) throws Exception{
		
		JSONObject xmlTags = readXml(new File(xmlPluginConfigFile));
		if (xmlTags.getString("pluginJarType").equals("tomcat")){
			deleteFromTomcat(xmlTags.getString("pluginJar"));
			log.info("*** deleting tomcat related plugin"+pluginId);
		}else{
			if (xmlTags.getString("pluginType").equals("local")){
				PluginOperations.getInstance().updatePlugin(username, pluginId, "", "", "", "false", "", "", "", "0", sessionID);
				log.info("*** deleting local plugin " +pluginId);
			}else{
				
				PluginOperations.getInstance().deletePlugin(username, pluginId);
				log.info("*** deleting remote plugin .... : "+pluginId);
				Path path =null;
				try {
					 path = Paths.get(EPADConfig.getEPADWebServerBaseDir()+"lib/plugins/"+xmlTags.getString("pluginJar"));
				    Files.delete(path);
				    path = Paths.get(EPADConfig.getEPADWebServerBaseDir()+"lib/plugins/"+xmlTags.getString("pluginMathlabJar"));
				    Files.delete(path);
					log.info("*** deleting necesary jar files from local folder for remote plugin .... : "+pluginId);
				} catch (NoSuchFileException x) {
				    System.err.format("%s: no such" + " file or directory%n", path);
				} catch (DirectoryNotEmptyException x) {
				    System.err.format("%s not empty%n", path);
				} catch (IOException x) {
				    System.err.println(x);
				}				
			}
		}	
	}
	
	public void installPluginFromStore (String pluginIdtoFind, String username, String sessionID, String xmlPluginConfigFile) throws Exception{
		JSONObject xmlTags = readXml(new File(xmlPluginConfigFile));
		if (xmlTags.getString("pluginJarType").equals("tomcat")){
			installToTomcat(xmlTags.getString("pluginJar"));
			log.info("*** installing tomcat related plugin : "+pluginIdtoFind);
		}else{
			
			if (xmlTags.getString("pluginType").equals("local")){
				PluginOperations.getInstance().updatePlugin(username, pluginIdtoFind, "", "", "", "true", "", "", "", "0", sessionID);
				log.info("*** installing local plugin : "+pluginIdtoFind);
			}else{
				
					if ( (xmlTags.getString("pluginJar") == null) || (xmlTags.getString("pluginJar")=="") )
						throw new Exception("pluginJar file property is empty ");
					else{
						if (getPluginStoreFile(xmlTags.getString("pluginJar"))!= true)
							throw new Exception("Plugin file plugin java class = " + xmlTags.getString("pluginJar") + " does not exist on the server");
							
						}
						
					if ( (xmlTags.getString("pluginMathlabJar") != null) && (xmlTags.getString("pluginMathlabJar")!="") )
						{
						if (getPluginStoreFile(xmlTags.getString("pluginMathlabJar"))!= true)
							throw new Exception("Plugin  mathlab file = " + xmlTags.getString("pluginMathlabJar") + " does not exist on the server");
								
						}
			
					if ( (xmlTags.getString("pluginTemplateXml") != null) && (xmlTags.getString("pluginTemplateXml")!="") )
						{
						if (getPluginStoreFile(xmlTags.getString("pluginTemplateXml"))!= true)
							throw new Exception("Plugin  template file = " + xmlTags.getString("pluginTemplateXml") + " does not exist on the server");
								
						}						

					/*jar,descxml,template,	classname,multiaim,	name,override,mathlabjar*/			
								try {
									 File file = new File(baseFilePath+xmlTags.getString("pluginMathlabJar"));
									 URL url = file.toURI().toURL();

									 URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
									 Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
									 method.setAccessible(true);
									 method.invoke(classLoader, url);
									 
									 file = new File(baseFilePath+xmlTags.getString("pluginJar"));
									 url = file.toURI().toURL();

									classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
									method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
									method.setAccessible(true);
									method.invoke(classLoader, url);
									
									PluginOperations po = new PluginOperations();
									String temp = null;
									String pluginName =null;
									try {
										
										String[] templateInfo=null;
										templateInfo=po.getTemplateCodeAndMeaning(baseFilePath+xmlTags.getString("pluginTemplateXml"));
										if (po.getTemplateWithCode(templateInfo[0])==null){
											temp = baseFilePath+xmlTags.getString("pluginTemplateXml");
											pluginName = xmlTags.getString("pluginName");
										}else{ 
											temp = baseFilePath+xmlTags.getString("pluginTemplateXml");
											pluginName = xmlTags.getString("pluginName");;
										}
									} catch (Exception e) {
										// TODO: handle exception
										temp = null;
									}
									String result=po.createLocalPlugin(
											baseFilePath+xmlTags.getString("pluginJar"), 
											null, 
											temp,
											xmlTags.getString("className"), 
											false, 
											pluginName, 
											false, 
											baseFilePath+xmlTags.getString("pluginMathlabJar"));
									log.info("*** Installing remote plugin :"+pluginIdtoFind+"  finished with code:  "+result);
									}catch (Exception x) {
									    log.info(x.toString());
									    log.info("*** Could not install plugin"+pluginIdtoFind);
									}
			}
		}
	}

}


