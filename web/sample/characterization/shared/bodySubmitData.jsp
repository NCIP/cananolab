<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table id="designDataTable" class="summaryViewLayer4" width="95%"
	align="center">
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
			<table>
				<tr>
					<td>
						<div id="datumColumnsDivDisplay" style="display: block;">
							<table id="datumColumnsTableDisplay" class="summaryViewLayer4"
								border="1" width="85%">
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
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<table id="datumColumnsDivRow" class="summaryViewLayer4"
				style="display: none;">
				<tbody id="datumColumns">
					<tr id="datumColumnPattern" style="display: none;">
						<td>
							<input id="datumColumnId" type="hidden" value="datumColumnId" />
							<input id="datumOrConditionColumn" type="hidden"
								value="datumOrConditionColumn" />
							<input id="conditionColumnProperty" type="hidden"
								value="conditionColumnProperty" />
							<input id="datumColumnRowId" type="hidden"
								value="datumColumnRowId" />
							<input id="datumColumnFindingId" type="hidden"
								value="datumColumnFindingId" />
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
					<tr id="datumColumnsDivRow2">
						<td>
							&nbsp;
						</td>
						<td align="right" colspan="1">
							<div id="addRowButtons" style="display: none;">
								<input class="noBorderButton" type="button" value="New"
									onclick="clearTheRow();" />
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
		</td>
	</tr>
	<tr id="datumMatrixDivRow">
		<td></td>
		<td>
			<div id="datumMatrixDiv" style="display: block;">
				<table id="datumMatrixTable" class="summaryViewLayer4" border="1"
					width="85%" align="left">
					<tbody id="datumMatrix">
						<tr id="matrixHeader" style="display: none;">
						</tr>
						<tr id="datumMatrixPatternRow" style="display: none;">
						</tr>
					</tbody>
				</table>
			</div>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<table id="columnDesign" style="display: none"
				class="summaryViewLayer4" width="85%" align="left">
				<tr>
					<th colspan="2">
						Column Information
					</th>
				</tr>
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
							<logic:iterate id="findingColumnValueType"
								name="findingColumnValueTypes">
								<option value="${findingColumnValueType}">
									${findingColumnValueType}
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
<input name="theFindingId" id="theFindingId" type="hidden"
	value="${characterizationForm.map.achar.theFinding.domain.id}">
<html:hidden styleId="findingId" property="achar.theFinding.domain.id" />