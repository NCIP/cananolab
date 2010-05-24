<div style="display:block" id="newAnimalDiet">
	<a name="submitPointOfContact">
<script type='text/javascript' src='javascript/POCManager.js'></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/POCManager.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th>
			Animal Diet Information
		</th>
	</tr>
	<tr>
		<td>
			<table>
			<tr>
				<td class="cellLabel">
					Is Diet Restricted?
				</td>
				<td colspan="3">
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					Dietary Restriction
				</td>
				<td>
					<textarea name="achar.description" cols="126" rows="5"></textarea>
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					Fee Lot Number
				</td>
				<td>
					<input type="text" size="126">
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					Fee
				</td>
				<td colspan="3">
					<input type="text" size="126">
				</td>
			</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%">
				<tr>
					<td>
						<div align="right">
							<input type="button" value="Save"
								onclick="closeSubmissionForm('AnimalDiet');" />
							<input type="button" value="Cancel"
								onclick="closeSubmissionForm('AnimalDiet');" />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>