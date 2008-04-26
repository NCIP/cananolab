<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="6">
				<div align="justify">
					Polymer Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Initiator</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.polymer.initiator" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.polymer.initiator}
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Cross Link Degree</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.polymer.crosslinkDegree" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.polymer.crosslinkDegree}
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Is Cross Linked</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.polymer.crosslinked" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.polymer.crosslinked}
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>