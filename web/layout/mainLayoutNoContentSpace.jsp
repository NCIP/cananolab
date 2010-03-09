<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%
	String cr = System.getProperty("line.separator");
	application.setAttribute("cr", cr);
%>
<html>
	<head>
		<title><tiles:getAsString name="title" ignore="true" /></title>
		<meta name="keywords"
			content="nano informatics, nanotechnology model, caNanoLab, nanotechnology, nanoparticle, cancer, information model, portal, data portal, data repository, caBIG, caGRID, NCL, nano characterization, nanoparticle composition, Cancer Nanotechnology Excellence">
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
		<script type='text/javascript' src='javascript/CountManager.js'></script>
	</head>
	<tiles:importAttribute scope="session" />
	<body style="cursor: default" onload="getPublicCounts();">
		<table height="100%" cellspacing="0" cellpadding="0" width="100%"
			summary="" border="0" align="center">
			<!-- nci hdr begins -->
			<tbody>
				<tr>
					<td>
						<%-- include NCI header --%>
						<tiles:insert attribute="nciHeader" />
					</td>
				</tr>
				<tr>
					<td valign="top" height="100%">
						<table height="100%" cellspacing="0" cellpadding="0" summary=""
							border="0" width="100%">
							<tbody>
								<tr>
									<td colspan="2" height="50">
										<%-- include caNanoLab header --%>
										<tiles:insert attribute="cananoHeader" />
									</td>
								</tr>
								<tr>
									<td class="sideMenu" valign="top" width="150">
										<%-- include sidemenu on the left --%>
										<tiles:insert attribute="cananoSidemenu" />
									</td>
									<td valign="top">
										<table height="100%" cellspacing="0" cellpadding="0"
											summary="" border="0" width="100%">
											<tbody>
												<tr>
													<td class="mainMenu" width="100%" height="20">
														<%-- include caNanoLab main menu --%>
														<tiles:insert attribute="cananoMainmenu" />
													</td>
												</tr>
												<tr>
													<td valign="top">
														<%-- include caNanoLab main content --%>
														<%--main content starts --%>
														<tiles:insert attribute="cananoContent" />
													</td>
												</tr>
												<tr>
													<td class="footerMenu" width="100%" height="20">
														<%-- include caNanoLab footer --%>
														<tiles:insert attribute="cananoFooter" />
													</td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						l
						<%-- include NCI footer --%>
						<tiles:insert attribute="nciFooter" />
					</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>
