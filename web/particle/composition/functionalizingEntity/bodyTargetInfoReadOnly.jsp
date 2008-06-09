<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="funcIndex" value="${param.funcInd }" />
<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" summary="" border="0">
	<tbody>
		<tr>
			<td class="leftLabelWithTop" valign="top">
				<strong>Target Type</strong>
			</td>
			<td class="labelWithTop" valign="top">
				<strong>Target Name</strong>
			</td>
			<td class="rightLabelWithTop" valign="top">
				<strong>Description</strong>
			</td>
		</tr>
		<logic:iterate id="targetData" name="functionalizingEntityForm"
			property="entity.functions[${param.funcInd}].targets"
			indexId="targetInd">
			<tr>
				<td class="leftLabel" valign="top">
					${functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].type}&nbsp;
					<c:if
						test="${functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].type == 'antigen' && !empty functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].antigen.species}">
						<br>(Species:&nbsp;
						${functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].antigen.species})
						</c:if>
					&nbsp;
				</td>
				<td class="label" valign="top" align="left" width="15%">
					${functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].name}&nbsp;
				</td>
				<td class="rightLabel" valign="top" align="right">
					${functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].description}&nbsp;
				</td>
			</tr>
		</logic:iterate>
	</tbody>
</table>

