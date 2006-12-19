<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Composition Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Emulsion Type</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="emulsion.emulsionType" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.emulsion.emulsionType}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>

			<td class="label">
				<strong>Molecular Formula</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="emulsion.molecularFormula" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.emulsion.molecularFormula}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Is Polymerized </strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:select property="emulsion.polymerized">
							<html:options name="booleanChoices"/>
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.emulsion.polymerized}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Polymer Name</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="emulsion.polymerName" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.emulsion.polymerName}&nbsp;
					</c:otherwise>
				</c:choose>				
			</td>
		</tr>
	</tbody>
</table>
