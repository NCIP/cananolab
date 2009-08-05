<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="promptbox" width="85%" align="center" id="matrix">
	<tr>
		<logic:iterate id="col" name="characterizationForm"
			property="achar.theFinding.columnHeaders" indexId="cInd">
			<td class="cellLabel">
				<div id="column${cInd}" style="position: relative">
					<a
						href="javascript:openColumnForm('${characterizationForm.map.achar.characterizationName}', ${cInd});"><span
						id="columnHeaderDisplayName${cInd}"> <c:choose>
								<c:when test="${!empty col.displayName}">
								${col.displayName}
								</c:when>
								<c:otherwise>
						Column ${cInd+1}
						</c:otherwise>
							</c:choose> </span> </a>
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].columnType"
						styleId="theColumnType${cInd}" />
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].columnName"
						styleId="theColumnName${cInd}" />
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].valueType"
						styleId="theValueType${cInd}" />
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].valueUnit"
						styleId="theValueUnit${cInd}" />
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].conditionProperty"
						styleId="theConditionProperty${cInd}" />
					<html:hidden
						property="achar.theFinding.columnHeaders[${cInd}].constantValue"
						styleId="theConstantValue${cInd}" />
				</div>
			</td>
		</logic:iterate>
		<td></td>
	</tr>
	<logic:iterate id="row" name="characterizationForm"
		property="achar.theFinding.rows" indexId="rInd">
		<tr>
			<logic:iterate id="cell" name="characterizationForm"
				property="achar.theFinding.rows[${rInd}].cells" indexId="cInd">
				<td>
					<html:text
						property="achar.theFinding.rows[${rInd}].cells[${cInd}].value"
						size="15" styleId="cellValue${rInd}:${cInd}"
						onkeydown="return filterFloatForColumn(event, 'theColumnType${cInd}');" />
				</td>
			</logic:iterate>
			<td>
				<a
					href="javascript:reduceMatrix(characterizationForm, 'Row', ${rInd})">Delete</a>
			</td>
		</tr>
	</logic:iterate>
</table>