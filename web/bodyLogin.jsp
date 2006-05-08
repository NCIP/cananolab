<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:form action="/login" focus="loginID">
	<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
		<tr>
			<td valign="top" width=600>
				<img src="images/bannerhome.jpg" width="600" height="140">
				<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
					<tr>
						<td class="welcomeTitle" height="20">
							WELCOME TO caLAB
						</td>
					</tr>
					<tr>
						<td class="welcomeContent" valign="top">
							Welcome to the cancer Laboratory Analysis Bench (caLAB). caLAB is a Laboratory Information Management System (LIMS) designed to capture the workflow in the laboratory. caLAB provides support for the execution of assays and the recording of assay
							results. Additionally, caLAB allows for the management of laboratory inventory (samples) that is leveraged as input to laboratory assays.
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
				<table summary="" cellpadding="0" cellspacing="0" border="1" height="100%">

					<!-- login begins -->
					<tr>
						<td valign="top" width="210">
							<table summary="" cellpadding="2" cellspacing="0" border="0" class="sidebarSection">
								<tr>
									<td class="sidebarTitle" height="20">
										USER LOGIN
									</td>
								</tr>
								<tr>
									<td class="sidebarContent">
										<jsp:include page="/bodyMessage.jsp"/>
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
													<html:submit value="Login" />&nbsp;&nbsp;<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=welcome_login')" class="helpText">Help</a>
												</td>
											</tr>
											<tr>
												<td colspan="2">

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
						<td valign="top" width="210">
							<table summary="" cellpadding="0" cellspacing="0" border="0" class="sidebarSection">
								<tr>
									<td class="sidebarTitle" height="20">
										WHAT'S NEW
									</td>
								</tr>
								<tr>
									<td class="sidebarContent">
										<strong>caLAB .50 is now available!</strong>
										<br>
										caLAB .50 contains the following features:
										<br>
										<ul>
											<li>
												Sample Management
											<li>
												Aliquot Creation
											<li>
												Create Assay Run
											<li>
												Submit Assay Results
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
						<td valign="top" height="100%" width="210">
							<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%" class="sidebarSection">
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
				<!-- sidebar ends -->

			</td>
		</tr>
	</table>
</html:form>
