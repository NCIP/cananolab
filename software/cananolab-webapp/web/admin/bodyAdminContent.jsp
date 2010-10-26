<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.cananolab.util.Constants"%>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	width="100%" height="100%">
	<jsp:include page="${itemDescription}" />
	<tr>
		<td valign="top" width="40%">
			<!-- sidebar begins -->
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				height="100%">
				<tr>
					<td height="30">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td valign="top">
						<c:if test="${!empty user && user.admin}">
							<table summary="" cellpadding="0" cellspacing="0" border="0"
								width="100%" height="100%" class="sidebarSection">
								<tr>
									<td class="sidebarTitle" height="20">
										<c:out value="${fn:toUpperCase(item)}" />
										LINKS
									</td>
								</tr>
								<tr>
									<td class="sidebarContent">
										<a href="javascript:showUPTWarning();"> Create User
											Accounts and Assign Privileges </a>
										<br>
										Click to launch the NCI User Provisioning Tool (UPT) which
										will assist in creating users.
									</td>
								</tr>
								<tr>
									<td class="sidebarContent">
										<a href="admin.do?dispatch=setupNew"> Configure Site
											Preferences </a>
										<br>
										Click to configure site preferences including custom logos and
										site banners.
									</td>
								</tr>
								<c:if test="${!empty user && user.curator}">
									<tr>
										<td class="sidebarContent">
											<a href="transferOwner.do?dispatch=setupNew&page=0">
												Transfer Data Ownership </a>
											<br>
											Click to reassign ownership for samples, protocols,
											publications and collaboration groups.
										</td>
									</tr>
								</c:if>
							</table>
						</c:if>
					</td>
				</tr>
			</table>
		</td>
		<td width="60%"></td>
	</tr>
</table>
<br>

