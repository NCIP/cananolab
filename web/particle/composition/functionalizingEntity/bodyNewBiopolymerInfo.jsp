<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="3">
				<div align="justify">
					Biopolymer Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Biopolymer Type*</strong>
			</td>
			<td class="rightLabel" colspan="2">
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
			<td class="rightLabel" colspan="2">
				<textarea name="entity.biopolymer.sequence" cols="80" rows="3"></textarea>
			</td>
		</tr>
	</tbody>
</table>
<br>