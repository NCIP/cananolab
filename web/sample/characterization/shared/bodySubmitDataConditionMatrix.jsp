<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<c:set var="matrixStyle" value="display:none" />
<c:if test="${param.dispatch eq 'drawMatrix'}">
	<c:set var="matrixStyle" value="display:block;" />
</c:if>
<table class="promptbox" width="85%" align="center">
	<tr>
		<td class="cellLabel">
			Number of Columns
		</td>
		<td>
			<html:text property="achar.theFinding.numberOfColumns" size="2"
				styleId="colNum" onkeydown="return filterInteger(event)"
				onkeyup="updateMatrix(characterizationForm)" />
		</td>
		<td class="cellLabel">
			Number of Rows
		</td>
		<td>
			<html:text property="achar.theFinding.numberOfRows" size="2"
				styleId="rowNum" onkeydown="return filterInteger(event)"
				onkeyup="updateMatrix(characterizationForm)" />
		</td>
	</tr>
</table>

<table class="promptbox" width="85%" align="center" id="matrix"
	border="1" style="">
	<tr>
		<logic:iterate id="col" name="characterizationForm"
			property="achar.theFinding.columns" indexId="cInd">
			<td class="cellLabel" id="column${cInd}">
				<div style="position: relative">
					<a href="javascript:show('columnDesign${cInd}');">Column
						${cInd+1}</a>
					<div id="newColumn">
						<table id="columnDesign${cInd}" style="display: none"
							class="promptbox">
							<tr>
								<td class="cellLabel">
									Column Type*
								</td>
								<td>
									<input type="hidden" id="columnId">
									<select id="datumOrCondition"
										onchange="setNameOptionsByCharName('${characterizationForm.map.achar.characterizationName}');">
										<option value="">
										</option>
										<option value="Datum">
											Datum
										</option>
										<option value="Condition">
											Condition
										</option>
									</select>
								</td>
							</tr>
							<tr>
								<td class="cellLabel">
									Column Name*
								</td>
								<td>
									<div id="columnNamePrompt" style="position: relative">
										<select id="name"
											onchange="javascript:callPrompt('Name', 'name', 'columnNamePrompt');setConditionPropertyOptionsByCharName(null);setColumnValueUnit();">
											<option value=""></option>
											<option value="[Other]">
												[Other]
											</option>
										</select>
									</div>
								</td>
							</tr>
							<tr id="conditionProperty" style="display: none;">
								<td class="cellLabel">
									Condition Property
								</td>
								<td>
									<div id="conditionPropertyPrompt" style="position: relative">
										<select id="property"
											onchange="javascript:callPrompt('Condition Property', 'property', 'conditionPropertyPrompt');setColumnValueUnit();">
										</select>
									</div>
								</td>
							</tr>
							<tr>
								<td class="cellLabel">
									Column Value Type
								</td>
								<td>
									<div id="columnValueType" style="position: relative">
										<select id="valueType"
											onchange="javascript:callPrompt('Column Value Type', 'valueType', 'columnValueType');">
											<option value=""></option>
											<logic:iterate id="type" name="datumConditionValueTypes">
												<option value="${type}">
													${type}
												</option>
											</logic:iterate>
											<option value="other">
												[Other]
											</option>
										</select>
									</div>
								</td>
							</tr>
							<tr>
								<td class="cellLabel">
									Column Value Unit
								</td>
								<td>
									<div id="columnValueUnitPrompt" style="position: relative">
										<select id="valueUnit"
											onchange="javascript:callPrompt('Column Value Unit', 'valueUnit', 'columnValueUnitPrompt');">
										</select>
									</div>
								</td>
							</tr>
							<tr>
								<td class="cellLabel">
									Constant Value?
								</td>
								<td>
									<input type="checkbox">
									<input type="text" id="value">
								</td>
							</tr>
							<tr>
								<td>
									<input class="promptButton" type="button" value="Remove"
										onclick="removeColumn(); hide('columnDesign')" />
								</td>
								<td>
									<div align="right">
										<input class="promptButton" type="button" value="Add"
											onclick="addColumn(); hide('columnDesign${cInd}')" />
										<input class="promptButton" type="button" value="Cancel"
											onclick="hide('columnDesign${cInd}');clearTheColumn();" />
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</td>
		</logic:iterate>
	</tr>
	<tr>
		<td
			colspan="${fn:length(characterizationForm.map.achar.theFinding.columns)}">

		</td>
	</tr>
	<logic:iterate id="row" name="characterizationForm"
		property="achar.theFinding.rows" indexId="rInd">
		<tr>
			<logic:iterate id="cell" name="characterizationForm"
				property="achar.theFinding.rows[${rInd}].cells" indexId="cInd">
				<td>
					<html:text
						property="achar.theFinding.rows[${rInd}].cells[${cInd}].value"
						size="15" />
				</td>
			</logic:iterate>
		</tr>
	</logic:iterate>
	<c:if
		test="${fn:length(characterizationForm.map.achar.theFinding.rows)>0}">
		<tr>
			<td
				colspan="${fn:length(characterizationForm.map.achar.theFinding.columns)-2}"></td>
			<td colspan="2">
				<div align="right">
					<input class="promptButton" type="Button" value="Add"
						onclick="javascript:submitAction(characterizationForm, 'characterization', 'addMatrix');">
					<input class="promptButton" type="Button" value="Cancel" onclick="">
				</div>
			</td>
		</tr>
	</c:if>
</table>