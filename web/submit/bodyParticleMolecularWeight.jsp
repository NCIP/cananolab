<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/nanoparticleMolecularWeight">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Physical Characterization - Molecular Weight
				</h4>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=nano_MW_help')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${nanoparticleMolecularWeightForm.map.particleName} (${nanoparticleMolecularWeightForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="bodySharedCharacterizationSummary.jsp?formName=nanoparticleMolecularWeightForm" />
				<jsp:include page="bodySharedCharacterizationInstrument.jsp?formName=nanoparticleMolecularWeightForm" />
				<%-- molecular weight characterization specific --%>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Molecular Weight Graph
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Number of Graphs</strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="achar.numberOfDerivedBioAssayData" />
									</c:when>
									<c:otherwise>
						${nanoparticleMolecularWeightForm.map.achar.numberOfDerivedBioAssayData}&nbsp;
					</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<input type="button" onclick="javascript:updateCharts(this.form, 'nanoparticleMolecularWeight')" value="Update Graphs">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<logic:iterate name="nanoparticleMolecularWeightForm" property="achar.derivedBioAssayDataList" id="derivedBioAssayData" indexId="chartInd">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														Graph ${chartInd+1}
													</div>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<strong>Graph Type </strong>
												</td>
												<td class="rightLabel" colspan="3">
													<c:choose>
														<c:when test="${canUserSubmit eq 'true'}">
															<html:select property="achar.derivedBioAssayDataList[${chartInd}].type">
																<html:options name="allMolecularWeightDistributionGraphTypes" />
															</html:select>
														</c:when>
														<c:otherwise>
						${nanoparticleMolecularWeightForm.map.achar.derivedBioAssayDataList[chartInd].type}&nbsp;
					</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<jsp:include page="bodySharedCharacterizationFile.jsp?chartInd=${chartInd}&formName=nanoparticleMolecularWeightForm&actionName=nanoparticleMolecularWeight" />
											<tr>
												<td class="leftLabel">
													<strong>Molecular Weight </strong>
												</td>
												<td class="rightLabel" colspan="3">
													<c:choose>
														<c:when test="${canUserSubmit eq 'true'}">
															<html:text property="achar.derivedBioAssayDataList[${chartInd}].datumList[0].value" />
													&nbsp; ${nanoparticleMolecularWeightForm.map.achar.derivedBioAssayDataList[chartInd].datumList[0].valueUnit}
														</c:when>
														<c:otherwise>
						${nanoparticleMolecularWeightForm.map.achar.derivedBioAssayDataList[chartInd].datumList[0].value}${nanoparticleMolecularWeightForm.map.achar.derivedBioAssayDataList[chartInd].datumList[0].valueUnit}&nbsp;
					</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</tbody>
									</table>
									<br>
									</logic:iterate>
							</td>
						</tr>
				</table>
				<%-- end of molecular weight characterization specific --%>
				<jsp:include page="bodySharedCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
