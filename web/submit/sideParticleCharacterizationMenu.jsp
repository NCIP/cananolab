<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<span class="largerText">Characterization</span>
<br>
<br>
<c:forEach var="charType" items="${allCharacterizationTypes}">
	<span class="indented${charType.indentLevel}"><strong>-${charType.type}</strong> </span>
	<c:choose>
		<c:when test="${canUserSubmit eq 'true' && charType.hasAction}">
			&nbsp;&nbsp;<a href="submitAction.do?submitType=${charType.type}&particleType=${particleType}&particleName=${particleName}&particleSource=${particleSource}"><em>add</em></a>
			<c:choose>
				<c:when test="${canUserDeleteChars eq 'true'}">
					&nbsp;<a href="deleteAction.do?page=0&charCategory=${charType.type}&dispatch=setup&particleType=${particleType}&particleName=${particleName}"><em>delete</em></a>
				</c:when>
			</c:choose>
		</c:when>
	</c:choose>
	<br>
	<c:forEach var="aChar" items="${allCharacterizations[charType.type]}">
		<c:url var="url" value="${aChar.actionName}.do?page=0">
			<c:param name="dispatch" value="${dispatchValue}" />
			<c:param name="particleName" value="${particleName}" />
			<c:param name="particleType" value="${particleType}" />
			<c:param name="particleSource" value="${particleSource}" />
			<c:param name="characterizationId" value="${aChar.id}" />
			<c:param name="submitType" value="${charType.type}" />
			<c:param name="actionName" value="${aChar.actionName}" />
			<c:param name="charName" value="${aChar.name}"/>
		</c:url>
		<span class="indented${charType.indentLevel+1}"><a href="${url}" title="${aChar.name}"> <font color="${aChar.viewColor}">${aChar.abbr}:${aChar.viewTitle}</font></a></span>
		<br>
	</c:forEach>
	<br>
</c:forEach>

