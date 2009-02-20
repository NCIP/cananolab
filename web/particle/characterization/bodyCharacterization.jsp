<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript"
	src="javascript/CharacterizationManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CharacterizationManager.js'></script>
<script type="text/javascript"
	src="javascript/ExperimentConfigManager.js"></script>
<script type="text/javascript" src="javascript/DataSetManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/ExperimentConfigManager.js'></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/DataSetManager.js'></script>
<script type="text/javascript" src="javascript/ProtocolManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/ProtocolManager.js'></script>
<script type="text/javascript" src="javascript/CompositionManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CompositionManager.js'></script>

<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<html:form action="/characterization" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${fn:toUpperCase(param.location)} ${particleName} ${pageTitle}
				</h4>
			</td>
			<c_rt:set var='dispatch'
				value='<%=request.getParameter("dispatch")%>' />
			<c:choose>
				<c:when test="${'setup' eq param.dispatch }">
					<c:remove var="dataId" scope="session" />
				</c:when>
				<c:when test="${'setupUpdate' eq param.dispatch }">
					<c:set var="dataId" value="${param.dataId}" scope="session" />
				</c:when>
			</c:choose>
			<c:set var="helpTopic" value="char_details_help" />
			<c:choose>
				<c:when
					test='${"Physical Characterization" eq pageTitle && ("setup" eq dispatch || empty dataId)}'>
					<c:set var="helpTopic" value="add_physical_char_help" />
				</c:when>
				<c:when
					test='${"In Vitro Characterization" eq pageTitle && ("setup" eq dispatch || empty dataId)}'>
					<c:set var="helpTopic" value="add_in_vitro_char_help" />
				</c:when>
			</c:choose>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="${helpTopic}" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${param.submitType}
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<jsp:include
					page="/particle/characterization/shared/bodyCharacterizationSummary.jsp" />
				<jsp:include
					page="/particle/characterization/shared/bodyCharacterizationDesignMethods.jsp" />
				<c:if test="${!empty characterizationDetailPage}">
					<jsp:include page="${characterizationDetailPage}" />
				</c:if>
				<jsp:include
					page="/particle/characterization/shared/bodyCharacterizationResults.jsp" />
				<%--
				<jsp:include page="/particle/bodyAnnotationCopy.jsp" />--%>
				<jsp:include
					page="/particle/characterization/shared/bodyCharacterizationConclusion.jsp" />
				<jsp:include
					page="/particle/characterization/shared/bodyCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
