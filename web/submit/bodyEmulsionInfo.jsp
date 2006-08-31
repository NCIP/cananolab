<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					${particleName}
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Characterization Source </strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:select property="dendrimer.characterizationSource">
					<option name="NCL">
						NCL
					</option>
					<option name="vendor">
						Vendor
					</option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>emulsionType</strong>
			</td>
			<td class="label">
				<html:text property="emulsion.emulsionType" />
			</td>

			<td class="label">
				<strong>Molecular Formula</strong>
			</td>
			<td class="rightLabel">
				<html:text property="emulsion.molecularFormula" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Is Polymerized </strong>
			</td>
			<td class="label">
				<html:select property="emulsion.polymerized">
					<option value="yes">
						Yes
					</option>
					<option value="no">
						No
					</option>
				</html:select>
			</td>
			<td class="label">
				<strong>Polymer Name</strong>
			</td>
			<td class="rightLabel">
				<html:text property="emulsion.polymerName" />
			</td>
		</tr>
	</tbody>
</table>