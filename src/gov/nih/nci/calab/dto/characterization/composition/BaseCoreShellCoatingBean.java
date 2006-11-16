package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ParticleComposition;
import gov.nih.nci.calab.service.util.CananoConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents properties of a base composition where there is one
 * core, one or more shells, one or more coatings to be shown in the view page.
 * 
 * @author pansu
 * 
 */
public class BaseCoreShellCoatingBean extends CompositionBean {
	private ComposingElementBean core = new ComposingElementBean();;

	private List<ComposingElementBean> shells = new ArrayList<ComposingElementBean>();

	private List<ComposingElementBean> coatings = new ArrayList<ComposingElementBean>();

	private String numberOfShells;

	private String numberOfCoatings;

	public BaseCoreShellCoatingBean() {
		super();
		core.setElementType(CananoConstants.CORE);
		getComposingElements().add(core);
	}

	public BaseCoreShellCoatingBean(ParticleComposition doComp) {
		super(doComp);
		core = new ComposingElementBean();
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

	public List<ComposingElementBean> getShells() {
		return shells;
	}

	public void setShells(List<ComposingElementBean> shells) {
		this.shells = shells;
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
}
