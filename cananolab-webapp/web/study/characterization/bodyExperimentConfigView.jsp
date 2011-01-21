<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="summaryViewNoGrid" align="left">
	<tr>
		<td width="20%" class="cellLabel">
			Technique
		</td>
		<td width="40%" class="cellLabel">
			Instruments
		</td>
		<td class="cellLabel">
			Description
		</td>
		<td>
		</td>
	</tr>
	<c:forEach var="experimentConfig" items="${charBean.experimentConfigs}"
		varStatus="configIndex">
		<tr>
			<td>
				<c:out value="${experimentConfig.techniqueDisplayName}"/>
			</td>
			<td>
				<c:if test="${! empty experimentConfig.instrumentDisplayNames}">
					<c:forEach var="instrumentDisplayName"
						items="${experimentConfig.instrumentDisplayNames}">
							<c:out value="${instrumentDisplayName}"/><br>
					</c:forEach>
				</c:if>
			</td>
			<td>
				<c:if test="${! empty experimentConfig.domain.description}">
					<c:out value="${experimentConfig.description}" escapeXml="false" />
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
