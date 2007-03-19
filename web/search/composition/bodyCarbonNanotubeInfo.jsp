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
				<strong>Growth Diameter</strong>
			</td>
			<td class="label">
				${nanoparticleCompositionForm.map.carbonNanotube.growthDiameter}&nbsp; &nbsp;nm
			</td>
			<td class="label">
				<strong>Chirality </strong>
			</td>
			<td class="rightLabel">
				${nanoparticleCompositionForm.map.carbonNanotube.chirality}&nbsp;
			</td>

		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Average Length</strong>
			</td>
			<td class="label" align="left">
				${nanoparticleCompositionForm.map.carbonNanotube.averageLength}&nbsp; &nbsp;nm
			</td>
			<td class="label">
				<strong>Wall Type</strong>
			</td>
			<td class="rightLabel">
				${nanoparticleCompositionForm.map.carbonNanotube.wallType}&nbsp;
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
				${nanoparticleCompositionForm.map.carbonNanotube.numberOfElements}&nbsp;
			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="carbonNanotube.composingElements" items="${nanoparticleCompositionForm.map.carbonNanotube.composingElements}" varStatus="status">
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
									${nanoparticleCompositionForm.map.carbonNanotube.composingElements[status.index].elementType}&nbsp;
								</td>
								<td class="label">
									<strong>Chemical Name</strong>
								</td>
								<td class="rightLabel">
									${nanoparticleCompositionForm.map.carbonNanotube.composingElements[status.index].chemicalName}&nbsp;
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									${nanoparticleCompositionForm.map.carbonNanotube.composingElements[status.index].description}&nbsp;
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
</table>

