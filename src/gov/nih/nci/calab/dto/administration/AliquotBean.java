package gov.nih.nci.calab.dto.administration;

/**
 * This class includes all properties of an aliquot that need to be viewed and
 * saved.
 * 
 * @author pansu
 * 
 */

/* CVS $Id: AliquotBean.java,v 1.4 2006-04-04 15:31:29 pansu Exp $ */

public class AliquotBean {
	private String aliquotId;

	private ContainerBean container;

	private String howCreated;
	
	private String creator;
	
	private String creationDate;

	private SampleBean sample;
	
	public AliquotBean() {
		container=new ContainerBean();
		sample=new SampleBean();
	}
	
	public AliquotBean(String aliquotId, ContainerBean container,
			String howCreated, String creator, String creationDate) {
		// TODO Auto-generated constructor stub
		this.aliquotId = aliquotId;
		this.container = container;
		this.howCreated = howCreated;
		this.creator=creator;
		this.creationDate=creationDate;
	}
	
	public AliquotBean(String aliquotId, ContainerBean container,
			String howCreated, String creator, String creationDate, SampleBean sample) {
		// TODO Auto-generated constructor stub
		this(aliquotId, container, howCreated, creator, creationDate);
		this.sample=sample;
	}

	public String getAliquotId() {
		return aliquotId;
	}

	public void setAliquotId(String aliquotId) {
		this.aliquotId = aliquotId;
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

}
