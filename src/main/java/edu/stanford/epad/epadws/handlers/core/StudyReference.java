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
package edu.stanford.epad.epadws.handlers.core;

import java.util.Map;

import edu.stanford.epad.epadws.handlers.HandlerUtil;

public class StudyReference
{
	public final String projectID;
	public final String subjectID;
	public final String studyUID;

	public StudyReference(String projectID, String subjectID, String studyUID)
	{
		this.projectID = projectID;
		this.subjectID = subjectID;
		this.studyUID = studyUID;
	}

	public static StudyReference extract(String template, String pathInfo)
	{
		Map<String, String> templateMap = HandlerUtil.getTemplateMap(template, pathInfo);
		String projectID = HandlerUtil.getTemplateParameter(templateMap, "project", "");
		String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject", "");
		String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");

		ProjectReference.validateProjectID(projectID);
		SubjectReference.validateSubjectID(subjectID);
		validateStudyUID(studyUID);

		return new StudyReference(projectID, subjectID, studyUID);
	}

	protected static void validateStudyUID(String studyUID)
	{
		if (studyUID == null || studyUID.equals(""))
			throw new RuntimeException("Invalid study UID found in request");
	}
}
