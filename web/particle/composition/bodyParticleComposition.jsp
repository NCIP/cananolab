<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.calab.service.util.StringUtils"%>
<script type="text/javascript" src="javascript/editableDropDown.js"></script>

<script type="text/javascript">
<!--//
function confirmDeletion()
{
	answer = confirm("Are you sure you want to delete the composition?")
	if (answer !=0)
	{
		this.document.forms[0].dispatch.value="deleteConfirmed";
		this.document.forms[0].submit(); 
		return true;
	}
}
//-->
</script>
<jsp:include page="/particle/submitMenu.jsp" />
<%-- turn off update when doing remote searches --%>
<c:choose>
	<c:when test="${!empty param.gridNodeHost}">
		<c:set var="isRemote" value="true" scope="session" />
	</c:when>
	<c:otherwise>
		<c:set var="isRemote" value="false" scope="session" />
	</c:otherwise>
</c:choose>
<html:form action="/composition">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Physical Characterization - Composition
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
					${nanoparticleCompositionForm.map.particleName}
					(${nanoparticleCompositionForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
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
							<strong>Characterization Source* </strong>
						</td>
						<td class="label">
							${canCreatenanoParticle}
							<c:choose>
								<c:when
									test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
									<html:select property="composition.characterizationSource"
										onkeydown="javascript:fnKeyDownHandler(this, event);"
										onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
										onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
										onchange="fnChangeHandler_A(this, event);">
										<option value="">
											--?--
										</option>
										<html:options name="characterizationSources" />
									</html:select>
								</c:when>
								<c:otherwise>
						${nanoparticleCompositionForm.map.characterizationSource}&nbsp;
					</c:otherwise>
							</c:choose>
						</td>
						<td class="label">
							<strong>View Title*</strong>
							<br>
							<em>(text will be truncated after 20 characters)</em>
						</td>
						<td class="rightLabel">
							<c:choose>
								<c:when
									test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
									<html:text property="composition.viewTitle" />
								</c:when>
								<c:otherwise>
						${nanoparticleCompositionForm.map.composition.viewTitle}&nbsp;
					</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<c:choose>
								<c:when
									test="${canCreateNanoparticle eq 'true'  && isRemote eq 'false'}">
									<html:textarea property="composition.description" rows="3"
										cols="80" />
								</c:when>
								<c:otherwise>
						${nanoparticleCompositionForm.map.description}&nbsp;
					</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
				<br>
				<c:set var="particleType" value="${param.particleType}" scope="page" />
				<jsp:useBean id="particleType" type="java.lang.String" />
				<%
							String includePage = StringUtils
							.getOneWordUpperCaseFirstLetter(particleType);
					pageContext.setAttribute("includePage", includePage);
				%>
				<jsp:include page="/particle/composition/body${includePage}Info.jsp" />
				<%--Composing Element Information --%>
				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Composing Element Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<c:choose>
											<c:when test="${canCreateNanoparticle eq 'true'}">
												<td valign="bottom">
													<a href="#"
														onclick="javascript:addComposingElement(nanoparticleCompositionForm, 'composition')"><span
														class="addLink">Add Composing Element</span> </a>
												</td>
											</c:when>
											<c:otherwise>
												<td></td>
											</c:otherwise>
										</c:choose>
										<td>
											<logic:iterate name="nanoparticleCompositionForm"
												property="composition.composingElements"
												id="composingElement" indexId="ind">

												<table class="topBorderOnly" cellspacing="0" cellpadding="3"
													width="100%" align="center" summary="" border="0">
													<tbody>
														<tr>
															<c:choose>
																<c:when test="${canCreateNanoparticle eq 'true'}">
																	<td class="formSubTitle" colspan="4" align="right">
																		<a href="#"
																			onclick="javascript:removeComposingElement(nanoparticleCompositionForm, 'composition', ${ind})">
																			<img src="images/delete.gif" border="0"
																				alt="remove this composing element"> </a>
																	</td>
																</c:when>
																<c:otherwise>
																	<td class="formSubTitle" colspan="4">
																		&nbsp;
																	</td>
																</c:otherwise>
															</c:choose>
														</tr>
														<tr>
															<td class="leftLabelWithTop" valign="top" width="15%">
																<strong>Composing Element Type</strong>
															</td>
															<c:choose>
																<c:when test="${canCreateNanoparticle eq 'true'}">
																	<td class="labelWithTop" valign="top">
																		<html:select
																			property="composition.composingElements[${ind}].elementType"
																			onkeydown="javascript:fnKeyDownHandler(this, event);"
																			onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
																			onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
																			onchange="fnChangeHandler_A(this, event);">
																			<option value="">
																				--?--
																			</option>
																			<html:options name="allComposingElementTypes" />
																		</html:select>
																	</td>
																</c:when>
																<c:otherwise>
																	<td class="labelWithTop">
																		${nanoparticleCompositionForm.map.composition.composingElements[ind].elementType}&nbsp;
																	</td>
																</c:otherwise>
															</c:choose>
															<td class="labelWithTop" valign="top">
																<strong>Chemical Name</strong>
															</td>
															<c:choose>
																<c:when test="${canCreateNanoparticle eq 'true'}">
																	<td class="rightLabelWithTop" valign="top">
																		<html:text
																			property="composition.composingElements[${ind}].chemicalName" size="30"/>
																	</td>
																</c:when>
																<c:otherwise>
																	<td class="rightLabelWithTop">
																		${nanoparticleCompositionForm.map.composition.composingElements[ind].chemicalName}
																		&nbsp;
																	</td>
																</c:otherwise>
															</c:choose>
														</tr>																		
														<tr>
															<td class="leftLabel" valign="top" colspan="1">
																<strong>Description</strong>
															</td>
															<c:choose>
																<c:when test="${canCreateNanoparticle eq 'true'}">
																	<td class="rightLabel" colspan="3">
																		<html:textarea
																			property="composition.composingElements[${ind}].description"
																			rows="3" cols="65" />
																	</td>
																</c:when>
																<c:otherwise>
																	<td class="rightLabel" colspan="3">
																		${nanoparticleCompositionForm.map.composition.composingElements[ind].description}&nbsp;
																	</td>
																</c:otherwise>
															</c:choose>
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
				<c:choose>
					<c:when
						test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
						<br>
						<table width="100%" border="0" align="center" cellpadding="3"
							cellspacing="0" class="topBorderOnly" summary="">
							<tr>
								<td width="30%">
									<span class="formMessage"> </span>
									<br>
									<c:choose>
										<c:when
											test="${param.dispatch eq 'setupUpdate' && canUserDeleteChars eq 'true'}">
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
														<input type="reset" value="Reset" onclick="">
														<input type="hidden" name="dispatch" value="create">
														<input type="hidden" name="page" value="2">
														<c:choose>
															<c:when
																test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
																<html:hidden property="particleType" />
															</c:when>
														</c:choose>
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
					</c:when>
				</c:choose>
			</td>
		</tr>
	</table>
</html:form>
