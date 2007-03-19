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
				${nanoparticleCompositionForm.map.quantumDot.core.chemicalName}&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Description</strong>
			</td>
			<td class="rightLabel" colspan="3">
				${nanoparticleCompositionForm.map.quantumDot.core.description}&nbsp;
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
				${nanoparticleCompositionForm.map.quantumDot.numberOfShells}&nbsp;
			</td>
			<td class="label">
				<strong>Number of Coatings</strong>
			</td>
			<td class="rightLabel">
				${nanoparticleCompositionForm.map.quantumDot.numberOfCoatings}&nbsp; &nbsp;&nbsp;&nbsp;
				<br>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="quantumDot.shells" items="${nanoparticleCompositionForm.map.quantumDot.shells}" varStatus="status">
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
									${nanoparticleCompositionForm.map.quantumDot.shells[status.index].chemicalName}&nbsp;
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									${nanoparticleCompositionForm.map.quantumDot.shells[status.index].description}&nbsp;
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
				<c:forEach var="quantumDot.coatings" items="${nanoparticleCompositionForm.map.quantumDot.coatings}" varStatus="status">
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
									${nanoparticleCompositionForm.map.quantumDot.coatings[status.index].chemicalName}&nbsp;
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									${nanoparticleCompositionForm.map.quantumDot.coatings[status.index].description}&nbsp;
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
</table>

