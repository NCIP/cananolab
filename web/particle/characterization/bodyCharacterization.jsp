<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

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
					${fn:toUpperCase(param.location)} ${particleName} ${pageTitle}
				</h4>
			</td>
			<c_rt:set var='dispatch' value='<%=request.getParameter("dispatch")%>'/>
			<c:choose>
				<c:when test="${'setup' eq param.dispatch }">
					<c:remove var="dataId" scope="session" />
				</c:when>										
				<c:when test="${'setupUpdate' eq param.dispatch }">
					<c:set var="dataId" value="${param.dataId}" scope="session" />
				</c:when>																			
			</c:choose>	
			<c:set var="helpTopic" value="char_details_help" />
			<c:choose>
				<c:when test='${"Physical Characterization" eq pageTitle && ("setup" eq dispatch || empty dataId)}'>
					<c:set var="helpTopic" value="add_physical_char_help" />
				</c:when>
				<c:when test='${"In Vitro Characterization" eq pageTitle && ("setup" eq dispatch || empty dataId)}'>
					<c:set var="helpTopic" value="add_in_vitro_char_help" />
				</c:when>				
			</c:choose>	
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">			
					<jsp:param name="topic" value="${helpTopic}" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>			
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${param.submitType}
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
				<c:if test="${!empty characterizationDetailPage}">
					<jsp:include page="${characterizationDetailPage}" />
				</c:if>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Derived Bioassay Data Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<c:choose>
											<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
												<td valign="bottom" width="10%">
													<a href="#"
														onclick="javascript:addComponent(document.forms[0], '${actionName}', 'addDerivedBioAssayData')"><span
														class="addLink">Add Derived Bioassay Data</span> </a>
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
																	value="achar.derivedBioAssayDataList[${fileInd}].fileBean" />
																<jsp:param name="fileId"
																	value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.domainFile.id}" />
																<jsp:param name="fileUri"
																	value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.domainFile.uri}" />
																<jsp:param name="fileTitle"
																	value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.domainFile.title}" />
																<jsp:param name="fileHidden"
																	value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.hidden}" />
																<jsp:param name="fileImage"
																	value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.image}" />
																<jsp:param name="fileUriExternal"
																	value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.domainFile.uriExternal}" />
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
																value="achar.derivedBioAssayDataList[${fileInd}].fileBean.domainFile" />
															<jsp:param name="fileId"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.domainFile.id}" />
															<jsp:param name="fileUri"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.domainFile.uri}" />
															<jsp:param name="fileType"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.domainFile.type}" />
															<jsp:param name="fileTitle"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.domainFile.title}" />
															<jsp:param name="fileKeywordsStr"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.keywordsStr}" />
															<jsp:param name="visibilityStr"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.visibilityStr}" />
															<jsp:param name="uriExternal"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.domainFile.uriExternal}" />
															<jsp:param name="fileImage"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.image}" />
															<jsp:param name="fileHidden"
																value="${characterizationForm.map.achar.derivedBioAssayDataList[fileInd].fileBean.hidden}" />
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
