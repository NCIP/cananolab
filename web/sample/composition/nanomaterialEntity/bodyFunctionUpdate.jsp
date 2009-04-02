<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<logic:iterate id="ifdata" name="nanomaterialEntityForm"
	property="entity.composingElements[${param.compEleInd}].inherentFunctions"
	indexId="ifInd">
	<c:choose>
		<c:when
			test="${nanomaterialEntityForm.map.entity.composingElements[param.compEleInd].inherentFunctions[ifInd].type == 'imaging'}">
			<c:set var="modalityDisplay" value="display: block;" />
		</c:when>
		<c:otherwise>
			<c:set var="modalityDisplay" value="display: none;" />
		</c:otherwise>
	</c:choose>
	<table class="topBorderOnly" cellspacing="0" cellpadding="3"
		width="100%" align="center" summary="" border="0">
		<tbody>
			<tr>
				<td class="formSubTitleNoRight" colspan="3">
					<span>Inherent Function #${ifInd + 1}</span>
				</td>
				<td class="formSubTitleNoLeft" align="right">
					<a href="#"
						onclick="javascript:removeChildComponent(nanomaterialEntityForm,
					'nanomaterialEntity', ${param.compEleInd}, ${ifInd}, 'removeInherentFunction');">
						<img src="images/delete.gif" border="0" alt="remove this function">
					</a>
				</td>
			</tr>
			<tr>
				<td class="leftLabelWithTop" valign="top">
					<strong>Function Type*</strong>
				</td>
				<td class="labelWithTop" valign="top">
					<html:select
						property="entity.composingElements[${param.compEleInd}].inherentFunctions[${ifInd}].type"
						size="1" styleId="funcType_${param.compEleInd}_${ifInd}"
						onchange="javascript:callPrompt('Function Type', 'funcType_${param.compEleInd}_${ifInd}');
									displayModality(${param.compEleInd}, ${ifInd}); ">
						<option value="" />
							<html:options name="functionTypes" />
						<option value="other">
							[Other]
						</option>
					</html:select>
					&nbsp;
				</td>
				<td class="labelWithTop" valign="top" align="right">
					<strong style="${modalityDisplay}"
						id="modalityTitle_${param.compEleInd}_${ifInd}">Modality
						Type</strong>&nbsp;
				</td>
				<td class="rightLabelWithTop" valign="top" align="left">

					<div id="modalityTypeTd_${param.compEleInd}_${ifInd}"
						style="${modalityDisplay}">
						<html:select
							property="entity.composingElements[${param.compEleInd}].inherentFunctions[${ifInd}].imagingFunction.modality"
							size="1"
							onchange="javascript:callPrompt('Modality Type', 'modalityType_${param.compEleInd}_${ifInd}');"
							styleId="modalityType_${param.compEleInd}_${ifInd}">
							<option value="" />
								<html:options name="modalityTypes" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</div>
					&nbsp;
				</td>
			</tr>

			<tr>
				<td class="leftLabel" valign="top">
					<strong>Description</strong>
				</td>
				<td class="rightLabel" valign="top" colspan="3">
					<html:textarea
						property="entity.composingElements[${param.compEleInd}].inherentFunctions[${ifInd}].description"
						rows="1" cols="50" />
					&nbsp;
				</td>

			</tr>
		</tbody>
	</table>
	<br>
</logic:iterate>
