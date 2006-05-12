<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="90%" align="center">
	<tr>
		<td width="10%">
			&nbsp;
		</td>
		<td>
			<h3>
				<br>
				Sample Search Results
			</h3>
		</td>
		<td align="right" width="10%">
			<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=sample_search_results')" class="helpText">Help</a>
		</td>
</table>
<jsp:include page="/bodyMessage.jsp?bundle=search" />
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="bottom">
		<td align="right">
			<c:choose>
				<c:when test="${empty param.fromSampleResult}">
					<input type="button" onClick="javascript:location.href='initSession.do?forwardPage=searchSample&rememberSearch=true'" value="Back">
				</c:when>
				<c:otherwise>
					<input type="button" onClick="javascript:history.go(-1)" value="Back">
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<br>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td width="55" class="formTitle">
			Sample ID
		</td>
		<td width="76" class="formTitle">
			Sample Accessioned Date <strong></strong>
		</td>
		<td width="41" class="formTitle">
			Sample Type
		</td>
		<td width="44" class="formTitle">
			Sample Location
		</td>
		<td width="48" class="formTitle">
			Sample Creator
		</td>
		<td width="102" class="formTitle">
			Actions
		</td>
	</tr>

	<c:set var="rowNum" value="-1" />
	<logic:iterate name="samples" id="sample" type="gov.nih.nci.calab.dto.administration.SampleBean" indexId="sampleNum">
		<logic:iterate name="sample" property="containers" id="container" type="gov.nih.nci.calab.dto.administration.ContainerBean" indexId="containerNum">
			<c:set var="rowNum" value="${rowNum+1}" />
			<c:choose>
				<c:when test="${rowNum % 2 == 0}">
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
					<bean:write name="sample" property="sampleName" />
					&nbsp;
				</td>
				<td class="${style}">
					<bean:write name="sample" property="accessionDate" />
					&nbsp;
				</td>
				<td class="${style}">
					<bean:write name="sample" property="sampleType" />
					&nbsp;
				</td>
				<td class="${style}">
					<logic:present name="container">
						<bean:write name="container" property="storageLocationStr" />
					</logic:present>
					&nbsp;
				</td>
				<td class="${style}">
					<bean:write name="sample" property="sampleSubmitter" />
					&nbsp;
				</td>
				<td class="${style}" valign="bottom">
					<c:url var="viewSampleDetailURL" value="/viewSampleDetail.do">
						<c:param name="sampleNum" value="${pageScope.sampleNum}" />
						<c:param name="containerNum" value="${pageScope.containerNum}" />
						<c:param name="isAliquot" value="false" />
					</c:url>
					<a href="${viewSampleDetailURL}">View Details</a> &nbsp;

					<c:url value="/searchAliquot.do" var="showAliquotURL">
						<c:param name="isAliquot" value="true" />
						<c:param name="searchName" value="${sample.sampleName}-*" />
						<c:param name="fromSampleResult" value="true" />
					</c:url>
					<a href="${showAliquotURL}">Show Aliquots</a> &nbsp;
				</td>
			</tr>
		</logic:iterate>
	</logic:iterate>
</table>
<br>
<br>

