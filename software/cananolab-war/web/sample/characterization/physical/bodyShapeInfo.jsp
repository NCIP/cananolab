<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<c:choose>
			<c:when test="${! empty charBean.shape.type}">
				<table class="summaryViewNoGrid" align="left">
					<tr>
						<td class="cellLabel">
							Type
						</td>
						<td class="cellLabel">
							Aspect Ratio
						</td>
						<td class="cellLabel">
							Minimum Dimension
						</td>
						<td class="cellLabel">
							Maximum Dimension
						</td>
					</tr>
					<tr>
						<td>
							${charBean.shape.type}
						</td>
						<td>
							${charBean.shape.aspectRatio}
						</td>
						<td>
							${charBean.shape.minDimension} ${charBean.shape.minDimensionUnit}
						</td>
						<td>
							${charBean.shape.maxDimension} ${charBean.shape.maxDimensionUnit}
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>N/A
	</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="4">
					Shape Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					Type *
				</td>
				<td>
					<div id="shapeTypePrompt">
						<select name="achar.shape.type" id="shapeType"
							onchange="javascript:callPrompt('Type', 'shapeType', 'shapeTypePrompt');">
							<option value=""></option>
							<c:forEach var="type" items="${shapeTypes}">
								<c:choose>
									<c:when
										test="${type eq characterizationForm.map.achar.shape.type}">
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
							<option value="other">
								[other]
							</option>
						</select>
					</div>
				</td>
				<td class="cellLabel">
					Aspect Ratio
				</td>
				<td>
					<input type="text" name="achar.shape.aspectRatio" id="aspectRatio"
						id="aspectRatio"
						value="${characterizationForm.map.achar.shape.aspectRatio}" />
					<%-- onkeydown="return filterFloatNumber(event)" /--%>
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					Minimum Dimension
				</td>
				<td>
					<input type="text" name="achar.shape.minDimension"
						id="shapeMinDimension"
						value="${characterizationForm.map.achar.shape.minDimension}" />
					<%-- onkeydown="return filterFloatNumber(event)" /--%>
					<div id="minDimensionUnitPrompt">
						<select name="achar.shape.minDimensionUnit" id="minDimensionUnit"
							onchange="callPrompt('Unit', 'minDimensionUnit', 'minDimensionUnitPrompt')">
							<option value=""></option>
							<c:forEach var="unit" items="${dimensionUnits}">
								<c:choose>
									<c:when
										test="${unit eq characterizationForm.map.achar.shape.minDimensionUnit}">
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
					Maximum Dimension
				</td>
				<td>
					<input type="text" name="achar.shape.maxDimension"
						id="shapeMaxDimension"
						value="${characterizationForm.map.achar.shape.maxDimension}" />
					<%-- onkeydown="return filterFloatNumber(event)" /--%>
					<div id="maxDimensionUnitPrompt">
						<select name="achar.shape.maxDimensionUnit" id="maxDimensionUnit"
							onchange="callPrompt('Unit', 'maxDimensionUnit', 'maxDimensionUnitPrompt')">
							<option value=""></option>
							<c:forEach var="unit" items="${dimensionUnits}">
								<c:choose>
									<c:when
										test="${unit eq characterizationForm.map.achar.shape.maxDimensionUnit}">
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
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>

