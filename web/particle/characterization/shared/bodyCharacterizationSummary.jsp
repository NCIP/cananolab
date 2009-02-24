<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/calendar2.js"></script>
<script type="text/javascript"
	src="javascript/CharacterizationManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/CharacterizationManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<table width="100%" border="0" align="center" cellpadding="3"
	cellspacing="0" class="topBorderOnly" summary="">
	<tr>
	<tr class="topBorder">
		<td class="formTitle" colspan="4">
			<div align="justify">
				Summary
			</div>
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Characterization Type * </strong>
		</td>
		<td class="label">
			<html:select property="achar.characterizationType" styleId="charType"
				onchange="javascript:callPrompt('Characterization Type', 'charType');setCharacterizationOptionsByCharType('charName')">
				<option value=""></option>
				<html:options name="characterizationTypes" />
				<option value="other">
					[Other]
				</option>
			</html:select>
		</td>
		<td class="label">
			<strong>Characterization*</strong>
		</td>
		<td class="rightLabel">
			<html:select property="achar.characterizationName" styleId="charName"
				onchange="javascript:callPrompt('Characterization', 'charName');setAssayEndpointOptionsByCharName()">
				<option value=""></option>
				<option value="other">
					[Other]
				</option>
			</html:select>
		</td>
	</tr>
	<tr>
		<td class="leftLabel" valign="top">
			<strong>Assay Endpoint</strong>
		</td>
		<td class="label">
			<html:select property="achar.domainChar.assayType"
				styleId="assayType"
				onchange="javascript:callPrompt('Assay Endpoint', 'assayType');">
				<option value=""></option>
				<option value="other">
					[Other]
				</option>
			</html:select>
		</td>
		<td class="label">
			<strong>Protocol Name - Version</strong>
		</td>
		<html:hidden styleId="updatedUri"
			property="achar.protocolFileBean.domainFile.uri" />
		<td class="rightLabel" colspan="3">
			<c:choose>
				<c:when test="${!empty characterizationProtocolFiles}">
					<html:select styleId="protocolFileId"
						property="achar.protocolFileBean.domainFile.id"
						onchange="retrieveProtocolFile()">
						<option />
							<html:options collection="characterizationProtocolFiles"
								property="domainFile.id" labelProperty="displayName" />
					</html:select> &nbsp;<span id="protocolFileLink"><a
						href="searchProtocol.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.protocolFileBean.domainFile.id}&amp;location=${location}">${characterizationForm.map.achar.protocolFileBean.domainFile.uri}</a>
					</span>
				</c:when>
				<c:otherwise>
						    No protocols available.
						</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Characterization Source* </strong>
		</td>
		<td class="label">
			<html:select property="achar.pocBean.domain.id" styleId="charSource"
				onchange="javascript:callPrompt('Characterization Point of Contact', 'charSource');">
				<option value=""></option>
				<html:options collection="particlePointOfContacts"
					labelProperty="displayName" property="domain.id" />
			</html:select>
		</td>
		<td class="label">
			<strong>Characterization Date</strong>
		</td>
		<td class="rightLabel">
			<html:text property="achar.dateString" size="10" styleId="charDate" />
			<a href="javascript:cal1.popup();"><img
					src="images/calendar-icon.gif" width="22" height="18" border="0"
					alt="Click Here to Pick up the date"
					title="Click Here to Pick up the date" align="middle"> </a>
		</td>
	</tr>

</table>
<br>
<script language="JavaScript">
	<!-- //
		var cal1 = new calendar2(document.getElementById('charDate'));
	    cal1.year_scroll = true;
		cal1.time_comp = false;
		cal1.context = '${pageContext.request.contextPath}';
  	//-->
</script>
