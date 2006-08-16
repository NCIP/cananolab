/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

import gov.nih.nci.calab.domain.nano.particle.BaseNanoParticle;

import java.util.Collection;

/**
 * @author zengje
 *
 */
public interface BaseCharacterization {
	public void setId(Long id);
	public Long getId();
	public void setOrigin(String origin);
	public String getOrigin();
	public void setCharacterizationCategory(String category);
	public String getCharacterizationCategory();
	public void setParticleCollection(Collection<BaseNanoParticle> particles);
	public Collection<BaseNanoParticle> getParticleCollection();
}
