<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="summaryViewLayer4" align="center" width="95%">
	<tr>
		<th width="33%">
			Technique
		</th>
		<th width="33%">
			Instruments
		</th>
		<th>
			Description
		</th>
		<th>
		</th>
	</tr>
	<c:forEach var="experimentConfig" items="${charBean.experimentConfigs}"
		varStatus="configIndex">
		<tr>
			<td>
				${experimentConfig.techniqueDisplayName}
			</td>
			<td>
				<c:if test="${! empty experimentConfig.instrumentDisplayNames}">
					<c:forEach var="instrumentDisplayName"
						items="${experimentConfig.instrumentDisplayNames}">
							${instrumentDisplayName}
					</c:forEach>
				</c:if>
			</td>
			<td>
				<c:if test="${! empty experimentConfig.domain.description}">
				${fn:replace(experimentConfig.domain.description, cr, "<br>")}
				</c:if>
			</td>
			<c:if test="${edit eq 'true'}">
				<td align="right">
					<a
						href="javascript:setTheExperimentConfig(${experimentConfig.domain.id});">Edit</a>&nbsp;
				</td>
			</c:if>
		</tr>
	</c:forEach>
</table>
