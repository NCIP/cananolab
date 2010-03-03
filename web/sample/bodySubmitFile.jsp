<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- different styles for different file submission forms --%>
<c:set var="submissionViewStyle" value="subSubmissionView" />
<c:set var="tableWidth" value="85%" />
<c:set var="buttonStyle" value="" />
<c:if test="${actionName eq 'characterization'}">
	<c:set var="submissionViewStyle" value="promptbox" />
	<c:set var="buttonStyle" value="promptButton" />
</c:if>
<c:if test="${actionName eq 'compositionFile'}">
	<c:set var="submissionViewStyle" value="submissionView" />
	<c:set var="tableWidth" value="100%" />
</c:if>
<table class="${submissionViewStyle}" width="${tableWidth}"
	align="center">
	<c:choose>
		<c:when test="${theFile.domainFile.uriExternal eq 'true' }">
			<c:set var="linkStyle" value="display: block" />
			<c:set var="loadStyle" value="display: none" />
		</c:when>
		<c:otherwise>
			<c:set var="linkStyle" value="display: none" />
			<c:set var="loadStyle" value="display: block" />
		</c:otherwise>
	</c:choose>
	<tr>
		<td class="cellLabel">
			<html:radio styleId="external0"
				property="${fileParent}.theFile.domainFile.uriExternal"
				value="false" onclick="displayFileRadioButton();" />
			Upload
		</td>
		<td class="cellLabel">
			<html:radio styleId="external1"
				property="${fileParent}.theFile.domainFile.uriExternal" value="true"
				onclick="displayFileRadioButton();" />
			Enter File URL
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<span id="load" style="${loadStyle}"> <html:file
					property="${fileParent}.theFile.uploadedFile" size="60"
					styleId="uploadedFile" /> &nbsp;&nbsp;</span>
			<c:set var="uploadedUriStyle" value="display:none" />
			<c:if
				test="${! empty theFile.domainFile.uri && theFile.domainFile.uriExternal eq false}">
				<c:set var="uploadedUriStyle" value="display:block" />
			</c:if>
			<span id="uploadedUri" style="${uploadedUriStyle}">${theFile.domainFile.uri
				}</span>
			<span id="link" style="${linkStyle }"><html:text
					property="${fileParent}.theFile.externalUrl" size="60"
					styleId="externalUrl" /> </span>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			File Type*
		</td>
		<td>
			<div id="fileTypePrompt">
				<html:select styleId="fileType"
					property="${fileParent}.theFile.domainFile.type"
					onchange="javascript:callPrompt('File Type', 'fileType', 'fileTypePrompt');">
					<option value="" />
						<html:options name="fileTypes" />
					<option value="other">
						[other]
					</option>
				</html:select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			File Title*
		</td>
		<td>
			<html:text property="${fileParent}.theFile.domainFile.title"
				styleId="fileTitle" size="60" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel" valign="top">
			Keywords
		</td>
		<td>
			<html:textarea property="${fileParent}.theFile.keywordsStr" rows="3"
				cols="60" styleId="fileKeywords" />
			<br>
			<em>(one word per line)</em>
		</td>
	</tr>
	<tr>
			<td class="cellLabel" valign="top">
				Description
			</td>
			<td>
				<html:textarea
					property="${fileParent}.theFile.domainFile.description" rows="3"
					cols="60" styleId="fileDescription" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel" valign="top">
				<strong>Visibility</strong>
			</td>
			<td>
				<html:select property="${fileParent}.theFile.visibilityGroups"
					multiple="true" size="6" styleId="fileVisibility">
					<html:options name="allVisibilityGroups" />
				</html:select>
				<br>
				<i>(${applicationOwner}_Researcher and
					${applicationOwner}_DataCurator are defaults if none of above is
					selected)</i>
			</td>
		</tr>
		<c:if test="${actionName eq 'characterization'}">
			<html:hidden property="${fileParent}.theFile.domainFile.id"
				styleId="hiddenFileId" />
			<html:hidden property="${fileParent}.theFile.domainFile.uri"
				styleId="hiddenFileUri" />
			<html:hidden property="${fileParent}.theFileIndex"
				styleId="hiddenFileIndex" value="-1" />
		</c:if>
		<c:if test="${actionName ne 'compositionFile'}">
			<tr>
				<td>
					<c:if test="${!empty user && user.curator && user.admin}">
						<input class="${buttonStyle}" type="button" value="Remove"
							onclick="removeFile('${actionName}', ${fileForm})"
							id="deleteFile" style="display: none;" />
					</c:if>
				</td>
				<td>
					<div align="right">
						<input class="${buttonStyle}" type="button" value="Save"
							onclick="addFile('${actionName}', ${fileForm});" />
						<input class="${buttonStyle}" type="button" value="Cancel"
							onclick="clearFile('${fileParent }');closeSubmissionForm('File');" />
					</div>
				</td>
			</tr>
		</c:if>
</table>
