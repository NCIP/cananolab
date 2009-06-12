<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				Sample Search Results
			</h3>
		</td>
		<td align="right" width="25%">
			<jsp:include page="/helpGlossary.jsp">
				<jsp:param name="topic" value="nano_search_results_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
			<a href="searchSample.do?dispatch=setup" class="helpText">Back</a>
		</td>
	</tr>
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
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			<display:table name="samples" id="sample"
				requestURI="searchSample.do" pagesize="25"
				class="displaytable"
				decorator="gov.nih.nci.cananolab.dto.particle.SampleDecorator">
				<display:column title="Sample Name"
					property="${sampleURL}" sortable="true" />
				<display:column title="Primary<br>Point Of Contact"
					property="pointOfContactName"
					sortable="true" />
				<display:column title="Composition"
					property="compositionStr" sortable="true" />
				<display:column title="Functions"
					property="functionStr" />
				<display:column title="Characterizations"
					property="characterizationStr" />
				<display:column title="Location" property="location" sortable="true" />
			</display:table>
		</td>
	</tr>
</table>

