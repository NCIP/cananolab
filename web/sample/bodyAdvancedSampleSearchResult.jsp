<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Advanced Sample Search Results" />
	<jsp:param name="topic" value="sample_search_results_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
	<jsp:param name="other" value="Back" />
</jsp:include>
<table width="100%" align="center">
	<tr>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty user && user.curator}">
					<c:set var="sampleURL" value="editSampleURL" />
				</c:when>
				<c:otherwise>
					<c:set var="sampleURL" value="viewSampleURL" />
				</c:otherwise>
			</c:choose>
			<jsp:include page="/bodyMessage.jsp?bundle=sample" />

			<table class="summaryViewLayer4" width="100%">
				<tr>
					<th style="text-align: center">
						Selected Criteria
					</th>
					<td style="text-align: right">
						<a href="advancedSampleSearch.do?dispatch=input">Edit</a>
					</td>
				</tr>
				<tr>
					<td>
						<c:out
							value="${advancedSampleSearchForm.map.searchBean.displayName}"
							escapeXml="false" />
					</td>
					<td></td>
				</tr>
			</table>
			<br />
			<display:table name="advancedSamples" id="sample"
				requestURI="advancedSampleSearch.do" pagesize="25"
				class="displaytable" partialList="true" size="resultSize"
				decorator="gov.nih.nci.cananolab.dto.particle.AdvancedSampleDecorator">
				<display:column title="Sample Name" property="${sampleURL}"
					sortable="true" />
				<c:forEach var="entry" items="${sample.attributeMap}"
					varStatus="ind1">
					<display:column title="${entry.key}" sortable="true">
						<c:forEach var="item" items="${entry.value}" varStatus="ind2">
								${item.displayName}&nbsp;
								<a href="${item.action}" target="detailView">View Details</a>
							<br />
							<br />
						</c:forEach>
					</display:column>
				</c:forEach>
				<display:column title="Site" property="location" sortable="true" />
			</display:table>
		</td>
	</tr>
</table>

