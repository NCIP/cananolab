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
	<c:when test="${!empty theStudy}">
		<c:set var="studyName" value="${theStudy.domain.name}"
			scope="session" />
		<c:set var="studyId" value="${theStudy.domain.id}" scope="session" />
	</c:when>
</c:choose>
<c:set var="dispatch" value="${param.dispatch}" />

<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%" width="150">
	<tr>
		<td class="subMenuPrimaryTitle" height="21">
			NAVIGATION TREE
		</td>
	</tr>
	<tr>
		<c:url var="studyUrl" value="study.do">
			<c:param name="dispatch" value="${dispatch}" />
			<c:param name="studyId" value="${studyId}" />
			<c:param name="page" value="0" />
			<c:param name="tab" value="ALL"/>
		</c:url>
		<c:choose>
			<c:when test="${actionPath eq '/study.do'}">
				<td class="subMenuSecondaryTitleSelected"
					onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
					onmouseout="changeMenuStyle(this,'subMenuSecondaryTitleSelected'), hideCursor()"
					onclick="gotoPage('${studyUrl}')" height="20">
					<a class="subMenuSecondary">GENERAL INFO</a>
				</td>
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle"
					onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
					onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
					onclick="gotoPage('${studyUrl}')" height="20">
					<a class="subMenuSecondary">GENERAL INFO</a>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<c:url var="sampleUrl" value="studySample.do">
			<<c:param name="dispatch" value="${dispatch}" />
			<c:param name="page" value="0" />
		</c:url>
		<c:choose>
			<c:when test="${actionPath eq '/studySample.do'}">
				<td class="subMenuSecondaryTitleSelected"
					onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
					onmouseout="changeMenuStyle(this,'subMenuSecondaryTitleSelected'), hideCursor()"
					onclick="gotoPage('${sampleUrl}')" height="20">
					<a class="subMenuSecondary">SAMPLE</a>
				</td>
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle"
					onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
					onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
					onclick="gotoPage('${sampleUrl}')" height="20">
					<a class="subMenuSecondary">SAMPLE</a>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<c:url var="characterizationUrl" value="studyCharacterization.do">
			<c:param name="dispatch" value="${dispatch}" />
			<c:param name="page" value="0" />
			<c:param name="tab" value="ALL"/>
		</c:url>
		<c:choose>
			<c:when test="${actionPath eq '/studyCharacterization.do'}">
				<td class="subMenuSecondaryTitleSelected"
					onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
					onmouseout="changeMenuStyle(this,'subMenuSecondaryTitleSelected'), hideCursor()"
					onclick="gotoPage('${characterizationUrl}')" height="20">
					<a class="subMenuSecondary">CHARACTERIZATION</a>
				</td>
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle"
					onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
					onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
					onclick="gotoPage('${characterizationUrl}')" height="20">
					<a class="subMenuSecondary">CHARACTERIZATION</a>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<c:url var="protocolUrl" value="studyProtocol.do">
			<c:param name="dispatch" value="${dispatch}" />
			<c:param name="page" value="0" />
		</c:url>
		<c:choose>
			<c:when test="${actionPath eq '/studyProtocol.do'}">
				<td class="subMenuSecondaryTitleSelected"
					onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
					onmouseout="changeMenuStyle(this,'subMenuSecondaryTitleSelected'), hideCursor()"
					onclick="gotoPage('${protocolUrl}')" height="20">
					<a class="subMenuSecondary">PROTOCOL</a>
				</td>
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle"
					onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
					onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
					onclick="gotoPage('${protocolUrl}')" height="20">
					<a class="subMenuSecondary">PROTOCOL</a>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<c:url var="publicationUrl" value="studyPublication.do">
			<c:param name="dispatch" value="${dispatch}" />
			<c:param name="page" value="0" />
			<c:param name="tab" value="ALL" />
		</c:url>
		<c:choose>
			<c:when test="${actionPath eq '/studyPublication.do'}">
				<td class="subMenuSecondaryTitleSelected"
					onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
					onmouseout="changeMenuStyle(this,'subMenuSecondaryTitleSelected'), hideCursor()"
					onclick="gotoPage('${publicationUrl}')" height="20">
					<a class="subMenuSecondary">PUBLICATION</a>
				</td>
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle"
					onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
					onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
					onclick="gotoPage('${publicationUrl}')" height="20">
					<a class="subMenuSecondary">PUBLICATION</a>
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
