<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0" rules="none">
	<tbody>
		<c:choose>
			<c:when
				test="${!empty characterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].datumList}">
				<tr class="formSubTitle">
					<td>
						<strong>Name*</strong>
					</td>
					<td>
						<strong>Value Type</strong>
					</td>
					<td>
						<strong>Value*</strong>
					</td>
					<td>
						<strong>Unit</strong>
					</td>
					<td>
						<strong>Description</strong>
					</td>
					<td></td>
				</tr>
			</c:when>
		</c:choose>
		<logic:iterate id="ddata" name="characterizationForm"
			property="achar.derivedBioAssayDataList[${param.fileInd}].datumList"
			indexId="dInd">
			<tr>
				<td class="leftLabel" valign="top">
					<c:choose>
						<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
							<html:select styleId="datumName${param.fileInd}-${dInd}"
								property="achar.derivedBioAssayDataList[${param.fileInd}].datumList[${dInd}].domainDerivedDatum.name"
								onchange="javascript:callPrompt('Name', 'datumName' + ${param.fileInd} + '-'+ ${dInd});getUnit(${param.fileInd}, ${dInd});">
								<option value=""></option>
								<html:options name="derivedDatumNames" />
								<option value="other">
									[Other]
								</option>
							</html:select>&nbsp; 	
						</c:when>
						<c:otherwise>
							${ddata.domainDerivedDatum.name}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="label" valign="top">
					<c:choose>
						<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
							<html:select styleId="valueType${param.fileInd}-${dInd}"
								property="achar.derivedBioAssayDataList[${param.fileInd}].datumList[${dInd}].domainDerivedDatum.valueType"
								onchange="javascript:callPrompt('Value Type', 'valueType' + ${param.fileInd} + '-'+${dInd});">
								<option value=""></option>
								<html:options name="derivedDatumValueTypes" />
								<option value="other">
									[Other]
								</option>
							</html:select>&nbsp; 						
						</c:when>
						<c:otherwise>
							${ddata.domainDerivedDatum.valueType}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="label" valign="top">
					<c:choose>
						<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
							<html:text
								property="achar.derivedBioAssayDataList[${param.fileInd}].datumList[${dInd}].valueStr"
								size="5" />&nbsp; 						
						</c:when>
						<c:otherwise>
							${ddata.valueStr}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="label" valign="top">
					<c:choose>
						<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
							<html:select styleId="unit${param.fileInd}-${dInd}"
								property="achar.derivedBioAssayDataList[${param.fileInd}].datumList[${dInd}].domainDerivedDatum.valueUnit"
								onchange="javascript:callPrompt('Unit', 'unit' + ${param.fileInd} + '-'+${dInd});">
								<option value=""></option>
								<c:forEach var="unit"
									items="${unitMap[characterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].datumList[dInd].domainDerivedDatum.name]}">
									<html:option value="${unit }" />
								</c:forEach>
								<option value="other">
									[Other]
								</option>
							</html:select>&nbsp; 						
						</c:when>
						<c:otherwise>
							${ddata.domainDerivedDatum.valueUnit}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="label" valign="top">
					<c:choose>
						<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
							<html:textarea styleId="description${param.fileInd}-${dInd}"
								property="achar.derivedBioAssayDataList[${param.fileInd}].datumList[${dInd}].domainDerivedDatum.description" />&nbsp; 												
						</c:when>
						<c:otherwise>
							${ddata.domainDerivedDatum.description}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">

						<td class="rightLabel">
							<a href="#" class="removeLink2"
								onclick="javascript:removeChildComponent(document.forms[0], '${actionName}', ${param.fileInd}, ${dInd}, 'removeDerivedDatum')">remove</a>
						</td>
					</c:when>
					<c:otherwise>
						<td class="rightLabel"></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</logic:iterate>
	</tbody>
</table>
<br>

