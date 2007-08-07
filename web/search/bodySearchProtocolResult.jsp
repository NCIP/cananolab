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
				<br>
				Protocol Search Results
			</h3>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=protocols_search_results_help')" class="helpText">Help</a>&nbsp;&nbsp; <a href="searchProtocol.do?dispatch=setup" class="helpText">back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=search" />
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<!-- c:set var="link" value="editProtocolURL" /-->
					<c:set var="link" value="editProtocolURL" />
				</c:when>
				<c:otherwise>
					<c:set var="link" value="viewProtocolURL" />
				</c:otherwise>
			</c:choose>
			<display:table name="sessionScope.protocols" id="protocol" requestURI="searchProtocol.do" pagesize="25" class="displaytable" decorator="gov.nih.nci.calab.dto.search.ProtocolDecorator">
				<!-- display:column title="Protocol Name" property="${link}" sortable="true" /-->
				<display:column title="Protocol Name" property="${link}" sortable="true" />
				<display:column title="Protocol Type" property="protocolBean.type" sortable="true" />
				<display:column title="Version" property="version" sortable="false" />
				<display:column title="File Title" property="title" sortable="true" />
				<display:column title="Description" property="description" sortable="false" />
				<%--
				<display:column title="Protocol Description" property="description" sortable="true" />
				--%>
			</display:table>
		</td>
	</tr>
</table>

