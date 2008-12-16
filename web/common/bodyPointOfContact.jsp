<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/POCManager.js"></script>
<script type="text/javascript" src="javascript/script.js"></script>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="formSubTitleWithBottomNoRight" colspan="5">
				<span>${param.pocTitle}</span>				
			</td>
			<td class="formSubTitleWithBottomNoLeft" align="right">
				&nbsp;
				<a href="#" onclick="showhide('${param.pocTitle}');">show/hide</a>&nbsp;
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

<div id="${param.pocTitle}" style="display: block;">
<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>		
		<tr>
			<c:choose>
				<c:when test="${param.pocTitle ne 'Primary Point of Contact' }">
					<td class="leftLabel" valign="top">
						<strong>Point of Contact Name</strong>
					</td>
					<td class="rightLabel" valign="top" colspan="5">
						<html:select styleId="pocId_${param.pocIndex}"
							property="${param.pocBean}.pocId"
							onchange="javascript:setSecondaryPOC(submitPointOfContactForm, 'pocId_${param.pocIndex }', '${param.pocIndex }' );">
							<option />
								<html:options collection="allPointOfContacts"
									labelProperty="displayName" property="domain.id" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</td>
				</c:when>
			</c:choose>
		</tr>
		<tr>
			<td class="leftLabel" valign="top">
				<strong>First Name</strong>
			</td>
			<td class="label" valign="top">
				<html:text property="${param.pocBean}.domain.firstName" size="15" />
			</td>
			<td class="label" valign="top">
				<strong>Middle Initial</strong>
			</td>
			<td class="label" valign="top">
				<html:text property="${param.pocBean}.domain.middleInitial" size="5" />
			</td>
			<td class="label" valign="top">
				<strong>Last Name</strong>
			</td>
			<td class="rightLabel" valign="top">
				<html:text property="${param.pocBean}.domain.lastName" size="15" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel" valign="top">
				<strong>Role*</strong>
			</td>
			<td class="rightLabel" valign="top" colspan="5">
				<html:select styleId="pocRole_${param.pocIndex }"
					property="${param.pocBean}.domain.role"
					onchange="javascript:callPrompt('Contact Role', 'pocRole_${param.pocBean}');">
					<option />
						<html:options name="contactRoles" />                                                                 
					<option value="other">
						[Other]
					</option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="leftLabel" valign="top">
				<strong>Email</strong>
			</td>
			<td class="label" valign="top" colspan="2">
				<html:text property="${param.pocBean}.domain.email" size="30" styleId="emailText" />
			</td>
			<td class="label" valign="top">
				<strong>Email Visibility</strong>
			</td>
			<td class="rightLabel" valign="top" colspan="2">
				<html:select property="${param.pocBean}.emailVisibilityGroups"
					multiple="true" size="6">
					<html:options name="allVisibilityGroups" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="leftLabel" valign="top">
				<strong>Phone Number</strong>
			</td>
			<td class="label" valign="top" colspan="2">
				<html:text property="${param.pocBean}.domain.phone" size="30" />
			</td>
			<td class="label" valign="top">
				<strong>Phone Visibility</strong>
			</td>
			<td class="rightLabel" valign="top" colspan="2">
				<html:select property="${param.pocBean}.phoneVisibilityGroups"
					multiple="true" size="6">
					<html:options name="allVisibilityGroups" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Point of Contact Visibility</strong>
			</td>
			<td class="rightLabel" colspan="5">
				<html:select property="${param.pocBean}.pocVisibilityGroups"
					multiple="true" size="6">
					<html:options name="allVisibilityGroups" />
				</html:select>
				<br>
				<i>(${applicationOwner}_Researcher and
					${applicationOwner}_DataCurator are always selected by default.)</i>
			</td>
		</tr>

		<tr>
			<td class="leftLabel">
				<strong>Organization Name</strong>
			</td>
			<td class="rightLabel" colspan="5">
				<html:select property="${param.pocBean}.organization.name"
					styleId="orgName_${param.pocIndex}"
					onchange="javascript:callPrompt('Organization Name', 'orgName_${param.pocIndex }');">
					<option value="" />
					<html:options name="allOrganizationNames" />
					<option value="other">
						[Other]
					</option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Address Line1</strong>
			</td>
			<td class="rightLabel" colspan="5">
				<html:text property="${param.pocBean}.organization.streetAddress1"
					size="50" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Address Line2</strong>
			</td>
			<td class="rightLabel" colspan="5">
				<html:text property="${param.pocBean}.organization.streetAddress2"
					size="50" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>City</strong>
			</td>
			<td class="label" colspan="2">
				<html:text property="${param.pocBean}.organization.city" size="20" />
				&nbsp;
			</td>
			<td class="label">
				<strong>State/Province</strong>
			</td>
			<td class="rightLabel" colspan="2">
				<html:text property="${param.pocBean}.organization.state" size="20" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Zip/Postal Code</strong>
			</td>
			<td class="label" colspan="2">
				<html:text property="${param.pocBean}.organization.postalCode"
					size="10" />
				&nbsp;
			</td>
			<td class="label">
				<strong>Country</strong>
			</td>
			<td class="rightLabel" colspan="2">
				<html:text property="${param.pocBean}.organization.country"
					size="30" />
				&nbsp;
			</td>
		</tr>

		<tr>
			<td class="leftLabel">
				<strong>Organization Visibility</strong>
			</td>
			<td class="rightLabel" colspan="5">
				<html:select property="${param.pocBean}.orgVisibilityGroups"
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