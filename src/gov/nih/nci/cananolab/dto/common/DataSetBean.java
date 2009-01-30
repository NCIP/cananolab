package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.DataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * View bean for Datum
 * 
 * @author pansu, tanq
 * 
 */
public class DataSetBean {
	private DataSet domainDataSet = new DataSet();
	private DataRowBean theDataRow = new DataRowBean();
	private List<DataRowBean> dataRows = new ArrayList<DataRowBean>();
	public DataSetBean() {
	}

	public DataSetBean(DataSet dataSet) {
		domainDataSet = dataSet;
	}

	
	public void setDomainDataSet(String createdBy, int index) throws Exception {
//		if (domainDerivedDatum.getId() == null
//				|| domainDerivedDatum.getCreatedBy() != null
//				&& domainDerivedDatum.getCreatedBy().equals(
//						CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX)) {
//			domainDerivedDatum.setCreatedBy(createdBy);
//			//domainDerivedDatum.setCreatedDate(new Date());
//			// fix for MySQL database, which supports precision only up to
//			// seconds
//			domainDerivedDatum.setCreatedDate(DateUtil
//					.addSecondsToCurrentDate(index));
//		}
//		if (domainDerivedDatum.getValueType() != null
//				&& domainDerivedDatum.getValueType().equals("boolean")) {
//			if (valueStr.equalsIgnoreCase("true")) {
//				domainDerivedDatum.setValue(new Float(1.0));
//			} else if (valueStr.equalsIgnoreCase("false")) {
//				domainDerivedDatum.setValue(new Float(0.0));
//			} else {
//				domainDerivedDatum.setValue(new Float(valueStr));
//			}
//		} else if (domainDerivedDatum.getName() != null) {
//			domainDerivedDatum.setValue(new Float(valueStr));
//		}
	}

	/**
	 * @return the domainDataSet
	 */
	public DataSet getDomainDataSet() {
		return domainDataSet;
	}

	/**
	 * @param domainDataSet the domainDataSet to set
	 */
	public void setDomainDataSet(DataSet domainDataSet) {
		this.domainDataSet = domainDataSet;
	}

	/**
	 * @return the theDataRow
	 */
	public DataRowBean getTheDataRow() {
		return theDataRow;
	}

	/**
	 * @param theDataRow the theDataRow to set
	 */
	public void setTheDataRow(DataRowBean theDataRow) {
		this.theDataRow = theDataRow;
	}

	/**
	 * @return the dataRows
	 */
	public List<DataRowBean> getDataRows() {
		return dataRows;
	}

	/**
	 * @param dataRows the dataRows to set
	 */
	
	public void addDataRow() {
		dataRows.add(this.theDataRow);
	}
	
	public void addDataRow(DataRowBean dataRow) {
		dataRows.add(dataRow);
	}
	
	public void removeDataRow(DataRowBean dataRow) {
		dataRows.remove(dataRow);
	}

	
}
