//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
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
package edu.stanford.epad.epadws.dcm4chee;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.common.dicom.DCM4CHEEImageDescription;
import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudySearchType;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;

/**
 * Defines all operations on the dcm4chee database used by ePAD.
 * 
 * @author martin
 * @see Dcm4CheeQueries
 */
public interface Dcm4CheeDatabaseOperations
{
	String getStudyUIDForSeries(String seriesUID);

	String getSeriesUIDForImage(String imageUID);

	Map<String, String> studySearch(String studyUID);

	List<Map<String, String>> getAllSeriesInStudy(String studyUID);

	Set<String> getAllSeriesUIDsInStudy(String studyUID);

	Set<String> getStudyUIDsForPatient(String patientID);

	Set<String> getImageUIDsForSeries(String seriesUID);

	String getFirstImageUIDInSeries(String seriesUID);

	int getNumberOfStudiesForPatient(String patientID);

	int getNumberOfStudiesForPatients(Set<String> patientIDs);

	Map<String, String> getParentStudyForSeries(String seriesUID);

	public Set<DICOMFileDescription> getDICOMFilesForSeries(String seriesUID);

	List<DCM4CHEEImageDescription> getImageDescriptions(String studyUID, String seriesUID);

	DCM4CHEEImageDescription getImageDescription(ImageReference imageReference);

	DCM4CHEEImageDescription getImageDescription(String studyUID, String seriesUID, String imageUID);

	int getPrimaryKeyForImageUID(String imageUID);

	/**
	 * Get all dcm4chee studies that have finished processing.
	 */
	Set<String> getAllReadyDcm4CheeSeriesUIDs();

	/**
	 * Get all dcm4chee studies/series.
	 */
	Set<String> getAllDcm4CheeSeriesUIDs();

	/**
	 * typeValue one of: patientName, patientId, studyDate, accessionNum, examType
	 * 
	 * @see DCM4CHEEStudySearchType
	 */
	List<Map<String, String>> studySearch(DCM4CHEEStudySearchType searchType, String typeValue);

	/**
	 * Returns a map describing a dcm4chee series with the following keys: pk, study_fk, mpps_fk, inst_code_fk,
	 * series_iuid, series_no, modality, body_part, laterality, series_desc, institution, station_name, department,
	 * perf_physician, perf_phys_fn_sx, perf_phys_gn_sx perf_phys_i_name, perf_phys_p_name, pps_start, series_custom1,
	 * series_custom2, series_custom3, num_instances, src_aet, ext_retr_aet, retrieve_aets, fileset_iuid, fileset_id,
	 * availability, series_status, created_time, updated_time, series_attrs
	 */
	Map<String, String> getSeriesData(String seriesUID);
}
