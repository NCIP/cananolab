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
				${pageTitle} ${submitType}
			</h4>
		</td>
		<td align="right" width="15%">
			<a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${characterizationForm.map.charSummary.className}_summary_view_help')"
				class="helpText">Help</a>
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
										<a href="javascript:printPage('${printFullSummaryViewLinkURL}')">Print
											Full Summary </a>
									</li>
								</ul>
							</li>
						</ul>
					</td>
					<td></td>
					<td>
						<c:url var="sumUrl"
							value="${characterizationForm.map.charSummary.actionName}.do">
							<c:param name="particleId" value="${particleId}" />
							<c:param name="submitType" value="${submitType}" />
							<c:param name="page" value="0" />
							<c:param name="dispatch" value="exportSummary" />
						</c:url>
						<c:url var="fullSumUrl"
							value="${characterizationForm.map.charSummary.actionName}.do">
							<c:param name="particleId" value="${particleId}" />
							<c:param name="submitType" value="${submitType}" />
							<c:param name="page" value="0" />
							<c:param name="dispatch" value="exportFullSummary" />
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
						colspan="${3+fn:length(characterizationForm.map.charSummary.columnLabels)}">
						<div align="justify">
							${characterizationForm.map.particle.sampleName}
							${characterizationForm.map.particle.sampleType} - ${
							submitType} Characterizations
						</div>
					</td>
				</tr>
				<tr>
					<th class="leftLabel">
						View Title /
						<br>
						Description
					</th>
					<c:forEach var="label" items="${characterizationForm.map.charSummary.columnLabels}">
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
				<c:forEach var="summaryRow"
					items="${characterizationForm.map.charSummary.summaryRows}">
					<tr>
						<td class="leftLabel" valign="top" width="15%">
							<c:url var="url"
								value="${characterizationForm.map.charSummary.actionName}.do">
								<c:param name="page" value="0" />
								<c:param name="dispatch" value="detailView" />
								<c:param name="particleId" value="${particleId}" />
								<c:param name="characterizationId"
									value="${summaryRow.charBean.id}" />
								<c:param name="submitType" value="${submitType}" />
							</c:url>
							<a href="${url}">${summaryRow.charBean.viewTitle}</a>
							<c:if test="${!empty summaryRow.charBean.description}">
								<br>
								<br>${summaryRow.charBean.description}
							</c:if>
						</td>

						<c:forEach var="label"
							items="${characterizationForm.map.charSummary.columnLabels}">
							<td class="label" valign="top">
								${summaryRow.datumMap[label]}&nbsp;
							</td>
						</c:forEach>
						<td class="label" valign="top">
							<c:if
								test="${!empty summaryRow.charFile && !empty summaryRow.charFile.uri}">
								<c:choose>
									<c:when test="${summaryRow.charFile.hidden eq 'true' }">
										Private file
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${summaryRow.charFile.image eq 'true'}">
												${summaryRow.charFile.title}<br>
												<br>
												<a href="#"
													onclick="popImage(event,'${characterizationForm.map.charSummary.actionName}.do?dispatch=download&amp;fileId=${summaryRow.charFile.id}', ${summaryRow.charFile.id})"><img
														src="${characterizationForm.map.charSummary.actionName}.do?dispatch=download&amp;fileId=${summaryRow.charFile.id}"
														border="0" width="150"> </a>
											</c:when>
											<c:otherwise>
												<a
													href="${characterizationForm.map.charSummary.actionName}.do?dispatch=download&amp;fileId=${summaryRow.charFile.id}">${summaryRow.charFile.title}</a>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</c:if>
							&nbsp;
						</td>
						<td class="RightLabel" valign="top">
							<c:if
								test="${!empty summaryRow.charBean.instrumentConfigBean && !empty summaryRow.charBean.instrumentConfigBean.instrumentBean.type}">						
									${summaryRow.charBean.instrumentConfigBean.instrumentBean.type}-
									${summaryRow.charBean.instrumentConfigBean.instrumentBean.manufacturer}
									&nbsp;
								<c:if
									test="${!empty summaryRow.charBean.instrumentConfigBean.instrumentBean.abbreviation}">
									(${summaryRow.charBean.instrumentConfigBean.instrumentBean.abbreviation})
								</c:if>
								<c:if
									test="${!empty summaryRow.charBean.instrumentConfigBean.description}">
									<br>
									<br>
									${summaryRow.charBean.instrumentConfigBean.description}
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


