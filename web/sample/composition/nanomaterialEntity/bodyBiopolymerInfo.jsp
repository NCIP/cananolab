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
					Biopolymer Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="LeftLabel">
				<strong>Name*</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:text property="entity.biopolymer.name" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.biopolymer.name}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Biopolymer Type*</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:select property="entity.biopolymer.type"
							styleId="biopolymerType"
							onchange="javascript:callPrompt('Biopolymer Type', 'biopolymerType');">
							<option value=""></option>
							<html:options name="biopolymerTypes" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.biopolymer.type}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Sequence</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<c:choose>
					<c:when test="${canCreateSample eq 'true' && location eq 'local'}">
						<html:textarea property="entity.biopolymer.sequence" cols="80"
							rows="3" />
					</c:when>
					<c:otherwise>
						${nanoparticleEntityForm.map.entity.biopolymer.sequence}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>