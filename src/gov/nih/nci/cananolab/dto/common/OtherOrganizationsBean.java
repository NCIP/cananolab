/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shuang Cai
 *
 */
public class OtherOrganizationsBean {

	private List<OrganizationBean> otherOrganizations = new ArrayList<OrganizationBean>();
	/**
	 * 
	 */
	public OtherOrganizationsBean() {
		// TODO Auto-generated constructor stub
	}
	public List<OrganizationBean> getOtherOrganizations() {
		return otherOrganizations;
	}
	public void setOtherOrganizations(List<OrganizationBean> otherOrganizations) {
		this.otherOrganizations = otherOrganizations;
	}

	public void addOrganization() {
		otherOrganizations.add(new OrganizationBean());
	}
	
	public void removeOrganization(int ind) {
		otherOrganizations.remove(ind);
	}
}
