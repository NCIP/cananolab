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
				<strong>Growth Diameter</strong>
			</td>
			<td class="label">
				<html:text property="carbonNanotube.growthDiameter" />
			</td>
			<td class="label">
				<strong>Chirality </strong>
			</td>
			<td class="rightLabel">
				<html:text property="carbonNanotube.chirality" />
			</td>

		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Average Length</strong>
			</td>
			<td class="label" align="left">
				<html:text property="carbonNanotube.averageLength" />
			</td>
			<td class="label">
				<strong>Wall Type</strong>
			</td>
			<td class="rightLabel">
				<html:select property="carbonNanotube.wallType">
					<option value="single">
						Single
					</option>
					<option value="double">
						Double
					</option>
					<option value="multiple">
						Multiple
					</option>
				</html:select>
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
				<html:text property="carbonNanotube.numberOfElements" />
			</td>
			<td class="rightLabel" colspan="2">
				<input type="button" onclick="javascript:updateComposition()" value="Update Modifications">
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="carbonNanotube.element" items="${nanoparticleCompositionForm.map.carbonNanotube.composingElements}" varStatus="status">
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
									<html:text name="carbonNanotube.element" indexed="true" property="elementType" />
								</td>
								<td class="label">
									<strong>Chemical Name</strong>
								</td>
								<td class="rightLabel">
									<html:text name="carbonNanotube.element" indexed="true" property="chemicalName" />									
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									<html:textarea name="carbonNanotube.element" indexed="true" property="description" rows="3" />
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
</table>

