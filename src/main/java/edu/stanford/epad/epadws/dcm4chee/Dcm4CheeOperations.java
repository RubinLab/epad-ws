/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.dcm4chee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * Operations on Dcm4Chee (as distinct from operations directly on DCM4CHEE's database).
 * 
 * 
 * @author martin
 * @see Dcm4CheeDatabaseOperations
 */
public class Dcm4CheeOperations
{
	private static EPADLogger log = EPADLogger.getInstance();
	
	static final String[] scriptFiles = {
		"dcmsnd",
		"dcmdeleteSeries",
		"dcmdeleteStudy",
		"twiddle.sh",
		"dcm2txt",
	};
	
	static final String[] libFiles = {
		"dcm4che-tool-dcmsnd-2.0.24.jar",
		"dcm4che-tool-dcm2txt-2.0.24.jar",
		"dcm4che-core-2.0.24.jar",
		"dcm4che-net-2.0.24.jar",
		"slf4j-log4j12-1.6.1.jar",
		"slf4j-api-1.6.1.jar",
		"log4j-1.2.16.jar",
		"commons-cli-1.2.jar",
		"twiddle.jar",
		"jbossall-client.jar",
		"getopt.jar",
		"jboss-jmx.jar",
	};
	
	public static void checkScriptFiles()
	{
		try
		{
			File scriptDir = new File(EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/");
			if (!scriptDir.exists())
				scriptDir.mkdirs();
			for (String scriptFile: scriptFiles)
			{
				File file = new File(scriptDir, scriptFile);
				if (!file.exists()) {
					InputStream in = null;
					OutputStream out = null;
					try {
						in = new Dcm4CheeOperations().getClass().getClassLoader().getResourceAsStream("scripts/" + scriptFile);
			            out = new FileOutputStream(file);
	
			            // Transfer bytes from in to out
			            byte[] buf = new byte[1024];
			            int len;
			            while ((len = in.read(buf)) > 0)
			            {
			                    out.write(buf, 0, len);
			            }
					} catch (Exception x) {
						
					} finally {
			            IOUtils.closeQuietly(in);
			            IOUtils.closeQuietly(out);
					}
				}
				if (file.exists())
					file.setExecutable(true);
			}
			File libDir = new File(EPADConfig.getEPADWebServerDICOMScriptsDir() + "lib/");
			if (!libDir.exists())
				libDir.mkdirs();
			for (String libFile: libFiles)
			{
				File file = new File(libDir, libFile);
				if (!file.exists()) {
					InputStream in = null;
					OutputStream out = null;
					try {
						in = new Dcm4CheeOperations().getClass().getClassLoader().getResourceAsStream("scripts/" + libFile);
			            out = new FileOutputStream(file);
	
			            // Transfer bytes from in to out
			            byte[] buf = new byte[1024];
			            int len;
			            while ((len = in.read(buf)) > 0)
			            {
			                    out.write(buf, 0, len);
			            }
					} catch (Exception x) {
						
					} finally {
			            IOUtils.closeQuietly(in);
			            IOUtils.closeQuietly(out);
					}
				}
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public static boolean dcmsnd(File inputDirFile, boolean throwException) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileWriter tagFileWriter = null;
		boolean success = false;
		try {
			String aeTitle = EPADConfig.aeTitle;
			String dicomServerIP = EPADConfig.dicomServerIP;
			String dicomServerPort = EPADConfig.dicomServerPort;
			String dicomServerTitleAndPort = aeTitle + "@" + dicomServerIP + ":" + dicomServerPort;

			dicomServerTitleAndPort = dicomServerTitleAndPort.trim();

			String dirPath = inputDirFile.getAbsolutePath();
			if (pathContainsSpaces(dirPath))
				dirPath = escapeSpacesInDirPath(dirPath);

			File dir = new File(dirPath);
			int nbFiles = -1;
			if (dir != null) {
				String[] filePaths = dir.list();
				if (filePaths != null)
					nbFiles = filePaths.length;
			}
			log.info("./dcmsnd: sending " + nbFiles + " file(s) - command: ./dcmsnd " + dicomServerTitleAndPort + " "
					+ dirPath);

			String[] command = { "./dcmsnd", dicomServerTitleAndPort, dirPath };
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String dicomScriptsDir = EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/";
			File script = new File(dicomScriptsDir, "dcmsnd");
			if (!script.exists())
				dicomScriptsDir = EPADConfig.getEPADWebServerDICOMBinDir();
			script = new File(dicomScriptsDir, "dcmsnd");
			if (!script.exists())
				throw new Exception("No script found:" + script.getAbsolutePath());
			// Java 6 - Runtime.getRuntime().exec("chmod u+x "+script.getAbsolutePath());
			script.setExecutable(true);
			processBuilder.directory(new File(dicomScriptsDir));
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.info("./dcmsend output: " + line);
			}

			try {
				int exitValue = process.waitFor();
				log.info("DICOM send exit value is: " + exitValue);
				if (exitValue == 0) success = true;
			} catch (InterruptedException e) {
				log.warning("Didn't send DICOM files in: " + inputDirFile.getAbsolutePath(), e);
			}
			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error"))
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			return success;
		} catch (Exception e) {
			log.warning("DicomSendTask failed to send DICOM files", e);
			if (e instanceof IllegalStateException && throwException) {
				throw e;
			}
			if (throwException) {
				throw new IllegalStateException("DicomSendTask failed to send DICOM files", e);
			}
			return success;
		} catch (OutOfMemoryError oome) {
			log.warning("DicomSendTask out of memory: ", oome);
			if (throwException) {
				throw new IllegalStateException("DicomSendTask out of memory: ", oome);
			}
			return success;
		} finally {
			IOUtils.closeQuietly(tagFileWriter);
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * TODO This does not work. The ./dcmdeleteSeries script invoked the dcm4chee twiddle command but it appears that the
	 * moveSeriesToTrash operation it calls has no effect in this version of dcm4chee. See:
	 * http://www.dcm4che.org/jira/browse/WEB-955
	 */
	public static boolean deleteSeries(String seriesUID, String seriesPk)
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		boolean success = false;

		try {
			log.info("Deleting series " + seriesUID + " seriesPK:" + seriesPk);

			String[] command = { "./dcmdeleteSeries", seriesPk, EPADConfig.xnatUploadProjectPassword };
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String dicomScriptsDir = EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/";
			File script = new File(dicomScriptsDir, "dcmdeleteSeries");
			if (!script.exists())
				dicomScriptsDir = EPADConfig.getEPADWebServerMyScriptsDir();
			script = new File(dicomScriptsDir, "dcmdeleteSeries");
			// Java 6 - Runtime.getRuntime().exec("chmod u+x "+script.getAbsolutePath());
			script.setExecutable(true);
			processBuilder.directory(new File(dicomScriptsDir));
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.info("./dcmdeleteSeries: " + line);
			}
			try {
				int exitValue = process.waitFor();
				if (exitValue == 0) {
					log.info("Deleted DICOM series " + seriesUID + " pk:" + seriesPk);
					success = true;
				} else {
					log.warning("Failed to delete DICOM series " + seriesUID + " pk=" + seriesPk + "; exitValue=" + exitValue + "\n" + sb.toString());
				}
			} catch (Exception e) {
				log.warning("Failed to delete DICOM series " + seriesUID, e);
			}

			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (IOException e) {
			log.warning("Failed to delete DICOM series " + seriesUID, e);
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
		}
		return success;
	}

	public static boolean deleteStudy(String studyUID)
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		boolean success = false;
		try {
			log.info("Deleting study " + studyUID + " files - command: ./dcmdeleteStudy " + studyUID);

			String[] command = { "./dcmdeleteStudy", studyUID };

			ProcessBuilder pb = new ProcessBuilder(command);
			String dicomScriptsDir = EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/";
			File script = new File(dicomScriptsDir, "dcmdeleteStudy");
			if (!script.exists())
				dicomScriptsDir = EPADConfig.getEPADWebServerMyScriptsDir();
			script = new File(dicomScriptsDir, "dcmdeleteStudy");
			// Java 6 - Runtime.getRuntime().exec("chmod u+x "+script.getAbsolutePath());
			script.setExecutable(true);
			pb.directory(new File(dicomScriptsDir));

			Process process = pb.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				log.info("./dcmdeleteStudy: " + line);
				sb.append(line).append("\n");
			}

			try {
				int exitValue = process.waitFor();
				log.info("DICOM delete study exit value is: " + exitValue);
				if (exitValue == 0) success = true;
			} catch (Exception e) {
				log.warning("Failed to delete DICOM study " + studyUID, e);
			}
			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (Exception e) {
			log.warning("Failed to delete DICOM study " + studyUID, e);
		}
		return success;
	}

	private static String parseError(String output)
	{
		String result = "";
		try {
			String[] lines = output.split("\n");
			for (String currLine : lines) {
				if (currLine.toLowerCase().contains("error")) {
					result = result + currLine;
				}
			}
		} catch (Exception e) {
			log.warning("DicomSendTask.parseError had: " + e.getMessage() + " for: " + output, e);
		}
		if (result.length() > 0)
			return result;
		else
			return output;
	}

	/**
	 * 
	 * @param dirPath String
	 * @return boolean true if this path contains spaces.
	 */
	private static boolean pathContainsSpaces(String dirPath)
	{
		return dirPath.indexOf(' ') > 0;
	}

	/**
	 * 
	 * @param path String
	 * @return String
	 */
	private static String escapeSpacesInDirPath(String path)
	{
		StringBuilder sb = new StringBuilder();
		String[] parts = path.split(" ");
		boolean isFirst = true;
		for (String part : parts) {
			if (!isFirst) {
				sb.append("\\ ");
			}
			sb.append(part);
			isFirst = false;
		}
		return sb.toString();
	}
}
