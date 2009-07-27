<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="fileParent" value="achar.theFinding" />
<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th colspan="2">
			Finding Info
		</th>
	</tr>
	<tr>
		<td class="cellLabel" width="15%">
			File
		</td>
		<td>
			<a style="display: block" id="addFile"
				href="javascript:clearFile();openSubmissionForm('File');">Add</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table class="submissionView" width="85%" align="center">
				<c:forEach var="file"
					items="${characterizationForm.map.achar.theFinding.files}"
					varStatus="ind">
					<tr>
						<td>
							${file.domainFile.uri}
						</td>
						<td>
							<a href="javascript:setTheFile(${ind.count-1})">Edit</a>
						</td>
					</tr>
				</c:forEach>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div style="display: none" id="newFile">
				<c:set var="fileForm" value="characterizationForm" />
				<c:set var="actionName" value="characterization" />
				<c:set var="theFile"
					value="${characterizationForm.map.achar.theFinding.theFile}" />
				<%@include file="../../bodySubmitFile.jsp"%>
				&nbsp;
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<span class="cellLabel">Data and Conditions</span> &nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td valign="top" colspan="2">
			<div id="newMatrix">
				<jsp:include page="bodySubmitDataConditionMatrix.jsp" />
				&nbsp;
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<c:if test="${not empty characterizationForm.map.achar.theFinding.domain.id}">
				<input type="button" value="Delete" id="deleteFinding"
					onclick="javascript:deleteTheFinding(characterizationForm);">
			</c:if>
		</td>
		<td>
			<div align="right">
				<c:if test="${not empty characterizationForm.map.achar.theFinding.domain.id}">
					<input type="hidden" value="${characterizationForm.map.achar.theFinding.domain.id}" />
				</c:if>
				<input type="button" value="Save"
					onclick="javascript:saveFinding(characterizationForm);">
				<input type="button" value="Cancel"
					onclick="javascript:closeSubmissionForm('Finding');">
			</div>
		</td>
	</tr>
</table>

