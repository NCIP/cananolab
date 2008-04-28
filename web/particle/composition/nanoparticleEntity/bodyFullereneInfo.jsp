<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
						${nanoparticleEntityForm.map.entity.fullerene.averageDiameter}&nbsp;
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
						${nanoparticleEntityForm.map.entity.fullerene.averageDiameterUnit}&nbsp;
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
						${nanoparticleEntityForm.map.entity.fullerene.numberOfCarbon}&nbsp;
					</c:otherwise>
			</td>
		</tr>
	</tbody>
</table>
<br>