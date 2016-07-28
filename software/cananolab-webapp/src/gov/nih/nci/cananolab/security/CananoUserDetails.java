package gov.nih.nci.cananolab.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.util.StringUtils;

public class CananoUserDetails implements UserDetails
{
	private static final long serialVersionUID = 2283492944205219618L;
	
	private String username;
	private String firstName;
	private String lastName;
	private String organization;
	private String department;
	private String title;
	private String phoneNumber;
	private String password;
	private String emailId;
	private boolean enabled;		//active_flag
	private List<String> roles;
	
	private List<String> groups;
	
	private  Collection<GrantedAuthority> authorities;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
		if (roles != null && roles.size() > 0)
		{
			if (authorities == null)
				authorities = new ArrayList<GrantedAuthority>();
			for (String role : roles)
			{
				if (!StringUtils.isEmpty(role))
					authorities.add(new SimpleGrantedAuthority(role));
			}
		}
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
		if (groups != null && groups.size() > 0)
		{
			if (authorities == null)
				authorities = new ArrayList<GrantedAuthority>();
			for (String group : groups)
			{
				if (!StringUtils.isEmpty(group))
					authorities.add(new SimpleGrantedAuthority(group));
			}
		}
	}
	
	public void addGroup(String groupName)
	{
		if (!StringUtils.isEmpty(groupName) && !this.groups.contains(groupName))
		{
			this.groups.add(groupName);
			authorities.add(new SimpleGrantedAuthority(groupName));
		}
	}
	
	public void removeGroup(String groupName)
	{
		if (!StringUtils.isEmpty(groupName))
		{
			this.groups.remove(groupName);
			int i = 0;
			for (Object obj: this.authorities)
			{
				if (((GrantedAuthority)obj).getAuthority().equalsIgnoreCase(groupName))
				{
					authorities.remove(i);
					break;
				}
				i++;
			}
		}
	}
	
	public boolean belongsToGroup(String groupName)
	{
		return (this.groups != null) ? this.groups.contains(groupName) : false;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean isCurator() {
		return isGranted(CaNanoRoleEnum.ROLE_CURATOR.toString());
	}
	
	public boolean isResearcher() {
		return isGranted(CaNanoRoleEnum.ROLE_RESEARCHER.toString());
	}
	
	public boolean isPublic() {
		return isGranted(CaNanoRoleEnum.ROLE_ANONYMOUS.toString());
	}
	
	public boolean isAdmin() {
		return isGranted(CaNanoRoleEnum.ROLE_ADMIN.toString());
	}
	
	private boolean isGranted(String role)
	{
		Collection authorities = getAuthorities();
		if(authorities == null)
			return false;
		for(Iterator iterator = authorities.iterator(); iterator.hasNext();)
		{
			GrantedAuthority grantedAuthority = (GrantedAuthority)iterator.next();
			if(role.equals(grantedAuthority.getAuthority()))
				return true;
		}

		return false;
    }
	
	public String getDisplayName()
	{
		String displayName = "";
		if (!StringUtils.isEmpty(firstName) && !StringUtils.isEmpty(lastName)) {
			displayName = lastName + ", " + firstName;
		} else if (!StringUtils.isEmpty(firstName)) {
			displayName = firstName;
		} else if (!StringUtils.isEmpty(lastName)) {
			displayName = lastName;
		} else {
			displayName = username;
		}
		return displayName;
	}
	
	public boolean equals(Object obj)
	{
		boolean eq = false;
		if (obj instanceof CananoUserDetails) {
			CananoUserDetails u = (CananoUserDetails) obj;
			String thisId = this.getUsername();
			if (thisId != null && thisId.equals(u.getUsername())) {
				eq = true;
			}
		}
		return eq;
	}

	public int compareTo(Object obj)
	{
		int diff = 0;
		if (obj instanceof CananoUserDetails) {
			CananoUserDetails u = (CananoUserDetails) obj;
			if (u.getLastName().equals(this.getLastName())) {
				return this.getFirstName().toLowerCase().compareTo(u.getFirstName().toLowerCase());
			} else {
				return this.getLastName().toLowerCase().compareTo(u.getLastName().toLowerCase());
			}
		}
		return diff;
	}

}
