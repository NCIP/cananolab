<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table border="0" align="center" cellpadding="3" cellspacing="0"
	class="topBorderOnly" summary="">
	<tr>
		<td class="leftLabelWithTopNoBottom">
			<select id="datumOrCondition">
				<option value="Datum">
					Datum
				</option>
				<option value="Condition">
					Condition
					</ption>
			</select>
		</td>
		<td class="labelWithTopNoBottom">
			<strong>Name*</strong>
		</td>
		<td class="rightLabelWithTopNoBottom">
			<html:select
				property="achar.theExperimentConfig.domain.technique.type"
				styleId="name" onchange="javascript:callPrompt('Name', 'name');">
				<option value=""></option>
				<option value="test">
					test
				</option>
				<option value="other">
					[Other]
				</option>
			</html:select>
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom">
			<strong>&nbsp;</strong>
		</td>
		<td class="labelNoBottom">
			<strong>Value Type</strong>
		</td>
		<td class="rightLabelNoBottom">
			<html:text styleId="valueType"
				property="achar.theExperimentConfig.domain.technique.type" />
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom">
			<strong>&nbsp;</strong>
		</td>
		<td class="labelNoBottom">
			<strong>Value Unit</strong>
		</td>
		<td class="rightLabelNoBottom">
			<html:text styleId="valueUnit"
				property="achar.theExperimentConfig.domain.technique.type" />
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom">
			<strong>&nbsp;</strong>
		</td>
		<td class="labelNoBottom">
			<strong>Constant Value</strong>
		</td>
		<td class="rightLabelNoBottom">
			<html:text styleId="value"
				property="achar.theExperimentConfig.domain.technique.type" />
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom" colspan="2">
			&nbsp;
		</td>
		<td class=rightLabelNoBottomRightAlign>
			<input class="noBorderButton" type="button" value="Add to Header"
				onclick="addDatumColumn()" />
		</td>
	</tr>
	<tr id="datumColumnsDivRow">
		<td class="leftLabelNoBottom" valign="top" colspan="2">
			<div id="datumColumnsDiv" style="display: block;">
				<table id="datumColumnsTable" class="smalltable" border="1"
					width="80%">
					<tbody id="datumColumns">
						<tr id="datumColumnPatternRow">
							<td id="datumColumnPattern" style="display: none;">
								<span id="datumColumnName" class="greyFont2">Name</span>
								<span id="datumColumnValueType" class="greyFont2">ValueType</span>
								<span id="datumColumnValueUnit" class="greyFont2">ValueUnit</span>
								<br>
								<input id="datumColumnValue" type="text" size="2"
									value="datumColumnValue" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			&nbsp;
		</td>	
		<td class="rightLabelNoBottom" valign="top" colspan="1">
			<div id="addRowButtons" style="display: none;">
				<input class="noBorderButton" type="button" value="New"
					onclick="clearTheDataRow()" />
				<input class="noBorderButton" type="button" value="Save"
					onclick="addRow()" />
				<input class="noBorderButton" type="button" value="Delete"
					onclick="deleteClicked()" />
			</div>
			&nbsp;
		</td>
	</tr>
	<tr id="datumMatrixDivRow">
		<td class="completeLabelNoTopBottom" valign="top" colspan="3">
			<div id="datumMatrixDiv" style="display: block;">
				<table id="datumMatrixTable" class="smalltable" border="1"
					width="80%">
					<tbody id="datumMatrix">
						<tr id="matrixHeader" class="greyFont2" style="display: none;">
						</tr>
						<tr id="datumMatrixPatternRow" class="greyFont2" style="display: none;">
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
		<td class="rightLabel" align="right" colspan="2">
			<div align="right">
				<input type="reset" value="Cancel"
					onclick="javascript:resetTheDataSet(false);">
				<input type="reset" value="Reset"
					onclick="javascript:window.location.href='${origUrl}'">
				<input type="button" value="Save"
					onclick="javascript:validateSaveConfig('${actionName}');">
			</div>
		</td>
	</tr>

	<html:hidden styleId="configId"
		property="achar.theExperimentConfig.domain.id" />

</table>