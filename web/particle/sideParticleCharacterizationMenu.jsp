<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- 
	<span class="indented${charType.indentLevel}"><strong>-${charType.type}</strong> </span>
	<c:choose>
		<c:when test="${canCreateNanoparticle eq 'true' && charType.hasAction}">
			&nbsp;&nbsp;<a href="submitAction.do?submitType=${charType.type}&particleType=${particleType}&particleName=${particleName}&particleSource=${particleSource}"><em>add</em></a>
			<c:choose>
				<c:when test="${canUserDeleteChars eq 'true'}">
					&nbsp;<a href="deleteAction.do?page=0&charCategory=${charType.type}&dispatch=setup&particleType=${particleType}&particleName=${particleName}"><em>delete</em></a>
				</c:when>
			</c:choose>
		</c:when>
	</c:choose>
-->

				
<c:set var="physicalType" value="Physical" />
<li class="toplist">
	<a href="#" >COMPOSITION</a>
	<ul class="sublist_1">
	<c:forEach var="subCharType" items="${allCharacterizations[physicalType]}">
	  <c:if test="${subCharType == 'Composition'}" >
        	<c:forEach var="leafCharBean" items="${nameToCharacterizations[subCharType]}">
				<c:url var="url" value="${leafCharBean.actionName}.do">
					<c:param name="page" value="0" />
					<c:param name="dispatch" value="${dispatchValue}" />
					<c:param name="particleName" value="${particleName}" />
					<c:param name="particleType" value="${particleType}" />
					<c:param name="particleSource" value="${particleSource}" />
					<c:param name="characterizationId" value="${leafCharBean.id}" />
					<c:param name="submitType" value="${subCharType}" />
					<c:param name="actionName" value="${leafCharBean.actionName}" />
					<c:param name="charName" value="${leafCharBean.name}"/>
				</c:url>
				<li><a href=${url}>${leafCharBean.viewTitle}</a></li>
			</c:forEach>
			<c:if test="${canCreateNanoparticle eq 'true'}">
				<c:url var="submitUrl" value="submitAction.do">
					<c:param name="particleName" value="${particleName}" />
					<c:param name="particleType" value="${particleType}" />
					<c:param name="particleSource" value="${particleSource}" />
					<c:param name="submitType" value="${subCharType}" />
				</c:url>
				<li><a href="${submitUrl}">Enter ${subCharType}</a></li>
			</c:if>
       </c:if>
	</c:forEach>
	</ul>
</li>

<li class="toplist"><a href="#">PHYSICAL CHARACTERIZATIONS</a>
	<ul class="sublist_1" style=${physicalDisplay}>
	<c:forEach var="subCharType" items="${allCharacterizations[physicalType]}">
	  <c:if test="${subCharType != 'Composition'}" >
		<li><a href="#">${subCharType}</a>
        	<ul class="sublist_2" style=${physicalDisplay}>
        	<c:forEach var="leafCharBean" items="${nameToCharacterizations[subCharType]}">
				<c:url var="url" value="${leafCharBean.actionName}.do">
					<c:param name="page" value="0" />
					<c:param name="dispatch" value="${dispatchValue}" />
					<c:param name="particleName" value="${particleName}" />
					<c:param name="particleType" value="${particleType}" />
					<c:param name="particleSource" value="${particleSource}" />
					<c:param name="characterizationId" value="${leafCharBean.id}" />
					<c:param name="submitType" value="${subCharType}" />
					<c:param name="actionName" value="${leafCharBean.actionName}" />
					<c:param name="charName" value="${leafCharBean.name}"/>
					<c:param name="physicalDisplay" value="display: block;"/>
					<c:param name="invitroDisplay" value="display: none;"/>
				</c:url>
				<li><a href=${url}>${leafCharBean.viewTitle}</a></li>
			</c:forEach>
			<c:if test="${canCreateNanoparticle eq 'true'}">
				<c:url var="submitUrl" value="submitAction.do">
					<c:param name="particleName" value="${particleName}" />
					<c:param name="particleType" value="${particleType}" />
					<c:param name="particleSource" value="${particleSource}" />
					<c:param name="submitType" value="${subCharType}" />
				</c:url>
				<li><a href="${submitUrl}">Enter ${subCharType}</a></li>
			</c:if>
        	</ul>
        </li>
       </c:if>
	</c:forEach>
	</ul>
</li>

		<c:set var="inVitroType" value="In Vitro" />
		<li class="toplist" ><a href="#">IN VITRO CHARACTERIZATIONS</a>
        <ul class="sublist_1" style=${invitroDisplay}>
        <c:forEach var="secondLevelChar" items="${allCharacterizations[inVitroType]}">
        	<li><a href="#">${secondLevelChar}</a>
        	<ul class="sublist_2" style=${invitroDisplay}>
        	<c:forEach var="thirdLevelChar" items="${allCharacterizations[secondLevelChar]}">
        		<li><a href="#">${thirdLevelChar}</a>
        		<ul class="sublist_3" style=${invitroDisplay}>
        		<c:forEach var="fourthLevelChar" items="${allCharacterizations[thirdLevelChar]}">
        			<li><a href="#">${fourthLevelChar}</a>
        			<ul class="sublist_4" style=${invitroDisplay}>
        			<c:forEach var="fifthLevelChar" items="${allCharacterizations[fourthLevelChar]}">
        				<li><a href="#">${fifthLevelChar}</a>
        				<ul class="sublist_5" style=${invitroDisplay}>
        				<c:forEach var="leafCharBean" items="${nameToCharacterizations[fifthLevelChar]}">
							<c:url var="url" value="${leafCharBean.actionName}.do">
								<c:param name="page" value="0" />
								<c:param name="dispatch" value="${dispatchValue}" />
								<c:param name="particleName" value="${particleName}" />
								<c:param name="particleType" value="${particleType}" />
								<c:param name="particleSource" value="${particleSource}" />
								<c:param name="characterizationId" value="${leafCharBean.id}" />
								<c:param name="submitType" value="${thirdLevelChar}" />
								<c:param name="actionName" value="${leafCharBean.actionName}" />
								<c:param name="charName" value="${leafCharBean.name}"/>
								<c:param name="physicalDisplay" value="display: none;"/>
								<c:param name="invitroDisplay" value="display: block;"/>
							</c:url>
							<li><a href=${url}>${leafCharBean.viewTitle}</a></li>
						</c:forEach>
						<c:if test="${canCreateNanoparticle eq 'true'}">
							<c:url var="submitUrl" value="submitAction.do">
								<c:param name="particleName" value="${particleName}" />
								<c:param name="particleType" value="${particleType}" />
								<c:param name="particleSource" value="${particleSource}" />
								<c:param name="submitType" value="${fifthLevelChar}" />
							</c:url>
							<li><a href="${submitUrl}">Enter ${fifthLevelChar}</a></li>
						</c:if>
						</ul></li>
					</c:forEach>
					</ul></li>
				</c:forEach>
				</ul></li>
			</c:forEach>
			</ul></li>	
		</c:forEach>
		</ul></li>


