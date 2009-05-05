<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="promptbox" width="85%" align="center">
	<tbody>
		<c:choose>
			<c:when
				test="${characterizationForm.map.achar.theFinding.theFile.domainFile.uriExternal eq 'true' }">
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
					property="achar.theFinding.theFile.domainFile.uriExternal"
					value="false" onclick="show('load');hide('link');" />
				Upload
			</td>
			<td class="cellLabel">
				<html:radio styleId="external1"
					property="achar.theFinding.theFile.domainFile.uriExternal"
					value="true" onclick="hide('load');show('link');" />
				Enter File URL
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<span id="load" style="${loadStyle}"> <html:file
						property="achar.theFinding.theFile.uploadedFile" size="60"
						styleId="uploadedFile" /> &nbsp;&nbsp; </span>
				<span id="link" style="${linkStyle}"><html:text
						property="achar.theFinding.theFile.externalUrl" size="60"
						styleId="externalUrl" /> </span>&nbsp;
				<span id="uploadedUri"></span>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				File Type*
			</td>
			<td>
				<div id="fileTypePrompt">
					<html:select styleId="fileType"
						property="achar.theFinding.theFile.domainFile.type"
						onchange="javascript:callPrompt('File Type', 'fileType', 'fileTypePrompt');">
						<option value="" />
							<html:options name="fileTypes" />
						<option value="other">
							[Other]
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
				<html:text property="achar.theFinding.theFile.domainFile.title"
					styleId="fileTitle" size="60" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel" valign="top">
				Keywords
			</td>
			<td>
				<html:textarea property="achar.theFinding.theFile.keywordsStr"
					rows="3" cols="60" styleId="fileKeywords" />
				<br><em>(one word per line)</em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel" valign="top">
				<strong>Visibility</strong>
			</td>
			<td>
				<html:select property="achar.theFinding.theFile.visibilityGroups"
					multiple="true" size="6" styleId="fileVisibility">
					<html:options name="allVisibilityGroups" />
				</html:select>
				<br>
				<i>(${applicationOwner}_Researcher and
					${applicationOwner}_DataCurator are defaults if none of above is
					selected.)</i>
			</td>
		</tr>
		<c:if
			test="${!empty characterizationForm.map.achar.theFinding.theFile.domainFile.id }">
			<html:hidden property="achar.theFinding.theFile.domainFile.id" />
			<html:hidden property="achar.theFinding.theFile.domainFile.uri" />
		</c:if>
		<tr>
			<td>
				<input class="promptButton" type="button" value="Remove"
					onclick="deleteFile()" id="deleteFile" style="display: none;" />
			</td>
			<td>
				<div align="right">
					<input class="promptButton" type="button" value="Add"
						onclick="addFile(characterizationForm);" />
					<input class="promptButton" type="button" value="Cancel"
						onclick="clearFile();hide('newFile');" />
				</div>
			</td>
		</tr>
</table>
