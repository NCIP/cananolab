<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table width="100%" border="0" align="center" cellpadding="3"
	cellspacing="0" class="topBorderOnly" summary="">
	<tr>
		<td class="leftLabelWithTop">
			<strong>Technique</strong>
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
		<td class="completeLabel" colspan="2" valign="top">
			<table class="smalltable" border="0" width="100%">
				<tr class="smallTableHeader">
					<th>
						Instrument Type
					</th>
					<th>
						Manufacturer
					</th>
					<th colspan="2">
						Model Name
					</th>
				</tr>
				<c:set var="instrumentCount" value="0" />
				<c:choose>
					<c:when
						test="${!empty characterizationForm.map.achar.theExperimentConfig.instruments}">
						<tr>
							<logic:iterate name="characterizationForm"
								property="achar.theExperimentConfig.instruments" id="instrument"
								indexId="instrumentInd">
								<tr>
									<td>
										<html:select
											property="achar.theExperimentConfig.instruments[${instrumentInd}].type"
											styleId="instrumentType${instrumentInd}"
											onchange="javascript:callPrompt('Instrment Type', 'instrumentType${instrumentInd}');">
											<option value=""></option>
											<option value="other">
												[Other]
											</option>
										</html:select>										
									</td>
									<td>
										<html:select
											property="achar.theExperimentConfig.instruments[${instrumentInd}].manufacturer"
											styleId="instrumentManufacturer${instrumentInd}"
											onchange="javascript:callPrompt('Manufacturer', 'instrumentManufacturer${instrumentInd}');">
											<option value=""></option>
											<html:options name="allManufacturers"/>
											<option value="other">
												[Other]
											</option>
										</html:select>										
									</td>
									<td>
										<html:text
											property="achar.theExperimentConfig.instruments[${instrumentInd}].modelName"
											size="17" styleId="instrumentModelName${instrumentInd}" />
									</td>
									<td>
										<a style="" id="removeInstrument" href="#"> <span
											class="addLink2">remove</span> </a> &nbsp;
									</td>
								</tr>
								<c:set var="instrumentCount" value="${instrumentInd}" />
							</logic:iterate>
						</tr>
					</c:when>
				</c:choose>
				<tr>
					<td class="completeLabel" colspan="4">
						<a href="#"
							onclick="javascript:addComponent(document.forms[0], 'submitExperimentConfig', 'addInstrument'); return false;">
							<span class="addLink2">Add</span>
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td class="leftLabel" valign="top">
			<strong>Description</strong>
		</td>
		<td class="rightLabel">
			<html:textarea styleId="configDescription"
				property="achar.theExperimentConfig.domain.description" rows="3"
				cols="80" />
		</td>
	</tr>
</table>
<br>
<table width="100%" border="0" align="center" cellpadding="3"
	cellspacing="0" class="topBorderOnly" summary="">
	<tr>
		<td>
			<span class="formMessage"> </span>
			<br>
			<table border="0" align="right" cellpadding="4" cellspacing="0">
				<tr>
					<td>
						<div align="right">
							<input type="reset" value="Reset"
								onclick="javascript:window.location.href='${origUrl}'">
							<input type="button" value="Submit"
								onclick="javascript:submitAction(document.forms[0],
										'${actionName}', 'saveExperimentConfig');">
						</div>
					</td>
				</tr>
			</table>
			<div align="right"></div>
		</td>
	</tr>
</table>

