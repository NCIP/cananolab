/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.Collection;

/**
 * @author zengje
 *
 */
public interface Characterization {
	public void setId(Long id);
	public Long getId();
	public void setSource(String source);
	public String getSource();
	public void setClassification(String classification);
	public String getClassification();
	public void setIdentificationName(String name);
	public String getIdentificationName();
	public void setDescription(String description);
	public String getDescription();
	public void setNanoparticleCollection(Collection<Nanoparticle> nanoparticleCollection);
	public Collection<Nanoparticle> getNanoparticleCollection();
}
