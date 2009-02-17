package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.DataRow;
import gov.nih.nci.cananolab.domain.common.DataSet;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.characterization.Datum;
import gov.nih.nci.cananolab.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View bean for Datum
 *
 * @author pansu, tanq
 *
 */
public class DataSetBean {
	private DataSet domain=new DataSet();
	private DataRowBean theDataRow = new DataRowBean();
	private List<DataRowBean> dataRows = new ArrayList<DataRowBean>();
	private List<Datum> data = new ArrayList<Datum>();
	private FileBean file=new FileBean();
	

	public DataSetBean() {

	}
	public DataSetBean(List<Datum> data) {
		domain=data.get(0).getDataSet();
		Map<DataRow, List<Datum>> dataMap = new HashMap<DataRow, List<Datum>>();
		List<DataRow> rows = new ArrayList<DataRow>();
		List<Datum> dataPerRow = null;
		for (Datum datum : data) {
			if (!rows.contains(datum.getDataRow())) {
				rows.add(datum.getDataRow());
			}
			if (dataMap.containsKey(datum.getDataRow())) {
				dataPerRow = dataMap.get(datum.getDataRow());
			} else {
				dataPerRow = new ArrayList<Datum>();
				dataMap.put(datum.getDataRow(), dataPerRow);
			}
			dataPerRow.add(datum);
		}
		for (DataRow row : rows) {
			DataRowBean rowBean = new DataRowBean(dataMap.get(row));
			dataRows.add(rowBean);
		}
	}

	/**
	 * @return the theDataRow
	 */
	public DataRowBean getTheDataRow() {
		return theDataRow;
	}

	/**
	 * @param theDataRow
	 *            the theDataRow to set
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
	 * @param dataRows
	 *            the dataRows to set
	 */

	public void addDataRow() {
		dataRows.add(this.theDataRow);
	}

	public void addDataRow(DataRowBean dataRow) {
		System.out.println("################ bef dataRows.size()   "+dataRows.size());
		System.out.println("################ bef dataRows.size()   "+dataRows.size());
		if (dataRows.contains(dataRow)) {
			dataRows.remove(dataRow);
		}
		dataRows.add(dataRow);
		System.out.println("################ after dataRows.size()   "+dataRows.size());
	}

	public void removeDataRow(DataRowBean dataRow) {
		dataRows.remove(dataRow);
	}

	public List<Datum> getData() {
		for(DataRowBean row: dataRows) {
			data.addAll(row.getData());
		}
		return data;
	}

	public void setupDomain(String createdBy) {
		int i = 0;
		for (DataRowBean dataRowBean : dataRows) {
			for (Datum datum : dataRowBean.getData()) {
				datum.setCreatedBy(createdBy);
				datum.setCreatedDate(DateUtil.addSecondsToCurrentDate(i));
				datum.setDataSet(domain);
				i++;
			}
		}
	}

	/**
	 * @return the domain
	 */
	public DataSet getDomain() {
		return domain;
	}
	public FileBean getFile() {
		return file;
	}
	public void setFile(FileBean file) {
		this.file = file;
	}
	
	public DataRowBean getDataRowBean(Datum myDatum) {
		DataRowBean dataRowBean = null;
		for (DataRowBean drb: this.dataRows) {
			Collection<Datum> datumColletion = drb.getData();
			if (datumColletion.contains(myDatum)) {
				dataRowBean = drb;
				break;
			}
		}
		return dataRowBean;
	}

}
