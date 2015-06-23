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

package edu.stanford.epad.epadws;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class EPadWebServerVersion
{
	static String version = null;
	static String buildDate = null;
	static String buildHost = null;
	public EPadWebServerVersion()
	{
		if (version == null)
		{
			version = "1.6";
			buildDate = "";
			InputStream is = null;
			try {
				is = this.getClass().getClassLoader().getResourceAsStream("version.txt");
				Properties properties = new Properties();
				properties.load(is);
				version = properties.getProperty("build.version");
				buildDate = properties.getProperty("build.date");
				buildHost = properties.getProperty("build.host");
				if (buildHost.startsWith("$") && buildHost.contains("}"))
					buildHost = buildHost.substring(buildHost.indexOf("}")+1);
				if (buildHost.contains("${"))
					buildHost = buildHost.substring(0, buildHost.indexOf("${"));
			} catch (Exception x){
				
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
	}
	public String getVersion() {
		return version;
	}
	public String getBuildDate() {
		return buildDate;
	}
	public String getBuildHost() {
		return buildHost;
	}
}
