package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

import java.util.Collection;

public interface ParticleComposition extends Characterization {
	public void setComposingElementCollection(
			Collection<ComposingElement> element);

	public Collection<ComposingElement> getComposingElementCollection();
}
