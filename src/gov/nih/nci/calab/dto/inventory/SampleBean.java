package gov.nih.nci.calab.dto.inventory;

import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.dto.common.SortableName;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

/**
 * This class represents all properties of a sample that need to be viewed and
 * saved.
 * 
 * @author pansu
 * 
 */

/* CVS $Id: SampleBean.java,v 1.8 2007-05-07 18:52:12 pansu Exp $ */
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

	private String dateReceivedStr;

	private String lotId = "";

	private String lotDescription = "";

	private String solubility = "";

	private String numberOfContainers = "";

	private String generalComments = "";

	private String sampleSubmitter = "";

	private Date accessionDate;

	private String accessionDateStr;

	private ContainerBean[] containers;

	public SampleBean() {
	}

	public SampleBean(String sampleId, String sampleName) {
		this.sampleId = sampleId;
		this.sampleName = sampleName;
	}

	public SampleBean(String sampleName, String sampleType, String sampleSOP,
			String sampleDescription, String sampleSource,
			String sourceSampleId, Date dateReceived, String solubility,
			String lotId, String lotDescription, String numberOfContainers,
			String generalComments, String sampleSubmitter, Date accessionDate) {
		super();
		this.sampleName = sampleName;
		this.sampleType = sampleType;
		this.sampleSOP = sampleSOP;
		this.sampleDescription = sampleDescription;
		this.sampleSource = sampleSource;
		this.sourceSampleId = sourceSampleId;
		this.dateReceived = dateReceived;
		this.lotId = lotId;
		this.lotDescription = lotDescription;
		this.solubility = solubility;
		this.numberOfContainers = numberOfContainers;
		this.generalComments = generalComments;
		this.sampleSubmitter = sampleSubmitter;
		this.accessionDate = accessionDate;
	}

	public SampleBean(String sampleNamePrefix, String sampleName,
			String sampleType, String sampleSOP, String sampleDescription,
			String sampleSource, String sourceSampleId, Date dateReceived,
			String solubility, String lotId, String lotDescription,
			String numberOfContainers, String generalComments,
			String sampleSubmitter, Date accessionDate,
			ContainerBean[] containers) {

		this(sampleName, sampleType, sampleSOP, sampleDescription,
				sampleSource, sourceSampleId, dateReceived, solubility, lotId,
				lotDescription, numberOfContainers, generalComments,
				sampleSubmitter, accessionDate);
		this.sampleNamePrefix = sampleNamePrefix;
		this.containers = containers;
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
		this.solubility = StringUtils.convertToString(sample.getSolubility());
		this.lotId = StringUtils.convertToString(sample.getLotId());
		this.lotDescription = StringUtils.convertToString(sample
				.getLotDescription());
		// exclude aliquots
		Set sampleContainers = (Set) sample
				.getSampleContainerCollection(CaNanoLabConstants.SAMPLE_CONTAINER);
		this.numberOfContainers = StringUtils.convertToString(sampleContainers
				.size());
		this.generalComments = StringUtils
				.convertToString(sample.getComments());
		this.sampleSubmitter = StringUtils.convertToString(sample
				.getCreatedBy());
		this.accessionDate = sample.getCreatedDate();
		this.containers = new ContainerBean[sampleContainers.size()];

		int i = 0;
		for (Object obj : sampleContainers) {
			SampleContainer sampleContainer = (SampleContainer) obj;
			containers[i] = new ContainerBean(sampleContainer);
			containers[i].setSample(this);
			i++;
		}
		Arrays.sort(containers,
				new CaNanoLabComparators.ContainerBeanComparator());
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public Date getAccessionDate() {
		return accessionDate;
	}

	public void setAccessionDate(Date accessionDate) {
		this.accessionDate = accessionDate;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public String getGeneralComments() {
		return generalComments;
	}

	public void setGeneralComments(String generalComments) {
		this.generalComments = generalComments;
	}

	public String getLotDescription() {
		return lotDescription;
	}

	public void setLotDescription(String lotDescription) {
		this.lotDescription = lotDescription;
	}

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public String getNumberOfContainers() {
		return numberOfContainers;
	}

	public void setNumberOfContainers(String numberOfContainers) {
		this.numberOfContainers = numberOfContainers;
	}

	public String getSampleDescription() {
		return sampleDescription;
	}

	public void setSampleDescription(String sampleDescription) {
		this.sampleDescription = sampleDescription;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleId) {
		this.sampleName = sampleId;
	}

	public String getSampleSOP() {
		return sampleSOP;
	}

	public void setSampleSOP(String sampleSOP) {
		this.sampleSOP = sampleSOP;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public String getSolubility() {
		return solubility;
	}

	public void setSolubility(String solubility) {
		this.solubility = solubility;
	}

	public String getSampleSource() {
		return sampleSource;
	}

	public void setSampleSource(String sampleSource) {
		this.sampleSource = sampleSource;
	}

	public String getSourceSampleId() {
		return sourceSampleId;
	}

	public void setSourceSampleId(String sourceSampleId) {
		this.sourceSampleId = sourceSampleId;
	}

	public String getSampleSubmitter() {
		return sampleSubmitter;
	}

	public void setSampleSubmitter(String sampleSubmitter) {
		this.sampleSubmitter = sampleSubmitter;
	}

	public ContainerBean[] getContainers() {
		return containers;
	}

	public void setContainers(ContainerBean[] containers) {
		this.containers = containers;
	}

	public String getSampleNamePrefix() {
		return sampleNamePrefix;
	}

	public void setSampleNamePrefix(String sampleIdPrefix) {
		this.sampleNamePrefix = sampleIdPrefix;
	}

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getAccessionDateStr() {
		if (accessionDate != null) {
			accessionDateStr = StringUtils.convertDateToString(accessionDate,
					CaNanoLabConstants.DATE_FORMAT);
		}
		return accessionDateStr;
	}

	public String getDateReceivedStr() {
		if (dateReceived != null) {
			dateReceivedStr = StringUtils.convertDateToString(dateReceived,
					CaNanoLabConstants.DATE_FORMAT);
		}
		return dateReceivedStr;
	}

	public SortableName getSortableName() {
		return new SortableName(sampleName);
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

}
