package gov.nih.nci.calab.dto.administration;

import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.CalabComparators;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.Set;
import java.util.Arrays;

/**
 * This class represents all properties of a sample that need to be viewed and
 * saved.
 * 
 * @author pansu
 * 
 */

/* CVS $Id: SampleBean.java,v 1.12 2006-04-19 19:52:01 pansu Exp $ */
public class SampleBean {
	private String sampleId="";
	
	private String sampleNamePrefix="";

	private String sampleName="";

	private String sampleType="";

	private String sampleSOP="";

	private String sampleDescription="";

	private String sampleSource="";

	private String sourceSampleId="";

	private String dateReceived="";

	private String lotId="";

	private String lotDescription="";

	private String solubility="";

	private String numberOfContainers="";

	private String generalComments="";

	private String sampleSubmitter="";

	private String accessionDate="";

	private ContainerBean[] containers;

	public SampleBean() {
	}

	public SampleBean(String sampleId, String sampleName) {
		this.sampleId=sampleId;
		this.sampleName=sampleName;
	}
	
	public SampleBean(String sampleName, String sampleType, String sampleSOP,
			String sampleDescription, String sampleSource,
			String sourceSampleId, String dateReceived, String solubility,
			String lotId, String lotDescription, String numberOfContainers,
			String generalComments, String sampleSubmitter, String accessionDate) {
		super();
		// TODO Auto-generated constructor stub
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
			String sampleSource, String sourceSampleId, String dateReceived,
			String solubility, String lotId, String lotDescription,
			String numberOfContainers, String generalComments,
			String sampleSubmitter, String accessionDate,
			ContainerBean[] containers) {
		// TODO Auto-generated constructor stub
		this(sampleName, sampleType, sampleSOP, sampleDescription, sampleSource,
				sourceSampleId, dateReceived, solubility, lotId, lotDescription,
				numberOfContainers, generalComments,
				sampleSubmitter, accessionDate);
		this.sampleNamePrefix = sampleNamePrefix;
		this.containers = containers;
	}

	public SampleBean(Sample sample) {
		this.sampleId=StringUtils.convertToString(sample.getId());
		this.sampleName = StringUtils.convertToString(sample.getName());
		this.sampleType = StringUtils.convertToString(sample.getType());
		this.sampleSOP = (sample.getSampleSOP() == null) ? "" : StringUtils.convertToString(sample
				.getSampleSOP().getName());
		this.sampleDescription = StringUtils.convertToString(sample.getDescription());
		this.sampleSource = (sample.getSource() == null) ? "" : StringUtils.convertToString(sample
				.getSource().getOrganizationName());
		this.sourceSampleId = StringUtils.convertToString(sample.getSourceSampleId());
		this.dateReceived = StringUtils.convertDateToString(sample
				.getReceivedDate(), CalabConstants.DATE_FORMAT);
		this.solubility = StringUtils.convertToString(sample.getSolubility());
		this.lotId = StringUtils.convertToString(sample.getLotId());
		this.lotDescription = StringUtils.convertToString(sample.getLotDescription());
		// exclude aliquots
		Set sampleContainers = (Set) sample.getSampleContainerCollection(CalabConstants.SAMPLE_CONTAINER);
		this.numberOfContainers = StringUtils.convertToString(sampleContainers.size());
		this.generalComments = StringUtils.convertToString(sample.getComments());
		this.sampleSubmitter = StringUtils.convertToString(sample.getCreatedBy());
		this.accessionDate = StringUtils.convertDateToString(sample
				.getCreatedDate(), CalabConstants.DATE_FORMAT);
		this.containers = new ContainerBean[sampleContainers.size()];
	
		int i = 0;
		for (Object obj : sampleContainers) {
			SampleContainer sampleContainer = (SampleContainer) obj;
			containers[i] = new ContainerBean(sampleContainer);
			i++;
		}
		Arrays.sort(containers, new CalabComparators.ContainerBeanComparator());
	}

	public String getDateReceived() {
		return dateReceived;
	}

	public String getAccessionDate() {
		return accessionDate;
	}

	public void setAccessionDate(String accessionDate) {
		this.accessionDate = accessionDate;
	}

	public void setDateReceived(String dateReceived) {
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

}
