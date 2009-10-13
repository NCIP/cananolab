<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<c:choose>
			<c:when test="${! empty charBean.solubility.solvent}">
				<table class="summaryViewLayer4" align="center" width="95%">
					<tr>
						<th>
							Solvent
						</th>
						<th>
							Is Soluble?
						</th>
						<th>
							Critical Concentration
						</th>
					</tr>
					<tr>
						<td>
							${charBean.solubility.solvent}
						</td>
						<td>
							${charBean.solubility.isSoluble}
						</td>
						<td>
							${charBean.solubility.criticalConcentration}
							${charBean.solubility.criticalConcentrationUnit}
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>N/A
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="4">
					Solubility Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					Solvent
				</td>
				<td>
					<div id="solventPrompt">
						<select name="achar.solubility.solvent" id="solvent"
							onchange="javascript:callPrompt('Solvent', 'solvent', 'solventPrompt');">
							<option value=""></option>
							<c:forEach var="type" items="${solventTypes}">
								<c:choose>
									<c:when
										test="${type eq characterizationForm.map.achar.solubility.solvent}">
										<option value="${type}" selected="selected">${type}</option>
									</c:when>
									<c:otherwise>
										<option value="${type}"/>${type}</option>
									</c:otherwise>
								</c:choose>						
							</c:forEach>
							<option value="other">
								[other]
							</option>
						</select>
					</div>
				</td>
				<td class="cellLabel">
					Is Soluble
				</td>
				<td>
					<c:choose>
						<c:when
							test="${empty characterizationForm.map.achar.isSoluble}">
							<c:set var="selectNoneStr" value='selected="selected"' />
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when
									test="${characterizationForm.map.achar.isSoluble eq 'true'}">
									<c:set var="selectYesStr" value='selected="selected"' />
								</c:when>
								<c:otherwise>
									<c:set var="selectNoStr" value='selected="selected"' />
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					<select name="achar.isSoluble">
						<option value="" ${selectNoneStr}></option>
						<option value="true" ${selectYesStr}>Yes</option>
						<option value="false" ${selectNoStr}>No</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					Critical Concentration
				</td>
				<td>
					<input type="text" name="achar.solubility.criticalConcentration"
						id="criticalConcentration"
						onkeydown="return filterFloatNumber(event)"
						value="${characterizationForm.map.achar.solubility.criticalConcentration}" />
					<div id="concentrationUnitPrompt">
						<select name="achar.solubility.criticalConcentrationUnit"
							id="concentrationUnit"
							onchange="callPrompt('Concentration Unit', 'concentrationUnit', 'concentrationUnitPrompt')">
							<option value=""></option>
							<c:forEach var="unit" items="${concentrationUnits}">
								<c:choose>
									<c:when
										test="${unit eq characterizationForm.map.achar.solubility.criticalConcentrationUnit}">
										<option value="${unit}" selected="selected">
											${unit}
										</option>
									</c:when>
									<c:otherwise>
										<option value="${unit}">
											${unit}
										</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<option value="other">
								[other]
							</option>
						</select>
					</div>
				</td>
				<td colspan="2">
					&nbsp;
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>
