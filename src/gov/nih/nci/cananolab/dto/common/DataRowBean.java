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

	public void addDatumColumn(Datum datum) {
		datum.setDataRow(domain);
//		System.out.println("debug XXXXXXXXXX  add ");
//		if (datum.getValue()!=null && datum.getValue().length()>0) {
//			for (Datum thisDatum: data) {
//				if (thisDatum.getValue()==null || thisDatum.getValue().length()==0) {
//					thisDatum.setValue(datum.getValue());
//				}
//			}
//		}
		System.out.println("XXXXXXXXXX end of debug ");
		if (data.contains(datum)) {
			for (Datum thisDatum: data) {
				if (thisDatum.getId().equals(datum.getId())) {
					thisDatum.setName(datum.getName());
					thisDatum.setValueType(datum.getValueType());
					thisDatum.setValueUnit(datum.getValueUnit());
				}
			}
		}else {
			data.add(datum);
		}
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

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof DataRowBean)
		{
			DataRowBean dataRowBean =(DataRowBean)obj;
			if(getDomain().getId() != null && getDomain().getId().equals(dataRowBean.getDomain().getId()))
				return true;
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getDomain().getId() != null)
			return getDomain().getId().hashCode();
		return 0;
	}
}
