<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<logic:present name="characterizationFile${param.chartInd}">
	<bean:define id="fileId" name='characterizationFile${param.chartInd}' property='id' type="java.lang.String" />
</logic:present>
<tr>
	<td class="leftLabel">
		<strong>Characterization File Name</strong>
	</td>
	<c:choose>
		<c:when test="${canUserSubmit eq 'true'}">
			<td class="label">
				<logic:present name="characterizationFile${param.chartInd}">
					<html:link page="/updateAssayFile.do?page=0&dispatch=setupUpdate&fileId=${fileId}&actionName=${param.actionName}">
						<bean:write name="characterizationFile${param.chartInd}" property="displayName" />
					</html:link>
				</logic:present>
				<logic:notPresent name="characterizationFile${param.chartInd}">
					Click on "Load File" button				
				</logic:notPresent>
			</td>
			<td class="rightLabel" colspan="2">
			</td>
		</c:when>
		<c:otherwise>
			<td class="rightLabel" colspan="3">
				<logic:present name="characterizationFile${param.chartInd}">
					<html:link page="/updateAssayFile.do?page=0&dispatch=setupView&fileId=${fileId}&actionName=${param.actionName}">
						<bean:write name="characterizationFile${param.chartInd}" property="displayName" />
					</html:link>
				</logic:present>
				&nbsp;
			</td>
		</c:otherwise>
	</c:choose>
</tr>
<logic:present name="characterizationFile${chartInd}">
	<html:hidden property="achar.derivedBioAssayDataList[${chartInd}].fileId" value="${fileId}" />
</logic:present>

