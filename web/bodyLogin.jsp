<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%">
	<tr>
		<td valign="top" width="450">
			<img src="images/bannerhome.jpg" width="450">
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
						portal. caNanoLab is a data sharing portal designed for
						laboratories performing nanoparticle assays. caNanoLab provides
						support for the annotation of nanoparticles with characterizations
						resulting from physical and in vitro nanoparticle assays and the
						sharing of these characterizations in a secure fashion.

						<div id="publicLinks">
							<h2 class="welcomeTitle" id="pubh2">
								Browse caNanoLab
							</h2>
							<div class="griddiv">
								<table class="girdtable">
									<tr>
										<th colspan="2">
											<label class="toplabel" for="location">
												Location
											</label>
											<select name="location" id="location" multiple="true"
													size="3" onchange="getGridCounts(this);">
												<option value="local" selected>
													Local
												</option>
												<option value="caNanoLab-PROD">
													caNanoLab-PROD
												</option>
												<option value="caNanoLab-NCL_PROD">
													caNanoLab-NCL_PROD
												</option>
												<option value="caNanoLab-WUSTL">
													caNanoLab-WUSTL
												</option>
												
											</select>
										</th>
									</tr>
									<tr class="gridTableHeader">
									<th>Data Type</th>
									<th id="results">Results</th>
									</tr>
									<tr>
										<td>
											<img src="images/icon_protocol_48x.jpg" width="15%" />
											&nbsp;Protocols&nbsp;(
											<a href="#" onclick="searchProtocols('location');">search</a> )
										</td>
										<td class="counts">
											<a href="#" onclick="browseProtocols('location');" id="protocolCount" class="countsLink"></a>
										</td>
									</tr>
									<tr class="alt">
										<td>
											<img src="images/icon_nanoparticle_48x.jpg" width="15%" />
											&nbsp;Nanoparticles&nbsp;(
											<a href="#" onclick="searchParitcles('location');">search</a> )
										</td>
										<td class="counts">
											<a href="#" onclick="browseParitcles('location');" id="particleCount" class="countsLink"></a>
										</td>
									</tr>
									<tr>
										<td>
											<img src="images/icon_report_48x.gif" width="15%" />
											&nbsp;Reports&nbsp;(
											<a href="#" onclick="searchReports('location');">search</a> )
										</td>
										<td class="counts">
											<a href="#" onclick="browseReports('location');" id="reportCount" class="countsLink"></a>
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
			<html:form action="/login">
				<table summary="" cellpadding="0" cellspacing="0" border="0"
					height="100%">

					<!-- login begins -->
					<tr>
						<td valign="top">
							<table summary="" cellpadding="2" cellspacing="0" border="0"
								class="sidebarSection" width="100%">
								<tr>
									<td class="sidebarTitle" height="20">
										USER LOGIN
									</td>
								</tr>
								<tr>
									<td class="sidebarContent">
										<jsp:include page="/bodyMessage.jsp" />
									</td>
								</tr>
								<tr>
									<td class="sidebarContent">
										<table cellpadding="2" cellspacing="0" border="0">
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
													&nbsp;&nbsp;
													<a
														href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=welcome_login')"
														class="helpText">Help</a>
													<br>
													<br>
													<input type="button" value="Update Password"
														onClick="javascript:location.href='changePassword.jsp'">
												</td>
											</tr>
										</table>
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
										<strong>caNanoLab 1.3 is now available!</strong>
										<br>
										<br>
										caNanoLab 1.3 contains the following primary features:
										<br>
										<ul>
											<li>
												New navigation and usability enhancements
											</li>
											<li>
												Public Browse for Protocols, Nanoparticles, and Reports
											</li>
											<li>
												Summary Views of Nanoparticle Information
											</li>
											<li>
												Print and Export of Nanoparticle Information
											</li>
										</ul>
										caNanoLab 1.3 expands upon existing caNanoLab functionality
										including:
										<br>
										<ul>
											<li>
												Sample and Aliquot Management
											</li>
											<li>
												Protocol Management
											</li>
											<li>
												Nanoparticle Information (Characterizations) Management
											</li>
											<li>
												Report Management
											</li>
											<li>
												Remote Grid Search (Reports and Physical Composition Data)
											</li>
											<li>
												Role-based Security
											</li>
											<li>
												MySQL 5.0.x Database Support
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
			</html:form>
		</td>
	</tr>
</table>


