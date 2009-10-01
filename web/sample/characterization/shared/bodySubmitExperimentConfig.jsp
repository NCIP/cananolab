<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th colspan="2">
			Technique and Instrument Info
		</th>
	</tr>
	<tr>
		<td class="cellLabel">
			Technique*
		</td>
		<td>
			<div id="techniqueTypePrompt">
				<html:select
					property="achar.theExperimentConfig.domain.technique.type"
					styleId="techniqueType"
					onchange="javascript:callPrompt('Technique Type', 'techniqueType', 'techniqueTypePrompt');retrieveTechniqueAbbreviation();">
					<option value=""></option>
					<html:options name="techniqueTypes" />
					<option value="other">
						[other]
					</option>
				</html:select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Abbreviation
		</td>
		<td>
			<html:text styleId="techniqueAbbr"
				property="achar.theExperimentConfig.domain.technique.abbreviation"
				size="30" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Instrument
		</td>
		<td>
			<div id="instrumentSection" style="position: relative;">
				<a style="display:block" id="addInstrument"
					href="javascript:clearInstrument();openSubmissionForm('Instrument');">Add</a>
				<br>
				<table id="instrumentTable" class="summaryViewLayer4" width="85%"
					style="display: none;">
					<tbody id="instrumentRows">
						<tr id="patternHeader">
							<td width="25%" class="cellLabel">
								Manufacturer
							</td>
							<td width="25%" class="cellLabel">
								Model Name
							</td>
							<td class="cellLabel">
								Type
							</td>
							<td>
							</td>
						</tr>
						<tr id="pattern" style="display: none;">
							<td>
								<span id="instrumentManufacturer">Manufacturer</span>
							</td>
							<td>
								<span id="instrumentModelName">ModelName</span>
							</td>
							<td>
								<span id="instrumentType">Type</span>
							</td>
							<td>
								<input class="noBorderButton" id="edit" type="button"
									value="Edit"
									onclick="editInstrument(this.id);openSubmissionForm('Instrument');" />
							</td>
						</tr>
					</tbody>
				</table>
				<table id="newInstrument" style="display: none;" class="promptbox">
						<tbody>
							<tr>
								<td class="cellLabel">
									Manufacturer
								</td>
								<td>
									<div id="manufacturerPrompt">
										<html:hidden property="achar.theInstrument.id" styleId="id" />
										<html:select property="achar.theInstrument.manufacturer"
											styleId="manufacturer"
											onchange="javascript:callPrompt('Manufacturer', 'manufacturer', 'manufacturerPrompt');">
											<option value=""></option>
											<html:options name="manufacturers" />
											<option value="other">
												[other]
											</option>
										</html:select>
									</div>
								</td>
							</tr>
							<tr>
								<td class="cellLabel">
									Model Name
								</td>
								<td>
									<html:text property="achar.theInstrument.modelName" size="30"
										styleId="modelName" />
								</td>
							</tr>
							<tr>
								<td class="cellLabel">
									Type
								</td>
								<td>
									<div id="instrumentTypePrompt">
										<html:select property="achar.theInstrument.type"
											styleId="type"
											onchange="javascript:callPrompt('Instrment Type', 'type', 'instrumentTypePrompt');">
											<option value=""></option>
											<option value="other">
												[other]
											</option>
										</html:select>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<input style="display: none;" class="promptButton" id="deleteInstrument"
										type="button" value="Remove" onclick="deleteTheInstrument()" />
								</td>
								<td>
									<div align="right">
										<input class="promptButton" type="button" value="Add"
											onclick="addInstrument();" />
										<input class="promptButton" type="button" value="Cancel"
											onclick="clearInstrument();" />
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Description
		</td>
		<td>
			<html:textarea styleId="configDescription"
				property="achar.theExperimentConfig.domain.description" rows="5"
				cols="70" />
		</td>
	</tr>
	<tr>
		<td>
			<c:if test="${!empty user && user.curator && user.admin}">
				<input style="display: none;" id="deleteExperimentConfig"
					type="button" value="Remove"
					onclick="javascript:deleteTheExperimentConfig()">
			</c:if>
		</td>
		<td align="right">
			<div align="right">
				<input type="button" value="Save"
					onclick="javascript:validateSaveConfig('characterization');">
				<input type="reset" value="Cancel"
					onclick="javascript:clearExperimentConfig();closeSubmissionForm('ExperimentConfig')">
			</div>
		</td>
	</tr>
	<html:hidden styleId="configId"
		property="achar.theExperimentConfig.domain.id" />
</table>
