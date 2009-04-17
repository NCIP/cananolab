<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/POCManager.js"></script>
<script type="text/javascript" src="javascript/script.js"></script>	
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/SampleManager.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="formSubTitleWithBottomNoRight" colspan="5">
				<span>${param.pocTitle}</span>
			</td>
			<td class="formSubTitleWithBottomNoLeft" align="right">
				&nbsp;
				<!--<a href="#" onclick="showhide('${param.pocTitle}');">show/hide</a>&nbsp;  -->
				<c:if test="${param.pocTitle ne 'Primary Point of Contact' }">
					<a href="#"
						onclick="removeComponent(submitPointOfContactForm, 'submitPointOfContact', ${param.pocIndex}, 'removePointOfContact');return false;">
						<img src="images/delete.gif" border="0"
							alt="remove this point of contact"> </a>
				</c:if>
			</td>
		</tr>
	</tbody>
</table>
<c:choose>
	<c:when test="${param.pocTitle ne 'Primary Point of Contact' }">
		<table class="topBorderOnly" cellspacing="0" cellpadding="3"
			width="100%" align="center" summary="" border="0">
			<tbody>
				<tr>
					<td class="completeLabelNoTop" valign="top">						
						<a href="javascript:showhide('allPOC_${param.pocTitle}');">
							<span class="addLink2">Search
							Existing Point of Contact</span></a>&nbsp;
					</td>
				</tr>
			</tbody>
		</table>

	</c:when>
</c:choose>
<div id="allPOC_${param.pocTitle}" style="display: none;">
	<table cellspacing="0" cellpadding="3"
		width="100%" align="center" summary="" border="0">
		<tbody>
			<tr>
				<c:choose>
					<c:when test="${param.pocTitle ne 'Primary Point of Contact' }">
						<td valign="top">
							<strong>&nbsp;&nbsp;&nbsp;&nbsp;</strong>
						</td>
						<td valign="top" colspan="5">
							<c:if test="${!empty allPointOfContacts}">
								<logic:iterate id="existingPocBean" name="allPointOfContacts">
									<a href="javascript:setSecondaryPOC(submitPointOfContactForm, '${existingPocBean.domain.id}', '${param.pocIndex}');">
										<bean:write name="existingPocBean" property="displayName"/></a><br>							  
								</logic:iterate>
							</c:if>
						</td>
					</c:when>
				</c:choose>
			</tr>
		</tbody>
	</table>
</div>
<div id="${param.pocTitle}" style="display: block;">
	<table class="submissionView" cellspacing="0" cellpadding="3"
		width="100%" align="center" summary="" border="0">
		<tbody>			
			<tr>
				<td class="cellLabel" valign="top">
					<strong>First Name</strong>
				</td>
				<td valign="top">
					<html:text property="${param.pocBean}.domain.firstName" size="15" />
				</td>
				<td class="cellLabel" valign="top">
					<strong>Middle Initial</strong>
				</td>
				<td valign="top">
					<html:text property="${param.pocBean}.domain.middleInitial"	size="5" />
				</td>
				<td class="cellLabel" valign="top">
					<strong>Last Name</strong>
				</td>
				<td valign="top">
					<html:text property="${param.pocBean}.domain.lastName" size="15" />
				</td>
			</tr>
			<tr>
				<td class="cellLabel" valign="top">
					<strong>Phone Number</strong>
				</td>
				<td valign="top">
					<html:text property="${param.pocBean}.domain.phone" size="30" />
				</td>
				<td class="cellLabel" valign="top">
					<strong>Email</strong>
				</td>
				<td valign="top" colspan="4">
					<html:text property="${param.pocBean}.domain.email" size="30" styleId="emailText" />
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					<strong>Organization Name*</strong>
				</td>
				<td>
					<html:select property="${param.pocBean}.organization.name"
						styleId="orgName_${param.pocIndex}"
						onchange="javascript:removeOrgVisibilityByName('orgName_${param.pocIndex}', '${param.pocBean}.visibilityGroup');setOrganization(submitPointOfContactForm, 'orgName_${param.pocIndex }', '${param.pocIndex }' );callPrompt('Organization Name', 'orgName_${param.pocIndex }');">
						<option value="" />
							<html:options name="allOrganizationNames" />
						<option value="other">
							[Other]
						</option>
					</html:select>
				</td>
				<td class="cellLabel" valign="top">
					<strong>Role</strong>
				</td>
				<td valign="top" colspan="4">
					<html:select styleId="${param.pocBean}_Role_${param.pocIndex}"
						property="${param.pocBean}.domain.role"
						onchange="javascript:callPrompt('Contact Role', '${param.pocBean}_Role_${param.pocIndex}');">
						<option />
							<html:options name="contactRoles" />
						<option value="other">
							[Other]
						</option>
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					<strong>Address Line1</strong>
				</td>
				<td colspan="5">
					<html:text property="${param.pocBean}.organization.streetAddress1" size="50" />
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					<strong>Address Line2</strong>
				</td>
				<td colspan="5">
					<html:text property="${param.pocBean}.organization.streetAddress2" size="50" />
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					<strong>City</strong>
				</td>
				<td>
					<html:text property="${param.pocBean}.organization.city" size="20" />
					&nbsp;
				</td>
				<td class="cellLabel">
					<strong>State/Province</strong>
				</td>
				<td>
					<html:text property="${param.pocBean}.organization.state" size="15" />
					&nbsp;
				</td>
				<td class="cellLabel">
					<strong>Zip/Postal Code</strong>
				</td>
				<td>
					<html:text property="${param.pocBean}.organization.postalCode" size="10" />
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					<strong>Country</strong>
				</td>
				<td colspan="5">
					<html:text property="${param.pocBean}.organization.country"	size="30" />
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					<strong>Visibility</strong>
				</td>
				<td colspan="5">
					<html:select styleId="${param.pocBean}.visibilityGroup" property="${param.pocBean}.visibilityGroups"
						multiple="true" size="6">
						<html:options name="allVisibilityGroups" />
					</html:select>
					<br>
					<i>(${applicationOwner}_Researcher and
						${applicationOwner}_DataCurator are always selected by default.)</i>
				</td>
			</tr>
	</table>
</div>