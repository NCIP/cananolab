<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Manage Collaborator Groups" />
	<jsp:param name="topic" value="manage_collaborator_groups_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/collaboratorGroup">
	<jsp:include page="/bodyMessage.jsp?bundle=community" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" colspan="2">
				Existing Collaborator Groups
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="edit" value="true" />
				<table class="editTableWithGrid" width="95%" align="center">
					<tr>
						<th>
							Group Name
						</th>
						<th>
							Group Description
						</th>
						<th>
							Users (Access)
						</th>
						<th></th>
					</tr>
					<tr valign="top">
						<td>
							NCL_DNT_Collaboration
						</td>
						<td>
							NCL's collaboration with DNT
						</td>
						<td>
							janedoe (READ)
						</td>
						<td align="right">
							<a
								href="javascript:show('newCollaboratorGroup');hide('newCollaboratorGroupLabel'); hide('addCollaboratorGroup');">Edit</a>&nbsp;
						</td>
					</tr>
				</table>
				<br />
			</td>
		</tr>
		<tr>
			<td class="cellLabel" width="20%" id="newCollaboratorGroupLabel"
				style="display: block">
				New Collaborator Group
			</td>
			<td>
				<c:set var="newAddCGButtonStyle" value="display:block" />
				<a href="#" onclick="javascript:show('newCollaboratorGroup')"
					id="addCollaboratorGroup" style="${newAddCGButtonStyle}"><img
						align="top" src="images/btn_add.gif" border="0" /> </a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="newCGStyle" value="display:none" />
				<div style="${newCGStyle}" id="newCollaboratorGroup">
					<a name="submitCollaboratorGroup"><%@ include
							file="bodySubmitCollaboratorGroup.jsp"%></a>
				</div>
			</td>
		</tr>
	</table>
	<br />
</html:form>