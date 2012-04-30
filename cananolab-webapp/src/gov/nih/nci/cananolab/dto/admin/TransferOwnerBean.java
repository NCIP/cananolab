package gov.nih.nci.cananolab.dto.admin;

import java.util.Date;
import java.util.Map;

/**
 * Bean for retrieving site preference from [administration] table.
 */
public class TransferOwnerBean {
	
	/**
	 * The site name.
	 */
	private String currentOwner;
	
	/**
	 * The site logo file name.
	 */
	private String newOwner;
	
	private Map<String,String> samples;
	private Map<String, String> publications;
	private Map<String, String> protocols;
	private Map<String,String> collaborationGroups;
	private String[] dataType;
	
	private String[] sampleIds;
	private String[] selectedSampleIds;
	

	public String[] getSampleIds() {
		return sampleIds;
	}

	public void setSampleIds(String[] sampleIds) {
		this.sampleIds = sampleIds;
	}

	public String[] getSelectedSampleIds() {
		return selectedSampleIds;
	}

	public void setSelectedSampleIds(String[] selectedSampleIds) {
		this.selectedSampleIds = selectedSampleIds;
	}

	public String[] getDataType() {
		return dataType;
	}

	public void setDataType(String[] dataType) {
		this.dataType = dataType;
	}

	public Map<String, String> getSamples() {
		return samples;
	}

	public void setSamples(Map<String, String> samples) {
		this.samples = samples;
	}

	public Map<String, String> getPublications() {
		return publications;
	}

	public void setPublications(Map<String, String> publications) {
		this.publications = publications;
	}

	public Map<String, String> getProtocols() {
		return protocols;
	}

	public void setProtocols(Map<String, String> protocols) {
		this.protocols = protocols;
	}

	public Map<String, String> getCollaborationGroups() {
		return collaborationGroups;
	}

	public void setCollaborationGroups(Map<String, String> collaborationGroups) {
		this.collaborationGroups = collaborationGroups;
	}

	/**
	 * Indicates the person or authoritative body who brought the item into
	 * existence.
	 **/
	private String updatedBy;

	/**
	 * The date of the process by which something is brought into existence;
	 * having been brought into existence.
	 **/
	private Date updatedDate;
	
	public String getCurrentOwner() {
		return currentOwner;
	}

	public void setCurrentOwner(String currentOwner) {
		this.currentOwner = currentOwner;
	}

	public String getNewOwner() {
		return newOwner;
	}

	public void setNewOwner(String newOwner) {
		this.newOwner = newOwner;
	}

	

	/**
	 * Retrieves the value of updatedBy attribute
	 * 
	 * @return updatedBy
	 **/
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * Sets the value of updatedBy attribute
	 **/
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * Retrieves the value of updatedDate attribute
	 * 
	 * @return updatedDate
	 **/
	public java.util.Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets the value of updatedDate attribute
	 **/
	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	
}
