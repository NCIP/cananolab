<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:if test="${charBean.characterizationName eq 'Shape'}">
	<c:choose>
		<c:when test="${! empty charBean.shape.type}">
			<table class="summaryViewLayer4" align="center" width="95%">
				<tr>
					<th>
						Type
					</th>
					<th>
						Aspect Ratio
					</th>
					<th>
						Minimum Dimension
					</th>
					<th>
						Maximum Dimension
					</th>
				</tr>
				<tr>
					<td>
						${charBean.shape.type}
					</td>
					<td>
						${charBean.shape.aspectRatio}
					</td>
					<td>
						${charBean.shape.minDimension} ${charBean.shape.minDimensionUnit}
					</td>
					<td>
						${charBean.shape.maxDimension} ${charBean.shape.maxDimensionUnit}
					</td>
			</table>
		</c:when>
		<c:otherwise>N/A
	</c:otherwise>
	</c:choose>
</c:if>
<c:if test="${charBean.characterizationName eq 'Physical State'}">
	<c:choose>
		<c:when test="${! empty charBean.physicalState.type}">
			<table class="summaryViewLayer4" align="center" width="95%">
				<tr>
					<th>
						Type
					</th>
				</tr>
				<tr>
					<td>
						${charBean.physicalState.type}
					</td>
			</table>
		</c:when>
		<c:otherwise>N/A
	</c:otherwise>
	</c:choose>
</c:if>
<c:if test="${charBean.characterizationName eq 'Solubility'}">
	<c:choose>
		<c:when test="${! empty charBean.solubility.solvent}">
			<table class="summaryViewLayer4" align="center" width="95%">
				<tr>
					<th>
						Solvent
					</th>
					<th>
						Is Soluble?
					</th>
					<th>
						Critical Concentration
					</th>
				</tr>
				<tr>
					<td>
						${charBean.solubility.solvent}
					</td>
					<td>
						${charBean.solubility.isSoluble}
					</td>
					<td>
						${charBean.solubility.criticalConcentration}
						${charBean.solubility.criticalConcentrationUnit}
					</td>
			</table>
		</c:when>
		<c:otherwise>N/A
	</c:otherwise>
	</c:choose>
</c:if>
<c:if test="${charBean.characterizationName eq 'Surface'}">
	<c:choose>
		<c:when test="${! empty charBean.surface.isHydrophobic}">
			<table class="summaryViewLayer4" align="center" width="95%">
				<tr>
					<th>
						Is Hydrophobic?
					</th>
				</tr>
				<tr>
					<td>
						${charBean.surface.isHydrophobic}
					</td>
			</table>
		</c:when>
		<c:otherwise>N/A
	</c:otherwise>
	</c:choose>
</c:if>
<c:if test="${charBean.characterizationName eq 'Cytotoxicity'}">
	<c:choose>
		<c:when test="${! empty charBean.cytotoxicity.cellLine}">
			<table class="summaryViewLayer4" align="center" width="95%">
				<tr>
					<th>
						Cell Line
					</th>
				</tr>
				<tr>
					<td>
						${charBean.cytotoxicity.cellLine}
					</td>
			</table>
		</c:when>
		<c:otherwise>N/A
	</c:otherwise>
	</c:choose>
</c:if>
<c:if test="${charBean.characterizationName eq 'Enzyme Induction'}">
	<c:choose>
		<c:when test="${! empty charBean.enzymeInduction.enzyme}">
			<table class="summaryViewLayer4" align="center" width="95%">
				<tr>
					<th>
						Enzyme Name
					</th>
				</tr>
				<tr>
					<td>
						${charBean.enzymeInduction.enzyme}
					</td>
			</table>
		</c:when>
		<c:otherwise>N/A
	</c:otherwise>
	</c:choose>
</c:if>


