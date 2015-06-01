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
package edu.stanford.epad.epadws.handlers.coordination;

import java.util.ArrayList;
import java.util.List;

/**
 * A coordination is an AIM Template concept that associates an ordered set of terms together.
 * <p>
 * See the <a href="https://wiki.nci.nih.gov/display/AIM/AIM+Template+Builder+2.0+User's+Guide">AIM 2.0 Template
 * Guide</a>.
 * 
 * @author martin
 * @see Term
 */
public class Coordination
{
	private final List<Term> terms;

	public Coordination(List<Term> terms)
	{
		this.terms = terms;
	}

	public List<Term> getTerms()
	{
		return new ArrayList<Term>(terms);
	}

	public int getNumberOfTerms()
	{
		return terms.size();
	}

	public boolean isValid()
	{
		if (terms == null)
			return false;
		else {
			for (Term term : terms)
				if (term == null || !term.isValid())
					return false;
			return true;
		}
	}

	/**
	 * 
	 * @return A user-friendly description of the coordination
	 */
	public String generateDescription()
	{
		StringBuilder description = new StringBuilder();

		int position = 0;
		for (Term term : terms) {
			if (position++ != 0)
				description.append(" ");
			description.append(term.getDescription());
		}

		return description.toString();
	}

	@Override
	public String toString()
	{
		StringBuilder description = new StringBuilder();

		description.append("Coordination[");

		int position = 0;
		for (Term term : terms) {
			if (position++ != 0)
				description.append(", ");
			description.append(term.toString());
		}

		description.append("]");

		return description.toString();
	}
}
