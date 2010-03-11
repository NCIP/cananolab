<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="promptbox" width="85%" align="center">
	<tr>
		<td class="cellLabel" colspan="${characterizationForm.map.achar.theFinding.numberOfColumns}">
			Please enter column order for each column below.
		</td>
	</tr>
	<tr>
		<logic:iterate id="col" name="characterizationForm"
			property="achar.theFinding.columnHeaders" indexId="cInd">
			<td class="cellLabel">
				<c:choose>
					<c:when test="${!empty col.displayName}">
						<span id="colOrderName${cInd}">${col.displayName}</span>
					</c:when>
					<c:otherwise>
						<span id="colOrderName${cInd}">Column ${cInd+1}</span>
					</c:otherwise>
				</c:choose>
			</td>
		</logic:iterate>
	</tr>
	<tr>
		<logic:iterate id="col" name="characterizationForm"
			property="achar.theFinding.columnHeaders" indexId="cInd">
			<td>
				<html:text styleId="columnOrder${cInd}" size="5" 
					property="achar.theFinding.columnHeaders[${cInd}].columnOrder" 
					onkeydown="return filterInteger(event)" />
			</td>
		</logic:iterate>
	</tr>
	<tr>
		<td colspan="${characterizationForm.map.achar.theFinding.numberOfColumns}">
			<div align="right">
				<input class="promptButton" type="button" value="Save"
					onclick="javascript:updateColumnOrder(characterizationForm);" />
				<input class="promptButton" type="button" value="Cancel"
					onclick="javascript:show('updateMatrixLink');show('newMatrix');hide('newColumnOrder');" />
			</div>
		</td>
	</tr>
</table>