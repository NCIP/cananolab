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
				href="javascript:clearFile(); show('submitFile');hide('submitData');">Add</a>
		</td>
		<td>
			<span class="cellLabel">Data and Conditions</span> &nbsp;&nbsp;
			<a id="addData"
				href="javascript:clearFile(); show('submitData');hide('submitFile');">Add</a>
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
			<table class="summaryViewLayer4" border="1">
				<tr>
					<c:forEach var="col"
						items="${characterizationForm.map.achar.theFinding.columnBeans}">
						<td>
							<strong>${col.columnLabel}</strong>
						</td>
					</c:forEach>
				</tr>
				<c:forEach var="row"
					items="${characterizationForm.map.achar.theFinding.rows}">
					<tr>
						<c:forEach var="condition" items="${row.conditions}">
							<td>
								${condition.value}
							</td>
						</c:forEach>
						<c:forEach var="datum" items="${row.data}">
							<td>
								${datum.value}
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top" colspan="2">
			<div style="display: none" id="submitFile">
				<jsp:include page="bodySubmitCharacterizationFile.jsp" />
				&nbsp;
			</div>
		</td>
	</tr>
	<tr>
		<td valign="top" colspan="2">
			<div style="display: none" id="submitData">
				<jsp:include page="bodySubmitData.jsp" />
				&nbsp;
			</div>
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
				<input type="button" value="Save"
					onclick="javascript:saveFinding('characterization');">
			</div>
		</td>
	</tr>
</table>
