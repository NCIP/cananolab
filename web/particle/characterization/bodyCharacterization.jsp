<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript'
	src='javascript/CharacterizationManager.js'></script>
<script type='text/javascript'
	src='dwr/interface/CharacterizationManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<html:form action="/${actionName}">
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
					page="/particle/characterization/physical/body${charClass}Info.jsp" />
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
											</c:when>
											<c:otherwise>
												<td></td>
											</c:otherwise>
										</c:choose>
										<td>
											<logic:iterate name="characterizationForm"
												property="achar.derivedBioAssayDataList"
												id="derivedBioAssayData" indexId="fileInd">
												<jsp:include
													page="/particle/characterization/shared/bodyCharacterizationFile.jsp">
													<jsp:param name="fileInd" value="${fileInd}" />
												</jsp:include>
												<br>
											</logic:iterate>
										</td>
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

