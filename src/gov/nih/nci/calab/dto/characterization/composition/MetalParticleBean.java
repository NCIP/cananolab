/**
 * 
 */
package gov.nih.nci.calab.dto.characterization.composition;

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
	private ComposingElementBean core = new ComposingElementBean();;

	private List<ComposingElementBean> shells = new ArrayList<ComposingElementBean>();

	private List<ComposingElementBean> coatings = new ArrayList<ComposingElementBean>();

	private String numberOfShells;

	private String numberOfCoatings;

	public MetalParticleBean() {	
		super();
		core.setElementType(CananoConstants.CORE);
		getComposingElements().add(core);
	}

	public MetalParticleBean(MetalParticleComposition metalParticle) {
		super(metalParticle);		
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
		getComposingElements().addAll(coatings);
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
		getComposingElements().addAll(shells);
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
}
