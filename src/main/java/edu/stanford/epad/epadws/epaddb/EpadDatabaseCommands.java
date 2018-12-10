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
package edu.stanford.epad.epadws.epaddb;

/**
 * This interface defines SQL commands to work with ePAD's MySQL database.
 * 
 * 
 * @author martin
 */
public interface EpadDatabaseCommands
{
	public static final String DELETE_ALL_FROM_SERIES_STATUS = "delete from epaddb.series_status";

	public static final String DELETE_SERIES_FROM_SERIES_STATUS = "delete from epaddb.series_status where series_iuid=?";

	public static final String SELECT_EPAD_IMAGE_UIDS_FOR_SERIES = "select i.sop_iuid from epaddb.epad_files as f, pacsdb.instance as i, pacsdb.series as s where series_iuid=? and i.series_fk=s.pk and f.instance_fk=i.pk";
	public static final String INSERT_INTO_EVENT = "INSERT INTO epaddb.events"
			+ "(username,event_status,aim_uid,aim_name,patient_id,patient_name,template_id,template_name,plugin_name,project_id,project_name,series_uid,study_uid,error)"
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String SELECT_EVENTS_FOR_SESSIONID = "SELECT * from epaddb.events where username=? ORDER BY pk";
	public static final String DELETE_EVENTS_FOR_SESSIONID = "DELETE from epaddb.events where username=? and pk <= ?";
	public static final String SELECT_EVENTS_FOR_AIMID = "SELECT * from epaddb.events where aim_uid=? ORDER BY pk desc";
	public static final String DELETE_OLD_EVENTS = "DELETE from epaddb.events where created_time < ?";
	
	public static final String INSERT_INTO_EPAD_SERIES_STATUS = "INSERT INTO epaddb.series_status(series_iuid,status) VALUES (?,?)";
	public static final String UPDATE_EPAD_SERIES_STATUS = "UPDATE epaddb.series_status SET status=? where series_iuid=?";
	public static final String UPDATE_EPAD_SERIES_TAGS = "UPDATE epaddb.series_status SET default_tags=? where series_iuid=?";
	public static final String GET_EPAD_SERIES_TAGS = "SELECT default_tags from epaddb.series_status where series_iuid=?";
	public static final String SELECT_EPAD_SERIES_BY_ID = "SELECT * from epaddb.series_status where series_iuid=?";
	public static final String SELECT_DCM4CHE_STUDY_BY_ID = "SELECT study_iuid,study_desc,study_datetime,accession_no from pacsdb.study where study_iuid=?";
	public static final String SELECT_ANNOTATION_STATUS_FOR_SERIES_BY_IDs = "SELECT annotationstatus from epaddb.project_subject_study_series_user_status s, epaddb.project p, epaddb.subject su, epaddb.study st, epaddb.user u  where s.project_id=p.id and s.subject_id=su.id and s.study_id=st.id and s.user_id=u.id and p.projectid=? and su.subjectuid=? and st.studyuid=? and series_uid=? and u.username=?";
	public static final String SELECT_ANNOTATION_DONE_COUNT_FOR_SERIES_BY_IDs = "SELECT count(*) from epaddb.project_subject_study_series_user_status s, epaddb.project p, epaddb.subject su, epaddb.study st  where s.project_id=p.id and s.subject_id=su.id and s.study_id=st.id and p.projectid=? and su.subjectuid=? and st.studyuid=? and series_uid=? and s.annotationstatus=3";
	public static final String SELECT_STATUS_FOR_SERIES_BY_ID = "SELECT status from epaddb.series_status where series_iuid=?";
	public static final String SELECT_STATUS_AND_CREATED_TIME_FOR_SERIES_BY_ID = "SELECT status,created_time from epaddb.series_status where series_iuid=?";

	public static final String SELECT_ALL_EPAD_FILE_PATHS_WITH_STATUS = "select file_path from epaddb.epad_files where file_status = ?";
	public static final String SELECT_EPAD_FILES_FOR_EXACT_PATH = "SELECT * from epaddb.epad_files where file_path=?";
	public static final String UPDATE_EPAD_FILES_FOR_EXACT_PATH = "UPDATE epaddb.epad_files SET file_status=?, file_size=?, err_msg=? where file_path=?";
	public static final String DELETE_ALL_FROM_EPAD_FILES = "delete from epaddb.epad_files";
	public static final String DELETE_FROM_EPAD_FILES = "delete from epaddb.epad_files where file_path like ?";
	public static final String INSERT_INTO_EPAD_FILES = "INSERT INTO epaddb.epad_files"
			+ "(instance_fk,file_type,file_path,file_size,file_status,err_msg,file_md5)" + "VALUES (?,?,?,?,?,?,?)";
	
