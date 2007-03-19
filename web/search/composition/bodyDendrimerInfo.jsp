<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script language="JavaScript">
<!--

//-->
</script>

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
				${nanoparticleCompositionForm.map.dendrimer.branch}&nbsp;
			</td>
			<td class="label">
				&nbsp;
			</td>
			<td class="label">
				&nbsp;
			</td>
			<td class="label">
				<strong>Repeat Unit</strong>
			</td>
			<td class="rightLabel">
				${nanoparticleCompositionForm.map.dendrimer.repeatUnit}&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Generation</strong>
			</td>
			<td class="label">
				${nanoparticleCompositionForm.map.dendrimer.generation}&nbsp;
			</td>
			<td class="label">
				&nbsp;
			</td>
			<td class="label">
				&nbsp;
			</td>
			<td class="label">
				<strong>Molecular Formula</strong>
			</td>
			<td class="rightLabel">
				${nanoparticleCompositionForm.map.dendrimer.molecularFormula}&nbsp;
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
				${nanoparticleCompositionForm.map.dendrimer.core.chemicalName}&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Description</strong>
			</td>
			<td class="rightLabel" colspan="3">
				${nanoparticleCompositionForm.map.dendrimer.core.description}&nbsp;
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
				${nanoparticleCompositionForm.map.dendrimer.numberOfSurfaceGroups}&nbsp;
			</td>
			<td class="rightLabel" colspan="2">
				&nbsp;
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
									${nanoparticleCompositionForm.map.dendrimer.surfaceGroups[status.index].name}&nbsp;
								</td>
								<td class="label">
									&nbsp;
								</td>
								<td class="label">
									&nbsp;
								</td>
								<td class="label">
									<strong>Modifier</strong>
								</td>
								<td class="rightLabel">
									${nanoparticleCompositionForm.map.dendrimer.surfaceGroups[status.index].modifier}&nbsp;
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
</table>
