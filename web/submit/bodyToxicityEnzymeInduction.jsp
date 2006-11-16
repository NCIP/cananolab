<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/invitroToxicityEnzymeInduction">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Invitro Characterization - Toxicity - Enzyme Induction
				</h4>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_nanoparticle')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${invitroToxicityEnzymeInductionForm.map.particleName} (${invitroToxicityEnzymeInductionForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="thisForm" value="${invitroToxicityEnzymeInductionForm}" />
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="bodySharedCharacterizationSummary.jsp" />
				<jsp:include page="bodySharedCharacterizationInstrument.jsp" />
				<%-- hemolysis characterization specific --%>
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
										${invitroToxicityEnzymeInductionForm.map.achar.numberOfDerivedBioAssayData}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<input type="button" onclick="javascript:update(this.form, 'invitroToxicityEnzymeInduction')" value="Update">
									</c:when>
								</c:choose>
							</td>
						</tr>
						
						<tr>
							<td class="completeLabel" colspan="4">
								<c:forEach var="achar.derivedBioAssayData" items="${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData}" varStatus="status">
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
																<html:hidden name="achar.derivedBioAssayData" property="fileId" value="${fileId}" indexed="true" />
																<a href="invitroToxicityEnzymeInduction.do?dispatch=download&amp;fileId=${fileId}"><bean:write name="characterizationFile${status.index}" property="name" /></a>
															</logic:present>
															<logic:notPresent name="characterizationFile${status.index}">
																Click on "Load File" button
															</logic:notPresent>
														</c:when>
														<c:otherwise>
															${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].file.name}&nbsp;
														</c:otherwise>
													</c:choose>													
												</td>
												<td class="rightLabel" colspan="2">
													<input type="button" onclick="javascript:loadFile(this.form, 'invitroToxicityEnzymeInduction', '${invitroToxicityEnzymeInductionForm.map.particleName}', ${status.index})" value="Load File">
												</td>
											</tr>
											
											<tr>
												<td class="leftLabel">
													<strong>Enzyme Induction Percentage</strong>
												</td>
												<td class="rightLabel" colspan="3">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:text name="achar.derivedBioAssayData" property="datumList[0].value" indexed="true" />
															&nbsp; ${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].datumList[0].valueUnit}	
														</c:when>
														<c:otherwise>
															${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].datumList[0].value}
															&nbsp; ${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].datumList[0].valueUnit}
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											
											<tr>
												<td class="leftLabel" colspan="4">
													<strong>Is Control?</strong>
													
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<logic:present name="achar.derivedBioAssayData" property="datumList[0].control">
																&nbsp;&nbsp;&nbsp;
																<input type="radio" name="isControl${status.index}" value="Yes" onclick="javascript:addControlConditions(this.form, 'addControl', 'invitroToxicityEnzymeInduction', ${status.index})" checked />Yes
																&nbsp;&nbsp;&nbsp;
																<input type="radio" name="isControl${status.index}" value="No" onclick="javascript:addControlConditions(this.form, 'addConditions', 'invitroToxicityEnzymeInduction', ${status.index})" />No
															</logic:present>
															<logic:notPresent name="achar.derivedBioAssayData" property="datumList[0].control">
																<logic:notPresent name="achar.derivedBioAssayData" property="datumList[0].conditionList">
																	&nbsp;&nbsp;&nbsp;
																	<input type="radio" name="isControl${status.index}" value="Yes" onclick="javascript:addControlConditions(this.form, 'addControl', 'invitroToxicityEnzymeInduction', ${status.index})" />Yes
																	&nbsp;&nbsp;&nbsp;
																	<input type="radio" name="isControl${status.index}" value="No" onclick="javascript:addControlConditions(this.form, 'addConditions', 'invitroToxicityEnzymeInduction', ${status.index})" />No
																</logic:notPresent>
																<logic:present name="achar.derivedBioAssayData" property="datumList[0].conditionList">
																	&nbsp;&nbsp;&nbsp;
																	<input type="radio" name="isControl${status.index}" value="Yes" onclick="javascript:addControlConditions(this.form, 'addControl', 'invitroToxicityEnzymeInduction', ${status.index})" />Yes
																	&nbsp;&nbsp;&nbsp;
																	<input type="radio" name="isControl${status.index}" value="No" onclick="javascript:addControlConditions(this.form, 'addConditions', 'invitroToxicityEnzymeInduction', ${status.index})" checked />No
																</logic:present>
															</logic:notPresent>
														</c:when>
														<c:otherwise>
															<logic:present name="achar.derivedBioAssayData" property="datumList[0].control">
																&nbsp;&nbsp;&nbsp;
																<input type="radio" name="isControl${status.index}" value="Yes" onclick="javascript:addControlConditions(this.form, 'addControl', 'invitroToxicityEnzymeInduction', ${status.index})" disabled checked />Yes
																&nbsp;&nbsp;&nbsp;
																<input type="radio" name="isControl${status.index}" value="No" onclick="javascript:addControlConditions(this.form, 'addConditions', 'invitroToxicityEnzymeInduction', ${status.index})" disabled />No
															</logic:present>
															<logic:notPresent name="achar.derivedBioAssayData" property="datumList[0].control">
																<logic:notPresent name="achar.derivedBioAssayData" property="datumList[0].conditionList">
																	&nbsp;&nbsp;&nbsp;
																	<input type="radio" name="isControl${status.index}" value="Yes" onclick="javascript:addControlConditions(this.form, 'addControl', 'invitroToxicityEnzymeInduction', ${status.index})" disabled />Yes
																	&nbsp;&nbsp;&nbsp;
																	<input type="radio" name="isControl${status.index}" value="No" onclick="javascript:addControlConditions(this.form, 'addConditions', 'invitroToxicityEnzymeInduction', ${status.index})" disabled />No
																</logic:notPresent>
																<logic:present name="achar.derivedBioAssayData" property="datumList[0].conditionList">
																	&nbsp;&nbsp;&nbsp;
																	<input type="radio" name="isControl${status.index}" value="Yes" onclick="javascript:addControlConditions(this.form, 'addControl', 'invitroToxicityEnzymeInduction', ${status.index})" disabled />Yes
																	&nbsp;&nbsp;&nbsp;
																	<input type="radio" name="isControl${status.index}" value="No" onclick="javascript:addControlConditions(this.form, 'addConditions', 'invitroToxicityEnzymeInduction', ${status.index})" disabled checked />No
																</logic:present>
															</logic:notPresent>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											
											<logic:present name="achar.derivedBioAssayData" property="datumList[0].control">
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
																	<td class="leftLabel" colspan="2">
																		<strong>Name:</strong>
																		&nbsp;&nbsp;&nbsp;
    																	<c:choose>
        																	<c:when test="${canUserUpdateParticle eq 'true'}">
																				<html:text name="achar.derivedBioAssayData" indexed="true" property="datumList[0].control.name" />
        																	</c:when>
        																	<c:otherwise>
																				${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].datumList[0].control.name}&nbsp;
        																	</c:otherwise>
    																	</c:choose>
																	</td>
																	<td class="label"  colspan="2">
																		<strong>Type:</strong>
																		&nbsp;&nbsp;&nbsp;
    																	<c:choose>
        																	<c:when test="${canUserUpdateParticle eq 'true'}">
																				<html:select name="achar.derivedBioAssayData" property="datumList[0].control.type" indexed="true">
																					<html:options name="allControlTypes" />
																				</html:select>
        																	</c:when>
        																	<c:otherwise>
																				${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].datumList[0].control.type}&nbsp;
        																	</c:otherwise>
    																	</c:choose>
																	</td>
																</tr>
															</tbody>
														</table>
													</td>
												</tr>
											</logic:present>
											
											<logic:present name="achar.derivedBioAssayData" property="datumList[0].conditionList">
												<tr>
 													<td class="completeLabel" colspan="4">
														<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
															<tbody>
																<tr class="topBorder">
																	<td class="formSubTitle" colspan="4">
																		<div align="justify">
																			Condition 
																		</div>	
																	</td>
																</tr>
																<tr>
																	<td class="leftLabel">
																		<strong>Particle Concentration</strong>
																	</td>
																	<td class="label">
    																	<c:choose>
        																	<c:when test="${canUserUpdateParticle eq 'true'}">
																				<html:text name="achar.derivedBioAssayData" property="datumList[0].conditionList[0].value" indexed="true"/>
        																	</c:when>
        																	<c:otherwise>
																				${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].datumList[0].conditionList[0].value}&nbsp;
        																	</c:otherwise>
    																	</c:choose>
    																	&nbsp;&nbsp;&nbsp;
    																	<c:choose>
        																	<c:when test="${canUserUpdateParticle eq 'true'}">
																				<html:select name="achar.derivedBioAssayData" property="datumList[0].conditionList[0].valueUnit" indexed="true">
																					<html:options name="allConcentrationUnits" />
																				</html:select>
        																	</c:when>
        																	<c:otherwise>
																				${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].datumList[0].conditionList[0].valueUnit}&nbsp;
        																	</c:otherwise>
    																	</c:choose>
																	</td>
																	<td class="leftLabel">
																		<strong>Molecular Concentration</strong>
																	</td>
																	<td class="label">
    	    															<c:choose>
        																	<c:when test="${canUserUpdateParticle eq 'true'}">
																				<html:text name="achar.derivedBioAssayData" property="datumList[0].conditionList[1].value" indexed="true"/>
 																				&nbsp; ${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].datumList[0].conditionList[1].valueUnit}	
        																	</c:when>
        																	<c:otherwise>
																				${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].datumList[0].conditionList[1].value}
 																				&nbsp; ${invitroToxicityEnzymeInductionForm.map.achar.derivedBioAssayData[status.index].datumList[0].conditionList[1].valueUnit}	
        																	</c:otherwise>
    																	</c:choose>
																	</td>
																</tr>
															</tbody>
														</table>
													</td>
												</tr>
											</logic:present>
										</tbody>
									</table>
								</c:forEach>
							</td>
						</tr>
					</tbody>
				</table>
				<%-- end of size characterization specific --%>
				<jsp:include page="bodySharedCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
