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
				<select name="entity.antibody.species" id="species"
					onchange="javascript:callPrompt('Species', 'species');">
					<option value="" />
					<html:options name="antibodySpecies" />
					<option value="other">
						[Other]
					</option>
				</select>
			</td>
			<td class="label">
				<strong>Isotype</strong>
			</td>
			<td class="label">
				<select name="entity.antibody.isotype" id="antibodyIsotype"
					onchange="javascript:callPrompt('Isotype', 'antibodyIsotype');">
					<option value="" />
					<html:options name="antibodyIsotypes" />
					<option value="other">
						[Other]
					</option>
				</select>
			</td>
			<td class="label">
				<strong>Type</strong>
			</td>
			<td class="rightLabel">
				<select name="entity.antibody.type" id="antibodyType"
					onchange="javascript:callPrompt('Type', 'antibodyType');">
					<option value="" />
					<html:options name="antibodyTypes" />
					<option value="other">
						[Other]
					</option>
				</select>
			</td>
		</tr>
	</tbody>
</table>