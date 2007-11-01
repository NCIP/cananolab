<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="formSubTitle" colspan="4" align="right">
				<a href="#"
					onclick="javascript:removeTarget(nanoparticleFunctionForm, ${param.linkageInd}, ${param.targetInd})">
					<img src="images/delete.gif" border="0"
						alt="remove this agent target"> </a>
			</td>
		</tr>
		<tr>
			<td class="leftLabelWithTop">
				<strong>Target Type*</strong>
			</td>
			<td class="labelWithTop">
				<html:select
					property="function.linkages[${param.linkageInd}].agent.agentTargets[${param.targetInd}].type">
					<option value=""></option>
					<html:options name="allAgentTargetTypes" />
				</html:select>
			</td>
			<td class="labelWithTop">
				<strong>Target Name</strong>
			</td>
			<td class="rightLabelWithTop">
				<html:text
					property="function.linkages[${param.linkageInd}].agent.agentTargets[${param.targetInd}].name" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Target Description</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:textarea
					property="function.linkages[${param.linkageInd}].agent.agentTargets[${param.targetInd}].description"
					rows="3" cols="60" />
			</td>
		</tr>
</table>
<br>
