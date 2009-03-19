<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<c:forEach var="experimentConfig"
	items="${charBean.experimentConfigs}"
	varStatus="configIndex">
	<table class="smalltable2" border="0" width="90%" align="center"">
		<tr>
			<td class="subformTitle" colspan="2" align="right">
				<c:if test="${edit eq 'true'}">
					<a href="javascript:setTheExperimentConfig(${experimentConfig.domain.id});">edit</a>&nbsp;
				</c:if>
			</td>
		</tr>
		<tr>
			<td>
				<strong>Technique: </strong>${experimentConfig.techniqueDisplayName}
			</td>
		</tr>
		<c:if test="${! empty experimentConfig.instrumentDisplayNames}">
			<tr>
				<td valign="top">
					<strong>Instrument(s): </strong>
					<div class="indented4">
						<c:forEach var="instrumentDisplayName"
							items="${experimentConfig.instrumentDisplayNames}">
							${instrumentDisplayName}
						</c:forEach>
					</div>
					&nbsp;
				</td>
			</tr>
		</c:if>
		<c:if test="${! empty experimentConfig.domain.description}">
			<tr>
				<td valign="top">
					<strong>Description: </strong>${experimentConfig.domain.description}
				</td>
			</tr>
		</c:if>
	</table>
	<br>
</c:forEach>
