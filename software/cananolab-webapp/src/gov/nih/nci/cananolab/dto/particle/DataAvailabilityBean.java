/**
 * 
 */
package gov.nih.nci.cananolab.dto.particle;

/**
 * @author lethai
 *
 */
public class DataAvailabilityBean {
	
	private Long sampleId;
	private String datasourceName;
	private String availableEntityName;
	private String createdBy;
	private java.util.Date createdDate;
	private String updatedBy;
	private java.util.Date updatedDate;
	public Long getSampleId() {
		return sampleId;
	}
	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}
	public String getDatasourceName() {
		return datasourceName;
	}
	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}
	public String getAvailableEntityName() {
		return availableEntityName;
	}
	public void setAvailableEntityName(String availableEntityName) {
		this.availableEntityName = availableEntityName;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public java.util.Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public java.util.Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	

}
