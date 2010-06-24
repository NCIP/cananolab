<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<tr>
	<td class="cellLabel" width="15%">
		Access to the Sample
	</td>
	<td>
		<c:set var="newAddAccessButtonStyle" value="display:block" />
		<a href="#" onclick="javascript:show('newAccess');hide('deleteAccess');show('accessByLabel');show('accessByType');" id="addAccess"
			style="${newAddAccessButtonStyle}"><img align="top"
				src="images/btn_add.gif" border="0" /> </a>
	</td>
</tr>
<tr>
	<td colspan="2">
		<c:set var="edit" value="true" />
		<table class="editTableWithGrid" width="95%" align="center">
			<tr>
				<th width="40%">
					Collaboration Group Name
				</th>
				<th width="55%">
					Access
				</th>
				<th></th>
			</tr>
			<tr valign="top">
				<td>
					NCL_DNT_Collaboration
				</td>
				<td>
					READ, UPDATE, DELETE
				</td>
				<td align="right">
					<a href="javascript:show('newAccess');show('deleteAccess');hide('accessByLabel'); hide('accessByType');">Edit</a>&nbsp;
				</td>
			</tr>
			<tr valign="top">
				<td>
					NCL_GT_Collaboration
				</td>
				<td>
					READ
				</td>
				<td align="right">
					<a href="javascript:show('newAccess');show('deleteAccess');hide('accessByLabel'); hide('accessByType');">Edit</a>&nbsp;
				</td>
			</tr>
		</table>
		<br />
	</td>
</tr>
<tr>
	<td colspan="2">
		<c:set var="edit" value="true" />
		<table class="editTableWithGrid" width="95%" align="center">
			<tr>
				<th width="40%">
					User Name
				</th>
				<th width="55%">
					Access
				</th>
				<th></th>
			</tr>
			<tr valign="top">
				<td>
					janedoe
				</td>
				<td>
					READ, UPDATE, DELETE
				</td>
				<td align="right">
					<a href="javascript:show('newAccess');show('deleteAccess');hide('accessByLabel'); hide('accessByType');">Edit</a>&nbsp;
				</td>
			</tr>
		</table>
		<br />
	</td>
</tr>
<tr>
	<td colspan="2">
		<c:set var="newAccesstyle" value="display:none" />
		<div style="${newAccesstyle}" id="newAccess">
			<table class="subSubmissionView" width="85%" align="center">
				<tr>
					<th colspan="4">
						Access Information
					</th>
				</tr>
				<tr>
					<td class="cellLabel" width="30%" id="accessByLabel" style="display:block">
						Access by
					</td>
					<td colspan="3" id="accessByType" style="display:block">
						<input type="radio" name="accessType" value="groupAccess"/>Collaboration Group &nbsp;&nbsp;
						<input type="radio" name="accessType" value="userAccess"/>User
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="30%">
						Collaboration Group Name *
					</td>
					<td>
					    <input type="text" id="access" value="NCL_DNT_Collaboration"/>
					</td>
					<td width="5">
						<a href="#userNameField" onclick=""><img
								src="images/icon_browse.jpg" align="middle"
								alt="search existing collaboration groups" border="0" /> </a>
					</td>
					<td width="50%">
						<table class="invisibleTable">
							<tr>
								<td>
									<img src="images/ajax-loader.gif" border="0" class="counts"
										id="loaderImg" style="display: none">
								</td>
								<td>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="30%">
						Access to the Sample *
					</td>
					<td colspan="2">
						<select name="access">
							<option></option>
							<option value="">
								READ
							</option>
							<option value="" selected>
								READ, UPDATE, DELETE
							</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<input class="promptButton"
							id="deleteAccess" type="button" value="Remove">
					</td>
					<td align="right" colspan="3">
						<div align="right">
							<input class="promptButton" type="button" value="Save" />
							<input class="promptButton" type="reset" value="Cancel"
								onclick="javascript:hide('newAccess');">
						</div>
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>
