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
					Average Diameter
				</th>
				<th>
					Number of Carbons
				</th>
			</tr>
			<tr>
				<td>
					${entity.fullerene.averageDiameter}
					${entity.fullerene.averageDiameterUnit}
				</td>
				<td>
					${entity.fullerene.numberOfCarbon}
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="6">
					Fullerene Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					Average Diameter
				</td>
				<td class="cellLabel">
					<input type="text" name="entity.fullerene.averageDiameter"
						id="averageDiameter" onkeydown="return filterFloatNumber(event)"
						value="${nanomaterialEntityForm.map.entity.fullerene.averageDiameter}" />
				</td>
				<td class="cellLabel">
					Average Diameter Unit
				</td>
				<td class="cellLabel">
					<select name="entity.fullerene.averageDiameterUnit"
						id="averageDiameterUnit"
						onchange="javascript:callPrompt('Average Diameter Unit', 'averageDiameterUnit');">
						<option value=""></option>
						<c:forEach var="unit" items="${dimensionUnits}">
							<c:choose>
								<c:when
									test="${unit eq nanomaterialEntityForm.map.entity.fullerene.averageDiameterUnit}">
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
					Number of Carbons
				</td>
				<td class="cellLabel">
					<input type="text" name="entity.fullerene.numberOfCarbon"
						id="numberOfCarbon" onkeydown="return filterInteger(event)"
						value="${nanomaterialEntityForm.map.entity.fullerene.numberOfCarbon}" />
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>