package gov.nih.nci.cananolab.dto.admin;

import java.util.Date;

/**
 * Bean for retrieving site preference from [administration] table.
 */
public class SitePreferenceBean {
	
	/**
	 * The site name.
	 */
	private String siteName;
	
	/**
	 * The site logo file name.
	 */
	private String siteLogoFilename;
	
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

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteLogoFilename(String siteLogoFilename) {
		this.siteLogoFilename = siteLogoFilename;
	}

	public String getSiteLogoFilename() {
		return siteLogoFilename;
	}
}
