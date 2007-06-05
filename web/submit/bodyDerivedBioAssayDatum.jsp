<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<logic:iterate id="ddata" name="nanoparticleCharacterizationForm" property="achar.derivedBioAssayDataList[${param.chartNum}].datumList" indexId="dInd">
	<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0" rules="none">
		<tbody>
			<tr class="topBorder">
				<td class="formSubTitle" colspan="4">
					<div align="justify">
						Data Point ${dInd+1}
					</div>
				</td>
			</tr>
			<tr>
				<td class="leftLabel">
					<strong>${dataLabel} *</strong>
				</td>
				<td class="rightLabel" colspan="3">
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<html:text property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].value" />&nbsp;
 							${dataUnit}	
						</c:when>
						<c:otherwise>
							${ddata.value} ${dataUnit}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td class="leftLabel">
					<strong>Is Control?</strong>&nbsp;&nbsp;
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<html:select property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].isAControl">
								<html:options name="booleanChoices" />
							</html:select>
						</c:when>
						<c:otherwise>
							${ddata.isAControl}
						</c:otherwise>
					</c:choose>
				</td>
				<td class="label">
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<strong>Control Name:</strong>
							<html:text property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].control.name" />
						</c:when>
						<c:otherwise>
							<strong>Control Name:</strong>${ddata.isAControl}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="rightLabel" colspan="2">
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<strong>Control Type:</strong>
							<html:select property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].control.type">
								<html:options name="allControlTypes" />
							</html:select>
						</c:when>
						<c:otherwise>
							<strong>Type:</strong>${ddata.isAControl}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td class="leftLabel">
					<strong>Number of Conditions</strong>
				</td>
				<td class="label">
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<html:text property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].numberOfConditions" />
						</c:when>
						<c:otherwise>
							${ddata.numberOfConditions}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="rightLabel" colspan="2">
					&nbsp;
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<input type="button" onclick="javascript:updateConditions(this.form, '${actionName}', ${param.chartNum}, ${dInd})" value="Update Conditions">
						</c:when>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td class="completeLabel" colspan="4">
					<logic:iterate id="cdata" name="ddata" property="conditionList" indexId="cInd">
						<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
							<tbody>
								<tr class="topBorder">
									<td class="formSubTitle" colspan="4">
										<div align="justify">
											Condition ${cInd+1}
										</div>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Condition Type:</strong>
									</td>
									<td class="label">
										<c:choose>
											<c:when test="${canUserSubmit eq 'true'}">
												<html:select property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].conditionList[${cInd}].type"
													onchange="javascript:doubleDropdownWithNestedProperties(this.form, 'achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].conditionList[${cInd}].type', 'achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].conditionList[${cInd}].unit', conditionTypeUnits)">
													<option value=""></option>
													<html:options name="allConditionTypes" />
												</html:select>
											</c:when>
											<c:otherwise>
												${cdata.type}&nbsp;
											</c:otherwise>
										</c:choose>
									</td>
									<td class="label">
										<strong>Condition Value:</strong>
									</td>
									<td class="rightLabel">
										<c:choose>
											<c:when test="${canUserSubmit eq 'true'}">
												<html:text property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].conditionList[${cInd}].value" />
											</c:when>
											<c:otherwise>
												${cdata.value}&nbsp;
        									</c:otherwise>
										</c:choose>
										&nbsp;&nbsp;&nbsp;
										<c:choose>
											<c:when test="${canUserSubmit eq 'true'}">
												<html:select property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].conditionList[${cInd}].unit">
													<option value=""></option>
													<option value="${this.form.map.achar.derivedBioAssayDataList[param.chartNum].datumList[dInd].conditionList[cInd].unit}" selected>
														${cdata.unit}
													</option>
												</html:select>
											</c:when>
											<c:otherwise>
												${cdata.unit}&nbsp;
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
		</tbody>
	</table>
	<br>
</logic:iterate>
