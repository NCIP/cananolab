<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<td class="formSubTitle" colspan="4" align="right">
						<a href="#"
							onclick="javascript:removeTarget(nanoparticleFunctionForm, ${param.linkageInd}, ${param.targetInd})">
							<img src="images/delete.gif" border="0"
								alt="remove this agent target"> </a>
					</td>
				</c:when>
				<c:otherwise>
					<td></td>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Target Type*</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:select
							property="function.linkages[${param.linkageInd}].agent.agentTargets[${param.targetInd}].type">
							<option value=""></option>
							<html:options name="allAgentTargetTypes" />
						</html:select>
					</c:when>
					<c:otherwise>
					   ${linkage.agent.agentTargets[param.targetInd].type}&nbsp;
					</c:otherwise>
				</c:choose>

			</td>
			<td class="label">
				<strong>Target Name</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text
							property="function.linkages[${param.linkageInd}].agent.agentTargets[${param.targetInd}].name" />
					</c:when>
					<c:otherwise>
						${linkage.agent.agentTargets[param.targetInd].name}&nbsp;
					</c:otherwise>
				</c:choose>

			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Target Description</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:textarea
							property="function.linkages[${param.linkageInd}].agent.agentTargets[${param.targetInd}].description"
							rows="3" cols="60" />
					</c:when>
					<c:otherwise>
					   ${linkage.agent.agentTargets[param.targetInd].description}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
</table>
<br>
