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
			this.composingElements.add(elementBean);
		}
		this.setNumberOfElements(this.composingElements.size() + "");
	}

	public List<ComposingElementBean> getComposingElements() {
		return this.composingElements;
	}

	public void setComposingElements(
			List<ComposingElementBean> composingElements) {
		this.composingElements = composingElements;
	}

	public String getNumberOfElements() {
		return this.numberOfElements;
	}

	public void setNumberOfElements(String numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public void updateDomainObj(ParticleComposition doComp) {
		super.updateDomainObj(doComp);
		updateComposingElements(doComp);
	}

	// update domain object's composing element collection
	private void updateComposingElements(ParticleComposition doComp) {
		// copy collection
		List<ComposingElement> doComposingElementList = new ArrayList<ComposingElement>(
				doComp.getComposingElementCollection());
		// clear the existing collection
		doComp.getComposingElementCollection().clear();
		for (ComposingElementBean elementBean : getComposingElements()) {
			ComposingElement doElement = null;
			// if no id, add new domain object
			if (elementBean.getId() == null) {
				doElement = new ComposingElement();
			} else {
				// find domain object with the same ID and add the updated
				// domain object
				for (ComposingElement element : doComposingElementList) {
					if (element.getId().equals(new Long(elementBean.getId()))) {
						doElement = element;
						break;
					}
				}
			}
			elementBean.updateDomainObj(doElement);
			doComp.getComposingElementCollection().add(doElement);
		}
	}
}
