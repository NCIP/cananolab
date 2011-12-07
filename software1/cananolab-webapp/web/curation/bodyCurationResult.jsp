<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<c:set var="title" value="Copy" />
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Long Running Processes" />
	<jsp:param name="topic" value="copy_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>

<c:choose>
	<c:when test="${empty requestScope.previousLongRunningProcesses}">
		<span class="welcomeContent">There are currently no pending
			long running processes or pending results to view.</span>
	</c:when>
	<c:otherwise>
		<display:table name="requestScope.previousLongRunningProcesses"
			id="process" requestURI="manageResult.do" pagesize="25"
			class="displaytable">
			<display:column title="Process Type" property="processType" />
			<display:column title="Process Status" property="statusMessage" />
		</display:table>
	</c:otherwise>
</c:choose>