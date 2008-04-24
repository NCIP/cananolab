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
					<option value="other">
						Other
					</option>
				</select>
			</td>
			<td class="label">
				<strong>Isotype</strong>
			</td>
			<td class="label">
				<select name="entity.antibody.isotype" id="isotype"
					onchange="javascript:callPrompt('Isotype', 'isotype');">
					<option value="" />
					<option value="IgA">
						IgA
					</option>
					<option value="IgD">
						IgD
					</option>
					<option value="IgE">
						IgE
					</option>
					<option value="IgG">
						IgG
					</option>
					<option value="IgM">
						IgM
					</option>
					<option value="other">
						Other
					</option>
				</select>
			</td>
			<td class="label">
				<strong>Type</strong>
			</td>
			<td class="rightLabel">
				<select name="entity.antibody.type" id="abType"
					onchange="javascript:callPrompt('Type', 'abType');">
					<option value="" />
					<option value="Fab">
						Fab
					</option>
					<option value="ScFv">
						ScFv
					</option>
					<option value="whole">
						Whole
					</option>
					<option value="other">
						Other
					</option>
				</select>
			</td>
		</tr>
	</tbody>
</table>