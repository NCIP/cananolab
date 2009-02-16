<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
	<c:when test="${displaytype == 'characterization'}">
		<c:set var="charDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="charDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>
<li class="controlList">
	<c:url var="url" value="submitNanoparticleSample.do">
		<c:param name="dispatch" value="setupUpdate" />
		<c:param name="particleId" value="${particleId}" />
		<c:param name="location" value="${location}" />
		<c:param name="submitType" value="characterization" />
	</c:url>
	<a href="${url}" class="subMenuSecondary">CHARACTERIZATION</a>
	<ul class="sublist_4" style="${charDisplay}">
		<li>
			<c:url var="submitUrl" value="characterization.do">
				<c:param name="particleId" value="${particleId}" />
				<c:param name="page" value="0" />
				<c:param name="dispatch" value="setupUpdate" />
				<c:param name="location" value="${param.location}" />
			</c:url>
			<a href="${submitUrl}" class="addlink"><img
					src="images/btn_add.gif" border="0" /> </a>
			<br>
		</li>
	</ul>
</li>