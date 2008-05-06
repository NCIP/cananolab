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
		<c:choose>
			<c:when test="${param.fileHidden eq 'false' }">

				<tr>
					<td class="leftLabel">
						<html:radio styleId="external_${param.fileInd }"
							property="${param.fileBean}.domainFile.uriExternal" value="false"
							onclick="radLinkOrUpload(${param.fileInd })" />
						<strong>Upload File</strong>
						<br>
						&nbsp;&nbsp;or
						<br>
						<html:radio property="${param.fileBean}.domainFile.uriExternal"
							value="true" onclick="radLinkOrUpload(${param.fileInd })" />
						<strong>Enter File URL</strong>
					</td>
					<td class="rightLabel" colspan="3">
						<span id="load_${param.fileInd }"> <html:file
								property="${param.fileBean}.uploadedFile" size="60" />
							&nbsp;&nbsp; </span>
						<br>
						<br>
						<span id="link_${param.fileInd }" style="display: none"><html:text
								property="${param.fileBean}.externalUrl" size="60" /> </span>&nbsp;
					</td>
					<c:if test="${!empty param.fileUri }">
						<tr>
							<td class="completeLabel" colspan="3">
								<c:choose>
									<c:when test="${param.fileImage eq 'true'}">
						 				${param.fileTitle}<br>
										<br>
										<a href="#"
											onclick="popImage(event, '${param.action}.do?dispatch=download&amp;fileId=${param.fileId}', ${param.fileId}, 100, 100)"><img
												src="${param.action}.do?dispatch=download&amp;fileId=${param.fileId}"
												border="0" width="150"> </a>
									</c:when>
									<c:otherwise>
										<strong>Uploaded File</strong> &nbsp;&nbsp;
										<c:set var="target" value="${param.fileBean}.urlTarget" />

										<a
											href="${param.action}.do?dispatch=download&amp;fileId=${param.fileId}"
											target="<bean:write name="${param.form }" property="${target }"/>">
											${param.fileUri}</a>

										<html:hidden property="${param.fileBean}.domainFile.uri" />
										<br>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:if>
				<tr>
					<td class="leftLabel">
						<strong>File Type*</strong>
					</td>
					<td class="rightLabel" colspan="2">
						<html:select styleId="fileType${param.fileInd}"
							property="${param.fileBean}.domainFile.type"
							onchange="javascript:callPrompt('File Type', 'fileType' + ${param.fileInd});">
							<option value="" />
								<html:options name="fileTypes" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="leftLabel">
						<strong>File Title*</strong>
					</td>
					<td class="rightLabel" colspan="2">
						<html:text property="${param.fileBean}.domainFile.title" size="60" />
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
						<html:select property="${param.fileBean}.visibilityGroups"
							multiple="true" size="3">
							<html:options name="allVisibilityGroups" />
						</html:select>
						<br>
						<i>(${applicationOwner}_Researcher and ${applicationOwner}_PI
							are defaults if none of above is selected.)</i>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="leftLabel">
						The file is private.
					</td>
					<td class="rightLabel" colspan="2">
						&nbsp;
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>

