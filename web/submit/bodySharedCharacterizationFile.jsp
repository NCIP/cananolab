<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<tr>
	<td class="leftLabel">
		<strong>Characterization File Name</strong>
	</td>
	<c:choose>
		<c:when test="${canUserUpdateParticle eq 'true'}">
			<td class="label">
				<logic:present name="characterizationFile${param.chartInd}">
					<html:link page="/viewAssayResultFile.do?chartInd=${param.chartInd}&actionName=${param.actionName}">
						<bean:write name="characterizationFile${param.chartInd}" property="displayName" />
					</html:link>
				</logic:present>
				<logic:notPresent name="characterizationFile${param.chartInd}">
					Click on "Load File" button				
				</logic:notPresent>
			</td>
			<td class="rightLabel" colspan="2">
				<input type="button" onclick="javascript:loadFile(this.form, '${param.actionName}', '<bean:write name="${param.formName}" property="particleName"/>', ${param.chartInd})" value="Load File">
			</td>
		</c:when>
		<c:otherwise>
			<td class="rightLabel" colspan="3">
				<logic:present name="characterizationFile${param.chartInd}">
					<html:link page="/viewAssayResultFile.do?chartInd=${param.chartInd}&actionName=${param.actionName}">
						<bean:write name="characterizationFile${param.chartInd}" property="displayName" />
					</html:link>
				</logic:present>
				&nbsp;
			</td>
		</c:otherwise>
	</c:choose>
</tr>
<logic:present name="characterizationFile${chartInd}">
	<bean:define id="fileId" name='characterizationFile${chartInd}' property='id' type="java.lang.String" />
	<html:hidden property="achar.derivedBioAssayDataList[${chartInd}].fileId" value="${fileId}" />
</logic:present>

