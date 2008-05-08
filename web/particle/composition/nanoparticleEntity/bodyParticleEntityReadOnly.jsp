<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/nanoparticleEntity">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${particleName} Sample Composition - Nanoparticle Entity
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
				<h5 align="center">
					Entity#1:Dendrimer
				</h5>
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
							<strong>Particle Entity Type</strong>
						</td>
						<td class="rightLabel">
							${nanoparticleEntityForm.map.entity.type}&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							${nanoparticleEntityForm.map.entity.description}&nbsp;
						</td>
					</tr>
				</table>
				<br>
				<div id="entityInclude">
					<c:if test="${!empty nanoparticleEntityForm.map.entity.type}">
						<c:set var="entityType"
							value="${nanoparticleEntityForm.map.entity.type}" scope="page" />
						<%
							String entityClass = gov.nih.nci.cananolab.ui.core.InitSetup
											.getInstance().getObjectName(
													(String) pageContext
															.getAttribute("entityType"),
													application);
									pageContext.setAttribute("entityClass", entityClass);
						%>
						<jsp:include
							page="/particle/composition/nanoparticleEntity/body${entityClass}Info.jsp" />
					</c:if>
				</div>
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
										<td></td>
										<td>
											<logic:iterate name="nanoparticleEntityForm"
												property="entity.composingElements" id="composingElement"
												indexId="ind">
												<table class="topBorderOnly" cellspacing="0" cellpadding="3"
													width="100%" align="center" summary="" border="0">
													<tbody>
														<tr>
															<td class="formSubTitle" colspan="4">
																Composing Element #${ind+1}
															</td>
														</tr>
														<tr>
															<td class="leftLabelWithTop" valign="top">
																<strong>Composing Element Type</strong>
															</td>
															<td class="labelWithTop">
																${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.type}&nbsp;
															</td>
															<td class="labelWithTop" valign="top">
																<strong>Chemical Name</strong>
															</td>
															<td class="rightLabelWithTop">
																${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.name}&nbsp;
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Molecular Formula Type</strong>
															</td>
															<td class="label" valign="top">
																${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.molecularFormulaType}&nbsp;
															</td>
															<td class="label" valign="top">
																<strong>Molecular Formula</strong>
															</td>
															<td class="rightLabel" valign="top">
																${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.molecularFormula}&nbsp;
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Value</strong>
															</td>
															<td class="label" valign="top">
																${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.value}&nbsp;
															</td>
															<td class="label" valign="top">
																<strong>Unit</strong>
															</td>
															<td class="rightLabel" valign="top">
																${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.valueUnit}&nbsp;
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Description</strong>
															</td>
															<td class="rightLabel" colspan="3">
																${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.description}&nbsp;
															</td>
														</tr>
														<tr>
															<td class="leftLabel">
																&nbsp;
															</td>
															<td colspan="3" class="rightLabel">
																<jsp:include
																	page="/particle/composition/nanoparticleEntity/bodyFunctionReadOnly.jsp">
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
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle">
								<div align="justify" id="peFileTitle">
									Particle Entity File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel">
								<logic:iterate name="nanoparticleEntityForm"
									property="entity.files" id="entityFile" indexId="fileInd">
									<jsp:include page="/particle/bodyLoadFileReadOnly.jsp">
										<jsp:param name="fileInd" value="${fileInd}" />
										<jsp:param name="action" value="nanoparticleEntity" />
										<jsp:param name="domainFile"
											value="entity.files[${fileInd}].domainFile" />
										<jsp:param name="fileId"
											value="${nanoparticleEntityForm.map.entity.files[fileInd].domainFile.id}" />
										<jsp:param name="fileUri"
											value="${nanoparticleEntityForm.map.entity.files[fileInd].domainFile.uri}" />
										<jsp:param name="fileType"
											value="${nanoparticleEntityForm.map.entity.files[fileInd].domainFile.type}" />
										<jsp:param name="fileTitle"
											value="${nanoparticleEntityForm.map.entity.files[fileInd].domainFile.title}" />
										<jsp:param name="fileKeywordsStr"
											value="${nanoparticleEntityForm.map.entity.files[fileInd].keywordsStr}" />
										<jsp:param name="fileVisibilityGroups"
											value="${nanoparticleEntityForm.map.entity.files[fileInd].visibilityGroups}" />
										<jsp:param name="uriExternal"
											value="${nanoparticleEntityForm.map.entity.files[fileInd].domainFile.uriExternal}" />
										<jsp:param name="fileImage"
											value="${nanoparticleEntityForm.map.entity.files[fileInd].image}" />
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