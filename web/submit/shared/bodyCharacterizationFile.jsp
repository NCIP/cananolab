<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<logic:present name="characterizationFile${param.fileInd}">
	<bean:define id="fileId" name='characterizationFile${param.fileInd}'
		property='id' type="java.lang.String" />
</logic:present>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="formSubTitle" colspan="4" align="right">
				<a href="#"
					onclick="javascript:removeCharacterizationFile(nanoparticleCharacterizationForm, '${param.actionName}', ${param.fileInd})">
					<img src="images/delete.gif" border="0" alt="remove this file">
				</a>
			</td>
		</tr>
		<tr>
			<td class="leftLabelWithTop" valign="top" width="10%">
				<strong>File Type</strong>
			</td>
			<td class="labelWithTop" valign="top">
				<html:select
					property="achar.derivedBioAssayDataList[${param.fileInd}].type"
					onkeydown="javascript:fnKeyDownHandler(this, event);"
											onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
											onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
											onchange="fnChangeHandler_A(this, event);">
					<option value="">--?--</option>
					<html:options name="allDerivedDataFileTypes" />
				</html:select>
			</td>
			<td class="labelWithTop" valign="top">
				<strong>Data Category</strong>
			</td>
			<td class="rightLabelWithTop" valign="top">
				<html:select
					property="achar.derivedBioAssayDataList[${param.fileInd}].type"
					multiple="yes" size="4"
					onkeydown="javascript:fnKeyDownHandler(this, event);"
											onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
											onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
											onchange="fnChangeHandler_A(this, event);">
					<option value="">
						--?--
					</option>
					<option value="Volume Distribution">
						Volume Distribution
					</option>
					<option value="Number Distribution">
						Number Distribution
					</option>
					<option value="Intensity Distribution">
						Intensity Distribution
					</option>
					<%--<html:options name="allFileCategories" />--%>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" valign="top" colspan="4">
				<strong>File Name</strong> &nbsp;&nbsp;&nbsp;
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<logic:present name="characterizationFile${param.fileInd}">
							<html:link
								page="/updateAssayFile.do?page=0&dispatch=setupUpdate&fileId=${fileId}&actionName=${param.actionName}">
								<bean:write name="characterizationFile${param.fileInd}"
									property="displayName" />
							</html:link>
						</logic:present>
						<logic:notPresent name="characterizationFile${param.fileInd}">
					Click on "Load File" button 
						</logic:notPresent>
						&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"
							onclick="javascript:loadFile(this.form, '${param.actionName}', '${param.particleName}', ${param.fileInd})"
							value="Load File">
					</c:when>
					<c:otherwise>
						<logic:present name="characterizationFile${param.fileInd}">
							<html:link
								page="/updateAssayFile.do?page=0&dispatch=setupView&fileId=${fileId}&actionName=${param.actionName}">
								<bean:write name="characterizationFile${param.fileInd}"
									property="displayName" />
							</html:link>
						</logic:present>
					&nbsp;				
			</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<table border="0" width="100%">
					<tr>
						<td valign="bottom">
							<img src="images/Plus.gif">
							&nbsp;<a href="#"
								onclick="javascript:addCharacterizationData(nanoparticleCharacterizationForm, '${param.actionName}', ${param.fileInd})"><span
								class="addLink">Add Derived Data</span> </a>
						</td>
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

