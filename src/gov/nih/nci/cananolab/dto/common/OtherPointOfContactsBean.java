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
		super();
	}
	
	public List<PointOfContactBean> getOtherPointOfContacts() {
		return otherPointOfContacts;
	}
	public void setOtherPointOfContacts(List<PointOfContactBean> otherPointOfContacts) {
		this.otherPointOfContacts = otherPointOfContacts;
	}

	public void addPointOfContact() {
		if (otherPointOfContacts==null) {
			otherPointOfContacts = new ArrayList<PointOfContactBean>();
		}
		otherPointOfContacts.add(new PointOfContactBean());
	}
	
	public void removePointOfContact(int ind) {
		otherPointOfContacts.remove(ind);
	}
	
	public void setPointOfContact(String pocIndex, PointOfContactBean pocBean) {
		this.otherPointOfContacts.set(Integer.parseInt(pocIndex), pocBean);
	}
}
