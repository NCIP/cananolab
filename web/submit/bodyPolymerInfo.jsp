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
				<strong>Is Crosslinked </strong>
			</td>
			<td class="label">
				<html:select property="polymer.crosslinked">
					<option value="yes">
						Yes
					</option>
					<option value="no">
						No
					</option>
				</html:select>
			</td>
			<td class="label">
				<strong>Crosslink Degree</strong>
			</td>
			<td class="rightLabel">
				<html:text property="polymer.crosslinkDegree" size="3" />
				<strong>%</strong>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Initiator </strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:select property="polymer.initiator">
					<option />
						<html:options name="allPolymerInitiators" />
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
					Monomer Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Monomers</strong>
			</td>
			<td class="label">
				<html:text property="polymer.numberOfElements" />
			</td>
			<td class="rightLabel" colspan="2">
				<input type="button" onclick="javascript:updateComposition()" value="Update Monomers">
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="polymer.element" items="${nanoparticleCompositionForm.map.polymer.composingElements}" varStatus="status">
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
									<html:text name="polymer.element" indexed="true" property="chemicalName" />
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									<html:textarea name="polymer.element" indexed="true" property="description" rows="3" />
								</td>
							</tr>
						</tbody>
					</table>
					<br>
					<html:hidden name="polymer.element" indexed="true" property="elementType" value="monomer" />
				</c:forEach>
			</td>
		</tr>
</table>
