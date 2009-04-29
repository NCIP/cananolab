<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript">
<!--//
function confirmDeletion()
{
	answer = confirm("Are you sure you want to delete the data set?")
	if (answer !=0)
	{
		this.document.forms[0].dispatch.value="deleteFinding";
		this.document.forms[0].page.value="1";
		this.document.forms[0].submit();
		return true;
	}
}
//-->
</script>
<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th colspan="2">
			Finding Info
		</th>
	</tr>
	<tr>
		<td>
			<span class="cellLabel">File</span> &nbsp;&nbsp;
			<a id="addFileButton"
				href="javascript:clearFile(); show('newFile');hide('newMatrix');">Add</a>
		</td>
		<td>
			<span class="cellLabel">Data and Conditions</span> &nbsp;&nbsp;
			<a id="addData"
				href="javascript:clearFile(); show('newMatrix');hide('newFile');">Add</a>
		</td>
	</tr>
	<tr>
		<td valign="top" colspan="2">
			<div style="display: none" id="newFile">
				<jsp:include page="bodySubmitCharacterizationFile.jsp" />
				&nbsp;
			</div>
		</td>
	</tr>
	<tr>
		<td valign="top" colspan="2">
			<c:set var="submitMatrixStyle" value="display:none" />
			<c:if test="${param.dispatch eq 'drawMatrix'}">
				<c:set var="submitMatrixStyle" value="display:block" />
			</c:if>
			<div style="${submitMatrixStyle}" id="newMatrix">
				<jsp:include page="bodySubmitDataConditionMatrix.jsp" />
				&nbsp;
			</div>
		</td>
	</tr>
	<tr>
		<td id="existingFiles">
			<table class="summaryViewLayer4" border="1" id="fileTable">
				<tbody id="fileRows">
					<tr id="pattern" style="display: none;">
						<td>
							<span id="fileUri">File</span>
						</td>
						<td>
							<input class="noBorderButton" id="edit" type="button"
								value="Edit"
								onclick="editClicked(this.id); show('patternAddRow');" />
						</td>
					</tr>
					<c:forEach var="file"
						items="${characterizationForm.map.achar.theFinding.files}">
						<tr>
							<td>
								${file.domainFile.uri}
							</td>
							<td>
								<a href="">Edit</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</td>
		<td>
		</td>
	</tr>
	<tr>
		<td>
			<input type="button" value="Delete" id="deleteFinding"
				style="display: none;" onclick="javascript:confirmDeletion()">
		</td>
		<td>
			<div align="right">
				<input type="button" value="Cancel"
					onclick="javascript:hide('newFinding'); show('existingFinding');">
				<c:if
					test="${fn:length(characterizationForm.map.achar.theFinding.files)>0 || fn:length(characterizationForm.map.achar.theFinding.rows)>0}">
					<input type="button" value="Save"
						onclick="javascript:saveFinding('characterization');">
				</c:if>
			</div>
		</td>
	</tr>
</table>
