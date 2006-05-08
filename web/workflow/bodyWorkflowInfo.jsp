<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
			<td class="formFieldGrey">
				<c:out value="${param.assayType}" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldWhite">
				<b>Assay Name</b>
			</td>
			<td class="formFieldWhite">
				<c:out value="${param.assayName}" />
				&nbsp;
			</td>
		</tr>
		<logic:present parameter="runName">
			<tr>
				<td class="leftBorderedFormFieldGrey">
					<b>Run</b>
				</td>
				<td class="formFieldGrey">
					<c:out value="${param.runName}" />
					&nbsp;
				</td>
			</tr>
		</logic:present>
		<tr>
			<td colspan="2">
				<br>
				<jsp:include page="/bodyMessage.jsp?bundle=workflow" />
			</td>
	</table>
	<br>
</center>
