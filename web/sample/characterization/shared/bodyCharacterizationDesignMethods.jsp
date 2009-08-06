<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel">
			Design and Methods Description
		</td>
		<td>
			<html:textarea property="achar.description" cols="120" rows="3" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="20%">
			Technique and Instrument
		</td>
		<td>
			<c:set var="addExpermentConfigButtonStyle" value="display:block" />
			<c:if test="${openExperimentConfig eq 'true'}">
				<c:set var="addExpermentConfigButtonStyle" value="display:none" />
			</c:if>
			<a style="${addExpermentConfigButtonStyle}" id="addExperimentConfig"
				href="javascript:clearExperimentConfig();openSubmissionForm('ExperimentConfig');"><img
					align="top" src="images/btn_add.gif" border="0" /> </a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:if
				test="${! empty characterizationForm.map.achar.experimentConfigs }">
				<c:set var="charBean" value="${characterizationForm.map.achar}" />
				<c:set var="edit" value="true" />
				<%@ include file="bodyExperimentConfigView.jsp"%>
			</c:if>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:set var="newExperimentConfigStyle" value="display:none" />
			<c:if test="${openExperimentConfig eq 'true'}">
				<c:set var="newExperimentConfigStyle" value="display:block" />
			</c:if>
			<div id="newExperimentConfig" style="${newExperimentConfigStyle}">
				<jsp:include page="bodySubmitExperimentConfig.jsp" />
			</div>
		</td>
	</tr>
</table>
<br>