package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the view bean for ComposingElement domain object
 * 
 * @author pansu
 * 
 */
public class ComposingElementBean {
	private ComposingElement composingElement=new ComposingElement();

	private List<FunctionBean> inherentFunctions = new ArrayList<FunctionBean>();

	public ComposingElementBean(ComposingElement composingElement) {
		this.composingElement = composingElement;
		for (Function function : composingElement
				.getInherentFunctionCollection()) {
			inherentFunctions.add(new FunctionBean(function));
		}
	}

	public ComposingElementBean() {
	}

	public ComposingElement getComposingElement() {
		return composingElement;
	}

	public void setComposingElement(ComposingElement composingElement) {
		this.composingElement = composingElement;
	}

	public List<FunctionBean> getInherentFunctions() {
		return inherentFunctions;
	}

	public void setInherentFunctions(List<FunctionBean> inherentFunctions) {
		this.inherentFunctions = inherentFunctions;
		List<Function> domainFunctions = new ArrayList<Function>();
		for (FunctionBean functionBean : inherentFunctions) {
			domainFunctions.add(functionBean.getDomainFunction());
		}
		composingElement.setInherentFunctionCollection(domainFunctions);
	}
}
