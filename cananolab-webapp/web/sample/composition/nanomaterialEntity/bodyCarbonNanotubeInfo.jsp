<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<table class="summaryViewNoGrid" align="left">
			<tr>
				<th scope="row" class="cellLabel">
					Average Length
				</th>
				<th scope="row" class="cellLabel">
					Chirality
				</th>
				<th scope="row" class="cellLabel">
					Diameter
				</th>
				<th scope="row" class="cellLabel">
					Wall Type
				</th>
			</tr>
			<tr>
				<td>
					<c:out value="${nanomaterialEntity.carbonNanotube.averageLength}"/>
					<c:out value="${nanomaterialEntity.carbonNanotube.averageLengthUnit}"/>
				</td>
				<td>
					<c:out value="${nanomaterialEntity.carbonNanotube.chirality}"/>
				</td>
				<td>
					<c:out value="${nanomaterialEntity.carbonNanotube.diameter}"/>
					<c:out value="${nanomaterialEntity.carbonNanotube.diameterUnit}"/>
				</td>
				<td>
					<c:out value="${nanomaterialEntity.carbonNanotube.wallType}"/>
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
					<label for="averageLength">Average Length</label>
				</td>
				<td class="cellLabel">
					<input type="text"
						id="averageLength"
						name="nanomaterialEntity.carbonNanotube.averageLength"
						value="${compositionForm.map.nanomaterialEntity.carbonNanotube.averageLength}" />
						<%-- onkeydown="return filterFloatNumber(event)" /--%>
				</td>
				<td class="cellLabel">
					<label for="averageLengthUnit">Average Length Unit</label>
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
											<c:out value="${unit}"/>
										</option>
									</c:when>
									<c:otherwise>
										<option value="${unit}">
											<c:out value="${unit}"/>
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
					<label for="chirality">Chirality</label>
				</td>
				<td class="cellLabel">
					<input id="chirality" type="text"
						name="nanomaterialEntity.carbonNanotube.chirality"
						value="${compositionForm.map.nanomaterialEntity.carbonNanotube.chirality}" />
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					<label for="tubeDiameter">Diameter</label>
				</td>
				<td class="cellLabel">
					<input type="text"
						id="tubeDiameter"
						name="nanomaterialEntity.carbonNanotube.diameter"
						value="${compositionForm.map.nanomaterialEntity.carbonNanotube.diameter}" />
						<%-- onkeydown="return filterFloatNumber(event)" /--%>
				</td>
				<td class="cellLabel">
					<label for="diameterUnit">Diameter Unit</label>
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
											<c:out value="${unit}"/>
										</option>
									</c:when>
									<c:otherwise>
										<option value="${unit}">
											<c:out value="${unit}"/>
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
					<label for="wallType">Wall Type</label>
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
										<c:out value="${type}"/>
									</option>
								</c:when>
								<c:otherwise>
									<option value="${type}">
										<c:out value="${type}"/>
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