	public static final String INSERT_PIXEL_VALUES_FOR_EXACT_PATH = "INSERT INTO epaddb.pixel_values"
			+ "(file_path,frame_num,value,image_uid)" + "VALUES (?,?,?,?)";
	
	public static final String SELECT_PIXEL_VALUES_FOR_PATH = "SELECT value from epaddb.pixel_values where file_path=?";
	public static final String SELECT_PIXEL_VALUES_FOR_IMAGE = "SELECT file_path, value from epaddb.pixel_values where image_uid=?";
	//pk added for removing broken links
	public static final String SELECT_EPAD_FILE_PATH_FOR_IMAGE = "SELECT file_path, f.pk from epaddb.epad_files as f, pacsdb.instance as i where i.sop_iuid=? and i.pk = f.instance_fk";
	public static final String SELECT_EPAD_FILE_PATH_BY_IMAGE_UID = "SELECT file_path from epaddb.epad_files as f where file_path like ?";

	public static final String SELECT_COORDINATION_USING_KEY = "select coordination_id, schema_name, schema_version, description "
			+ "from epaddb.coordinations as c where c.coordination_key = ?";

	// coordination_id, schema, schema_version, description; column 0 is auto-increment coordination_key
	public static final String INSERT_COORDINATION = "insert into epaddb.coordinations values(NULL, ?, ?, ?, ?) ";

	// coordination_id, schema, schema_version, description; column 0 is auto-increment coordination_key
	public static final String UPDATE_COORDINATION = "update epaddb.coordinations set coordination_id = ? where coordination_key = ?";

	// coordination_key, term_key, position
	public static final String INSERT_COORDINATION2TERM = "insert into epaddb.coordination2term values(?, ?, ?) ";

	// term_id, schema, schema_version, description; column 0 is auto-increment key
	public static final String INSERT_COORDINATION_TERM = "insert into epaddb.terms values(NULL, ?, ?, ?, ?)";

	public static final String SELECT_COORDINATION_TERM_KEY = "select term_key from epaddb.terms as t where t.term_id = ? and t.schema_name = ? and t.schema_version = ?";

	public static final String SELECT_COORDINATION_BY_ID = "select coordination_id,t.term_id,t.schema_name,t.description from terms t,coordination2term c2,coordinations c where c.coordination_key=c2.coordination_key and t.term_key=c2.term_key and coordination_id like ? order by coordination_id,position";
	
	public static final String CLEANUP_OBSOLETE_EPAD_FILES = "delete from epad_files where instance_fk not in (select pk from pacsdb.instance)";

	public static final String SELECT_DISTINCT_EPADS = "select distinct host from epaddb.epadstatistics";
	
	public static final String SELECT_TEMPLATE_STATS = "select t.templatecode,t.templatename,t.authors,t.version,t.templateleveltype,t.templatedescription,f.filepath,f.id, (select count(*) from annotations a where a.templatecode=t.templatecode) as aims from template t,epad_file f where t.file_id=f.id;";
	
	public static final String CALCULATE_MONTHLY_CUMULATIVE_USAGE = "insert into epadstatistics_monthly(numOfUsers, numOfProjects,numOfPatients,numOfStudies,numOfSeries,numOfAims,numOfDSOs,numOfWorkLists,numOfPacs,numOfAutoQueries,numOfFiles,numOfPlugins,numOfTemplates,creator,updatetime) (select sum(numOfUsers), sum(numOfProjects), sum(numOfPatients), sum(numOfStudies), sum(numOfSeries), sum(numOfAims),sum(numOfDSOs),sum(numOfWorkLists),sum(numOfPacs),sum(numOfAutoQueries),sum(numOfFiles),sum(numOfPlugins),sum(numOfTemplates),'admin',now()  from (select * from epadstatistics a where createdtime =(select max(createdtime) from epadstatistics b where b.host = a.host) group by host order by host) ab)";
	
	public static final String GET_YEARLY_CUMULATIVE_STAT = "select sum(numOfUsers),sum(numOfProjects), sum(numOfPatients),sum(numOfStudies),sum(numOfSeries),sum(numofAims),sum(numOfDsos),sum(numOfWorkLists),sum(numOfPacs),sum(numOfAutoQueries),sum(numOfFiles),max(numOfPlugins),max(numOfTemplates) from epadstatistics mt inner join(select max(id) id from epadstatistics where host not like '%epad-build.stanford.edu%' and host not like '%epad-dev5.stanford.edu%' and host not like '%epad-dev4.stanford.edu%' and updatetime like ? group by host ) st on mt.id = st.id ";
	
}
