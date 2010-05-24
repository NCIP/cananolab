<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<table class="summaryViewNoGrid" align="left">
			<tr>
				<td class="cellLabel">
					Average Length
				</td>
				<td class="cellLabel">
					Chirality
				</td>
				<td class="cellLabel">
					Diameter
				</td>
				<td class="cellLabel">
					Wall Type
				</td>
			</tr>
			<tr>
				<td>
					${nanomaterialEntity.carbonNanotube.averageLength}
					${nanomaterialEntity.carbonNanotube.averageLengthUnit}
				</td>
				<td>
					${nanomaterialEntity.carbonNanotube.chirality}
				</td>
				<td>
					${nanomaterialEntity.carbonNanotube.diameter}
					${nanomaterialEntity.carbonNanotube.diameterUnit}
				</td>
				<td>
					${nanomaterialEntity.carbonNanotube.wallType}
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
					<input type="text"
						id="averageLength"
						name="nanomaterialEntity.carbonNanotube.averageLength"
						value="${compositionForm.map.nanomaterialEntity.carbonNanotube.averageLength}" />
						<%-- onkeydown="return filterFloatNumber(event)" /--%>
				</td>
				<td class="cellLabel">
					Average Length Unit
				</td>
				<td class="cellLabel">
					<div id="averageLengthUnitPrompt">
						<select name="nanomaterialEntity.carbonNanotube.averageLengthUnit"
							id="averageLengthUnit"
							onchange="javascript:callPrompt('Average Length Unit', 'averageLengthUnit', 'averageLengthUnitPrompt');">
							<option value=""></option>
							<c:forEach var="unit" items="${dimensionUnits}">
								<c:choose>
									<c:when
										test="${unit eq compositionForm.map.nanomaterialEntity.carbonNanotube.averageLengthUnit}">
										<option value="${unit}" selected="selected">
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
								[other]
							</option>
						</select>
					</div>
				</td>
				<td class="cellLabel">
					Chirality
				</td>
				<td class="cellLabel">
					<input type="text"
						name="nanomaterialEntity.carbonNanotube.chirality"
						value="${compositionForm.map.nanomaterialEntity.carbonNanotube.chirality}" />
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					Diameter
				</td>
				<td class="cellLabel">
					<input type="text"
						id="tubeDiameter"
						name="nanomaterialEntity.carbonNanotube.diameter"
						value="${compositionForm.map.nanomaterialEntity.carbonNanotube.diameter}" />
						<%-- onkeydown="return filterFloatNumber(event)" /--%>
				</td>
				<td class="cellLabel">
					Diameter Unit
				</td>
				<td class="cellLabel">
					<div id="diameterUnitPrompt">
						<select name="nanomaterialEntity.carbonNanotube.diameterUnit"
							id="diameterUnit"
							onchange="javascript:callPrompt('Diameter Unit', 'diameterUnit', 'diameterUnitPrompt');">
							<option value=""></option>
							<c:forEach var="unit" items="${dimensionUnits}">
								<c:choose>
									<c:when
										test="${unit eq compositionForm.map.nanomaterialEntity.carbonNanotube.diameterUnit}">
										<option value="${unit}" selected="selected">
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
								[other]
							</option>
						</select>
					</div>
				</td>
				<td class="cellLabel">
					Wall Type
				</td>
				<td class="cellLabel">
					<select name="nanomaterialEntity.carbonNanotube.wallType"
						id="wallType">
						<option value=""></option>
						<c:forEach var="type" items="${wallTypes}">
							<c:choose>
								<c:when
									test="${type eq compositionForm.map.nanomaterialEntity.carbonNanotube.wallType}">
									<option value="${type}" selected="selected">
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