<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="promptbox" width="85%" align="center" id="matrix">
	<tr>
		<td class="cellLabel" colspan="${characterizationForm.map.achar.theFinding.numberOfColumns}">
			Please set column order for each column below.
		</td>
	</tr>
	<tr>
		<logic:iterate id="col" name="characterizationForm"
			property="achar.theFinding.columnHeaders" indexId="cInd">
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
		</logic:iterate>
		<td></td>
	</tr>
	<tr>
		<logic:iterate id="col" name="characterizationForm"
			property="achar.theFinding.columnHeaders" indexId="cInd">
			<td class="cellLabel">
				<c:set var="currentOrder" value="${col.columnOrder}"/>
				<input type="hidden" id="theColumnOrder${cInd}"
					value="${currentOrder}" />
				<html:select styleId="columnOrder${cInd}"
					property="achar.theFinding.columnHeaders[${cInd}].columnOrder">
					<c:forEach var="i" begin="1" end="${characterizationForm.map.achar.theFinding.numberOfColumns}">
						<c:choose>
							<c:when test="${i eq currentOrder}">
								<option value="${i}" selected="selected">
									${i}
								</option>
							</c:when>
							<c:otherwise>
								<option value="${i}">
									${i}
								</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</html:select>
			</td>
		</logic:iterate>
		<td></td>
	</tr>
	<tr>
		<td colspan="${characterizationForm.map.achar.theFinding.numberOfColumns}">
			<div align="right">
				<input class="promptButton" type="button" value="Save"
					onclick="javascript:updateColumnOrder(characterizationForm);" />
				<input class="promptButton" type="button" value="Cancel"
					onclick="javascript:clearColumnOrder();hide('columnOrder');" />
			</div>
		</td>
		<td></td>
	</tr>
</table>