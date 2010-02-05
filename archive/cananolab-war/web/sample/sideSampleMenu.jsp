<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String actionPath = request.getAttribute(
			org.apache.struts.Globals.ORIGINAL_URI_KEY).toString();
	pageContext.setAttribute("actionPath", actionPath);
%>
<link rel="StyleSheet" type="text/css" href="css/sidemenu.css">
<c:choose>
	<c:when test="${!empty theSample}">
		<c:set var="sampleName" value="${theSample.domain.name}"
			scope="session" />
		<c:set var="sampleId" value="${theSample.domain.id}" scope="session" />
		<c:set var="location" value="${theSample.location}" scope="session" />
	</c:when>
</c:choose>

<c:choose>
	<c:when test="${!empty user && user.curator && location eq applicationOwner}">
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
		<td class="subMenuFill" height="5">
			&nbsp;
		</td>
	</tr>
	<tr>
		<c:url var="sampleUrl" value="sample.do">
			<c:param name="dispatch" value="${dispatch}" />
			<c:param name="sampleId" value="${sampleId}" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
		</c:url>
		<td class="subMenuPrimaryTitle" height="60" onmouseover="showCursor()"
			onmouseout="hideCursor()" onclick="gotoPage('${sampleUrl}')"
			height="20">
			<a class="pname">${location} Sample<br> <br>${sampleName}</a>
		</td>
	</tr>
	<tr>
		<c:choose>
			<c:when
				test="${theSample.hasComposition|| !empty user && user.curator}">
				<c:url var="compUrl" value="composition.do">
					<c:param name="dispatch" value="${dispatch}" />
					<c:param name="sampleId" value="${sampleId}" />
					<c:param name="location" value="${location}" />
					<c:param name="page" value="0" />
					<c:param name="tab" value="ALL"/>
				</c:url>
				<c:choose>
					<c:when
						test="${actionPath eq '/composition.do' || actionPath eq '/nanomaterialEntity.do' || actionPath eq '/functionalizingEntity.do' ||actionPath eq '/chemicalAssociation.do' ||actionPath eq '/compositionFile.do'}">
						<td class="subMenuSecondaryTitleSelected"
							onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
							onmouseout="changeMenuStyle(this,'subMenuSecondaryTitleSelected'), hideCursor()"
							onclick="gotoPage('${compUrl}')" height="20">
							<a class="subMenuSecondary">COMPOSITION</a>
						</td>
					</c:when>
					<c:otherwise>
						<td class="subMenuSecondaryTitle"
							onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
							onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
							onclick="gotoPage('${compUrl}')" height="20">
							<a class="subMenuSecondary">COMPOSITION</a>
						</td>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle" height="20">
					<i>COMPOSITION</i>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<c:choose>
			<c:when
				test="${theSample.hasCharacterizations || !empty user && user.curator}">
				<c:url var="charUrl" value="characterization.do">
					<c:param name="dispatch" value="${dispatch}" />
					<c:param name="sampleId" value="${sampleId}" />
					<c:param name="location" value="${location}" />
					<c:param name="page" value="0" />
					<c:param name="tab" value="ALL"/>
				</c:url>
				<c:choose>
					<c:when test="${actionPath eq '/characterization.do'}">
						<td class="subMenuSecondaryTitleSelected"
							onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
							onmouseout="changeMenuStyle(this,'subMenuSecondaryTitleSelected'), hideCursor()"
							onclick="gotoPage('${charUrl}')" height="20">
							<a class="subMenuSecondary">CHARACTERIZATION</a>
						</td>
					</c:when>
					<c:otherwise>
						<td class="subMenuSecondaryTitle"
							onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
							onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
							onclick="gotoPage('${charUrl}')" height="20">
							<a class="subMenuSecondary">CHARACTERIZATION</a>
						</td>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle" height="20">
					<i>CHARACTERIZATION</i>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<c:choose>
			<c:when
				test="${theSample.hasPublications || !empty user && user.curator}">
				<c:url var="pubUrl" value="publication.do">
					<c:param name="dispatch" value="${dispatch}" />
					<c:param name="sampleId" value="${sampleId}" />
					<c:param name="location" value="${location}" />
					<c:param name="page" value="0" />
					<c:param name="tab" value="ALL"/>
				</c:url>
				<c:choose>
					<c:when test="${actionPath eq '/publication.do'}">
						<td class="subMenuSecondaryTitleSelected"
							onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
							onmouseout="changeMenuStyle(this,'subMenuSecondaryTitleSelected'), hideCursor()"
							onclick="gotoPage('${pubUrl}')" height="20">
							<a class="subMenuSecondary">PUBLICATION</a>
						</td>
					</c:when>
					<c:otherwise>
						<td class="subMenuSecondaryTitle"
							onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
							onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
							onclick="gotoPage('${pubUrl}')" height="20">
							<a class="subMenuSecondary">PUBLICATION</a>
						</td>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle" height="20">
					<i>PUBLICATION</i>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<td class="subMenuFill" height="50">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
		<jsp:include page="/html/cananoBaseSidemenu.html"/>
		</td>
	</tr>
</table>
