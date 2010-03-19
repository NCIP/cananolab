<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/CompositionManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CompositionManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<c:set var="fileParent" value="comp" />
<c:set var="theFile" value="${compositionForm.map.comp.theFile}" />
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${sampleName} Sample Composition - Composition File" />
	<jsp:param name="topic" value="compo_file_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/compositionFile" enctype="multipart/form-data">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<c:set var="fileForm" value="compositionForm" />
	<c:set var="theFile" value="${compositionForm.map.comp.theFile}" />
	<c:set var="actionName" value="compositionFile" />
	<%@include file="../bodySubmitFile.jsp"%>
	<br>

	<c:set var="updateId" value="${compositionForm.map.comp.theFile.domainFile.id}"/>
	<c:set var="resetOnclick" value="this.form.reset();displayFileRadioButton();"/>
	<c:set var="deleteOnclick" value="deleteData('composition file', compositionForm, 'compositionFile', 'delete')"/>
	<c:set var="deleteButtonName" value="Delete"/>
	<c:set var="hiddenDispatch" value="create"/>
	<c:set var="hiddenPage" value="2"/>
	<%@include file="../../bodySubmitButtons.jsp"%>
</html:form>