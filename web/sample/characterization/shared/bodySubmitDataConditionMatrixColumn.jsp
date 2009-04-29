<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table id="columnDesign" class="promptbox">
	<tr>
		<td class="cellLabel">
			Column Type*
		</td>
		<td>
			<select
				name="achar.theFinding.columnHeaders[${param.cInd}].columnType"
				id="columnType"
				onchange="javascript:setNameOptionsByCharName('${characterizationForm.map.achar.characterizationName}');">
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
			<div id="columnNamePrompt">
				<select name="achar.theFinding.columnHeaders[${param.cInd}].columnName"
					id="columnName"
					onchange="javascript:callPrompt('Column Name', 'columnName', 'columnNamePrompt');setConditionPropertyOptionsByCharName(null);setColumnValueUnit();">
					<option value=""></option>
					<option value="[Other]">
						[Other]
					</option>
				</select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<div id="conditionPropertyLabel" style="display: none">
				Condition Property
			</div>
		</td>
		<td>
			<div id="conditionPropertyPrompt"
				style="display: none;">
				<select
					name="achar.theFinding.columnHeaders[${param.cInd}].conditionProperty"
					id="conditionProperty"
					onchange="javascript:callPrompt('Condition Property', 'conditionProperty', 'conditionPropertyPrompt');setColumnValueUnit();">
				</select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Column Value Type
		</td>
		<td>
			<div id="columnValueTypePrompt">
				<select
					name="achar.theFinding.columnHeaders[${param.cInd}].valueType"
					id="valueType"
					onchange="javascript:callPrompt('Column Value Type', 'valueType', 'columnValueTypePrompt');">
					<option value=""></option>
					<logic:iterate id="vtype" name="datumConditionValueTypes">
						<option value="${vtype}">
							${vtype}
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
			<div id="columnValueUnitPrompt">
				<select
					name="achar.theFinding.columnHeaders[${param.cInd}].valueUnit"
					id="valueUnit"
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
			<input type="text"
				name="achar.theFinding.columnHeaders[${param.cInd}].constantValue"
				id="constantValue" />
		</td>
	</tr>
	<tr>
		<td>
			<input class="promptButton" type="button" value="Remove"
				onclick="reduceMatrix(characterizationForm, 'Column', ${param.cInd});" />
		</td>
		<td>
			<div align="right">
				<input class="promptButton" type="button" value="Add"
					onclick="addColumnHeader(${param.cInd});" />
				<input class="promptButton" type="button" value="Cancel"
					onclick="cancelColumn(${param.cInd})" />
			</div>
		</td>
	</tr>
</table>