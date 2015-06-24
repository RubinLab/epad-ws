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
