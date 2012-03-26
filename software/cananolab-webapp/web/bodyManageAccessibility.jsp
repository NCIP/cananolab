<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript" src="javascript/AccessibilityManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/AccessibilityManager.js'></script>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<c:if test="${isOwner || newData eq 'true'}">

	<tr>
		<td class="cellLabel" width="15%" id="addAccessLabel">
			Access to the ${dataType}
		</td>
		<td>
			<c:set var="newAddAccessButtonStyle" value="display:block" />
			<a href="#"
				onclick="confirmAddNew(['PointOfContact'], 'Access', 'Access', 'clearAccess(\'${parentFormName}\', \'${dataType}\')');"
				id="addAccess" style="${newAddAccessButtonStyle}"><img
					align="top" src="images/btn_add.gif" border="0" alt="add accessibility"/></a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:set var="edit" value="true" />
			<c:if test="${!empty groupAccesses}">
				<table class="editTableWithGrid" width="95%" align="center">
					<tr>
						<th width="40%" scope="col">
							Group Name
						</th>
						<th width="55%" scope="col">
							Access
						</th>
						<th></th>
					</tr>
					<c:forEach var="access" items="${groupAccesses}">
						<tr valign="top">
							<td>
								<c:out value="${access.groupName}" />
							</td>
							<td>
								<c:out value="${access.roleDisplayName}"/>
							</td>
							<td><div align="right">
								<c:choose>
									<c:when
										test="${access.groupName eq 'Curator' || access.groupName eq 'Public' && !user.curator}">
									</c:when>
									<c:otherwise>
										<a
											href="javascript:setTheAccess('${parentFormName}', 'group', '${access.groupName}', '${dataType}', '${protectedData}');">Edit</a>&nbsp;
							    </c:otherwise>
								</c:choose>
								</div>
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
						<th width="40%" scope="col">
							User Login Name
						</th>
						<th width="55%" scope="col">
							Access
						</th>
						<th></th>
					</tr>
					<c:forEach var="access" items="${userAccesses}">
						<tr valign="top">
							<td>
								<c:out value="${access.userBean.loginName}"/>
							</td>
							<td>
								<c:out value="${access.roleDisplayName}"/>
							</td>
							<td><div align="right">
								<c:choose>
									<c:when test="${access.userBean.loginName eq user.loginName}">
									</c:when>
									<c:otherwise>
										<a
											href="javascript:setTheAccess('${parentFormName}', 'user', '${access.userBean.loginName}', '${dataType}', '${protectedData}');">Edit</a>&nbsp;
							    </c:otherwise>
								</c:choose>
								</div>
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
			<c:if test="${openAccess eq 'true'}">
				<c:set var="newAccesstyle" value="display:block" />
			</c:if>
			<div style="${newAccesstyle}" id="newAccess">
				<table class="subSubmissionView" width="85%" align="center" summary="layout">
					<tr>
						<th colspan="4">
							Access Information
						</th>
					</tr>
					<tr>
						<td class="cellLabel" width="25%">
							Access by *
						</td>
						<td colspan="3">
							<html:radio styleId="byGroup"
								property="${accessParent}.theAccess.accessBy" value="group"
								onclick="displayAccessNameLabel();" />
							<label for="byGroup">Collaboration Group</label>
							<html:radio styleId="byUser"
								property="${accessParent}.theAccess.accessBy" value="user"
								onclick="displayAccessNameLabel();" />
							<label for="byUser">User&nbsp;&nbsp;</label>
							<c:if test="${user.curator}">
								<html:radio styleId="byPublic"
									property="${accessParent}.theAccess.accessBy" value="public"
									onclick="displayAccessNameLabel();" />
							<label for="byPublic">Public</label>
							</c:if>
						</td>
					</tr>
					<tr>
						<c:set var="accessNameLabelValue"
							value="Collaboration Group Name *" />
						<td class="cellLabel" id="accessNameLabel">
							<c:out value="${accessNameLabelValue}"/>
							
						</td>
						<td><label for="groupName" style="display:none">Group Name</label>
							<html:text styleId="groupName"
								property="${accessParent}.theAccess.groupName" />
							<label for="userName" style="display:none">Login Name</label>
							<html:text styleId="userName"
								property="${accessParent}.theAccess.userBean.loginName"
								style="display:none" />
						</td>
						<td>
							<a href="#userNameField" id="browseIcon"
								onclick="javascript:showMatchedGroupOrUserDropdown('${ownerName}')"><img
									src="images/icon_browse.jpg" align="middle"
									alt="search existing collaboration groups" border="0" /></a>
						</td>
						<td width="50%">
							<table class="invisibleTable">
								<tr>
									<td>
										<img src="images/ajax-loader.gif" border="0" class="counts"
											id="loaderImg" style="display: none" alt="collaboration groups or user login names">										
									</td>
									<td>
									    <label for="matchedUserNameSelect" style="display:none" id="userNameSelectLabel">Select a user</label>
										<html:select
											property="${accessParent}.theAccess.userBean.loginName"
											size="10" styleId="matchedUserNameSelect"
											style="display: none" onclick="updateUserLoginName()">
										</html:select>										
										<label for="matchedGroupNameSelect" style="display:none">Select a group</label>
										<html:select property="${accessParent}.theAccess.groupName"
											size="10" styleId="matchedGroupNameSelect"
											style="display: none" onclick="updateGroupName()">
										</html:select>
									</td>
									<td>
										<a id="cancelBrowse" style="display: none"
											href="javascript:cancelBrowseSelect();">Cancel</a>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Access to the <label for="roleName"><c:out value="${dataType}"/> *</label>
						</td>
						<td colspan="2">
							<html:select property="${accessParent}.theAccess.roleName"
								styleId="roleName">
								<option></option>
								<html:options collection="csmRoleNames" labelProperty="label"
									property="value" />
							</html:select>
						</td>
						<td></td>
					</tr>
					<tr>
						<td><input id="deleteAccess" type="button" value="Remove"
								onclick="javascript:deleteTheAccess('${parentFormName}', '${parentAction}', ${parentPage});"
								style="display: none;">
						</td>
						<td align="right" colspan="3">
							<div align="right">
								<html:hidden property="${accessParent}.theAccess.roleName"
									styleId="hiddenRoleName" />
								<html:hidden property="${accessParent}.theAccess.groupName"
									styleId="hiddenGroupName" />
								<input type="button" value="Save"
									onclick="javascript:addAccess('${parentFormName}', '${parentAction}', ${parentPage}, ${isPublic});" />
								<input type="button" value="Cancel"
									onclick="javascript:clearAccess('${parentFormName}', '${dataType}');closeSubmissionForm('Access');">
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
</c:if>