package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.DataRow;
import gov.nih.nci.cananolab.domain.common.Datum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * View bean for Datum
 *
 * @author pansu, tanq
 *
 */
public class DataRowBean {
	private DataRow domain = new DataRow();
	private List<Datum> data = new ArrayList<Datum>();

	public DataRowBean() {
	}

	public DataRowBean(List<Datum> data) {
		domain = data.get(0).getDataRow();
		this.data = data;
	}

	public void addDatum(Datum datum) {
		if (data.contains(datum)) {
			data.remove(datum);
		}
		datum.setDataRow(domain);
		data.add(datum);
	}

	public void removeDatum(Datum datum) {
		data.remove(datum);
	}

	/**
	 * @return the data
	 */
	public Collection<Datum> getData() {
		return data;
	}

	/**
	 * @return the domain
	 */
	public DataRow getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(DataRow domain) {
		this.domain = domain;
	}
}
