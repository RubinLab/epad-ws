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
