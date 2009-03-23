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
		this.document.forms[0].dispatch.value="deleteDataSet";
		this.document.forms[0].page.value="1";
		this.document.forms[0].submit();
		return true;
	}
}
//-->
</script>
<table width="100%" align="center" class="submissionView">
	<tr>
		<th>
			Results
		</th>
	</tr>
	<tr>
		<td class="cellLabel">
			DataSet&nbsp;&nbsp;
			<a id="addDataSet" href="javascript:resetTheDataSet(true);"><img
					align="top" src="images/btn_add.gif" border="0" /> </a>
		</td>
	</tr>
	<tr>
		<td>
			<c:if test="${! empty characterizationForm.map.achar.dataSets }">
				<c:set var="charBean" value="${characterizationForm.map.achar}" />
				<c:set var="edit" value="true" />
				<%@ include file="bodyDataSetView.jsp"%>
			</c:if>
		</td>
	<tr>
		<td>
			<div id="newDataSet" style="display: none;">
				<table class="summaryViewLayer4" width="85%" align="center">
					<tr>
						<td colspan="2">
							<b>Data</b>&nbsp;&nbsp;
							<a href="javascript:showhide('submitDatum');">show/hide</a>
						</td>
					</tr>
					<tr>
						<td valign="top" colspan="2">
							<div style="display: block" id="submitDatum">
								<jsp:include page="bodySubmitData.jsp" />
								&nbsp;
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<b>File</b>&nbsp;&nbsp;
							<a href="javascript:showhide('loadDatumFile');">show/hide</a>
						</td>
					</tr>
					<tr>
						<td valign="top" colspan="2">
							<div style="display: none" id="loadDatumFile">
								<jsp:include page="bodySubmitCharacterizationFile.jsp" />
								&nbsp;
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<input type="button" value="Delete"
								onclick="javascript:confirmDeletion()">
						</td>
						<td class="rightLabel" align="right" colspan="1">
							<div align="right">
								<input type="button" value="Cancel"
									onclick="javascript:hide('newDataSet'); show('existingDataSet');">
								<input type="button" value="Save"
									onclick="javascript:saveDataSet('characterization');">
							</div>
						</td>
					</tr>
				</table>
	</div>
	<br>
	</td>
	</tr>
</table>
<br>
