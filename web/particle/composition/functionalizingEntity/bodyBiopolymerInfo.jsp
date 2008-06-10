<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="3">
				<div align="justify">
					Biopolymer Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Biopolymer Type*</strong>
			</td>
			<td class="rightLabel" colspan="2">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
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
						${functionalizingEntityForm.map.entity.biopolymer.type}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Sequence</strong>
			</td>
			<td class="rightLabel" colspan="2">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:textarea property="entity.biopolymer.sequence" cols="80"
							rows="3" />
					</c:when>
					<c:otherwise>
						${functionalizingEntityForm.map.entity.biopolymer.sequence}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>