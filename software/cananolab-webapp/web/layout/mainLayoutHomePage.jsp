<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
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
<script type="text/javascript" src="javascript/script.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/PublicationManager.js'></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/ProtocolManager.js'></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/SampleManager.js'></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CharacterizationManager.js'></script>
<script type='text/javascript' src='javascript/CountManager.js'></script>
</head>
<tiles:importAttribute scope="session" />
<body style="cursor: default" onload="getPublicCounts();">
	<table cellspacing="0" cellpadding="0" width="100%" summary="layout"
		border="0">
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
				<td valign="top" class="sideMenu">
					<%-- include sidemenu on the left --%>					 
					<tiles:insert
						attribute="cananoSidemenu" /></td>					
				<td valign="top" class="mainContent">
					<table cellspacing="0" cellpadding="0" summary="layout" border="0" width="100%">
						<tbody>
							<tr>
								<td align="left" valign="top" class="mainMenu">
									<%-- include caNanoLab main menu --%> 
									<tiles:insert
										attribute="cananoMainmenu" />
								</td>
							</tr>
							<tr>
								<td align="left" valign="top" class="mainContent">
									<%-- include caNanoLab main content --%>									
									<tiles:insert attribute="cananoContent" /> 									 
								</td>
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
</body>
</html>
