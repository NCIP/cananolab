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
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=sample_search_results')" class="helpText">Help</a> &nbsp;&nbsp;
				<c:choose>
					<c:when test="${empty param.fromSampleResult}">
						<a href="searchAliquot.do?dispatch=setup&page=0&rememberSearch=true" class="helpText">back</a>
					</c:when>
					<c:otherwise>
						<a href="sampleResultForward.do" class="helpText">back</a>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=search" />

				<display:table name="sessionScope.aliquots" id="aliquot" class="displaytable" pagesize="25" requestURI="searchAliquot.do">
					<display:column title="Select">
						<input type="radio" name="aliquotId" value="${aliquot.aliquotId}" checked>
					</display:column>
					<display:column title="Sample<br>ID" property="sample.sortableName" sortable="true" />
					<display:column title="Sample<br>Accession<br>Date" property="sample.accessionDate" sortable="true" format="{0,date,MM-dd-yyyy}" />
					<display:column title="Sample<br>Submitter" property="sample.sampleSubmitter" sortable="true" />
					<display:column title="Aliquot<br>ID" property="sortableName" sortable="true" />
					<display:column title="Aliquoted<br>Date" property="creationDate" sortable="true" format="{0,date,MM-dd-yyyy}" />
					<display:column title="Aliquot<br>Location" property="container.storageLocation" sortable="true" />
					<display:column title="Aliquot<br>Creator" property="creator" sortable="true" />
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
						<input type="button" value="View Details" onclick="javascript:submitAction(document.resultForm, 'viewAliquotDetail.do')">
					</div>
				</logic:present>
			</td>
		</tr>
	</table>
</form>

<%--
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<div align="center">
		<td width="55" class="formTitle">
			Sample ID
		</td>
		<td width="76" class="formTitle">
			Sample Accessioned Date
		</td>
		<td width="41" class="formTitle">
			Sample Type
		</td>
		<td width="44" class="formTitle">
			Sample Submitter
		</td>
		<td width="75" class="formTitle">
			Aliquot ID
		</td>
		<td width="76" class="formTitle">
			Aliquoted Date
		</td>
		<td width="76" class="formTitle">
			Aliquoted Location
		</td>
		<td width="76" class="formTitle">
			Aliquoted Creator
		</td>
		<td width="82" class="formTitle">
			Actions
		</td>
	</tr>
	<logic:iterate name="aliquots" id="aliquot" type="gov.nih.nci.calab.dto.inventory.AliquotBean" indexId="aliquotNum">
		<c:choose>
			<c:when test="${aliquotNum % 2 == 0}">
				<c:set var="style" value="formFieldGrey" />
				<c:set var="style0" value="leftBorderedFormFieldGrey" />
			</c:when>
			<c:otherwise>
				<c:set var="style" value="formFieldWhite" />
				<c:set var="style0" value="leftBorderedFormFieldWhite" />
			</c:otherwise>
		</c:choose>
		<tr>
			<td class="${style0}">
				<bean:write name="aliquot" property="sample.sampleName" />
				&nbsp;
			</td>
			<td class="${style}">
				<bean:write name="aliquot" property="sample.accessionDate" />
				&nbsp;
			</td>
			<td class="${style}">
				<bean:write name="aliquot" property="sample.sampleType" />
				&nbsp;
			</td>
			<td class="${style}">
				<bean:write name="aliquot" property="sample.sampleSubmitter" />
				&nbsp;
			</td>
			<td class="${style}">
				<bean:write name="aliquot" property="aliquotName" />
				&nbsp;
			</td>
			<td class="${style}">
				<bean:write name="aliquot" property="creationDate" />
				&nbsp;
			</td>
			<td class="${style}">
				<bean:write name="aliquot" property="container.storageLocationStr" />
				&nbsp;
			</td>
			<td class="${style}">
				<bean:write name="aliquot" property="creator" />
				&nbsp;
			</td>
			<td class="${style}">
				<c:url var="viewAliquotDetailURL" value="/viewSampleDetail.do">
					<c:param name="aliquotNum" value="${pageScope.aliquotNum}" />
					<c:param name="isAliquot" value="true" />
				</c:url>
				<a href="${viewAliquotDetailURL}">View Details</a>&nbsp;
			</td>
		</tr>
	</logic:iterate>
</table>
--%>
