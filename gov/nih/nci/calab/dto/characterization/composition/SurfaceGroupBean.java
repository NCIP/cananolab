package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.SurfaceGroup;
import gov.nih.nci.calab.service.util.CananoConstants;

/**
 * This class represents properties of a SurfaceGroup to be shown in the view page.
 * @author pansu
 *
 */
public class SurfaceGroupBean {
	private String id;
	private String name;
	private String otherName;
	private String modifier;
	
	public SurfaceGroupBean() {
		
	}
	public SurfaceGroupBean(SurfaceGroup surfaceGroup) {
		this.id=surfaceGroup.getId().toString();
		this.name=surfaceGroup.getName();
		this.modifier=surfaceGroup.getModifier();		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public SurfaceGroup getDomainObj() {
		SurfaceGroup surfaceGroup = new SurfaceGroup();
		if (name.equals(CananoConstants.OTHER)) {
			surfaceGroup.setName(otherName);
		} else {
			surfaceGroup.setName(name);	
		}
		surfaceGroup.setModifier(modifier);
		if (getId()!=null&&getId().length() > 0) {
			surfaceGroup.setId(new Long(getId()));
		}
		return surfaceGroup;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
}
