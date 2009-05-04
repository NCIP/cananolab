<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<c:set var="matrixStyle" value="display:none" />
<c:if test="${param.dispatch eq 'drawMatrix'}">
	<c:set var="matrixStyle" value="display:block;" />
</c:if>
<table class="promptbox" width="85%" align="center">
	<tr>
		<td class="cellLabel">
			Number of Columns
		</td>
		<td>
			<html:text property="achar.theFinding.numberOfColumns" size="2"
				styleId="colNum" onkeydown="return filterInteger(event)"
				onkeyup="updateMatrix(characterizationForm)" />
		</td>
		<td class="cellLabel">
			Number of Rows
		</td>
		<td>
			<html:text property="achar.theFinding.numberOfRows" size="2"
				styleId="rowNum" onkeydown="return filterInteger(event)"
				onkeyup="updateMatrix(characterizationForm)" />
		</td>
	</tr>
	<tr>
	<td colspan="4"><jsp:include page="/bodyMessage.jsp?bundle=particle"/></td>
	</tr>
</table>

<table class="promptbox" width="85%" align="center" id="matrix"
	border="1" style="">
	<tr>
		<logic:iterate id="col" name="characterizationForm"
			property="achar.theFinding.columnHeaders" indexId="cInd">
			<td class="cellLabel">
				<div id="column${cInd}" style="position: relative">
					<a href="javascript:openColumnForm(${cInd});"><span
						id="columnHeaderDisplayName${cInd}">Column ${cInd+1}</span> </a>
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].columnType"
						styleId="theColumnType" />
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].columnName"
						styleId="theColumnName" />
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].valueType"
						styleId="theValueType" />
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].valueUnit"
						styleId="theValueUnit" />
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].conditionProperty"
						styleId="theConditionProperty" />
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].constantValue"
						styleId="theConstantValue" />
				</div>
			</td>
		</logic:iterate>
	</tr>
	<logic:iterate id="row" name="characterizationForm"
		property="achar.theFinding.rows" indexId="rInd">
		<tr>
			<logic:iterate id="cell" name="characterizationForm"
				property="achar.theFinding.rows[${rInd}].cells" indexId="cInd">
				<td>
					<html:text
						property="achar.theFinding.rows[${rInd}].cells[${cInd}].value"
						size="15" styleId="cellValue${rInd}:${cInd}"/>
				</td>
			</logic:iterate>
			<td><a href="javascript:reduceMatrix(characterizationForm, 'Row', ${rInd})"><img border="0" src="images/btn_delete.gif" alt="delete row"></a></td>
		</tr>
	</logic:iterate>
	<c:if
		test="${fn:length(characterizationForm.map.achar.theFinding.rows)>0}">
		<tr>
			<td
				colspan="${fn:length(characterizationForm.map.achar.theFinding.columnHeaders)-2}"></td>
			<td colspan="2">
				<div align="right">
					<input class="promptButton" type="Button" value="Add"
						onclick="javascript:submitAction(characterizationForm, 'characterization', 'addMatrix');">
					<input class="promptButton" type="Button" value="Cancel" onclick="">
				</div>
			</td>
		</tr>
	</c:if>
</table>