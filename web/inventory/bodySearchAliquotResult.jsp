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
					Aliquot Search Results
				</h3>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=aliquot_search_results_help')"
					class="helpText">Help</a> &nbsp;&nbsp;
				<c:choose>
					<c:when test="${empty param.fromSampleResult}">
						<a href="searchAliquot.do?dispatch=setup&page=0" class="helpText">back</a>
					</c:when>
					<c:otherwise>
						<a href="sampleResultForward.do" class="helpText">back</a>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=inventory" />

				<display:table name="sessionScope.aliquots" id="aliquot"
					class="displaytable" pagesize="25" requestURI="searchAliquot.do"
					decorator="gov.nih.nci.calab.dto.inventory.AliquotDecorator">
					<display:column title="Select" property="aliquotId"/>
					<display:column title="Sample<br>ID" property="sample.sortableName"
						sortable="true" />
					<display:column title="Sample<br>Accession<br>Date"
						property="sample.accessionDate" sortable="true"
						format="{0,date,MM-dd-yyyy}" />
					<display:column title="Sample<br>Submitter"
						property="sample.sampleSubmitter" sortable="true" />
					<display:column title="Aliquot<br>ID" property="sortableName"
						sortable="true" />
					<display:column title="Aliquoted<br>Date" property="creationDate"
						sortable="true" format="{0,date,MM-dd-yyyy}" />
					<display:column title="Aliquot<br>Location"
						property="container.storageLocation" sortable="true" />
					<display:column title="Aliquot<br>Creator" property="creator"
						sortable="true" />
					<%--
	<display:column title="Actions">
		<c:url var="viewAliquotDetailURL" value="/viewSampleDetail.do">
			<c:param name="aliquotNum" value="${aliquot_rowNum-1}" />
			<c:param name="isAliquot" value="true" />
		</c:url>
		<a href="${viewAliquotDetailURL}">View Details</a>&nbsp;</display:column>
	--%>
				</display:table>
				<logic:present name="aliquots">
					<div align="right">
						<input type="button" value="View Details"
							onclick="javascript:submitAction(document.resultForm, 'viewAliquotDetail.do')">
					</div>
				</logic:present>
			</td>
		</tr>
	</table>