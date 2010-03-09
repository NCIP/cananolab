<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table id="newColumnOrder" style="display: none;" class="promptbox" width="100%" align="center">
	<tr>
		<td class="cellLabel" colspan="2">
			Please enter column order for each column below.
		</td>
	</tr>
<logic:iterate id="col" name="characterizationForm"
	property="achar.theFinding.columnHeaders" indexId="cInd">
	<tr>
		<td class="cellLabel">
			<c:choose>
				<c:when test="${!empty col.displayName}">
					${col.displayName}
				</c:when>
				<c:otherwise>
					Column ${cInd+1}
				</c:otherwise>
			</c:choose>
		</td>
		<td>
			<html:text styleId="columnOrder${cInd}" size="5" 
				property="achar.theFinding.columnHeaders[${cInd}].columnOrder" 
				onkeydown="return filterInteger(event)" />
		</td>
	</tr>
</logic:iterate>
	<tr>
		<td class="cellLabel" colspan="2">
			<div align="right">
				<input class="promptButton" type="button" value="Save"
					onclick="javascript:updateColumnOrder(characterizationForm);" />
				<input class="promptButton" type="button" value="Cancel"
					onclick="javascript:resetColumnOrder();hide('newColumnOrder');" />
			</div>
		</td>
	</tr>
</table>