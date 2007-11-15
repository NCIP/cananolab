<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/sidemenu.css">
<script type="text/javascript" src="javascript/sidemenu.js"></script>

<!-- submenu begins -->
<c:choose>
	<c:when test="${!empty param.submitType && param.submitType != 'none' &&
					param.displayType != 'Composition' }">
		<c:set var="displaytype" value="${param.submitType}" scope="request" />
	</c:when>
	<c:otherwise>
		<c:set var="displaytype" value="${param.displayType}" scope="request" />
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
	<c:when test="${!empty param.particleSource}">
		<c:set var="particleSource" value="${param.particleSource}"
			scope="session" />
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${!empty requestScope.particleSource}">
				<c:set var="particleSource" value="${requestScope.particleSource}"
					scope="session" />
			</c:when>
		</c:choose>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${canUserSubmit eq 'true'}">
		<c:set var="dispatchValue" value="setupUpdate" scope="session" />
	</c:when>
	<c:otherwise>
		<c:set var="dispatchValue" value="setupView" scope="session" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${displaytype == 'report'}">
		<c:set var="reportDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="reportDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${displaytype == 'associateFile'}">
		<c:set var="fileDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="fileDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%" width="250">
	<tr>
		<td class="subMenuPrimaryTitle" height="21">NAVIGATION TREE</td>
	</tr>
	<tr><td>

	<ul class="slidingmenu" id="menuroot">
	 
	  <li id="view_particle" >VIEWING PARTICLE: <c:out value="${particleName}" /></li>
      <li class="toplist">
			<c:url var="url" value="nanoparticleGeneralInfo.do">
				<c:param name="dispatch" value="${dispatchValue}" />
				<c:param name="particleName" value="${particleName}" />
				<c:param name="particleType" value="${particleType}" />
				<c:param name="particleSource" value="${particleSource}" />
			</c:url>
			<a href="${url}" class="subMenuSecondary" >GENERAL INFORMATION</a>
      </li>
      <jsp:include page="sideParticleFunctionMenu.jsp"></jsp:include>
      <jsp:include page="sideParticleCharacterizationMenu.jsp"></jsp:include>
      
	<li class="toplist">
		<a href="#" class="subMenuSecondary">ASSOCIATE FILES</a>
		<c:if test="${!empty particleAssociatedFiles}" >
			<ul class="sublist_5" style="${fileDisplay}">
				<c:forEach var="aReport" items="${particleAssociatedFiles}">
					<c:url var="url" value="updateReportForParticle.do">
						<c:param name="page" value="0" />
						<c:param name="dispatch" value="${dispatchValue}" />
						<c:param name="submitType" value="none" />
						<c:param name="fileId" value="${aReport.id}" />
						<c:param name="fileType" value="${aReport.type}"/>
						<c:param name="displayType" value="associateFile"/>
					</c:url>
					<li><a	href="${url}" title="${aReport.displayName}"><span class="data_anchar">>&nbsp;</span>${aReport.name}</a></li>
				</c:forEach>
			</ul>
		</c:if>						
	</li>
	<li class="toplist">
		<a href="#" class="subMenuSecondary">REPORTS</a>
		<ul class="sublist_5" style="${reportDisplay}">
			<c:forEach var="aReport" items="${particleReports}">
				<c:url var="url" value="updateReportForParticle.do">
					<c:param name="displayType" value="report"/>
					<c:param name="page" value="0" />
					<c:param name="dispatch" value="${dispatchValue}" />
					<c:param name="submitType" value="none" />
					<c:param name="fileId" value="${aReport.id}" />
					<c:param name="fileType" value="${aReport.type}"/>
				</c:url>
				<li><a	href="${url}" title="${aReport.displayName}"><span class="data_anchar">>&nbsp;</span>${aReport.name}</a></li>
			</c:forEach>
		</ul>
	</li>

      <li class="toplist" id="invivolist" >
        IN VIVO CHARACTERIZATIONS
      </li>
    </ul>
    
	</td></tr>
	<tr><td class="subMenuFill">
		&nbsp;
	</td></tr>
	<tr>
		<td class="subMenuPrimaryTitle" height="27">QUICK LINKS</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle" onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()" onclick="openWindow('http://www.cancer.gov')" onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()" height="20">
				<a class="subMenuSecondary">NCI HOME</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle" onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver')" onclick="openWindow('http://ncicb.nci.nih.gov/')" onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle')" height="20">
			<a class="subMenuSecondary">NCICB HOME</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle" onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver')" onclick="openWindow('http://ncl.cancer.gov/')" onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle')" height="20">
			<a class="subMenuSecondary">NCL HOME</a>
		</td>
	</tr>
	<tr><td class="subMenuFill" height="100%">
		&nbsp;
	</td></tr>

	<tr>
		<td class="subMenuFooter" height="22">
			&nbsp;
		</td>
	</tr>
</table>
