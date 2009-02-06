<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table width="100%" align="center">
	<tr>
		<td>
			<h4>
				<br>
				${pageTitle} ${param.submitType}
			</h4>
		</td>
		<td align="right" width="20%">
			<jsp:include page="/helpGlossary.jsp">
				<jsp:param name="topic" value="char_details_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table width="100%" border="0" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<td class="formTitle" colspan="2">
						<table width="100%">
							<tr>
								<td class="formTitle" width="100%">
									${fn:toUpperCase(param.location)} ${particleName}
								</td>
								<td align="right" class="formTitle">
									<c:url var="url" value="${actionName}.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="setupUpdate" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="dataId"
											value="${characterizationForm.map.achar.domainChar.id}" />
										<c:param name="submitType" value="${param.submitType}" />
										<c:param name="location" value="${location}" />
									</c:url>
									<c:if
										test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
										<td>
											<a href="${url}"><img src="images/icon_edit_23x.gif"
													alt="edit characterization" title="edit characterization"
													border="0"> </a>
										</td>
									</c:if>
								<td>
									<a href="javascript:printPage('${printDetailViewLinkURL}')"><img
											src="images/icon_print_23x.gif"
											alt="print characterization detail"
											title="print characterization detail" border="0"> </a>
								</td>
								<td>
									<c:url var="exportUrl" value="${actionName}.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="exportDetail" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="dataId"
											value="${characterizationForm.map.achar.domainChar.id}" />
										<c:param name="dataClassName" value="${param.dataClassName }" />
										<c:param name="submitType" value="${param.submitType}" />
										<c:param name="location" value="${location}" />
									</c:url>
									<a href="${exportUrl}"><img src="images/icon_excel_23x.gif"
											alt="export characterization detail"
											title="export characterization detail" border="0"> </a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						View Title - Characterization Source
					</th>
					<td class="rightLabel">
						${ characterizationForm.map.achar.viewTitle} - ${
						characterizationForm.map.achar.pocBean.displayName}
					</td>
				</tr>
				<c:if test="${!empty characterizationForm.map.achar.description}">
					<tr>
						<th class="leftLabel" valign="top">
							Description
						</th>
						<td class="rightLabel">
							${characterizationForm.map.achar.description}&nbsp;
						</td>
					</tr>
				</c:if>
				<c:if
					test="${!empty characterizationForm.map.achar.protocolFileBean.domainFile.id}">
					<tr>
						<th class="leftLabel" valign="top">
							Protocol
						</th>
						<td class="rightLabel" valign="top">
							<c:choose>
								<c:when
									test="${characterizationForm.map.achar.protocolFileBean.hidden eq 'true'}">
									Private protocol
								</c:when>
								<c:otherwise>
							${characterizationForm.map.achar.protocolFileBean.displayName}&nbsp;
							<a
										href="searchProtocol.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.protocolFileBean.domainFile.id}&amp;location=${location}">${characterizationForm.map.achar.protocolFileBean.domainFile.uri}</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
				<c:if
					test="${!empty characterizationForm.map.achar.experimentConfigs }">
					<tr>
						<th class="leftLabel" valign="top">
							Technique and Instrument
						</th>
						<td class="rightLabel" valign="top">
							<c:forEach var="experimentConfig"
								items="${characterizationForm.map.achar.experimentConfigs}">
								${experimentConfig.displayDetailString}<br>
							</c:forEach>&nbsp;
						</td>
					</tr>
				</c:if>
				<%--
				<logic:iterate name="characterizationForm"
					property="achar.derivedBioAssayDataList" id="derivedBioAssayData"
					indexId="fileInd">
					<c:if
						test="${!empty derivedBioAssayData.fileBean.domainFile.description
							&& derivedBioAssayData.fileBean.hidden ne 'true'}">
						<tr>
							<th class="leftLabel" valign="top">
								Characterization File #${fileInd+1} Description
							</th>
							<td class="rightLabel" valign="top">
								${derivedBioAssayData.fileBean.domainFile.description}&nbsp;
							</td>
						</tr>
					</c:if>
					<c:if
						test="${!empty derivedBioAssayData && !empty derivedBioAssayData.fileBean.domainFile.uri}">
						<tr>
							<th class="leftLabel" valign="top">
								Characterization File #${fileInd+1}
							</th>
							<td class="rightLabel" valign="top">
								<c:choose>
									<c:when test="${derivedBioAssayData.fileBean.hidden eq 'true'}">
									Private file
								</c:when>
									<c:otherwise>
										<c:choose>
											<c:when
												test="${derivedBioAssayData.fileBean.image eq 'true'}">
 												${derivedBioAssayData.fileBean.domainFile.title}<br>
												<br>
												<a href="#"
													onclick="popImage(event, '${actionName}.do?dispatch=download&amp;fileId=${derivedBioAssayData.fileBean.domainFile.id}&amp;location=${location}', ${derivedBioAssayData.fileBean.domainFile.id}, 100, 100)"><img
														src="${actionName}.do?dispatch=download&amp;fileId=${derivedBioAssayData.fileBean.domainFile.id}&amp;location=${location}"
														border="0" width="150"> </a>
											</c:when>
											<c:otherwise>
												<a
													href="${actionName}.do?dispatch=download&amp;fileId=${derivedBioAssayData.fileBean.domainFile.id}&amp;location=${location}"
													target="${derivedBioAssayData.fileBean.urlTarget}">${derivedBioAssayData.fileBean.domainFile.title}</a>
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
										<logic:iterate id="datum" name="characterizationForm"
											property="achar.derivedBioAssayDataList[${fileInd}].datumList"
											indexId="datumInd">
											<th class="whiteBorderLessLabel">
												${datum.domainDerivedDatum.name}
												<c:if test="${!empty datum.domainDerivedDatum.valueUnit}">(${datum.domainDerivedDatum.valueUnit})</c:if>
											</th>
										</logic:iterate>
									</tr>
									<tr>
										<logic:iterate id="datum" name="characterizationForm"
											property="achar.derivedBioAssayDataList[${fileInd}].datumList"
											indexId="datumInd">
											<td class="whiteBorderLessLabel">
												${datum.valueStr}
											</td>
										</logic:iterate>
									</tr>
								</table>
							</th>
						</tr>
					</c:if>
				</logic:iterate>
				--%>
			</table>
		</td>
	</tr>
</table>
