<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html:form action="/functionalizingEntity">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${particleName} Sample Comosition - Functionalizing Entity
				</h4>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=composition_help')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center" id="entityTypeTitle"></h5>
				<br>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="" id="summary">
					<tr>
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify">
								Summary
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Functionalizing Entity Type</strong>
						</td>
						<td class="label">
							<html:select styleId="feType" property="entity.type"
								onchange="javascript:callPrompt('Functionalizing Entity Type', 'feType'); setEntityInclude('feType', '/particle/composition/functionalizingEntity'); getFETypeOptions('feType');">
								<option value=""></option>
								<html:options name="functionalizingEntityTypes" />
								<option value="other">
									[Other]
								</option>
							</html:select>
						</td>
						<td class="label" valign="top">
							<strong>Chemical Name</strong>
						</td>
						<td class="rightLabel">
							<html:text property="entity.name" />
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Molecular Formula Type</strong>
						</td>
						<td class="label">
							<html:select styleId="mfType"
								property="entity.molecularFormulaType"
								onchange="javascript:callPrompt('Molecular Formula Type', 'mfType'); ">
								<option value=""></option>
								<html:options name="molecularFormulaTypes" />
								<option value="other">
									[Other]
								</option>
							</html:select>
						</td>
						<td class="label" valign="top">
							<strong>Molecular Formula</strong>
						</td>
						<td class="rightLabel">
							<html:text property="entity.molecularFormula" />
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Value</strong>
						</td>
						<td class="label">
							<html:text property="entity.value" />
						</td>
						<td class="label" valign="top">
							<strong>Value Unit</strong>
						</td>
						<td class="rightLabel">
							<html:select styleId="feUnit" property="entity.valueUnit"
								onchange="javascript:callPrompt('Value Unit', 'feUnit');">
								<option value=""></option>
								<html:options name="functionalizingEntityUnits" />
								<option value="other">
									[Other]
								</option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<html:textarea property="entity.description" rows="2" cols="80" />
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Activation Method</strong>
						</td>
						<td class="label">
							<html:select styleId="feaMethod"
								property="entity.activationMethod.type"
								onchange="javascript:callPrompt('Activation Method', 'feaMethod');">
								<option value=""></option>
								<html:options name="activationMethods" />
								<option value="other">
									[Other]
								</option>
							</html:select>
						</td>
						<td class="label" valign="top">
							<strong>Activation Effect</strong>
						</td>
						<td class="rightLabel">
							<html:text property="entity.activationMethod.activationEffect" />
						</td>
					</tr>
				</table>
				<br>
				<div id="entityInclude">
					<c:if test="${!empty functionalizingEntityForm.map.entity.type}">
						<c:set var="entityType"
							value="${functionalizingEntityForm.map.entity.type}" scope="page" />
						<%
									String entityClass = gov.nih.nci.cananolab.ui.core.InitSetup
									.getInstance().getObjectName(
									(String) pageContext.getAttribute("entityType"),
									application);
							pageContext.setAttribute("entityClass", entityClass);
						%>
						<jsp:include
							page="/particle/composition/functionalizingEntity/body${entityClass}Info.jsp" />						
					</c:if>
				</div>				
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify" id="funcInfoTitle">
									Function Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<td valign="bottom">
											<a href="#"
												onclick="javascript:addComponent(functionalizingEntityForm, 'functionalizingEntity', 'addFunction'); return false;">
												<span class="addLink">Add Function</span> </a>
										</td>

										<td id="functionTd">
											<logic:iterate name="functionalizingEntityForm"
												property="entity.functions" id="function" indexId="ind">
												<c:choose>
													<c:when
														test="${functionalizingEntityForm.map.entity.functions[ind].type == 'imaging'}">
														<c:set var="modalityDisplay" value="display: block;" />
													</c:when>
													<c:otherwise>
														<c:set var="modalityDisplay" value="display: none;" />
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when
														test="${functionalizingEntityForm.map.entity.functions[ind].type == 'targeting'}">
														<c:set var="targetDisplay" value="display: block;" />
													</c:when>
													<c:otherwise>
														<c:set var="targetDisplay" value="display: none;" />
													</c:otherwise>
												</c:choose>
												<table class="topBorderOnly" cellspacing="0" cellpadding="3"
													width="100%" align="center" summary="" border="0">
													<tbody>
														<tr>
															<td class="formSubTitleNoRight" colspan="3">
																<span>Function #${ind + 1}</span>
															</td>
															<td class="formSubTitleNoLeft" align="right">
																<a href="#"
																	onclick="removeComponent(functionalizingEntityForm, 'functionalizingEntity', ${ind}, 'removeFunction');return false;">
																	<img src="images/delete.gif" border="0"
																		alt="remove this function"> </a>
															</td>

														</tr>
														<tr>
															<td class="leftLabelWithTop" valign="top">
																<strong>Function Type</strong>
															</td>
															<td class="labelWithTop" valign="top">
																<html:select styleId="funcType_${ind}"
																	property="entity.functions[${ind}].type"
																	onchange="javascript:callPrompt('Function Type', 'funcType_${ind}');
																			displayFEModality(${ind});
																			displayTarget(${ind}); ">
																	<option value="" />
																		<html:options name="functionTypes" />
																	<option value="other">
																		[Other]
																	</option>
																</html:select>
															</td>
															<td class="labelWithTop" valign="top">

																<strong style="${modalityDisplay}"
																	id="modalityStrong_${ind}">Modality Type</strong>&nbsp;
															</td>
															<td class="rightLabelWithTop" valign="top">
																<div id="modalityDiv_${ind}" style="${modalityDisplay}">
																	<html:select
																		property="entity.functions[${ind}].imagingFunction.modality"
																		size="1"
																		onchange="javascript:callPrompt('Modality Type', 'modalityType_${ind}');"
																		styleId="modalityType_${ind}">
																		<option value="" />
																			<html:options name="modalityTypes" />
																		<option value="other">
																			[Other]
																		</option>
																	</html:select>
																</div>
																&nbsp;
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Description</strong>
															</td>
															<td class="rightLabel" colspan="3">
																<html:textarea
																	property="entity.functions[${ind}].description"
																	rows="2" cols="65" />
															</td>
														</tr>
														<tr>
															<td valign="bottom" class="leftLabel">
																<span id="targetSpan_${ind }" style="${targetDisplay }">
																	<a href="#"
																	onclick="javascript:addChildComponent(functionalizingEntityForm, 'functionalizingEntity', ${ind}, 'addTarget'); return false;">
																		<span class="addLink2">Add Target</span>
																</a>
																</span>&nbsp;
															</td>
															<td colspan="4" class="rightLabel">
																&nbsp;
																<div id="targetDiv_${ind }" style="${targetDisplay }">
																	<jsp:include
																		page="/particle/composition/functionalizingEntity/bodyTargetInfo.jsp">
																		<jsp:param name="funcInd" value="${ind}" />
																	</jsp:include>
																</div>
															</td>
														</tr>
													</tbody>
												</table>
												<br>
											</logic:iterate>
										</td>
									</tr>
								</table>
							</td>
						</tr>
				</table>

				<%-- File Information --%>
				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify" id="peFileTitle">
									Functionalizing Entity File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<td valign="bottom">
											<a href="#"
												onclick="javascript:addComponent(functionalizingEntityForm, 'functionalizingEntity', 'addFile'); return false;">
												<span class="addLink">Add File</span> </a>
										</td>
										<td id="fileTd">

											<logic:iterate name="functionalizingEntityForm"
												property="entity.files" id="entityFile" indexId="fileInd">
												<jsp:include
													page="/particle/bodyLoadFileUpdate.jsp">
													<jsp:param name="fileInd" value="${fileInd}" />
													<jsp:param name="form" value="functionalizingEntityForm" />
													<jsp:param name="action" value="functionalizingEntity" />
													<jsp:param name="fileBean" value="entity.files[${fileInd}]" />
													<jsp:param name="domainFile" value="entity.files[${fileInd}].domainFile" />
													<jsp:param name="fileId" value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.id}" />
													<jsp:param name="fileUri" value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.uri}" />
													<jsp:param name="fileDisplayName" value="${functionalizingEntityForm.map.entity.files[fileInd].displayName}" />
													<jsp:param name="fileHidden" value="${functionalizingEntityForm.map.entity.files[fileInd].hidden}" />
													<jsp:param name="fileExternal" value="${functionalizingEntityForm.map.entity.files[fileInd].external}" />
													<jsp:param name="fileImage" value="${functionalizingEntityForm.map.entity.files[fileInd].image}" />
												</jsp:include>
												
												<br>
											</logic:iterate>
										</td>
									</tr>
								</table>
							</td>
						</tr>
				</table>
				<br>
				<jsp:include page="/particle/shared/bodyCharacterizationCopy.jsp" />

				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<table height="32" border="0" align="left" cellpadding="4"
								cellspacing="0">
								<tr>
									<td height="32">
										<div align="left">
											<input type="button" value="Delete"
												onclick="confirmDeletion();">
										</div>
									</td>
								</tr>
							</table>
							<table width="498" height="32" border="0" align="right"
								cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="32">
										<div align="right">
											<div align="right">
												<input type="reset" value="Reset" onclick="">
												<input type="hidden" name="dispatch" value="create">
												<input type="hidden" name="page" value="2">
												<input type="hidden" name="submitType"
													value="${param.submitType}" />
												<html:hidden property="entity.createdBy"
													value="${user.loginName}" />
												<html:submit />
											</div>
										</div>
									</td>
								</tr>
							</table>
							<div align="right"></div>
						</td>
					</tr>
				</table>

			</td>
		</tr>
	</table>
</html:form>
