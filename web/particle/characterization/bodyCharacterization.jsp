<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript"
	src="javascript/CharacterizationManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CharacterizationManager.js'></script>
<script type="text/javascript" src="javascript/ProtocolManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/ProtocolManager.js'></script>
<script type="text/javascript" src="javascript/CompositionManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CompositionManager.js'></script>

<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<html:form action="/${actionName}" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${particleName} ${pageTitle}
				</h4>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${characterizationForm.map.achar.className}_help')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${submitType}
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<jsp:include
					page="/particle/characterization/shared/bodyCharacterizationSummary.jsp" />
				<jsp:include
					page="/particle/characterization/shared/bodyCharacterizationInstrument.jsp" />
				<jsp:include
					page="/particle/characterization/physical/body${characterizationForm.map.achar.className}Info.jsp" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Characterization File/Derived Data Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<c:choose>
											<c:when test="${canCreateNanoparticle eq 'true'}">
												<td valign="bottom">
													<a href="#"
														onclick="javascript:addComponent(document.forms[0], '${actionName}', 'addDerivedBioAssayData')"><span
														class="addLink">Add File/Derived Data</span> </a>
												</td>
												<td>
													<logic:iterate name="characterizationForm"
														property="achar.derivedBioAssayDataList"
														id="derivedBioAssayData" indexId="fileInd">
														<jsp:include page="/particle/bodyLoadFileUpdate.jsp">
															<jsp:param name="fileInd" value="${fileInd}" />
															<jsp:param name="form" value="characterizationForm" />
															<jsp:param name="action" value="${actionName}" />
															<jsp:param name="removeCmd"
																value="\'removeDerivedBioAssayData\'" />
															<jsp:param name="fileBean"
																value="achar.derivedBioAssayDataList[${fileInd}].labFileBean" />
															<jsp:param name="fileId"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.domainFile.id}" />
															<jsp:param name="fileUri"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.domainFile.uri}" />
															<jsp:param name="fileTitle"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.domainFile.title}" />
															<jsp:param name="fileHidden"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.hidden}" />
															<jsp:param name="fileImage"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.image}" />
														</jsp:include>
														<br>
													</logic:iterate>
												</td>
											</c:when>
											<c:otherwise>
												<td>
													<logic:iterate name="characterizationForm"
														property="achar.derivedBioAssayDataList"
														id="derivedBioAssayData" indexId="fileInd">
														<jsp:include page="/particle/bodyLoadFileReadOnly.jsp">
															<jsp:param name="fileInd" value="${fileInd}" />
															<jsp:param name="action" value="${actionName}" />
															<jsp:param name="domainFile"
																value="achar.derivedBioAssayDataList[${fileInd}].labFileBean.domainFile" />
															<jsp:param name="fileId"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.domainFile.id}" />
															<jsp:param name="fileUri"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.domainFile.uri}" />
															<jsp:param name="fileType"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.domainFile.type}" />
															<jsp:param name="fileTitle"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.domainFile.title}" />
															<jsp:param name="fileKeywordsStr"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.keywordsStr}" />
															<jsp:param name="fileVisibilityGroups"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.visibilityGroups}" />
															<jsp:param name="uriExternal"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.domainFile.uriExternal}" />
															<jsp:param name="fileImage"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].labFileBean.image}" />
														</jsp:include>
														<br>
													</logic:iterate>
												</td>
											</c:otherwise>
										</c:choose>
									</tr>
								</table>
							</td>
						</tr>
				</table>
				<br>
				<jsp:include page="/particle/bodyAnnotationCopy.jsp" />
				<jsp:include
					page="/particle/characterization/shared/bodyCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
