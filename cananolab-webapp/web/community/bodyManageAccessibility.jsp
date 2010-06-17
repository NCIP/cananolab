<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<tr>
	<td class="cellLabel" width="15%">
		Access to the Sample
	</td>
	<td>
		<c:set var="newAddAccessButtonStyle" value="display:block" />
		<a href="#" onclick="javascript:show('newAccess');hide('deleteAccess');" id="addAccess"
			style="${newAddAccessButtonStyle}"><img align="top"
				src="images/btn_add.gif" border="0" /> </a>
	</td>
</tr>
<tr>
	<td colspan="2">
		<c:set var="edit" value="true" />
		<table class="editTableWithGrid" width="95%" align="center">
			<tr>
				<th>
					Collaboration Group Name
				</th>
				<th>
					Access
				</th>
				<th></th>
			</tr>
			<tr valign="top">
				<td>
					NCL_DNT_Collaboration
				</td>
				<td>
					READ, UPDATE, DELETE
				</td>
				<td align="right">
					<a href="javascript:show('newAccess');show('deleteAccess');">Edit</a>&nbsp;
				</td>
			</tr>
			<tr valign="top">
				<td>
					NCL_GT_Collaboration
				</td>
				<td>
					READ
				</td>
				<td align="right">
					<a href="javascript:show('newAccess');show('deleteAccess');">Edit</a>&nbsp;
				</td>
			</tr>
		</table>
		<br />
	</td>
</tr>
<tr>
	<td colspan="2">
		<c:set var="newAccesstyle" value="display:none" />
		<div style="${newAccesstyle}" id="newAccess">
			<table class="subSubmissionView" width="85%" align="center">
				<tr>
					<th colspan="4">
						Access Information
					</th>
				</tr>
				<tr>
					<td class="cellLabel" width="15%">
						Collaboration Group Name*
					</td>
					<td>
						<select name="access">
							<option></option>
							<option value="" selected>
								NCL_DNT_Collaboration
							</option>
							<option value="">
								NCL_GT_Collaboration
							</option>
							<option value="">Public</option>
						</select>
					</td>
					<td class="cellLabel" width="15%">
						Access to the Sample
					</td>
					<td>
						<select name="access">
							<option></option>
							<option value="">
								READ
							</option>
							<option value="" selected>
								READ, UPDATE, DELETE
							</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<input class="promptButton"
							id="deleteAccess" type="button" value="Remove">
					</td>
					<td align="right" colspan="3">
						<div align="right">
							<input class="promptButton" type="button" value="Save" />
							<input class="promptButton" type="reset" value="Cancel"
								onclick="javascript:hide('newAccess');">
						</div>
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>
