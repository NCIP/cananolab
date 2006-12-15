<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Composing Nanoparticle Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Composing Nanoparticles</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<html:text property="complexParticle.numberOfElements" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.complexParticle.numberOfElements}&nbsp;
					</c:otherwise>
				</c:choose>

			</td>
			<td class="rightLabel" colspan="2">&nbsp;
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<input type="button" onclick="javascript:updateComposition()" value="Update Composing Nanoparticles">
					</c:when>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="complexParticle.composingElements" items="${nanoparticleCompositionForm.map.complexParticle.composingElements}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="4">
									<div align="justify">
										Composing Nanoparticle ${status.index+1}
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Composing Nanoparticle Type</strong>
								</td>
								<td class="label">
									<c:choose>
										<c:when test="${canUserUpdateParticle eq 'true'}">
											<html:select name="complexParticle.composingElements" indexed="true" property="elementType">
												<option value=""></option>
												<html:options name="allSampleTypes" />
											</html:select>
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.complexParticle.composingElements[status.index].elementType}&nbsp;
					</c:otherwise>
									</c:choose>
								</td>
								<td class="label">
									<strong>Chemical Name</strong>
								</td>
								<td class="rightLabel">
									<c:choose>
										<c:when test="${canUserUpdateParticle eq 'true'}">
											<html:text name="complexParticle.composingElements" indexed="true" property="chemicalName" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.complexParticle.composingElements[status.index].chemicalName}&nbsp;
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
											<html:textarea name="complexParticle.composingElements" indexed="true" property="description" rows="3" cols="80"/>
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.complexParticle.composingElements[status.index].description}&nbsp;
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
