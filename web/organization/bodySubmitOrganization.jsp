<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/OrganizationManager.js"></script>

<c:set var="action" value="Submit" scope="request" />
<c:if test="${param.dispatch eq 'setupUpdate'}">
	<c:set var="action" value="Update" scope="request" />
</c:if>
<html:form action="/submitOrganization">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					${action} Contact Organization
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_publication_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=organization" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0" id="pubTable">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Organization Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Name</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<html:text property="orga.domain.name" size="20" />
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Address Line1</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<html:text property="orga.domain.streetAddress1" size="50" />
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Address Line2</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<html:text property="orga.domain.streetAddress2" size="50" />
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>City</strong>
							</td>
							<td class="label">
								<html:text property="orga.domain.city" size="20" />
								&nbsp;
							</td>
							<td class="label">
								<strong>State/Province</strong>
							</td>
							<td class="rightLabel">
								<html:text property="orga.domain.state" size="20" />
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Zip/Postal Code</strong>
							</td>
							<td class="label">
								<html:text property="orga.domain.postalCode" size="10" />
								&nbsp;
							</td>
							<td class="label">
								<strong>Country</strong>
							</td>
							<td class="rightLabel">
								<html:text property="orga.domain.country" size="50" />
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
													property="orga.pocs" id="pointOfContact" indexId="ind">

													<table class="topBorderOnly" cellspacing="0"
														cellpadding="3" width="100%" align="center" summary=""
														border="0">
														<tbody>
															<tr>
																<td class="formSubTitleNoRight" colspan="5">
																	<span>Point of Contact #${ind + 1}</span>
																</td>
																<td class="formSubTitleNoLeft" align="right">
																	<a href="#"
																		onclick="removeComponent(submitOrganizationForm, 'submitOrganization', ${ind}, 'removePointOfContact');return false;">
																		<img src="images/delete.gif" border="0"
																			alt="remove this point of contact"> </a>
																</td>
															</tr>
															<tr>
																<td class="leftLabelWithTop" valign="top">
																	<strong>First Name</strong>
																</td>
																<td class="labelWithTop" valign="top">
																	<html:text property="orga.pocs[${ind}].firstName"
																		size="15" />
																</td>
																<td class="labelWithTop" valign="top">
																	<strong>Middle Initial</strong>
																</td>
																<td class="labelWithTop" valign="top">
																	<html:text property="orga.pocs[${ind}].middleInitial"
																		size="5" />
																</td>
																<td class="labelWithTop" valign="top">
																	<strong>Last Name</strong>
																</td>
																<td class="rightLabelWithTop" valign="top">
																	<html:text property="orga.pocs[${ind}].lastName"
																		size="15" />
																</td>
															</tr>
															<tr>
																<td class="leftLabel" valign="top">
																	<strong>Role*</strong>
																</td>
																<td class="rightLabel" valign="top" colspan="5">
																	<html:select styleId="role${ind}"
																		property="orga.pocs[${ind}].role"
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
																<td class="label" valign="top">
																	<html:text property="orga.pocs[${ind}].email" size="30" />
																</td>
																<td class="label" valign="top">
																	<strong>Email Visibility</strong>
																</td>
																<td class="rightLabel" valign="top" colspan="3">
																	<html:select property="orga.visibilityGroups"
																		multiple="true" size="6">
																		<html:options name="allVisibilityGroups" />
																	</html:select>
																</td>
															</tr>
															<tr>
																<td class="leftLabel" valign="top">
																	<strong>Phone Number</strong>
																</td>
																<td class="label" valign="top">
																	<html:text property="orga.pocs[${ind}].phone" size="30" />
																</td>
																<td class="label" valign="top">
																	<strong>Phone Number Visibility</strong>
																</td>
																<td class="rightLabel" valign="top" colspan="3">
																	<html:select property="orga.visibilityGroups"
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
								<html:select property="file.visibilityGroups" multiple="true"
									size="6">
									<html:options name="allVisibilityGroups" />
								</html:select>
								<br>
								<i>(${applicationOwner}_Researcher and
									${applicationOwner}_DataCurator are always selected by
									default.)</i>
							</td>
						</tr>
					</tbody>
				</table>

				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<table width="498" height="32" border="0" align="right"
								cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="32">
										<div align="right">
											<div align="right">
												<c:set var="dataId"
													value="${submitOrganizationForm.map.orga.domain.id}" />
												<c:set var="origUrl"
													value="submitOrganization.do?page=0&particleId=${docParticleId }&dispatch=setup&location=local" />
												<c:if test="${!empty dataId}">
													<c:set var="origUrl"
														value="submitOrganization.do?page=0&particleId=${docParticleId }&dispatch=setupUpdate&location=local&fileId=${dataId }" />
												</c:if>
												<input type="reset" value="Reset"
													onclick="javascript:window.location.href='${origUrl}'">
												<input type="hidden" name="dispatch" value="create">
												<input type="hidden" name="submitType" value="organizations">
												<input type="hidden" name="page" value="2">
												<input type="hidden" name="location" value="local">
												<input type="hidden" name="particleId"
													value="${docParticleId}">
												<html:submit />
											</div>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
