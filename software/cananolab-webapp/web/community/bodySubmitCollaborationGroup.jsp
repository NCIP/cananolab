<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="subSubmissionView" width="85%" align="center" summary="layout">
	<tr>
		<th colspan="2">
			Collaboration Group Information
		</th>
	</tr>
	<tr>
		<td class="cellLabel" width="15%">
			<label for="groupName">Name*</label>
		</td>
		<td>
			<html:text styleId="groupName" property="group.name" size="30"/>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="15%">
			<label for="groupDescription">Description</label>
		</td>
		<td>
			<html:textarea styleId="groupDescription"
				property="group.description" rows="5" cols="70"/>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="15%" id="userLabel">
			User
		</td>
		<td>
			<div id="userSection">
				<a style="display:block" id="addUser" href="javascript:show('newUser');clearUserAccess();">Add</a>
				<br />
				<table id="userTable" class="editTableWithGrid" width="95%"
					style="display: none;">
					<tbody id="userRows">
						<tr id="patternHeader">
							<td width="40%" class="cellLabel" scope="col">
								Login Name
							</td>
							<td class="cellLabel" scope="col">
								Access to the Group
							</td>
							<td>
							</td>
						</tr>
						<tr id="pattern" style="display: none;">
							<td>
								<span id="rowUserLoginName">login name</span>
							</td>
							<td>
								<span id="rowRoleName">role name</span>
							</td>
							<td>
								<input type="button" class="noBorderButton" id="edit"
									value="Edit" onclick="editUserAccess(this.id);">
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</td>
	</tr>

	<tr>
		<td class="cellLabel" width="15%">
		</td>
		<td>
			<div id="newUser" style="display: none">
				<table class="promptbox" width="95%" summary="layout" border="10">
					<tr>
						<td class="cellLabel" width="30%">
							<label for="userBean.loginName">User Login Name*</label>
						</td>
						<td width="30%">
							<html:text property="group.theAccess.userBean.loginName"
								styleId="userBean.loginName"/>
						</td>
						<td>
									<a href="#userNameField"
										onclick="javascript:showMatchedUserDropdown()"><img
											src="images/icon_browse.jpg" align="middle"
											alt="search existing users" border="0" /></a>
						</td>
						<td>
							<table class="invisibleTable" summary="layout">
								<tr>
									<td>
										<img src="images/ajax-loader.gif" border="0" class="counts"
											id="loaderImg" style="display: none" alt="load existing users">
									</td>
									<td><label for="matchedUserNameSelect">&nbsp;</label>
										<html:select
											property="group.theAccess.userBean.loginName"
											size="10" styleId="matchedUserNameSelect" style="display: none" onclick="updateUserLoginName()">
										</html:select>
									</td>
									<td><a id="cancelBrowse" style="display:none" href="javascript:cancelBrowseSelect();">Cancel</a></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							<label for="roleName">Access to the Group*</label>
						</td>
						<td colspan="3">
								<html:select property="group.theAccess.roleDisplayName"
								styleId="roleName" onchange="">
								<option></option>
								<html:options collection="csmRoleNames" labelProperty="label" property="value"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td>
							<input class="promptButton" style="display: none;" id="deleteUser" type="button"
								value="Remove" onclick="deleteTheUserAccess();">
						</td>
						<td align="right" colspan="3">
							<div align="right">
								<input class="promptButton" type="button" value="Save" onclick="javascript:addUserAccess();" />
								<input class="promptButton" type="button" value="Cancel" onclick="clearUserAccess();closeSubmissionForm('User');">
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td><input style="display: none;" id="deleteCollaborationGroup" type="button"
				value="Remove" onclick="javascript:deleteTheCollaborationGroup()">
		<br></td>
		<td align="right" colspan="3">
			<div align="right">
				<html:submit styleId="submitButton"/>
					<input type="hidden" name="dispatch" value="create">
					<input type="hidden" name="page" value="1">
					<html:hidden styleId="groupId"	property="group.id" />
				<input type="reset" value="Cancel" onclick="javascript:hide('newCollaborationGroup');show('newCollaborationGroupLabel'); show('addCollaborationGroup');" id="resetButton">
			</div>
		</td>
	</tr>
</table>
