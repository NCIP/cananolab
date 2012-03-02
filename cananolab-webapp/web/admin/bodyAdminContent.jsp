<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.cananolab.util.Constants"%>

<jsp:include page="${itemDescription}" />
<br>
<c:if test="${!empty user && user.admin}">
	<table summary="" cellpadding="0" cellspacing="0" border="0"
		width="40%" height="100%" class="sidebarSection">
		<tr>
			<th scope="col" align="left" class="sidebarTitle" height="20"><c:out
					value="${fn:toUpperCase(menu)}" /> LINKS</td>
		</tr>
		<tr>
			<td class="sidebarContent"><a
				href="javascript:showUPTWarning();"> Create User Accounts and
					Assign Privileges</a> <br> Click to launch the NCI User
				Provisioning Tool (UPT) which will assist in creating users.</td>
		</tr>
		<tr>
			<c:set var="dispatch" value="setupNew"/>
			<c:if test="${!empty existingSiteBean && !empty existingSiteBean.siteName}">
				<c:set var="dispatch" value="setupUpdate"/>
			</c:if>
			<td class="sidebarContent"><a href="admin.do?dispatch=${dispatch}&page=0">
					Configure Site Preferences</a> <br> Click to configure site
				preferences including custom logos and site banners.</td>
		</tr>
		<c:if test="${!empty user && user.curator}">
			<tr>
				<td class="sidebarContent"><a
					href="transferOwner.do?dispatch=setupNew&page=0"> Transfer Data
						Ownership</a> <br> Click to reassign ownership for samples,
					protocols, publications and collaboration groups.</td></tr>
		</c:if>
	</table>
</c:if>

