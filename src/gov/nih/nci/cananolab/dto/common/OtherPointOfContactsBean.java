/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanq, cais
 *
 */
public class OtherPointOfContactsBean {

	private List<PointOfContactBean> otherPointOfContacts = new ArrayList<PointOfContactBean>();
	/**
	 * 
	 */
	public OtherPointOfContactsBean() {
		// TODO Auto-generated constructor stub
	}
	public List<PointOfContactBean> getOtherPointOfContacts() {
		return otherPointOfContacts;
	}
	public void setOtherPointOfContacts(List<PointOfContactBean> otherPointOfContacts) {
		this.otherPointOfContacts = otherPointOfContacts;
	}

	public void addPointOfContact() {
		otherPointOfContacts.add(new PointOfContactBean());
	}
	
	public void removePointOfContact(int ind) {
		otherPointOfContacts.remove(ind);
	}
}
