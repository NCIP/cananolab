package gov.nih.nci.cananolab.restful.core;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.restful.view.SimpleTabsBean;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;

@Component("tabGenerationBO")
public class TabGenerationBO {

	public SimpleTabsBean getTabs(HttpServletRequest httpRequest, CananoUserDetails userDetails, String homePage) {
		
		List<String[]> tabs = new ArrayList<String[]>();
		HttpSession session = httpRequest.getSession();
		Object hasResult = (session == null)? null : session.getAttribute("hasResultsWaiting") ;
		boolean hasResultWaiting = (hasResult == null) ? false : (Boolean)hasResult;
		homePage = homePage.trim().toLowerCase();
		
		String urlBase = getUrlBase(httpRequest.getRequestURL().toString());
		
		SimpleTabsBean tabsBean = new SimpleTabsBean();
		tabsBean.setUserLoggedIn(SpringSecurityUtil.isUserLoggedIn());
		
		if (!SpringSecurityUtil.isUserLoggedIn()) { //not logged in
			String[] tabWithLink = new String[2];
			if (homePage.length() == 0 || !homePage.startsWith("true")) {
				tabWithLink[0] = "HOME";
				tabWithLink[1] = urlBase + "index.html#/";
				tabs.add(tabWithLink.clone());
				
				tabWithLink = new String[2];
				tabWithLink[0] = "PROTOCOLS";
				tabWithLink[1] =  urlBase + "#/manageProtocols";;
				tabs.add(tabWithLink);
				
				tabWithLink = new String[2];
				tabWithLink[0] = "SAMPLES";
				tabWithLink[1] =  urlBase + "#/manageSamples";
				tabs.add(tabWithLink);
				
				tabWithLink = new String[2];
				tabWithLink[0] = "PUBLICATIONS";
				tabWithLink[1] =  urlBase + "#/managePublications";;
				tabs.add(tabWithLink);
			}
			tabWithLink = new String[2];
			tabWithLink[0] = "HELP";
			tabWithLink[1] = "https://wiki.nci.nih.gov/x/14CUBg";
			tabs.add(tabWithLink);
			tabWithLink = new String[2];
			tabWithLink[0] = "GLOSSARY";
			tabWithLink[1] = "https://wiki.nci.nih.gov/x/9ICUBg";
			tabs.add(tabWithLink);
			
			if (homePage.length() == 0 || !homePage.startsWith("true")) {
				tabWithLink = new String[2];
				tabWithLink[0] = "LOGIN";
				tabWithLink[1] =  urlBase + "index.html#/login";;
				tabs.add(tabWithLink);
			}
		} else {
			
			String[] tabWithLink = new String[2];
			
			tabWithLink[0] = "HOME";
			tabWithLink[1] = urlBase + "#/";
			tabs.add(tabWithLink.clone());
			
			tabWithLink = new String[2];
			tabWithLink[0] = "WORKFLOW";
			tabWithLink[1] =  urlBase + "#/workflow";;
			tabs.add(tabWithLink);
			

			tabWithLink = new String[2];
			tabWithLink[0] = "PROTOCOLS";
			tabWithLink[1] =  urlBase + "#/manageProtocols";;
			tabs.add(tabWithLink);

			tabWithLink = new String[2];
			tabWithLink[0] = "SAMPLES";
			tabWithLink[1] =  urlBase + "#/manageSamples";;
			tabs.add(tabWithLink);

			tabWithLink = new String[2];
			tabWithLink[0] = "PUBLICATIONS";
			tabWithLink[1] =  urlBase + "#/managePublications";;
			tabs.add(tabWithLink);
			
			tabWithLink = new String[2];
			tabWithLink[0] = "GROUPS";
			tabWithLink[1] =  urlBase + "#/manageGroups";
			tabs.add(tabWithLink);
			
			
			if (userDetails.isCurator()) {
				tabWithLink = new String[2];
				tabWithLink[0] = "CURATION";
				tabWithLink[1] =  urlBase + "#/manageCuration";
				tabs.add(tabWithLink);
			}
			
			
			if (userDetails.isCurator() && hasResultWaiting) {
				tabWithLink = new String[2];
				tabWithLink[0] = "RESULTS";
				tabWithLink[1] =  urlBase + "#/batchDataResults";
				tabs.add(tabWithLink);
			}
			
			tabWithLink = new String[2];
			tabWithLink[0] = "MY WORKSPACE";
			tabWithLink[1] =  urlBase + "#/myWorkspace";;
			tabs.add(tabWithLink);
			
			tabWithLink = new String[2];
			tabWithLink[0] = "MY FAVORITES";
			tabWithLink[1] =  urlBase + "#/myFavorites";;
			tabs.add(tabWithLink);
			
//			tabWithLink = new String[2];
//			tabWithLink[0] = "HELP";
//			tabWithLink[1] = "https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+User%27s+Guide";
//			tabs.add(tabWithLink);
//			
//			tabWithLink = new String[2];
//			tabWithLink[0] = "GLOSSARY";
//			tabWithLink[1] = "https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+Glossary";
//			tabs.add(tabWithLink);
			
			if (userDetails.isAdmin())
			{
				tabWithLink = new String[2];
				tabWithLink[0] = "ADMIN";
				tabWithLink[1] =  urlBase + "#/manageUsers";
				tabs.add(tabWithLink);
			}
			
			tabWithLink = new String[2];
			tabWithLink[0] = "LOGOUT";
			tabWithLink[1] =  urlBase + "index.html#/logout";;
			tabs.add(tabWithLink);
		}
		
		tabsBean.setTabs(tabs);

		return tabsBean;
		
	}
	
	protected String getUrlBase(String fullUrl) {
		if (fullUrl == null || fullUrl.length() == 0)
			return "";
		String token = "/caNanoLab/";
		int end = fullUrl.indexOf(token) + token.length();
		return fullUrl.substring(0, end);
	}
}
