package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.sample.InitSampleSetup;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class SampleEditGeneralBean {
	
	private static Logger logger = Logger.getLogger(SampleEditGeneralBean.class);
	
	String sampleName;
	String cloningSampleName;
	long sampleId;
	boolean userIsCurator;
	
	List<SimplePointOfContactBean> pointOfContacts;
	List<String> keywords = new ArrayList<String>();
	Map<String, List<SimpleAccessBean>> accessToSample;
	
	SimpleDataAvailabilityBean dataAvailability;
	
	//These are lookups needed for dropdown lists
	List<String> organizationNamesForUser;
	List<String> contactRoles;
	List<String> allGroupNames;
	Map<String, String> filteredUsers;
	Map<String, String> roleNames;
	
	boolean showReviewButton;

	List<String> errors = new ArrayList<String>();

	public String getCloningSampleName() {
		return cloningSampleName;
	}

	public void setCloningSampleName(String cloningSampleName) {
		this.cloningSampleName = cloningSampleName;
	}

	public Map<String, String> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(Map<String, String> roleNames) {
		this.roleNames = roleNames;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public long getSampleId() {
		return sampleId;
	}

	public void setSampleId(long sampleId) {
		this.sampleId = sampleId;
	}

	public List<SimplePointOfContactBean> getPointOfContacts() {
		return pointOfContacts;
	}

	public void setPointOfContacts(List<SimplePointOfContactBean> pointOfContacts) {
		this.pointOfContacts = pointOfContacts;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Map<String, List<SimpleAccessBean>> getAccessToSample() {
		return accessToSample;
	}

	public void setAccessToSample(Map<String, List<SimpleAccessBean>> accessToSample) {
		this.accessToSample = accessToSample;
	}

	public SimpleDataAvailabilityBean getDataAvailability() {
		return dataAvailability;
	}

	public void setDataAvailability(SimpleDataAvailabilityBean dataAvailability) {
		this.dataAvailability = dataAvailability;
	}

	public boolean isUserIsCurator() {
		return userIsCurator;
	}

	public void setUserIsCurator(boolean userIsCurator) {
		this.userIsCurator = userIsCurator;
	}

	public boolean isShowReviewButton() {
		return showReviewButton;
	}

	public void setShowReviewButton(boolean showReviewButton) {
		this.showReviewButton = showReviewButton;
	}

	public List<String> getAllGroupNames() {
		return allGroupNames;
	}

	public void setAllGroupNames(List<String> allGroupNames) {
		this.allGroupNames = allGroupNames;
	}

	public Map<String, String> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(Map<String, String> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	public List<String> getErrors() {
		return errors;
	}
	
	public List<String> getOrganizationNamesForUser() {
		return organizationNamesForUser;
	}

	public void setOrganizationNamesForUser(List<String> organizationNamesForUser) {
		this.organizationNamesForUser = organizationNamesForUser;
	}

	public List<String> getContactRoles() {
		return contactRoles;
	}

	public void setContactRoles(List<String> contactRoles) {
		this.contactRoles = contactRoles;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	public void transferSampleBeanData(HttpServletRequest request, 
			CurationService curatorService, SampleBean sampleBean, String[] availableEntityNames) 
	throws Exception {
		
		this.sampleName = sampleBean.getDomain().getName();
		this.sampleId = sampleBean.getDomain().getId();
		this.userIsCurator = sampleBean.getUser().isCurator();
		
		transferPointOfContactData(sampleBean);
		
		this.keywords = new ArrayList<String>(sampleBean.getKeywordSet());

		transferAccessibilityData(sampleBean);
		
		transferDataAvailability(request, sampleBean, availableEntityNames);
		
		setupLookups(request);
		setupGroupNamesForNewAccess(request);
		setupFilteredUsersParamForNewAccess(request, sampleBean.getDomain().getCreatedBy());
			
		setupReviewButton(request, curatorService, sampleBean);
		setupRoleNameMap();
	}
	
	protected void setupRoleNameMap() {
		this.roleNames = new HashMap<String, String>();
		roleNames.put(AccessibilityBean.CSM_READ_ROLE, AccessibilityBean.R_ROLE_DISPLAY_NAME);
		roleNames.put(AccessibilityBean.CSM_CURD_ROLE, AccessibilityBean.CURD_ROLE_DISPLAY_NAME);
	}
	
	/**
	 * Logic moved from SampleAction.setUpSubmitForReviewButton()
	 * @param request
	 * @param curatorService
	 * @param sampleBean
	 * @throws Exception
	 */
	protected void setupReviewButton(HttpServletRequest request, CurationService curatorService, SampleBean sampleBean) 
	throws Exception {
		boolean publicData = sampleBean.getPublicStatus();
		if (!publicData) {
			UserBean user = (UserBean) request.getSession()
					.getAttribute("user");
			//SecurityService securityService = getSecurityServiceFromSession(request);
			SecurityService securityService = (SecurityService) request
					.getSession().getAttribute("securityService");
			DataReviewStatusBean reviewStatus = curatorService
					.findDataReviewStatusBeanByDataId(sampleBean.getDomain().getId()
							.toString(), securityService);

			if (!user.isCurator()
					&& (reviewStatus == null || reviewStatus != null
							&& reviewStatus.getReviewStatus().equals(
									DataReviewStatusBean.RETRACTED_STATUS))) {
				this.showReviewButton = true;
			} else {
				this.showReviewButton = false;
			}
		} else {
			this.showReviewButton = false;
		}
	}
	
	/**
	 * Logic for DWRAccessibilityManager.getMatchedUsers()
	 * 
	 * @param request
	 * @param dataOwner
	 */
	protected void setupFilteredUsersParamForNewAccess(HttpServletRequest request, String dataOwner) {
		
		try {
			SampleService sampleService = (SampleService) request.getSession().getAttribute("sampleService");
			List<UserBean> matchedUsers = sampleService.findUserBeans("");
			List<UserBean> updatedUsers = new ArrayList<UserBean>(matchedUsers);
			// remove current user from the list
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			updatedUsers.remove(user);

			// remove data owner from the list if owner is not the current user
			if (!dataOwner.equalsIgnoreCase(user.getLoginName())) {
				for (UserBean userBean : matchedUsers) {
					if (userBean.getLoginName().equalsIgnoreCase(dataOwner)) {
						updatedUsers.remove(userBean);
						break;
					}
				}
			}
			// exclude curators;
			SecurityService securityService = (SecurityService) request
					.getSession().getAttribute("securityService");
			List<String> curators = securityService
					.getUserNames(AccessibilityBean.CSM_DATA_CURATOR);
			for (UserBean userBean : matchedUsers) {
				for (String curator : curators) {
					if (userBean.getLoginName().equalsIgnoreCase(curator)) {
						updatedUsers.remove(userBean);
					}
				}
			}

			UserBean[] users = updatedUsers.toArray(new UserBean[updatedUsers.size()]);
			this.filteredUsers = new HashMap<String, String>();
			for (UserBean u :users) {
				this.filteredUsers.put(u.getLoginName(), u.getDisplayName());
			}
		} catch (Exception e) {
			logger.error("Got error while setting up params for adding access");
		}
	}
	
	protected void setupGroupNamesForNewAccess(HttpServletRequest request) {
		try {
			SampleService sampleService = (SampleService) request.getSession().getAttribute("sampleService");
			this.allGroupNames = sampleService.findGroupNames("");
			
		} catch (Exception e) {
			logger.error("Got error while setting up params for adding access");
		}
	}
	
	protected void setupFilteredUsersForNewAccess(HttpServletRequest request) {
		try {
			SampleService sampleService = (SampleService) request.getSession().getAttribute("sampleService");
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			
			List<UserBean> matchedUsers = sampleService.findUserBeans("");
			List<UserBean> updatedUsers = new ArrayList<UserBean>(matchedUsers);
			
			// remove current user from the list
			updatedUsers.remove(user);

//			// remove data owner from the list if owner is not the current user
//			if (!dataOwner.equalsIgnoreCase(user.getLoginName())) {
//				for (UserBean userBean : matchedUsers) {
//					if (userBean.getLoginName().equalsIgnoreCase(dataOwner)) {
//						updatedUsers.remove(userBean);
//						break;
//					}
//				}
//			}
//			// exclude curators;
//			List<String> curators = securityService
//					.getUserNames(AccessibilityBean.CSM_DATA_CURATOR);
//			for (UserBean userBean : matchedUsers) {
//				for (String curator : curators) {
//					if (userBean.getLoginName().equalsIgnoreCase(curator)) {
//						updatedUsers.remove(userBean);
//					}
//				}
//			}
//
//			UserBean[] users = updatedUsers.toArray(new UserBean[updatedUsers.size()]);
//			
//			return updatedUsers.toArray(new UserBean[updatedUsers.size()]);
			
		} catch (Exception e) {
			logger.error("Got error while setting up params for adding access");
		}
	}
	
	/**
	 * 
	 * @param request
	 */
	public void setupLookups(HttpServletRequest request) {
		try {
			InitSampleSetup.getInstance().setPOCDropdowns(request);
			SortedSet<String> organizationNames = (SortedSet<String>)request.getSession().getAttribute("allOrganizationNames");
			this.organizationNamesForUser = new ArrayList<String>(organizationNames);
			
			SortedSet<String> roles = (SortedSet<String>)request.getSession().getAttribute("contactRoles");
			this.contactRoles = new ArrayList<String>(roles);
			
		} catch (Exception e) {
			logger.error("Got error while setting up POC lookup for sample edit");
		}
	}
	
	/**
	 * Replicate logic in bodyManageAccessibility.jsp
	 * 
	 * @param sampleBean
	 */
	protected void transferAccessibilityData(SampleBean sampleBean) {
		 accessToSample = new HashMap<String, List<SimpleAccessBean>>();
		
		List<AccessibilityBean> groupAccess = sampleBean.getGroupAccesses();
		if (groupAccess != null) {
			List<SimpleAccessBean> groupList = new ArrayList<SimpleAccessBean>();
			for (AccessibilityBean accBean : groupAccess) {
				String groupName = accBean.getGroupName();
				SimpleAccessBean aBean = new SimpleAccessBean();
				aBean.setGroupName(groupName);
				aBean.setRoleDisplayName(accBean.getRoleDisplayName());
				groupList.add(aBean);
			}
			
			accessToSample.put("groupAccesses", groupList);
		}
		
		List<AccessibilityBean> userAccess = sampleBean.getUserAccesses();
		if (userAccess != null) {
			List<SimpleAccessBean> userList = new ArrayList<SimpleAccessBean>();
			for (AccessibilityBean accBean : userAccess) {
				SimpleAccessBean aBean = new SimpleAccessBean();
				aBean.setLoginName(accBean.getUserBean().getLoginName());
				aBean.setRoleDisplayName(accBean.getRoleDisplayName());
				
				userList.add(aBean);
			}
			
			accessToSample.put("userAccesses", userList);
		}
	}
	
	//edit
	protected void transferDataAvailability(HttpServletRequest request, SampleBean sampleBean, String[] availableEntityNames) {
		if (!sampleBean.getHasDataAvailability()) 
			return; 
		
		if (request == null) {
			logger.error("HttpServletRequest object is null. Unable to transfer DataAvailability data");
			return;
		}
		
		dataAvailability = new SimpleDataAvailabilityBean();
		dataAvailability.transferSampleBeanForDataAvailability(sampleBean, request, availableEntityNames);
//		dataAvailability.setCaNanoLabScore(sampleBean.getCaNanoLabScore());
//		dataAvailability.setMincharScore(sampleBean.getMincharScore());
//		
//		SortedSet<String> ca = (SortedSet<String>) request.getSession().getServletContext().getAttribute("chemicalAssocs");
//		dataAvailability.setChemicalAssocs(new ArrayList<String>(ca));
//		
//		dataAvailability.setCaNano2MINChar((Map<String, String>) request.getSession().getServletContext()
//				.getAttribute("caNano2MINChar"));
//		
//		
//		SortedSet<String> pc = (SortedSet<String>) request.getSession().getServletContext().getAttribute("physicoChars");
//		dataAvailability.setPhysicoChars(new ArrayList<String>(pc));
//		SortedSet<String> iv = (SortedSet<String>) request.getSession().getServletContext().getAttribute("invitroChars");
//		dataAvailability.setInvitroChars(new ArrayList<String>(iv));
//		SortedSet<String> invivo = (SortedSet<String>) request.getSession().getServletContext().getAttribute("invivoChars");
//		dataAvailability.setInvivoChars(new ArrayList<String>(invivo));
	}
	
	protected void transferPointOfContactData(SampleBean sampleBean) {
		pointOfContacts = new ArrayList<SimplePointOfContactBean>();
		PointOfContact samplePOC = sampleBean.getPrimaryPOCBean().getDomain();
		if (samplePOC != null && samplePOC.getId() > 0) {
			SimplePointOfContactBean poc = new SimplePointOfContactBean();
			transferPointOfContactData(samplePOC, poc);
			poc.setPrimaryContact(true);
			pointOfContacts.add(poc);
		}
		
		List<PointOfContactBean> others = sampleBean.getOtherPOCBeans();
		if (others == null) return;
		
		for (PointOfContactBean aPoc : others) {
			SimplePointOfContactBean poc = new SimplePointOfContactBean();
			transferPointOfContactData(aPoc.getDomain(), poc);
			pointOfContacts.add(poc);
		}
	}
	
	protected void transferPointOfContactData(PointOfContact samplePOC, SimplePointOfContactBean poc) {
		
		poc.setFirstName(samplePOC.getFirstName());
		poc.setLastName(samplePOC.getLastName());
		
		SimpleOrganizationBean simpleOrg = new SimpleOrganizationBean();
		simpleOrg.setName(samplePOC.getOrganization().getName());
		simpleOrg.setId(samplePOC.getOrganization().getId());
		poc.setOrganization(simpleOrg);
		poc.setRole(samplePOC.getRole());
		poc.setId(samplePOC.getId());
		
		SimpleAddressBean simpleAddress = new SimpleAddressBean();
		
		simpleAddress.setLine1(samplePOC.getOrganization().getStreetAddress1());
		simpleAddress.setLine2(samplePOC.getOrganization().getStreetAddress2());
		simpleAddress.setCity(samplePOC.getOrganization().getCity());
		simpleAddress.setStateProvince(samplePOC.getOrganization().getState());
		simpleAddress.setCountry(samplePOC.getOrganization().getCountry());
		simpleAddress.setZip(samplePOC.getOrganization().getPostalCode());
		
		poc.setAddress(simpleAddress);
		
	}
	
	/**
	 * Populate input data for saving a sample to a SampleBean. Currently, only sampleName
	 * and keywords are needed
	 * 
	 * @param destSampleBean
	 */
	public void populateDataForSavingSample(SampleBean destSampleBean) {
		if (destSampleBean == null)
			return;
	
		
		//When saving keywords, current implementation is to replace the whole set
		//ref. SampleServiceLocalImpl.saveSample()
		List<String> keywords = this.getKeywords();
		if (keywords != null) {
			Collection<Keyword> keywordColl = new HashSet<Keyword>();
			String kws = "";
			for (String keyword : keywords) {
				kws += keyword;
				kws += "\n";
//				Keyword kw = new Keyword();
//				kw.setName(keyword);
//				keywordColl.add(kw);
			}
			
			destSampleBean.setKeywordsStr(kws);
			
		}
		
		destSampleBean.getDomain().setName(this.sampleName);
	}
}
