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
		<c:url var="studyUrl" value="study.do">
			<c:param name="dispatch" value="studyEdit" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
			<c:param name="tab" value="ALL"/>
		</c:url>
		<td class="subMenuPrimaryTitle" height="60" onmouseover="showCursor()"
			onmouseout="hideCursor()" onclick="gotoPage('${studyUrl}')"
			height="20">
			<a class="pname">${location} Study:<br><br>Efficacy of nanoparticle</a>
		</td>
	</tr>
	<tr>
		<c:url var="sampleInfoUrl" value="study.do">
			<c:param name="dispatch" value="sampleSummary" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
			<c:param name="tab" value="ALL"/>
		</c:url>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			onclick="gotoPage('${sampleInfoUrl}')" height="20">
			<a class="subMenuSecondary">SAMPLE</a>
		</td>
	</tr>
	<tr>
		<c:url var="animalInfoUrl" value="studyAnimalModel.do">
			<c:param name="dispatch" value="summaryEdit" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
			<c:param name="tab" value="ALL"/>
		</c:url>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			onclick="gotoPage('${animalInfoUrl}')" height="20">
			<a class="subMenuSecondary">ANIMAL INFORMATION</a>
		</td>
	</tr>
	<tr>
		<c:url var="characterizationUrl" value="study.do">
			<c:param name="dispatch" value="charSummary" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
			<c:param name="tab" value="ALL"/>
		</c:url>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			onclick="gotoPage('${characterizationUrl}')" height="20">
			<a class="subMenuSecondary">CHARACTERIZATION</a>
		</td>
	</tr>
	<tr>
		<c:url var="ProtocolUrl" value="studyProtocol.do">
			<c:param name="dispatch" value="summaryEdit" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
			<c:param name="tab" value="ALL"/>
		</c:url>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			onclick="gotoPage('${ProtocolUrl}')" height="20">
			<a class="subMenuSecondary">PROTOCOL</a>
		</td>
	</tr>
	<tr>
		<c:url var="publicationUrl" value="studyPublication.do">
			<c:param name="dispatch" value="summaryEdit" />
			<c:param name="location" value="${location}" />
			<c:param name="page" value="0" />
			<c:param name="tab" value="ALL"/>
		</c:url>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			onclick="gotoPage('${publicationUrl}')" height="20">
			<a class="subMenuSecondary">PUBLICATION</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuFill" height="50">
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
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://csn.ncifcrf.gov/csn/index2.php', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">NCI CSN</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle" height="20">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://oregonstate.edu/nbi/nbi/nanomaterial.php', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">NBI</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://www2a.cdc.gov/niosh-nil/', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">NIOSH</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://beta.internano.org/component/opti', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">InterNano</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://www.nanohub.org/home', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">nanoHUB</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://icon.rice.edu/research.cfm', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">ICON</a>
		</td>
	</tr>
	<tr>
		<td class="subMenuSecondaryTitle"
			onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'), showCursor()"
			onclick="openWindow('http://www.safenano.org/AdvancedSearch.aspx', '', '800', '800')"
			onmouseout="changeMenuStyle(this,'subMenuSecondaryTitle'), hideCursor()"
			height="20">
			<a class="subMenuSecondary">SAFENANO</a>
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
