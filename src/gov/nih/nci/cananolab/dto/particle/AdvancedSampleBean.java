package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents shared properties of samples resulted from advanced
 * sample search.
 * 
 * @author pansu
 * 
 */
public class AdvancedSampleBean {
	private String sampleName;
	private String location; // e.g. NCICB, NCL, WUSTL, etc.
	private String sampleId;
	private Map<String, String> attributeMap = new LinkedHashMap<String, String>();

	public AdvancedSampleBean() {
	}

	public AdvancedSampleBean(String sampleName, String location) {
		super();
		this.sampleName = sampleName;
		this.location = location;
	}

	public AdvancedSampleBean(String sampleName, String sampleId,
			String location, Map<String, String> attributeMap) {
		this(sampleName, location);
		this.sampleId = sampleId;
		this.attributeMap = attributeMap;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
