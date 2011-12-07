<form name="protocolForm" method="post" action="/caNanoLab/protocol.do" enctype="multipart/form-data">












	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel">

				Protocol Type*
			</td>
			<td>
				<div id="protocolTypePrompt">
					<select name="protocol.domain.type" onchange="javascript:callPrompt('Protocol Type', 'protocolType', 'protocolTypePrompt'); retrieveProtocolNames();" id="protocolType"><option value="" />
							<option value="in vitiro 3">in vitiro 3</option>
<option value="in vitro assay">in vitro assay</option>
<option value="in vivo assay">in vivo assay</option>
<option value="physico-chemical assay">physico-chemical assay</option>

<option value="radio labeling">radio labeling</option>
<option value="safety">safety</option>
<option value="sample preparation">sample preparation</option>
<option value="sterility">sterility</option>
<option value="synthesis">synthesis</option>
<option value="test">test</option>
<option value="test1">test1</option>

							<option value="other">

								[other]
							</option></select>
				</div>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Protocol Name*
			</td>
			<td>

				<div id="protocolNamePrompt">
					<select name="protocol.domain.name" onchange="javascript:callPrompt('Protocol Name', 'protocolName', 'protocolNamePrompt'); retrieveProtocolVersions()" id="protocolName"><option value="" />

							<option value="other">
								[other]
							</option></select>
				</div>
			</td>
		</tr>
		<tr>

			<td class="cellLabel">
				Protocol Version*
			</td>
			<td>
				<div id="protocolVersionPrompt">
					<select name="protocol.domain.version" onchange="javascript:callPrompt('Protocol Version', 'protocolVersion', 'protocolVersionPrompt');retrieveProtocol('');" id="protocolVersion"><option value="" />

							<option value="other">
								[other]
							</option></select>
				</div>

			</td>
		</tr>
		<tr>
			<td class="cellLabel" width="20%">
				Protocol Abbreviation
			</td>
			<td>
				<input type="text" name="protocol.domain.abbreviation" size="30" value="" id="protocolAbbreviation">
			</td>

		</tr>
		<tr>
			<td class="cellLabel">
				Protocol File
			</td>
			<td>
				<input type="file" name="protocol.fileBean.uploadedFile" value="" onchange="javascript:writeLink(null);">
				&nbsp;&nbsp;
				<span id="protocolFileLink">  </span>&nbsp;

			</td>
			<input type="hidden" name="protocol.domain.id" value="" id="protocolId">
			<input type="hidden" name="protocol.fileBean.domainFile.id" value="" id="fileId">
			<input type="hidden" name="protocol.fileBean.domainFile.uri" value="" id="fileUri">
			<input type="hidden" name="protocol.fileBean.domainFile.name" value="" id="fileName">
		</tr>
		<tr>
			<td class="cellLabel">
				File Title
			</td>

			<td>
				<input type="text" name="protocol.fileBean.domainFile.title" size="100" value="" id="fileTitle">
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Description
			</td>
			<td>

				<textarea name="protocol.fileBean.domainFile.description" cols="80" rows="3" id="fileDescription"></textarea>
			</td>
		</tr>

















<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript" src="javascript/AccessibilityManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/AccessibilityManager.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">



	<tr>
		<td class="cellLabel" width="15%" id="addAccessLabel">
			Access to the Protocol
		</td>
		<td>

			<a href="#"
				onclick="confirmAddNew(['PointOfContact'], 'Access', 'Access', 'clearAccess(\'protocolForm\', \'Protocol\')');"
				id="addAccess" style="display:block"><img
					align="top" src="images/btn_add.gif" border="0" />
			</a>

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


			<div style="display:none" id="newAccess">
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
							<input type="radio" name="protocol.theAccess.accessBy" value="group" checked="checked" onclick="displayAccessNameLabel();" id="byGroup">
							Collaboration Group
							<input type="radio" name="protocol.theAccess.accessBy" value="user" onclick="displayAccessNameLabel();" id="byUser">
							User&nbsp;&nbsp;


								<input type="radio" name="protocol.theAccess.accessBy" value="public" onclick="displayAccessNameLabel();" id="byPublic">
							Public

						</td>
					</tr>
					<tr>

						<td class="cellLabel" id="accessNameLabel">
							Collaboration Group Name *
						</td>
						<td>
							<input type="text" name="protocol.theAccess.groupName" value="" id="groupName">

							<input type="text" name="protocol.theAccess.userBean.loginName" value="" id="userName" style="display:none">
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
										<select name="protocol.theAccess.userBean.loginName" size="10" onclick="updateUserLoginName()" id="matchedUserNameSelect" style="display: none"></select>
										<select name="protocol.theAccess.groupName" size="10" onclick="updateGroupName()" id="matchedGroupNameSelect" style="display: none"></select>
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
							Access to the Protocol *
						</td>

						<td colspan="2">
							<select name="protocol.theAccess.roleName" id="roleName"><option></option>
								<option value="R">read</option>
<option value="CURD">read update delete</option></select>
						</td>
						<td></td>
					</tr>
					<tr>

						<td>
							<input id="deleteAccess" type="button" value="Remove"
								onclick="javascript:deleteTheAccess('protocol', 2);"
								style="display: none;">
						</td>
						<td align="right" colspan="3">
							<div align="right">
								<input type="hidden" name="protocol.theAccess.roleName" value="" id="hiddenRoleName">
								<input type="hidden" name="protocol.theAccess.groupName" value="" id="hiddenGroupName">
								<input type="button" value="Save"
									onclick="javascript:addAccess('protocol', 2, false);" />
								<input type="button" value="Cancel"
									onclick="javascript:clearAccess('protocolForm', 'Protocol');closeSubmissionForm('Access');">

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
				id="resetButton"/>
			&nbsp;







							<input type="submit" value="Submit" id="submitButton">



					<input type="hidden" name="dispatch" value="create">
					<input type="hidden" name="page" value="1">


		</td>
	</tr>
</table>
