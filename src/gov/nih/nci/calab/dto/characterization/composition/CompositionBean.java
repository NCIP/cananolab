package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents shared properties of phyisical composition characterizations to be shown in
 * different physical composition view pages.
 * @author pansu
 *
 */
public class CompositionBean extends CharacterizationBean {
	private String numberOfElements;
	
	private List<ComposingElementBean> composingElements;

	public CompositionBean() {
		composingElements=new ArrayList<ComposingElementBean>();
	}

	public List<ComposingElementBean> getComposingElements() {
		return composingElements;
	}

	public void setComposingElements(List<ComposingElementBean> composingElements) {
		this.composingElements = composingElements;
	}
	
	public ComposingElementBean getElement(int ind) {
		return composingElements.get(ind);
	}

	public void setElement(int ind, ComposingElementBean composingElement) {
		composingElements.set(ind, composingElement);
	}

	public String getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(String numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

}
