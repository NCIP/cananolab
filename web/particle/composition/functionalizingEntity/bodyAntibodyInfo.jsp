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
					Antibody Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Type</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:select property="entity.antibody.type"
							styleId="antibodyType"
							onchange="javascript:callPrompt('Type', 'antibodyType');">
							<option value="" />
								<html:options name="antibodyTypes" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</c:when>
					<c:otherwise>
						${functionalizingEntityForm.map.entity.antibody.type}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Isotype</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:select property="entity.antibody.isotype"
							styleId="antibodyIsotype"
							onchange="javascript:callPrompt('Isotype', 'antibodyIsotype');">
							<option value="" />
								<html:options name="antibodyIsotypes" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</c:when>
					<c:otherwise>
						${functionalizingEntityForm.map.entity.antibody.isotype}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Species</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:select property="entity.antibody.species"
							styleId="antibodySpecies"
							onchange="javascript:callPrompt('Species', 'species');">
							<option value="" />
								<html:options name="antibodySpecies" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</c:when>
					<c:otherwise>
						${functionalizingEntityForm.map.entity.antibody.species}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>