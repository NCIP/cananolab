/**
 * 
 */
package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.function.Linkage;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengje
 * 
 */
public class FunctionBean {

	private String id;

	private String type;

	private String activationMethod;

	private String description;

	private String viewTitle;

	private List<LinkageBean> linkages = new ArrayList<LinkageBean>();

	/**
	 * 
	 */
	public FunctionBean() {

	}

	public FunctionBean(String id, String type, String viewTitle) {
		this.id = id;
		this.type = type;
		this.viewTitle = viewTitle;
	}

	public FunctionBean(Function function) {
		this.id = function.getId().toString();
		this.type = function.getType();
		this.activationMethod = function.getActivationMethod();
		this.description = function.getDescription();
		this.viewTitle = function.getIdentificationName();
		for (Linkage linkage : function.getLinkageCollection()) {
			this.linkages.add(new LinkageBean(linkage));
		}
	}

	public String getActivationMethod() {
		return this.activationMethod;
	}

	public void setActivationMethod(String activationMethod) {
		this.activationMethod = activationMethod;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<LinkageBean> getLinkages() {
		return this.linkages;
	}

	public void setLinkages(List<LinkageBean> linkages) {
		this.linkages = linkages;
	}

	public int getNumberOfLinkages() {
		return this.linkages.size();
	}

	public String getViewTitle() {
		// get only the first number of characters of the title
		if (this.viewTitle != null
				&& this.viewTitle.length() > CaNanoLabConstants.MAX_VIEW_TITLE_LENGTH) {
			return this.viewTitle.substring(0,
					CaNanoLabConstants.MAX_VIEW_TITLE_LENGTH);
		}
		return this.viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void updateDomainObj(Function doFunction) {
		doFunction.setActivationMethod(this.activationMethod);
		doFunction.setDescription(this.description);
		doFunction.setIdentificationName(this.viewTitle);
		doFunction.setType(this.type);
		updateLinkages(doFunction);
	}

	private void updateLinkages(Function doFunction) {
		// copy collection
		List<Linkage> doLinkages = new ArrayList<Linkage>(doFunction
				.getLinkageCollection());
		// clear the existing collection
		doFunction.getLinkageCollection().clear();
		for (LinkageBean linkageBean : getLinkages()) {
			Linkage doLinkage = null;
			// if no id, add new domain object
			if (linkageBean.getId() == null) {
				doLinkage = linkageBean.getDomainObj();
			} else {
				// find domain object with the same ID and add the updated
				// domain object
				if (doLinkages.size() > 0) {
					for (Linkage aDoLinkage : doLinkages) {
						// if agent type is changed, have to create new linkage
						// domain obj
						if (aDoLinkage.getAgent().getClass().getSimpleName()
								.equals(linkageBean.getAgent().getType())) {
							if (aDoLinkage.getId().equals(
									new Long(linkageBean.getId()))) {
								// if type is not changed, update the domainObj
								if (aDoLinkage.getClass().getSimpleName()
										.equals(linkageBean.getType())) {
									doLinkage = aDoLinkage;
									linkageBean.updateDomainObj(doLinkage);
								} else {
									doLinkage = linkageBean.getDomainObj();
								}
								break;
							}
						} else {
							doLinkage = linkageBean.getDomainObj();
						}
					}
				} else {
					doLinkage = linkageBean.getDomainObj();
				}
			}
			doFunction.getLinkageCollection().add(doLinkage);
		}
	}
}
