<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Particle Surface Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<div align="justify">
					<strong>Surface Area</strong>
				</div>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="surface.surfaceArea" />&nbsp;
						${nanoparticleCharacterizationForm.map.surface.surfaceAreaUnit}&nbsp;
					</c:when>
					<c:otherwise>
						${nanoparticleCharacterizationForm.map.surface.surfaceArea}&nbsp;
						${nanoparticleCharacterizationForm.map.surface.surfaceAreaUnit}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<div align="justify">
					<strong>isHydrophobic</strong>
				</div>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:select property="surface.isHydrophobic">
							<option value=""></option>
							<html:options name="booleanChoices" />
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCharacterizationForm.map.surface.isHydrophobic}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<div align="justify">
					<strong>Charge</strong>
				</div>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="surface.charge" />&nbsp;
						<html:select property="surface.chargeUnit">
							<option value="" />
								<html:options name="allChargeMeasureUnits" />
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCharacterizationForm.map.surface.charge}&nbsp;
						${nanoparticleCharacterizationForm.map.surface.chargeUnit}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<div align="justify">
					<strong>Zeta Potential</strong>
				</div>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="surface.zetaPotential" />&nbsp;
						${nanoparticleCharacterizationForm.map.surface.zetaPotentialUnit}&nbsp;
					</c:when>
					<c:otherwise>
						${nanoparticleCharacterizationForm.map.surface.zetaPotential}&nbsp;
						${nanoparticleCharacterizationForm.map.surface.zetaPotentialUnit}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>

<br>
<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Surface Chemistry Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<table border="0" width="100%">
					<tr>
						<c:choose>
							<c:when test="${canCreateNanoparticle eq 'true'}">
								<td valign="bottom">
									<a href="#"
										onclick="javascript:addComponent(nanoparticleCharacterizationForm,'${nanoparticleCharacterizationForm.map.achar.actionName}', 'addSurfaceChemistry')"><span
										class="addLink">Add Surface Chemistry</span> </a>
								</td>
							</c:when>
							<c:otherwise>
								<td></td>
							</c:otherwise>
						</c:choose>
						<td>
							<c:forEach var="surface.surfaceChemistries"
								items="${nanoparticleCharacterizationForm.map.surface.surfaceChemistries}"
								varStatus="status">
								<table class="topBorderOnly" cellspacing="0" cellpadding="3"
									width="100%" align="center" summary="" border="0">
									<tbody>
										<tr>
											<c:choose>
												<c:when test="${canCreateNanoparticle eq 'true'}">
													<td class="formSubTitleNoRight" colspan="3">
														Surface Chemistry #${status.index+1}
													</td>
													<td class="formSubTitleNoLeft" align="right">
														<a href="#"
															onclick="javascript:removeComponent(nanoparticleCharacterizationForm, '${nanoparticleCharacterizationForm.map.achar.actionName}', ${status.index}, 'removeSurfaceChemistry')">
															<img src="images/delete.gif" border="0"
																alt="remove this file"> </a>
													</td>
												</c:when>
												<c:otherwise>
													<td class="formSubTitle" colspan="4">
														Surface Chemistry #${status.index+1}
													</td>
												</c:otherwise>
											</c:choose>
										</tr>
										<tr>
											<td class="leftLabel">
												<strong>Number of Molecule* </strong>
											</td>
											<td class="label">
												&nbsp;
												<c:choose>
													<c:when test="${canCreateNanoparticle eq 'true'}">
														<html:text name="surface.surfaceChemistries"
															indexed="true" property="numberOfMolecules" /> &nbsp;															
														</c:when>
													<c:otherwise>
															${nanoparticleCharacterizationForm.map.surface.surfaceChemistries[status.index].numberOfMolecules}&nbsp;
														</c:otherwise>
												</c:choose>
											</td>
											<td class="label">
												<strong>Molecule Name </strong>
											</td>
											<td class="rightLabel">
												&nbsp;
												<c:choose>
													<c:when test="${canCreateNanoparticle eq 'true'}">
														<html:text name="surface.surfaceChemistries"
															indexed="true" property="moleculeName" /> &nbsp;															
														</c:when>
													<c:otherwise>
															${nanoparticleCharacterizationForm.map.surface.surfaceChemistries[status.index].moleculeName}&nbsp;
														</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<td class="leftLabel">
												<strong>Molecular Formula Type</strong>
											</td>
											<td class="label">
												<html:select name="surface.surfaceChemistries"
													indexed="true" property="molecularFormulaType">
													<option />
														<html:options name="allMolecularFormulaTypes" />
												</html:select>
											</td>
											<td class="label">
												<strong>Molecular Formula</strong>
											</td>
											<td class="rightLabel">
												<c:choose>
													<c:when test="${canCreateNanoparticle eq 'true'}">
														<html:text name="surface.surfaceChemistries"
															indexed="true" property="moleculeName" size="30" />
													</c:when>
													<c:otherwise>
						${nanoparticleCharacterizationForm.map.surface.surfaceChemistries[status.index].moleculeName}&nbsp;
														</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</tbody>
								</table>
								<br>
							</c:forEach>
						</td>
					</tr>
				</table>
			</td>
		</tr>
</table>
<br>
