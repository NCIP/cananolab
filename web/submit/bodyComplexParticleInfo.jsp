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
				<strong>Name </strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:text property="complexParticle.name" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Description</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:textarea property="complexParticle.description" rows="4" />
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
					Composing Nanoparticle Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Composing Nanoparticles</strong>
			</td>
			<td class="label">
				<html:text property="complexParticle.numberOfElements" />
			</td>
			<td class="rightLabel" colspan="2">
				<input type="button" onclick="javascript:updateComposition()" value="Update Composing Nanoparticles">
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<c:forEach var="complexParticle.element" items="${nanoparticleCompositionForm.map.complexParticle.composingElements}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formSubTitle" colspan="4">
									<div align="justify">
										Composing Nanoparticle ${status.index+1}
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Composing Nanoparticle Type</strong>
								</td>
								<td class="label">									
									<html:select name="complexParticle.element" indexed="true" property="elementType">
										<option value=""></option>
										<html:options name="allParticleTypes" />
									</html:select>
								</td>							
								<td class="label">
									<strong>Chemical Name</strong>
								</td>
								<td class="rightLabel">
									<html:text name="complexParticle.element" indexed="true" property="chemicalName" />
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Description</strong>
								</td>
								<td class="rightLabel" colspan="3">
									<html:textarea name="complexParticle.element" indexed="true" property="description" rows="3" />
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
</table>
