<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/POCManager.js'></script>
<script type='text/javascript'
	'src='/caNanoLab/dwr/interface/POCManager.js'></script>
<script type="text/javascript" src="javascript/script.js"></script>
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/SampleManager.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th colspan="6">
			Point of Contact Information
		</th>
	<tr>
		<td class="cellLabel" valign="top">
			<strong>First Name</strong>
		</td>
		<td valign="top">
			<html:text property="sampleBean.thePOC.domain.firstName" size="15" styleId="domain.firstName"/>
		</td>
		<td class="cellLabel" valign="top">
			<strong>Middle Initial</strong>
		</td>
		<td valign="top">
			<html:text property="sampleBean.thePOC.domain.middleInitial" size="5" styleId="domain.middleInitial" />
		</td>
		<td class="cellLabel" valign="top">
			<strong>Last Name</strong>
		</td>
		<td valign="top">
			<html:text property="sampleBean.thePOC.domain.lastName" size="15" styleId="domain.lastName" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel" valign="top">
			<strong>Phone Number</strong>
		</td>
		<td valign="top">
			<html:text property="sampleBean.thePOC.domain.phone" size="30" styleId="domain.phone"/>
		</td>
		<td class="cellLabel" valign="top">
			<strong>Email</strong>
		</td>
		<td valign="top" colspan="4">
			<html:text property="sampleBean.thePOC.domain.email" size="30"
				 styleId="domain.email"/>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<strong>Organization Name*</strong>
		</td>
		<td>
			<div id="orgNamePrompt">
				<html:select property="sampleBean.thePOC.domain.organization.name"
					styleId="domain.organization.name"
					onchange="javascript:removeOrgVisibilityByName('domain.organization.name', 'sampleBean.thePOC.visibilityGroup');setOrganization(submitPointOfContactForm, 'domain.organization.name');callPrompt('Organization Name', 'domain.organization.name', 'orgNamePrompt');">
					<option value="" />
						<html:options name="allOrganizationNames" />
					<option value="other">
						[Other]
					</option>
				</html:select>
			</div>
		</td>
		<td class="cellLabel" valign="top">
			<strong>Role</strong>
		</td>
		<td valign="top" colspan="4">
			<div id="rolePrompt">
				<html:select styleId="domain.role" property="sampleBean.thePOC.domain.role"
					onchange="javascript:callPrompt('Contact Role', 'domain.role', 'rolePrompt');">
					<option />
						<html:options name="contactRoles" />
					<option value="other">
						[Other]
					</option>
				</html:select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<strong>Address Line1</strong>
		</td>
		<td colspan="5">
			<html:text
				property="sampleBean.thePOC.domain.organization.streetAddress1"
				size="50" styleId="domain.organization.streetAddress1"/>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<strong>Address Line2</strong>
		</td>
		<td colspan="5">
			<html:text
				property="sampleBean.thePOC.domain.organization.streetAddress2"
				size="50" styleId="domain.organization.streetAddress2"/>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<strong>City</strong>
		</td>
		<td>
			<html:text property="sampleBean.thePOC.domain.organization.city"
				size="20" styleId="domain.organization.city"/>
			&nbsp;
		</td>
		<td class="cellLabel">
			<strong>State/Province</strong>
		</td>
		<td>
			<html:text property="sampleBean.thePOC.domain.organization.state"
				size="15" styleId="domain.organization.state"/>
			&nbsp;
		</td>
		<td class="cellLabel">
			<strong>Zip/Postal Code</strong>
		</td>
		<td>
			<html:text
				property="sampleBean.thePOC.domain.organization.postalCode"
				size="10" styleId="domain.organization.postalCode"/>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<strong>Country</strong>
		</td>
		<td colspan="5">
			<html:text property="sampleBean.thePOC.domain.organization.country"
				size="30" styleId="domain.organization.country"/>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<strong>Visibility</strong>
		</td>
		<td colspan="5">
			<html:select styleId="visibilityGroups"
				property="sampleBean.thePOC.visibilityGroups" multiple="true"
				size="6">
				<html:options name="allVisibilityGroups" />
			</html:select>
			<br>
			<i>(${applicationOwner}_Researcher and
				${applicationOwner}_DataCurator are always selected by default.)</i>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<strong>Is Primary Point of Contact ?</strong>
		</td>
		<td colspan="5">
			<html:checkbox styleId="primaryStatus"
				property="sampleBean.thePOC.primaryStatus">
			</html:checkbox>
		</td>
	</tr>
	<tr>
		<td>
			<input type="button" value="Remove"
				onclick="removePointOfContact('sample');clearPointOfContact()"
				id="deletePointOfContact" style="display: none;" />
		</td>
		<td colspan="5">
			<div align="right">
				<input type="button" value="Add"
					onclick="addPointOfContact('sample')" />
				<input type="button" value="Cancel"
					onclick="clearPointOfContact();hide('newPointOfContact');" />
			</div>
		</td>
	</tr>
</table>