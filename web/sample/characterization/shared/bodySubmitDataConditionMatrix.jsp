<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<div id="submissionPrompt">
	<table class="promptbox" width="100%" align="center">
		<logic:iterate id="header" name="characterizationForm"
			property="achar.theFinding.columnHeaders" indexId="hInd">
			<tr>
				<td class="cellLabel">
					<c:choose>
						<c:when test="${header.type eq 'condition'">
							<c:set var="defaultLabel" value="Condition${hInd}" />
						</c:when>
						<c:otherwise>
							<c:set var="defaultLabel" value="Datum${hInd}" />
						</c:otherwise>
						<div id="headerDisplayName${hInd}">
							${defaultLabel}
						</div>
					</c:choose>
				</td>
			</tr>
		</logic:iterate>
		<logic:iterate id="row" name="characteriationForm"
			property="achar.theFinding.rows" indexId="rInd">
			<tr>
				<logic:iterate id="condition" name="characterizationForm"
					property="achar.theFinding.rows[${rInd}].conditions" indexId="cInd">
					<td>
						<html:text
							property="achar.theFinding.rows[${rInd}].conditions[${cInd}]" />
					</td>
				</logic:iterate>
				<logic:iterate id="datum" name="characterizationForm"
					property="achar.theFinding.rows[${rInd}].data" indexId="dInd">
					<td>
						<html:text property="achar.theFinding.rows[${rInd}].data[${dInd}]" />
					</td>
				</logic:iterate>
			</tr>
		</logic:iterate>
	</table>
</div>