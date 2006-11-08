<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%
String mode = (String)session.getAttribute("pageMode");
%>

<html:form action="/invitroImmunotoxicityOxidativeBurst">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Invitro Characterization - Immunotoxicity - OxidativeBurst
				</h4>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_nanoparticle')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${invitroImmunotoxicityOxidativeBurstForm.map.particleName} (${invitroImmunotoxicityOxidativeBurstForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="thisForm" value="${invitroImmunotoxicityOxidativeBurstForm}" />
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="bodySharedCharacterizationSummary.jsp" />
				<jsp:include page="bodySharedCharacterizationInstrument.jsp" />
				<%-- OxidativeBurst characterization specific --%>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Histogram/Bar Chart Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Number of Histogram/Bar Charts</strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<html:text property="achar.numberOfDerivedBioAssayData" />
									</c:when>
									<c:otherwise>
										${invitroImmunotoxicityOxidativeBurstForm.map.achar.numberOfDerivedBioAssayData}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<input type="button" onclick="javascript:update(this.form, 'invitroImmunotoxicityOxidativeBurst')" value="Update">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<c:forEach var="achar.table" items="${invitroImmunotoxicityOxidativeBurstForm.map.achar.derivedBioAssayData}" varStatus="status">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														Histogram/Bar Chart ${status.index+1}
													</div>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<strong>Characterization File Name</strong>
												</td>
												<td class="label">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<logic:present name="characterizationFile${status.index}">
																<a href="#"><bean:write name="characterizationFile${status.index}" property="name" /></a>
																<bean:define id="fileId" name='characterizationFile${status.index}' property='id' type="java.lang.String"/>
																<html:hidden name="achar.table" property="fileId" value="${fileId}" indexed="true" />
															</logic:present>
															<logic:notPresent name="characterizationFile${status.index}">
																Click on "Load File" button
															</logic:notPresent>
														</c:when>
														<c:otherwise>
															${invitroImmunotoxicityOxidativeBurstForm.map.achar.derivedBioAssayData[status.index].file.name}&nbsp;
														</c:otherwise>
													</c:choose>													
												</td>
												<td class="rightLabel" colspan="2">
													<input type="button" onclick="javascript:loadFile(this.form, 'invitroImmunotoxicityOxidativeBurst', '${invitroImmunotoxicityOxidativeBurstForm.map.particleName}', ${status.index})" value="Load File">
												</td>
											</tr>
											
											<tr>
												<td class="leftLabel">
													<strong>OxidativeBurst Percentage</strong>
												</td>
												<td class="rightLabel" colspan="3">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:text name="achar.table" indexed="true" property="datumList[0].value" />
														</c:when>
														<c:otherwise>
															${invitroImmunotoxicityOxidativeBurstForm.map.achar.derivedBioAssayData[status.index].datumList[0].value}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<strong>Is Control?</strong>
													&nbsp;&nbsp;&nbsp;
													<input type="radio" name="isControl" value="Yes" />Yes
													&nbsp;&nbsp;&nbsp;
													<input type="radio" name="isControl" value="No" />No
													&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="button" onclick="javascript:addControlConditions(this.form, 'invitroImmunotoxicityOxidativeBurst', ${status.index})" value="Add">
												</td>
											</tr>
											
											<logic:present name="achar.table" property="datumList[0].control">
												<tr>
 													<td class="completeLabel" colspan="4">
														<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
															<tbody>
																<tr class="topBorder">
																	<td class="formSubTitle" colspan="4">
																		<div align="justify">
																			Control 
																		</div>	
																	</td>
																</tr>
																<tr>
																	<td class="leftLabel">
																		<strong>Name:</strong>
																	</td>
																	<td class="label">
    																	<c:choose>
        																	<c:when test="${canUserUpdateParticle eq 'true'}">
																				<html:text name="achar.table" indexed="true" property="datumList[0].control.name" />
        																	</c:when>
        																	<c:otherwise>
																				${invitroImmunotoxicityOxidativeBurstForm.map.achar.derivedBioAssayData[status.index].datumList[0].control.name}&nbsp;
        																	</c:otherwise>
    																	</c:choose>
																	</td>
																	<td class="label">
																		<strong>Type:</strong>
																	</td>
																	<td class="rightLabel">
    																	<c:choose>
        																	<c:when test="${canUserUpdateParticle eq 'true'}">
																				<html:text name="achar.table" indexed="true" property="datumList[0].control.type" />
        																	</c:when>
        																	<c:otherwise>
																				${invitroImmunotoxicityOxidativeBurstForm.map.achar.derivedBioAssayData[status.index].datumList[0].control.type}&nbsp;
        																	</c:otherwise>
    																	</c:choose>
																	</td>
																</tr>
															</tbody>
														</table>
													</td>
												</tr>
											</logic:present>
											<%
											if ( mode != null && mode.equals("showConditionEntry") ) {
											%>
												<tr>
													<td class="leftLabel">
														<strong>Number of Conditions</strong>
													</td>
													<td class="label">
														<c:choose>
															<c:when test="${canUserUpdateParticle eq 'true'}">
																<html:text name="achar.table" property="datumList[0].numberOfConditions" />
															</c:when>
															<c:otherwise>
																${invitroImmunotoxicityOxidativeBurstForm.map.achar.derivedBioAssayData[status.index].datumList[0].numberOfConditions}&nbsp;
															</c:otherwise>
														</c:choose>
													</td>
													<td class="rightLabel" colspan="2">
														&nbsp;
														<c:choose>
															<c:when test="${canUserUpdateParticle eq 'true'}">
																<input type="button" onclick="javascript:updateConditions(this.form, 'invitroImmunotoxicityOxidativeBurst', ${status.index})" value="Update Conditions">
															</c:when>
														</c:choose>
													</td>
												</tr>
 											<%
											}
											%>
											<logic:present name="achar.table" property="datumList[0].conditionList">
												<tr>
													<td class="completeLabel" colspan="4">
														<c:forEach var="achar.table" items="${invitroImmunotoxicityOxidativeBurstForm.map.achar.derivedBioAssayData[status.index].datumList[0].conditionList}" varStatus="cstatus">
															<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
																<tbody>
																	<tr class="topBorder">
																		<td class="formSubTitle" colspan="4">
																			<div align="justify">
																				Condition ${cstatus.index+1}
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td class="leftLabel">
																			<strong>Type:</strong>
																		</td>
																		<td class="label">
    																		<c:choose>
        																		<c:when test="${canUserUpdateParticle eq 'true'}">
																					<html:text name="achar.table" indexed="true" property="datumList[0].condition[cstatus.index].type" />
        																		</c:when>
        																		<c:otherwise>
																					${invitroImmunotoxicityOxidativeBurstForm.map.achar.derivedBioAssayData[status.index].datumList[0].condition[cstatus.index].type}&nbsp;
        																		</c:otherwise>
    																		</c:choose>
																		</td>
																		<td class="label">
																			<strong>Value:</strong>
																		</td>
																		<td class="rightLabel">
    																		<c:choose>
        																		<c:when test="${canUserUpdateParticle eq 'true'}">
																					<html:text name="achar.table" indexed="true" property="datumList[0].condition[cstatus.index].value" />
        																		</c:when>
        																		<c:otherwise>
																					${invitroImmunotoxicityOxidativeBurstForm.map.achar.derivedBioAssayData[status.index].datumList[0].condition[cstatus.index].value}&nbsp;
        																		</c:otherwise>
    																		</c:choose>
																		</td>
																	</tr>
																</tbody>
															</table>
														</c:forEach>
													</td>
												</tr>
											</logic:present>
										</tbody>
									</table>
								</c:forEach>
							</td>
						</tr>
				</table>
				<%-- end of size characterization specific --%>
				<jsp:include page="bodySharedCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
