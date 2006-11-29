<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/nanoparticleFunction">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Particle ${submitType} Function
				</h4>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_nanoparticle')" class="helpText">Help</a>
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
									<c:when test="${canUserUpdateParticle eq 'true'}">
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
									<c:when test="${canUserUpdateParticle eq 'true'}">
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
												<td class="label" colspan="3">
													<table cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
														<tr>
															<td class="borderlessLabel">
																<c:choose>
																	<c:when test="${canUserUpdateParticle eq 'true'}">
																		<html:radio property="function.linkages[${linkageInd}].type" value="Attachment" />
																Attachement
																</c:when>
																	<c:otherwise>
																${linkage.type}
																</c:otherwise>
																</c:choose>
															</td>
															<td class="borderlessLabel">
																<strong>Bond Type</strong>
																<c:choose>
																	<c:when test="${canUserUpdateParticle eq 'true'}">
																		<html:text property="function.linkages[${linkageInd}].value" /> &nbsp;				
																	</c:when>
																	<c:otherwise>
																		${linkage.value}&nbsp;
																	</c:otherwise>
																</c:choose>
															</td>
														</tr>
														<tr>
															<td class="borderlessLabel">
																<c:choose>
																	<c:when test="${canUserUpdateParticle eq 'true'}">
																		<html:radio property="function.linkages[${linkageInd}].type" value="Encapsulation" />
																Encapsulation
																</c:when>
																	<c:otherwise>
																${linkage.type}
																</c:otherwise>
																</c:choose>
															</td>
															<td class="borderlessLabel">
																<strong>Localization</strong>
																<c:choose>
																	<c:when test="${canUserUpdateParticle eq 'true'}">
																		<html:text property="function.linkages[${linkageInd}].value" /> &nbsp;
														</c:when>
																	<c:otherwise>
															${linkage.value}&nbsp;
														</c:otherwise>
																</c:choose>
															</td>
														</tr>
													</table>
												</td>
											</tr>

											<tr>
												<td class="leftLabel">
													<Strong>Linkage Description</Strong>
												</td>
												<td class="rightLabel" colspan="3">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:textarea property="function.linkages[${linkageInd}].description" rows="3" />
														</c:when>
														<c:otherwise>
															${linkage.description}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
											</tr>

											<tr>
												<td class="leftLabel">
													<strong>Agent Type</strong>
												</td>
												<td class="rightLabel" colspan="3">
													<table cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
														<c:forEach var="agentType" items="${allAgentTypes[submitType]}" varStatus="status">
															<tr>
																<td class="borderlessLabel">
																	<c:choose>
																		<c:when test="${canUserUpdateParticle eq 'true'}">
																			<html:radio property="function.linkages[${linkageInd}].agent.type" value="${agentType}" />
																	${agentType}
																		</c:when>
																		<c:otherwise>
															${linkage.agent.type}&nbsp;
														</c:otherwise>
																	</c:choose>
																	&nbsp;&nbsp;&nbsp;
																</td>
																<td class="borderlessLabel">
																	<c:choose>
																		<c:when test="${agentType eq 'Peptide' || agentType eq 'DNA'}">
																			<strong>Sequence</strong>
																			<c:choose>
																				<c:when test="${canUserUpdateParticle eq 'true'}">
																					<html:text property="function.linkages[${linkageInd}].agent.otherValue" />
																				</c:when>
																				<c:otherwise>
															${linkage.agent.otherValue}&nbsp;
														</c:otherwise>
																			</c:choose>
																		</c:when>
																		<c:otherwise>
																			<strong>Name</strong>
																			<c:choose>
																				<c:when test="${canUserUpdateParticle eq 'true'}">
																					<html:text property="function.linkages[${linkageInd}].agent.name" />
																				</c:when>
																				<c:otherwise>
															${linkage.agent.name}&nbsp;
														</c:otherwise>
																			</c:choose>
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="borderlessLabel">
																	<c:choose>
																		<c:when test="${canUserUpdateParticle eq 'true'}">
																			<c:choose>
																				<c:when test="${agentType eq 'Small Molecule'}">
																					<strong>Compound Name</strong>
																				</c:when>
																				<c:when test="${agentType eq 'Probe'}">
																					<strong>Probe Type</strong>
																				</c:when>
																				<c:when test="${agentType eq 'Antibody'}">
																					<strong>Species</strong>
																				</c:when>
																				<c:when test="${agentType eq 'Image Contrast Agent'}">
																					<strong>Contrast Agent Type</strong>
																				</c:when>
																			</c:choose>
																			<html:text property="function.linkages[${linkageInd}].agent.otherValue" />
																		</c:when>
																		<c:otherwise>																		
																			${linkage.agent.otherValue}" /> &nbsp;
																		</c:otherwise>
																	</c:choose>
																</td>
															</tr>
														</c:forEach>
													</table>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<Strong>Agent Description</Strong>
												</td>
												<td class="rightLabel" colspan="3">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:textarea property="function.linkages[${linkageInd}].agent.description" rows="3" />
														</c:when>
														<c:otherwise>
															${linkage.agent.description}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
											</tr>

											<tr>
												<td class="leftLabel">
													<strong>Number of Agent Targets </strong>
												</td>
												<td class="label">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:text property="function.linkages[${linkageInd}].agent.numberOfAgentTargets" />
														</c:when>
														<c:otherwise>
										${linkage.agent.numberOfAgentTargets}&nbsp;
									</c:otherwise>
													</c:choose>
												</td>
												<td class="rightLabel" colspan="2">
													&nbsp;
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<input type="button" onclick="javascript:updateAgentTargets('${linkageInd}')" value="Update Agent Targets">
														</c:when>
													</c:choose>
												</td>
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
																			<c:when test="${canUserUpdateParticle eq 'true'}">
																				<html:select property="function.linkages[${linkageInd}].agent.agentTargets[${tIndex}].type">
																					<c:forEach var="agentTargetType" items="${allAgentTargetTypes[linkage.agent.type]}">
																						<html:option value="${agentTargetType}" />
																					</c:forEach>
																				</html:select>
																			</c:when>
																			<c:otherwise>
																											${linkage.agent.type}&nbsp;
									</c:otherwise>
																		</c:choose>

																	</td>
																	<td class="label">
																		<strong>Target Name</strong>
																	</td>
																	<td class="rightLabel">
																		<c:choose>
																			<c:when test="${canUserUpdateParticle eq 'true'}">
																				<html:text property="function.linkages[${linkageInd}].agent.agentTargets[${tIndex}].name" />
																			</c:when>
																			<c:otherwise>
										${linkage.agent.name}&nbsp;
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
																			<c:when test="${canUserUpdateParticle eq 'true'}">
																				<html:textarea property="function.linkages[${linkageInd}].agent.agentTargets[${tIndex}].description" rows="3" />
																			</c:when>
																			<c:otherwise>
										${linkage.agent.description}&nbsp;
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
