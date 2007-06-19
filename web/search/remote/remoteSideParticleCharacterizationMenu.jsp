<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<span class="largerText">Characterization</span>
<br>
<br>
<c:forEach var="charType" items="${allCharacterizationTypes}">
	<span class="indented${charType.indentLevel}"><strong>-${charType.type}</strong>
	</span>
	<br>
	<c:forEach var="aChar" items="${remoteCharTypeChars[charType.type]}">
		<c:choose>
			<c:when test="${aChar.name eq 'Composition'}">
				<c:url var="url" value="remoteNanoparticleComposition.do?page=0">
					<c:param name="dispatch" value="view" />
					<c:param name="particleName" value="${particleName}" />
					<c:param name="particleType" value="${particleType}" />
					<c:param name="characterizationId" value="${aChar.id}" />
					<c:param name="submitType" value="${charType.type}" />
					<c:param name="actionName" value="${aChar.actionName}" />
					<c:param name="gridNodeHost" value="${gridNodeHost}"/>
				</c:url>
			</c:when>
			<c:otherwise>
				<c:url var="url" value="underConstruction.do">
				</c:url>
			</c:otherwise>
		</c:choose>
		<span class="indented${charType.indentLevel+1}"><a
			href="${url}" title="${aChar.name}"> <font
				color="${aChar.viewColor}">${aChar.abbr}:${aChar.viewTitle}</font> </a>
		</span>
		<br>
	</c:forEach>
	<br>
</c:forEach>

