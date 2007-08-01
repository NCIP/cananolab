<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

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
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=particle_function_help')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${nanoparticleFunctionForm.map.particleName}
					(${nanoparticleFunctionForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="bodyFunctionSummary.jsp" />

				<%-- Function Linkage Agent --%>

				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
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
										<input type="button"
											onclick="javascript:updateFunctionLinkages()"
											value="Update Linkages">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<br>
								<logic:iterate id="linkage" name="nanoparticleFunctionForm"
									property="function.linkages" indexId="linkageInd">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3"
										width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														Linkage ${linkageInd+1}
													</div>
													<br>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<strong>Linkage Type </strong>
													<br>
												</td>
												<td class="rightlabel" colspan="3">
													<table cellspacing="0" cellpadding="3" width="100%"
														align="center" summary="" border="0">
														<c:choose>
															<c:when test="${canUserSubmit eq 'true'}">
																<tr>
																	<td class="borderlessLabel">
																		<html:radio
																			property="function.linkages[${linkageInd}].type"
																			value="Attachment"
																			onclick="javascript:disableTextElement(this.form, 'function.linkages[${linkageInd}].localization');enableTextElement(this.form, 'function.linkages[${linkageInd}].bondType');" />
																		Attachment
																		<br>
																	</td>
																	<td class="borderlessLabel">
																		<strong>Bond Type</strong>
																		<html:select
																			property="function.linkages[${linkageInd}].bondType">
																			<option value=""></option>
																			<html:options name="allBondTypes" />
																		</html:select>
																		&nbsp;
																		<br>
																	</td>
																</tr>
																<tr>
																	<td class="borderlessLabel">
																		<html:radio
																			property="function.linkages[${linkageInd}].type"
																			value="Encapsulation"
																			onclick="javascript:disableTextElement(this.form, 'function.linkages[${linkageInd}].bondType');enableTextElement(this.form, 'function.linkages[${linkageInd}].localization');" />
																		Encapsulation
																		<br>
																	</td>
																	<td class="borderlessLabel">
																		<strong>Localization</strong>
																		<html:text
																			property="function.linkages[${linkageInd}].localization" />
																		&nbsp;
																		<br>
																	</td>
																</tr>
																<tr>
																	<td class="borderlessLabel">
																		<html:radio
																			property="function.linkages[${linkageInd}].type"
																			value="Other"
																			onclick="javascript:disableTextElement(this.form, 'function.linkages[${linkageInd}].localization');disableTextElement(this.form, 'function.linkages[${linkageInd}].bondType');" />
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
																property="function.linkages[${linkageInd}].description"
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
													<table cellspacing="0" cellpadding="3" width="100%"
														align="center" summary="" border="0">
														<c:choose>
															<%-- read and write view --%>
															<c:when test="${canUserSubmit eq 'true'}">
																<c:forEach var="agentType"
																	items="${allAgentTypes[submitType]}" varStatus="status">
																	<tr>
																		<td class="borderlessLabel">
																			<html:radio
																				property="function.linkages[${linkageInd}].agent.type"
																				value="${agentType}"
																				onclick="javascript:clearOtherAgents('${agentType}', 'function.linkages[${linkageInd}]')" />
																			${agentType}
																			<br>
																		</td>
																		<c:choose>
																			<c:when test="${agentType eq 'Peptide'}">
																				<td class="borderlessLabel" colspan="2">
																					<strong>Sequence</strong>
																					<html:text
																						property="function.linkages[${linkageInd}].peptide.sequence"
																						size="50" />
																					<br>
																				</td>
																			</c:when>
																			<c:when test="${agentType eq 'DNA'}">
																				<td class="borderlessLabel" colspan="2">
																					<strong>Sequence</strong>
																					<html:text
																						property="function.linkages[${linkageInd}].dna.sequence"
																						size="50" />
																					<br>
																				</td>
																			</c:when>
																			<c:when test="${agentType eq 'Small Molecule'}">
																				<td class="borderlessLabel">
																					<strong>Name</strong>
																					<html:text
																						property="function.linkages[${linkageInd}].smallMolecule.name" />
																					<br>
																				</td>
																				<td class="borderlessLabel">
																					<strong>Compound Name</strong>
																					<html:text
																						property="function.linkages[${linkageInd}].smallMolecule.compoundName" />
																					<br>
																				</td>
																			</c:when>
																			<c:when test="${agentType eq 'Antibody'}">
																				<td class="borderlessLabel">
																					<strong>Name</strong>
																					<html:text
																						property="function.linkages[${linkageInd}].antibody.name" />
																					<br>
																				</td>
																				<td class="borderlessLabel">
																					<strong>Species</strong>
																					<html:select
																						property="function.linkages[${linkageInd}].antibody.species">
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
																						property="function.linkages[${linkageInd}].probe.name" />
																					<br>
																				</td>
																				<td class="borderlessLabel">
																					<strong>Type</strong>
																					<html:text
																						property="function.linkages[${linkageInd}].probe.type" />
																					&nbsp;
																					<br>
																				</td>
																			</c:when>
																			<c:when test="${agentType eq 'Image Contrast Agent'}">
																				<td class="borderlessLabel">
																					<strong>Name</strong>
																					<html:text
																						property="function.linkages[${linkageInd}].imageContrastAgent.name" />
																					<br>
																				</td>
																				<td class="borderlessLabel">
																					<strong>Type</strong>
																					<html:select
																						property="function.linkages[${linkageInd}].imageContrastAgent.type">
																						<option value=""></option>
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
																				<strong>Sequence</strong>
																				${linkage.peptide.sequence}&nbsp;
																				<br>
																			</td>
																		</c:when>
																		<c:when test="${linkage.agent.type eq 'DNA'}">
																			<td class="borderlessLabel" colspan="2">
																				<strong>Sequence</strong>
																				${linkage.dna.sequence}&nbsp;
																				<br>
																			</td>
																		</c:when>
																		<c:when
																			test="${linkage.agent.type eq 'Small Molecule'}">
																			<td class="borderlessLabel">
																				<strong>Name</strong>
																				${linkage.smallMolecule.name}&nbsp;
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
																				<strong>Species</strong>
																				${linkage.antibody.species}&nbsp;
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
																		<c:when
																			test="${linkage.agent.type eq 'Image Contrast Agent'}">
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
																property="function.linkages[${linkageInd}].agent.description"
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
												<c:choose>
													<c:when test="${canUserSubmit eq 'true'}">
														<td class="leftLabel">
															<strong>Number of Agent Targets </strong>
															<br>
														</td>
														<td class="label">
															<br>															
															<html:text
																property="function.linkages[${linkageInd}].agent.numberOfAgentTargets" />
															<br>
															<br>
														</td>
														<td class="rightLabel" colspan="2">
															&nbsp;
															<input type="button"
																onclick="javascript:updateAgentTargets('${linkageInd}')"
																value="Update Agent Targets">
														</td>
													</c:when>
													<c:otherwise>
														<td class="leftLabel" colspan="4">
															<strong>Number of Agent Targets </strong>
															&nbsp;&nbsp;&nbsp;${linkage.agent.numberOfAgentTargets}&nbsp;
														</td>
													</c:otherwise>
												</c:choose>
											</tr>
											<tr>
												<td class="completeLabel" colspan="4">
													<logic:iterate id="target" name="nanoparticleFunctionForm"
														property="function.linkages[${linkageInd}].agent.agentTargets"
														indexId="tIndex">
														<table class="topBorderOnly" cellspacing="0"
															cellpadding="3" width="100%" align="center" summary=""
															border="0">
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
																				<html:select
																					property="function.linkages[${linkageInd}].agent.agentTargets[${tIndex}].type">
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
																				<html:text
																					property="function.linkages[${linkageInd}].agent.agentTargets[${tIndex}].name" />
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
																				<html:textarea
																					property="function.linkages[${linkageInd}].agent.agentTargets[${tIndex}].description"
																					rows="3" cols="60" />
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
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<c:choose>
								<c:when test="${canUserSubmit eq 'true'}">
									<table height="32" border="0" align="right" cellpadding="4"
										cellspacing="0">
										<tr>
											<td width="490" height="32">
												<div align="right">
													<input type="reset" value="Reset" onclick="">
													<input type="hidden" name="dispatch" value="create">
													<input type="hidden" name="page" value="2">
													<html:submit />
												</div>
											</td>
										</tr>
									</table>
								</c:when>
							</c:choose>
							<div align="right"></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
