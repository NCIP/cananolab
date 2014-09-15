package gov.nih.nci.cananolab.restful.view.edit;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SimpleFunctionBean {

	String type = "";
	String modality = "";
	String description = "";
	Long id = 0L;
	String targetId = "";
	String targetType = "";
	String speciesType = "";
	String targetName = "";
	String targetDescription ="";
	String createdBy ="";
	Date createdDate;
	List<Map<String, String>> targets;
		
	public List<Map<String, String>> getTargets() {
		return targets;
	}
	public void setTargets(List<Map<String, String>> targets) {
		this.targets = targets;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public String getSpeciesType() {
		return speciesType;
	}
	public void setSpeciesType(String speciesType) {
		this.speciesType = speciesType;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getTargetDescription() {
		return targetDescription;
	}
	public void setTargetDescription(String targetDescription) {
		this.targetDescription = targetDescription;
	}
	
	
}
