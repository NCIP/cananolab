package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.DataSet;
import gov.nih.nci.cananolab.domain.particle.characterization.Datum;
import gov.nih.nci.cananolab.dto.particle.characterization.DatumBean;

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
	private DataSet domainDataSet = new DataSet();
	private List<Datum> data = new ArrayList<Datum>();

	public DataRowBean() {
	}

	public DataRowBean(DataSet dataSet) {
		domainDataSet = dataSet;
	}

	public void addDatum(Datum datum) {
		if (data.contains(datum)) {
			data.remove(datum);
		}
		data.add(datum);
	}

	public void removeDatum(Datum datum) {
		data.remove(datum);
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
	 * @return the data
	 */
	public Collection<Datum> getData() {
		return data;
	}

	
}
