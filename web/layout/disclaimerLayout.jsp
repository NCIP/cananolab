<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html>
	<head>
		<title><tiles:getAsString name="title" ignore="true" /></title>
		<meta name="keywords"
			content="nano informatics, nanotechnology model, caNanoLab, nanotechnology, nanoparticle, cancer, information model, portal, data portal, data repository, caBIG, caGRID, NCL, nano characterization, nanoparticle composition, Cancer Nanotechnology Excellence">
		<meta name="description"
			content="caNanoLab is a data sharing portal designed to facilitate information sharing in the biomedical nanotechnology research community to expedite and validate the use of nanotechnology in biomedicine">
		<link rel="icon" href="images/favicon.ico" />
		<link rel="shortcut icon" href="images/favicon.ico" />
		<LINK rel="stylesheet" type="text/css" href="css/main.css">
	</head>
	<tiles:importAttribute scope="session" />
	<body>
		<table height="100%" cellspacing="0" cellpadding="0" width="100%" summary="" border="0" align="center">
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
						<table cellspacing="0" cellpadding="0" summary="" border="0">
							<tbody>
								<tr>
									<td height="50">
										<%-- include caNanoLab header --%>
										<tiles:insert attribute="cananoHeader" />
									</td>
								</tr>
								<tr>
									<td>
										<tiles:insert attribute="cananoContent" />
									</td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<tr>
					<td class="footerMenu" width="100%" height="20">
						<%-- include caNanoLab footer --%>
						<tiles:insert attribute="cananoFooter" />
					</td>
				</tr>
				<tr>
					<td>
						<%-- include NCI footer --%>
						<tiles:insert attribute="nciFooter" />
					</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>