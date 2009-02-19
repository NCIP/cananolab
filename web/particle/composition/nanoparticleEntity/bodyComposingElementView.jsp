<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="entity"
	value="${compositionForm.map.comp.nanoparticleEntities[param.entityIndex]}" />

<table cellspacing="0" cellpadding="3"
	width="85%" align="center" border="0">
	<tbody>
		<tr>
			<td colspan="4">
				<table border="0" width="85%">
					<tr>
						<td></td>
						<td>
							<table class="smalltable2" 
								width="85%" align="center" border="1">
								<tbody>
									<tr>
										<td valign="top">
											<strong>Composing Element Type</strong>
										</td>
										<td valign="top">
											<strong>Chemical Name</strong>
										</td>
										<td valign="top">
											<strong>Description</strong>
										</td>
										<td valign="top">
											<strong>Molecular Formula Type</strong>
										</td>
										<td valign="top">
											<strong>Molecular Formula</strong>
										</td>
										<td valign="top">
											<strong>Amount</strong>
										</td>
										<td valign="top">
											<strong>Amount Unit</strong>
										</td>
										<td  valign="top">
											<strong>Inherent Functions</strong>
										</td>
									</tr>
									<logic:iterate name="entity"
										property="composingElements" id="composingElement"
										indexId="ind">
										<tr>
											<td valign="top">
												${composingElement.domainComposingElement.type}&nbsp;
											</td>
											<td valign="top">
												${composingElement.domainComposingElement.name}&nbsp;
											</td>
											<td valign="top" width="15%">
												${composingElement.domainComposingElement.description}&nbsp;
											</td>
											<td valign="top">
												${composingElement.domainComposingElement.molecularFormulaType}&nbsp;
											</td>
											<td valign="top" style="{white-space: pre-wrap; word-wrap: break-word;overflow: hidden;}">
												${composingElement.domainComposingElement.molecularFormula}&nbsp;
											</td>
											<td valign="top">
												${composingElement.domainComposingElement.value}&nbsp;
											</td>

											<td valign="top">
												${composingElement.domainComposingElement.valueUnit}&nbsp;
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