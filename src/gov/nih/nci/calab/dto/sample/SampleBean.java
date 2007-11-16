package gov.nih.nci.calab.dto.sample;

import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.dto.common.SortableName;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * This class represents all properties of a sample that need to be viewed and
 * saved.
 * 
 * @author pansu
 * 
 */

/* CVS $Id: SampleBean.java,v 1.2.2.2 2007-11-16 22:22:42 pansu Exp $ */
public class SampleBean {
	private String sampleId = "";

	private String sampleNamePrefix = "";

	private String sampleName = "";

	private String sampleType = "";

	private String sampleSOP = "";

	private String sampleDescription = "";

	private String sampleSource = "";

	private String sourceSampleId = "";

	private Date dateReceived;

	private String dateReceivedStr = "";

	private String lotId = "";

	private String lotDescription = "";

	private String solubility = "";

	private String generalComments = "";

	private String sampleSubmitter = "";

	private Date accessionDate;

	private String accessionDateStr;

	private List<ContainerBean> containers = new ArrayList<ContainerBean>();

	public SampleBean() {
		// initialize to have one container
		containers.add(new ContainerBean());
	}

	public SampleBean(Sample sample) {
		this.sampleId = StringUtils.convertToString(sample.getId());
		this.sampleName = StringUtils.convertToString(sample.getName());
		this.sampleType = StringUtils.convertToString(sample.getType());
		this.sampleSOP = (sample.getSampleSOP() == null) ? "" : StringUtils
				.convertToString(sample.getSampleSOP().getName());
		this.sampleDescription = StringUtils.convertToString(sample
				.getDescription());
		this.sampleSource = (sample.getSource() == null) ? "" : StringUtils
				.convertToString(sample.getSource().getOrganizationName());
		this.sourceSampleId = StringUtils.convertToString(sample
				.getSourceSampleId());
		this.dateReceived = sample.getReceivedDate();
		if (this.dateReceived != null)
			this.dateReceivedStr = StringUtils.convertDateToString(
					dateReceived, CaNanoLabConstants.DATE_FORMAT);
		this.solubility = StringUtils.convertToString(sample.getSolubility());
		this.lotId = StringUtils.convertToString(sample.getLotId());
		this.lotDescription = StringUtils.convertToString(sample
				.getLotDescription());
		// exclude aliquots
		Collection<SampleContainer> sampleContainers = sample
				.getSampleContainerCollection(CaNanoLabConstants.SAMPLE_CONTAINER);
		this.generalComments = StringUtils
				.convertToString(sample.getComments());
		this.sampleSubmitter = StringUtils.convertToString(sample
				.getCreatedBy());
		this.accessionDate = sample.getCreatedDate();
		if (this.accessionDate != null) {
			this.accessionDateStr = StringUtils.convertDateToString(
					accessionDate, CaNanoLabConstants.DATE_FORMAT);
		}
		for (SampleContainer container : sampleContainers) {
			ContainerBean containerBean = new ContainerBean(container);
			containerBean.setSample(this);
			containers.add(containerBean);
		}
		Collections.sort(this.containers,
				new CaNanoLabComparators.ContainerBeanComparator());
	}

	public SampleBean(String sampleId, String sampleName) {
		this.sampleId = sampleId;
		this.sampleName = sampleName;
	}

	public Date getDateReceived() {
		if (dateReceivedStr.length() > 0) {
			this.dateReceived = StringUtils.convertToDate(dateReceivedStr,
					CaNanoLabConstants.DATE_FORMAT);
		}
		return this.dateReceived;
	}

	public Date getAccessionDate() {
		return this.accessionDate;
	}

	public void setAccessionDate(Date accessionDate) {
		this.accessionDate = accessionDate;
	}

	public void setDateReceived(Date dateReceived) {		
		this.dateReceived = dateReceived;
	}

	public String getGeneralComments() {
		return this.generalComments;
	}

	public void setGeneralComments(String generalComments) {
		this.generalComments = generalComments;
	}

	public String getLotDescription() {
		return this.lotDescription;
	}

	public void setLotDescription(String lotDescription) {
		this.lotDescription = lotDescription;
	}

	public String getLotId() {
		return this.lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public String getSampleDescription() {
		return this.sampleDescription;
	}

	public void setSampleDescription(String sampleDescription) {
		this.sampleDescription = sampleDescription;
	}

	public String getSampleName() {
		return this.sampleName;
	}

	public void setSampleName(String sampleId) {
		this.sampleName = sampleId;
	}

	public String getSampleSOP() {
		return this.sampleSOP;
	}

	public void setSampleSOP(String sampleSOP) {
		this.sampleSOP = sampleSOP;
	}

	public String getSampleType() {
		return this.sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public String getSolubility() {
		return this.solubility;
	}

	public void setSolubility(String solubility) {
		this.solubility = solubility;
	}

	public String getSampleSource() {
		return this.sampleSource;
	}

	public void setSampleSource(String sampleSource) {
		this.sampleSource = sampleSource;
	}

	public String getSourceSampleId() {
		return this.sourceSampleId;
	}

	public void setSourceSampleId(String sourceSampleId) {
		this.sourceSampleId = sourceSampleId;
	}

	public String getSampleSubmitter() {
		return this.sampleSubmitter;
	}

	public void setSampleSubmitter(String sampleSubmitter) {
		this.sampleSubmitter = sampleSubmitter;
	}

	public List<ContainerBean> getContainers() {
		return this.containers;
	}

	public void setContainers(List<ContainerBean> containers) {
		this.containers = containers;
	}

	public String getSampleNamePrefix() {
		return this.sampleNamePrefix;
	}

	public void setSampleNamePrefix(String sampleIdPrefix) {
		this.sampleNamePrefix = sampleIdPrefix;
	}

	public String getSampleId() {
		return this.sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getAccessionDateStr() {
		if (this.accessionDate != null) {
			this.accessionDateStr = StringUtils.convertDateToString(
					this.accessionDate, CaNanoLabConstants.DATE_FORMAT);
		}
		return this.accessionDateStr;
	}

	public String getDateReceivedStr() {
		if (this.dateReceived != null) {
			this.dateReceivedStr = StringUtils.convertDateToString(
					this.dateReceived, CaNanoLabConstants.DATE_FORMAT);
		}
		return this.dateReceivedStr;
	}

	public SortableName getSortableName() {
		return new SortableName(this.sampleName);
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof SampleBean) {
			SampleBean c = (SampleBean) obj;
			String thisId = getSampleId();

			if (thisId != null && thisId.equals(c.getSampleId())) {
				eq = true;
			}

		}
		return eq;
	}

	public int hashCode() {
		int h = 0;
		if (getSampleId() != null) {
			h += getSampleId().hashCode();
		}
		return h;
	}

	public void setDateReceivedStr(String dateReceivedStr) {
		this.dateReceivedStr = dateReceivedStr;
	}

}
