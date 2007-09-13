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
			<td class="formSubTitle" colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<c:set var="attachmentVis" value="hidden" />
			<c:set var="encapsulationVis" value="hidden" />
			<c:set var="linkageType"
				value="${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].type}" />
			<td class="leftLabelWithTop" valign="top" width="15%">
				<strong>Linkage Type</strong>
				<br>
			</td>
			<td class="labelWithTop" width="25%">
				${linkageType}
			</td>
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
			<td class="rightLabelWithTop">
				<span id="Attachment Detail${param.linkageInd}"
					style="visibility: ${attachmentVis};position:absolute;"> <strong>Bond Type</strong>
					${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].bondType}&nbsp;
				</span>
				<span id="Encapsulation Detail${param.linkageInd}"
					style="visibility: ${encapsulationVis}"> <strong>Localization</strong>
					${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].localization}
					&nbsp; </span>
			</td>
		</tr>		
		<tr>
			<td class="leftLabel">
				<Strong>Linkage Description</Strong>
				<br>
			</td>
			<td class="rightLabel" colspan="2">
				${nanoparticleFunctionForm.map.linkage.description}&nbsp;
				<br>
			</td>
		</tr>
		<tr>
			<c:set var="antibodyVis" value="hidden" />
			<c:set var="dnaVis" value="hidden" />
			<c:set var="contrastAgentVis" value="hidden" />
			<c:set var="peptideVis" value="hidden" />
			<c:set var="smallMolVis" value="hidden" />
			<c:set var="agentType"
				value="${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].agent.type}" />
			<td class="leftLabel" valign="top">
				<strong>Agent Type</strong>
			</td>
			<td class="label" valign="top">
				${agentType}
			</td>
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
				<span id="Antibody Detail${param.linkageInd}" style="visibility: ${antibodyVis};position:absolute;">
					<table>
						<tr>
							<td class="borderlessLabel">
								<strong>Name</strong>
							</td>
							<td class="borderlessLabel">
								${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].agent.antibody.name}
							</td>
						</tr>
						<tr>
							<td class="borderlessLabel">
								<strong>Species</strong>
							</td>
							<td class="borderlessLabel">
								${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].agent.antibody.species}&nbsp;
							</td>
						</tr>
					</table> </span>
				<span id="DNA Detail${param.linkageInd}" style="visibility: ${dnaVis};position:absolute;"><table>
						<tr>
							<td class="borderlessLabel">
								<strong>Sequence</strong>
							</td>
							<td class="borderlessLabel">
								${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].agent.dna.sequence}&nbsp;
							</td>
						</tr>
					</table> </span>
				<span id="Image Contrast Agent Detail${param.linkageInd}"
					style="visibility: ${contrastAgentVis};position:absolute;">
					<table>
						<tr>
							<td class="borderlessLabel">
								<strong>Name</strong>
							</td>
							<td class="borderlessLabel">
								${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].agent.imageContrastAgent.name}&nbsp;
							</td>
						</tr>
						<tr>
							<td class="borderlessLabel">
								<strong>Type</strong>
							</td>
							<td class="borderlessLabel">
								${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].agent.imageContrastAgent.type}&nbsp;
							</td>
						</tr>
					</table> </span>
				<span id="Peptide Detail${param.linkageInd}" style="visibility: ${peptideVis};position:absolute;"><table>
						<tr>
							<td class="borderlessLabel">
								<strong>Sequence</strong>
							</td>
							<td class="borderlessLabel">
								${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].agent.peptide.sequence}&nbsp;
							</td>
						</tr>
					</table> </span>
				<span id="Small Molecule Detail${param.linkageInd}"
					style="visibility: ${smallMolVis};position:absolute;"><table>
						<tr>
							<td class="borderlessLabel">
								<strong>Name</strong>
							</td>
							<td class="borderlessLabel">
								${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].agent.smallMolecule.name}&nbsp;
							</td>
						</tr>
						<tr>
							<td class="borderlessLabel">
								<strong>Compound Name</strong>
							</td>
							<td class="borderlessLabel">
								${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].agent.smallMolecule.compoundName}&nbsp;
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
				${nanoparticleFunctionForm.map.function.linkages[param.linkageInd].description}&nbsp;
				<br>
			</td>
		</tr>		
		<tr>
			<td class="completeLabel" colspan="4">
				<table border="0" width="100%">
					<tr>
						<td></td>
						<td>
							<logic:iterate id="target" name="nanoparticleFunctionForm"
								property="function.linkages[${param.linkageInd}].agent.agentTargets"
								indexId="tIndex">
								<jsp:include
									page="/submit/function/bodyParticleFunctionAgentTargetReadOnly.jsp">
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
