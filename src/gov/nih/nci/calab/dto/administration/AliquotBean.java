package gov.nih.nci.calab.dto.administration;

import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class includes all properties of an aliquot that need to be viewed and
 * saved.
 * 
 * @author pansu
 * 
 */

/* CVS $Id: AliquotBean.java,v 1.8 2006-04-25 16:58:04 pansu Exp $ */

public class AliquotBean {
	private String aliquotId = "";

	private String aliquotName = "";

	private ContainerBean container;

	private String howCreated = "";

	private String creator = "";

	private String creationDate = "";

	private SampleBean sample;
	
	private String maskStatus;

	public AliquotBean() {
		container = new ContainerBean();
		sample = new SampleBean();
	}

	//used in WorkflowResultBean
	public AliquotBean(String aliquotName) {
		this.aliquotName=aliquotName;
	}
	
	public AliquotBean(String aliquotId, String aliquotName, String maskStatus) {
		this.aliquotId=aliquotId;
		this.aliquotName=aliquotName;
		this.maskStatus=maskStatus;
	}
	
	public AliquotBean(String aliquotName, ContainerBean container,
			String howCreated, String creator, String creationDate) {
		// TODO Auto-generated constructor stub
		this.aliquotName = aliquotName;
		this.container = container;
		this.howCreated = howCreated;
		this.creator = creator;
		this.creationDate = creationDate;
	}
	
	public AliquotBean(String aliquotId, String aliquotName, ContainerBean container,
			String howCreated, String creator, String creationDate) {
		// TODO Auto-generated constructor stub
		this.aliquotId=aliquotId;
		this.aliquotName = aliquotName;
		this.container = container;
		this.howCreated = howCreated;
		this.creator = creator;
		this.creationDate = creationDate;
	}

	public AliquotBean(String aliquotName, ContainerBean container,
			String howCreated, String creator, String creationDate,
			SampleBean sample) {
		// TODO Auto-generated constructor stub
		this(aliquotName, container, howCreated, creator, creationDate);
		this.sample = sample;
	}

	public AliquotBean(Aliquot aliquot) {
		this.aliquotId=StringUtils.convertToString(aliquot.getId());
		this.aliquotName = StringUtils.convertToString(aliquot.getName());
		this.container = new ContainerBean(aliquot);
		this.howCreated = StringUtils.convertToString(aliquot
				.getCreatedMethod());
		this.creator = StringUtils.convertToString(aliquot.getCreatedBy());
		this.creationDate = StringUtils.convertDateToString(aliquot
				.getCreatedDate(), CalabConstants.DATE_FORMAT);
		this.sample = new SampleBean(aliquot.getSample());
	}

	public ContainerBean getContainer() {
		return container;
	}

	public void setContainer(ContainerBean container) {
		this.container = container;
	}

	public String getHowCreated() {
		return howCreated;
	}

	public void setHowCreated(String howCreated) {
		this.howCreated = howCreated;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public SampleBean getSample() {
		return sample;
	}

	public void setSample(SampleBean sample) {
		this.sample = sample;
	}

	public String getAliquotId() {
		return aliquotId;
	}

	public void setAliquotId(String aliquotId) {
		this.aliquotId = aliquotId;
	}

	public String getAliquotName() {
		return aliquotName;
	}

	public void setAliquotName(String aliquotName) {
		this.aliquotName = aliquotName;
	}

	public String getMaskStatus() {
		return maskStatus;
	}

	public void setMaskStatus(String maskStatus) {
		this.maskStatus = maskStatus;
	}
}
