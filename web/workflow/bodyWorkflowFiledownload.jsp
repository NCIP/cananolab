<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<center>
	<table width="80%" align="center">
		<tr>
			<td width="10%">
				&nbsp;
			</td>
			<td>
				<br>
				<h3>
					Download Files
				</h3>
			</td>
			<td align="right" width="10%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=file_download')" class="helpText">Help</a>
			</td>
	</table>
	<jsp:include page="/workflow/bodyWorkflowInfo.jsp" />
	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td colspan="3" class="formTitle" align="center">
				Uploaded
				<bean:write name="fileDownloadForm" property="inout" />
				Files
			</td>
		<tr>
		<tr>
			<td class="dataTableSecondaryLabel">
				File Name
			</td>
			<td class="dataTableSecondaryLabel">
				Uploaded Date
			</td>
			<td class="dataTableSecondaryLabel" align="right">
				Download Action
			</td>
		</tr>
		<%int i = 0;

			%>
		<logic:iterate id="fileInfo" name="fileDownloadForm" property="fileInfoList">
			<tr>
				<td class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>>
					<bean:write name="fileInfo" property="fileName" />
				</td>
				<td class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>>
					<bean:write name="fileInfo" property="uploadDate" />
				</td>
				<td align="right" class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>>
					<b> <a href="<bean:write name='fileInfo' property='action'/>"> Download </a> </b>
				</td>
			</tr>
			<%i++;

			%>
		</logic:iterate>
		<tr>
			<td colspan="3">
			</td>
		</tr>
		<tr>
			<td colSpan="3" align="right" class="leftBorderedFormFieldWhite">
				<b> <%if (i > 0) {%> <a href="<bean:write name='fileDownloadForm' property='downloadAll'/>"> Download All Files </a> <%}

		%> </b>
			</td>
		</tr>
	</table>
</center>
