<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/caLab.css">
		<script type="text/javascript" src="javascript/script.js"></script>
	</head>
	<body onload="window.print();self.close()">
		<jsp:include
			page="shared/bodyCharacterizationSummaryPrintViewTable.jsp" />
		<p style="page-break-before: always">
			<c:forEach var="charBean" items="${charSummary.charBeans}">
				<table width="100%" border="1" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<th class="formTitle" colspan="2" align="center">
							${particleName} - ${charBean.viewTitle} - ${
							charBean.characterizationSource}
						</th>
					</tr>
					<c:if test="${!empty charBean.description}">
						<tr>
							<th class="leftLabel" valign="top">
								Description
							</th>
							<td class="rightLabel">
								${charBean.description}
							</td>
						</tr>
					</c:if>
					<c:if test="${!empty charBean.protocolFileBean.domainFile.id}">
						<tr>
							<th class="leftLabel" valign="top">
								Protocol
							</th>
							<td class="rightLabel" valign="top">
								<c:choose>
									<c:when test="${charBean.protocolFileBean.hidden eq 'true'}">
									Private protocol
								</c:when>
									<c:otherwise>
							${charBean.protocolFileBean.displayName}&nbsp;
${charBean.protocolFileBean.domainFile.uri}
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:if>
					<c:if
						test="${!empty charBean.instrumentConfiguration && !empty charBean.instrumentConfiguration.instrument.type}">
						<tr>
							<th class="leftLabel" valign="top">
								Instrument
							</th>
							<td class="rightLabel" valign="top">
								${charBean.instrumentConfiguration.instrument.type}-
								${charBean.instrumentConfiguration.instrument.manufacturer}
								&nbsp;
								<c:if
									test="${!empty charBean.instrumentConfiguration.instrument.abbreviation}">
							(${charBean.instrumentConfiguration.instrument.abbreviation})
							</c:if>
								<c:if
									test="${!empty charBean.instrumentConfiguration.description}">
									<br>
									<br>
							${charBean.instrumentConfiguration.description}
							</c:if>
							</td>
						</tr>
					</c:if>
					<c:forEach var="derivedBioAssayData"
						items="${charBean.derivedBioAssayDataList}" varStatus="fileInd">
						<c:if
							test="${!empty derivedBioAssayData.labFileBean.domainFile.description 
								&& derivedBioAssayData.labFileBean.hidden ne 'true'}">
							<tr>
								<th class="leftLabel" valign="top">
									Characterization File #${fileInd.index+1} Description
								</th>
								<td class="rightLabel" valign="top">
									${derivedBioAssayData.labFileBean.domainFile.description}&nbsp;
								</td>
							</tr>
						</c:if>
						<c:if
							test="${!empty derivedBioAssayData && !empty derivedBioAssayData.labFileBean.domainFile.uri}">
							<tr>
								<th class="leftLabel" valign="top">
									Characterization File #${fileInd.index+1}
								</th>
								<td class="rightLabel" valign="top">
									<c:if
										test="${!empty derivedBioAssayData.labFileBean.domainFile.type}">
								${derivedBioAssayData.labFileBean.domainFile.type}
								<br>
									</c:if>
									<c:choose>
										<c:when
											test="${derivedBioAssayData.labFileBean.hidden eq 'true'}">
									Private file
								</c:when>
										<c:otherwise>
											<c:choose>
												<c:when
													test="${derivedBioAssayData.labFileBean.image eq 'true'}">
													<img
														src="${actionName}.do?dispatch=download&amp;fileId=${derivedBioAssayData.labFileBean.domainFile.id}&amp;location=${location}"
														border="0">

												</c:when>
												<c:otherwise>${derivedBioAssayData.labFileBean.domainFile.title}
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:if>
						<c:if test="${!empty derivedBioAssayData.datumList}">
							<tr>
								<th class="completeLabel" align="left" colspan="2">
									Characterization Derived Data #${fileInd+1}
									<br>
									<br>
									<table border="1" borderColor="#CCCCCC" cellpadding="3"
										cellspacing="0">
										<tr>
											<c:forEach var="datum"
												items="${derivedBioAssayData.datumList}">
												<th class="whiteBorderLessLabel">
													${datum.domainDerivedDatum.name}
													<c:if test="${!empty datum.domainDerivedDatum.valueUnit}">(${datum.domainDerivedDatum.valueUnit})</c:if>
												</th>
											</c:forEach>
										</tr>
										<tr>
											<c:forEach var="datum"
												items="${derivedBioAssayData.datumList}">
												<td class="whiteBorderLessLabel">
													${datum.valueStr}
												</td>
											</c:forEach>
										</tr>
									</table>
								</th>
							</tr>
						</c:if>
					</c:forEach>
				</table>
				<p style="page-break-before: always">
			</c:forEach>
	</body>
</html>


