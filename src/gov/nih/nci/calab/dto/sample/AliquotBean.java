package gov.nih.nci.calab.dto.sample;

import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.dto.common.SortableName;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.Date;

/**
 * This class includes all properties of an aliquot that need to be viewed and
 * saved.
 * 
 * @author pansu
 * 
 */

/* CVS $Id: AliquotBean.java,v 1.2 2007-11-08 20:41:35 pansu Exp $ */

public class AliquotBean {
	private String aliquotId = "";

	private String aliquotName = "";

	private ContainerBean container;

	private String howCreated = "";

	private String creator = "";

	private Date creationDate;

	private String creationDateStr = "";

	private SampleBean sample;

	private String maskStatus = "";

	public AliquotBean() {
		this.container = new ContainerBean();
		this.sample = new SampleBean();
	}

	// used in WorkflowResultBean
	public AliquotBean(String aliquotName, String maskStatus) {
		this.aliquotName = aliquotName;
		this.maskStatus = (maskStatus.length() == 0 && aliquotName.length() > 0) ? CaNanoLabConstants.ACTIVE_STATUS
				: maskStatus;
	}

	public AliquotBean(String aliquotId, String aliquotName, String maskStatus) {
		this.aliquotId = aliquotId;
		this.aliquotName = aliquotName;
		this.maskStatus = (maskStatus.length() == 0 && aliquotName.length() > 0) ? CaNanoLabConstants.ACTIVE_STATUS
				: maskStatus;
	}

	public AliquotBean(String aliquotName, ContainerBean container,
			String howCreated, String creator, Date creationDate) {
		this.aliquotName = aliquotName;
		this.container = container;
		this.howCreated = howCreated;
		this.creator = creator;
		this.creationDate = creationDate;
	}

	public AliquotBean(String aliquotId, String aliquotName,
			ContainerBean container, String howCreated, String creator,
			Date creationDate) {
		this.aliquotId = aliquotId;
		this.aliquotName = aliquotName;
		this.container = container;
		this.howCreated = howCreated;
		this.creator = creator;
		this.creationDate = creationDate;
	}

	public AliquotBean(String aliquotName, ContainerBean container,
			String howCreated, String creator, Date creationDate,
			SampleBean sample) {
		this(aliquotName, container, howCreated, creator, creationDate);
		this.sample = sample;
	}

	public AliquotBean(Aliquot aliquot) {
		this.aliquotId = StringUtils.convertToString(aliquot.getId());
		this.aliquotName = StringUtils.convertToString(aliquot.getName());
		this.container = new ContainerBean(aliquot);
		this.howCreated = StringUtils.convertToString(aliquot
				.getCreatedMethod());
		this.creator = StringUtils.convertToString(aliquot.getCreatedBy());
		this.creationDate = aliquot.getCreatedDate();
		this.sample = new SampleBean(aliquot.getSample());
	}

	public ContainerBean getContainer() {
		return this.container;
	}

	public void setContainer(ContainerBean container) {
		this.container = container;
	}

	public String getHowCreated() {
		return this.howCreated;
	}

	public void setHowCreated(String howCreated) {
		this.howCreated = howCreated;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public SampleBean getSample() {
		return this.sample;
	}

	public void setSample(SampleBean sample) {
		this.sample = sample;
	}

	public String getAliquotId() {
		return this.aliquotId;
	}

	public void setAliquotId(String aliquotId) {
		this.aliquotId = aliquotId;
	}

	public String getAliquotName() {
		return this.aliquotName;
	}

	public void setAliquotName(String aliquotName) {
		this.aliquotName = aliquotName;
	}

	public String getMaskStatus() {
		return this.maskStatus;
	}

	public void setMaskStatus(String maskStatus) {
		this.maskStatus = (maskStatus.length() == 0 && getAliquotName()
				.length() > 0) ? CaNanoLabConstants.ACTIVE_STATUS : maskStatus;
	}

	public String getCreationDateStr() {
		if (this.creationDate != null) {
			this.creationDateStr = StringUtils.convertDateToString(this.creationDate,
					CaNanoLabConstants.DATE_FORMAT);
		}
		return this.creationDateStr;
	}

	// used for display tag
	public SortableName getSortableName() {
		return new SortableName(this.aliquotName);
	}
}
