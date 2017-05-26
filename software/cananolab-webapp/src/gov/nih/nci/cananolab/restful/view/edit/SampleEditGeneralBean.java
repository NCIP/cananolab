package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.sample.InitSampleSetup;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.enums.CaNanoPermissionEnum;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.service.UserService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.sample.SampleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class SampleEditGeneralBean {

	private static Logger logger = Logger.getLogger(SampleEditGeneralBean.class);

	String sampleName;
	String newSampleName;
	long sampleId;

	List<SimplePointOfContactBean> pointOfContacts = new ArrayList<SimplePointOfContactBean>();;
	List<String> keywords = new ArrayList<String>();
	Map<String, List<SimpleAccessBean>> accessToSample;

	List<AccessControlInfo> groupAccesses;// = new ArrayList<AccessibilityBean>();
	List<AccessControlInfo> userAccesses; // =  new ArrayList<AccessibilityBean>();
	AccessControlInfo theAccess = new AccessControlInfo();

	SimpleDataAvailabilityBean dataAvailability;

	//These are lookups needed for dropdown lists
	List<String> organizationNamesForUser;
	List<String> contactRoles;
	List<String> allGroupNames = new ArrayList<String>();
	Map<String, String> filteredUsers;
	Map<String, String> roleNames;
	Boolean isPublic = false;

	boolean showReviewButton;

	List<String> errors = new ArrayList<String>();
	String errorType = "";
	String message; //requested by front end


	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<AccessControlInfo> getGroupAccesses() {
		return groupAccesses;
	}

	public void setGroupAccesses(List<AccessControlInfo> groupAccesses) {
		this.groupAccesses = groupAccesses;
	}

	public List<AccessControlInfo> getUserAccesses() {
		return userAccesses;
	}

	public void setUserAccesses(List<AccessControlInfo> userAccesses) {
		this.userAccesses = userAccesses;
	}

	public AccessControlInfo getTheAccess() {
		return theAccess;
	}

	public void setTheAccess(AccessControlInfo theAccess) {
		this.theAccess = theAccess;
	}

	public String getNewSampleName() {
		return newSampleName;
	}

	public void setNewSampleName(String newSampleName) {
		this.newSampleName = newSampleName;
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

	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void setupRoleNameMap() {
		this.roleNames = new HashMap<String, String>();
		roleNames.put("R", CaNanoPermissionEnum.R.getPermValue());
		roleNames.put("RWD", CaNanoPermissionEnum.R.getPermValue() + " " + CaNanoPermissionEnum.W.getPermValue() + " " + CaNanoPermissionEnum.D.getPermValue());
		
	}

	/**
	 * Logic moved from SampleAction.setUpSubmitForReviewButton()
	 * @param request
	 * @param curatorService
	 * @param sampleBean
	 * @throws Exception
	 */
	public void setupReviewButton(HttpServletRequest request, CurationService curatorService, SampleBean sampleBean, 
								  SpringSecurityAclService springSecurityAclService) throws Exception
	{
		boolean publicData = springSecurityAclService.checkObjectPublic(sampleBean.getDomain().getId(), SecureClassesEnum.SAMPLE.getClazz());
		if (!publicData)
		{
			DataReviewStatusBean reviewStatus = curatorService.findDataReviewStatusBeanByDataId(sampleBean.getDomain().getId().toString());

			if (!SpringSecurityUtil.getPrincipal().isCurator() && (reviewStatus == null || reviewStatus != null && 
				reviewStatus.getReviewStatus().equals(DataReviewStatusBean.RETRACTED_STATUS))) {
				this.showReviewButton = true;
			} else {
				this.showReviewButton = false;
			}
		} else {
			this.showReviewButton = false;
		}
	}

	public void setupGroupNamesForNewAccess(HttpServletRequest request, UserService userService) {

		//TODO: only need this after a save new access
		try {
			List<String> groupNames = (List<String>)request.getSession().getAttribute("allGroupNames");

			if (groupNames == null) {
				groupNames = userService.getGroupsAccessibleToUser("");
				request.getSession().setAttribute("allGroupNames", groupNames);
			}

			this.allGroupNames.addAll(groupNames);

		} catch (Exception e) {
			logger.error("Got error while setting up params for adding access");
		}
	}

	/**
	 * 
	 * @param request
	 */
	public void setupLookups(HttpServletRequest request, SampleService sampleService) {
		try {
			InitSampleSetup.getInstance().setPOCDropdowns(request, sampleService);
			SortedSet<String> organizationNames = sampleService.getAllOrganizationNames();
			request.getSession().setAttribute("allOrganizationNames", organizationNames);
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
	public void transferAccessibilityData(SampleBean sampleBean)
	{
		accessToSample = new HashMap<String, List<SimpleAccessBean>>();

		List<AccessControlInfo> groupAccess = sampleBean.getGroupAccesses();

		this.groupAccesses = groupAccess;

		if (groupAccess != null) {
			List<SimpleAccessBean> groupList = new ArrayList<SimpleAccessBean>();
			for (AccessControlInfo accBean : groupAccess) {
				SimpleAccessBean aBean = new SimpleAccessBean();
				aBean.setGroupName(accBean.getRecipient());
				aBean.setAccessBy(accBean.getAccessType());
				aBean.setRoleDisplayName(accBean.getRoleName());
				groupList.add(aBean);
			}

			accessToSample.put("groupAccesses", groupList);
		}

		List<AccessControlInfo> userAccess = sampleBean.getUserAccesses();
		this.userAccesses = userAccess;
		if (userAccess != null) {
			List<SimpleAccessBean> userList = new ArrayList<SimpleAccessBean>();
			for (AccessControlInfo accBean : userAccess) {
				SimpleAccessBean aBean = new SimpleAccessBean();
				aBean.setLoginName(accBean.getRecipient());
				aBean.setAccessBy(accBean.getAccessType());
				aBean.setRoleDisplayName(accBean.getRoleName());

				userList.add(aBean);
			}

			accessToSample.put("userAccesses", userList);
		}
	}

	//edit
	public void transferDataAvailability(HttpServletRequest request, SampleBean sampleBean, String[] availableEntityNames) {
		if (!sampleBean.getHasDataAvailability()) 
			return; 

		if (request == null) {
			logger.error("HttpServletRequest object is null. Unable to transfer DataAvailability data");
			return;
		}

		dataAvailability = new SimpleDataAvailabilityBean();
		dataAvailability.transferSampleBeanForDataAvailability(sampleBean, request, availableEntityNames);
	}

	public void transferPointOfContactData(SampleBean sampleBean)
	{
		//pointOfContacts = new ArrayList<SimplePointOfContactBean>();
		PointOfContact samplePOC = sampleBean.getPrimaryPOCBean().getDomain();
		long sampleId = (sampleBean.getDomain().getId() == null) ? 0 : sampleBean.getDomain().getId().longValue();
		if (samplePOC != null && samplePOC.getId() > 0) {
			SimplePointOfContactBean poc = new SimplePointOfContactBean();
			transferPointOfContactData(samplePOC, poc, sampleId);
			poc.setPrimaryContact(true);
			pointOfContacts.add(poc);
		}

		List<PointOfContactBean> others = sampleBean.getOtherPOCBeans();
		if (others == null) return;

		for (PointOfContactBean aPoc : others) {
			SimplePointOfContactBean poc = new SimplePointOfContactBean();
			transferPointOfContactData(aPoc.getDomain(), poc, sampleId);
			pointOfContacts.add(poc);
		}
	}

	public void transferPointOfContactData(PointOfContact samplePOC, SimplePointOfContactBean poc, long sampleId)
	{
		poc.setFirstName(samplePOC.getFirstName());
		poc.setLastName(samplePOC.getLastName());
		poc.setMiddleInitial(samplePOC.getMiddleInitial());
		poc.setSampleId(sampleId);

		SimpleOrganizationBean simpleOrg = new SimpleOrganizationBean();
		simpleOrg.setName(samplePOC.getOrganization().getName());
		simpleOrg.setId(samplePOC.getOrganization().getId());

		poc.setRole(samplePOC.getRole());
		poc.setId(samplePOC.getId());
		poc.setPhoneNumber(samplePOC.getPhone());
		poc.setEmail(samplePOC.getEmail());

		SimpleAddressBean simpleAddress = new SimpleAddressBean();

		simpleAddress.setLine1(samplePOC.getOrganization().getStreetAddress1());
		simpleAddress.setLine2(samplePOC.getOrganization().getStreetAddress2());
		simpleAddress.setCity(samplePOC.getOrganization().getCity());
		simpleAddress.setStateProvince(samplePOC.getOrganization().getState());
		simpleAddress.setCountry(samplePOC.getOrganization().getCountry());
		simpleAddress.setZip(samplePOC.getOrganization().getPostalCode());

		simpleOrg.setAddress(simpleAddress);
		poc.setOrganization(simpleOrg);

		poc.setAddress(simpleAddress);

	}

	/**
	 * Populate input data for saving a sample to a SampleBean. Currently, only sampleName
	 * and keywords are needed
	 * 
	 * @param destSampleBean
	 */
	public void populateDataForSavingSample(SampleBean destSampleBean)
	{
		if (destSampleBean == null)
			return;

		//When saving keywords, current implementation is to replace the whole set
		//ref. SampleServiceLocalImpl.saveSample()
		List<String> keywords = this.getKeywords();
		if (keywords != null) {
			String keywordString = "";
			for (String keyword : keywords) {
				keywordString += keyword;
				keywordString += "\r\n";
			}

			destSampleBean.setKeywordsStr(keywordString);
		}

		destSampleBean.getDomain().setName(this.sampleName);
	}

}
