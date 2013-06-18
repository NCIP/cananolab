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

<table class="summaryViewNoGrid" align="left">
	<tr>
		<th scope="col" class="cellLabel">
			Type
		</th>
		<c:if test="${entity.withImagingFunction eq 'true'}">
			<th scope="col" class="cellLabel">
				Image Modality
			</th>
		</c:if>
		<c:if test="${entity.withTargetingFunction eq 'true'}">
			<th scope="row" class="cellLabel" width="30%">
				Targets
			</th>
		</c:if>
		<th scope="row" class="cellLabel">
			Description
		</th>
		<th>
		</th>
	</tr>
	<c:forEach var="function" items="${entity.functions}">
		<tr>
			<td>
				<c:out value="${function.type}" />
			</td>
			<c:if test="${entity.withImagingFunction eq 'true'}">
				<td>
					<c:if test="${! empty function.imagingFunction.modality }">
						<c:out value="${function.imagingFunction.modality}" />
					</c:if>
				</td>
			</c:if>
			<c:if test="${entity.withTargetingFunction eq 'true'}">
				<td>
					<c:if test="${! empty function.targetDisplayNames}">
						<c:forEach var="targetDisplayName"
							items="${function.targetDisplayNames}">
							<c:out value="${targetDisplayName}" />
							<br>
							<br>
						</c:forEach>
					</c:if>
				</td>
			</c:if>
			<td>
				<c:if test="${! empty function.description}">
					<c:out value="${function.descriptionDisplayName}" escapeXml="false" />
				</c:if>
			</td>
			<c:if test="${edit eq 'true'}">
				<td align="right">
					<a href="javascript:setTheFunction(${function.domainFunction.id});">Edit</a>&nbsp;
				</td>
			</c:if>
		</tr>
	</c:forEach>
</table>
