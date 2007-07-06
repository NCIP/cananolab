<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
	<c:when test="${!empty param.actionName}">
		<c:set var="actionName" value="${param.actionName}" scope="session" />
	</c:when>
</c:choose>
<html:form action="/${actionName}">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					${pageTitle}
				</h4>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${submitType}_help')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${param.particleName} ${param.particleType}
				</h5>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />

				<jsp:include page="/submit/shared/bodyCharacterizationSummary.jsp" />

				<jsp:include
					page="/submit/shared/bodyCharacterizationInstrument.jsp" />

				<logic:present name="detailPage">
					<jsp:include page="${detailPage}" />
				</logic:present>

				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Characterization File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<c:choose>
											<c:when test="${canUserSubmit eq 'true'}">
												<td valign="bottom">													
													<a href="#"
														onclick="javascript:addCharacterizationFile(nanoparticleCharacterizationForm, '${param.charName}', '${actionName}')"><span
														class="addLink">Add File</span> </a>
												</td>
											</c:when>
											<c:otherwise>
												<td></td>
											</c:otherwise>
										</c:choose>
										<td>
											<logic:iterate name="nanoparticleCharacterizationForm"
												property="achar.derivedBioAssayDataList"
												id="derivedBioAssayData" indexId="fileInd">
												<jsp:include
													page="/submit/shared/bodyCharacterizationFile.jsp?actionName=${actionName}&fileInd=${fileInd}" />
												<br>
											</logic:iterate>
										</td>
									</tr>
								</table>
							</td>
						</tr>
				</table>
				<br>
				<jsp:include page="/submit/shared/bodyCharacterizationCopy.jsp" />
				<jsp:include page="/submit/shared/bodyCharacterizationSubmit.jsp" />
				<html:hidden property="particleName" value="${param.particleName}"/>
			</td>
		</tr>
	</table>
</html:form>

