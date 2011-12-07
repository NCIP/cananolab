<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel">
		<input type="radio" name="type" value="false" onclick="javascript:hide('selectStudyProtocol');show('newStudyProtocol');show('submitButtons');">
		Create a new protocol
		<br>
		&nbsp;&nbsp;or
		<br>
		<input type="radio" name="type" value="true" onclick="javascript:hide('newStudyProtocol');show('selectStudyProtocol');show('submitButtons');">
		Select an existing protocol<br/><br/>
		</td>
	</tr>
	<tr>
		<td>
			<div style="display:none" id="newStudyProtocol">
				<jsp:include page="bodyStudySubmitProtocol.jsp"/>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div style="display:none" id="selectStudyProtocol">
			<table width="100%" align="left" class="submissionView">
				<tr>
					<td class="cellLabel" width="20%">
						protocol
					</td>
					<td>
						<select name="otherSamples" multiple="multiple" size="4"
							id="allSampleNameSelect" style="display: block;">
							<option value="BROWN_STANFORD-HLeeJNM2008-01">
								ITA-1
							</option>
							<option value="BROWN_STANFORD-HLeeJNM2008-02">
								ITA-2
							</option>
							<option value="BROWN_STANFORD-HLeeJNM2008-03">
								ITA-3
							</option>
							<option value="BROWN_STANFORD-HLeeJNM2008-04">
								ITA-4
							</option>
							<option value="BROWN_STANFORD-HLeeJNM2008-05">
								ITA-5
							</option>
						</select>
					</td>
				</tr>
			</table>
			</div>
		</td>
	</tr>
</table>