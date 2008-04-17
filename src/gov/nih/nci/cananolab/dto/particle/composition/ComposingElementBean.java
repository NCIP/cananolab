package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the view bean for ComposingElement domain object
 * 
 * @author pansu
 * 
 */
public class ComposingElementBean {
	private ComposingElement domainComposingElement=new ComposingElement();

	private List<FunctionBean> inherentFunctions = new ArrayList<FunctionBean>();

	public ComposingElementBean(ComposingElement composingElement) {
		this.domainComposingElement = composingElement;
		for (Function function : composingElement
				.getInherentFunctionCollection()) {
			inherentFunctions.add(new FunctionBean(function));
		}
	}

	public ComposingElementBean() {
		FunctionBean fb = new FunctionBean();
		inherentFunctions.add(fb);
	}

	public ComposingElement getDomainComposingElement() {
		return domainComposingElement;
	}

	public List<FunctionBean> getInherentFunctions() {
		Set<Function> domainFunctions = new HashSet<Function>();
		for (FunctionBean functionBean : inherentFunctions) {
			domainFunctions.add(functionBean.getDomainFunction());
		}
		domainComposingElement.setInherentFunctionCollection(domainFunctions);
		return inherentFunctions;
	}
}
