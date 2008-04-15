<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/particleEntity.js"></script>

<script type="text/javascript">
<!--//
function confirmDeletion()
{
	answer = confirm("Are you sure you want to delete the composition?")
	if (answer !=0)
	{
		this.document.forms[0].dispatch.value="deleteConfirmed";
		this.document.forms[0].submit(); 
		return true;
	}
}

//-->
</script>
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
<%--<c:choose>--%>
<%--	<c:when	test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">--%>
		<%@ include file="bodyParticleEntityUpdate.jsp" %> 
<%--	</c:when>--%>
<%--	<c:otherwise>--%>
<%--		<%@ include file="bodyParticleEntityReadOnly.jsp" %> --%>
<%--	</c:otherwise>--%>
<%--</c:choose>--%>
