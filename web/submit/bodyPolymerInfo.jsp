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
				<strong>Is Crosslinked </strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:select property="polymer.crosslinked">
							<html:options name="booleanChoices"/>
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.polymer.crosslinked}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Crosslink Degree</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="polymer.crosslinkDegree" size="3" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.polymer.crosslinkDegree}&nbsp;
					</c:otherwise>
				</c:choose>
				<strong>%</strong>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Initiator </strong>
			</td>
			<td class="rightLabel" colspan="3">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:select property="polymer.initiator">
							<option />
								<html:options name="allPolymerInitiators" />
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.polymer.initiator}&nbsp;
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
					Monomer Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Monomers</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="polymer.numberOfElements" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.polymer.numberOfElements}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<input type="button" onclick="javascript:updateComposition()" value="Update Monomers">
					</c:when>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="polymer.composingElements" items="${nanoparticleCompositionForm.map.polymer.composingElements}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="4">
									<div align="justify">
										Monomer ${status.index+1}
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
											<html:text name="polymer.composingElements" indexed="true" property="chemicalName" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.polymer.composingElements[status.index].chemicalName}&nbsp;
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
											<html:textarea name="polymer.composingElements" indexed="true" property="description" rows="3" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.polymer.composingElements[status.index].description}&nbsp;
					</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
					<br>
					<c:choose>
						<c:when test="${canUserUpdateParticle eq 'true'}">
							<html:hidden name="polymer.composingElements" indexed="true" property="elementType" value="monomer" />
						</c:when>
					</c:choose>
				</c:forEach>
			</td>
		</tr>
</table>
