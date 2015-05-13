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

public class StudiesRouteTemplates
{
	public static final String STUDY_LIST = "/studies/";
	public static final String STUDY = STUDY_LIST + "{study}";
	public static final String STUDY_AIM_LIST = STUDY + "/aims/";
	public static final String STUDY_AIM = STUDY_AIM_LIST + "{aid}";
	public static final String SERIES_LIST = STUDY + "/series/";
	public static final String SERIES = SERIES_LIST + "{series}";
	public static final String SERIES_AIM_LIST = SERIES + "/aims/";
	public static final String SERIES_AIM = SERIES_AIM_LIST + "{aid}";
	public static final String IMAGE_LIST = SERIES + "/images/";
	public static final String IMAGE = IMAGE_LIST + "{image}";
	public static final String IMAGE_AIM_LIST = IMAGE + "/aims/";
	public static final String IMAGE_AIM = IMAGE_AIM_LIST + "{aid}";
	public static final String FRAME_LIST = IMAGE + "/frames/";
	public static final String FRAME = FRAME_LIST + "{frame}";
	public static final String FRAME_AIM_LIST = FRAME + "/aims/";
	public static final String FRAME_AIM = FRAME_AIM_LIST + "{aid}";
}
