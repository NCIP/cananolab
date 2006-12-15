<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.calab.dto.particle.*"%>

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
						<html:text property="metalParticle.core.chemicalName" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.metalParticle.core.chemicalName}&nbsp;
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
						<html:textarea property="metalParticle.core.description" rows="3" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.metalParticle.core.description}&nbsp;
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
					Shell and Coating Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Shells</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="metalParticle.numberOfShells" size="3"/>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.metalParticle.numberOfShells}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Number of Coatings</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="metalParticle.numberOfCoatings" size="3"/>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.metalParticle.numberOfCoatings}&nbsp;
					</c:otherwise>
				</c:choose>
				&nbsp;&nbsp;&nbsp;
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<input type="button" onclick="javascript:updateComposition()" value="Update Shells and Coatings">
					</c:when>
				</c:choose>
				<br>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="metalParticle.shells" items="${nanoparticleCompositionForm.map.metalParticle.shells}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="4">
									<div align="justify">
										Shell ${status.index+1}
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
											<html:text name="metalParticle.shells" indexed="true" property="chemicalName" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.metalParticle.shells[status.index].chemicalName}&nbsp;
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
											<html:textarea name="metalParticle.shells" indexed="true" property="description" rows="3" cols="80" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.metalParticle.shells[status.index].description}&nbsp;
					</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
					<br>
					<c:choose>
						<c:when test="${canUserUpdateParticle eq 'true'}">
							<html:hidden name="metalParticle.shells" indexed="true" property="elementType" value="shell" />
						</c:when>
					</c:choose>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="metalParticle.coatings" items="${nanoparticleCompositionForm.map.metalParticle.coatings}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="4">
									<div align="justify">
										Coating ${status.index+1}
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
											<html:text name="metalParticle.coatings" indexed="true" property="chemicalName" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.metalParticle.coatings[status.index].chemicalName}&nbsp;
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
											<html:textarea name="metalParticle.coatings" indexed="true" property="description" rows="3" cols="80" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.metalParticle.coatings[status.index].description}&nbsp;
					</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
					<br>
					<c:choose>
						<c:when test="${canUserUpdateParticle eq 'true'}">
							<html:hidden name="metalParticle.coatings" indexed="true" property="elementType" value="coating" />
						</c:when>
					</c:choose>
				</c:forEach>
			</td>
		</tr>
</table>

