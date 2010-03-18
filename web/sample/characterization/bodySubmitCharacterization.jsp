<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript"
	src="javascript/CharacterizationManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CharacterizationManager.js'></script>
<script type="text/javascript" src="javascript/FindingManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/FindingManager.js'></script>
<script type="text/javascript"
	src="javascript/ExperimentConfigManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/ExperimentConfigManager.js'></script>
<script type="text/javascript" src="javascript/ProtocolManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/ProtocolManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<c:choose>
	<c:when	test="${!empty characterizationForm.map.achar.domainChar.id}">
		<c:set var="charTitle"
			value="${fn:toUpperCase(param.location)} ${sampleName} ${characterizationForm.map.achar.characterizationType} -
			${characterizationForm.map.achar.characterizationName}"/>
	</c:when>
	<c:otherwise>
		<c:set var="charTitle"
			value="${fn:toUpperCase(param.location)} ${sampleName} Characterization"/>
	</c:otherwise>
</c:choose>
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
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${charTitle}" />
	<jsp:param name="topic" value="submit_char_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/characterization" enctype="multipart/form-data"
	onsubmit="return validateShapeInfo() && validateSolubilityInfo() &&
	validateSavingTheData('newExperimentConfig', 'Technique and Instrument') &&
	validateSavingTheData('newFinding', 'Finding');">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<jsp:include
		page="/sample/characterization/shared/bodyCharacterizationSummary.jsp" />

	<div id="characterizationDetail">
	<c:if test="${!empty characterizationDetailPage}">
		<jsp:include page="${characterizationDetailPage}" />
	</c:if>
	</div>
	<a name="designAndMethod"> <jsp:include
			page="shared/bodyCharacterizationDesignMethods.jsp" /></a>
	<a name="result"> <jsp:include
			page="shared/bodyCharacterizationResults.jsp" /></a>
	<jsp:include page="shared/bodyCharacterizationConclusion.jsp" />
	<jsp:include
		page="/sample/bodyAnnotationCopy.jsp?annotation=characterization" />
	<br/>		
	<c:set var="updateId" value="${characterizationForm.map.achar.domainChar.id}"/>
	<c:set var="resetLink" value="this.form.reset();displayFileRadioButton();"/>
	<c:set var="deleteOnclick" value="deleteData('characterization', characterizationForm, 'characterization', 'delete')"/>
	<c:set var="deleteButtonName" value="Delete"/>
	<c:set var="hiddenDispatch" value="create"/>
	<c:set var="hiddenPage" value="2"/>	
	<%@include file="../../bodySubmitButtons.jsp"%>
</html:form>
