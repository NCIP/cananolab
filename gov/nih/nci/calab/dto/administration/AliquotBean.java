package gov.nih.nci.calab.dto.administration;

/**
 * This class includes all properties of an aliquot that need to be viewed and
 * saved.
 * 
 * @author pansu
 * 
 */

/* CVS $Id: AliquotBean.java,v 1.2 2006-03-17 21:44:08 pansu Exp $ */

public class AliquotBean {
	private String aliquotId;

	private ContainerBean container;

	private String howCreated;

	public AliquotBean() {
		container=new ContainerBean();
	}
	
	public AliquotBean(String aliquotId, ContainerBean container,
			String howCreated) {
		// TODO Auto-generated constructor stub
		this.aliquotId = aliquotId;
		this.container = container;
		this.howCreated = howCreated;
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

}
