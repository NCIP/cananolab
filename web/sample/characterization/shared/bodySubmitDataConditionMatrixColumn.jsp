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
				id="columnType${param.cInd}"
				onchange="javascript:setNameOptionsByCharName(${param.cInd});">
				<option value="">
				</option>
				<option value="datum">
					datum
				</option>
				<option value="condition">
					condition
				</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Column Name*
		</td>
		<td>
			<div id="columnNamePrompt${param.cInd}">
				<select
					name="achar.theFinding.columnHeaders[${param.cInd}].columnName"
					id="columnName${param.cInd}"
					onchange="javascript:callPrompt('Column Name', 'columnName${param.cInd}', 'columnNamePrompt${param.cInd}');setConditionPropertyOptionsByCharName(null, ${param.cInd});setColumnValueUnit(${param.cInd});">
					<option value=""></option>
					<option value="[other]">
						[other]
					</option>
				</select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<div id="conditionPropertyLabel${param.cInd}" style="display: none">
				Condition Property
			</div>
		</td>
		<td>
			<div id="conditionPropertyPrompt${param.cInd}" style="display: none;">
				<select
					name="achar.theFinding.columnHeaders[${param.cInd}].conditionProperty"
					id="conditionProperty${param.cInd}"
					onchange="javascript:callPrompt('Condition Property', 'conditionProperty${param.cInd}', 'conditionPropertyPrompt${param.cInd}');setColumnValueUnit(${param.cInd});">
				</select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Column Value Type
		</td>
		<td>
			<div id="columnValueTypePrompt${param.cInd}">
				<select
					name="achar.theFinding.columnHeaders[${param.cInd}].valueType"
					id="valueType${param.cInd}"
					onchange="javascript:callPrompt('Column Value Type', 'valueType${param.cInd}', 'columnValueTypePrompt${param.cInd}');">
					<option value=""></option>
					<logic:iterate id="vtype" name="datumConditionValueTypes">
						<option value="${vtype}">
							${vtype}
						</option>
					</logic:iterate>
					<option value="other">
						[other]
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
			<div id="columnValueUnitPrompt${param.cInd}">
				<select
					name="achar.theFinding.columnHeaders[${param.cInd}].valueUnit"
					id="valueUnit${param.cInd}"
					onchange="javascript:callPrompt('Column Value Unit', 'valueUnit${param.cInd}', 'columnValueUnitPrompt${param.cInd}');">
				</select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Constant Value?
		</td>
		<td>
			<input type="text"
				name="achar.theFinding.columnHeaders[${param.cInd}].constantValue"
				id="constantValue${param.cInd}" />
				<%--onkeydown="return filterFloatForColumn(event, 'columnType${param.cInd}');" /--%>
				<br>
				<em>For boolean column value type, please enter 1 for true, 0 for false</em>
		</td>
	</tr>
	<tr>
		<td>
			<input class="promptButton" type="button" value="Remove"
				onclick="reduceMatrix(characterizationForm, 'Column', ${param.cInd});" />
		</td>
		<td>
			<div align="right">
				<input class="promptButton" type="button" value="Save"
					onclick="addColumnHeader(${param.cInd});" />
				<input class="promptButton" type="button" value="Cancel"
					onclick="cancelColumn(${param.cInd})" />
			</div>
		</td>
	</tr>
</table>