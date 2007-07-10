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
				<c:when test="${canUserSubmit eq 'true'}">
					<td class="formSubTitle" colspan="4" align="right">
						<a href="#"
							onclick="javascript:removeCharacterizationFile(nanoparticleCharacterizationForm, '${param.charName}', '${param.actionName}', ${param.fileInd})">
							<img src="images/delete.gif" border="0" alt="remove this file">
						</a>
					</td>
				</c:when>
				<c:otherwise>
					<td></td>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td class="leftLabelWithTop" valign="top" width="10%">
				<strong>File Type</strong>
			</td>
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<td class="labelWithTop" valign="top">
						<html:select
							property="achar.derivedBioAssayDataList[${param.fileInd}].type"
							onkeydown="javascript:fnKeyDownHandler(this, event);"
							onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
							onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
							onchange="fnChangeHandler_A(this, event);">
							<option value="">
								--?--
							</option>
							<html:options name="allDerivedDataFileTypes" />
						</html:select>
					</td>
				</c:when>
				<c:otherwise>
					<td class="labelWithTop">
						${nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].type}&nbsp;
					</td>
				</c:otherwise>
			</c:choose>
			<td class="labelWithTop" valign="top">
				<strong>Data Category</strong>				
			</td>
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<td class="rightLabelWithTop" valign="top">
						<html:select 
							property="achar.derivedBioAssayDataList[${param.fileInd}].categories"
							multiple="yes" size="4"
							onkeydown="javascript:fnKeyDownHandler(this, event);"
							onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
							onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
							onchange="fnChangeHandler_A(this, event); filterDatumCategories(${param.fileInd}, ${fn:length(nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].datumList)})">
							<option value="">
								--?-- 
							</option>
							<html:options name="derivedDataCategories" />
						</html:select>
					</td>
				</c:when>
				<c:otherwise>
					<td class="rightLabelWithTop">
						<c:forEach var="category"
							items="${nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].categories}">
							${category}
						</c:forEach>
						&nbsp;
					</td>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td class="completeLabel" valign="top" colspan="4">
				<strong>File Name</strong> &nbsp;&nbsp;&nbsp;
				<c:choose>
					<c:when
						test="${!empty nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].uri}">
						<html:link
							page="/updateAssayFile.do?page=0&dispatch=setupUpdate&fileId=${nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].id}&actionName=${param.actionName}">
								${nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].displayName}
							</html:link>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<c:choose>
							<c:when
								test="${empty nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].uri}">	
								Click on "Load File" button 	
							</c:when>
						</c:choose>
						&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"
							onclick="javascript:loadFile(this.form, '${param.charName}', '${param.actionName}', ${param.fileInd})"
							value="Load File">
					</c:when>
				</c:choose>
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
										onclick="javascript:addCharacterizationData(nanoparticleCharacterizationForm, '${param.charName}', '${param.actionName}', ${param.fileInd})"><span
										class="addLink">Add Derived Data</span> </a>
								</td>
							</c:when>
							<c:otherwise>
								<td></td>
							</c:otherwise>
						</c:choose>
						<td>
							<jsp:include page="/submit/shared/bodyDerivedBioAssayDatum.jsp">
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
		property="achar.derivedBioAssayDataList[${param.fileInd}].fileId"
		value="${fileId}" />
</logic:present>
