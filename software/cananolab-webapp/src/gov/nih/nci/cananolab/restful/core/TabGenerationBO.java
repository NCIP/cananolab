package gov.nih.nci.cananolab.restful.core;

import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class TabGenerationBO {

	public List<String[]> getTabs(HttpServletRequest httpRequest, String homePage) {
		List<String[]> tabs = new ArrayList<String[]>();
		HttpSession session = httpRequest.getSession(false);
		UserBean userBean = (session == null)? null : (UserBean)session.getAttribute("user");	
		homePage = homePage.trim().toLowerCase();
		
		String urlBase = getUrlBase(httpRequest.getRequestURL().toString());		
		
		if (userBean == null) { //not logged in
			String[] tabWithLink = new String[2];
			if (homePage.length() == 0 || !homePage.startsWith("true")) {
				tabWithLink[0] = "HOME";
				tabWithLink[1] = urlBase + "index.html#/";
				tabs.add(tabWithLink.clone());
				
				tabWithLink = new String[2];
				tabWithLink[0] = "PROTOCOLS";
				tabWithLink[1] =  urlBase + "manageProtocol.do";;
				tabs.add(tabWithLink);
				
				tabWithLink = new String[2];
				tabWithLink[0] = "SAMPLES";
				tabWithLink[1] =  urlBase + "manageSample.do";;
				tabs.add(tabWithLink);
				
				tabWithLink = new String[2];
				tabWithLink[0] = "PUBLICATIONS";
				tabWithLink[1] =  urlBase + "managePublication.do";;
				tabs.add(tabWithLink);
				
			}
			tabWithLink = new String[2];
			tabWithLink[0] = "HELP";
			tabWithLink[1] = "https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+User%27s+Guide";
			tabs.add(tabWithLink);
			tabWithLink = new String[2];
			tabWithLink[0] = "GLOSSARY";
			tabWithLink[1] = "https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+Glossary";
			tabs.add(tabWithLink);
			
			if (homePage.length() == 0 || !homePage.startsWith("true")) {
				tabWithLink = new String[2];
				tabWithLink[0] = "LOGIN";
				tabWithLink[1] =  urlBase + "loginPage.do";;
				tabs.add(tabWithLink);
			}
		} else {
			
			String[] tabWithLink = new String[2];
			
			tabWithLink[0] = "HOME";
			tabWithLink[1] = urlBase + "index.html#/";
			tabs.add(tabWithLink.clone());

			tabWithLink = new String[2];
			tabWithLink[0] = "PROTOCOLS";
			tabWithLink[1] =  urlBase + "manageProtocol.do";;
			tabs.add(tabWithLink);

			tabWithLink = new String[2];
			tabWithLink[0] = "SAMPLES";
			tabWithLink[1] =  urlBase + "manageSample.do";;
			tabs.add(tabWithLink);

			tabWithLink = new String[2];
			tabWithLink[0] = "PUBLICATIONS";
			tabWithLink[1] =  urlBase + "managePublication.do";;
			tabs.add(tabWithLink);
			
			if (userBean.isAdmin()) {
				tabWithLink[0] = "ADMINISTRATION";
				tabWithLink[1] =  urlBase + "TBD";
				tabs.add(tabWithLink);
			}
			
			if (userBean.isCurator()) {
				tabWithLink[0] = "CURATION";
				tabWithLink[1] =  urlBase + "TBD";
				tabs.add(tabWithLink);
			}
			
			//TODO: 
			//if (userBean.isCurator() && hasResultWaiting)
			//	tabs.add("RESULT");
			
			tabWithLink = new String[2];
			tabWithLink[0] = "HELP";
			tabWithLink[1] = "https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+User%27s+Guide";
			tabs.add(tabWithLink);
			tabWithLink = new String[2];
			tabWithLink[0] = "GLOSSARY";
			tabWithLink[1] = "https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+Glossary";
			tabs.add(tabWithLink);
			
			tabWithLink = new String[2];
			tabWithLink[0] = "LOGOUT";
			tabWithLink[1] =  urlBase + "rest/security/logout";;
			tabs.add(tabWithLink);
		}

		return tabs;
		
	}
	
	protected String getUrlBase(String fullUrl) {
		if (fullUrl == null || fullUrl.length() == 0)
			return "";
		String token = "/caNanoLab/";
		int end = fullUrl.indexOf(token) + token.length();
		return fullUrl.substring(0, end);
	}
}
