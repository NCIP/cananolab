/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.core;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.restful.util.InputValidationUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.enums.AccessTypeEnum;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.ui.form.PublicationForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.cananolab.util.StringUtils;

/**
 * Base action for all annotation actions
 * 
 * @author pansu
 * 
 */
public abstract class BaseAnnotationBO extends AbstractDispatchBO
{
	public abstract CurationService getCurationServiceDAO();
	public abstract SampleService getSampleService();
	public abstract SpringSecurityAclService getSpringSecurityAclService();
	public abstract UserDetailsService getUserDetailsService();
	
	/**
	 * This is a simplified version of setupSample(SampleForm sampleForm, HttpServletRequest request)
	 * for restful service implementation.
	 * 
	 * @param sampleId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected SampleBean setupSampleById(String sampleId, HttpServletRequest request) throws Exception
	{
		if (StringUtils.isEmpty(sampleId)) 
			throw new NotExistException("Null or empty sample Id passed in setupSampleById()");
		
		// sample service has been created earlier
		SampleBean sampleBean = getSampleService().findSampleById(sampleId, true);
		if (sampleBean == null) {
			throw new NotExistException("No such sample in the system");
		}
		//request.setAttribute("theSample", sampleBean);

		return sampleBean;
	}
	
	protected SampleBean setupSampleById(String sampleId, HttpServletRequest request, boolean loadAccess) throws Exception
	{	
		if (StringUtils.isEmpty(sampleId)) 
			throw new NotExistException("Null or empty sample Id passed in setupSampleById()");
		
		SampleBean sampleBean = getSampleService().findSampleById(sampleId, loadAccess);
		if (sampleBean == null) {
			throw new NotExistException("No such sample in the system");
		}
		request.setAttribute("theSample", sampleBean);

		return sampleBean;
	}

	// check for cases where delete can't happen
	protected boolean checkDelete(HttpServletRequest request, String id) throws Exception {
		return true;
	}

	/**
	 * Download action to handle file downloading and viewing
	 * 
	 * @param
	 * @return
	 */
	protected String downloadFile(BaseService service, String fileId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	//	String fileId = request.getParameter("fileId");
	
		FileBean fileBean = service.findFileById(fileId);
		System.out.println("fileBean.getDomainFile().getUri()"+fileBean.getDomainFile().getUri());

		if (fileBean != null) {
			if (fileBean.getDomainFile().getUriExternal()) {
				response.sendRedirect(fileBean.getDomainFile().getUri());
				return null;
			}
		}
		String fileRoot = PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "fileRepositoryDir");
		java.io.File dFile = new java.io.File(fileRoot + java.io.File.separator
				+ fileBean.getDomainFile().getUri());
				//+ "particles/composition.png");
				
		
		
