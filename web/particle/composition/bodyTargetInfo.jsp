<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<logic:iterate id="targetData" name="functionalizingEntityForm"
	property="entity.functions[${param.funcInd}].targets"
	indexId="targetInd">
	<table class="topBorderOnly" cellspacing="0" cellpadding="3"
		width="100%" align="center" summary="" border="0">
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
					<strong>Species</strong>
				</td>
				<td class="labelWithTop" valign="top">
					<strong>Description</strong>
				</td>

				<td class="rightLabelWithTop">
					<Strong>&nbsp;</Strong>
				</td>
			</tr>
			</c:if>
			<tr>
				<td class="leftLabel" valign="top">
					<html:select property="entity.functions[${param.funcInd}].targets[${targetInd }].type" size="1"
						styleId="targetType_${param.funcInd}_${targetInd }"
						onchange="javascript:callPrompt('Target Type', 'targetType_${param.funcInd}_${targetInd }');
									displayModality(${param.funcInd}, ${targetInd}); ">
						<option value="" />
						<html:options name="functionTypes" />
						<option value="other">
							Other
						</option>
					</html:select>
				</td>
				<td class="label" valign="top">
					<html:text property="entity.functions[${param.funcInd}].targets[${targetInd }].name" />
				</td>
				<td class="label" valign="top">
					<html:select property="entity.functions[${param.funcInd}].targets[${targetInd}].antigen.species" size="1" >
						<option value=""></option>
						<html:options name="antigenSpecies" />
					</html:select>
				</td>
				<td class="label">
					<html:textarea property="entity.functions[${param.funcInd}].targets[${targetInd }].description" rows="1"
						cols="25" />
				</td>
				<td class="rightLabel">
					<a href="#" onclick="javascript:removeChildComponent(functionalizingEntityForm, 
					'functionalizingEntity', ${param.funcInd}, ${targetInd}, 'removeTarget');">
					<span class="addLink2">remove</span> </a>
				</td>
			</tr>

		</tbody>
	</table>
</logic:iterate>
