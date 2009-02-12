<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
<!--//
function confirmDeletion()
{
	answer = confirm("Are you sure you want to delete the composition file?")
	if (answer !=0)
	{
		this.document.forms[0].dispatch.value="delete";
		this.document.forms[0].page.value="1";
		this.document.forms[0].submit();
		return true;
	}
}
//-->
</script>
<html:form action="/${actionName}" enctype="multipart/form-data">
	<table class="topBorderOnly" cellspacing="0" cellpadding="3"
		width="100%" align="center" summary="" border="0">
		<tbody>
			<tr class="topBorder">
				<td class="subformTitle" colspan="3">
					<div align="justify">
						Result File Information
					</div>
				</td>
			</tr>
			<c:choose>
				<c:when
					test="${characterizationForm.map.achar.theDataSet.file.hidden eq 'false' }">
					<c:choose>
						<c:when
							test="${characterizationForm.map.achar.theDataSet.file.domainFile.uriExternal eq 'true' }">
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
							<html:radio styleId="external0"
								property="achar.theDataSet.file.domainFile.uriExternal" value="false"
								onclick="radLinkOrUpload()" />
							<strong>Upload File</strong>
							<br>
							&nbsp;&nbsp;or
							<br>
							<html:radio styleId="external1"
								property="achar.theDataSet.file.domainFile.uriExternal" value="true"
								onclick="radLinkOrUpload()" />
							<strong>Enter File URL</strong>
						</td>
						<td class="rightLabel" colspan="2">
							<span id="load"> <html:file
									property="achar.theDataSet.file.uploadedFile" size="60" />
								&nbsp;&nbsp; </span>
							<br>
							<br>
							<span id="link" style=""><html:text
									property="achar.theDataSet.file.externalUrl" size="60" /> </span>&nbsp;
						</td>
					</tr>
					<c:if
						test="${!empty characterizationForm.map.achar.theDataSet.file.domainFile.uri }">
						<tr>
							<td class="completeLabel" colspan="3">
								<c:choose>
									<c:when
										test="${characterizationForm.map.achar.theDataSet.file.image eq 'true'}">
						 				${characterizationForm.map.achar.theDataSet.file.domainFile.title}<br>
										<br>
										<a href="#"
											onclick="popImage(event, '${actionName}.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.theDataSet.file.domainFile.id}&amp;location=${location}',
														${characterizationForm.map.achar.theDataSet.file.domainFile.id}, 100, 100)"><img
												src="xxxxxxxx.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.theDataSet.file.domainFile.id}&amp;location=${location}"
												border="0" width="150"> </a>
									</c:when>
									<c:otherwise>
										<strong>Uploaded File</strong> &nbsp;&nbsp;
										<a
											href="xxxxxxxx.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.theDataSet.file.domainFile.id}&amp;location=${location}"
											target="${characterizationForm.map.achar.theDataSet.file.urlTarget}">
											${characterizationForm.map.achar.theDataSet.file.domainFile.uri}</a>


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
							<html:select styleId="fileType"
								property="achar.theDataSet.file.domainFile.type"
								onchange="javascript:callPrompt('File Type', 'fileType');">
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
							<html:text property="achar.theDataSet.file.domainFile.title" size="60" />
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Keywords <em>(one word per line)</em> </strong>
						</td>
						<td class="rightLabel" colspan="2">
							<html:textarea property="achar.theDataSet.file.keywordsStr" rows="3" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Visibility</strong>
						</td>
						<td class="rightLabel" colspan="2">
							<html:select property="achar.theDataSet.file.visibilityGroups"
								multiple="true" size="6">
								<html:options name="allVisibilityGroups" />
							</html:select>
							<br>
							<i>(${applicationOwner}_Researcher and
								${applicationOwner}_DataCurator are defaults if none of above is
								selected.)</i>
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

			<c:if
				test="${!empty characterizationForm.map.achar.theDataSet.file.domainFile.id }">
				<html:hidden property="achar.theDataSet.file.domainFile.id" />
				<html:hidden property="achar.theDataSet.file.domainFile.uri" />
			</c:if>
	</table>
	<br>

	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td width="30%">
				<span class="formMessage"> </span>
				<br>
				<c:choose>
					<c:when
						test="${param.dispatch eq 'setupUpdate'&& canUserDelete eq 'true'}">
						<table height="32" border="0" align="left" cellpadding="4"
							cellspacing="0">
							<tr>
								<td height="32">
									<div align="left">
										<input type="button" value="Delete"
											onclick="confirmDeletion();">
									</div>
								</td>
							</tr>
						</table>
					</c:when>
				</c:choose>

				<table width="498" border="0" align="right"
					cellpadding="4" cellspacing="0">
					<tr>
						<td width="490">
							<div align="right">
								<div align="right">
									<c:choose>
										<c:when test="${!empty param.particleId }">
											<html:hidden property="particleId"
												value="${param.particleId }" />
										</c:when>
										<c:otherwise>
											<html:hidden property="particleId" />
										</c:otherwise>
									</c:choose>

									<input type="hidden" name="dispatch" value="create">
									<input type="hidden" name="page" value="2">
									<input type="hidden" name="submitType"
										value="${param.submitType}" />
									<html:submit />
								</div>
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
</html:form>