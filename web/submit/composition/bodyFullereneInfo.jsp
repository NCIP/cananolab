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
				<strong>Number of Carbons</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="fullerene.numberOfCarbons" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.fullerene.numberOfCarbons}&nbsp;
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
					Modification Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Modifications</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="fullerene.numberOfElements" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.fullerene.numberOfElements}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<input type="button" onclick="javascript:updateComposition()" value="Update Modifications">
					</c:when>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="fullerene.composingElements" items="${nanoparticleCompositionForm.map.fullerene.composingElements}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="4">
									<div align="justify">
										Modification ${status.index+1}
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Modification Type</strong>
								</td>
								<td class="label">
									<c:choose>
										<c:when test="${canUserSubmit eq 'true'}">
											<html:text name="fullerene.composingElements" indexed="true" property="elementType" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.fullerene.composingElements[status.index].elementType}&nbsp;
					</c:otherwise>
									</c:choose>
								</td>
								<td class="label">
									<strong>Chemical Name</strong>
								</td>
								<td class="rightLabel">
									<c:choose>
										<c:when test="${canUserSubmit eq 'true'}">
											<html:text name="fullerene.composingElements" indexed="true" property="chemicalName" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.fullerene.composingElements[status.index].chemicalName}&nbsp;
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
										<c:when test="${canUserSubmit eq 'true'}">
											<html:textarea name="fullerene.composingElements" indexed="true" property="description" rows="3" cols="80" />
										</c:when>
										<c:otherwise>
						${nanoparticleCompositionForm.map.fullerene.composingElements[status.index].description}&nbsp;
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

