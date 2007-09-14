<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<form name="resultForm">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Sample Search Results
				</h3>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=sample_search_results')"
					class="helpText">Help</a>&nbsp;&nbsp;
				<a href="searchSample.do?dispatch=setup&page=0" class="helpText">back</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=inventory" />
				<display:table name="sessionScope.sampleContainers" id="container"
					requestURI="searchSample.do" pagesize="25" class="displaytable"
					decorator="gov.nih.nci.calab.dto.inventory.ContainerDecorator">
					<display:column title="Select" property="containerId" />
					<display:column title="Sample ID" property="sample.sortableName"
						sortable="true" />
					<display:column title="Container Name" property="sortableName"
						sortable="true" />
					<display:column title="Sample Accession<br>Date"
						property="sample.accessionDate" sortable="true"
						format="{0,date,MM-dd-yyyy}" />
					<display:column title="Sample Type" property="sample.sampleType"
						sortable="true" />
					<display:column title="Sample Location" property="storageLocation"
						sortable="true" />
					<display:column title="Sample Submitter"
						property="sample.sampleSubmitter" sortable="true" />
					<%--
	<display:column title="Actions">
		<c:url var="viewSampleDetailURL" value="/viewSampleDetail.do">
			<c:param name="sampleId" value="${container.sample.sampleId}" />
			<c:param name="containerNum" value="${container.containerNumber}" />
			<c:param name="isAliquot" value="false" />
		</c:url>
		<a href="${viewSampleDetailURL}">View Details</a>
		<c:url value="/searchAliquot.do" var="showAliquotURL">
			<c:param name="searchName" value="${container.sample.sampleName}-*" />
			<c:param name="fromSampleResult" value="true" />
		</c:url>
		<a href="${showAliquotURL}">Show Aliquots</a> &nbsp;
	</display:column>
   --%>
				</display:table>
				<div align="right">
					<input type="button" value="View Details"
						onclick="javascript:submitAction(document.resultForm, 'viewSampleDetail.do')">
					<input type="hidden" name="dispatch" value="search">
					<input type="hidden" name="page" value="1">
					<input type="hidden" name="fromSampleResult" value="true">
					<input type="button" value="Show Aliquots"
						onclick="javascript:submitAction(document.resultForm, 'searchAliquot.do')">
				</div>
			</td>
		</tr>
	</table>
</form>
