<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">

function update() {
  document.addParticlePropertiesForm.action="addParticleProperties.do?dispatch=update&page=0";
  document.addParticlePropertiesForm.submit();
}

</script>
<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Polymer Information
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
			<td class="label">
				<html:select property="polymer.initiator">
					<option/>
					<html:options name="allPolymerInitiators"/>
				</html:select>
			</td>
			<td class="label">
				<strong>Number of Monomers</strong>
			</td>
			<td class="rightLabel">
				<html:text property="polymer.numberOfMonomers" />
			</td>
		</tr>
	</tbody>
</table>
<br>
<c:forEach var="polymer.monomer" items="${addParticlePropertiesForm.map.polymer.monomers}" varStatus="status">
	<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
		<tbody>
			<tr class="topBorder">
				<td class="formTitle" colspan="2">
					<div align="justify">
						Polymer Monomer ${status.index+1} Information
					</div>
				</td>
			</tr>
			<tr>
				<td class="leftLabel">
					<strong>Name </strong>
				</td>
				<td class="rightLabel">
					<html:text name="polymer.monomer" indexed="true" property="name" />
				</td>
			</tr>
		</tbody>
	</table>
	<br>
</c:forEach>
<input type="button" onclick="javascript:update()" value="Update Monomers">

