<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="summaryViewLayer4" width="95%" align="center">
	<tbody>
		<c:choose>
			<c:when
				test="${characterizationForm.map.achar.theFinding.theFile.domainFile.uriExternal eq 'true' }">
				<c:set var="linkDisplay" value="display: inline" />
				<c:set var="loadDisplay" value="display: none" />
			</c:when>
			<c:otherwise>
				<c:set var="linkDisplay" value="display: none" />
				<c:set var="loadDisplay" value="display: inline" />
			</c:otherwise>
		</c:choose>
		<tr>
			<td class="cellLabel">
				<html:radio styleId="external0"
					property="achar.theFinding.theFile.domainFile.uriExternal"
					value="false" onclick="radLinkOrUpload()" />
				Upload File
				<br>
				&nbsp;&nbsp;or
				<br>
				<html:radio styleId="external1"
					property="achar.theFinding.theFile.domainFile.uriExternal"
					value="true" onclick="radLinkOrUpload()" />
				Enter File URL
			</td>
			<td colspan="2">
				<span id="load"> <html:file
						property="achar.theFinding.theFile.uploadedFile" size="60"
						styleId="uploadedFile" /> &nbsp;&nbsp; </span>
				<br>
				<br>
				<span id="link" style=""><html:text
						property="achar.theFinding.theFile.externalUrl" size="60"
						styleId="externalUrl" /> </span>&nbsp;
			</td>
		</tr>
		<c:if
			test="${!empty characterizationForm.map.achar.theFinding.theFile.domainFile.uri }">
			<tr>
				<td colspan="3">
					<c:choose>
						<c:when
							test="${characterizationForm.map.achar.theFinding.theFile.image eq 'true'}">
						 				${characterizationForm.map.achar.theFinding.theFile.domainFile.title}<br>
							<br>
							<a href="#"
								onclick="popImage(event, '${actionName}.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.theFinding.theFile.domainFile.id}&amp;location=${location}',
														${characterizationForm.map.achar.theFinding.theFile.domainFile.id}, 100, 100)"><img
									src="xxxxxxxx.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.theFinding.theFile.domainFile.id}&amp;location=${location}"
									border="0" width="150"> </a>
						</c:when>
						<c:otherwise>
							<strong>Uploaded File</strong> &nbsp;&nbsp;
										<a
								href="xxxxxxxx.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.theFinding.theFile.domainFile.id}&amp;location=${location}"
								target="${characterizationForm.map.achar.theFinding.theFile.urlTarget}">
								${characterizationForm.map.achar.theFinding.theFile.domainFile.uri}</a>
							<br>
						</c:otherwise>
					</c:choose>
				</td>

			</tr>
		</c:if>
		<tr>
			<td class="cellLabel">
				File Type*
			</td>
			<td colspan="2">
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
			<td colspan="2">
				<html:text property="achar.theFinding.theFile.domainFile.title"
					styleId="fileTitle" size="60" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel" valign="top">
				Keywords
				<em>(one word per line)</em>
			</td>
			<td colspan="2">
				<html:textarea property="achar.theFinding.theFile.keywordsStr"
					rows="3" cols="60" styleId="fileKeywords" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="cellLabel" valign="top">
				<strong>Visibility</strong>
			</td>
			<td colspan="2">
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
				<input class="noBorderButton" type="button" value="Delete"
					onclick="deleteFile()" id="deleteFile" style="display: none;" />
			</td>
			<td></td>
			<td>
				<div align="right">
					<input class="noBorderButton" type="button" value="Cancel"
						onclick="hide('newFile');" />
					<input class="noBorderButton" type="button" value="Add"
						onclick="addFile('characterization');" />
				</div>
			</td>
		</tr>
</table>


