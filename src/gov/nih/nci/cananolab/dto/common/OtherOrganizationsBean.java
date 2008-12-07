/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.PointOfContact;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shuang Cai
 *
 */
public class OtherOrganizationsBean {

	private List<OrganizationBean> ortherOrganizations = new ArrayList<OrganizationBean>();
	/**
	 * 
	 */
	public OtherOrganizationsBean() {
		// TODO Auto-generated constructor stub
	}
	public List<OrganizationBean> getOrtherOrganizations() {
		return ortherOrganizations;
	}
	public void setOrtherOrganizations(List<OrganizationBean> ortherOrganizations) {
		this.ortherOrganizations = ortherOrganizations;
	}

	public void addOrganization() {
		ortherOrganizations.add(new OrganizationBean());
	}
	
	public void removeOrganization(int ind) {
		ortherOrganizations.remove(ind);
	}
}
