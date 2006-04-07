package gov.nih.nci.calab.dto.administration;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class represents all properties of a sample that need to be viewed and
 * saved.
 * 
 * @author pansu
 * 
 */

/* CVS $Id: SampleBean.java,v 1.5 2006-04-07 21:04:20 pansu Exp $ */
public class SampleBean {
	private String sampleIdPrefix;

	private String sampleId;

	private String sampleType;

	private String sampleSOP;

	private String sampleDescription;

	private String sampleSource;

	private String sourceSampleId;

	private String dateReceived;

	private String lotId;

	private String lotDescription;

	private String solubility;

	private String numberOfContainers;

	private String generalComments;

	private String sampleSubmitter;

	private String accessionDate;

	private ContainerBean[] containers;

	public SampleBean() {

	}

	public SampleBean(String sampleId, String sampleType, String sampleSOP,
			String sampleDescription, String sampleSource,
			String sourceSampleId, String dateReceived, String solubility,
			String lotId, String lotDescription, String numberOfContainers,
			String generalComments, String sampleSubmitter, String accessionDate) {
		super();
		// TODO Auto-generated constructor stub
		this.sampleId = sampleId;
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

	public SampleBean(String sampleId, String sampleType, String sampleSOP,
			String sampleDescription, String sampleSource,
			String sourceSampleId, String dateReceived, String solubility,
			String lotId, String lotDescription, String numberOfContainers,
			String generalComments, String sampleSubmitter,
			String accessionDate, ContainerBean[] containers) {
		// TODO Auto-generated constructor stub
		this(sampleId, sampleType, sampleSOP, sampleDescription, sampleSource,
				sourceSampleId, dateReceived, lotId, lotDescription,
				solubility, numberOfContainers, generalComments,
				sampleSubmitter, accessionDate);
		this.containers = containers;

	}

	public SampleBean(String sampleIdPrefix, String sampleId,
			String sampleType, String sampleSOP, String sampleDescription,
			String sampleSource, String sourceSampleId, String dateReceived,
			String solubility, String lotId, String lotDescription,
			String numberOfContainers, String generalComments,
			String sampleSubmitter, String accessionDate,
			ContainerBean[] containers) {
		// TODO Auto-generated constructor stub
		this(sampleId, sampleType, sampleSOP, sampleDescription, sampleSource,
				sourceSampleId, dateReceived, lotId, lotDescription,
				solubility, numberOfContainers, generalComments,
				sampleSubmitter, accessionDate);
		this.sampleIdPrefix = sampleIdPrefix;
		this.containers = containers;

	}

	public SampleBean(Sample sample) {
		this.sampleId = sample.getName();
		this.sampleType = sample.getType();
		this.sampleSOP = (sample.getSampleSOP()==null)?"":sample.getSampleSOP().getName();
		this.sampleDescription = sample.getDescription();
		this.sampleSource = (sample.getSource()==null)?"":sample.getSource().getOrganizationName();
		this.sourceSampleId = sample.getSourceSampleId();
		this.dateReceived = StringUtils.convertDateToString(sample
				.getReceivedDate(), CalabConstants.DATE_FORMAT);
		this.solubility=sample.getSolubility();
		this.lotId=sample.getLotId();
		this.lotDescription=sample.getLotDescription();
		Set sampleContainers=(Set)sample.getSampleContainerCollection();
		this.numberOfContainers=sampleContainers.size()+"";
		this.generalComments=sample.getComments();
		this.sampleSubmitter=sample.getCreatedBy();
		this.accessionDate=StringUtils.convertDateToString(sample.getCreatedDate(), CalabConstants.DATE_FORMAT);
		this.containers=new ContainerBean[sampleContainers.size()];		
		
		int i=0;
		for (Object obj: sampleContainers) {
			SampleContainer sampleContainer=(SampleContainer)obj;
			containers[i]=new ContainerBean(sampleContainer);
		}
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

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
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

	public String getSampleIdPrefix() {
		return sampleIdPrefix;
	}

	public void setSampleIdPrefix(String sampleIdPrefix) {
		this.sampleIdPrefix = sampleIdPrefix;
	}

}
