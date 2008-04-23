package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
	}

	public ComposingElementBean() {
	}

	public ComposingElement getDomainComposingElement() {
		return domainComposingElement;
	}

	public List<FunctionBean> getInherentFunctions() {
		if (domainComposingElement.getInherentFunctionCollection() != null) {
			domainComposingElement.getInherentFunctionCollection().clear();
		} else {
			domainComposingElement
					.setInherentFunctionCollection(new HashSet<Function>());
		}
		for (FunctionBean functionBean : inherentFunctions) {
			domainComposingElement.getInherentFunctionCollection().add(
					functionBean.getDomainFunction());
			//TODO add date to function
		}
		return inherentFunctions;
	}

	public void addFunction() {
		inherentFunctions.add(new FunctionBean());
	}

	public void removeFunction(int ind) {
		inherentFunctions.remove(ind);
	}
}
