package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
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
public class QuantumDotBean extends CompositionBean {
	private ComposingElementBean core = new ComposingElementBean();;

	private List<ComposingElementBean> shells = new ArrayList<ComposingElementBean>();

	private List<ComposingElementBean> coatings = new ArrayList<ComposingElementBean>();

	private String numberOfShells;

	private String numberOfCoatings;

	public QuantumDotBean() {
	}

	public QuantumDotBean(QuantumDotComposition quantumDot) {
		super(quantumDot);
		for (ComposingElementBean element : getComposingElements()) {
			if (element.getElementType().equals(CananoConstants.CORE)) {
				core = element;
			} else if (element.getElementType().equals(CananoConstants.COATING)) {
				coatings.add(element);
			} else if (element.getElementType().equals(CananoConstants.SHELL)) {
				shells.add(element);
			}
		}
		this.setNumberOfShells(shells.size() + "");
		this.setNumberOfCoatings(coatings.size() + "");
	}

	public List<ComposingElementBean> getCoatings() {
		return coatings;
	}

	public void setCoatings(List<ComposingElementBean> coatings) {
		this.coatings = coatings;
	}

	public ComposingElementBean getCore() {
		return core;
	}

	public void setCore(ComposingElementBean core) {
		this.core = core;
	}

	public String getNumberOfCoatings() {
		return numberOfCoatings;
	}

	public void setNumberOfCoatings(String numberOfCoatings) {
		this.numberOfCoatings = numberOfCoatings;
	}

	public String getNumberOfShells() {
		return numberOfShells;
	}

	public void setNumberOfShells(String numberOfShells) {
		this.numberOfShells = numberOfShells;
	}

	public List<ComposingElementBean> getShells() {
		return shells;
	}

	public void setShells(List<ComposingElementBean> shells) {
		this.shells = shells;
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
