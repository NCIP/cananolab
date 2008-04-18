
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
				propertity=""
				indexId="ifInd">
		<tr>
			<td class="leftLabel" valign="top">
				<html:select
					property="entity.composingElements[${param.compEleInd}].inherentFunctions[${ifInd}].type"
					size="1" styleId="funcType"
					onchange="javascript:callPrompt('Function Type', 'functionType'); showModality();">
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
					rows="1" cols="30" />
			</td>
			<td class="label" id="modalityText" style="display: none">
				<html:text property="entity.composingElements[${param.compEleInd}].inherentFunctions[${ifInd}].imagingFunction.modality" />
			</td>
			<td class="rightLabel">
				<a href="#"><span class="addLink2">remove</span> </a>
			</td>
		</tr>
		</logic:iterate>
	</tbody>
</table>