package edu.stanford.epad.epadws.dcm4chee;

import edu.stanford.epad.dtos.internal.DCM4CHEEStudySearchType;

/**
 * 
 * @author amsnyder
 */
public class Dcm4CheeStudyQueryBuilder
{
	private final String type;
	private final boolean useLike;
	private final String typeValue;

	public Dcm4CheeStudyQueryBuilder(DCM4CHEEStudySearchType searchType, String typeValue)
	{
		this.type = searchType.getName();
		this.typeValue = typeValue;
		useLike = checkForWildCard(typeValue);
	}

	private static boolean checkForWildCard(String typeValue)
	{
		return typeValue.contains("*");
	}

	public String createStudySearchQuery()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(createStudySelectClause());
		sb.append(createStudyWhereClause());
		sb.append(createStudyGroupByClause());
		sb.append(createStudyOrderByClause());
		sb.append(";");
		return sb.toString();
	}

	private String createStudySelectClause()
	{
		return "select st.study_iuid, p.pat_name, p.pat_id, s.modality, st.study_datetime, st.study_status, "
				+ " count(s.series_iuid) as number_series, s.series_iuid, s.pps_start, st.accession_no, "
				+ " sum(s.num_instances) as sum_images, st.study_id, st.study_desc, st.ref_physician, p.pat_birthdate, p.pat_sex "
				+ " from pacsdb.study as st, pacsdb.patient as p, pacsdb.series as s";
	}

	private String createStudyWhereClause()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(" where ");

		if ("patientName".equals(type)) {
			sb.append("p.pat_name ");
			sb.append(equalOrLikeClause(typeValue));
		} else if ("patientId".equals(type)) {
			sb.append("p.pat_id ");
			sb.append(equalOrLikeClause(typeValue));
		} else if ("studyDate".equals(type)) {
			sb.append("st.study_datetime ");
			sb.append(equalOrLikeClause(typeValue));
		} else if ("accessionNum".equals(type)) {
			sb.append("st.accession_no ");
			sb.append(equalOrLikeClause(typeValue));
		} else if ("examType".equals(type)) {
			sb.append("s.modality ");
			sb.append(upperCaseEqualOrLikeClause(typeValue));
		} else if ("studyUID".equals(type)) {
			sb.append("st.study_iuid ");
			sb.append(equalClause(typeValue));
		}
		sb.append(" and s.study_fk=st.pk");
		sb.append(" and st.patient_fk=p.pk");

		return sb.toString();
	}

	private String createStudyGroupByClause()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" group by st.study_iuid ");
		return sb.toString();
	}

	private String createStudyOrderByClause()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" order by s.created_time ");
		return sb.toString();
	}

	private String equalClause(String typeValue)
	{
		return "= '" + typeValue + "'";
	}

	private String equalOrLikeClause(String typeValue)
	{
		if (useLike) {
			return "LIKE '" + typeValue.replace('*', '%') + "'";
		} else {
			return "= '" + typeValue + "'";
		}
	}

	/**
	 * This is a special case for modality, which testing shows needs upper case values like 'CT'
	 * 
	 * @param typeValue String
	 * @return String
	 */
	private String upperCaseEqualOrLikeClause(String typeValue)
	{
		if (useLike) {
			return "LIKE '" + typeValue.replace('*', '%') + "'";
		} else {
			return "= '" + typeValue.toUpperCase() + "'";
		}
	}
}
