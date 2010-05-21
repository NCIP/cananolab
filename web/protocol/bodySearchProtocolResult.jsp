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
<table width="100%" align="center">
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=protocol" />
			<display:table name="sessionScope.protocols" id="protocol"
				requestURI="searchProtocol.do" pagesize="25" class="displaytable"
				decorator="gov.nih.nci.cananolab.dto.common.ProtocolDecorator">
				<c:if test="${!empty user && user.curator}">
					<display:column title="" property="editURL"/>
				</c:if>
				<display:column title="Protocol Name" property="viewName"
					sortable="true" />
				<display:column title="Protocol Abbreviation"
					property="domain.abbreviation" sortable="true" />
				<display:column title="Protocol Type" property="domain.type"
					sortable="true" />
				<display:column title="Version" property="domain.version"
					sortable="true" />
				<display:column title="File" property="downloadURL" sortable="true" />
				<display:column title="Description"
					property="fileBean.domainFile.description" sortable="false" />
				<display:column title="Protocol Created Date"
					property="domain.createdDate" sortable="true"
					format="{0,date,MM-dd-yyyy}" />
				<display:column title="Location" property="location" sortable="true" />
			</display:table>
		</td>
	</tr>
</table>

