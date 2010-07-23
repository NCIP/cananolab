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
		Access to the ${dataType}
	</td>
	<td>
		<c:set var="newAddAccessButtonStyle" value="display:block" />
		<a href="#" onclick="confirmAddNew(['PointOfContact'], 'Access', 'Access', 'clearAccess()'); disableButtons(['submitButton', 'resetButton']);" id="addAccess"
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
				<c:forEach var="access" items="${groupAccesses}">
					<tr valign="top">
						<td>
							${access.groupName}
						</td>
						<td>
							${access.roleDisplayName}
						</td>
						<td align="right">
							<c:choose>
								<c:when test="${access.groupName eq 'Public'}">
									<c:if test="${user.curator}">
									    <a href="#">remove</a>
									</c:if>
								</c:when>
								<c:otherwise>
									<a
										href="javascript:setTheAccess('${parentForm}', 'group', '${access.groupName}', 'sample', '${protectedData}');">Edit</a>&nbsp;
							    </c:otherwise>
							</c:choose>
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
				<c:forEach var="access" items="${userAccesses}">
					<tr valign="top">
						<td>
							${access.userBean.loginName}
						</td>
						<td>
							${access.roleDisplayName}
						</td>
						<td align="right">
							<c:choose>
								<c:when test="${access.userBean.loginName eq user.loginName}">
								</c:when>
								<c:otherwise>
									<a
										href="javascript:setTheAccess('${parentForm}', 'user', '${access.userBean.loginName}', 'sample', '${protectedData}');">Edit</a>&nbsp;
							    </c:otherwise>
							</c:choose>
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
					<td colspan="2">
						<html:radio styleId="byGroup"
							property="${accessParent}.theAccess.groupAccess" value="true" onclick="displayAccessNameLabel();"  />
						Collaboration Group &nbsp;&nbsp;<html:radio styleId="byUser" property="${accessParent}.theAccess.groupAccess" value="false"  onclick="displayAccessNameLabel();"/>User
					</td>
					<td></td>
				</tr>
				<tr>
					<c:set var="accessNameLabelValue" value="Collaboration Group Name *"/>
					<td class="cellLabel" id="accessNameLabel">
						${accessNameLabelValue}
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
						<input
							id="deleteAccess" type="button" value="Remove" onclick="javascript:deleteTheAccess('sample');">
					</td>
					<td align="right" colspan="3">
						<div align="right">
							<input type="button" value="Save" onclick="javascript:addAccess('sample');" />
							<input type="reset" value="Cancel"
								onclick="javascript:closeSubmissionForm('Access');">
						</div>
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>
