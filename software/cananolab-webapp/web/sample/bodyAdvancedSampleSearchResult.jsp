<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<c:url var="printUrl" value="advancedSampleSearch.do">
	<c:param name="dispatch" value="print" />
</c:url>
<c:url var="exportUrl" value="advancedSampleSearch.do">
	<c:param name="dispatch" value="export" />
</c:url>

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Advanced Sample Search Results" />
	<jsp:param name="topic" value="sample_search_results_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
	<jsp:param name="other" value="Back" />
	<jsp:param name="printLink" value="${printUrl}" />
	<jsp:param name="exportLink" value="${exportUrl}" />

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
			<table class="editTableWithGrid" width="100%">
				<tr>
					<th style="text-align: center">
						Selected Criteria
					</th>
					<td style="text-align: right">
						<c:if test="${empty printView}">
							<a href="advancedSampleSearch.do?dispatch=validateSetup">Edit</a>
						</c:if>
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
							<c:if test="${!empty item.displayName}">
						    ${item.displayName}&nbsp;
						 	<div id="details${sample.sampleId}:${ind1.count}:${ind2.count}"
									style="position: relative">
									<%--<a href="${item.action}" target="detailView">View Details</a>--%>
									<a
										id="detailLink${sample.sampleId}:${ind1.count}:${ind2.count}"
										href="#"
										onclick="showDetailView('${sample.sampleId}:${ind1.count}:${ind2.count}', '${item.action}'); return false;">Details</a>
									<img src="images/ajax-loader.gif" border="0" class="counts"
										id="loaderImg${sample.sampleId}:${ind1.count}:${ind2.count}"
										style="display: none">
									<table
										id="detailView${sample.sampleId}:${ind1.count}:${ind2.count}"
										style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width: 500px; font-size: 10px; background-color: #FFFFFF"
										class="promptbox">
										<tr>
											<td>
												<div
													id="content${sample.sampleId}:${ind1.count}:${ind2.count}"></div>
											</td>
										</tr>
									</table>
								</div>
								<br />
								<br />
							</c:if>
						</c:forEach>
					</display:column>
				</c:forEach>
			</display:table>
		</td>
	</tr>
</table>