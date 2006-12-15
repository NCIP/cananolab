<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Composition Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Is Polymerized </strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:select property="liposome.polymerized">
							<html:options name="booleanChoices" />
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.liposome.polymerized}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Polymer Name</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="liposome.polymerName" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.liposome.polymerName}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>
<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Lipid Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Lipids</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="liposome.numberOfElements" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.liposome.numberOfElements}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<input type="button" onclick="javascript:updateComposition()" value="Update Lipids">
					</c:when>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="liposome.composingElements" items="${nanoparticleCompositionForm.map.liposome.composingElements}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="4">
									<div align="justify">
										Lipid ${status.index+1}
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Chemical Name</strong>
								</td>
								<td class="rightLabel" colspan="3">
									<c:choose>
										<c:when test="${canUserUpdateParticle eq 'true'}">
											<html:text name="liposome.composingElements" indexed="true" property="chemicalName" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.liposome.composingElements[status.index].chemicalName}&nbsp;
					</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									<c:choose>
										<c:when test="${canUserUpdateParticle eq 'true'}">
											<html:textarea name="liposome.composingElements" indexed="true" property="description" rows="3" cols="80" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.liposome.composingElements[status.index].description}&nbsp;
					</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
					<br>
					<c:choose>
						<c:when test="${canUserUpdateParticle eq 'true'}">
							<html:hidden name="liposome.composingElements" indexed="true" property="elementType" value="lipid" />
						</c:when>
					</c:choose>
				</c:forEach>
			</td>
		</tr>
</table>


