package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
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
		if (composingElement.getInherentFunctionCollection() != null) {
			for (Function function : composingElement
					.getInherentFunctionCollection()) {
				inherentFunctions.add(new FunctionBean(function));
			}
			Collections.sort(inherentFunctions,
					new CaNanoLabComparators.FunctionBeanDateComparator());
		}
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
			String createdBy, int index) throws Exception {
		if (domainComposingElement.getId() == null
				|| domainComposingElement.getCreatedBy() != null
				&& domainComposingElement.getCreatedBy().equals(
						CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainComposingElement.setCreatedBy(createdBy);
			// domainComposingElement.setCreatedDate(new Date());
			// fix for MySQL database, which supports precision only up to
			// seconds
			domainComposingElement.setCreatedDate(DateUtil
					.addSecondsToCurrentDate(index));
		}
		if (domainComposingElement.getInherentFunctionCollection() != null) {
			domainComposingElement.getInherentFunctionCollection().clear();
		} else {
			domainComposingElement
					.setInherentFunctionCollection(new HashSet<Function>());
		}
		int i=0;
		for (FunctionBean functionBean : inherentFunctions) {
			functionBean.setupDomainFunction(typeToClass, createdBy, i);
			domainComposingElement.getInherentFunctionCollection().add(
					functionBean.getDomainFunction());
			i++;
		}
	}
}
