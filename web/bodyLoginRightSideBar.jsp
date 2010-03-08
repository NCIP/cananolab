<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%">
	<!-- login begins -->
	<tr>
		<td valign="top">
			<jsp:include page="/bodyLoginFields.jsp" />
		</td>
	</tr>
	<!-- login ends -->
	<tr>
		<td valign="top">
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				class="sidebarSectionNoBottom" width="100%">
				<tr>
					<th>
						stats
					</th>
				</tr>
				<tr>
					<td>
						<b>caNanoLab public data counts across sites:</b>
						<br/> <em>(update every 4 hours)</em>
						<ul>
							<li>
								# protocols
							</li>
							<li>
								# samples from # sources
							</li>
							<li>
								# characterizations: # physico-chemical, # in vitro, and # other
							</li>
							<li>
								# publications
							</li>
						</ul>
					</td>
				</tr>
			</table>
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				class="sidebarSectionNoBottom" width="100%">
				<tr>
					<th>
						features
					</th>
				</tr>
				<tr>
					<td>
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
								<a class="sidebarSectionLink" href="#">Secure submission</a> of
								protocols, samples (nanomaterial formulation), and publications
							</li>
							<li>
								Basic search facilities for searching for
								<a class="sidebarSectionLink" href="#">protocols</a>,
								<a class="sidebarSectionLink" href="#">samples</a>, and
								<a class="sidebarSectionLink" href="#">publications</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">Advanced search</a>
								facilities for formulating range and nested queries
							</li>
							<li>
								Tools for managing users via NCI Common Security Module (CSM)
							</li>
							<li>
								Ability to operate as a stand-alone system or as a site in a
								federated system using caBIG<sup>&reg;</sup> grid (caGrid)
							</li>
						</ul>
					</td>
				</tr>
			</table>
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				class="sidebarSectionNoBottom" width="100%">
				<tr>
					<th>
						how to
					</th>
				</tr>
				<tr>
					<td>
						<b>Below are frequently asked questions on caNanoLab:</b>
						<br />
						<br />
						<span class="boldAndUnderlined">Functional</span>
						<ul>
							<li>
								<a class="sidebarSectionLink" href="#">How do I find
									nanotechnology protocols?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">How do I find NCI
									Nanotechnology Allicance publications?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">How can I search for
									nanomaterials from a specific organization or investigator?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">How can I search for
									characterizations for a specific type of nanomaterial?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">Where can I get a
									definition of nanotechnology concepts used in caNanoLab?</a>
							</li>
						</ul>
						<span class="boldAndUnderlined">General</span>
						<ul>

							<li>
								<a class="sidebarSectionLink" href="#">How do I install my
									own caNanoLab system?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">How do I submit data
									into caNanoLab?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">What types of data
									can I submit into caNanoLab?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">Who can I contact to
									submit data into caNanoLab?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">What is a caNanoLab
									site and how do I search across caNanoLab sites?</a>
							</li>
						</ul>
					</td>
				</tr>
			</table>
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				class="sidebarSectionNoBottom" width="100%">
				<tr>
					<th>
						what's new
					</th>
				</tr>
				<tr>
					<td>
						<b>caNanoLab 1.5.1 is now available for </b><a
							class="sidebarSectionLink" href="#">download</a><b>. For
							information on caNanoLab release, please refer to the </b>
						<a class="sidebarSectionLink" href="#">caNanoLab Release Notes</a>.
					</td>
				</tr>
			</table>
		</td>
		<td height="30">
		</td>
	</tr>
</table>