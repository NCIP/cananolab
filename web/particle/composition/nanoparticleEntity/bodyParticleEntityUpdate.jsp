<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
<!--//
function confirmDeletion()
{
	answer = confirm("Are you sure you want to delete the nanoparticle entity?")
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
<html:form action="/nanoparticleEntity" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${particleName} Sample Composition - Nanoparticle Entity
				</h4>
			</td>
			<td align="right" width="20/helpGlossary.jspe="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="nano_entity_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center" id="entityTypeTitle">
					Nanoparticle Entity
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
							<strong>Nanoparticle Entity Type*</strong>
						</td>
						<td class="rightLabel">
							<c:choose>
								<c:when
									test="${param.dispatch eq 'setup' || empty nanoparticleEntityForm.map.entity.type}">
									<html:select styleId="peType" property="entity.type"
										onchange="javascript:callPrompt('Particle Entity Type', 'peType'); 
										setEntityInclude('peType', '/particle/composition/nanoparticleEntity'); getNETypeOptions();">
										<option value=""></option>
										<html:options name="nanoparticleEntityTypes" />
										<option value="other">
											[Other]
										</option>
									</html:select>
								</c:when>
								<c:otherwise>
									${nanoparticleEntityForm.map.entity.type}&nbsp;
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<html:textarea property="entity.description" rows="3" cols="80" />
						</td>
					</tr>
				</table>
				<br>
				<c:choose>
					<c:when test="${!empty nanoparticleEntityForm.map.entity.type}">
						<c:set var="entityType"
							value="${nanoparticleEntityForm.map.entity.type}" scope="page" />
						<%
									String entityClass = gov.nih.nci.cananolab.ui.core.InitSetup
									.getInstance().getObjectName(
									(String) pageContext.getAttribute("entityType"),
									application);
							pageContext.setAttribute("entityClass", entityClass);
						%>
						<jsp:include
							page="/particle/composition/nanoparticleEntity/body${entityClass}Info.jsp" />
					</c:when>
				</c:choose>
				<div id="entityInclude"></div>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify" id="compEleInfoTitle">
									Composing Element Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<td valign="bottom">
											<a href="#"
												onclick="javascript:addComponent(nanoparticleEntityForm, 'nanoparticleEntity', 'addComposingElement'); return false;">
												<span class="addLink">Add Composing Element</span> </a>
										</td>
										<td id="compEleTd">
											<logic:iterate name="nanoparticleEntityForm"
												property="entity.composingElements" id="composingElement"
												indexId="ind">

												<table class="topBorderOnly" cellspacing="0" cellpadding="3"
													width="100%" align="center" summary="" border="0">
													<tbody>
														<tr>
															<td class="formSubTitleNoRight" colspan="3">
																<span>Composing Element #${ind + 1}</span>
															</td>
															<td class="formSubTitleNoLeft" align="right">
																<a href="#"
																	onclick="removeComponent(nanoparticleEntityForm, 'nanoparticleEntity', ${ind}, 'removeComposingElement');return false;">
																	<img src="images/delete.gif" border="0"
																		alt="remove this composing element"> </a>
															</td>
														</tr>
														<tr>
															<td class="leftLabelWithTop" valign="top">
																<strong>Composing Element Type*</strong>
															</td>
															<td class="labelWithTop" valign="top">
																<html:select styleId="compElemType${ind}"
																	property="entity.composingElements[${ind}].domainComposingElement.type"
																	onchange="javascript:callPrompt('Composing Element Type', 'compElemType${ind}');">
																	<option />
																		<c:choose>
																			<c:when test="${entityType ne 'emulsion'}">
																				<html:options name="composingElementTypes" />
																			</c:when>
																			<c:otherwise>
																				<html:options name="emulsionComposingElementTypes" />
																			</c:otherwise>
																		</c:choose>
																	<option value="other">
																		[Other]
																	</option>
																</html:select>
															</td>

															<td class="labelWithTop" valign="top">
																<strong>Chemical Name*</strong>
															</td>
															<td class="rightLabelWithTop" valign="top">
																<html:text
																	property="entity.composingElements[${ind}].domainComposingElement.name"
																	size="30" />
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Molecular Formula Type</strong>
															</td>
															<td class="rightLabel" valign="top" colspan="3">
																<html:select styleId="molFormulaType${ind}"
																	property="entity.composingElements[${ind}].domainComposingElement.molecularFormulaType"
																	onchange="javascript:callPrompt('Molecular Formula Type', 'molFormulaType${ind}');">
																	<option value="" />
																		<html:options name="ceMolecularFormulaTypes" />
																	<option value="other">
																		[Other]
																	</option>
																</html:select>
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Molecular Formula</strong>
															</td>
															<td class="rightLabel" valign="top" colspan="3">
																<html:textarea
																	property="entity.composingElements[${ind}].domainComposingElement.molecularFormula"
																	rows="2" cols="65"/>
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Amount</strong>
															</td>
															<td class="label" valign="top">
																<html:text
																	property="entity.composingElements[${ind}].domainComposingElement.value"
																	onkeydown="return filterFloatNumber(event)" size="30" />
															</td>
															<td class="label" valign="top">
																<strong>Amount Unit</strong>
															</td>
															<td class="rightLabel" valign="top">
																<html:select styleId="compEleUnit${ind}"
																	property="entity.composingElements[${ind}].domainComposingElement.valueUnit"
																	onchange="javascript:callPrompt('Unit', 'compEleUnit${ind}');">
																	<option value="" />
																		<html:options name="composingElementUnits" />
																	<option value="other">
																		[Other]
																	</option>
																</html:select>
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top" colspan="1">
																<strong>Description</strong>
															</td>
															<td class="rightLabel" colspan="3">
																<html:textarea
																	property="entity.composingElements[${ind}].domainComposingElement.description"
																	rows="3" cols="65" />
															</td>
														</tr>

														<tr>
															<td valign="bottom" class="leftLabel">
																<a href="#"
																	onclick="javascript:addChildComponent(nanoparticleEntityForm, 'nanoparticleEntity', ${ind}, 'addInherentFunction'); return false;">
																	<span class="addLink2">Add Inherent Function</span> </a>
															</td>
															<td colspan="3" class="rightLabel">
																<jsp:include
																	page="/particle/composition/nanoparticleEntity/bodyFunctionUpdate.jsp">
																	<jsp:param name="compEleInd" value="${ind}" />
																</jsp:include>
																&nbsp;
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
				<br>
				<%--Particle Entity File Information --%>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify" id="peFileTitle">
									Particle Entity File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<td valign="bottom" width="10%">
											<a href="#"
												onclick="javascript:addComponent(nanoparticleEntityForm, 'nanoparticleEntity', 'addFile'); return false;">
												<span class="addLink">Add File</span> </a>
										</td>
										<td id="fileTd">
											<logic:iterate name="nanoparticleEntityForm"
												property="entity.files" id="entityFile" indexId="fileInd">
												<jsp:include page="/particle/bodyLoadFileUpdate.jsp">
													<jsp:param name="fileInd" value="${fileInd}" />
													<jsp:param name="form" value="nanoparticleEntityForm" />
													<jsp:param name="action" value="nanoparticleEntity" />
													<jsp:param name="removeCmd" value="\'removeFile\'" />
													<jsp:param name="fileBean" value="entity.files[${fileInd}]" />
													<jsp:param name="fileId"
														value="${nanoparticleEntityForm.map.entity.files[fileInd].domainFile.id}" />
													<jsp:param name="fileUri"
														value="${nanoparticleEntityForm.map.entity.files[fileInd].domainFile.uri}" />
													<jsp:param name="fileTitle"
														value="${nanoparticleEntityForm.map.entity.files[fileInd].domainFile.title}" />
													<jsp:param name="fileHidden"
														value="${nanoparticleEntityForm.map.entity.files[fileInd].hidden}" />
													<jsp:param name="fileImage"
														value="${nanoparticleEntityForm.map.entity.files[fileInd].image}" />
													<jsp:param name="fileUriExternal"
														value="${nanoparticleEntityForm.map.entity.files[fileInd].domainFile.uriExternal}" />
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
												<c:choose>
													<c:when test="${!empty param.particleId }">
														<html:hidden property="particleId"
															value="${param.particleId }" />
													</c:when>
													<c:otherwise>
														<html:hidden property="particleId" />
													</c:otherwise>
												</c:choose>												
												<input type="hidden" name="submitType"
													value="${param.submitType}" />
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
