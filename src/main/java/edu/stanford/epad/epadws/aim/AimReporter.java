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
import java.util.List;
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
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
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
		//make sure they are lower case
		for (int i=0;i<columns.length;i++){
			columns[i]=columns[i].toLowerCase();
		}
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
					if (values.containsKey("studydate")) {
						try{
							values.put("studydate", formJsonObj(((DicomImageReferenceEntity)ia.getImageReferenceEntityCollection().get(0)).getImageStudy().getStartDate()));
						}catch(Exception e){
							log.warning("The value for StudyDate couldn't be retrieved ", e);
						}
					}
					if (values.containsKey("name")) {
						try{
							values.put("name", formJsonObj(ia.getName().getValue()));
						}catch(Exception e){
							log.warning("The value for Name couldn't be retrieved ", e);
						}
					}
					if (values.containsKey("studyuid")) {
						try{
							values.put("studyuid", formJsonObj(((DicomImageReferenceEntity)ia.getImageReferenceEntityCollection().get(0)).getImageStudy().getInstanceUid().getRoot()));
						}catch(Exception e){
							log.warning("The value for StudyUID couldn't be retrieved ", e);
						}
					}
					if (values.containsKey("seriesuid")) {
						try{
							values.put("seriesuid", formJsonObj(((DicomImageReferenceEntity)ia.getImageReferenceEntityCollection().get(0)).getImageStudy().getImageSeries().getInstanceUid().getRoot()));
						}catch(Exception e){
							log.warning("The value for SeriesUID couldn't be retrieved ", e);
						}
					}
					if (values.containsKey("modality")) {
						try{
							values.put("modality", formJsonObj(((DicomImageReferenceEntity)ia.getImageReferenceEntityCollection().get(0)).getImageStudy().getImageSeries().getModality().getDisplayName().getValue(),((DicomImageReferenceEntity)ia.getImageReferenceEntityCollection().get(0)).getImageStudy().getImageSeries().getModality().getCode()));
						}catch(Exception e){
							log.warning("The value for modality couldn't be retrieved ", e);
						}
					}
					if (values.containsKey("aimuid")) {
						try{
							values.put("aimuid", formJsonObj(iac.getUniqueIdentifier().getRoot()));
						}catch(Exception e){
							log.warning("The value for AimUID couldn't be retrieved ", e);
						}
					}
					
					//look through observation entities
					if (ia.getImagingObservationEntityCollection()!=null){
						for (ImagingObservationEntity ob: ia.getImagingObservationEntityCollection().getImagingObservationEntityList()){
							
							if (values.containsKey(ob.getLabel().getValue().toLowerCase())) { //key exists put the value
								values.put(ob.getLabel().getValue().toLowerCase(), formJsonObj(ob.getListTypeCode().get(0).getDisplayName().getValue(),ob.getListTypeCode().get(0).getCode()));

							}
							//look through observation characteristics
							if (ob.getImagingObservationCharacteristicCollection()!=null) {
								for (ImagingObservationCharacteristic obChar: ob.getImagingObservationCharacteristicCollection().getImagingObservationCharacteristicList()){
									if (values.containsKey(obChar.getLabel().getValue().toLowerCase())) { //key exists put the value
										//if it has a quantification put that
										if (obChar.getCharacteristicQuantificationCollection().size()>0){
											Scale sq=(Scale)obChar.getCharacteristicQuantificationCollection().get(0);
											values.put(obChar.getLabel().getValue().toLowerCase(), formJsonObj(sq.getValue().getValue(),obChar.getListTypeCode().get(0).getCode()));
										} else
											values.put(obChar.getLabel().getValue().toLowerCase(), formJsonObj(obChar.getListTypeCode().get(0).getDisplayName().getValue(),obChar.getListTypeCode().get(0).getCode()));
									}
								}
							}
						}
					}
					
					//look through physical entities
					if (ia.getImagingPhysicalEntityCollection()!=null){
						for (ImagingPhysicalEntity phy: ia.getImagingPhysicalEntityCollection().getImagingPhysicalEntityList()){
							
							if (values.containsKey(phy.getLabel().getValue().toLowerCase())) { //key exists put the value
								values.put(phy.getLabel().getValue().toLowerCase(), formJsonObj(phy.getListTypeCode().get(0).getDisplayName().getValue(),phy.getListTypeCode().get(0).getCode()));

							}
						}
					}
					//look through calculation entities
					if (ia.getCalculationEntityCollection()!=null){
						for (CalculationEntity cal: ia.getCalculationEntityCollection().getCalculationEntityList()){
							
							if (values.containsKey(cal.getDescription().getValue().toLowerCase())) { //key exists put the value
								try {
									String value=((ExtendedCalculationResult)cal.getCalculationResultCollection().getCalculationResultList().get(0)).getCalculationDataCollection().get(0).getValue().getValue();
//									log.info("value is "+value + "|");
									if (value==null || value.trim().equals("")) value="0";
									//check the units. if they are mm. convert to cm
									String units=((ExtendedCalculationResult)cal.getCalculationResultCollection().getCalculationResultList().get(0)).getUnitOfMeasure().getValue().trim();
									if (units.equalsIgnoreCase("mm")){
										value=String.valueOf(Double.parseDouble(value)/10);
									}
									values.put(cal.getDescription().getValue().toLowerCase(), formJsonObj(value,cal.getListTypeCode().get(0).getCode()));
								}catch(Exception e) {
									log.warning("The value for "+cal.getDescription().getValue() + " couldn't be retrieved ", e);
								}
							}
						}
					}
				}
				String[] strValues=new String[columns.length];
				for (int i=0;i<columns.length;i++) {
					//length is mandatory put 0 if it is not in the aim
					if ( values.get(columns[i]).equals("") ){
						if (columns[i].equals("length")) {
							log.info("putting non-existent length");
							values.put(columns[i], formJsonObj("0","RID39123"));
						} else {
							values.put(columns[i], formJsonObj(""));
						}
					}
					strValues[i]="\""+columns[i]+"\":"+values.get(columns[i]);
					
				}
				table[row++]=strValues;
				
				
			} catch (AimException e) {
				log.info("Aim exception getting the aim from string " + e.getMessage());
			}
		}
		
		ArrayList<String> rows=new ArrayList<>();
		
		for (int i=0;i<table.length;i++){
			StringBuilder rowStr=new StringBuilder();
			if (table[i]==null) 
				continue;
			rowStr.append("{");
			for (int j = 0; j < table[i].length; j++) {
				rowStr.append(table[i][j]);
				if (j<table[i].length-1)
					rowStr.append(",");
			}
			rowStr.append("}");
			rows.add(rowStr.toString());
			
		}
		
		StringBuilder tableJson=new StringBuilder();
		tableJson.append("[");
		for (int i=0;i<rows.size();i++){
			tableJson.append(rows.get(i));
			if (i<rows.size()-1 )
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
		String table=AimReporter.fillTable(aims,"RECIST",new String[]{"Name","StudyDate","Lesion","Type", "Location","Length","StudyUID","SeriesUID","AimUID","LongAxis","ShortAxis", "Modality"});
		//get and append recist_mint records
		String tableMint=AimReporter.fillTable(aims,"RECIST_MINT",new String[]{"Name","StudyDate","Timepoint","Type", "Lesion Status", "Location","Length","StudyUID","SeriesUID","AimUID","LongAxis","ShortAxis", "Modality"});
		
		if ((table==null || table.isEmpty()) && (tableMint==null || tableMint.isEmpty())) 
			return null;
		JSONArray lesions;
		try{
			lesions=new JSONArray(table);
			JSONArray lesionsMint=new JSONArray(tableMint);
			log.info("lesions len "+ lesions.length()+ " lesionsmint len "+ lesionsMint.length());
			lesions=concatArray(lesions,lesionsMint);
		}catch(Exception e) {
			log.warning("couldn't parse json for "+table + " "+ tableMint +  " " +e.getMessage());
			return null;
		}
		//get targets
		ArrayList<String> tLesionNames=new ArrayList<>();
		ArrayList<String> studyDates=new ArrayList<>();
		ArrayList<String> ntLesionNames=new ArrayList<>();
//		ArrayList<String> ntStudyDates=new ArrayList<>();
		ArrayList<String> targetTypes=new ArrayList<>();
		ArrayList<String> ntNewLesionStudyDates=new ArrayList<>();
		Integer[] tTimepoints=null;
		
		targetTypes.add("target");
		targetTypes.add("target lesion"); //for new recist mint template
		targetTypes.add("resolved lesion");
		//first pass fill in the lesion names and study dates (x and y axis of the table)
		for (int i = 0; i < lesions.length(); i++)
		{
			String lesionName = ((JSONObject)((JSONObject)lesions.get(i)).get("name")).getString("value");
			String studyDate = ((JSONObject)((JSONObject)lesions.get(i)).get("studydate")).getString("value");
			String type=((JSONObject)((JSONObject)lesions.get(i)).get("type")).getString("value");
			if (!studyDates.contains(studyDate))
				studyDates.add(studyDate);
			if (targetTypes.contains(type.toLowerCase())) {
				if (!tLesionNames.contains(lesionName))
					tLesionNames.add(lesionName);
			}else {
				//will not work with the new version, but should keep for the old version
				if (type.equalsIgnoreCase("new lesion") && !ntNewLesionStudyDates.contains(studyDate)) {
					ntNewLesionStudyDates.add(studyDate);
				}
				if (!ntLesionNames.contains(lesionName))
					ntLesionNames.add(lesionName);
//				if (!ntStudyDates.contains(studyDate))
//					ntStudyDates.add(studyDate);
			}
		}
		//sort lists
		Collections.sort(tLesionNames);
		Collections.sort(studyDates);
		Collections.sort(ntLesionNames);
//		Collections.sort(ntStudyDates);
		
		if (!tLesionNames.isEmpty() && !studyDates.isEmpty()){
			//fill in the table for target lesions
			if (tTimepoints==null)
				tTimepoints=new Integer[studyDates.size()];
			RecistReportUIDCell[][] tUIDs=new RecistReportUIDCell[tLesionNames.size()][studyDates.size()];
			String [][] tTable=fillRecistTable(tLesionNames, studyDates, lesions, targetTypes,tTimepoints, tUIDs);
//			Integer[] timepoints=checkAndFormat(tTimepoints);
			
			
			
//			Integer[] ntTimepoints=new Integer[studyDates.size()];
			RecistReportUIDCell[][] ntUIDs=new RecistReportUIDCell[ntLesionNames.size()][studyDates.size()];
			String [][] ntTable=null;
			if (!ntLesionNames.isEmpty() && !studyDates.isEmpty()){
				//fill in the table for non-target lesions
				ArrayList<String> nonTargetTypes=new ArrayList<>();
				nonTargetTypes.add("non-target");
				nonTargetTypes.add("nontarget");
				nonTargetTypes.add("non-cancer lesion");
				nonTargetTypes.add("new lesion");
				
				ntTable=fillRecistTable(ntLesionNames, studyDates, lesions, nonTargetTypes, tTimepoints, ntUIDs);
				for (int i = 0; i < ntTable.length; i++) {
					
					for (int j = 0; j < studyDates.size(); j++) {
						if (ntTable[i][j+3]!=null && ntTable[i][j+3].trim().equalsIgnoreCase("new lesion") && !ntNewLesionStudyDates.contains(studyDates.get(j))) {
							ntNewLesionStudyDates.add(studyDates.get(j));
						}
					}
				}
			}
			
			Boolean[] isThereNewLesion=new Boolean[studyDates.size()];
			if (!ntNewLesionStudyDates.isEmpty()) {
				for (String studyDate:ntNewLesionStudyDates)
					isThereNewLesion[studyDates.indexOf(studyDate)]=true;
			}
			
			//calculate the sums first
			Double[] tSums=calcSums(tTable, tTimepoints);
			//calculate the rrs
			Double[] tRRBaseline=calcRRBaseline(tSums, tTimepoints);
			Double[] tRRMin=calcRRMin(tSums, tTimepoints);
			//use rrmin not baseline
			String[] responseCats=calcResponseCat(tRRMin,tTimepoints, isThereNewLesion,tSums);
			//check for the reappear. we just have reappear in nontarget right now
			//if the previous was CR, and there is a reappear it is PD
			for (int i=0;i<responseCats.length;i++){
				if (responseCats[i]!=null && responseCats[i].equalsIgnoreCase("CR") && i<responseCats.length-1 && !ntLesionNames.isEmpty()){
					//this is cr, find the next timepoint
					//stop looking if the timepoint is greater than +1
					for (int k=i+1;k<tTimepoints.length;k++){
						if (tTimepoints[k]==tTimepoints[i]+1){
							//see for all the nontarget lesions
							for (int j=0;j<ntTable.length;j++){
								if (ntTable[j][k].toLowerCase().contains("reappeared"))
									responseCats[k]="PD";
							}
						}else if (tTimepoints[k]>tTimepoints[i]+1){
							break;
						}
					}
				}
			}
			
			if (!ntLesionNames.isEmpty() && !studyDates.isEmpty()){	
				RecistReport rr= new RecistReport(tLesionNames.toArray(new String[tLesionNames.size()]), studyDates.toArray(new String[studyDates.size()]), tTable, tSums, tRRBaseline, tRRMin, responseCats, tUIDs,
						ntLesionNames.toArray(new String[ntLesionNames.size()]), ntTable, ntUIDs);
				rr.setTimepoints(tTimepoints);
				return rr;
			}else {
				RecistReport rr= new RecistReport(tLesionNames.toArray(new String[tLesionNames.size()]), studyDates.toArray(new String[studyDates.size()]), tTable, tSums, tRRBaseline, tRRMin, responseCats, tUIDs);
				rr.setTimepoints(tTimepoints);
				return rr;
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
			String lesionName = ((JSONObject)((JSONObject)lesions.get(i)).get("name")).getString("value");
			String studyDate = ((JSONObject)((JSONObject)lesions.get(i)).get("studydate")).getString("value");
			String aimType=((JSONObject)((JSONObject)lesions.get(i)).get("type")).getString("value");
			JSONObject statusObject=((JSONObject)lesions.get(i)).optJSONObject("lesion status");
			String aimStatus=null;
			if (statusObject!=null)
				aimStatus=statusObject.optString("value");
			
			if (!type.contains(aimType.toLowerCase())) {
				continue;
			}
			table[lesionNames.indexOf(lesionName)][0]=lesionName;
			//check if exists and if different and put warnings.
			//changes anyhow
			if (table[lesionNames.indexOf(lesionName)][1]!=null && !table[lesionNames.indexOf(lesionName)][1].equalsIgnoreCase(((JSONObject)((JSONObject)lesions.get(i)).get("type")).getString("value")))
				log.warning("Type at date "+ studyDate + " is different from the same lesion on a different date. The existing one is:"+table[lesionNames.indexOf(lesionName)][1] +" whereas this is:"+((JSONObject)((JSONObject)lesions.get(i)).get("type")).getString("value"));
			table[lesionNames.indexOf(lesionName)][1]=((JSONObject)((JSONObject)lesions.get(i)).get("type")).getString("value");
			
			if (table[lesionNames.indexOf(lesionName)][2]!=null && !table[lesionNames.indexOf(lesionName)][2].equalsIgnoreCase(((JSONObject)((JSONObject)lesions.get(i)).get("location")).getString("value")))
				log.warning("Location at date "+ studyDate + " is different from the same lesion on a different date. The existing one is:"+table[lesionNames.indexOf(lesionName)][2] +" whereas this is:"+((JSONObject)((JSONObject)lesions.get(i)).get("location")).getString("value"));
			table[lesionNames.indexOf(lesionName)][2]=((JSONObject)((JSONObject)lesions.get(i)).get("location")).getString("value");
			//get the lesion and get the timepoint. if it is integer put that otherwise calculate using study dates
			JSONObject tpObj=(JSONObject) ((JSONObject)lesions.get(i)).opt("timepoint");
			if (tpObj==null)
				tpObj=(JSONObject) ((JSONObject)lesions.get(i)).opt("lesion");
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
				baselineIndex=studyDates.indexOf(studyDate);
			if (timepoints[studyDates.indexOf(studyDate)]!=null && timepoints[studyDates.indexOf(studyDate)]!= timepoint) {
				//TODO How to handle timepoint changes? I currently override with the latest for now
				log.info("why is the timepoint "+ timepoint + " different from the already existing "+timepoints[studyDates.indexOf(studyDate)] + " "+studyDate );
				for (Integer t:timepoints){
					log.info("timepoint "+ t);
				}
				for (String st:studyDates){
					log.info("studyDates  "+ st);
				}
			}
			timepoints[studyDates.indexOf(studyDate)]=timepoint;
//			log.info("setting timepoint index "+studyDates.indexOf(studyDate) + " for study "+studyDate + " is set to "+timepoint);
			//check if it is the nontarget table and fill in with text instead of values
			if (type.contains("nontarget")){
				if (aimStatus!=null && !aimStatus.equals("")){
					table[lesionNames.indexOf(lesionName)][studyDates.indexOf(studyDate)+3]=((JSONObject)((JSONObject)lesions.get(i)).get("lesion status")).getString("value");
				} else {
					String status="";
					if (aimType.equals("resolved lesion") || aimType.equals("new lesion"))
						status=aimType;
					else 
						status="present lesion";
						
					table[lesionNames.indexOf(lesionName)][studyDates.indexOf(studyDate)+3]=status;
				}
				
			}else{
				if (!aimType.equals("resolved lesion")){
					//get length and put it in table
					//if there are longaxis and shortaxis 
					//use short if it is lymph, use long otherwise
					//if there is just length use that
					String length="";
					JSONObject longaxis=((JSONObject)((JSONObject)lesions.get(i)).optJSONObject("longaxis"));
					JSONObject shortaxis=((JSONObject)((JSONObject)lesions.get(i)).optJSONObject("shortaxis"));
					if (longaxis!=null && !longaxis.getString("value").equals("")  && shortaxis!=null && !shortaxis.getString("value").equals("")){
						if (((JSONObject)((JSONObject)lesions.get(i)).get("location")).getString("value").toLowerCase().contains("lymph"))
							length=shortaxis.getString("value");
						else
							length=longaxis.getString("value");
//						log.info("Gotta use long axis, short axis. length is "+length);
					}else{
						length=((JSONObject)((JSONObject)lesions.get(i)).get("length")).getString("value");
					}
					table[lesionNames.indexOf(lesionName)][studyDates.indexOf(studyDate)+3]=length;
				
				}else 
					table[lesionNames.indexOf(lesionName)][studyDates.indexOf(studyDate)+3]="0";
			}
			if (UIDs!=null){
				String studyUID = ((JSONObject)((JSONObject)lesions.get(i)).get("studyuid")).getString("value");
				String seriesUID = ((JSONObject)((JSONObject)lesions.get(i)).get("seriesuid")).getString("value");
				String aimUID=((JSONObject)((JSONObject)lesions.get(i)).get("aimuid")).getString("value");
				String location=((JSONObject)((JSONObject)lesions.get(i)).get("location")).getString("value");
				String modality=((JSONObject)((JSONObject)lesions.get(i)).get("modality")).getString("code");
				if (modality.equals("99EPADM0"))
					modality=((JSONObject)((JSONObject)lesions.get(i)).get("modality")).getString("value");
				//put as a UID cell object
				UIDs[lesionNames.indexOf(lesionName)][studyDates.indexOf(studyDate)]=new RecistReportUIDCell(studyUID, seriesUID, aimUID,timepoint,aimType,location,modality);
				
			}
			
			
		}
		//I need to do this after the table is populated
		if (type.contains("nontarget")){
			for (int i = 0; i < table.length; i++) {
				
				for (int j = 0; j < studyDates.size(); j++) {
					//if this is new lesion mark all following consecutive new lesions as present
					if (table[i][j+3]!=null && table[i][j+3].trim().equalsIgnoreCase("new lesion")){
						for (int k = j+1; k < studyDates.size(); k++) {
//							log.info("marking i="+i+ " j="+j+ " k=" +k+ " table[i,j+3]="+table[i][j+3]+ " table[i,k+3]="+table[i][k+3]);
							
							if (table[i][k+3]!=null && table[i][k+3].trim().equalsIgnoreCase("new lesion")){
								table[i][k+3]="present lesion";
							}else if (table[i][k+3]!=null && table[i][k+3].trim().equalsIgnoreCase("resolved lesion")){
								break;
							}
						}
					}
					
					if (table[i][j+3]!=null && table[i][j+3].trim().equalsIgnoreCase("resolved lesion")){
						if (j<studyDates.size()-1 && table[i][j+4]!=null && table[i][j+4].trim().equalsIgnoreCase("present lesion")){
								table[i][j+4]="reappeared lesion";
						}
					}
					
				}
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
	 * calculate sums of lesion dimensions for each timepoint
	 * @param table
	 * @param timepoints. timepoints should start from 0 and be continuous but timepoint can repeat(they need to be adjacent)
	 * @return it will return the sums for each timepoint. if the timepoint is listed twice. it will have the same amount twice
	 */
	private static Double[] calcSums(String[][] table, Integer[] timepoints){
		Double[] sums=new Double[table[0].length-3];
		for (int k=0; k< table[0].length-3; k++) {
			sums[k]=0.0;
			log.info("k is "+k);
			for (int j=k; j< table[0].length-3; j++) {
				log.info("j is "+j);
				if (timepoints[j]==timepoints[k]){
					if (j!=k)
						sums[j]=null;
			
					for(int i=0; i<table.length; i++){
						try{
							sums[k]+=Double.parseDouble(table[i][j+3]);
		
						}catch(Exception e) {
							log.warning("Couldn't convert to double value="+table[i][j+3]);
						}
					}
				}else{
					//break if you see any other timepoint and skip the columns already calculated
					k=j-1;
					log.info("jumping to "+(k+1));
					break;
				}
			}

		}
//		for (int i=0;i<sums.length;i++)
//			if (sums[i]==null)
//				sums[i]=0.0;
		for (int i=0;i<sums.length;i++)
			log.info("sum "+ i+ " " + sums[i]);
		return sums;
	}
	/**
	 * calculate response rates in reference to baseline (first)
	 * @param sums
	 * @return
	 */
	private static Double[] calcRRBaseline(Double[] sums,Integer[] timepoints) {
		Double baseline=sums[0];
		Double[] rrBaseline=new Double[sums.length];
		StringBuilder rrBaseStr= new StringBuilder();
		for (int i=0;i<sums.length;i++) {
			if (sums[i]!=null){
				if (timepoints[i]!=null && timepoints[i]==0) {
					baseline=sums[i];
					log.info("baseline changed. New baseline is:"+i);
				}
				rrBaseline[i]=(sums[i]-baseline)*100.0/baseline;
				rrBaseStr.append(rrBaseline[i]+ "  ");
			}
		}
		return rrBaseline;
	}
	
	//removed as Dr. Rubin said the method below is rrmin
//	/**
//	 * calculate response rates in reference to the min value (overall)
//	 * @param sums
//	 * @return
//	 */
//	private static Double[] calcRRMin(Double[] sums,Integer[] timepoints) {
//		Double min=999999.0;
//		for (int i=0;i<sums.length;i++) {
//			if ((timepoints[i]!=null && timepoints[i]==0) || sums[i]<min) {
//				min=sums[i];
//				log.info("Min changed. New min is:"+min);
//			}
//		}
//		log.info("Min is "+min);
//		Double[] rrMin=new Double[sums.length];
//		StringBuilder rrMinStr= new StringBuilder();
//		for (int i=0;i<sums.length;i++) {
//			rrMin[i]=(sums[i]-min)*100.0/min;	
//			rrMinStr.append(rrMin[i]+ "  ");
//			
//		}
//		return rrMin;
//	}
	
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
	private static Double[] calcRRMin(Double[] sums,Integer[] timepoints) {
		Double min=sums[0];
		log.info("Min is "+min);
		Double[] rr=new Double[sums.length];
		StringBuilder rrStr= new StringBuilder();
		for (int i=0;i<sums.length;i++) {
			if (sums[i]!=null){
				if (timepoints[i]!=null && timepoints[i]==0) {
					min=sums[i];
					log.info("Min changed. New baseline.min is:"+min);
				}
				if (min==0){
					log.warning("min is 0. returning 999999.9 for rr");
					rr[i]=999999.9;
				}else
					rr[i]=(sums[i]-min)*100.0/min;	
				rrStr.append(rr[i]+ "  ");
				if (sums[i]<min) {
					min=sums[i];
					log.info("Min changed. Smaller rr. min is:"+min);
				}
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
	private static String[] calcResponseCat(Double[] rr, Integer[] timepoints, Boolean[] isThereNewLesion, Double[] sums ){
		String[] responseCats=new String[rr.length];
		for (int i=0;i<rr.length;i++) {
			if (rr[i]!=null) {
				if (i==0 || (timepoints[i]!=null && timepoints[i]==0)) {
					responseCats[i]="BL";
				}
				else if (rr[i] >= 20 || (isThereNewLesion!=null && isThereNewLesion[i]!=null && isThereNewLesion[i]==true)) {
					responseCats[i]="PD"; //progressive
				} else if (sums[i]==0){
					responseCats[i]="CR"; //complete response
				}
				else if (rr[i] <= -30) {
					responseCats[i]="PR";//partial response
				}  else {
					responseCats[i]="SD"; //stable disease
				}
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
	public static WaterfallReport getWaterfallProject(String projectID, String username, String sessionID, String type){
		EpadProjectOperations projOp = DefaultEpadProjectOperations.getInstance();
		ArrayList<String> subjects=new ArrayList<>();
		try {
			List<Subject> subjectObjs=projOp.getSubjectsForProject(projectID);
			for (Subject s:subjectObjs)
				subjects.add(s.getSubjectUID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getWaterfall(subjects, username, sessionID, type,projectID);
	}
	
	
	public static WaterfallReport getWaterfall(String subjectIDs, String username, String sessionID, String type, String projectID){
		ArrayList<String> subjects = new ArrayList<>();
		if (subjectIDs != null) {
			String[] ids = subjectIDs.split(",");
			for (String id: ids)
				subjects.add(id.trim());
		}
		return getWaterfall(subjects, username, sessionID, type, projectID);
	}
	
	
	/**
	 * 
	 * @param subjectIDs
	 * @param username
	 * @param sessionID
	 * @return
	 */
	public static WaterfallReport getWaterfall(ArrayList<String> subjects, String username, String sessionID, String type, String projectID){
		
		ArrayList<Double> values=new ArrayList<>();
		ArrayList<String> projects=new ArrayList<>();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		ArrayList<String> validSubjects =new ArrayList<>();
		for(String subjectID:subjects) {
			SubjectReference subjectReference=new SubjectReference(null, subjectID);
			EPADAIMList aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
			log.info(aims.ResultSet.totalRecords+ " aims found for "+ subjectID);
			RecistReport recist=getRecist(aims);
			if (recist==null) {
				log.warning("Couldn't retrieve recist report for patient "+ subjectID);
				continue;
			}
			validSubjects.add(subjectID);
			projects.add(projectID);
			switch(type){
			case "BASELINE":
				values.add(recist.getMinRRBaseLine());
//				responses.add(recist.getBestResponseBaseline());
				break;
			case "MIN":
				values.add(recist.getMinRRMinimum());
//				responses.add(recist.getBestResponseMin());
				break;
			default:
				values.add(recist.getMinRRBaseLine());
//				responses.add(recist.getBestResponseBaseline());
				break;
			}
		}
		//let Waterfall handle the sorting

//		return new WaterfallReport(validSubjects.toArray(new String[validSubjects.size()]), values.toArray(new Double[values.size()]));
		return new WaterfallReport(validSubjects.toArray(new String[validSubjects.size()]), values.toArray(new Double[values.size()]), projects.toArray(new String[projects.size()]));
	}
	
	
	public static WaterfallReport getWaterfall(JSONArray subj_proj_array, String username, String sessionID, String type){
		
		ArrayList<Double> values=new ArrayList<>();
		ArrayList<String> projects=new ArrayList<>();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		ArrayList<String> validSubjects =new ArrayList<>();
		for (int i = 0; i < subj_proj_array.length(); i++)
		{
			JSONObject sub_prj = subj_proj_array.getJSONObject(i);
			SubjectReference subjectReference=new SubjectReference(sub_prj.getString("projectID"), sub_prj.getString("subjectID"));
			EPADAIMList aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
			log.info(aims.ResultSet.totalRecords+ " aims found for "+ sub_prj.getString("subjectID"));
			RecistReport recist=getRecist(aims);
			if (recist==null) {
				log.warning("Couldn't retrieve recist report for patient "+ sub_prj.getString("subjectID"));
				continue;
			}
			validSubjects.add(sub_prj.getString("subjectID"));
			switch(type){
			case "BASELINE":
				values.add(recist.getMinRRBaseLine());
				projects.add(sub_prj.getString("projectID"));
				break;
			case "MIN":
				values.add(recist.getMinRRMinimum());
				projects.add(sub_prj.getString("projectID"));
				break;
			default:
				values.add(recist.getMinRRBaseLine());
				projects.add(sub_prj.getString("projectID"));
				break;
			}
		}
		//let Waterfall handle the sorting

		return new WaterfallReport(validSubjects.toArray(new String[validSubjects.size()]), values.toArray(new Double[values.size()]), projects.toArray(new String[projects.size()]));
	}

}
