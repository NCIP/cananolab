<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
	<c:when test="${displaytype == 'publication'}">
		<c:set var="pubDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="pubDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>
<li class="controlList">
	<c:url var="url" value="submitNanoparticleSample.do">
		<c:param name="dispatch" value="setupUpdate" />
		<c:param name="particleId" value="${particleId}" />
		<c:param name="location" value="${location}" />
		<c:param name="submitType" value="publication" />
	</c:url>
	<a href="${url}" class="subMenuSecondary">PUBLICATION</a>
	<ul class="sublist_4" style="${pubDisplay}">
		<li>
			<c:url var="submitUrl" value="publication.do">
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
