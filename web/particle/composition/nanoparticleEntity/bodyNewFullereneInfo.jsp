<table class="topBorderOnlyTable" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="6">
				<div align="justify">
					Fullerene Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Average Diameter</strong>
			</td>
			<td class="label">
				<input type="text" name="entity.fullerene.averageDiameter"
					id="averageDiameter" onkeydown="return filterFloatNumber(event)"/>
			</td>
			<td class="label">
				<strong>Average Diameter Unit</strong>
			</td>
			<td class="label">
				<select name="entity.fullerene.averageDiameterUnit" id="averageDiameterUnit"
					onchange="javascript:callPrompt('Average Diameter Unit', 'averageDiameterUnit');">
					<option value=""></option>
					<option value="other">
						[Other]
					</option>
				</select>
			</td>
			<td class="label">
				<strong>Number of Carbons</strong>
			</td>
			<td class="rightLabel">
				<input type="text" name="entity.fullerene.numberOfCarbon"
					id="numberOfCarbon" onkeydown="return filterInteger(event)"/>
			</td>
		</tr>
	</tbody>
</table>
<br>