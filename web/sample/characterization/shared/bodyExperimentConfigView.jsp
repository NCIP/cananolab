<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<c:forEach var="experimentConfig" items="${charBean.experimentConfigs}" varStatus="configIndex">
<table class="summaryViewLayer4" align="center" width="95%">
	<c:if test="${edit eq 'true'}">
		<tr>
			<th style="text-align: right">
				<a
					href="javascript:setTheExperimentConfig(${experimentConfig.domain.id});">Edit</a>&nbsp;
			</th>
		</tr>
	</c:if>
	<tr>
		<td class="cellLabel">
			Technique
		</td>
	</tr>
	<tr>
		<td>
			${experimentConfig.techniqueDisplayName}
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Description
		</td>
	</tr>
	<tr>
		<td>
			<c:choose>
				<c:when test="${! empty experimentConfig.domain.description}">
					${fn:replace(experimentConfig.domain.description, cr, "<br>")}
				</c:when>
				<c:otherwise>
					N/A
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Instruments
		</td>
	</tr>
	<tr>
		<td>
			<c:choose>
				<c:when test="${! empty experimentConfig.instruments}">
					<table class="summaryViewLayer4" width="95%" align="center">
						<tr>
							<td width="25%" class="cellLabel">
								Manufacturer
							</td>
							<td width="25%" class="cellLabel">
								Model Name
							</td>
							<td class="cellLabel">
								Type
							</td>
						</tr>
						<c:forEach var="instrument" items="${experimentConfig.instruments}" varStatus="configIndex">
							<tr>
								<td>
									${instrument.manufacturer}
								</td>
								<td>
									${instrument.modelName}
								</td>
								<td>
									${instrument.type}
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:when>
				<c:otherwise>
					N/A
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<br>
</c:forEach>
