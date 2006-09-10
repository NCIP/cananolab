package gov.nih.nci.calab.dto.characterization.composition;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComposingElement;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;

/**
 * This class represents properties of a Liposome composition to be shown in the
 * view page.
 * 
 * @author pansu
 * 
 */
public class LiposomeBean extends CompositionBean {
	private String polymerized="no";

	private String polymerName;

	public LiposomeBean() {
		super();
	}

	public LiposomeBean(LiposomeComposition liposome) {
		this.setId(liposome.getId().toString());
		this.polymerized = (liposome.isPolymerized()) ? "Yes" : "No";
		this.polymerName = liposome.getPolymerName();
		List<ComposingElementBean> elementBeans = new ArrayList<ComposingElementBean>();
		for (ComposingElement element : liposome
				.getComposingElementCollection()) {
			ComposingElementBean elementBean = new ComposingElementBean(element);
			elementBeans.add(elementBean);
		}
		this.setComposingElements(elementBeans);
		this.setNumberOfElements(elementBeans.size() + "");
	}

	public String getPolymerized() {
		return polymerized;
	}

	public void setPolymerized(String polymerized) {
		this.polymerized = polymerized;
	}

	public String getPolymerName() {
		return polymerName;
	}

	public Characterization getDomainObj() {
		LiposomeComposition doComp = new LiposomeComposition();
		boolean polymerizedStatus = (polymerized.equalsIgnoreCase("yes")) ? true
				: false;
		doComp.setPolymerized(polymerizedStatus);
		doComp.setPolymerName(polymerName);
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

	public void setPolymerName(String polymerName) {
		this.polymerName = polymerName;
	}

}
