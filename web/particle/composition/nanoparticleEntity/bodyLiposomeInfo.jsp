<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
				<strong>Polymer Name*</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:text property="entity.liposome.polymerName" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.liposome.polymerName}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Is Polymerized</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:select property="entity.liposome.polymerized">
							<option value=""></option>
							<html:options collection="booleanChoices" property="value"
								labelProperty="label" />
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.liposome.polymerized}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>
