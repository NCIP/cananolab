<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" align="center">
	<tr>
		<td colspan="2">
			<h4>
				<br>
				${pageTitle} ${submitType}
			</h4>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table width="100%" border="0" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<td class="formTitle" colspan="2">
						${nanoparticleCharacterizationForm.map.particle.sampleName}
						${nanoparticleCharacterizationForm.map.particle.sampleType} - ${
						nanoparticleCharacterizationForm.map.achar.viewTitle} - ${
						nanoparticleCharacterizationForm.map.achar.characterizationSource}
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
					test="${!empty nanoparticleCharacterizationForm.map.achar.protocolFileBean.id}">
					<tr>
						<th class="leftLabel" valign="top">
							Protocol
						</th>
						<td class="rightLabel" valign="top">
							<c:choose>
								<c:when
									test="${nanoparticleCharacterizationForm.map.achar.protocolFileBean.hidden eq 'true'}">
									Private protocol
								</c:when>
								<c:otherwise>
							${nanoparticleCharacterizationForm.map.achar.protocolFileBean.displayName}&nbsp;
							<a
										href="searchProtocol.do?dispatch=download&amp;fileId=${nanoparticleCharacterizationForm.map.achar.protocolFileBean.id}">${nanoparticleCharacterizationForm.map.achar.protocolFileBean.uri}</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
				<c:if
					test="${!empty nanoparticleCharacterizationForm.map.achar.instrumentConfigBean && !empty nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.type}">
					<tr>
						<th class="leftLabel" valign="top">
							Instrument
						</th>
						<td class="rightLabel" valign="top">
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
								Characterization File #${fileInd+1} Description
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
								Characterization File #${fileInd+1}
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
												<img
													src="${nanoparticleCharacterizationForm.map.achar.actionName}.do?dispatch=download&amp;fileId=${derivedBioAssayData.id}"
													border="0">
											</c:when>
											<c:otherwise>
												<a
													href="${nanoparticleCharacterizationForm.map.achar.actionName}.do?dispatch=download&amp;fileId=${derivedBioAssayData.id}">${derivedBioAssayData.uri}</a>
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
