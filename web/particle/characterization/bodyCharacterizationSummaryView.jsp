<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="StyleSheet" type="text/css" href="css/printExport.css">
<script type="text/javascript" src="javascript/printExport.js"></script>
<table width="100%" align="center">
	<tr>
		<td>
			<h4>
				<br>
				${pageTitle} ${param.submitType}
			</h4>
		</td>
		<td align="right" width="20%">
			<j/helpGlossary.jsp/helpGlossary.jsp">
				<jsp:param name="topic" value="char_summary_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
		</td>
	</tr>
	<tr>
		<td colspan="2" align="left">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			<table>
				<tr>
					<td>
						<ul class="pemenu" id="printChara">
							<li class="pelist">
								<a href="#"><img src="images/icon_print_23x.gif" border="0"
										align="middle"> </a>
								<ul>
									<li>
										<a href="javascript:printPage('${printSummaryViewLinkURL}')">Print
											Summary </a>
									</li>
									<li>
										<a
											href="javascript:printPage('${printFullSummaryViewLinkURL}')">Print
											Full Summary </a>
									</li>
								</ul>
							</li>
						</ul>
					</td>
					<td></td>
					<td>
						<c:url var="sumUrl" value="${actionName}.do">
							<c:param name="particleId" value="${particleId}" />
							<c:param name="submitType" value="${param.submitType}" />
							<c:param name="page" value="0" />
							<c:param name="dispatch" value="exportSummary" />
							<c:param name="location" value="${location}" />
						</c:url>
						<c:url var="fullSumUrl" value="${actionName}.do">
							<c:param name="particleId" value="${particleId}" />
							<c:param name="submitType" value="${param.submitType}" />
							<c:param name="page" value="0" />
							<c:param name="dispatch" value="exportFullSummary" />
							<c:param name="location" value="${location}" />
						</c:url>
						<ul class="pemenu" id="exportChara">
							<li class="pelist">
								<a href="#"><img src="images/icon_excel_23x.gif" border="0"
										align="middle"> </a>
								<ul>
									<li>
										<a href="${sumUrl}">Export Summary</a>
									</li>
									<li>
										<a href="${fullSumUrl}">Export Full Summary</a>
									</li>
								</ul>
							</li>
						</ul>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table width="100%" border="0" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<td class="formTitle"
						colspan="${3+fn:length(charSummary.columnLabels)}">
						<div align="justify">
							${fn:toUpperCase(param.location)} ${particleName} - ${
							param.submitType} Characterizations
						</div>
					</td>
				</tr>
				<tr>
					<th class="leftLabel">
						View Title /
						<br>
						Description
					</th>
					<c:forEach var="label" items="${charSummary.columnLabels}">
						<th class="label">
							${label}
						</th>
					</c:forEach>
					<th class="label">
						Characterization File
					</th>
					<th class="rightLabel">
						Instrument Info
					</th>
				</tr>
				<c:forEach var="summaryRow" items="${charSummary.summaryRows}">
					<tr>
						<td class="leftLabel" valign="top" width="15%">
							<c:url var="url" value="${actionName}.do">
								<c:param name="page" value="0" />
								<c:param name="dispatch" value="detailView" />
								<c:param name="particleId" value="${particleId}" />
								<c:param name="dataId"
									value="${summaryRow.charBean.domainChar.id}" />
								<c:param name="dataClassName" value="${summaryRow.charBean.className}" />
								<c:param name="submitType" value="${param.submitType}" />
								<c:param name="location" value="${location}" />
							</c:url>
							<a href="${url}">${summaryRow.charBean.viewTitle}</a>
							<c:if test="${!empty summaryRow.charBean.description}">
								<br>
								<br>${summaryRow.charBean.description}
							</c:if>
						</td>

						<c:forEach var="label" items="${charSummary.columnLabels}">
							<td class="label" valign="top">
								${summaryRow.datumMap[label]}&nbsp;
							</td>
						</c:forEach>
						<td class="label" valign="top">
							<c:if
								test="${!empty summaryRow.derivedBioAssayDataBean && !empty summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.uri}">
								<c:choose>
									<c:when
										test="${summaryRow.derivedBioAssayDataBean.labFileBean.hidden eq 'true' }">
										Private file
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when
												test="${summaryRow.derivedBioAssayDataBean.labFileBean.image eq 'true'}">
												${summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.title}<br>
												<br>
												<a href="#"
													onclick="popImage(event,'${actionName}.do?dispatch=download&amp;fileId=${summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.id}&amp;location=${location}', ${summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.id})"><img
														src="${actionName}.do?dispatch=download&amp;fileId=${summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.id}&amp;location=${location}"
														border="0" width="150"> </a>
											</c:when>
											<c:otherwise>
												<a
													href="${actionName}.do?dispatch=download&amp;fileId=${summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.id}&amp;location=${location}">${summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.title}</a>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</c:if>
							&nbsp;
						</td>
						<td class="RightLabel" valign="top">
							<c:if
								test="${!empty summaryRow.charBean.instrumentConfiguration && !empty summaryRow.charBean.instrumentConfiguration.instrument.type}">						
									${summaryRow.charBean.instrumentConfiguration.instrument.type}-
									${summaryRow.charBean.instrumentConfiguration.instrument.manufacturer}
									&nbsp;
								<c:if
									test="${!empty summaryRow.charBean.instrumentConfiguration.instrument.abbreviation}">
									(${summaryRow.charBean.instrumentConfiguration.instrument.abbreviation})
								</c:if>
								<c:if
									test="${!empty summaryRow.charBean.instrumentConfiguration.description}">
									<br>
									<br>
									${summaryRow.charBean.instrumentConfiguration.description}
								</c:if>
							</c:if>
							&nbsp;
						</td>
					</tr>
				</c:forEach>
			</table>
		</td>
	</tr>
</table>