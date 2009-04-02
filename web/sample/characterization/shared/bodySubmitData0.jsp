<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table id="designDataTable" class="summaryViewLayer4" width="95%"
	align="right">
	<tr>
		<td class="cellLabel" width="5%">
			Columns
		</td>
		<td align="left">
			<a style="" id="addColumn" href="javascript:show('columnDesign');"><img
					align="top" src="images/btn_add.gif" border="0" /> </a>
		</td>
	</tr>
	<tr id="datumColumnsDivRowDisplay">
		<td></td>
		<td>
			<table id="datumColumnsTableDisplay" class="summaryViewLayer4"
				border="1" style="display: block;" width="85%" align="left">
				<tr id="datumColumnPatternRowDisplay">
					<td id="datumColumnPatternDisplay" style="display: none;">
						<input class="noBorderButton" id="datumColumnNameDisplay"
							type="button" size="2" value="datumColumnNameDisplay"
							onclick="editColumn(this.id)" />
						<span id="columnDisplayName" style="display: none;">columnDisplayName</span>
					</td>
				</tr>
				<tr id="datumColumnsDivRow" style="display: none;">
					<td id="datumColumnPattern" style="display: none;">
						<input id="datumColumnId" type="hidden" value="datumColumnId" />
						<input id="datumOrConditionColumn" type="hidden"
							value="datumOrConditionColumn" />
						<input id="conditionColumnProperty" type="hidden"
							value="conditionColumnProperty" />
						<input id="datumColumnDataRowId" type="hidden"
							value="datumColumnDataRowId" />
						<input id="datumColumnDataSetId" type="hidden"
							value="datumColumnDataSetId" />
						<span id="datumColumnName" class="cellLabel">datumColumnName</span>
						(
						<span id="datumColumnValueType" class="cellLabel">ValueType</span>,
						<span id="datumColumnValueUnit" class="cellLabel"><strong>ValueUnit</strong>
						</span>)
					</td>
					<td>
						<input id="datumColumnValue" type="text" size="6"
							value="datumColumnValue" />
					</td>
				</tr>
				<%--
				<tr id="datumColumnsDivRow2">
					<td align="right" colspan="1">
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
				--%>
				<tr id="matrixHeader" style="display: none;">
				</tr>
				<tr id="datumMatrixPatternRow" style="display: none;">
				</tr>
			</table>
		</td>
	</tr>
	<%--
					<div id="datumMatrixDivRow">
					<tr id="matrixHeader" style="display: none;">
					</tr>
					<tr id="datumMatrixPatternRow" style="display: none;">
					</tr>
					</div>
--%>
	<tr>
		<td></td>
		<td>
			<table id="columnDesign" style="display: none"
				class="summaryViewLayer4" width="85%" align="left">
				<tr>
					<td class="cellLabel">
						Column Type*
					</td>
					<td>
						<input type="hidden" id="columnId">
						<select id="datumOrCondition"
							onChange="showDatumConditionInfo(null);">
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
					<td class="cellLabel">
						Condition Property
					</td>
					<td>
						<select id="property"
							onchange="javascript:callPrompt('Condition Property', 'property');setColumnValueUnit();">
						</select>
					</td>
				</tr>
				<tr>
					<td class="cellLabel">
						Column Value Type
					</td>
					<td>
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
				</tr>
				<tr>
					<td class="cellLabel">
						Column Value Unit
					</td>
					<td>
						<select id="valueUnit"
							onchange="javascript:callPrompt('Column Value Unit', 'valueUnit');">
						</select>
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
					<td></td>
					<td style="align: right;">
						<input class="noBorderButton" type="button" value="Delete"
							onclick="deleteDatumColumn()" />
						<input class="noBorderButton" type="button" value="Cancel"
							onclick="hide('columnDesign');clearTheDatumColumn;" />
						<input class="noBorderButton" type="button" value="Save"
							onclick="addDatumColumn(); hide('columnDesign')" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<input name="theDataSetId" id="theDataSetId" type="hidden"
	value="${characterizationForm.map.achar.theDataSet.domain.id}">
<html:hidden styleId="dataSetId" property="achar.theDataSet.domain.id" />