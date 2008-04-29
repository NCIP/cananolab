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

				<c:choose>
					<c:when test="${param.fileExternal eq 'true'}">
						<c:set var="uploadCheck" value="" />
						<c:set var="linkCheck" value="checked" />
						<c:set var="upladStyle" value="display: none" />
						<c:set var="linkStyle" value="display: inline" />
					</c:when>
					<c:otherwise>
						<c:set var="uploadCheck" value="checked" />
						<c:set var="linkCheck" value="" />
						<c:set var="upladStyle" value="display: inline" />
						<c:set var="linkStyle" value="display: none" />
					</c:otherwise>
				</c:choose>

				<tr>
					<td class="leftLabel">
						<input type="radio" name="linkOrUpload_${param.fileInd}"
							value="upload"
							${uploadCheck}
							onclick="radLinkOrUpload(0, ${param.fileInd })" />
						Upload File
						<input type="radio" name="linkOrUpload_${param.fileInd}"
							${linkCheck}
							value="link"
							onclick="radLinkOrUpload(1, ${param.fileInd })" />
						Enter URL
					</td>
					<td class="label" align="right">
						<strong id="lutitle_${param.fileInd }">Upload New File</strong>
					</td>
					<td class="rightLabel" align="left">
						<c:choose>
							<c:when test="${!empty param.fileUri && empty param.fileId }">
								<html:hidden property="${param.domainFile}.name" />
								<html:hidden property="${param.domainFile}.uri" />
								<br>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${!empty param.fileId}">
								<c:choose>
									<c:when test="${param.fileImage eq 'true'}">
 												${param.fileDisplayName}<br>
										<br>
										<a href="#"
											onclick="popImage(event, '${param.action}.do?dispatch=download&amp;fileId=${param.fileId}', ${param.fileId}, 100, 100)"><img
												src="${param.action}.do?dispatch=download&amp;fileId=${param.fileId}"
												border="0" width="150"> </a>
									</c:when>
									<c:otherwise>
										<a
											href="${param.action}.do?dispatch=download&amp;fileId=${param.fileId}">${param.fileDisplayName}</a>
										<html:hidden property="${param.domainFile}.id" />
										<html:hidden property="${param.domainFile}.name" />
										<html:hidden property="${param.domainFile}.uri" />
										<br>
									</c:otherwise>
								</c:choose>

							</c:when>
						</c:choose>
						<span id="loadEle_${param.fileInd }" style="${uploadStyle}"><html:file
								property="${param.fileBean}.uploadedFile" /> </span>
						<span id="linkEle_${param.fileInd }" style="${linkStyle}"><html:text
								property="${param.domainFile}.uri" size="60" /> </span> &nbsp;
					</td>
				</tr>
				<tr>
					<td class="leftLabel">
						<strong>File Type*</strong>
					</td>
					<td class="rightLabel" colspan="2">
						<html:select styleId="fileType${param.fileInd}"
							property="${param.domainFile}.type"
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
						<html:select property="${param.fileBean}.visibilityGroups"
							multiple="true" size="3">
							<%--					<html:options name="allVisibilityGroups" />--%>
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

