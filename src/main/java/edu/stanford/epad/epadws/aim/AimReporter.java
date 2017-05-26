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

package edu.stanford.epad.epadws.aim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.RecistReport;
import edu.stanford.epad.dtos.RecistReportUIDCell;
import edu.stanford.epad.dtos.WaterfallReport;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.hakan.aim4api.base.AimException;
import edu.stanford.hakan.aim4api.base.CalculationEntity;
import edu.stanford.hakan.aim4api.base.DicomImageReferenceEntity;
import edu.stanford.hakan.aim4api.base.ExtendedCalculationResult;
import edu.stanford.hakan.aim4api.base.ImageAnnotationCollection;
import edu.stanford.hakan.aim4api.base.ImagingObservationCharacteristic;
import edu.stanford.hakan.aim4api.base.ImagingObservationEntity;
import edu.stanford.hakan.aim4api.base.ImagingPhysicalEntity;
import edu.stanford.hakan.aim4api.base.Scale;
import edu.stanford.hakan.aim4api.usage.AnnotationGetter;

public class AimReporter {
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final String xsdFilePathV4 = EPADConfig.xsdFilePathV4;
	
	/**
	 * Fills in a table of String values traversing through input aim files looking for the input columns
	 * the columns can be Name, StudyDate
	 * or any value stored in a ImagingObservationEntity, ImagingObservationEntityCharacteristic, ImagingPhysicalEntity or CalculationEntity
	 * label is matched to the column in all but calculation (for calculation description)
	 * value and code (if exists) is returned as a json object 
	 * @param aims
	 * @param templatecode
	 * @param columns
	 * @return a json array in string format. json array contains a json object for each aim with column names as attributes
	 */
	public static String fillTable(EPADAIMList aims,String templatecode, String[] columns){
		
		String [][] table=null;
		if (aims.ResultSet.totalRecords==0) return null;
		table=new String[aims.ResultSet.totalRecords][columns.length];
		int row=0;
		//for each aim. find the item that contains the label to match and return the value (and/or code?)
		for (EPADAIM aim:aims.ResultSet.Result) {
			ImageAnnotationCollection iac=null;
			Map<String,String> values=new HashMap<>();
			for (int i=0;i<columns.length;i++) {
				values.put(columns[i],"");
			}
			try {
				iac = AnnotationGetter.getImageAnnotationCollectionFromString(aim.xml, xsdFilePathV4);
				if (iac!=null) {
					edu.stanford.hakan.aim4api.base.ImageAnnotation ia= iac.getImageAnnotation();
					//check the template
					if (templatecode!=null) {
						if (!ia.getListTypeCode().get(0).getCode().equalsIgnoreCase(templatecode)) {
							log.warning("Aim template is "+ia.getListTypeCode().get(0).getCode() + " was looking for "+templatecode);
							table[row++]=null;
							continue;
						}
					}
					if (values.containsKey("StudyDate")) {
						try{
							values.put("StudyDate", formJsonObj(((DicomImageReferenceEntity)ia.getImageReferenceEntityCollection().get(0)).getImageStudy().getStartDate()));
						}catch(Exception e){
							log.warning("The value for StudyDate couldn't be retrieved ", e);
						}
					}
					if (values.containsKey("Name")) {
						try{
							values.put("Name", formJsonObj(ia.getName().getValue()));
						}catch(Exception e){
							log.warning("The value for Name couldn't be retrieved ", e);
						}
					}
					if (values.containsKey("StudyUID")) {
						try{
							values.put("StudyUID", formJsonObj(((DicomImageReferenceEntity)ia.getImageReferenceEntityCollection().get(0)).getImageStudy().getInstanceUid().getRoot()));
						}catch(Exception e){
							log.warning("The value for StudyUID couldn't be retrieved ", e);
						}
					}
					if (values.containsKey("SeriesUID")) {
						try{
							values.put("SeriesUID", formJsonObj(((DicomImageReferenceEntity)ia.getImageReferenceEntityCollection().get(0)).getImageStudy().getImageSeries().getInstanceUid().getRoot()));
						}catch(Exception e){
							log.warning("The value for SeriesUID couldn't be retrieved ", e);
						}
					}
					if (values.containsKey("AimUID")) {
						try{
							values.put("AimUID", formJsonObj(iac.getUniqueIdentifier().getRoot()));
						}catch(Exception e){
							log.warning("The value for AimUID couldn't be retrieved ", e);
						}
					}
					
					//look through observation entities
					if (ia.getImagingObservationEntityCollection()!=null){
						for (ImagingObservationEntity ob: ia.getImagingObservationEntityCollection().getImagingObservationEntityList()){
							
							if (values.containsKey(ob.getLabel().getValue())) { //key exists put the value
								values.put(ob.getLabel().getValue(), formJsonObj(ob.getListTypeCode().get(0).getDisplayName().getValue(),ob.getListTypeCode().get(0).getCode()));

							}
							//look through observation characteristics
							if (ob.getImagingObservationCharacteristicCollection()!=null) {
								for (ImagingObservationCharacteristic obChar: ob.getImagingObservationCharacteristicCollection().getImagingObservationCharacteristicList()){
									if (values.containsKey(obChar.getLabel().getValue())) { //key exists put the value
										//if it has a quantification put that
										if (obChar.getCharacteristicQuantificationCollection().size()>0){
											Scale sq=(Scale)obChar.getCharacteristicQuantificationCollection().get(0);
											values.put(obChar.getLabel().getValue(), formJsonObj(sq.getValue().getValue(),obChar.getListTypeCode().get(0).getCode()));
										} else
											values.put(obChar.getLabel().getValue(), formJsonObj(obChar.getListTypeCode().get(0).getDisplayName().getValue(),obChar.getListTypeCode().get(0).getCode()));
									}
								}
							}
						}
					}
					
					//look through physical entities
					if (ia.getImagingPhysicalEntityCollection()!=null){
						for (ImagingPhysicalEntity phy: ia.getImagingPhysicalEntityCollection().getImagingPhysicalEntityList()){
							
							if (values.containsKey(phy.getLabel().getValue())) { //key exists put the value
								values.put(phy.getLabel().getValue(), formJsonObj(phy.getListTypeCode().get(0).getDisplayName().getValue(),phy.getListTypeCode().get(0).getCode()));

							}
						}
					}
					//look through calculation entities
					if (ia.getCalculationEntityCollection()!=null){
						for (CalculationEntity cal: ia.getCalculationEntityCollection().getCalculationEntityList()){
							
							if (values.containsKey(cal.getDescription().getValue())) { //key exists put the value
								try {
									String value=((ExtendedCalculationResult)cal.getCalculationResultCollection().getCalculationResultList().get(0)).getCalculationDataCollection().get(0).getValue().getValue();
									values.put(cal.getDescription().getValue(), formJsonObj(value,cal.getListTypeCode().get(0).getCode()));
								}catch(Exception e) {
									log.warning("The value for "+cal.getDescription().getValue() + " couldn't be retrieved ", e);
								}
							}
						}
					}
				}
				String[] strValues=new String[columns.length];
				for (int i=0;i<columns.length;i++) {
					strValues[i]="\""+columns[i]+"\":"+values.get(columns[i]);
					
				}
				table[row++]=strValues;
				
				
			} catch (AimException e) {
				log.info("Aim exception getting the aim from string " + e.getMessage());
			}
		}
		
		StringBuilder tableJson=new StringBuilder();
		tableJson.append("[");
		
		for (int i=0;i<table.length;i++){
			if (table[i]==null) 
				continue;
			tableJson.append("{");
			for (int j = 0; j < table[i].length; j++) {
				tableJson.append(table[i][j]);
				if (j!=table[i].length-1)
					tableJson.append(",");
			}
			tableJson.append("}");
			if (i!=table.length-1)
				tableJson.append(",");
		}
		tableJson.append("]");
//		log.info("The produced string is "+tableJson.toString());
		return tableJson.toString();
	}
	
