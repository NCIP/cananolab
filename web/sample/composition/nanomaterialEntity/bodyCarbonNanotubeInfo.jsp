<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<table class="summaryViewLayer4" align="center" width="95%">
			<tr>
				<th>
					Average Length
				</th>
				<th>
					Chirality
				</th>
				<th>
					Diameter
				</th>
				<th>
					Wall Type
				</th>
			</tr>
			<tr>
				<td>
					${entity.carbonNanotube.averageLength}
					${entity.carbonNanotube.averageLengthUnit}
				</td>
				<td>
					${entity.carbonNanotube.chirality}
				</td>
				<td>
					${entity.carbonNanotube.diameter}
					${entity.carbonNanotube.diameterUnit}
				</td>
				<td>
					${entity.carbonNanotube.wallType}
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="6">
					Carbon Nanotube Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					Average Length
				</td>
				<td class="cellLabel">
					<input type="text" name="entity.carbonNanotube.averageLength"
						value="${nanomaterialEntityForm.map.entity.carbonNanotube.averageLength}"
						onkeydown="return filterFloatNumber(event)" />
				</td>
				<td class="cellLabel">
					Average Length Unit
				</td>
				<td class="cellLabel">
					<select name="entity.carbonNanotube.averageLengthUnit"
						id="averageLengthUnit"
						onchange="javascript:callPrompt('Average Length Unit', 'averageLengthUnit');">
						<option value=""></option>
						<c:forEach var="unit" items="${dimensionUnits}">
							<c:choose>
								<c:when
									test="${unit eq nanomaterialEntityForm.map.entity.carbonNanotube.averageLengthUnit}">
									<option value="${unit}" selected>
										${unit}
									</option>
								</c:when>
								<c:otherwise>
									<option value="${unit}">
										${unit}
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<option value="other">
							[Other]
						</option>
					</select>
				</td>
				<td class="cellLabel">
					Chirality
				</td>
				<td class="cellLabel">
					<input type="text" name="entity.carbonNanotube.chirality"
						value="${nanomaterialEntityForm.map.entity.carbonNanotube.chirality}" />
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					Diameter
				</td>
				<td class="cellLabel">
					<input type="text" name="entity.carbonNanotube.diameter"
						value="${nanomaterialEntityForm.map.entity.carbonNanotube.diameter}"
						onkeydown="return filterFloatNumber(event)" />
				</td>
				<td class="cellLabel">
					Diameter Unit
				</td>
				<td class="cellLabel">
					<select name="entity.carbonNanotube.diameterUnit" id="diameterUnit"
						onchange="javascript:callPrompt('Diameter Unit', 'diameterUnit');">
						<option value=""></option>
						<c:forEach var="unit" items="${dimensionUnits}">
							<c:choose>
								<c:when
									test="${unit eq nanomaterialEntityForm.map.entity.carbonNanotube.diameterUnit}">
									<option value="${unit}" selected>
										${unit}
									</option>
								</c:when>
								<c:otherwise>
									<option value="${unit}">
										${unit}
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<option value="other">
							[Other]
						</option>
					</select>
				</td>
				<td class="cellLabel">
					Wall Type
				</td>
				<td class="cellLabel">
					<select name="entity.carbonNanotube.wallType" id="wallType">
						<option value=""></option>
						<c:forEach var="type" items="${wallTypes}">
							<c:choose>
								<c:when
									test="${type eq nanomaterialEntityForm.map.entity.carbonNanotube.wallType}">
									<option value="${type}" selected>
										${type}
									</option>
								</c:when>
								<c:otherwise>
									<option value="${type}">
										${type}
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>