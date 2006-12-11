<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/nanoparticleSurface">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Physical Characterization - Surface
				</h4>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_nanoparticle')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${nanoparticleSurfaceForm.map.particleName} (${nanoparticleSurfaceForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="thisForm" value="${nanoparticleSurfaceForm}" />
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="bodySharedCharacterizationSummary.jsp" />
				<jsp:include page="bodySharedCharacterizationInstrument.jsp" />

				<%-- Surface characterization specific --%>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Particle Surface Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<div align="justify">
									<strong>Surface Area</strong>
								</div>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<html:text property="achar.surfaceArea" />&nbsp;
										<html:select property="achar.surfaceAreaUnit">
											<html:options name="allAreaMeasureUnits" />
										</html:select>
									</c:when>
									<c:otherwise>
										${nanoparticleSurfaceForm.achar.surfaceArea}&nbsp;
										${nanoparticleSurfaceForm.achar.surfaceAreaUnit}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="label">
								<div align="justify">
									<strong>isHydrophobic</strong>
								</div>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<html:select property="achar.isHydrophobic">
											<html:options name="booleanChoices" />
										</html:select>
									</c:when>
									<c:otherwise>
										${nanoparticleSurfaceForm.achar.isHydrophobic}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<div align="justify">
									<strong>Charge</strong>
								</div>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<html:text property="achar.charge" />&nbsp;
										<html:select property="achar.chargeUnit">
											<html:options name="allChargeMeasureUnits" />
										</html:select>
									</c:when>
									<c:otherwise>
										${nanoparticleSurfaceForm.achar.charge}&nbsp;
										${nanoparticleSurfaceForm.achar.chargeUnit}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="label">
								<div align="justify">
									<strong>Zeta Potential</strong>
								</div>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<html:text property="achar.zetaPotential" />&nbsp;mV
									</c:when>
									<c:otherwise>
										${nanoparticleSurfaceForm.achar.zetaPotential}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</tbody>
				</table>

				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Surface Chemistry
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Number of Surface Chemistry</strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<html:text property="achar.numberOfSurfaceChemistries" />
									</c:when>
									<c:otherwise>
						${nanoparticleSurfaceForm.map.achar.numberOfSurfaceChemistries}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<input type="button" onclick="javascript:updateSurfaceChemistries(this.form, 'nanoparticleSurface')" value="Update Surface Chemistries">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<c:forEach var="achar.surfaceChemistries" items="${nanoparticleSurfaceForm.map.achar.surfaceChemistries}" varStatus="status">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														Surface Chemistry ${status.index+1}
													</div>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<strong>Molecule</strong>
												</td>
												<td class="label">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:text name="achar.surfaceChemistries" indexed="true" property="moleculeName" />
														</c:when>
														<c:otherwise>
						${nanoparticleSurfaceForm.map.achar.surfaceChemistries[status.index].moleculeName}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
												<td class="label">
													<strong>Number of Molecule </strong>
												</td>
												<td class="rightLabel">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:text name="achar.surfaceChemistries" indexed="true" property="numberOfMolecules" /> &nbsp;															
														</c:when>
														<c:otherwise>
															${nanoparticleSurfaceForm.map.achar.surfaceChemistries[status.index].numberOfMolecules}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</tbody>
									</table>
								</c:forEach>
							</td>
						</tr>
				</table>



				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Plot Chart
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Number of Plot Chart</strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<html:text property="achar.numberOfDerivedBioAssayData" />
									</c:when>
									<c:otherwise>
						${nanoparticleSurfaceForm.map.achar.numberOfDerivedBioAssayData}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<input type="button" onclick="javascript:updateCharts(this.form, 'nanoparticleSurface')" value="Update Plot Charts">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<c:forEach var="achar.derivedBioAssayDataList" items="${nanoparticleSurfaceForm.map.achar.derivedBioAssayDataList}" varStatus="status">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														Chart ${status.index+1}
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
																<bean:define id="fileId" name='characterizationFile${status.index}' property='id' type="java.lang.String" />
																<html:hidden name="achar.derivedBioAssayDataList" property="fileId" value="${fileId}" indexed="true" />
																<a href="nanoparticleSize.do?dispatch=download&amp;fileId=${fileId}"><bean:write name="characterizationFile${status.index}" property="displayName" /></a>
															</logic:present>
															<logic:notPresent name="characterizationFile${status.index}">
												Click on "Load File" button
											</logic:notPresent>
														</c:when>
														<c:otherwise>
						${nanoparticleSurfaceForm.map.achar.derivedBioAssayDataList[status.index].file.name}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
												<td class="rightLabel" colspan="2">
													<input type="button" onclick="javascript:loadFile(this.form, 'nanoparticleSurface', '${nanoparticleSurfaceForm.map.particleName}', ${status.index})" value="Load File">
												</td>
											</tr>
										</tbody>
									</table>
								</c:forEach>
							</td>
						</tr>
				</table>
				<%-- end of Surface characterization specific --%>
				<jsp:include page="bodySharedCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
