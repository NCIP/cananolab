<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<br>
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
				<strong>Growth Diameter</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'  && isRemote eq 'false'}">
						<html:text property="carbonNanotube.growthDiameter" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.carbonNanotube.growthDiameter}&nbsp;
					</c:otherwise>
				</c:choose>
				&nbsp;nm
			</td>
			<td class="label">
				<strong>Chirality </strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
						<html:text property="carbonNanotube.chirality" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.carbonNanotube.chirality}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>

		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Average Length</strong>
			</td>
			<td class="label" align="left">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
						<html:text property="carbonNanotube.averageLength" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.carbonNanotube.averageLength}&nbsp;
					</c:otherwise>
				</c:choose>
				&nbsp;nm
			</td>
			<td class="label">
				<strong>Wall Type</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
						<html:select property="carbonNanotube.wallType">
							<option value=""></option>
							<html:options name="allCarbonNanotubeWallTypes"/>
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.carbonNanotube.wallType}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>

