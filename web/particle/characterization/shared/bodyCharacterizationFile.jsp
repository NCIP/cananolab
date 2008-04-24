<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<logic:present name="characterizationFile${param.fileInd}">
	<bean:define id="fileId" name='characterizationFile${param.fileInd}'
		property='id' type="java.lang.String" />
</logic:present>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<c:choose>
				<c:when test="${canCreateNanoparticle eq 'true'}">
					<td class="formSubTitleNoRight" colspan="3">
						Characterization File #${param.fileInd+1}
					</td>
					<td class="formSubTitleNoLeft" align="right">
						<a href="#"
							onclick="javascript:removeComponent(characterizationForm, '${actionName}', ${param.fileInd}, 'removeDerivedBioAssayData')">
							<img src="images/delete.gif" border="0" alt="remove this file">
						</a>
					</td>
				</c:when>
				<c:otherwise>
					<td class="formSubTitle" colspan="4">
						Characterization File #${param.fileInd+1}
					</td>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td class="leftLabelWithTop" valign="top" width="15%">
				<strong>File Type</strong>
			</td>
			<c:choose>
				<c:when test="${canCreateNanoparticle eq 'true'}">
					<td class="completeLabelWithTop" valign="top" colspan="4">
						<html:select styleId="fileType${param.fileInd}"
							property="achar.derivedBioAssayDataList[${param.fileInd}].labFileBean.domainFile.type"
							onchange="javascript:callPrompt('File Type', 'fileType' + ${param.fileInd});">
							<option value=""></option>
							<html:options name="fileTypes" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</td>
				</c:when>
				<c:otherwise>
					<td class="completeLabelWithTop" colspan="4">
						${characterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].labFileBean.domainFile.type}&nbsp;
					</td>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td class="completeLabel" valign="top" colspan="4">
				<strong>File Name</strong> &nbsp;&nbsp;&nbsp;
				<c:choose>
					<c:when
						test="${!empty characterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].labFileBean.domainFile.uri}">
						<c:choose>
							<c:when
								test="${characterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].labFileBean.hidden==false}">
								<html:link
									page="/${actionName}.do?page=0&dispatch=loadFile&fileNumber=${param.fileInd}">
								${characterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].labFileBean.displayName}
							</html:link>
							</c:when>
							<c:otherwise>
								The file is private.
							</c:otherwise>
						</c:choose>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<c:choose>
							<c:when
								test="${empty characterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].labFileBean.domainFile.uri}">	
								Click on "Load File" button 	
									&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"
									onclick="javascript:loadFile(this.form, '${actionName}', ${param.fileInd})"
									value="Load File">
							</c:when>
						</c:choose>
					</c:when>
				</c:choose>
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
										onclick="javascript:addChildComponent(characterizationForm, '${actionName}', ${param.fileInd}, 'addDerivedDatum')"><span
										class="addLink">Add Derived Data</span> </a>
								</td>
							</c:when>
							<c:otherwise>
								<td></td>
							</c:otherwise>
						</c:choose>
						<td>
							<jsp:include
								page="/particle/characterization/shared/bodyDerivedDatum.jsp">
								<jsp:param name="fileInd" value="${param.fileInd}" />
							</jsp:include>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</tbody>
</table>

<logic:present name="characterizationFile${param.fileInd}">
	<html:hidden
		property="achar.derivedBioAssayDataList[${param.fileInd}].labFileBean.domainFile.id"
		value="${fileId}" />
</logic:present>
