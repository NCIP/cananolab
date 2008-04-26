<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="compEleIndex" value="${param.compEleInd }" />
<logic:iterate id="ifdata" name="nanoparticleEntityForm"
	property="entity.composingElements[${param.compEleInd}].inherentFunctions"
	indexId="ifInd">
	<table class="topBorderOnly" cellspacing="0" cellpadding="3"
		width="100%" align="center" summary="" border="0">
		<tbody>
			<c:if test="${ifInd == 0 }">
				<tr>
					<td class="leftLabelWithTop" valign="top">
						<strong>Function Type</strong>
					</td>
					<td class="labelWithTop" valign="top">
						<strong>Description</strong>
					</td>
					<td class="labelWithTop" valign="top"
						id="modalityTitle_${param.compEleInd}_${ifInd}">
						<strong>Modality Type</strong>
					</td>
					<td class="rightLabelWithTop">
						<Strong>&nbsp;</Strong>
					</td>
				</tr>
			</c:if>
			<tr>
				<td class="leftLabel" valign="top" width="85">
					${nanoparticleEntityForm.map.entity.composingElements[compEleIndex].inherentFunctions[ifInd].type}
				</td>
				<td class="label">
					${nanoparticleEntityForm.map.entity.composingElements[compEleIndex].inherentFunctions[ifInd].description}
					&nbsp;
				</td>
				<td class="label">
					<c:choose>
						<c:when
							test="${nanoparticleEntityForm.map.entity.composingElements[param.compEleInd].inherentFunctions[ifInd].type == 'imaging'}">
							<c:set var="modalityDisplay" value="display: block;" />
						</c:when>
						<c:otherwise>
							<c:set var="modalityDisplay" value="display: none;" />
						</c:otherwise>
					</c:choose>
					<div id="modalityTypeTd_${param.compEleInd}_${ifInd}" style="">
						${nanoparticleEntityForm.map.entity.composingElements[compEleIndex].inherentFunctions[ifInd].imagingFunction.modality}
					</div>
					&nbsp;
				</td>
			</tr>
		</tbody>
	</table>
</logic:iterate>
