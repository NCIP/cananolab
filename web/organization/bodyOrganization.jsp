<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/OrganizationManager.js"></script>


<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="formSubTitleNoRight" colspan="3">
				<span>${param.orgTitle}</span>
			</td>
			<td class="formSubTitleNoLeft" align="right">
				<c:if test="${param.orgTitle ne 'Primary Organization' }">
					<a href="#"
					onclick="removeComponent(submitOrganizationForm, 'submitOrganization', ${param.orgIndex}, 'removeOrganization');return false;">
					<img src="images/delete.gif" border="0"
						alt="remove this point of contact"> </a>
				</c:if>&nbsp;
			</td>
		</tr>

		<tr>
			<td class="leftLabel">
				<strong>Name</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:text property="${param.orgBean}.domain.name" size="50" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Address Line1</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:text property="${param.orgBean}.domain.streetAddress1" size="50" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Address Line2</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:text property="${param.orgBean}.domain.streetAddress2" size="50" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>City</strong>
			</td>
			<td class="label">
				<html:text property="${param.orgBean}.domain.city" size="20" />
				&nbsp;
			</td>
			<td class="label">
				<strong>State/Province</strong>
			</td>
			<td class="rightLabel">
				<html:text property="${param.orgBean}.domain.state" size="20" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Zip/Postal Code</strong>
			</td>
			<td class="label">
				<html:text property="${param.orgBean}.domain.postalCode" size="10" />
				&nbsp;
			</td>
			<td class="label">
				<strong>Country</strong>
			</td>
			<td class="rightLabel">
				<html:text property="${param.orgBean}.domain.country" size="50" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<table border="0" width="100%">
					<tbody>
						<tr>
							<td valign="bottom">
								<a href="#"
									onclick="javascript:addComponent(submitOrganizationForm, 'submitOrganization', 'addPointOfContact'); return false;">
									<span class="addLink">Add Point of Contact</span> </a>
							</td>
							<td id="compEleTd">
								<logic:iterate name="submitOrganizationForm"
									property="${param.orgBean}.pocs" id="pointOfContact" indexId="ind">

									<table class="topBorderOnly" cellspacing="0" cellpadding="3"
										width="100%" align="center" summary="" border="0">
										<tbody>
											<tr>
												<td class="formSubTitleNoRight" colspan="5">
													<span>Point of Contact #${ind + 1}</span>
												</td>
												<td class="formSubTitleNoLeft" align="right">
													&nbsp;<c:if test="${ind != 0 }">
													<a href="#"
														onclick="removeComponent(submitOrganizationForm, 'submitOrganization', ${ind}, 'removePointOfContact');return false;">
														<img src="images/delete.gif" border="0"
															alt="remove this point of contact"> </a>
													</c:if>
												</td>
											</tr>
											<tr>
												<td class="leftLabelWithTop" valign="top">
													<strong>First Name</strong>
												</td>
												<td class="labelWithTop" valign="top">
													<html:text property="${param.orgBean}.pocs[${ind}].firstName" size="15" />
												</td>
												<td class="labelWithTop" valign="top">
													<strong>Middle Initial</strong>
												</td>
												<td class="labelWithTop" valign="top">
													<html:text property="${param.orgBean}.pocs[${ind}].middleInitial"
														size="5" />
												</td>
												<td class="labelWithTop" valign="top">
													<strong>Last Name</strong>
												</td>
												<td class="rightLabelWithTop" valign="top">
													<html:text property="${param.orgBean}.pocs[${ind}].lastName" size="15" />
												</td>
											</tr>
											<tr>
												<td class="leftLabel" valign="top">
													<strong>Role*</strong>
												</td>
												<td class="rightLabel" valign="top" colspan="5">
													<html:select styleId="role${ind}"
														property="${param.orgBean}.pocs[${ind}].role"
														onchange="javascript:callPrompt('Contact Role', 'role${ind}');">
														<option />
															<%--																			<html:options name="contactRoles" />--%>
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
													<html:text property="${param.orgBean}.pocs[${ind}].email" size="30" />
												</td>
												<td class="label" valign="top">
													<strong>Email Visibility</strong>
												</td>
												<td class="rightLabel" valign="top" colspan="2">
													<html:select property="${param.orgBean}.visibilityGroups"
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
													<html:text property="${param.orgBean}.pocs[${ind}].phone" size="30" />
												</td>
												<td class="label" valign="top">
													<strong>Phone Number Visibility</strong>
												</td>
												<td class="rightLabel" valign="top" colspan="2">
													<html:select property="${param.orgBean}.visibilityGroups"
														multiple="true" size="6">
														<html:options name="allVisibilityGroups" />
													</html:select>
												</td>
											</tr>
										</tbody>
									</table>
									<br>
								</logic:iterate>
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Organization Visibility</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:select property="${param.orgBean}.visibilityGroups" multiple="true"
					size="6">
					<html:options name="allVisibilityGroups" />
				</html:select>
				<br>
				<i>(${applicationOwner}_Researcher and
					${applicationOwner}_DataCurator are always selected by default.)</i>
			</td>
		</tr>
</table>