<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="smalltable2" cellspacing="0" width="80%"
	align="center">
	<tbody>
		<tr>
			<td class="leftLabel">
				<strong>Branch</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when
						test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:text property="entity.dendrimer.branch" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.dendrimer.branch}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Generation</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when
						test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:text property="entity.dendrimer.generation" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.dendrimer.generation}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>
