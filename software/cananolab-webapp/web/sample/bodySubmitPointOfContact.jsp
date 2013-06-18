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
<script type='text/javascript' src='javascript/POCManager.js'></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/POCManager.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<table class="subSubmissionView" width="85%" align="center" summary="layout">
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
						<label for="domain.organization.name">Organization Name*</label>
					</td>
					<td>
						<div id="orgNamePrompt">
							<html:select
								property="sampleBean.thePOC.domain.organization.name"
								styleId="domain.organization.name"
								onchange="javascript:callPrompt('Organization Name', 'domain.organization.name', 'orgNamePrompt');updateOrganizationInfo();">
								<option value="" />
									<html:options name="allOrganizationNames" />
								<option value="other">
									[other]
								</option>
							</html:select>
						</div>
					</td>
					<td class="cellLabel" valign="top">
						<label for="domain.role">Role</label>
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
						<label for="domain.organization.streetAddress1">Address Line1</label>
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
						<label for="domain.organization.streetAddress2">Address Line2</label>
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
						<label for="domain.organization.city">City</label>
					</td>
					<td>
						<html:text property="sampleBean.thePOC.domain.organization.city"
							size="20" styleId="domain.organization.city" />
						&nbsp;
					</td>
					<td class="cellLabel">
						<label for="domain.organization.state">State/Province</label>
					</td>
					<td>
						<html:text property="sampleBean.thePOC.domain.organization.state"
							size="5" styleId="domain.organization.state" />
						&nbsp;
					</td>
					<td class="cellLabel">
						<label for="domain.organization.postalCode">Zip/Postal Code</label>
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
						<label for="domain.organization.country">Country</label>
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
						<label for="domain.firstName">First Name</label>
					</td>
					<td valign="top">
						<html:text property="sampleBean.thePOC.domain.firstName" size="15"
							styleId="domain.firstName" />
					</td>
					<td class="cellLabel" valign="top">
						<label for="domain.middleInitial">Middle Initial</label>
					</td>
					<td valign="top">
						<html:text property="sampleBean.thePOC.domain.middleInitial"
							size="5" styleId="domain.middleInitial" />
					</td>
					<td class="cellLabel" valign="top">
						<label for="domain.lastName">Last Name</label>
					</td>
					<td valign="top">
						<html:text property="sampleBean.thePOC.domain.lastName" size="15"
							styleId="domain.lastName" onchange="updatePersonInfo()" />
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
						<label for="domain.phone">Phone Number</label>
					</td>
					<td valign="top">
						<html:text property="sampleBean.thePOC.domain.phone" size="30"
							styleId="domain.phone" />
					</td>
					<td class="cellLabel" valign="top">
						<label for="domain.email">Email</label>
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
			<table width="100%">
				<tr>
					<td>
						<c:if test="${sampleForm.map.sampleBean.userDeletable eq 'true'}">
							<input type="button" value="Remove"
								onclick="removePointOfContact('sample')"
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