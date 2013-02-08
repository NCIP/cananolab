<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Publication Search Results" />
	<jsp:param name="topic" value="publications_search_results_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=publication" />


<%--add dispatch when it's missing after coming back from sample summary page --%>
<c:set var="requestURI" value="searchPublication.do" />
<c:if test="${empty param.dispatch}">
	<c:set var="requestURI" value="searchPublication.do?dispatch=search" />
</c:if>

<display:table name="sessionScope.publications" id="publication"
	requestURI="${requestURI}" pagesize="25" class="displaytable"
	partialList="true" size="sessionScope.resultSize"
	decorator="gov.nih.nci.cananolab.dto.common.PublicationDecorator">
	<c:if test="${!empty user}">
		<display:column title="" property="detailURL" />
	</c:if>
	<display:column title="Bibliography Info" property="displayName"
		sortable="true" headerScope="col" />
	<display:column title="Publication<br>Type" property="publicationType"
		sortable="true" escapeXml="true" headerScope="col" />
	<display:column title="Research<br>Category" property="researchArea"
		sortable="true" headerScope="col" />
	<display:column title="Associated <br>Sample Names"
		property="sampleNames" sortable="true" headerScope="col" />
	<display:column title="Description" property="descriptionDetail"
		sortable="true" headerScope="col" />
	<display:column title="Publication<br>Status"
		property="domainFile.status" sortable="true" escapeXml="true"
		headerScope="col" />
	<display:column title="Created<br>Date"
		property="domainFile.createdDate" sortable="true"
		format="{0,date,MM-dd-yyyy}" headerScope="col" />
</display:table>

<div style="padding:5px;margin:5px; background-color:#fff; border:1px solid #416599;">
	To view the PDF documents, you may need to install the Adobe PDF Reader for your browser. Please click <a target="_new" href="http://get.adobe.com/reader/">here</a> to download this free plug-in.<br/>
</div>