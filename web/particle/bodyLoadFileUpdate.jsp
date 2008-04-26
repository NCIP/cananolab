<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="formSubTitleNoRight" colspan="2">
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
			<td class="leftLabel">
				<input type="radio" name="linkOrUpload_${param.fileInd}" value="upload" checked onclick="radLinkOrUpload(0, ${param.fileInd })"/>Upload File
				<input type="radio" name="linkOrUpload_${param.fileInd}" value="link" onclick="radLinkOrUpload(1, ${param.fileInd })" />Enter URL
			</td>
			<td class="label" align="right">
				<strong id="lutitle_${param.fileInd }">Upload New File</strong>
			</td>
			<td class="rightLabel" align="left">
				<c:set var="formUri" value="${param.form}.map.${param.domainFile}.uri"/>
				<c:set var="formId" value="${param.form}.map.${param.domainFile}.id"/>
				<c:choose>
					<c:when test="${!empty formUri && 
									empty formId}">
									    	${file.displayName} 
									    	<html:hidden property="${param.domainFile}.name" />
						<html:hidden property="${param.domainFile}.uri" />
						<br>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${!empty formId}">
						<a
							href="${nanoparticleCharacterizationForm.map.achar.actionName}.do?dispatch=download&amp;fileId=${file.id}">${file.displayName}</a>
						<html:hidden property="${param.domainFile}.id" />
						<html:hidden property="${param.domainFile}.name" />
						<html:hidden property="${param.domainFile}.uri" />
						<br>
					</c:when>
				</c:choose>
				<span id="loadEle_${param.fileInd }"><html:file property="${param.fileBean}.uploadedFile" /></span>
				<span id="linkEle_${param.fileInd }" style="display: none;"><html:text property="${param.domainFile}.uri" size="60" /></span>
					&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>File Title*</strong>
			</td>
			<td class="rightLabel" colspan="2">
				<html:text property="${param.domainFile}.title" size="60" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel" valign="top">
				<strong>Keywords <em>(one word per line)</em> </strong>
			</td>
			<td class="rightLabel" colspan="2">
				<html:textarea property="${param.fileBean}.keywordsStr" rows="2" />
					&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel" valign="top">
				<strong>Visibility</strong>
			</td>
			<td class="rightLabel" colspan="2">
				<html:select property="${param.fileBean}.visibilityGroups" multiple="true"
					size="3">
<%--					<html:options name="allVisibilityGroups" />--%>
				</html:select>
				<br>
				<i>(${applicationOwner}_Researcher and ${applicationOwner}_PI
					are defaults if none of above is selected.)</i>
			</td>
		</tr>
	</tbody>
</table>

