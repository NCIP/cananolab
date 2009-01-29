package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.common.DataSet;

/**
 * View bean for Datum
 * 
 * @author pansu, tanq
 * 
 */
public class DataSetBean {
	private DataSet domainDataSet = new DataSet();

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

	
}
