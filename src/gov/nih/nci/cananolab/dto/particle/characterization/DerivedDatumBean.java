package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.DateUtil;

/**
 * View bean for DerivedDatum
 * 
 * @author pansu
 * 
 */
public class DerivedDatumBean {
	private DerivedDatum domainDerivedDatum = new DerivedDatum();
	private String valueStr = "";

	public DerivedDatumBean() {
	}

	public DerivedDatumBean(DerivedDatum derivedDatum) {
		domainDerivedDatum = derivedDatum;
		if (derivedDatum.getValueType() != null
				&& derivedDatum.getValueType().equals("boolean")) {
			valueStr = (derivedDatum.getValue() == 1.0) ? "true" : "false";
		} else {
			valueStr = derivedDatum.getValue().toString();
		}
	}

	public DerivedDatum getDomainDerivedDatum() {
		return domainDerivedDatum;
	}

	public String getValueStr() {
		return valueStr;
	}

	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
	}

	public void setDomainDerivedDatum(String createdBy, int index) throws Exception {
		if (domainDerivedDatum.getId() == null
				|| domainDerivedDatum.getCreatedBy() != null
				&& domainDerivedDatum.getCreatedBy().equals(
						CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainDerivedDatum.setCreatedBy(createdBy);
			//domainDerivedDatum.setCreatedDate(new Date());
			// fix for MySQL database, which supports precision only up to
			// seconds
			domainDerivedDatum.setCreatedDate(DateUtil
					.addSecondsToCurrentDate(index));
		}
		if (domainDerivedDatum.getValueType() != null
				&& domainDerivedDatum.getValueType().equals("boolean")) {
			if (valueStr.equalsIgnoreCase("true")) {
				domainDerivedDatum.setValue(new Float(1.0));
			} else if (valueStr.equalsIgnoreCase("false")) {
				domainDerivedDatum.setValue(new Float(0.0));
			} else {
				domainDerivedDatum.setValue(new Float(valueStr));
			}
		} else if (domainDerivedDatum.getName() != null) {
			domainDerivedDatum.setValue(new Float(valueStr));
		}
	}
}
