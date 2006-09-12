/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

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
}
