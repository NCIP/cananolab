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
					<c:when test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:text property="entity.fullerene.averageDiameter"
							styleId="averageDiameter" onkeydown="return filterFloatNumber(event)"/>
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.fullerene.averageDiameter}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Average Diameter Unit</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:select property="entity.fullerene.averageDiameterUnit"
							styleId="averageDiameterUnit"
							onchange="javascript:callPrompt('Average Diameter Unit', 'averageDiameterUnit');">
							<option value=""></option>
							<html:options name="fullereneAverageDiameterUnit" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.fullerene.averageDiameterUnit}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Number of Carbons</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:text property="entity.fullerene.numberOfCarbon"
							styleId="numberOfCarbon" onkeydown="return filterInteger(event)"/>
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.fullerene.numberOfCarbon}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>