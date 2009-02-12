<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table border="0" align="center" cellpadding="3" cellspacing="0"
	class="topBorderOnly"  style="display: block" id="designDataTable" width="90%">
	<tr>
		<td valign="top" colspan="2" class="subformTitle">
			Design Data Table
		</td>
	</tr>
	<tr>
		<td class="leftLabelWithTopNoBottom">
			<strong>Column Type</strong>
		</td>
		<td class="rightLabelWithTopNoBottom">
			<select id="datumOrCondition">
				<option value="Datum">
					Datum
				</option>
				<option value="Condition">
					Condition
					</ption>
			</select>
		</td>
	</tr>

	<tr>
		<td class="leftLabelWithTopNoBottom">
			<strong>Column Name*</strong>
		</td>

		<td class="rightLabelWithTopNoBottom">
			<html:select
				property="achar.theExperimentConfig.domain.technique.type"
				styleId="name" onchange="javascript:callPrompt('Name', 'name');">
				<option value=""></option>				
				<option value="PDI">
					PDI
				</option>
				<option value="z-avg">
					z-avg
				</option>
				<option value="temperature">
					temperature
				</option>
				<option value="z-avg">
					z-avg
				</option>
				<option value="other">
					[Other]
				</option>
			</html:select>
		</td>
	</tr>


	<tr>
		<td class="leftLabelNoBottom">
			<strong>Column Value Type</strong>
		</td>
		<td class="rightLabelNoBottom" align="left">
			<html:text styleId="valueType"
				property="achar.theExperimentConfig.domain.technique.type" />
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom">
			<strong>Column Value Unit</strong>
		</td>
		<td class="rightLabelNoBottom">
			<html:text styleId="valueUnit"
				property="achar.theExperimentConfig.domain.technique.type" />
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom" colspan="1">
			<strong>Is column value constant?</strong>
			<input type="checkbox">
		</td>
		<td class="rightLabelNoBottom">
			<html:text styleId="value"
				property="achar.theExperimentConfig.domain.technique.type" />
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom" colspan="1">
			&nbsp;
		</td>
		<td class="rightLabelNoBottomRightAlign">
			<!--<input class="noBorderButton" type="button" value="Save Column"
				onclick="addDatumColumn()" />
			<input class="noBorderButton" type="button" value="Delete Column"
				onclick="deleteDatumColumn()" />
		-->
		
			<input class="noBorderButton" type="button" value="Save"
				onclick="addDatumColumn(); show('populateDataTableButton');" />
			<input class="noBorderButton" type="button" value="Delete"
				onclick="deleteDatumColumn()" />
		</td>
	</tr>

	<tr id="datumColumnsDivRowDisplay">
		<td class="leftLabelNoBottom" valign="top" colspan="2">
			<div id="datumColumnsDivDisplay" style="display: block;">
				<table id="datumColumnsTableDisplay" class="smalltable" border="1">
					<tbody id="datumColumnsDisplay">
						<tr id="datumColumnPatternRowDisplay">
							<td id="datumColumnPatternDisplay" style="display: none;">
								<span id="datumColumnNameDisplay" class="greyFont2">datumColumnName</span>
								<span id="datumColumnValueTypeDisplay" class="greyFont2">ValueType</span>
								<span id="datumColumnValueUnitDisplay" class="greyFont2">ValueUnit</span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			&nbsp;
		</td>
	</tr>
	
	<tr id="populateDataTableButton" style="display: none;">
		<td class="completeLabelNoBottom" colspan="2">
			<input class="noBorderButton" type="button"
				value="Populate Data Table"
				onclick="show('populateDataTable')" />
		</td>
	</tr>
</table>

<table border="0" align="center" class="smalltable3"
	id="populateDataTable" style="display: none;" width="90%">

	<tr>
		<td valign="top" colspan="2" class="subformTitle">
			Populate Data Table
		</td>
	</tr>

	<tr id="datumColumnsDivRow">
		<td class="leftLabelNoBottom" valign="top" colspan="2">
			<div id="datumColumnsDiv" style="display: block;">
				<table id="datumColumnsTable" class="smalltable" border="1">
					<tbody id="datumColumns">
						<tr id="datumColumnPatternRow">
							<td id="datumColumnPattern" style="display: none;">
								<input id="datumColumnId" type="hidden" value="datumColumnId" />
								<input id="datumColumnDataRowId" type="hidden"
									value="datumColumnDataRowId" />
								<input id="datumColumnDataSetId" type="hidden"
									value="datumColumnDataSetId" />
								<!--
								<input class="noBorderButton" id="datumColumnName" type="button"
									size="2" value="datumColumnName" onclick="editColumn(this.id)" />
								
								-->
								<span id="datumColumnName" class="greyFont2">datumColumnName</span>
								<span id="datumColumnValueType" class="greyFont2">ValueType</span>
								<span id="datumColumnValueUnit" class="greyFont2">ValueUnit</span>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<input id="datumColumnValue" type="text" size="2"
									value="datumColumnValue" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			&nbsp;
		</td>
	</tr>
	<tr id="datumColumnsDivRow2" style="display: block">
		<td class="rightLabelNoBottom" valign="top" align="right" colspan="2">
			<div id="addRowButtons" style="display: none;">
				<!--<input class="noBorderButton" type="button" value="New"
					onclick="clearTheDataRow()" />
				<input class="noBorderButton" type="button" value="Save Row"
					onclick="addRow()" />
				<input class="noBorderButton" type="button" value="Delete"
					onclick="deleteClicked()" />
			-->

				<input class="noBorderButton" type="button" value="Save"
					onclick="addRow()" />
				<input class="noBorderButton" type="button" value="Delete"
					onclick="deleteClicked()" />
			</div>
			&nbsp;
		</td>
	</tr>

	<tr id="datumMatrixDivRow">
		<td class="completeLabelNoTopBottom" valign="top" colspan="2">
			<div id="datumMatrixDiv" style="display: block;">
				<table id="datumMatrixTable" class="smalltable" border="1">
					<tbody id="datumMatrix">
						<tr id="matrixHeader" class="greyFont2" style="display: none;">
						</tr>
						<tr id="datumMatrixPatternRow" class="greyFont2"
							style="display: none;">
						</tr>
					</tbody>
				</table>
			</div>
			&nbsp;
		</td>
	</tr>
	
	<tr>
		<td class="leftLabel" valign="top">
			<input type="button" value="Delete"
				onclick="javascript:submitAction(document.forms[0],
										'${actionName}', 'deleteExperimentConfig');">
		</td>
		<td class="rightLabel" align="right" colspan="1">
			<div align="right">
				<input type="reset" value="Cancel"
					onclick="javascript:resetTheDataSet(false);">
				<input type="button" value="Save"
					onclick="javascript:saveDataSet('${actionName}');">
			</div>
		</td>
	</tr>

</table>

<html:hidden styleId="dataSetId" property="achar.theDataSet.domain.id" />