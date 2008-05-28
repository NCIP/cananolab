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
							<jsp:include page="/bodyMessage.jsp" />
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
											
											<select name="searchLocations" id="location" multiple="true"
													size="3" onchange="getGridCounts(this);">
<%--												<option value="" />--%>
												<option value="local" selected>
													Local
												</option>
<%--												<html:options collection="allGridNodes" property="key"--%>
<%--																labelProperty="key" />--%>
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
											<a href="#" onclick="searchProtocols();">search</a> )
										</td>
										<td class="counts">
											<a href="#" onclick="browseProtocols();" id="protocolCount" class="countsLink"></a>
										</td>
									</tr>
									<tr class="alt">
										<td>
											<img src="images/icon_nanoparticle_48x.jpg" width="15%" />
											&nbsp;Nanoparticles&nbsp;(
											<a href="#" onclick="searchParitcles();">search</a> )
										</td>
										<td class="counts">
											<a href="#" onclick="browseParitcles();" id="particleCount" class="countsLink"></a>
										</td>
									</tr>
									<tr>
										<td>
											<img src="images/icon_report_48x.gif" width="15%" />
											&nbsp;Reports&nbsp;(
											<a href="#" onclick="searchReports();">search</a> )
										</td>
										<td class="counts">
											<a href="#" onclick="browseReports();" id="reportCount" class="countsLink"></a>
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
										<strong>caNanoLab 1.4 is now available!</strong>
										<br>
										<br>
										caNanoLab 1.4 contains the following primary features:
										<br>
										<ul>
											<li>
												New composition structure to include nanoparticle and functionalizing entities, chemical associations, and composition files
											</li>
											<li>
												New metadata constraints for composition and physical characterizations
											</li>
											<li>
												Additional grid services and seamless local/remote search
											</li>
											<li>
												Disabled sample management functionality and combined with nanoparticle submission
											</li>
											<li>
												New glossary of terms
											</li>
											<li>
												Product upgrades to the caCORE SDK 4.0 and caGrid 1.2
											</li>
										</ul>
										caNanoLab 1.4 expands upon existing caNanoLab functionality
										including:
										<br>
										<ul>
											<li>
												Support for nanoparticle protocols, characterizations, and reports
											</li>
											<li>
												Support for physical and in vitro nanoparticle characterizations
											</li>
											<li>
												Nanoparticle Information (Characterizations) Management
											</li>
											<li>
												Summary views of nanoparticle information with print and export feature
											</li>
											<li>
												Basic local and caBIG<sup>TM</sup> grid (caGrid) search functionality
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


