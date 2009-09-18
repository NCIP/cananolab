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

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${action} Protocol" />
	<jsp:param name="topic" value="submit_protocol_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/submitProtocol" enctype="multipart/form-data">
	<jsp:include page="/bodyMessage.jsp?bundle=protocol" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel">
				Protocol Type*
			</td>
			<td colspan="3">
				<div id="protocolTypePrompt">
					<html:select styleId="protocolType" property="protocol.domain.type"
						onchange="javascript:callPrompt('Protocol Type', 'protocolType', 'protocolTypePrompt'); retrieveProtocols();">
						<option value="" />
							<html:options name="protocolTypes" />
						<option value="other">
							[other]
						</option>
					</html:select>
				</div>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Protocol Name*
			</td>
			<td colspan="3">
				<div id="protocolNamePrompt">
					<html:select styleId="protocolName" property="protocol.domain.name"
						onchange="javascript:callPrompt('Protocol Name', 'protocolName', 'protocolNamePrompt');">
						<option value="" />
							<c:if test="${!empty protocolsByType}">
								<html:options collection="protocolsByType"
									property="domain.name" labelProperty="domain.name" />
							</c:if>
						<option value="other">
							[other]
						</option>
					</html:select>
				</div>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Protocol Version*
			</td>
			<td>
				<div id="protocolVersionPrompt">
					<html:select styleId="protocolVersion"
						property="protocol.domain.version"
						onchange="javascript:callPrompt('Protocol Version', 'protocolVersion', 'protocolVersionPrompt');retrieveProtocol();">
						<option value="" />
							<c:if test="${!empty protocolsByType}">
								<html:options collection="protocolsByType"
									property="domain.version" labelProperty="domain.version" />
							</c:if>
						<option value="other">
							[other]
						</option>
					</html:select>
				</div>
			</td>
			<td class="cellLabel" width="20%">
				Protocol Abbreviation
			</td>
			<td>
				<html:text styleId="protocolAbbreviation"
					property="protocol.domain.abbreviation" size="30" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Protocol File
			</td>
			<td colspan="3">
				<html:file property="protocol.fileBean.uploadedFile"
					onchange="javascript:writeLink(null);" />
				&nbsp;&nbsp;
				<span id="protocolFileLink"> <c:if
						test="${!empty submitProtocolForm.map.protocol.fileBean.domainFile.uri }">&nbsp;&nbsp;
									<a
							href="searchProtocol.do?dispatch=download&amp;fileId=${submitProtocolForm.map.protocol.fileBean.domainFile.id}&amp;location=${applicationOwner}">
							${submitProtocolForm.map.protocol.fileBean.domainFile.uri }</a>
					</c:if> </span>&nbsp;
			</td>
			<html:hidden property="protocol.domain.id" styleId="protocolId" />
		</tr>
		<tr>
			<td class="cellLabel">
				File Title
			</td>
			<td colspan="3">
				<html:text styleId="fileTitle"
					property="protocol.fileBean.domainFile.title" size="100" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Description
			</td>
			<td colspan="3">
				<html:textarea styleId="fileDescription"
					property="protocol.fileBean.domainFile.description" rows="3"
					cols="80" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Visibility
			</td>
			<td colspan="3">
				<html:select property="protocol.visibilityGroups" multiple="true"
					size="6" styleId="visibility">
					<html:options name="allVisibilityGroups" />
				</html:select>
				<br>
				<i>(${applicationOwner}_Researcher and
					${applicationOwner}_DataCurator are always selected by default.)</i>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td width="30%">
				<table width="498" height="15" border="0" align="right"
					cellpadding="4" cellspacing="0">
					<tr>
						<td width="490" height="15">
							<div align="right">
								<div align="right">
									<input type="reset" value="Reset"
										onclick="javascript:location.href='submitProtocol.do?dispatch=setup&page=0'">
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
</html:form>
