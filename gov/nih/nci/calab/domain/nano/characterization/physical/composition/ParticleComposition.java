package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

import java.util.ArrayList;
import java.util.Collection;

public class ParticleComposition extends Characterization {
//	public void setComposingElementCollection(
//			Collection<ComposingElement> element);
//
//	public Collection<ComposingElement> getComposingElementCollection();
	
	private Collection<ComposingElement> composingElementCollection = new ArrayList<ComposingElement>();
	
	public void setComposingElementCollection(
			Collection<ComposingElement> element) {
		this.composingElementCollection = element;
	}

	public Collection<ComposingElement> getComposingElementCollection() {
		return this.composingElementCollection;
	}

}
