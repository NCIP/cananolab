<div style="display:block" id="newPointOfContact">
	<a name="submitPointOfContact">
<script type='text/javascript' src='javascript/POCManager.js'></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/POCManager.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th>
			<span id="primaryTitle" style="display: none">Primary</span><span
				id="secondaryTitle" style="display: none">Secondary</span> Point of
			Contact Information
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
							<select name="sampleBean.thePOC.domain.organization.name" onchange="javascript:callPrompt('Organization Name', 'domain.organization.name', 'orgNamePrompt');updateOrganizationInfo();removeOrgFromVisibilityGroups('domain.organization.name'); removeOrgFromSampleVisibilityGroups('domain.organization.name');" id="domain.organization.name"><option value="" />
									<option value="BROWN_STANFORD">BROWN_STANFORD</option>
<option value="CAS_VT_VCU">CAS_VT_VCU</option>
<option value="CLM_UHA_CDS_INSERM">CLM_UHA_CDS_INSERM</option>
<option value="CP_UCLA_CalTech">CP_UCLA_CalTech</option>
<option value="CWRU">CWRU</option>
<option value="GATECH">GATECH</option>

<option value="GATECH_EMORY">GATECH_EMORY</option>
<option value="GATECH_UCSF">GATECH_UCSF</option>
<option value="JSTA_FHU_NEC_MU_AIST_JAPAN">JSTA_FHU_NEC_MU_AIST_JAPAN</option>
<option value="JST_AIST_FHU_TU_NEC_MU_JAPAN">JST_AIST_FHU_TU_NEC_MU_JAPAN</option>
<option value="KU_JSTC_JAPAN">KU_JSTC_JAPAN</option>
<option value="MIT_MGH">MIT_MGH</option>
<option value="MIT_UC_BBIC_HST_CU">MIT_UC_BBIC_HST_CU</option>
<option value="MSKCC_CU_UA">MSKCC_CU_UA</option>
<option value="NCL">NCL</option>

<option value="NEU">NEU</option>
<option value="NEU_MGH_UP_FCCC">NEU_MGH_UP_FCCC</option>
<option value="NEU_MIT_MGH">NEU_MIT_MGH</option>
<option value="NIOSH">NIOSH</option>
<option value="NWU">NWU</option>
<option value="PURDUE">PURDUE</option>
<option value="SNU_CHINA">SNU_CHINA</option>
<option value="STANFORD">STANFORD</option>
<option value="TTU">TTU</option>

<option value="UAM_CSIC_IMDEA">UAM_CSIC_IMDEA</option>
<option value="UCSD_MIT_MGH">UCSD_MIT_MGH</option>
<option value="UC_HU_UEN_GERMANY">UC_HU_UEN_GERMANY</option>
<option value="UI">UI</option>
<option value="UM">UM</option>
<option value="UNC">UNC</option>
<option value="VCU_VT_EHC">VCU_VT_EHC</option>
<option value="WSU">WSU</option>
<option value="WUSTL">WUSTL</option>

<option value="WU_SU_CHINA">WU_SU_CHINA</option>

								<option value="other">
									[other]
								</option></select>
						</div>
					</td>
					<td class="cellLabel" valign="top">
						Role
					</td>

					<td valign="top" colspan="4">
						<div id="rolePrompt">
							<select name="sampleBean.thePOC.domain.role" onchange="javascript:callPrompt('Contact Role', 'domain.role', 'rolePrompt');" id="domain.role"><option />
									<option value="investigator">investigator</option>
