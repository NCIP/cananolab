<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Composition Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Emulsion Type</strong>
			</td>
			<td class="label">
				${nanoparticleCompositionForm.map.emulsion.emulsionType}&nbsp;
			</td>

			<td class="label">
				<strong>Molecular Formula</strong>
			</td>
			<td class="rightLabel">
				${nanoparticleCompositionForm.map.emulsion.molecularFormula}&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Is Polymerized </strong>
			</td>
			<td class="label">
				${nanoparticleCompositionForm.map.emulsion.polymerized}&nbsp;
			</td>
			<td class="label">
				<strong>Polymer Name</strong>
			</td>
			<td class="rightLabel">
				${nanoparticleCompositionForm.map.emulsion.polymerName}&nbsp;
			</td>
		</tr>
	</tbody>
</table>
