<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="summaryViewLayer4" align="center" width="95%">
	<tr>
		<th class="cellLabel">
			Component Information
		</th>
		<th>
		</th>
	</tr>
	<tr>
		<td class="cellLabel">
			Component Name*
		</td>
		<td colspan="3">
			<input type="text" value="Ceramide" size="97">
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Dose
		</td>
		<td class="cellLabel">
			<input type="text" value="15" size="20">&nbsp;&nbsp;
			<SELECT >
				<option value="">
					mg/kg
				</option>
				<option value="other">
					[other]
				</option>
			</SELECT>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Dose Volume
		</td>
		<td class="cellLabel">
			<input type="text" value="2" size="20">&nbsp;&nbsp;
			<SELECT >
				<option value="">
					mg/kg
				</option>
				<option value="other">
					[other]
				</option>
			</SELECT>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Description
		</td>
		<td>
			<textarea rows="3" cols="97">14C-4.87 uCi/2mL/kg iv in saline
			</textarea>
		</td>
	</tr>
</table>
