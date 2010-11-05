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
					Average Diameter
				</td>
				<td class="cellLabel">
					Number of Carbons
				</td>
			</tr>
			<tr>
				<td>
					<c:out value="${nanomaterialEntity.fullerene.averageDiameter}"/>
					<c:out value="${nanomaterialEntity.fullerene.averageDiameterUnit}"/>
				</td>
				<td>
					<c:out value="${nanomaterialEntity.fullerene.numberOfCarbon}"/>
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
					<input type="text" name="nanomaterialEntity.fullerene.averageDiameter"
						id="averageDiameter" 
						value="${compositionForm.map.nanomaterialEntity.fullerene.averageDiameter}" />
						<%--onkeydown="return filterFloatNumber(event)"--%>
				</td>
				<td class="cellLabel">
					Average Diameter Unit
				</td>
				<td class="cellLabel">
					<div id="averageDiameterUnitPrompt">
						<select name="nanomaterialEntity.fullerene.averageDiameterUnit"
							id="averageDiameterUnit"
							onchange="javascript:callPrompt('Average Diameter Unit', 'averageDiameterUnit', 'averageDiameterUnitPrompt');">
							<option value=""></option>
							<c:forEach var="unit" items="${dimensionUnits}">
								<c:choose>
									<c:when
										test="${unit eq compositionForm.map.nanomaterialEntity.fullerene.averageDiameterUnit}">
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
					Number of Carbons
				</td>
				<td class="cellLabel">
					<input type="text" name="nanomaterialEntity.fullerene.numberOfCarbon"
						id="numberOfCarbon" onkeydown="return filterInteger(event)"
						value="${compositionForm.map.nanomaterialEntity.fullerene.numberOfCarbon}" />
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>