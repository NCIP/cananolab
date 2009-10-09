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
		<td class="cellLabel" width="15%">
			File 
		</td>
		<td>
			<c:set var="addFileButtonStyle" value="display:block" />
			<c:if test="${openFile eq 'true'}">
				<c:set var="addFileButtonStyle" value="display:none" />
			</c:if>
			<a style="${addFileButtonStyle}" id="addFile"
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
			<c:set var="newFileStyle" value="display:none" />
			<c:if test="${openFile eq 'true'}">
				<c:set var="newFileStyle" value="display:block" />
			</c:if>
			<div style="${newFileStyle}" id="newFile">
				<c:set var="fileForm" value="characterizationForm" />
				<c:set var="fileParent" value="achar.theFinding" />
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
			<html:text property="achar.theFinding.numberOfColumns" size="1"
				styleId="colNum" onkeydown="return filterInteger(event)" />
			columns
			<html:text property="achar.theFinding.numberOfRows" size="1"
				styleId="rowNum" onkeydown="return filterInteger(event)" />
			rows &nbsp;&nbsp;
			<a href="javascript:updateMatrix(characterizationForm)">Update</a>
			<input type="hidden" name="numberOfColumns" value="${characterizationForm.map.numberOfColumns}">
			<input type="hidden" name="numberOfRows" value="${characterizationForm.map.numberOfRows}">
		</td>
	</tr>
	<tr>
		<td valign="top" colspan="2">
			<div id="newMatrix">
				<jsp:include page="bodySubmitDataConditionMatrix.jsp" />
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<c:if
				test="${not empty characterizationForm.map.achar.theFinding.domain.id}">
				<input type="button" value="Remove" id="deleteFinding"
					onclick="javascript: deleteTheFinding(characterizationForm);">
			</c:if>
		</td>
		<td>
			<div align="right">
				<c:if
					test="${not empty characterizationForm.map.achar.theFinding.domain.id}">
					<input type="hidden"
						value="${characterizationForm.map.achar.theFinding.domain.id}" />
				</c:if>
				<html:hidden property="fileSupplied" 
					value="${characterizationForm.map.fileSupplied}" />
				<input type="button" value="Save"
					onclick="javascript:saveFinding('characterization');">
				<input type="button" value="Cancel"	onclick="javascript:closeSubmissionForm('Finding');">
			</div>
		</td>
	</tr>
</table>