	public static String formJsonObj(String value) {
		return "{\"value\":\""+value+"\"}";
	}
	public static String formJsonObj(String value, String code) {
		return "{\"value\":\""+value+"\",\"code\":\""+code+"\"}";
	}
	
	private static JSONArray concatArray(JSONArray... arrs)
	        throws JSONException {
	    JSONArray result = new JSONArray();
	    for (JSONArray arr : arrs) {
	        for (int i = 0; i < arr.length(); i++) {
	            result.put(arr.get(i));
	        }
	    }
	    return result;
	}
	
	/**
	 * creates a recist report object from the input aims list
	 * @param aims
	 * @return
	 */
	public static RecistReport getRecist(EPADAIMList aims){
		String table=AimReporter.fillTable(aims,"RECIST",new String[]{"Name","StudyDate","Lesion","Type", "Location","Length","StudyUID","SeriesUID","AimUID"});
		//get and append recist_mint records
		String tableMint=AimReporter.fillTable(aims,"RECIST_MINT",new String[]{"Name","StudyDate","Timepoint","Type", "Location","Length","StudyUID","SeriesUID","AimUID"});
		
		if ((table==null || table.isEmpty()) && (tableMint==null || tableMint.isEmpty())) 
			return null;
		JSONArray lesions;
		try{
			lesions=new JSONArray(table);
			JSONArray lesionsMint=new JSONArray(tableMint);
			log.info("lesions len "+ lesions.length()+ " lesionsmint len "+ lesionsMint.length());
			lesions=concatArray(lesions,lesionsMint);
		}catch(Exception e) {
			log.warning("couldn't parse json for "+table);
			return null;
		}
		//get targets
		ArrayList<String> tLesionNames=new ArrayList<>();
		ArrayList<String> tStudyDates=new ArrayList<>();
		ArrayList<String> ntLesionNames=new ArrayList<>();
		ArrayList<String> ntStudyDates=new ArrayList<>();
		ArrayList<String> targetTypes=new ArrayList<>();
		ArrayList<String> tNewLesionStudyDates=new ArrayList<>();
		Integer[] tTimepoints=null;
		
		targetTypes.add("target");
		targetTypes.add("target lesion"); //for new recist mint template
		targetTypes.add("new lesion");
		targetTypes.add("resolved lesion");
		//first pass fill in the lesion names and study dates (x and y axis of the table)
		for (int i = 0; i < lesions.length(); i++)
		{
			String lesionName = ((JSONObject)((JSONObject)lesions.get(i)).get("Name")).getString("value");
			String studyDate = ((JSONObject)((JSONObject)lesions.get(i)).get("StudyDate")).getString("value");
			String type=((JSONObject)((JSONObject)lesions.get(i)).get("Type")).getString("value");
			if (targetTypes.contains(type.toLowerCase())) {
				if (!tLesionNames.contains(lesionName))
					tLesionNames.add(lesionName);
				if (!tStudyDates.contains(studyDate))
					tStudyDates.add(studyDate);
				if (type.equalsIgnoreCase("new lesion") && !tNewLesionStudyDates.contains(studyDate)) {
					tNewLesionStudyDates.add(studyDate);
				}
			}else {
				if (!ntLesionNames.contains(lesionName))
					ntLesionNames.add(lesionName);
				if (!ntStudyDates.contains(studyDate))
					ntStudyDates.add(studyDate);
			}
		}
		//sort lists
		Collections.sort(tLesionNames);
		Collections.sort(tStudyDates);
		Collections.sort(ntLesionNames);
		Collections.sort(ntStudyDates);
		
		if (!tLesionNames.isEmpty() && !tStudyDates.isEmpty()){
			//fill in the table for target lesions
			if (tTimepoints==null)
				tTimepoints=new Integer[tStudyDates.size()];
			RecistReportUIDCell[][] tUIDs=new RecistReportUIDCell[tLesionNames.size()][tStudyDates.size()];
			String [][] tTable=fillRecistTable(tLesionNames, tStudyDates, lesions, targetTypes,tTimepoints, tUIDs);
			//calculate the sums first
			Double[] tSums=calcSums(tTable);
			//calculate the rrs
			Double[] tRRBaseline=calcRRBaseline(tSums);
			Double[] tRRMin=calcRRMin(tSums);
			Double[] tRR=calcRR(tSums, tTimepoints);
			Boolean[] isThereNewLesion=new Boolean[tStudyDates.size()];
			if (!tNewLesionStudyDates.isEmpty()) {
				for (String studyDate:tNewLesionStudyDates)
					isThereNewLesion[tStudyDates.indexOf(studyDate)]=true;
			}
			
			
			
			String[] responseCats=calcResponseCat(tRR,tTimepoints, isThereNewLesion);
			
			if (!ntLesionNames.isEmpty() && !ntStudyDates.isEmpty()){
				//fill in the table for non-target lesions
				ArrayList<String> nonTargetTypes=new ArrayList<>();
				nonTargetTypes.add("non-target");
				Integer[] ntTimepoints=new Integer[ntStudyDates.size()];
				RecistReportUIDCell[][] ntUIDs=new RecistReportUIDCell[ntLesionNames.size()][ntStudyDates.size()];
				String [][] ntTable=fillRecistTable(ntLesionNames, ntStudyDates, lesions, nonTargetTypes, ntTimepoints, ntUIDs);
		
				//calculate the sums first
				Double[] ntSums=calcSums(ntTable);
				//calculate the rrs
				Double[] ntRRBaseline=calcRRBaseline(ntSums);
				Double[] ntRRMin=calcRRMin(ntSums);
				
				
				return new RecistReport(tLesionNames.toArray(new String[tLesionNames.size()]), tStudyDates.toArray(new String[tStudyDates.size()]), tTable, tSums, tRRBaseline, tRRMin, tRR, responseCats, tUIDs,
						ntLesionNames.toArray(new String[ntLesionNames.size()]), ntStudyDates.toArray(new String[ntStudyDates.size()]), ntTable, ntSums, ntRRBaseline, ntRRMin, ntUIDs);
	
			}else {
				return new RecistReport(tLesionNames.toArray(new String[tLesionNames.size()]), tStudyDates.toArray(new String[tStudyDates.size()]), tTable, tSums, tRRBaseline, tRRMin, tRR, responseCats, tUIDs);
			}
		}else {
			log.info("no target lesion in table " +table );
		}
		
		return null;
		

	}
	
