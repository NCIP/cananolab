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
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="surface.surfaceArea" />&nbsp;sq nm
										<!-- <html:select property="surface.surfaceAreaUnit">
											<html:options name="allAreaMeasureUnits" />
										</html:select> -->
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
					<c:when test="${canUserSubmit eq 'true'}">
						<html:select property="surface.isHydrophobic">
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
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="surface.charge" />&nbsp;
										<html:select property="surface.chargeUnit">
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
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="surface.zetaPotential" />&nbsp;mV
									</c:when>
					<c:otherwise>
										${nanoparticleCharacterizationForm.map.surface.zetaPotential}&nbsp;
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
					Surface Chemistry
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Surface Chemistry</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="surface.numberOfSurfaceChemistries" />
					</c:when>
					<c:otherwise>
						${nanoparticleCharacterizationForm.map.surface.numberOfSurfaceChemistries}&nbsp;
									</c:otherwise>
				</c:choose>
			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<input type="button"
							onclick="javascript:updateSurfaceChemistries(this.form, 'nanoparticleSurface')"
							value="Update Surface Chemistries">
					</c:when>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="achar.surfaceChemistries"
					items="${nanoparticleCharacterizationForm.map.surface.surfaceChemistries}"
					varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3"
						width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="4">
									<div align="justify">
										Surface Chemistry ${status.index+1}
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Molecule</strong>
								</td>
								<td class="label">
									<c:choose>
										<c:when test="${canUserSubmit eq 'true'}">
											<html:text name="achar.surfaceChemistries" indexed="true"
												property="moleculeName" />
										</c:when>
										<c:otherwise>
						${nanoparticleCharacterizationForm.map.surface.surfaceChemistries[status.index].moleculeName}&nbsp;
														</c:otherwise>
									</c:choose>
								</td>
								<td class="label">
									<strong>Number of Molecule </strong>
								</td>
								<td class="rightLabel">
									<c:choose>
										<c:when test="${canUserSubmit eq 'true'}">
											<html:text name="surface.surfaceChemistries" indexed="true"
												property="numberOfMolecules" /> &nbsp;															
														</c:when>
										<c:otherwise>
															${nanoparticleCharacterizationForm.map.surface.surfaceChemistries[status.index].numberOfMolecules}&nbsp;
														</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
				</c:forEach>
			</td>
		</tr>
</table>
<br>
