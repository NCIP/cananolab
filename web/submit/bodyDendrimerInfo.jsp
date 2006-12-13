<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="6">
				<div align="justify">
					Composition Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Branch</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:select property="dendrimer.branch">
							<option value=""></option>
							<html:options name="allDendrimerBranches" />
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.branch}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Other Branch</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="dendrimer.otherBranch" />
					</c:when>
					<c:otherwise>
						&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Repeat Unit</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="dendrimer.repeatUnit" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.repeatUnit}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Generation</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:select property="dendrimer.generation">
							<option value=""></option>
							<html:options name="allDendrimerGenerations" />
							<option value="Other">
								Other
							</option>
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.generation}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Other Generation</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="dendrimer.otherGeneration" />
					</c:when>
					<c:otherwise>
						&nbsp;
					</c:otherwise>
				</c:choose>
			</td>

			<td class="label">
				<strong>Molecular Formula</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="dendrimer.molecularFormula" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.molecularFormula}&nbsp;
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
					Core Information
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
						<html:text property="dendrimer.core.chemicalName" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.core.chemicalName}&nbsp;
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
						<html:textarea property="dendrimer.core.description" rows="3" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.core.description}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
</table>

<br>
<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Surface Group Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Surface Groups</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="dendrimer.numberOfSurfaceGroups" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.numberOfSurfaceGroups}&nbsp;
					</c:otherwise>
				</c:choose>

			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<input type="button" onclick="javascript:updateComposition()" value="Update Surface Groups">
					</c:when>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="dendrimer.surfaceGroups" items="${nanoparticleCompositionForm.map.dendrimer.surfaceGroups}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="6">
									<div align="justify">
										Surface Group ${status.index+1}
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Name </strong>
								</td>
								<td class="label">
									<c:choose>
										<c:when test="${canUserUpdateParticle eq 'true'}">
											<html:select name="dendrimer.surfaceGroups" indexed="true" property="name">
												<option />
													<html:options name="allDendrimerSurfaceGroupNames" />
												<option value="Other">Other</option>
											</html:select>
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.surfaceGroups[status.index].name}&nbsp;
					</c:otherwise>
									</c:choose>
								</td>
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<td class="label">
											<strong>Other name</strong>
										</td>
										<td class="label">
											<html:text name="dendrimer.surfaceGroups" indexed="true" property="otherName" />
										</td>
									</c:when>
									<c:otherwise>
										<td>
											&nbsp;
										</td>
										<td>
											&nbsp;
										</td>
									</c:otherwise>
									</c:choose>
									<td class="label">
										<strong>Modifier</strong>
									</td>
									<td class="rightLabel">
										<c:choose>
											<c:when test="${canUserUpdateParticle eq 'true'}">
												<html:text name="dendrimer.surfaceGroups" indexed="true" property="modifier" />
											</c:when>
											<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.surfaceGroups[status.index].modifier}&nbsp;
					</c:otherwise>
										</c:choose>
									</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
</table>