	/**
	 * fills the recist table where lesion names are the rows. and the columns are the info and the study dates 
	 * The info sits in the first 3 columns (Name,Type,Location)
	 * Also analyses the table and fills in the timepoint array which has timepoint numbers. 
	 * 0, 1, ...
	 * 0 is baseline
	 * and fills in the UIDStruct table. table is constructed in the same manner with the recist table (lesion names are rows, study dates are columns), but it doesn't have the extra info columns recist table has
	 * for each annotation UIDStruct has StudyUID, SeriesUID and AimUID
	 * @param lesionNames
	 * @param studyDates
	 * @param lesions
	 * @param type
	 * @return
	 */
	public static String [][] fillRecistTable(ArrayList<String> lesionNames, ArrayList<String> studyDates, JSONArray lesions, ArrayList<String> type, Integer[] timepoints, RecistReportUIDCell[][] UIDs){
		String [][] table=new String[lesionNames.size()][studyDates.size()+3];
		
		int baselineIndex=0;
		//get the values to the table
		for (int i = 0; i < lesions.length(); i++)
		{
			String lesionName = ((JSONObject)((JSONObject)lesions.get(i)).get("Name")).getString("value");
			String studyDate = ((JSONObject)((JSONObject)lesions.get(i)).get("StudyDate")).getString("value");
			String aimType=((JSONObject)((JSONObject)lesions.get(i)).get("Type")).getString("value");
			
			if (!type.contains(aimType.toLowerCase())) {
				continue;
			}
			table[lesionNames.indexOf(lesionName)][0]=lesionName;
			//check if exists and if different and put warnings.
			//changes anyhow
			if (table[lesionNames.indexOf(lesionName)][1]!=null && !table[lesionNames.indexOf(lesionName)][1].equalsIgnoreCase(((JSONObject)((JSONObject)lesions.get(i)).get("Type")).getString("value")))
				log.warning("Type at date "+ studyDate + " is different from the same lesion on a different date. The existing one is:"+table[lesionNames.indexOf(lesionName)][1] +" whereas this is:"+((JSONObject)((JSONObject)lesions.get(i)).get("Type")).getString("value"));
			table[lesionNames.indexOf(lesionName)][1]=((JSONObject)((JSONObject)lesions.get(i)).get("Type")).getString("value");
			
			if (table[lesionNames.indexOf(lesionName)][2]!=null && !table[lesionNames.indexOf(lesionName)][2].equalsIgnoreCase(((JSONObject)((JSONObject)lesions.get(i)).get("Location")).getString("value")))
				log.warning("Location at date "+ studyDate + " is different from the same lesion on a different date. The existing one is:"+table[lesionNames.indexOf(lesionName)][2] +" whereas this is:"+((JSONObject)((JSONObject)lesions.get(i)).get("Location")).getString("value"));
			table[lesionNames.indexOf(lesionName)][2]=((JSONObject)((JSONObject)lesions.get(i)).get("Location")).getString("value");
			//get the lesion and get the timepoint. if it is integer put that otherwise calculate using study dates
			JSONObject tpObj=(JSONObject) ((JSONObject)lesions.get(i)).opt("Timepoint");
			if (tpObj==null)
				tpObj=(JSONObject) ((JSONObject)lesions.get(i)).opt("Lesion");
			String lesionTimepoint=tpObj.optString("value");
			int timepoint=0;
			try{
				timepoint=Integer.parseInt(lesionTimepoint);
			}catch(NumberFormatException ne) {
				log.info("Trying to get timepoint from text "+lesionTimepoint);
				if (lesionTimepoint.toLowerCase().contains("baseline")) {
					timepoint=0;
				}else {
					timepoint=studyDates.indexOf(studyDate)-baselineIndex;
				}
			}
			if (timepoint==0)
				baselineIndex=0;
			if (timepoints[studyDates.indexOf(studyDate)]!=null && timepoints[studyDates.indexOf(studyDate)]!= timepoint) {
				log.info("why is the timepoint "+ timepoint + " different from the already existing "+timepoints[studyDates.indexOf(studyDate)] + " "+studyDate );
				for (int t:timepoints){
					log.info("timepoint "+ t);
				}
				for (String st:studyDates){
					log.info("studyDates  "+ st);
				}
			}
			timepoints[studyDates.indexOf(studyDate)]=timepoint;
			log.info("setting timepoint index "+studyDates.indexOf(studyDate) + " for study "+studyDate + " is set to "+timepoint);
			table[lesionNames.indexOf(lesionName)][studyDates.indexOf(studyDate)+3]=((JSONObject)((JSONObject)lesions.get(i)).get("Length")).getString("value");
			if (UIDs!=null){
				String studyUID = ((JSONObject)((JSONObject)lesions.get(i)).get("StudyUID")).getString("value");
				String seriesUID = ((JSONObject)((JSONObject)lesions.get(i)).get("SeriesUID")).getString("value");
				String aimUID=((JSONObject)((JSONObject)lesions.get(i)).get("AimUID")).getString("value");
				//put as a UID cell object
				UIDs[lesionNames.indexOf(lesionName)][studyDates.indexOf(studyDate)]=new RecistReportUIDCell(studyUID, seriesUID, aimUID);
				
			}
		}
		
		
		return table;
	}
	/**
	 * calculate sums of lesion dimensions for each study date
	 * @param table
	 * @return
	 */
	private static Double[] calcSums(String[][] table){
		Double[] sums=new Double[table[0].length-3];
		for (int j=0; j< table[0].length-3; j++) {
			sums[j]=0.0;
			for(int i=0; i<table.length; i++){
				try{
					sums[j]+=Double.parseDouble(table[i][j+3]);

				}catch(Exception e) {
					log.warning("Couldn't convert to double value="+table[i][j+3]);
				}
			}

		}

		return sums;
	}
	
