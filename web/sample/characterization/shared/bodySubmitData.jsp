<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table class="summaryViewLayer4" width="95%" align="center">
	<tr>
		<td class="cellLabel" width="30%">
			Datum or Condition*
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
			Name*
		</td>
		<td>
			<div id="namePrompt">
				<select id="name"
					onchange="javascript:callPrompt('Name', 'name', 'namePrompt');setConditionPropertyOptionsByCharName(null);setColumnValueUnit();">
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
			<div id="propertyPrompt">
				<select id="property"
					onchange="javascript:callPrompt('Condition Property', 'property', 'propertyPrompt');setColumnValueUnit();">
				</select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Value Type
		</td>
		<td>
			<div id="valueTypePrompt">
				<select id="valueType"
					onchange="javascript:callPrompt('Value Type', 'valueType', 'valueTypePrompt');">
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
			Value Unit
		</td>
		<td>
			<div id="valueUnitPrompt">
				<select id="valueUnit"
					onchange="javascript:callPrompt('Value Unit', 'valueUnit', 'valueUnitPrompt');">
				</select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Value
		</td>
		<td>
			<input type="checkbox">
			<input type="text" id="value">
		</td>
	</tr>
	<tr>
		<td></td>
		<td style="align: right;">
			<input class="noBorderButton" type="button" value="Delete"
				onclick="deleteColumn(); hide('columnDesign')" />
			<input class="noBorderButton" type="button" value="Cancel"
				onclick="hide('columnDesign');clearTheColumn();" />
			<input class="noBorderButton" type="button" value="Add"
				onclick="addColumn(); hide('columnDesign')" />
		</td>
	</tr>
</table>
