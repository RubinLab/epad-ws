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
package edu.stanford.epad.epadws.processing.model;

import java.util.ArrayList;
import java.util.Set;

import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * Keeps all the information about an uploaded series that is being processed by ePAD. A {@link SeriesPipelineState}
 * tracks the processing state of this series when it is in the pipeline.
 */
public class SeriesProcessingDescription
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private final int numberOfInstances;
	private final ArrayList<DicomImageDescription> instances;
	private final String studyUID;
	private final String seriesUID;
	private final String subjectName;
	private final String subjectID;

	public SeriesProcessingDescription(int numberOfInstances, String seriesUID, String studyUID, String patientName,
			String subjectID)
	{
		if (numberOfInstances < 1)
			throw new IllegalArgumentException("numInstances must be a positive value.");

		this.numberOfInstances = numberOfInstances;
		int instanceSize = 2000;
		if (instanceSize < numberOfInstances + 1)
			instanceSize = 2 * numberOfInstances;

		instances = new ArrayList<DicomImageDescription>(instanceSize);
		for (int i = 0; i < instanceSize + 1; i++)
			instances.add(null); // Indicates that it is not processed yet

		this.seriesUID = seriesUID;
		this.studyUID = studyUID;
		this.subjectName = patientName;
		this.subjectID = subjectID;
	}

	public String getSeriesUID()
	{
		return seriesUID;
	}

	public String getStudyUID()
	{
		return studyUID;
	}

	public String getPatientName()
	{
		return subjectName;
	}

	public String getSubjectID()
	{
		return subjectID;
	}

	public boolean isComplete()
	{
		int size = instances.size();
		for (int i = 0; i < size; i++) {
			if (!hasInstance(i)) {
				return false;
			}
		}
		return true;
	}

	public float percentComplete()
	{
		int numberOfInstances = getNumberOfInstances();
		int numberOfCompletedInstances = getNumberOfCompletedInstances();

		return (numberOfCompletedInstances) / ((float)numberOfInstances) * 100.0f;
	}

	public int getNumberOfCompletedInstances()
	{
		int count = 0;
		int size = size();
		for (int i = 0; i < size; i++) {
			if (hasInstance(i)) {
				count++;
			}
		}
		return count;
	}

	public int getNumberOfInstances()
	{
		return numberOfInstances;
	}

	private int size()
	{
		return instances.size();
	}

	public boolean hasInstance(int index)
	{
		if (index >= instances.size()) {
			return false;
		}

		return instances.get(index) != null;
	}

	public void updateWithDICOMFileDescriptions(Set<DICOMFileDescription> dicomFileDescriptions)
	{
		for (DICOMFileDescription dicomFileDescription : dicomFileDescriptions) {
			int instanceNumber = dicomFileDescription.instanceNumber;
			String imageUID = dicomFileDescription.imageUID;
			addCompletedInstance(instanceNumber, imageUID);
		}
	}

	/**
	 * @param instanceNumber int
	 * @param sopInstanceUID String
	 */
	private void addCompletedInstance(int instanceNumber, String sopInstanceUID)
	{
		DicomImageDescription imageEntry = new DicomImageDescription(instanceNumber, sopInstanceUID);
		if (!hasInstance(instanceNumber)) {
			if (instances.size() < instanceNumber + 1) {
				int start = instances.size();
				logger.warning("resizing array from=" + instances.size() + " to=" + (instanceNumber + 1) + " series="
						+ seriesUID);
				instances.ensureCapacity(instanceNumber + 1);
				for (int i = start; i < instanceNumber + 1; i++) {
					instances.add(start, null);
				}
			}
			instances.set(instanceNumber, imageEntry);
		}
	}
}