	/**
	 * calculate response rates in reference to baseline (first)
	 * @param sums
	 * @return
	 */
	private static Double[] calcRRBaseline(Double[] sums) {
		Double baseline=sums[0];
		Double[] rrBaseline=new Double[sums.length];
		StringBuilder rrBaseStr= new StringBuilder();
		for (int i=0;i<sums.length;i++) {
			rrBaseline[i]=(sums[i]-baseline)*100.0/baseline;
			rrBaseStr.append(rrBaseline[i]+ "  ");
		}
		return rrBaseline;
	}
	
	/**
	 * calculate response rates in reference to the min value (overall)
	 * @param sums
	 * @return
	 */
	private static Double[] calcRRMin(Double[] sums) {
		Double min=999999.0;
		for (int i=0;i<sums.length;i++) {
			if (sums[i]<min)
				min=sums[i];
		}
		log.info("Min is "+min);
		Double[] rrMin=new Double[sums.length];
		StringBuilder rrMinStr= new StringBuilder();
		for (int i=0;i<sums.length;i++) {
			rrMin[i]=(sums[i]-min)*100.0/min;	
			rrMinStr.append(rrMin[i]+ "  ");
			
		}
		return rrMin;
	}
	
	/**
	 * calculate response rates in reference to the current baseline and current min.
	 * at the baseline min=baseline=0
	 * till I reach min use baseline as the reference after that use min
	 * it also handles multiple baselines and gets the latest
	 * needs timepoints for that
	 * @param sums
	 * @param timepoints
	 * @return
	 */
	private static Double[] calcRR(Double[] sums,Integer[] timepoints) {
		Double min=sums[0];
		log.info("Min is "+min);
		Double[] rr=new Double[sums.length];
		StringBuilder rrStr= new StringBuilder();
		for (int i=0;i<sums.length;i++) {
			if (timepoints[i]==0) {
				min=sums[i];
				log.info("Min changed. New baseline.min is:"+min);
			}
			rr[i]=(sums[i]-min)*100.0/min;	
			rrStr.append(rr[i]+ "  ");
			if (sums[i]<min) {
				min=sums[i];
				log.info("Min changed. Smaller rr. min is:"+min);
			}
		}
		return rr;
	}
	
