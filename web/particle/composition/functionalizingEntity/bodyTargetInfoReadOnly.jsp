<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="funcIndex" value="${param.funcInd }" />
<logic:iterate id="targetData" name="functionalizingEntityForm"
	property="entity.functions[${param.funcInd}].targets"
	indexId="targetInd">
	<table class="topBorderOnly" cellspacing="0" cellpadding="3"
		width="100%" summary="" border="0">
		<tbody>
			<c:if test="${targetInd == 0 }">
				<tr>
					<td class="leftLabelWithTop" valign="top">
						<strong>Target Type</strong>
					</td>
					<td class="labelWithTop" valign="top">
						<strong>Target Name</strong>
					</td>
					<td class="labelWithTop" valign="top">
						<strong>Description</strong>
					</td>
					<td class="rightLabelWithTop" valign="top">
						&nbsp;
					</td>
				</tr>
			</c:if>
			<tr>
				<td class="leftLabel" valign="top" width="15%">
					${functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].type}&nbsp;
				</td>
				<td class="label" valign="top" align="left" width="15%">
					${functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].name}&nbsp;
				</td>
				<td class="label" valign="top" align="right">
					${functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].description}&nbsp;
				</td>
				<td class="rightLabel" valign="top" align="left" width="30%">
					<c:choose>
						<c:when
							test="${functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].type == 'antigen'}">
							<c:set var="speciesDisplay" value="display: block;" />
						</c:when>
						<c:otherwise>
							<c:set var="speciesDisplay" value="display: none;" />
						</c:otherwise>
					</c:choose>
					<div id="speciesDiv_${funcIndex}_${targetInd}" style="${speciesDisplay }">
						<strong>Species:</strong>&nbsp;
						${functionalizingEntityForm.map.entity.functions[funcIndex].targets[targetInd].antigen.species}
					</div>
					&nbsp;
				</td>
			</tr>
		</tbody>
	</table>
</logic:iterate>
