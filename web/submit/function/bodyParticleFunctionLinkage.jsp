<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript" src="javascript/editableDropDown.js"></script>

<script language="JavaScript">
<!--
function updateDetail(elementId, ind) {
	var sel=document.getElementById(elementId);
	for (var i=0; i<sel.options.length; i++) {
	  var type=sel.options[i].value;	  	  
	  if (i!=sel.selectedIndex) {
	     hideDetail(type+" Detail"+ind);
	  }
	  else {
	     showDetail(type+" Detail"+ind);
	  } 
	}
}

function showDetail(elementId) {
	var element=document.getElementById(elementId);
	if (element!=null) {
	  element.style.visibility="visible";
	}
}

function hideDetail(elementId) {
	var element=document.getElementById(elementId);
	if (element!=null) {
	  element.style.visibility="hidden";
    }
}
//-->
</script>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<td class="formSubTitle" colspan="3" align="right">
						<a href="#"
							onclick="javascript:removeLinkage(nanoparticleFunctionForm, ${param.linkageInd})">
							<img src="images/delete.gif" border="0" alt="remove this linkage">
						</a>
					</td>
				</c:when>
				<c:otherwise>
					<td></td>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td class="leftLabel" valign="top" width="15%">
				<strong>Linkage Type*</strong>
				<br>
			</td>
			<td class="label" width="25%">
				<html:select styleId="linkageType${param.linkageInd}"
					property="function.linkages[${param.linkageInd}].type"
					onchange="javascript:updateDetail('linkageType${param.linkageInd}', ${param.linkageInd})">
					<option value=""></option>
					<html:options name="allLinkageTypes" />
				</html:select>
			</td>
			<c:set var="attachmentVis" value="hidden" />
			<c:set var="encapsulationVis" value="hidden" />
			<c:set var="linkageType"
				value="${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].type}" />
			<c:choose>
				<c:when test="${linkageType eq 'Attachment'}">
					<c:set var="attachmentVis" value="show" />
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${linkageType eq 'Encapsulation'}">
					<c:set var="encapsulationVis" value="show" />
				</c:when>
			</c:choose>
			<td class="rightLabel">
				<span id="Attachment Detail${param.linkageInd}"
					style="visibility:${attachmentVis};position:absolute;"> <strong>Bond
						Type</strong> <html:select
						property="function.linkages[${param.linkageInd}].bondType"
						onkeydown="javascript:fnKeyDownHandler(this, event);"
						onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
						onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
						onchange="fnChangeHandler_A(this, event);">
						<option value="">
							--?--
						</option>
						<html:options name="allBondTypes" />
					</html:select> </span>
				<span id="Encapsulation Detail${param.linkageInd}"
					style="visibility:${encapsulationVis};"> <strong>Localization</strong>
					<html:text
						property="function.linkages[${param.linkageInd}].localization" />
					&nbsp; </span>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<Strong>Linkage Description</Strong>
				<br>
			</td>
			<td class="rightLabel" colspan="2">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:textarea
							property="function.linkages[${param.linkageInd}].description"
							rows="3" cols="60" />
					</c:when>
					<c:otherwise>
															${linkage.description}&nbsp;
														</c:otherwise>
				</c:choose>
				<br>
			</td>
		</tr>

		<tr>
			<td class="leftLabel" valign="top">
				<strong>Agent Type*</strong>
			</td>
			<td class="label" valign="top">
				<html:select styleId="agentType${param.linkageInd}"
					property="function.linkages[${param.linkageInd}].agent.type"
					onchange="javascript:updateDetail('agentType${param.linkageInd}', ${param.linkageInd})">
					<option value=""></option>
					<html:options name="allAgentTypes" />
				</html:select>
			</td>
			<c:set var="antibodyVis" value="hidden" />
			<c:set var="dnaVis" value="hidden" />
			<c:set var="contrastAgentVis" value="hidden" />
			<c:set var="peptideVis" value="hidden" />
			<c:set var="smallMolVis" value="hidden" />
			<c:set var="agentType"
				value="${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].agent.type}" />
			<c:choose>
				<c:when test="${agentType eq 'Antibody'}">
					<c:set var="antibodyVis" value="show" />
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${agentType eq 'DNA'}">
					<c:set var="dnaVis" value="show" />
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${agentType eq 'Image Contrast Agent'}">
					<c:set var="contrastAgentVis" value="show" />
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${agentType eq 'Peptide'}">
					<c:set var="peptideVis" value="show" />
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${agentType eq 'Small Molecule'}">
					<c:set var="smallMolVis" value="show" />
				</c:when>
			</c:choose>
			<td class="rightLabel" valign="top" height="60">
				<span id="Antibody Detail${param.linkageInd}"
					style="visibility:${antibodyVis};position:absolute;">
					<table>
						<tr>
							<td class="borderLessLabel">
								<strong>Name</strong>
							</td>
							<td>
								<html:text
									property="function.linkages[${param.linkageInd}].antibody.name"
									size="30" />
							</td>
						</tr>
						<tr>
							<td class="borderLessLabel">
								<strong>Species</strong>
							</td>
							<td>
								<html:select
									property="function.linkages[${param.linkageInd}].antibody.species">
									<option value=""></option>
									<html:options name="allSpecies" />
								</html:select>
							</td>
						</tr>
					</table> </span>
				<span id="DNA Detail${param.linkageInd}"
					style="visibility:${dnaVis};position:absolute;"><table>
						<tr>
							<td class="borderLessLabel">
								<strong>Sequence</strong>
							</td>
							<td>
								<html:text
									property="function.linkages[${param.linkageInd}].dna.sequence"
									size="30" />
							</td>
						</tr>
					</table> </span>
				<span id="Image Contrast Agent Detail${param.linkageInd}"
					style="visibility:${contrastAgentVis};position:absolute;">
					<table>
						<tr>
							<td class="borderLessLabel">
								<strong>Name</strong>
							</td>
							<td>
								<html:text
									property="function.linkages[${param.linkageInd}].imageContrastAgent.name"
									size="30" />
							</td>
						</tr>
						<tr>
							<td class="borderLessLabel">
								<strong>Type</strong>
							</td>
							<td>
								<html:select
									property="function.linkages[${param.linkageInd}].imageContrastAgent.type"
									onkeydown="javascript:fnKeyDownHandler(this, event);"
									onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
									onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
									onchange="fnChangeHandler_A(this, event);">
									<option value="">
										--?--
									</option>
									<html:options name="allImageContrastAgentTypes" />
								</html:select>
							</td>
						</tr>
					</table> </span>
				<span id="Peptide Detail${param.linkageInd}"
					style="visibility:${peptideVis};position:absolute;"><table>
						<tr>
							<td class="borderLessLabel">
								<strong>Sequence</strong>
							</td>
							<td>
								<html:text
									property="function.linkages[${param.linkageInd}].peptide.sequence"
									size="30" />
							</td>
						</tr>
					</table> </span>
				<span id="Small Molecule Detail${param.linkageInd}"
					style="visibility:${smallMolVis};position:absolute;"><table>
						<tr>
							<td class="borderLessLabel">
								<strong>Name</strong>
							</td>
							<td>
								<html:text
									property="function.linkages[${param.linkageInd}].smallMolecule.name"
									size="30" />
							</td>
						</tr>
						<tr>
							<td class="borderLessLabel">
								<strong>Compound Name</strong>
							</td>
							<td>
								<html:text
									property="function.linkages[${param.linkageInd}].smallMolecule.compoundName"
									size="30" />
							</td>
						</tr>
					</table> </span>&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<Strong>Agent Description</Strong>
				<br>
			</td>
			<td class="rightLabel" colspan="3">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:textarea
							property="function.linkages[${param.linkageInd}].agent.description"
							rows="3" cols="60" />
					</c:when>
					<c:otherwise>
						${linkage.agent.description}&nbsp;
					</c:otherwise>
				</c:choose>
				<br>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<table border="0" width="100%">
					<tr>
						<c:choose>
							<c:when test="${canUserSubmit eq 'true'}">
								<td valign="bottom">
									<a href="#"
										onclick="javascript:addTarget(nanoparticleFunctionForm, ${param.linkageInd})"><span
										class="addLink">Add Agent Target</span> </a>
								</td>
							</c:when>
							<c:otherwise>
								<td></td>
							</c:otherwise>
						</c:choose>
						<td>
							<logic:iterate id="target" name="nanoparticleFunctionForm"
								property="function.linkages[${param.linkageInd}].agent.agentTargets"
								indexId="tIndex">
								<jsp:include
									page="/submit/function/bodyParticleFunctionAgentTarget.jsp">
									<jsp:param name="linkageInd" value="${param.linkageInd}" />
									<jsp:param name="targetInd" value="${tIndex}" />
								</jsp:include>
								<br>
							</logic:iterate>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</tbody>
</table>
<br>
