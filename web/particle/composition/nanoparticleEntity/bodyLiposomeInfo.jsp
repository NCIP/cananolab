<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Liposome Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Polymer Name</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.liposome.polymerName" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.liposome.polymerName}
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Is Polymerized</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true'}">
						<html:text property="entity.liposome.polymerized" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.liposome.polymerized}
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>