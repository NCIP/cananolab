<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="6">
				<div align="justify">
					Carbon Nanotube Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Average Length</strong>
			</td>
			<td class="label">
				<input type="text" name="entity.carbonNanotube.averageLength"
					value="${nanomaterialEntityForm.map.entity.carbonNanotube.averageLength}"
					onkeydown="return filterFloatNumber(event)" />
			</td>
			<td class="label">
				<strong>Average Length Unit</strong>
			</td>
			<td class="label">
				<select name="entity.carbonNanotube.averageLengthUnit" id="averageLengthUnit"
					onchange="javascript:callPrompt('Average Length Unit', 'averageLengthUnit');">
					<option value=""></option>
					<option value="other">
						[Other]
					</option>
				</select>
			</td>
			<td class="label">
				<strong>Chirality</strong>
			</td>
			<td class="rightLabel">
				<input type="text" name="entity.carbonNanotube.chirality" value="${nanomaterialEntityForm.map.entity.carbonNanotube.chirality}"/>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Diameter</strong>
			</td>
			<td class="label">
				<input type="text" name="entity.carbonNanotube.diameter"
					value="${nanomaterialEntityForm.map.entity.carbonNanotube.diameter}"
					onkeydown="return filterFloatNumber(event)" />
			</td>
			<td class="label">
				<strong>Diameter Unit</strong>
			</td>
			<td class="label">
				<select name="entity.carbonNanotube.diameterUnit" id="diameterUnit"
					onchange="javascript:callPrompt('Diameter Unit', 'diameterUnit');">
					<option value=""></option>
					<option value="other">
						[Other]
					</option>
				</select>
			</td>
			<td class="label">
				<strong>Wall Type</strong>
			</td>
			<td class="rightLabel">
				<select name="entity.carbonNanotube.wallType" id="wallType">
					<option value=""></option>
				</select>
			</td>
		</tr>
	</tbody>
</table>
<br>