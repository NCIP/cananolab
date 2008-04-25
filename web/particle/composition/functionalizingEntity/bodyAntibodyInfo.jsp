<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="6">
				<div align="justify">
					Antibody Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Species</strong>
			</td>
			<td class="label">
				<html:select property="entity.antibody.species" styleId="species"
					onchange="javascript:callPrompt('Species', 'species');">
					<option value="" />
					<html:options name="antibodySpecies" />
					<option value="other">
						[Other]
					</option>
				</html:select>
			</td>
			<td class="label">
				<strong>Isotype</strong>
			</td>
			<td class="label">
				<html:select property="entity.antibody.isotype" styleId="antibodyIsotype"
					onchange="javascript:callPrompt('Isotype', 'antibodyIsotype');">
					<option value="" />
					<html:options name="antibodyIsotypes" />
					<option value="other">
						[Other]
					</option>
				</html:select>
			</td>
			<td class="label">
				<strong>Type</strong>
			</td>
			<td class="rightLabel">
				<html:select property="entity.antibody.type" styleId="antibodyType"
					onchange="javascript:callPrompt('Type', 'antibodyType');">
					<option value="" />
					<html:options name="antibodyTypes" />
					<option value="other">
						[Other]
					</option>
				</html:select>
			</td>
		</tr>
	</tbody>
</table>
<br>