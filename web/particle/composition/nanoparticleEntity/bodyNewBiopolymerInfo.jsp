<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Biopolymer Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="LeftLabel">
				<strong>Name*</strong>
			</td>
			<td class="label">
				<input type="text" name="entity.biopolymer.name">
			</td>
			<td class="label">
				<strong>Biopolymer Type*</strong>
			</td>
			<td class="rightLabel">
				<select name="entity.biopolymer.type" id="biopolymerType"
					onchange="javascript:callPrompt('Biopolymer Type', 'biopolymerType');">
					<option value=""></option>
					<option value="other">
						[Other]
					</option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Sequence</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<textarea name="entity.biopolymer.sequence" cols="80" rows="3"></textarea>
			</td>
		</tr>
	</tbody>
</table>
<br>