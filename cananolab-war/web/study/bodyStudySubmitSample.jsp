<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<html:form action="/sample" onsubmit="return validateSavingTheData('newPointOfContact', 'point of contact');">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<table width="100%" align="center" class="submissionView">		
		<tr>
			<td class="cellLabel" width="20%">
				Sample Name *
			</td>
			<td>
				<input type="text" size="80" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Point of Contact
			</td>
			<td>
				<a href="#"
					onclick="javascript:openSubmissionForm('PointOfContact');"
					id="addPointOfContact" style="display:block"><img align="top"
						src="images/btn_add.gif" border="0" /></a>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<div style="display:none" id="newPointOfContact">
					<a name="submitPointOfContact"><jsp:include
							page="bodyStudySubmitPointOfContact.jsp"/></a>
				</div>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Keywords
				<i>(one keyword per line)</i>
			</td>
			<td>
				<textarea rows="3" cols="97"></textarea>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Visibility
			</td>
			<td>
				<select name="sampleBean.visibilityGroups" multiple="multiple"
					size="6" id="sampleVisibilityGroups">
					<option value="Public">
						Public
					</option>
					<option value="BROWN_STANFORD">
						BROWN_STANFORD
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
					<option value="CTRAIN">
						CTRAIN
					</option>
				</select>
				<br>
				<i>(${applicationOwner}_Researcher,
					${applicationOwner}_DataCurator, and the organization name for the
					primary point of contact are always selected by default.)</i>
			</td>
		</tr>
	</table>
</html:form>