	/**
	 * calculates the response categories using rr array, timepoints and isThereNewLesion boolean array
	 * if isThereNewLesion is null it won't handle the PD properly
	 * @param rr
	 * @param timepoints
	 * @param isThereNewLesion
	 * @return
	 */
	private static String[] calcResponseCat(Double[] rr, Integer[] timepoints, Boolean[] isThereNewLesion){
		String[] responseCats=new String[rr.length];
		for (int i=0;i<rr.length;i++) {
			//TODO check if there is a new lesion
			
			if (i==0 || timepoints[i]==0) {
				responseCats[i]="BL";
			}
			else if (rr[i] <= -30) {
				responseCats[i]="PR";
			} else if (rr[i] >= 20 || (isThereNewLesion!=null && isThereNewLesion[i]!=null && isThereNewLesion[i]==true)) {
				responseCats[i]="PD";
			} else {
				responseCats[i]="SD";
			}
		}
		return responseCats;
	}

	
	/**
	 * 
	 * @param subjectIDs
	 * @param username
	 * @param sessionID
	 * @return
	 */
	public static WaterfallReport getWaterfall(String subjectIDs, String username, String sessionID){
		ArrayList<String> subjects = new ArrayList<>();
		if (subjectIDs != null) {
			String[] ids = subjectIDs.split(",");
			for (String id: ids)
				subjects.add(id.trim());
		}
		ArrayList<Double> values=new ArrayList<>();
		ArrayList<String> responses=new ArrayList<>();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		for(String subjectID:subjects) {
			SubjectReference subjectReference=new SubjectReference(null, subjectID);
			EPADAIMList aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
			log.info(aims.ResultSet.totalRecords+ " aims found for "+ subjectID);
			RecistReport recist=getRecist(aims);
			if (recist==null) {
				log.warning("Couldn't retrieve recist report for patient "+ subjectID);
				continue;
			}
			values.add(recist.getMinRR());
			responses.add(recist.getMinRRResponse());
		}
		//let Waterfall handle the sorting
		return new WaterfallReport(subjects.toArray(new String[subjects.size()]), values.toArray(new Double[values.size()]), responses.toArray(new String[responses.size()]));
	}

}
