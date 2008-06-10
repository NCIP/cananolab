<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="2">
				<div align="justify">
					Particle Surface Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<div align="justify">
					<strong>Is Hydrophobic?</strong>
				</div>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:select
							property="achar.surfaceBean.domainSurface.isHydrophobic">
							<option value=""></option>
							<html:options collection="booleanChoices" property="value"
								labelProperty="label" />
						</html:select>
					</c:when>
					<c:otherwise>
						${characterizationForm.map.achar.surfaceBean.domainSurface.isHydrophobic}&nbsp;
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
							<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
								<td valign="bottom">
									<a href="#"
										onclick="javascript:addComponent(document.forms[0],'${actionName}', 'addSurfaceChemistry')"><span
										class="addLink">Add Surface Chemistry</span> </a>
								</td>
							</c:when>
							<c:otherwise>
								<td></td>
							</c:otherwise>
						</c:choose>
						<td>
							<c:forEach var="chemistry"
								items="${characterizationForm.map.achar.surfaceBean.surfaceChemistryList}"
								varStatus="status">
								<table class="topBorderOnly" cellspacing="0" cellpadding="3"
									width="100%" align="center" summary="" border="0">
									<tbody>
										<tr>
											<c:choose>
												<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
													<td class="formSubTitleNoRight" colspan="3">
														Surface Chemistry #${status.index+1}
													</td>
													<td class="formSubTitleNoLeft" align="right">
														<a href="#"
															onclick="javascript:removeComponent(document.forms[0], '${actionName}', ${status.index}, 'removeSurfaceChemistry')">
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
													<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
														<html:text
															onkeydown="return filterInteger(event)"
															property="achar.surfaceBean.surfaceChemistryList[${status.index}].numberOfMolecule" />
															&nbsp;															
														</c:when>
													<c:otherwise>
															${characterizationForm.map.achar.surfaceBean.surfaceChemistryList[status.index].numberOfMolecule}&nbsp;
														</c:otherwise>
												</c:choose>
											</td>
											<td class="label">
												<strong>Molecule Name </strong>
											</td>
											<td class="rightLabel">
												&nbsp;
												<c:choose>
													<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
														<html:text
															property="achar.surfaceBean.surfaceChemistryList[${status.index}].moleculeName" /> &nbsp;															
														</c:when>
													<c:otherwise>
															${characterizationForm.map.achar.surfaceBean.surfaceChemistryList[status.index].moleculeName}&nbsp;
														</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<td class="leftLabel">
												<strong>Molecular Formula Type</strong>
											</td>
											<td class="label">
												<html:select
													property="achar.surfaceBean.surfaceChemistryList[${status.index}].molecularFormulaType">
													<option />
														<html:options name="scMolecularFormulaTypes" />
												</html:select>
											</td>
											<td class="label">
												<strong>Molecular Formula</strong>
											</td>
											<td class="rightLabel">
												<c:choose>
													<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
														<html:text
															property="achar.surfaceBean.surfaceChemistryList[${status.index}].molecularFormula"
															size="30" />
													</c:when>
													<c:otherwise>
						${characterizationForm.map.achar.surfaceBean.surfaceChemistryList[status.index].molecularFormula}&nbsp;
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
