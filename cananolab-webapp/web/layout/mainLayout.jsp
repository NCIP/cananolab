<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%
	String cr = System.getProperty("line.separator");
	application.setAttribute("cr", cr);
%>
<html>
	<head>
		<title><tiles:getAsString name="title" ignore="true" /></title>
		<meta name="keywords"
			content="nanoinformatics, nanotechnology model, caNanoLab, nanotechnology, nanoparticle, nanomaterial, cancer, information model, portal, data portal, data repository, caBIG, caGRID, NCL, nano characterization, nanoparticle composition, Cancer Nanotechnology Excellence, Nanoparticle Ontology">
		<meta name="description"
			content="caNanoLab is a data sharing portal designed to facilitate information sharing in the biomedical nanotechnology research community to expedite and validate the use of nanotechnology in biomedicine">
		<link rel="icon" href="images/favicon.ico" />
		<link rel="shortcut icon" href="images/favicon.ico" />
		<link rel="stylesheet" type="text/css" href="css/main.css">
		<link rel="stylesheet" type="text/css" href="css/summaryView.css">
		<link rel="stylesheet" type="text/css" href="css/tabmenu.css">
		<%-- doesn't work in FireFox
		<c:if test="${!empty printLinkURL}">
			<link rel="alternate" name="printlink" media="print"
				href="${printLinkURL}">
		</c:if>
		--%>
		<script type="text/javascript" src="javascript/script.js"></script>
	</head>
	<tiles:importAttribute scope="session" />
	<%--<tiles:importAttribute name="onloadJavascript" />--%>
	<c:if test="${! empty onloadJavascript && !empty anchor}">
		<body style="cursor: default"
			onload="${onloadJavascript};location.href='#${anchor}'">
	</c:if>
	<c:if test="${!empty onloadJavascript && empty anchor}">
		<body style="cursor: default" onload="${onloadJavascript};">
	</c:if>
	<c:if test="${empty onloadJavascript && !empty anchor}">
		<body style="cursor: default" onload="location.href='#${anchor}'">
	</c:if>
	<c:if test="${empty onloadJavascript && empty anchor}">
		<body style="cursor: default"">
	</c:if>
	<table cellspacing="0" cellpadding="0" summary="layout" border="0" width="100%">
		<!-- nci hdr begins -->
		<tbody>
			<tr>
				<td colspan="2">
					<%-- include NCI header --%> <tiles:insert attribute="nciHeader" />
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<%-- include caNanoLab header --%> <tiles:insert
						attribute="cananoHeader" /></td>
			</tr>
			<tr>
				<td class="sideMenu">
					<%-- include sidemenu on the left --%>					 
					<tiles:insert
						attribute="cananoSidemenu" /></td>					
				<td class="mainContent">
					<table cellspacing="0" cellpadding="0" summary="layout" border="0" width="800px">
						<tbody>
							<tr>
								<td colspan="2" class="mainMenu">
									<%-- include caNanoLab main menu --%> 
									<tiles:insert
										attribute="cananoMainmenu" />
								</td>
							</tr>
							<tr>
								<td class="spacer"></td>
								<td class="mainContent">
									<%-- include caNanoLab main content --%>									
									<tiles:insert attribute="cananoContent" /> 									 
								</td>
							</tr>
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<td class="footerMenu">&nbsp;</td>
				<td class="footerMenu">
					<%-- include caNanoLab footer --%> 
					<tiles:insert
						attribute="cananoFooter" /></td>
			</tr>
			<tr>
				<td colspan="2">
					<%-- include NCI footer --%> <tiles:insert attribute="nciFooter" />
				</td>
			</tr>
		</tbody>
	</table>
</html>
