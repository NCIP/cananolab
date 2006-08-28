<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					${particleName}
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Characterization Source </strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:select property="dendrimer.characterizationSource">
					<option name="NCL">
						NCL
					</option>
					<option name="vendor">
						Vendor
					</option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Branch</strong>
			</td>
			<td class="label">
				<html:text property="dendrimer.branch" />
			</td>

			<td class="label">
				<strong>Repeat Unit</strong>
			</td>
			<td class="rightLabel">
				<html:text property="dendrimer.repeatUnit" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Generation</strong>
			</td>
			<td class="label">
				<html:text property="dendrimer.generation" />
			</td>
			<td class="label">
				<strong>Molecular Formula</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:text property="dendrimer.molecularFormula" />
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
			<td class="label">
				<html:text property="dendrimer.core.chemicalName" />
			</td>
			<td class="label">
				<strong>Percent Molecular Weight</strong>
			</td>
			<td class="rightLabel">
				<html:text property="dendrimer.core.percentMolecularWeight" />
				%
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Description</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:textarea property="dendrimer.core.description" rows="3" />
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
				<html:text property="dendrimer.numberOfSurfaceGroups" />
			</td>
			<td class="rightLabel" colspan="2">
				<input type="button" onclick="javascript:updateComposition()" value="Update Surface Groups">
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="dendrimer.surfaceGroup" items="${nanoparticleCompositionForm.map.dendrimer.surfaceGroups}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="4">
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
									<html:select name="dendrimer.surfaceGroup" indexed="true" property="name">
										<option />
											<html:options name="allDendrimerSurfaceGroupNames" />
									</html:select>
								</td>
								<td class="label">
									<strong>Modifier</strong>
								</td>
								<td class="rightLabel">
									<html:text name="dendrimer.surfaceGroup" indexed="true" property="modifier" />
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
</table>
