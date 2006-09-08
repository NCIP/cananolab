/**
 * 
 */
package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComposingElement;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.MetalParticleComposition;
import gov.nih.nci.calab.service.util.CananoConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents properties of a Metal Particle composition to be shown
 * in the view page.
 * 
 * @author zeng, pansu
 */
public class MetalParticleBean extends CompositionBean {
	private ComposingElementBean core;

	private List<ComposingElementBean> shells;

	private List<ComposingElementBean> coatings;

	private String numberOfShells;

	private String numberOfCoatings;

	public MetalParticleBean() {
		super();
		shells = new ArrayList<ComposingElementBean>();
		coatings = new ArrayList<ComposingElementBean>();
		List<ComposingElementBean> composingElements = getComposingElements();
		core = new ComposingElementBean();
		core.setElementType(CananoConstants.CORE);
		composingElements.add(core);
		composingElements.addAll(shells);
		composingElements.addAll(coatings);
		setComposingElements(composingElements);
	}

	public MetalParticleBean(MetalParticleComposition metalParticle) {
		this.setId(metalParticle.getId().toString());
		List<ComposingElementBean> shellBeans = new ArrayList<ComposingElementBean>();
		List<ComposingElementBean> coatingBeans = new ArrayList<ComposingElementBean>();
		for (ComposingElement element : metalParticle
				.getComposingElementCollection()) {
			if (element.getElementType().equals(CananoConstants.CORE)) {
				core = new ComposingElementBean(element);
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

	public ComposingElementBean getShell(int ind) {
		return shells.get(ind);
	}

	public void setShell(int ind, ComposingElementBean shell) {
		shells.set(ind, shell);
	}

	public ComposingElementBean getCoating(int ind) {
		return coatings.get(ind);
	}

	public void setCoating(int ind, ComposingElementBean coating) {
		coatings.set(ind, coating);
	}

	public Characterization getDomainObj() {
		MetalParticleComposition doComp = new MetalParticleComposition();
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
