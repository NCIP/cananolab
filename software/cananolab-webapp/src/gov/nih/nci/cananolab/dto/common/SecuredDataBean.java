/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.dto.common;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import gov.nih.nci.cananolab.security.AccessControlInfo;

public class SecuredDataBean
{
	private List<AccessControlInfo> userAccesses = new ArrayList<AccessControlInfo>();
	private List<AccessControlInfo> groupAccesses = new ArrayList<AccessControlInfo>();
	
	private AccessControlInfo theAccess = new AccessControlInfo();
	
	protected String createdBy;
	
	private Boolean userUpdatable = false;
	private Boolean userDeletable = false;
	private Boolean userIsOwner = false;
	
	private Boolean publicStatus = false;
	
	public Boolean getPublicStatus() {
		return publicStatus;
	}
	
	public void setPublicStatus(Boolean publicStatus)
	{
		this.publicStatus = publicStatus;
	}

	@JsonIgnore
	private List<AccessControlInfo> allAccesses = new ArrayList<AccessControlInfo>();

	public List<AccessControlInfo> getUserAccesses() {
		return userAccesses;
	}

	public void setUserAccesses(List<AccessControlInfo> userAccesses) {
		this.userAccesses = userAccesses;
		allAccesses.addAll(userAccesses);
	}

	public void addUserAccess(AccessControlInfo userAccess)
	{
		this.userAccesses.add(userAccess);
	}

	public List<AccessControlInfo> getGroupAccesses() {
		return groupAccesses;
	}

	public void setGroupAccesses(List<AccessControlInfo> groupAccesses) {
		this.groupAccesses = groupAccesses;
		allAccesses.addAll(groupAccesses);
	}
	
	public void addGroupAccess(AccessControlInfo groupAccess)
	{
		this.groupAccesses.add(groupAccess);
	}

	public List<AccessControlInfo> getAllAccesses() {
		return allAccesses;
	}

	public AccessControlInfo getTheAccess() {
		return theAccess;
	}

	public void setTheAccess(AccessControlInfo theAccess) {
		this.theAccess = theAccess;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Boolean getUserUpdatable() {
		return userUpdatable;
	}

	public void setUserUpdatable(Boolean userUpdatable) {
		this.userUpdatable = userUpdatable;
	}

	public Boolean getUserDeletable() {
		return userDeletable;
	}

	public void setUserDeletable(Boolean userDeletable) {
		this.userDeletable = userDeletable;
	}
	
}
