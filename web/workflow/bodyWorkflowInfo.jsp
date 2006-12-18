<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<br>
<%--define variables needed for menu links --%>
<c:choose>
	<c:when test="${!empty param.menuType}">
		<c:set var="menuType" value="${param.menuType}" scope="session" />
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${!empty param.inout}">
		<c:set var="inout" value="${param.inout}" scope="session" />
	</c:when>
</c:choose>

<%-- remove runName and runId when menuType is assay --%>
<c:choose>
	<c:when test="${menuType eq 'assay'}">
		<c:remove var="runId" scope="session" />
		<c:remove var="runName" scope="session" />
	</c:when>
</c:choose>
<center>
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right" width="15%" colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="formTitle" colspan="2" align="center">
				General Information for the workflow
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldGrey">
				<b>Source Name</b>
			</td>
			<td class="formFieldGrey">
				<c:out value="${currentRun.sampleSourceName}" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldWhite">
				<b>Assay Type</b>
			</td>
			<td class="formFieldWhite">
				<c:out value="${currentRun.assayBean.assayType}" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldGrey">
				<b>Assay Name</b>
			</td>
			<td class="formFieldGrey">
				<c:out value="${currentRun.assayBean.assayName}" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldWhite">
				<b>Run Name</b>
			</td>
			<td class="formFieldWhite">
				<c:out value="${currentRun.name}" />
				&nbsp;
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<br>
				<jsp:include page="/bodyMessage.jsp?bundle=workflow" />
			</td>
	</table>
	<br>
</center>
