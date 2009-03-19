<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table id="designDataTable" border="0" cellpadding="3"
	cellspacing="0" class="smallTable3" width="90%">
	<tr>
		<td valign="top" colspan="2" class="subformTitle">
			Design Data Table
		</td>
	</tr>
	<tr>
		<td class="leftLabelWithTopNoBottom">
			<strong>Column Type*</strong>
			<input type="hidden" id="columnId">
			&nbsp;&nbsp;
			<select id="datumOrCondition" onChange="showDatumConditionInfo(null)">
				<option value="">
				</option>
				<option value="Datum">
					Datum
				</option>
				<option value="Condition">
					Condition
					</ption>
			</select>
		</td>
		<td class="rightLabelWithTopNoBottom">
			<strong>Column Name*</strong> &nbsp;&nbsp;
			<select id="name"
				onchange="javascript:callPrompt('Name', 'name');setConditionPropertyOptionsByCharName(null);setColumnValueUnit();">
				<option value=""></option>
				<option value="[Other]">
					[Other]
				</option>
			</select>
		</td>
	</tr>
	<tr id="conditionProperty" style="display: none;">
		<td class="completeLabelNoBottom" colspan="2">
			<strong>Condition Property</strong> &nbsp;&nbsp;
			<select id="property"
				onchange="javascript:callPrompt('Condition Property', 'property');setColumnValueUnit();">
			</select>
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom">
			<strong>Column Value Type</strong> &nbsp;&nbsp;
			<select id="valueType"
				onchange="javascript:callPrompt('Column Value Type', 'valueType');">
				<option value=""></option>
				<logic:iterate id="dataSetColumnValueType"
					name="dataSetColumnValueTypes">
					<option value="${dataSetColumnValueType}">
						${dataSetColumnValueType}
					</option>
				</logic:iterate>
				<option value="other">
					[Other]
				</option>
			</select>
		</td>
		<td class="labelNoBottom">
			<strong>Column Value Unit</strong> &nbsp;&nbsp;
			<select id="valueUnit"
				onchange="javascript:callPrompt('Column Value Unit', 'valueUnit');">
			</select>
		</td>
	</tr>
	<tr>
		<td class="rightLabelNoBottom" colspan="2">
			<strong>Is column value constant?</strong>
			<input type="checkbox">
			&nbsp;&nbsp;
			<input type="text" id="value">
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom">
			&nbsp;
		</td>
		<td class="rightLabelNoBottomRightAlign">
			<input class="noBorderButton" type="button" value="New"
				onclick="clearTheDataColumn();" />
			<input class="noBorderButton" type="button" value="Save"
				onclick="addDatumColumn();" />
			<input class="noBorderButton" type="button" value="Delete"
				onclick="deleteDatumColumn()" />
		</td>
	</tr>

	<tr id="datumColumnsDivRowDisplay">
		<td class="leftLabelNoBottom" valign="top" colspan="2">
			<table>
				<tr>
					<td colspan="2">
						<div id="datumColumnsDivDisplay" style="display: block;">
							<table id="datumColumnsTableDisplay" class="smalltable"
								border="1">
								<tbody id="datumColumnsDisplay">
									<tr id="datumColumnPatternRowDisplay">
										<td id="datumColumnPatternDisplay" style="display: none;">
											<input class="noBorderButton" id="datumColumnNameDisplay"
												type="button" size="2" value="datumColumnNameDisplay"
												onclick="editColumn(this.id)" />
											<span id="columnDisplayName" class="greyFont2"
												style="display: none;">columnDisplayName</span>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td align="right">
						<input class="noBorderButton" type="button"
							value="Populate Data Table" onclick="show('populateDataTable')" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br>

<div id="populateDataTable" style="display: none;">
	<table border="0" class="smalltable3" width="90%">
		<tr>
			<td valign="top" colspan="2" class="subformTitle">
				Populate Data Table
			</td>
		</tr>
		<tr id="datumColumnsDivRow" style="display: none;">
			<td class="leftLabelNoBottom" valign="top" colspan="2">
				<div id="datumColumnsDiv" style="display: block;">
					<table id="datumColumnsTable" class="smalltable" border="0">
						<tbody id="datumColumns">
							<tr id="datumColumnPattern" style="display: none;">
								<td>
									<input id="datumColumnId" type="hidden" value="datumColumnId" />
									<input id="datumOrConditionColumn" type="hidden"
										value="datumOrConditionColumn" />
									<input id="conditionColumnProperty" type="hidden"
										value="conditionColumnProperty" />
									<input id="datumColumnDataRowId" type="hidden"
										value="datumColumnDataRowId" />
									<input id="datumColumnDataSetId" type="hidden"
										value="datumColumnDataSetId" />
									<span id="datumColumnName" class="greyFont2">datumColumnName</span>
									(
									<span id="datumColumnValueType" class="greyFont2">ValueType</span>,
									<span id="datumColumnValueUnit" class="greyFont2"><strong>ValueUnit</strong>
									</span>)
								</td>
								<td>
									<input id="datumColumnValue" type="text" size="6"
										value="datumColumnValue" />
								</td>
							</tr>
							<tr id="datumColumnsDivRow2">
								<td>
									&nbsp;
								</td>
								<td class="rightLabelNoBottom" valign="top" align="right"
									colspan="1">
									<div id="addRowButtons" style="display: none;">
										<input class="noBorderButton" type="button" value="New"
											onclick="clearTheDataRow();" />
										<input class="noBorderButton" type="button" value="Save"
											onclick="addRow()" />
										<input class="noBorderButton" type="button" value="Delete"
											onclick="deleteClicked()" />
									</div>
									&nbsp;
								</td>
							</tr>
						</tbody>
					</table>
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
	</table>
</div>
<input name="theDataSetId" id="theDataSetId" type="hidden"
	value="${characterizationForm.map.achar.theDataSet.domain.id}">
<html:hidden styleId="dataSetId" property="achar.theDataSet.domain.id" />