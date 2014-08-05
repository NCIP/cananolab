package gov.nih.nci.cananolab.restful.view;

import java.util.List;

public class SimpleTabsBean {
	
	boolean userLoggedIn;
	
	List<String[]> tabs;

	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(boolean userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public List<String[]> getTabs() {
		return tabs;
	}

	public void setTabs(List<String[]> tabs) {
		this.tabs = tabs;
	}
	
	

}
