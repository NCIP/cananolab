<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="compEleIndex" value="${param.compEleInd }" />
<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="leftLabelWithTop" valign="top" width="25%">
				<strong>Function Type</strong>
			</td>
			<td class="rightLabelWithTop" valign="top">
				<strong>Description</strong>
			</td>
		</tr>
		<logic:iterate id="ifdata" name="nanomaterialEntityForm"
			property="entity.composingElements[${param.compEleInd}].inherentFunctions"
			indexId="ifInd">
			<tr>
				<td class="leftLabel" valign="top" width="85">
					${nanomaterialEntityForm.map.entity.composingElements[compEleIndex].inherentFunctions[ifInd].type}&nbsp;
					<c:if
						test="${nanomaterialEntityForm.map.entity.composingElements[param.compEleInd].inherentFunctions[ifInd].type == 'imaging'}">
						<br>(Modality Type:&nbsp;${nanomaterialEntityForm.map.entity.composingElements[compEleIndex].inherentFunctions[ifInd].imagingFunction.modality})
					</c:if>
					&nbsp;
				</td>
				<td class="rightLabel" valign="top">
					${nanomaterialEntityForm.map.entity.composingElements[compEleIndex].inherentFunctions[ifInd].description}&nbsp;
					&nbsp;
				</td>
			</tr>
		</logic:iterate>
	</tbody>
</table>

