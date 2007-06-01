<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/nanoparticleShape">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Physical Characterization - Shape
				</h4>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=nano_shape_help')" class="helpText">Help</a>
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
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="6">
								<div align="justify">
									Shape Property
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Type *</strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:select property="shape.type" onchange="javascript:updateOtherField(nanoparticleCharacterizationForm, 'shape.type', 'shape.otherShapeType')">
											<option value=""></option>
											<html:options name="allShapeTypes" />
										</html:select>
										&nbsp;
										<strong> Other </strong>&nbsp;
										
										<c:choose>
											<c:when test="${nanoparticleCharacterizationForm.map.shape.type eq 'Other'}">
												<html:text property="shape.otherShapeType" disabled="false" />
											</c:when>
											<c:otherwise>
												<html:text property="shape.otherShapeType" disabled="true" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										${nanoparticleCharacterizationForm.map.shape.type}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Minimum Dimension </strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="shape.minDimension" />
										${nanoparticleCharacterizationForm.map.shape.minDimensionUnit}&nbsp;
									</c:when>
									<c:otherwise>
										${nanoparticleCharacterizationForm.map.shape.minDimension}&nbsp;
										${nanoparticleCharacterizationForm.map.shape.minDimensionUnit}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="leftLabel">
								<strong>Maximum Dimension </strong>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="shape.maxDimension" />
										${nanoparticleCharacterizationForm.map.shape.maxDimensionUnit}&nbsp;
									</c:when>
									<c:otherwise>
										${nanoparticleCharacterizationForm.map.shape.maxDimension}&nbsp;
										${nanoparticleCharacterizationForm.map.shape.maxDimensionUnit}&nbsp;
									</c:otherwise>
								</c:choose>
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
									Shape Image
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
										${nanoparticleCharacterizationForm.map.achar.numberOfDerivedBioAssayData}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<input type="button" onclick="javascript:updateCharts(this.form, 'nanoparticleShape')" value="Update Images">
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
														Graph ${chartInd+1}
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
																<html:options name="allShapeDistributionGraphTypes" />
															</html:select>
														</c:when>
														<c:otherwise>
						${nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[chartInd].type}&nbsp;
					</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<jsp:include page="/submit/bodySharedCharacterizationFile.jsp?chartInd=${chartInd}&actionName=nanoparticleShape" />
										</tbody>
									</table>
									<br>
								</logic:iterate>
							</td>
						</tr>
				</table>
				<%-- end of size characterization specific --%>
				<br>
				<jsp:include page="/submit/bodySharedCharacterizationCopy.jsp" />
				<jsp:include page="/submit/bodySharedCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
