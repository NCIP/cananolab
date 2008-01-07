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
		<c:forEach var="charBean" items="${nanoparticleCharacterizationForm.map.charSummary.charBeans}">
			<table width="100%" border="1" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<th class="formTitle" colspan="2" align="center">
						${nanoparticleCharacterizationForm.map.particle.sampleName}
						${nanoparticleCharacterizationForm.map.particle.sampleType} -
						${charBean.viewTitle} - ${ charBean.characterizationSource}
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
				<c:if test="${!empty charBean.protocolFileBean.id}">
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
${charBean.protocolFileBean.uri}
									</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
				<c:if
					test="${!empty charBean.instrumentConfigBean && !empty charBean.instrumentConfigBean.instrumentBean.type}">
					<tr>
						<th class="leftLabel" valign="top">
							Instrument
						</th>
						<td class="rightLabel" valign="top">
							${charBean.instrumentConfigBean.instrumentBean.type}-
							${charBean.instrumentConfigBean.instrumentBean.manufacturer}
							&nbsp;
							<c:if
								test="${!empty charBean.instrumentConfigBean.instrumentBean.abbreviation}">
							(${charBean.instrumentConfigBean.instrumentBean.abbreviation})
							</c:if>
							<c:if test="${!empty charBean.instrumentConfigBean.description}">
								<br>
								<br>
							${charBean.instrumentConfigBean.description}
							</c:if>
						</td>
					</tr>
				</c:if>
				<c:forEach var="derivedBioAssayData"
					items="${charBean.derivedBioAssayDataList}" varStatus="fileInd">
					<c:if test="${!empty derivedBioAssayData.description}">
						<tr>
							<th class="leftLabel" valign="top">
								Characterization File #${fileInd.index+1} Description
							</th>
							<td class="rightLabel" valign="top">
								${derivedBioAssayData.description}&nbsp;
							</td>
						</tr>
					</c:if>
					<c:if
						test="${!empty derivedBioAssayData && !empty derivedBioAssayData.uri}">
						<tr>
							<th class="leftLabel" valign="top">
								Characterization File #${fileInd.index+1}
							</th>
							<td class="rightLabel" valign="top">
								<c:if test="${!empty derivedBioAssayData.type}">
								${derivedBioAssayData.type}
								<br>
								</c:if>
								<c:choose>
									<c:when test="${derivedBioAssayData.hidden eq 'true'}">
									Private file
								</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${derivedBioAssayData.image eq 'true'}">
											<img src="${nanoparticleCharacterizationForm.map.charSummary.actionName}.do?dispatch=download&amp;fileId=${derivedBioAssayData.id}"
															border="0">
											
											</c:when>
											<c:otherwise>${derivedBioAssayData.title}
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
								Characterization Derived Data #1
								<br>
								<br>
								<table border="1" borderColor="#CCCCCC" cellpadding="3"
									cellspacing="0">
									<tr>
										<c:forEach var="datum"
											items="${derivedBioAssayData.datumList}">
											<th class="whiteBorderLessLabel">
												${datum.name}
												<c:if test="${!empty datum.unit}">(${datum.unit})</c:if>
											</th>
										</c:forEach>
									</tr>
									<tr>
										<c:forEach var="datum"
											items="${derivedBioAssayData.datumList}">
											<td class="whiteBorderLessLabel">
												${datum.value}
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


