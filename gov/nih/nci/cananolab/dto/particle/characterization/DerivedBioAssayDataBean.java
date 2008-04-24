package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;

import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * View bean for DerivedBioAssayData
 * 
 * @author pansu
 * 
 */
public class DerivedBioAssayDataBean {
	private DerivedBioAssayData domainBioAssayData = new DerivedBioAssayData();

	private LabFileBean labFileBean = new LabFileBean();

	private List<DerivedDatum> datumList = new ArrayList<DerivedDatum>();

	private String createdBy;

	public DerivedBioAssayDataBean() {

	}

	public DerivedBioAssayDataBean(DerivedBioAssayData derivedBioAssayData) {
		domainBioAssayData = derivedBioAssayData;
		labFileBean = new LabFileBean(domainBioAssayData.getLabFile());
		datumList.addAll(domainBioAssayData.getDerivedDatumCollection());
		// TODO sort the datum list
		createdBy = derivedBioAssayData.getCreatedBy();
	}

	public DerivedBioAssayData getDomainBioAssayData() {
		return domainBioAssayData;
	}

	public LabFileBean getLabFileBean() {
		return labFileBean;
	}

	public List<DerivedDatum> getDatumList() {
		if (domainBioAssayData.getDerivedDatumCollection() != null) {
			domainBioAssayData.getDerivedDatumCollection().clear();
		} else {
			domainBioAssayData
					.setDerivedDatumCollection(new HashSet<DerivedDatum>());
		}
		for (DerivedDatum datum : datumList) {
			domainBioAssayData.getDerivedDatumCollection().add(datum);
			if (datum.getId() == null) {
				datum.setCreatedBy(createdBy);
				datum.setCreatedDate(new Date());
			}
		}
		return datumList;
	}

	public void addDerivedDatum() {
		datumList.add(new DerivedDatum());
	}

	public void removeDerivedDatum(int ind) {
		datumList.remove(ind);
	}
}
