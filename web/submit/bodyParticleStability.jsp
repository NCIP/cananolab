<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/nanoparticleStability">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Physical Characterization - Stability
				</h4>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=nano_stability_help')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${nanoparticleStabilityForm.map.particleName} (${nanoparticleStabilityForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">				
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="bodySharedCharacterizationSummary.jsp?formName=nanoparticleStabilityForm"/>
				<jsp:include page="bodySharedCharacterizationInstrument.jsp?formName=nanoparticleStabilityForm" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="6">
								<div align="justify">
									Stability Property
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Measurement Type </strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.measurementType" />
									</c:when>
									<c:otherwise>
											${nanoparticleStabilityForm.map.achar.measurementType}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
							</td>
						</tr>

						<tr>
							<td class="leftLabel">
								<strong>Short Term Storage </strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.shortTermStorage" />
										<html:select property="achar.shortTermStorageUnit">
											<html:options name="allTimeUnits" />
										</html:select>
									</c:when>
									<c:otherwise>
										${nanoparticleStabilityForm.map.achar.shortTermStorage}&nbsp;
										${nanoparticleStabilityForm.map.achar.shortTermStorageUnit}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="leftLabel">
								<strong>Long Term Storage </strong>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.longTermStorage" />
										<html:select property="achar.longTermStorageUnit">
											<html:options name="allTimeUnits" />
										</html:select>
									</c:when>
									<c:otherwise>
											${nanoparticleStabilityForm.map.achar.longTermStorage}&nbsp;
											${nanoparticleStabilityForm.map.achar.longTermStorageUnit}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Stress Result </strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.stressResult" />
									</c:when>
									<c:otherwise>
										${nanoparticleStabilityForm.map.achar.stressResult}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="leftLabel">
								<strong>Release Kinetics Description </strong>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.releaseKineticsDescription" />
									</c:when>
									<c:otherwise>
										${nanoparticleStabilityForm.map.achar.releaseKineticsDescription}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Stressor Type</strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:select property="achar.stressor.type">
											<option value=""></option>
											<html:options name="allStressorTypes" />
										</html:select>
										&nbsp;
										<strong> Other </strong>&nbsp;
										<html:text property="achar.stressor.otherType" />
									</c:when>
									<c:otherwise>
										${nanoparticleStabilityForm.map.achar.stressor.type}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="leftLabel">
								<strong>Stressor Value </strong>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.stressor.value" />
										<html:select property="achar.stressor.valueUnit">
											<html:options name="allAreaMeasureUnits" />
										</html:select>
									</c:when>
									<c:otherwise>
											${nanoparticleStabilityForm.map.achar.stressor.value}&nbsp;
											${nanoparticleStabilityForm.map.achar.stressor.valueUnit}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Stressor Description</strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.stressor.description" />
									</c:when>
									<c:otherwise>
										${nanoparticleStabilityForm.map.achar.stressor.description}&nbsp;
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
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Stability Image
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Number of Images</strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.numberOfDerivedBioAssayData" />
									</c:when>
									<c:otherwise>
										${nanoparticleStabilityForm.map.achar.numberOfDerivedBioAssayData}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<input type="button" onclick="javascript:updateCharts(this.form, 'nanoparticleStability')" value="Update Images">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<logic:iterate name="nanoparticleStabilityForm" property="achar.derivedBioAssayDataList" id="derivedBioAssayData" indexId="chartInd">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														Graph ${status.index+1}
													</div>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<strong>Type </strong>
												</td>
												<td class="rightLabel" colspan="3">
													<c:choose>
														<c:when test="${canUserSubmit eq 'true'}">
															<html:select property="achar.derivedBioAssayDataList[${chartInd}].type">
																<html:options name="allStabilityDistributionGraphTypes" />
															</html:select>
														</c:when>
														<c:otherwise>
						${nanoparticleStabilityForm.map.achar.derivedBioAssayDataList[status.index].type}&nbsp;
					</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<jsp:include page="bodySharedCharacterizationFile.jsp?chartInd=${chartInd}&formName=nanoparticleStabilityForm&actionName=nanoparticleStability" />

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
