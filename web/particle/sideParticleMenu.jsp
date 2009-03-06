<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/sidemenu.css">

<c:choose>
	<c:when test="${!empty theParticle}">
		<c:set var="particleName"
			value="${theParticle.domainParticleSample.name}" scope="session" />
		<c:set var="particleId" value="${theParticle.domainParticleSample.id}"
			scope="session" />
		<c:set var="location" value="${theParticle.location}" scope="session" />
	</c:when>
</c:choose>

<c:choose>
	<c:when
		test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
		<c:set var="dispatch" value="summaryEdit" />
	</c:when>
	<c:otherwise>
		<c:set var="dispatch" value="summaryView" />
	</c:otherwise>
</c:choose>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%" width="150">
	<tr>
		<td class="subMenuPrimaryTitle" height="21">
			NAVIGATION TREE
		</td>
	</tr>
	<tr>
		<td class="subMenuPrimaryTitle" height="20">
			${fn:toUpperCase(location)} PARTICLE
			<br>
			<span class="pname"><c:out value="${particleName}" /> </span>
		</td>
	</tr>
	<tr>
		<c:url var="sampleUrl" value="nanoparticleSample.do">
			<c:param name="dispatch" value="${dispatch}" />
			<c:param name="particleId" value="${particleId}" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
		</c:url>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			onclick="gotoPage('${sampleUrl}')" height="20">
			<a class="subMenuSecondary">SAMPLE</a>
		</td>
	</tr>
	<tr>
		<c:url var="compUrl" value="composition.do">
			<c:param name="dispatch" value="${dispatch}" />
			<c:param name="particleId" value="${particleId}" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
		</c:url>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			onclick="gotoPage('${compUrl}')" height="20">
			<a class="subMenuSecondary">COMPOSITION</a>
		</td>
	</tr>
	<tr>
		<c:url var="charUrl" value="characterization.do">
			<c:param name="dispatch" value="${dispatch}" />
			<c:param name="particleId" value="${particleId}" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
		</c:url>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			onclick="gotoPage('${charUrl}')" height="20">
			<a class="subMenuSecondary">CHARACTERIZATION</a>
		</td>
	</tr>
	<tr>
		<c:url var="pubUrl" value="publication.do">
			<c:param name="dispatch" value="${dispatch}" />
			<c:param name="particleId" value="${particleId}" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
		</c:url>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			onclick="gotoPage('${pubUrl}')" height="20">
			<a class="subMenuSecondary">PUBLICATION</a>
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
			onclick="openWindow('https://wiki.nci.nih.gov/display/ICR/caNanoLab', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">caNanoLab Wiki</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://www.cancer.gov', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">NCI HOME</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://ncicb.nci.nih.gov/', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">NCICB HOME</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://ncl.cancer.gov/', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
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
