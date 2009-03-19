<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
					<c:when test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:text property="entity.polymer.initiator" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.polymer.initiator}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Cross Link Degree</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:text property="entity.polymer.crossLinkDegree" onkeydown="return filterFloatNumber(event)"/>
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.polymer.crossLinkDegree}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Is Cross Linked</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:select property="entity.polymer.crossLinked">
							<option value=""></option>
							<html:options collection="booleanChoices" property="value"
								labelProperty="label" />
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.polymer.crossLinked}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>
