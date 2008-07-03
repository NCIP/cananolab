package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * View bean for DerivedBioAssayData
 * 
 * @author pansu
 * 
 */
public class DerivedBioAssayDataBean {
	private DerivedBioAssayData domainBioAssayData = new DerivedBioAssayData();

	private LabFileBean labFileBean = new LabFileBean();

	private List<DerivedDatumBean> datumList = new ArrayList<DerivedDatumBean>();

	public DerivedBioAssayDataBean() {

	}

	public DerivedBioAssayDataBean(DerivedBioAssayData derivedBioAssayData) {
		domainBioAssayData = derivedBioAssayData;
		if (domainBioAssayData.getLabFile() != null
				&& domainBioAssayData.getLabFile().getName() != null) {
			labFileBean = new LabFileBean(domainBioAssayData.getLabFile());
		} else {
			labFileBean = null;
		}
		if (domainBioAssayData.getDerivedDatumCollection() != null) {
			for (DerivedDatum datum : domainBioAssayData
					.getDerivedDatumCollection()) {
				datumList.add(new DerivedDatumBean(datum));
			}
		}
		Collections.sort(datumList,
				new CaNanoLabComparators.DerivedDatumBeanDateComparator());
	}

	public DerivedBioAssayData getDomainBioAssayData() {
		return domainBioAssayData;
	}

	public LabFileBean getLabFileBean() {
		return labFileBean;
	}

	public List<DerivedDatumBean> getDatumList() {
		return datumList;
	}

	public void addDerivedDatum() {
		datumList.add(new DerivedDatumBean());
	}

	public void removeDerivedDatum(int ind) {
		datumList.remove(ind);
	}

	public void setupDomainBioAssayData(Map<String, String> typeToClass,
			String createdBy, String internalUriPath) throws Exception {
		if (domainBioAssayData.getId() == null
				|| domainBioAssayData.getCreatedBy() != null
				&& domainBioAssayData.getCreatedBy().equals(
						CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainBioAssayData.setCreatedBy(createdBy);
			domainBioAssayData.setCreatedDate(new Date());
		}
		if (domainBioAssayData.getDerivedDatumCollection() != null) {
			domainBioAssayData.getDerivedDatumCollection().clear();
		} else {
			domainBioAssayData
					.setDerivedDatumCollection(new HashSet<DerivedDatum>());
		}
		if (labFileBean != null) {
			labFileBean.setupDomainFile(internalUriPath, createdBy);
			domainBioAssayData.setLabFile(labFileBean.getDomainFile());
		}
		for (DerivedDatumBean datum : datumList) {
			datum.setDomainDerivedDatum(createdBy);
			domainBioAssayData.getDerivedDatumCollection().add(
					datum.getDomainDerivedDatum());
		}
	}
}
