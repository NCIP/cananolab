<table class="topBorderOnlyTable" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Dendrimer Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Branch</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.dendrimer.branch" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.dendrimer.branch}
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Generation</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.dendrimer.generation" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.dendrimer.generation}
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>