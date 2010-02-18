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
				class="sidebarSection" width="100%">
				<tr>
					<th>
						stats
					</th>
				</tr>
				<tr>
					<td>
						<b>caNanoLab contains the following public data counts across all grid nodes:</b>
						<ul>
							<li>
								# protocols
							</li>
							<li>
								# samples from of sources
							</li>
							<li>
								# characterizations: # physico-chemical,
								# in vitro, and # other characterizations
							</li>
							<li>
								# publications
							</li>
						</ul>
						<%--
						<br/><br/>
						# of protocols, # of samples, # of physico-chemical
						characterization, # of in vitro charcterization, # of other
						characterizations, # of publications. (across all nodes as of 2/17/10, stats are every # hours)
						--%>
					</td>
				</tr>
			</table>
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				class="sidebarSection" width="100%">
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
								Protocols supporting the characterization and synthesis of
								nanomaterials
							</li>
							<li>
								The composition of nanomaterial formulations including
								functional associations
							</li>
							<li>
								Physico-chemical characterizations performed on nanomaterials
								including size, molecular weight, shape, physical state, surface
								chemistry, purity, solubility, and relaxivity
							</li>
							<li>
								In Vitro characterizations such as cytotoxicity, blood contact
								properties, oxidative stress, immune cell functions, and other
								characterizations
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
								<a class="sidebarSectionLink" href="#">Secure submission</a> of protocols, samples (nanomaterial
								formulation), and publications
							</li>
							<li>
								Basic search facilities for searching for
								<a class="sidebarSectionLink" href="#">protocols</a>,
								<a class="sidebarSectionLink" href="#">samples</a>, and
								<a class="sidebarSectionLink" href="#">publications</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">Advanced search</a> facilities for formulating range and
								nested queries
							</li>
							<li>
								Tools for managing user accounts leveraging the NCI’s Common
								Security Module (CSM)
							</li>
							<li>
								Ability to operate as a stand-alone system or as a site in a
								federated system. caNanoLab operates in a federated system using
								the caBIG grid infrastructure (caGrid).
							</li>
						</ul>					
					</td>
				</tr>
			</table>
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				class="sidebarSection" width="100%">
				<tr>
					<th>
						how to
					</th>
				</tr>
				<tr>
					<td>
						<b>Below are frequently asked questions on caNanoLab usage:</b>
						<br />
						<br />
						<span class="boldAndUnderlined">Functional</span>
						<ul>
							<li>
								<a class="sidebarSectionLink" href="#">How do I find what nanotechnology protocols are being
									used?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">How do I find publications submitted by NCI
									Nanotechnology Alliance members?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">How can I search for nanomaterials from a specific
									organization or investigator?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">How can I search for characterizations for a specific
									type of nanomaterial?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">Where can I get a definition of nanotechnology concepts
									used in caNanoLab?</a>
							</li>
						</ul>
						<span class="boldAndUnderlined">General</span>
						<ul>

							<li>
								<a class="sidebarSectionLink" href="#">How do I install my own caNanoLab system?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">How do I submit data into caNanoLab?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">What types of data can I submit into caNanoLab?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">Who can I contact to send my data for submission into an
									existing caNanoLab System?</a>
							</li>
							<li>
								<a class="sidebarSectionLink" href="#">What is a caNanoLab site and how do I search across
									caNanoLab sites?</a>
							</li>
						</ul>
					</td>
				</tr>
			</table>
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				class="sidebarSection" width="100%">
				<tr>
					<th>
						what's new
					</th>
				</tr>
				<tr>
					<td>
						<b>caNanoLab 1.5.1 is now available for download. For
							information on caNanoLab 1.5.1, please refer to the </b>
						<a class="sidebarSectionLink" href="#">caNanoLab 1.5.1 Release Notes</a>.
					</td>
				</tr>
			</table>
		</td>
		<td height="30">
		</td>
	</tr>
</table>