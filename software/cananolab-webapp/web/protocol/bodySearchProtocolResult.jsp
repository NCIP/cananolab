<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Protocol Search Results" />
	<jsp:param name="topic" value="protocol_search_results_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
	<jsp:param name="other" value="Back" />
	<jsp:param name="otherLink" value="searchProtocol.do?dispatch=setup" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=protocol" />

<%--add dispatch when it's missing after coming back from sample summary page --%>
<c:set var="requestURI" value="searchProtocol.do" />
<c:if test="${empty param.dispatch}">
	<c:set var="requestURI" value="searchProtocol.do?dispatch=search" />
</c:if>

<display:table name="sessionScope.protocols" id="protocol"
	requestURI="${requestURI}" pagesize="25" class="displaytable"
	size="sessionScope.resultSize" partialList="true"
	decorator="gov.nih.nci.cananolab.dto.common.ProtocolDecorator">
	<c:if test="${!empty user}">
		<display:column title="" property="detailURL" />
	</c:if>
	<display:column title="Protocol Type" property="domain.type"
		sortable="true" escapeXml="true" headerScope="col" />
	<display:column title="Protocol Name" property="viewName"
		style="width: 20%" sortable="true" escapeXml="true" headerScope="col" />
	<display:column title="Protocol <br> Abbreviation"
		property="domain.abbreviation" sortable="true" escapeXml="true"
		headerScope="col" />
	<display:column title="Version" property="domain.version"
		sortable="true" escapeXml="true" headerScope="col" />
	<display:column title="Protocol File" style="width: 25%"
		property="fileInfo" sortable="true" headerScope="col" />
	<display:column title="Created Date" property="domain.createdDate"
		sortable="true" format="{0,date,MM-dd-yyyy}" headerScope="col" />
</display:table>

<div style="padding:5px;margin:5px; background-color:#fff; border:1px solid #416599;">
	To view the PDF documents, you may need to install the Adobe PDF Reader for your browser. Please click <a target="_new" href="http://get.adobe.com/reader/">here</a> to download this free plug-in.<br/>
</div>