		if (dFile.exists()) {
			
			ExportUtils.prepareReponseForImage(response, fileBean.getDomainFile().getUri());

			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(dFile));
				out = response.getOutputStream();
				byte[] bytes = new byte[32768];
				int numRead = 0;
				while ((numRead = in.read(bytes)) > 0) {
					out.write(bytes, 0, numRead);
				}
				
			} finally {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			}
		} else {
			String msg = PropertyUtil.getProperty("sample", "error.noFile");
			throw new FileException("Target download file doesn't exist");
		}
		
		return null;
	}
	
	protected java.io.File downloadImage(BaseService service, String fileId,
			HttpServletRequest request) throws Exception {

		FileBean fileBean = service.findFileById(fileId);
		System.out.println("fileBean.getDomainFile().getUri()"+fileBean.getDomainFile().getUri());

		if (fileBean != null) {
			if (fileBean.getDomainFile().getUriExternal()) {
				throw new FileException("UriExternal file download can't be handled in downloadImage method");
			}
		}
		String fileRoot = PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "fileRepositoryDir");
		java.io.File dFile = new java.io.File(fileRoot + java.io.File.separator
				+ fileBean.getDomainFile().getUri());
				//+ "particles/composition.png");

		if (dFile.exists()) 
			return dFile;
		else 

			throw new FileException(PropertyUtil.getProperty("sample", "error.noFile"));
	}

	/**
	 * Download action to handle file downloading and viewing
	 * 
	 * @param
	 * @return
	 */
	public String download(HttpServletRequest request) throws Exception {
		return null;
	}

	protected SampleBean[] prepareCopy(HttpServletRequest request, List<String> otherSamples, SampleBean oldSampleBean)	throws Exception
	{
		//List<String> otherSamples = simpleEdit.getSelectedOtherSampleNames();
		if (otherSamples.size() == 0) {
			return null;
		}
		SampleBean[] sampleBeans = new SampleBean[otherSamples.size()];
		
		int i = 0;
		for (String other : otherSamples) {
			SampleBean sampleBean = getSampleService().findSampleByName(other);
			sampleBean.setGroupAccesses(oldSampleBean.getGroupAccesses());
			sampleBean.setUserAccesses(oldSampleBean.getUserAccesses());
			sampleBeans[i] = sampleBean;
			i++;
		}
		return sampleBeans;
	}

	protected List<String> validateFileBean(HttpServletRequest request, List<String> msgs, FileBean fileBean)
	{	
		if (fileBean == null) {
			return msgs;
		}
		File File = fileBean.getDomainFile();
		if (File.getTitle().length() == 0) {
			msgs.add("File Title is required.");
		}

		if (File.getType().length() == 0) {
			msgs.add("File Type is required.");
		}

		if (File.getUriExternal()) {
			if (fileBean.getExternalUrl() == null
					|| fileBean.getExternalUrl().trim().length() == 0) {
				msgs.add("External URL is required.");
			}else{
				if(!InputValidationUtil.isUrlValid(fileBean.getExternalUrl())){
					msgs.add("External URL is invalid");
				}
			}
		} else {
			// all empty
			if ((fileBean.getNewFileData() == null)&&(StringUtils
					.isEmpty(fileBean.getDomainFile().getUri())
					&& (fileBean.getExternalUrl() == null || fileBean
							.getExternalUrl().trim().length() == 0))){
//					&& (fileBean.getDomainFile() == null || fileBean
//							.getDomainFile().getTitle() == null))) {
				
						msgs.add("Uploading a file is required.");

				// the case that user switch from url to upload file, but no
				// file is selected
			} else if ((fileBean.getNewFileData() == null)&& StringUtils
					.isEmpty(fileBean.getDomainFile().getUri())
					&& !StringUtils.isEmpty(fileBean.getExternalUrl())) {

				msgs.add("Uploading a file is required.");

			}
		}
		return msgs;
	}

	/**
	 * If user entered customized value by selecting [other] option previously,
	 * then add the value in collection, so user can see it again.
	 * 
	 * @param request
	 * @param value
	 * @param sessionName
	 * @param attributeName
	 */
	protected void setOtherValueOption(HttpServletRequest request, String value, String sessionName)
	{
		if (!StringUtils.isEmpty(value)) {
			Collection<String> otherTypes = (Collection<String>) request.getSession().getAttribute(sessionName);
			if (otherTypes != null && !otherTypes.contains(value) && StringUtils.xssValidate(value)) {
				otherTypes.add(value);
			}
		}
	}

	/**
	 * Returns a partial URL for downloading a file from local/remote host.
	 * 
	 * @param request
	 * @param serviceUrl
	 * @return
	 * @throws Exception
	 */
	protected StringBuilder getDownloadUrl(HttpServletRequest request) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		sb.append(request.getRequestURL().toString());

		sb.append(ExportUtils.DOWNLOAD_URL).append("&fileId=");

		return sb;
	}

	public Boolean canUserExecutePrivateDispatch(HttpServletRequest request, String protectedData) throws Exception
	{
		if (!SpringSecurityUtil.isUserLoggedIn()) {
			return false;
		}
		if (protectedData == null) {
			return true;
		} else {
			// per app scan check id format
			if (protectedData != null && !protectedData.matches(Constants.NUMERIC_PATTERN)) {
				throw new NotExistException("Invalid ID format");
			}
			
			//Commented to remove CSM - this method is not being used anywhere
			/*try {
				return securityService.checkCreatePermission(protectedData);
			} catch (Exception e) {
				throw new SecurityException();
			}*/
			return true;
		}
	}

	public String submitForReview(HttpServletRequest request, DataReviewStatusBean dataReviewStatusBean) throws Exception
	{
		String forwardName = request.getParameter("forwardName");
		
		//	set default forward name to be summaryEdit
		if (forwardName == null) {
			forwardName = "summaryEdit";
		}
		dataReviewStatusBean.setReviewStatus(DataReviewStatusBean.PENDING_STATUS);
		dataReviewStatusBean.setSubmittedBy(SpringSecurityUtil.getLoggedInUserName());
		dataReviewStatusBean.setSubmittedDate(new Date());
		getCurationServiceDAO().submitDataForReview(dataReviewStatusBean);

		String msg = PropertyUtil.getProperty(dataReviewStatusBean.getDataType(), "message.submitReview");
		return "success";
	}

	protected void setUpSubmitForReviewButton(HttpServletRequest request, String dataId, Boolean publicData) throws Exception
	{
		// show 'submit for review' button if data is not public and doesn't
		// have tracted status and user is
		// not curator
		if (!publicData) {
			CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
			DataReviewStatusBean reviewStatus = getCurationServiceDAO().findDataReviewStatusBeanByDataId(dataId);

			if (!userDetails.isCurator() && (reviewStatus == null || reviewStatus != null && 
				 reviewStatus.getReviewStatus().equals(DataReviewStatusBean.RETRACTED_STATUS))) {
				request.setAttribute("review", true);
			} else {
				request.setAttribute("review", false);
			}
		} else {
			request.setAttribute("review", false);
		}
	}

	protected void switchPendingReviewToPublic(HttpServletRequest request, String dataId) throws Exception
	{
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		DataReviewStatusBean reviewStatus = getCurationServiceDAO().findDataReviewStatusBeanByDataId(dataId);
		if (userDetails.isCurator() && reviewStatus != null && 
			reviewStatus.getReviewStatus().equals(DataReviewStatusBean.PENDING_STATUS)) {
			reviewStatus.setReviewStatus(DataReviewStatusBean.PUBLIC_STATUS);
			getCurationServiceDAO().submitDataForReview(reviewStatus);
		}
	}

	/**
	 * Update the review status of the given data to the give status
	 * 
	 * @param status
	 * @param request
	 * @param dataId
	 * @param dataName
	 * @param dataType
	 * @throws Exception
	 */
	protected void updateReviewStatusTo(String status, HttpServletRequest request, String dataId, String dataName,
			String dataType) throws Exception
	{
		DataReviewStatusBean reviewStatus = getCurationServiceDAO().findDataReviewStatusBeanByDataId(dataId);
		if (reviewStatus == null) {
			reviewStatus = new DataReviewStatusBean();
			reviewStatus.setDataId(dataId);
			reviewStatus.setDataName(dataName);
			reviewStatus.setDataType(dataType);
			reviewStatus.setSubmittedBy(SpringSecurityUtil.getLoggedInUserName());
			reviewStatus.setSubmittedDate(new Date());
		} else {
			if (reviewStatus.getReviewStatus().equals(status)) {
				return;
			}
		}
		reviewStatus.setReviewStatus(status);
		getCurationServiceDAO().submitDataForReview(reviewStatus);
	}

	/**
	 * 
	 * @param entityId sample id, publication id, etc.
	 * @param request
	 * @param dataId
	 * @param dataName
	 * @param dataType
	 * @throws Exception
	 */
	protected void retractFromPublic(String entityId, HttpServletRequest request, Long dataId, String dataName, String dataType, Class clazz) throws Exception
	{
		updateReviewStatusTo(DataReviewStatusBean.RETRACTED_STATUS, request, dataId.toString(), dataName, dataType);
		getSpringSecurityAclService().retractObjectFromPublic(dataId, clazz);
	}

	// check if the entered group name is valid
	private Boolean validateGroupAccess(HttpServletRequest request, AccessControlInfo access) throws Exception
	{
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		
		if (userDetails.isCurator() && AccessTypeEnum.ROLE.getAccessType().equalsIgnoreCase(access.getAccessType()) &&
			CaNanoRoleEnum.ROLE_ANONYMOUS.getRoleName().equalsIgnoreCase(access.getRecipientDisplayName()))
		{
			return true;
		}
		else if (AccessTypeEnum.GROUP.getAccessType().equalsIgnoreCase(access.getAccessType()))
			return userDetails.belongsToGroup(access.getRecipient());
		else if (AccessTypeEnum.USER.getAccessType().equalsIgnoreCase(access.getAccessType()))
			return true;
		
		return false;
	}

	// check if the entered user name is valid
	private Boolean validateUserAccess(HttpServletRequest request, AccessControlInfo access) throws Exception
	{
		if (AccessTypeEnum.USER.getAccessType().equalsIgnoreCase(access.getAccessType()))
		{
			UserDetails userDetails = getUserDetailsService().loadUserByUsername(access.getRecipient());
			if (userDetails != null)
				return true;
		}
		else 
			return true;
		
		return false;
	}

	protected List<String> validateAccess(HttpServletRequest request, AccessControlInfo theAccess) throws Exception
	{
		Boolean accessValid = true;
		
		List<String> errors = new ArrayList<String>();
		if (AccessTypeEnum.fromString(theAccess.getAccessType()) == null)
		{
			String err = PropertyUtil.getPropertyReplacingToken("sample", "error.invalidAccessBy", "0", theAccess.getAccessType());
			errors.add(err);
			accessValid = false;
		}
		String permStr = theAccess.getRoleName();
		if (StringUtils.isEmpty(permStr)) {
			String err = PropertyUtil.getPropertyReplacingToken("sample", "error.invalidRoleName", "0", theAccess.getRoleName());
			errors.add(err);
			accessValid = false;
		}
		if (!validateGroupAccess(request, theAccess)) {
			errors.add("You've entered an invalid group name. Or you don't have acess to the group.");
			String err = PropertyUtil.getPropertyReplacingToken("sample", "error.invalidGroup", "0", theAccess.getRecipient());
			errors.add(err);
			accessValid = false;
		}
		if (!validateUserAccess(request, theAccess)) {
			String err = PropertyUtil.getPropertyReplacingToken("sample", "error.invalidUser", "0", theAccess.getRecipient());
			errors.add(err);
			accessValid = false;
		}
		request.getSession().setAttribute("accessValid", accessValid);
		return errors;
	}

	protected String validateId(HttpServletRequest request, String idParameter) throws Exception
	{
		String id = request.getParameter(idParameter);
		if (id == null) {
			id = (String) request.getAttribute(idParameter);
		}
		if (id == null) {
			throw new NotExistException("No such ID in the database");
		}
		// per app scan
		if (!id.matches(Constants.NUMERIC_PATTERN)) {
			throw new NotExistException("Invalid value for ID");
		}
		return id;
	}
	
	/**
	 * Another version of validateId() with id value passed in.
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	protected String validateCharId(String id) throws Exception
	{	
		if (id == null) {
			throw new NotExistException("No such ID in the database");
		}
		// per app scan
		if (!id.matches(Constants.NUMERIC_PATTERN)) {
			throw new NotExistException("Invalid value for ID");
		}
		return id;
	}

	protected void checkOpenAccessForm(HttpServletRequest request) throws Exception
	{
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
		Boolean accessValid = true;
		if (session.getAttribute("accessValid") != null) {
			accessValid = (Boolean) session.getAttribute("accessValid");
		}
		Boolean openAccess = false;
		if (dispatch.equals("input") && browserDispatch.equals("saveAccess") || !accessValid) {
			openAccess = true;
		}
		session.setAttribute("openAccess", openAccess);
		session.removeAttribute("accessValid");
		// in case of validation error still show access label correctly.
		request.setAttribute("onloadJavascript", "toggleAccessNameLabel()");
	}

	protected void setSummaryTab(HttpServletRequest request, int numTabs)
	{
		//TODO: watch
		
		String tab = (String) getValueFromRequest(request, "tab");
		// per app scan default tab to all;
		if (tab == null || tab.equals("ALL") || !tab.matches(Constants.NUMERIC_PATTERN)) {
			tab = "ALL";
		}
		if (tab.equals("ALL")) {
			request.getSession().removeAttribute("onloadJavascript");
			request.getSession().removeAttribute("tab");
		} else {
			request.getSession().setAttribute("onloadJavascript", "showSummary('" + tab + "', " + numTabs + ")");
		}
	}

	// escape XML for file uri in case xss is embedded in the URI and displayed
	// in the where validation errors are shown
	protected void escapeXmlForFileUri(FileBean file)
	{
		if (file != null && file.getDomainFile() != null && file.getDomainFile().getUri() != null) {
			String origUri = file.getDomainFile().getUri();
			file.getDomainFile().setUri(StringEscapeUtils.escapeXml(origUri));
		}
	}

	public void checkOpenAccessForm(PublicationForm form, HttpServletRequest request)
	{
		String dispatch = form.getDispatch();
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
		Boolean accessValid = true;
		if (session.getAttribute("accessValid") != null) {
			accessValid = (Boolean) session.getAttribute("accessValid");
		}
		Boolean openAccess = false;
		if (dispatch.equals("input") && browserDispatch.equals("saveAccess") || !accessValid) {
			openAccess = true;
		}
		session.setAttribute("openAccess", openAccess);
		session.removeAttribute("accessValid");
		// in case of validation error still show access label correctly.
		request.setAttribute("onloadJavascript", "toggleAccessNameLabel()");
	}
}
