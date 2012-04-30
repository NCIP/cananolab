<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.cananolab.util.Constants"%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Site Preference" />
	<jsp:param name="topic" value="site_preference_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/admin" enctype="multipart/form-data" styleId="sitePreferenceForm">
	<jsp:include page="/bodyMessage.jsp" />
	<table width="100%" align="center" class="submissionView" summary="layout">
		<tr>
			<td class="cellLabel"><label for="siteName">Site Name *</label></td>
			<td colspan="3"><html:text styleId="siteName"
					property="sitePreference.siteName" size="50" /></td>
		</tr>
		<tr>
			<td class="cellLabel"><label for="siteLogoFile">Upload Logo File</label></td>
			<td colspan="3"><html:file property="sitePreference.siteLogoFile.uploadedFile" styleId="siteLogoFile"/>&nbsp; <em>(Recommended
					image dimension: 304 x 83 pixels, maximum image size: <%=Constants.MAX_LOGO_SIZE%>
					bytes)</em>					
			<c:set var="uploadedUriStyle" value="display:none" />
			<c:if test="${!empty existingSiteBean && !empty existingSiteBean.siteLogoFilename}">
				<c:set var="uploadedUriStyle" value="display:block" />
			</c:if>
			<span id="uploadedUri" style="${uploadedUriStyle}"><c:out value="${existingSiteBean.siteLogoFilename}"/></span>			 
			</td>
		</tr>
	</table>
	<br>
	<c:set var="updateId" value="bogus" />
	<c:set var="hiddenDispatch" value="update" />
	<c:set var="hiddenPage" value="1" />
	<c:set var="resetOnclick"
		value="javascript:location.href='admin.do?dispatch=setupNew&page=0'" />
	<c:if test="${!empty existingSiteBean.siteName}">
		<c:set var="showDelete" value="true"/>
	</c:if>
	<c:set var="deleteOnclick"
		value="deleteData('site preference', 'sitePreferenceForm', 'admin', 'remove')"/>
	<c:set var="deleteButtonName" value="Delete" />
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>
