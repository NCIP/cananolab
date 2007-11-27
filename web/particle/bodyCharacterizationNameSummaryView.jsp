<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%" align="center">
	<tr>
		<td>
			<h4>
				<br>
				${pageTitle} ${nanoparticleCharacterizationForm.map.charName}
			</h4>
		</td>
		<td align="right" width="15%">
			<a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${helpName}')"
				class="helpText">Help</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			<table width="100%" border="0" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<td colspan="2" align="right">
						<table>
							<tr>
								<c:url var="url"
									value="${nanoparticleCharacterizationForm.map.charName}.do">
									<c:param name="page" value="0" />
									<c:param name="dispatch" value="setupUpdate" />
									<c:param name="particleId" value="${particleId}" />
									<c:param name="characterizationId"
										value="${nanoparticleCharacterizationForm.map.achar.id}" />
								</c:url>
								<c:if test="${canCreateNanoparticle eq 'true'}">
									<td>
										<a href="${url}"><img src="images/icon_edit_23x.gif"
												alt="edit characterization" border="0"> </a>
									</td>
								</c:if>
								<td>
									<a href="#"><img src="images/icon_print_23x.gif"
											alt="print characterization summary" border="0"> </a>
								</td>
								<td>
									<a href="#"><img src="images/icon_excel_23x.gif"
											alt="export characterization summary" border="0"> </a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr class="topBorder">
					<td class="formTitle" colspan="${3+fn:length(datumLabels)}">
						<div align="justify">
							${nanoparticleCharacterizationForm.map.particle.sampleName}
							${nanoparticleCharacterizationForm.map.particle.sampleType} - ${
							nanoparticleCharacterizationForm.map.charName} Characterizations
						</div>
					</td>
				</tr>
				<tr>
					<th class="leftLabel">
						Size View Title
					</th>
					<th class="label">
						Instrument Info
					</th>
					<c:forEach var="label" items="${datumLabels}">
						<th class="label">
							${label}
						</th>
					</c:forEach>
					<th class="rightLabel">
						Characterization File
					</th>
				</tr>
				<c:forEach var="summaryBean" items="nameCharacterizationSummary">
					<tr>
						<td class="leftLabel">
							${summaryBean.charBean.viewTitle}
						</td>
						<td class="label">
							<c:if
								test="${!empty summaryBean.charBean.instrumentConfigBean && !empty summaryBean.charBean.instrumentConfigBean.instrumentBean.type}">						
									${nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.type}-
									${nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.manufacturer}
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
						</td>
						<c:forEach var="label" items="${datumLabels}">
							<td class="label">
								${summaryBean.datumMap[label]}
							</td>
						</c:forEach>
						<td class="rightLabel">
							${summaryBean.charFile.type}
							<br>
							<c:if test="${!empty summaryBean.charFile}">
								<a class="thumbnail" href="#thumb"><img
										src="${nanoparticleCharacterizationForm.map.charName}.do?dispatch=download&amp;fileId=${summaryBean.charFile.id}"
										border="0" width="150"> <span><img
											src="${nanoparticleCharacterizationForm.map.charName}.do?dispatch=download&amp;fileId=${summaryBean.charFile.id}">
										<br /> </span> </a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
		</td>
	</tr>
</table>
