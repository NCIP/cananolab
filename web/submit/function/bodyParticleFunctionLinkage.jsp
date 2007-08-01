<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript" src="javascript/editableDropDown.js"></script>

<script language="JavaScript">
<!--
function clearOtherAgents(agentType, elementPrefix) {
	var sequenceTypes=new Array('peptide', 'dna');
	var nameTypes=new Array('smallMolecule', 'antibody', 'probe', 'imageContrastAgent');
	var typeTypes=new Array('probe', 'imageContrastAgent');
	
	for (var i=0; i<sequenceTypes.length; i++) {
	  disableTextElement(nanoparticleFunctionForm, elementPrefix+'.'+sequenceTypes[i]+'.sequence');	  
	}	
	for (var i=0; i<nameTypes.length; i++) {
	  disableTextElement(nanoparticleFunctionForm, elementPrefix+'.'+nameTypes[i]+'.name');
	}
	for (var i=0; i<typeTypes.length; i++) {
	  disableTextElement(nanoparticleFunctionForm, elementPrefix+'.'+typeTypes[i]+'.type');
	}	
	disableTextElement(nanoparticleFunctionForm, elementPrefix+'.antibody.species');
	disableTextElement(nanoparticleFunctionForm, elementPrefix+'.smallMolecule.compoundName');
	
	if (agentType =='Peptide') {
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.peptide.sequence');
	}
	else if (agentType=='DNA') {
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.dna.sequence');
	}
	else if (agentType=='Antibody') {
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.antibody.name');
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.antibody.species');
	}
	else if (agentType=='Small Molecule') {
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.smallMolecule.name');
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.smallMolecule.compoundName');
	}
	else if (agentType=='Probe') {
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.probe.name');
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.probe.type');
	}
	else if (agentType=='Image Contrast Agent') {
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.imageContrastAgent.name');
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.imageContrastAgent.type');
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
					<td class="formSubTitle" colspan="4" align="right">
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
			<td class="leftLabel">
				<strong>Linkage Type </strong>
				<br>
			</td>
			<td class="rightlabel" colspan="3">
				<table cellspacing="0" cellpadding="3" width="100%" align="center"
					summary="" border="0">
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<tr>
								<td class="borderlessLabel">
									<html:radio
										property="function.linkages[${param.linkageInd}].type"
										value="Attachment"
										onclick="javascript:disableTextElement(this.form, 'function.linkages[${param.linkageInd}].localization');enableTextElement(this.form, 'function.linkages[${param.linkageInd}].bondType');" />
									Attachment
									<br>
								</td>
								<td class="borderlessLabel">
									<strong>Bond Type</strong>
									<html:select
										property="function.linkages[${param.linkageInd}].bondType"
										onkeydown="javascript:fnKeyDownHandler(this, event);"
										onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
										onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
										onchange="fnChangeHandler_A(this, event);">
										<option value="">
											--?--
										</option>
										<html:options name="allBondTypes" />
									</html:select>
									&nbsp;
									<br>
								</td>
							</tr>
							<tr>
								<td class="borderlessLabel">
									<html:radio
										property="function.linkages[${param.linkageInd}].type"
										value="Encapsulation"
										onclick="javascript:disableTextElement(this.form, 'function.linkages[${param.linkageInd}].bondType');enableTextElement(this.form, 'function.linkages[${param.linkageInd}].localization');" />
									Encapsulation
									<br>
								</td>
								<td class="borderlessLabel">
									<strong>Localization</strong>
									<html:text
										property="function.linkages[${param.linkageInd}].localization" />
									&nbsp;
									<br>
								</td>
							</tr>
							<tr>
								<td class="borderlessLabel">
									<html:radio
										property="function.linkages[${param.linkageInd}].type"
										value="Other"
										onclick="javascript:disableTextElement(this.form, 'function.linkages[${param.linkageInd}].localization');disableTextElement(this.form, 'function.linkages[${param.linkageInd}].bondType');" />
									Other
									<br>
								</td>
								<td>
									<br>
								</td>
							</tr>
						</c:when>
						<%--  read only view --%>
						<c:otherwise>
							<tr>
								<td class="borderlessLabel">
									${linkage.type}&nbsp;
									<br>
								</td>
								<c:choose>
									<c:when test="${linkage.type eq 'Attachment'}">
										<td class="borderlessLabel">
											<strong>Bond Type</strong>&nbsp;
											<br>
										</td>
										<td class="borderlessLabel">
											${linkage.bondType}&nbsp;
											<br>
										</td>
									</c:when>
									<c:when test="${linkage.type eq 'Encapsulation'}">
										<td class="borderlessLabel">
											<strong>Localization</strong>&nbsp;
											<br>
										</td>
										<td class="rightLabel">
											${linkage.localization}&nbsp;
											<br>
										</td>
									</c:when>
									<c:when test="${agentType eq 'Other'}">
										<td class="borderlessLabel">
											<strong>Other</strong>&nbsp;
											<br>
										</td>
										<td class="borderlessLabel">
											&nbsp;
											<br>
										</td>
									</c:when>
								</c:choose>
							</tr>
						</c:otherwise>
					</c:choose>
				</table>
				<br>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<Strong>Linkage Description</Strong>
				<br>
			</td>
			<td class="rightLabel" colspan="3">
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
			<td class="leftLabel" width="15%">
				<strong>Agent Type</strong>
				<br>
			</td>
			<td class="rightLabel" colspan="3">
				<table cellspacing="0" cellpadding="3" width="100%" align="center"
					summary="" border="0">
					<c:choose>
						<%-- read and write view --%>
						<c:when test="${canUserSubmit eq 'true'}">
							<c:forEach var="agentType" items="${allAgentTypes[submitType]}"
								varStatus="status">
								<tr>
									<td class="borderlessLabel">
										<html:radio
											property="function.linkages[${param.linkageInd}].agent.type"
											value="${agentType}"
											onclick="javascript:clearOtherAgents('${agentType}', 'function.linkages[${param.linkageInd}]')" />
										${agentType}
										<br>
									</td>
									<c:choose>
										<c:when test="${agentType eq 'Peptide'}">
											<td class="borderlessLabel" colspan="2">
												<strong>Sequence</strong>
												<html:text
													property="function.linkages[${param.linkageInd}].peptide.sequence"
													size="50" />
												<br>
											</td>
										</c:when>
										<c:when test="${agentType eq 'DNA'}">
											<td class="borderlessLabel" colspan="2">
												<strong>Sequence</strong>
												<html:text
													property="function.linkages[${param.linkageInd}].dna.sequence"
													size="50" />
												<br>
											</td>
										</c:when>
										<c:when test="${agentType eq 'Small Molecule'}">
											<td class="borderlessLabel">
												<strong>Name</strong>
												<html:text
													property="function.linkages[${param.linkageInd}].smallMolecule.name" />
												<br>
											</td>
											<td class="borderlessLabel">
												<strong>Compound Name</strong>
												<html:text
													property="function.linkages[${param.linkageInd}].smallMolecule.compoundName" />
												<br>
											</td>
										</c:when>
										<c:when test="${agentType eq 'Antibody'}">
											<td class="borderlessLabel">
												<strong>Name</strong>
												<html:text
													property="function.linkages[${param.linkageInd}].antibody.name" />
												<br>
											</td>
											<td class="borderlessLabel">
												<strong>Species</strong>
												<html:select
													property="function.linkages[${param.linkageInd}].antibody.species">
													<option value=""></option>
													<html:options name="allSpecies" />
												</html:select>
												<br>
											</td>
										</c:when>
										<c:when test="${agentType eq 'Probe'}">
											<td class="borderlessLabel">
												<strong>Name</strong>
												<html:text
													property="function.linkages[${param.linkageInd}].probe.name" />
												<br>
											</td>
											<td class="borderlessLabel">
												<strong>Type</strong>
												<html:text
													property="function.linkages[${param.linkageInd}].probe.type" />
												&nbsp;
												<br>
											</td>
										</c:when>
										<c:when test="${agentType eq 'Image Contrast Agent'}">
											<td class="borderlessLabel">
												<strong>Name</strong>
												<html:text
													property="function.linkages[${param.linkageInd}].imageContrastAgent.name" />
												<br>
											</td>
											<td class="borderlessLabel">
												<strong>Type</strong>
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
												<br>
											</td>
										</c:when>
									</c:choose>
								</tr>
							</c:forEach>
						</c:when>

						<%--  read only view --%>
						<c:otherwise>
							<tr>
								<td class="borderlessLabel">
									${linkage.agent.type}&nbsp;
									<br>
								</td>
								<c:choose>
									<c:when test="${linkage.agent.type eq 'Peptide'}">
										<td class="borderlessLabel" colspan="2">
											<strong>Sequence</strong> ${linkage.peptide.sequence}&nbsp;
											<br>
										</td>
									</c:when>
									<c:when test="${linkage.agent.type eq 'DNA'}">
										<td class="borderlessLabel" colspan="2">
											<strong>Sequence</strong> ${linkage.dna.sequence}&nbsp;
											<br>
										</td>
									</c:when>
									<c:when test="${linkage.agent.type eq 'Small Molecule'}">
										<td class="borderlessLabel">
											<strong>Name</strong> ${linkage.smallMolecule.name}&nbsp;
											<br>
										</td>
										<td class="borderlessLabel">
											<strong>Compound Name</strong>${linkage.smallMolecule.compoundName}&nbsp;
											<br>
										</td>
									</c:when>
									<c:when test="${linkage.agent.type eq 'Antibody'}">
										<td class="borderlessLabel">
											<strong>Name</strong>${linkage.antibody.name}&nbsp;
											<br>
										</td>
										<td class="borderlessLabel">
											<strong>Species</strong> ${linkage.antibody.species}&nbsp;
											<br>
										</td>
									</c:when>
									<c:when test="${linkage.agent.type eq 'Probe'}">
										<td class="borderlessLabel">
											<strong>Name</strong> ${linkage.probe.name}&nbsp;
											<br>
										</td>
										<td class="borderlessLabel">
											${linkage.probe.type}&nbsp;
											<br>
										</td>
									</c:when>
									<c:when test="${linkage.agent.type eq 'Image Contrast Agent'}">
										<td class="borderlessLabel">
											<strong>Name</strong>
											${linkage.imageContrastAgent.name}&nbsp;
											<br>
										</td>
										<td class="borderlessLabel">
											<strong>Contrast Agent Type</strong>
											${linkage.imageContrastAgent.type}&nbsp;
											<br>
										</td>
									</c:when>
								</c:choose>
							</tr>
						</c:otherwise>
					</c:choose>
				</table>
				<br>
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
