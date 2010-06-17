<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th colspan="2">
			Collaborator Group Information
		</th>
	</tr>
	<tr>
		<td class="cellLabel" width="15%">
			Group Name*
		</td>
		<td>
			<html:text styleId="groupName" property="group.name" size="30" value="NCL_DNT_Collaboration"/>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="15%">
			Group Description
		</td>
		<td>
			<html:textarea styleId="groupDescription"
				property="group.description" rows="5" cols="70" value="NCL's collaboration with DNT"/>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="15%">
			User
		</td>
		<td>
			<div id="userSection">
				<a style="display: block" id="addUser" href="javascript:show('newUser')">Add</a>
				<br />
				<table id="userTable" class="editTableWithGrid" width="85%"
					style="display: none;">
					<tbody id="userRows">
						<tr id="patternHeader">
							<td width="30%" class="cellLabel">
								User Login Name
							</td>
							<td width="30%" class="cellLabel">
								Access to the Group
							</td>
							<td>
							</td>
						</tr>
						<tr id="pattern" style="display: block;">
							<td>
								<span id="userLoginName">janedoe</span>
							</td>
							<td>
								<span id="userRole">READ</span>
							</td>

							<td>
								<input type="button" class="noBorderButton" id="edit"
									value="Edit" onclick="">
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
				<table class="promptbox" width="85%">
					<tr>
						<td class="cellLabel" width="15%">
							User Login Name
						</td>
						<td>
							<html:text property="group.theUserAccessibility.userLoginName"
								styleId="userLoginName" onchange=""/>
						</td>
						<td class="cellLabel" width="20%">
							Access to the Group
						</td>
						<td>
							<html:select property="group.theUserAccessibility.roleName"
								styleId="roleName" onchange="">
								<option></option>
								<option value="">
									READ
								</option>
								<option value="">
									READ, UPDATE, DELETE
								</option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td>
							<input style="display: none;" id="deleteUser" type="button"
								value="Remove">
						</td>
						<td align="right" colspan="3">
							<div align="right">
								<input class="promptButton" type="button" value="Save" />
								<input class="promptButton" type="reset" value="Cancel">
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td><input style="display: none;" id="deleteUser" type="button"
				value="Remove">
		<br></td>
		<td align="right" colspan="3">
			<div align="right">
				<input type="button" value="Save" />
				<input type="reset" value="Cancel" onclick="javascript:hide('newCollaboratorGroup');show('newCollaboratorGroupLabel'); show('addCollaboratorGroup');">
			</div>
		</td>
	</tr>
</table>
