<table class="topBorderOnlyTable" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="6">
				<div align="justify">
					Fullerene Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Average Diameter</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.fullerene.averageDiameter"
							styleId="averageDiameter" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.fullerene.averageDiameter}
					</c:otherwise>
			</td>
			<td class="label">
				<strong>Average Diameter Unit</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.fullerene.averageDiameterUnit"
							styleId="averageDiameter" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.fullerene.averageDiameterUnit}
					</c:otherwise>
			</td>
			<td class="label">
				<strong>Number of Carbons</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.fullerene.numberOfCarbon"
							styleId="numberOfCarbon" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.fullerene.numberOfCarbon}
					</c:otherwise>
			</td>
		</tr>
	</tbody>
</table>
<br>