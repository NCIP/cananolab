<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
	<c:when
		test="${param.action eq 'physicalCharacterization' || param.action eq 'invitroCharacterization'}">
		<c:set var="isChar" value="true" />
		<c:set var="titleType" value="Derived Bioassay Data" />
		<c:set var="required" value="" />
	</c:when>
	<c:otherwise>
		<c:set var="isChar" value="false" />
		<c:set var="titleType" value="File" />
		<c:set var="required" value="*" />
	</c:otherwise>
</c:choose>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="formSubTitleNoRight" colspan="2">
				${titleType } #${param.fileInd+1}
			</td>
			<td class="formSubTitleNoLeft" align="right">
				<a href="#"
					onclick="removeComponent(document.forms[0], '${param.action}', ${param.fileInd}, ${param.removeCmd });return false;">
					<img src="images/delete.gif" border="0" alt="remove this file">
				</a>
			</td>
		</tr>

		<c:choose>
			<c:when test="${param.fileHidden eq 'false' }">
				<c:choose>
					<c:when test="${param.fileUriExternal eq 'true' }">
						<c:set var="linkDisplay" value="display: inline" />
						<c:set var="loadDisplay" value="display: none" />
					</c:when>
					<c:otherwise>
						<c:set var="linkDisplay" value="display: none" />
						<c:set var="loadDisplay" value="display: inline" />
					</c:otherwise>
				</c:choose>
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
					<td class="rightLabel" colspan="2">
						<span id="load_${param.fileInd }" style="${loadDisplay }">
							<html:file property="${param.fileBean}.uploadedFile" size="60" />
							&nbsp;&nbsp; </span>
						<br>
						<br>
						<span id="link_${param.fileInd }" style="${linkDisplay }"><html:text
								property="${param.fileBean}.externalUrl" size="60" /> </span>&nbsp;
					</td>
					</tr>
					<c:if test="${!empty param.fileUri }">
						<tr>
							<td class="completeLabel" colspan="3">
								<c:choose>
									<c:when test="${param.fileImage eq 'true'}">
						 				${param.fileTitle}<br>
										<br>
										<a href="#"
											onclick="popImage(event, '${param.action}.do?dispatch=download&amp;fileId=${param.fileId}&amp;location=${location}', ${param.fileId}, 100, 100)"><img
												src="${param.action}.do?dispatch=download&amp;fileId=${param.fileId}&amp;location=${location}"
												border="0" width="150"> </a>
									</c:when>
									<c:otherwise>
										<strong>Uploaded File</strong> &nbsp;&nbsp;
										<c:set var="target" value="${param.fileBean}.urlTarget" />

										<a
											href="${param.action}.do?dispatch=download&amp;fileId=${param.fileId}&amp;location=${location}"
											target="<bean:write name="${param.form }" property="${target }"/>">
											${param.fileUri}</a>

										<br>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:if>
				<tr>
					<td class="leftLabel">
						<strong>File Type ${required}</strong>
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
						<strong>File Title ${required}</strong>
					</td>
					<td class="rightLabel" colspan="2">
						<html:text property="${param.fileBean}.domainFile.title" size="60" />
					</td>
				</tr>
				<tr>
					<td class="leftLabel">
						<strong>File Description</strong>
					</td>
					<td class="rightLabel" colspan="2">
						<html:textarea property="${param.fileBean}.domainFile.description" rows="5" cols="60"/>
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
						<i>(${applicationOwner}_Researcher and ${applicationOwner}_DataCurator
							are defaults if none of above is selected.)</i>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.fileHidden eq 'true'}">
					<tr>
						<td class="leftLabel">
							The file is private.
						</td>
						<td class="rightLabel" colspan="2">
							&nbsp;
						</td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:if test="${!empty param.fileUri }">
			<html:hidden property="${param.fileBean}.domainFile.uri" />
		</c:if>
		<c:if test="${!empty param.fileId }">
			<html:hidden property="${param.fileBean}.domainFile.id" />
		</c:if>
		<c:if test="${isChar eq 'true'}">
			<tr>
				<td class="completeLabel" colspan="4">
					<table border="0" width="100%">
						<tr>

							<td valign="bottom">
								<a href="#"
									onclick="javascript:addChildComponent(document.forms[0], '${param.action}', ${param.fileInd}, 'addDerivedDatum')"><span
									class="addLink">Add Derived Datum</span> </a>
							</td>

							<td>
								<jsp:include
									page="/sample/characterization/shared/bodyDerivedDatum.jsp">
									<jsp:param name="fileInd" value="${param.fileInd}" />
								</jsp:include>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</c:if>
	</tbody>
</table>

