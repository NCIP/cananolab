<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
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
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${helpName}')"
				class="helpText">Help</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			<table align="right">
				<tr>
					<c:url var="url"
						value="${nanoparticleCharacterizationForm.map.achar.actionName}.do">
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
	<tr>
		<td colspan="2">
			<table width="100%" border="0" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<td class="formTitle" colspan="2">
						<div align="justify">
							${nanoparticleCharacterizationForm.map.particle.sampleName}
							${nanoparticleCharacterizationForm.map.particle.sampleType} - ${
							nanoparticleCharacterizationForm.map.achar.viewTitle} - ${
							nanoparticleCharacterizationForm.map.achar.characterizationSource}
						</div>
					</td>
				</tr>
				<c:if
					test="${!empty nanoparticleCharacterizationForm.map.achar.description}">
					<tr>
						<th class="leftLabel" valign="top">
							Description
						</th>
						<td class="rightLabel">
							${nanoparticleCharacterizationForm.map.achar.description}
						</td>
					</tr>
				</c:if>
				<c:if
					test="${!empty nanoparticleCharacterizationForm.map.achar.instrumentConfigBean && !empty nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.type}">
					<tr>
						<th class="leftLabel" valign="top">
							Instrument
						</th>
						<td class="rightLabel">
							${nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.type}-
							${nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.manufacturer}
							&nbsp;
							<c:if
								test="${!empty nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.abbreviation}">
							(${nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.abbreviation})
							</c:if>
							<c:if
								test="${!empty nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.description}">
								<br>
								<br>
							${nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.description}
							</c:if>
						</td>
					</tr>
				</c:if>
				<logic:iterate name="nanoparticleCharacterizationForm"
					property="achar.derivedBioAssayDataList" id="derivedBioAssayData"
					indexId="fileInd">
					<c:if test="${!empty derivedBioAssayData.description}">
						<tr>
							<th class="leftLabel" valign="top">
								Characterization File # ${fileInd+1} Description
							</th>
							<td class="rightLabel">
								${derivedBioAssayData.description}&nbsp;
							</td>
						</tr>
					</c:if>
					<c:if
						test="${!empty derivedBioAssayData && !empty derivedBioAssayData.uri}">
						<tr>
							<th class="leftLabel" valign="top">
								Characterization File # ${fileInd+1}
							</th>
							<td class="rightLabel">
								${derivedBioAssayData.type}
								<br>
								<a class="thumbnail" href="#thumb"><img
										src="${nanoparticleCharacterizationForm.map.achar.actionName}.do?dispatch=download&amp;fileId=${derivedBioAssayData.id}"
										border="0" width="150"> <span><img
											src="${nanoparticleCharacterizationForm.map.achar.actionName}.do?dispatch=download&amp;fileId=${derivedBioAssayData.id}">
										<br /> </span> </a>
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
										<logic:iterate id="datum"
											name="nanoparticleCharacterizationForm"
											property="achar.derivedBioAssayDataList[${fileInd}].datumList"
											indexId="datumInd">
											<th class="whiteBorderLessLabel">
												${datum.name}
												<c:if test="${!empty datum.unit}">(${datum.unit})</c:if>
											</th>
										</logic:iterate>
									</tr>
									<tr>
										<logic:iterate id="datum"
											name="nanoparticleCharacterizationForm"
											property="achar.derivedBioAssayDataList[${fileInd}].datumList"
											indexId="datumInd">
											<td class="whiteBorderLessLabel">
												${datum.value}
											</td>
										</logic:iterate>
									</tr>
								</table>
							</th>
						</tr>
					</c:if>
				</logic:iterate>
			</table>
		</td>
	</tr>
</table>
