package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Represents the view bean for ComposingElement domain object
 * 
 * @author pansu
 * 
 */
public class ComposingElementBean {
	private ComposingElement domainComposingElement = new ComposingElement();

	private List<FunctionBean> inherentFunctions = new ArrayList<FunctionBean>();

	public ComposingElementBean(ComposingElement composingElement) {
		this.domainComposingElement = composingElement;
		for (Function function : composingElement
				.getInherentFunctionCollection()) {
			inherentFunctions.add(new FunctionBean(function));
		}
		Collections.sort(inherentFunctions,
				new CaNanoLabComparators.FunctionBeanDateComparator());
	}

	public ComposingElementBean() {
	}

	public ComposingElement getDomainComposingElement() {
		return domainComposingElement;
	}

	public List<FunctionBean> getInherentFunctions() {
		return inherentFunctions;
	}

	public void addFunction() {
		inherentFunctions.add(new FunctionBean());
	}

	public void removeFunction(int ind) {
		inherentFunctions.remove(ind);
	}

	public String getDisplayName() {
		return getDomainComposingElement().getType() + ":"
				+ getDomainComposingElement().getName();
	}

	// for dwr ajax in bodyChemicalAssociation.jsp
	public String getDomainComposingElementId() {
		return getDomainComposingElement().getId().toString();
	}

	public void setupDomainComposingElement(Map<String, String> typeToClass,
			String createdBy) throws Exception {
		if (domainComposingElement.getId() == null
				|| domainComposingElement.getCreatedBy().equals(
						CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainComposingElement.setCreatedBy(createdBy);
			domainComposingElement.setCreatedDate(new Date());
		}
		if (domainComposingElement.getInherentFunctionCollection() != null) {
			domainComposingElement.getInherentFunctionCollection().clear();
		} else {
			domainComposingElement
					.setInherentFunctionCollection(new HashSet<Function>());
		}
		for (FunctionBean functionBean : inherentFunctions) {
			functionBean.setupDomainFunction(typeToClass, createdBy);
			domainComposingElement.getInherentFunctionCollection().add(
					functionBean.getDomainFunction());
		}
	}
}
