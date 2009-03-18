package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.DataRow;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.util.Comparators;

import java.util.ArrayList;
import java.util.Collections;
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
	private List<Condition> conditions = new ArrayList<Condition>();

	public DataRowBean() {
	}

	public DataRowBean(List<Datum> data) {
		domain = data.get(0).getDataRow();
		this.data = data;
		//use condition for the first data is sufficent
		conditions = new ArrayList<Condition>(data.get(0)
				.getConditionCollection());
		Collections.sort(conditions,
				new Comparators.ConditionDateComparator());
	}

	public void addDatum(Datum datum) {
		if (data.contains(datum)) {
			setCreatedByNDate(datum);
			data.remove(datum);
		}
		datum.setDataRow(domain);
		data.add(datum);
		if (datum.getConditionCollection() != null) {
			// add only once, otherwise, conditions will be duplicated with same
			// id
			for (Condition condition : datum.getConditionCollection()) {
				if (!conditions.contains(condition)) {
					conditions.add(condition);
				}
			}
		}
	}

	public void addDatumColumn(Datum datum) {
		datum.setDataRow(domain);
		if (data.contains(datum)) {
			for (Datum thisDatum : data) {
				if (thisDatum.getId().equals(datum.getId())) {
					thisDatum.setName(datum.getName());
					thisDatum.setValueType(datum.getValueType());
					thisDatum.setValueUnit(datum.getValueUnit());
				}
			}
		} else {
			data.add(datum);
		}
	}

	public void removeDatum(Datum datum) {
		data.remove(datum);
	}

	/**
	 * @return the data
	 */
	public List<Datum> getData() {
		return data;
	}

	public void removeCondition(Condition condition) {
		conditions.remove(condition);
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
	 * Compares <code>obj</code> to it self and returns true if they both are
	 * same
	 *
	 * @param obj
	 */
	public boolean equals(Object obj) {
		if (obj instanceof DataRowBean) {
			DataRowBean dataRowBean = (DataRowBean) obj;
			if (getDomain().getId() != null
					&& getDomain().getId().equals(
							dataRowBean.getDomain().getId()))
				return true;
		}
		return false;
	}

	/**
	 * Returns hash code for the primary key of the object
	 */
	public int hashCode() {
		if (getDomain().getId() != null)
			return getDomain().getId().hashCode();
		return 0;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void addConditionColumn(Condition condition) {
		// condition.setDataRow(domain);
		if (conditions.contains(condition)) {
			for (Condition thisCondition : conditions) {
				if (thisCondition.getId().equals(condition.getId())) {
					thisCondition.setName(condition.getName());
					thisCondition.setValueType(condition.getValueType());
					thisCondition.setValueUnit(condition.getValueUnit());
				}
			}
		} else {
			conditions.add(condition);
		}
	}

	/**
	 * @param conditions
	 *            the conditions to set
	 */
	public void setConditions(List<Condition> conditions) {
		if (this.conditions != null) {
			for (Condition originalCondition : this.conditions) {
				int index = conditions.indexOf(originalCondition);
				if (index != -1) {
					// copy createBy and createDate
					conditions.get(index).setCreatedBy(
							originalCondition.getCreatedBy());
					conditions.get(index).setCreatedDate(
							originalCondition.getCreatedDate());
				}
			}
		}
		this.conditions = conditions;
	}

	private void setCreatedByNDate(Datum datum) {
		for (Datum thisDatum : data) {
			if (thisDatum.getId() != null
					&& thisDatum.getId().equals(datum.getId())) {
				if (datum.getCreatedBy() == null) {
					datum.setCreatedBy(thisDatum.getCreatedBy());
				}
				if (datum.getCreatedDate() == null) {
					datum.setCreatedDate(thisDatum.getCreatedDate());
				}
				break;
			}
		}
	}

	public void removeDatumColumn(int index) {
		if (data.size() > index)
			data.remove(index);
	}

	public void removeConditionColumn(int index) {
		if (conditions.size() > index)
			conditions.remove(index);
	}
}
