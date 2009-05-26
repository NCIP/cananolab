<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th colspan="2">
			Finding Info
		</th>
	</tr>
	<tr>
		<td colspan="2">
			<span class="cellLabel">File</span> &nbsp;&nbsp;
			<a href="javascript:clearFile();show('newFile');">Add</a>
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
			<c:set var="newFileStyle" value="display:none" />
			<c:if
				test="${param.dispatch eq 'addFile' || fn:length(characterizationForm.map.achar.theFinding.files)>0}">
				<c:set var="newFileStyle" value="display:none" />
			</c:if>
			<div style="${newFileStyle }" id="newFile">
				<c:set var="fileParent" value="achar.theFinding" />
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
			<c:if
				test="${!empty characterizationForm.map.achar.theFinding.domain.id}">
				<input type="button" value="Delete" id="deleteFinding"
					onclick="javascript:deleteFinding(characterizationForm);">
			</c:if>
		</td>
		<td>
			<div align="right">
				<input type="button" value="Cancel"
					onclick="javascript:hide('newFinding');">
				<input type="button" value="Save"
					onclick="javascript:saveFinding(characterizationForm);">
			</div>
		</td>
	</tr>
</table>

