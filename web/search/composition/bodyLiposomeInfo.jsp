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
				${nanoparticleCompositionForm.map.liposome.polymerized}&nbsp;
			</td>
			<td class="label">
				<strong>Polymer Name</strong>
			</td>
			<td class="rightLabel">
				${nanoparticleCompositionForm.map.liposome.polymerName}&nbsp;
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
				${nanoparticleCompositionForm.map.liposome.numberOfElements}&nbsp;
			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
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
									${nanoparticleCompositionForm.map.liposome.composingElements[status.index].chemicalName}&nbsp;
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									${nanoparticleCompositionForm.map.liposome.composingElements[status.index].description}&nbsp;
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
</table>


