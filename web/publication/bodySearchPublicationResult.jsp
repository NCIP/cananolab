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
				Nanoparticle Publication Search Results
			</h3>
		</td>
		<td align="right" width="25%">
			<jsp:include page="/webHelp/helpGlossary.jsp">
				<jsp:param name="topic" value="search_reports_results_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>				
			<a href="searchPublication.do?dispatch=setup" class="helpText">Back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=publication" />
			<display:table name="sessionScope.publications" id="publication"
				requestURI="searchPublication.do" pagesize="25" class="displaytable"
				decorator="gov.nih.nci.cananolab.dto.common.PublicationDecorator">
				<display:column title="Title" property="editDocumentURL"
					sortable="true" />
				<display:column title="Publication<br>Category" property="publicationOrReport"
					sortable="true" />		
				<display:column title="Research<br>Category"
					property="researchArea" sortable="true" />
				<display:column title="Associated Particle<br>Sample Names"
					property="particleNames" sortable="true" />
				<display:column title="Created<br>Date"
					property="domainFile.createdDate" sortable="true"
					format="{0,date,MM-dd-yyyy}" />
				<display:column title="Location" property="location" sortable="true" />
			</display:table>
		</td>
	</tr>
</table>

