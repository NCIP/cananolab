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
					<th class="formTitle" colspan="${3+fn:length(datumLabels)}"
						align="center">
						${nanoparticleCharacterizationForm.map.particle.sampleName}
						${nanoparticleCharacterizationForm.map.particle.sampleType} - ${
						submitType} Characterizations
					</th>
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
							${summaryBean.charBean.viewTitle}
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
							<c:if test="${!empty summaryBean.charFile.type}">
							${summaryBean.charFile.type}
							<br>
							</c:if>
							<c:if
								test="${!empty summaryBean.charFile && !empty summaryBean.charFile.uri}">
								<c:choose>
									<c:when test="${summaryBean.charFile.hidden eq 'true' }">
										Private file
									</c:when>
									<c:otherwise>
										${summaryBean.charFile.title}
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

