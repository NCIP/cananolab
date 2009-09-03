<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%">
	<tr>
		<td valign="top" width="600">
			<img src="images/bannerhome.jpg" width="600">
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				height="100%">
				<tr>
					<td class="welcomeTitle" height="20">
						WELCOME TO caNanoLab
					</td>
				</tr>
				<tr>
					<td class="welcomeContent" valign="top">
						Welcome to the
						<strong>cancer Nanotechnology Laboratory (caNanoLab)</strong>
						portal. caNanoLab is a data sharing portal designed to facilitate
						information sharing in the biomedical nanotechnology research
						community to expedite and validate the use of nanotechnology in
						biomedicine. caNanoLab allows researchers to share information on
						nanoparticles including the composition of the particle, the
						functions (e.g. therapeutic, targeting, diagnostic imaging) of the
						particle, the characterizations of the particle from
						physico-chemical (e.g. size, molecular weight, surface) and in
						vitro (e.g. cytotoxicity, blood contact) nanoparticle assays, and
						the protocols of these characterization.
						<div class="message">
							<jsp:include page="/bodyMessage.jsp" />
						</div>
						<div id="publicLinks">
							<h2 class="pubBrowse">
								Browse caNanoLab
							</h2>
							<div class="griddiv">
								<table class="gridtable">
									<tr class="gridTableHeader">
										<th>
											Site
										</th>
										<th>
											Data Type
										</th>
										<th id="results">
											Public Results
										</th>
									</tr>
									<tr align="left">
										<td rowspan="3">
											<select name="searchLocations" id="sites" multiple="true"
												size="4" onchange="getPublicCounts();">
												<option value="${applicationOwner}" selected="selected">
													${applicationOwner}
												</option>
												<c:forEach var="location" items="${allGridNodes}">
													<option value="${location.hostName}">
														${location.hostName}
													</option>
												</c:forEach>
											</select>
										</td>
										<td>
											<table class="gridtableNoBorder">
												<tr>
													<td rowspan="2">
														<a href="#" onclick="gotoProtocols('setup')">
															<img src="images/icon_protocol_48x.jpg" style="border-style: none;" alt="Search Protocals"/>
														</a>
													</td>
													<td>
														<a href="#" onclick="gotoProtocols('setup')"><b>Search Protocols</b></a>
													</td>
												</tr>
												<tr>
													<td>
														Search for nanotechnology protocols leveraged in performing
														nanoparticle characterization assays.
													</td>
												</tr>
											</table>
										</td>
										<td style="padding-left:22px">							
											<span id="protocolCount"><img src="images/ajax-loader.gif"></span>
										</td>
									</tr>
									<tr class="alt">
										<td>
											<table class="gridtableNoBorder">
												<tr>
													<td rowspan="2">
														<a href="#" onclick="gotoSamples('setup')">
															<img src="images/icon_nanoparticle_48x.jpg" style="border-style: none;" alt="Search Samples"/>
														</a>
													</td>
													<td>
														<a href="#" onclick="gotoSamples('setup')"><b>Search Samples</b></a>
													</td>
												</tr>
												<tr>
													<c:url var="advanceSearchUrl" value="advancedSampleSearch.do">
														<c:param name="dispatch" value="setup" />
													</c:url>
													<td>
														Search for information on nanoparticle formulations including
														the composition of the particle, results of nanoparticle
														physico-chemical, in vitro, and other characterizations, and
														associated publications. See also <a href="${advanceSearchUrl}" id="advanceSearch">Advanced Sample Search</a>.
													</td>
												</tr>
											</table>
										</td>
										<td style="padding-left:22px">							
											<span id="sampleCount"><img src="images/ajax-loader.gif"></span>
										</td>
									</tr>
									<tr>
										<td>
											<table class="gridtableNoBorder">
												<tr>
													<td rowspan="2">
														<a href="#" onclick="gotoPublications('setup')">
															<img src="images/icon_report_48x.gif" style="border-style: none;" alt="Search Publications"/>
														</a>
													</td>
													<td>
														<a href="#" onclick="gotoPublications('setup')"><b>Search Publications</b></a>
													</td>
												</tr>
												<tr>
													<td>
														Search for information on nanotechnology publications
														including peer reviewed articles, reviews, and other types of
														reports related to the use of nanotechnology in biomedicine.
													</td>
												</tr>
											</table>
										</td>
										<td style="padding-left:22px">							
											<span id="publicationCount"><img src="images/ajax-loader.gif"></span>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</td>
				</tr>
			</table>
			<!-- welcome ends -->
			<p>
				&nbsp;
			</p>
		</td>
		<td valign="top">

			<!-- sidebar begins -->
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				height="100%">

				<!-- login begins -->
				<tr>
					<td valign="top">
						<c:set var="loginDisplay" value="display: none" />
						<c:if test="${!empty param.loginDisplay}">
							<c:set var="loginDisplay" value="display: block" />
						</c:if>
						<table summary="" cellpadding="2" cellspacing="0" border="0"
							class="sidebarSection" width="100%">
							<tr>
								<td class="sidebarTitle" height="25">
									<span class="loginLink"><a href="#" class="loginText"
										onClick="displayLogin();">Login</a> </span>
									<span class="loginLink"><a href="#" class="loginText"
										onClick="javascript:location.href='changePassword.jsp'">Update
											Password</a> </span>
									<span class="loginLink"><a
										href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=welcome_login')"
										class="loginText">Help</a> </span>
								</td>
							</tr>
							<tr>
								<td class="sidebarContent">
									<html:form action="/login">
										<table cellpadding="2" cellspacing="0" border="0"
											id="loginBlock" style="${loginDisplay}">
											<tr>
												<td class="sidebarLogin" align="right">
													<label for="loginID">
														LOGIN ID
													</label>
												</td>
												<td class="formFieldLogin">
													<input type="text" name="loginId" size="14" />
												</td>
											</tr>
											<tr>
												<td class="sidebarLogin" align="right">
													<label for="password">
														PASSWORD
													</label>
												</td>
												<td class="formFieldLogin">
													<input type="password" name="password" size="14" />
												</td>
											</tr>
											<tr>
												<td>
													&nbsp;
												</td>
												<td>
													<html:submit value="Login" />
												</td>
											</tr>
										</table>
										<input type="hidden" name="loginDisplay" value="display:block" />
									</html:form>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<!-- login ends -->
				<!-- what's new begins -->
				<tr>
					<td valign="top">
						<table summary="" cellpadding="0" cellspacing="0" border="0"
							class="noBottomSidebarSection" width="100%">
							<tr>
								<td class="sidebarTitle" height="20">
									WHAT'S NEW
								</td>
							</tr>
							<tr>
								<td class="sidebarContent">
									<strong>caNanoLab 1.5 is now available!</strong>
									<br>
									<br>
									caNanoLab 1.5 contains the following primary features:
									<br>
									<ul>
										<li>
											Support for the creation of custom assay characterizations
										</li>
										<li>
											Enhanced Source Metadata including support for investigators and manufacturers associated with a nanoparticle characterization
										</li>
										<li>
											Support for conditions (temperature, time, media solvent, etc.) associated with characterizations
										</li>
										<li>
											Curated techniques and instruments used in characterization assays
										</li>
										<li>
											Enhanced summary views for particle composition, characterizations, and publications
										</li>
										<li>
											Cross-references to PubChem for chemical names associated with material components
										</li>
										<li>
											Inclusion of particle relaxivity as a physico-chemical characterization
										</li>
										<li>
											Administrative functions supporting site preferences (site name, logo)
										</li>
										<li>
											Upgrade to the latest version of the NCI Common Security Module (CSM) which provides support for user authentication and authorization
										</li>
									</ul>
									caNanoLab 1.5 expands upon existing caNanoLab functionality	including:
									<br>
									<ul>
										<li>
											Support for the composition and structure of nanoparticles including material entities, functionalizing entities, and chemical associations
										</li>
										<li>
											Support for nanoparticle protocols, characterizations, and publications
										</li>
										<li>
											Support for physico-chemical and in vitro nanoparticle characterizations
										</li>
										<li>
											Summary views of nanoparticle characterizations with print, export, and delete features
										</li>
										<li>
											Basic local and caBIG&reg; grid (caGrid) remote search functionality
										</li>
										<li>
											Role-based security supporting user authentication and authorization
										</li>
										<li>
											Product upgrades to the caCORE SDK 4.0, caGrid 1.3.0.1, and MySQL 5.0.x Database
										</li>
									</ul>
									<br>
								</td>
							</tr>
							<tr height="100%">
								<td>
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr height="100%">
					<td class="loginMenuFill">
						&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


