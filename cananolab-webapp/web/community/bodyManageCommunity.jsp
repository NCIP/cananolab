<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Manage Community" />
	<jsp:param name="topic" value="manage_collaboration_groups_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<div class="welcomeContent">This is the manage community section
	which allows a user to manage collaboration groups. In this section, a
	user can create, edit, or delete a collaboration group and assign users
	to participate in the collaboration group.</div>
<br>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="40%"
	height="100%" class="sidebarSection">
	<tr>
		<th scope="col" align="left" height="20" class="sidebarTitle">COMMUNITY
			LINKS</th>
	</tr>
	<tr>
		<td class="sidebarContent"><a
			href="collaborationGroup.do?dispatch=setupNew&page=0">Manage
				Collaboration Groups</a> <br> Select to create, update, delete
			collaboration groups and manage users within the groups.</td>
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