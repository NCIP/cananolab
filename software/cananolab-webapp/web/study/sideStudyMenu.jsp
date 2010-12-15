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
<c:set var="dispatch" value="summaryView" />
<c:if test="${theStudy.userUpdatable}">
	<c:set var="dispatch" value="summaryEdit" />
</c:if>
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
	<c:choose>
			<c:when test="${theStudy.hasSamples || !empty user}">
				<c:url var="sampleUrl" value="sample.do">
					<c:param name="dispatch" value="${dispatch}" />
					<c:param name="studyId" value="${studyId}" />
					<c:param name="page" value="0" />
					<c:param name="tab" value="ALL" />
				</c:url>
				<c:choose>
					<c:when test="${actionPath eq '/sample.do'}">
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
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle" height="20">
					<i>SAMPLE</i>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
	<c:choose>
			<c:when test="${theStudy.hasCharacterizations || !empty user}">
				<c:url var="charUrl" value="characterization.do">
					<c:param name="dispatch" value="${dispatch}" />
					<c:param name="sampleId" value="${sampleId}" />
					<c:param name="page" value="0" />
					<c:param name="tab" value="ALL" />
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
			<c:when test="${theStudy.hasPublications || !empty user}">
				<c:url var="pubUrl" value="publication.do">
					<c:param name="dispatch" value="${dispatch}" />
					<c:param name="studyId" value="${studyId}" />
					<c:param name="page" value="0" />
					<c:param name="tab" value="ALL" />
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
							onmouseout="changeMenuStyle(this, 'subMenuSecondaryTitle'), hideCursor();" onclick="gotoPage('${pubUrl}')" height="20">
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
		<c:choose>
			<c:when test="${theStudy.hasProtocols || !empty user}">
				<c:url var="protocolUrl" value="protocol.do">
					<c:param name="dispatch" value="${dispatch}" />
					<c:param name="studyId" value="${studyId}" />
					<c:param name="page" value="0" />
					<c:param name="tab" value="ALL" />
				</c:url>
				<c:choose>
					<c:when test="${actionPath eq '/protocol.do'}">
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
							onmouseout="changeMenuStyle(this, 'subMenuSecondaryTitle'), hideCursor();" onclick="gotoPage('${pubUrl}')" height="20">
							<a class="subMenuSecondary">PROTOCOL</a>
						</td>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle" height="20">
					<i>PROTOCOL</i>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<c:choose>
			<c:when test="${theStudy.hasAnimalInfo || !empty user}">
				<c:url var="animalUrl" value="animal.do">
					<c:param name="dispatch" value="${dispatch}" />
					<c:param name="studyId" value="${studyId}" />
					<c:param name="page" value="0" />
					<c:param name="tab" value="ALL" />
				</c:url>
				<c:choose>
					<c:when test="${actionPath eq '/animal.do'}">
						<td class="subMenuSecondaryTitleSelected"
							onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
							onmouseout="changeMenuStyle(this,'subMenuSecondaryTitleSelected'), hideCursor()"
							onclick="gotoPage('${animalUrl}')" height="20">
							<a class="subMenuSecondary">ANIMAL INFORMATION</a>
						</td>
					</c:when>
					<c:otherwise>
						<td class="subMenuSecondaryTitle"
							onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
							onmouseout="changeMenuStyle(this, 'subMenuSecondaryTitle'), hideCursor();" onclick="gotoPage('${pubUrl}')" height="20">
							<a class="subMenuSecondary">ANIMAL INFORMATION</a>
						</td>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<td class="subMenuSecondaryTitle" height="20">
					<i>ANIMAL INFORMATION</i>
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
