<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="6">
				<div align="justify">
					Carbon Nanotube Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Average Length</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.carbonNanotube.averageLength" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.carbonNanotube.averageLength}
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Average Length Unit</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.carbonNanotube.averageLengthUnit" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.carbonNanotube.averageLengthUnit}
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Chirality</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.carbonNanotube.chirality" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.carbonNanotube.chirality}
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Growth Diameter</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.carbonNanotube.growthDiameter" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.carbonNanotube.growthDiameter}
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Growth Diameter Unit</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.carbonNanotube.growthDiameterUnit" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.carbonNanotube.growthDiameterUnit}
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Wall Type</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.carbonNanotube.wallType" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.carbonNanotube.wallType}
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>