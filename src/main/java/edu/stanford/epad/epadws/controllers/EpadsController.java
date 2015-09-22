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
package edu.stanford.epad.epadws.controllers;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADDataList;
import edu.stanford.epad.dtos.EPADUsage;
import edu.stanford.epad.dtos.EPADUsageList;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.models.EpadStatistics;
import edu.stanford.epad.epadws.service.SessionService;

@RestController
@RequestMapping("/epads")
public class EpadsController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public EPADDataList getEpads( 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADDataList epads = EpadDatabase.getInstance().getEPADDatabaseOperations().getEpadHostNames();
		return epads;
	}
 
	@RequestMapping(value = "/{hostname}", method = RequestMethod.GET)
	public EPADUsageList getEpadUsage( 
			@PathVariable String hostname,
			HttpServletRequest request, 
	        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		List<EpadStatistics> stats = new EpadStatistics().getObjects("host like '" + hostname + "%' order by createdtime desc");
		EPADUsageList eul = new EPADUsageList();
		for (EpadStatistics stat: stats)
		{
			eul.addUsage(new EPADUsage(stat.getHost(), stat.getNumOfUsers(), stat.getNumOfProjects(),
			stat.getNumOfPatients(), stat.getNumOfStudies(), stat.getNumOfSeries(),
			stat.getNumOfAims(), stat.getNumOfDSOs(), stat.getNumOfPacs(), stat.getNumOfAutoQueries(),
			stat.getNumOfWorkLists(), dateformat.format(stat.getCreatedTime())));
		}
		return eul;
	}
	static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");

}
