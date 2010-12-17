<form name="sampleForm" method="post" action="/caNanoLab/sample.do"
	onsubmit="return validateSavingTheData('newPointOfContact', 'point of contact');">
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="20%">

				Sample Name *
			</td>
			<td>
				<input type="text" name="sampleBean.domain.name" size="80" value="">

			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Point of Contact *
			</td>
			<td>
				<a href="#"
					onclick="javascript:confirmAddNew(['Access'], 'PointOfContact', 'Point Of Contact', 'clearPointOfContact()');"
					id="addPointOfContact" style="display: none"><img align="top"
						src="images/btn_add.gif" border="0" /> </a>
			</td>
		</tr>

		<tr>
			<td colspan="2">





				<div style="display: block" id="newPointOfContact">
					<a name="submitPointOfContact"> <script type='text/javascript'
							src='javascript/POCManager.js'></script> <script
							type='text/javascript'
							src='/caNanoLab/dwr/interface/POCManager.js'></script> <script
							type='text/javascript' src='/caNanoLab/dwr/engine.js'></script> <script
							type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

						<table class="subSubmissionView" width="85%" align="center">
							<tr>
								<th>
									<span id="primaryTitle" style="display: none">Primary</span><span
										id="secondaryTitle" style="display: none">Secondary</span>
									Point of Contact Information
								</th>

							</tr>
							<tr>
								<td>
									<table>
										<tr>
											<td class="cellLabel">
												Organization Name*
											</td>
											<td>

												<div id="orgNamePrompt">
													<select name="sampleBean.thePOC.domain.organization.name"
														onchange="javascript:callPrompt('Organization Name', 'domain.organization.name', 'orgNamePrompt');updateOrganizationInfo();"
														id="domain.organization.name">
														<option value="" />
															<option value="BROWN_STANFORD">
																BROWN_STANFORD
															</option>
															<option value="C-Sixty (CNI)">
																C-Sixty (CNI)
															</option>
															<option value="CAS_VT_VCU">
																CAS_VT_VCU
															</option>
															<option value="CLM_UHA_CDS_INSERM">
																CLM_UHA_CDS_INSERM
															</option>
															<option value="CP_UCLA_CalTech">
																CP_UCLA_CalTech
															</option>
															<option value="CWRU">
																CWRU
															</option>

															<option value="DNT">
																DNT
															</option>
															<option value="DWU_SNU_SU_US">
																DWU_SNU_SU_US
															</option>
															<option value="GATECH">
																GATECH
															</option>
															<option value="GATECH_EMORY">
																GATECH_EMORY
															</option>
															<option value="GATECH_UCSF">
																GATECH_UCSF
															</option>
															<option value="Harvard_MIT_DHST">
																Harvard_MIT_DHST
															</option>
															<option value="JSTA_FHU_NEC_MU_AIST_JAPAN">
																JSTA_FHU_NEC_MU_AIST_JAPAN
															</option>
															<option value="JSTA_JFCR_NEC_FHU_TUSM_NIAIST">
																JSTA_JFCR_NEC_FHU_TUSM_NIAIST
															</option>
															<option value="JST_AIST_FHU_TU_NEC_MU_JAPAN">
																JST_AIST_FHU_TU_NEC_MU_JAPAN
															</option>

															<option value="Joe Barchi">
																Joe Barchi
															</option>
															<option value="KI">
																KI
															</option>
															<option value="KU_JSTC_JAPAN">
																KU_JSTC_JAPAN
															</option>
															<option value="LMRT">
																LMRT
															</option>
															<option value="MIT_ChemD">
																MIT_ChemD
															</option>
															<option value="MIT_LMRT">
																MIT_LMRT
															</option>
															<option value="MIT_MGH">
																MIT_MGH
															</option>
															<option value="MIT_MGH_GIST">
																MIT_MGH_GIST
															</option>
															<option value="MIT_UC_BBIC_HST_CU">
																MIT_UC_BBIC_HST_CU
															</option>

															<option value="MSKCC_CU_UA">
																MSKCC_CU_UA
															</option>
															<option value="Mansoor Amiji">
																Mansoor Amiji
															</option>
															<option value="Mark Kester">
																Mark Kester
															</option>
															<option value="Mark Kester PSU">
																Mark Kester PSU
															</option>
															<option value="NCL">
																NCL
															</option>
															<option value="NEU">
																NEU
															</option>
															<option value="NEU_DPS">
																NEU_DPS
															</option>
															<option value="NEU_MGH_UP_FCCC">
																NEU_MGH_UP_FCCC
															</option>
															<option value="NEU_MIT_MGH">
																NEU_MIT_MGH
															</option>

															<option value="NIOSH">
																NIOSH
															</option>
															<option value="NWU">
																NWU
															</option>
															<option value="NWU_ChemD_IIN">
																NWU_ChemD_IIN
															</option>
															<option
																value="Nanotechnology Characterization Laboratory">
																Nanotechnology Characterization Laboratory
															</option>
															<option value="PNNL_CBBG">
																PNNL_CBBG
															</option>
															<option value="PURDUE">
																PURDUE
															</option>
															<option value="PURDUEU_BN">
																PURDUEU_BN
															</option>
															<option value="RIT_KI_SU">
																RIT_KI_SU
															</option>
															<option value="SNU_CHINA">
																SNU_CHINA
															</option>

															<option value="STANFORD">
																STANFORD
															</option>
															<option value="SUNYB_ILPB">
																SUNYB_ILPB
															</option>
															<option value="TAM_UT">
																TAM_UT
															</option>
															<option value="TTU">
																TTU
															</option>
															<option value="UAM_CSIC_IMDEA">
																UAM_CSIC_IMDEA
															</option>
															<option value="UCSD_ChemBiochemD">
																UCSD_ChemBiochemD
															</option>
															<option value="UCSD_MIT_MGH">
																UCSD_MIT_MGH
															</option>
															<option value="UC_HU_UEN_GERMANY">
																UC_HU_UEN_GERMANY
															</option>
															<option value="UI">
																UI
															</option>

															<option value="UKY">
																UKY
															</option>
															<option value="UL_NL">
																UL_NL
															</option>
															<option value="UM">
																UM
															</option>
															<option value="UM-C">
																UM-C
															</option>
															<option value="UMC_DVP">
																UMC_DVP
															</option>
															<option value="UMC_RadiolD">
																UMC_RadiolD
															</option>
															<option value="UN">
																UN
															</option>
															<option value="UNC">
																UNC
															</option>
															<option value="UNC_ChemD">
																UNC_ChemD
															</option>

															<option value="UToronto">
																UToronto
															</option>
															<option value="VCU_VT_EHC">
																VCU_VT_EHC
															</option>
															<option value="WSU">
																WSU
															</option>
															<option value="WUSTL">
																WUSTL
															</option>
															<option value="WUSTL_DIM">
																WUSTL_DIM
															</option>
															<option value="WU_SU_CHINA">
																WU_SU_CHINA
															</option>

															<option value="other">
																[other]
															</option>
													</select>

												</div>
											</td>
											<td class="cellLabel" valign="top">
												Role
											</td>
											<td valign="top" colspan="4">
												<div id="rolePrompt">
													<select name="sampleBean.thePOC.domain.role"
														onchange="javascript:callPrompt('Contact Role', 'domain.role', 'rolePrompt');"
														id="domain.role">
														<option />
															<option value="investigator">
																investigator
															</option>

															<option value="manufacturer">
																manufacturer
															</option>

															<option value="other">
																[other]
															</option>
													</select>
												</div>
											</td>
										</tr>
									</table>
								</td>

							</tr>
							<tr>
								<td>
									<table>
										<tr>
											<td class="cellLabel">
												Address Line1
											</td>
											<td colspan="5">

												<input type="text"
													name="sampleBean.thePOC.domain.organization.streetAddress1"
													size="50" value="" id="domain.organization.streetAddress1">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td class="cellLabel">
												Address Line2
											</td>
											<td colspan="5">

												<input type="text"
													name="sampleBean.thePOC.domain.organization.streetAddress2"
													size="50" value="" id="domain.organization.streetAddress2">
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>

									<table>
										<tr>
											<td class="cellLabel">
												City
											</td>
											<td>
												<input type="text"
													name="sampleBean.thePOC.domain.organization.city" size="20"
													value="" id="domain.organization.city">
												&nbsp;
											</td>

											<td class="cellLabel">
												State/Province
											</td>
											<td>
												<input type="text"
													name="sampleBean.thePOC.domain.organization.state" size="5"
													value="" id="domain.organization.state">
												&nbsp;
											</td>
											<td class="cellLabel">
												Zip/Postal Code
											</td>

											<td>
												<input type="text"
													name="sampleBean.thePOC.domain.organization.postalCode"
													size="10" value="" id="domain.organization.postalCode">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>

									<table>
										<tr>
											<td class="cellLabel">
												Country
											</td>
											<td>
												<input type="text"
													name="sampleBean.thePOC.domain.organization.country"
													size="30" value="" id="domain.organization.country">
											</td>
										</tr>

									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table>
										<tr>
											<td class="cellLabel" valign="top">
												First Name
											</td>

											<td valign="top">
												<input type="text" name="sampleBean.thePOC.domain.firstName"
													size="15" value="" id="domain.firstName">
											</td>
											<td class="cellLabel" valign="top">
												Middle Initial
											</td>
											<td valign="top">
												<input type="text"
													name="sampleBean.thePOC.domain.middleInitial" size="5"
													value="" id="domain.middleInitial">
											</td>

											<td class="cellLabel" valign="top">
												Last Name
											</td>
											<td valign="top">
												<input type="text" name="sampleBean.thePOC.domain.lastName"
													size="15" value="" onchange="updatePersonInfo()"
													id="domain.lastName">
											</td>
										</tr>
									</table>
								</td>

							</tr>
							<tr>
								<td>
									<table>
										<tr>
											<td class="cellLabel" valign="top">
												Phone Number
											</td>
											<td valign="top">

												<input type="text" name="sampleBean.thePOC.domain.phone"
													size="30" value="" id="domain.phone">
											</td>
											<td class="cellLabel" valign="top">
												Email
											</td>
											<td valign="top" colspan="4">
												<input type="text" name="sampleBean.thePOC.domain.email"
													size="30" value="" id="domain.email">
											</td>
										</tr>

									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%">
										<tr>
											<td>

											</td>

											<td>
												<div align="right">
													<input type="button" value="Save"
														onclick="addPointOfContact('sample')" />
													<input type="button" value="Cancel"
														onclick="clearPointOfContact();closeSubmissionForm('PointOfContact');" />
													<input type="hidden" name="sampleBean.thePOC.domain.id"
														value="" id="domain.id">
													<input type="hidden" name="sampleBean.thePOC.primaryStatus"
														value="true" id="primaryStatus">
												</div>
											</td>
										</tr>

									</table>
								</td>
							</tr>
						</table> </a>
				</div>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">

				Keywords
				<i>(one keyword per line)</i>
			</td>
			<td>
				<textarea name="sampleBean.keywordsStr" cols="80" rows="6"></textarea>
			</td>
		</tr>

















		<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>

		<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
		<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
		<script type="text/javascript"
			src="javascript/AccessibilityManager.js"></script>
		<script type='text/javascript'
			src='/caNanoLab/dwr/interface/AccessibilityManager.js'></script>
		<link rel="StyleSheet" type="text/css" href="css/promptBox.css">



		<tr>
			<td class="cellLabel" width="15%" id="addAccessLabel">
				Access to the Sample
			</td>

			<td>

				<a href="#"
					onclick="confirmAddNew(['PointOfContact'], 'Access', 'Access', 'clearAccess(\'sampleForm\', \'Sample\')');"
					id="addAccess" style="display: block"><img align="top"
						src="images/btn_add.gif" border="0" /> </a>
			</td>
		</tr>
		<tr>
			<td colspan="2">


			</td>
		</tr>

		<tr>
			<td colspan="2">


			</td>
		</tr>
		<tr>
			<td colspan="2">


				<div style="display: none" id="newAccess">
					<table class="subSubmissionView" width="85%" align="center">
						<tr>

							<th colspan="4">
								Access Information
							</th>
						</tr>
						<tr>
							<td class="cellLabel" width="25%">
								Access by *
							</td>
							<td colspan="3">
								<input type="radio" name="sampleBean.theAccess.accessBy"
									value="group" checked="checked"
									onclick="displayAccessNameLabel();" id="byGroup">

								Collaboration Group
								<input type="radio" name="sampleBean.theAccess.accessBy"
									value="user" onclick="displayAccessNameLabel();" id="byUser">
								User&nbsp;&nbsp;

								<input type="radio" name="sampleBean.theAccess.accessBy"
									value="public" onclick="displayAccessNameLabel();"
									id="byPublic">
								Public

							</td>
						</tr>
						<tr>

							<td class="cellLabel" id="accessNameLabel">

								Collaboration Group Name *
							</td>
							<td>
								<input type="text" name="sampleBean.theAccess.groupName"
									value="" id="groupName">
								<input type="text"
									name="sampleBean.theAccess.userBean.loginName" value=""
									id="userName" style="display: none">
							</td>
							<td>
								<a href="#userNameField" id="browseIcon"
									onclick="javascript:showMatchedGroupOrUserDropdown('')"><img
										src="images/icon_browse.jpg" align="middle"
										alt="search existing collaboration groups" border="0" /> </a>
							</td>

							<td width="50%">
								<table class="invisibleTable">
									<tr>
										<td>
											<img src="images/ajax-loader.gif" border="0" class="counts"
												id="loaderImg" style="display: none">
										</td>
										<td>
											<select name="sampleBean.theAccess.userBean.loginName"
												size="10" onclick="updateUserLoginName()"
												id="matchedUserNameSelect" style="display: none"></select>
											<select name="sampleBean.theAccess.groupName" size="10"
												onclick="updateGroupName()" id="matchedGroupNameSelect"
												style="display: none"></select>

										</td>
										<td>
											<a id="cancelBrowse" style="display: none"
												href="javascript:cancelBrowseSelect();">Cancel</a>
										</td>
									</tr>
								</table>
							</td>
						</tr>

						<tr>
							<td class="cellLabel" width="10%">
								Access to the Sample *
							</td>
							<td colspan="2">
								<select name="sampleBean.theAccess.roleName" id="roleName">
									<option></option>
									<option value="R">
										read
									</option>
									<option value="CURD">
										read update delete
									</option>
								</select>
							</td>

							<td></td>
						</tr>
						<tr>
							<td>
								<input id="deleteAccess" type="button" value="Remove"
									onclick="javascript:deleteTheAccess('sample', 4);"
									style="display: none;">
							</td>
							<td align="right" colspan="3">
								<div align="right">
									<input type="hidden" name="sampleBean.theAccess.roleName"
										value="" id="hiddenRoleName">

									<input type="hidden" name="sampleBean.theAccess.groupName"
										value="" id="hiddenGroupName">
									<input type="button" value="Save"
										onclick="javascript:addAccess('sample', 4, false);" />
									<input type="button" value="Cancel"
										onclick="javascript:clearAccess('sampleForm', 'Sample');closeSubmissionForm('Access');">
								</div>
							</td>
						</tr>
					</table>
				</div>
			</td>

		</tr>


	</table>
	<br>




















	<table width="100%" class="invisibleTable">
		<tr>
			<td align="left" width="600">


			</td>
			<td align="right" width="300">



				<input type="reset" value="Reset" onclick="this.form.reset();"
					id="resetButton" />
				&nbsp;







				<input type="submit" value="Submit" disabled="disabled"
					id="submitButton">


				<input type="hidden" name="dispatch" value="create">
				<input type="hidden" name="page" value="2">


			</td>
		</tr>
	</table>