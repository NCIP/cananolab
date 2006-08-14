<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">

function refreshSurfaceGroups() {
  document.addParticlePropertiesForm.action="addParticleProperties.do?dispatch=update&page=0";
  document.addParticlePropertiesForm.submit();
}

</script>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Dendrimer Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Core </strong>
			</td>
			<td class="label">
				<html:select property="dendrimer.core">
					<option/>
					<html:options name="allDendrimerCores"/>
				</html:select>
			</td>
			<td class="label">
				<strong>Branch</strong>
			</td>
			<td class="rightLabel">
				<html:text property="dendrimer.branch" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Repeat Unit</strong>
			</td>
			<td class="label">
				<html:text property="dendrimer.repeatUnit" />
			</td>
			<td class="label">
				<strong>Generation</strong>
			</td>
			<td class="rightLabel">
				<html:text property="dendrimer.generation" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Molecular Formula</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:text property="dendrimer.molecularFormula" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Surface Groups</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:text property="dendrimer.numberOfSurfaceGroups" />
			</td>
		</tr>
	</tbody>
</table>
<br>
<c:forEach var="dendrimer.surfaceGroup" items="${addParticlePropertiesForm.map.dendrimer.surfaceGroups}" varStatus="status">
	<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
		<tbody>
			<tr class="topBorder">
				<td class="formTitle" colspan="4">
					<div align="justify">
						Dendrimer Surface Group ${status.index+1} Information 
					</div>					
				</td>
			</tr>
			<tr>
				<td class="leftLabel">
					<strong>Name </strong>
				</td>
				<td class="label">
					<html:select name="dendrimer.surfaceGroup" indexed="true" property="name">
						<option/>
						<html:options name="allDendrimerSurfaceGroupNames"/>
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
<input type="button" onclick="javascript:refreshSurfaceGroups()" value="Update Surface Groups">
