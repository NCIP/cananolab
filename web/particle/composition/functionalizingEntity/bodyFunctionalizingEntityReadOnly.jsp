<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html:form action="/functionalizingEntity">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${particleName} Sample Composition - Functionalizing Entity
				</h4>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="function_entity_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>	
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
							${functionalizingEntityForm.map.entity.type}&nbsp;
						</td>
						<td class="label" valign="top">
							<strong>Chemical Name</strong>
						</td>
						<td class="rightLabel">
							${functionalizingEntityForm.map.entity.name}&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Molecular Formula Type</strong>
						</td>
						<td class="label">
							${functionalizingEntityForm.map.entity.molecularFormulaType}&nbsp;
						</td>
						<td class="label" valign="top">
							<strong>Molecular Formula</strong>
						</td>
						<td class="rightLabel">
							${functionalizingEntityForm.map.entity.molecularFormula}&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Value</strong>
						</td>
						<td class="label">
							${functionalizingEntityForm.map.entity.value}&nbsp;
						</td>
						<td class="label" valign="top">
							<strong>Value Unit</strong>
						</td>
						<td class="rightLabel">
							${functionalizingEntityForm.map.entity.valueUnit}&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							${functionalizingEntityForm.map.entity.description}&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Activation Method</strong>
						</td>
						<td class="label">
							${functionalizingEntityForm.map.entity.activationMethod.type}&nbsp;
						</td>
						<td class="label" valign="top">
							<strong>Activation Effect</strong>
						</td>
						<td class="rightLabel">
							${functionalizingEntityForm.map.entity.activationMethod.activationEffect}&nbsp;
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
													(String) pageContext
															.getAttribute("entityType"),
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
										<td id="functionTd">
											<logic:iterate name="functionalizingEntityForm"
												property="entity.functions" id="function" indexId="ind">
												<c:choose>
													<c:when
														test="${functionalizingEntityForm.map.entity.functions[ind].type == 'imaging'}">
														<c:set var="modalityDisplay" value="display: inline;" />
													</c:when>
													<c:otherwise>
														<c:set var="modalityDisplay" value="display: none;" />
													</c:otherwise>
												</c:choose>

												<table class="topBorderOnly" cellspacing="0" cellpadding="3"
													width="100%" align="center" summary="" border="0">
													<tbody>
														<tr>
															<td class="formSubTitleNoRight" colspan="4">
																<span>Function #${ind + 1}</span>
															</td>

														</tr>
														<tr>
															<td class="leftLabelWithTop" valign="top">
																<strong>Function Type</strong>
															</td>
															<td class="labelWithTop" valign="top">
																${functionalizingEntityForm.map.entity.functions[ind].type}&nbsp;

															</td>
															<td class="labelWithTop" valign="top">
																<strong style="" id="modalityStrong_${ind}">Modality
																	Type </strong>&nbsp;
															</td>
															<td class="rightLabelWithTop" valign="top">
																<div id="modalityDiv_${ind}" style="">
																	${functionalizingEntityForm.map.entity.functions[ind].imagingFunction.modality}&nbsp;

																</div>
																&nbsp;
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Description</strong>
															</td>
															<td class="rightLabel" colspan="3">
																${functionalizingEntityForm.map.entity.functions[ind].description}&nbsp;
															</td>
														</tr>
														<c:if
															test="${functionalizingEntityForm.map.entity.functions[ind].type == 'targeting'}">
															<tr>
																<td colspan="4" class="completeLabel">
																	<jsp:include
																		page="/particle/composition/functionalizingEntity/bodyTargetInfoReadOnly.jsp">
																		<jsp:param name="funcInd" value="${ind}" />
																	</jsp:include>
																</td>
															</tr>
														</c:if>
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
							<td class="formTitle" >
								<div align="justify" id="peFileTitle">
									Functionalizing Entity File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" >
								<logic:iterate name="functionalizingEntityForm"
									property="entity.files" id="entityFile" indexId="fileInd">
									<jsp:include page="/particle/bodyLoadFileReadOnly.jsp">
										<jsp:param name="fileInd" value="${fileInd}" />
										<jsp:param name="action" value="functionalizingEntity" />
										<jsp:param name="domainFile"
											value="entity.files[${fileInd}].domainFile" />
										<jsp:param name="fileId"
											value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.id}" />
										<jsp:param name="fileUri"
											value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.uri}" />
										<jsp:param name="fileType"
											value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.type}" />
										<jsp:param name="fileTitle"
											value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.title}" />
										<jsp:param name="fileKeywordsStr"
											value="${functionalizingEntityForm.map.entity.files[fileInd].keywordsStr}" />
										<jsp:param name="fileVisibilityGroups"
											value="${functionalizingEntityForm.map.entity.files[fileInd].visibilityGroups}" />
										<jsp:param name="uriExternal"
											value="${functionalizingEntityForm.map.entity.files[fileInd].domainFile.uriExternal}" />
										<jsp:param name="fileImage"
											value="${functionalizingEntityForm.map.entity.files[fileInd].image}" />
									</jsp:include>

									<br>
								</logic:iterate>
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</table>
</html:form>
