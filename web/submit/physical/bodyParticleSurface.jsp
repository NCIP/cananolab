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
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=nano_surface_help')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${nanoparticleCharacterizationForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">				
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="/submit/bodySharedCharacterizationSummary.jsp" />
				<jsp:include page="/submit/bodySharedCharacterizationInstrument.jsp" />

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
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="surface.surfaceArea" />&nbsp;sq nm
										<!-- <html:select property="surface.surfaceAreaUnit">
											<html:options name="allAreaMeasureUnits" />
										</html:select> -->
									</c:when>
									<c:otherwise>
										${nanoparticleCharacterizationForm.map.surface.surfaceArea}&nbsp;
										${nanoparticleCharacterizationForm.map.surface.surfaceAreaUnit}&nbsp;
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
									<c:when test="${canUserSubmit eq 'true'}">
										<html:select property="surface.isHydrophobic">
											<html:options name="booleanChoices" />
										</html:select>
									</c:when>
									<c:otherwise>
										${nanoparticleCharacterizationForm.map.surface.isHydrophobic}&nbsp;
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
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="surface.charge" />&nbsp;
										<html:select property="surface.chargeUnit">
											<html:options name="allChargeMeasureUnits" />
										</html:select>
									</c:when>
									<c:otherwise>
										${nanoparticleCharacterizationForm.map.surface.charge}&nbsp;
										${nanoparticleCharacterizationForm.map.surface.chargeUnit}&nbsp;
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
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="surface.zetaPotential" />&nbsp;mV
									</c:when>
									<c:otherwise>
										${nanoparticleCharacterizationForm.map.surface.zetaPotential}&nbsp;
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
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="surface.numberOfSurfaceChemistries" />
									</c:when>
									<c:otherwise>
						${nanoparticleCharacterizationForm.map.surface.numberOfSurfaceChemistries}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<input type="button" onclick="javascript:updateSurfaceChemistries(this.form, 'nanoparticleSurface')" value="Update Surface Chemistries">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<c:forEach var="achar.surfaceChemistries" items="${nanoparticleCharacterizationForm.map.surface.surfaceChemistries}" varStatus="status">
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
														<c:when test="${canUserSubmit eq 'true'}">
															<html:text name="achar.surfaceChemistries" indexed="true" property="moleculeName" />
														</c:when>
														<c:otherwise>
						${nanoparticleCharacterizationForm.map.surface.surfaceChemistries[status.index].moleculeName}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
												<td class="label">
													<strong>Number of Molecule </strong>
												</td>
												<td class="rightLabel">
													<c:choose>
														<c:when test="${canUserSubmit eq 'true'}">
															<html:text name="surface.surfaceChemistries" indexed="true" property="numberOfMolecules" /> &nbsp;															
														</c:when>
														<c:otherwise>
															${nanoparticleCharacterizationForm.map.surface.surfaceChemistries[status.index].numberOfMolecules}&nbsp;
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
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.numberOfDerivedBioAssayData" />
									</c:when>
									<c:otherwise>
						${nanoparticleCharacterizationForm.map.achar.numberOfDerivedBioAssayData}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<input type="button" onclick="javascript:updateCharts(this.form, 'nanoparticleSurface')" value="Update Plot Charts">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<logic:iterate name="nanoparticleCharacterizationForm" property="achar.derivedBioAssayDataList" id="derivedBioAssayData" indexId="chartInd">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														Chart ${chartInd+1}
													</div>
												</td>
											</tr>
											<jsp:include page="/submit/bodySharedCharacterizationFile.jsp?chartInd=${chartInd}&actionName=nanoparticleSurface" />

										</tbody>
									</table>
									<br>
								</logic:iterate>
							</td>
						</tr>
				</table>				
				<%-- end of Surface characterization specific --%>
				<br>
				<jsp:include page="/submit/bodySharedCharacterizationCopy.jsp" />
				<jsp:include page="/submit/bodySharedCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
