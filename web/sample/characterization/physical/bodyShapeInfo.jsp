<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

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
			<select name="achar.shape.type" id="shapeType"
				onchange="javascript:callPrompt('Type', 'shapeType');">
				<option value=""></option>
				<c:forEach var="type" items="${shapeTypes}">
					<c:choose>
						<c:when
							test="${type eq characterizationForm.map.achar.shape.type}">
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
				<option value="other">
					[Other]
				</option>
			</select>
		</td>
		<td class="cellLabel">
			Aspect Ratio
		</td>
		<td>
			<input type="text" name="achar.shape.aspectRatio" id="aspectRatio"
				value="${characterizationForm.map.achar.shape.aspectRatio}"
				onkeydown="return filterFloatNumber(event)" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Minimum Dimension
		</td>
		<td>
			<input type="text" name="achar.shape.minDimension"
				value="${characterizationForm.map.achar.shape.minDimension}"
				onkeydown="return filterFloatNumber(event)" />
			<select name="achar.shape.minDimensionUnit" id="minDimensionUnit"
				onchange="callPrompt('Unit', 'minDimensionUnit')">
				<option value=""></option>
				<c:forEach var="unit" items="${dimensionUnits}">
					<c:choose>
						<c:when
							test="${unit eq characterizationForm.map.achar.shape.minDimensionUnit}">
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
			Maximum Dimension
		</td>
		<td>
			<input type="text" name="achar.shape.maxDimension"
				value="${characterizationForm.map.achar.shape.maxDimension}"
				onkeydown="return filterFloatNumber(event)" />
			<select name="achar.shape.maxDimensionUnit" id="maxDimensionUnit"
				onchange="callPrompt('Unit', 'maxDimensionUnit')">
				<option value=""></option>
				<c:forEach var="unit" items="${dimensionUnits}">
					<c:choose>
						<c:when
							test="${unit eq characterizationForm.map.achar.shape.maxDimensionUnit}">
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
	</tr>
</table>
</br>
