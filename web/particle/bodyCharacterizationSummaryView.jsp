<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=nano_${nanoparticleCharacterizationForm.map.achar.actionName}_help')"
				class="helpText">Help</a>
		</td>
	</tr>
	<tr>
		<td colspan="2" align="right">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			<table>
				<tr>
					<td>
						<a href="javascript:printPage('${printLinkURL}')"><img
								src="images/icon_print_23x.gif"
								alt="print characterization summary" border="0"> </a>
					</td>
					<td>
						<a href="javascript:printPage('${printAllLinkURL}')"><img
								src="images/icon_print_23x.gif"
								alt="print full characterization summary" border="0"> </a>
					</td>
					<td>
						<c:url var="sumUrl"
							value="${nanoparticleCharacterizationForm.map.achar.actionName}.do">
							<c:param name="particleId" value="${particleId}" />
							<c:param name="submitType" value="${submitType}" />
							<c:param name="page" value="0" />
							<c:param name="dispatch" value="exportSummary" />
						</c:url>
						<a href="${sumUrl}"><img src="images/icon_excel_23x.gif"
								alt="export characterization summary" border="0"> </a>
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
					<td class="formTitle" colspan="${3+fn:length(datumLabels)}">
						<div align="justify">
							${nanoparticleCharacterizationForm.map.particle.sampleName}
							${nanoparticleCharacterizationForm.map.particle.sampleType} - ${
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
					<c:forEach var="label" items="${datumLabels}">
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
				<c:forEach var="summaryBean" items="${summaryViewBeans}">
					<tr>
						<td class="leftLabel" valign="top" width="15%">
							<c:url var="url"
								value="${nanoparticleCharacterizationForm.map.achar.actionName}.do">
								<c:param name="page" value="0" />
								<c:param name="dispatch" value="detailView" />
								<c:param name="particleId" value="${particleId}" />
								<c:param name="characterizationId"
									value="${summaryBean.charBean.id}" />
								<c:param name="submitType" value="${submitType}" />
							</c:url>
							<a href="${url}">${summaryBean.charBean.viewTitle}</a>
							<c:if test="${!empty summaryBean.charBean.description}">
								<br>
								<br>${summaryBean.charBean.description}
							</c:if>
						</td>

						<c:forEach var="label" items="${datumLabels}">
							<td class="label" valign="top">
								${summaryBean.datumMap[label]}&nbsp;
							</td>
						</c:forEach>
						<td class="label" valign="top">
							<c:if
								test="${!empty summaryBean.charFile && !empty summaryBean.charFile.uri}">
								<c:choose>
									<c:when test="${summaryBean.charFile.hidden eq 'true' }">
										Private file
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${summaryBean.charFile.image eq 'true'}">
												${summaryBean.charFile.title}<br>
												<br>
												<a href="#"
													onclick="popImage(event,'${nanoparticleCharacterizationForm.map.achar.actionName}.do?dispatch=download&amp;fileId=${summaryBean.charFile.id}', ${summaryBean.charFile.id})"><img
														src="${nanoparticleCharacterizationForm.map.achar.actionName}.do?dispatch=download&amp;fileId=${summaryBean.charFile.id}"
														border="0" width="150"> </a>
											</c:when>
											<c:otherwise>
												<a
													href="${nanoparticleCharacterizationForm.map.achar.actionName}.do?dispatch=download&amp;fileId=${summaryBean.charFile.id}">${summaryBean.charFile.title}</a>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</c:if>
							&nbsp;
						</td>
						<td class="RightLabel" valign="top">
							<c:if
								test="${!empty summaryBean.charBean.instrumentConfigBean && !empty summaryBean.charBean.instrumentConfigBean.instrumentBean.type}">						
									${summaryBean.charBean.instrumentConfigBean.instrumentBean.type}-
									${summaryBean.charBean.instrumentConfigBean.instrumentBean.manufacturer}
									&nbsp;
								<c:if
									test="${!empty summaryBean.charBean.instrumentConfigBean.instrumentBean.abbreviation}">
									(${summaryBean.charBean.instrumentConfigBean.instrumentBean.abbreviation})
								</c:if>
								<c:if
									test="${!empty summaryBean.charBean.instrumentConfigBean.description}">
									<br>
									<br>
									${summaryBean.charBean.instrumentConfigBean.description}
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


