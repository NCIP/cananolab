<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/nanoparticleSolubility">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Physical Characterization - Solubility
				</h4>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=nano_solubility_help')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${nanoparticleSolubilityForm.map.particleName}
					(${nanoparticleSolubilityForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include
					page="bodySharedCharacterizationSummary.jsp?formName=nanoparticleSolubilityForm" />
				<jsp:include
					page="bodySharedCharacterizationInstrument.jsp?formName=nanoparticleSolubilityForm" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="6">
								<div align="justify">
									Solubility Property
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Solvent </strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.solvent" />
									</c:when>
									<c:otherwise>
										${nanoparticleSolubilityForm.map.achar.solvent}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="leftLabel">
								<strong>Is Soluble </strong>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:select property="achar.isSoluble">
											<html:options name="booleanChoices" />
										</html:select>
									</c:when>
									<c:otherwise>
											${nanoparticleSolubilityForm.map.achar.isSoluble}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Critical Concentration</strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.criticalConcentration" />
										<html:select property="achar.criticalConcentrationUnit">
											<html:options name="allConcentrationUnits" />
										</html:select>
									</c:when>
									<c:otherwise>
										${nanoparticleSolubilityForm.map.achar.criticalConcentration}&nbsp;
										${nanoparticleSolubilityForm.map.achar.criticalConcentrationUnit}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
							</td>
						</tr>

					</tbody>
				</table>
				<br />
				<%-- size characterization specific --%>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
								    Characterization File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Number of Files</strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.numberOfDerivedBioAssayData" />
									</c:when>
									<c:otherwise>
										${nanoparticleSolubilityForm.map.achar.numberOfDerivedBioAssayData}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<input type="button"
											onclick="javascript:updateCharts(this.form, 'nanoparticleSolubility')"
											value="Update Files">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<logic:iterate name="nanoparticleSolubilityForm"
									property="achar.derivedBioAssayDataList"
									id="derivedBioAssayData" indexId="chartInd">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3"
										width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														File ${chartInd+1}
													</div>
												</td>
											</tr>
											<jsp:include
												page="bodySharedCharacterizationFile.jsp?chartInd=${chartInd}&formName=nanoparticleSolubilityForm&actionName=nanoparticleSolubility" />
										</tbody>
									</table>
									<br>
								</logic:iterate>
							</td>
						</tr>
				</table>
				<%-- end of size characterization specific --%>
				<jsp:include page="bodySharedCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
