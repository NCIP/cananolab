<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="entity"
	value="${compositionForm.map.comp.nanoparticleEntities[param.entityIndex]}" />

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify" id="compEleInfoTitle">
					Composing Element Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<table border="0" width="100%">
					<tr>
						<td></td>
						<td>
							<table class="topBorderOnly" cellspacing="0" cellpadding="3"
								width="100%" align="center" summary="" border="0">
								<tbody>
									<tr>
										<td class="leftLabelWithTop" valign="top">
											<strong>Composing Element Type</strong>
										</td>
										<td class="labelWithTop" valign="top">
											<strong>Chemical Name</strong>
										</td>
										<td class="labelWithTop" valign="top">
											<strong>Description</strong>
										</td>
										<td class="labelWithTop" valign="top">
											<strong>Molecular Formula Type</strong>
										</td>
										<td class="labelWithTop" valign="top">
											<strong>Molecular Formula</strong>
										</td>
										<td class="labelWithTop" valign="top">
											<strong>Amount</strong>
										</td>
										<td class="labelWithTop" valign="top">
											<strong>Amount Unit</strong>
										</td>
										<td class="rightLabelWithTop" valign="top">
											<strong>Inherent Functions</strong>
										</td>
									</tr>
									<logic:iterate name="nanoparticleEntityForm"
										property="entity.composingElements" id="composingElement"
										indexId="ind">
										<tr>
											<td class="leftLabel" valign="top">
												${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.type}&nbsp;
											</td>
											<td class="label" valign="top">
												${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.name}&nbsp;
					`						</td>
											<td class="label" valign="top" width="15%">
												${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.description}&nbsp;
											</td>
											<td class="label" valign="top">
												${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.molecularFormulaType}&nbsp;
											</td>
											<td class="label" valign="top" width="15%">
												${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.molecularFormula}&nbsp;
											</td>
											<td class="label" valign="top">
												${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.value}&nbsp;
											</td>

											<td class="label" valign="top">
												${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.valueUnit}&nbsp;
											</td>
											<td colspan="4" class="rightLabel">
												<c:if
													test="${! empty nanoparticleEntityForm.map.entity.composingElements[ind].inherentFunctions}">
													<jsp:include
														page="/particle/composition/nanoparticleEntity/bodyFunctionReadOnly.jsp">
														<jsp:param name="compEleInd" value="${ind}" />
													</jsp:include>
												</c:if>
												&nbsp;
											</td>
										</tr>
									</logic:iterate>
								</tbody>
							</table>
							<br>

						</td>
					</tr>
				</table>
			</td>
		</tr>
</table>