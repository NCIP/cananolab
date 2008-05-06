<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="formSubTitleNoRight" colspan="3">
				File #${param.fileInd+1}
			</td>
			<td class="formSubTitleNoLeft" align="right">
				<a href="#"
					onclick="removeComponent(${param.form}, '${param.action}', ${param.fileInd}, 'removeFile');return false;">
					<img src="images/delete.gif" border="0" alt="remove this file">
				</a>
			</td>
		</tr>
		<tr>
			<td class="completeLabelWithTop" valign="top" colspan="4">
				<strong>File Name</strong> &nbsp;&nbsp;&nbsp;
				<%--				<c:choose>--%>
				<%--					<c:when--%>
				<%--						test="${!empty nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].uri}">--%>
				<%--						<c:choose>--%>
				<%--							<c:when--%>
				<%--								test="${nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].hidden==false}">--%>
				<%--								<html:link--%>
				<%--									page="/${nanoparticleCharacterizationForm.map.achar.actionName}.do?page=0&dispatch=loadFile&fileNumber=${param.fileInd}">--%>
				<%--								${nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].displayName}--%>
				<%--							</html:link>--%>
				<%--							</c:when>--%>
				<%--							<c:otherwise>--%>
				<%--								The file is private.--%>
				<%--							</c:otherwise>--%>
				<%--						</c:choose>--%>
				<%--					</c:when>--%>
				<%--				</c:choose>--%>
				<%--				--%>
				<%--						<c:choose>--%>
				<%--							<c:when--%>
				<%--								test="${empty nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].uri}">	--%>
				Click on "Load File" button &nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button"
					onclick="javascript:loadFile(this.form, 'loadFileActionForTest', ${param.fileInd})"
					value="Load File">
				<%--							</c:when>--%>
				<%--						</c:choose>--%>

			</td>
		</tr>
		<tr>
			<td class="leftLabel" valign="top" width="15%">
				<strong>File Type</strong>
			</td>
			<td class="label" valign="top">
				<html:select styleId="fileType${param.fileInd}"
					property="entity.files[${param.fileInd}].type"
					onchange="javascript:callPrompt('File Type', 'fileType' + ${param.fileInd});">
					<option value="" />
						<html:options name="fileTypes" />
					<option value="other">
						[Other]
					</option>
				</html:select>
			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
			</td>
		</tr>
	</tbody>
</table>
