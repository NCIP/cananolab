<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<br>
<br>
<center>
	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td class="formTitle" colspan="2" align="center">
				General Information for the workflow
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldGrey">
				<b>Assay Type</b>
			</td>
			<td class="leftBorderedFormFieldGrey">
				<bean:write name="maskFileForm" property="assayType" />
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldWhite">
				<b>Assay</b>
			</td>
			<td class="leftBorderedFormFieldWhite">
				<bean:write name="maskFileForm" property="assayName" />
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldGrey">
				<b>Run</b>
			</td>
			<td class="leftBorderedFormFieldGrey">
				<bean:write name="maskFileForm" property="runName" />
			</td>
		</tr>

	</table>
	<br>
	<br>
	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td colspan="3" class="formTitle" align="center">
				Uploaded
				<bean:write name="maskFileForm" property="inout" />
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
				Mask Action
			</td>
		</tr>
		<%int i = 0;

			%>
		<logic:iterate id="file" name="filesToMask">
			<tr>
				<td class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>>
					<bean:write name="file" property="filename" />
				</td>
				<td class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>>
					<bean:write name="file" property="createdDate" />
				</td>
				<c:url var="maskFileURL" value="/preMaskFile.do">
					<c:param name="runId" value="${param.runId}"/>
					<c:param name="method" value="file"/>
					<c:param name="inout" value="${param.inout}"/>
					<c:param name="fileId" value="${file.id}"/>
					<c:param name="fileName" value="${file.filename}"/>
				</c:url>
				<td align="right" class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>>
					<b><a href="${maskFileURL}">Mask </a> </b>
				</td>
			</tr>
		</logic:iterate>
	</table>
</center>
