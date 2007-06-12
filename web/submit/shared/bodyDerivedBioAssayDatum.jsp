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
				test="${!empty nanoparticleCharacterizationForm.map.achar.derivedBioAssayDataList[param.fileInd].datumList}">
				<tr class="formSubTitle">
					<td>
						<strong>Name</strong>
					</td>
					<td>
						<strong>Value</strong>
					</td>
					<td width="10">
						<strong>Standard Deviation</strong>
					</td>
					<td>
						<strong>Unit</strong>
					</td>
					<td>
						<strong>Data Category</strong>
					</td>
					<td></td>
				</tr>
			</c:when>
		</c:choose>
		<logic:iterate id="ddata" name="nanoparticleCharacterizationForm"
			property="achar.derivedBioAssayDataList[${param.fileInd}].datumList"
			indexId="dInd">
			<tr>
				<td class="leftLabel">
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<html:select
								property="achar.derivedBioAssayDataList[${param.fileInd}].datumList[${dInd}].name">
								<option value="">
									--?--
								</option>
								<option>
									Average/Mean
								</option>
								<option>
									Z-Average
								</option>
								<option>
									PDI
								</option>
								<%--						<html:options name="allTypes" />--%>
							</html:select>&nbsp; 	
						</c:when>
						<c:otherwise>
							${ddata.name}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="label">
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<html:text
								property="achar.derivedBioAssayDataList[${param.fileInd}].datumList[${dInd}].value"
								size="5" />&nbsp; 						
						</c:when>
						<c:otherwise>
							${ddata.value}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="label">
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<html:text
								property="achar.derivedBioAssayDataList[${param.fileInd}].datumList[${dInd}].std"
								size="5" />&nbsp; 						
						</c:when>
						<c:otherwise>
							${ddata.std}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="label">
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<html:select
								property="achar.derivedBioAssayDataList[${param.fileInd}].datumList[${dInd}].unit">
								<option value="">
									--?--
								</option>
								<%--						<html:options name="allUnits" />--%>
							</html:select>&nbsp; 						
						</c:when>
						<c:otherwise>
							${ddata.unit}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="label">
					<c:choose>
						<c:when test="${canUserSubmit eq 'true'}">
							<html:select
								property="achar.derivedBioAssayDataList[${param.fileInd}].datumList[${dInd}].category">
								<option />
								<option value="Volume Distribution">
									Volume Distribution
								</option>
								<option value="Number Distribution">
									Number Distribution
								</option>
								<option value="Intensity Distribution">
									Intensity Distribution
								</option>

								<%--						<html:options name="allCategories" />--%>
							</html:select>&nbsp; 						
						</c:when>
						<c:otherwise>
							${ddata.category}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td class="rightLabel">
					<img src="images/Minus.gif" border="0" alt="remove this data">
					&nbsp;<a href="#" class="removeLink"
						onclick="javascript:removeCharacterizationData(nanoparticleCharacterizationForm, '${actionName}', ${param.fileInd}, ${dInd})">remove</a>
				</td>
			</tr>
		</logic:iterate>
	</tbody>
</table>
<br>