<option value="manufacturer">manufacturer</option>

								<option value="other">
									[other]
								</option></select>

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
						<input type="text" name="sampleBean.thePOC.domain.organization.streetAddress1" size="50" value="" id="domain.organization.streetAddress1">
						&nbsp;
					</td>
				</tr>

				<tr>
					<td class="cellLabel">
						Address Line2
					</td>
					<td colspan="5">
						<input type="text" name="sampleBean.thePOC.domain.organization.streetAddress2" size="50" value="" id="domain.organization.streetAddress2">
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
						<input type="text" name="sampleBean.thePOC.domain.organization.city" size="20" value="" id="domain.organization.city">
						&nbsp;
					</td>
					<td class="cellLabel">
						State/Province
					</td>
					<td>
						<input type="text" name="sampleBean.thePOC.domain.organization.state" size="5" value="" id="domain.organization.state">

						&nbsp;
					</td>
					<td class="cellLabel">
						Zip/Postal Code
					</td>
					<td>
						<input type="text" name="sampleBean.thePOC.domain.organization.postalCode" size="10" value="" id="domain.organization.postalCode">
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
						<input type="text" name="sampleBean.thePOC.domain.organization.country" size="30" value="" id="domain.organization.country">
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
						<input type="text" name="sampleBean.thePOC.domain.firstName" size="15" value="" id="domain.firstName">
					</td>
					<td class="cellLabel" valign="top">

						Middle Initial
					</td>
					<td valign="top">
						<input type="text" name="sampleBean.thePOC.domain.middleInitial" size="5" value="" id="domain.middleInitial">
					</td>
					<td class="cellLabel" valign="top">
						Last Name
					</td>
					<td valign="top">
						<input type="text" name="sampleBean.thePOC.domain.lastName" size="15" value="" id="domain.lastName">

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
						<input type="text" name="sampleBean.thePOC.domain.phone" size="30" value="" id="domain.phone">
					</td>
					<td class="cellLabel" valign="top">
						Email
					</td>
					<td valign="top" colspan="4">

						<input type="text" name="sampleBean.thePOC.domain.email" size="30" value="" id="domain.email">
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
						Visibility
					</td>
					<td colspan="5">
						<select name="sampleBean.thePOC.visibilityGroups" multiple="multiple" size="6" id="visibilityGroups"><option value="Public">Public</option>
<option value="BROWN_STANFORD">BROWN_STANFORD</option>
<option value="CAS_VT_VCU">CAS_VT_VCU</option>
<option value="CLM_UHA_CDS_INSERM">CLM_UHA_CDS_INSERM</option>

<option value="CP_UCLA_CalTech">CP_UCLA_CalTech</option>
<option value="CTRAIN">CTRAIN</option>
<option value="CWRU">CWRU</option>
<option value="GATECH">GATECH</option>
<option value="GATECH_EMORY">GATECH_EMORY</option>
<option value="GATECH_UCSF">GATECH_UCSF</option>
<option value="JSTA_FHU_NEC_MU_AIST_JAPAN">JSTA_FHU_NEC_MU_AIST_JAPAN</option>
<option value="JST_AIST_FHU_TU_NEC_MU_JAPAN">JST_AIST_FHU_TU_NEC_MU_JAPAN</option>
<option value="KU_JSTC_JAPAN">KU_JSTC_JAPAN</option>

<option value="MIT_MGH">MIT_MGH</option>
<option value="MIT_UCBBIC_HST_CU">MIT_UCBBIC_HST_CU</option>
<option value="MIT_UC_BBIC_HST_CU">MIT_UC_BBIC_HST_CU</option>
<option value="MSKCC_CU_UA">MSKCC_CU_UA</option>
<option value="NCL">NCL</option>
<option value="NCL_DataCurator">NCL_DataCurator</option>
<option value="NCL_Researcher">NCL_Researcher</option>
<option value="NEU">NEU</option>
<option value="NEU_MGH_UP_FCCC">NEU_MGH_UP_FCCC</option>

<option value="NEU_MIT_MGH">NEU_MIT_MGH</option>
<option value="NIOSH">NIOSH</option>
<option value="NWU">NWU</option>
<option value="PURDUE">PURDUE</option>
<option value="SNU_CHINA">SNU_CHINA</option>
<option value="STANFORD">STANFORD</option>
<option value="TTU">TTU</option>
<option value="UAM-CSIC-IMDEA">UAM-CSIC-IMDEA</option>
<option value="UAM_CSIC_IMDEA">UAM_CSIC_IMDEA</option>

<option value="UCSD_MIT_MGH">UCSD_MIT_MGH</option>
<option value="UC_HU_UEN_GERMANY">UC_HU_UEN_GERMANY</option>
<option value="UI">UI</option>
<option value="UM">UM</option>
<option value="UNC">UNC</option>
<option value="VCU_VT_EHC">VCU_VT_EHC</option>
<option value="WSU">WSU</option>
<option value="WUSTL">WUSTL</option>
<option value="WU_SU_CHINA">WU_SU_CHINA</option></select>

						<br>
						<i>(WUSTL_Researcher,
							WUSTL_DataCurator, and the organization name are
							always selected by default.)</i>
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
						
							<input type="button" value="Remove"
								onclick="removePointOfContact('sample');clearPointOfContact();"
								id="deletePointOfContact" style="display: none;" />
						
					</td>
					<td>
						<div align="right">
							<input type="button" value="Save"
								onclick="addPointOfContact('sample')" />

							<input type="button" value="Cancel"
								onclick="closeSubmissionForm('PointOfContact');" />
							<input type="hidden" name="sampleBean.thePOC.domain.id" value="" id="domain.id">
							<input type="hidden" name="sampleBean.thePOC.primaryStatus" value="true" id="primaryStatus">
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>