package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComposingElement;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ParticleComposition;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents shared properties of phyisical composition
 * characterizations to be shown in different physical composition view pages.
 * 
 * @author pansu
 * 
 */
public class CompositionBean extends CharacterizationBean {
	private String numberOfElements;

	private List<ComposingElementBean> composingElements = new ArrayList<ComposingElementBean>();

	public CompositionBean() {
	}

	public CompositionBean(ParticleComposition composition) {
		super(composition);
		for (ComposingElement element : composition
				.getComposingElementCollection()) {
			ComposingElementBean elementBean = new ComposingElementBean(element);
			composingElements.add(elementBean);
		}
		this.setNumberOfElements(composingElements.size() + "");
	}

	public List<ComposingElementBean> getComposingElements() {
		return composingElements;
	}

	public void setComposingElements(
			List<ComposingElementBean> composingElements) {
		this.composingElements = composingElements;
	}

	public String getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(String numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public void updateDomainObj(ParticleComposition doComp) {
		super.updateDomainObj(doComp);
		for (ComposingElementBean element : getComposingElements()) {
			doComp.getComposingElementCollection().add(element.getDomainObj());
		}
	}
}
