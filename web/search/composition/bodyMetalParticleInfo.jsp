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
				${nanoparticleCompositionForm.map.metalParticle.core.chemicalName}&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Description</strong>
			</td>
			<td class="rightLabel" colspan="3">
				${nanoparticleCompositionForm.map.metalParticle.core.description}&nbsp;
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
				${nanoparticleCompositionForm.map.metalParticle.numberOfShells}&nbsp;
			</td>
			<td class="label">
				<strong>Number of Coatings</strong>
			</td>
			<td class="rightLabel">
				${nanoparticleCompositionForm.map.metalParticle.numberOfCoatings}&nbsp; &nbsp;&nbsp;&nbsp;
				<input type="button" onclick="javascript:updateComposition()" value="Update Shells and Coatings">
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
									${nanoparticleCompositionForm.map.metalParticle.shells[status.index].chemicalName}&nbsp;
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									${nanoparticleCompositionForm.map.metalParticle.shells[status.index].description}&nbsp;
								</td>
							</tr>
						</tbody>
					</table>
					<br>
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
									${nanoparticleCompositionForm.map.metalParticle.coatings[status.index].chemicalName}&nbsp;
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									${nanoparticleCompositionForm.map.metalParticle.coatings[status.index].description}&nbsp;
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
</table>

