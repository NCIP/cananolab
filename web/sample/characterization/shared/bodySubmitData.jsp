<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table id="designDataTable" class="summaryViewLayer4" width="100%"
	align="center">
	<tr>
		<td class="cellLabel">
			Add a Column for *
			<input type="hidden" id="columnId">
			&nbsp;&nbsp;
			<select id="datumOrCondition"
				onChange="showDatumConditionInfo(null);">
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
		<td>
			<table id="columnDesign" style="display: none">
				<tr>
					<td class="cellLabel">
						Column Name*
						<select id="name"
							onchange="javascript:callPrompt('Name', 'name');setConditionPropertyOptionsByCharName(null);setColumnValueUnit();">
							<option value=""></option>
							<option value="[Other]">
								[Other]
							</option>
						</select>
					</td>
					<td></td>
				</tr>
				<tr>
					<td class="cellLabel">
						<div id="conditionProperty" style="display: none;">
							Condition Property
							<select id="property"
								onchange="javascript:callPrompt('Condition Property', 'property');setColumnValueUnit();">
							</select>
						</div>
					</td>
					<td></td>
				</tr>
				<tr>
					<td class="cellLabel">
						Column Value Type
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
					<td></td>
				</tr>
				<tr>
					<td class="cellLabel">
						Column Value Unit
						<select id="valueUnit"
							onchange="javascript:callPrompt('Column Value Unit', 'valueUnit');">
						</select>
					</td>
					<td></td>
				</tr>
				<tr>
					<td class="cellLabel">
						Constant Value?
						<input type="checkbox">
						<input type="text" id="value">
					</td>
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td style="align: right;">
						<input class="noBorderButton" type="button" value="Delete"
							onclick="deleteDatumColumn()" />
						<input class="noBorderButton" type="button" value="Save"
							onclick="addDatumColumn();show('populateDataTable');" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr id="datumColumnsDivRowDisplay">
		<td colspan="2">
			<table>
				<tr>
					<td>
						<div id="datumColumnsDivDisplay" style="display: block;">
							<table id="datumColumnsTableDisplay" class="summaryViewLayer4"
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
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div id="populateDataTable" style="display: none;">
				<table id="designDataTable" class="summaryViewLayer4" style="border-style:none;"
					align="center">
					<tr id="datumColumnsDivRow" style="display: none;">
						<td colspan="2">
							<div id="datumColumnsDiv" style="display: block;">
								<table id="datumColumnsTable" class="summaryViewLayer4" width="100%">
									<tbody id="datumColumns">
										<tr id="datumColumnPattern" style="display: none;">
											<td>
												<input id="datumColumnId" type="hidden"
													value="datumColumnId" />
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
										<tr id="datumColumnsDivRow2">
											<td>
												&nbsp;
											</td>
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
									</tbody>
								</table>
							</div>

							&nbsp;
						</td>
					</tr>

					<tr id="datumMatrixDivRow">
						<td colspan="2">
							<div id="datumMatrixDiv" style="display: block;">
								<table id="datumMatrixTable" class="summaryViewLayer4" border="1" width="100%">
									<tbody id="datumMatrix">
										<tr id="matrixHeader" style="display: none;">
										</tr>
										<tr id="datumMatrixPatternRow"
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
		</td>
	</tr>
</table>
<input name="theDataSetId" id="theDataSetId" type="hidden"
	value="${characterizationForm.map.achar.theDataSet.domain.id}">
<html:hidden styleId="dataSetId" property="achar.theDataSet.domain.id" />