<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="leftLabelWithTop" valign="top">
				<strong>Function Type</strong>
			</td>
			<td class="labelWithTop" valign="top">
				<strong>Description</strong>
			</td>
			<td class="labelWithTop" valign="top" id="modalityTitle" style="display: none">
				<strong>Modality</strong>
			</td>
			<td class="rightLabelWithTop">
				<Strong>&nbsp;</Strong>
			</td>
		</tr>
		<logic:iterate id="ifdata" name="nanoparticleEntityForm"
				property="entity.composingElements[${param.compEleInd}].inherentFunctions"
				indexId="ifInd">
		<tr>
			<td class="leftLabel" valign="top">
				<html:select
					property="entity.composingElements[${param.compEleInd}].inherentFunctions[${ifInd}].type"
					size="1" styleId="funcType"
					onchange="javascript:callPrompt('Function Type', 'functionType');">
					<option value="imaging">
						Imaging
					</option>
					<option value="targeting">
						Targeting
					</option>
					<option value="therapeutic">
						Therapeutic
					</option>
					<option value="other">
						[Other]
					</option>
				</html:select>
			</td>
			<td class="label">
				<html:textarea
					property="entity.composingElements[${param.compEleInd}].inherentFunctions[${ifInd}].description"
					rows="1" cols="30" />&nbsp;
			</td>
			<td class="label" id="modalityText" style="display: none">
				<html:text property="entity.composingElements[${param.compEleInd}].inherentFunctions[${ifInd}].imagingFunction.modality" />&nbsp;
			</td>
			<td class="rightLabel">
				<a href="#" onclick="javascript:removeChildComponent(nanoparticleEntityForm, 
					'nanoparticleEntity',${param.compEleInd}, ${ifInd}, 'removeInherentFunction');"><span class="addLink2">remove</span></a>
			</td>
		</tr>
		</logic:iterate>
	</tbody>
</table>