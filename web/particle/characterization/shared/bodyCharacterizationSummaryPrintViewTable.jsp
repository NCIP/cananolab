<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" align="center">
	<tr>
		<td colspan="2">
			<table width="100%" border="1" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<th class="formTitle"
						colspan="${3+fn:length(charSummary.columnLabels)}" align="center">
						${particleName} - ${ param.submitType} Characterizations
					</th>
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
					<th class="rightLabel">
						Characterization File / Instrument Info
					</th>
				</tr>
				<c:forEach var="summaryRow" items="${charSummary.summaryRows}">
					<tr>
						<td class="leftLabel" valign="top" width="15%">
							${summaryRow.charBean.viewTitle}
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

						<td class="RightLabel" valign="top">
							<c:if
								test="${!empty summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.type}">
								${summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.type}
								<br>
							</c:if>
							<c:if
								test="${!empty summaryRow.derivedBioAssayDataBean.labFileBean.domainFile && !empty summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.uri}">
								<c:choose>
									<c:when
										test="${summaryRow.derivedBioAssayDataBean.labFileBean.hidden eq 'true' }">
										Private file
									</c:when>
									<c:otherwise>
										${summaryRow.derivedBioAssayDataBean.labFileBean.domainFile.title}
									</c:otherwise>
								</c:choose>
								<br>
								<br>
							</c:if>
							<c:if
								test="${!empty summaryRow.charBean.instrumentConfiguration && !empty summaryRow.charBean.instrumentConfiguration.instrument.type}">	${summaryRow.charBean.instrumentConfiguration.instrument.type}-
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

