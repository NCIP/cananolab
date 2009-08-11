<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
			<display:table name="advancedSamples" id="sample"
				requestURI="searchSample.do" pagesize="25" class="displaytable">
				<display:column title="Sample Name" property="sampleName"
					sortable="true" />
				<display:column title="Site" property="location" sortable="true" />
			</display:table>
		</td>
	</tr>
</table>

