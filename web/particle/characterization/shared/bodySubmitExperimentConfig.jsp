<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table border="0" align="center" cellpadding="3" cellspacing="0"
	class="topBorderOnly" summary="">
	<tr>
		<td class="leftLabelWithTop">
			<strong>Technique*</strong>
		</td>
		<td class="rightLabelWithTop">
			<html:select
				property="achar.theExperimentConfig.domain.technique.type"
				styleId="techniqueType"
				onchange="javascript:callPrompt('Technique Type', 'techniqueType');retrieveTechniqueAbbreviation();">
				<option value=""></option>
				<html:options collection="allTechniques" labelProperty="type"
					property="type" />
				<option value="other">
					[Other]
				</option>
			</html:select>
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Abbreviation</strong>
		</td>
		<td class="rightLabel">
			<html:text styleId="techniqueAbbr"
				property="achar.theExperimentConfig.domain.technique.abbreviation"
				size="30" />
		</td>
	</tr>
	<tr>
		<td class="leftLabel" valign="top">
			<strong>Instrument</strong>
		</td>
		<td class="rightLabel" valign="top">
			<br>
			<table>
				<tr>
					<td valign="top" width="80">
						<div id="patternAddRow" style="display: block;">
							<table border="0" width="100%" class="smalltableNoBorder">
							  <tbody>
								<tr class="smallTableHeader">
									<td class="greyFont">
										<strong>Manufacturer</strong>
									</td>
									<td>
										<html:hidden property="achar.theInstrument.id" styleId="id" />
										<html:select property="achar.theInstrument.manufacturer"
											styleId="manufacturer"
											onchange="javascript:callPrompt('Manufacturer', 'manufacturer');">
											<option value=""></option>
											<html:options name="allManufacturers" />
											<option value="other">
												[Other]
											</option>
										</html:select>
									</td>
								</tr>
								<tr class="smallTableHeader">
									<td class="greyFont">
										<strong>Model Name</strong>
									</td>
									<td>
										<html:text property="achar.theInstrument.modelName" size="17"
											styleId="modelName" />
									</td>
								</tr>
								<tr class="smallTableHeader">
									<td class="greyFont">
										<strong>Type</strong>
									</td>
									<td>
										<html:select property="achar.theInstrument.type"
											styleId="type"
											onchange="javascript:callPrompt('Instrment Type', 'type');">
											<option value=""></option>
											<option value="other">
												[Other]
											</option>
										</html:select>
									</td>
								</tr>
								<tr>									
									<td colspan="2" style="text-align:right">
									    <input class="noBorderButton" type="button" value="New"
											onclick="clearInstrument()" />
										&nbsp;
										<input class="noBorderButton" type="button" value="Save"
											onclick="addInstrument()" />
										&nbsp;
										<input class="noBorderButton" id="delete" type="button"
												value="Delete" onclick="deleteClicked()" />
									</td>
								</tr>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<div id="instrumentTableDiv" style="display: block;">
							<table id="instrumentTable" class="smalltable" border="0"
								width="80%">
								<tbody id="instrumentRows">
									<tr id="patternHeader" class="smallTableHeader">
										<th class="greyFont">
											Manufacturer
										</th>
										<th class="greyFont">
											Model Name
										</th>
										<th class="greyFont">
											Type
										</th>
										<th class="greyFont" colspan="1">
											&nbsp;
										</th>
									</tr>
									<tr id="pattern" style="display: none;">
										<td valign="top">
											<span id="instrumentManufacturer" class="greyFont2">Manufacturer</span>
										</td>
										<td valign="top">
											<span id="instrumentModelName" class="greyFont2">ModelName</span>
										</td>
										<td valign="top">
											<span id="instrumentType" class="greyFont2">Type</span>
										</td>
										<td valign="top">
											<input class="noBorderButton" id="edit" type="button"
												value="Select" onclick="editClicked(this.id)" />
										</td>										
									</tr>
								</tbody>
							</table>
						</div>
						&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td class="leftLabelNoBottom" valign="top">
			<strong>Description</strong>
		</td>
		<td class="rightLabelNoBottom">
			<html:textarea styleId="configDescription"
				property="achar.theExperimentConfig.domain.description" rows="3"
				cols="70" />
			<br>
		</td>
	</tr>
	<tr>
		<td class="leftLabel" valign="top">
			<input type="button" value="Delete"
				onclick="javascript:submitAction(document.forms[0],
										'${actionName}', 'deleteExperimentConfig');">
		</td>
		<td class="rightLabel" align="right">
			<div align="right">
				<input type="reset" value="Cancel"
					onclick="javascript:resetTheExperimentConfig(false);">
				<input type="reset" value="Reset"
					onclick="javascript:window.location.href='${origUrl}'">
				<input type="button" value="Save"
					onclick="javascript:validateSaveConfig('${actionName}');">
			</div>
		</td>
	</tr>
	<html:hidden styleId="configId"
		property="achar.theExperimentConfig.domain.id" />

</table>