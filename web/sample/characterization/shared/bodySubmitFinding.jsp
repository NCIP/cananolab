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
		<td colspan="2">
			<span class="cellLabel">File</span> &nbsp;&nbsp;
			<a id="addFileButton"
				href="javascript:clearFile(); show('newFile');hide('newMatrix');">Add</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:forEach var="file"
				items="${characterizationForm.map.achar.theFinding.files}">
						${file.domainFile.uri}
								<a href="">Edit</a>
				<br>
			</c:forEach>
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
			<input type="button" value="Delete" id="deleteFinding"
				style="display: none;" onclick="javascript:confirmDeletion()">
		</td>
		<td>
			<div align="right">
				<input type="button" value="Cancel"
					onclick="javascript:hide('newFinding');">
				<c:if
					test="${fn:length(characterizationForm.map.achar.theFinding.files)>0 || fn:length(characterizationForm.map.achar.theFinding.rows)>0}">
					<input type="button" value="Save"
						onclick="javascript:saveFinding('characterization');">
				</c:if>
			</div>
		</td>
	</tr>
</table>
