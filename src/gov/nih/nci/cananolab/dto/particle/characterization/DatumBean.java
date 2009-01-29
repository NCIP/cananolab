package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.particle.characterization.Datum;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.DateUtil;

/**
 * View bean for Datum
 * 
 * @author pansu, tanq
 * 
 */
public class DatumBean {
	private Datum domainDatum = new Datum();

	public DatumBean() {
	}

	public DatumBean(Datum datum) {
		domainDatum = datum;
	}

	
	public void setDomainDatum(String createdBy, int index) throws Exception {
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
	 * @return the domainDatum
	 */
	public Datum getDomainDatum() {
		return domainDatum;
	}

	/**
	 * @param domainDatum the domainDatum to set
	 */
	public void setDomainDatum(Datum domainDatum) {
		this.domainDatum = domainDatum;
	}
}
