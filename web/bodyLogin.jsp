<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:form action="/login" focus="loginID">
	<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
		<tr>
			<td valign="top" width="450">
				<img src="images/bannerhome.jpg" width="450">
				<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
					<tr>
						<td class="welcomeTitle" height="20">
							WELCOME TO caLAB
						</td>
					</tr>
					<tr>
						<td class="welcomeContent" valign="top">
							Welcome to the cancer Laboratory Analysis Bench (caLAB)! caLAB is a Laboratory Information Management System (LIMS) designed to facilitate data sharing in laboratories performing nanoparticle assays.  caLAB provides support for the accessioning of samples (nanoparticles), execution of nanoparticle assays, and recording of assay results.  Additionally, caLAB allows for  the  annotation of nanoparticles with characterizations resulting from physical and in vitro nanoparticle assays and the sharing of these characterizations in a secure fashion.
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
				<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">

					<!-- login begins -->
					<tr>
						<td valign="top">
							<table summary="" cellpadding="2" cellspacing="0" border="0" class="sidebarSection" width="100%">
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
													<html:text property="loginId" size="14" />
												</td>
											</tr>
											<tr>
												<td class="sidebarLogin" align="right">
													<label for="password">
														PASSWORD
													</label>
												</td>
												<td class="formFieldLogin">
													<html:password property="password" size="14" />
												</td>
											</tr>
											<tr>
												<td>
													&nbsp;
												</td>
												<td>
													<html:submit value="Login" />													
													&nbsp;&nbsp;<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=welcome_login')" class="helpText">Help</a>
													<br><br><input type="button" value="Update Password" onClick="javascript:location.href='changePassword.jsp'">
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
							<table summary="" cellpadding="0" cellspacing="0" border="0" class="noBottomSidebarSection" width="100%">
								<tr>
									<td class="sidebarTitle" height="20">
										WHAT'S NEW
									</td>
								</tr>
								<tr>
									<td class="sidebarContent">
										<strong>caLAB 1.0 is now available!</strong>
										<br>
										caLAB 1.0 contains the following features:
										<br>
										<ul>
											<li>
												Sample and Aliquot Management
											<li>
												Assay Run Creation
											<li>
												Assay Result Management
											<li>
												Nanoparticle Annotation
											<li>
												Role-based Security
										</ul>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<!-- what's new ends -->

					<!-- did you know? begins -->
					<!-- did you know? ends -->

					<!-- spacer cell begins (keep for dynamic expanding) -->
					<tr height="100%">
						<td valign="top" height="100%">
							<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%" class="noTopBottomSidebarSection">
								<tr>
									<td class="sidebarContent" valign="top">
										&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<!-- spacer cell ends -->

				</table>
			</td>
		</tr>
		<!-- sidebar ends -->
	</table>
</html:form>
