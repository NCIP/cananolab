<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table border="0" align="center" cellpadding="3" cellspacing="0"
	width="100%" class="topBorderOnly" summary="">
	<tr>
	<tr class="topBorder">
		<td class="formTitle" colspan="4">
			<div align="justify">
				Design and Methods
			</div>
		</td>
	</tr>
	<tr>
		<td class="completeLabel" valign="top" colspan="4">
			<strong>Technique and Instrument</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<c:if
				test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
				<a style="" id="addTechniqueInstrument"
					href="javascript:showhide('newExperimentConfig');"> <span
					class="addLink2">Add</span> </a>
			</c:if>
			&nbsp;
			<blockquote>
				<c:forEach var="experimentConfig"
					items="${characterizationForm.map.achar.experimentConfigs}">
					<a
						href="javascript:setTheExperimentConfig(${experimentConfig.domain.id})">${experimentConfig.displayName}</a>
					<br>
				</c:forEach>
				&nbsp;
				<br>
				<div id="newExperimentConfig" style="display: none;">
					<jsp:include page="/common/bodySubmitExperimentConfig.jsp" />
				</div>
			</blockquote>
		</td>
	</tr>
</table>
<br>
