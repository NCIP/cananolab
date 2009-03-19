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
<table border="0" align="center" cellpadding="3" cellspacing="0"
	width="100%" class="topBorderOnly" summary="">
	<tr>
	<tr class="topBorder">
		<td class="formTitle" colspan="4">
			<div align="justify">
				Results
			</div>
		</td>
	</tr>
	<tr>
		<td class="completeLabelNoBottom" valign="top" colspan="4">
			<strong>DataSet</strong>&nbsp;&nbsp;
			<a id="addDataSet" href="javascript:resetTheDataSet(true);"><img
					align="top" src="images/btn_add.gif" border="0" /> </a>
		</td>
	</tr>
	<tr>
		<td class="completeLabelNoTop" valign="top" colspan="4">
			<div id="existingDataSet" style="display: block;">
				<c:set var="charBean" value="${characterizationForm.map.achar}" />
				<c:set var="edit" value="true" />
				<%@ include
					file="/sample/characterization/shared/bodyDataSetView.jsp"%>&nbsp;
			</div>
			<div id="newDataSet" style="display: none;">
				<table class="smalltable2" border="0" width="90%" align="center">
					<tr>
						<td class="subformTitle" colspan="2">
							<b>New DataSet</b>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<b>Data</b>&nbsp;&nbsp;
							<a href="javascript:showhide('submitDatum');">add</a>
						</td>
					</tr>
					<tr>
						<td valign="top" colspan="2">
							<div style="display: none" id="submitDatum" class="indented4">
								<jsp:include
									page="/sample/characterization/shared/bodyDataSetEdit.jsp" />
								&nbsp;
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<b>File</b>&nbsp;&nbsp;
							<a href="javascript:showhide('loadDatumFile');">add</a>
						</td>
					</tr>
					<tr>
						<td valign="top" colspan="2">
							<div style="display: none" id="loadDatumFile" class="indented4">
								<jsp:include
									page="/sample/characterization/shared/bodyCharacterizationFile.jsp" />
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
