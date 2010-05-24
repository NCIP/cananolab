package gov.nih.nci.cananolab.dto.admin;

import java.util.Date;

/**
 * Bean for retrieving visitor count from [administration] table.
 */
public class VisitorCountBean {
	/**
	 * The visitor count.
	 */
	private Integer visitorCount;
	
	/**
	 * The count start date.
	 */
	private Date counterStartDate;
	
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

	public void setVisitorCount(Integer visitorCount) {
		this.visitorCount = visitorCount;
	}

	public Integer getVisitorCount() {
		return visitorCount;
	}

	public void setCounterStartDate(Date counterStartDate) {
		this.counterStartDate = counterStartDate;
	}

	public Date getCounterStartDate() {
		return counterStartDate;
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
