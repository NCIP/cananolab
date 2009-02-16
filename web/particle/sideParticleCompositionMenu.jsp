<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
	<c:when test="${displaytype == 'composition'}">
		<c:set var="compDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="compDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>
<li class="controlList">
	<c:url var="url" value="submitNanoparticleSample.do">
		<c:param name="dispatch" value="setupUpdate" />
		<c:param name="particleId" value="${particleId}" />
		<c:param name="location" value="${location}" />
		<c:param name="submitType" value="composition" />
	</c:url>
	<a href="${url}" class="subMenuSecondary">SAMPLE COMPOSITION</a>
	<ul class="sublist_4" style="${compDislay}">
		<li>
			<c:url var="submitUrl" value="nanoparticleEntityAction.do">
				<c:param name="particleId" value="${particleId}" />
				<c:param name="page" value="0" />
				<c:param name="dispatch" value="setupUpdate" />
				<c:param name="location" value="${param.location}" />
			</c:url>
			<a href="${submitUrl}" class="addlink">Nanoparticle Entity <img
					src="images/btn_add.gif" border="0" /> </a>
		</li>
		<li>
			<c:url var="submitUrl" value="functionalizingEntityAction.do">
				<c:param name="particleId" value="${particleId}" />
				<c:param name="page" value="0" />
				<c:param name="dispatch" value="setupUpdate" />
				<c:param name="location" value="${param.location}" />
			</c:url>
			<a href="${submitUrl}" class="addlink">Functionalizing Entity <img
					src="images/btn_add.gif" border="0" /> </a>
		</li>
		<li>
			<c:url var="submitUrl" value="chemicalAssociationAction.do">
				<c:param name="particleId" value="${particleId}" />
				<c:param name="page" value="0" />
				<c:param name="dispatch" value="setupUpdate" />
				<c:param name="location" value="${param.location}" />
			</c:url>
			<a href="${submitUrl}" class="addlink">Chemical Association <img
					src="images/btn_add.gif" border="0" /> </a>
		</li>
		<li>
			<c:url var="submitUrl" value="compositionFile.do">
				<c:param name="particleId" value="${particleId}" />
				<c:param name="page" value="0" />
				<c:param name="dispatch" value="setupUpdate" />
				<c:param name="location" value="${param.location}" />
			</c:url>
			<a href="${submitUrl}" class="addlink">Composition File <img
					src="images/btn_add.gif" border="0" /> </a>
			<br>
		</li>
	</ul>
</li>