<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<c:set var="helpTopic" value="char_details_help" />
<%--TODO: create online help topic for this page.--%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Submit Study" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/study" enctype="multipart/form-data"
	onsubmit="return validateShapeInfo() && validateSolubilityInfo() &&
	validateSavingTheData('newExperimentConfig', 'Technique and Instrument') &&
	validateSavingTheData('newFinding', 'Finding');">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<%@include file="../sample/bodySubmitButtons.jsp"%>
</html:form>
