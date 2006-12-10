<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/${actionName}">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					${pageTitle}
				</h4>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_nanoparticle')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					<bean:write name="${formName}" property="particleName" />
					<bean:write name="${formName}" property="particleType" />
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="bodySharedCharacterizationSummary.jsp" />
				<jsp:include page="bodySharedCharacterizationInstrument.jsp" />
				<logic:present name="detailPage">
					<jsp:include page="${detailPage}" />
				</logic:present>
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
										<bean:write name="${formName}" property="achar.numberOfDerivedBioAssayData" />&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<input type="button" onclick="javascript:updateCharts(this.form, '${actionName}')" value="Update Charts">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<logic:iterate name="${formName}" property="achar.derivedBioAssayDataList" id="derivedBioAssayData" indexId="chartInd">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														Histogram/Bar Chart ${chartInd+1}
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
															<logic:present name="characterizationFile${chartInd}">
																<bean:define id="fileId" name='characterizationFile${chartInd}' property='id' type="java.lang.String" />
																<html:hidden name="achar.derivedBioAssayDataList" property="fileId" value="${fileId}" indexed="true" />
																<a href="${actionName}.do?dispatch=download&amp;fileId=${fileId}"><bean:write name="characterizationFile${chartInd}" property="name" /></a>
															</logic:present>
															<logic:notPresent name="characterizationFile${chartInd}">
																Click on "Load File" button
															</logic:notPresent>
														</c:when>
														<c:otherwise>
															${derivedBioAssayData.file.name}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
												<td class="rightLabel" colspan="2">
													<input type="button" onclick="javascript:loadFile(this.form, '${actionName}', '<bean:write name="${formName}" property="particleName"/>', ${chartInd})" value="Load File">
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<strong>Number of Data Points</strong>
												</td>
												<td class="label">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:text property="achar.derivedBioAssayDataList[${chartInd}].numberOfDataPoints" />
														</c:when>
														<c:otherwise>
															${derivedBioAssayData.numberOfDataPoints}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
												<td class="rightLabel" colspan="2">
													&nbsp;
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<input type="button" onclick="javascript:updateChartDataPoints(this.form, '${actionName}', ${chartInd})" value="Update Data Points">
														</c:when>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="completeLabel" colspan="4">
													<jsp:include page="bodyDerivedBioAssayDatum.jsp">
														<jsp:param name="chartNum" value="${chartInd}" />
													</jsp:include>
												</td>
											</tr>
										</tbody>
									</table>
									<br>
								</logic:iterate>
							</td>
						</tr>
				</table>
				<jsp:include page="bodySharedCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>

<script language="JavaScript">
<!--//
  /* populate a hashtable containing condition type units */
  var conditionTypeUnits=new Array();    
  <c:forEach var="item" items="${allConditionTypeUnits}">
    var conditionUnits=new Array();
    <c:forEach var="conditionUnit" items="${allConditionTypeUnits[item.key]}" varStatus="count">  		
  		conditionUnits[${count.index}]='${conditionUnit}';
    </c:forEach>
    conditionTypeUnits['${item.key}'] = conditionUnits;
  </c:forEach> 
//-->
</script>
<script language="JavaScript">
