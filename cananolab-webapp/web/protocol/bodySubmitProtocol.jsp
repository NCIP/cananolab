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
<html:form action="/protocol" enctype="multipart/form-data">
	<jsp:include page="/bodyMessage.jsp?bundle=protocol" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel">
				Protocol Type*
			</td>
			<td>
				<div id="protocolTypePrompt">
					<html:select styleId="protocolType" property="protocol.domain.type"
						onchange="javascript:callPrompt('Protocol Type', 'protocolType', 'protocolTypePrompt'); retrieveProtocolNames();">
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
			<td>
				<div id="protocolNamePrompt">
					<html:select styleId="protocolName" property="protocol.domain.name"
						onchange="javascript:callPrompt('Protocol Name', 'protocolName', 'protocolNamePrompt'); retrieveProtocolVersions()">
						<option value="" />
							<c:if test="${!empty protocolNamesByType}">
								<html:options name="protocolNamesByType" />
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
						onchange="javascript:callPrompt('Protocol Version', 'protocolVersion', 'protocolVersionPrompt');retrieveProtocol('${applicationOwner}');">
						<option value="" />
							<c:if test="${!empty protocolVersionsByTypeName}">
								<html:options name="protocolVersionsByTypeName" />
							</c:if>
							<option value="other">
								[other]
							</option>
					</html:select>
				</div>
			</td>
		</tr>
		<tr>
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
			<td>
				<html:file property="protocol.fileBean.uploadedFile"
					onchange="javascript:writeLink(null);" />
				&nbsp;&nbsp;
				<span id="protocolFileLink"> <c:if
						test="${!empty protocolForm.map.protocol.fileBean.domainFile.uri }">&nbsp;&nbsp;
									<a
							href="protocol.do?dispatch=download&amp;fileId=${protocolForm.map.protocol.fileBean.domainFile.id}">
							${protocolForm.map.protocol.fileBean.domainFile.uri }</a>
					</c:if> </span>&nbsp;
			</td>
			<html:hidden property="protocol.domain.id" styleId="protocolId" />
			<html:hidden property="protocol.fileBean.domainFile.id"
				styleId="fileId" />
			<html:hidden property="protocol.fileBean.domainFile.uri"
				styleId="fileUri" />
			<html:hidden property="protocol.fileBean.domainFile.name"
				styleId="fileName" />
		</tr>
		<tr>
			<td class="cellLabel">
				File Title
			</td>
			<td>
				<html:text styleId="fileTitle"
					property="protocol.fileBean.domainFile.title" size="100" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Description
			</td>
			<td>
				<html:textarea styleId="fileDescription"
					property="protocol.fileBean.domainFile.description" rows="3"
					cols="80" />
			</td>
		</tr>
		<c:set var="groupAccesses"
			value="${protocolForm.map.protocol.groupAccesses}" />
		<c:set var="userAccesses"
			value="${protocolForm.map.protocol.userAccesses}" />
		<c:set var="accessParent" value="protocol" />
		<c:set var="dataType" value="Protocol" />
		<c:set var="parentAction" value="protocol" />
		<c:set var="parentForm" value="protocolForm" />
		<c:set var="parentPage" value="2"/>
		<c:set var="protectedData"
			value="${protocolForm.map.protocol.domain.id}" />
		<c:set var="newData" value="true"/>
		<c:if test="${updateProtocol}">
		   <c:set var="newData" value="false"/>
		</c:if>
		<c:set var="isPublic" value="${protocolForm.map.protocol.publicStatus}"/>
		<c:set var="isOwner" value="${protocolForm.map.protocol.userIsOwner}"/>
		<c:set var="ownerName" value="${protocolForm.map.protocol.domain.createdBy}"/>
		<%@include file="../bodyManageAccessibility.jsp"%>
	</table>
	<br>
	<c:set var="updateId" value="${param.protocolId}" />
	<c:set var="resetOnclick" value="this.form.reset();" />
	<c:set var="deleteButtonName" value="Delete" />
	<c:set var="deleteOnclick"
		value="deleteData('protocol', protocolForm, 'protocol', 'delete')" />
	<c:set var="hiddenDispatch" value="create" />
	<c:set var="hiddenPage" value="1" />
	<c:if test="${review}">
		<c:set var="submitForReviewOnclick"
			value="submitReview(protocolForm, 'protocol', '${protocolForm.map.protocol.domain.id}', '${protocolForm.map.protocol.domain.name}', 'protocol')" />
	</c:if>
	<c:set var="validate" value="false" />
	<c:if test="${!user.curator && protocolForm.map.protocol.publicStatus}">
		<c:set var="validate" value="true" />
	</c:if>
	<c:if test="${protocolForm.map.protocol.userDeletable && !empty param.protocolId}">
	   <c:set var="showDelete" value="true"/>
	</c:if>
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>
