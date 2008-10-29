<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
<!--//
function confirmDeletion()
{
	answer = confirm("Are you sure you want to delete the functionalizing entity?")
	if (answer !=0)
	{
		this.document.forms[0].dispatch.value="delete";
		this.document.forms[0].page.value="1";
		this.document.forms[0].submit(); 
		return true;
	}
}
//-->
</script>
<html:form action="/functionalizingEntity" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${particleName} Sample Composition - Functionalizing Entity
				</h4>
			</td>
			<td align="right" width="1/helpGlossary.jspge="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="function_entity_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center" id="entityTypeTitle">
					Functionalizing Entity
				</h5>
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
							<strong>Functionalizing Entity Type*</strong>
						</td>
						<td class="label">
							<c:choose>
								<c:when
									test="${param.dispatch eq 'setup'||empty functionalizingEntityForm.map.entity.type}">
									<html:select styleId="feType" property="entity.type"
										onchange="javascript:callPrompt('Functionalizing Entity Type', 'feType'); setEntityInclude('feType', '/particle/composition/functionalizingEntity'); getFETypeOptions();">
										<option value=""></option>
										<html:options name="functionalizingEntityTypes" />
										<option value="other">
											[Other]
										</option>
									</html:select>
								</c:when>
								<c:otherwise>
								${functionalizingEntityForm.map.entity.type}
								</c:otherwise>
							</c:choose>
							&nbsp;
						</td>
						<td class="label" valign="top">
							<strong>Chemical Name*</strong>
						</td>
						<td class="rightLabel">
							<html:text property="entity.name" size="70"/>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Molecular Formula Type</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<html:select styleId="mfType"
								property="entity.molecularFormulaType"
								onchange="javascript:callPrompt('Molecular Formula Type', 'mfType'); ">
								<option value=""></option>
								<html:options name="feMolecularFormulaTypes" />
								<option value="other">
									[Other]
								</option>
							</html:select>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Molecular Formula</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<html:textarea property="entity.molecularFormula" rows="2" cols="80"/>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Amount</strong>
						</td>
						<td class="label">
							<html:text property="entity.value"
								onkeydown="return filterFloatNumber(event)" />
						</td>
						<td class="label" valign="top">
							<strong>Amount Unit</strong>
						</td>
						<td class="rightLabel">
							<html:select styleId="feUnit" property="entity.valueUnit"
								onchange="javascript:callPrompt('Amount Unit', 'feUnit');">
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
							<html:text property="entity.activationMethod.activationEffect" size="70"/>
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
																<strong>Function Type*</strong>
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

																<strong style="${modalityDisplay}" id="modalityStrong_${ind}">Modality
																	Type</strong>&nbsp;
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
																<span id="targetSpan_${ind }" style="${targetDisplay}"> <a
																	href="#"
																	onclick="javascript:addChildComponent(functionalizingEntityForm, 'functionalizingEntity', ${ind}, 'addTarget'); return false;">
																		<span class="addLink2">Add Target</span> </a> </span>&nbsp;
															</td>
															<td colspan="4" class="rightLabel">
																&nbsp;
																<div id="targetDiv_${ind }" style="${targetDisplay}">
																	<jsp:include
																		page="/particle/composition/functionalizingEntity/bodyTargetInfoUpdate.jsp">
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
										<td valign="bottom" width="10%">
											<a href="#"
												onclick="javascript:addComponent(functionalizingEntityForm, 'functionalizingEntity', 'addFile'); return false;">
												<span class="addLink">Add File</span> </a>
										</td>
										<td id="fileTd">

											<logic:iterate name="functionalizingEntityForm"
												property="entity.files" id="entityFile" indexId="fileInd">
												<jsp:include page="/particle/bodyLoadFileUpdate.jsp">
													<jsp:param name="fileInd" value="${fileInd}" />
													<jsp:param name="form" value="functionalizingEntityForm" />
													<jsp:param name="action" value="functionalizingEntity" />
													<jsp:param name="removeCmd" value="\'removeFile\'" />
													<jsp:param name="fileBean" value="entity.files[${fileInd}]" />
													<jsp:param name="fileId"
														value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.id}" />
													<jsp:param name="fileUri"
														value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.uri}" />
													<jsp:param name="fileTitle"
														value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.title}" />
													<jsp:param name="fileHidden"
														value="${functionalizingEntityForm.map.entity.files[fileInd].hidden}" />
													<jsp:param name="fileImage"
														value="${functionalizingEntityForm.map.entity.files[fileInd].image}" />
													<jsp:param name="fileUriExternal"
														value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.uriExternal}" />
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
				<jsp:include page="/particle/bodyAnnotationCopy.jsp" />

				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<c:choose>
								<c:when
									test="${param.dispatch eq 'setupUpdate'&& canUserDelete eq 'true'}">
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
								</c:when>
							</c:choose>
							<table width="498" height="32" border="0" align="right"
								cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="32">
										<div align="right">
											<div align="right">
												<c:choose>
													<c:when test="${'setup' eq param.dispatch }">
														<c:remove var="dataId" scope="session" />
													</c:when>										
													<c:when test="${'setupUpdate' eq param.dispatch }">
														<c:set var="dataId" value="${param.dataId}" scope="session" />
													</c:when>																			
												</c:choose>
												<c:set var="origUrl" value="${actionName}.do?particleId=${particleId}&submitType=${submitType}&page=0&dispatch=setup&location=${location}" />
												<c:if test="${!empty dataId}">
													<c:set var="origUrl" value="${actionName}.do?particleId=${particleId}&submitType=${submitType}&page=0&dispatch=setupUpdate&location=${location}&dataId=${dataId}" />
												</c:if>	
												<input type="reset" value="Reset"
													onclick="javascript:window.location.href='${origUrl}'">
												<input type="hidden" name="dispatch" value="create">
												<input type="hidden" name="page" value="2">
												<input type="hidden" name="submitType"
													value="${param.submitType}" />
												<html:submit />												
												<c:choose>
													<c:when test="${!empty param.particleId }">
														<html:hidden property="particleId"
															value="${param.particleId }" />
													</c:when>
													<c:otherwise>
														<html:hidden property="particleId" />
													</c:otherwise>
												</c:choose>
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
