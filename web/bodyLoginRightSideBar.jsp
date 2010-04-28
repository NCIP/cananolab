<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%" width="100%">
	<!-- login begins -->
	<tr>
		<td valign="top">
			<jsp:include page="/bodyLoginFields.jsp" />
		</td>
	</tr>
	<!-- login ends -->
	<tr>
		<td valign="top">
			<table class="sidebarSectionNoBottom" width="100%">
				<tr>
					<td height="20" class="sidebarTitle">
						STATS
					</td>
				</tr>
				<tr>
					<td class="sidebarContent">
						<b>caNanoLab public data counts across sites:</b>						
						<ul>
							<li>
								<span class="grayTextHighlight">${allPublicDataCounts.numOfPublicProtocols}</span> protocols
							</li>
							<li>
								<span class="grayTextHighlight">${allPublicDataCounts.numOfPublicSamples}</span> samples from
								<span class="grayTextHighlight">${allPublicDataCounts.numOfPublicSources}</span> sources
							</li>
							<li>
								<span class="grayTextHighlight">${allPublicDataCounts.numOfPublicCharacterizations}</span>
								characterizations:
								<span class="grayTextHighlight">${allPublicDataCounts.numOfPublicPhysicoChemicalCharacterizations}</span>
								physico-chemical,
								<span class="grayTextHighlight">${allPublicDataCounts.numOfPublicInvitroCharacterizations}</span> in
								vitro,
								<span class="grayTextHighlight">${allPublicDataCounts.numOfPublicInvivoCharacterizations}</span> in
								vivo, and
								<span class="grayTextHighlight">${allPublicDataCounts.numOfPublicOtherCharacterizations}</span> other
							</li>
							<li>
								<span class="grayTextHighlight">${allPublicDataCounts.numOfPublicPublications}</span> publications
							</li>
						</ul>
					</td>
				</tr>
			</table>
			<table class="sidebarSectionNoBottom" width="100%">
				<tr>
					<td height="20" class="sidebarTitle">
						FEATURES
					</td>
				</tr>
				<tr>
					<td class="sidebarContent">
						<b>caNanoLab provides access to information on:</b>
						<ul>
							<li>
								Nanotechnology protocols in biomedicine
							</li>
							<li>
								Composition of nanomaterial formulations
							</li>
							<li>
								Physico-chemical characterizations including size, molecular
								weight, shape, physical state, surface chemistry, purity,
								solubility, and relaxivity
							</li>
							<li>
								In Vitro characterizations such as cytotoxicity, blood contact
								properties, oxidative stress, immune cell functions, and other
							</li>
							<li>
								In Vivo characterizations supporting pharmacokinetics and
								toxicology (coming soon!)
							</li>
							<li>
								Publications and reports from nanotechnology studies in
								biomedicine
							</li>
						</ul>
						<b>Primary caNanoLab features include:</b>
						<ul>
							<li>
								<a href="sample.do?dispatch=setupNew&page=0">Secure
									submission</a> of protocols, samples (nanomaterial formulation),
								and publications
							</li>
							<li>
								Basic search facilities for searching for
								<a href="searchProtocol.do?dispatch=setup&page=0">protocols</a>,
								<a href="searchSample.do?dispatch=setup&page=0">samples</a>, and
								<a href="searchPublication.do?dispatch=setup&page=0">publications</a>
							</li>
							<li>
								<a href="advancedSampleSearch.do?dispatch=setup&page=0">Advanced
									search</a> facilities for formulating range and nested queries
							</li>
							<li>
								Tools for managing users via NCI Common Security Module (CSM)
							</li>
							<li>
								Ability to operate as a stand-alone system or as a site in a
								federated system using caBIG
								<sup>
									&reg;
								</sup>
								grid (caGrid)
							</li>
						</ul>
					</td>
				</tr>
			</table>

			<table class="sidebarSectionNoBottom" width="100%">
				<tr>
					<td height="20" class="sidebarTitle">
						HOW TO
					</td>
				</tr>
				<tr>
					<td class="sidebarContent">
						<b>Below are frequently asked questions on caNanoLab:</b>
						<br />
						<br />
						<span class="boldAndUnderlined">Functional</span>
						<ul>
							<li>
								<a href="searchProtocol.do?dispatch=setup&page=0">How do I
									find nanotechnology protocols?</a>
							</li>
							<li>
								<a href="searchPublication.do?dispatch=setup&page=0">How do
									I find Nanotechnology publications?</a>
							</li>

							<li>
								<a href="searchSample.do?dispatch=setup&page=0">How can I
									search for nanomaterials</a>
							</li>
							<li>
								<a href="searchSample.do?dispatch=setup&page=0">How can I search for nanomaterial
									characterizations?</a>
							</li>
							<li>
								<a href="#" onClick="openWindow('https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+FAQ#caNanoLabFAQ-concepts', 'caNanoLabFAQ', '800', '800')">Where can I get definitions for nanotechnology
									concepts?</a>
							</li>
						</ul>
						<span class="boldAndUnderlined">General</span>
						<ul>

							<li>
								<a
									href="#" onClick="openWindow('https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+FAQ#caNanoLabFAQ-install', 'caNanoLabFAQ', '800', '800')">How
									do I install my own caNanoLab system?</a>
							</li>
							<li>
								<a
									href="#" onClick="openWindow('https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+FAQ#caNanoLabFAQ-submit', 'caNanoLabFAQ', '800', '800')">How
									do I submit data into caNanoLab?</a>
							</li>
							<li>
								<a
									href="#" onClick="openWindow('https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+FAQ#caNanoLabFAQ-datatypes', 'caNanoLabFAQ', '800', '800')">What
									types of data can I submit into caNanoLab?</a>
							</li>
							<li>
								<a
									href="#" onClick="openWindow('https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+FAQ#caNanoLabFAQ-curator', 'caNanoLabFAQ', '800', '800')">Who
									can I contact to submit data into caNanoLab?</a>
							</li>
							<li>
								<a
									href="#" onClick="openWindow('https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+FAQ#caNanoLabFAQ-site', 'caNanoLabFAQ', '800', '800')">What
									is a caNanoLab site?</a>
							</li>
						</ul>
					</td>
				</tr>
			</table>
			<table class="sidebarSectionNoBottom" width="100%">
				<tr>
					<td height="20" class="sidebarTitle">
						WHAT'S NEW
					</td>
				</tr>
				<tr>
					<td class="sidebarContent">
						<b>caNanoLab 1.5.1 is now available for </b><a
							href="http://ncicb.nci.nih.gov/download/downloadcalab.jsp">download</a><b>.
							For information on caNanoLab release, please refer to the </b>
						<a href="#">caNanoLab Release Notes</a>.
					</td>
				</tr>
			</table>
		</td>
		<td height="30">
		</td>
	</tr>
</table>