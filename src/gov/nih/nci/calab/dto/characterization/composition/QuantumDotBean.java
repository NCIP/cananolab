package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComposingElement;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.QuantumDotComposition;
import gov.nih.nci.calab.service.util.CananoConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents properties of a Quantum Dot composition to be shown in
 * the view page.
 * 
 * @author zeng, pansu
 * 
 */
public class QuantumDotBean extends MetalParticleBean {

	public QuantumDotBean() {

	}

	public QuantumDotBean(QuantumDotComposition quantumDot) {
		this.setId(quantumDot.getId().toString());
		List<ComposingElementBean> shellBeans = new ArrayList<ComposingElementBean>();
		List<ComposingElementBean> coatingBeans = new ArrayList<ComposingElementBean>();
		for (ComposingElement element : quantumDot
				.getComposingElementCollection()) {
			if (element.getElementType().equals(CananoConstants.CORE)) {
				this.setCore(new ComposingElementBean(element));
			} else if (element.getElementType().equals(CananoConstants.COATING)) {
				coatingBeans.add(new ComposingElementBean(element));
			} else if (element.getElementType().equals(CananoConstants.SHELL)) {
				shellBeans.add(new ComposingElementBean(element));
			}
		}
		this.setShells(shellBeans);
		this.setNumberOfShells(shellBeans.size() + "");

		this.setCoatings(coatingBeans);
		this.setNumberOfCoatings(coatingBeans.size() + "");
	}

	public Characterization getDomainObj() {
		QuantumDotComposition doComp = new QuantumDotComposition();
		doComp.setSource(getCharacterizationSource());
		doComp.setIdentificationName(getViewTitle());
		doComp.setDescription(getDescription());
		if (getId() != null && getId().length() > 0) {
			doComp.setId(new Long(getId()));
		}
		for (ComposingElementBean element : getComposingElements()) {
			doComp.getComposingElementCollection().add(element.getDomainObj());
		}
		return doComp;
	}
}
