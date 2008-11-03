<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="6">
				<div align="justify">
					Solubility Property
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Solvent </strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:select property="achar.solubility.solvent" styleId="solvent"
							onchange="javascript:callPrompt('Solvent', 'solvent');">
							<option value=""></option>
							<html:options name="solventTypes" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</c:when>
					<c:otherwise>
										${characterizationForm.map.achar.solubility.solvent}&nbsp;
									</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Is Soluble </strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:select property="achar.solubility.isSoluble">
							<option value=""></option>
							<html:options collection="booleanChoices" property="value"
								labelProperty="label" />
						</html:select>
					</c:when>
					<c:otherwise>
											${characterizationForm.map.achar.solubility.isSoluble}&nbsp;
									</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Critical Concentration</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:text property="achar.solubility.criticalConcentration" onkeydown="return filterFloatNumber(event)"/>
						<html:select property="achar.solubility.criticalConcentrationUnit">
							<option value=""></option>
							<html:options name="concentrationUnits" />
						</html:select>
					</c:when>
					<c:otherwise>
										${characterizationForm.map.achar.solubility.criticalConcentration}&nbsp;
										${characterizationForm.map.achar.solubility.criticalConcentrationUnit}&nbsp;
									</c:otherwise>
				</c:choose>
			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
			</td>
		</tr>

	</tbody>
</table>
<br />
