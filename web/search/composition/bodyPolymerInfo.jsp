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
				${nanoparticleCompositionForm.map.polymer.crosslinked}&nbsp;
			</td>
			<td class="label">
				<strong>Crosslink Degree</strong>
			</td>
			<td class="rightLabel">
				${nanoparticleCompositionForm.map.polymer.crosslinkDegree}&nbsp; <strong>%</strong>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Initiator </strong>
			</td>
			<td class="label">
				${nanoparticleCompositionForm.map.polymer.initiator}&nbsp;
			</td>
			<td class="rightlabel" colspan="2">
				&nbsp;
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
				${nanoparticleCompositionForm.map.polymer.numberOfElements}&nbsp;
			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
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
									${nanoparticleCompositionForm.map.polymer.composingElements[status.index].chemicalName}&nbsp;
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									${nanoparticleCompositionForm.map.polymer.composingElements[status.index].description}&nbsp;
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
</table>
