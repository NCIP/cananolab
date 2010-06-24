<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	width="100%" height="100%">
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyTitle.jsp">
				<jsp:param name="pageTitle" value="Manage Community" />
				<jsp:param name="topic" value="manage_community_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="welcomeContent">
			This is the manage community section which allows users ...
			<br>
		</td>
	</tr>

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
						<table summary="" cellpadding="0" cellspacing="0" border="0"
							width="100%" height="100%" class="sidebarSection">
							<tr>
								<td height="20" class="sidebarTitle">
									COMMUNITY LINKS
								</td>
							</tr>
							<tr>
								<td class="sidebarContent">
									<a href="collaborationGroup.do?dispatch=setupNew">Manage Collaboration Groups</a>
									<br>
									Select to create, update, delete collaboration groups and
									manage users within the groups.
								</td>
							</tr>
							<%--
							<tr>
								<td class="sidebarContent">
									<a href="${createLink}">Manage Organizations</a>
									<br>
									Select to create, update, delete organizations and
									manage point of contacts within the organizations.
								</td>
							</tr>
							--%>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td width="60%"></td>
	</tr>
</table>
<br>

