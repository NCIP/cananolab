<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='javascript/ProtocolManager.js'></script>
<script type='text/javascript' src='dwr/interface/ProtocolManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<c:set var="action" value="Submit" scope="request" />
<c:if test="${param.dispatch eq 'setupUpdate'}">
	<c:set var="action" value="Update" scope="request" />
</c:if>

<html:form action="/submitProtocol" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					${action } Protocol
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_protocol_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>

			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=protocol" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="2">
								<div align="justify">
									Description
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Protocol Type*</strong>
							</td>
							<td class="rightLabel">
								<html:select styleId="protocolType"
									property="file.domainFile.protocol.type"
									onchange="javascript:callPrompt('Protocol Type', 'protocolType'); retrieveProtocols();">
									<option value="" />
										<html:options name="protocolTypes" />
									<option value="other">
										[Other]
									</option>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Protocol Name* </strong>
							</td>
							<td class="rightLabel">
								<html:select styleId="protocolName"
									property="file.domainFile.protocol.name"
									onchange="javascript:callPrompt('Protocol Name', 'protocolName'); retrieveProtocolFileVersions();">
									<c:if test="${!empty protocolNamesByType}">
										<option value="" />
											<html:options name="protocolNamesByType" />
										<option value="other">
											[Other]
										</option>
									</c:if>
								</html:select>
								&nbsp; &nbsp;
								<strong>Protocol Version* </strong>&nbsp;
								<html:select styleId="protocolFileId"
									property="file.domainFileId"
									onchange="javascript:callPrompt('Protocol Version', 'protocolFileId');retrieveProtocolFile();">
									<c:if test="${!empty protocolFilesByTypeName}">
										<option value="" />
											<html:optionsCollection name="protocolFilesByTypeName"
												label="domainFile.version" value="domainFile.id" />
										<option value="other">
											[Other]
										</option>
									</c:if>
								</html:select>
								&nbsp; &nbsp;
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Protocol File</strong>
							</td>
							<td class="rightLabel">
								<html:file property="file.uploadedFile" />
								&nbsp;&nbsp;
								<span id="protocolFileLink"> <c:if
										test="${!empty submitProtocolForm.map.file.domainFile.uri }">&nbsp;&nbsp;
									<a
											href="searchProtocol.do?dispatch=download&amp;fileId=${submitProtocolForm.map.file.domainFile.id}&amp;location=local">
											${submitProtocolForm.map.file.domainFile.uri }</a>
										<html:hidden property="file.domainFile.uri" />
										<html:hidden property="file.domainFile.name" />
									</c:if> </span>&nbsp;
							</td>
						</tr>
						<c:if
							test="${!empty submitProtocolForm.map.file.domainFile.version}">
							<html:hidden property="file.domainFile.version" />
						</c:if>
						<html:hidden styleId="updatedUri" property="file.updatedFileUri" />
						<html:hidden styleId="updatedName" property="file.updatedFileName" />
						<html:hidden styleId="updatedVersion"
							property="file.updatedFileVersion" />
						<tr>
							<td class="leftLabel">
								<strong>File Title</strong>
							</td>
							<td class="rightLabel">
								<html:text styleId="fileTitle" property="file.domainFile.title"
									size="80" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Description</strong>
							</td>
							<td class="rightLabel">
								<html:textarea styleId="fileDescription"
									property="file.domainFile.description" rows="3" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Visibility</strong>
							</td>
							<td class="rightLabel">
								<html:select property="file.visibilityGroups" multiple="true"
									size="6">
									<html:options name="allVisibilityGroups" />
								</html:select>
								<br>
								<i>(${applicationOwner}_Researcher and
									${applicationOwner}_DataCurator are always selected by
									default.)</i>
							</td>
						</tr>
					</tbody>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<table width="498" height="15" border="0" align="right"
								cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="15">
										<div align="right">
											<div align="right">
												<input type="reset" value="Reset"
													onclick="javascript:location.reload()">
												<input type="hidden" name="dispatch" value="create">
												<input type="hidden" name="page" value="2">
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
			</td>
		</tr>
	</table>
</html:form>
