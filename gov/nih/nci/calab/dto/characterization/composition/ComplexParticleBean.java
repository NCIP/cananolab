package gov.nih.nci.calab.dto.characterization.composition;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComplexComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComposingElement;

/**
 * This class represents properties of a Complext Nanoparticle composition to be
 * shown in the view page.
 * 
 * @author pansu
 * 
 */
public class ComplexParticleBean extends CompositionBean {

	public ComplexParticleBean() {		
	}

	public ComplexParticleBean(ComplexComposition complex) {
		this.setId(complex.getId().toString());
		List<ComposingElementBean> elementBeans = new ArrayList<ComposingElementBean>();
		for (ComposingElement element : complex
				.getComposingElementCollection()) {
			ComposingElementBean elementBean = new ComposingElementBean(element);
			elementBeans.add(elementBean);
		}
		this.setComposingElements(elementBeans);
		this.setNumberOfElements(elementBeans.size() + "");	
	}

	public Characterization getDomainObj() {
		ComplexComposition doComp = new ComplexComposition();		
		doComp.setSource(getCharacterizationSource());
		doComp.setIdentificationName(getViewTitle());
		doComp.setDescription(getDescription());
		if (getId()!=null&&getId().length() > 0) {
			doComp.setId(new Long(getId()));
		}
		for (ComposingElementBean element : getComposingElements()) {
			doComp.getComposingElementCollection().add(element.getDomainObj());
		}
		return doComp;
	}
}
