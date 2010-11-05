<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript"
	src="javascript/CollaborationGroupManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CollaborationGroupManager.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Manage Collaboration Groups" />
	<jsp:param name="topic" value="manage_collaboration_groups_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/collaborationGroup">
	<jsp:include page="/bodyMessage.jsp?bundle=community" />

	<table width="100%" align="center" class="submissionView">
		<c:if test="${!empty existingCollaborationGroups}">
			<tr>
				<td class="cellLabel" width="25%">
					Existing Collaboration Groups
				</td>
				<td></td>
			</tr>
			<tr>
				<td colspan="2">
					<c:set var="edit" value="true" />
					<table class="editTableWithGrid" width="95%" align="center">
						<tr>
							<th>
								Name
							</th>
							<th width="33%">
								Description
							</th>
							<th>
								Owner
							</th>
							<th>
								Users (Access)
							</th>
							<th></th>
						</tr>
						<c:forEach var="group" items="${existingCollaborationGroups}">
							<tr valign="top">
								<td>
									<c:out value="${group.name}"/>
								</td>
								<td>
									<c:out value="${group.description}"/>
								</td>
								<td>
									<c:out value="${group.ownerName}"/>
								</td>
								<td>
									<c:forEach var="userAccess" items="${group.userAccesses}">
									<c:out value="${userAccess.userBean.loginName}"/> (<c:out value="${userAccess.roleDisplayName}"/>)<br />
									</c:forEach>
								</td>
								<td align="right">
									<c:if test="${group.userUpdatable}">
										<a href="javascript:setTheCollaborationGroup(${group.id});">Edit</a>&nbsp;
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</table>
					<br />
				</td>
			</tr>
		</c:if>
		<tr>
			<td class="cellLabel" id="newCollaborationGroupLabel"
				style="display: block" width="25%">
				New Collaboration Group
			</td>
			<td>
				<c:set var="newAddCGButtonStyle" value="display:block" />
				<a href="#"
					onclick="javascript:clearCollaborationGroup();openSubmissionForm('CollaborationGroup');"
					id="addCollaborationGroup" style="${newAddCGButtonStyle}"><img
						align="top" src="images/btn_add.gif" border="0" /> </a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="newCGStyle" value="display:none" />
				<c:if test="${openCollaborationGroup}">
					<c:set var="newCGStyle" value="display:block" />
				</c:if>
				<div style="${newCGStyle}" id="newCollaborationGroup">
					<a name="submitCollaborationGroup"><%@ include
							file="bodySubmitCollaborationGroup.jsp"%></a>
				</div>
			</td>
		</tr>
	</table>
	<br />
</html:form>