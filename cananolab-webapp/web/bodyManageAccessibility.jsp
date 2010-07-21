<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript"
	src="javascript/AccessibilityManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/AccessibilityManager.js'></script>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<tr>
	<td class="cellLabel" width="15%">
		Access to the Sample
	</td>
	<td>
		<c:set var="newAddAccessButtonStyle" value="display:block" />
		<a href="#" onclick="javascript:openSubmissionForm('Access');" id="addAccess"
			style="${newAddAccessButtonStyle}"><img align="top"
				src="images/btn_add.gif" border="0" /> </a>
	</td>
</tr>
<tr>
	<td colspan="2">
		<c:set var="edit" value="true" />
		<c:if test="${!empty groupAccesses}">
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
				<c:forEach var="access" items="groupAccesses">
					<tr valign="top">
						<td>
							${access.groupName}
						</td>
						<td>
							${access.roleDisplayName}
						</td>
						<td align="right">
							<a
								href="javascript:show('newAccess');show('deleteAccess');hide('accessByLabel'); hide('accessByType');">Edit</a>&nbsp;
						</td>
					</tr>
				</c:forEach>
			</table>
			<br />
		</c:if>
	</td>
</tr>
<tr>
	<td colspan="2">
		<c:set var="edit" value="true" />
		<c:if test="${!empty userAccesses}">
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
				<c:forEach var="access" items="userAccesses">
				<tr valign="top">
					<td>
						${access.userBean.loginName}
					</td>
					<td>
						${access.roleDisplayName}
					</td>
					<td align="right">
						<a
							href="javascript:show('newAccess');show('deleteAccess');hide('accessByLabel'); hide('accessByType');">Edit</a>&nbsp;
					</td>
				</tr>
				</c:forEach>
			</table>
			<br />
		</c:if>
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
					<td class="cellLabel" width="25%">
						Access by *
					</td>
					<td colspan="2" id="accessByType">
						<html:radio styleId="accessByGroup"
							property="${accessParent}.theAccess.accessBy" value="group" onclick="displayAccessNameLabel();"  />
						Collaboration Group &nbsp;&nbsp;<html:radio styleId="accessByUser" property="${accessParent}.theAccess.accessBy" value="user"  onclick="displayAccessNameLabel();"/>User
					</td>
					<td></td>
				</tr>
				<tr>
					<td class="cellLabel" id="accessNameLabel">
						Collaboration Group Name
					</td>
					<td>
						<html:text styleId="groupName" property="${accessParent}.theAccess.groupName"/>
						<html:text styleId="userName" property="${accessParent}.theAccess.userBean.loginName" style="display:none"/>
					</td>
					<td>
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
					<td class="cellLabel" width="10%">
						Access to the Sample *
					</td>
					<td colspan="2">
						<html:select property="${accessParent}.theAccess.roleName"
							styleId="roleName" onchange="">
							<option></option>
							<html:options collection="csmRoleNames" labelProperty="label"
								property="value" />
						</html:select>
					</td>
					<td></td>
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
								onclick="javascript:closeSubmissionForm('Access');">
						</div>
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>
