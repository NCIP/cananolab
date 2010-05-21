<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/POCManager.js'></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/POCManager.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th>
			<span id="primaryTitle" style="display: none">Primary</span><span
				id="secondaryTitle" style="display: none">Secondary</span> Point of
			Contact Information
		</th>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td class="cellLabel">
						Organization Name*
					</td>
					<td>
						<div id="orgNamePrompt">
							<html:select
								property="sampleBean.thePOC.domain.organization.name"
								styleId="domain.organization.name"
								onchange="javascript:callPrompt('Organization Name', 'domain.organization.name', 'orgNamePrompt');updateOrganizationInfo();removeOrgFromVisibilityGroups('domain.organization.name'); removeOrgFromSampleVisibilityGroups('domain.organization.name');">
								<option value="" />
									<html:options name="allOrganizationNames" />
								<option value="other">
									[other]
								</option>
							</html:select>
						</div>
					</td>
					<td class="cellLabel" valign="top">
						Role
					</td>
					<td valign="top" colspan="4">
						<div id="rolePrompt">
							<html:select styleId="domain.role"
								property="sampleBean.thePOC.domain.role"
								onchange="javascript:callPrompt('Contact Role', 'domain.role', 'rolePrompt');">
								<option />
									<html:options name="contactRoles" />
								<option value="other">
									[other]
								</option>
							</html:select>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td class="cellLabel">
						Address Line1
					</td>
					<td colspan="5">
						<html:text
							property="sampleBean.thePOC.domain.organization.streetAddress1"
							size="50" styleId="domain.organization.streetAddress1" />
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="cellLabel">
						Address Line2
					</td>
					<td colspan="5">
						<html:text
							property="sampleBean.thePOC.domain.organization.streetAddress2"
							size="50" styleId="domain.organization.streetAddress2" />
						&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td class="cellLabel">
						City
					</td>
					<td>
						<html:text property="sampleBean.thePOC.domain.organization.city"
							size="20" styleId="domain.organization.city" />
						&nbsp;
					</td>
					<td class="cellLabel">
						State/Province
					</td>
					<td>
						<html:text property="sampleBean.thePOC.domain.organization.state"
							size="5" styleId="domain.organization.state" />
						&nbsp;
					</td>
					<td class="cellLabel">
						Zip/Postal Code
					</td>
					<td>
						<html:text
							property="sampleBean.thePOC.domain.organization.postalCode"
							size="10" styleId="domain.organization.postalCode" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td class="cellLabel">
						Country
					</td>
					<td>
						<html:text
							property="sampleBean.thePOC.domain.organization.country"
							size="30" styleId="domain.organization.country" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td class="cellLabel" valign="top">
						First Name
					</td>
					<td valign="top">
						<html:text property="sampleBean.thePOC.domain.firstName" size="15"
							styleId="domain.firstName" />
					</td>
					<td class="cellLabel" valign="top">
						Middle Initial
					</td>
					<td valign="top">
						<html:text property="sampleBean.thePOC.domain.middleInitial"
							size="5" styleId="domain.middleInitial" />
					</td>
					<td class="cellLabel" valign="top">
						Last Name
					</td>
					<td valign="top">
						<html:text property="sampleBean.thePOC.domain.lastName" size="15"
							styleId="domain.lastName" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td class="cellLabel" valign="top">
						Phone Number
					</td>
					<td valign="top">
						<html:text property="sampleBean.thePOC.domain.phone" size="30"
							styleId="domain.phone" />
					</td>
					<td class="cellLabel" valign="top">
						Email
					</td>
					<td valign="top" colspan="4">
						<html:text property="sampleBean.thePOC.domain.email" size="30"
							styleId="domain.email" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td class="cellLabel">
						Visibility
					</td>
					<td colspan="5">
						<html:select styleId="visibilityGroups"
							property="sampleBean.thePOC.visibilityGroups" multiple="true"
							size="6">
							<html:options name="allVisibilityGroups" />
						</html:select>
						<br>
						<i>(${applicationOwner}_Researcher,
							${applicationOwner}_DataCurator, and the organization name are
							always selected by default.)</i>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%">
				<tr>
					<td>
						<c:if test="${!empty user && user.curator && user.admin}">
							<input type="button" value="Remove"
								onclick="removePointOfContact('sample');clearPointOfContact();"
								id="deletePointOfContact" style="display: none;" />
						</c:if>
					</td>
					<td>
						<div align="right">
							<input type="button" value="Save"
								onclick="addPointOfContact('sample')" />
							<input type="button" value="Cancel"
								onclick="clearPointOfContact();closeSubmissionForm('PointOfContact');" />
							<html:hidden styleId="domain.id"
								property="sampleBean.thePOC.domain.id" />
							<html:hidden styleId="primaryStatus"
								property="sampleBean.thePOC.primaryStatus" />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>