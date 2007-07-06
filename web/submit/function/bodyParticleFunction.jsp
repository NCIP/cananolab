<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script language="JavaScript">
<!--
function clearOtherAgents(agentType, elementPrefix) {
	var sequenceTypes=new Array('peptide', 'dna');
	var nameTypes=new Array('smallMolecule', 'antibody', 'imageContrastAgent');
	var typeTypes=new Array('imageContrastAgent');
	
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
	else if (agentType=='Image Contrast Agent') {
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.imageContrastAgent.name');
	  enableTextElement(nanoparticleFunctionForm, elementPrefix+'.imageContrastAgent.type');
	}
}
//-->
</script>
<html:form action="/nanoparticleFunction">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Particle ${submitType} Function
					<html:hidden property="function.type" value="${submitType}" />
				</h4>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=particle_function_help')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${nanoparticleFunctionForm.map.particleName} (${nanoparticleFunctionForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="bodyFunctionSummary.jsp" />

				<%-- Function Linkage Agent --%>

				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Linkage Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Number of Linkages </strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="function.numberOfLinkages" />
									</c:when>
									<c:otherwise>
										${nanoparticleFunctionForm.map.function.numberOfLinkages}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<input type="button" onclick="javascript:updateFunctionLinkages()" value="Update Linkages">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<logic:iterate id="linkage" name="nanoparticleFunctionForm" property="function.linkages" indexId="linkageInd">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														Linkage ${linkageInd+1}
													</div>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<strong>Linkage Type </strong>
												</td>
												<td class="rightlabel" colspan="3">
													<table cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
														<c:choose>
															<c:when test="${canUserSubmit eq 'true'}">
																<tr>
																	<td class="borderlessLabel">
																		<html:radio property="function.linkages[${linkageInd}].type" value="Attachment"
																			onclick="javascript:disableTextElement(this.form, 'function.linkages[${linkageInd}].localization');enableTextElement(this.form, 'function.linkages[${linkageInd}].bondType');" />
																		Attachment
																	</td>
																	<td class="borderlessLabel">
																		<strong>Bond Type</strong>
																		<html:text property="function.linkages[${linkageInd}].bondType" />
																		&nbsp;
																	</td>
																</tr>
																<tr>
																	<td class="borderlessLabel">
																		<html:radio property="function.linkages[${linkageInd}].type" value="Encapsulation"
																			onclick="javascript:disableTextElement(this.form, 'function.linkages[${linkageInd}].bondType');enableTextElement(this.form, 'function.linkages[${linkageInd}].localization');" />
																		Encapsulation
																	</td>
																	<td class="borderlessLabel">
																		<strong>Localization</strong>
																		<html:text property="function.linkages[${linkageInd}].localization" />
																		&nbsp;
																	</td>
																</tr>
																<tr>
																	<td class="borderlessLabel">
																		<html:radio property="function.linkages[${linkageInd}].type" value="Other"
																			onclick="javascript:disableTextElement(this.form, 'function.linkages[${linkageInd}].localization');disableTextElement(this.form, 'function.linkages[${linkageInd}].bondType');" />
																		Other
																	</td>
																	<td></td>
																</tr>
															</c:when>
															<%--  read only view --%>
															<c:otherwise>
																<tr>
																	<td class="borderlessLabel">
																		${linkage.type}&nbsp;
																	</td>
																	<c:choose>
																		<c:when test="${linkage.type eq 'Attachment'}">
																			<td class="borderlessLabel">
																				<strong>Bond Type</strong>&nbsp;
																			</td>
																			<td class="borderlessLabel">
																				${linkage.bondType}&nbsp;
																			</td>
																		</c:when>
																		<c:when test="${linkage.type eq 'Encapsulation'}">
																			<td class="borderlessLabel">
																				<strong>Localization</strong>&nbsp;
																			</td>
																			<td class="rightLabel">
																				${linkage.localization}&nbsp;
																			</td>
																		</c:when>
																		<c:when test="${agentType eq 'Other'}">
																			<td class="borderlessLabel">
																				<strong>Other</strong>&nbsp;
																			</td>
																			<td class="borderlessLabel">
																				&nbsp;
																			</td>
																		</c:when>
																	</c:choose>
																</tr>
															</c:otherwise>
														</c:choose>
													</table>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<Strong>Linkage Description</Strong>
												</td>
												<td class="rightLabel" colspan="3">
													<c:choose>
														<c:when test="${canUserSubmit eq 'true'}">
															<html:textarea property="function.linkages[${linkageInd}].description" rows="3" cols="60" />
														</c:when>
														<c:otherwise>
															${linkage.description}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="leftLabel" width="15%">
													<strong>Agent Type</strong>
												</td>
												<td class="rightLabel" colspan="3">
													<table cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
														<c:choose>
															<%-- read and write view --%>
															<c:when test="${canUserSubmit eq 'true'}">
																<c:forEach var="agentType" items="${allAgentTypes[submitType]}" varStatus="status">
																	<tr>
																		<td class="borderlessLabel">
																			<html:radio property="function.linkages[${linkageInd}].agent.type" value="${agentType}" onclick="javascript:clearOtherAgents('${agentType}', 'function.linkages[${linkageInd}]')" />
																			${agentType}
																		</td>
																		<c:choose>
																			<c:when test="${agentType eq 'Peptide'}">
																				<td class="borderlessLabel" colspan="2">
																					<strong>Sequence</strong>
																					<html:text property="function.linkages[${linkageInd}].peptide.sequence" size="50" />
																				</td>
																			</c:when>
																			<c:when test="${agentType eq 'DNA'}">
																				<td class="borderlessLabel" colspan="2">
																					<strong>Sequence</strong>
																					<html:text property="function.linkages[${linkageInd}].dna.sequence" size="50" />
																				</td>
																			</c:when>
																			<c:when test="${agentType eq 'Small Molecule'}">
																				<td class="borderlessLabel">
																					<strong>Name</strong>
																					<html:text property="function.linkages[${linkageInd}].smallMolecule.name" />
																				</td>
																				<td class="borderlessLabel">
																					<strong>Compound Name</strong>
																					<html:text property="function.linkages[${linkageInd}].smallMolecule.compoundName" />
																				</td>
																			</c:when>
																			<c:when test="${agentType eq 'Antibody'}">
																				<td class="borderlessLabel">
																					<strong>Name</strong>
																					<html:text property="function.linkages[${linkageInd}].antibody.name" />
																				</td>
																				<td class="borderlessLabel">
																					<strong>Species</strong>
																					<html:select property="function.linkages[${linkageInd}].antibody.species">
																						<html:options collection="allSpecies" property="value" labelProperty="label" />
																					</html:select>
																				</td>
																			</c:when>
																			<c:when test="${agentType eq 'Image Contrast Agent'}">
																				<td class="borderlessLabel">
																					<strong>Name</strong>
																					<html:text property="function.linkages[${linkageInd}].imageContrastAgent.name" />
																				</td>
																				<td class="borderlessLabel">
																					<strong>Type</strong>
																					<html:text property="function.linkages[${linkageInd}].imageContrastAgent.type" />
																					&nbsp;
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
																	</td>
																	<c:choose>
																		<c:when test="${linkage.agent.type eq 'Peptide'}">
																			<td class="borderlessLabel" colspan="2">
																				<strong>Sequence</strong> ${linkage.peptide.sequence}&nbsp;
																			</td>
																		</c:when>
																		<c:when test="${linkage.agent.type eq 'DNA'}">
																			<td class="borderlessLabel" colspan="2">
																				<strong>Sequence</strong> ${linkage.dna.sequence}&nbsp;
																			</td>
																		</c:when>
																		<c:when test="${linkage.agent.type eq 'Small Molecule'}">
																			<td class="borderlessLabel">
																				<strong>Name</strong> ${linkage.smallMolecule.name}&nbsp;
																			</td>
																			<td class="borderlessLabel">
																				<strong>Compound Name</strong>${linkage.smallMolecule.compoundName}&nbsp;
																			</td>
																		</c:when>
																		<c:when test="${linkage.agent.type eq 'Antibody'}">
																			<td class="borderlessLabel">
																				<strong>Name</strong>${linkage.antibody.name}&nbsp;
																			</td>
																			<td class="borderlessLabel">
																				<strong>Species</strong> ${linkage.antibody.species}&nbsp;
																			</td>
																		</c:when>
																		<c:when test="${linkage.agent.type eq 'Image Contrast Agent'}">
																			<td class="borderlessLabel">
																				<strong>Name</strong> ${linkage.imageContrastAgent.name}&nbsp;
																			</td>
																			<td class="borderlessLabel">
																				<strong>Contrast Agent Type</strong> ${linkage.imageContrastAgent.type}&nbsp;
																			</td>
																		</c:when>
																	</c:choose>
																</tr>
															</c:otherwise>
														</c:choose>
													</table>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<Strong>Agent Description</Strong>
												</td>
												<td class="rightLabel" colspan="3">
													<c:choose>
														<c:when test="${canUserSubmit eq 'true'}">
															<html:textarea property="function.linkages[${linkageInd}].agent.description" rows="3" cols="60" />
														</c:when>
														<c:otherwise>
															${linkage.agent.description}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<c:choose>
													<c:when test="${canUserSubmit eq 'true'}">
														<td class="leftLabel">
															<strong>Number of Agent Targets </strong>
														</td>
														<td class="label">
															<html:text property="function.linkages[${linkageInd}].agent.numberOfAgentTargets" />
														</td>
														<td class="rightLabel" colspan="2">
															&nbsp;
															<input type="button" onclick="javascript:updateAgentTargets('${linkageInd}')" value="Update Agent Targets">
														</td>
													</c:when>
													<c:otherwise>
														<td class="leftLabel" colspan="4">
															<strong>Number of Agent Targets </strong> &nbsp;&nbsp;&nbsp;${linkage.agent.numberOfAgentTargets}&nbsp;
														</td>
													</c:otherwise>
												</c:choose>
											</tr>
											<tr>
												<td class="completeLabel" colspan="4">
													<logic:iterate id="target" name="nanoparticleFunctionForm" property="function.linkages[${linkageInd}].agent.agentTargets" indexId="tIndex">
														<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
															<tbody>
																<tr class="topBorder">
																	<td class="formSubTitle" colspan="4">
																		<div align="justify">
																			Agent Target ${tIndex+1}
																		</div>
																	</td>
																</tr>
																<tr>
																	<td class="leftLabel">
																		<strong>Target Type</strong>
																	</td>
																	<td class="label">
																		<c:choose>
																			<c:when test="${canUserSubmit eq 'true'}">
																				<html:select property="function.linkages[${linkageInd}].agent.agentTargets[${tIndex}].type">
																					<html:options name="allAgentTargetTypes" />																					
																				</html:select>
																			</c:when>
																			<c:otherwise>
																				${linkage.agent.agentTargets[tIndex].type}&nbsp;
																			</c:otherwise>
																		</c:choose>

																	</td>
																	<td class="label">
																		<strong>Target Name</strong>
																	</td>
																	<td class="rightLabel">
																		<c:choose>
																			<c:when test="${canUserSubmit eq 'true'}">
																				<html:text property="function.linkages[${linkageInd}].agent.agentTargets[${tIndex}].name" />
																			</c:when>
																			<c:otherwise>
																				${linkage.agent.agentTargets[tIndex].name}&nbsp;
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
																				<html:textarea property="function.linkages[${linkageInd}].agent.agentTargets[${tIndex}].description" rows="3" cols="60"/>
																			</c:when>
																			<c:otherwise>
																				${linkage.agent.agentTargets[tIndex].description}&nbsp;
																			</c:otherwise>
																		</c:choose>
																	</td>
																</tr>
														</table>
														<br>
													</logic:iterate>
												</td>
											</tr>
										</tbody>
									</table>
									<br>
								</logic:iterate>
							</td>
						</tr>
				</table>
				<%-- end of Function Linkage Agent  --%>
				<jsp:include page="../bodySharedCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
