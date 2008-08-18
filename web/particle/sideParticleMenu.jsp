<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/sidemenu.css">
<script type="text/javascript" src="javascript/sidemenu.js"></script>
<script type="text/javascript" src="javascript/particleEntity.js"></script>
<script type="text/javascript" src="javascript/functionalizingEntity.js"></script>

<c:choose>
	<c:when test="${!empty submitType }">
		<c:set var="displaytype" value="${submitType}" scope="session" />
	</c:when>
	<c:otherwise>
		<c:if test="${!empty param.submitType }">
			<c:set var="displaytype" value="${param.submitType}" scope="session" />
		</c:if>
	</c:otherwise>
</c:choose>
<c:if test="${particleId ne theParticle.domainParticleSample.id }">
	<c:set var="displaytype" value="" scope="session"/>
</c:if>
<c:choose>
	<c:when test="${!empty theParticle}">
		<c:set var="particleName"
			value="${theParticle.domainParticleSample.name}" scope="session" />
		<c:set var="particleId" value="${theParticle.domainParticleSample.id}"
			scope="session" />
		<c:set var="particleSource"
			value="${theParticle.domainParticleSample.source.organizationName}"
			scope="session" />
		<c:set var="location" value="${theParticle.location}" scope="session" />
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
		<c:set var="dispatchValue" value="setupUpdate" scope="session" />
	</c:when>
	<c:otherwise>
		<c:set var="dispatchValue" value="setupView" scope="session" />
	</c:otherwise>
</c:choose>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%" width="250">
	<tr>
		<td class="subMenuPrimaryTitle" height="21">
			NAVIGATION TREE ${form}
		</td>
	</tr>
	<tr>
		<td>
			<ul class="slidingmenu" id="menuroot">

				<li id="view_particle">
					<b>${fn:toUpperCase(location)} PARTICLE:</b>					
					<c:out value="${particleName}" />
				</li>
				<li class="controlList">
					<c:url var="url" value="submitNanoparticleSample.do">
						<c:param name="dispatch" value="${dispatchValue}" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="location" value="${location}" />
					</c:url>
					<a href="${url}" class="subMenuSecondary">GENERAL INFORMATION</a>
				</li>
				<jsp:include page="sideParticleCompositionMenu.jsp"></jsp:include>
				<jsp:include page="sideParticleCharacterizationMenu.jsp"></jsp:include>
				<jsp:include page="sideParticleReportMenu.jsp"></jsp:include>

				<li class="nodatali" id="invivolist">
					IN VIVO CHARACTERIZATIONS
				</li>
			</ul>

		</td>
	</tr>
	<tr>
		<td class="subMenuFill">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="subMenuPrimaryTitle" height="27">
			QUICK LINKS
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://www.cancer.gov')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">NCI HOME</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver')"
			onclick="openWindow('http://ncicb.nci.nih.gov/')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle')"
			height="20">
			<a class="subMenuSecondary">NCICB HOME</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver')"
			onclick="openWindow('http://ncl.cancer.gov/')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle')"
			height="20">
			<a class="subMenuSecondary">NCL HOME</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuFill" height="100%">
			&nbsp;
		</td>
	</tr>

	<tr>
		<td class="subMenuFooter" height="22">
			&nbsp;
		</td>
	</tr>
</table>
