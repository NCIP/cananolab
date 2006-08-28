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
				<html:select property="fullerene.characterizationSource">
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
				<strong>Growth Diameter</strong>
			</td>
			<td class="label">
				<html:text property="fullerene.growthDiameter" />
			</td>
			<td class="label">
				<strong>Chirality </strong>
			</td>
			<td class="rightLabel">
				<html:text property="fullerene.chirality" />
			</td>

		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Average Length</strong>
			</td>
			<td class="label" align="left">
				<html:text property="fullerene.averageLength" />
			</td>
			<td class="label">
				<strong>wall Type</strong>
			</td>
			<td class="rightLabel">
				<html:select property="fullerene.wallType">
					<option value="single">
						Single
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
					Non-Carbon Atom Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Non-Carbon Atoms</strong>
			</td>
			<td class="label">
				<html:text property="fullerene.numberOfElements" />
			</td>
			<td class="rightLabel" colspan="2">
				<input type="button" onclick="javascript:updateComposition()" value="Update Non-Carbon Atoms">
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="fullerene.element" items="${nanoparticleCompositionForm.map.fullerene.composingElements}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="4">
									<div align="justify">
										Non-Carbon Atoms ${status.index+1}
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Chemical Name</strong>
								</td>
								<td class="label">
									<html:text name="fullerene.element" indexed="true" property="chemicalName" />
								</td>
								<td class="label">
									<strong>Percent Molecular Weight</strong>
								</td>
								<td class="rightLabel">
									<html:text name="fullerene.element" indexed="true" property="percentMolecularWeight" />
									%
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									<html:textarea name="fullerene.element" indexed="true" property="description" rows="3" />
								</td>
							</tr>
						</tbody>
					</table>
					<br>
					<html:hidden name="fullerene.element" indexed="true" property="elementType" value="non-carbon atom" />
				</c:forEach>
			</td>
		</tr>
</table>

