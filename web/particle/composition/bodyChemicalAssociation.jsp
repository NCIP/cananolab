<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<script type="text/javascript"
	src="javascript/CompositionManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CompositionManager.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<%--<jsp:include page="/particle/submitMenu.jsp" />--%>
<%-- turn off update when doing remote searches --%>
<%--<c:choose>--%>
<%--	<c:when test="${!empty param.gridNodeHost}">--%>
<%--		<c:set var="isRemote" value="true" scope="session" />--%>
<%--	</c:when>--%>
<%--	<c:otherwise>--%>
<%--		<c:set var="isRemote" value="false" scope="session" />--%>
<%--	</c:otherwise>--%>
<%--</c:choose>--%>
<c:choose>
	<c:when	test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
		<%@ include file="bodyChemicalAssociationUpdate.jsp" %> 
	</c:when>
	<c:otherwise>
		<%@ include file="bodyChemicalAssociationReadOnly.jsp" %> 
	</c:otherwise>
</c:choose>
