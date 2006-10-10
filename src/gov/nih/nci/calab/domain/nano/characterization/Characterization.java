/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

import gov.nih.nci.calab.domain.Instrument;

import java.util.Collection;
import java.util.Date;


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
	public void setName(String name);
	public String getName();
	public void setCreatedBy(String createdBy);
	public String getCreatedBy();
	public void setCreatedDate(Date createdDate);
	public Date getCreatedDate();
	
	public void setDerivedBioAssayDataCollection(Collection<DerivedBioAssayData> derivedBioAssayData);
	public Collection<DerivedBioAssayData> getDerivedBioAssayDataCollection();

	
	public void setInstrument(Instrument instrument);
	public Instrument getInstrument();
	
	public void setCharacterizationProtocol(CharacterizationProtocol protocol);
	public CharacterizationProtocol getCharacterizationProtocol();
}
