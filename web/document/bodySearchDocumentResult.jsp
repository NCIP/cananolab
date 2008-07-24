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
				Nanoparticle Report Search Results
			</h3>
		</td>
		<td align="right" width="25%">
			<jsp:include page="/webHelp/helpGlossary.jsp">
				<jsp:param name="topic" value="search_reports_results_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>				
			<a href="searchReport.do?dispatch=setup" class="helpText">Back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=report" />
			<c:choose>
				<c:when test="${canCreateReport eq 'true'}">
					<c:set var="link" value="editReportURL" />
				</c:when>
				<c:otherwise>
					<c:set var="link" value="domainFile.title" />
				</c:otherwise>
			</c:choose>
			<display:table name="sessionScope.reports" id="report"
				requestURI="searchReport.do" pagesize="25" class="displaytable"
				decorator="gov.nih.nci.cananolab.dto.common.ReportDecorator">
				<display:column title="Report Title" property="${link}"
					sortable="true" />
				<display:column title="Report Category"
					property="domainFile.category" sortable="true" />
				<display:column title="Report Link"
					property="downloadURL" sortable="true" />
				<display:column title="Report Description"
					property="domainFile.description" sortable="true" />
				<display:column title="Associated <br>Particle Sample Names"
					property="particleNames" sortable="true" />
				<display:column title="Report Created Date"
					property="domainFile.createdDate" sortable="true"
					format="{0,date,MM-dd-yyyy}" />
				<display:column title="Location" property="location" sortable="true" />
			</display:table>
		</td>
	</tr>
</table>

