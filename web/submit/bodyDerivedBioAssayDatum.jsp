<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<logic:iterate id="ddata" name="${formName}" property="achar.derivedBioAssayDataList[${param.chartNum}].datumList" indexId="dInd">
	<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
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
					<strong>${dataLabel}</strong>
				</td>
				<td class="rightLabel" colspan="3">

					<c:choose>
						<c:when test="${canUserUpdateParticle eq 'true'}">
							<html:text property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].value" />
 ${ddata.valueUnit}	
														</c:when>
						<c:otherwise>
															${ddata.value} ${ddata.valueUnit}&nbsp;
														</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td class="completeLabel" colspan="4">
					<strong>Is Control?</strong>
					<c:choose>
						<c:when test="${canUserUpdateParticle eq 'true'}">
							<html:select property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].isAControl">
								<html:options name="booleanChoices" />
							</html:select>
						&nbsp;&nbsp;&nbsp;
						<strong>Control Name:</strong>
							<html:text property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].control.name" />
						&nbsp;&nbsp;&nbsp;
						<strong>Control Type:</strong>
							<html:text property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].control.type" />
						</c:when>
						<c:otherwise>
						${ddata.isAControl}&nbsp;
						&nbsp;&nbsp;&nbsp;
						<strong>Control Name:</strong>${ddata.isAControl}&nbsp;
						&nbsp;&nbsp;&nbsp;
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
						<c:when test="${canUserUpdateParticle eq 'true'}">
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
						<c:when test="${canUserUpdateParticle eq 'true'}">
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
											<c:when test="${canUserUpdateParticle eq 'true'}">
												<html:select property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].conditionList[${cInd}].type">
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
											<c:when test="${canUserUpdateParticle eq 'true'}">
												<html:text property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].conditionList[${cInd}].value" />
											</c:when>
											<c:otherwise>
														${cdata.value}&nbsp;
        											</c:otherwise>
										</c:choose>
										&nbsp;&nbsp;&nbsp;
										<c:choose>
											<c:when test="${canUserUpdateParticle eq 'true'}">
												<html:select property="achar.derivedBioAssayDataList[${param.chartNum}].datumList[${dInd}].conditionList[${cInd}].valueUnit">
													<html:options name="allConcentrationUnits" />
												</html:select>
											</c:when>
											<c:otherwise>
														${cdata.valueUnit}&nbsp;
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

