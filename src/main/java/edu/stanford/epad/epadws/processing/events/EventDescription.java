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
package edu.stanford.epad.epadws.processing.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EventDescription
{
	public final List<ProjectEventDescription> projectEvents;
	private final ConcurrentMap<String, ProjectEventDescription> projectEventMap; // projectID -> ProjectEventDescription

	public EventDescription()
	{
		this.projectEvents = new ArrayList<ProjectEventDescription>();
		this.projectEventMap = new ConcurrentHashMap<String, ProjectEventDescription>();
	}

	public synchronized ProjectEventDescription recordProjectEvent(String projectID)
	{
		if (!projectEventMap.containsKey(projectID)) {
			ProjectEventDescription projectEventDescription = new ProjectEventDescription(projectID);
			this.projectEventMap.put(projectID, projectEventDescription);
			this.projectEvents.add(projectEventDescription);
			return projectEventDescription;
		} else
			return projectEventMap.get(projectID);
	}

	public List<ProjectEventDescription> getProjectEvents()
	{
		return new ArrayList<ProjectEventDescription>(projectEventMap.values());
	}
}
