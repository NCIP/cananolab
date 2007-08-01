
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.calab.service.util.StringUtils"%>
<c:choose>
	<c:when test="${!empty param.submitType}">
		<c:set var="submitType" value="${param.submitType}" scope="session" />
	</c:when>
	<c:otherwise>
		<c:set var="submitType" value="${sessionScope.submitType}" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${!empty param.particleName}">
		<c:set var="particleName" value="${param.particleName}"
			scope="session" />
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${!empty param.particleType}">
		<c:set var="particleType" value="${param.particleType}"
			scope="session" />
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${canUserSubmit eq 'true'}">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<c:forEach var="achar" items="${allCharTypeChars[submitType]}">
				    <jsp:useBean id="achar" type="gov.nih.nci.calab.dto.characterization.CharacterizationBean" />
				    <%
				       String actionName=StringUtils.getOneWordLowerCaseFirstLetter(achar.getName());
				       pageContext.setAttribute("actionName", actionName);
				     %>
					<td class="secondMenuItem" height="20">
						<c:url var="link" value="${actionName}.do">
						   <c:param name="submitType" value="${submitType}"/>
						   <c:param name="dispatch" value="setup"/>
						   <c:param name="page" value="0"/>
						   <c:param name="particleName" value="${particleName}"/>
						   <c:param name="particleType" value="${particleType}"/>
						   <c:param name="actionName" value="${actionName}"/>
						   <c:param name="charName" value="${achar.name}"/>
						</c:url>
						<a href="${link}" class="secondMenuLink">${achar.name}</a>
					</td>
					<td>
						<img height="16" alt="" src="images/mainMenuSeparator.gif"
							width="1">
					</td>
				</c:forEach>
			</tr>
		</table>
	</c:when>
</c:choose>