package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComplexComposition;

/**
 * This class represents properties of a Complext Nanoparticle composition to be
 * shown in the view page.
 * 
 * @author pansu
 * 
 */
public class ComplexParticleBean extends CompositionBean {
	private String name;

	public ComplexParticleBean() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Characterization getDomainObj() {
		ComplexComposition doComp = new ComplexComposition();
		doComp.setName(name);
		doComp.setSource(getCharacterizationSource());
		doComp.setClassification(getCharacterizationClassification());
		doComp.setIdentificationName(getViewTitle());
		doComp.setDescription(getDescription());
		for (ComposingElementBean element : getComposingElements()) {
			doComp.getComposingElementCollection().add(element.getDomainObj());
		}
		return doComp;
	}
}
