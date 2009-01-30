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
			<strong>Datum</strong>
		</td>
		<td class="labelWithTopNoBottom">
			<strong>Name</strong>
		</td>
		<td class="labelWithTopNoBottom">
			<html:select
				property="achar.theExperimentConfig.domain.technique.type"
				styleId="techniqueType"
				onchange="javascript:callPrompt('Technique Type', 'techniqueType');retrieveTechniqueAbbreviation();">
				<option value=""></option>
				<option value="other">
					[Other]
				</option>
			</html:select>
		</td>
		<td class="labelWithTopNoBottom">
			<strong>Condition</strong>
		</td>
		<td class="labelWithTopNoBottom">
			<strong>Name</strong>
		</td>
		<td class="rightLabelWithTopNoBottom">
			<html:select
				property="achar.theExperimentConfig.domain.technique.type"
				styleId="techniqueType"
				onchange="javascript:callPrompt('Technique Type', 'techniqueType');retrieveTechniqueAbbreviation();">
				<option value=""></option>
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
		<td class="labelNoBottom">
			<html:text property="achar.theExperimentConfig.domain.technique.type" />
		</td>
		<td class="labelNoBottom">
			&nbsp;
		</td>
		<td class="labelNoBottom">
			<strong>Value Type</strong>
		</td>
		<td class="rightLabelNoBottom">
			<html:text property="achar.theExperimentConfig.domain.technique.type" />
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom">
			&nbsp;
		</td>
		<td class="labelNoBottom">
			<strong>Value Unit</strong>
		</td>
		<td class="labelNoBottom">
			<html:text property="achar.theExperimentConfig.domain.technique.type" />
		</td>
		<td class="labelNoBottom">
			&nbsp;
		</td>
		<td class="labelNoBottom">
			<strong>Value Unit</strong>
		</td>
		<td class="rightLabelNoBottom">
			<html:text property="achar.theExperimentConfig.domain.technique.type" />
		</td>
	</tr>

	<tr>
		<td class="leftLabelNoBottom" colspan="2">
			&nbsp;
		</td>
		<td align="right" class="labelNoBottomRightAlign">
			 <input class="noBorderButton" type="button" value="Add"
					onclick="addDatumColumn()" />
		</td>
		<td class="labelNoBottom">
			&nbsp;
		</td>
		<td class="labelNoBottom">
			<strong>Constant Value</strong>
		</td>
		<td class="rightLabelNoBottom">
			<html:text property="achar.theExperimentConfig.domain.technique.type" />
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom" colspan="5">
			&nbsp;
		</td>		
		<td class=rightLabelNoBottomRightAlign>
			 <input class="noBorderButton" type="button" value="Add"
				 onclick="addConditionColumn()" />
		</td>
	</tr>

	<tr>
		<td class="leftLabel" valign="top">
			<input type="button" value="Delete"
				onclick="javascript:submitAction(document.forms[0],
										'${actionName}', 'deleteExperimentConfig');">
		</td>
		<td class="rightLabel" align="right" colspan="5">
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