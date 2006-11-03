<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript">
<!--
function refreshAgent(form, action,index) {
    form.action = form.action + '?dispatch='+action+'&index='+index
    form.submit();
}
//-->
</script>

<html:form action="/nanoparticleFunction">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Particle Function
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
				<c:set var="thisForm" value="${nanoparticleFunctionForm}" />
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="bodyFunctionSummary.jsp" />

				<%-- Function Linkage Agent --%>

				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Agent Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Number of Agent </strong>
							</td>
							<td class="label">
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<html:text property="function.numberOfLinkage" />
									</c:when>
									<c:otherwise>
										${thisForm.map.function.numberOfLinkage}&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td class="rightLabel" colspan="2">
								&nbsp;
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<input type="button" onclick="javascript:updateFunctionLinkage()" value="Update Agents">
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<c:forEach var="function.linkage" items="${thisForm.map.function.linkages}" varStatus="status">
									<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<tbody>
											<tr class="topBorder">
												<td class="formSubTitle" colspan="4">
													<div align="justify">
														Agent ${status.index+1}
													</div>
												</td>
											</tr>
											<tr>
												<td class="leftLabel" rowspan="2">
													<strong>Linkage Type </strong>
												</td>
												<td class="leftlabel">
													<html:radio name="function.linkage" property="type" value="Attachement" />
													Attachement
												</td>
												<td class="label">
													<strong>Bond Type</strong>
												</td>
												<td class="rightLabel" colspan="2">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:text name="function.linkage" indexed="true" property="value" /> &nbsp;				
														</c:when>
														<c:otherwise>
															${thisForm.map.function.linkages[status.index].value}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="leftlabel">
													<html:radio name="function.linkage" property="type" value="Encapsulation" />
													Encapsulation
												</td>
												<td class="label">
													<strong>Localization</strong>
												</td>
												<td class="rightLabel" colspan="2">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:text name="function.linkage" indexed="true" property="value" /> &nbsp;
														</c:when>
														<c:otherwise>
															${thisForm.map.function.linkages[status.index].value}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<Strong>Linkage Description</Strong>
												</td>
												<td class="rightLabel" colspan="3">
													<c:choose>
														<c:when test="${canUserUpdateParticle eq 'true'}">
															<html:textarea name="function.linkage" property="description" rows="3" />
														</c:when>
														<c:otherwise>
															${thisForm.map.function.linkages[status.index].description}&nbsp;
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="leftLabel">
													<strong>Agent Type</strong>
												</td>
												<td class="lable">
													<html:select name="function.linkage" property="agent.type" onchange="javascript:refreshAgent(this.form, 'updateAgentForm', ${status.index})">
														<option />
															<html:options name="allAgentTypes" />
													</html:select>
												</td>
												<logic:notEmpty name="function.linkage" property="agent.type">
													<logic:equal name="function.linkage" property="agent.type" value="DNA">
														<td class="label">
															<strong>Sequence</strong>
														</td>
														<td class="rightLabel">
															<html:text name="function.linkage" indexed="true" property="agent.value"/>
															&nbsp;
														</td>
													</logic:equal>
													<logic:equal name="function.linkage" property="agent.type" value="Peptide">
														<td class="label">
															<strong>Sequence</strong>
														</td>
														<td class="rightLabel">
															<html:text name="function.linkage" indexed="true" property="agent.otherValue"/>
															&nbsp;
														</td>
													</logic:equal>
													<logic:equal name="function.linkage" property="agent.type" value="Samll Molecule">
														<td class="label">
															<strong>Name</strong>
														</td>
														<td class="label">
															<html:text name="function.linkage" indexed="true" property="agent.name"/>
															&nbsp;
														</td>
														<td class="label">
															<strong>Compound Name</strong>
														</td>
														<td class="rightLabel">
															<html:text name="function.linkage" indexed="true" property="agent.otherValue"/>
															&nbsp;
														</td>
													</logic:equal>
													<logic:equal name="function.linkage.agent.type" value="Probe">
														<td class="label">
															<strong>Name</strong>
														</td>
														<td class="label">
															<html:text name="function.linkage" indexed="true" property="agent.name"/>
															&nbsp;
														</td>
														<td class="label">
															<strong>Type</strong>
														</td>
														<td class="rightLabel">
															<html:text name="function.linkage" indexed="true" property="agent.otherValue"/>
															&nbsp;
														</td>
													</logic:equal>
													<logic:equal name="function.linkage.agent.type" value="Antibody">
														<td class="label">
															<strong>Name</strong>
														</td>
														<td class="label">
															<html:text name="function.linkage" indexed="true" property="agent.name"/>													
															&nbsp;
														</td>
														<td class="label">
															<strong>Species</strong>
														</td>
														<td class="rightLabel">
															<html:text name="function.linkage" indexed="true" property="agent.otherValue"/>
															&nbsp;
														</td>
													</logic:equal>
													<logic:equal name="function.linkage.agent.type" value="Image Contrast Agent">
														<td class="label">
															<strong>Name</strong>
														</td>
														<td class="label">
															<html:text name="function.linkage" indexed="true" property="agent.name"/>
															&nbsp;
														</td>
														<td class="label">
															<strong>Type</strong>
														</td>
														<td class="rightLabel">
															<html:text name="function.linkage" indexed="true" property="agent.otherValue"/>
															&nbsp;
														</td>
													</logic:equal>
												</logic:notEmpty>
											</tr>
											<!--   based on the value of function type -->
											
											<!--   end of based on the value of function type -->
										</tbody>
									</table>
								</c:forEach>
							</td>
						</tr>
				</table>

				<%-- end of Function Linkage Agent  --%>
				<jsp:include page="../bodySharedCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>
